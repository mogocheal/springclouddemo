package com.example.cmiss.config;

import java.io.Serializable;

public class StationInfo implements Serializable {
	private String stationNum;
	private String stationName;
	private String province;

	private String city;
	private String County;
	private double longitude;

	private double latitude;

	private double altitude;
	
	private String adminCode;

	private String stationLevl;

	private String stationType;

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return County;
	}

	public void setCounty(String county) {
		County = county;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public String getAdminCode() {
		return adminCode;
	}

	public void setAdminCode(String adminCode) {
		this.adminCode = adminCode;
	}

	public String getStationLevl() {
		return stationLevl;
	}

	public void setStationLevl(String stationLevl) {
		this.stationLevl = stationLevl;
	}

	public String getStationType() {
		return stationType;
	}

	public void setStationType(String stationType) {
		this.stationType = stationType;
	}
}
