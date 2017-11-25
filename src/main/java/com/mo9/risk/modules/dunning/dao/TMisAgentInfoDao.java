/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;

@MyBatisDao
public interface TMisAgentInfoDao extends CrudDao<TMisAgentInfo> {

	/**
	 * 更新坐席信息
	 * @param entity
	 * @return
	 */
	public int updateInfo(TMisAgentInfo entity);

	/**
	 * 保存坐席登录日志
	 * @param entity
	 * @return
	 */
	public int saveLonginLog(TMisAgentInfo entity);

	/**
	 * 根据通话时间获取坐席状态
	 * @param tMisCallingRecord
	 * @return
	 */
	public TMisAgentInfo getAgentStateOnMoment(TMisCallingRecord tMisCallingRecord);

	/**
	 * 根据催收员Id获取坐席
	 * @param peopleId
	 * @return
	 */
	public TMisAgentInfo getInfoByPeopleId(String peopleId);

	/**
	 * 根据坐席号获取坐席
	 * @param agent
	 * @return
	 */
	public TMisAgentInfo getInfoByAgent(String agent);

	/**
	 * 根据分机号获取坐席
	 * @param extension
	 * @return
	 */
	public TMisAgentInfo getInfoByExtension(String extension);

	/**
	 * 根据队列获取坐席
	 * @param queue
	 * @return
	 */
	public TMisAgentInfo getInfoByQueue(String queue);

	/**
	 * 校验坐席号唯一性
	 * @param tmisAgentInfo
	 * @return
	 */
	public TMisAgentInfo validateAgent(TMisAgentInfo tmisAgentInfo);

	/**
	 * 校验队列唯一性
	 * @param tmisAgentInfo
	 * @return
	 */
	public TMisAgentInfo validateQueue(TMisAgentInfo tmisAgentInfo);

	/**
	 * 校验分机号唯一性
	 * @param tmisAgentInfo
	 * @return
	 */
	public TMisAgentInfo validateExtension(TMisAgentInfo tmisAgentInfo);

	/**
	 * 校验直线号唯一性
	 * @param tmisAgentInfo
	 * @return
	 */
	public TMisAgentInfo validateDirect(TMisAgentInfo tmisAgentInfo);
}
