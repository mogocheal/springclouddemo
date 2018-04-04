package com.thunder.model.secondaryModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@Entity
@IdClass(Destination.class)
public class Thunder2014 {
  private String latitude;
  private String longitude;
  private String intens;
  private String slope;
  private String error;
  private String location;
  @Id
  private Date datetime;
  private String hour;
  private String minute;
  private String second;
  @Id
  private String minisecond;
  private String province;
  private String district;
  private String country;
  private java.sql.Date inputtime;
  private String processflag;
  private String usedids;
  @Column(name = "cg_ic")
  private String type;
  private String height;

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getIntens() {
    return intens;
  }

  public void setIntens(String intens) {
    this.intens = intens;
  }

  public String getSlope() {
    return slope;
  }

  public void setSlope(String slope) {
    this.slope = slope;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getDatetime() {
    return datetime;
  }

  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }

  public String getHour() {
    return hour;
  }

  public void setHour(String hour) {
    this.hour = hour;
  }

  public String getMinute() {
    return minute;
  }

  public void setMinute(String minute) {
    this.minute = minute;
  }

  public String getSecond() {
    return second;
  }

  public void setSecond(String second) {
    this.second = second;
  }

  public String getMinisecond() {
    return minisecond;
  }

  public void setMinisecond(String minisecond) {
    this.minisecond = minisecond;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public java.sql.Date getInputtime() {
    return inputtime;
  }

  public void setInputtime(java.sql.Date inputtime) {
    this.inputtime = inputtime;
  }

  public String getProcessflag() {
    return processflag;
  }

  public void setProcessflag(String processflag) {
    this.processflag = processflag;
  }

  public String getUsedids() {
    return usedids;
  }

  public void setUsedids(String usedids) {
    this.usedids = usedids;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }
}
