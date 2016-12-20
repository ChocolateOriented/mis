/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportMonth;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportMonthDetail;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SRiskOrderOutReportMonthDao;

/**
 * 委外月报表Service
 * @author 徐盛
 * @version 2016-12-09
 */
@Service
@Transactional(readOnly = true)
public class SRiskOrderOutReportMonthService extends CrudService<SRiskOrderOutReportMonthDao, SRiskOrderOutReportMonth> {

	public SRiskOrderOutReportMonth get(String id) {
		return super.get(id);
	}
	
	public List<SRiskOrderOutReportMonth> findList(SRiskOrderOutReportMonth sRiskOrderOutReportMonth) {
		return super.findList(sRiskOrderOutReportMonth);
	}
	
	public Page<SRiskOrderOutReportMonth> findPage(Page<SRiskOrderOutReportMonth> page, SRiskOrderOutReportMonth sRiskOrderOutReportMonth) {
		return super.findPage(page, sRiskOrderOutReportMonth);
	}
	
	@Transactional(readOnly = false)
	public void save(SRiskOrderOutReportMonth sRiskOrderOutReportMonth) {
		super.save(sRiskOrderOutReportMonth);
	}
	
	@Transactional(readOnly = false)
	public void delete(SRiskOrderOutReportMonth sRiskOrderOutReportMonth) {
		super.delete(sRiskOrderOutReportMonth);
	}
	
	/**
	 * 点击详情
	 * @param orderOutReportMonth
	 * @return
	 */
	public List<SRiskOrderOutReportMonthDetail> findOutReportMonthDetail(SRiskOrderOutReportMonth orderOutReportMonth){
		return dao.findOutReportMonthDetail(orderOutReportMonth);
	}
	
}