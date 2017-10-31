/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.mo9.risk.modules.dunning.bean.CallCenterCallinInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterCalloutInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterPageResponse;
import com.mo9.risk.modules.dunning.bean.CallCenterQueryCallInfo;
import com.mo9.risk.modules.dunning.dao.TMisCallingRecordDao;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisCallingRecord.CallType;
import com.mo9.risk.modules.dunning.manager.CallCenterManager;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

@Service
@Lazy(false)
@Transactional(readOnly = true)
public class TMisCallingRecordService extends CrudService<TMisCallingRecordDao, TMisCallingRecord> {
	
	@Autowired
	private CallCenterManager callCenterManager;
	
	@Autowired
	private TMisAgentInfoService tMisAgentInfoService;
	
	@Autowired
	private TMisDunningGroupService tMisDunningGroupService;

	@Override
	public Page<TMisCallingRecord> findPage(Page<TMisCallingRecord> page, TMisCallingRecord entity) {
		int permissions = TMisDunningTaskService.getPermissions();
		List<String> allowedGroupIds = new ArrayList<String>();
		if(TMisDunningTaskService.DUNNING_ALL_PERMISSIONS == permissions){
			entity.setPeopleId(null);
		}
		if (TMisDunningTaskService.DUNNING_INNER_PERMISSIONS == permissions) {
			entity.setPeopleId(null);
			allowedGroupIds.addAll(tMisDunningGroupService.findIdsByLeader(UserUtils.getUser()));
			entity.setGroupIds(allowedGroupIds);
		}
		if(TMisDunningTaskService.DUNNING_OUTER_PERMISSIONS == permissions){
			entity.setPeopleId(null);
		}
		if(TMisDunningTaskService.DUNNING_COMMISSIONER_PERMISSIONS == permissions){
			entity.setPeopleId(UserUtils.getUser().getId());
		}
		if(TMisDunningTaskService.DUNNING_FINANCIAL_PERMISSIONS == permissions){
			entity.setPeopleId(null);
		}
		if (TMisDunningTaskService.DUNNING_SUPERVISOR == permissions) {
			TMisDunningGroup group = new TMisDunningGroup();
			group.setSupervisor(UserUtils.getUser());
			List<String> groupIds = tMisDunningGroupService.findSupervisorGroupList(group);
			allowedGroupIds.addAll(groupIds);
			entity.setGroupIds(allowedGroupIds);
		}
		entity.setPage(page);
		page.setUsePaginationInterceptor(false);
		page.setCount(dao.listCount(entity));
		List<TMisCallingRecord> records = dao.findList(entity);
		page.setList(records);
		return page;
	}
	
	/**
	 * 同步CTI通话记录
	 * @return
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	@Transactional(readOnly = false)
	public void syncCallRecord() {
		logger.info("定时同步电话通话信记录开始");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -1);
		Date end = c.getTime();
		c.add(Calendar.MINUTE, -5);
		Date start = c.getTime();
		
		CallCenterQueryCallInfo action = new CallCenterQueryCallInfo();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String starttime = dateFormat.format(start);
		String endtime = dateFormat.format(end);
		action.setStarttime(starttime);
		action.setEndtime(endtime);
		
		syncCalloutInfo(action);
		syncCallinInfo(action);
		logger.info("定时同步电话通话信记录结束");
	}
	
	/**
	 * 每日同步CTI通话记录
	 * @return
	 */
	@Scheduled(cron = "0 0 3 * * ?")
	@Transactional(readOnly = false)
	public void syncCallRecordDaily() {
		logger.info("每日同步电话通话信记录开始");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date end = c.getTime();
		c.add(Calendar.DATE, -1);
		Date start = c.getTime();
		
		CallCenterQueryCallInfo action = new CallCenterQueryCallInfo();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String starttime = dateFormat.format(start);
		String endtime = dateFormat.format(end);
		action.setStarttime(starttime);
		action.setEndtime(endtime);
		
		syncCalloutInfo(action);
		syncCallinInfo(action);
		logger.info("每日同步电话通话信记录开始");
	}
	
