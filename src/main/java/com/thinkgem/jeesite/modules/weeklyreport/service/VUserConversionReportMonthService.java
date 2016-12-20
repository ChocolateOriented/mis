/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VUserConversionReportMonth;
import com.thinkgem.jeesite.modules.weeklyreport.dao.VUserConversionReportMonthDao;

/**
 * 用户月报表Service
 * @author 徐盛
 * @version 2016-07-21
 */
@Service
@Transactional(readOnly = true)
public class VUserConversionReportMonthService extends CrudService<VUserConversionReportMonthDao, VUserConversionReportMonth> {

	public VUserConversionReportMonth get(String id) {
		return super.get(id);
	}
	
	public List<VUserConversionReportMonth> findList(VUserConversionReportMonth vUserConversionReportMonth) {
		return super.findList(vUserConversionReportMonth);
	}
	
	public Page<VUserConversionReportMonth> findPage(Page<VUserConversionReportMonth> page, VUserConversionReportMonth vUserConversionReportMonth) {
		return super.findPage(page, vUserConversionReportMonth);
	}
	
	@Transactional(readOnly = false)
	public void save(VUserConversionReportMonth vUserConversionReportMonth) {
		super.save(vUserConversionReportMonth);
	}
	
	@Transactional(readOnly = false)
	public void delete(VUserConversionReportMonth vUserConversionReportMonth) {
		super.delete(vUserConversionReportMonth);
	}
	
}