package com.example.cmiss.config;

import java.util.List;

/**
 * 缓存接口
 * @author pengt
 *
 */
public interface IMcCache<T> {

	/**
	 * 一次性获取所有
	 * @return
	 */
    List<T> get();
	
	/**
	 * 获取单个数据项目
	 * @param key
	 * @return
	 */
    T get(String key);
	
	/**
	 * 加载数据到缓存
	 */
    void load();
}
