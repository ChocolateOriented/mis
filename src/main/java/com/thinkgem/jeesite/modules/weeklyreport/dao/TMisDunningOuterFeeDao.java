/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportOrderWeekBean;
import com.thinkgem.jeesite.modules.weeklyreport.entity.TMisDunningOuterFee;

/**
 * 基础佣金费率表DAO接口
 * @author 徐盛
 * @version 2016-11-08
 */
@MyBatisDao
public interface TMisDunningOuterFeeDao extends CrudDao<TMisDunningOuterFee> {
	
	/**
	 * 查询基础佣金费率的last的信息
	 * @return
	 */
	public TMisDunningOuterFee findListOneByNewFee(TMisDunningOuterFee outerFee);
	
}