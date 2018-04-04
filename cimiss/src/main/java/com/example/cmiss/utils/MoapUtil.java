package com.example.cmiss.utils;




import com.example.cmiss.config.DeepCopy;
import com.example.cmiss.config.StationInfo;
import com.example.cmiss.model.Feature;
import com.example.cmiss.model.FeatureCollection;
import com.example.cmiss.model.FeatureJson;
import com.example.cmiss.model.GridJson;
import com.example.cmiss.model.geojson.Geometry;
import com.example.cmiss.model.swan.RadarColor;
import com.example.cmiss.model.swan.SwanDataSet;
import com.example.cmiss.model.swan.SwanGridData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import wContour.Global.PointD;
import wContour.Global.PolyLine;
import wContour.Global.Polygon;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by lm on 2017/6/8.
 */
public class MoapUtil {
    public static Properties prop = PropertiesUtils
            .loadProperties("classpath:yth.properties");
    public static Map<String, String> relateMap = new HashMap<String, String>() {{
        put("TEM_MAX_24H", "temp24hSbtVal");
        put("TEM_MIN_24H", "temp24hSbtVal");
        put("PRE_24H", "pre24hSbtVal");
        put("PRE_1H","pre1hSbtVal");
        put("PRE_3H","pre3hSbtVal");
        put("PRE_6H","pre6hSbtVal");
        put("PRE_12H","pre12hSbtVal");
        put("PRE_24H","pre24hSbtVal");
        put("PRE_0808","pre24hSbtVal");
        put("PRE_2020","pre24hSbtVal");
        put("PRE_0820","pre12hSbtVal");
        put("PRE_2008","pre12hSbtVal");
        put("PRE_0814","pre6hSbtVal");
        put("PRE_1420","pre6hSbtVal");
        put("PRE_2002","pre6hSbtVal");
        put("PRE_0208","pre6hSbtVal");
        put("MAXPRE_0808","pre24hSbtVal");
        put("MAXPRE_2020","pre24hSbtVal");
        put("TEMP","temp24hSbtVal");
        put("CHANGETEMP24","temp24hSbtVal");
        put("MINTEMP24","temp24hSbtVal");
        put("MINTEMP","temp24hSbtVal");
        put("MAXTEMP24","temp24hSbtVal");
        put("MAXTEMP","temp24hSbtVal");
        put("MAXTEMP_0808","temp24hSbtVal");
        put("MAXTEMP_2020","temp24hSbtVal");
        put("MINTEMP_0808","temp24hSbtVal");
        put("MINTEMP_2020","temp24hSbtVal");
        put("VISIBILITY1","visbilitySbtVal");
        put("VISIBILITY10","visbilitySbtVal");
        put("MINVIS","visbilitySbtVal");
        put("VISIBILITY","visbilitySbtVal");
        put("MINVIS_0808","visbilitySbtVal");
        put("MINVIS_2020","visbilitySbtVal");
        put("SUMPRE", "pre24hSbtVal");
        put("SRHU", "SRHUSbtVal");
        put("RELHUMIDITY", "RELHUMIDITYSbtVal");
        put("MINRELHUMIDITY", "RELHUMIDITYSbtVal");
        put("MAXRHU_0808", "RELHUMIDITYSbtVal");
        put("MAXRHU_2020", "RELHUMIDITYSbtVal");
        put("MINRHU_0808", "RELHUMIDITYSbtVal");
        put("MINRHU_2020", "RELHUMIDITYSbtVal");
        put("DEWTEMP", "temp24hSbtVal");
        put("SNOW_DEPTH", "Snow_DepthSbtVal");
        put("MINT", "temp24hSbtVal");
        put("MAXT", "temp24hSbtVal");
        put("SUMPRE", "pre24hSbtVal");
        put("PRE1", "pre24hSbtVal");
        put("TEMP24", "temp24hSbtVal");
        put("RHU24", "RELHUMIDITYSbtVal");
        put("VIS24", "visbilitySbtVal");
        put("PRS24", "pre24hSbtVal");
    }};


