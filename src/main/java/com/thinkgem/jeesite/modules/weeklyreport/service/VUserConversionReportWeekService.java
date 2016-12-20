/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VUserConversionReportWeek;
import com.thinkgem.jeesite.modules.weeklyreport.dao.VUserConversionReportWeekDao;

/**
 * 用户周报表Service
 * @author 徐盛
 * @version 2016-07-20
 */
@Service
@Transactional(readOnly = true)
public class VUserConversionReportWeekService extends CrudService<VUserConversionReportWeekDao, VUserConversionReportWeek> {

	public VUserConversionReportWeek get(String id) {
		return super.get(id);
	}
	
	public List<VUserConversionReportWeek> findList(VUserConversionReportWeek vUserConversionReportWeek) {
		return super.findList(vUserConversionReportWeek);
	}
	
	public Page<VUserConversionReportWeek> findPage(Page<VUserConversionReportWeek> page, VUserConversionReportWeek vUserConversionReportWeek) {
		return super.findPage(page, vUserConversionReportWeek);
	}
	
	
}