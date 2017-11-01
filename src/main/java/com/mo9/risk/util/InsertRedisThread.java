package com.mo9.risk.util;

import com.mo9.risk.modules.dunning.manager.RiskBuyerContactManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdbc.DbUtils;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.bean.ThreadBuyerid;
import com.mo9.risk.modules.dunning.entity.PhoneInfo;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyer2contacts;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.thinkgem.jeesite.common.utils.JedisUtils;


@Transactional(readOnly = true)
public class InsertRedisThread  implements Runnable{
	
	private static Logger logger = Logger.getLogger(InsertRedisThread.class);
	
//	private Vector<String> buyerids;
//	public Vector<String> getBuyerids() {
//		return buyerids;
//	}
//	public void setBuyerids(Vector<String> buyerids) {
//		this.buyerids = buyerids;
//	}
	
	private Vector<ThreadBuyerid> threadBuyerids;
	public Vector<ThreadBuyerid> getThreadBuyerids() {
		return threadBuyerids;
	}
	public void setThreadBuyerids(Vector<ThreadBuyerid> threadBuyerids) {
		this.threadBuyerids = threadBuyerids;
	}
	
	private int cacheSeconds;
	public int getCacheSeconds() {
		return cacheSeconds;
	}
	public void setCacheSeconds(int cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}
	//	private static int cacheSeconds = 5209600 ;
	
	@Override
	public synchronized void run() {
		try {
			if(!threadBuyerids.isEmpty()){
//				synchronized(this) {
				logger.info("线程" + Thread.currentThread().getName() + "redis预提醒通话记录条数:" + threadBuyerids.size());
				for(ThreadBuyerid threadBuyerid : threadBuyerids){
					logger.info("buyerid" + threadBuyerid.getBuyerid() + "mobile" + threadBuyerid.getMobile() + "=========" + Thread.currentThread().getName());
					insertDunningTaskJedis(threadBuyerid.getBuyerid(),threadBuyerid.getMobile(),cacheSeconds);
				}
				logger.info("线程" + Thread.currentThread().getName() +"redis缓存预提醒通话记录完成时间:" + new Date());
//				}
			}else{
				logger.info("线程" + Thread.currentThread().getName() +"查询预提醒新进入正在催收案件buyerid为0个" + new Date());
			}
		} catch (Exception e) {
			logger.warn("线程" + Thread.currentThread().getName() +"redis缓存预提醒通话记录异常", e);
		}
	}
	
	/**
	 * 预缓存通话记录
	 * @param buyerId
	 * @return
	 */
	public String insertDunningTaskJedis(String buyerId,String mobile,int cacheSeconds){
		int getPageNo = 1;
		int getPageSize = 50;
		String pageKey = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ buyerId + "_PageNo:" + getPageNo + "_PageSize:" + getPageSize ;
		String pagecount = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_listSize_buyerId:"+ buyerId;
		try {
			if(null == JedisUtils.getObjectMap(pageKey)){
				int index = 1;
				int PageNo = 1;
				Map<String, Object> map2 = new HashMap<String, Object>();
				DbUtils dbUtils = new DbUtils();
				List<TRiskBuyerContactRecords> contactRecordsList = null;
				RiskBuyerContactManager contactRecordsManager = new RiskBuyerContactManager();
				try {
					contactRecordsList = contactRecordsManager.findContactRecordsByMobile(mobile);
					logger.info("预缓存http接口返回通讯录条数：" + contactRecordsList.size());
				} catch (Exception e1) {
					logger.warn("预缓存http接口返回通讯录失败：mobile-"+ mobile, e1);
				}
//				List<TRiskBuyerContactRecords> contactRecordsList = contactRecordsManager.findByByMobile(mobile);
//				logger.info("预缓存http接口返回通讯录条数：" + contactRecordsList.size());
				
				if(null == contactRecordsList || contactRecordsList.isEmpty()){
					logger.info("预缓存http接口返回null,通话记录buyerId切源查询:" + buyerId);
//					logger.info("预缓存通话记录切源查询buyerId：" + buyerId);
					contactRecordsList = dbUtils.findBuyerContactRecordsListByBuyerId(buyerId);
					logger.info("buyerId：" + buyerId + "预缓存通话记录条数：" + contactRecordsList.size());
				} else {
					matchContactRecordRelation(contactRecordsList, buyerId, mobile);
				}
				
				if(!contactRecordsList.isEmpty()){
					for(TRiskBuyerContactRecords records : contactRecordsList){
						map2.put(String.valueOf(index), records);
						if(index % getPageSize == 0){
							String key = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ buyerId + "_PageNo:" + PageNo + "_PageSize:" + getPageSize ;
							JedisUtils.setObjectMap( key , map2 , cacheSeconds);
							map2 = new HashMap<String, Object>();
							PageNo += 1;
							index = 0;
						}
						index += 1;
					}
					// 最后一页
					double lastpageNo =  Math.ceil((double)contactRecordsList.size() / (double)getPageSize) + 1;
					if(lastpageNo  > PageNo){
						String key = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ buyerId + "_PageNo:" + PageNo + "_PageSize:" + getPageSize ;
						JedisUtils.setObjectMap( key , map2 , cacheSeconds);
					}
				}else{
					map2.put("0", new TRiskBuyerContactRecords());
					JedisUtils.setObjectMap( pageKey , map2 , cacheSeconds);
				}
				JedisUtils.setObject(pagecount, contactRecordsList.size(), cacheSeconds);
			}else{
				logger.info("buyerId:" + buyerId + "缓存已存在");
			}
		} catch (Exception e) {
			logger.info("预缓存通讯失败：buyerid-"+buyerId,e);
			return "f";
		}
		return "t";
	}	
	
