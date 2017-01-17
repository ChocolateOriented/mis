/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 通话记录DAO接口
 * @author beragao
 * @version 2016-07-15
 */
@MyBatisDao
public interface TRiskBuyerContactRecordsDao {
	
	public List<TRiskBuyerContactRecords> findList(TRiskBuyerContactRecords tRiskBuyerContactRecords);	

	public List<TMisSendMsgInfo> getCommunicateByBuyerId (String buyerId);
	
	/**
	 * 通话记录redis缓存查询
	 * @param tRiskBuyerContactRecords
	 * @return
	 */
	public List<TRiskBuyerContactRecords> findBuyerContactRecordsListByBuyerId(TRiskBuyerContactRecords tRiskBuyerContactRecords);	
	
	/**
	 * 催收操作记录
	 * @param tRiskBuyerContactRecords
	 * @return
	 */
	public List<TRiskBuyerContactRecords> findContantNumberListBydealcode(TRiskBuyerContactRecords tRiskBuyerContactRecords);	

}