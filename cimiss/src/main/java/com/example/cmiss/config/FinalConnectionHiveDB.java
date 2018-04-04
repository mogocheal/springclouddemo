package com.example.cmiss.config;

import com.example.cmiss.utils.CommUtil;
import com.example.cmiss.utils.Formator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * 数据库
 * 
 */
@Controller
public class FinalConnectionHiveDB extends AbstractController {

	@Autowired
	DatabaseConnection databaseConnection;

	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 返回要素信息 用于绘制色斑图
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */

	public List queryRetrun(String sql) {
		boolean flag = false;
		List list = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = databaseConnection.getConnection();
			if (CommUtil.isNullOrBlanK(connection)) {
				flag = true;
				connection = new Cetion().createConnection();
				System.out.println("开始使用创建的连接");
			}
			logger.info("查询的sql:" + sql);
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery(sql);
			list = resultSetToList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (flag) {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("创建的连接已经关闭");
				}
			} else {
				if (connection != null) {
					databaseConnection.releaseConnection(connection);
				}
			}
		}
		return list;
	}

	public int update(String sql) throws SQLException {
		boolean flag = false;
		Connection conn = null;
		Statement statement = null;
		int updateRow = 0;
		try {
			conn = databaseConnection.getConnection();
			if (CommUtil.isNullOrBlanK(conn)) {
				flag = true;
				conn = new Cetion().createConnection();
				System.out.println("开始使用创建的连接");
			}
			statement = conn.createStatement();
			updateRow = statement.executeUpdate(sql);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			statement.close();
			if (flag) {
				if (conn != null) {
					conn.close();
					System.out.println("创建的连接已经关闭");
				}
			} else {
				if (conn != null) {
					databaseConnection.releaseConnection(conn);
				}
			}
		}
		return updateRow;
	}

	/**
	 * 
	 * 描述：@param sql 描述：@return 描述：@throws SQLException
	 */
	@RequestMapping("/execute")
	public void execute(HttpServletRequest request,
			@RequestParam(required = true) String sql,
			HttpServletResponse response) throws SQLException {
		boolean flag = false;
		Connection conn = null;
		Statement statement = null;
		int updateRow = 0;
		try {
			conn = databaseConnection.getConnection();
			if (CommUtil.isNullOrBlanK(conn)) {
				flag = true;
				conn = new Cetion().createConnection();
				System.out.println("开始使用创建的连接");
			}
			statement = conn.createStatement();
			updateRow = statement.executeUpdate(sql);
			System.out.println("执行的sql:" + sql);
			conn.commit();
			this.printJsonObjectData(updateRow, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			statement.close();
			if (flag) {
				if (conn != null) {
					conn.close();
					System.out.println("创建的连接已经关闭");
				}
			} else {
				if (conn != null) {
					databaseConnection.releaseConnection(conn);
				}
			}
		}
		// return updateRow;
	}

	/**
	 * 返回要素信息 用于 补国家站没到的站点（颜色标注为红色）
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */

	@RequestMapping("/fillKnife")
	public void fillKnife(HttpServletRequest request,
			@RequestParam(required = true) String sql,
			HttpServletResponse response) throws SQLException {
		boolean flag = false;
		List list = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = databaseConnection.getConnection();
			if (CommUtil.isNullOrBlanK(connection)) {
				flag = true;
				connection = new Cetion().createConnection();
				System.out.println("开始使用创建的连接");
			}
			logger.info("查询的sql:" + sql);
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery(sql);
			list = resultSetToList(resultSet);
			this.printJsonObjectData(list, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (flag) {
				if (connection != null) {
					connection.close();
					System.out.println("创建的连接已经关闭");
				}
			} else {
				if (connection != null) {
					databaseConnection.releaseConnection(connection);
				}
			}
		}
	}

	@RequestMapping("/addStatistics")
	public void addStatistics(HttpServletRequest request,
			HttpServletResponse response) throws SQLException,
			UnsupportedEncodingException {
		// request.setCharacterEncoding("UTF-8");
		if (request.getParameter("data").contains(",")) {
			String[] data = request.getParameter("data").split(",");
			String productName = data[0], areaName = data[1];
			String visitIP = getIpAddr(request);
			String visitTime = sf.format(new Date());
			String row = visitIP + "," + visitTime + "," + productName + ","
					+ areaName;
			String sqlString = "UPSERT INTO PRODUCT_VIEWS_LOG (ROW_ID, PRODUCTNAME, AREANAME, VISITTIME, VISITIP) "
					+ "VALUES ('"
					+ row
					+ "', '"
					+ productName
					+ "', '"
					+ areaName
					+ "', to_date('"
					+ visitTime
					+ "','yyyy-MM-dd HH:mm:ss'), '" + visitIP + "')";
			update(sqlString);
		}
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 从数据库中取数据的方式 --这是最早的方式 描述：@param time 描述：@return
	 * 
	 * @throws SQLException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/hbase")
	public void query(HttpServletRequest request,
                      @RequestParam(required = true) String sql,
                      @RequestParam(required = false) String ts,// 阈值 格式 key:value
                      HttpServletResponse response) throws SQLException {
		if (sql.contains("@") || sql.contains("*")) {// 这条sql是 任意时段 分钟雨量的sql
														// 每条子ql用@拼接
			// 增加排序功能
			int limit = -1;
			int limitIndex = sql.indexOf("LIMIT");
			if (limitIndex > -1) {
				limit = Integer.parseInt(sql.substring(limitIndex + 5).trim());
			}
			int orderByIndex = sql.indexOf("ORDER BY");
			List<String> orderByElementList = new ArrayList<String>();
			List<String> orderByMethodList = new ArrayList<String>();
			if (orderByIndex > -1) {
				String elementStr = "";
				if (limitIndex > -1) {
					elementStr = sql.substring(orderByIndex + 8, limitIndex);
				} else {
					elementStr = sql.substring(orderByIndex + 8);
				}
				sql = sql.substring(0, orderByIndex);
				String[] split = elementStr.split(",");
				if (split.length > 0) {
					for (int i = 0; i < split.length; i++) {
						String element = split[i].trim();
						String[] elementSplit = element.split(" ");
						int kIndex = -1;
						for (int j = 0; j < elementSplit.length; j++) {
							String part = elementSplit[j].trim();
							if (part.length() > 0) {
								kIndex++;
								if (kIndex == 0) {
									orderByElementList.add(part);
								} else if (kIndex == 1) {
									orderByMethodList.add(part);
								}
							}
						}
						if (kIndex == 0) {
							orderByMethodList.add("ASC");
						}
					}
				}
			}
			// 后台计算分钟雨量值
			// Connection connection = null;
			// PreparedStatement statement = null;
			// ResultSet resultSet = null;
			List list = null;
			List listgj = null;
			String[] sqls = sql.split("@");
			// String sss = sqls[0];
			long l1 = System.currentTimeMillis();
			// 计算分钟雨量需要 先计算区域站 后 加上国家站的数据
			// 区域站计算
			list = computePre(sqls, ts, "SURF_DATA_REG");
			if (sql.contains("SURF_DATA_REG") || sql.contains("surf_data_reg")) {// 查询区域站的																	// 并上国家站数据
				// 国家站计算
				listgj = computePre(sqls, ts, "SURF_DATA");
				list.addAll(listgj);
			}
			List returnList = new ArrayList();
			// 暂时只支持单元素排序
			if (orderByElementList.size() > 0 && limit > 0) {
				if (list.size() <= limit) {
					returnList.addAll(list);
				} else {
					String key = orderByElementList.get(0);
					String orderMethod = orderByMethodList.get(0).toUpperCase();
					List<Double> valueList = new ArrayList<Double>();
					Map<Double, List<Map<String, Object>>> classfyMap = new HashMap<Double, List<Map<String, Object>>>();
					for (Object obj : list) {
						Map<String, Object> objMap = (Map<String, Object>) obj;
						if (objMap.containsKey(key)) {
							String value = objMap.get(key).toString();
							double valuedb = Double.parseDouble(value);
							objMap.put(key, Double.toString(valuedb));
							if (!valueList.contains(valuedb)) {
								valueList.add(valuedb);
							}
							List<Map<String, Object>> valList = null;
							if (classfyMap.containsKey(valuedb)) {
								valList = classfyMap.get(valuedb);
							} else {
								valList = new ArrayList<Map<String, Object>>();
								classfyMap.put(valuedb, valList);
							}
							valList.add(objMap);
						}
					}
					int sum = 0;
					// 对valueList排序
					for (int i = 0; i < valueList.size() - 1; i++) {
						for (int j = i + 1; j < valueList.size(); j++) {
							double firstVal = valueList.get(i);
							double secondVal = valueList.get(j);
							if (orderMethod.equals("ASC")) {
								if (secondVal < firstVal) {
									valueList.set(i, secondVal);
									valueList.set(j, firstVal);
								}
							} else {
								if (firstVal < secondVal) {
									valueList.set(i, secondVal);
									valueList.set(j, firstVal);
								}
							}
						}
						double orderVal = valueList.get(i);
						int size = classfyMap.get(orderVal).size();
						sum += size;
						if (sum < limit) {
							returnList.addAll(classfyMap.get(orderVal));
						} else {
							int addSize = size - (sum - limit);
							for (int k = 0; k < addSize; k++) {
								returnList.add(classfyMap.get(orderVal).get(k));
							}
							break;
						}
					}
				}
			} else {
				returnList.addAll(list);
			}
			long l2 = System.currentTimeMillis();
			System.out.println("------------------------------");
			System.out.println("共耗时：" + (l2 - l1));
			this.printJsonObjectData(returnList, response);
		} else {
			boolean flag = false;
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			List list = null;
			List list_gj = null; // 查询区域站时需要将国家站的数据添加进去
			try {
				logger.info("查询的sql:" + sql);
				connection = databaseConnection.getConnection();
				if (CommUtil.isNullOrBlanK(connection)) {
					flag = true;
					connection = new Cetion().createConnection();
					System.out.println("开始使用创建的连接");
				}
				statement = connection.prepareStatement(sql);
				resultSet = statement.executeQuery(sql);
				if (CommUtil.isNullOrBlanK(ts)) {
					list = resultSetToList(resultSet);
				} else {
					if (ts.split(":").length <= 1)
						list = resultSetToList(resultSet);
					else
						list = resultSetToList(resultSet, ts);
				}
				this.printJsonObjectData(list, response);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (flag) {
					if (connection != null) {
						connection.close();
						System.out.println("创建的连接已经关闭");
					}
				} else {
					if (connection != null) {
						databaseConnection.releaseConnection(connection);
					}
				}
			}
		}
	}

	/**
	 * 查询分段时间数据
	 * 
	 * @param sql
	 * @param connection
	 * @param ts
	 * @return
	 */
	public List queryData(String sql, Connection connection, String ts) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List queryList = null;
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery(sql);
			if (CommUtil.isNullOrBlanK(ts)) {
				queryList = resultSetToList(resultSet);
			} else {
				if (ts.split(":").length <= 1) {
					queryList = resultSetToList(resultSet);
				} else {
					queryList = resultSetToList(resultSet, ts);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return queryList;
	}

	/**
	 * 合并数据
	 * 
	 * @param firstDataList
	 * @param secondDataList
	 * @return
	 */
	private List joinData(List firstDataList, List secondDataList) {
		String fieldName = "SUMPRE";
		String stationNumField = "STATIONNUM";
		Map<String, Map<String, Object>> firstStationMap = groupByField(
				firstDataList, stationNumField);
		Map<String, Map<String, Object>> secondStationMap = groupByField(
				secondDataList, stationNumField);
		Set<String> stationSet=new HashSet<String>();
		stationSet.addAll(firstStationMap.keySet());
		stationSet.addAll(secondStationMap.keySet());
		List returnList=new ArrayList();
		for(String stationNum:stationSet){
			double value=0;
			Map<String,Object> returnStationObj=null;
			Map<String,Object> firstStationObj=firstStationMap.get(stationNum);
			Map<String,Object> secondStationObj=secondStationMap.get(stationNum);
			if(firstStationObj!=null && secondStationObj!=null){
				returnStationObj=firstStationObj;
				if(secondStationObj.containsKey(fieldName) && firstStationObj.containsKey(fieldName)){
					BigDecimal b1 = new BigDecimal(secondStationObj.get(fieldName).toString());
					BigDecimal b2 = new BigDecimal(
							firstStationObj.get(fieldName).toString());
					value = b1.add(b2).doubleValue();
					returnStationObj.put(fieldName, value);
				}
			}else{
				if(firstStationObj!=null){
					returnStationObj=firstStationObj;
				}
				if(secondStationObj!=null){
					returnStationObj=secondStationObj;
				}
			}
			if(returnStationObj!=null){
				returnList.add(returnStationObj);
			}
			
		}
		return returnList;
	}

	/**
	 * 对数据按照站号分租
	 * 
	 * @param firstDataList
	 * @param field
	 * @return
	 */
	private Map<String, Map<String, Object>> groupByField(List firstDataList,
			String field) {
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		for (Object obj : firstDataList) {
			Map<String, Object> stationMap = (Map<String, Object>) obj;
			if (stationMap.containsKey(field)) {
				String stationNum = stationMap.get(field).toString();
				if (!map.containsKey(stationNum)) {
					map.put(stationNum, stationMap);
				}
			}
		}
		return map;
	}

	public static List resultSetToList(ResultSet rs)
			throws SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List list = new ArrayList();
		Map rowData = new HashMap();
		while (rs.next()) {
			rowData = new HashMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {// 判断为999999 或者是 null
													// 的2种情况时都为""
				String column = md.getColumnName(i).toUpperCase()
						.endsWith("_AS") ? md.getColumnName(i).toUpperCase()
						.split("_AS")[0] : md.getColumnName(i);
				if (CommUtil.isNullOrBlanK(rs.getObject(i))) {
					rowData.put(column, "");
				} else {
					rowData.put(
							column,
							rs.getObject(i).toString().equals("999999")
									|| rs.getObject(i).toString()
											.equals("99999") ? "" : rs
									.getObject(i));// 从ResultSet获取数据时有两种方式，
													// rs.getObject(int
													// column_index) 和
													// rs.getObject(String
													// column_label)
													// rs.getObject(int
													// column_index)：这种方式直接根据索引从rs对象中取出
													// ，最快rs.getObject(String
													// column_label) ：
													// 这种方式需要先通过label获取到索引，然后再根据索引取数据，比直接利用索引多走了一步.
				}
			}
			list.add(rowData);
		}

		return list;
	}

	public static List resultSetToList(ResultSet rs, String ts)
			throws SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		String tn = ts.split(":")[0];
		String tv = ts.split(":")[1];
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List list = new ArrayList();
		Map rowData = new HashMap();
		while (rs.next()) {
			boolean sign = false;
			rowData = new HashMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {// 判断为999999 或者是 null
													// 的2种情况时都为""
				String column = md.getColumnName(i).toUpperCase()
						.endsWith("_AS") ? md.getColumnName(i).toUpperCase()
						.split("_AS")[0] : md.getColumnName(i);
				if (column.equals(tn)) {
					if (CommUtil.isNullOrBlanK(rs.getObject(i)))
						continue;
					if (Double.parseDouble((rs.getObject(i)).toString()) >= Double
							.parseDouble(tv)
							&& Double.parseDouble((rs.getObject(i)).toString()) < 99999) {
						sign = true;
						rowData.put(
								column,
								CommUtil.isNullOrBlanK(rs.getObject(i))
										|| rs.getObject(i).toString()
												.equals("999999")
										|| rs.getObject(i).toString()
												.equals("99999") ? "" : rs
										.getObject(i));
					}
				} else {
					if (CommUtil.isNullOrBlanK(rs.getObject(i)))
						continue;
					rowData.put(column, CommUtil.isNullOrBlanK(rs.getObject(i))
							|| rs.getObject(i).toString().equals("999999")
							|| rs.getObject(i).toString().equals("99999") ? ""
							: rs.getObject(i));// 从ResultSet获取数据时有两种方式，
												// rs.getObject(int
												// column_index) 和
												// rs.getObject(String
												// column_label)
												// rs.getObject(int
												// column_index)：这种方式直接根据索引从rs对象中取出
												// ，最快rs.getObject(String
												// column_label) ：
												// 这种方式需要先通过label获取到索引，然后再根据索引取数据，比直接利用索引多走了一步.
				}
			}

			if (sign) {
				list.add(rowData);
			}
		}
		return list;
	}

	/**
	 * 组成按照站点编号为key，站点全部数据为value的Map
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Map<String, Object>> resultSetToMap(ResultSet rs)
			throws SQLException {
		Map<String, Map<String, Object>> keyMap = new HashMap<String, Map<String, Object>>();
		if (rs == null)
			return null;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		// List list = new ArrayList();
		Map<String, Object> rowData = new HashMap<String, Object>();
		while (rs.next()) {
			rowData = new HashMap<String, Object>(columnCount);
			for (int i = 1; i <= columnCount; i++) {// 判断为999999 或者是 null
													// 的2种情况时都为""
				String column = md.getColumnName(i).toUpperCase()
						.endsWith("_AS") ? md.getColumnName(i).toUpperCase()
						.split("_AS")[0] : md.getColumnName(i);
				if (CommUtil.isNullOrBlanK(rs.getObject(i))) {
					rowData.put(column, "");
				} else {
					rowData.put(
							column,
							rs.getObject(i).toString().equals("999999")
									|| rs.getObject(i).toString()
											.equals("99999") ? "" : rs
									.getObject(i));// 从ResultSet获取数据时有两种方式，
													// rs.getObject(int
													// column_index) 和
													// rs.getObject(String
													// column_label)
													// rs.getObject(int
													// column_index)：这种方式直接根据索引从rs对象中取出
													// ，最快rs.getObject(String
													// column_label) ：
													// 这种方式需要先通过label获取到索引，然后再根据索引取数据，比直接利用索引多走了一步.
				}
			}
			keyMap.put(String.valueOf(rowData.get("STATIONNUM")), rowData);
		}

		return keyMap;
	}

	public List computePre(String[] sqls, String ts, String qy_gj_type)
			throws SQLException {
		// qy_gj_type 表示区域还是国家表的数据 区域站的分钟雨量需要加上国家站的分钟雨量 SURF_DATA_REG
		if (!qy_gj_type.contains("REG")) {// 国家站 将sql中的SURF_DATA_REG 换成
			for (int i = 0; i < sqls.length; i++) {
				sqls[i] = sqls[i].replaceAll("SURF_DATA_REG", "SURF_DATA")
						.replaceAll("surf_data_reg", "SURF_DATA")
						.replaceAll("sta_info_reg", "sta_info_test")
						.replaceAll("STA_INFO_REG", "STA_INFO_TEST")
						.replaceAll("SURF_DAY_REG_TAB", "SURF_DAY_TAB")
						.replaceAll("surf_day_reg_tab", "SURF_DAY_TAB");
			}
		}
		// 含有@ 的sql 是需要进行相加的sql 含有 $的sql 是需要进行相减的sql 且是 后面减去前面
		boolean flag = false;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List list = null;
		List<Map<String, Object>> listA = null;// listA,和listB是存储进行减法计算的 数据
		List<Map<String, Object>> listB = null;//
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();// list1,list2,list3
																				// 是存储进行加法计算的数据
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();

		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> mapA = new HashMap<String, Map<String, Object>>();// mapA,和mapB是存储进行减法计算的
																							// 数据
		Map<String, Map<String, Object>> mapB = new HashMap<String, Map<String, Object>>();//
		Map<String, Map<String, Object>> map1 = new HashMap<String, Map<String, Object>>();// map1,map2,map3
																							// 是存储进行加法计算的数据
		Map<String, Map<String, Object>> map2 = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> map3 = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> map4 = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> map5 = new HashMap<String, Map<String, Object>>();
		DecimalFormat df = new DecimalFormat("######0.00");
		try {
			connection = databaseConnection.getConnection();
			if (CommUtil.isNullOrBlanK(connection)) {
				flag = true;
				connection = new Cetion().createConnection();
				System.out.println("开始使用创建的连接");
			}
			List<String> tmpList = new ArrayList<String>();
			for (int i = 0; i < sqls.length; i++) {
				map = new HashMap<String, Map<String, Object>>();
				String sql = sqls[i];
				if (i > 0) {// 后面的sql块
					statement = connection.prepareStatement(sql);
					resultSet = statement.executeQuery(sql);
					map = resultSetToMap(resultSet);
					switch (i) {
					case 1:
						map2 = map;
						break;
					case 2:
						map3 = map;
						break;
					}
				} else {// 第一个sql块 这个sql块 会出现相减的情况
					if (sql.contains("*")) {// 这条sql需要做减法
						String[] ss = sql.split("\\*");
						for (int n = 0; n < ss.length; n++) {
							statement = connection.prepareStatement(ss[n]);
							resultSet = statement.executeQuery(ss[n]);
							if (n == 0)
								mapA = resultSetToMap(resultSet);
							else
								mapB = resultSetToMap(resultSet);
						}
						// 将mapB 减去 mapA 的值 存储在list1中
						Map<String, Object> mx = null;
						Map<String, Object> my = null;
						Set<String> keyA = mapA.keySet();
						Set<String> keyB = mapB.keySet();

						for (String b : keyB) {
							mx = mapB.get(b);
							// 此处相减计算以后面的时次mapB的为主，当mapB站点数据为空时，数据为零；mapA中多的站点数据不管
							my = mapA.get(b);
							String sumpre_mx = StringUtils.isNotEmpty(String
									.valueOf(mx.get("PRE_1H"))) ? mx.get(
									"PRE_1H").toString() : "0";
							String sumpre_my = "0";
							if (my != null) {
								sumpre_my = StringUtils.isNotEmpty(String
										.valueOf(my.get("PRE_1H"))) ? my.get(
										"PRE_1H").toString() : "0";
							}
							double dx = Double.parseDouble(sumpre_mx);
							double dy = Double.parseDouble(sumpre_my);

							mx.remove("PRE_1H");
							mx.put("SUMPRE", Math.abs(dx - dy));// 可能会出问题
																// 后期调试需要注意这行计算的结果
																// 是否符合业务需求

							map1.put(String.valueOf(mx.get("STATIONNUM")), mx);
						}
						// for(int x=0;x<listB.size();x++){
						// mx = listB.get(x);
						// for(int y=0;y<listA.size();y++){
						// my = listA.get(y);
						// if(mx.get("STATIONNUM").toString().equals(my.get("STATIONNUM").toString())){
						// String sumpre_mx = mx.get("PRE_1H") != null ?
						// mx.get("PRE_1H").toString() :"0";
						// String sumpre_my = my.get("PRE_1H") != null ?
						// my.get("PRE_1H").toString() :"0";
						// double dx = Double.parseDouble( sumpre_mx);
						// double dy = Double.parseDouble( sumpre_my);
						//
						// mx.remove("PRE_1H");
						// mx.put("SUMPRE",Math.abs( dx-dy));//可能会出问题
						// 后期调试需要注意这行计算的结果 是否符合业务需求
						// break;
						// }
						// }
						// list1.add(mx);
						// }
					} else {// 这条sql不需要进行减法计算
						logger.info("小时累计sql:" + sql);
						statement = connection.prepareStatement(sql);
						resultSet = statement.executeQuery(sql);
						map = resultSetToMap(resultSet);
						// Map<String,Object> mi = null;
						map1 = map;
					}
				}
			}
			Map<String, Object> mi = null;
			Map<String, Object> mj = null;
			Map<String, Object> mk = null;
			if (sqls.length == 1) {// sqls长度为1 并且又包含 @ 或者 * 拼接字符 说明之可能包含*
									// 说明这是一个同小时内的 分钟不同 做减法的sql
				// list5.addAll(list1);
				map5 = map1;
			} else if (sqls.length == 2) {
				map5 = combineMap(map1, map2);
				// for(int i=0;i<list1.size();i++){
				// mi = list1.get(i);
				// for(int j=0;j<list2.size();j++){
				// mj = list2.get(j);
				// if(mi.get("STATIONNUM").toString().equals(mj.get("STATIONNUM").toString())){
				// String sumpre_mi = mi.get("SUMPRE") != null ?
				// mi.get("SUMPRE").toString() :"0";
				// String sumpre_mj = mj.get("SUMPRE") != null ?
				// mj.get("SUMPRE").toString() :"0";
				// double dx = Double.parseDouble( sumpre_mi);
				// double dy = Double.parseDouble( sumpre_mj);
				// mi.remove("SUMPRE");
				// mi.put("SUMPRE", df.format(dy+dx));//可能会出问题 后期调试需要注意这行计算的结果
				// 是否符合业务需求
				// break;
				// }
				// }
				// list4.add(mi);
				// }
				// list5.addAll(list4);
			} else if (sqls.length == 3) {
				map4 = combineMap(map1, map2);
				map5 = combineMap(map4, map3);

				// for(int i=0;i<list1.size();i++){
				// mi = list1.get(i);
				// for(int j=0;j<list2.size();j++){
				// mj = list2.get(j);
				// if(mi.get("STATIONNUM").toString().equals(mj.get("STATIONNUM").toString())){
				// String sumpre_mi = mi.get("SUMPRE") != null ?
				// mi.get("SUMPRE").toString() :"0";
				// String sumpre_mj = mj.get("SUMPRE") != null ?
				// mj.get("SUMPRE").toString() :"0";
				// double dx = Double.parseDouble( sumpre_mi);
				// double dy = Double.parseDouble( sumpre_mj);
				// mi.remove("SUMPRE");
				// mi.put("SUMPRE",df.format( dy+dx));//可能会出问题 后期调试需要注意这行计算的结果
				// 是否符合业务需求
				// break;
				// }
				// }
				// list4.add(mi);
				// }
				// for(int i=0;i<list4.size();i++){
				// mi = list4.get(i);
				// for(int k =0;k<list3.size();k++){
				// mj = list3.get(k);
				// if(mi.get("STATIONNUM").toString().equals(mj.get("STATIONNUM").toString())){
				// String sumpre_mi = mi.get("SUMPRE") != null ?
				// mi.get("SUMPRE").toString() :"0";
				// String sumpre_mj = mj.get("SUMPRE") != null ?
				// mj.get("SUMPRE").toString() :"0";
				// double dx = Double.parseDouble( sumpre_mi);
				// double dy = Double.parseDouble( sumpre_mj);
				// mi.remove("SUMPRE");
				// mi.put("SUMPRE", df.format(dy+dx));//可能会出问题 后期调试需要注意这行计算的结果
				// 是否符合业务需求
				// break;
				// }
				// }
				// list5.add(mi);
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (flag) {
				if (connection != null) {
					connection.close();
					System.out.println("创建的连接已经关闭");
				}
			} else {
				if (connection != null) {
					databaseConnection.releaseConnection(connection);
				}
			}
		}
		List<Map<String, Object>> list6 = new ArrayList<Map<String, Object>>();

		if (StringUtils.isNotEmpty(ts)) {
			String tn = ts.split(":")[0];
			String tv = ts.split(":")[1];
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			for (String key : map5.keySet()) {
				tmpMap = map5.get(key);
				if (Double.parseDouble(tmpMap.get(tn).toString()) >= Double
						.parseDouble(tv)) {
					if (tmpMap.containsKey("SUMPRE")) {
						String value = tmpMap.get("SUMPRE").toString();
						double valuedb = Double.parseDouble(value);
						valuedb = (double) Math.round(valuedb * 10) / 10;
						tmpMap.put("SUMPRE", Double.toString(valuedb));
					}
					list6.add(tmpMap);
				}
			}
		} else {
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			for (String key : map5.keySet()) {
				tmpMap = map5.get(key);
				if (tmpMap.containsKey("SUMPRE")) {
					String value = tmpMap.get("SUMPRE").toString();
					double valuedb = Double.parseDouble(value);
					valuedb = (double) Math.round(valuedb * 10) / 10;
					tmpMap.put("SUMPRE", Double.toString(valuedb));
				}
				// if(Double.parseDouble(tmpMap.get(tn).toString())>Double.parseDouble(tv)){
				list6.add(tmpMap);
				// }
			}
		}

		// list5 = new ArrayList<Map<String,Object>>();
		// if (!CommUtil.isNullOrBlanK(ts)) {//
		// String tn = ts.split(":")[0];
		// String tv = ts.split(":")[1];
		// Map<String,Object> tmpMap = new HashMap<String,Object>();
		// for(int i = 0 ;i<list5.size();i++){
		// tmpMap = list5.get(i);
		// if(Double.parseDouble(tmpMap.get(tn).toString())>Double.parseDouble(tv)){
		// list6.add(tmpMap);
		// }
		// }
		// }else{
		// list6.addAll(list5);
		// }
		return list6;
	}

	public Map<String, Map<String, Object>> combineMap(
			Map<String, Map<String, Object>> map1,
			Map<String, Map<String, Object>> map2) {
		Map<String, Object> mi = null;
		Map<String, Object> mj = null;
		Map<String, Map<String, Object>> rtMap = new HashMap<String, Map<String, Object>>();
		DecimalFormat df = new DecimalFormat("######0.0");
		// 先计算map1里面的所有站点信息
		for (String b : map1.keySet()) {
			mi = map1.get(b);
			mj = map2.get(b);
			String sumpre_my = "0";
			if (mj != null) {
				sumpre_my = StringUtils.isNotEmpty(String.valueOf(mj
						.get("SUMPRE"))) ? mj.get("SUMPRE").toString() : "0";
			}
			String sumpre_mx = StringUtils.isNotEmpty(String.valueOf(mi
					.get("SUMPRE"))) ? mi.get("SUMPRE").toString() : "0";

			double dx = Double.parseDouble(sumpre_mx);
			double dy = Double.parseDouble(sumpre_my);

			mi.put("SUMPRE", df.format(dy + dx));// 可能会出问题 后期调试需要注意这行计算的结果
													// 是否符合业务需求

			rtMap.put(String.valueOf(mi.get("STATIONNUM")), mi);

			map2.remove(b);
		}
		// 单独计算map2里面的多余站点信息
		for (String a : map2.keySet()) {
			mi = map2.get(a);
			String sumpre_mx = StringUtils.isNotEmpty(String.valueOf(mi
					.get("SUMPRE"))) ? mi.get("SUMPRE").toString() : "0";
			double dx = Double.parseDouble(sumpre_mx);
			mi.put("SUMPRE", df.format(dx));

			rtMap.put(String.valueOf(mi.get("STATIONNUM")), mi);
		}

		return rtMap;

	}

	public void querySumRainBetween(String startDate, String endDate) {
		List<String> dateofcache = new ArrayList<String>();
		Set<String> alltime = fillDatetime(startDate, endDate);// [2014043007,2014043008....2014050108]
		Set<String> betweenday = getDay(alltime);// [20140430,20140501]
		for (String bd : betweenday) {
			// 08
			Set<String> day08list = fill08(bd);
			if (alltime.containsAll(day08list)) {
				alltime.removeAll(day08list);
				dateofcache.add(bd + "_08");
			}
			// 20
			Set<String> day20list = fill20(bd);
			if (alltime.containsAll(day20list)) {
				alltime.removeAll(day20list);
				dateofcache.add(bd + "_20");
			}
		}

	}

	/**
	 * 填充 某天 08时的每小时序列返回[2014050109 2014050110 .... 2014050208]
	 * 
	 * @param day
	 * @return
	 */
	public static Set<String> fill08(String day) {
		Set<String> ret = new HashSet<String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar today = Calendar.getInstance();
		try {
			today.setTime(df.parse(day));
			ret.add(day + "08");
			ret.add(day + "07");
			ret.add(day + "06");
			ret.add(day + "05");
			ret.add(day + "04");
			ret.add(day + "03");
			ret.add(day + "02");
			ret.add(day + "01");
			ret.add(day + "00");
			today.add(Calendar.DAY_OF_MONTH, -1);
			day = Formator.getDateStr(today, "yyyyMMdd");
			ret.add(day + "09");
			ret.add(day + "10");
			ret.add(day + "11");
			ret.add(day + "12");
			ret.add(day + "13");
			ret.add(day + "14");
			ret.add(day + "15");
			ret.add(day + "16");
			ret.add(day + "17");
			ret.add(day + "18");
			ret.add(day + "19");
			ret.add(day + "20");
			ret.add(day + "21");
			ret.add(day + "22");
			ret.add(day + "23");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 填充 某天 08时的每小时序列返回[2014050109 2014050110 .... 2014050208]
	 * 
	 * @param day
	 * @return
	 */
	public static Set<String> fill20(String day) {
		Set<String> ret = new HashSet<String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar today = Calendar.getInstance();
		try {
			today.setTime(df.parse(day));
			ret.add(day + "20");
			ret.add(day + "19");
			ret.add(day + "18");
			ret.add(day + "17");
			ret.add(day + "16");
			ret.add(day + "15");
			ret.add(day + "14");
			ret.add(day + "13");
			ret.add(day + "12");
			ret.add(day + "11");
			ret.add(day + "10");
			ret.add(day + "09");
			ret.add(day + "08");
			ret.add(day + "07");
			ret.add(day + "06");
			ret.add(day + "05");
			ret.add(day + "04");
			ret.add(day + "03");
			ret.add(day + "02");
			ret.add(day + "01");
			ret.add(day + "00");
			today.add(Calendar.DAY_OF_MONTH, -1);
			day = Formator.getDateStr(today, "yyyyMMdd");
			ret.add(day + "23");
			ret.add(day + "22");
			ret.add(day + "21");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 获取一个时间段内的日期 如[2014050100...2014050123 2014050200...] 返回
	 * [20140501、20140502]
	 *
	 * @return
	 */
	public static Set<String> getDay(Set<String> alltime) {
		Set<String> ret = new HashSet<String>();
		for (String a : alltime) {
			ret.add(a.substring(0, 8));
		}
		return ret;
	}

	/**
	 * 填充一个时间段内的小时 返回如[2014050100,2014050101,2014050102..]
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Set<String> fillDatetime(String startDate, String endDate) {
		Set<String> ret = new HashSet<String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		try {
			from.setTime(df.parse(startDate));
			to.setTime(df.parse(endDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (from.after(to)) {
			throw new RuntimeException("起始时间不能大于结束时间..");
		}
		ret.add(startDate);
		while (from.compareTo(to) < 0) {
			from.add(Calendar.HOUR_OF_DAY, 1);
			ret.add(Formator.getDateStr(from, "yyyyMMddHH"));
		}
		ret.add(endDate);
		return ret;
	}

}
