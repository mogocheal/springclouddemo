package com.example.cmiss.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 
* @ClassName: JsonUtil 
* @Description: JSON转换工具类
* @author penghuaxia
* @date 2014-7-3 上午11:28:54 
*
 */
public class JsonUtil {
	
    /**
     * 
     * @param json
     * @param c
     * @Description: JSON转Bean
     * @return
     */
    public static Object toBean(String json, Class<?> c) {
        return JSON.parseObject(json, c);
    }

    /**
     * 
     * @param json
     * @param c
     * @Description: JSON转List
     * @return
     */
    public static List toList(String json, Class<?> c) {
        List list = JSON.parseArray(json, c);
        return list;
    }
    
    public static String toJson(Object o){
        return JSON.toJSONString(o);
    }
    
    public static String toJson(List list){
        return JSON.toJSONString(list);
    }

}
