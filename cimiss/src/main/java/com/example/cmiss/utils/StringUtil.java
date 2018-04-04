package com.example.cmiss.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	/**
	 * 检查字符串的值
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public String checkValue(Object oldValue, String newValue){
		String str = String.valueOf(oldValue);
		if(str!=null&&str!="null"&&str.length()>0){
			return str;
		}else{
			return newValue;
		}
	}
	
	/**
	 * 是否是数字(整型和浮点型都包括)
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		   Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]*$");
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
}
