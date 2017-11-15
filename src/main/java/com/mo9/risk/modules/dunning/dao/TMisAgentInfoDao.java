/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;

import java.util.List;

@MyBatisDao
public interface TMisAgentInfoDao extends CrudDao<TMisAgentInfo> {

	public int updateInfo(TMisAgentInfo entity);

	public int saveLonginLog(TMisAgentInfo entity);

	public List<TMisCallingRecord> getLoginLogTodaybyId(TMisCallingRecord tMisCallingRecord);
	
	public TMisAgentInfo getInfoByPeopleId(String peopleId);
	
	public TMisAgentInfo getInfoByAgent(String agent);
	
	public TMisAgentInfo getInfoByExtension(String extension);
	
	public TMisAgentInfo getInfoByQueue(String queue);

	public TMisAgentInfo validateAgent(TMisAgentInfo tmisAgentInfo);

	public TMisAgentInfo validateQueue(TMisAgentInfo tmisAgentInfo);
	
	public TMisAgentInfo validateExtension(TMisAgentInfo tmisAgentInfo);

	public TMisAgentInfo validateDirect(TMisAgentInfo tmisAgentInfo);
}
