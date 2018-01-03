/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.bean.ThreadBuyerid;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerContactRecordsDao;
import com.mo9.risk.modules.dunning.entity.PhoneInfo;
import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyer2contacts;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.mo9.risk.modules.dunning.manager.RiskBuyerContactManager;
import com.mo9.risk.util.InsertRedisThread;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdbc.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 通话记录Service
 * @author beragao
 * @version 2016-07-15
 */
@Service
@Transactional(readOnly = true)
@Lazy(false)
public class TRiskBuyerContactRecordsService {
	
	private static Logger logger = Logger.getLogger(TRiskBuyerContactRecordsService.class);
	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	@Autowired
	private TRiskBuyerContactRecordsDao tRiskBuyerContactRecordsDao;
	@Autowired
	private TBuyerContactService tBuyerContactService;
	@Autowired
	private TRiskBuyerWorkinfoService tRiskBuyerWorkinfoService;
	@Autowired
	private TRiskBuyer2contactsService tRiskBuyer2contactsService;
	
	public Page<TRiskBuyerContactRecords> findPage(Page<TRiskBuyerContactRecords> page, TRiskBuyerContactRecords tRiskBuyerContactRecords) {
		tRiskBuyerContactRecords.setPage(page);
		page.setList(tRiskBuyerContactRecordsDao.findList(tRiskBuyerContactRecords));
		return page;
	}
	
	private static int cacheSeconds = 5209600 ;
	
