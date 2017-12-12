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

import java.math.RoundingMode;
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

	public static final String BLACK_LIST_CACHE_PREFIX = "RiskQualityInfo_blackListRelation_";
	private static final Map<String, String> rounddownMap;
	static  
    {  
		rounddownMap = new HashMap<String, String>();
		rounddownMap.put("120", "a1");
		rounddownMap.put("119", "a2");
		rounddownMap.put("118", "a3");
		rounddownMap.put("117", "a4");
		rounddownMap.put("116", "a5");

		rounddownMap.put("115", "b1");
		rounddownMap.put("114", "b2");
		rounddownMap.put("113", "b3");
		rounddownMap.put("112", "b4");
		rounddownMap.put("111", "b5");

		rounddownMap.put("110", "c1");
		rounddownMap.put("109", "c2");
		rounddownMap.put("108", "c3");
		rounddownMap.put("107", "c4");
		rounddownMap.put("106", "c5");

		rounddownMap.put("105", "d1");
		rounddownMap.put("104", "d2");
		rounddownMap.put("103", "d3");
		rounddownMap.put("102", "d4");
		rounddownMap.put("101", "d5");

		rounddownMap.put("100", "e1");
		rounddownMap.put("99", "e2");
		rounddownMap.put("98", "e3");
		rounddownMap.put("97", "e4");
		rounddownMap.put("96", "e5");

		rounddownMap.put("95", "f1");
		rounddownMap.put("94", "f2");
		rounddownMap.put("93", "f3");
		rounddownMap.put("92", "f4");
		rounddownMap.put("91", "f5");
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
		if(score > 600){
			return "a1";
		}
		if(score < 455){
			return "g";
		}
		DecimalFormat df = new DecimalFormat("0");
		df.setRoundingMode(RoundingMode.DOWN);
		return rounddownMap.get(df.format(score/5.0));
	}

	/**
	 * @Description  获取逾期任务的黑名单联系人信息
	 * @author jxli
	 * @version 2017/12/5
	 */
//	@Scheduled(cron = "0 30 3 * * ?")
	public void refreshBlacklistRelation() {
		final List<String> paymentOrderUserMobile = dao.findPaymentOrderUserMobile();
		logger.info("正在获取黑名单联系人信息, 共"+paymentOrderUserMobile.size());
		int count = 0;
		for (int i = 0; i < paymentOrderUserMobile.size(); i++) {
			String mobile = paymentOrderUserMobile.get(i);
			//只取缓存中没有的数据
			String cache = JedisUtils.get(BLACK_LIST_CACHE_PREFIX +mobile);
			if (StringUtils.isBlank(cache)){
				threadPoolTaskExecutor.submit(new CacheBlacklistRelationTask(mobile));
				count++;
			}
		}
		logger.info("获取黑名单联系人信息任务创建完成, 共"+count);
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
				JedisUtils.set(BLACK_LIST_CACHE_PREFIX + mobile,JSONObject.toJSONString(blackListRelation),0);
			} catch (Exception e) {
				logger.info(mobile+"黑名单联系人获取失败",e);
			}
		}
	}
}
