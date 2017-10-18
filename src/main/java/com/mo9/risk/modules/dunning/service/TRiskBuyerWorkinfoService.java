/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TRiskBuyerWorkinfoDao;
import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerWorkinfo;

/**
 * 公司信息Service
 * @author beargao
 * @version 2016-07-15
 */
@Service
@Transactional(readOnly = true)
public class TRiskBuyerWorkinfoService {

	@Autowired
	private TRiskBuyerWorkinfoDao tRiskBuyerWorkinfoDao;
		
	public List<TRiskBuyerWorkinfo> getWorkInfoByBuyerId(@Param("buyerId")String buyerId,@Param("dealcode")String dealcode){
		return tRiskBuyerWorkinfoDao.getWorkInfoByBuyerId(buyerId,dealcode);
	}
	
	public TMisSendMsgInfo getWorkTelInfoByBuyerId(String buyerId){
		return tRiskBuyerWorkinfoDao.getWorkTelInfoByBuyerId(buyerId);
	}
}