    /**
     * 封装moap点数据
     *
     * @param jsonArray
     * @param element   经纬度需放在最前面
     * @return
     */
    public static FeatureJson getPointJson(com.alibaba.fastjson.JSONArray jsonArray, String element,Map<String, StationInfo> stationInfoMap,String num) throws IOException, ClassNotFoundException {
        Map<String, StationInfo> stationInfos = stationInfoMap;
        String[] elements = element.split(",");
        FeatureJson featureJson = new FeatureJson();
        featureJson.setStatus("1");
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setType("FeatureCollection");
        Properties properties = new Properties();
        featureCollection.setProperties(properties);
        Feature feature = new Feature();
        String[] field = new String[elements.length];
        String[] filedType = new String[elements.length];
        int eleLength = elements.length;
        //加载field属性,类型
        for (int i = 0; i < field.length-2; i++) {
            field[i] = elements[i + 2];
            filedType[i] = "String";
        }
        field[field.length-2] = "status";
        field[field.length-1] = "top";
        filedType[field.length-2] = "String";
        filedType[field.length-1] = "String";
        feature.setField(field);
        feature.setFieldType(filedType);
        feature.setGeoType("Point");
        StringBuffer sb = new StringBuffer();
        boolean flag = false;
        int _num = 0;
        if(!num.equals("null")&& !"all".equals(num)){
            flag = true;
            _num = Integer.parseInt(num);
        }

        //要素信息
        for (int i = 0; i < jsonArray.size(); i++) {
            com.alibaba.fastjson.JSONObject json = jsonArray.getJSONObject(i);
            String stationId = json.getString("STATIONNUM");
            if(stationInfos.containsKey(stationId)){
                stationInfos.remove(stationId);
            }
            for (int m = 0; m < eleLength; m++) {
                //去掉首尾的空格
                String eleValue=json.get(elements[m])==null ||  json.get(elements[m]).equals("null")? "999999":json.get(elements[m]) + "";
                eleValue=eleValue.trim();
                eleValue+=" ";
                sb.append(eleValue);
            }
            sb.append("1 ");
            if(flag){
                if(i<_num){
                    sb.append("1");
                }else{
                    sb.append("0");
                }
            }else{
                sb.append("-1");
            }
            if (i != jsonArray.size() - 1) {
                sb.append(";");
            }
        }
        boolean pointFlag = false;

        if(stationInfos.size()>0){
            if (jsonArray.size()>0){
                pointFlag = true;
                if(sb.length()>0){
                    sb.append(";");
                }
                for(Map.Entry entry:stationInfos.entrySet()){
                    StationInfo tempMap = (StationInfo) entry.getValue();
                    sb.append(tempMap.getLongitude()+" ");
                    sb.append(tempMap.getLatitude()+" ");
                    for (int m = 0; m < eleLength-2; m++) {
                        sb.append("999999");
                        sb.append(" ");
                    }

                    sb.append("0 ");
                    if(flag){
                        sb.append("0");
                    }else{
                        sb.append("-1");
                    }
                    sb.append(";");
                }
            }else {
                for(Map.Entry entry:stationInfos.entrySet()){
                    StationInfo tempMap = (StationInfo) entry.getValue();
                    sb.append(tempMap.getLongitude()+" ");
                    sb.append(tempMap.getLatitude()+" ");
                    sb.append("999999 ");
                    sb.append(tempMap.getStationNum()+" ");
                    sb.append("0 ");
                    if(flag){
                        sb.append("0");
                    }else{
                        sb.append("-1");
                    }
                    sb.append(";");
                }
            }
            if(pointFlag){
                sb.deleteCharAt(sb.length()-1);
            }
            }

        feature.setData(sb.toString());
        featureCollection.setFeature(feature);
        featureJson.setData(featureCollection);
        return featureJson;
    }