	/**
	 * redis 分页缓存
	 * @param page
	 * @param tRiskBuyerContactRecords
	 * @param buyerId
	 * @return
	 */
	public Page<TRiskBuyerContactRecords> findPageByRediscache(Page<TRiskBuyerContactRecords> page, TRiskBuyerContactRecords tRiskBuyerContactRecords,String buyerId,String mobile) {
		String pageKey =   "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ tRiskBuyerContactRecords.getBuyerId() + "_PageNo:" + page.getPageNo() + "_PageSize:" + page.getPageSize() ;
		String pagecount = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_listSize_buyerId:"+ tRiskBuyerContactRecords.getBuyerId();
		if(null == JedisUtils.getObjectMap(pageKey)){
			logger.info("手机号码" + mobile +"&buyerId" + buyerId + "未找到缓存，预先进入http接口返回通讯录");
			int index = 1;
			int PageNo = 1;
			Map<String, Object> map2 = new HashMap<String, Object>();
//			List<TRiskBuyerContactRecords> contactRecordsList = tRiskBuyerContactRecordsDao.findBuyerContactRecordsListByBuyerId(tRiskBuyerContactRecords);
			DbUtils dbUtils = new DbUtils();
			List<TRiskBuyerContactRecords> contactRecordsList = null;
			RiskBuyerContactManager contactRecordsManager = new RiskBuyerContactManager();
			try {
				contactRecordsList = contactRecordsManager.findContactRecordsByMobile(mobile);
				logger.info("http接口返回通讯录条数：" + contactRecordsList.size());
			} catch (Exception e1) {
				logger.warn("http接口返回通讯录失败：mobile-"+ mobile, e1);
			}
			
			if(null == contactRecordsList || contactRecordsList.isEmpty()){
				try {
					logger.info("http接口返回null,通话记录buyerId切源查询:" + buyerId);
					contactRecordsList = dbUtils.findBuyerContactRecordsListByBuyerId(buyerId);
					logger.info("切源查询通话记录条数："+contactRecordsList.size());
				} catch (Exception e) {
					logger.warn("切源查询通讯失败：buyerid-"+tRiskBuyerContactRecords.getBuyerId(),e);
				}
			} else {
				matchContactRecordRelation(contactRecordsList, buyerId);
			}
			if(!contactRecordsList.isEmpty()){
				for(TRiskBuyerContactRecords records : contactRecordsList){
					map2.put(String.valueOf(index), records);
					if(index % page.getPageSize() == 0){
						String key = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ tRiskBuyerContactRecords.getBuyerId() + "_PageNo:" + PageNo + "_PageSize:" + page.getPageSize() ;
						JedisUtils.setObjectMap( key , map2 , cacheSeconds);
						map2 = new HashMap<String, Object>();
						PageNo += 1;
						index = 0;
					}
					index += 1;
				}
				// 最后一页
				double lastpageNo =  Math.ceil((double)contactRecordsList.size() / (double)page.getPageSize()) + 1;
				if(lastpageNo  > PageNo){
					String key = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ tRiskBuyerContactRecords.getBuyerId() + "_PageNo:" + PageNo + "_PageSize:" + page.getPageSize() ;
					JedisUtils.setObjectMap( key , map2 , cacheSeconds);
				}
			}else{
				map2.put("0", new TRiskBuyerContactRecords());
				JedisUtils.setObjectMap( pageKey , map2 , cacheSeconds);
			}
			JedisUtils.setObject(pagecount, contactRecordsList.size(), cacheSeconds);
		}
		
		List<TRiskBuyerContactRecords> list = new ArrayList<TRiskBuyerContactRecords>();
		/* 获取redis缓存总条数  */
		long count = Long.valueOf(JedisUtils.getObject(pagecount).toString()); 
		if(count>0){
//			Map<Integer, Object> map = new TreeMap<Integer, Object>(JedisUtils.getObjectMap2(pageKey));
			Map<String, Object> map = JedisUtils.getObjectMap(pageKey);
			
			
			/* 获取操作记录  */
			Map<String, TRiskBuyerContactRecords> contantNumberMap =  getCountyHashMap(tRiskBuyerContactRecordsDao.findContantNumberListBydealcode(tRiskBuyerContactRecords));
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				TRiskBuyerContactRecords value = (TRiskBuyerContactRecords) entry.getValue();
				if(contantNumberMap.containsKey(value.getTel())){
					int smsNum = contantNumberMap.get(value.getTel()).getSmsNum();
					int telNum = contantNumberMap.get(value.getTel()).getTelNum();
					int effectiveActionNum = contantNumberMap.get(value.getTel()).getEffectiveActionNum();
					value.setSmsNum(smsNum);
					value.setTelNum(telNum);
					value.setEffectiveActionNum(effectiveActionNum);
				}else{
					value.setSmsNum(0);
					value.setTelNum(0);
					value.setEffectiveActionNum(0);
				}
				list.add(value);
//				list.add(Integer.parseInt(entry.getKey()), value);
//				String key = entry.getKey().toString();
			}
		}
//		ListSortUtil<TRiskBuyerContactRecords> sortList = new ListSortUtil<TRiskBuyerContactRecords>();  
//		sortList.sort(list, "sumtime", "asc");
        Collections.sort(list, new Comparator<TRiskBuyerContactRecords>(){  
            public int compare(TRiskBuyerContactRecords o1, TRiskBuyerContactRecords o2) {  
                if(o1.getSumtime() > o2.getSumtime()){  
                    return -1;  
                }  
                if(o1.getSumtime() == o2.getSumtime()){  
                    return 0;  
                }  
                return 1;  
            }  
        }); 
//		Collections.sort(list.get(index));
		page.setCount(count);
		tRiskBuyerContactRecords.setPage(page);
		page.setList(list);
		return page;
	}
	
	
	/**
	 * list conversion map 
	 * @param list
	 * @return
	 */
	public static Map<String, TRiskBuyerContactRecords> getCountyHashMap(List<TRiskBuyerContactRecords> list) {
	    Map<String, TRiskBuyerContactRecords> map = new HashMap<String, TRiskBuyerContactRecords>();
		for(TRiskBuyerContactRecords entry : list){
			map.put(entry.getTel(), entry);
		}
	    return map;
	}
	
	/**
	 * 通话记录redis缓存查询
	 * @param tRiskBuyerContactRecords
	 * @return
	 */
	public List<TRiskBuyerContactRecords> findBuyerContactRecordsListByBuyerId(TRiskBuyerContactRecords tRiskBuyerContactRecords){
		return tRiskBuyerContactRecordsDao.findBuyerContactRecordsListByBuyerId(tRiskBuyerContactRecords);
	}
	
	
	
