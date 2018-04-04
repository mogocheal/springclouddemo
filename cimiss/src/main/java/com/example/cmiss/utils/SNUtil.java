package com.example.cmiss.utils;

import java.util.Date;
import java.util.UUID;

public class SNUtil {

	private static Date date = new Date();
	private static StringBuilder buf = new StringBuilder();
	private static int seq = 0;
	private static final int ROTATION = 99999;

	public static String uuid(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	} 
	
	public static synchronized String next(){
	    if (seq > ROTATION) seq = 0;
	    buf.delete(0, buf.length());
	    date.setTime(System.currentTimeMillis());
	    String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$02d", date, seq++);
	    return str;
	}
	
	public static void main(String args[]){
	}
}
