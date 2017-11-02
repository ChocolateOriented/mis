/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;
import java.util.Map;

import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 逾期未还订单视图DAO接口
 * @author beargao
 * @version 2016-07-13
 */
@MyBatisDao
public interface TRiskBuyerPersonalInfoDao {
	public TRiskBuyerPersonalInfo getBuyerInfoByDealcode(String dealcode);
	
	public List<TMisSendMsgInfo> getSelfTelInfo(String buyerId);
	
	public List<TRiskBuyerPersonalInfo> getBuyerListByRepaymentTime(Map<String,Object> params);
	
	public TRiskBuyerPersonalInfo getNewBuyerInfoByDealcode(String dealcode);
	
	public List<TRiskBuyerPersonalInfo> getMessgeByRepaymentTime();
	
	public TRiskBuyerPersonalInfo getbuyerIfo(String dealcode);

	String findMobileByBuyerId(String buyerId);
	
	public TRiskBuyerPersonalInfo getBuyerInfoByMobile(String mobile);
}