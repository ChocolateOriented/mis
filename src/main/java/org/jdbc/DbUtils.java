package org.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.mo9.risk.modules.dunning.entity.AppLoginLog;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.thinkgem.jeesite.modules.buyer.entity.MRiskBuyerReport;

//import org.apache.log4j.Logger;

/**
 * 数据库操作辅助类
 */
public class DbUtils {
	
	//private static Logger logger = Logger.getLogger("DbUtils");
	
	/**
	 * 该语句必须是一个 SQL INSERT、UPDATE 或 DELETE 语句
	 * @param sql
	 * @param paramList：参数，与SQL语句中的占位符一一对应
	 * @return
	 * @throws Exception
	 */
	public int execute(String sql, List<Object> paramList) throws Exception {
		if(sql == null || sql.trim().equals("")) {
			//logger.info("parameter is valid!");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			conn = getConnection();
			pstmt = DbUtils.getPreparedStatement(conn, sql);
			setPreparedStatementParam(pstmt, paramList);
			if(pstmt == null) {
				return -1;
			}
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			//logger.info(e.getMessage());
			throw new Exception(e);
		} finally {
			closeStatement(pstmt);
			closeConn(conn);
		}

		return result;
	}
	
	/**
	 * 将查询数据库获得的结果集转换为Map对象
	 * @param sql：查询语句
	 * @param paramList：参数
	 * @return
	 */
	public List<Map<String, Object>> getQueryList(String sql, List<Object> paramList) throws Exception {
		if(sql == null || sql.trim().equals("")) {
			//logger.info("parameter is valid!");
			return null;
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> queryList = null;
		try {
			conn = getConnection();
			pstmt = DbUtils.getPreparedStatement(conn, sql);
			setPreparedStatementParam(pstmt, paramList);
			if(pstmt == null) {
				return null;
			}
			rs = getResultSet(pstmt);
			queryList = getQueryList(rs);
		} catch (RuntimeException e) {
			//logger.info(e.getMessage());
			System.out.println("parameter is valid!");
			throw new Exception(e);
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
			closeConn(conn);
		}
		return queryList;
	}
	
	private void setPreparedStatementParam(PreparedStatement pstmt, List<Object> paramList) throws Exception {
		if(pstmt == null || paramList == null || paramList.isEmpty()) {
			return;
		}
		DateFormat df = DateFormat.getDateTimeInstance();
		for (int i = 0; i < paramList.size(); i++) {
			if(paramList.get(i) instanceof Integer) {
				int paramValue = ((Integer)paramList.get(i)).intValue();
				pstmt.setInt(i+1, paramValue);
			} else if(paramList.get(i) instanceof Float) {
				float paramValue = ((Float)paramList.get(i)).floatValue();
				pstmt.setFloat(i+1, paramValue);
			} else if(paramList.get(i) instanceof Double) {
				double paramValue = ((Double)paramList.get(i)).doubleValue();
				pstmt.setDouble(i+1, paramValue);
			} else if(paramList.get(i) instanceof Date) {
				pstmt.setString(i+1, df.format((Date)paramList.get(i)));
			} else if(paramList.get(i) instanceof Long) {
				long paramValue = ((Long)paramList.get(i)).longValue();
				pstmt.setLong(i+1, paramValue);
			} else if(paramList.get(i) instanceof String) {
				pstmt.setString(i+1, (String)paramList.get(i));
			}
		}
		return;
	}
	
	/**
	 * 获得数据库连接
	 * @return
	 * @throws Exception
	 */
	private Connection getConnection() throws Exception {
		InitialContext cxt = new InitialContext();
		DataSource ds = (DataSource) cxt.lookup(jndiName);
		if ( ds == null ) {
		   throw new Exception("Data source not found!");
		}
		
		return ds.getConnection();
	}
	
	private static PreparedStatement getPreparedStatement(Connection conn, String sql) throws Exception {
		if(conn == null || sql == null || sql.trim().equals("")) {
			return null;
		}
		PreparedStatement pstmt = conn.prepareStatement(sql.trim());
		return pstmt;
	}
	
	/**
	 * 获得数据库查询结果集
	 * @param pstmt
	 * @return
	 * @throws Exception
	 */
	private ResultSet getResultSet(PreparedStatement pstmt) throws Exception {
		if(pstmt == null) {
			return null;
		}
		ResultSet rs = pstmt.executeQuery();
		return rs;
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> getQueryList(ResultSet rs) throws Exception {
		if(rs == null) {
			return null;
		}
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int columnCount = rsMetaData.getColumnCount();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			for (int i = 0; i < columnCount; i++) {
				dataMap.put(rsMetaData.getColumnName(i+1), rs.getObject(i+1));
			}
			dataList.add(dataMap);
		}
		return dataList;
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	private void closeConn(Connection conn) {
		if(conn == null) {
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			//logger.info(e.getMessage());
		}
	}
	
	/**
	 * 关闭
	 * @param stmt
	 */
	private void closeStatement(Statement stmt) {
		if(stmt == null) {
			return;
		}
		try {
			stmt.close();
		} catch (SQLException e) {
			//logger.info(e.getMessage());
		}
	}
	
	/**
	 * 关闭
	 * @param rs
	 */
	private void closeResultSet(ResultSet rs) {
		if(rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException e) {
			//logger.info(e.getMessage());
		}
	}
	
	private String jndiName = "java:comp/env/jdbc/mo9jeesite";

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	
	
	
	private String jndiName2 = "java:comp/env/jdbc/jeesite3";

	public void setJndiName2(String jndiName2) {
		this.jndiName2 = jndiName2;
	}
	
	private Connection getConnection2() throws Exception {
		InitialContext cxt = new InitialContext();
		DataSource ds = (DataSource) cxt.lookup(jndiName2);
		if ( ds == null ) {
		   throw new Exception("Data source2 not found!");
		}
		return ds.getConnection();
	}
	
	
	/*          
	 *   ============================================               查询方法（临时）            ============================================        
     */
	
	/**
	 * 根据手机号码查询登录Log
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	public List<AppLoginLog> getApploginlog(String mobile) throws Exception {
		String sql = "SELECT id,mobile,localMobile,deviceModel,mo9ProductName,marketName,createTime from t_app_login_log where mobile = '" + mobile +"' ORDER BY createTime  DESC LIMIT 50";
		if(sql == null || sql.trim().equals("")) {
			//logger.info("parameter is valid!");
			return null;
		}
		List<AppLoginLog> appLoginLogs = new ArrayList<AppLoginLog>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = DbUtils.getPreparedStatement(conn, sql);
			rs = getResultSet(pstmt);
			 while (rs.next()) {
                 AppLoginLog appLoginLog = new AppLoginLog();
                 appLoginLog.setDbid(rs.getInt("id"));
                 appLoginLog.setMobile(rs.getString("mobile"));
                 appLoginLog.setLocalMobile(rs.getString("localMobile"));
                 appLoginLog.setDeviceModel(rs.getString("deviceModel"));
                 appLoginLog.setMo9ProductName(rs.getString("mo9ProductName"));
                 appLoginLog.setMarketName(rs.getString("marketName"));
                 appLoginLog.setCreateTime(rs.getTimestamp("createTime"));
                 appLoginLogs.add(appLoginLog);
             }
		} catch (RuntimeException e) {
			System.out.println("parameter is valid!");
			throw new Exception(e);
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
			closeConn(conn);
		}
		return appLoginLogs;
	}
	
	/**
	 * 通话记录
	 */
	public List<TRiskBuyerContactRecords> findBuyerContactRecordsListByBuyerId(String buyerId) throws Exception {
		String sql = "SELECT  " +
				" CASE WHEN w.company_tel IS NOT NULL THEN concat(w.company_name,'(单位&联系人)')" +
				" WHEN b.type IS NOT NULL THEN concat(c.name,'(单位&联系人)')" +
				" WHEN d.contact_name IS NOT NULL THEN concat(d.contact_name,'(通讯录)')" +
				" WHEN d.contact_name IS NULL AND c.name IS NOT NULL THEN c.name" +
				" ELSE '未知'" +
				" END as 'name'," +
				" b.family_relation as 'familyRelation', " +
				" c.tel as 'tel'," +
				" a.location as 'location'," +
				" CONCAT(" +
				" FLOOR(SUM(a.times) / 3600), '小时'," +
				" FLOOR(SUM(a.times) % 3600 / 60), '分'," +
				" FLOOR(SUM(a.times) % 60), '秒'" +
				" ) as 'times'," +
				" SUM(a.contact_number) as 'number'," +
				" a.contact_type as 'contactType'," +
				" a.buyer_id as 'buyerId'," +
				" c.name AS rcname," +
				" SUM(a.times) AS sumtime" +
				" FROM (" +
				" SELECT" +
				" buyer_id," +
				" contact_id," +
				" contact_type," +
				" location," +
				" IFNULL(SUM(begin_date), 0) AS contact_number," +
				" SUM(times) AS times" +
				" FROM t_risk_buyer_contact_records" +
				" WHERE buyer_id =  '" + buyerId + "'" +
				" GROUP BY buyer_id,contact_id,contact_type,location) a" +
				" LEFT JOIN t_risk_contact c on c.id  = a.contact_id" +
				" LEFT JOIN t_buyer_contact AS d ON d.buyer_id = a.buyer_id AND d.contact_mobile = c.tel" +
				" INNER JOIN t_risk_buyer2contacts  b  ON c.id = b.buyer_contact_id  AND a.buyer_id = b.buyer_id" +
				" LEFT JOIN t_risk_buyer_workinfo AS w  ON w.buyer_id = a.buyer_id AND REPLACE(w.company_tel, '-', '') = c.tel" +
				" WHERE a.buyer_id = '" + buyerId + "'  GROUP BY c.tel ORDER BY SUM(a.times) DESC  LIMIT 50 " ;
		if(sql == null || sql.trim().equals("")) {
			//logger.info("parameter is valid!");
			return null;
		}
		List<TRiskBuyerContactRecords> buyerContactRecords = new ArrayList<TRiskBuyerContactRecords>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection2();
			pstmt = DbUtils.getPreparedStatement(conn, sql);
			rs = getResultSet(pstmt);
			 while (rs.next()) {
				 TRiskBuyerContactRecords buyerContactRecord = new TRiskBuyerContactRecords();
//					private String name;
//					private String familyRelation;
//					private String tel;
//					private String location;		// location
//					private String times;		// times
//					private String rcname;
//					private String contactType;		// contact_type
//					private String buyerId;		// buyer_id
//					private Integer number;
//					private Integer sumtime;
					buyerContactRecord.setName(rs.getString("name"));
					buyerContactRecord.setFamilyRelation(rs.getString("familyRelation"));
					buyerContactRecord.setTel(rs.getString("tel"));
					buyerContactRecord.setLocation(rs.getString("location"));
					buyerContactRecord.setTimes(rs.getString("times"));
					buyerContactRecord.setRcname(rs.getString("rcname"));
					buyerContactRecord.setContactType(rs.getString("contactType"));
					buyerContactRecord.setBuyerId(rs.getString("buyerId"));
					buyerContactRecord.setNumber(rs.getInt("number"));
					buyerContactRecord.setSumtime(rs.getInt("sumtime"));
                 buyerContactRecords.add(buyerContactRecord);
             }
		} catch (RuntimeException e) {
			System.out.println("parameter is valid!");
			throw new Exception(e);
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
			closeConn(conn);
		}
		return buyerContactRecords;
	}
	

	
	
	/**
	 * 测试方法
	 */
	public List<MRiskBuyerReport> getQueryList() throws Exception {
		String sql = "select * from m_risk_buyer_report ";
		if(sql == null || sql.trim().equals("")) {
			//logger.info("parameter is valid!");
			return null;
		}
		List<MRiskBuyerReport> buyerReports = new ArrayList<MRiskBuyerReport>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<MRiskBuyerReport> queryList = null;
		try {
//			conn = ConnectionFactory.getFactory().getDeliveryProxoolConnection();
//			ps = conn.prepareStatement(sql);
//			rs = ps.executeQuery();
			conn = getConnection();
			pstmt = DbUtils.getPreparedStatement(conn, sql);
			rs = getResultSet(pstmt);
			 while (rs.next()) {
                 int id_ = rs.getInt("id");
//                 String domain = rs.getString("BW_DOMAIN");
//                 String ip = rs.getString("BW_IP");
//                 long value = rs.getLong("BW_VALUE");
                 MRiskBuyerReport bdbean = new MRiskBuyerReport();
                 bdbean.setId(String.valueOf(id_));
                 buyerReports.add(bdbean);
             }
		} catch (RuntimeException e) {
			System.out.println("parameter is valid!");
			throw new Exception(e);
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
			closeConn(conn);
		}
		return buyerReports;
	}
	
}