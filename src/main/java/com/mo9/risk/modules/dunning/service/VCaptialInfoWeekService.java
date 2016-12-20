/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.mo9.risk.modules.dunning.entity.VCaptialInfoWeek;
import com.mo9.risk.modules.dunning.dao.VCaptialInfoWeekDao;

/**
 * 资金成本周报Service
 * @author 徐盛
 * @version 2016-09-25
 */
@Service
@Transactional(readOnly = true)
public class VCaptialInfoWeekService extends CrudService<VCaptialInfoWeekDao, VCaptialInfoWeek> {

	public VCaptialInfoWeek get(String id) {
		return super.get(id);
	}
	
	public List<VCaptialInfoWeek> findList(VCaptialInfoWeek vCaptialInfoWeek) {
		return super.findList(vCaptialInfoWeek);
	}
	
	public Page<VCaptialInfoWeek> findPage(Page<VCaptialInfoWeek> page, VCaptialInfoWeek vCaptialInfoWeek) {
		return super.findPage(page, vCaptialInfoWeek);
	}
	
	@Transactional(readOnly = false)
	public void save(VCaptialInfoWeek vCaptialInfoWeek) {
		super.save(vCaptialInfoWeek);
	}
	
	@Transactional(readOnly = false)
	public void delete(VCaptialInfoWeek vCaptialInfoWeek) {
		super.delete(vCaptialInfoWeek);
	}
	
}