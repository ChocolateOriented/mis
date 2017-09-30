/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessagChecked;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 财务确认汇款信息DAO接口
 * @author 徐盛
 * @version 2016-08-11
 */
@MyBatisDao
public interface TMisRemittanceMessageDao extends CrudDao<TMisRemittanceMessage> {
	
	public TMisRemittanceMessage findRemittanceMesListByDealcode(String code);

	public List<TMisRemittanceMessage> findBySerialNumbers(@Param("list") List<TMisRemittanceMessage> tMisRemittanceList,@Param("channel")String channel);

	public int saveList(@Param("list") List<TMisRemittanceMessage> tMisRemittanceList);

	void batchUpdateMatched(List<TMisRemittanceMessage> successMatchList);

	public List<TMisRemittanceMessage> findAcountPageList( TMisRemittanceMessage entity);

	List<TMisRemittanceMessage> findAfterFinancialTimeNotAuditList(Date date);

	public void updateList(@Param("item") TMisRemittanceMessage tMisRemittanceMessage,@Param("channel")String channel);

	public List<TMisRemittanceMessagChecked> findMessagCheckedList(TMisRemittanceMessagChecked entity);

	List<TMisRemittanceConfirm> findNotFinish(@Param("remittanceChannel") String remittanceChannel,@Param("remittanceSerialNumber") String remittanceSerialNumber);

	public List<TMisRemittanceMessagChecked> findMessagFinishList(TMisRemittanceMessagChecked entity);

	TMisRemittanceMessagChecked findRemittanceMessagChecked(String remittanceConfirmId);
}