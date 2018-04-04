package com.example.cmiss.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Formator {
	public final static String FORMATOR_Y = "yyyy";
	public final static String FORMATOR_YM = "yyyyMM";
	public final static String FORMATOR_YMD = "yyyyMMdd";
	public final static String FORMATOR_YMDH = "yyyyMMddHH";
	public final static String FORMATOR_YMDHM = "yyyyMMddHHmm";
	public final static String FORMATOR_YMDHMS = "yyyyMMddHHmmss";

	public static String formatDate(String daystr) {
		return daystr.substring(0, 4) + "年" + daystr.substring(4, 6) + "月"
				+ daystr.substring(6) + "日";
	}

	public static String formatDate2(String daystr) {
		return daystr.substring(0, 4) + "-" + daystr.substring(4, 6) + "-"
				+ daystr.substring(6);
	}

	public static String formatDateHour(String daystr) {
		return daystr.substring(0, 4) + "年" + daystr.substring(4, 6) + "月"
				+ daystr.substring(6, 8) + "日" + daystr.substring(8, 10) + "时";
	}

	public static String formatDateHour2(String daystr) {
		return daystr.substring(0, 4) + "-" + daystr.substring(4, 6) + "-"
				+ daystr.substring(6, 8) + " " + daystr.substring(8, 10) + "";
	}

	public static String formatDateMinute(String daystr) {
		return daystr.substring(0, 4) + "-" + daystr.substring(4, 6) + "-"
				+ daystr.substring(6, 8) + " " + daystr.substring(8, 10) + ":"
				+ daystr.substring(10, 12);
	}

	public static String formatTime(String daystr) {
		return daystr.substring(0, 2) + ":" + daystr.substring(2);
	}

	public static String currentTime() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String strdate = df.format(date);
		return strdate;
	}

	/**
	 * 当前时间的前8小时
	 * 
	 * @return
	 */
	public static String currentHourSub8() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, -8);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
		String strdate = df.format(cal.getTime());
		return strdate;
	}

	/**
	 * 当前时间
	 * 
	 * @return
	 */
	public static String currentHour() {
		return currentTime(Formator.FORMATOR_YMDH);
	}

	/**
	 * 当前时间
	 * 
	 * @return
	 */
	public static String currentTime(String formartStr) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(formartStr);
		String strdate = df.format(date);
		return strdate;
	}

	/**
	 * 获取当前时间的前一天
	 * 
	 * @return
	 */
	public static String currentDayPreDay(String formatStr) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
		String strdate = df.format(cal.getTime());
		return strdate;
	}

	/**
	 * 北京时转世界时
	 * 
	 * @param dataStr
	 *            时间字符串 默认格式yyyyMMddHH
	 * @return
	 */
	public static String BJTimeToUTC(String dataStr) {
		return getTimeStrByAmount(dataStr, Formator.FORMATOR_YMDH,
				Formator.FORMATOR_YMDH, -8, Calendar.HOUR_OF_DAY);
	}

	/**
	 * 世界时转北京时
	 * 
	 * @param dataStr
	 *            时间字符串 默认格式yyyyMMddHH
	 * @return
	 */
	public static String UTCToBJTime(String dataStr) {
		return getTimeStrByAmount(dataStr, Formator.FORMATOR_YMDH,
				Formator.FORMATOR_YMDH, 8, Calendar.HOUR_OF_DAY);
	}

	public static String UTCToBJTime2(Date dataStr) {
		return getTimeStrByAmount2(dataStr, Formator.FORMATOR_YMDH,
				Formator.FORMATOR_YMDH, 8, Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取一串时间字符串，根据给定时间字符串和字符串格式，以及差值,计算方式 给定时间字符串与格式字符串一致
	 * 例如技算给定时间字符串一天之后的时间字符串getTimeStrByAmount
	 * ("20131014","yyyyMMdd",1,Calendar.DAY_OF_MONTH)
	 * 
	 * @return
	 */
	public static String getTimeStrByAmount(String dateStr,
			String parseFormatStr, String returnFormatStr, int amount, int field) {
		String timeStr = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(parseFormatStr);
		Date day;
		try {
			day = df.parse(dateStr);
			cal.setTime(day);
			cal.add(field, amount);
			df = new SimpleDateFormat(returnFormatStr);
			timeStr = df.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeStr;
	}

	public static String getTimeStrByAmount2(Date dateStr,
			String parseFormatStr, String returnFormatStr, int amount, int field) {
		String timeStr = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(returnFormatStr);
		cal.setTime(dateStr);
		cal.add(field, amount);
		timeStr = df.format(cal.getTime());
		return timeStr;
	}

	public static String year() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		String strdate = df.format(date);
		return strdate;
	}

	public static String date() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String strdate = df.format(date);
		return strdate;
	}

	public static String dateJson() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		String strdate = df.format(date);
		strdate = "['" + strdate.substring(0, 4) + "','"
				+ strdate.substring(4, 6) + "','" + strdate.substring(6, 8)
				+ "','" + strdate.substring(8, 10) + "','"
				+ strdate.substring(10, 12) + "']";
		return strdate;
	}

	/**
	 * time yyyyMMddhhmm
	 * 
	 * @param time
	 * @return
	 */
	public static String sub24(String time) {
		Calendar cal = Calendar.getInstance();
		int y = Integer.parseInt(time.substring(0, 4));
		int m = Integer.parseInt(time.substring(4, 6));
		int d = Integer.parseInt(time.substring(6, 8));
		cal.set(y, m - 1, d);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(cal.getTime());
	}

	/**
	 * 当前到昨天同时刻的小时数据
	 * 
	 * @return
	 */
	public static String getJsonDatePastHour() {
		List<String> datelist = new ArrayList<String>();
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH时");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		for (int i = 0; i < 25; i++) {
			String strdate = df.format(date);
			datelist.add(strdate);
			cal.add(Calendar.HOUR_OF_DAY, -1);
			date = cal.getTime();
		}
		return JsonUtils.toJson(datelist);
	}

	/**
	 * 得到一小时内的分钟时间数据,10分钟间隔
	 * 
	 * @return
	 */
	public static String getJsonDatePastMinute() {
		List<String> datelist = new ArrayList<String>();
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH时mm分");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int mm = cal.get(Calendar.MINUTE);
		mm = mm / 10;
		cal.set(Calendar.MINUTE, mm * 10);
		date = cal.getTime();
		for (int i = 0; i < 7; i++) {
			String strdate = df.format(date);
			datelist.add(strdate);
			cal.add(Calendar.MINUTE, -10);
			date = cal.getTime();
		}
		return JsonUtils.toJson(datelist);
	}

	public static String getDateStr(Calendar cal) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(cal.getTime());
	}

	public static String getDateStr(Calendar cal, String fmt) {
		SimpleDateFormat df = new SimpleDateFormat(fmt);
		return df.format(cal.getTime());
	}

	public static String getDateStr(Date cal) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(cal.getTime());
	}

	public static void main(String args[]) {

	}

	/**
	 * 获取北京时间，suse上和wiendows上都可
	 * 
	 * @param fmt
	 * @return
	 */
	public static String getBJTime(String fmt) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(fmt);
		TimeZone timeZone = cal.getTimeZone();
		if ("GMT".equals(timeZone.getID())
				|| "Etc/GMT+0".equals(timeZone.getID())) { // suse上是国际时间 北京时间就+8
			cal.add(Calendar.HOUR_OF_DAY, 8);
		}
		return df.format(cal.getTime());
	}

	/**
	 * 获取国际时间，suse上和wiendows上都可
	 * 
	 * @param fmt
	 * @return
	 */
	public static String getGMTime(String fmt) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(fmt);
		TimeZone timeZone = cal.getTimeZone();
		if ((!("GMT".equals(timeZone.getID())))
				&& (!("Etc/GMT+0".equals(timeZone.getID())))) { // windows上是北京时间
																// 国际时间 -8
			cal.add(Calendar.HOUR_OF_DAY, -8);
		}
		return df.format(cal.getTime());
	}

	public static Date parse(String time, String parseFormatStr) {
		SimpleDateFormat df = new SimpleDateFormat(parseFormatStr);
		try {
			return df.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
