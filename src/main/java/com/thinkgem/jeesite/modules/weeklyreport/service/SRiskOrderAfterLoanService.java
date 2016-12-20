/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SRiskOrderAfterLoanDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderAfterLoan;

/**
 * 贷后风险Service
 * @author 徐盛
 * @version 2016-06-16
 */
@Service
@Transactional(readOnly = true)
public class SRiskOrderAfterLoanService extends CrudService<SRiskOrderAfterLoanDao, SRiskOrderAfterLoan> {

	public SRiskOrderAfterLoan get(String id) {
		return super.get(id);
	}
	
	public List<SRiskOrderAfterLoan> findList(SRiskOrderAfterLoan sRiskOrderAfterLoan) {
		return super.findList(sRiskOrderAfterLoan);
	}
	
	public Page<SRiskOrderAfterLoan> findPage(Page<SRiskOrderAfterLoan> page, SRiskOrderAfterLoan sRiskOrderAfterLoan) {
		return super.findPage(page, sRiskOrderAfterLoan);
	}
	
	@Transactional(readOnly = false)
	public void save(SRiskOrderAfterLoan sRiskOrderAfterLoan) {
		super.save(sRiskOrderAfterLoan);
	}
	
	@Transactional(readOnly = false)
	public void delete(SRiskOrderAfterLoan sRiskOrderAfterLoan) {
		super.delete(sRiskOrderAfterLoan);
	}
	
}