package com.example.cmiss.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @功能描述: 字符串操作帮助类
 * @开发人员: 黄浩
 * @创建日期: 2013-5-15 上午11:41:37
 */
public class MyStringUtils {

	/**
	 * 下划线 _
	 */
	public static final String SPAN = "_";
	/**
	 * 斜杠 \
	 */
	public static final String SLASH = "\\";
	/**
	 * 反斜杠 /
	 */
	public static final String BACKSLASH = "/";
	/**
	 * 点 .
	 */
	public static final String DOT = ".";
	/**
	 * 逗号 ,
	 */
	public static String COMMA = ",";
	/**
	 * 冒号 :
	 */
	public static String COLON = ":";
	/**
	 * @
	 */
	public static String ALT = "@";
	/**
	 * 时间格式 yyyy-MM-dd HH24:mm:ss for oracle
	 */
	public static final String TIMEFORMAT_DB = "YYYY-MM-DD HH24:MI:SS";
	/**
	 * 时间格式 yyyy-MM-dd HH:mm:ss
	 */
	public static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 时间格式 yyyyMMddHHmmss
	 */
	public static final String MINITIMEFORMAT = "yyyyMMddHHmmss";
	/**
	 * 时间格式 yyyyMMddHH24mmss for oracle
	 */
	public static final String MINITIMEFORMAT_DB = "yyyyMMddHH24miss";
	/**
	 * remote 返回结果集，默认分页每页数据集大小
	 */
	public static final int pageSize = 50;
	/**
	 * 空字符串 ""
	 */
	public static final String EMPTY = "";
	/**
	 * 空格 " "
	 */
	public static final String BLANK = " ";
	/**
	 * 双下划线 "__"
	 */
	public static final String DOUBLE_DASH_SEPERATOR = "__";
	public static final String TMPFILEEXT = "tmp";

	/**
	 * 判断的指定的变量是否为空
	 * 
	 * @param t
	 *            需要判断的字符串
	 * @return 如为 null 范围 “”，如不为null，范围实际内容
	 */
	public static String checkNull(String t) {
		if (t == null) {
			return "";
		} else {
			return t;
		}
	}

	/**
	 * 
	 * @功能描述: TODO(这里用一句话描述这个方法的作用)
	 * @输入参数: @param t
	 * @输入参数: @return
	 * @返回描述:
	 * @异常类型:
	 */
	public static String checkNullObj(Object t) {
		if (t == null) {
			return "";
		} else {
			return (String) t;
		}
	}

	private static DateFormat df = new SimpleDateFormat(TIMEFORMAT);

	/**
	 * 将时间类型变量转换为字符串，格式 yyyy-MM-dd HH24:mi:ss
	 * 
	 * @param d
	 *            要转换的日期
	 * @return 转换后的字符串 yyyy-MM-dd HH24:mi:ss 格式
	 */
	public static String time2str(Date d) {
		return df.format(d);
	}

	/**
	 * 按指定格式将时间类型变量转换为字符串
	 * 
	 * @param d
	 *            要转换的日期
	 * @param format
	 *            要转换的字符串格式
	 * @return 转换后的字符串
	 */
	public static String time2str(Date d, String format) {
		DateFormat dft = new SimpleDateFormat(format);
		return dft.format(d);
	}

	/**
	 * 将指定格式字符串转换为日期类型，默认输入字符串格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param s
	 *            要转换的字符串
	 * @return 转换后的日期变量
	 * @throws ParseException
	 *             格式不对抛出异常
	 */
	public static Date str2date(String s) throws ParseException {
		return df.parse(s);
	}

	/**
	 * 将指定格式字符串转换为日期类型
	 *
	 * @param s
	 *            要转换的字符串
	 * @param format
	 *            要转换的字符串格式
	 * @return 转换后的日期变量
	 * @throws ParseException
	 *             格式不对抛出异常
	 */
	public static Date str2date(String s, String format) throws ParseException {
		DateFormat dft = new SimpleDateFormat(format);
		return dft.parse(s);
	}

