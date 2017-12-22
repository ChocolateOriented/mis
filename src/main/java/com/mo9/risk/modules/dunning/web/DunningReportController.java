/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.util.StringUtils;
import com.mo9.risk.modules.dunning.entity.*;
import com.mo9.risk.modules.dunning.service.*;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 报表Controller
 * @author 李敬翔
 * @version 2017-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/report")
public class DunningReportController extends BaseController {

	@Autowired
	private DunningReportService reportService;
	@Autowired
	private TMisDunningGroupService groupService;
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	@Autowired
	private TMisCallingRecordService tMisCallingRecordService;
	
	/**
	 * @Description: 催收小组集合
	 */
	@ModelAttribute
	public void groupType(Model model) {
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
	}
	
	/**
	 * @Description 催收员案件活动日报
	 * @param smMisDunningProductivePowerDailyReport
	 * @param request
	 * @param response
	 * @param model
	 * @return java.lang.String
	 */
	@RequiresPermissions("dunning:sMisDunningProductivePowerDailyReport:view")
	@RequestMapping(value = {"productivePowerDailyReport"})
	public String findDunningProductivePowerDailyReport(SMisDunningProductivePowerDailyReport smMisDunningProductivePowerDailyReport, HttpServletRequest request, HttpServletResponse response, Model model) {
/*		//添加默认查询条件
		reportService.setQueryConditions(smMisDunningProductivePowerDailyReport);
        //催收小组列表,若无限制则查询所有小组
        List<TMisDunningGroup> groups = smMisDunningProductivePowerDailyReport.getQueryGroups();*/
        TMisDunningPeople dunningPeople = new TMisDunningPeople();
        Map<String, Object> map = initQueryAuthority(dunningPeople);
        List<TMisDunningPeople> dunningPeoples = (List<TMisDunningPeople>) map.get("dunningPeoples");
        List<TMisDunningGroup> groups = (List<TMisDunningGroup>) map.get("groups");
        smMisDunningProductivePowerDailyReport.setQueryGroups(groups);
        boolean groupLimit = true;
        if (groups == null) {
            groupLimit = false;
        }
        if (StringUtils.isEmpty(smMisDunningProductivePowerDailyReport.getDunningPeopleName())){
            List<String> peopleNames = new ArrayList<String>(dunningPeoples.size());
            for (TMisDunningPeople people : dunningPeoples){
                peopleNames.add(people.getName());
            }
            smMisDunningProductivePowerDailyReport.setpNames(peopleNames);
        }
		Page<SMisDunningProductivePowerDailyReport> page = reportService
				.findProductivePowerDailyReport(new Page<SMisDunningProductivePowerDailyReport>(request, response), smMisDunningProductivePowerDailyReport);
		model.addAttribute("page", page);
		model.addAttribute("groupList", groups);
		model.addAttribute("groupLimit", groupLimit);

		//若只管理一个小组则默认选中
		if (groups.size() == 1) {
			smMisDunningProductivePowerDailyReport.setGroupId(groups.get(0).getId());
		}
		return "modules/dunning/sMisDunningProductivePowerDailyReportList";
	}