	/**
	 * 同步呼出信息
	 * @param action
	 * @return
	 */
	@Transactional(readOnly = false)
	public void syncCalloutInfo(CallCenterQueryCallInfo action) {
		action.setPage(null);
		try {
			CallCenterPageResponse<CallCenterCalloutInfo> result = callCenterManager.calloutInfo(action);
			if (result == null || !"0".equals(result.getErrorCode())) {
				throw new ServiceException(result.getErrorMsg());
			}
			
			CallCenterPageResponse.CallCenterPageData<CallCenterCalloutInfo> page = result.getData();
			if (page == null || page.isEmpty()) {
				return;
			}
			
			saveCalloutResults(page.getResults());
			
			int total = page.getTotal();
			int totalPage = total / 10;
			if (total % 10 > 0) {
				totalPage++;
			}
			logger.info("同步呼出通话信息页数" + totalPage);
			
			for (int i = 2; i <= totalPage; i++) {
				action.setPage(String.valueOf(i));
				CallCenterPageResponse<CallCenterCalloutInfo> next = callCenterManager.calloutInfo(action);
				if (result == null || !"0".equals(result.getErrorCode())) {
					throw new ServiceException(result.getErrorMsg());
				}
				
				CallCenterPageResponse.CallCenterPageData<CallCenterCalloutInfo> nextPage = next.getData();
				if (nextPage == null || nextPage.isEmpty()) {
					return;
				}
				
				saveCalloutResults(nextPage.getResults());
			}
		} catch (Exception e) {
			logger.info("同步呼出通话信息失败,失败信息:" + e);
		}
	}
	
	/**
	 * 同步呼入信息
	 * @param action
	 * @return
	 */
	@Transactional(readOnly = false)
	public void syncCallinInfo(CallCenterQueryCallInfo action) {
		action.setPage(null);
		try {
			CallCenterPageResponse<CallCenterCallinInfo> result = callCenterManager.callinInfo(action);
			if (result == null || !"0".equals(result.getErrorCode())) {
				throw new ServiceException(result.getErrorMsg());
			}
			
			CallCenterPageResponse.CallCenterPageData<CallCenterCallinInfo> page = result.getData();
			if (page == null || page.isEmpty()) {
				return;
			}
			
			saveCallinResults(page.getResults());
			
			int total = page.getTotal();
			int totalPage = total / 10;
			if (total % 10 > 0) {
				totalPage++;
			}
			logger.info("同步呼入通话信息页数" + totalPage);
			
			for (int i = 2; i <= totalPage; i++) {
				action.setPage(String.valueOf(i));
				CallCenterPageResponse<CallCenterCallinInfo> next = callCenterManager.callinInfo(action);
				if (result == null || !"0".equals(result.getErrorCode())) {
					throw new ServiceException(result.getErrorMsg());
				}
				
				CallCenterPageResponse.CallCenterPageData<CallCenterCallinInfo> nextPage = next.getData();
				if (nextPage == null || nextPage.isEmpty()) {
					return;
				}
				
				saveCallinResults(nextPage.getResults());
			}
		} catch (Exception e) {
			logger.info("同步呼入通话信息失败,失败信息:" + e);
		}
	}
	
	/**
	 * 保存呼出信息数据
	 * @param action
	 * @return
	 */
	private void saveCalloutResults(List<CallCenterCalloutInfo> calloutInfos) {
		for (CallCenterCalloutInfo callInfo : calloutInfos) {
			TMisCallingRecord entity = new TMisCallingRecord();
			entity.setCallType(CallType.out);
			entity.setUuid(callInfo.getEuuid());
			TMisCallingRecord current = dao.getRecordByInOutUuid(entity);
			if (current != null) {
				continue;
			}
			
			TMisAgentInfo agentInfo = tMisAgentInfoService.getInfoByExtension(callInfo.getExtension());
			
			if (agentInfo == null) {
				continue;
			}
			
			TMisCallingRecord record = new TMisCallingRecord(callInfo);
			record.setPeopleId(agentInfo.getPeopleId());
			save(record);
		}
	}
	
	/**
	 * 保存呼入信息数据
	 * @param action
	 * @return
	 */
	private void saveCallinResults(List<CallCenterCallinInfo> callinInfos) {
		for (CallCenterCallinInfo callInfo : callinInfos) {
			TMisCallingRecord entity = new TMisCallingRecord();
			entity.setCallType(CallType.in);
			entity.setUuid(callInfo.getSessionid());
			TMisCallingRecord current = dao.getRecordByInOutUuid(entity);
			if (current != null) {
				continue;
			}
			TMisAgentInfo agentInfo = tMisAgentInfoService.getInfoByQueue(callInfo.getQueue());
			
			if (agentInfo == null) {
				continue;
			}
			
			TMisCallingRecord record = new TMisCallingRecord(callInfo);
			record.setPeopleId(agentInfo.getPeopleId());
			if (StringUtils.isEmpty(record.getAgent())) {
				record.setAgent(agentInfo.getAgent());
			}
			if (StringUtils.isEmpty(record.getExtensionNumber())) {
				record.setExtensionNumber(agentInfo.getExtension());
			}
			save(record);
		}
	}

}