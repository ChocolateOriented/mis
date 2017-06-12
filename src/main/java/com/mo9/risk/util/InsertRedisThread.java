package com.mo9.risk.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdbc.DbUtils;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.thinkgem.jeesite.common.utils.JedisUtils;


@Transactional(readOnly = true)
public class InsertRedisThread  implements Runnable{
	
	private static Logger logger = Logger.getLogger(InsertRedisThread.class);
//	@Autowired
//	private TMisDunningTaskDao tMisDunningTaskDao;
	
	private Vector<String> buyerids;
	public Vector<String> getBuyerids() {
		return buyerids;
	}
	public void setBuyerids(Vector<String> buyerids) {
		this.buyerids = buyerids;
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
			if(!buyerids.isEmpty()){
//				synchronized(this) {
				logger.info("redis预提醒通话记录条数:" + buyerids.size());
				for(String buyerid : buyerids){
					System.out.println(buyerid + "=========" + Thread.currentThread().getName());
					insertDunningTaskJedis(buyerid,cacheSeconds);
				}
				logger.info("线程" + Thread.currentThread().getName() +"redis缓存预提醒通话记录完成:" + new Date());
//				}
			}else{
				logger.info("线程" + Thread.currentThread().getName() +"查询预提醒新进入正在催收案件buyerid为0个" + new Date());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("线程" + Thread.currentThread().getName() +"redis缓存预提醒通话记录异常", e);
		}
	}
	
	/**
	 * 预缓存通话记录
	 * @param buyerId
	 * @return
	 */
	public String insertDunningTaskJedis(String buyerId,int cacheSeconds){
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
				System.out.println("预缓存通话记录buyerId切源查询:" + buyerId);
				List<TRiskBuyerContactRecords> contactRecordsList = dbUtils.findBuyerContactRecordsListByBuyerId(buyerId);
				System.out.println("预缓存通话记录条数："+contactRecordsList.size());
				
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
			System.out.println("预缓存通讯失败：buyerid-"+buyerId);
			e.printStackTrace();
			return "f";
		}
		return "t";
	}			
}
