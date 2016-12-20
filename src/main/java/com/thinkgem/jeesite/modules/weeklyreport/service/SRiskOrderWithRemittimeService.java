/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderWithRemittime;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SRiskOrderWithRemittimeDao;

/**
 * 月放款订单催回率Service
 * @author 徐盛
 * @version 2016-11-30
 */
@Service
@Transactional(readOnly = true)
public class SRiskOrderWithRemittimeService extends CrudService<SRiskOrderWithRemittimeDao, SRiskOrderWithRemittime> {

	public SRiskOrderWithRemittime get(String id) {
		return super.get(id);
	}
	
	public List<SRiskOrderWithRemittime> findList(SRiskOrderWithRemittime sRiskOrderWithRemittime) {
		return super.findList(sRiskOrderWithRemittime);
	}
	
	public Page<SRiskOrderWithRemittime> findPage(Page<SRiskOrderWithRemittime> page, SRiskOrderWithRemittime sRiskOrderWithRemittime) {
		return super.findPage(page, sRiskOrderWithRemittime);
	}
	
	@Transactional(readOnly = false)
	public void save(SRiskOrderWithRemittime sRiskOrderWithRemittime) {
		super.save(sRiskOrderWithRemittime);
	}
	
	@Transactional(readOnly = false)
	public void delete(SRiskOrderWithRemittime sRiskOrderWithRemittime) {
		super.delete(sRiskOrderWithRemittime);
	}
	
}