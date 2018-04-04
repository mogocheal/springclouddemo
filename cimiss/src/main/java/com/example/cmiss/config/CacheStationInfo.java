package com.example.cmiss.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/9/8.
 */
@Component
public class CacheStationInfo implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    FinalConnectionHiveDB FinalConnectionHiveDB;

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 站点列表映射：区域（hb，cjly,hz,湖北市县行政编码）--站点类型（gj,qy）--站号列表
     */
    public static Map<String, Map<String, String>> areaStationListMap = new HashMap<String, Map<String, String>>();

    /**
     * 站点列表映射：区域（hb，cjly,hz,湖北市县行政编码）--站点类型（gj,qy）--站点详细信息
     */
    public static Map<String, Map<String, Map<String,StationInfo>>> areaStationInfoMap = new HashMap<String, Map<String, Map<String,StationInfo>>>();

    /**
     * 湖北市级映射
     */
    private static Map<String, String> hbcityMap = new HashMap<String, String>();

    /**
     * 交通站
     */
    public static List<String> trafficeStationList=new ArrayList<String>();

    static {
        hbcityMap.put("武汉", "420100");
        hbcityMap.put("黄石","420200");
        hbcityMap.put("十堰", "420300");
        hbcityMap.put("宜昌", "420500");
        hbcityMap.put("襄阳", "420600");
        hbcityMap.put("荆门", "420800");
        hbcityMap.put("孝感", "420900");
        hbcityMap.put("荆州", "421000");
        hbcityMap.put("黄冈", "421100");
        hbcityMap.put("咸宁", "421200");
        hbcityMap.put("随州", "421300");
        hbcityMap.put("恩施", "422800");
        hbcityMap.put("鄂州", "420700");
        hbcityMap.put("天门", "429006");
        hbcityMap.put("潜江", "429005");
        hbcityMap.put("仙桃", "429004");
        hbcityMap.put("神农架", "429021");
    }

    /**
     * 县级行政编码列表
     */
    private static String countAdminCodeList = "420113,420114,420115,420116,420117,420222,420281,420304,420322,420323,420324,420325,420381,420506,420525,420526,420527,420528,420529,420581,420582,420583," +
            "420624,420625,420626,420682,420683,420684,420821,420822,420881,420921,420922,420923,420981,420982,420984,421003,421022,421023,421081,421083,421087," +
            "421121,421122,421123,421124,421125,421126,421127,421181,421182,421221,421222,421223,421224,421281,421381,422802,422822,422823,422825,422826,422827,422828";

    /**
     * 区域条件
     */
    private static String[] areaConditionArray = new String[]{"province = '湖北省'", "STATIONTYPE=1", "province = '湖北省' or  province = '湖南省' or  province = '河南省'"};

    /**
     * 站点基础信息字段
     */
    private static String stationInfoFields = "STATIONNUM,STATIONNAME,ADMINCODE,PROVINCE,CITY,COUNTY,STATIONLEVL,STATIONTYPE,LATITUDE,LONGITUDE,ALTITUDE";

    /**
     * @param arg0
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        logger.info("开始缓存所有的站点信息");
        String STA_INFO_QYSQL = "select " + stationInfoFields + " from STA_INFO_REG where STATIONTYPE=1 or province = '湖北省' or  province = '湖南省' or  province = '河南省'";
        String STA_INFO_GJSQL = "select " + stationInfoFields + " from STA_INFO_TEST where STATIONTYPE=1 or province = '湖北省' or  province = '湖南省' or  province = '河南省'";
        String STA_INFO_TRAFFIC="select " + stationInfoFields + " from STA_INFO_REG where STATIONLEVL=15";
        List<StationInfo> qyStationList = new ArrayList<>();
        getStationInfo(qyStationList,  STA_INFO_QYSQL);
        List<StationInfo> gjStationList = new ArrayList<>();
        getStationInfo(gjStationList,  STA_INFO_GJSQL);
        addAreaStation(qyStationList,"qy");
        addAreaStation(gjStationList,"gj");
        String [] countys=countAdminCodeList.split(",");
        logger.info("地市个数:"+countys.length);
        //添加湖北市县级信息
        addHBAreaInfo("gj");
        addHBAreaInfo("qy");

        List<StationInfo> trafficStationList = new ArrayList<>();
        getStationInfo(trafficStationList,  STA_INFO_TRAFFIC);
        for(StationInfo stationInfo:trafficStationList){
            trafficeStationList.add(stationInfo.getStationNum());
        }
        logger.info("所有的站点信息缓存结束");
    }

    //获取市级站号
    public static String getCityStationNum(String cityName){  //cityName:城市的中文名 例如：武汉、十堰
        return hbcityMap.get(cityName);
    }

    /**
     * 获取站点信息
     *
     * @param qyStationList 站点信息集合
     * @param sql           查询SQL
     */
    private void getStationInfo(List<StationInfo> qyStationList, String sql) {
        List resultList = FinalConnectionHiveDB.queryRetrun(sql);
        for (Object obj : resultList) {
            Map rowData = (Map) obj;
            StationInfo stationInfo=new StationInfo();
            String stationNum=rowData.get("STATIONNUM").toString();
            if(stationNum.length()>0){
                stationInfo.setStationNum(stationNum);
                stationInfo.setStationName(rowData.get("STATIONNAME").toString());
                stationInfo.setAdminCode(rowData.get("ADMINCODE").toString());
                stationInfo.setProvince(rowData.get("PROVINCE").toString());
                stationInfo.setCity(rowData.get("CITY").toString());
                stationInfo.setCounty(rowData.get("COUNTY").toString());
                stationInfo.setStationLevl(rowData.get("STATIONLEVL").toString());
                stationInfo.setStationType(rowData.get("STATIONTYPE").toString());
                String latitude=rowData.get("LATITUDE").toString();
                String longitude=rowData.get("LONGITUDE").toString();
                String altitude=rowData.get("ALTITUDE").toString();
                try{
                    if(latitude.length()>0){
                        stationInfo.setLatitude(Double.parseDouble(latitude));
                    }else{
                        logger.error("站号："+stationInfo.getStationNum()+",站名："+stationInfo.getStationName()+" 纬度为空");
                    }
                    if(longitude.length()>0){
                        stationInfo.setLongitude(Double.parseDouble(longitude));
                    }else{
                        logger.error("站号："+stationInfo.getStationNum()+",站名："+stationInfo.getStationName()+" 经度为空");
                    }
                    if(altitude.length()>0){
                        stationInfo.setAltitude(Double.parseDouble(altitude));
                    }else{
                        logger.info("站号："+stationInfo.getStationNum()+",站名："+stationInfo.getStationName()+" 海拔为空");
                    }
                    qyStationList.add(stationInfo);
                }catch (Exception e){
                    logger.error("站号："+stationInfo.getStationNum()+",站名："+stationInfo.getStationName() +"经纬度或者海拔高度非数值类型");
                }
            }else{
                logger.error("站号为空或者非法");
            }
        }
    }

    /**
     * 添加区域站点
     *
     * @param stationList 站点列表
     * @param stationKind 站点种类
     */
    private void addAreaStation(List<StationInfo> stationList,String stationKind){
        StringBuilder hbStationNumBuilder=new StringBuilder();
        StringBuilder cjlyStationNumBuilder=new StringBuilder();
        StringBuilder hzqyStationNumBuilder=new StringBuilder();
        Map<String,StationInfo> hbStationMap=new HashMap<String,StationInfo>();
        Map<String,StationInfo> cjlyStationMap=new HashMap<String,StationInfo>();
        Map<String,StationInfo> hzqyStationMap=new HashMap<String,StationInfo>();
        for(StationInfo stationInfo:stationList){
            String stationNum=stationInfo.getStationNum();
            String province=stationInfo.getProvince();
            String stationType=stationInfo.getStationType();
            if("1".equals(stationType)){
                cjlyStationNumBuilder.append(stationNum+",");
                cjlyStationMap.put(stationNum,stationInfo);
            }
            if("湖北省".equals(province)){
                hbStationNumBuilder.append(stationNum+",");
                hbStationMap.put(stationNum,stationInfo);
            }

            if("湖北省".equals(province) || "湖南省".equals(province) || "河南省".equals(province)){
                hzqyStationNumBuilder.append(stationNum+",");
                hzqyStationMap.put(stationNum,stationInfo);
            }
        }
        initStationResult("hb", stationKind,hbStationNumBuilder, hbStationMap);
        initStationResult("cjly", stationKind,cjlyStationNumBuilder, cjlyStationMap);
        initStationResult("hz", stationKind,hzqyStationNumBuilder, hzqyStationMap);
    }

    /**
     * 初始化站点结果
     * @param area 站点区域
     * @param stationKind 站点类型
     * @param stationNumBuilder 站号列表
     * @param stationMap 站点信息集合
     */
    private void initStationResult(String area,String stationKind,StringBuilder stationNumBuilder, Map<String,StationInfo> stationMap){
        if(stationMap.size()>0){
            String stationNums=stationNumBuilder.toString();
            stationNums=stationNums.substring(0,stationNums.length()-1);
            Map<String,String> stationListMap=null;
            if(areaStationListMap.containsKey(area)){
                stationListMap=areaStationListMap.get(area);
            }else{
                stationListMap=new HashMap<String,String>();
                areaStationListMap.put(area,stationListMap);
            }
            if(!stationListMap.containsKey(stationKind)){
                stationListMap.put(stationKind,stationNums);
            }

            Map<String,Map<String,StationInfo>> stationInfoMap=null;
            if(areaStationInfoMap.containsKey(area)){
                stationInfoMap=areaStationInfoMap.get(area);
            }else{
                stationInfoMap=new HashMap<String,Map<String,StationInfo>>();
                areaStationInfoMap.put(area,stationInfoMap);
            }
            if(!stationInfoMap.containsKey(stationKind)){
                stationInfoMap.put(stationKind,stationMap);
            }
        }
    }

    /**
     * 添加湖北省市级站点信息
     * @param  stationKind 站点种类
     */
    private void addHBAreaInfo(String stationKind){
        Map<String,StationInfo> hbStationInfoMap=areaStationInfoMap.get("hb").get(stationKind);
        for(String stationNum:hbStationInfoMap.keySet()){
            StationInfo stationInfo=hbStationInfoMap.get(stationNum);
            String adminCode=stationInfo.getAdminCode();
            //某个站可能是某个县级市也可能属于地级市
            if(countAdminCodeList.contains(adminCode)){
                addAdminCodeStation(stationKind, adminCode, stationNum, stationInfo);
            }
            String city=stationInfo.getCity();
            for(String cityName:hbcityMap.keySet()){
                String cityAdminCode=hbcityMap.get(cityName);
                if(city.contains(cityName)){
                    addAdminCodeStation( stationKind, cityAdminCode, stationNum, stationInfo);
                    break;
                }
            }
        }
    }

    /**
     * 添加行政编码的站点信息
     * @param stationKind 站点种类
     * @param adminCode 行政编码
     * @param stationNum 站号
     * @param stationInfo 站点信息
     */
    private void addAdminCodeStation(String stationKind,String adminCode,String stationNum,StationInfo stationInfo){
        Map<String,Map<String,StationInfo>> areaStationInfoMapOfAdminCode=null;
        Map<String,String> areaStationNumsMapOfAdminCode=null;
        if(areaStationInfoMap.containsKey(adminCode)){
            areaStationInfoMapOfAdminCode=areaStationInfoMap.get(adminCode);
            areaStationNumsMapOfAdminCode=areaStationListMap.get(adminCode);
        }else{
            areaStationInfoMapOfAdminCode=new HashMap<String,Map<String,StationInfo>>();
            areaStationInfoMap.put(adminCode,areaStationInfoMapOfAdminCode);
            areaStationNumsMapOfAdminCode=new HashMap<String,String>();
            areaStationListMap.put(adminCode,areaStationNumsMapOfAdminCode);
        }
        Map<String,StationInfo> stationInfoMapOfAdminCode=null;
        if(areaStationInfoMapOfAdminCode.containsKey(stationKind)){
            stationInfoMapOfAdminCode=areaStationInfoMapOfAdminCode.get(stationKind);
        }else{
            stationInfoMapOfAdminCode=new HashMap<String,StationInfo>();
            areaStationInfoMapOfAdminCode.put(stationKind,stationInfoMapOfAdminCode);
        }
        stationInfoMapOfAdminCode.put(stationNum,stationInfo);
        String stationNums="";
        if(areaStationNumsMapOfAdminCode.containsKey(stationKind)){
            stationNums=areaStationNumsMapOfAdminCode.get(stationKind);
        }
        StringBuilder stationNumBuilder=new StringBuilder();
        if(stationNums.length()>0){
            stationNumBuilder.append(stationNums);
            stationNumBuilder.append(",");
        }
        stationNumBuilder.append(stationNum);
        areaStationNumsMapOfAdminCode.put(stationKind,stationNumBuilder.toString());
    }


}
