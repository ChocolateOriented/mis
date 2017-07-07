/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisRemittanceConfirmDao;
import com.mo9.risk.modules.dunning.dao.TMisRemittanceConfirmLogDao;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirmLog;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;

/**
 * 汇款确认信息日志Service
 * @author shijlu
 * @version 2017-05-26
 */
@Service
@Transactional(readOnly = true)
public class TMisRemittanceConfirmLogService extends CrudService<TMisRemittanceConfirmLogDao, TMisRemittanceConfirmLog> {
	
	@Autowired
	private TMisRemittanceConfirmDao misRemittanceConfirmDao;
	
	@Override
	public TMisRemittanceConfirmLog get(String id) {
		return super.get(id);
	}
	
	@Override
	public List<TMisRemittanceConfirmLog> findList(TMisRemittanceConfirmLog tMisRemittanceConfirmLog) {
		return super.findList(tMisRemittanceConfirmLog);
	}
	
	@Override
	public Page<TMisRemittanceConfirmLog> findPage(Page<TMisRemittanceConfirmLog> page, TMisRemittanceConfirmLog tMisRemittanceConfirmLog) {
		return super.findPage(page, tMisRemittanceConfirmLog);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void save(TMisRemittanceConfirmLog tMisRemittanceConfirmLog) {
		super.save(tMisRemittanceConfirmLog);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void delete(TMisRemittanceConfirmLog tMisRemittanceConfirm) {
		super.delete(tMisRemittanceConfirm);
	}
	
	@Transactional(readOnly = false)
	public void saveLog(TMisRemittanceConfirm record) {
		if (record == null) {
			throw new ServiceException("record is not existed");
		}
		TMisRemittanceConfirmLog logData = new TMisRemittanceConfirmLog(record);
		save(logData);
	}
	
	@Transactional(readOnly = false)
	public void saveLog(String id) {
		TMisRemittanceConfirm record = misRemittanceConfirmDao.get(id);
		saveLog(record);
	}
	
	@Transactional(readOnly = false)
	public void saveLog(List<String> ids) {
		for (String id : ids) {
			saveLog(id);
		}
	}
}