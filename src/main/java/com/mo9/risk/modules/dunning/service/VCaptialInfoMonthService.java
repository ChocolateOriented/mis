/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.mo9.risk.modules.dunning.entity.VCaptialInfoMonth;
import com.mo9.risk.modules.dunning.dao.VCaptialInfoMonthDao;

/**
 * 资金成本月报Service
 * @author 徐盛
 * @version 2016-09-25
 */
@Service
@Transactional(readOnly = true)
public class VCaptialInfoMonthService extends CrudService<VCaptialInfoMonthDao, VCaptialInfoMonth> {

	public VCaptialInfoMonth get(String id) {
		return super.get(id);
	}
	
	public List<VCaptialInfoMonth> findList(VCaptialInfoMonth vCaptialInfoMonth) {
		return super.findList(vCaptialInfoMonth);
	}
	
	public Page<VCaptialInfoMonth> findPage(Page<VCaptialInfoMonth> page, VCaptialInfoMonth vCaptialInfoMonth) {
		return super.findPage(page, vCaptialInfoMonth);
	}
	
	@Transactional(readOnly = false)
	public void save(VCaptialInfoMonth vCaptialInfoMonth) {
		super.save(vCaptialInfoMonth);
	}
	
	@Transactional(readOnly = false)
	public void delete(VCaptialInfoMonth vCaptialInfoMonth) {
		super.delete(vCaptialInfoMonth);
	}
	
}