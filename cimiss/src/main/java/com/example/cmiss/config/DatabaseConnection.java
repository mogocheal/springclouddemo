package com.example.cmiss.config;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * the param of count is used for save current connection num(include of unused and usein)
 * 
 * the param of wait is used for threa synchronization lock when cann't get connection and need other connection be recycled and call in getConnection()
 * 
 * the releaseConnection() is ralease connection when call notify() notify the other waitting of threads 
 *  
 * get connection when judge of connection whether effective
 * 
 * @author:yjf
 * @ClassName:DatabaseConnection
 * @date:2016年3月24日上午11:59:30
 */

@Component
public class DatabaseConnection {

    public Connection getConnection() {
        return new Cetion().createConnection();
    }  
  
    public void releaseConnection(Connection connection) {
        try{
            if(connection != null){
                connection.close();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    } 
	
}
