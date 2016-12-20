/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportOrderMonthBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportOrderWeekBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportRepayMonthBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportRepayWeekBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanMonthReportBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanWeekReportBean;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SCashLoanDailyReport;

/**
 * 财务日报DAO接口
 * @author 徐盛
 * @version 2016-06-23
 */
@MyBatisDao
public interface SCashLoanDailyReportDao extends CrudDao<SCashLoanDailyReport> {
	
	/**
	 * 周报
	 * @return
	 */
	public List<SCashLoanDailyReport> findweekList(SCashLoanDailyReport report);
	/**
	 * 周报导出
	 * @return
	 */
	public List<SCashLoanWeekReportBean> weekReportExportFile(SCashLoanWeekReportBean report);
	
	/**
	 * 月报
	 * @return
	 */
	public List<SCashLoanDailyReport> findmonthList(SCashLoanDailyReport report);
	/**
	 * 月报导出
	 * @return
	 */
	public List<SCashLoanMonthReportBean> monthReportExportFile(SCashLoanMonthReportBean report);
	
	
	/**
	 * 周报详情
	 * @return
	 */
	public SCashLoanDailyReportOrderWeekBean findWeekDetails(String week);
	
	/**
	 * 月报详情
	 * @return
	 */
	public SCashLoanDailyReportOrderMonthBean findMonthDetails(String month);
	
	/**
	 * 周还款成本
	 * @return
	 */
	public List<SCashLoanDailyReportRepayWeekBean> findWeekRepayDetails(String week);
	
	/**
	 * 月还款成本
	 * @return
	 */
	public List<SCashLoanDailyReportRepayMonthBean> findMonthRepayDetails(String month);
	
}