	/**
	 * 空属性 Properties() 对象
	 */
	public static final Properties EMPTY_PROPERTIES = new Properties();

	/**
	 * 把以空格分隔的key value pair组成的字符串转换为属性 如：fileName=a.txt taskId =123
	 * 
	 * @param args
	 * @return
	 */
	public static Properties parseArgs(String args) {
		if (args != null) {
			args = checkNull(args);
			return parseArgs(args.split(","));
		}
		return EMPTY_PROPERTIES;
	}

	/**
	 * 把key value pair组成的字符串数组转换为属性 如：fileName=a.txt
	 * 
	 * @param args
	 * @return
	 */
	public static Properties parseArgs(String[] args) {

		if (args == null || args.length < 1) {
			return EMPTY_PROPERTIES;
		}

		Properties p = new Properties();

		for (int i = 0; i < args.length; i++) {
			String[] strings = args[i].split("=");
			p.setProperty(strings[0].trim(), strings[1].trim());
		}
		return p;
	}

	static String localHostIP = null;

	public static String getLocalHostIPByNet() {
		if (localHostIP == null) {
			try {
				// 根据网卡取本机配置的IP
				Enumeration<?> e1 = NetworkInterface
						.getNetworkInterfaces();
				while (e1.hasMoreElements()) {
					NetworkInterface ni = (NetworkInterface) e1.nextElement();
					if (!ni.getName().equals("eth3")) {
						continue;
					} else {
						Enumeration<?> e2 = ni.getInetAddresses();
						while (e2.hasMoreElements()) {
							InetAddress ia = (InetAddress) e2.nextElement();
							if (ia instanceof Inet6Address) {
								continue;
							}
							localHostIP = ia.getHostAddress();
						}
						break;
					}
				}
			} catch (SocketException ex) {
				// Log.error("获取本机IP地址出错：" + ex.getMessage(), ex);
			}
		}
		return localHostIP;
	}