    /**
     * 封装moap点数据
     *
     * @param jsonArray
     * @param element   经纬度需放在最前面
     * @return
     */
    public static FeatureJson getPointJsonAnytimes(com.alibaba.fastjson.JSONArray jsonArray, String element,Map<String, StationInfo> stationInfoMap,String num) throws IOException, ClassNotFoundException {
        Map<String, StationInfo> stationInfos = stationInfoMap;
        String[] elements = element.split(",");
        FeatureJson featureJson = new FeatureJson();
        featureJson.setStatus("1");
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setType("FeatureCollection");
        Properties properties = new Properties();
        featureCollection.setProperties(properties);
        Feature feature = new Feature();
        String[] field = new String[elements.length];
        String[] filedType = new String[elements.length];
        int eleLength = elements.length;
        //加载field属性,类型
        for (int i = 0; i < field.length-2; i++) {
            field[i] = elements[i + 2];
            filedType[i] = "String";
        }
        field[field.length-2] = "status";
        field[field.length-1] = "top";
        filedType[field.length-2] = "String";
        filedType[field.length-1] = "String";
        feature.setField(field);
        feature.setFieldType(filedType);
        feature.setGeoType("Point");
        StringBuffer sb = new StringBuffer();
        boolean flag = false;
        int _num = 0;
        if(!num.equals("null")&& !"all".equals(num)){
            flag = true;
            _num = Integer.parseInt(num);
        }

        //要素信息
        for (int i = 0; i < jsonArray.size(); i++) {
            com.alibaba.fastjson.JSONObject json = jsonArray.getJSONObject(i);
            String stationId = json.getString("STATIONNUM");
            if(stationInfos.containsKey(stationId)){
                stationInfos.remove(stationId);
            }
            for (int m = 0; m < eleLength; m++) {
                //去掉首尾的空格
                String eleValue=json.get(elements[m])==null ||  json.get(elements[m]).equals("null")? "999999":json.get(elements[m]) + "";
                eleValue=eleValue.trim();
                eleValue+=" ";
                sb.append(eleValue);
            }
            sb.append("1 ");
            if(flag){
                if(i<_num){
                    sb.append("1");
                }else{
                    sb.append("0");
                }
            }else{
                sb.append("-1");
            }
            if (i != jsonArray.size() - 1) {
                sb.append(";");
            }
        }

        feature.setData(sb.toString());
        featureCollection.setFeature(feature);
        featureJson.setData(featureCollection);
        return featureJson;
    }


    /**
     * 封装moap面数据,多层次
     *
     * @param jsonArray
     * @param element   经纬度需放在最前面
     * @return
     */
    public static FeatureJson getPolygonMdJson(JSONArray jsonArray, String element,String layerType,String layerValue, String lon, String lat, double xMin, double xMax,
                                               double yMin, double yMax, int xNum, int yNum, String bound) {
        FeatureJson featureJson = new FeatureJson();
        featureJson.setStatus("1");
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setType("FeatureCollection");
        Properties properties=new Properties();
        featureCollection.setProperties(properties);
        Feature feature = new Feature();
        String[] field = new String[]{element};
        String[] filedType = new String[]{"double"};
        feature.setField(field);
        feature.setFieldType(filedType);
        feature.setGeoType("Polygon");
        StringBuffer sb = new StringBuffer();
        int num = jsonArray.size();
        double[] xs = new double[num];
        double[] ys = new double[num];
        double[] values = new double[num];
        String typeValue = relateMap.get(element.toUpperCase());
        String sbvalue = prop.getProperty(typeValue);
        double[] contourValues = new double[sbvalue.split(",").length];
        for (int i = 0; i < sbvalue.split(",").length; i++) {
            contourValues[i] = Double.parseDouble(sbvalue.split(",")[i]);
        }
        for (int i = 0; i < num; i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            if(!(json.get(layerType)+"").equals(layerValue)) continue;
            if(!StringUtils.isEmpty(json.getString(element)) && !json.getString(element).equals("null")){
                xs[i] = Double.parseDouble(json.getString(lon));
                ys[i] = Double.parseDouble(json.getString(lat));
                String tempValue = json.getString(element);
                if ("999990.0".equals(tempValue) || "999999".equals(tempValue) || "999998".equals(tempValue)) {
                    values[i] = 0;
                } else {
                    values[i] = Double.parseDouble(tempValue);
                }
            }
        }
        List<Polygon> list = null;
        String[] bounds =null;
        double[] clipXs = null;
        double[] clipYs = null;
        if(!StringUtils.isEmpty(bound)) {
            bounds=bound.split(";");
            int bsum = bounds.length;
            clipXs = new double[bsum];
            clipYs = new double[bsum];
            //获取边界信息
            for (int i = 0; i < bsum; i++) {
                String[] temp = bounds[i].split(",");
                clipXs[i] = Double.parseDouble(temp[0]);
                clipYs[i] = Double.parseDouble(temp[1]);
            }
        }
        try {
            boolean b = true;
            b = values.length > 1000 ? false : true;
            list = ContourUtil.genCountourLines(xs, ys, values, contourValues, xMin, xMax, yMin, yMax,
                    xNum, yNum, 999990, clipXs, clipYs, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //面
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Polygon polygon = list.get(i);
                PolyLine polyLine = polygon.OutLine;
                int number = polyLine.PointList.size();
                for (int m = 0; m < number; m++) {
                    PointD pointD = polyLine.PointList.get(m);
                    sb.append(pointD.X + " ");
                    if (m != number - 1) {
                        sb.append(pointD.Y + " ");
                    } else {
                        sb.append(pointD.Y + ",");
                    }
                }
                double pvalue = polygon.LowValue;
                if (polygon.IsHighCenter) {
                    if (indexOfArray(contourValues, pvalue) < contourValues.length - 1) {
                        pvalue = contourValues[indexOfArray(contourValues, pvalue) + 1];
                    } else {
                        pvalue = 999999;
                    }
                }
                sb.append(pvalue);
                sb.append(";");
            }
        }
        String resultData = sb.toString();
        if (resultData.contains(";"))
            resultData = resultData.substring(0, resultData.length() - 1);
        feature.setData(resultData);
        featureCollection.setFeature(feature);
        featureJson.setData(featureCollection);
        return featureJson;
    }




