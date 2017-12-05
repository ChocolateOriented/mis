/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisDunningScoreCardDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningScoreCard;
import com.mo9.risk.modules.dunning.manager.RiskqQualityInfoManager;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.User;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评分卡Service
 * @author shijlu
 * @version 2017-08-29
 */
@Service
@Transactional(readOnly = true)
@Lazy(false)
public class TMisDunningScoreCardService extends CrudService<TMisDunningScoreCardDao, TMisDunningScoreCard> {

	@Autowired
	private RiskqQualityInfoManager riskScorecardManager;
	
	private static Logger logger = LoggerFactory.getLogger(TMisDunningScoreCardService.class);
	
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
				String result = riskScorecardManager.scApplicationCol(mobile, dealcode, buyerId, orderId, oldOrder);
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
				save(tMisDunningScoreCard);
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
}