	/**
	 * 匹配单位/联系人/通讯录/归属地
	 * @param tRiskBuyerContactRecords
	 * @param buyerId
	 * @param mobile
	 * @return
	 */
	public void matchContactRecordRelation(List<TRiskBuyerContactRecords> list, String buyerId, String mobile) {
		TMisSendMsgInfo workinfo = getWorkTelInfoByBuyerId(buyerId);
		List<TRiskBuyer2contacts> buyer2Contacts = getBuyerContacts(buyerId);
		List<TMisSendMsgInfo> buyerContactsInfo = getContactsByBuyerId(buyerId, mobile);
		List<PhoneInfo> phoneInfos = findContactRecordsPhoneInfo(list);
		
		Map<String, TRiskBuyer2contacts> buyer2ContactMap = new HashMap<String, TRiskBuyer2contacts>(64);
		Map<String, TMisSendMsgInfo> buyerContactsMap = new HashMap<String, TMisSendMsgInfo>(128);
		Map<String, PhoneInfo> PhoneInfoMap = new HashMap<String, PhoneInfo>(128);
		
		if (buyer2Contacts != null && !buyer2Contacts.isEmpty()) {
			for (TRiskBuyer2contacts buyerContact : buyer2Contacts) {
				buyer2ContactMap.put(buyerContact.getTel(), buyerContact);
			}
		}
		
		if (buyerContactsInfo != null && !buyerContactsInfo.isEmpty()) {
			for (TMisSendMsgInfo contactInfo : buyerContactsInfo) {
				buyerContactsMap.put(contactInfo.getTel(), contactInfo);
			}
		}
		
		if (phoneInfos != null && !phoneInfos.isEmpty()) {
			for (PhoneInfo phone : phoneInfos) {
				PhoneInfoMap.put(phone.getPreNumber(), phone);
			}
		}
		
		for (TRiskBuyerContactRecords contactRecord : list) {
			String recordTel = contactRecord.getTel();
			if (StringUtils.isEmpty(recordTel)) {
				continue;
			}
			
			//匹配归属地
			if (recordTel.length() >= 7) {
				PhoneInfo phone = PhoneInfoMap.get(recordTel.substring(0, 7));
				if (phone != null) {
					String province = phone.getProvince() == null ? "" : phone.getProvince();
					String city = phone.getCity() == null ? "" : phone.getCity();
					String location = province.equals(city) ? city : province + city;
					contactRecord.setLocation(location);
				}
			}
			
			//匹配单位
			if (workinfo != null && recordTel.equals(workinfo.getTel())) {
				String name = workinfo.getName() + "(单位&联系人)";
				contactRecord.setName(name);
				continue;
			}
			
			//匹配联系人
			TRiskBuyer2contacts buyer2Contact = buyer2ContactMap.get(recordTel);
			if (buyer2Contact != null) {
				String name = buyer2Contact.getName() + "(单位&联系人)";
				contactRecord.setName(name);
				continue;
			}
			
			//匹配通讯录
			TMisSendMsgInfo contactInfo = buyerContactsMap.get(recordTel);
			if (contactInfo != null) {
				String name = contactInfo.getName() + "(通讯录)";
				contactRecord.setName(name);
				continue;
			}
		}
		
	}
	
