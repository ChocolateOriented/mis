/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerWorkinfo;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 公司信息DAO接口
 * @author beargao
 * @version 2016-07-15
 */
@MyBatisDao
public interface TRiskBuyerWorkinfoDao  {
	public List<TRiskBuyerWorkinfo> getWorkInfoByBuyerId(@Param("buyerId")String buyerId,@Param("dealcode")String dealcode);
	
	public List<TMisSendMsgInfo> getWorkTelByBuyerId(String buyerId);
	
	public TMisSendMsgInfo getWorkTelInfoByBuyerId(String buyerId);
}