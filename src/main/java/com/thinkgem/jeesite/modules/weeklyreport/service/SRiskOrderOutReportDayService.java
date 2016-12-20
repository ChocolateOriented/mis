/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportDay;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SRiskOrderOutReportDayDao;

/**
 * 委外日报表Service
 * @author 徐盛
 * @version 2016-12-09
 */
@Service
@Transactional(readOnly = true)
public class SRiskOrderOutReportDayService extends CrudService<SRiskOrderOutReportDayDao, SRiskOrderOutReportDay> {

	public SRiskOrderOutReportDay get(String id) {
		return super.get(id);
	}
	
	public List<SRiskOrderOutReportDay> findList(SRiskOrderOutReportDay sRiskOrderOutReportDay) {
		return super.findList(sRiskOrderOutReportDay);
	}
	
	public Page<SRiskOrderOutReportDay> findPage(Page<SRiskOrderOutReportDay> page, SRiskOrderOutReportDay sRiskOrderOutReportDay) {
		return super.findPage(page, sRiskOrderOutReportDay);
	}
	
	@Transactional(readOnly = false)
	public void save(SRiskOrderOutReportDay sRiskOrderOutReportDay) {
		super.save(sRiskOrderOutReportDay);
	}
	
	@Transactional(readOnly = false)
	public void delete(SRiskOrderOutReportDay sRiskOrderOutReportDay) {
		super.delete(sRiskOrderOutReportDay);
	}
	
}