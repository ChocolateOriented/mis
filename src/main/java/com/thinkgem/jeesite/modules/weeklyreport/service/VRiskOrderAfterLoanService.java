/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.dao.VRiskOrderAfterLoanDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderAfterLoan;

/**
 * 贷后还款情况Service
 * @author 徐盛
 * @version 2016-06-20
 */
@Service
@Transactional(readOnly = true)
public class VRiskOrderAfterLoanService extends CrudService<VRiskOrderAfterLoanDao, VRiskOrderAfterLoan> {

	public VRiskOrderAfterLoan get(String id) {
		return super.get(id);
	}
	
	public List<VRiskOrderAfterLoan> findList(VRiskOrderAfterLoan vRiskOrderAfterLoan) {
		return super.findList(vRiskOrderAfterLoan);
	}
	
	public Page<VRiskOrderAfterLoan> findPage(Page<VRiskOrderAfterLoan> page, VRiskOrderAfterLoan vRiskOrderAfterLoan) {
		return super.findPage(page, vRiskOrderAfterLoan);
	}
	
	@Transactional(readOnly = false)
	public void save(VRiskOrderAfterLoan vRiskOrderAfterLoan) {
		super.save(vRiskOrderAfterLoan);
	}
	
	@Transactional(readOnly = false)
	public void delete(VRiskOrderAfterLoan vRiskOrderAfterLoan) {
		super.delete(vRiskOrderAfterLoan);
	}
	
}