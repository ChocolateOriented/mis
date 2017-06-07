/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import com.mo9.risk.modules.dunning.entity.PerformanceDayReport;
import com.mo9.risk.modules.dunning.entity.SMisDunningProductivePowerDailyReport;
import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 催收员案件活动日报DAO接口
 * @author 李敬翔
 * @version 2017-05-09
 */
@MyBatisDao
public interface DunningReportDao extends BaseDao {
	/**
	 * @Description: 案件活动日报
	 * @param productivePowerDailyReport
	 * @return
	 * @return: List<SMisDunningProductivePowerDailyReport>
	 */
	public List<SMisDunningProductivePowerDailyReport> findProductivePowerDailyReport(SMisDunningProductivePowerDailyReport productivePowerDailyReport);
	
	/**
	 * @Description: 工作日报
	 * @param performanceDayReport
	 * @return
	 * @return: List<PerformanceDayReport>
	 */
	public List<PerformanceDayReport> findPerformanceDayReport(PerformanceDayReport performanceDayReport);
}