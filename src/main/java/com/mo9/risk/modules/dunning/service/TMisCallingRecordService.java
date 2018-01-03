/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mo9.risk.modules.dunning.entity.DunningPhoneReportFile;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.bean.CallCenterAgentStatus;
import com.mo9.risk.modules.dunning.bean.CallCenterCallinInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterCalloutInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterPageResponse;
import com.mo9.risk.modules.dunning.bean.CallCenterQueryCallInfo;
import com.mo9.risk.modules.dunning.dao.TMisCallingRecordDao;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;
import com.mo9.risk.modules.dunning.entity.TMisCallingRecord.CallType;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
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
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.MINUTE, -1);
		Date end = c.getTime();
		c.add(Calendar.MINUTE, -5);
		c.add(Calendar.SECOND, -1);
		Date start = c.getTime();
		
		CallCenterQueryCallInfo action = new CallCenterQueryCallInfo();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String starttime = dateFormat.format(start);
		String endtime = dateFormat.format(end);
		action.setStarttime(starttime);
		action.setEndtime(endtime);
		
		syncAgentCallInfo(action);
		logger.info("定时同步电话通话信记录结束");
	}
	
	/**
	 * 每小时同步CTI通话记录
	 * @return
	 */
	@Scheduled(cron = "0 0 0/1 * * ?")
	@Transactional(readOnly = false)
	public void syncCallRecordHourly() {
		logger.info("每小时同步电话通话信记录开始");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.add(Calendar.MINUTE, -30);
		Date end = c.getTime();
		c.add(Calendar.HOUR_OF_DAY, -1);
		c.add(Calendar.SECOND, -1);
		Date start = c.getTime();

		CallCenterQueryCallInfo action = new CallCenterQueryCallInfo();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String starttime = dateFormat.format(start);
		String endtime = dateFormat.format(end);
		action.setStarttime(starttime);
		action.setEndtime(endtime);

		syncAgentCallInfo(action);
		logger.info("每小时同步电话通话信记录结束");
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
		c.add(Calendar.SECOND, -1);
		Date start = c.getTime();
		
		CallCenterQueryCallInfo action = new CallCenterQueryCallInfo();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String starttime = dateFormat.format(start);
		String endtime = dateFormat.format(end);
		action.setStarttime(starttime);
		action.setEndtime(endtime);
		
		syncAgentCallInfo(action);
		logger.info("每日同步电话通话信记录结束");
	}
	
	/**
	 * 手动同步CTI通话记录
	 * @param date
	 * @return
	 */
	@Transactional(readOnly = false)
	public void syncCallRecordManual(Date date) {
		logger.info("手动同步CTI通话记录,同步时间" + date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DATE, 1);
		Date end = c.getTime();
		c.add(Calendar.DATE, -1);
		c.add(Calendar.SECOND, -1);
		Date start = c.getTime();
		
		CallCenterQueryCallInfo action = new CallCenterQueryCallInfo();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String starttime = dateFormat.format(start);
		String endtime = dateFormat.format(end);
		action.setStarttime(starttime);
		action.setEndtime(endtime);
		
		syncAgentCallInfo(action);
		logger.info("手动同步CTI通话记录结束");
	}
	
	/**
	 * 同步坐席呼入呼出信息
	 * @param action
	 * @return
	 */
	@Transactional(readOnly = false)
	public void syncAgentCallInfo(CallCenterQueryCallInfo action) {
		List<TMisAgentInfo> agents = tMisAgentInfoService.findAllList();
		if (agents == null || agents.isEmpty()) {
			logger.info("没有需要同步通话记录的坐席");
			return;
		}
		
		for (TMisAgentInfo agent : agents) {
			if (StringUtils.isEmpty(agent.getPeopleId())) {
				continue;
			}
			logger.info("同步坐席" + agent.getAgent());
			action.setQueue(null);
			action.setAgent(agent.getAgent());
			syncCalloutInfo(action);
			action.setAgent(null);
			action.setQueue(agent.getQueue());
			syncCallinInfo(action);
		}
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
			if (result == null) {
				logger.info("同步呼出通话信息失败");
				return;
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
			if (result == null) {
				logger.info("同步呼入通话信息失败");
				return;
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
	 * @param calloutInfos
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
			String target = record.getTargetNumber();
			target = TMisDunningPhoneService.filterCtiCallInfoNumber(target);
			record.setTargetNumber(target);
			record.setPeopleId(agentInfo.getPeopleId());
			record.setAgentState(getAgentStateOnMoment(record));
			save(record);
		}
	}
	
	/**
	 * 保存呼入信息数据
	 * @param callinInfos
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
			String target = record.getTargetNumber();
			target = TMisDunningPhoneService.filterCtiCallInfoNumber(target);
			record.setTargetNumber(target);
			record.setAgentState(getAgentStateOnMoment(record));
			save(record);
		}
	}

	/**
	 * 获取呼入/呼出的坐席状态
	 */
	private String getAgentStateOnMoment(TMisCallingRecord record){
		TMisAgentInfo agentInfo = tMisAgentInfoService.getAgentStateOnMoment(record);
		if (agentInfo == null) {
			return CallCenterAgentStatus.LOGGED_OUT;
		}
		return agentInfo.getStatus();
	}
	
	/**
	 * 查询手机号归属地
	 * @param mobile
	 * @return
	 */
	public String queryMobileLocation(String mobile) {
		if (mobile == null || mobile.length() != 11) {
			return "";
		}
		return dao.queryMobileLocation(mobile.substring(0, 7));
	}

	/**
	 * 查询软电话日常报表
	 */

	public Page<DunningPhoneReportFile> exportStatementFile(Page<DunningPhoneReportFile> page, DunningPhoneReportFile entity) {
		entity.setPage(page);
		page.setUsePaginationInterceptor(false);

		List<DunningPhoneReportFile> list = exportSoftPhoneReportFile(entity);
		filterPhoneReportByDepartment(list,entity,page);
		page.setList(exportSoftPhoneReportFile(entity));
		page.setCount(dao.countExportStatementFile(entity));
		return page;
	}

	/**
	 * 查询软电话日常报表
	 */

	public List<DunningPhoneReportFile> exportSoftPhoneReportFile(DunningPhoneReportFile entity) {
		List<DunningPhoneReportFile> reportFiles = dao.exportStatementFile(entity);
		for (DunningPhoneReportFile reportFile : reportFiles){
			if (reportFile.getDateTime() == null || "".equals(reportFile.getDateTime())) {
				reportFile.setDateTime(DateUtils.formatDate(entity.getDatetimestart(), "yyyy-MM-dd HH:mm") + "至" + DateUtils.formatDate(entity.getDatetimeend(), "yyyy-MM-dd HH:mm"));
			}
			if (!StringUtils.isEmpty(reportFile.getLogiName())) {
				User user = UserUtils.getByLoginName(reportFile.getLogiName());
				if (!(user.getCompany().getName() == null && "".equals(user.getCompany().getName())
						&& user.getOffice().getName() == null && "".equals(user.getOffice().getName()))) {
					reportFile.setDepartment(user.getCompany().getName() + user.getOffice().getName());
				}
			}
		}
		filterPhoneReportByDepartment(reportFiles,entity,null);
		return reportFiles;
	}


	/**
	 * 查询软电话日常报表（日常）
	 */
	public List<DunningPhoneReportFile> exportSoftPhoneReportFileForEveryDay(DunningPhoneReportFile entity){
		List<DunningPhoneReportFile> reportFiles = dao.exportStatementFileForEveryDay(entity);
		filterPhoneReportByDepartment(reportFiles,entity,null);
		return countDunningPhoneReport(reportFiles, entity);
	}

	/**
	 * 查询软电话日常报表（日常）
	 */
	public Page<DunningPhoneReportFile> exportStatementFileForEveryDay(Page<DunningPhoneReportFile> page,DunningPhoneReportFile entity){
		entity.setPage(page);
		page.setUsePaginationInterceptor(false);
		List<DunningPhoneReportFile> list = exportSoftPhoneReportFileForEveryDay(entity);
		filterPhoneReportByDepartment(list,entity,page);
		page.setList(list);
		page.setCount(dao.countExportStatementFileForEveryDay(entity));
		return page;
	}

	/**
	 * 两个str类型的数字相除返回str
	 * @param front 除号前
	 * @param back 除号后
	 * @return
	 */
	private String strDivideStr(String front , String back){
		if (StringUtils.isEmpty(front) || StringUtils.isEmpty(back) || "0".equals(front) ||  "0".equals(back)){
			return "0.00";
		}
		Double double1 = Double.valueOf(front);
		Double double2 = Double.valueOf(back);
		Double rst = double1/double2;
		DecimalFormat df = new DecimalFormat("0.00");
		return  df.format(rst);
	}

	/**
	 * str秒转化为str时
	 * @param second
	 * @return
	 */
	private String strsecond2Strhour(String second){
		if ("".equals(second) || second == null){
			return "0";
		}
		DecimalFormat df   = new DecimalFormat("####0.0000000000");
		Double doubleSecond = Double.valueOf(second);
		Double doubleHour = doubleSecond/3600.00;
		return  df.format(doubleHour).toString();
	}

	/**
	 * 计算每小时电话报表部分数据
	 */
	private List<DunningPhoneReportFile> countDunningPhoneReport(List<DunningPhoneReportFile> reportFiles, DunningPhoneReportFile entity) {
		for (DunningPhoneReportFile countReport : reportFiles) {
			countReport.setConnectRate(strDivideStr(countReport.getConnectAmout(), countReport.getCallingAmount()));
			countReport.setCallingAmountOnHour(strDivideStr(countReport.getCallingAmount(), strsecond2Strhour(countReport.getOntime())));
			countReport.setConnectAmountOnHour(strDivideStr(countReport.getConnectAmout(), strsecond2Strhour(countReport.getOntime())));
			countReport.setCallDurationOnHour(strDivideStr(countReport.getCallDuration(), strsecond2Strhour(countReport.getOntime())));
			countReport.setDealCaseAmountOnHour(strDivideStr(countReport.getDealCaseAmount(), strsecond2Strhour(countReport.getOntime())));
			if (countReport.getDateTime() == null || "".equals(countReport.getDateTime())) {
				countReport.setDateTime(DateUtils.formatDate(entity.getDatetimestart(), "yyyy-MM-dd HH:mm") + "至" + DateUtils.formatDate(entity.getDatetimeend(), "yyyy-MM-dd HH:mm"));
			}
			if (!StringUtils.isEmpty(countReport.getLogiName())) {
				User user = UserUtils.getByLoginName(countReport.getLogiName());
				if (!(user.getCompany().getName() == null && "".equals(user.getCompany().getName())
						&& user.getOffice().getName() == null && "".equals(user.getOffice().getName()))) {
					countReport.setDepartment(user.getCompany().getName() + user.getOffice().getName());
				}
			}
		}
		return reportFiles;
	}

	/**
	 * 过滤查询出来的数据，通过机构名称
	 * @param reportFiles
	 * @param entity
	 * @param page
	 */
	private void filterPhoneReportByDepartment(List<DunningPhoneReportFile> reportFiles, DunningPhoneReportFile entity,Page<DunningPhoneReportFile> page){
		if (!(entity.getDepartment() == null || "".equals(entity.getDepartment()))){
			if (!(entity.getDepartment() == null || "".equals(entity.getDepartment().trim()))){
				Iterator<DunningPhoneReportFile> it = reportFiles.iterator();
				while(it.hasNext()){
					DunningPhoneReportFile file = it.next();
					if(!(file.getDepartment() == null || "".equals(file.getDepartment()))) {
						if (!file.getDepartment().equals(entity.getDepartment())) {
							it.remove();
							if (page != null) {
								page.setCount(page.getCount() - 1);
							}
						}
					}
				}
			}
		}
	}

}