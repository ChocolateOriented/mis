/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.SMisDunningProductivePowerDailyReportDao;
import com.mo9.risk.modules.dunning.entity.SMisDunningProductivePowerDailyReport;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 催收员案件活动日报Service
 * @author 李敬翔
 * @version 2017-05-09
 */
@Service
@Transactional(readOnly = true)
public class SMisDunningProductivePowerDailyReportService extends CrudService<SMisDunningProductivePowerDailyReportDao, SMisDunningProductivePowerDailyReport> {
	@Autowired
	private TMisDunningGroupService groupService ;
	
	/**
	 * 催收员案件活动日报
	 * @param performanceMonthReport
	 * @return
	 */
	@Override
	public List<SMisDunningProductivePowerDailyReport> findList(SMisDunningProductivePowerDailyReport entity){
		this.setQueryConditions(entity);
		return dao.findList(entity) ;
	}

	/**
	 * 催收员案件活动日报-分页
	 * @param performanceMonthReport
	 * @return
	 */
	@Override
	public Page<SMisDunningProductivePowerDailyReport> findPage(Page<SMisDunningProductivePowerDailyReport> page, SMisDunningProductivePowerDailyReport entity) {
		this.setQueryConditions(entity);
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}
	
	/**
	 * @Description: 添加默认查询条件
	 * @param entity
	 * @return: void
	 */
	private void setQueryConditions(SMisDunningProductivePowerDailyReport entity) {
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
}