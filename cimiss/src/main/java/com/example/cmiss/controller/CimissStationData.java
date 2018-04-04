package com.example.cmiss.controller;


import cma.cimiss.client.DataQueryClient;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cmiss.config.*;
import com.example.cmiss.model.FeatureJson;
import com.example.cmiss.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * Created by dell on 2017/9/7.
 */
@Controller
public class CimissStationData extends AbstractController {

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 站点字段映射
     */
    private static Map<String, String> webToCIMISSStationFieldMap = new HashMap<String, String>();

    /**
     * 时间字段映射
     */
    private static Map<String, String> webToCIMISSTimeFieldMap = new HashMap<String, String>();

    /**
     * Main分钟字段映射
     */
    private static Map<String, String> webToCIMISSMainFieldMap = new HashMap<String, String>();


    /**
     * Other分钟字段映射
     */
    private static Map<String, String> webToCIMISSOtherFieldMap = new HashMap<String, String>();

    /**
     * 日数据字段映射
     */
    private static Map<String, String> webToCIMISSDayFieldMap = new HashMap<String, String>();

    /**
     * 只包含小时数据的字段映射：
     */
    private static Map<String, String> webToCIMISSHourFieldMap = new HashMap<String, String>();


    /**
     * 交通站数据的字段映射
     */
    private static Map<String, String> webToCIMISSTrafficFieldMap = new HashMap<String, String>();


    /**
     * 1分钟数据的字段映射
     */
    private static Map<String, String> webToCIMISSOneMinFieldMap = new HashMap<String, String>();


    Properties prop = PropertiesUtils.loadProperties("classpath:yth.properties");
    public Properties prophb = PropertiesUtils
            .loadProperties("classpath:hbbounds.properties");
    public Properties proply = PropertiesUtils
            .loadProperties("classpath:lybounds.properties");
    public Properties prophz = PropertiesUtils
            .loadProperties("classpath:hzbounds.properties");

    /*
    * 1 与Main分钟字段和Other分钟字段存在重叠要素，表示该要素既有分钟数据又有小时数据;（当查询时间为整时的时候，只能查CIMISS小时资料代码）
    * 2 小时字段中存在但是分钟字段不存在，表明该要素只有小时数据
    * 3 小时字段中不存在但是分钟字段存在，表明该要素只有分钟数据
    */


