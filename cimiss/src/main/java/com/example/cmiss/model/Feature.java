package com.example.cmiss.model;

public class Feature {
	
	private String geoType;//点、线、面数据类型标识（例如：点：Point;多点：MultiPoint;线：LineString;面：Polygon等）
	private String[] field;//要素字段名集合（例如：温、压、湿、风等要素）
	private String[] fieldType;//要素字段类型集合 （例如："int"、"string"、"float"等要素类型）
	private String data; //数据内容：
	//1.点数据格式串：x1 y1 字段值1 字段值2 字段值3;x2 y2 字段值1 字段值2 字段值3;......
	//2.多点数据格式串：x1 y1 x2 y2 x3 y3 字段值1 字段值2;x1 y1 x2 y2 x3 y3 字段值1 字段值2;......
	//3.线数据格式串：x1 y1 x2 y2 x3 y3 字段值1 字段值2;x1 y1 x2 y2 x3 y3 字段值1 字段值2;......
	//4.面数据格式串：x1 y1 x2 y2 x3 y3,字段值1 字段值2;x1 y1 x2 y2 x3 y3,字段值1 字段值2;......
	public String getGeoType() {
		return geoType;
	}
	public void setGeoType(String geoType) {
		this.geoType = geoType;
	}
	public String[] getField() {
		return field;
	}
	public void setField(String[] field) {
		this.field = field;
	}
	public String[] getFieldType() {
		return fieldType;
	}
	public void setFieldType(String[] fieldType) {
		this.fieldType = fieldType;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}
