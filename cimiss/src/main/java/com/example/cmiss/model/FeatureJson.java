package com.example.cmiss.model;

public class FeatureJson {
	
	private FeatureCollection data;  //要素几何数据
	private String status;  //数据是否有效状态（例如：0：无效;1：有效）
	                        
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public FeatureCollection getData() {
		return data;
	}
	public void setData(FeatureCollection data) {
		this.data = data;
	}
}
