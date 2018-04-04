package com.example.cmiss.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil
{
	public final static String DATEFORMAT_YMD = "yyyyMMdd";
	public final static String DATEFORMART_YMDH = "yyyyMMddHH";
	public final static String DATEFORMART_HM = "yyyyMMddHHmm";
	public final static String DATEFORMART_HMS = "yyyyMMddHHmmss";
	
	/**
	 * 本系统默认的字符串都统一采用此时间格式 
	 */
	public final static String WEB_FORMATOR_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	public final static String WEB_FORMATOR_YMD = "yyyy-MM-dd";
	
	public final static String WEB_FORMATOR_YMDH = "yyyy-MM-dd HH";
	
	public final static String WEB_FORMATOR_YMDHM = "yyyy-MM-dd HH:mm";
	
	public static DateFormat FORMAT_TIME = null;


	/**
	 * 根据格式转换
	 * 
	 * @param date_Str
	 *            时间串
	 * @param formate
	 *            时间格式
	 * @return date对象
	 */
	public static Date str2DateByFormat(String date_Str, String formate) {
		Date date = null;
		try {
			DateFormat FORMAT_TIME = new SimpleDateFormat(formate);
			date = FORMAT_TIME.parse(date_Str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date str2DateBylength(String date_Str){
		String dateStr = date_Str;
		int len = dateStr.length();
		try {
			if (len >= 14 ) {
				dateStr = dateStr.substring(0, 14);
				FORMAT_TIME = new SimpleDateFormat(DATEFORMART_HMS);
			}else if(len >= 12) {
				dateStr = dateStr.substring(0, 12);
				FORMAT_TIME = new SimpleDateFormat(DATEFORMART_HM);
			}else if (len >= 10) {
				dateStr = dateStr.substring(0, 10);
				FORMAT_TIME = new SimpleDateFormat(DATEFORMART_YMDH);
			}else if (len >= 8) {
				dateStr = dateStr.substring(0, 8);
				FORMAT_TIME = new SimpleDateFormat(DATEFORMAT_YMD);
			}else {
				return new Date();
			}
			return FORMAT_TIME.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}
		
	}
	/**  
     * 对日期(时间)中的日进行加减计算. <br>  
     * 例子: <br>  
     * 如果Date类型的d为 2005年8月20日,那么 <br>  
     * calculateByDate(d,-10)的值为2005年8月10日 <br>  
     * 而calculateByDate(d,+10)的值为2005年8月30日 <br>  
     *   
     * @param d  
     *            日期(时间).  
     * @param amount  
     *            加减计算的幅度.+n=加n天;-n=减n天.  
     * @return 计算后的日期(时间).  
     */ 
	public static Date calculateByYear(Date d, int amount) {   
        return calculate(d, GregorianCalendar.YEAR, amount);   
    }
	
	public static Date calculateByMonth(Date d, int amount) {   
        return calculate(d, GregorianCalendar.MONTH, amount);   
    }
	
    public static Date calculateByDate(Date d, int amount) {   
        return calculate(d, GregorianCalendar.DATE, amount);   
    } 
    
    public static Date calculateByHour(Date d, int amount) {   
        return calculate(d, GregorianCalendar.HOUR, amount);   
    }
    
    public static Date calculateByMinute(Date d, int amount) {   
        return calculate(d, GregorianCalendar.MINUTE, amount);   
    }   
       
   
    public static String calculateByMinute(String d ,int amount){
    	Date date = str2DateBylength(d);
    	date = calculate(date, GregorianCalendar.MINUTE, amount);  
    	return date2String(DATEFORMART_HMS,date);
    }
    /**  
     * 对日期(时间)中由field参数指定的日期成员进行加减计算. <br>  
     * 例子: <br>  
     * 如果Date类型的d为 2005年8月20日,那么 <br>  
     * calculate(d,GregorianCalendar.YEAR,-10)的值为1995年8月20日 <br>  
     * 而calculate(d,GregorianCalendar.YEAR,+10)的值为2015年8月20日 <br>  
     *   
     * @param d  
     *            日期(时间).  
     * @param field  
     *            日期成员. <br>  
     *            日期成员主要有: <br>  
     *            年:GregorianCalendar.YEAR <br>  
     *            月:GregorianCalendar.MONTH <br>  
     *            日:GregorianCalendar.DATE <br>  
     *            时:GregorianCalendar.HOUR <br>  
     *            分:GregorianCalendar.MINUTE <br>  
     *            秒:GregorianCalendar.SECOND <br>  
     *            毫秒:GregorianCalendar.MILLISECOND <br>  
     * @param amount  
     *            加减计算的幅度.+n=加n个由参数field指定的日期成员值;-n=减n个由参数field代表的日期成员值.  
     * @return 计算后的日期(时间).  
     */  
    public static Date calculate(Date d, int field, int amount) {   
        if (d == null)   
            return null;   
        GregorianCalendar g = new GregorianCalendar();   
        //g.setGregorianChange(d);
        g.setTime(d);
        g.add(field, amount);   
        return g.getTime();   
    }   
    
    public static Date dateTimeString2Date(String date_str,String date_format,Locale locale) {
    	try {
    		return  new SimpleDateFormat(date_format, locale).parse(date_str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    /**  
     * 日期(时间)转化为字符串.  
     *   
     * @param formater  
     *            日期或时间的格式.  
     * @param aDate  
     *            java.util.Date类的实例.  
     * @return 日期转化后的字符串.  
     */  
    public static String date2String(String formater, Date aDate) {   
        if (formater == null || "".equals(formater))   
            return null;   
        if (aDate == null)   
            return null;   
        return (new SimpleDateFormat(formater)).format(aDate);   
    }   
  
    /**  
     * 当前日期(时间)转化为字符串.  
     *   
     * @param formater  
     *            日期或时间的格式.  
     * @return 日期转化后的字符串.  
     */  
    public static String date2String(String formater) {   
        return date2String(formater, new Date());   
    }   
       
    /**  
     * 获取当前日期对应的星期数.  
     * <br>1=星期天,2=星期一,3=星期二,4=星期三,5=星期四,6=星期五,7=星期六  
     * @return 当前日期对应的星期数  
     */  
    public static int dayOfWeek() {   
        GregorianCalendar g = new GregorianCalendar();   
        int ret = g.get(java.util.Calendar.DAY_OF_WEEK);   
        g = null;   
        return ret;   
    }   
  
  
    /**  
     * 获取所有的时区编号. <br>  
     * 排序规则:按照ASCII字符的正序进行排序. <br>  
     * 排序时候忽略字符大小写.  
     *   
     * @return 所有的时区编号(时区编号已经按照字符[忽略大小写]排序).  
     */  
    public static String[] fecthAllTimeZoneIds() {   
        Vector<String> v = new Vector<String>();   
        String[] ids = TimeZone.getAvailableIDs();   
        for (int i = 0; i < ids.length; i++) {   
            v.add(ids[i]);   
        }   
        java.util.Collections.sort(v, String.CASE_INSENSITIVE_ORDER);   
        v.copyInto(ids);   
        v = null;   
        return ids;   
    }   
  
    /**  
     * 测试的main方法.  
     *   
     * @param argc  
     */  
    public static void main(String[] argc) {   
    	
    	SimpleDateFormat format = new SimpleDateFormat("MMMM dd yyyy",Locale.ENGLISH);
    	try {
			Date date = format.parse("January 23 2014");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//String timeString = "2010-10-10 12:00:00";
    	//Date dd = dateTimeString2Date(timeString);
    	//logger.debug(dd.toLocaleString());
    	
    	//String srcDateTime = "2010-11-30 16:30:10";	  
  	   	 //String dstDateTime = stringTimezone("yyyy-MM-dd HH:mm:ss:SSS",new Date().toLocaleString(), "yyyyMMdd HH:mm:ss");
         //logger.debug(dstDateTime);

    	//String[] ids = fecthAllTimeZoneIds();   
        //String nowDateTime =date2String("yyyy-MM-dd HH:mm:ss");   
        //logger.debug("The time Asia/Shanhai is " + nowDateTime);//程序本地运行所在时区为[Asia/Shanhai]   
        //显示世界每个时区当前的实际时间   
        /*for(int i=0;i <ids.length;i++){   
            logger.debug(" * " + ids[i] + "=" + string2TimezoneDefault(nowDateTime,ids[i]));    
        }   
        //显示程序运行所在地的时区   
        logger.debug("TimeZone.getDefault().getID()=" +TimeZone.getDefault().getID());   */
    }   
    /**
     * 将世界时的日期时间字符串根据转换为北京时的日期时间.
     * 
     * @param srcFormater
     *            待转化的日期时间的格式.
     * @param srcDateTime
     *            待转化的日期时间.    
     * @param dstFormater
     *            目标的日期时间的格式.    
     * 
     * @return dstDateTime 转化后的日期时间.
     */
    public static String stringTimezone(String srcFormater, String srcDateTime, String dstFormater) {
        if (srcFormater == null || "".equals(srcFormater))
            return null;
        if (srcDateTime == null || "".equals(srcDateTime))
            return null;        
        if (dstFormater == null || "".equals(dstFormater))
            return null; 
        String dstDateTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
        try {
            int diffTime = TimeZone.getTimeZone("UTC").getRawOffset() - TimeZone.getDefault().getRawOffset();
            Date d = sdf.parse(srcDateTime);
            long nowTime = d.getTime();
            long newNowTime = nowTime - diffTime;
            d = new Date(newNowTime);
            dstDateTime = (new SimpleDateFormat(dstFormater)).format(d);            
        } catch (ParseException e) {           
            
        } 
        return dstDateTime;
    }

    /**  
     * 将日期时间字符串根据转换为指定时区的日期时间.  
     *   
     * @param srcFormater  
     *            待转化的日期时间的格式.  
     * @param srcDateTime  
     *            待转化的日期时间.  
     * @param dstFormater  
     *            目标的日期时间的格式.  
     * @param dstTimeZoneId  
     *            目标的时区编号.  
     *   
     * @return 转化后的日期时间.  
     */  
    public static String string2Timezone(String srcFormater,   
            String srcDateTime, String dstFormater, String dstTimeZoneId) {   
        if (srcFormater == null || "".equals(srcFormater))   
            return null;   
        if (srcDateTime == null || "".equals(srcDateTime))   
            return null;   
        if (dstFormater == null || "".equals(dstFormater))   
            return null;   
        if (dstTimeZoneId == null || "".equals(dstTimeZoneId))   
            return null;   
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);   
        try {   
            int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);   
            Date d = sdf.parse(srcDateTime);   
            long nowTime = d.getTime();   
            long newNowTime = nowTime - diffTime;   
            d = new Date(newNowTime);   
            return date2String(dstFormater, d);   
        } catch (ParseException e) {   
            //Log.output(e.toString(), Log.STD_ERR);
            return null;   
        } finally {   
            sdf = null;   
        }   
    }   
  
    /**  
     * 获取系统当前默认时区与UTC的时间差.(单位:毫秒)  
     *   
     * @return 系统当前默认时区与UTC的时间差.(单位:毫秒)  
     */  
    /*private static int getDefaultTimeZoneRawOffset() {   
        return TimeZone.getDefault().getRawOffset();   
    }  */ 
  
    /**  
     * 获取指定时区与UTC的时间差.(单位:毫秒)  
     *   
     * @param timeZoneId  
     *            时区Id  
     * @return 指定时区与UTC的时间差.(单位:毫秒)  
     */  
    /*private static int getTimeZoneRawOffset(String timeZoneId) {   
        return TimeZone.getTimeZone(timeZoneId).getRawOffset();   
    } */  
  
    /**  
     * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)  
     *   
     * @param timeZoneId  
     *            时区Id  
     * @return 系统当前默认时区与指定时区的时间差.(单位:毫秒)  
     */  
    private static int getDiffTimeZoneRawOffset(String timeZoneId) {   
        return TimeZone.getDefault().getRawOffset()   
                - TimeZone.getTimeZone(timeZoneId).getRawOffset();   
    }   
  
    /**  
     * 将日期时间字符串根据转换为指定时区的日期时间.  
     *   
     * @param srcDateTime  
     *            待转化的日期时间.  
     * @param dstTimeZoneId  
     *            目标的时区编号.  
     *   
     * @return 转化后的日期时间.  
     * @see #string2Timezone(String, String, String, String)  
     */  
    public static String string2TimezoneDefault(String srcDateTime,   
            String dstTimeZoneId) {   
        return string2Timezone("yyyy-MM-dd HH:mm:ss", srcDateTime,   
                "yyyy-MM-dd HH:mm:ss", dstTimeZoneId);   
    }
    
    /**
	 * 判断时间strStartDate是否在时间strEndDate之前
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 */
	public static boolean isDateBefore(String strStartDate, String strEndDate) {

		try {
			DateFormat df = SimpleDateFormat.getDateTimeInstance();
			return df.parse(strStartDate).before(df.parse(strEndDate));

		} catch (ParseException e) {
			System.out.print("[SYS] " + e.getMessage());
			return false;
		}
	}

	public static String format(String format, Date date) {

		try {
			SimpleDateFormat df = new SimpleDateFormat(format);
			return df.format(date);

		} catch (Exception e) {
			System.out.print("[SYS] " + e.getMessage());
			return "";
		}
	}
	
	public static String getDateCharString(String time,String type){
		String timeStr = "";
		
		return timeStr;
	}
	
	public static String getImgTitleTimeStr(String type,String time){
		StringBuffer sb = new StringBuffer();
		String[] tmpArr = time.split("-");
		if(type.equals("hou")){
			sb.append(tmpArr[0]).append("年");
			sb.append(tmpArr[1]).append("月");
			sb.append(tmpArr[2]).append("日");
			sb.append(tmpArr[3]).append("时");
		}else if(type.equals("day")){
			sb.append(tmpArr[0]).append("年");
			sb.append(tmpArr[1]).append("月");
			sb.append(tmpArr[2]).append("日");
		}
		return sb.toString();
	}
}
