package com.example.cmiss.config;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 手动补入国家站小时数据
 * @author:yjf
 * @ClassName:Manual
 * @Version:版本
 * @ModifiedBy:修改人
 * @Copyright:公司名称
 * @date:2016年4月20日下午4:28:25
 */
public class Cetion {

	private static Logger logger = Logger.getLogger(Cetion.class);
	
	static {  
        try {  
            // 加载数据库驱动程序  
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");  
        } catch (ClassNotFoundException e) {  
        	logger.error("加载驱动错误:"+e);  
            //System.out.println(e.getMessage());  
        }  
    }
	
	/**
	 * 创建连接
	 * @return
	 */
	public static Connection createConnection() {  
        try {
        	Properties pps = new Properties();
			pps.setProperty("hbase.sink.tracing.class", "org.apache.phoenix.trace.PhoenixMetricsSink");
			pps.setProperty("hbase.sink.tracing.writer-class", "org.apache.phoenix.trace.PhoenixTableMetricsWriter");
			pps.setProperty("hbase.sink.tracing.context", "tracing");
			pps.setProperty("phoenix.query.threadPoolSize", "128");
			pps.setProperty("phoenix.query.queueSize", "5000");
            return DriverManager.getConnection("jdbc:phoenix:nw1,nw2,nw3:2181", pps);
        } catch (SQLException e) {
        	logger.error("createConnection 方法创建连接异常:"+e); 
        	//e.printStackTrace();  
        }  
        return null;  
    }
	
}
