/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.mo9.risk.modules.dunning.entity.TMisDunnedHistory;
import com.mo9.risk.modules.dunning.dao.TMisDunnedHistoryDao;

/**
 * 催收历史Service
 * @author 徐盛
 * @version 2016-07-12
 */
@Service
@Transactional(readOnly = true)
public class TMisDunnedHistoryService extends CrudService<TMisDunnedHistoryDao, TMisDunnedHistory> {

	public TMisDunnedHistory get(String id) {
		return super.get(id);
	}
	
	public List<TMisDunnedHistory> findList(TMisDunnedHistory tMisDunnedHistory) {
		return super.findList(tMisDunnedHistory);
	}
	
	public Page<TMisDunnedHistory> findPage(Page<TMisDunnedHistory> page, TMisDunnedHistory tMisDunnedHistory) {
		return super.findPage(page, tMisDunnedHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisDunnedHistory tMisDunnedHistory) {
		super.save(tMisDunnedHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisDunnedHistory tMisDunnedHistory) {
		super.delete(tMisDunnedHistory);
	}
	
}