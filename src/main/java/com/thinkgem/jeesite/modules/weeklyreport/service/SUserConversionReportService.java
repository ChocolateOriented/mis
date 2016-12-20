/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SUserConversionReport;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SUserConversionReportDao;

/**
 * 用户报表Service
 * @author 徐盛
 * @version 2016-07-20
 */
@Service
@Transactional(readOnly = true)
public class SUserConversionReportService extends CrudService<SUserConversionReportDao, SUserConversionReport> {

	public SUserConversionReport get(String id) {
		return super.get(id);
	}
	
	public List<SUserConversionReport> findList(SUserConversionReport sUserConversionReport) {
		return super.findList(sUserConversionReport);
	}
	
	public Page<SUserConversionReport> findPage(Page<SUserConversionReport> page, SUserConversionReport sUserConversionReport) {
		return super.findPage(page, sUserConversionReport);
	}
	
	@Transactional(readOnly = false)
	public void save(SUserConversionReport sUserConversionReport) {
		super.save(sUserConversionReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(SUserConversionReport sUserConversionReport) {
		super.delete(sUserConversionReport);
	}
	
}