package com.example.cmiss.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	public final static String FORMATOR_Y = "yyyy";
	public final static String FORMATOR_M = "MM";
	public final static String FORMATOR_D = "dd";
	public final static String FORMATOR_H = "HH";
	public final static String FORMATOR_YM = "yyyyMM";
	public final static String FORMATOR_YMD = "yyyyMMdd";
	public final static String FORMATOR_YMDH = "yyyyMMddHH";
	public final static String FORMATOR_YMDHM = "yyyyMMddHHmm";
	public final static String FORMATOR_YMDHMS = "yyyyMMddHHmmss";
	public final static String FORMATOR_YMDHMSMS = "yyyyMMddHHmmssSSS";
	public final static String WEB_FORMATOR_YM = "yyyy-MM";
	public final static String WEB_FORMATOR_YMD = "yyyy-MM-dd";
	public final static String WEB_FORMATOR_YMDH = "yyyy-MM-dd HH";
	public final static String WEB_FORMATOR_YMDHM = "yyyy-MM-dd HH:mm";
	public final static String WEB_FORMATOR_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public final static String ZN_FORMATOR_YMD = "yyyy年MM月dd日";
	public final static String ZN_FORMATOR_YMDH = "yyyy年MM月dd日 HH时";
	public final static String ZN_FORMATOR_MDH = "MM月dd日 HH时";
	public final static String ZN_FORMATOR_DH = "dd日 HH时";
	public final static String ZN_FORMATOR_YMDHMS = "yyyy年MM月dd日 HH:mm:ss";
	public final static String WEB_FORMATOR_YMDHMS_ZONE = "yyyy-MM-dd HH:mm:ssZZ";
	public static String now_ymdhms() {
		return TimeUtil.format(new Date(),TimeUtil.WEB_FORMATOR_YMDHMS);
	}

	/**
	 * 根据条件获取当前时间
	 * 
	 * @param formatStr
	 *            格式化字符串
	 * @return
	 */
	public static String currentTime(String formatStr) {
		String currentHour = "";
		DateTime dt = new DateTime();
		DateTimeFormatter dtf = DateTimeFormat.forPattern(formatStr);
		currentHour = dt.toString(dtf);
		return currentHour;
	}

	/**
	 * 不同格式时间字符串之间相互转换
	 * 
	 * @param dataStr
	 * @param oldfrt
	 * @param newfrt
	 * @return
	 */
	public static String changeStyle(String dataStr, String oldfrt,
			String newfrt) {
		DateTimeFormatter oldtf = DateTimeFormat.forPattern(oldfrt);
		DateTimeFormatter newtf = DateTimeFormat.forPattern(newfrt);
		DateTime day = oldtf.parseDateTime(dataStr);
		return day.toString(newtf);
	}

	/**
	 * 计算N小时前时间
	 * 
	 * @param dataStr
	 *            时间字符串 默认为yyyyMMdd HH:mm
	 * @return
	 */
	public static String preMonth(String dataStr, int months) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(TimeUtil.WEB_FORMATOR_YMDHM);
		DateTime month = dtf.parseDateTime(dataStr);
		month = month.minusMonths(months);
		return month.toString(dtf);
	}

	/**
	 计算当前时间前 @param个月
	 * @param months
	 * @return
     */
	public static String preMonth(int months) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(WEB_FORMATOR_YMDHM);
		DateTime month = dtf.parseDateTime(TimeUtil.format(new Date(),TimeUtil.WEB_FORMATOR_YMDHM));
		month = month.minusMonths(months);
		return month.toString(dtf);
	}

	/**
	 * 计算N天后日期
	 * 
	 * @param dataStr
	 *            日期字符串 默认为yyyyMMdd
	 * @param days
	 *            需要计算的天数
	 * @return
	 */
	public static String nextDay(String dataStr, int days) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(WEB_FORMATOR_YMD);
		DateTime day = dtf.parseDateTime(dataStr);
		day = day.plusDays(days);
		return day.toString(dtf);
	}

	/**
	 * 计算N天后日期
	 * 
	 * @param dataStr
	 *            日期字符串 默认为yyyyMMdd
	 * @param days
	 *            需要计算的天数
	 * @return
	 */
	public static String nextDay(String dataStr, int days, String formatter) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(formatter);
		DateTime day = dtf.parseDateTime(dataStr);
		day = day.plusDays(days);
		return day.toString(dtf);
	}

	/**
	 * 计算N天前日期
	 * 
	 * @param dataStr
	 *            日期字符串 默认为yyyyMMdd
	 * @param days
	 *            需要计算的天数
	 * @return
	 */
	public static String preDay(String dataStr, int days) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(FORMATOR_YMD);
		DateTime day = dtf.parseDateTime(dataStr);
		day = day.minusDays(days);
		return day.toString(dtf);
	}

	/**
	 * 计算N天前日期
	 * 
	 * @param dataStr
	 *            日期字符串 默认为yyyyMMdd
	 * @param days
	 *            需要计算的天数
	 * @return
	 */
	public static String preDay(String dataStr, int days, String formatter) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(formatter);
		DateTime day = dtf.parseDateTime(dataStr);
		day = day.minusDays(days);
		return day.toString(dtf);
	}

	/**
	 *从今天算起的days天之前
	 * @param days
     * @return
     */


	public static String preDay(int days) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(WEB_FORMATOR_YMD);
		DateTime day = dtf.parseDateTime(TimeUtil.format(new Date(),TimeUtil.WEB_FORMATOR_YMD));
		day = day.minusDays(days);
		return day.toString(dtf);
	}

	/**传入一个时间字符串
	 * 计算N小时前时间
	 *
	 * @param dataStr
	 *            时间字符串 默认为yyyy-MM-dd HH
	 * @return
	 */
	public static String preHour(String dataStr, int hours) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(WEB_FORMATOR_YMDH);
		DateTime hour = dtf.parseDateTime(dataStr);
		hour = hour.minusHours(hours);
		return hour.toString(dtf);
	}

	/**
	 * 计算N小时后时间
	 * 
	 * @param dataStr
	 *            时间字符串 默认为yyyyMMddHH
	 * @return
	 */
	public static String nextHour(String dataStr, int hours) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(FORMATOR_YMDH);
		DateTime hour = dtf.parseDateTime(dataStr);
		hour = hour.plusHours(hours);
		return hour.toString(dtf);
	}

	/**
	 * 计算N小时后时间
	 * 
	 * @param dataStr
	 *            时间字符串
	 * @return
	 */
	public static String nextHour(String dataStr, int hours, String fmt) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(fmt);
		DateTime hour = dtf.parseDateTime(dataStr);
		hour = hour.plusHours(hours);
		return hour.toString(dtf);
	}
	/**
	 * 计算N小时后时间
	 *
	 * @param dataStr
	 *            时间字符串
	 * @return
	 */
	public static String nextHour(String dataStr, int hours, String fromfmt,String tofmt) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(fromfmt);
		DateTimeFormatter todft = DateTimeFormat.forPattern(tofmt);
		DateTime hour = dtf.parseDateTime(dataStr);
		hour = hour.plusHours(hours);
		return hour.toString(todft);
	}

	/**
	 * 计算N秒钟前时间
	 * 
	 * @param t
	 * @param seconds
	 * @return 时间字符串 默认为yyyyMMddHHmmss
	 */
	public static String nextSecond(DateTime t, int seconds) {
		t = t.minusSeconds(seconds);
		return t.toString(DateTimeFormat.forPattern(FORMATOR_YMDHMS));
	}

	/**
	 * 北京时间转世界时间
	 * 
	 * @param dataStr
	 *            时间字符串 默认为 yyyyMMddHH
	 * @return
	 */
	public static String BJTimeToUTC(String dataStr, String formate) {
		DateTime utc = null;
		DateTimeFormatter dtf = null;
		try {
			dtf = DateTimeFormat.forPattern(formate).withZoneUTC();
			DateTime dt = dtf.parseDateTime(dataStr);
			utc = dt.minusHours(8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return utc.toString(dtf);
	}

	/**
	 * 北京时间转世界时间
	 * 
	 * @param dataStr
	 *            时间字符串 默认为 yyyyMMddHH
	 * @return
	 */
	public static String BJTimeToUTC(String dataStr) {
		DateTime utc = null;
		DateTimeFormatter dtf = null;
		try {
			dtf = DateTimeFormat.forPattern(FORMATOR_YMDH).withZoneUTC();
			DateTime dt = dtf.parseDateTime(dataStr);
			utc = dt.minusHours(8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return utc.toString(dtf);
	}

	/**
	 * 世界时间转北京时间
	 * 
	 * @param dataStr
	 *            时间字符串 默认为yyyyMMddHH
	 * @return
	 */
	public static String UTCToBJTime(String dataStr) {
		String end = dataStr.substring(10);
		dataStr = dataStr.substring(0, 10);
		DateTime utc = null;
		DateTimeFormatter dtf = null;
		try {
			dtf = DateTimeFormat.forPattern(FORMATOR_YMDH).withZoneUTC();
			DateTime dt = dtf.parseDateTime(dataStr);
			utc = dt.plusHours(8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return utc.toString(dtf) + end;
	}

	/**
	 * 获取国际时间
	 * 
	 * @param formatStr
	 * @return
	 */
	public static String getGMTime(String formatStr) {
		String time = "";
		DateTimeZone zone = DateTimeZone.forID("GMT");
		DateTime dt = new DateTime(zone);
		DateTimeFormatter dtf = DateTimeFormat.forPattern(formatStr);
		time = dt.toString(dtf);
		return time;
	}

	/**
	 * 获取北京时间
	 * 
	 * @param formatStr
	 * @return
	 */
	public static String getBJTime(String formatStr) {
		String time = "";
		DateTimeZone zone = DateTimeZone.forID("Asia/Shanghai");
		DateTime dt = new DateTime(zone);
		DateTimeFormatter dtf = DateTimeFormat.forPattern(formatStr);
		time = dt.toString(dtf);
		return time;
	}

	/**
	 * 日期格式转换
	 * 
	 * @param time
	 * @param fromfmt
	 * @param tofmt
	 * @return
	 */
	public static String format(String time, String fromfmt, String tofmt) {
		DateTimeFormatter fm = DateTimeFormat.forPattern(fromfmt);
		DateTimeFormatter to = DateTimeFormat.forPattern(tofmt);
		DateTime dt = fm.parseDateTime(time);
		return dt.toString(to);
	}

	public static String format(Date time, String tofmt) {
		if (time == null) {
			return "";
		}
		DateTime dt = new DateTime(time);
		DateTimeFormatter to = DateTimeFormat.forPattern(tofmt);
		return dt.toString(to);
	}

	/**
	 * 获取当前日期 yyyyMMDD
	 * 
	 * @return
	 */
	public static Date currentDay() {
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

	public static void main(String[] args) {
		String time = TimeUtil.getGMTime(TimeUtil.FORMATOR_YMD);
		time = TimeUtil.preDay(time, 2);
		System.out.println(TimeUtil.isAfter(time, time, TimeUtil.FORMATOR_YMD));
		// System.out.println("世界时间转北京时间："+UTCToBJTime("2014082200"));
		// System.out.println("北京时间转世界时间："+BJTimeToUTC("2014082200"));
		// System.out.println("计算前N天："+preDay("20140822", 1));
		// System.out.println("计算后N天："+nextDay("20140822", 1));
		// System.out.println("计算前N小时："+preHour("2014082214", 2));
		// System.out.println("计算后N小时："+nextHour("2014082214", 2));
		// System.out.println("获取当前分钟："+currentTime(FORMATOR_YMDH));
		// System.out.println("获取国际时间："+getGMTime(FORMATOR_YMDH));
		// System.out.println("获取北京时间："+getBJTime(FORMATOR_YMDH));
		// System.out.println(changeDay("2014082110", 1));
		// System.out.println(changeHour("2014082210",5));
		// DateTimeFormatter dft=
		// DateTimeFormat.forPattern(FORMATOR_YMDH).withZoneUTC();
		// int month = dt.getMonthOfYear();//8
		// String month2 = dt.monthOfYear().getAsShortText();//八月
		// String month3 = dt.monthOfYear().getAsString();//8
		// String month4 = dt.monthOfYear().getAsText();//八月
		// String month5 = dt.monthOfYear().getName();//monthOfYear
		// System.out.println(month + "|" + month2 + "|" + month3 + "|" + month4
		// + "|" + month5);
		//
		//
		// DateTime year2013 = dt.withYear(2013);
		// DateTime eighthourslater = dt.plusHours(8);
		// System.out.println(year2013.getYear()+"|"+year2013.getYearOfCentury()+"|"+year2013.getYearOfEra());
		// System.out.println(eighthourslater.hourOfDay().getAsShortText());
		// System.out.println(eighthourslater.hourOfDay().getAsText());

	}

	/**
	 * 比较时间大小
	 * 
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static boolean isAfter(String dt1, String dt2) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(FORMATOR_YMDH);
		DateTime dateTime1 = dtf.parseDateTime(dt1);
		DateTime dateTime2 = dtf.parseDateTime(dt2);
		return dateTime1.isAfter(dateTime2);
	}

	/**
	 * 比较时间大小
	 * 
	 * @param dt1
	 * @param dt2
	 * @param fmt
	 * @return
	 */
	public static boolean isAfterOrEqual(String dt1, String dt2, String fmt) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(fmt);
		DateTime dateTime1 = dtf.parseDateTime(dt1);
		DateTime dateTime2 = dtf.parseDateTime(dt2);
		return dateTime1.isAfter(dateTime2);
	}

	/**
	 * 比较时间大小
	 * 
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static boolean isAfterOrEqual(String dt1, String dt2) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(FORMATOR_YMDH);
		DateTime dateTime1 = dtf.parseDateTime(dt1);
		DateTime dateTime2 = dtf.parseDateTime(dt2);
		return dateTime1.isAfter(dateTime2) || dateTime1.isEqual(dateTime2);
	}

	/**
	 * 比较时间大小
	 * 
	 * @param dt1
	 * @param dt2
	 * @param fmt
	 * @return
	 */
	public static boolean isAfter(String dt1, String dt2, String fmt) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(fmt);
		DateTime dateTime1 = dtf.parseDateTime(dt1);
		DateTime dateTime2 = dtf.parseDateTime(dt2);
		return dateTime1.isAfter(dateTime2);
	}

	/**
	 * 比较时间大小
	 * 
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static boolean isEqual(String dt1, String dt2) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(FORMATOR_YMDH);
		DateTime dateTime1 = dtf.parseDateTime(dt1);
		DateTime dateTime2 = dtf.parseDateTime(dt2);
		return dateTime1.isEqual(dateTime2);
	}

	/**
	 * 比较时间大小
	 * 
	 * @param dt1
	 * @param dt2
	 * @param fmt
	 * @return
	 */
	public static boolean isEqual(String dt1, String dt2, String fmt) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern(fmt);
		DateTime dateTime1 = dtf.parseDateTime(dt1);
		DateTime dateTime2 = dtf.parseDateTime(dt2);
		return dateTime1.isEqual(dateTime2);
	}


    public static Date parseString(String str, String formtor) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formtor);
        return simpleDateFormat.parse(str);
    }

    //计算前后时间
	public static String ComputeByMount(String dataStr,String format,int field,int num) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat= new SimpleDateFormat(format);
		Date date = simpleDateFormat.parse(dataStr);
		calendar.setTime(date);
		calendar.add(field,num);
		date = calendar.getTime();
		return simpleDateFormat.format(date);
	}
}
