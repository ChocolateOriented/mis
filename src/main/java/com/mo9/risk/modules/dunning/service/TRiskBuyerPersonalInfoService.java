/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TRiskBuyerPersonalInfoDao;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;

/**
 * 逾期未还订单视图Service
 * @author beargao
 * @version 2016-07-13
 */
@Service
@Transactional(readOnly = true)
public class TRiskBuyerPersonalInfoService {

	@Autowired
	private TRiskBuyerPersonalInfoDao personalInfoDao;
	
	public TRiskBuyerPersonalInfo getBuyerInfoByDealcode(String dealcode){
		return personalInfoDao.getBuyerInfoByDealcode(dealcode);
	}
	
	
	public List<TRiskBuyerPersonalInfo> getBuyerListByRepaymentTime(Map<String,Object> params){
		return personalInfoDao.getBuyerListByRepaymentTime(params);
	}
	
	public TRiskBuyerPersonalInfo getNewBuyerInfoByDealcode(String dealcode){
		return personalInfoDao.getNewBuyerInfoByDealcode(dealcode);
	}
	
	public TRiskBuyerPersonalInfo getBuyerInfoByMobile(String mobile){
		return personalInfoDao.getBuyerInfoByMobile(mobile);
	}
}