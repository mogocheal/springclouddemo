package com.example.cmiss.model;

import java.io.Serializable;

public class GridProperties implements Serializable {
	
	private static final long serialVersionUID = -5108812314151105991L;
	
	private float[][] gridDatas;  //格点数据：数据按先纬向后经向放（直角坐标网格时为先X方向后Y方向）
	                              //例如：Diamond4类数据,只有1层格点数据即 gridDatas = new float[1][row*column]
	                              //  
	                              //例如：Diamond11类数据,有4层格点数据即 gridDatas = new float[4][row*column]
								  //第1层放风速数据;第2层放风向数据;第3层放U分量数据;第4层放V分量数据。
								  //
	private float lonGap;         //经度间隔
	private float latGap;         //纬度间隔
	private float startLon;       //起始经度
	private float endLon;         //结束经度
	private float startLat;       //起始纬度
	private float endLat;         //结束纬度
	private int row;              //纬度格点数
	private int column;           //经度格点数
	private float analyseStep;    //分析值间隔
	private float startValue;     //开始分析值
	private float endValue;       //结束分析值
	private float smooth;         //平滑值
	
	public float[][] getGridDatas() {
		return gridDatas;
	}
	public void setGridDatas(float[][] gridDatas) {
		this.gridDatas = gridDatas;
	}
	public float getLonGap() {
		return lonGap;
	}
	public void setLonGap(float lonGap) {
		this.lonGap = lonGap;
	}
	public float getLatGap() {
		return latGap;
	}
	public void setLatGap(float latGap) {
		this.latGap = latGap;
	}
	public float getStartLon() {
		return startLon;
	}
	public void setStartLon(float startLon) {
		this.startLon = startLon;
	}
	public float getEndLon() {
		return endLon;
	}
	public void setEndLon(float endLon) {
		this.endLon = endLon;
	}
	public float getStartLat() {
		return startLat;
	}
	public void setStartLat(float startLat) {
		this.startLat = startLat;
	}
	public float getEndLat() {
		return endLat;
	}
	public void setEndLat(float endLat) {
		this.endLat = endLat;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public float getAnalyseStep() {
		return analyseStep;
	}
	public void setAnalyseStep(float analyseStep) {
		this.analyseStep = analyseStep;
	}
	public float getStartValue() {
		return startValue;
	}
	public void setStartValue(float startValue) {
		this.startValue = startValue;
	}
	public float getEndValue() {
		return endValue;
	}
	public void setEndValue(float endValue) {
		this.endValue = endValue;
	}
	public float getSmooth() {
		return smooth;
	}
	public void setSmooth(float smooth) {
		this.smooth = smooth;
	}
	
	
	
}
