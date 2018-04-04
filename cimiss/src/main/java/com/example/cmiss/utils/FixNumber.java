package com.example.cmiss.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class FixNumber {

	/**
	 * 保留 number 小数点后 size 位
	 * @param number
	 * @param size
	 * @return
	 */
	public static String fix(String number,int size){
		if(StringUtils.isEmpty(number)){
			return "";
		}
		BigDecimal bd = new BigDecimal(number).setScale(size, BigDecimal.ROUND_HALF_UP);
		if(bd.intValue() > 9999){
			return "9999";
		}
		return bd.toString();
	}
	
	/**
	 * 长度不够时自动补0
	 * @param number
	 * @param length
	 * @return
	 */
	public static String pad(String number,int length){
		if(number.length() > length){
			return number.substring(length);
		}else{
			int len = number.length();
			while(len < length) {
				number = "0" + number;
				len++;
			}
			return number;
		}
	}
	
	
	public static void main(String args[]){
		
	
	}
}