	//查询归属地
	private List<PhoneInfo> findContactRecordsPhoneInfo(List<TRiskBuyerContactRecords> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		String jndiName = "java:comp/env/jdbc/jeesite_read";
		DbUtils dbUtils = new DbUtils();
		dbUtils.setJndiName(jndiName);
		String sql = "SELECT a.phone, a.province, a.city FROM phone a WHERE a.phone IN (";
		StringBuilder builder = new StringBuilder();
		builder.append(sql);
		for (TRiskBuyerContactRecords records : list) {
			String tel = records.getTel();
			if (tel == null || tel.length() < 7) {
				continue;
			}
			builder.append("'").append(tel.substring(0, 7)).append("'").append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		sql = builder.toString();
		
		List<Object> paramList = new ArrayList<Object>();
		try {
			List<Map<String, Object>> results = dbUtils.getQueryList(sql, paramList);
			if (results == null || results.isEmpty()) {
				return null;
			}
			List<PhoneInfo> phoneInfos = new ArrayList<PhoneInfo>();
			for (Map<String, Object> record : results) {
				PhoneInfo info = new PhoneInfo();
				String phone = (String) record.get("phone");
				String province = (String) record.get("province");
				String city = (String) record.get("city");
				info.setPreNumber(phone);
				info.setProvince(province);
				info.setCity(city);
				phoneInfos.add(info);
			}
			return phoneInfos;
		} catch (Exception e) {
			logger.info("查询联系人信息异常:" + e);
		}
		return null;
	}
	
	//查询单位信息
	private TMisSendMsgInfo getWorkTelInfoByBuyerId(String buyerId) {
		String jndiName = "java:comp/env/jdbc/jeesite_read";
		String sql = "SELECT a.buyer_id, a.company_tel, a.company_name, 'worktel' AS \"relation\" FROM t_risk_buyer_workinfo a WHERE a.buyer_id = ? limit 1";
		DbUtils dbUtils = new DbUtils();
		dbUtils.setJndiName(jndiName);
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(buyerId);
		try {
			List<Map<String, Object>> results = dbUtils.getQueryList(sql, paramList);
			if (results == null || results.isEmpty()) {
				return null;
			}
			TMisSendMsgInfo workinfo = new TMisSendMsgInfo();
			Map<String, Object> record = results.get(0);
			String tel = (String) record.get("company_tel");
			String name = (String) record.get("company_name");
			String relation = (String) record.get("relation");
			workinfo.setTel(tel);
			workinfo.setName(name);
			workinfo.setRelation(relation);
			return workinfo;
		} catch (Exception e) {
			logger.info("查询单位信息异常:" + e);
		}
		return null;
	}
	
	//查询联系人信息
	private List<TRiskBuyer2contacts> getBuyerContacts(String buyerId) {
		String jndiName = "java:comp/env/jdbc/jeesite_read";
		String sql = "SELECT a.id, a.buyer_id, a.buyer_contact_id, a.family_relation, a.type, b.tel, b.name "
				+ "FROM t_risk_buyer2contacts a LEFT JOIN t_risk_contact b ON a.buyer_contact_id = b.id WHERE a.buyer_id = ? AND a.type is not NULL";
		DbUtils dbUtils = new DbUtils();
		dbUtils.setJndiName(jndiName);
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(buyerId);
		try {
			List<Map<String, Object>> results = dbUtils.getQueryList(sql, paramList);
			if (results == null || results.isEmpty()) {
				return null;
			}
			List<TRiskBuyer2contacts> buyer2contacts = new ArrayList<TRiskBuyer2contacts>();
			for (Map<String, Object> record : results) {
				TRiskBuyer2contacts contact = new TRiskBuyer2contacts();
				String tel = (String) record.get("tel");
				String name = (String) record.get("name");
				String relation = (String) record.get("family_relation");
				contact.setTel(tel);
				contact.setName(name);
				contact.setFamilyRelation(relation);
				buyer2contacts.add(contact);
			}
			return buyer2contacts;
		} catch (Exception e) {
			logger.info("查询联系人信息异常:" + e);
		}
		return null;
	}
	
	//查询通讯录信息
	private List<TMisSendMsgInfo> getContactsByBuyerId(String buyerId, String mobile) {
		try {
			RiskBuyerContactManager riskBuyerContactManager = new RiskBuyerContactManager();
			List<TBuyerContact> tBuyerContacts = riskBuyerContactManager.getBuyerContactInfo(mobile);
			List<TMisSendMsgInfo> results = new ArrayList<TMisSendMsgInfo>();
			if (tBuyerContacts != null && tBuyerContacts.size() != 0) {
				for (TBuyerContact contact : tBuyerContacts) {
					TMisSendMsgInfo tMisSendMsgInfo = new TMisSendMsgInfo();
					tMisSendMsgInfo.setTel(contact.getContactMobile());
					tMisSendMsgInfo.setName(contact.getContactName());
					results.add(tMisSendMsgInfo);
				}
				return results;
			}
			
		} catch (Exception e) {
			logger.info("调用江湖救急通讯录接口失败：" + e.getMessage());
		}
		//异常或接口无数据时查询mis库
		String jndiName = "java:comp/env/jdbc/jeesite_read";
		String sql = "SELECT a.buyer_id, a.contact_name, a.contact_mobile, 'cantact' AS \"relation\" FROM t_buyer_contact a WHERE a.buyer_id = ?";
		DbUtils dbUtils = new DbUtils();
		dbUtils.setJndiName(jndiName);
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(buyerId);
		try {
			List<Map<String, Object>> results = dbUtils.getQueryList(sql, paramList);
			if (results == null || results.isEmpty()) {
				return null;
			}
			List<TMisSendMsgInfo> contactInfos = new ArrayList<TMisSendMsgInfo>();
			for (Map<String, Object> record : results) {
				TMisSendMsgInfo contact = new TMisSendMsgInfo();
				String tel = (String) record.get("contact_mobile");
				String name = (String) record.get("contact_name");
				String relation = (String) record.get("relation");
				contact.setTel(tel);
				contact.setName(name);
				contact.setRelation(relation);
				contactInfos.add(contact);
			}
			return contactInfos;
		} catch (Exception e) {
			logger.info("查询通讯录信息异常:" + e);
		}
		return null;
	}
}