    static {
        webToCIMISSMainFieldMap.put("STATIONNUM", "Station_Id_C");
        webToCIMISSHourFieldMap.put("STATIONNUM", "Station_Id_C");
        webToCIMISSOtherFieldMap.put("STATIONNUM", "Station_Id_C");
        webToCIMISSDayFieldMap.put("STATIONNUM", "Station_Id_C");
        webToCIMISSTrafficFieldMap.put("STATIONNUM", "Station_Id_C");
        webToCIMISSOneMinFieldMap.put("STATIONNUM", "Station_Id_C");
        webToCIMISSOneMinFieldMap.put("PRE_1H", "PRE");
        webToCIMISSOneMinFieldMap.put("Q_PRE_1H", "Q_PRE");

        webToCIMISSStationFieldMap.put("STATIONNAME", "Station_Name");
        webToCIMISSStationFieldMap.put("LATITUDE", "Lat");
        webToCIMISSStationFieldMap.put("LONGITUDE", "Lon");
        webToCIMISSStationFieldMap.put("ALTITUDE", "Alti");
        webToCIMISSStationFieldMap.put("PROVINCE", "Province");
        webToCIMISSStationFieldMap.put("CITY", "City");
        webToCIMISSStationFieldMap.put("COUNTY", "Cnty");
        webToCIMISSStationFieldMap.put("STATIONLEVL", "Station_Type");
        webToCIMISSStationFieldMap.put("STATIONTYPE", "Station_levl");
        webToCIMISSStationFieldMap.put("ADMINCODE", "Admin_Code_CHN");


        webToCIMISSTimeFieldMap.put("YEAR", "Year");
        webToCIMISSTimeFieldMap.put("MONTH", "Mon");
        webToCIMISSTimeFieldMap.put("DAY", "Day");
        webToCIMISSTimeFieldMap.put("HOUR", "Hour");
        webToCIMISSTimeFieldMap.put("MINUTE", "Min");
        webToCIMISSTimeFieldMap.put("OBSERVTIMES", "Datetime");

        /**
         * 气压相关
         */
        webToCIMISSOtherFieldMap.put("PRS_MAX", "PRS_Max");

        webToCIMISSOtherFieldMap.put("Q_PRS_MAX", "Q_PRS_Max");

        webToCIMISSOtherFieldMap.put("PRS_MAX_OTIME", "PRS_Max_OTime");

        webToCIMISSOtherFieldMap.put("Q_PRS_MAX_OTIME", "Q_PRS_Max_OTime");

        webToCIMISSOtherFieldMap.put("PRS_MIN", "PRS_Min");

        webToCIMISSOtherFieldMap.put("Q_PRS_MIN", "Q_PRS_Min");

        webToCIMISSOtherFieldMap.put("PRS_MIN_OTIME", "PRS_Min_OTime");

        webToCIMISSOtherFieldMap.put("Q_PRS_MIN_OTIME", "Q_PRS_Min_OTime");


        webToCIMISSOneMinFieldMap.put("PRS_MAX", "PRS_Max");

        webToCIMISSOneMinFieldMap.put("Q_PRS_MAX", "Q_PRS_Max");

        webToCIMISSOneMinFieldMap.put("PRS_MAX_OTIME", "PRS_Max_OTime");

        webToCIMISSOneMinFieldMap.put("Q_PRS_MAX_OTIME", "Q_PRS_Max_OTime");

        webToCIMISSOneMinFieldMap.put("PRS_MIN", "PRS_Min");

        webToCIMISSOneMinFieldMap.put("Q_PRS_MIN", "Q_PRS_Min");

        webToCIMISSOneMinFieldMap.put("PRS_MIN_OTIME", "PRS_Min_OTime");

        webToCIMISSOneMinFieldMap.put("Q_PRS_MIN_OTIME", "Q_PRS_Min_OTime");


        //水气压
        webToCIMISSOtherFieldMap.put("VAPOURPRESS", "VAP");
        webToCIMISSOtherFieldMap.put("Q_VAPOURPRESS", "Q_VAP");

        webToCIMISSTrafficFieldMap.put("VAPOURPRESS", "VAP");
        webToCIMISSTrafficFieldMap.put("Q_VAPOURPRESS", "Q_VAP");

        webToCIMISSOneMinFieldMap.put("VAPOURPRESS", "VAP");
        webToCIMISSOneMinFieldMap.put("Q_VAPOURPRESS", "Q_VAP");


        webToCIMISSOneMinFieldMap.put("PRS_SEA", "PRS_Sea");
        webToCIMISSOneMinFieldMap.put("Q_PRS_SEA", "Q_PRS_Sea");

        webToCIMISSOneMinFieldMap.put("STATIONPRESS", "PRS");
        webToCIMISSOneMinFieldMap.put("Q_PRS", "Q_PRS");

        webToCIMISSHourFieldMap.put("PRS_SEA", "PRS_Sea");
        webToCIMISSHourFieldMap.put("Q_PRS_SEA", "Q_PRS_Sea");

        webToCIMISSHourFieldMap.put("PRS_CHANGE_3H", "PRS_Change_3h");
        webToCIMISSHourFieldMap.put("Q_PRS_CHANGE_3H", "Q_PRS_Change_3h");

        webToCIMISSHourFieldMap.put("PRS_CHANGE_24H", "PRS_Change_24h");
        webToCIMISSHourFieldMap.put("Q_PRS_CHANGE_24H", "Q_PRS_Change_24h");

        webToCIMISSMainFieldMap.put("STATIONPRESS", "PRS");
        webToCIMISSMainFieldMap.put("Q_PRS", "Q_PRS");


        /**
         * 气温
         */
        webToCIMISSMainFieldMap.put("TEMP", "TEM");
        webToCIMISSMainFieldMap.put("Q_TEM", "Q_TEM");

        webToCIMISSTrafficFieldMap.put("TEMP", "TEM");
        webToCIMISSTrafficFieldMap.put("Q_TEM", "Q_TEM");

        webToCIMISSOneMinFieldMap.put("TEMP", "TEM");
        webToCIMISSOneMinFieldMap.put("Q_TEM", "Q_TEM");


        webToCIMISSOtherFieldMap.put("MAXTEMP", "TEM_Max");
        webToCIMISSOtherFieldMap.put("Q_MAXTEMP", "Q_TEM_Max");

        webToCIMISSOtherFieldMap.put("TIMEMAXTEMP", "TEM_Max_OTime");
        webToCIMISSOtherFieldMap.put("Q_TIMEMAXTEMP", "Q_TEM_Max_OTime");

        webToCIMISSOtherFieldMap.put("MINTEMP", "TEM_Min");
        webToCIMISSOtherFieldMap.put("Q_MINTEMP", "Q_TEM_Min");

        webToCIMISSOtherFieldMap.put("TIMEMINTEMP", "TEM_Min_OTime");
        webToCIMISSOtherFieldMap.put("Q_TIMEMINTEMP", "Q_TEM_Min_OTime");


        webToCIMISSTrafficFieldMap.put("MAXTEMP", "TEM_Max");
        webToCIMISSTrafficFieldMap.put("Q_MAXTEMP", "Q_TEM_Max");

        webToCIMISSTrafficFieldMap.put("TIMEMAXTEMP", "TEM_Max_OTime");
        webToCIMISSTrafficFieldMap.put("Q_TIMEMAXTEMP", "Q_TEM_Max_OTime");

        webToCIMISSTrafficFieldMap.put("MINTEMP", "TEM_Min");
        webToCIMISSTrafficFieldMap.put("Q_MINTEMP", "Q_TEM_Min");

        webToCIMISSTrafficFieldMap.put("TIMEMINTEMP", "TEM_Min_OTime");
        webToCIMISSTrafficFieldMap.put("Q_TIMEMINTEMP", "Q_TEM_Min_OTime");


        webToCIMISSOneMinFieldMap.put("MAXTEMP", "TEM_Max");
        webToCIMISSOneMinFieldMap.put("Q_MAXTEMP", "Q_TEM_Max");

        webToCIMISSOneMinFieldMap.put("TIMEMAXTEMP", "TEM_Max_OTime");
        webToCIMISSOneMinFieldMap.put("Q_TIMEMAXTEMP", "Q_TEM_Max_OTime");

        webToCIMISSOneMinFieldMap.put("MINTEMP", "TEM_Min");
        webToCIMISSOneMinFieldMap.put("Q_MINTEMP", "Q_TEM_Min");

        webToCIMISSOneMinFieldMap.put("TIMEMINTEMP", "TEM_Min_OTime");
        webToCIMISSOneMinFieldMap.put("Q_TIMEMINTEMP", "Q_TEM_Min_OTime");

        webToCIMISSOtherFieldMap.put("DEWTEMP", "DPT");
        webToCIMISSOtherFieldMap.put("Q_DEWTEMP", "Q_DPT");

        webToCIMISSTrafficFieldMap.put("DEWTEMP", "DPT");
        webToCIMISSTrafficFieldMap.put("Q_DEWTEMP", "Q_DPT");

        webToCIMISSOneMinFieldMap.put("DEWTEMP", "DPT");
        webToCIMISSOneMinFieldMap.put("Q_DEWTEMP", "Q_DPT");

        webToCIMISSHourFieldMap.put("CHANGETEMP24", "TEM_ChANGE_24h");
        webToCIMISSHourFieldMap.put("Q_CHANGETEMP24", "Q_TEM_ChANGE_24h");

        webToCIMISSHourFieldMap.put("MAXTEMP24", "TEM_Max_24h");
        webToCIMISSHourFieldMap.put("Q_MAXTEMP24", "Q_TEM_Max_24h");

        webToCIMISSHourFieldMap.put("MINTEMP24", "TEM_Min_24h");
        webToCIMISSHourFieldMap.put("Q_MINTEMP24", "Q_TEM_Min_24h");

        /**
         * 相对湿度
         */
        webToCIMISSMainFieldMap.put("RELHUMIDITY", "RHU");
        webToCIMISSMainFieldMap.put("Q_RELHUMIDITY", "Q_RHU");

        webToCIMISSTrafficFieldMap.put("RELHUMIDITY", "RHU");
        webToCIMISSTrafficFieldMap.put("Q_RELHUMIDITY", "Q_RHU");


        webToCIMISSOneMinFieldMap.put("RELHUMIDITY", "RHU");
        webToCIMISSOneMinFieldMap.put("Q_RELHUMIDITY", "Q_RHU");

        webToCIMISSOneMinFieldMap.put("MINRELHUMIDITY", "RHU_Min");
        webToCIMISSOneMinFieldMap.put("Q_MINRELHUMIDITY", "Q_RHU_Min");

        webToCIMISSOneMinFieldMap.put("TIMEMINRELH", "RHU_Min_OTIME");
        webToCIMISSOneMinFieldMap.put("Q_TIMEMINRELH", "Q_RHU_Min_OTIME");


        webToCIMISSOtherFieldMap.put("MINRELHUMIDITY", "RHU_Min");
        webToCIMISSOtherFieldMap.put("Q_MINRELHUMIDITY", "Q_RHU_Min");

        webToCIMISSOtherFieldMap.put("TIMEMINRELH", "RHU_Min_OTIME");
        webToCIMISSOtherFieldMap.put("Q_TIMEMINRELH", "Q_RHU_Min_OTIME");


        webToCIMISSTrafficFieldMap.put("MINRELHUMIDITY", "RHU_Min");
        webToCIMISSTrafficFieldMap.put("Q_MINRELHUMIDITY", "Q_RHU_Min");

        webToCIMISSTrafficFieldMap.put("TIMEMINRELH", "RHU_Min_OTIME");
        webToCIMISSTrafficFieldMap.put("Q_TIMEMINRELH", "Q_RHU_Min_OTIME");


        /**
         * 降水
         */
        webToCIMISSOtherFieldMap.put("PRE_1H", "PRE_1h");
        webToCIMISSOtherFieldMap.put("Q_PRE_1H", "Q_PRE_1h");

        webToCIMISSTrafficFieldMap.put("PRE_1H", "PRE_1h");
        webToCIMISSTrafficFieldMap.put("Q_PRE_1H", "Q_PRE_1h");

        webToCIMISSOneMinFieldMap.put("PRE_1H", "PRE");
        webToCIMISSOneMinFieldMap.put("Q_PRE_1H", "Q_PRE");


        webToCIMISSHourFieldMap.put("PRE_3H", "PRE_3h");
        webToCIMISSHourFieldMap.put("Q_PRE_3H", "Q_PRE_3h");

        webToCIMISSHourFieldMap.put("PRE_6H", "PRE_6h");
        webToCIMISSHourFieldMap.put("Q_PRE_6H", "Q_PRE_6h");

        webToCIMISSHourFieldMap.put("PRE_12H", "PRE_12h");
        webToCIMISSHourFieldMap.put("Q_PRE_12H", "Q_PRE_12h");

        webToCIMISSHourFieldMap.put("PRE_24H", "PRE_24h");
        webToCIMISSHourFieldMap.put("Q_PRE_24H", "Q_PRE_24h");

        webToCIMISSHourFieldMap.put("PRE_0808", "PRE_Time_0808");


        //蒸发(大型)
        webToCIMISSOtherFieldMap.put("EVP_BIG", "EVP_Big");
        webToCIMISSOtherFieldMap.put("Q_EVP_BIG", "Q_EVP_Big");

        webToCIMISSOneMinFieldMap.put("EVP_BIG", "EVP_Big");
        webToCIMISSOneMinFieldMap.put("Q_EVP_BIG", "Q_EVP_Big");


        /**
         * 风
         */
        webToCIMISSMainFieldMap.put("WINDDIRECT1", "WIN_D_Avg_1mi");
        webToCIMISSMainFieldMap.put("Q_WINDDIRECT1", "Q_WIN_D_Avg_1mi");

        webToCIMISSMainFieldMap.put("WINDVELOCITY1", "WIN_S_Avg_1mi");
        webToCIMISSMainFieldMap.put("Q_WINDVELOCITY1", "Q_WIN_S_Avg_1mi");

        webToCIMISSOtherFieldMap.put("WINDDIRECT2", "WIN_D_Avg_2mi");
        webToCIMISSOtherFieldMap.put("Q_WINDDIRECT2", "Q_WIN_D_Avg_2mi");

        webToCIMISSOtherFieldMap.put("WINDVELOCITY2", "WIN_S_Avg_2mi");
        webToCIMISSOtherFieldMap.put("Q_WINDVELOCITY2", "Q_WIN_S_Avg_2mi");

        webToCIMISSOtherFieldMap.put("WINDDIRECT10", "WIN_D_Avg_10mi");
        webToCIMISSOtherFieldMap.put("Q_WINDDIRECT10", "Q_WIN_D_Avg_10mi");

        webToCIMISSOtherFieldMap.put("WINDVELOCITY10", "WIN_S_Avg_10mi");
        webToCIMISSOtherFieldMap.put("Q_WINDVELOCITY10", "Q_WIN_S_Avg_10mi");

        webToCIMISSOtherFieldMap.put("MAXWINDD10", "WIN_D_S_Max");
        webToCIMISSOtherFieldMap.put("Q_MAXWINDD10", "Q_WIN_D_S_Max");

        webToCIMISSOtherFieldMap.put("MAXWINDV10", "WIN_S_Max");
        webToCIMISSOtherFieldMap.put("Q_MAXWINDV10", "Q_WIN_S_Max");

        webToCIMISSOtherFieldMap.put("TIMEMAXWIND10", "WIN_S_Max_OTime");
        webToCIMISSOtherFieldMap.put("Q_TIMEMAXWIND10", "Q_WIN_S_Max_OTime");


        webToCIMISSOneMinFieldMap.put("WINDDIRECT1", "WIN_D_Avg_1mi");
        webToCIMISSOneMinFieldMap.put("Q_WINDDIRECT1", "Q_WIN_D_Avg_1mi");

        webToCIMISSOneMinFieldMap.put("WINDVELOCITY1", "WIN_S_Avg_1mi");
        webToCIMISSOneMinFieldMap.put("Q_WINDVELOCITY1", "Q_WIN_S_Avg_1mi");

        webToCIMISSOneMinFieldMap.put("WINDDIRECT2", "WIN_D_Avg_2mi");
        webToCIMISSOneMinFieldMap.put("Q_WINDDIRECT2", "Q_WIN_D_Avg_2mi");


        webToCIMISSOneMinFieldMap.put("WINDVELOCITY2", "WIN_S_Avg_2mi");
        webToCIMISSOneMinFieldMap.put("Q_WINDVELOCITY2", "Q_WIN_S_Avg_2mi");

        webToCIMISSOneMinFieldMap.put("WINDDIRECT10", "WIN_D_Avg_10mi");
        webToCIMISSOneMinFieldMap.put("Q_WINDDIRECT10", "Q_WIN_D_Avg_10mi");

        webToCIMISSOneMinFieldMap.put("WINDVELOCITY10", "WIN_S_Avg_10mi");
        webToCIMISSOneMinFieldMap.put("Q_WINDVELOCITY10", "Q_WIN_S_Avg_10mi");

        webToCIMISSOneMinFieldMap.put("MAXWINDD10", "WIN_D_S_Max");
        webToCIMISSOneMinFieldMap.put("Q_MAXWINDD10", "Q_WIN_D_S_Max");

        webToCIMISSOneMinFieldMap.put("MAXWINDV10", "WIN_S_Max");
        webToCIMISSOneMinFieldMap.put("Q_MAXWINDV10", "Q_WIN_S_Max");

        webToCIMISSOneMinFieldMap.put("TIMEMAXWIND10", "WIN_S_Max_OTime");
        webToCIMISSOneMinFieldMap.put("Q_TIMEMAXWIND10", "Q_WIN_S_Max_OTime");


        webToCIMISSTrafficFieldMap.put("WINDVELOCITY2", "WIN_S_Avg_2mi");
        webToCIMISSTrafficFieldMap.put("Q_WINDVELOCITY2", "Q_WIN_S_Avg_2mi");

        webToCIMISSTrafficFieldMap.put("WINDDIRECT10", "WIN_D_Avg_10mi");
        webToCIMISSTrafficFieldMap.put("Q_WINDDIRECT10", "Q_WIN_D_Avg_10mi");

        webToCIMISSTrafficFieldMap.put("WINDVELOCITY10", "WIN_S_Avg_10mi");
        webToCIMISSTrafficFieldMap.put("Q_WINDVELOCITY10", "Q_WIN_S_Avg_10mi");

        webToCIMISSTrafficFieldMap.put("MAXWINDD10", "WIN_D_S_Max");
        webToCIMISSTrafficFieldMap.put("Q_MAXWINDD10", "Q_WIN_D_S_Max");

        webToCIMISSTrafficFieldMap.put("MAXWINDV10", "WIN_S_Max");
        webToCIMISSTrafficFieldMap.put("Q_MAXWINDV10", "Q_WIN_S_Max");

        webToCIMISSTrafficFieldMap.put("TIMEMAXWIND10", "WIN_S_Max_OTime");
        webToCIMISSTrafficFieldMap.put("Q_TIMEMAXWIND10", "Q_WIN_S_Max_OTime");


        webToCIMISSOtherFieldMap.put("INSTANTWINDD", "WIN_D_INST");
        webToCIMISSOtherFieldMap.put("Q_INSTANTWINDD", "Q_WIN_D_INST");

        webToCIMISSOtherFieldMap.put("INSTANTWINDV", "WIN_S_INST");
        webToCIMISSOtherFieldMap.put("Q_INSTANTWINDV", "Q_WIN_S_INST");


        webToCIMISSOtherFieldMap.put("EXMAXWINDD", "WIN_D_INST_Max");
        webToCIMISSOtherFieldMap.put("Q_EXMAXWINDD", "Q_WIN_D_INST_Max");

        webToCIMISSOtherFieldMap.put("EXMAXWINDV", "WIN_S_Inst_Max");
        webToCIMISSOtherFieldMap.put("Q_EXMAXWINDV", "Q_WIN_S_Inst_Max");

        webToCIMISSOtherFieldMap.put("TIMEEXMAXWINDV", "WIN_S_INST_Max_OTime");
        webToCIMISSOtherFieldMap.put("Q_TIMEEXMAXWINDV", "Q_WIN_S_INST_Max_OTime");


        webToCIMISSOneMinFieldMap.put("INSTANTWINDD", "WIN_D_INST");
        webToCIMISSOneMinFieldMap.put("Q_INSTANTWINDD", "Q_WIN_D_INST");

        webToCIMISSOneMinFieldMap.put("INSTANTWINDV", "WIN_S_INST");
        webToCIMISSOneMinFieldMap.put("Q_INSTANTWINDV", "Q_WIN_S_INST");


        webToCIMISSOneMinFieldMap.put("EXMAXWINDD", "WIN_D_INST_Max");
        webToCIMISSOneMinFieldMap.put("Q_EXMAXWINDD", "Q_WIN_D_INST_Max");

        webToCIMISSOneMinFieldMap.put("EXMAXWINDV", "WIN_S_Inst_Max");
        webToCIMISSOneMinFieldMap.put("Q_EXMAXWINDV", "Q_WIN_S_Inst_Max");

        webToCIMISSOneMinFieldMap.put("TIMEEXMAXWINDV", "WIN_S_INST_Max_OTime");
        webToCIMISSOneMinFieldMap.put("Q_TIMEEXMAXWINDV", "Q_WIN_S_INST_Max_OTime");

        webToCIMISSTrafficFieldMap.put("EXMAXWINDD", "WIN_D_INST_Max");
        webToCIMISSTrafficFieldMap.put("Q_EXMAXWINDD", "Q_WIN_D_INST_Max");

        webToCIMISSTrafficFieldMap.put("EXMAXWINDV", "WIN_S_Inst_Max");
        webToCIMISSTrafficFieldMap.put("Q_EXMAXWINDV", "Q_WIN_S_Inst_Max");

        webToCIMISSTrafficFieldMap.put("TIMEEXMAXWINDV", "WIN_S_INST_Max_OTime");
        webToCIMISSTrafficFieldMap.put("Q_TIMEEXMAXWINDV", "Q_WIN_S_INST_Max_OTime");


        webToCIMISSHourFieldMap.put("WIN_D_INST_MAX_6H", "WIN_D_Inst_Max_6h");
        webToCIMISSHourFieldMap.put("Q_WIN_D_INST_MAX_6H", "Q_WIN_D_Inst_Max_6h");

        webToCIMISSHourFieldMap.put("WIN_S_INST_MAX_6H", "WIN_S_Inst_Max_6h");
        webToCIMISSHourFieldMap.put("Q_WIN_S_INST_MAX_6H", "Q_WIN_S_Inst_Max_6h");

        webToCIMISSHourFieldMap.put("WIN_D_INST_MAX_12H", "WIN_D_Inst_Max_12h");
        webToCIMISSHourFieldMap.put("Q_WIN_D_INST_MAX_12H", "Q_WIN_D_Inst_Max_12h");

        webToCIMISSHourFieldMap.put("WIN_S_INST_MAX_12H", "WIN_S_Inst_Max_12h");
        webToCIMISSHourFieldMap.put("Q_WIN_S_INST_MAX_12H", "Q_WIN_S_Inst_Max_12h");


        /**
         * 地面温度
         */
        webToCIMISSMainFieldMap.put("GST", "GST");
        webToCIMISSMainFieldMap.put("Q_GST", "Q_GST");

        webToCIMISSOtherFieldMap.put("GST_MAX", "GST_Max");
        webToCIMISSOtherFieldMap.put("Q_GST_MAX", "Q_GST_Max");

        webToCIMISSOtherFieldMap.put("GST_MAX_OTIME", "GST_Max_Otime");
        webToCIMISSOtherFieldMap.put("Q_GST_MAX_OTIME", "Q_GST_Max_Otime");

        webToCIMISSOtherFieldMap.put("GST_MIN", "GST_Min");
        webToCIMISSOtherFieldMap.put("Q_GST_MIN", "Q_GST_Min");

        webToCIMISSOtherFieldMap.put("GST_MIN_OTIME", "GST_Min_OTime");
        webToCIMISSOtherFieldMap.put("Q_GST_MIN_OTIME", "Q_GST_Min_OTime");

        webToCIMISSMainFieldMap.put("GST_5CM", "GST_5cm");
        webToCIMISSMainFieldMap.put("Q_GST_5CM", "Q_GST_5cm");

        webToCIMISSMainFieldMap.put("GST_10CM", "GST_10cm");
        webToCIMISSMainFieldMap.put("Q_GST_10CM", "Q_GST_10cm");

        webToCIMISSMainFieldMap.put("GST_15CM", "GST_15cm");
        webToCIMISSMainFieldMap.put("Q_GST_15CM", "Q_GST_15cm");

        webToCIMISSMainFieldMap.put("GST_20CM", "GST_20cm");
        webToCIMISSMainFieldMap.put("Q_GST_20CM", "Q_GST_20cm");

        webToCIMISSMainFieldMap.put("GST_40CM", "GST_40Cm");
        webToCIMISSMainFieldMap.put("Q_GST_40CM", "Q_GST_40Cm");

        webToCIMISSOtherFieldMap.put("GST_80CM", "GST_80cm");
        webToCIMISSOtherFieldMap.put("Q_GST_80CM", "Q_GST_80cm");

        webToCIMISSOtherFieldMap.put("GST_160CM", "GST_160cm");
        webToCIMISSOtherFieldMap.put("Q_GST_160CM", "Q_GST_160cm");

        webToCIMISSOtherFieldMap.put("GST_320CM", "GST_320cm");
        webToCIMISSOtherFieldMap.put("Q_GST_320CM", "Q_GST_320cm");

        webToCIMISSHourFieldMap.put("GST_MIN_12H", "GST_Min_12h");
        webToCIMISSHourFieldMap.put("Q_GST_MIN_12H", "Q_GST_Min_12h");


        webToCIMISSOneMinFieldMap.put("GST", "GST");
        webToCIMISSOneMinFieldMap.put("Q_GST", "Q_GST");

        webToCIMISSOneMinFieldMap.put("GST_MAX", "GST_Max");
        webToCIMISSOneMinFieldMap.put("Q_GST_MAX", "Q_GST_Max");

        webToCIMISSOneMinFieldMap.put("GST_MAX_OTIME", "GST_Max_Otime");
        webToCIMISSOneMinFieldMap.put("Q_GST_MAX_OTIME", "Q_GST_Max_Otime");

        webToCIMISSOneMinFieldMap.put("GST_MIN", "GST_Min");
        webToCIMISSOneMinFieldMap.put("Q_GST_MIN", "Q_GST_Min");

        webToCIMISSOneMinFieldMap.put("GST_MIN_OTIME", "GST_Min_OTime");
        webToCIMISSOneMinFieldMap.put("Q_GST_MIN_OTIME", "Q_GST_Min_OTime");

        webToCIMISSOneMinFieldMap.put("GST_5CM", "GST_5cm");
        webToCIMISSOneMinFieldMap.put("Q_GST_5CM", "Q_GST_5cm");

        webToCIMISSOneMinFieldMap.put("GST_10CM", "GST_10cm");
        webToCIMISSOneMinFieldMap.put("Q_GST_10CM", "Q_GST_10cm");

        webToCIMISSOneMinFieldMap.put("GST_15CM", "GST_15cm");
        webToCIMISSOneMinFieldMap.put("Q_GST_15CM", "Q_GST_15cm");

        webToCIMISSOneMinFieldMap.put("GST_20CM", "GST_20cm");
        webToCIMISSOneMinFieldMap.put("Q_GST_20CM", "Q_GST_20cm");

        webToCIMISSOneMinFieldMap.put("GST_40CM", "GST_40Cm");
        webToCIMISSOneMinFieldMap.put("Q_GST_40CM", "Q_GST_40Cm");


        webToCIMISSOneMinFieldMap.put("GST_80CM", "GST_80cm");
        webToCIMISSOneMinFieldMap.put("Q_GST_80CM", "Q_GST_80cm");

        webToCIMISSOneMinFieldMap.put("GST_160CM", "GST_160cm");
        webToCIMISSOneMinFieldMap.put("Q_GST_160CM", "Q_GST_160cm");

        webToCIMISSOneMinFieldMap.put("GST_320CM", "GST_320cm");
        webToCIMISSOneMinFieldMap.put("Q_GST_320CM", "Q_GST_320cm");


        /**
         * 草面(雪面)温度
         */
        webToCIMISSMainFieldMap.put("LGST", "LGST");
        webToCIMISSMainFieldMap.put("Q_LGST", "Q_LGST");

        webToCIMISSOtherFieldMap.put("LGST_MAX", "LGST_Max");
        webToCIMISSOtherFieldMap.put("Q_LGST_MAX", "Q_LGST_Max");

        webToCIMISSOtherFieldMap.put("LGST_MAX_OTIME", "LGST_Max_OTime");
        webToCIMISSOtherFieldMap.put("Q_LGST_MAX_OTIME", "Q_LGST_Max_OTime");

        webToCIMISSOtherFieldMap.put("LGST_MIN", "LGST_Min");
        webToCIMISSOtherFieldMap.put("Q_LGST_MIN", "Q_LGST_Min");

        webToCIMISSOtherFieldMap.put("LGST_MIN_OTIME", "LGST_Min_OTime");
        webToCIMISSOtherFieldMap.put("Q_LGST_MIN_OTIME", "Q_LGST_Min_OTime");


        webToCIMISSOneMinFieldMap.put("LGST", "LGST");
        webToCIMISSOneMinFieldMap.put("Q_LGST", "Q_LGST");

        webToCIMISSOneMinFieldMap.put("LGST_MAX", "LGST_Max");
        webToCIMISSOneMinFieldMap.put("Q_LGST_MAX", "Q_LGST_Max");

        webToCIMISSOneMinFieldMap.put("LGST_MAX_OTIME", "LGST_Max_OTime");
        webToCIMISSOneMinFieldMap.put("Q_LGST_MAX_OTIME", "Q_LGST_Max_OTime");

        webToCIMISSOneMinFieldMap.put("LGST_MIN", "LGST_Min");
        webToCIMISSOneMinFieldMap.put("Q_LGST_MIN", "Q_LGST_Min");

        webToCIMISSOneMinFieldMap.put("LGST_MIN_OTIME", "LGST_Min_OTime");
        webToCIMISSOneMinFieldMap.put("Q_LGST_MIN_OTIME", "Q_LGST_Min_OTime");

        /**
         * 能见度
         */
        //水平能见度(人工)
        webToCIMISSHourFieldMap.put("VISIBILITY", "VIS");
        webToCIMISSHourFieldMap.put("Q_VISIBILITY", "Q_VIS");

        webToCIMISSOtherFieldMap.put("VISIBILITY1", "VIS_HOR_1MI");
        webToCIMISSOtherFieldMap.put("Q_VISIBILITY1", "Q_VIS_HOR_1MI");

        webToCIMISSTrafficFieldMap.put("VISIBILITY1", "VIS_HOR_1MI");
        webToCIMISSTrafficFieldMap.put("Q_VISIBILITY1", "Q_VIS_HOR_1MI");

        webToCIMISSOtherFieldMap.put("VISIBILITY10", "VIS_HOR_10MI");
        webToCIMISSOtherFieldMap.put("Q_VISIBILITY10", "Q_VIS_HOR_10MI");

        webToCIMISSOtherFieldMap.put("MINVIS", "VIS_Min");
        webToCIMISSOtherFieldMap.put("Q_MINVIS", "Q_VIS_Min");

        webToCIMISSOtherFieldMap.put("TIMEMINVIS", "VIS_Min_OTime");
        webToCIMISSOtherFieldMap.put("Q_TIMEMINVIS", "Q_VIS_Min_OTime");

        webToCIMISSTrafficFieldMap.put("MINVIS", "VIS_Min");
        webToCIMISSTrafficFieldMap.put("Q_MINVIS", "Q_VIS_Min");

        webToCIMISSTrafficFieldMap.put("TIMEMINVIS", "VIS_Min_OTime");
        webToCIMISSTrafficFieldMap.put("Q_TIMEMINVIS", "Q_VIS_Min_OTime");


        webToCIMISSOneMinFieldMap.put("VISIBILITY1", "VIS_HOR_1MI");
        webToCIMISSOneMinFieldMap.put("Q_VISIBILITY1", "Q_VIS_HOR_1MI");

        webToCIMISSOneMinFieldMap.put("VISIBILITY10", "VIS_HOR_10MI");
        webToCIMISSOneMinFieldMap.put("Q_VISIBILITY10", "Q_VIS_HOR_10MI");


        webToCIMISSOneMinFieldMap.put("MINVIS", "VIS_Min");
        webToCIMISSOneMinFieldMap.put("Q_MINVIS", "Q_VIS_Min");

        webToCIMISSOneMinFieldMap.put("TIMEMINVIS", "VIS_Min_OTime");
        webToCIMISSOneMinFieldMap.put("Q_TIMEMINVIS", "Q_VIS_Min_OTime");


        /**
         * 云数据
         */
        webToCIMISSHourFieldMap.put("TOTALCLOUD", "CLO_Cov");
        webToCIMISSHourFieldMap.put("Q_TOTALCLOUD", "Q_CLO_Cov");

        webToCIMISSHourFieldMap.put("LOWCLOUD", "CLO_Cov_Low");
        webToCIMISSHourFieldMap.put("Q_LOWCLOUD", "Q_CLO_Cov_Low");

        webToCIMISSHourFieldMap.put("CLO_COV_LM", "CLO_COV_LM");
        webToCIMISSHourFieldMap.put("Q_CLO_COV_LM", "Q_CLO_COV_LM");

        webToCIMISSHourFieldMap.put("CLO_HEIGHT_LOM", "CLO_Height_LoM");
        webToCIMISSHourFieldMap.put("Q_CLO_HEIGHT_LOM", "Q_CLO_Height_LoM");

        webToCIMISSHourFieldMap.put("CLO_FOME_LOW", "CLO_Fome_Low");
        webToCIMISSHourFieldMap.put("Q_CLO_FOME_LOW", "Q_CLO_Fome_Low");


        webToCIMISSHourFieldMap.put("CLO_FOME_MID", "CLO_FOME_MID");

        webToCIMISSOneMinFieldMap.put("TOTALCLOUD", "CLO_Cov");
        webToCIMISSOneMinFieldMap.put("Q_TOTALCLOUD", "Q_CLO_Cov");

        webToCIMISSOneMinFieldMap.put("CLO_HEIGHT_LOM", "CLO_Height_LoM");
        webToCIMISSOneMinFieldMap.put("Q_CLO_HEIGHT_LOM", "Q_CLO_Height_LoM");


        /**
         * 新增
         */
        webToCIMISSHourFieldMap.put("Q_CLO_FOME_MID", "Q_CLO_FOME_MID");

        webToCIMISSHourFieldMap.put("CLO_FOME_HIGH", "CLO_Fome_High");
        /**
         * 新增
         */
        webToCIMISSHourFieldMap.put("Q_CLO_FOME_HIGH", "Q_CLO_Fome_High");

        //现在天气
        webToCIMISSHourFieldMap.put("WEP_NOW", "WEP_Now");
        webToCIMISSHourFieldMap.put("Q_WEP_NOW", "Q_WEP_Now");

        //积雪深度(厘米)
        webToCIMISSHourFieldMap.put("SNOW_DEPTH", "Snow_Depth");
        webToCIMISSHourFieldMap.put("Q_SNOW_DEPTH", "Q_Snow_Depth");

        webToCIMISSTrafficFieldMap.put("SNOW_DEPTH", "Snow_Depth");
        webToCIMISSTrafficFieldMap.put("Q_SNOW_DEPTH", "Q_Snow_Depth");

        webToCIMISSOneMinFieldMap.put("SNOW_DEPTH", "Snow_Depth");
        webToCIMISSOneMinFieldMap.put("Q_SNOW_DEPTH", "Q_Snow_Depth");

        //电线积冰直径
        webToCIMISSHourFieldMap.put("EICED", "EICED");
        webToCIMISSHourFieldMap.put("Q_EICED", "Q_EICED");

        /**
         * 日数据
         */
       /*  webToCIMISSDayFieldMap.put("PRE_0808","PRE_Time_0808");
       webToCIMISSDayFieldMap.put("PRE_0820","PRE_Time_0820");
       webToCIMISSDayFieldMap.put("PRE_2020","PRE_Time_2020");
        webToCIMISSDayFieldMap.put("PRE_2008","PRE_Time_2008");*/
//
//        webToCIMISSDayFieldMap.put("MAXWINDD10_0808",);
//        webToCIMISSDayFieldMap.put("MAXWINDV10_0808",);
//        webToCIMISSDayFieldMap.put("TIMEMAXWIND10_0808",);
//
//        webToCIMISSDayFieldMap.put("MAXWINDD10_2020",);
//        webToCIMISSDayFieldMap.put("MAXWINDV10_2020",);
//        webToCIMISSDayFieldMap.put("TIMEMAXWIND10_2020",);
//
//        webToCIMISSDayFieldMap.put("EXMAXWINDD_0808",);
//        webToCIMISSDayFieldMap.put("EXMAXWINDV_0808",);
//        webToCIMISSDayFieldMap.put("TIMEEXMAXWINDV_0808",);
//
//        webToCIMISSDayFieldMap.put("EXMAXWINDD_2020",);
//        webToCIMISSDayFieldMap.put("EXMAXWINDV_2020",);
//        webToCIMISSDayFieldMap.put("TIMEEXMAXWINDV_2020",);
//
//
//        webToCIMISSDayFieldMap.put("MAXPRE_0808",);
//        webToCIMISSDayFieldMap.put("TIMEMAXPRE_0808",);
//        webToCIMISSDayFieldMap.put("MAXPRE_2020",);
//        webToCIMISSDayFieldMap.put("TIMEMAXPRE_2020",);
//
//        webToCIMISSDayFieldMap.put("MAXTEMP_0808",);
//        webToCIMISSDayFieldMap.put("TIMEMAXTEMP_0808",);
//
//        webToCIMISSDayFieldMap.put("MINTEMP_0808",);
//        webToCIMISSDayFieldMap.put("TIMEMINTEMP_0808",);
//
//        webToCIMISSDayFieldMap.put("MAXTEMP_2020",);
//        webToCIMISSDayFieldMap.put("TIMEMAXTEMP_2020",);
//
//        webToCIMISSDayFieldMap.put("MINTEMP_2020",);
//        webToCIMISSDayFieldMap.put("TIMEMINTEMP_2020",);

    }


