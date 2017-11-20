/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;

@MyBatisDao
public interface TMisCallingRecordDao extends CrudDao<TMisCallingRecord> {

	public int listCount(TMisCallingRecord entity);

	/**
	 * 根据呼入呼出uuid查询通话记录
	 * @param entity
	 * @return
	 */
	public TMisCallingRecord getRecordByInOutUuid(TMisCallingRecord entity);
	
	/**
	 * 根据手机号开头查询归属地
	 * @param preNumber
	 * @return
	 */
	public String queryMobileLocation(String preNumber);

}