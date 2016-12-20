/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VUserConversionReportMonth;

/**
 * 用户月报表DAO接口
 * @author 徐盛
 * @version 2016-07-21
 */
@MyBatisDao
public interface VUserConversionReportMonthDao extends CrudDao<VUserConversionReportMonth> {
	
}