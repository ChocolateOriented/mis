/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.DunningReportDao;
import com.mo9.risk.modules.dunning.entity.PerformanceDayReport;
import com.mo9.risk.modules.dunning.entity.SMisDunningProductivePowerDailyReport;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 催收员案件活动日报Service
 * @author 李敬翔
 * @version 2017-05-09
 */
@Service
@Transactional(readOnly = true)
public class DunningReportService extends BaseService {
	@Autowired
	private DunningReportDao dunningReportDao ;
	@Autowired
	private TMisDunningGroupService groupService ;
	
	/**
	 * 催收员案件活动日报
	 * @param entity
	 * @return
	 */
	public List<SMisDunningProductivePowerDailyReport> findProductivePowerDailyReport(SMisDunningProductivePowerDailyReport entity){
		return dunningReportDao.findProductivePowerDailyReport(entity) ;
	}

	/**
	 * 催收员案件活动日报-分页
	 * @return
	 */
	public Page<SMisDunningProductivePowerDailyReport> findProductivePowerDailyReport(Page<SMisDunningProductivePowerDailyReport> page, SMisDunningProductivePowerDailyReport entity) {
		entity.setPage(page);
		page.setList(dunningReportDao.findProductivePowerDailyReport(entity));
		return page;
	}
	
	/**
	 * @Description: 添加默认查询条件
	 * @param entity
	 * @return: void
	 */
	public void setQueryConditions(SMisDunningProductivePowerDailyReport entity) {
		//查询条件默认当天
		if (null == entity.getBeginCreateTime()) {
			entity.setBeginCreateTime( DateUtils.getDateToDay(new Date()));
		}
		if (null == entity.getEndCreateTime()) {
			entity.setEndCreateTime( DateUtils.getDateToDay(new Date()));
		}
		
		//催收主管权限或委外主管权限视为组长,查询管理的小组
		int permissions = TMisDunningTaskService.getPermissions();
		if(TMisDunningTaskService.DUNNING_INNER_PERMISSIONS.equals(permissions) || TMisDunningTaskService.DUNNING_OUTER_PERMISSIONS.equals(permissions)){
			TMisDunningGroup queryGroup = new TMisDunningGroup();
			queryGroup.setLeader(UserUtils.getUser());
			List<TMisDunningGroup> groups = groupService.findList(queryGroup);
			entity.setQueryGroups(groups);
		}
	}
	
	/**
	 * 催收日报表
	 * @param performanceDayReport
	 * @param allowedGroups
	 * @return
	 */
	public List<PerformanceDayReport> findPerformanceDayReport(PerformanceDayReport performanceDayReport, List<TMisDunningGroup> allowedGroups){
		//添加限制组ID
		List<String> allowedGroupIds = new ArrayList<>(allowedGroups.size());
		if (performanceDayReport.getGroup() == null){
			TMisDunningGroup group = new TMisDunningGroup();
			performanceDayReport.setGroup(group);
		}
		for (TMisDunningGroup allowedGroup: allowedGroups) {
			allowedGroupIds.add(allowedGroup.getId());
		}
		performanceDayReport.getGroup().setGroupIds(allowedGroupIds);

		return dunningReportDao.findPerformanceDayReport(performanceDayReport);
	}

	/**
	 * @Description  催收日报表-分页
	 * @param page
	 * @param performanceDayReport
	 * @return com.thinkgem.jeesite.common.persistence.Page<com.mo9.risk.modules.dunning.entity.PerformanceDayReport>
	 */
	public Page<PerformanceDayReport> findPerformanceDayReport(Page<PerformanceDayReport> page, PerformanceDayReport performanceDayReport, List<TMisDunningGroup> allowedGroups) {
		performanceDayReport.setPage(page);
		page.setUsePaginationInterceptor(false);

		//添加限制组ID
		List<String> allowedGroupIds = new ArrayList<>(allowedGroups.size());
		if (performanceDayReport.getGroup() == null){
			TMisDunningGroup group = new TMisDunningGroup();
			performanceDayReport.setGroup(group);
		}
		for (TMisDunningGroup allowedGroup: allowedGroups) {
			allowedGroupIds.add(allowedGroup.getId());
		}

		performanceDayReport.getGroup().setGroupIds(allowedGroupIds);
		page.setCount(dunningReportDao.findPerformanceDayReportCount(performanceDayReport));
		page.setList(dunningReportDao.findPerformanceDayReport(performanceDayReport));
		return page;
	}
	
}