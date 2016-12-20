/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportMonth;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportMonthDetail;

/**
 * 委外月报表DAO接口
 * @author 徐盛
 * @version 2016-12-09
 */
@MyBatisDao
public interface SRiskOrderOutReportMonthDao extends CrudDao<SRiskOrderOutReportMonth> {
	
	/**
	 * 点击详情
	 * @param orderOutReportMonth
	 * @return
	 */
	public List<SRiskOrderOutReportMonthDetail> findOutReportMonthDetail(SRiskOrderOutReportMonth orderOutReportMonth);
	 
	
}