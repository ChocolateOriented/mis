/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportWeek;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SRiskOrderOutReportWeekDao;

/**
 * 委外周报表Service
 * @author 徐盛
 * @version 2016-12-09
 */
@Service
@Transactional(readOnly = true)
public class SRiskOrderOutReportWeekService extends CrudService<SRiskOrderOutReportWeekDao, SRiskOrderOutReportWeek> {

	public SRiskOrderOutReportWeek get(String id) {
		return super.get(id);
	}
	
	public List<SRiskOrderOutReportWeek> findList(SRiskOrderOutReportWeek sRiskOrderOutReportWeek) {
		return super.findList(sRiskOrderOutReportWeek);
	}
	
	public Page<SRiskOrderOutReportWeek> findPage(Page<SRiskOrderOutReportWeek> page, SRiskOrderOutReportWeek sRiskOrderOutReportWeek) {
		return super.findPage(page, sRiskOrderOutReportWeek);
	}
	
	@Transactional(readOnly = false)
	public void save(SRiskOrderOutReportWeek sRiskOrderOutReportWeek) {
		super.save(sRiskOrderOutReportWeek);
	}
	
	@Transactional(readOnly = false)
	public void delete(SRiskOrderOutReportWeek sRiskOrderOutReportWeek) {
		super.delete(sRiskOrderOutReportWeek);
	}
	
}