/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.weeklyreport.dao.SCashLoanWeeklyReportDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SCashLoanWeeklyReport;

/**
 * 现金贷款周报Service
 * @author 徐盛
 * @version 2016-06-06
 */
@Service
@Transactional(readOnly = true)
public class SCashLoanWeeklyReportService extends CrudService<SCashLoanWeeklyReportDao, SCashLoanWeeklyReport> {

	@Autowired
	private SCashLoanWeeklyReportDao sCashLoanWeeklyReportDao;
	
	public SCashLoanWeeklyReport get(String id) {
		return super.get(id);
	}
	
	public List<SCashLoanWeeklyReport> findList(SCashLoanWeeklyReport sCashLoanWeeklyReport) {
		return super.findList(sCashLoanWeeklyReport);
	}
	
	public Page<SCashLoanWeeklyReport> findPage(Page<SCashLoanWeeklyReport> page, SCashLoanWeeklyReport sCashLoanWeeklyReport) {
		return super.findPage(page, sCashLoanWeeklyReport);
	}
	
	@Transactional(readOnly = false)
	public void save(SCashLoanWeeklyReport sCashLoanWeeklyReport) {
		super.save(sCashLoanWeeklyReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(SCashLoanWeeklyReport sCashLoanWeeklyReport) {
		super.delete(sCashLoanWeeklyReport);
	}
	
	/**
	 * 累计订单数
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findOrdernumincludedelay(Integer week) {
		return sCashLoanWeeklyReportDao.findOrdernumincludedelay(week);
	}
	
	/**
	 * 新增订单数
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findOrdernumUser(Integer week) {
		return sCashLoanWeeklyReportDao.findOrdernumUser(week);
	}
	
	/**
	 * 订单來源分布
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findOrdernumneworderperiodpercent(@Param("week")Integer week){
		return sCashLoanWeeklyReportDao.findOrdernumneworderperiodpercent(week); 
	}
	
	/**
	 * 订单金額分布
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findOrdernumneworderperiodamountpercent(@Param("week")Integer week){
		return sCashLoanWeeklyReportDao.findOrdernumneworderperiodamountpercent(week);
	}
	
	/**
	 * 总收益 
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findAmountallincome(@Param("week")Integer week){
		return sCashLoanWeeklyReportDao.findAmountallincome(week);
	}
	
	/**
	 * 累计放款用户数
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findSingleusernum(@Param("week")Integer week){
		return sCashLoanWeeklyReportDao.findSingleusernum(week);
	}
	
	
}