/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdbc.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerContactRecordsDao;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

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
	
//	private Vector<String> buyerids;
//	private int data;
//	public Vector<String> getBuyerids() {
//		return buyerids;
//	}
//	public void setBuyerids(Vector<String> buyerids) {
//		this.buyerids = buyerids;
//	}
//	public int getData() {
//		return data;
//	}
//	public void setData(int data) {
//		this.data = data;
//	}
	
	
	
	public Page<TRiskBuyerContactRecords> findPage(Page<TRiskBuyerContactRecords> page, TRiskBuyerContactRecords tRiskBuyerContactRecords) {
		tRiskBuyerContactRecords.setPage(page);
		page.setList(tRiskBuyerContactRecordsDao.findList(tRiskBuyerContactRecords));
		return page;
	}
	
	private static int cacheSeconds = 2509600 ;
	
	/**
	 * redis 分页缓存
	 * @param page
	 * @param tRiskBuyerContactRecords
	 * @param buyerId
	 * @return
	 */
	public Page<TRiskBuyerContactRecords> findPageByRediscache(Page<TRiskBuyerContactRecords> page, TRiskBuyerContactRecords tRiskBuyerContactRecords,String buyerId) {
		String pageKey =   "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ tRiskBuyerContactRecords.getBuyerId() + "_PageNo:" + page.getPageNo() + "_PageSize:" + page.getPageSize() ;
		String pagecount = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_listSize_buyerId:"+ tRiskBuyerContactRecords.getBuyerId();
		if(null == JedisUtils.getObjectMap(pageKey)){
			int index = 1;
			int PageNo = 1;
			Map<String, Object> map2 = new HashMap<String, Object>();
//			List<TRiskBuyerContactRecords> contactRecordsList = tRiskBuyerContactRecordsDao.findBuyerContactRecordsListByBuyerId(tRiskBuyerContactRecords);
			DbUtils dbUtils = new DbUtils();
			List<TRiskBuyerContactRecords> contactRecordsList = null;
			try {
				System.out.println("通话记录buyerId切源查询:" + buyerId);
				contactRecordsList = dbUtils.findBuyerContactRecordsListByBuyerId(buyerId);
				System.out.println("通话记录条数："+contactRecordsList.size());
			} catch (Exception e) {
				System.out.println("通讯失败：buyerid-"+tRiskBuyerContactRecords.getBuyerId());
				e.printStackTrace();
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
					value.setSmsNum(smsNum);
					value.setTelNum(telNum);
				}else{
					value.setSmsNum(0);
					value.setTelNum(0);
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
	
	
	
	/**
	 *  定时缓存预提醒通话记录任务
	 */
	@Scheduled(cron = "0 15 21 * * ?")  //每天上午4点10
	@Transactional(readOnly = false)
	public void insertDunningTaskJedisJob(){
		
		String scheduledBut =  DictUtils.getDictValue("insertDunningTaskJedis","Scheduled","false");
		if(scheduledBut.equals("true")){
			
			logger.info("redis开始缓存预提醒通话记录任务:" + new Date());
			String begin_Q0 = tMisDunningTaskService.getCycleDict_Q0().get("begin");
			logger.info("redis预提醒DAY:" + begin_Q0);
//			Vector<String> buyerids = tMisDunningTaskDao.findBuyeridByNewTask(begin_Q0);
			List<String>  buyerids = tMisDunningTaskDao.findBuyeridByNewTask(begin_Q0);
			
			if(!buyerids.isEmpty()){
				for(String buyerid : buyerids){
					String mes = insertDunningTaskJedis(buyerid);
					if("f".equals(mes)){
						logger.warn("缓存失败buyerid:"+ buyerid + new Date());
					}
				}
//				TRiskBuyerContactRecordsService insertRedisThread = new TRiskBuyerContactRecordsService();
//				insertRedisThread.setBuyerids(buyerids);
//				insertRedisThread.setData(buyerids.size());
//				Integer threadNum = Integer.parseInt(DictUtils.getDictDescription("insertDunningTaskJedis","Scheduled","10"));
//				for (int i = 0; i < 2; i++) {
//					new Thread(insertRedisThread).start();
//				}
				logger.info("redis缓存预通话记录任务完成:" + new Date());
			}else{
				logger.info("查询预提醒新进入正在催收案件buyerid为0个" + new Date());
			}
			
		}
		
	}
	
	
	/**
	 * 预缓存通话记录
	 * @param buyerId
	 * @return
	 */
	public String insertDunningTaskJedis(String buyerId){
		int getPageNo = 1;
		int getPageSize = 30;
		String pageKey = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ buyerId + "_PageNo:" + getPageNo + "_PageSize:" + getPageSize ;
		String pagecount = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_listSize_buyerId:"+ buyerId;
		try {
			if(null == JedisUtils.getObjectMap(pageKey)){
				int index = 1;
				int PageNo = 1;
				Map<String, Object> map2 = new HashMap<String, Object>();
				DbUtils dbUtils = new DbUtils();
//				List<TRiskBuyerContactRecords> contactRecordsList = null;
				System.out.println("通话记录buyerId切源查询:" + buyerId);
				List<TRiskBuyerContactRecords> contactRecordsList = dbUtils.findBuyerContactRecordsListByBuyerId(buyerId);
				System.out.println("通话记录条数："+contactRecordsList.size());
				
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
				System.out.println("buyerId:" + buyerId + "缓存已存在");
			}
		} catch (Exception e) {
			System.out.println("通讯失败：buyerid-"+buyerId);
			e.printStackTrace();
			return "f";
		}
		return "t";
	}
	
}


//class ShareData implements Runnable { 
//	
//	private static Logger logger = Logger.getLogger(TRiskBuyerContactRecordsService.class);
//	
//	@Override
//	public synchronized void run() {
//		try {
//			//			synchronized(this) {
//			logger.info("redis预提醒通话记录条数:" + buyerids.size());
//			for(int i = 0; i < data;){
//				data -- ;
//				String buyerId = buyerids.get(data);
//				System.out.println(Thread.currentThread().getName() + ": " + buyerId);
//				this.insertDunningTaskJedis(buyerId);
//			}
//			logger.info("redis缓存预提醒通话记录完成:" + new Date());
//			//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.warn("redis缓存预提醒通话记录异常", e);
//		}
//	}
//	
//}
