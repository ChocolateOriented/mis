/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.mo9.risk.modules.dunning.entity.SMisDunningTaskMonthReport;
import com.mo9.risk.modules.dunning.dao.SMisDunningTaskMonthReportDao;

/**
 * 催收月绩效Service
 * @author 徐盛
 * @version 2016-11-30
 */
@Service
@Transactional(readOnly = true)
public class SMisDunningTaskMonthReportService extends CrudService<SMisDunningTaskMonthReportDao, SMisDunningTaskMonthReport> {

	public SMisDunningTaskMonthReport get(String id) {
		return super.get(id);
	}
	
	public List<SMisDunningTaskMonthReport> findList(SMisDunningTaskMonthReport sMisDunningTaskMonthReport) {
		return super.findList(sMisDunningTaskMonthReport);
	}
	
	public Page<SMisDunningTaskMonthReport> findPage(Page<SMisDunningTaskMonthReport> page, SMisDunningTaskMonthReport sMisDunningTaskMonthReport) {
		return super.findPage(page, sMisDunningTaskMonthReport);
	}
	
	@Transactional(readOnly = false)
	public void save(SMisDunningTaskMonthReport sMisDunningTaskMonthReport) {
		super.save(sMisDunningTaskMonthReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(SMisDunningTaskMonthReport sMisDunningTaskMonthReport) {
		super.delete(sMisDunningTaskMonthReport);
	}

	/**
	 * @Description 自动邮件
	 * @param
	 * @return void
	 */
	@Scheduled(cron = "0 0 8 * * ?")
	public void autoSendMail(){
	}
}