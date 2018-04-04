package com.example.cmiss.utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtils {

	/**
	 * 合并文件 到 新文件
	 * @param files
	 * @param mFile
	 * @return
	 */
	public static File combineFiles(List<File> files, File mFile) {
		try {
			FileChannel mFileChannel = new FileOutputStream(mFile).getChannel();
			FileChannel inFileChannel;
			for (File file : files) {

				inFileChannel = new FileInputStream(file).getChannel();
				inFileChannel.transferTo(0, inFileChannel.size(), mFileChannel);

				inFileChannel.close();
			}
			mFileChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mFile;
	}
	
	/**
	 * 设置文件夹内文件的最后修改日期
	 * @param f
	 * @param d
	 */
	public static void changeLastModiyTime(File f , Date d){
		File[] files = f.listFiles();
		for(File fi : files){
			fi.setLastModified(d.getTime());
		}
	}
	
	
	
	
	public static boolean createFile(String destFileName) {  
        File file = new File(destFileName);  
        if(file.exists()) {  
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");  
            return false;  
        }  
        if (destFileName.endsWith(File.separator)) {  
            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");  
            return false;  
        }  
        //判断目标文件所在的目录是否存在  
        if(!file.getParentFile().exists()) {  
            //如果目标文件所在的目录不存在，则创建父目录  
            System.out.println("目标文件所在目录不存在，准备创建它！");  
            if(!file.getParentFile().mkdirs()) {  
                System.out.println("创建目标文件所在目录失败！");  
                return false;  
            }  
        }  
        //创建目标文件  
        try {  
            if (file.createNewFile()) {  
                System.out.println("创建单个文件" + destFileName + "成功！");  
                return true;  
            } else {  
                System.out.println("创建单个文件" + destFileName + "失败！");  
                return false;  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());  
            return false;  
        }  
    }  
     
     
    public static boolean createDir(String destDirName) {  
        File dir = new File(destDirName);  
        if (dir.exists()) {  
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");  
            return false;  
        }  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        //创建目录  
        if (dir.mkdirs()) {  
            System.out.println("创建目录" + destDirName + "成功！");  
            return true;  
        } else {  
            System.out.println("创建目录" + destDirName + "失败！");  
            return false;  
        }  
    }  
     
     
    public static String createTempFile(String prefix, String suffix, String dirName) {  
        File tempFile = null;  
        if (dirName == null) {  
            try{  
                //在默认文件夹下创建临时文件  
                tempFile = File.createTempFile(prefix, suffix);  
                //返回临时文件的路径  
                return tempFile.getCanonicalPath();  
            } catch (IOException e) {  
                e.printStackTrace();  
                System.out.println("创建临时文件失败！" + e.getMessage());  
                return null;  
            }  
        } else {  
            File dir = new File(dirName);  
            //如果临时文件所在目录不存在，首先创建  
            if (!dir.exists()) {  
                if (!FileUtils.createDir(dirName)) {  
                    System.out.println("创建临时文件失败，不能创建临时文件所在的目录！");  
                    return null;  
                }  
            }  
            try {  
                //在指定目录下创建临时文件  
                tempFile = File.createTempFile(prefix, suffix, dir);  
                return tempFile.getCanonicalPath();  
            } catch (IOException e) {  
                e.printStackTrace();  
                System.out.println("创建临时文件失败！" + e.getMessage());  
                return null;  
            }  
        }  
    }


    /**
     *
     * @param path
     *            文件路径
     * @param encode
     *            编码
     * @return
     * @throws IOException
     */
    public static List<String> ReaderFileByLine(String path, String encode)
            throws IOException {
        List<String> lines=new ArrayList<String>();
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    file), encode));
            String temp = "";
            temp = br.readLine();
            while (temp != null) {
                lines.add(temp);
                temp = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return lines;
    }

    /**
     * 根据指定编码读取文件
     *
     * @param path
     *            文件路径
     * @param encode
     *            编码
     * @return
     * @throws IOException
     */
    public static StringBuffer BufferReaderFile(String path, String encode)
            throws IOException {
        File file = new File(path);
        StringBuffer sb = new StringBuffer();
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), encode));
        String temp = "";
        temp = br.readLine();
        while (temp != null) {
            sb.append(temp + "\r\n");
            temp = br.readLine();
        }
        br.close();
        return sb;
    }



}
