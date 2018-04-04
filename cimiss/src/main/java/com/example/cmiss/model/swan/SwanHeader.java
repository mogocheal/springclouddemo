package com.example.cmiss.model.swan;

import java.io.Serializable;
import java.util.Date;

public class SwanHeader implements Serializable {
    //文件头格式，长度1024个字节
    private String zonName;                    // diamond 131 12个字节
    private String dataName;                //数据说明(例如 2008年5月19日雷达三维拼图)38个字节
    private String flag;                    // 文件标志，"swan"   8字节
    private String version;                    // 数据版本号，"1.0" 8字节
    private Date time;
    //private ushort year;                    //2008 两个字节
    //private ushort month;                   //05  两个字节
    //private ushort day;                     //19    两个字节
    //private ushort hour;                    //14   两个字节
    //private ushort minute;                  //31 两个字节
    private int interval;                //两个字节        
    private int xNumGrids;               //1300 两个字节
    private int yNumGrids;               //800 两个字节
    private int zNumGrids;               //20  两个字节
    private int radarCount;                 //拼图雷达数 四个字节
    private float startLon;                 //网格开始经度（左上角） 四个字节
    private float startLat;                 //网格开始纬度（左上角） 四个字节
    private float centerLon;                //网格中心经度 四个字节
    private float centerLat;                //网格中心纬度 四个字节
    private float xReso;                    //经度方向分辨率 四个字节
    private float yReso;                    //纬度方向分辨率 四个字节
    private float[] zhighGrids;             //40垂直方向的高度（单位km）数目根据ZnumGrids而得（最大40层） 160个字节。
    private String[] radarStationName;      //雷达站点名称,	20*16字节
    private float[] radarLongitude;        //雷达站点所在经度，单位：度， 4*20字节
    private float[] radarLatitude;         //雷达站点所在纬度，单位：度， 4*20字节
    private float[] radarAltitude;         //雷达所在海拔高度，单位：米， 4*20字节
    private byte[] mosaicFlag;              //该雷达数据是否包含在本次拼图中，未包含:0，包含:1, 20字节
    //private	byte[] reserveds;			    //备用 172字节

    public String getZonName() {
        return zonName;
    }

    public void setZonName(String zonName) {
        this.zonName = zonName;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {

        this.time = time;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getXNumGrids() {
        return xNumGrids;
    }

    public void setXNumGrids(int numGrids) {
        xNumGrids = numGrids;
    }

    public int getYNumGrids() {
        return yNumGrids;
    }

    public void setYNumGrids(int numGrids) {
        yNumGrids = numGrids;
    }

    public int getZNumGrids() {
        return zNumGrids;
    }

    public void setZNumGrids(int numGrids) {
        zNumGrids = numGrids;
    }

    public int getRadarCount() {
        return radarCount;
    }

    public void setRadarCount(int radarCount) {
        this.radarCount = radarCount;
    }

    public float getStartLon() {
        return startLon;
    }

    public void setStartLon(float startLon) {
        this.startLon = startLon;
    }

    public float getStartLat() {
        return startLat;
    }

    public void setStartLat(float startLat) {
        this.startLat = startLat;
    }

    public float getCenterLon() {
        return centerLon;
    }

    public void setCenterLon(float centerLon) {
        this.centerLon = centerLon;
    }

    public float getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(float centerLat) {
        this.centerLat = centerLat;
    }

    public float getXReso() {
        return xReso;
    }

    public void setXReso(float reso) {
        xReso = reso;
    }

    public float getYReso() {
        return yReso;
    }

    public void setYReso(float reso) {
        yReso = reso;
    }

    public float[] getZhighGrids() {
        return zhighGrids;
    }

    public void setZhighGrids(float[] zhighGrids) {
        this.zhighGrids = zhighGrids;
    }

    public String[] getRadarStationName() {
        return radarStationName;
    }

    public void setRadarStationName(String[] radarStationName) {
        this.radarStationName = radarStationName;
    }

    public float[] getRadarLongitude() {
        return radarLongitude;
    }

    public void setRadarLongitude(float[] radarLongitude) {
        this.radarLongitude = radarLongitude;
    }

    public float[] getRadarLatitude() {
        return radarLatitude;
    }

    public void setRadarLatitude(float[] radarLatitude) {
        this.radarLatitude = radarLatitude;
    }

    public float[] getRadarAltitude() {
        return radarAltitude;
    }

    public void setRadarAltitude(float[] radarAltitude) {
        this.radarAltitude = radarAltitude;
    }

    public byte[] getMosaicFlag() {
        return mosaicFlag;
    }

    public void setMosaicFlag(byte[] mosaicFlag) {
        this.mosaicFlag = mosaicFlag;
    }
    /*public byte[] getReserveds() {
        return reserveds;
	}
	public void setReserveds(byte[] reserveds) {
		this.reserveds = reserveds;
	}*/
}
