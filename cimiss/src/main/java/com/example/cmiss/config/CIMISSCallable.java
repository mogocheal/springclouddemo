package com.example.cmiss.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cmiss.utils.JsonUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by dell on 2017/9/11.
 */
public class CIMISSCallable implements Callable<Object> {

    private Logger logger = Logger.getLogger(getClass());

    private String cimissId;

    private String cimissPsw;

    private List<Map<String,String>> paramList;

    private String interfaceName;

    private int taskNum;

    /**
     * 构造函数
     * @param cimissId CIMISS用户ID
     * @param cimissPsw CIMISS用户密码
     * @param interfaceName 接口名称
     * @param paramList 参数列表
     */
    public CIMISSCallable(String cimissId, String cimissPsw, String interfaceName, List<Map<String,String>> paramList, int taskNum) {
        this.cimissId = cimissId;
        this.cimissPsw = cimissPsw;
        this.interfaceName = interfaceName;
        this.paramList = paramList;
        this.taskNum=taskNum;
    }

    /**
     * 任务调用
     * @return 查询结果
     * @throws Exception
     */
    public Object call() throws Exception {
        logger.info("任务"+taskNum+"启动...");
        Date dateTmp1 = new Date();
        CimissDataQueryClientBean client=null;
        Map<String,JSONArray> dataCodeJsonMap=new HashMap<String,JSONArray>();
        try{
            client=new CimissDataQueryClientBean();
            for(int i=0;i<paramList.size();i++){
                StringBuffer retStr = new StringBuffer() ;
                HashMap<String,String> param=(HashMap<String,String>)(paramList.get(i));
                String dataCode=param.get("dataCode");
                int rst = client.callAPI_to_serializedStr(cimissId,cimissPsw, interfaceName, param, "JSON",retStr);
                JSONArray dataArray=new JSONArray();
                if (rst == 0) {
                    JSONObject jo = (JSONObject) JsonUtils.toObject(retStr.toString());
                    if(jo!= null){
                        String retureCode=jo.getString("returnCode");
                        if("0".equals(retureCode)){
                             dataArray=jo.getJSONArray("DS");
                        }else{
                            logger.error("CIMISS调用失败,返回码为"+retureCode+",异常原因:"+jo.getString("returnMessage"));
                        }
                    }else{
                        logger.error("CIMISS调用失败,toObjec结果为null");
                    }
                } else {
                    logger.error("CIMISS调用失败,返回结果:0");
                }
                JSONArray allArray=null;
                if(dataCodeJsonMap.containsKey(dataCode)){
                    allArray=dataCodeJsonMap.get(dataCode);
                }else{
                    allArray=new JSONArray();
                    dataCodeJsonMap.put(dataCode,allArray);
                }
                allArray.addAll(dataArray);
            }
        }catch (Exception e){
            logger.error("任务"+taskNum+"执行异常:"+e);
        }finally {
            if(client!=null){
                client.destroyResources();
            }
        }
        Date dateTmp2 = new Date();
        long time = dateTmp2.getTime() - dateTmp1.getTime();
        logger.info("任务"+taskNum+"执行完毕，累计耗时["+ time + "]毫秒");

        return dataCodeJsonMap;
    }
}