	public static String getLocalHostIP() {
		if (localHostIP == null) {
			try {
				Enumeration allNetInterfaces = NetworkInterface
						.getNetworkInterfaces();
				InetAddress ip = null;
				while (allNetInterfaces.hasMoreElements()) {
					NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
							.nextElement();
					System.out.println(netInterface.getName());
					Enumeration addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = (InetAddress) addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {
							localHostIP = ip.getHostAddress();
							String tt = netInterface.getName();
						}
					}
				}
			} catch (SocketException ex) {

				// Log.error("获取本机IP地址出错：" + ex.getMessage(), ex);
			}
		}
		return localHostIP;
	}

	private static Properties configInfo = null;

	/**
	 * 将指定的文件名格式转换为正则表达式
	 * 
	 * @param str
	 * @return
	 */
	public static String convert2regEx(String str) {
		String regExp = str;
		regExp = regExp.replace('.', '#');
		regExp = regExp.replaceAll("#", "\\\\.");
		regExp = regExp.replace('*', '#');
		regExp = regExp.replaceAll("#", ".*");
		regExp = regExp.replace('?', '#');
		regExp = regExp.replaceAll("#", ".?");
		regExp = "^" + regExp + "$";
		return regExp;
	}

	/**
	 * 日期偏移后返回字条字符串
	 * 
	 * @param date
	 * @param format
	 * @param addHour
	 * @return
	 */
	public static String GetDateString(Date date, String format, int addHour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, addHour);
		DateFormat df = new SimpleDateFormat(format);// ("yyyyMMddHHmm");
		return df.format(cal.getTime());
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}
	public static boolean isEmptys(String ...args) {
		if(args == null){
			return true;
		}
		for(String s:args){
			if(isEmpty(s)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断字符是否为字母
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	/**
	 * 判断字符是否为数字
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	/**
	 * 计算字符串中给定字符出现的次数
	 * 
	 * @param str
	 * @param c
	 * @return
	 */
	public static int count(String str, char c) {
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 将字符串的首字母大写
	 * 
	 * @param s
	 * @return
	 */
	public static String upperCaseFirstChar(String s) {
		if (s == null || s.length() <= 0) {
			return s;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(s.substring(0, 1).toUpperCase());
		sb.append(s);
		sb.deleteCharAt(1);
		return sb.toString();
	}

	/**
	 * 将小写字母转化为大写字母
	 * 
	 * @param c
	 * @return
	 */
	public static char toUpperCase(char c) {
		if (c >= 'a' && c <= 'z') {
			return (char) (c - 32);
		}
		return c;
	}

	/**
	 * 将大写字母转化为小写字母
	 * 
	 * @param c
	 * @return
	 */
	public static char toLowerCase(char c) {
		if (c >= 'A' && c <= 'Z') {
			return (char) (c + 32);
		}
		return c;
	}

	/**
	 * 判断字符是否为空字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isBlank(char c) {
		return c == ' ' || c == '\t' || c == '\r' || c == '\n';
	}

	/**
	 * 将byte数组中指定的部分转化为字符串
	 * 
	 * @param buf
	 *            byte数组
	 * @param first
	 *            第一个byte的位置
	 * @param last
	 *            最后一个byte的位置
	 * @return
	 */
	public static String valueOf(byte[] buf, int first, int last) {
		int start = first;
		for (; start < last; start++) {
			if (buf[start] != 0) {
				break;
			}
		}

		int end = last - 1;
		for (; end >= start; end--) {
			if (buf[end] != 0) {
				break;
			}
		}

		if (end < start) {
			return null;
		}
		return new String(buf, start, end + 1);
	}

	/**
	 * 将byte数组转化为字符串
	 * 
	 * @param buf
	 *            byte数组
	 * @return
	 */
	public static String valueOf(byte[] buf) {
		return valueOf(buf, 0, buf.length);
	}

	/**
	 * 将BigDecimal转化为字符串
	 * 
	 * @param b
	 * @param scale
	 *            小数位数
	 * @return
	 */
	public static String valueOf(BigDecimal b, int scale) {
		int n = 0;
		String s = b.setScale(scale, RoundingMode.HALF_UP).toString();
		int dotidx = s.indexOf(".");
		if (dotidx == -1) {
			s = s + ".";
			n = scale;
			dotidx = s.length() - 1;
		} else {
			n = scale - (s.length() - dotidx - 1);
		}
		if (scale == 0) {
			return s.substring(0, dotidx);
		}

		return append(s, n, '0');
	}

	/**
	 * 计算字符串的长度
	 * 
	 * @param s
	 * @return
	 */
	public static int size(String s) {
		int size = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > 0x7F) {
				size += 2;
			} else {
				size += 1;
			}
		}
		return size;
	}

	/**
	 * 在字符串后追加空格，使其长度达到n
	 * 
	 * @param s
	 *            原始字符串
	 * @param n
	 *            结果字符串的长度
	 * @return
	 */
	public static String append(String s, int n) {
		return append(s, n, ' ');
	}

	/**
	 * 在字符串后追加字符c，使其长度达到n
	 * 
	 * @param s
	 *            原始字符串
	 * @param n
	 *            结果字符串的长度
	 * @return
	 */
	public static String append(String s, int n, char c) {
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < n; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 在字符串左边添加空格，使其长度达到n
	 * 
	 * @param s
	 *            原始字符串
	 * @param n
	 *            结果字符串的长度
	 * @return
	 */
	public static String lpad(String s, int n) {
		return lpad(s, n, ' ');
	}

	/**
	 * 在字符串右边添加空格，使其长度达到n
	 * 
	 * @param s
	 *            原始字符串
	 * @param n
	 *            结果字符串的长度
	 * @return
	 */
	public static String rpad(String s, int n) {
		return rpad(s, n, ' ');
	}

	/**
	 * 在字符串左边添加指定字符串，使其长度达到n
	 * 
	 * @param s
	 *            原始字符串
	 * @param n
	 *            结果字符串的长度
	 * @param padStr
	 *            需要添加的字符串
	 * @return
	 */
	public static String lpad(String s, int n, String padStr) {
		StringBuilder sb = new StringBuilder();
		int pad = n - s.length();

		if (padStr.length() == 0) {
			padStr = " ";
		}
		int times = pad / padStr.length();
		int remain = pad % padStr.length();
		for (int i = 0; i < times; i++) {
			sb.append(padStr);
		}

		if (remain != 0) {
			sb.append(padStr.substring(0, remain));
		}
		sb.append(s);

		return sb.toString();
	}

	/**
	 * 在字符串右边添加指定字符串，使其长度达到n
	 * 
	 * @param s
	 *            原始字符串
	 * @param n
	 *            结果字符串的长度
	 * @param padStr
	 *            需要添加的字符串
	 * @return
	 */
	public static String rpad(String s, int n, String padStr) {
		StringBuilder sb = new StringBuilder(s);

		if (padStr.length() == 0) {
			padStr = " ";
		}
		int pad = n - s.length();
		int times = pad / padStr.length();
		int remain = pad % padStr.length();
		for (int i = 0; i < times; i++) {
			sb.append(padStr);
		}

		if (remain != 0) {
			sb.append(padStr.substring(0, remain));
		}

		return sb.toString();
	}

	/**
	 * 在字符串左边添加指定字符，使其长度达到n
	 * 
	 * @param s
	 *            原始字符串
	 * @param n
	 *            结果字符串的长度
	 * @param c
	 *            需要添加的字符
	 * @return
	 */
	public static String lpad(String s, int n, char c) {
		StringBuilder sb = new StringBuilder();
		int size = size(s);
		int pad = n - size;
		for (int i = 0; i < pad; i++) {
			sb.append(c);
		}
		sb.append(s);
		return sb.toString();
	}

	/**
	 * 在字符串右边添加指定字符，使其长度达到n
	 * 
	 * @param s
	 *            原始字符串
	 * @param n
	 *            结果字符串的长度
	 * @param c
	 *            需要添加的字符
	 * @return
	 */
	public static String rpad(String s, int n, char c) {
		StringBuilder sb = new StringBuilder(s);
		int size = size(s);
		int pad = n - size;
		for (int i = 0; i < pad; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 将整形数转化为16进制字符串
	 * 
	 * @param n
	 * @return
	 */
	public static String byte2Hex(int n) {
		final char[] HS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 2; i++) {
			int h = (n >> i * 4) & 0xf;
			sb.append(HS[h]);
		}
		return sb.toString();
	}

	/**
	 * 从字符串中删除指定的字符
	 * 
	 * @param s
	 *            原始字符串
	 * @param c
	 *            需要删除的字符
	 * @return
	 */
	public static String filter(String s, char c) {
		return filter(s, new char[] { c });
	}

	/**
	 * 从字符串中删除所有指定的字符
	 * 
	 * @param s
	 *            原始字符串
	 * @param filter
	 *            需要删除的所有字符
	 * @return
	 */
	public static String filter(String s, String filter) {
		char[] ca = new char[filter.length()];
		for (int i = 0; i < filter.length(); i++) {
			ca[i] = filter.charAt(i);
		}
		return filter(s, ca);
	}

	/**
	 * 从字符串中删除所有指定的字符
	 * 
	 * @param s
	 *            原始字符串
	 * @param ca
	 *            需要删除的所有字符数组
	 * @return
	 */
	public static String filter(String s, char[] ca) {
		StringBuilder sb = new StringBuilder();
		for (int start = 0; start < s.length(); start++) {
			char ch = s.charAt(start);
			if (!contains(ca, ch)) {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符串首尾两端的字符c删除
	 * 
	 * @param s
	 *            原始字符串
	 * @param c
	 *            指定字符
	 * @return
	 */
	public static String trim(String s, char c) {
		return trim(s, new char[] { c });
	}

	/**
	 * 将字符串首尾两端的空字符删除
	 * 
	 * @param s
	 *            原始字符串
	 * @return
	 */
	public static String trim(String s) {
		return trim(s, new char[] { ' ', '\t', '\r', '\n' });
	}

	/**
	 * 将字符串首尾两端的所有字符删除
	 * 
	 * @param s
	 *            原始字符串
	 * @param ca
	 *            指定字符数组
	 * @return
	 */
	public static String trim(String s, char[] ca) {
		int start = 0;
		for (; start < s.length(); start++) {
			if (!contains(ca, s.charAt(start))) {
				break;
			}
		}

		int end = s.length() - 1;
		for (; end >= start; end--) {
			if (!contains(ca, s.charAt(end))) {
				break;
			}
		}

		if (end < start) {
			return "";
		}
		return s.substring(start, end + 1);
	}

	public static String[] split(String s, String regex) {
		String[] sa = s.split(regex);
		ArrayList<String> list = new ArrayList<String>(sa.length);

		for (int i = 0; i < sa.length; i++) {
			if (sa[i] == null || sa[i].length() == 0) {
				continue;
			}
			list.add(sa[i]);
		}
		return list.toArray(new String[0]);
	}

	/**
	 * 判断字符数组中是否包含给定字符
	 * 
	 * @param ca
	 *            字符数组
	 * @param c
	 *            给定字符
	 * @return
	 */
	private static boolean contains(char[] ca, char c) {
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] == c) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将字符串转化为正则表达式
	 * 
	 * @param s
	 * @return
	 */
	public static String toRegex(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (MyStringUtils.isLetter(c)) {
				sb.append("[");
				sb.append(MyStringUtils.toLowerCase(c));
				sb.append(MyStringUtils.toUpperCase(c));
				sb.append("]");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/** 
	 * @Method collect2String 
	 * @Description String集合转String，以regex分隔
	 * @param col
	 * @param regex
	 * @return
	 */
	public static String collect2String(Collection<String> col, String regex) {
		Iterator it = col.iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			String str = (String) it.next();
			sb.append(regex);
			sb.append(str);
		}
		return sb.toString().substring(1);
	}
	
	/** 
	 * @Method collect2String 
	 * @Description String集合转String，以regex分隔
	 * @param col
	 * @param regex
	 * @return
	 */
	public static String sqlCollect2String(Collection<String> col) {
		Iterator it = col.iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			String str = (String) it.next();
			sb.append(",");
			sb.append("'"+str+"'");
		}
		return sb.toString().substring(1);
	}
	
	/** 
	 * @Method collect2String 
	 * @Description String集合转String，以regex分隔
	 * @param col
	 * @param regex
	 * @return
	 */
	public static String getSqlWhereFiled(String col, String value) {
		if(value!=null && !value.trim().isEmpty()){
			return " and "+col+"='"+value+"' ";
		}
		return "";
	}
	
	
	public static String toUtf8(String src) {
		String out = "";
		try {
			out = URLDecoder.decode(src,"utf-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return out;
	}

	/**
	* JAVA判断字符串数组中是否包含某字符串元素
	*
	* @param substring 某字符串
	* @param source 源字符串数组
	* @return 包含则返回true，否则返回false
	*/
	public static boolean isInList(String substring, String[] source) {
		if (source == null || source.length == 0 || substring == null) {
			return false;
		}
		for (int i = 0; i < source.length; i++) {
			String aSource = source[i];
			if (aSource.equals(substring)) {
				return true;
			}
		}
		return false;
	}
	
	//判断参数种是否有空字符传
	public static boolean isHaveEmpty(String... args){
		if (args == null || args.length <= 0) {
			return false;
		}
		for (int i = 0; i < args.length; i++) {
			String aSource = args[i];
			if (isEmpty(aSource)) {
				return true;
			}
		}
		return false;
	}
}
