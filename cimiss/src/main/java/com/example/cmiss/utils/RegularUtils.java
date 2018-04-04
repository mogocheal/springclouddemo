package com.example.cmiss.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {
	
	
	/**
	 * 匹配成功 返回一个map map里是获取的组 key 是组的 index (0..n)
	 * 匹配失败 返回null 
	 * @param name
	 * @param reg
	 * @return
	 */
	public static Map<String,String> compile(String text,String reg){
		Map<String,String> ret = new HashMap<String,String>();
		Pattern p = Pattern.compile(reg);  
        Matcher m = p.matcher(text);  
        if (m.find()) {  
        	int c = m.groupCount();
        	for(int cc=0;cc<=c;cc++){
        		String val = m.group(cc); 
        		ret.put(String.valueOf(cc), val);
        	}
        }else{
        	return null;
        }
		return ret;
	}

	
	public static void main(String args[]){
		System.out.println(compile("长江流域2014年5月气温.png","长江流域(\\d{4})年(\\d{2}|\\d{1})月气温.png"));
	}
}
