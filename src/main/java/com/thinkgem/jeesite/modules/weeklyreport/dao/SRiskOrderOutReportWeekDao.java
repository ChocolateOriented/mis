/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportWeek;

/**
 * 委外周报表DAO接口
 * @author 徐盛
 * @version 2016-12-09
 */
@MyBatisDao
public interface SRiskOrderOutReportWeekDao extends CrudDao<SRiskOrderOutReportWeek> {
	
}