//	/**
//	 *  定时缓存预提醒通话记录任务
//	 */
//	@Transactional(readOnly = false)
//	public void insertDunningTaskJedisJob(){
//		
//		String scheduledBut =  DictUtils.getDictValue("insertDunningTaskJedis","Scheduled","false");
//		if(scheduledBut.equals("true")){
//			
//			logger.info("redis开始预缓存提醒通话记录任务:" + new Date());
//			String begin_Q0 = tMisDunningTaskService.getCycleDict_Q0().get("begin");
//			logger.info("redis预提醒DAY:" + begin_Q0);
//			Vector<String> buyerids = tMisDunningTaskDao.findBuyeridByNewTask(begin_Q0);
////			List<String>  buyerids = tMisDunningTaskDao.findBuyeridByNewTask(begin_Q0);
//			
//			if(!buyerids.isEmpty()){
//				logger.info("redis缓存预提醒通话记录总条数:" + buyerids.size());
////				for(String buyerid : buyerids){
////					String mes = insertDunningTaskJedis(buyerid);
////					if("f".equals(mes)){
////						logger.warn("缓存失败buyerid:"+ buyerid + new Date());
////					}
////				}
//				int index = 0;
//				List<Vector<String>> result = new ArrayList<Vector<String>>();
//				Integer threadNum = Integer.parseInt(DictUtils.getDictDescription("insertDunningTaskJedis","Scheduled","10"));
//				for (int i = 0; i < threadNum; i++) {
//					result.add(new Vector<String>());
//				}
//				for (int i = 0; i < buyerids.size(); i++) {
//					result.get(index).add(buyerids.get(i));
//					if (++index >= threadNum) {
//						index = 0;
//					}
//				}
//				for(Vector<String> threadBuyerids : result){
//					InsertRedisThread insertRedisThread = new InsertRedisThread();
//					insertRedisThread.setBuyerids(threadBuyerids);
//					insertRedisThread.setCacheSeconds(cacheSeconds);
//					new Thread(insertRedisThread).start();
//				}
//				logger.info("redis缓存预通话记录定时多线程任务完成:" + new Date());
//			}else{
//				logger.info("查询预提醒新进入正在催收案件buyerid为0个" + new Date());
//			}
//			
//		}
//		
//	}
	
	
	/**
	 *  定时缓存预提醒通话记录任务 - new 
	 */
	@Scheduled(cron = "0 20 3 * * ?")  //每天上午4点10
	@Transactional(readOnly = false)
	public void insertDunningTaskJedisJob(){
		
		String scheduledBut =  DictUtils.getDictValue("insertDunningTaskJedis","Scheduled","false");
		if(scheduledBut.equals("true")){
			
			logger.info("redis开始预缓存提醒通话记录任务:" + new Date());
			String begin_Q0 = tMisDunningTaskService.getCycleDict_Q0().get("begin");
			logger.info("redis预提醒DAY:" + begin_Q0);
			Vector<ThreadBuyerid> buyerids = tMisDunningTaskDao.findThreadBuyeridByNewTask(begin_Q0);
			
			if(!buyerids.isEmpty()){
				logger.info("redis缓存预提醒通话记录总条数:" + buyerids.size());
				int index = 0;
				List<Vector<ThreadBuyerid>> result = new ArrayList<Vector<ThreadBuyerid>>();
				Integer threadNum = Integer.parseInt(DictUtils.getDictDescription("insertDunningTaskJedis","Scheduled","10"));
				for (int i = 0; i < threadNum; i++) {
					result.add(new Vector<ThreadBuyerid>());
				}
				for (int i = 0; i < buyerids.size(); i++) {
					result.get(index).add(buyerids.get(i));
					if (++index >= threadNum) {
						index = 0;
					}
				}
				for(Vector<ThreadBuyerid> threadBuyerids : result){
					InsertRedisThread insertRedisThread = new InsertRedisThread();
					insertRedisThread.setThreadBuyerids(threadBuyerids);
					insertRedisThread.setCacheSeconds(cacheSeconds);
					new Thread(insertRedisThread).start();
				}
				logger.info("redis缓存预通话记录定时多线程任务完成:" + new Date());
			}else{
				logger.info("查询预提醒新进入正在催收案件buyerid为0个" + new Date());
			}
			
		}
		
	}

	/**
	 * 匹配单位/联系人/通讯录/归属地
	 * @param tRiskBuyerContactRecords
	 * @param buyerId
	 * @return
	 */
	private void matchContactRecordRelation(List<TRiskBuyerContactRecords> list, String buyerId) {
		TMisSendMsgInfo workinfo = tRiskBuyerWorkinfoService.getWorkTelInfoByBuyerId(buyerId);
		List<TRiskBuyer2contacts> buyer2Contacts = tRiskBuyer2contactsService.getBuyerContacts(buyerId);
		List<TMisSendMsgInfo> buyerContactsInfo = tBuyerContactService.getContactsByBuyerId(buyerId);
		List<PhoneInfo> phoneInfos = tRiskBuyerContactRecordsDao.findContactRecordsPhoneInfo(list);
		
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
				String name = contactInfo.getName() + "(关联人)";
				contactRecord.setName(name);
				continue;
			}
			
		}
		
	}
	
	/**
	 * 预缓存通话记录
	 * @param buyerId
	 * @return
	 */
