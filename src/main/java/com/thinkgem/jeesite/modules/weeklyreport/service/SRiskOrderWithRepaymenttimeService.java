/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderWithRepaymenttime;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SRiskOrderWithRepaymenttimeDao;

/**
 * 月逾期订单催回率Service
 * @author 徐盛
 * @version 2016-11-30
 */
@Service
@Transactional(readOnly = true)
public class SRiskOrderWithRepaymenttimeService extends CrudService<SRiskOrderWithRepaymenttimeDao, SRiskOrderWithRepaymenttime> {

	public SRiskOrderWithRepaymenttime get(String id) {
		return super.get(id);
	}
	
	public List<SRiskOrderWithRepaymenttime> findList(SRiskOrderWithRepaymenttime sRiskOrderWithRepaymenttime) {
		return super.findList(sRiskOrderWithRepaymenttime);
	}
	
	public Page<SRiskOrderWithRepaymenttime> findPage(Page<SRiskOrderWithRepaymenttime> page, SRiskOrderWithRepaymenttime sRiskOrderWithRepaymenttime) {
		return super.findPage(page, sRiskOrderWithRepaymenttime);
	}
	
	@Transactional(readOnly = false)
	public void save(SRiskOrderWithRepaymenttime sRiskOrderWithRepaymenttime) {
		super.save(sRiskOrderWithRepaymenttime);
	}
	
	@Transactional(readOnly = false)
	public void delete(SRiskOrderWithRepaymenttime sRiskOrderWithRepaymenttime) {
		super.delete(sRiskOrderWithRepaymenttime);
	}
	
}