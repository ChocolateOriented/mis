/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.bean.CallCenterWebSocketMessage;
import com.mo9.risk.modules.dunning.dao.TMisAgentInfoDao;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;

@Service
@Transactional(readOnly = true)
public class TMisAgentInfoService extends CrudService<TMisAgentInfoDao, TMisAgentInfo> {
	
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	/**
	 * 更新坐席状态
	 * @param agentInfo
	 * @return
	 */
	@Transactional(readOnly = false)
	public void updateStatus(TMisAgentInfo agentInfo) {
		dao.updateInfo(agentInfo);
		saveLonginLog(agentInfo);
	}

	/**
	 * 更新坐席状态
	 * @param msg
	 * @return
	 */
	@Transactional(readOnly = false)
	public void updateStatus(CallCenterWebSocketMessage msg) {
		TMisAgentInfo loginLog = new TMisAgentInfo();
		loginLog.setAgent(msg.getAgent());
		loginLog.setPeopleId(msg.getPeopleId());
		loginLog.setStatus(msg.getOperation());
		updateStatus(loginLog);
	}

	/**
	 * 更新坐席信息
	 * <p>Update by peopleId. If peopleId is empty, update by agent<p/>
	 * @param agentInfo
	 * @return
	 */
	@Transactional(readOnly = false)
	public void updateInfo(TMisAgentInfo agentInfo) {
		dao.updateInfo(agentInfo);
	}

	/**
	 * 保存坐席登录状态日志
	 * @param agentInfo
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveLonginLog(TMisAgentInfo agentInfo) {
		agentInfo.preInsert();
		dao.saveLonginLog(agentInfo);
	}

	/**
	 * 根据催收人员id获取坐席信息
	 * @param peopleId
	 * @return
	 */
	public TMisAgentInfo getInfoByPeopleId(String peopleId) {
		return dao.getInfoByPeopleId(peopleId);
	}
	
	/**
	 * 根据坐席号获取坐席信息
	 * @param agent
	 * @return
	 */
	public TMisAgentInfo getInfoByAgent(String agent) {
		return dao.getInfoByAgent(agent);
	}
	
	/**
	 * 根据分机获取坐席信息
	 * @param extension
	 * @return
	 */
	public TMisAgentInfo getInfoByExtension(String extension) {
		return dao.getInfoByExtension(extension);
	}
	
	public Page<TMisAgentInfo> findPageList(Page<TMisAgentInfo> page, TMisAgentInfo entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}
	
//	@Transactional(readOnly = false)
//	public void saveAgent(TMisAgentInfo tmisAgentInfo) {
//		if(StringUtils.isNotEmpty(tmisAgentInfo.getId())){
//			TMisAgentInfo info = dao.getInfoByPeopleId(tmisAgentInfo.getId());
//			TMisDunningPeople tMisDunningPeople = tMisDunningPeopleService.get(info.getPeopleId());
//			tMisDunningPeople.setAgent(tmisAgentInfo.getAgent());
//			tMisDunningPeople.setExtensionNumber(tmisAgentInfo.getAgent());
//			tMisDunningPeopleService.save(tMisDunningPeople);
//		}
//		super.save(tmisAgentInfo);
//	}
//	
//	@Transactional(readOnly = false)
//	public void deleteAgent(TMisAgentInfo tmisAgentInfo) {
//		TMisDunningPeople tdp=tMisDunningPeopleService.getByAgent(tmisAgentInfo.getAgent());
//		if(tdp!=null){
//			tdp.setAgent(null);
//			tdp.setExtensionNumber(null);;
//			tMisDunningPeopleService.save(tdp);
//		}
//		super.delete(tmisAgentInfo);
//	}

	public Boolean validateAgent(TMisAgentInfo tmisAgentInfo) {
		return dao.validateAgent(tmisAgentInfo)==null;
	}

	public Boolean validateQueue(TMisAgentInfo tmisAgentInfo) {
		return dao.validateQueue(tmisAgentInfo)==null;
	}

	public Boolean validateExtension(TMisAgentInfo tmisAgentInfo) {
		return dao.validateExtension(tmisAgentInfo)==null;
	}
}