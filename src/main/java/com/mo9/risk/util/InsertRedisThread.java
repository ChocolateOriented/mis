package com.mo9.risk.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdbc.DbUtils;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.bean.ThreadBuyerid;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.mo9.risk.modules.dunning.manager.RiskContactRecordsManager;
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
		int getPageSize = 30;
		String pageKey = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ buyerId + "_PageNo:" + getPageNo + "_PageSize:" + getPageSize ;
		String pagecount = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_listSize_buyerId:"+ buyerId;
		try {
			if(null == JedisUtils.getObjectMap(pageKey)){
				int index = 1;
				int PageNo = 1;
				Map<String, Object> map2 = new HashMap<String, Object>();
				DbUtils dbUtils = new DbUtils();
				List<TRiskBuyerContactRecords> contactRecordsList = null;
				RiskContactRecordsManager contactRecordsManager = new RiskContactRecordsManager();
				try {
					contactRecordsList = contactRecordsManager.findByByMobile(mobile);
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
}
