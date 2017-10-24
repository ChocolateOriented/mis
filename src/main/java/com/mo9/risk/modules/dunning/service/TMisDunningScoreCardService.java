/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.manager.RiskScorecardManager;
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

import com.mo9.risk.modules.dunning.dao.TMisDunningScoreCardDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningScoreCard;
import com.mo9.risk.modules.dunning.manager.RiskOrderManager;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.User;

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
	private RiskScorecardManager riskScorecardManager;
	
	private static Logger logger = LoggerFactory.getLogger(TMisDunningScoreCardService.class);
	
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
				logger.info("获取评分数据失败" + e.getMessage());
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
}