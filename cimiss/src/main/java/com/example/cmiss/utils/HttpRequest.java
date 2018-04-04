package com.example.cmiss.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     * @throws Exception 
     */
    public static String sendPost(String url, String param) throws Exception {
        PrintWriter out = null;
        BufferedReader br = null;
        String result = "";
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter ow = new OutputStreamWriter(os, "utf-8");
        out = new PrintWriter(ow,true);
       // out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        out.print(param);
        // flush输出流的缓冲
        out.flush();
        
        br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        String line;
        while ((line = br.readLine()) != null) {
            result += line;
        }
        //使用finally块来关闭输出流、输入流
        try{
            if(out!=null){
                out.close();
            }
            if(br!=null){
                br.close();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return result;
    }    
    public static void main(String args[]){
    	//发送 POST 请求
        String sr;
		try {
			sr = HttpRequest.sendPost("http://59.172.208.250:9093/", "");
			System.out.println(sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}