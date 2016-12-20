/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SCashFlowReport;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SCashFlowReportDao;

/**
 * 资金流日报Service
 * @author 徐盛
 * @version 2016-11-18
 */
@Service
@Transactional(readOnly = true)
public class SCashFlowReportService extends CrudService<SCashFlowReportDao, SCashFlowReport> {

	public SCashFlowReport get(String id) {
		return super.get(id);
	}
	
	public List<SCashFlowReport> findList(SCashFlowReport sCashFlowReport) {
		return super.findList(sCashFlowReport);
	}
	
	public Page<SCashFlowReport> findPage(Page<SCashFlowReport> page, SCashFlowReport sCashFlowReport) {
		return super.findPage(page, sCashFlowReport);
	}
	
	@Transactional(readOnly = false)
	public void save(SCashFlowReport sCashFlowReport) {
		super.save(sCashFlowReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(SCashFlowReport sCashFlowReport) {
		super.delete(sCashFlowReport);
	}
	
}