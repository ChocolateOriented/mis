/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningScoreCard;
import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import java.util.List;

/**
 * 评分卡DAO接口
 * @author shijlu
 * @version 2017-08-29
 */
@MyBatisDao
public interface RiskQualityInfoDao  extends BaseDao {

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

	/**
	 * @Description 保存评分卡 
	 * @param tMisDunningScoreCard
	 * @return void
	 */
	void insertScoreCard(TMisDunningScoreCard tMisDunningScoreCard);

	/**
	 * @Description 查询未还清订单用户的手机
	 * @param
	 * @return java.util.List<java.lang.String>
	 */
	List<String> findPaymentOrderUserMobile();
}