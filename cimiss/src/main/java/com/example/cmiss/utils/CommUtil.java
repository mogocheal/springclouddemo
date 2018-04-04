package com.example.cmiss.utils;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommUtil{
	
	private static final Log log = LogFactory.getLog(CommUtil.class);
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


	public static List<List<String>> getList(JSONArray jsonArray) {
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> clist = null;
		if (jsonArray.size() > 0) {
			for (int i = 0; i < jsonArray.size(); i++) {
				clist = new ArrayList<String>();
				JSONArray ja = jsonArray.getJSONArray(i);
				for (int j = 0; j < ja.size(); j++) {
					clist.add(ja.getString(j));
				}
				list.add(clist);
			}
		}
		return list;
	}

	/**
	 * 判断某个字符串中是否包含某个字符
	 * @param str  要检测的字符串
	 * @param regEx  是否包含的字符
	 * @return boolean
	 */
	public static boolean isInclude(String str,String regEx){
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		boolean rs = m.find();
		return rs;
	}
	
	
	
	 /**
     * 
     * @创建时间：Feb 17, 2013 9:23:14 PM
     * @方法描述：（对象为空字符或null或集合长度为0）
     * @返回值：true/false
     * @version
     */
    public static boolean isNullOrBlanK(Object param) {
    	 if(param==null){
         	return true;
    	 }else if(param.toString().length() == 0){
    		return true;
    	 }else{
         	if(String.class.isInstance(param)){
         		if(!"".equals(((String) param).trim())){
         			return false;
         		}
         	}else if(List.class.isInstance(param)){
         		if(((ArrayList) param).size()!=0){
         			return false;
         		}
         	}else if(Map.class.isInstance(param)){
         		if(((HashMap) param).size()!=0){
         			return false;
         		}
         	}else if(String[].class.isInstance(param)){
         		if(((Object[]) param).length!=0){
         			return false;
         		}
         	}else{
         		return false;
         	}
         	return true;
         }
    }


    /**
     *
     * @创建时间：Feb 17, 2013 10:24:15 PM
     * @方法描述：根据类型+当前时间+随机数生成一个ID(最少3位)
     * @参数:int
     * @返回值：String
     * @version
     */
    public static synchronized String getNewID(String type,int randomLen) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		// type1年4月2日2时2分2秒2毫秒1共14位
		return type + sdf.format(new Date()) + getRandom(randomLen);

	}


    /**
     *
     * @创建时间：Feb 17, 2013 10:24:15 PM
     * @方法描述：(生产一个最少3位的随机数(仅数字),无参数或参数小于3者默认生产3位随机,大于等于3则为实际填写数字)
     * @参数:int
     * @返回值：String
     * @version
     */
    public static String getRandom(int iLength) {
		try {
			String[] m_srcStr = new String[] { "012345678998765432",
					"123456789009876543" };
			int len = 3;
			if (iLength >= 3)
				len = iLength;
			Random m_rnd = new Random();
			byte[] m_b = new byte[len];
			m_rnd.nextBytes(m_b);
			String m_pwdStr = "";
			for (int i = 0; i < len; i++) {
				int startIdx = Math.abs((int) m_b[i] % 18);
				m_pwdStr += m_srcStr[i % 2].substring(startIdx, startIdx + 1);
			}
			return m_pwdStr;
		} catch (Exception ex) {
			log.info("生成随机密码时发生异常:"+ex);
		}
		return null;
	}


    /**
     * 将数组或list转换成sql查询字符串   String[] str = {"1","2"} 转换后 '1','2'
     * @param str
     * @return
     */
    public static String getSqlinStrbyObjs(Object param){
    	String result = "";
    	if(param==null){
         	return "''";
         }else{
         	if(List.class.isInstance(param)){
         		for (Object string : (List<String>)param) {
            		result+="'"+string.toString()+"',";
        		}
         	}else if(String[].class.isInstance(param)){
         		for (String string : (String[])param) {
            		result+="'"+string+"',";
        		}
         	}else if(String.class.isInstance(param)){
         		if(!isNullOrBlanK(param)){
         			if(param.toString().indexOf(",")!=-1){
         				for (String string : param.toString().split(",")) {
                    		result+="'"+string+"',";
                		}
         			}else{
         				result+="'"+param.toString()+"',";
         			}
         		}
         	}else{
         		return "''";
         	}
         	return result.length()>0?result.substring(0,result.length()-1):"''";
         }
    }
    
    
    /**
     * 生成一个最少3位字母和数字混合的验证码
     * @return
     */
    public static String getVerifyCode(int iLength){
    	StringBuffer inviteCode = new StringBuffer();
    	String[] source = {"1","2","3","4","5","6","7","8","9","0",
    			"a","b","c","d","e","f","g","h","j","k","m","n","p","q","r","s","t","u","v","w","x","y","z",
    			"A","B","C","D","E","F","G","H","J","K","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"};
    	Random r = new Random();
    	if(iLength<=3){
    		iLength = 3;
    	}
    	for (int i = 0; i < iLength; i++) {
    		inviteCode.append(source[r.nextInt(source.length)]);
		}
    	return inviteCode.toString();
    }
    
    /**
     * 获取邀请码（大写字母+数字）
     * @param iLength
     * @return
     */
    public static String getInviteCode(int iLength){
    	StringBuffer inviteCode = new StringBuffer();
    	String[] source = {"1","2","3","4","5","6","7","8","9","0",
    			"A","B","C","D","E","F","G","H","J","K","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"};
    	Random r = new Random();
    	if(iLength<=3){
    		iLength = 3;
    	}
    	for (int i = 0; i < iLength; i++) {
    		inviteCode.append(source[r.nextInt(source.length)]);
    	}
    	return inviteCode.toString();
    }
    
    /**
     * 构造Oracle分页SQL
     * 
     * @param sql
     */
    public static String buildPageSql(String sql, String startNum, String EndNum) {

            if (startNum == null || startNum.equals("")) {
                    return sql;
            }
            if (EndNum == null || EndNum.equals("")) {
                    return sql;
            }
            StringBuffer sb = new StringBuffer();
            sb.append(" SELECT AA.* FROM (SELECT ROWNUM NUM,KK.* FROM ( ");
            sb.append(sql);
            sb.append(" )KK WHERE ROWNUM <= ").append(EndNum);
            sb.append(" ) AA WHERE NUM > ");
            sb.append(startNum);
            return sb.toString();
    }
    
    

	 /**通过request获取IP地址
		 * @param request
		 * @return
		 */
		public static String getIpAddr(HttpServletRequest request) {
			String ip = request.getHeader("X-Forwarded-For");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			return ip;
		}	
		
		public static boolean isNumeric(String str){ 
		    Pattern pattern = Pattern.compile("[0-9]*(.)?[0-9]+"); 
		    return pattern.matcher(str).matches();    
		 }

		/**
		 * 获取本周所有日期,yyyy-MM-dd
		 * @return
		 */
		public static List<String> getWeekdays() {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EE");
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			List<String> list = new ArrayList<String>();
			while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			       calendar.add(Calendar.DATE, -1);
			}
			for (int i = 0; i < 7; i++) {
				log.info(dateFormat.format(calendar.getTime()));
				list.add(dateFormat.format(calendar.getTime()));
				calendar.add(Calendar.DATE, 1);
			}
			return list;
	    }

		/**
		 * 获取本月所有日期,yyyy-MM-dd
		 * @return
		 */
		public static List<String> getAllTheDateOftheMonth() {
			List<String> list = new ArrayList<String>();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.DATE, 1);
			int month = cal.get(Calendar.MONTH);
			while(cal.get(Calendar.MONTH) == month){
				list.add(dateFormat.format(cal.getTime()));
				cal.add(Calendar.DATE, 1);
			}
			return list;
		}
    
		/**
		 * 图片缩放
		 * @param fromFileStr 图片地址
		 * @param saveToFileStr 目标地址
		 * @param width 缩放宽度
		 * @param hight 缩放高度
		 * @param flag 师傅本比例缩放
		 * @throws Exception
		 */
		public static void saveImageAsJpg(String fromFileStr, String saveToFileStr,
				int width, int hight,boolean flag) throws Exception {
			BufferedImage srcImage;
			String imgType = "JPEG";
			if (fromFileStr.toLowerCase().endsWith(".png")) {
				imgType = "PNG";
			}
			File saveFile = new File(saveToFileStr);
			File fromFile = new File(fromFileStr);
			srcImage = ImageIO.read(fromFile);
			if (width > 0 || hight > 0) {
				// targetW，targetH分别表示目标长和宽
				int type = srcImage.getType();
				BufferedImage target = null;
				double sx = (double) width / srcImage.getWidth();
				double sy = (double) hight / srcImage.getHeight();
				// 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
				if(flag){
					if (sx > sy) {
					sx = sy;
					width = (int) (sx * srcImage.getWidth());
					} else {
						sy = sx;
						hight = (int) (sy * srcImage.getHeight());
					}
				}
				if (type == BufferedImage.TYPE_CUSTOM) {
					ColorModel cm = srcImage.getColorModel();
					WritableRaster raster = cm.createCompatibleWritableRaster(width,
							hight);
					boolean alphaPremultiplied = cm.isAlphaPremultiplied();
					target = new BufferedImage(cm, raster, alphaPremultiplied, null);
				} else
					target = new BufferedImage(width, hight, type);
				Graphics2D g = target.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_QUALITY);
				g.drawRenderedImage(srcImage, AffineTransform.getScaleInstance(sx, sy));
				g.dispose();
				srcImage = target;
			}
			ImageIO.write(srcImage, imgType, saveFile);
		}
		
		public static String getSignature(Map<String, String> params, String dev_server_secret) throws IOException{
			 // 先将参数以其参数名的字典序升序进行排序
	        Map<String, String> sortedParams = new TreeMap<String, String>(params);
	 
	        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
	        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	        StringBuilder basestring = new StringBuilder();
	        for (Map.Entry<String, String> param : entrys) {
	        	if (!"sign".equals(param.getKey())) {
	        		basestring.append(param.getKey()).append("=").append(param.getValue());
				}
	        }
	        basestring.append(dev_server_secret);
	        System.out.println(basestring.toString());
	        // 使用MD5对待签名串求签
	        byte[] bytes = null;
	        try {
	            MessageDigest md5 = MessageDigest.getInstance("MD5");
	            bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
	        } catch (GeneralSecurityException ex) {
	            throw new IOException(ex);
	        }
	        // 将MD5输出的二进制结果转换为小写的十六进制
	        StringBuilder sign = new StringBuilder();
	        for (int i = 0; i < bytes.length; i++) {
	            String hex = Integer.toHexString(bytes[i] & 0xFF);
	            if (hex.length() == 1) {
	                sign.append("0");
	            }
	            sign.append(hex);
	        }
	        return sign.toString();
		}

	/**
	 *获取特定字符串第n次出现的索引
	 * @param str    字符串
	 * @param pattern  特定字符
	 * @param count  出现的次数
	 * @return
	 */
	public static int getIndexByCount(String str,String pattern,int count){
		Matcher matcher = Pattern.compile(pattern).matcher(str);
		int index = 0;
		while(matcher.find()) {
			index++;
			//当"/"符号第三次出现的位置
			if(index == count){
				break;
			}
		}
		return matcher.start();
	}
}
