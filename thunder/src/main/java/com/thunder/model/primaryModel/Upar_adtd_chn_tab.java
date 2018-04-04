package com.thunder.model.primaryModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="UPAR_ADTD_CHN_TAB" )
public class Upar_adtd_chn_tab {
  @Id
  private String d_record_id;
  private java.sql.Date d_rymdhm;
  private java.sql.Date d_update_time;
  @Column(name = "d_datetime")
  private String dateTime;
  private String d_data_id;
  private java.sql.Date d_iymdhm;
  private String v05001;
  private String v06001;
  private String v04001;
  private String v04002;
  private String v04003;
  private String v04004;
  private String v04005;
  private String v04006;
  private String v04007;
  private String v08300;
  private String v73016;
  private String v73011;
  private String v73110;
  @Column(name = "v01015_1")
  private String province;
  @Column(name = "v01015_2")
  private String city;
  @Column(name = "v01015_3")
  private String county;
  private String q73016;
  private String v73023;

  public String getD_record_id() {
    return d_record_id;
  }

  public void setD_record_id(String d_record_id) {
    this.d_record_id = d_record_id;
  }

  public java.sql.Date getD_rymdhm() {
    return d_rymdhm;
  }

  public void setD_rymdhm(java.sql.Date d_rymdhm) {
    this.d_rymdhm = d_rymdhm;
  }

  public java.sql.Date getD_update_time() {
    return d_update_time;
  }

  public void setD_update_time(java.sql.Date d_update_time) {
    this.d_update_time = d_update_time;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public String getD_data_id() {
    return d_data_id;
  }

  public void setD_data_id(String d_data_id) {
    this.d_data_id = d_data_id;
  }

  public java.sql.Date getD_iymdhm() {
    return d_iymdhm;
  }

  public void setD_iymdhm(java.sql.Date d_iymdhm) {
    this.d_iymdhm = d_iymdhm;
  }

  public String getV05001() {
    return v05001;
  }

  public void setV05001(String v05001) {
    this.v05001 = v05001;
  }

  public String getV06001() {
    return v06001;
  }

  public void setV06001(String v06001) {
    this.v06001 = v06001;
  }

  public String getV04001() {
    return v04001;
  }

  public void setV04001(String v04001) {
    this.v04001 = v04001;
  }

  public String getV04002() {
    return v04002;
  }

  public void setV04002(String v04002) {
    this.v04002 = v04002;
  }

  public String getV04003() {
    return v04003;
  }

  public void setV04003(String v04003) {
    this.v04003 = v04003;
  }

  public String getV04004() {
    return v04004;
  }

  public void setV04004(String v04004) {
    this.v04004 = v04004;
  }

  public String getV04005() {
    return v04005;
  }

  public void setV04005(String v04005) {
    this.v04005 = v04005;
  }

  public String getV04006() {
    return v04006;
  }

  public void setV04006(String v04006) {
    this.v04006 = v04006;
  }

  public String getV04007() {
    return v04007;
  }

  public void setV04007(String v04007) {
    this.v04007 = v04007;
  }

  public String getV08300() {
    return v08300;
  }

  public void setV08300(String v08300) {
    this.v08300 = v08300;
  }

  public String getV73016() {
    return v73016;
  }

  public void setV73016(String v73016) {
    this.v73016 = v73016;
  }

  public String getV73011() {
    return v73011;
  }

  public void setV73011(String v73011) {
    this.v73011 = v73011;
  }

  public String getV73110() {
    return v73110;
  }

  public void setV73110(String v73110) {
    this.v73110 = v73110;
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
    return county;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  public String getQ73016() {
    return q73016;
  }

  public void setQ73016(String q73016) {
    this.q73016 = q73016;
  }

  public String getV73023() {
    return v73023;
  }

  public void setV73023(String v73023) {
    this.v73023 = v73023;
  }
}
