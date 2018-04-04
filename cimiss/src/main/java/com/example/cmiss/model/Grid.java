package com.example.cmiss.model;

import java.io.Serializable;

public class Grid implements Serializable {
	
	private static final long serialVersionUID = 767133183349343622L;
	
	private String type;                //默认Grid类型
	private GridProperties properties;  //格点数据属性
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public GridProperties getProperties() {
		return properties;
	}
	public void setProperties(GridProperties properties) {
		this.properties = properties;
	}
	
}
