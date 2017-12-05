/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.bean.BlackListRelation;
import com.mo9.risk.modules.dunning.dao.RiskQualityInfoDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningScoreCard;
import com.mo9.risk.modules.dunning.manager.RiskQualityInfoManager;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 用户质量信息
 * @author jxli
 * @version 2017/12/5
 */
@Service
@Transactional(readOnly = true)
@Lazy(false)
public class RiskQualityInfoService extends BaseService{

	@Autowired
	RiskQualityInfoDao dao;
	@Autowired
	private RiskQualityInfoManager qualityInfoManager;
	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;

	private static final String BLACK_LIST_CACHE_PREFIX = "RiskQualityInfo_blackListRelation_";
	private static final Map<String, String> rounddownMap;
	static  
    {  
		rounddownMap = new HashMap<String, String>();  
		rounddownMap.put("6.5", "a1");  
		rounddownMap.put("6.4", "a2");  
		rounddownMap.put("6.3", "a3");  
		rounddownMap.put("6.2", "a4");  
		rounddownMap.put("6.1", "a5");  
		
		rounddownMap.put("6.0", "b1");  
		rounddownMap.put("5.9", "b2");  
		rounddownMap.put("5.8", "b3");  
		rounddownMap.put("5.7", "b4");  
		rounddownMap.put("5.6", "b5");  
		
		rounddownMap.put("5.5", "c1");  
		rounddownMap.put("5.4", "c2");  
		rounddownMap.put("5.3", "c3");  
		rounddownMap.put("5.2", "c4");  
		rounddownMap.put("5.1", "c5");  
		
		rounddownMap.put("5.0", "d1");  
		rounddownMap.put("4.9", "d2");  
		rounddownMap.put("4.8", "d3");  
		rounddownMap.put("4.7", "d4");  
		rounddownMap.put("4.6", "d5");  
		
		rounddownMap.put("4.5", "e1");  
		rounddownMap.put("4.4", "e2");  
		rounddownMap.put("4.3", "e3");  
		rounddownMap.put("4.2", "e4");  
		rounddownMap.put("4.1", "e5");  
		
		rounddownMap.put("4.0", "f1");  
		rounddownMap.put("3.9", "f2");  
		rounddownMap.put("3.8", "f3");  
		rounddownMap.put("3.7", "f4");  
		rounddownMap.put("3.6", "f5");  
		
		rounddownMap.put("3.5", "g1");  
		rounddownMap.put("3.4", "g2");  
		rounddownMap.put("3.3", "g3");  
		rounddownMap.put("3.2", "g4");  
		rounddownMap.put("3.1", "g5");  
    }
	
	/**
	 * 根据订单号获取评分卡
	 * @param dealcode
	 * @return
	 */
	public TMisDunningScoreCard getScoreCardByDealcode(String dealcode) {
		return dao.getScoreCardByDealcode(dealcode);
	}
	
	/**
	 * 批量获取评分卡数据
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	@Transactional(readOnly = false)
	public void queryRiskOrderScoreCard() {
		DunningOrder dunningOrder = new DunningOrder();
		dunningOrder.setOverduedays(-1);
		List<DunningOrder> orders = dao.getNewRiskOrder(dunningOrder);
		
		if (orders == null || orders.size() == 0) {
			logger.info("没有需要获取评分的订单");
			return;
		}
		
		for (DunningOrder order : orders) {
			String mobile = order.getMobile();
			String dealcode = order.getDealcode();
			String buyerId = String.valueOf(order.getBuyerid());
			String orderId = order.getId();
			boolean oldOrder = order.getFirstOrder() == null ? false : !order.getFirstOrder();
			
			try {
				String result = qualityInfoManager.scApplicationCol(mobile, dealcode, buyerId, orderId, oldOrder);
				if (null == result || "".equals(result)) {
					logger.info("获取评分数据无效，订单号" + dealcode);
					continue;
				}
				double score = Double.parseDouble(result);
				String grade = getCalculateGrade(score);
				
				TMisDunningScoreCard tMisDunningScoreCard = new TMisDunningScoreCard();
				tMisDunningScoreCard.setDealcode(dealcode);
				tMisDunningScoreCard.setBuyerid(buyerId);
				tMisDunningScoreCard.setBuyername(order.getRealname());
				tMisDunningScoreCard.setMobile(mobile);
				tMisDunningScoreCard.setScore(score);
				tMisDunningScoreCard.setGrade(grade);
				
				User user = new User("auto_admin");
				user.setName("auto_admin");
				tMisDunningScoreCard.setCreateBy(user);
				tMisDunningScoreCard.setUpdateBy(user);
				dao.insertScoreCard(tMisDunningScoreCard);
			} catch (Exception e) {
				logger.info("订单" + dealcode + "获取评分数据失败" + e.getMessage());
			}
		}
	}
	
	private String getCalculateGrade(double score){
		if(score > 650){
			return "a1";
		}
		if(score < 310){
			return "g5";
		}
		DecimalFormat df = new DecimalFormat("#,###.0");
		return rounddownMap.get(df.format(score * 0.01));
	}

	/**
	 * @Description  获取全部任务的黑名单联系人信息
	 * @author jxli
	 * @version 2017/12/5
	 */
	@Scheduled(cron = "0 30 4 * * ?")
	public void refreshBlacklistRelation() {
		final List<String> paymentOrderUserMobile = dao.findPaymentOrderUserMobile();
		logger.info("正在获取黑名单联系人信息, 共"+paymentOrderUserMobile.size());
		for (int i = 0; i < paymentOrderUserMobile.size(); i++) {
			String mobile = paymentOrderUserMobile.get(i);
			threadPoolTaskExecutor.submit(new CacheBlacklistRelationTask(mobile));
		}
	}
	/**
	 * @Description 通过手机号, 从缓存获取黑名单联系人信息
	 * @param mobile
	 * @return com.mo9.risk.modules.dunning.bean.BlackListRelation
	 */
	public BlackListRelation getBlackListRelationByMobile(String mobile) {
		String cache = JedisUtils.get(BLACK_LIST_CACHE_PREFIX +mobile);
		if (StringUtils.isBlank(cache)){
			return null ;
		}
		return JSON.parseObject(cache,BlackListRelation.class);
	}

	class CacheBlacklistRelationTask implements Runnable {

		private String mobile;

		public CacheBlacklistRelationTask(String mobile) {
			this.mobile = mobile;
		}

		@Override
		public void run() {
			try {
				BlackListRelation blackListRelation = qualityInfoManager.blackListRelation(mobile);
				JedisUtils.set(BLACK_LIST_CACHE_PREFIX + mobile,JSONObject.toJSONString(blackListRelation),60*60*24*2);
			} catch (Exception e) {
				logger.info(mobile+"黑名单联系人获取失败",e);
			}
		}
	}
}
