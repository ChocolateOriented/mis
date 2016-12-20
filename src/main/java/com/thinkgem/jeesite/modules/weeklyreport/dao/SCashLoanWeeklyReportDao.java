/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SCashLoanWeeklyReport;

/**
 * 现金贷款周报DAO接口
 * @author 徐盛
 * @version 2016-06-06
 */
@MyBatisDao
public interface SCashLoanWeeklyReportDao extends CrudDao<SCashLoanWeeklyReport> {
	
	/**
	 * 累计订单数
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findOrdernumincludedelay(@Param("week")Integer week);
	
	/**
	 * 新增订单数
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findOrdernumUser(@Param("week")Integer week);
	
	/**
	 * 订单來源分布
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findOrdernumneworderperiodpercent(@Param("week")Integer week);
	
	/**
	 * 订单金額分布
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findOrdernumneworderperiodamountpercent(@Param("week")Integer week);
	
	/**
	 * 总收益 
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findAmountallincome(@Param("week")Integer week);
	
	/**
	 * 累计放款用户数
	 * @param week
	 * @return
	 */
	public List<SCashLoanWeeklyReport> findSingleusernum(@Param("week")Integer week);
	
	
}