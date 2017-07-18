/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.entity.SMisDunningTaskMonthReport;

/**
 * 催收月绩效DAO接口
 * @author 徐盛
 * @version 2016-11-30
 */
@MyBatisDao
public interface SMisDunningTaskMonthReportDao extends CrudDao<SMisDunningTaskMonthReport> {
	
	//迁徙率关于户数
	//更新已经采集的逾期订单
	public void householdsUpdateHaveBeenCollectDealcode();
	//采集今天逾期一天的数据
	public void householdsInsertOverOneDay(@Param("cycle")int cycle,@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	//采集新的迁徙率的统计数据
	public void householdsInsertStatisticalData(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	//更新PayoffQ1	
	public void householdsUpdatePayoffQ1();
	//更新PayoffQ2
	public void householdsUpdatePayoffQ2();
	//更新PayoffQ3
	public void householdsUpdatePayoffQ3();
	//更新PayoffQ4
	public void householdsUpdatePayoffQ4();
	//更新Q2
	public void householdsUpdateQ2();
	//更新Q3
	public void householdsUpdateQ3();
	//更新Q4
	public void householdsUpdateQ4();
	//更新逾期1天的当天到期的订单数
	public void householdsUpdateOverOneDay();
	
	//迁徙率关于本金
	//采集新的迁徙率的统计数据
	public void principalInsertStatisticalData(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	//更新PayoffQ1	
	public void principalUpdatePayoffQ1();
	//更新PayoffQ2
	public void principalUpdatePayoffQ2();
	//更新PayoffQ3
	public void principalUpdatePayoffQ3();
	//更新PayoffQ4
	public void principalUpdatePayoffQ4();
	//更新Q2
	public void principalUpdateQ2();
	//更新Q3
	public void principalUpdateQ3();
	//更新Q4
	public void principalUpdateQ4();
	//更新逾期1天的当天到期的订单数
	public void principalUpdateOverOneDay();
}