    /**
     * 封装moap面数据
     * @param jsonArray 返回的json数组
     * @param eleType 要素类型
     * @param xMin   最小x
     * @param xMax   最大x
     * @param yMin   最小y
     * @param yMax   最大y
     * @param xNum   栅格x行数
     * @param yNum   栅格y行数
     * @param bound  地图裁剪边界
     * @return
     */
    public static  FeatureJson dataToPolygonJason(JSONArray jsonArray, String lon, String lat, String eleType, double xMin, double xMax,
                                                 double yMin, double yMax, int xNum, int yNum, String bound) {
        FeatureJson featureJson = new FeatureJson();
        featureJson.setStatus("1");
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setType("FeatureCollection");
        Properties properties=new Properties();
        featureCollection.setProperties(properties);
        Feature feature = new Feature();
        String[] field = new String[]{eleType};
        String[] filedType = new String[]{"double"};
        feature.setField(field);
        feature.setFieldType(filedType);
        feature.setGeoType("Polygon");
        StringBuffer sb = new StringBuffer();
        int num = jsonArray.size();
        double[] xs = new double[num];
        double[] ys = new double[num];
        double[] values = new double[num];
        String typeValue = relateMap.get(eleType.toUpperCase());
        String sbvalue = prop.getProperty(typeValue);
        double[] contourValues = new double[sbvalue.split(",").length];
        for (int i = 0; i < sbvalue.split(",").length; i++) {
            contourValues[i] = Double.parseDouble(sbvalue.split(",")[i]);
        }
        for (int i = 0; i < num; i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            xs[i] = Double.parseDouble(json.getString(lon));
            ys[i] = Double.parseDouble(json.getString(lat));
            if(!StringUtils.isEmpty(json.getString(eleType)) && !json.getString(eleType).equals("null")){
                String tempValue = json.getString(eleType);
                if ("999990.0".equals(tempValue) || "999999".equals(tempValue) || "999998".equals(tempValue)) {
                    values[i] = 0;
                } else {
                    values[i] = Double.parseDouble(json.getString(eleType));
                }
            }else{
                if(eleType.contains("PRE") || eleType.contains("Pre") || eleType.contains("pre")){
                    values[i] = 0;
                }else{
                    values[i] = 999990;
                }
            }
        }

        List<Polygon> list = null;
        //湖北省边界
        String[] bounds =null;
        double[] clipXs = null;
        double[] clipYs = null;
        if(!StringUtils.isEmpty(bound)) {
            bounds=bound.split(";");
            int bsum = bounds.length;
            clipXs = new double[bsum];
            clipYs = new double[bsum];
            for (int i = 0; i < bsum; i++) {
                String[] temp = bounds[i].split(",");
                clipXs[i] = Double.parseDouble(temp[0]);
                clipYs[i] = Double.parseDouble(temp[1]);
            }
        }
        try {
            boolean b = true;
            b = values.length > 1000 ? false : true;
            if("SNOW_DEPTH".equals(eleType)){
                list = ContourUtil.genCountourLinesEary(xs, ys, values, contourValues, xMin, xMax, yMin, yMax,
                        xNum, yNum, 999990, clipXs, clipYs, b);
            }else{
                list = ContourUtil.genCountourLines(xs, ys, values, contourValues, xMin, xMax, yMin, yMax,
                        xNum, yNum, 999990, clipXs, clipYs, b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //面
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Polygon polygon = list.get(i);
                PolyLine polyLine = polygon.OutLine;
                int number = polyLine.PointList.size();
                for (int m = 0; m < number; m++) {
                    PointD pointD = polyLine.PointList.get(m);
                    sb.append(pointD.X + " ");
                    if (m != number - 1) {
                        sb.append(pointD.Y + " ");
                    } else {
                        sb.append(pointD.Y + ",");
                    }
                }
                double pvalue = polygon.LowValue;
                if (polygon.IsHighCenter && !eleType.equals("SNOW_DEPTH")) {
                    if (indexOfArray(contourValues, pvalue) < contourValues.length - 1) {
                        pvalue = contourValues[indexOfArray(contourValues, pvalue) + 1];
                    } else {
                        pvalue = 999999;
                    }
                }
                sb.append(pvalue);
                sb.append(";");
            }
        }
        String resultData = sb.toString();
        if (resultData.contains(";"))
            resultData = resultData.substring(0, resultData.length() - 1);
        feature.setData(resultData);
        featureCollection.setFeature(feature);
        featureJson.setData(featureCollection);
        return featureJson;
    }


    /**
     * 封装moap面数据
     * @param jsonArray 返回的json数组
     * @param eleType 要素类型
     * @param xMin   最小x
     * @param xMax   最大x
     * @param yMin   最小y
     * @param yMax   最大y
     * @param xNum   栅格x行数
     * @param yNum   栅格y行数
     * @param bound  地图裁剪边界
     * @return
     */
    public static  FeatureJson dataToPolygonJason(com.alibaba.fastjson.JSONArray jsonArray, String lon, String lat, String eleType, double xMin, double xMax,
                                                  double yMin, double yMax, int xNum, int yNum, String bound) {
        FeatureJson featureJson = new FeatureJson();
        featureJson.setStatus("1");
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setType("FeatureCollection");
        Properties properties=new Properties();
        featureCollection.setProperties(properties);
        Feature feature = new Feature();
        String[] field = new String[]{eleType};
        String[] filedType = new String[]{"double"};
        feature.setField(field);
        feature.setFieldType(filedType);
        feature.setGeoType("Polygon");
        StringBuffer sb = new StringBuffer();
        int num = jsonArray.size();
        double[] xs = new double[num];
        double[] ys = new double[num];
        double[] values = new double[num];
        String typeValue = relateMap.get(eleType.toUpperCase());
        String sbvalue = prop.getProperty(typeValue);
        double[] contourValues = new double[sbvalue.split(",").length];
        for (int i = 0; i < sbvalue.split(",").length; i++) {
            contourValues[i] = Double.parseDouble(sbvalue.split(",")[i]);
        }
        for (int i = 0; i < num; i++) {
            com.alibaba.fastjson.JSONObject json = jsonArray.getJSONObject(i);
            xs[i] = Double.parseDouble(json.getString(lon));
            ys[i] = Double.parseDouble(json.getString(lat));
            if(!StringUtils.isEmpty(json.getString(eleType)) && !json.getString(eleType).equals("null")){
                String tempValue = json.getString(eleType);
                if ("999990.0".equals(tempValue) || "999999".equals(tempValue) || "999998".equals(tempValue)) {
                    values[i] = 0;
                } else {
                    values[i] = Double.parseDouble(json.getString(eleType));
                }
            }else{
                if(eleType.contains("PRE") || eleType.contains("Pre") || eleType.contains("pre")){
                    values[i] = 0;
                }else{
                    values[i] = 999990;
                }
            }
        }

        List<Polygon> list = null;
        //湖北省边界
        String[] bounds =null;
        double[] clipXs = null;
        double[] clipYs = null;
        if(!StringUtils.isEmpty(bound)) {
            bounds=bound.split(";");
            int bsum = bounds.length;
            clipXs = new double[bsum];
            clipYs = new double[bsum];
            for (int i = 0; i < bsum; i++) {
                String[] temp = bounds[i].split(",");
                clipXs[i] = Double.parseDouble(temp[0]);
                clipYs[i] = Double.parseDouble(temp[1]);
            }
        }
        try {
            boolean b = true;
            b = values.length > 1000 ? false : true;
            if("SNOW_DEPTH".equals(eleType)){
                list = ContourUtil.genCountourLinesEary(xs, ys, values, contourValues, xMin, xMax, yMin, yMax,
                        xNum, yNum, 999990, clipXs, clipYs, b);
            }else{
                list = ContourUtil.genCountourLines(xs, ys, values, contourValues, xMin, xMax, yMin, yMax,
                        xNum, yNum, 999990, clipXs, clipYs, b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //面
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Polygon polygon = list.get(i);
                PolyLine polyLine = polygon.OutLine;
                int number = polyLine.PointList.size();
                for (int m = 0; m < number; m++) {
                    PointD pointD = polyLine.PointList.get(m);
                    sb.append(pointD.X + " ");
                    if (m != number - 1) {
                        sb.append(pointD.Y + " ");
                    } else {
                        sb.append(pointD.Y + ",");
                    }
                }
                double pvalue = polygon.LowValue;
                if (polygon.IsHighCenter && !eleType.equals("SNOW_DEPTH")) {
                    if (indexOfArray(contourValues, pvalue) < contourValues.length - 1) {
                        pvalue = contourValues[indexOfArray(contourValues, pvalue) + 1];
                    } else {
                        pvalue = 999999;
                    }
                }
                sb.append(pvalue);
                sb.append(";");
            }
        }
        String resultData = sb.toString();
        if (resultData.contains(";"))
            resultData = resultData.substring(0, resultData.length() - 1);
        feature.setData(resultData);
        featureCollection.setFeature(feature);
        featureJson.setData(featureCollection);
        return featureJson;
    }

    public static int indexOfArray(double[] array, double ele) {
        for (int i = 0; i < array.length; i++) {
            double temp = array[i];
            if (temp == ele) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 根据封装好的对象，输出文件
     * @param featureJson
     * @param path
     * @param fileName
     */
    public static void generateFile(FeatureJson featureJson,String path,String fileName){
        File eleFile = new File(path);
        if(!eleFile.exists()){
            eleFile.mkdirs();
        }
        File file = new File(path, fileName);
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            JSONObject jsono = JSONObject.fromObject(featureJson);
            out.write(jsono.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static GridJson getWindGridJson(JSONArray jsonArray, String[] types, String[] nums) {
        GridJson gridJson = new GridJson();
        gridJson.setStatus("1");
        for (String num : nums) {
            String speed = types[0] + num;
            String dir = types[1] + num;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
            }
        }
        return null;
    }

    /**
     * 解析swan数据，提供给前台可以展示的数据
     *
     * @param swanDataSet
     */
    public static List<SwanGridData> getSwanGrid(SwanDataSet swanDataSet, double missingValue, String type,
                                                 int fitting, Double minlon, Double maxlon, Double minlat, Double maxlat) {
        List<SwanGridData> resultList = new ArrayList<>();
        HashMap<String, Object> hashMap = null;
        boolean flag = org.springframework.util.StringUtils.isEmpty(minlon);
        try {
            hashMap = RadarColor.loadRadarColorConfig();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> productColorMap = (HashMap<String, Object>) hashMap.get(type);
        List<HashMap<String, Object>> colors = (List<HashMap<String, Object>>) productColorMap.get("colors");
        double[][] data = swanDataSet.getData()[0];
        double[] xaxis = swanDataSet.getGridX();
        double[] yaxis = swanDataSet.getGridY();
        int xnum = swanDataSet.getXNum();
        int ynum = swanDataSet.getYNum();
        if (fitting == 1) {
            for (int i = 0; i + 1 < ynum; i++) {
                for (int j = 0; j + 2 < xnum; j = j + 2) {
                    SwanGridData swanGridData = new SwanGridData();
                    Map<String, Object> style = new HashMap<>();
                    double[] startLatlng = new double[]{yaxis[i], xaxis[j]};
                    double[] endtLatlng = new double[]{yaxis[i + 1], xaxis[j + 2]};
                    double x = (startLatlng[1] + endtLatlng[1]) / 2;
                    double y = (startLatlng[0] + endtLatlng[0]) / 2;
                    swanGridData.setStartPoint(startLatlng);
                    swanGridData.setEndPoint(endtLatlng);
                    swanGridData.setValue(data[i][j]);
                    if (!flag) {
                        if (x < minlon || x > maxlon || y < minlat || y > maxlat) {
                            continue;
                        }
                    }
                    Color color = null;
                    if (data[i][j] != missingValue) {
                        color = RadarColor.getColorByValue(data[i][j], colors);
                        if (color != null) {
                            style.put("fillcolor", new int[]{color.getRed(), color.getGreen(), color.getBlue()});
                        }
                        swanGridData.setStyle(style);
                    }
                    resultList.add(swanGridData);
                }
            }
            return resultList;
        }
        double intevalX = (swanDataSet.getXMax() - swanDataSet.getXMin()) / (xnum - 1) * fitting;
        double intevalY = (swanDataSet.getYMax() - swanDataSet.getYMin()) / (ynum - 1) * fitting;
        for (int i = 0; (i + fitting / 2) < ynum; i = i + fitting) {
            for (int j = 0; (j + fitting / 2) < xnum; j = j + fitting) {
                SwanGridData swanGridData = new SwanGridData();
                Map<String, Object> style = new HashMap<>();
                double x = xaxis[j + fitting / 2];
                double y = yaxis[i + fitting / 2];
                if (!flag) {
                    if (x < minlon || x > maxlon || y < minlat || y > maxlat) {
                        continue;
                    }
                }
                Color color = null;
                swanGridData.setStartPoint(new double[]{y + intevalY / 2, x - intevalX / 2});
                swanGridData.setEndPoint(new double[]{y - intevalY / 2, x + intevalX / 2});
                swanGridData.setValue(data[i + fitting / 2][j + fitting / 2]);
                if (data[i + fitting / 2][j + fitting / 2] != missingValue) {
                    color = RadarColor.getColorByValue(data[i + fitting / 2][j + fitting / 2], colors);
                    if (color != null) {
                        style.put("fillcolor", new int[]{color.getRed(), color.getGreen(), color.getBlue()});
                    }
                    swanGridData.setStyle(style);
                }
                resultList.add(swanGridData);
            }
        }
        return resultList;
    }


    /**
     * 解析swan数据，提供给前台可以展示的数据
     *
     * @param swanDataSet
     */
    public static com.example.cmiss.model.geojson.FeatureCollection getSwanGrids(SwanDataSet swanDataSet, double missingValue, String type,
                                                                       int fitting) {
        com.example.cmiss.model.geojson.FeatureCollection featureCollection = new com.example.cmiss.model.geojson.FeatureCollection();
        List<com.example.cmiss.model.geojson.Feature> features = new ArrayList<>();
        HashMap<String, Object> hashMap = null;
        try {
            hashMap = RadarColor.loadRadarColorConfig();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> productColorMap = (HashMap<String, Object>) hashMap.get(type);
        List<HashMap<String, Object>> colors = (List<HashMap<String, Object>>) productColorMap.get("colors");
        double[][] data = swanDataSet.getData()[0];
        double[] xaxis = swanDataSet.getGridX();
        double[] yaxis = swanDataSet.getGridY();
        int xnum = swanDataSet.getXNum();
        int ynum = swanDataSet.getYNum();
        for (int i = 0; (i + fitting) < ynum; i = i + fitting) {
            for (int j = 0; (j + fitting) < xnum; j = j + fitting) {
                com.example.cmiss.model.geojson.Feature feature = new com.example.cmiss.model.geojson.Feature();
                List<Double[]> lonLats = new ArrayList<>();
                Geometry geometry = new Geometry();
                geometry.setType("Polygon");
                lonLats.add(new Double[]{xaxis[j], yaxis[i]});
                lonLats.add(new Double[]{xaxis[j + fitting], yaxis[i]});
                lonLats.add(new Double[]{xaxis[j + fitting], yaxis[i + fitting]});
                lonLats.add(new Double[]{xaxis[j], yaxis[i + fitting]});
                List<List<Double[]>> coordinate = new ArrayList<>();
                coordinate.add(lonLats);
                geometry.setCoordinates(coordinate);
                feature.setGeometry(geometry);
                Color color = null;
                if (data[i + fitting / 2][j + fitting / 2] != missingValue) {
                    color = RadarColor.getColorByValue(data[i + fitting / 2][j + fitting / 2], colors);
                    if (color != null) {
                        feature.getProperties().put("value", data[i + fitting / 2][j + fitting / 2]);
                        feature.getProperties().put("fillcolor", "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")");
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                features.add(feature);
            }
        }
        featureCollection.setFeatures(features);
        return featureCollection;
    }

}
