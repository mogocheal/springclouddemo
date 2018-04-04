package com.example.cmiss.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * FTP文件传输方式
 * 
 * @author YangHaiBin 2013-5-3
 */
public class FtpUtil {

	public Logger logger = LoggerFactory.getLogger("ftp");

	private FTPClient ftpClient = new FTPClient();

	private String encoding = "GBK";// System.getProperty("file.encoding");

	/**
	 * 关闭
	 */
	public void disconnect() {
		logger.debug("断开连接..");
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException ioe) {
			}
		}
	}

	public boolean connect(String url, int port, String username, String password) throws Exception {
		ftpClient.connect(url.trim(), port);
		ftpClient.login(username, password);
		ftpClient.setControlEncoding(encoding);
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.enterLocalPassiveMode();
		// 检验是否连接成功
		int reply = ftpClient.getReplyCode();
        return FTPReply.isPositiveCompletion(reply);
    }

	/**
	 * 上传文件或文件夹
	 * 
	 * @param file
	 *            上传的文件或文件夹
	 * @throws Exception
	 */
	public void upload(File file) throws Exception {
		if (file.isDirectory()) {
			ftpClient.makeDirectory(file.getName());
			ftpClient.changeWorkingDirectory(file.getName());
			String[] files = file.list();
			for (int i = 0; i < files.length; i++) {
				File file1 = new File(file.getPath() + "\\" + files[i]);
				if (file1.isDirectory()) {
					upload(file1);
					ftpClient.changeToParentDirectory();
				} else {
					File file2 = new File(file.getPath() + "\\" + files[i]);
					FileInputStream input = new FileInputStream(file2);
					ftpClient.storeFile(file2.getName(), input);
					input.close();
				}
			}
		} else {
			File file2 = new File(file.getPath());
			FileInputStream input = new FileInputStream(file2);
			// FTP协议里面，规定文件名编码为iso-8859-1
			// 当前系统使用的Oracle的编码格式为gbk
			ftpClient.storeFile(new String(file2.getName().getBytes("gbk"),"iso-8859-1"), input);
			input.close();
		}
	}

	/**
	 * 下载单个文件
	 * 
	 * @param remotePath
	 * @param filename
	 * @param localFile
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public File downSingleFile(String remotePath, String filename, File localFile) throws Exception {
		//切换至根目录先,否则会在上一次的remotePath下后面添加路径
		ftpClient.changeWorkingDirectory("/");
		// 转移到FTP服务器目录至指定的目录下
		boolean result = ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), "iso-8859-1"));
		logger.debug("跳到文件夹" + remotePath + " :" + result);
		// 获取文件列表

		FTPFile[] fs = ftpClient.listFiles(new String(filename.getBytes(encoding), "iso-8859-1"));
		for (FTPFile ff : fs) {
			if (!localFile.getParentFile().exists()) {
				localFile.getParentFile().mkdirs();
			}
			if (localFile.exists()) {
				localFile.delete();
			}
			localFile.createNewFile();
			OutputStream os = new FileOutputStream(localFile);
			logger.debug("开始拷贝：" + filename);
			result = ftpClient.retrieveFile(new String(ff.getName().getBytes(encoding), "iso-8859-1"), os);
			logger.debug("拷贝结果:" + localFile.getAbsolutePath() + "#####" + result);
			os.close();
			if (result) {
				return localFile;
			}
		}
		return null;
	}

	/**
	 * \brief 下载多个文件
	 * 
	 * @param remotePath
	 * @param filename
	 * @param request
	 * @param response
	 * @param downname
	 * @return
	 * @throws Exception
	 * @attention 方法的使用注意事项
	 * @date 2015年3月6日
	 * @note begin modify by 修改人 修改时间 修改内容摘要说明
	 */
	public void downMoreFile(String remotePath, String filename, HttpServletRequest request, HttpServletResponse response, String downname)
			throws Exception {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		ServletOutputStream os = response.getOutputStream();
		// 转移到FTP服务器目录至指定的目录下
		boolean result = ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), "iso-8859-1"));
		String[] filenames = filename.split(",");
		String[] rPath = remotePath.split(",");
		if (1 == filenames.length) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			result = ftpClient.retrieveFile(remotePath, os);
			os.close();
		} else {
			for (int i = 0; i < filenames.length; i++) {
				zos.putNextEntry(new ZipEntry(filenames[i]));
				result = ftpClient.retrieveFile(rPath[i], zos);
				zos.closeEntry();
			}
			zos.close();
			bos.close();

			response.setContentType("application/octet-stream");
			// 如果下载的个例数据的个数大于1，则包名为个例文件
			String downLoadName = new String((downname + ".zip").getBytes("gbk"), "iso-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + downLoadName);
			os.write(bos.toByteArray());
			os.flush();
			os.close();
		}
	}

	/**
	 * 下载后返回文件内容
	 * 
	 * @param pathname
	 * @param filter
	 * @return
	 */
	public List<String> readFile(String pathname, FTPFileFilter filter) {
		// 转移到FTP服务器目录至指定的目录下
		List<String> ret = new ArrayList<String>();
		try {
			ftpClient.changeWorkingDirectory(new String(pathname.getBytes(encoding), "iso-8859-1"));
			FTPFile[] fs = ftpClient.listFiles(new String(pathname.getBytes(encoding), "iso-8859-1"), filter);
			for (FTPFile ff : fs) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ftpClient.retrieveFile(new String(ff.getName().getBytes(encoding), "iso-8859-1"), baos);
				ret.add(new String(baos.toByteArray()));
				baos.close();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public String[] getFileList(String remotePath) {
		String[] dis = null;
		try {
			dis = ftpClient.listNames(remotePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dis;
	}
}
