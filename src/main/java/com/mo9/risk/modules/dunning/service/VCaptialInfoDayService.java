/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.mo9.risk.modules.dunning.entity.VCaptialInfoDay;
import com.mo9.risk.modules.dunning.dao.VCaptialInfoDayDao;

/**
 * 资金成本日报Service
 * @author 徐盛
 * @version 2016-09-25
 */
@Service
@Transactional(readOnly = true)
public class VCaptialInfoDayService extends CrudService<VCaptialInfoDayDao, VCaptialInfoDay> {

	public VCaptialInfoDay get(String id) {
		return super.get(id);
	}
	
	public List<VCaptialInfoDay> findList(VCaptialInfoDay vCaptialInfoDay) {
		return super.findList(vCaptialInfoDay);
	}
	
	public Page<VCaptialInfoDay> findPage(Page<VCaptialInfoDay> page, VCaptialInfoDay vCaptialInfoDay) {
		return super.findPage(page, vCaptialInfoDay);
	}
	
	@Transactional(readOnly = false)
	public void save(VCaptialInfoDay vCaptialInfoDay) {
		super.save(vCaptialInfoDay);
	}
	
	@Transactional(readOnly = false)
	public void delete(VCaptialInfoDay vCaptialInfoDay) {
		super.delete(vCaptialInfoDay);
	}
	
}