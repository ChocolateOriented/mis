/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.dao.VRiskOrderAfterLoanPushingDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SCashLoanWeeklyReport;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderAfterLoanPushing;

/**
 * 贷后催款情况报表Service
 * @author 徐盛
 * @version 2016-06-20
 */
@Service
@Transactional(readOnly = true)
public class VRiskOrderAfterLoanPushingService extends CrudService<VRiskOrderAfterLoanPushingDao, VRiskOrderAfterLoanPushing> {
	@Autowired
	private VRiskOrderAfterLoanPushingDao vRiskOrderAfterLoanPushingDao;
	
	public VRiskOrderAfterLoanPushingDao getvRiskOrderAfterLoanPushingDao() {
		return vRiskOrderAfterLoanPushingDao;
	}

	public void setvRiskOrderAfterLoanPushingDao(
			VRiskOrderAfterLoanPushingDao vRiskOrderAfterLoanPushingDao) {
		this.vRiskOrderAfterLoanPushingDao = vRiskOrderAfterLoanPushingDao;
	}

	public VRiskOrderAfterLoanPushing get(String id) {
		return super.get(id);
	}
	
	public List<VRiskOrderAfterLoanPushing> findList(VRiskOrderAfterLoanPushing vRiskOrderAfterLoanPushing) {
		return super.findList(vRiskOrderAfterLoanPushing);
	}
	
	public Page<VRiskOrderAfterLoanPushing> findPage(Page<VRiskOrderAfterLoanPushing> page, VRiskOrderAfterLoanPushing vRiskOrderAfterLoanPushing) {
		return super.findPage(page, vRiskOrderAfterLoanPushing);
	}
	
	@Transactional(readOnly = false)
	public void save(VRiskOrderAfterLoanPushing vRiskOrderAfterLoanPushing) {
		super.save(vRiskOrderAfterLoanPushing);
	}
	
	@Transactional(readOnly = false)
	public void delete(VRiskOrderAfterLoanPushing vRiskOrderAfterLoanPushing) {
		super.delete(vRiskOrderAfterLoanPushing);
	}
	
	@Transactional(readOnly = false)
	public List<String> findPushingTotal(Map<String, String> map){
//		List<String> list = vRiskOrderAfterLoanPushingDao.findPushingTotal(map);
		return vRiskOrderAfterLoanPushingDao.findPushingTotal(map);
	}
	
}