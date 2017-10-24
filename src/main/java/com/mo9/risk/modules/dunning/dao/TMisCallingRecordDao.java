/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;

import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;

@MyBatisDao
public interface TMisCallingRecordDao extends CrudDao<TMisCallingRecord> {

	public int listCount(TMisCallingRecord entity);
	
	/**
	 * 根据坐席查询未完成的通话记录
	 * @param agent
	 * @return
	 */
	public List<TMisCallingRecord> getUnfinishedCallingByAgent(String agent);
	
	/**
	 * 根据sessionId查询通话记录通话记录
	 * @param sessionId
	 * @return
	 */
	public TMisCallingRecord getRecordBySessionId(String sessionId);
	
	/**
	 * 根据sessionId更新为完成的通话记录
	 * @param tMisCallingRecord
	 * @return
	 */
	public int updateRecordBySessionId(TMisCallingRecord tMisCallingRecord);

}