	/**
	 * @Description: 催收员案件活动日报导出
	 * @return: String
	 */
	@RequiresPermissions("dunning:sMisDunningProductivePowerDailyReport:view")
	@RequestMapping(value = "productivePowerDailyReportExport", method = RequestMethod.POST)
	public String dunningProductivePowerDailyReportExport(SMisDunningProductivePowerDailyReport smMisDunningProductivePowerDailyReport,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String fileName = "productivePowerDayReport" + DateUtils.getDate("yyyy-MM-dd HHmmss") + ".xlsx";
		//添加默认查询条件
		reportService.setQueryConditions(smMisDunningProductivePowerDailyReport);
		List<SMisDunningProductivePowerDailyReport> page = reportService.findProductivePowerDailyReport(smMisDunningProductivePowerDailyReport);
		try {
			new ExportExcel("催收员案件活动日报", SMisDunningProductivePowerDailyReport.class).setDataList(page).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			logger.info("催收员案件活动日报导出失败",e);
			addMessage(redirectAttributes, "导出失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/dunning/report/productivePowerDailyReport?repage";
	}
	
	/**
	 * @Description  催收绩效日表
	 * @param performanceDayReport
	 * @param dunningPeople
	 * @param request
	 * @param response
	 * @param model
	 * @return java.lang.String
	 */
	@RequiresPermissions("dunning:tMisDunningTask:viewReport")
	@RequestMapping(value = {"findPerformanceDayReport", ""})
	public String findPerformanceDayReport(PerformanceDayReport performanceDayReport,TMisDunningPeople dunningPeople, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			if(null == performanceDayReport.getDatetimestart()){
				performanceDayReport.setDatetimestart(DateUtils.getDateToDay(new Date()));
			}
			if(null == performanceDayReport.getDatetimeend()){
				performanceDayReport.setDatetimeend( DateUtils.getDateToDay(new Date()));
			}
			Map<String, Object> map = initQueryAuthority(dunningPeople);
            List<TMisDunningPeople> dunningPeoples = (List<TMisDunningPeople>) map.get("dunningPeoples");
			if (performanceDayReport.getGroup() == null){
			    performanceDayReport.setGroup((TMisDunningGroup) map.get("tMisDunningGroup"));
            }
            if (StringUtils.isEmpty(performanceDayReport.getPersonnel())){
                List<String> peopleNames = new ArrayList<String>(dunningPeoples.size());
			    for (TMisDunningPeople people : dunningPeoples){
			        peopleNames.add(people.getName());
                }
                performanceDayReport.setpNames(peopleNames);
            }
			Page<PerformanceDayReport> page = reportService.findPerformanceDayReport(new Page<PerformanceDayReport>(request, response), performanceDayReport);
			//List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findList(dunningPeople);
			model.addAttribute("groupList", map.get("groups"));
			model.addAttribute("dunningPeoples", dunningPeoples);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.info("催收绩效日表打开失败",e);
			return "error";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		return "modules/dunning/performanceDayReportList";
	}

	/**
	 * @return java.lang.String
	 * @Description 催收绩效日报导出
	 */
	@RequiresPermissions("dunning:tMisDunningTask:viewReport")
	@RequestMapping(value = "performanceDayReportExport", method = RequestMethod.POST)
	public String performanceDayReportExport(PerformanceDayReport performanceDayReport, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			if(null == performanceDayReport.getDatetimestart()){
				performanceDayReport.setDatetimestart(DateUtils.getDateToDay(new Date()));
			}
			if(null == performanceDayReport.getDatetimeend()){
				performanceDayReport.setDatetimeend( DateUtils.getDateToDay(new Date()));
			}
			String fileName = "performanceDayReport" + DateUtils.getDate("yyyy-MM-dd HHmmss") + ".xlsx";
			List<PerformanceDayReport> page = reportService.findPerformanceDayReport(performanceDayReport);
			new ExportExcel("导出催收日表", PerformanceDayReport.class).setDataList(page).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			logger.info("催收绩效日报导出失败！", e);
			addMessage(redirectAttributes, "导出失败！失败信息：" + e.getMessage());
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		return "redirect:" + adminPath + "/dunning/tMisDunningTask/findPerformanceDayReport?repage";
	}

	/**
	 * 软电话通话详单
	 * @param dunningPhoneReportFile
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions("dunning:tMisCallingRecord:viewReport")
	@RequestMapping(value = "softPhoneCommunicateReportExport")
	public String softPhoneCommunicateReportExport(DunningPhoneReportFile dunningPhoneReportFile,  HttpServletRequest request, HttpServletResponse response,
												   RedirectAttributes redirectAttributes){
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			List<DunningPhoneReportFile> entityList = tMisCallingRecordService.exportSoftPhoneReportFile(dunningPhoneReportFile);
			String fileName = DateUtils.formatDate(dunningPhoneReportFile.getDatetimestart(), "yyyy-MM-dd HH:mm")+"至"+DateUtils.formatDate(dunningPhoneReportFile.getDatetimeend(), "yyyy-MM-dd HH:mm") + "软电话通话详单";
			new ExportExcel(fileName, DunningPhoneDetailReportFile.class).setDataList(entityList).write(response,fileName+".xlsx").dispose();
		} catch (IOException e) {
			logger.info("软电话通话详单!", e);
			addMessage(redirectAttributes, "导出失败！失败信息：" + e.getMessage());
		}finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		return "redirect:"+ adminPath + "/dunning/tMisCallingRecord/getPhoneCallingReport";
	}


	/**
	 * 软电话通话详单（日常）
	 * @param dunningPhoneReportFile
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions("dunning:tMisCallingRecord:viewReport")
	@RequestMapping(value = "softPhoneCommunicateReportExportForEveryDayList")
	public String softPhoneCommunicateReportExportForEveryDayList(DunningPhoneReportFile dunningPhoneReportFile,  HttpServletRequest request, HttpServletResponse response,
												   RedirectAttributes redirectAttributes){
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			List<DunningPhoneReportFile> entityList = tMisCallingRecordService.exportSoftPhoneReportFileForEveryDay(dunningPhoneReportFile);
			String fileName = DateUtils.formatDate(dunningPhoneReportFile.getDatetimestart(), "yyyy-MM-dd HH:mm")+"至"+DateUtils.formatDate(dunningPhoneReportFile.getDatetimeend(), "yyyy-MM-dd HH:mm") + "软电话报表";
			new ExportExcel(fileName, DunningPhoneReportFile.class).setDataList(entityList).write(response,fileName+".xlsx").dispose();
		} catch (IOException e) {
			logger.info("软电话报表详单!", e);
			addMessage(redirectAttributes, "导出失败！失败信息：" + e.getMessage());
		}finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		return "redirect:"+ adminPath + "/dunning/tMisCallingRecord/getPhoneCallingReportForEveryDayList";
	}

	/**
	 * 初始化查询权限
	 * 催收组长权限可看到自己组员的数据
	 * 催收监理权限可看到自己机构下所有组员的数据
	 * 催收总监权限可以看到全部数据
	 * @param dunningPeople
	 * @return
	 */
	private Map<String, Object> initQueryAuthority(TMisDunningPeople dunningPeople){
		Map<String, Object> restMap = new HashMap<String, Object>();
		List<TMisDunningPeople> dunningPeoples = null;
		List<TMisDunningGroup> groups = new ArrayList<TMisDunningGroup>();
		TMisDunningGroup tMisDunningGroup = new TMisDunningGroup();
		int permissions = TMisDunningTaskService.getPermissions();
		//催收专员
		if (permissions == TMisDunningTaskService.DUNNING_COMMISSIONER_PERMISSIONS){
/*			TMisDunningPeople dp = new TMisDunningPeople();
			dunningPeoples = tMisDunningPeopleService.findList(dp);
			groups.add(dunningPeoples.get(0).getGroup());*/
		}else {
			//催收主管
			if (permissions == TMisDunningTaskService.DUNNING_INNER_PERMISSIONS) {
				tMisDunningGroup.setLeader(UserUtils.getUser());
			}
			//催收监管
			if (permissions == TMisDunningTaskService.DUNNING_SUPERVISOR) {
				tMisDunningGroup.setSupervisor(UserUtils.getUser());
			}
			groups = groupService.findList(tMisDunningGroup);
			StringBuffer stringBuffer = new StringBuffer("");
			for (TMisDunningGroup group : groups) {
				stringBuffer.append("," + group.getId());
			}
			List<String> groupIds = null;
			if (stringBuffer.toString().length() == 0){
				groupIds = new ArrayList<String>();
			}else {
				groupIds = Arrays.asList(stringBuffer.toString().substring(1).split(","));
			}
			tMisDunningGroup.setGroupIds(groupIds);

			dunningPeople.setGroup(tMisDunningGroup);
			dunningPeoples = tMisDunningPeopleService.findList(dunningPeople);
		}
		restMap.put("dunningPeoples", dunningPeoples);
		restMap.put("tMisDunningGroup", tMisDunningGroup);
		restMap.put("groups", groups);
		return restMap;
	}

}