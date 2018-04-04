package com.example.cmiss.model;

public class GridJson {
	
	private GridCollection data;   //格点数据
	private String status;         //数据是否有效状态（例如：0：无效;1：有效）
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public GridCollection getData() {
		return data;
	}
	public void setData(GridCollection data) {
		this.data = data;
	}
	
}