//	public String insertDunningTaskJedis(String buyerId){
//		int getPageNo = 1;
//		int getPageSize = 30;
//		String pageKey = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ buyerId + "_PageNo:" + getPageNo + "_PageSize:" + getPageSize ;
//		String pagecount = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_listSize_buyerId:"+ buyerId;
//		try {
//			if(null == JedisUtils.getObjectMap(pageKey)){
//				int index = 1;
//				int PageNo = 1;
//				Map<String, Object> map2 = new HashMap<String, Object>();
//				DbUtils dbUtils = new DbUtils();
////				List<TRiskBuyerContactRecords> contactRecordsList = null;
//				List<TRiskBuyerContactRecords> contactRecordsList = dbUtils.findBuyerContactRecordsListByBuyerId(buyerId);
//				System.out.println("预缓存通话记录buyerId切源查询:" + buyerId +"预缓存通话记录条数："+contactRecordsList.size());
//				
//				if(!contactRecordsList.isEmpty()){
//					for(TRiskBuyerContactRecords records : contactRecordsList){
//						map2.put(String.valueOf(index), records);
//						if(index % getPageSize == 0){
//							String key = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ buyerId + "_PageNo:" + PageNo + "_PageSize:" + getPageSize ;
//							JedisUtils.setObjectMap( key , map2 , cacheSeconds);
//							map2 = new HashMap<String, Object>();
//							PageNo += 1;
//							index = 0;
//						}
//						index += 1;
//					}
//					// 最后一页
//					double lastpageNo =  Math.ceil((double)contactRecordsList.size() / (double)getPageSize) + 1;
//					if(lastpageNo  > PageNo){
//						String key = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ buyerId + "_PageNo:" + PageNo + "_PageSize:" + getPageSize ;
//						JedisUtils.setObjectMap( key , map2 , cacheSeconds);
//					}
//				}else{
//					map2.put("0", new TRiskBuyerContactRecords());
//					JedisUtils.setObjectMap( pageKey , map2 , cacheSeconds);
//				}
//				JedisUtils.setObject(pagecount, contactRecordsList.size(), cacheSeconds);
//			}else{
//				System.out.println("buyerId:" + buyerId + "缓存已存在");
//			}
//		} catch (Exception e) {
//			System.out.println("预缓存通讯失败：buyerid-"+buyerId);
//			e.printStackTrace();
//			return "f";
//		}
//		return "t";
//	}
	
}

