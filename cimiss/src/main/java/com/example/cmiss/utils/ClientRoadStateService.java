package com.example.cmiss.utils;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.rmi.Remote;


@WebService(targetNamespace = "http://ws.cases.hitec/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ClientRoadStateService extends Remote{
  
 	/** 
	* \brief 获取个例实例集 
	* @param XmlDataDocument
	* @return
	* @attention 方法的使用注意事项 
	* @date 2015年3月3日 
	* @note  begin modify by 修改人 修改时间   修改内容摘要说明
	*/
String GetICaseCollection(String XmlDataDocument);
 
 	/** 
	* \brief 获取个例实例数据
	* @param InstanceID
	* @return
	* @attention 方法的使用注意事项 
	* @date 2015年3月3日 
	* @note  begin modify by 修改人 修改时间   修改内容摘要说明
	*/
String GetInstanceDatas(String InstanceID, String elementcategoryid);

	/** 
	* \brief 删除个例
	* @param InstanceID
	* @return
	* @attention 方法的使用注意事项 
	* @date 2015年3月23日 
	* @note  begin modify by 修改人 修改时间   修改内容摘要说明
	*/
String DeleteCase(String InstanceID);

	/** 
	* \brief 根据个例id获取数据类型  
	* @param InstanceID
	* @return
	* @attention 方法的使用注意事项 
	* @date 2015年4月28日 
	* @note  begin modify by 修改人 修改时间   修改内容摘要说明
	*/
String GetInstanceElement(String InstanceID);
}
