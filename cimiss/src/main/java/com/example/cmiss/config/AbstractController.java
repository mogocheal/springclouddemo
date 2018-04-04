package com.example.cmiss.config;

import com.example.cmiss.utils.JsonUtils;
import com.example.cmiss.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class AbstractController {

	public Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 打印text数据
	 * 
	 * @param text
	 * @param response
	 */
	public void printTextData(String text, HttpServletResponse response) {

		ServletUtils.setTextAjaxResponseHeader(response);

		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.print(text);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * object 转json
	 * 
	 * @param response
	 */
	public void printJsonObjectData(Object obj, HttpServletResponse response) {
		ServletUtils.setJsonAjaxResponseHeader(response);
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.print(JsonUtils.toJson(obj));
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出图片
	 * 
	 * @param f
	 * @param response
	 */
	public void printImg(File f, HttpServletResponse response) {
		ServletUtils.setImgResponseHeader(response);
		try {
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(new FileInputStream(f), out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 输出文件
	 * 
	 * @param f
	 * @param response
	 */
	public void printFile(File f, HttpServletResponse response) {
		ServletUtils.setFileDownloadHeader(response, f.getName());
		try {
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(new FileInputStream(f), out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 输出pdf
	 * @param f
	 * @param response
	 */
	public void printPdf(File f,HttpServletResponse response){
		ServletUtils.setPdfResponseHeader(response);
		try {
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(new FileInputStream(f), out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
