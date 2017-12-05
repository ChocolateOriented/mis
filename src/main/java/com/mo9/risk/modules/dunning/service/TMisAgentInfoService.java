/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.bean.CallCenterBaseAction;
import com.mo9.risk.modules.dunning.bean.CallCenterWebSocketMessage;
import com.mo9.risk.modules.dunning.dao.TMisAgentInfoDao;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;
import com.mo9.risk.modules.dunning.manager.CallCenterManager;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

@Service
@Transactional(readOnly = true)
public class TMisAgentInfoService extends CrudService<TMisAgentInfoDao, TMisAgentInfo> {
	
	@Autowired
	private CallCenterManager callCenterManager;
	
	public static final Map<String, String> callStatus = new ConcurrentHashMap<String, String>();
	
	@Override
	@Transactional(readOnly = false)
	public void save(TMisAgentInfo entity) {
		if (entity.getIsNewRecord()) {
			CallCenterBaseAction action = new CallCenterBaseAction();
			action.setAgent(entity.getAgent());
			action.setExtension(entity.getExtension());
			try {
				callCenterManager.bindExtension(action);
			} catch (Exception e) {
				logger.info(entity.getAgent() + "绑定坐席分机失败");
			}
		}
		super.save(entity);
	}

	/**
	 * 更新坐席状态
	 * @param agentInfo
	 * @return
	 */
	@Transactional(readOnly = false)
	public void updateStatus(TMisAgentInfo agentInfo) {
		agentInfo.preUpdate();
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
	
	/**
	 * 根据队列获取坐席信息
	 * @param queue
	 * @return
	 */
	public TMisAgentInfo getInfoByQueue(String queue) {
		return dao.getInfoByQueue(queue);
	}
	
	/**
	 * 获取所有坐席信息
	 * @param queue
	 * @return
	 */
	public List<TMisAgentInfo> findAllList() {
		return dao.findAllList(null);
	}
	
	/**
	 * 根据通话时间获取坐席状态
	 * @param tMisCallingRecord
	 * @return
	 */
	public TMisAgentInfo getAgentStateOnMoment(TMisCallingRecord tMisCallingRecord) {
		return dao.getAgentStateOnMoment(tMisCallingRecord);
	}
	
	public Page<TMisAgentInfo> findPageList(Page<TMisAgentInfo> page, TMisAgentInfo entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 校验坐席号唯一性
	 * @param tmisAgentInfo
	 * @return
	 */
	public Boolean validateAgent(TMisAgentInfo tmisAgentInfo) {
		return dao.validateAgent(tmisAgentInfo) == null;
	}

	/**
	 * 校验队列唯一性
	 * @param tmisAgentInfo
	 * @return
	 */
	public Boolean validateQueue(TMisAgentInfo tmisAgentInfo) {
		return dao.validateQueue(tmisAgentInfo) == null;
	}

	/**
	 * 校验分机号唯一性
	 * @param tmisAgentInfo
	 * @return
	 */
	public Boolean validateExtension(TMisAgentInfo tmisAgentInfo) {
		return dao.validateExtension(tmisAgentInfo) == null;
	}

	/**
	 * 校验直线号唯一性
	 * @param tmisAgentInfo
	 * @return
	 */
	public Boolean validateDirect(TMisAgentInfo tmisAgentInfo) {
		return dao.validateDirect(tmisAgentInfo) == null;
	}
}