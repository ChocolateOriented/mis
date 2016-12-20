/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.dao.VRiskOrderDailyReportDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderDailyReport;

/**
 * 现金贷日报表Service
 * @author 徐盛
 * @version 2016-06-13
 */
@Service
@Transactional(readOnly = true)
public class VRiskOrderDailyReportService extends CrudService<VRiskOrderDailyReportDao, VRiskOrderDailyReport> {

	public VRiskOrderDailyReport get(String id) {
		return super.get(id);
	}
	
	public List<VRiskOrderDailyReport> findList(VRiskOrderDailyReport vRiskOrderDailyReport) {
		return super.findList(vRiskOrderDailyReport);
	}
	
	public Page<VRiskOrderDailyReport> findPage(Page<VRiskOrderDailyReport> page, VRiskOrderDailyReport vRiskOrderDailyReport) {
//		page.setOrderBy("createtime");
		return super.findPage(page, vRiskOrderDailyReport);
	}
	
	@Transactional(readOnly = false)
	public void save(VRiskOrderDailyReport vRiskOrderDailyReport) {
		super.save(vRiskOrderDailyReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(VRiskOrderDailyReport vRiskOrderDailyReport) {
		super.delete(vRiskOrderDailyReport);
	}
	
}