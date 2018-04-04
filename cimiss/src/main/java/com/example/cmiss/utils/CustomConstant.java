package com.example.cmiss.utils;

public class CustomConstant {
	public static String CIMISS_ENCODING_UTF8 = "UTF-8";//解析文件时候用到的编码格式
	public static String CIMISS_ENCODING_GBK = "GBK";//解析文件时候用到的编码格式
	
	//闪电定位
	public static String CIMISS_SDDW_INTERFACEID = "getUparLightFilesByTimeRange";//接口ID 
	 
	//卫星资料展示
	public static String CIMISS_WX_INTERFACEID = "getSateFileByTimeRange";//接口ID
	public static String CIMISS_WX_SAVEDIR = "/temp/wx/";//卫星资料展示存放目录
	
	
	//=============雷达参数区=====================
	/**
		{Z9270-武汉站,Z9717-宜昌站,Z9718-恩施站,Z9719-十堰站,Z9722-随州站,Z9060-神龙架站,Z9716-荆州站,Z9710-襄阳站}
	 */
	public static String  SITES = "Z9270,Z9717,Z9718,Z9719,Z9722,Z9060,Z9716,Z9710";
	
	public static String CIMISS_LD_SAVEDIR = "/temp/ld/";//雷达数据目录
	
	public static String CIMISS_LD_PT_INTERFACEID = "getRadaFileByTimeRange";//拼图数据接口ID
	
	//质控后单站多普勒天气雷达产品-基本反射率-(R)
	public static String CIMISS_LD_R_INTERFACEID = "getRadaFileByTimeRangeAndStaId";//接口ID
	public static String CIMISS_LD_R_SAVEDIR = "/temp/ld/r/";//R数据
	public static String CIMISS_LD_R_PT_SAVEDIR = "/temp/ld/r/pt/";//R拼图数据-长江流域
	public static String CIMISS_LD_R_HZPT_SAVEDIR = "/temp/ld/r/hzpt/";//R拼图数据-华中区域
	public static String CIMISS_LD_R_HBPT_SAVEDIR = "/temp/ld/r/hbpt/";//R拼图数据-湖北
	
	//质控后单站多普勒天气雷达产品-组合反射率-(CR)
	public static String CIMISS_LD_CR_INTERFACEID = "getRadaFileByTimeRangeAndStaId";//接口ID
	public static String CIMISS_LD_CR_SAVEDIR = "/temp/ld/cr/";//CR数据
	public static String CIMISS_LD_CR_PT_SAVEDIR = "/temp/ld/cr/pt/";//CR拼图数据-长江流域
	public static String CIMISS_LD_CR_HZPT_SAVEDIR = "/temp/ld/cr/hzpt/";//CR拼图数据-华中区域
	public static String CIMISS_LD_CR_HBPT_SAVEDIR = "/temp/ld/cr/hbpt/";//CR拼图数据-湖北
	
	//质控后单站多普勒天气雷达产品-回波顶高-(ET)
	public static String CIMISS_LD_ET_INTERFACEID = "getRadaFileByTimeRangeAndStaId";//接口ID
	public static String CIMISS_LD_ET_SAVEDIR = "/temp/ld/et/";//ET数据
	
	//质控后单站多普勒天气雷达产品-垂直累积液态水含量-(VIL)
	public static String CIMISS_LD_VIL_INTERFACEID = "getRadaFileByTimeRangeAndStaId";//接口ID
	public static String CIMISS_LD_VIL_SAVEDIR = "/temp/ld/vil/";//VIL数据
	public static String CIMISS_LD_VIL_PT_SAVEDIR = "/temp/ld/vil/pt/";//VIL拼图数据-长江流域
	public static String CIMISS_LD_VIL_HZPT_SAVEDIR = "/temp/ld/vil/hzpt/";//VIL拼图数据-华中区域
	public static String CIMISS_LD_VIL_HBPT_SAVEDIR = "/temp/ld/vil/hbpt/";//VIL拼图数据-湖北
}