    @RequestMapping("/singleStationQuery")
    public void querySingleStationData(HttpServletRequest request,
                                       @RequestParam(required = true) Map<String, Object> param,
                                       HttpServletResponse response) {
        Map<String, Object> rsMap = new HashMap<String, Object>();
        JSONArray dataList = new JSONArray();
        boolean flag = true;
        try {
            //1 获取参数
            Map<String, String> queryParam = new HashMap<String, String>();
            String dataInterfaceName = param.get("interfaceName").toString();
            String timeType = param.get("timeType").toString();
            String timeRange = param.get("timeRange").toString();
            queryParam.put("timeRange", timeRange);
            String orderBy = param.get("orderBy").toString();
            Map<String, String> orderByMap = commonConditionChange(orderBy, ",");
            orderBy = orderByMap.get("value");
            queryParam.put("orderBy", orderBy);
            String staIds = param.get("staIds").toString();
            queryParam.put("staIds", staIds);
            String elements = param.get("elements").toString();
            Map<String, String> fieldMap = analyzeFileMap(elements);
            String cimiss = fieldMap.get("cimiss");
            queryParam.put("elements", cimiss);
            String dataCode = "";
            //判断站点是否为交通站
            List<String> trafficStationList = CacheStationInfo.trafficeStationList;
            if (trafficStationList.contains(staIds)) {
                dataCode = "SURF_CHN_TRAFW_MUL";
            } else {
                if ("Hour".equals(timeType)) {
                    dataCode = "SURF_CHN_MUL_HOR";
                } else {
                    String main = fieldMap.get("main");
                    String other = fieldMap.get("other");
                    if (main.length() > 0) {
                        dataCode = "SURF_CHN_MAIN_MIN";
                    } else {
                        dataCode = "SURF_CHN_OTHER_MIN";
                    }
                }
            }
            queryParam.put("dataCode", dataCode);
            String userid = prop.getProperty("cimiss_userid");
            String psw = prop.getProperty("cimiss_psw");
            dataList = queryCIMISSData(userid, psw, dataInterfaceName, (HashMap<String, String>) queryParam);
            dataList = transferCIMISSToWeb(dataList, fieldMap, null, null);
            //将时间值转化为北京时
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                JSONObject obj = (JSONObject) dataList.get(i);
                String times = obj.get("OBSERVTIMES").toString();
                Date date = DateUtil.str2DateByFormat(times, "yyyy-MM-dd HH:mm:ss");
                date = DateUtil.calculateByHour(date, 8);
                long timeSecons = date.getTime();
                obj.put("OBSERVTIMES", timeSecons);
            }
        } catch (Exception e) {
            flag = false;
            String message = e.getMessage();
            rsMap.put("message", message);
            e.printStackTrace();
        } finally {
            rsMap.put("state", flag);
            rsMap.put("data", dataList);
            this.printJsonObjectData(rsMap, response);
        }
    }

    @ResponseBody
    @RequestMapping("/cimissAnyTimeQuery")
    public FeatureJson queryAnyTimeData(HttpServletRequest request,
                                        @RequestParam(required = true) Map<String, Object> param) {
        JSONArray dataList = new JSONArray();
        try {
            //时间类型：单时次还是任意时次
            String timeType = param.get("timeType").toString();
            String num = param.get("num").toString();
            //站点区域：湖北，华中，长江流域，湖北市县
            String areaType = param.get("areaType").toString();
            //国家站还是区域站
            String stationKind = param.get("stationKind").toString();
            //分钟数据还是国家数据
            String dataKind = param.get("dataKind").toString();
//            boolean isMultiQuery = Boolean.parseBoolean(param.get("isMultiQuery").toString());
//            boolean isCalculate = Boolean.parseBoolean(param.get("isCalculate").toString());
//            logger.info("时间类型:" + timeType + ",站点区域:" + areaType + ",站点种类:" + stationKind + ",TOP值：" + num + ",TOP字段:" + ",是否多次查询：" + isMultiQuery + ",是否计算:" + isCalculate);
            //1 获取站点列表
            Map<String, StationInfo> stationInfoMap = new HashMap<>();
            String stationNums = getBaseInfo(areaType, stationKind, stationInfoMap);
            //2 查询分析
            dataList = processAnyTimeQuery(timeType, param, stationNums, stationInfoMap, dataKind,stationKind);
            String  polyGon = param.get("polyGon").toString();
            orderData(polyGon, param.get("orderby").toString(), num, dataList);
            return getFeatureJson(param.get("timeRange").toString(), areaType, param.get("showType").toString(), polyGon, dataList, stationInfoMap, num);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取多条件查询参数
     *
     * @param param 解析子查询参数
     * @return
     */
    private List<Map<String, Object>> getQueryArray(Map<String, Object> param) {
        List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
        for (String key : param.keySet()) {
            if (key.startsWith("queryArray")) {
                String value = param.get(key).toString();
                int startIndex = 0;
                int indexStart = key.indexOf('[', startIndex);
                int indexEnd = key.indexOf(']', startIndex);
                startIndex = indexEnd;
                int keyStart = key.indexOf('[', startIndex);
                int keyEnd = key.length() - 1;
                int index = Integer.parseInt(key.substring(indexStart + 1, indexEnd));
                String arrayKey = key.substring(keyStart + 1, keyEnd);
                Map<String, Object> subParam = null;
                if (paramList.size() == index) {
                    subParam = new HashMap<String, Object>();
                    subParam.put(arrayKey, value);
                    paramList.add(subParam);
                } else {
                    if (paramList.size() < index) {
                        while (paramList.size() - 1 < index) {
                            Map<String, Object> tempParam = new HashMap<String, Object>();
                            paramList.add(tempParam);
                        }
                    }
                    subParam = paramList.get(index);
                    subParam.put(arrayKey, value);
                }
            }
        }
        return paramList;
    }


    @ResponseBody
    @RequestMapping("cimissRzQuery")
    public FeatureJson cimissRzQuery(String times, String area, String type,  String showType, String polyGon,String orderby,String num,String dataCode) {
        JSONArray rzDataList = new JSONArray();
        Map<String, JSONArray> allDataCodeResultArray = new HashMap<>();
        try {
//            String elements ="RHU_Avg,VAP_Avg,RHU_Min,EVP,EVP_Big,WIN_S_2mi_Avg,WIN_S_Inst_Max,WIN_S_Max,VIS_Min,WIN_D_INST_Max,WIN_D_S_Max,WIN_S_Max_OTime,WIN_S_INST_Max_OTime";
//            if("qy".equals(type)){
//                elements+=",EVP,EVP_Big";
//            }
            //1 获取站点列表
            //过滤城市
             Map<String, StationInfo> map =  new HashMap<>();
            String stationNums = getBaseInfo(area,type,map);
            //获得站号
            rzDataList = queryRZData(times, dataCode, polyGon, area, allDataCodeResultArray, stationNums);
            String newWord = "";
            if (polyGon.contains("PRE")) {
                newWord = "PRE1";
            } else if (polyGon.contains("TEM")) {
                newWord = "TEMP24";
            } else if (polyGon.contains("WIND")) {
                newWord = "WIND";
            } else if (polyGon.contains("RHU")) {
                newWord = "RHU24";
            } else if (polyGon.contains("VIS")) {
                newWord = "VIS24";
            } else if (polyGon.contains("PRS") || polyGon.contains("VAP")) {
                newWord = "PRS24";
            } else {
                newWord = "other24";
            }

            //cimiss转换到web字段
            String finalNewWord = newWord;
            rzDataList.forEach(obj->{
              Map<String,Object> jsonObject =   (Map)obj;
                  jsonObject.put(finalNewWord,jsonObject.get(polyGon));
                  jsonObject.remove(polyGon,jsonObject.get(polyGon));
                  jsonObject.put("STATIONNUM",jsonObject.get("Station_Id_C"));
                  jsonObject.remove("Station_Id_C",jsonObject.get("Station_Id_C"));
                jsonObject.put("LATITUDE",jsonObject.get("Lat"));
                jsonObject.remove("Lat",jsonObject.get("Lat"));
                jsonObject.put("LONGITUDE",jsonObject.get("Lon"));
                jsonObject.remove("Lon",jsonObject.get("Lon"));
            });
            if(rzDataList.size()>0){
              orderData(polyGon,orderby,num,rzDataList);
              return getFeatureJson(times, area, showType, newWord, rzDataList, map, num);
            }else {
                return getFeatureJson(times, area, showType, newWord, rzDataList, map, num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONArray queryRZData(String times, String dataCode, String elements, String area, Map<String, JSONArray> allDataCodeResultArray, String stationNums) throws InterruptedException, java.util.concurrent.ExecutionException {
        JSONArray rzDataList = new JSONArray();
        //判断站号是否超过999，超过的话就分组查询
        String[] numArr = stationNums.split(",");
        if (numArr.length > 999) {
            List<String> idGroups = new ArrayList<>(); //分组结果
            String idStrTemp = "";
            for (int i = 0; i < numArr.length; i++) { //对站号进行分组每组998个
                if (!"".equals(idStrTemp)) {
                    idStrTemp += ",";
                }
                idStrTemp += numArr[i];
                if ((i + 1) % 998 == 0 || i == numArr.length - 1) {
                    idGroups.add(idStrTemp);
                    idStrTemp = "";
                }
            }
            int threadNum = 5;
            if (idGroups.size() <= 5) {
                threadNum = idGroups.size();
            }
            String userid = prop.getProperty("cimiss_userid");
            String psw = prop.getProperty("cimiss_psw");
            ExecutorService pool = Executors.newFixedThreadPool(threadNum);
            // 创建多个有返回值的任务
            List<Future> list = new ArrayList<Future>();
            for (int i = 0; i < threadNum; i++) {
                List<String> taskStaIdList = getCimissTaskIdList(i, threadNum, idGroups);
                List<Map<String, String>> taskParamList = new ArrayList<>();
                for (String ids : taskStaIdList) {
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("dataCode", dataCode);
                    paramMap.put("elements", "Station_Id_C,Lat,Lon," + elements);
                    paramMap.put("times", times);
                    paramMap.put("staIds", ids);
                    taskParamList.add(paramMap);
                }
                Callable c = new CIMISSCallable(userid, psw, "getSurfEleByTimeAndStaID", taskParamList, i);
                // 执行任务并获取Future对象
                Future f = pool.submit(c);
                list.add(f);
            }
            // 关闭线程池
            pool.shutdown();
            // 获取所有并发任务的运行结果
            for (Future f : list) {
                Map<String, JSONArray> taskResultArray = (Map<String, JSONArray>) (f.get());
                for (String code : taskResultArray.keySet()) {
                    JSONArray dataArray = taskResultArray.get(code);
                    JSONArray allDataArray = null;
                    if (allDataCodeResultArray.containsKey(code)) {
                        allDataArray = allDataCodeResultArray.get(code);
                    } else {
                        allDataArray = new JSONArray();
                        allDataCodeResultArray.put(code, allDataArray);
                    }
                    allDataArray.addAll(dataArray);
                }
            }
            JSONArray array = allDataCodeResultArray.get(dataCode);
            rzDataList.addAll(array);
        } else {
            rzDataList = queryRzData(dataCode, elements, times, stationNums); //查询！
        }
        return rzDataList;
    }

    //从cimiss中查询数据
    public JSONArray queryRzData(String dataCode, String elementsStr, String timeStr, String idStr) {
        JSONArray dlist = new JSONArray();
        JSONObject jo = new JSONObject();
        JSONArray dataArray = new JSONArray();
        HashMap<String, String> params = new HashMap<String, String>();
        String elements = "Station_Id_C,Lat,Lon," + elementsStr;
        String[] elementArray = elements.split(",");
        params.put("dataCode", dataCode);
        params.put("times", timeStr);
        params.put("elements", elements);
        params.put("staIds", idStr);

        String userid = prop.getProperty("cimiss_userid");
        String psw = prop.getProperty("cimiss_psw");
        StringBuffer retStr = new StringBuffer();
        DataQueryClient cimissClient = new DataQueryClient();
        try {
            cimissClient.initResources();
            int rst = cimissClient.callAPI_to_serializedStr(userid, psw, "getSurfEleByTimeAndStaID", params, "JSON", retStr);
            if (rst == 0) {
                jo = (JSONObject) JsonUtils
                        .toObject(retStr.toString());
            }
            dataArray = new JSONArray();
            if (jo.containsKey("DS")) {
                dataArray = (JSONArray) jo
                        .get("DS");
                int length = dataArray.size();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = dataArray
                            .getJSONObject(i);
                    HashMap<String, String> hm = new HashMap<String, String>();
                    for (String ele : elementArray) {
                        hm.put(ele, jsonObject.getString(ele));
                    }
                    dlist.add(hm);
                }
            }

        } catch (Exception e) {
            logger.error("数据查询异常:" + e);
        } finally {
            cimissClient.destroyResources();
        }
        return dlist;
    }

    @ResponseBody
    @RequestMapping("/cimissSingleTimeQuery")
    public FeatureJson querySingleTimeData(String times, String timeType, String areaType, String stationKind, String dataKind, String tabType, String showType,
                                           String polyGon, boolean isCalculate, String orderby, String num) {
        JSONArray dataList = new JSONArray();
        try {
            Map<String, Object> param = new HashMap<>();
            //时间类型：单时次还是任意时次
            param.put("timeType", timeType);
            //站点区域：湖北，华中，长江流域，湖北市县
            param.put("areaType", areaType);
            //国家站还是区域站
            param.put("stationKind", stationKind);
            //分钟数据还是国家数据
            param.put("dataKind", dataKind);
            //Tabble页类型
            param.put("tabType", tabType);
            param.put("showType", showType);
//            param.put("polyGon",polyGon);
            param.put("isCalculate", isCalculate);
            param.put("times", times);

            logger.info("时间类型:" + timeType + ",站点区域:" + areaType + ",站点种类:" + stationKind + ",TOP值：" + num + ",TOP字段:" + polyGon  + ",是否计算:" + isCalculate + ",Tab页:" + tabType);
            //1 获取站点列表
            Map<String, StationInfo> stationInfoMap = new HashMap<>();
            String stationNums = getBaseInfo(areaType, stationKind, stationInfoMap);
            //2 解析查询步骤
            //一次查询
            String baseEle = "STATIONNAME,STATIONNUM,PROVINCE,CITY,COUNTY,LONGITUDE,LATITUDE,ALTITUDE,";
            String elements = baseEle + "PRE_1H,Q_PRE_1H,PRE_3H,Q_PRE_3H,PRE_6H,Q_PRE_6H,PRE_12H,Q_PRE_12H,PRE_24H,Q_PRE_24H";
            elements += ",TEMP,Q_TEM,MAXTEMP,Q_MAXTEMP,TIMEMAXTEMP,MINTEMP,Q_MINTEMP,TIMEMINTEMP,CHANGETEMP24,Q_CHANGETEMP24,MAXTEMP24,Q_MAXTEMP24,MINTEMP24,Q_MINTEMP24";
            elements += ",STATIONPRESS,Q_PRS,PRS_SEA,Q_PRS_SEA,PRS_MAX,Q_PRS_MAX,PRS_MAX_OTIME,PRS_MIN,Q_PRS_MIN,PRS_MIN_OTIME,PRS_CHANGE_3H,Q_PRS_CHANGE_3H,PRS_CHANGE_24H,Q_PRS_CHANGE_24H";
            elements += ",RELHUMIDITY,Q_RELHUMIDITY,MINRELHUMIDITY,Q_MINRELHUMIDITY,TIMEMINRELH,DEWTEMP,Q_DEWTEMP";
            elements += ",VISIBILITY,Q_VISIBILITY,MINVIS,Q_MINVIS,VISIBILITY1,Q_VISIBILITY1,VISIBILITY10,Q_VISIBILITY10";
            elements += ",WINDDIRECT2,Q_WINDDIRECT2,WINDVELOCITY2,Q_WINDVELOCITY2,WINDDIRECT10,Q_WINDDIRECT10,WINDVELOCITY10,Q_WINDVELOCITY10,MAXWINDD10,Q_MAXWINDD10,MAXWINDV10,TIMEMAXWIND10,INSTANTWINDD,Q_INSTANTWINDD,INSTANTWINDV,EXMAXWINDD,Q_EXMAXWINDD,EXMAXWINDV,TIMEEXMAXWINDV";
            elements += ",SNOW_DEPTH,Q_SNOW_DEPTH";
            elements += ",WEP_NOW,Q_WEP_NOW,LOWCLOUD,Q_LOWCLOUD,EVP_BIG,Q_EVP_BIG,CLO_COV_LM,Q_CLO_COV_LM,TOTALCLOUD,Q_TOTALCLOUD,GST,Q_GST,";
//                电线积冰
            if ("tabOneMinute".equals(tabType)) {
                elements = baseEle + ",PRE_1H,Q_PRE_1H,";
                elements += "TEMP,Q_TEM,MAXTEMP,Q_MAXTEMP,MINTEMP,Q_MINTEMP" +
                        ",STATIONPRESS,Q_PRS,PRS_SEA,Q_PRS_SEA" +
                        ",MINVIS,Q_MINVIS,VISIBILITY1,Q_VISIBILITY1,VISIBILITY10,Q_VISIBILITY10" +
                        ",WINDDIRECT2,Q_WINDDIRECT2,WINDVELOCITY2,Q_WINDVELOCITY2,WINDDIRECT10,Q_WINDDIRECT10,WINDVELOCITY10,Q_WINDVELOCITY10,MAXWINDD10,Q_MAXWINDD10,MAXWINDV10,TIMEMAXWIND10,INSTANTWINDD,Q_INSTANTWINDD,INSTANTWINDV,EXMAXWINDD,Q_EXMAXWINDD,EXMAXWINDV,TIMEEXMAXWINDV" +
                        ",EVP_BIG,Q_EVP_BIG" +
                        ",RELHUMIDITY,Q_RELHUMIDITY,MINRELHUMIDITY,Q_MINRELHUMIDITY,TIMEMINRELH,DEWTEMP,Q_DEWTEMP";
//                    }

            }
            Map<String, String> fieldMap = analyzeFileMap(elements);
            dataList = processQuery(timeType, param, fieldMap, stationNums, stationInfoMap, dataKind, stationKind, tabType);

            //可能出现一个站点重复记录
            List<String> stationNumList = new ArrayList<>();
            List<JSONObject> repeatObjectList = new ArrayList<JSONObject>();
            for (Object aDataList : dataList) {
                JSONObject jsonObject = ((JSONObject) aDataList);
                String station_id_c = jsonObject.getString("Station_Id_C");
                if (stationNumList.contains(station_id_c)) {
                    repeatObjectList.add(jsonObject);
                } else {
                    stationNumList.add(station_id_c);
                }
            }
            dataList.removeAll(repeatObjectList);
//            如果是1一分钟数据 合并降水和其他要素
            for (JSONObject jsonObject : repeatObjectList) {
                String station_id_c = jsonObject.getString("Station_Id_C");
                for (Object aDataList : dataList) {
                    JSONObject jsonObject1 = (JSONObject) aDataList;
                    if (station_id_c.equals(jsonObject1.getString("Station_Id_C"))) {
                        for (String key : jsonObject.keySet()) {
                            jsonObject1.put(key, jsonObject.get(key));
                        }
                    }
                }
            }
            //将CIMISS字段转为WEB字段
            dataList = transferCIMISSToWeb(dataList, fieldMap, stationInfoMap, null);
            if (isCalculate) {
                //表示查询后需要对字段间的值进行计算，然后生成新的结果值
                List<Object> calculateArray = (List<Object>) param.get("calculateArray");
                calculateResult(dataList, calculateArray);
            }
            orderData(polyGon, orderby, num, dataList);

            return getFeatureJson(times, areaType, showType, polyGon, dataList, stationInfoMap, num);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ;
        return null;
    }

    private void orderData(String polyGon, String orderby, String num, JSONArray dataList) {
        String[] invalidValue = new String[]{"999999", "99999", "9999", "999998", "999990", "null", ""};
        List<String> invalidValues = Arrays.asList(invalidValue);
        int flag = "desc".equals(orderby) ? 1 : -1;
        if (!num.equals("null")) {
            dataList.sort((a, b) -> {
                String eleValueA = ((JSONObject) a).getString(polyGon);
                String eleValueB = ((JSONObject) b).getString(polyGon);
                if (invalidValues.contains(eleValueB) || eleValueB == null) {
                    return -1;
                } else if (invalidValues.contains(eleValueA) || eleValueA == null) {
                    return 1;
                } else {
                    double m = Double.parseDouble(eleValueA);
                    double n = Double.parseDouble(eleValueB);
                    if (m > n) {
                        return -flag;
                    } else if (m < n) {
                        return flag;
                    } else {
                        return 0;
                    }
                }
            });
        }
    }

    private FeatureJson getFeatureJson(String times, String areaType, String showType, String polyGon, JSONArray dataList, Map<String, StationInfo> stationInfoMap, String num) throws IOException, ClassNotFoundException {
        FeatureJson featureJson;
        String elements ="";
        if(dataList.size()>0){
        Set<String> haha = new HashSet<>();
            haha.addAll(dataList.getJSONObject(0).keySet());
            haha.remove("LATITUDE");
            haha.remove("LONGITUDE");
             elements = StringUtils.join(haha.toArray(), ",");
            elements = "LONGITUDE," + "LATITUDE," + elements;
        }else {
            elements = "LONGITUDE," + "LATITUDE,"+polyGon+",STATIONNUM";
        }

        //点数据
        if ("point".equals(showType)) {
            featureJson = MoapUtil.getPointJson(dataList, elements, stationInfoMap, num);
            Properties pro = featureJson.getData().getProperties();
            pro.setProperty("date", times);
            pro.setProperty("type", "aws");
            pro.setProperty("title", TimeUtil.currentTime("yyyy年MM月dd日HH时") + "地面国家站观测");
        } else {
            //面数据
            double minLon = 0, maxLon = 0, minLat = 0, maxLat = 0;
            int gridx = 0, gridy = 0;
            String bounds = "";
            switch (areaType) {
                case "hb":
                    minLon = 108.369;
                    maxLon = 116.647;
                    minLat = 28.291;
                    maxLat = 33.9;
                    bounds = prophb.getProperty("hbbound");
                    break;
                case "ly":
                    minLon = 90.534;
                    maxLon = 122.00;
                    minLat = 24.461;
                    maxLat = 35.753;
                    bounds = proply.getProperty("lybound");
                    break;
                case "hz":
                    minLon = 107.235;
                    maxLon = 117.5;
                    minLat = 24.6;
                    maxLat = 36.6;
                    bounds = prophz.getProperty("hzbound");
                    break;
                default:
                    ;
            }
            gridx = (int) ((maxLon - minLon) / 0.1);
            gridy = (int) ((maxLat - minLat) / 0.1);
            featureJson = MoapUtil.dataToPolygonJason(dataList, "LONGITUDE", "LATITUDE",
                    polyGon.toUpperCase(), minLon, maxLon, minLat, maxLat, gridx, gridy, bounds);
        }
        return featureJson;
    }

    /**
     * 对数据按照TOP字段排序
     *
     * @param topField top字段
     * @param dataList 数据列表
     */
    private JSONArray orderTopVal(final String topField, JSONArray dataList) {
        List<JSONObject> objList = new ArrayList<JSONObject>();
        int dataSize = dataList.size();
        for (int i = 0; i < dataSize; i++) {
            objList.add((JSONObject) dataList.get(i));
        }
        objList.sort((arg0, arg1) -> {
            double value0 = Double.MIN_VALUE;
            double value1 = Double.MIN_VALUE;
            if (arg0.containsKey(topField)) {
                String fileldValueStr = arg0.get(topField).toString();
                if (fileldValueStr.length() > 0) {
                    double fileldStrVal = Double.parseDouble(fileldValueStr);
                    //TOP排序的值肯定不会超过99999
                    if (fileldStrVal < 99999) {
                        value0 = fileldStrVal;
                    }
                }
            }
            if (arg1.containsKey(topField)) {
                String fileldValueStr = arg1.get(topField).toString();
                if (fileldValueStr.length() > 0) {
                    double fileldStrVal = Double.parseDouble(fileldValueStr);
                    //TOP排序的值肯定不会超过99999
                    if (fileldStrVal < 99999) {
                        value1 = fileldStrVal;
                    }
                }
            }

            int returnVal = 1;
            if (value0 > value1) {
                returnVal = -1;
            } else if (value0 == value1) {
                returnVal = 0;
            }
            return returnVal;
        });
        JSONArray orderList = new JSONArray();
        for (JSONObject jsonObject : objList) {
            orderList.add(jsonObject);
        }
        return orderList;
    }

    /**
     * 获取单次查询结果
     *
     * @param timeType       时间类型
     * @param param          参数集合
     * @param stationNums    站号列表
     * @param stationInfoMap 站号信息
     * @param dataKind       数据类型（分钟数据还是日数据）
     * @return 单次查询结果
     */
    private JSONArray processAnyTimeQuery(String timeType, Map<String, Object> param, String stationNums, Map<String, StationInfo> stationInfoMap, String dataKind, String stationKind) {
        JSONArray queryResultArray = new JSONArray();
        try {
            //dataCode对应的查询结果
            Map<String, JSONArray> allDataCodeResultArray = new HashMap<String, JSONArray>();
            String elements = param.get("elements").toString();
            Map<String, String> fieldMap = analyzeFileMap(elements);
            //1 对站号分组（由于CIMISS资料代码对查询的站点数存在限制,如果站点数大于999，因此需要对站点进行分组）
            List<String> stationGrouopList = getStationGroup(stationNums);
            //获取交通站数据
            String trafficStationNums = getTrafficStation(stationInfoMap);
            //2 获取接口参数List
            Map<String, String> dataCodeMap = new HashMap<String, String>();
            Map<String, List<Map<String, String>>> interfaceParamMap = getAnyTimeInterfaceParamMap(stationGrouopList, trafficStationNums, param, fieldMap, stationKind, dataCodeMap);
            //3 查询数据：采用多线程模式(一般来说只有一个接口名)
            for (String interfaceName : interfaceParamMap.keySet()) {
                List<Map<String, String>> paramList = interfaceParamMap.get(interfaceName);
                int threadNum = 5;
                if (paramList.size() <= 5) {
                    threadNum = paramList.size();
                }
                ExecutorService pool = Executors.newFixedThreadPool(threadNum);
                // 创建多个有返回值的任务
                List<Future> list = new ArrayList<Future>();
                for (int i = 0; i < threadNum; i++) {
                    List<Map<String, String>> taskParamList = getTaskParam(i, threadNum, paramList);
                    String userid = prop.getProperty("cimiss_userid");
                    String psw = prop.getProperty("cimiss_psw");
                    Callable c = new CIMISSCallable(userid, psw, interfaceName, taskParamList, i);
                    // 执行任务并获取Future对象
                    Future f = pool.submit(c);
                    list.add(f);
                }
                // 关闭线程池
                pool.shutdown();
                // 获取所有并发任务的运行结果
                for (Future f : list) {
                    // 从Future对象上获取任务的返回值，并输出到控制台
                    Map<String, JSONArray> taskResultArray = (Map<String, JSONArray>) (f.get());
                    for (String dataCode : taskResultArray.keySet()) {
                        JSONArray dataArray = taskResultArray.get(dataCode);
                        JSONArray allDataArray = null;
                        if (allDataCodeResultArray.containsKey(dataCode)) {
                            allDataArray = allDataCodeResultArray.get(dataCode);
                        } else {
                            allDataArray = new JSONArray();
                            allDataCodeResultArray.put(dataCode, allDataArray);
                        }
                        allDataArray.addAll(dataArray);
                    }
                }
            }
            joinQueryResult(allDataCodeResultArray, dataCodeMap, queryResultArray);
            //实现Elment中的部分字段与显示字段映射
            Map<String, String> webFieldMap = new HashMap<String, String>();
            initFieldMap(webFieldMap, param);

            //将CIMISS字段转为WEB字段
            queryResultArray = transferCIMISSToWeb(queryResultArray, fieldMap, stationInfoMap, webFieldMap);
            if (param.containsKey("accMethod")) {
                String accMethod = param.get("accMethod").toString();
                Map<String, String> accMethodMap = new HashMap<String, String>();
                String[] accMethodSplit = accMethod.split(",");
                for (String segment : accMethodSplit) {
                    String[] segmentSplit = segment.split(":");
                    if (segmentSplit.length == 2) {
                        accMethodMap.put(segmentSplit[0].trim(), segmentSplit[1].trim());
                    }
                }
                int size = queryResultArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject = (JSONObject) queryResultArray.get(i);
                    for (String accField : accMethodMap.keySet()) {
                        String accVal = accMethodMap.get(accField);
                        if (accVal.equals("-") && jsonObject.containsKey(accField)) {
                            String fieldVal = jsonObject.get(accField).toString();
                            if (fieldVal.length() > 0) {
                                double val = Double.parseDouble(fieldVal);
                                jsonObject.put(accField, 0 - val);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return queryResultArray;
    }

    /**
     * 获取交通站点
     *
     * @param stationInfoMap
     * @return
     */
    private String getTrafficStation(Map<String, StationInfo> stationInfoMap) {
        List<String> trafficStationList = CacheStationInfo.trafficeStationList;
        StringBuilder builder = new StringBuilder();
        for (String stationNum : stationInfoMap.keySet()) {
            if (trafficStationList.contains(stationNum)) {
                builder.append(stationNum + ",");
            }
//            StationInfo staInfo =stationInfoMap.get(stationNum);
//            if("15".equals(staInfo.getStationLevl())){
//                builder.append(stationNum+",");
//            }
        }
        String stationNums = builder.toString();
        if (stationNums.length() > 0) {
            stationNums = stationNums.substring(0, stationNums.length() - 1);
        }
        return stationNums;
    }

    /**
     * 初始化字段映射
     *
     * @param cimissFieldMap
     * @param param
     */
    private void initFieldMap(Map<String, String> cimissFieldMap, Map<String, Object> param) {
        Map<String, String> statFieldMap = null;
        if (param.containsKey("statEles")) {
            statFieldMap = new HashMap<String, String>();
            String statEles = param.get("statEles").toString();
            Map<String, String> statSurfEleChangeMap = commonConditionChange(statEles, ",");
            statEles = statSurfEleChangeMap.get("value");
            StringBuilder statSurfEleBuilder = new StringBuilder();
            String[] split = statEles.split(",");
            Map<String, String> statSurfEleMap = new HashMap<String, String>();
            for (String segment : split) {
                String[] segmentSplit = segment.split(":");
                if (segmentSplit.length == 2) {
                    statFieldMap.put(segmentSplit[1].trim() + "_" + segmentSplit[0].trim(), segmentSplit[0].trim());
                }
            }
        }
        String webFieldMapStr = param.get("fieldMap").toString();
        Map<String, String> webFieldChangeMap = commonConditionChange(webFieldMapStr, ",");
        String changeWebFieldMapStr = webFieldChangeMap.get("value");
        String[] split = changeWebFieldMapStr.split(",");
        for (String segment : split) {
            String[] segmentSplit = segment.split(":");
            if (segmentSplit.length == 2) {
                String cimissField = segmentSplit[0].trim();
                String webField = segmentSplit[1].trim();
                if (statFieldMap == null) {
                    cimissFieldMap.put(cimissField, webField);
                } else {
                    for (String key : statFieldMap.keySet()) {
                        String statVal = statFieldMap.get(key);
                        if (statVal.equals(cimissField)) {
                            cimissFieldMap.put(key, webField);
                        }
                    }
                }
            }
        }
    }


    /**
     * 获取单次查询结果
     *
     * @param timeType       时间类型
     * @param param          参数集合
     * @param stationNums    站号列表
     * @param stationInfoMap 站号信息
     * @param dataKind       数据类型（分钟数据还是日数据）
     * @param stationType    站点类型(国家站还是区域站)
     * @param stationType    Table页
     * @return 单次查询结果
     */
    private JSONArray processQuery(String timeType, Map<String, Object> param, Map<String, String> fieldMap, String stationNums, Map<String, StationInfo> stationInfoMap, String dataKind, String stationType, String tabType) throws Exception {
        JSONArray queryResultArray = new JSONArray();
        try {
            Map<String, JSONArray> allDataCodeResultArray = new HashMap<String, JSONArray>();
            //1 获取资料代码与字段映射
            //获取交通站数据
            String trafficStationNums = getTrafficStation(stationInfoMap);
            Map<String, String> dataCodeMap = getDataCodeFields(fieldMap, timeType, dataKind, param, stationType, tabType, trafficStationNums);
            //2 对站号分组（由于CIMISS资料代码对查询的站点数存在限制,如果站点数大于999，因此需要对站点进行分组）
            List<String> stationGrouopList = getStationGroup(stationNums);
            //3 获取接口参数List
            Map<String, List<Map<String, String>>> interfaceParamMap = getInterfaceParamMap(dataCodeMap, stationGrouopList, trafficStationNums, param, timeType, dataKind);
            //4 查询数据：采用多线程模式(一般来说只有一个接口名)
            for (String interfaceName : interfaceParamMap.keySet()) {
                List<Map<String, String>> paramList = interfaceParamMap.get(interfaceName);
                int threadNum = 5;
                if (paramList.size() <= 5) {
                    threadNum = paramList.size();
                }
                ExecutorService pool = Executors.newFixedThreadPool(threadNum);
                // 创建多个有返回值的任务
                List<Future> list = new ArrayList<Future>();
                for (int i = 0; i < threadNum; i++) {
                    List<Map<String, String>> taskParamList = getTaskParam(i, threadNum, paramList);
                    String userid = prop.getProperty("cimiss_userid");
                    String psw = prop.getProperty("cimiss_psw");
                    Callable c = new CIMISSCallable(userid, psw, interfaceName, taskParamList, i);
                    // 执行任务并获取Future对象
                    Future f = pool.submit(c);
                    list.add(f);
                }
                // 关闭线程池
                pool.shutdown();
                // 获取所有并发任务的运行结果
                for (Future f : list) {
                    // 从Future对象上获取任务的返回值，并输出到控制台
                    Map<String, JSONArray> taskResultArray = (Map<String, JSONArray>) (f.get());
                    for (String dataCode : taskResultArray.keySet()) {
                        JSONArray dataArray = taskResultArray.get(dataCode);
                        JSONArray allDataArray = null;
                        if (allDataCodeResultArray.containsKey(dataCode)) {
                            allDataArray = allDataCodeResultArray.get(dataCode);
                        } else {
                            allDataArray = new JSONArray();
                            allDataCodeResultArray.put(dataCode, allDataArray);
                        }
                        allDataArray.addAll(dataArray);
                    }
                }
            }
            joinQueryResult(allDataCodeResultArray, dataCodeMap, queryResultArray);
        } catch (Exception e) {
            throw e;
        }
        return queryResultArray;
    }

    /**
     * 获取任务对应的参数集合
     *
     * @param threadIndex 线程索引
     * @param threadNum   线程数
     * @param paramList   参数集合
     * @return
     */
    private List<Map<String, String>> getTaskParam(int threadIndex, int threadNum, List<Map<String, String>> paramList) {
        List<Map<String, String>> taskParamList = new ArrayList<Map<String, String>>();
        int paramSize = paramList.size();
        if (threadNum == paramSize) {
            taskParamList.add(paramList.get(threadIndex));
        } else {
            int size = paramSize / threadNum;
            int mode = paramSize % threadNum;
            if (mode > 0) {
                size += 1;
            }
            int start = threadIndex * size;
            int end = (threadIndex + 1) * size;
            if (end > paramSize) {
                end = paramSize;
            }
            for (int i = start; i < end; i++) {
                taskParamList.add(paramList.get(i));
            }
        }
        return taskParamList;
    }

    /**
     * 分解字段映射
     *
     * @param elements 字段列表
     * @return
     */
    private Map<String, String> analyzeFileMap(String elements) {
        StringBuilder stationFieldBuilder = new StringBuilder();
        StringBuilder mainFieldBuilder = new StringBuilder();
        StringBuilder otherFieldBuilder = new StringBuilder();
        StringBuilder hourFieldBuilder = new StringBuilder();
        StringBuilder timeFieldBuilder = new StringBuilder();
        StringBuilder trafficFieldBuilder = new StringBuilder();
        StringBuilder oneMinFieldBuilder = new StringBuilder();

        //TODO cimissFieldBuilder可取消
        StringBuilder cimissFieldBuilder = new StringBuilder();
        String[] fieldArray = elements.split(",");
        for (String field : fieldArray) {
            String webField = field.trim();
            String cimissField = "";
            if (webToCIMISSStationFieldMap.containsKey(webField)) {
                cimissField = webToCIMISSStationFieldMap.get(webField);
                stationFieldBuilder.append(cimissField + ",");
            }
            //小时字段由分钟主要素字段+分钟其他要素字段+小时自有字段组成
            if (webToCIMISSMainFieldMap.containsKey(webField)) {
                cimissField = webToCIMISSMainFieldMap.get(webField);
                mainFieldBuilder.append(cimissField + ",");
                hourFieldBuilder.append(cimissField + ",");
            }
            if (webToCIMISSOtherFieldMap.containsKey(webField)) {
                cimissField = webToCIMISSOtherFieldMap.get(webField);
                otherFieldBuilder.append(cimissField + ",");
                if (!("Station_Id_C").equals(cimissField)) {
                    hourFieldBuilder.append(cimissField + ",");
                }
            }
            if (webToCIMISSHourFieldMap.containsKey(webField)) {
                cimissField = webToCIMISSHourFieldMap.get(webField);
                if (!("Station_Id_C").equals(cimissField)) {
                    hourFieldBuilder.append(cimissField + ",");
                }
            }
            if (webToCIMISSTimeFieldMap.containsKey(webField)) {
                cimissField = webToCIMISSTimeFieldMap.get(webField);
                timeFieldBuilder.append(cimissField + ",");
            }

            if (webToCIMISSTrafficFieldMap.containsKey(webField)) {
                String trafficCimissField = webToCIMISSTrafficFieldMap.get(webField);
                trafficFieldBuilder.append(trafficCimissField + ",");
            }

            if (webToCIMISSOneMinFieldMap.containsKey(webField)) {
                String oneMinCimissField = webToCIMISSOneMinFieldMap.get(webField);
                oneMinFieldBuilder.append(oneMinCimissField + ",");
            }

            if (cimissField.length() > 0) {
                if (!("Station_Id_C").equals(cimissField)) {
                    cimissFieldBuilder.append(cimissField + ",");
                }
            } else {
                logger.error("不存在映射字段:" + webField);
            }
        }
        StringBuilder[] builderArray = new StringBuilder[]{stationFieldBuilder, mainFieldBuilder, otherFieldBuilder, hourFieldBuilder, timeFieldBuilder, cimissFieldBuilder, trafficFieldBuilder, oneMinFieldBuilder};
        String[] keyArray = new String[]{"station", "main", "other", "hour", "time", "cimiss", "traffic", "oneMin"};
        Map<String, String> fieldMap = new HashMap<String, String>();
        for (int i = 0; i < builderArray.length; i++) {
            StringBuilder builder = builderArray[i];
            String key = keyArray[i];
            String builderInfo = builder.toString();
            if (builderInfo.length() > 0) {
                builderInfo = builderInfo.substring(0, builderInfo.length() - 1);
            }
            fieldMap.put(key, builderInfo);
        }
        return fieldMap;
    }

    /**
     * 获取查询的资料代码对应的字段
     *
     * @param fieldMap    字段映射
     * @param timeType    时间类型
     * @param dataKind    数据类型：分钟数据还是日值数据
     * @param param       参数
     * @param stationType 站点类型
     * @param tabType     Table页
     * @return 资料代码对应的字段
     * @throws Exception
     */
    private Map<String, String> getDataCodeFields(Map<String, String> fieldMap, String timeType, String dataKind, Map<String, Object> param, String stationType, String tabType, String trafficStationNums) throws Exception {
        Map<String, String> dataCodeMap = new HashMap<String, String>();
        if ("SingleTime".equals(timeType) && "fz".equals(dataKind)) {
            if ("tabOneMinute".equals(tabType)) {
                //1分钟数据
                String oneMinFields = fieldMap.get("oneMin");
//                if(oneMinFields.indexOf("PRE")>0){
                dataCodeMap.put("SURF_WEA_CBF_PRE_MIN_TAB", "Station_Id_C,PRE,Q_PRE");
//                }
//                if(oneMinFields.indexOf("TEM")>-1){

                String haha = "Station_Id_C,TEM,Q_TEM,TEM_Max,Q_TEM_Max,TEM_Min,Q_TEM_Min,PRS,Q_PRS,PRS_Sea,Q_PRS_Sea," +
                        "VIS_Min,Q_VIS_Min,VIS_HOR_1MI,Q_VIS_HOR_1MI,VIS_HOR_10MI,Q_VIS_HOR_10MI,WIN_D_Avg_2mi," +
                        "Q_WIN_D_Avg_2mi,WIN_S_Avg_2mi,Q_WIN_S_Avg_2mi,WIN_D_Avg_10mi,Q_WIN_D_Avg_10mi," +
                        "WIN_S_Avg_10mi,Q_WIN_S_Avg_10mi,WIN_D_S_Max,Q_WIN_D_S_Max,WIN_S_Max,WIN_S_Max_OTime," +
                        "WIN_D_INST,Q_WIN_D_INST,WIN_S_INST,WIN_D_INST_Max,Q_WIN_D_INST_Max,WIN_S_Inst_Max," +
                        "WIN_S_INST_Max_OTime,EVP_Big,Q_EVP_Big,RHU,Q_RHU,RHU_Min,Q_RHU_Min,RHU_Min_OTIME,DPT,Q_DPT";
                dataCodeMap.put("SURF_WEA_CBF_MUL_MIN_MAIN_TAB", haha);

//                }
            } else if ("tabDay".equals(tabType)) {
                String hourField = fieldMap.get("cimiss");
                dataCodeMap.put("SURF_CHN_MUL_DAY", hourField);
            } else if ("tabHour".equals(tabType)) {
                String hourField = fieldMap.get("hour");
//                if(hourField.indexOf("EICED")>0){
                dataCodeMap.put("SURF_CHN_WSET_FTM", "Station_Id_C,EICED,Q_EICED");
//                }else {
                dataCodeMap.put("SURF_CHN_MUL_HOR", hourField);
//                }
            } else if ("tabMinute".equals(tabType)) {
                //5分钟数据
                String mainFields = fieldMap.get("main");
                String otherFields = fieldMap.get("other");
                if ("Station_Id_C".equals(mainFields) || "Station_Id_C".equals(otherFields)) {
                    if ("Station_Id_C".equals(mainFields) && "Station_Id_C".equals(otherFields)) {
                        throw new Exception("对应的分钟主要素和次要素为空");
                    } else if ("Station_Id_C".equals(mainFields)) {
                        dataCodeMap.put("SURF_CHN_OTHER_MIN", otherFields);
                    } else {
                        dataCodeMap.put("SURF_CHN_MAIN_MIN", mainFields);
                    }
                } else {
                    dataCodeMap.put("SURF_CHN_MAIN_MIN", mainFields);
                    dataCodeMap.put("SURF_CHN_OTHER_MIN", otherFields);
                }
            } else {
                //兼容页面为更新时的参数
                String queryTime = param.get("times").toString();
                if (queryTime.endsWith("0000")) {
                    //小时数据
                    String hourField = fieldMap.get("hour");
                    dataCodeMap.put("SURF_CHN_MUL_HOR", hourField);
                } else {
                    //分钟数据
                    String mainFields = fieldMap.get("main");
                    String otherFields = fieldMap.get("other");
                    if ("Station_Id_C".equals(mainFields) || "Station_Id_C".equals(otherFields)) {
                        if ("Station_Id_C".equals(mainFields) && "Station_Id_C".equals(otherFields)) {
                            throw new Exception("对应的分钟主要素和次要素为空");
                        } else if ("Station_Id_C".equals(mainFields)) {
                            dataCodeMap.put("SURF_CHN_OTHER_MIN", otherFields);
                        } else {
                            dataCodeMap.put("SURF_CHN_MAIN_MIN", mainFields);
                        }
                    } else {
                        dataCodeMap.put("SURF_CHN_MAIN_MIN", mainFields);
                        dataCodeMap.put("SURF_CHN_OTHER_MIN", otherFields);
                    }
                }
            }
            if ("all".equals(stationType)) {
                if (trafficStationNums.length() > 0) {
                    String trafficField = fieldMap.get("traffic");
                    dataCodeMap.put("SURF_CHN_TRAFW_MUL", trafficField);
                }
            }
        } else if ("AnyTimes".equals(timeType) && "fz".equals(dataKind)) {

        } else if ("rz".equals(dataKind)) {
            String mainFields = fieldMap.get("main");
            String otherFields = fieldMap.get("other");
            dataCodeMap.put("SURF_CHN_MAIN_MIN", mainFields);
            dataCodeMap.put("SURF_CHN_OTHER_MIN", otherFields);
        }
        return dataCodeMap;
    }

    /**
     * 对站号列表进行分组
     *
     * @param stationNums 站号列表
     * @return 站号分组列表
     */
    private List<String> getStationGroup(String stationNums) {
        List<String> stationGroupList = new ArrayList<>();
        String[] stationNumArray = stationNums.split(",");
        int stationCount = stationNumArray.length;
        if (stationCount < 1000) {
            stationGroupList.add(stationNums);
        } else {
            int size = stationCount / 999;
            int mode = stationCount % 999;
            if (mode > 0) {
                size += 1;
            }
            for (int i = 0; i < size; i++) {
                StringBuilder builder = new StringBuilder();
                int start = i * 999;
                int end = (i + 1) * 999;
                if (stationCount < end) {
                    end = stationCount;
                }
                for (int j = start; j < end; j++) {
                    builder.append(stationNumArray[j] + ",");
                }
                String groupStationNums = builder.toString();
                if (groupStationNums.length() > 0) {
                    groupStationNums = groupStationNums.substring(0, groupStationNums.length() - 1);
                    stationGroupList.add(groupStationNums);
                }
            }
        }
        logger.info("站点数:" + stationCount + ",分组数:" + stationGroupList.size());
        return stationGroupList;
    }

    /**
     * 获取任意时次的接口参数
     *
     * @param stationGrouopList  站号列表
     * @param trafficStationNums 交通站站点表
     * @param param              参数
     * @param fieldMap           字段映射
     * @return
     */
    private Map<String, List<Map<String, String>>> getAnyTimeInterfaceParamMap(List<String> stationGrouopList, String trafficStationNums, Map<String, Object> param, Map<String, String> fieldMap, String stationKind, Map<String, String> dataCodeMap) {
        Map<String, List<Map<String, String>>> interfaceParamMap = new HashMap<String, List<Map<String, String>>>();
        String interfaceName = param.get("interfaceName").toString();
        Map<String, String> queryParam = new HashMap<String, String>();
        Map<String, String> trafficParam = null;
        if (trafficStationNums.length() > 0) {
            trafficParam = new HashMap<String, String>();
            trafficParam.put("dataCode", "SURF_CHN_TRAFW_MUL");
            trafficParam.put("elements", fieldMap.get("traffic"));
        }
        String dataCode = param.get("dataCode").toString();
        queryParam.put("dataCode", dataCode);
        String cimissElements = "";
        if (dataCode.equals("SURF_CHN_MUL_HOR")) {
            cimissElements = fieldMap.get("hour");
        } else if (dataCode.equals("SURF_CHN_OTHER_MIN")) {
            cimissElements = fieldMap.get("other");
        } else if (dataCode.equals("SURF_WEA_CBF_MUL_MIN_MAIN_TAB")) {
            cimissElements = fieldMap.get("oneMin");
        }
        queryParam.put("elements", cimissElements);
        String eleValueRanges = param.get("eleValueRanges").toString();
        Map<String, String> eleValueRangesChangeMap = commonConditionChange(eleValueRanges, ";");
        eleValueRanges = eleValueRangesChangeMap.get("value");
        queryParam.put("eleValueRanges", eleValueRanges);
        if (trafficParam != null) {
            trafficParam.put("eleValueRanges", eleValueRanges);
        }
        if (param.containsKey("times")) {
            String times = param.get("times").toString();
            queryParam.put("times", times);
            if (trafficParam != null) {
                trafficParam.put("times", times);
            }
        } else {
            String timeRange = param.get("timeRange").toString();
            queryParam.put("timeRange", timeRange);
            if (trafficParam != null) {
                trafficParam.put("timeRange", timeRange);
            }
        }
        if ("statSurfEleByStaID".equals(interfaceName)) {
            String statEles = param.get("statEles").toString();
            Map<String, String> statSurfEleChangeMap = commonConditionChange(statEles, ",");
            statEles = statSurfEleChangeMap.get("value");
            StringBuilder statSurfEleBuilder = new StringBuilder();
            String[] split = statEles.split(",");
            Map<String, String> statSurfEleMap = new HashMap<String, String>();
            for (String segment : split) {
                String[] segmentSplit = segment.split(":");
                if (segmentSplit.length == 2) {
                    statSurfEleBuilder.append(segmentSplit[1].trim() + "_" + segmentSplit[0].trim() + ",");
                }
            }
            statEles = statSurfEleBuilder.toString();
            if (statEles.length() > 0) {
                statEles = statEles.substring(0, statEles.length() - 1);
            }
            queryParam.put("statEles", statEles);
            if (trafficParam != null) {
                trafficParam.put("statEles", statEles);
                dataCodeMap.put("SURF_CHN_TRAFW_MUL", fieldMap.get("traffic") + "," + statEles);
            }
            dataCodeMap.put(dataCode, cimissElements + "," + statEles);
        } else {
            if (trafficParam != null) {
                dataCodeMap.put("SURF_CHN_TRAFW_MUL", fieldMap.get("traffic"));
            }
            dataCodeMap.put(dataCode, cimissElements);
        }

        List<Map<String, String>> queryParamList = new ArrayList<Map<String, String>>();
        for (String stationNums : stationGrouopList) {
            Map<String, String> queryParamMap = new HashMap<String, String>();
            queryParamMap.putAll(queryParam);
            queryParamMap.put("staIds", stationNums);
            queryParamList.add(queryParamMap);
        }
        interfaceParamMap.put(interfaceName, queryParamList);
        if ("all".equals(stationKind)) {
            if (trafficParam != null) {
                trafficParam.put("staIds", trafficStationNums);
                if (dataCode.contains("HOR")) {
                    String trafficEleValueRanges = "";
                    if (trafficParam.containsKey("eleValueRanges")) {
                        trafficEleValueRanges = trafficParam.get("eleValueRanges");
                        trafficEleValueRanges += ";Min:0";
                    } else {
                        trafficEleValueRanges = "Min:0";
                    }
                    trafficParam.put("eleValueRanges", trafficEleValueRanges);
                }
                queryParamList.add(trafficParam);
            }
//            List<Map<String, String>> trafficParamList = new ArrayList<Map<String, String>>();
//
//            trafficParamList.add(trafficParam);
//
//
//            if("statSurfEleByStaID".equals(interfaceName)){
//                interfaceParamMap.put("statSurfEleByStaIDRange", trafficParamList);
//            }else if("getSurfEleByTimeAndStaID".equals(interfaceName)){
//                interfaceParamMap.put("getSurfEleByTimeAndStaIdRange", trafficParamList);
//            }else{
//                queryParamList.add(trafficParam);
//            }
        }
        return interfaceParamMap;
    }

    /**
     * 获取接口与参数映射
     *
     * @param dataCodeMap       数据代码
     * @param stationGrouopList 站号分组
     * @param param             查询条件
     * @param timeType          时间类型
     * @return
     */
    private Map<String, List<Map<String, String>>> getInterfaceParamMap(Map<String, String> dataCodeMap, List<String> stationGrouopList, String trafficStationNums, Map<String, Object> param, String timeType, String dataKind) {
        Map<String, List<Map<String, String>>> interfaceParamMap = new HashMap<String, List<Map<String, String>>>();
        List<Map<String, String>> basicParamList = joinBasicParam(dataCodeMap, stationGrouopList, trafficStationNums);
        //添加共同条件
        addCommonParam(basicParamList, param);
        if ("SingleTime".equals(timeType) && "fz".equals(dataKind)) {
            //根据datacode来区分
            List<Map<String, String>> trafficList = new ArrayList<Map<String, String>>();
            List<Map<String, String>> otherList = new ArrayList<Map<String, String>>();
            for (Map<String, String> cimissParam : basicParamList) {
                String dataCode = cimissParam.get("dataCode");
                if ("SURF_CHN_TRAFW_MUL".equals(dataCode)) {
                    trafficList.add(cimissParam);
                } else {
                    otherList.add(cimissParam);
                }
            }
            interfaceParamMap.put("getSurfEleByTimeAndStaID", otherList);
            if (trafficList.size() > 0) {
                otherList.addAll(trafficList);
                //interfaceParamMap.put("getSurfEleByTimeAndStaID", trafficList);
            }
        } else if ("SingleTime".equals(timeType) && "rz".equals(dataKind)) {

        } else if ("AnyTimes".equals(timeType) && "rz".equals(dataKind)) {

        }
        return interfaceParamMap;
    }


    /**
     * 拼接基本参数:资料代码,查询字段，查询站点
     *
     * @param dataCodeMap       资料代码映射
     * @param stationGrouopList 站号分组列表
     * @return
     */
    List<Map<String, String>> joinBasicParam(Map<String, String> dataCodeMap, List<String> stationGrouopList, String trafficStationNums) {
        List<Map<String, String>> basicParamList = new ArrayList<Map<String, String>>();
        for (String dataCode : dataCodeMap.keySet()) {
            String elements = dataCodeMap.get(dataCode);
            if (dataCode.equals("SURF_CHN_TRAFW_MUL")) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("dataCode", dataCode);
                param.put("elements", elements);
                param.put("staIds", trafficStationNums);
                basicParamList.add(param);
            } else {
                for (String stationNums : stationGrouopList) {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("dataCode", dataCode);
                    param.put("elements", elements);
                    param.put("staIds", stationNums);
                    basicParamList.add(param);
                }
            }
        }
        return basicParamList;
    }

    /**
     * 添加通用参数：如排序，条件等
     *
     * @param basicParamList 基础参数
     * @param param          查询参数
     */
    private void addCommonParam(List<Map<String, String>> basicParamList, Map<String, Object> param) {
        Map<String, String> conditonMap = new HashMap<String, String>();
        String orderBy = "";
        String orderByField = "";
        //排序字段
        if (param.containsKey("orderBy")) {
            orderBy = param.get("orderBy").toString();
            Map<String, String> orderByMap = commonConditionChange(orderBy, ",");
            if (basicParamList.size() == 1) {
                //因为多条件下排序对结果无意义
                conditonMap.put("orderBy", orderByMap.get("value"));
                orderByField = orderByMap.get("fields");
            }
        }
        String[] orderByFieldArray = null;
        if (orderByField.length() > 0) {
            orderByFieldArray = orderByField.split(",");
        } else {
            orderByFieldArray = new String[0];
        }
        String eleValueRanges = "";
        //要素值范围
        if (param.containsKey("eleValueRanges")) {
            eleValueRanges = param.get("eleValueRanges").toString();
            Map<String, String> eleValueRangesMap = commonConditionChange(eleValueRanges, ";");
            conditonMap.put("eleValueRanges", eleValueRangesMap.get("value"));
        }
        if (param.containsKey("times")) {
            conditonMap.put("times", param.get("times").toString());
        }
        if (conditonMap.size() > 0) {
            for (Map<String, String> basicParam : basicParamList) {
                //排序字段必须包含在查询字段中
                String elements = basicParam.get("elements");
                if (orderByFieldArray.length > 0) {
                    elements = addOrderByField(elements, orderByFieldArray);
                }
                basicParam.put("elements", elements);
                basicParam.putAll(conditonMap);
            }
        }
    }

    /**
     * 添加排序字段
     *
     * @param elements          原查询字段
     * @param orderByFieldArray
     * @return
     */
    private String addOrderByField(String elements, String[] orderByFieldArray) {
        Map<String, Boolean> isAddFlagMap = new HashMap<String, Boolean>();
        for (int i = 0; i < orderByFieldArray.length; i++) {
            isAddFlagMap.put(orderByFieldArray[i], false);
        }
        String[] fieldSplit = elements.split(",");
        for (int i = 0; i < orderByFieldArray.length; i++) {
            String orderByField = orderByFieldArray[i];
            for (int j = 0; j < fieldSplit.length; j++) {
                if (orderByField.equals(fieldSplit[j])) {
                    isAddFlagMap.put(orderByField, true);
                    break;
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String orderByField : isAddFlagMap.keySet()) {
            boolean flag = isAddFlagMap.get(orderByField);
            if (!flag) {
                builder.append(orderByField + ",");
            }
        }
        String addElements = builder.toString();
        String returnElments = elements;
        if (addElements.length() > 0) {
            addElements = addElements.substring(0, addElements.length() - 1);
            returnElments += "," + addElements;
        }
        return returnElments;
    }


    /**
     * 条件参数转换：条件中包含WEB字段,需要转化为相应的CIMISS字段
     *
     * @param condition 条件值
     * @param splitChar 分隔符
     * @return
     */
    private Map<String, String> commonConditionChange(String condition, String splitChar) {
        StringBuilder builder = new StringBuilder();
        StringBuilder fieldBuilder = new StringBuilder();
        String[] split = condition.split(splitChar);
        for (String segment : split) {
            int index = segment.indexOf(":");
            if (index > -1) {
                String webField = segment.substring(0, index).trim();
                String value = segment.substring(index + 1);
                String cimissField = "";
                if (webToCIMISSStationFieldMap.containsKey(webField)) {
                    cimissField = webToCIMISSStationFieldMap.get(webField);
                } else if (webToCIMISSTimeFieldMap.containsKey(webField)) {
                    cimissField = webToCIMISSTimeFieldMap.get(webField);
                } else if (webToCIMISSHourFieldMap.containsKey(webField)) {
                    cimissField = webToCIMISSHourFieldMap.get(webField);
                } else if (webToCIMISSMainFieldMap.containsKey(webField)) {
                    cimissField = webToCIMISSMainFieldMap.get(webField);
                } else if (webToCIMISSOtherFieldMap.containsKey(webField)) {
                    cimissField = webToCIMISSOtherFieldMap.get(webField);
                } else {
                    logger.error("orderBy或者eleValueRanges 条件参数中包含无映射字段:" + webField);
                }
                if (cimissField.length() > 0) {
                    builder.append(cimissField + ":" + value + splitChar);
                    fieldBuilder.append(cimissField + ",");
                }
            } else {
                logger.error("orderBy或者eleValueRanges 条件参数不符合规范:" + segment);
            }
        }
        String valueAfterChange = builder.toString();
        if (valueAfterChange.length() > 0) {
            valueAfterChange = valueAfterChange.substring(0, valueAfterChange.length() - 1);
        }
        String fieldStr = fieldBuilder.toString();
        if (fieldStr.length() > 0) {
            fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
        }
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("fields", fieldStr);
        returnMap.put("value", valueAfterChange);
        return returnMap;
    }

    /**
     * 将CIMISS结果转化为WEB结果
     *
     * @param queryResultList CIMISS查询结果
     * @param fieldMap        查询要素中包含的CIMISS字段分类
     * @param stationInfoMap  站点信息
     * @param transferMap     字段转换
     * @return
     */
    private JSONArray transferCIMISSToWeb(JSONArray queryResultList, Map<String, String> fieldMap, Map<String, StationInfo> stationInfoMap, Map<String, String> transferMap) {
        //1 获取要转换的字段和新增的站点字段
        Map<String, String> cimissToWebFieldMap = new HashMap<String, String>();
        String[] keyArray = new String[]{"main", "other", "hour", "station", "time", "traffic", "oneMin"};
        List<Map<String, String>> fieldMapList = new ArrayList<Map<String, String>>();
        fieldMapList.add(webToCIMISSMainFieldMap);
        fieldMapList.add(webToCIMISSOtherFieldMap);
        fieldMapList.add(webToCIMISSHourFieldMap);
        fieldMapList.add(webToCIMISSStationFieldMap);
        fieldMapList.add(webToCIMISSTimeFieldMap);
        fieldMapList.add(webToCIMISSTrafficFieldMap);
        fieldMapList.add(webToCIMISSOneMinFieldMap);
        for (int i = 0; i < keyArray.length; i++) {
            String key = keyArray[i];
            Map<String, String> webToCIMISSFieldMap = fieldMapList.get(i);
            getCIMISSToWebField(fieldMap, webToCIMISSFieldMap, key, cimissToWebFieldMap);
        }
        String cimissStationFields = fieldMap.get("station");
        List<String> webStationFieldList = new ArrayList<String>();
        if (cimissStationFields != null && cimissStationFields.length() > 0) {
            Map<String, String> stationCIMISSToWebField = new HashMap<String, String>();
            for (String webField : webToCIMISSStationFieldMap.keySet()) {
                stationCIMISSToWebField.put(webToCIMISSStationFieldMap.get(webField), webField);
            }
            String[] cimissStationFieldArray = cimissStationFields.split(",");
            for (int i = 0; i < cimissStationFieldArray.length; i++) {
                if (stationCIMISSToWebField.containsKey(cimissStationFieldArray[i])) {
                    webStationFieldList.add(stationCIMISSToWebField.get(cimissStationFieldArray[i]));
                }
            }
        }
        JSONArray transferResultList = new JSONArray();
        int size = queryResultList.size();
        for (int i = 0; i < size; i++) {
            JSONObject jsonObject = (JSONObject) queryResultList.get(i);
            JSONObject transferObject = new JSONObject();
            Set<String> keySet = jsonObject.keySet();
            for (String key : keySet) {
                Object val = jsonObject.get(key);
                if (val != null) {
                    String valueStr = val.toString();
//                    if ("999999".equals(valueStr)) {
//                        val = "";
//                    }
                }
                if (cimissToWebFieldMap.containsKey(key)) {
                    String webKey = cimissToWebFieldMap.get(key);
                    if (transferMap != null && transferMap.containsKey(key)) {
                        webKey = transferMap.get(key);
                    }
                    transferObject.put(webKey, val);
                } else if (transferMap != null && transferMap.containsKey(key)) {
                    String webKey = transferMap.get(key);
                    transferObject.put(webKey, val);
                } else {
                    logger.error("不存在CIMISS到WEB的字段映射：" + key);
                }
            }
            boolean isAdd = true;
            if (transferObject.containsKey("STATIONNUM")) {
                String stationNum = transferObject.getString("STATIONNUM");
                if (stationInfoMap.containsKey(stationNum)) {
                    StationInfo stationInfo = stationInfoMap.get(stationNum);
                    for (String stationField : webStationFieldList) {
                        addStationInfo(transferObject, stationInfo, stationField);
                    }
                }
            }
            transferResultList.add(transferObject);
        }
        return transferResultList;
    }

    /**
     * 获取CIMISS字段到WEB字段的映射关系
     *
     * @param fieldMap            查询要素中包含的CIMISS字段分类
     * @param webToCIMISSFieldMap WEB字段到CIMISS字段的映射关系
     * @param key                 CIMISS字段分类
     * @param cimissoWebFieldMap  CIMISS字段到WEB字段的映射关系结果
     */
    private void getCIMISSToWebField(Map<String, String> fieldMap, Map<String, String> webToCIMISSFieldMap, String key, Map<String, String> cimissoWebFieldMap) {
        String webFieldStr = "";
        if (fieldMap.containsKey(key)) {
            webFieldStr = fieldMap.get(key);
        }
        Map<String, String> transferCIMISSToWeb = new HashMap<String, String>();
        for (String webField : webToCIMISSFieldMap.keySet()) {
            transferCIMISSToWeb.put(webToCIMISSFieldMap.get(webField), webField);
        }
        if (webFieldStr.length() > 0 && transferCIMISSToWeb.size() > 0) {
            String[] fieldArray = webFieldStr.split(",");
            for (String cimissField : fieldArray) {
                if (transferCIMISSToWeb.containsKey(cimissField)) {
                    String webField = transferCIMISSToWeb.get(cimissField);
                    if (!cimissoWebFieldMap.containsKey(cimissField)) {
                        cimissoWebFieldMap.put(cimissField, webField);
                    }
                }
            }
        }
    }

    /**
     * 添加站点字段信息
     *
     * @param transferObject 添加对象
     * @param stationInfo    站点对象
     * @param stationField   站点字段
     */
    private void addStationInfo(JSONObject transferObject, StationInfo stationInfo, String stationField) {
        if ("STATIONNAME".equals(stationField)) {
            transferObject.put("STATIONNAME", stationInfo.getStationName());
        } else if ("LATITUDE".equals(stationField)) {
            transferObject.put("LATITUDE", stationInfo.getLatitude());
        } else if ("LONGITUDE".equals(stationField)) {
            transferObject.put("LONGITUDE", stationInfo.getLongitude());
        } else if ("ALTITUDE".equals(stationField)) {
            transferObject.put("ALTITUDE", stationInfo.getAltitude());
        } else if ("PROVINCE".equals(stationField)) {
            transferObject.put("PROVINCE", stationInfo.getProvince());
        } else if ("CITY".equals(stationField)) {
            transferObject.put("CITY", stationInfo.getCity());
        } else if ("STATIONLEVL".equals(stationField)) {
            transferObject.put("STATIONLEVL", stationInfo.getStationLevl());
        } else if ("STATIONTYPE".equals(stationField)) {
            transferObject.put("STATIONTYPE", stationInfo.getStationType());
        } else if ("ADMINCODE".equals(stationField)) {
            transferObject.put("ADMINCODE", stationInfo.getAdminCode());
        } else if ("COUNTY".equals(stationField)) {
            transferObject.put("COUNTY", stationInfo.getCounty());
        }
    }

    /**
     * 对结果进行计算
     *
     * @param queryResultList 查询结果
     * @param calculateArray  计算数组
     */
    private void calculateResult(JSONArray queryResultList, List<Object> calculateArray) {
        ListIterator<Object> iterator = queryResultList.listIterator();
        while (iterator.hasNext()) {
            JSONObject jsonObject = (JSONObject) iterator.next();
            for (Object object : calculateArray) {
                Map maObj = (Map) object;
                String method = maObj.get("method").toString();
                String firstElement = maObj.get("firstElement").toString();
                String secondElement = maObj.get("secondElement").toString();
                String resultElement = maObj.get("resultElement").toString();
                if (jsonObject.containsKey(firstElement) && jsonObject.containsKey(secondElement)) {
                    Object firstElementVal = jsonObject.get(firstElement);
                    Object secondElementVal = jsonObject.get(secondElement);
                    if (firstElementVal == null || secondElementVal == null) {
                        jsonObject.put(resultElement, "");
                    } else {
                        String firstElementValStr = firstElementVal.toString().trim();
                        String secondElementValStr = secondElementVal.toString().trim();
                        if (firstElementValStr.length() == 0 || secondElementValStr.length() == 0) {
                            jsonObject.put(resultElement, "");
                        } else {
                            double firstElementDoubleVal = Double.parseDouble(firstElementValStr);
                            double secondElementDoubleVal = Double.parseDouble(secondElementValStr);
                            if (firstElementDoubleVal > 99999 || secondElementDoubleVal > 99999) {
                                jsonObject.put(resultElement, "");
                            } else {
                                if ("-".equals(method)) {
                                    jsonObject.put(resultElement, firstElementDoubleVal - secondElementDoubleVal);
                                } else if ("+".equals(method)) {
                                    jsonObject.put(resultElement, firstElementDoubleVal + secondElementDoubleVal);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 合并查询结果
     *
     * @param allDataCodeResultArray 查询结果
     * @param dataCodeMap            数据编码与字段映射
     * @param joinResultArray        合并后的结果集合
     */
    private void joinQueryResult(Map<String, JSONArray> allDataCodeResultArray, Map<String, String> dataCodeMap, JSONArray joinResultArray) {
        if (allDataCodeResultArray.containsKey("SURF_CHN_MUL_HOR")) {
            joinResultArray.addAll(allDataCodeResultArray.get("SURF_CHN_MUL_HOR"));
        }
        if (allDataCodeResultArray.containsKey("SURF_CHN_WSET_FTM")) {
            joinResultArray.addAll(allDataCodeResultArray.get("SURF_CHN_WSET_FTM"));
        }
        if (allDataCodeResultArray.containsKey("SURF_CHN_MUL_DAY")) {
            joinResultArray.addAll(allDataCodeResultArray.get("SURF_CHN_MUL_DAY"));
        }
        if (allDataCodeResultArray.containsKey("SURF_CHN_TRAFW_MUL")) {
            joinResultArray.addAll(allDataCodeResultArray.get("SURF_CHN_TRAFW_MUL"));
        }
        if (allDataCodeResultArray.containsKey("SURF_WEA_CBF_MUL_MIN_MAIN_TAB")) {
            joinResultArray.addAll(allDataCodeResultArray.get("SURF_WEA_CBF_MUL_MIN_MAIN_TAB"));
        }
        if (allDataCodeResultArray.containsKey("SURF_WEA_CBF_PRE_MIN_TAB")) {
            joinResultArray.addAll(allDataCodeResultArray.get("SURF_WEA_CBF_PRE_MIN_TAB"));
        }
        if (allDataCodeResultArray.containsKey("SURF_CHN_MAIN_MIN") || allDataCodeResultArray.containsKey("SURF_CHN_OTHER_MIN")) {
            Map<String, JSONObject> stationMap = new HashMap<String, JSONObject>();
            int index = 0;
            String historyFields = "";
            String[] codeArray = new String[]{"SURF_CHN_MAIN_MIN", "SURF_CHN_OTHER_MIN"};
            for (String dataCode : codeArray) {
                if (allDataCodeResultArray.containsKey(dataCode)) {
                    JSONArray dataArray = allDataCodeResultArray.get(dataCode);
                    String fields = dataCodeMap.get(dataCode);
                    String[] currentFieldArray = fields.split(",");
                    if (historyFields.length() == 0) {
                        historyFields += fields;
                    } else {
                        historyFields += "," + fields;
                    }
                    String[] historyFieldArray = historyFields.split(",");
                    int size = dataArray.size();
                    Map<String, Boolean> existStationFlagMap = new HashMap<String, Boolean>();
                    for (String indexStationNum : stationMap.keySet()) {
                        existStationFlagMap.put(indexStationNum, false);
                    }
                    for (int i = 0; i < size; i++) {
                        JSONObject obj = (JSONObject) dataArray.get(i);
                        String stationNum = obj.getString("Station_Id_C");
                        if (index == 0) {
                            stationMap.put(stationNum, obj);
                        } else {
                            if (stationMap.containsKey(stationNum)) {
                                existStationFlagMap.put(stationNum, true);
                                //合并字段
                                JSONObject existDataCodeObj = stationMap.get(stationNum);
                                Set<String> keySet = obj.keySet();
                                for (String key : keySet) {
                                    if (!existDataCodeObj.containsKey(key)) {
                                        existDataCodeObj.put(key, obj.get(key));
                                    }
                                }
                            } else {
                                //代表历史无该站点,需要补充历史字段
                                stationMap.put(stationNum, obj);
                                //增补新增站点的历史站点数据
                                for (int j = 0; j < historyFieldArray.length; j++) {
                                    if (!obj.containsKey(historyFieldArray[j])) {
                                        obj.put(historyFieldArray[j], "");
                                    }
                                }
                            }
                        }
                    }
                    //代表本次中无历史站点，需要补充当前字段
                    for (String stationNum : existStationFlagMap.keySet()) {
                        boolean existFlag = existStationFlagMap.get(stationNum);
                        if (!existFlag) {
                            JSONObject obj = stationMap.get(stationNum);
                            for (int j = 0; j < currentFieldArray.length; j++) {
                                if (!obj.containsKey(currentFieldArray[j])) {
                                    obj.put(currentFieldArray[j], "");
                                }
                            }
                        }
                    }
                    index++;
                }
            }
            for (String stationNum : stationMap.keySet()) {
                joinResultArray.add(stationMap.get(stationNum));
            }
        } else if (dataCodeMap != null) {
            if (dataCodeMap.containsKey("SURF_CHN_MAIN_MIN")) {
                joinResultArray.addAll(allDataCodeResultArray.get("SURF_CHN_MAIN_MIN"));
            }
            if (dataCodeMap.containsKey("SURF_CHN_OTHER_MIN")) {
                joinResultArray.addAll(allDataCodeResultArray.get("SURF_CHN_OTHER_MIN"));
            }
        }

    }

    /**
     * 计算多次查询结果
     *
     * @param allDataList
     * @param foreignKey
     * @param accFields
     * @return
     */
    private JSONArray calculateMultiQuery(List<JSONArray> allDataList, String foreignKey, String accFields) {
        List<String> accFieldList = new ArrayList<String>();
        String[] split = accFields.split(",");
        for (String field : split) {
            accFieldList.add(field);
        }
        Map<String, JSONObject> foreignMap = new HashMap<String, JSONObject>();
        Map<String, Integer> commonStationMap = new HashMap<String, Integer>();
        int arrayIndex = -1;
        for (JSONArray array : allDataList) {
            arrayIndex++;
            int size = array.size();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                if (jsonObject.containsKey(foreignKey)) {
                    String foreignVal = jsonObject.get(foreignKey).toString();
                    JSONObject lastJsonObject = null;
                    if (foreignMap.containsKey(foreignVal)) {
                        //统计累计次数
                        commonStationMap.put(foreignVal, commonStationMap.get(foreignVal) + 1);
                        lastJsonObject = foreignMap.get(foreignVal);
                        for (String accKey : accFieldList) {
                            if (lastJsonObject.containsKey(accKey) && jsonObject.containsKey(accKey)) {
                                String lastVal = lastJsonObject.getString(accKey);
                                String currentVal = jsonObject.getString(accKey);
                                if (currentVal.length() > 0) {
                                    if (lastVal.length() > 0) {
                                        double val = Double.parseDouble(lastVal) + Double.parseDouble(currentVal);
                                        lastJsonObject.put(accKey, fix(Double.toString(val), 1));
                                    } else {
                                        lastJsonObject.put(accKey, currentVal);
                                    }
                                }
                            }
                        }
                    } else {
                        if (foreignVal.equals("57385")) {
                            logger.info("57385 SUMPRE:" + arrayIndex + "," + jsonObject.get("SUMPRE"));
                        }
                        //解除新增的不一致性
                        if (arrayIndex == 0) {
                            foreignMap.put(foreignVal, jsonObject);
                            commonStationMap.put(foreignVal, 1);
                        }
                    }
                }
            }
        }
        JSONArray resultJSONArray = new JSONArray();
        for (String key : foreignMap.keySet()) {
            int count = commonStationMap.get(key);
            //保证每次查询的数据都参与计算
            if (count == allDataList.size()) {
                resultJSONArray.add(foreignMap.get(key));
            }
        }
        return resultJSONArray;
    }

    public String fix(String number, int size) {
        if (StringUtils.isEmpty(number)) {
            return "";
        }
        BigDecimal bd = new BigDecimal(number).setScale(size, BigDecimal.ROUND_HALF_UP);
        if (bd.intValue() > 9999) {
            return "9999";
        }
        return bd.toString();
    }

    /**
     * @param cimissId      用户ID
     * @param cimissPsw     用户密码
     * @param interfaceName 接口名
     * @param param         参数
     * @return
     */
    private JSONArray queryCIMISSData(String cimissId, String cimissPsw, String interfaceName, HashMap<String, String> param) {
        JSONArray dataArray = new JSONArray();
        CimissDataQueryClientBean client = null;
        try {
            client = new CimissDataQueryClientBean();
            StringBuffer retStr = new StringBuffer();
            int rst = client.callAPI_to_serializedStr(cimissId, cimissPsw, interfaceName, param, "JSON", retStr);
            if (rst == 0) {
                JSONObject jo = (JSONObject) JsonUtils.toObject(retStr.toString());
                if (jo != null) {
                    String retureCode = jo.getString("returnCode");
                    if ("0".equals(retureCode)) {
                        dataArray = jo.getJSONArray("DS");
                    } else {
                        logger.error("CIMISS调用失败,返回码为" + retureCode + ",异常原因:" + jo.getString("returnMessage"));
                    }
                } else {
                    logger.error("CIMISS调用失败,toObjec结果为null");
                }
            } else {
                logger.error("CIMISS调用失败,返回结果:0");
            }

        } catch (Exception e) {
            logger.error("CIMISS执行异常:" + e);
        } finally {
            if (client != null) {
                client.destroyResources();
            }
        }
        return dataArray;
    }


    public List<String> getCimissTaskIdList(int threadIndex, int totalThreadNum, List<String> idList) {
        int size = idList.size();
        int everyThreadNum = size / totalThreadNum;
        if (size % totalThreadNum > 0) {
            everyThreadNum = size / totalThreadNum + 1;
        }
        List<List<String>> containList = new ArrayList<>();
        for (int i = 0; i < totalThreadNum; i++) {
            int startIndex = i * everyThreadNum;
            int endIndex = (i + 1) * everyThreadNum;
            if (endIndex > size) {
                endIndex = size;
            }
            containList.add(idList.subList(startIndex, endIndex));
        }
        return containList.get(threadIndex);
    }

    private static String getBaseInfo(String areaType, String stationKind, Map<String, StationInfo> stationInfoMap) {
        String stationNums = "";
        Map<String, Map<String, String>> areaStationListMap = CacheStationInfo.areaStationListMap;
        Map<String, Map<String, Map<String, StationInfo>>> areaStationInfoMap = CacheStationInfo.areaStationInfoMap;
        if (areaStationListMap.containsKey(areaType)) {
            if ("gj".equals(stationKind)) {
                stationNums = areaStationListMap.get(areaType).get("gj");
                stationInfoMap.putAll(areaStationInfoMap.get(areaType).get("gj"));
            } else {
                stationNums = areaStationListMap.get(areaType).get("gj");
                if (stationNums != null) {
                    stationNums += "," + areaStationListMap.get(areaType).get("qy");
                } else {
                    stationNums += areaStationListMap.get(areaType).get("qy");
                }
                stationInfoMap.putAll(areaStationInfoMap.get(areaType).get("gj"));
                stationInfoMap.putAll(areaStationInfoMap.get(areaType).get("qy"));
            }
        }
        return stationNums;
    }

}
