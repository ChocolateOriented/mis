/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.mo9.risk.modules.dunning.entity.TMisDunningTaskLog;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskLogDao;

/**
 * 催收任务logService
 * @author 徐盛
 * @version 2017-03-01
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningTaskLogService extends CrudService<TMisDunningTaskLogDao, TMisDunningTaskLog> {

	public TMisDunningTaskLog get(String id) {
		return super.get(id);
	}
	
	public List<TMisDunningTaskLog> findList(TMisDunningTaskLog tMisDunningTaskLog) {
		return super.findList(tMisDunningTaskLog);
	}
	
	public Page<TMisDunningTaskLog> findPage(Page<TMisDunningTaskLog> page, TMisDunningTaskLog tMisDunningTaskLog) {
		return super.findPage(page, tMisDunningTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisDunningTaskLog tMisDunningTaskLog) {
		super.save(tMisDunningTaskLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisDunningTaskLog tMisDunningTaskLog) {
		super.delete(tMisDunningTaskLog);
	}
	
}