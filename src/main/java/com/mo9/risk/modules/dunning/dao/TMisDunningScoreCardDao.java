/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;

import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningScoreCard;

/**
 * 评分卡DAO接口
 * @author shijlu
 * @version 2017-08-29
 */
@MyBatisDao
public interface TMisDunningScoreCardDao extends CrudDao<TMisDunningScoreCard> {

	/**
	 * 根据订单号获取评分卡
	 * @param dealcode
	 * @return
	 */
	public TMisDunningScoreCard getScoreCardByDealcode(String dealcode);
	
	/**
	 * 获取逾期江湖救急订单
	 * @param order
	 * @return
	 */
	public List<DunningOrder> getNewRiskOrder(DunningOrder order);
}