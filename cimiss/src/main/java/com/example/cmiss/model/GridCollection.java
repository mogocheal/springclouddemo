package com.example.cmiss.model;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

public class GridCollection implements Serializable {
    
	private static final long serialVersionUID = -2699401464339118681L;
	
	private String type; //默认GridCollection类型
	private List<Grid> grids; //格点对象集合
	private Properties properties; //元数据的属性信息：包括数据类型、数据时效、数据层次、数据说明、文件名、数据日期以及其他可扩充属性
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Grid> getGrids() {
		return grids;
	}
	public void setGrids(List<Grid> grids) {
		this.grids = grids;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	
}
