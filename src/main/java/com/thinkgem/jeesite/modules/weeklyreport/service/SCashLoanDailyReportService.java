/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportOrderMonthBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportOrderWeekBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportRepayMonthBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportRepayWeekBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanMonthReportBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanWeekReportBean;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SCashLoanDailyReportDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SCashLoanDailyReport;

/**
 * 财务日报Service
 * @author 徐盛
 * @version 2016-06-23
 */
@Service
@Transactional(readOnly = true)
public class SCashLoanDailyReportService extends CrudService<SCashLoanDailyReportDao, SCashLoanDailyReport> {
	
	@Autowired
	private SCashLoanDailyReportDao sCashLoanDailyReportDao;

	public SCashLoanDailyReport get(String id) {
		return super.get(id);
	}
	
//	public SCashLoanDailyReport get(SCashLoanDailyReport cashLoanDailyReport) {
//		return super.get(cashLoanDailyReport);
//	}
	
	public List<SCashLoanDailyReport> findList(SCashLoanDailyReport sCashLoanDailyReport) {
		return super.findList(sCashLoanDailyReport);
	}
	
	public Page<SCashLoanDailyReport> findPage(Page<SCashLoanDailyReport> page, SCashLoanDailyReport sCashLoanDailyReport) {
		return super.findPage(page, sCashLoanDailyReport);
	}
	
	@Transactional(readOnly = false)
	public void save(SCashLoanDailyReport sCashLoanDailyReport) {
		super.save(sCashLoanDailyReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(SCashLoanDailyReport sCashLoanDailyReport) {
		super.delete(sCashLoanDailyReport);
	}
	
	/**
	 * 周报
	 * @param week
	 * @return
	 */
	public List<SCashLoanDailyReport> findweekList(SCashLoanDailyReport report) {
		return sCashLoanDailyReportDao.findweekList(report);
	}
	/**
	 * 周报导出
	 * @return
	 */
	public List<SCashLoanWeekReportBean> weekReportExportFile(SCashLoanWeekReportBean report){
		return sCashLoanDailyReportDao.weekReportExportFile(report);
	}
	
	/**
	 * 月报
	 * @param week
	 * @return
	 */
	public List<SCashLoanDailyReport> findmonthList(SCashLoanDailyReport report) {
		return sCashLoanDailyReportDao.findmonthList(report);
	}
	
	/**
	 * 月报导出
	 * @return
	 */
	public List<SCashLoanMonthReportBean> monthReportExportFile(SCashLoanMonthReportBean report){
		return sCashLoanDailyReportDao.monthReportExportFile(report);
	}
	
	/**
	 * 周报详情
	 * @param week
	 * @return
	 */
	public SCashLoanDailyReportOrderWeekBean findWeekDetails(String week){
		return sCashLoanDailyReportDao.findWeekDetails(week);
	}
	
	/**
	 * 月报详情
	 * @param Month
	 * @return
	 */
	public SCashLoanDailyReportOrderMonthBean findMonthDetails(String month){
		return sCashLoanDailyReportDao.findMonthDetails(month);
	}
	
	
	/**
	 * 周还款成本
	 * @return
	 */
	public List<SCashLoanDailyReportRepayWeekBean> findWeekRepayDetails(String week){
		return sCashLoanDailyReportDao.findWeekRepayDetails(week);
	}
	
	/**
	 * 月还款成本
	 * @return
	 */
	public List<SCashLoanDailyReportRepayMonthBean> findMonthRepayDetails(String month){
		return sCashLoanDailyReportDao.findMonthRepayDetails(month);
	}
	
	
}