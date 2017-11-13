/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.PerformanceDayReport;
import com.mo9.risk.modules.dunning.entity.SMisDunningProductivePowerDailyReport;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.service.DunningReportService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
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
		//添加默认查询条件
		reportService.setQueryConditions(smMisDunningProductivePowerDailyReport);
		Page<SMisDunningProductivePowerDailyReport> page = reportService
				.findProductivePowerDailyReport(new Page<SMisDunningProductivePowerDailyReport>(request, response), smMisDunningProductivePowerDailyReport);
		model.addAttribute("page", page);

		//催收小组列表,若无限制则查询所有小组
		List<TMisDunningGroup> groups = smMisDunningProductivePowerDailyReport.getQueryGroups();
		boolean groupLimit = true;
		if (groups == null) {
			groups = groupService.findList(new TMisDunningGroup());
			groupLimit = false;
		}
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
			Page<PerformanceDayReport> page = reportService.findPerformanceDayReport(new Page<PerformanceDayReport>(request, response), performanceDayReport);
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findList(dunningPeople);
			model.addAttribute("groupList", groupService.findList(new TMisDunningGroup()));
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
			performanceDayReport.setDatetimestart(
					null == performanceDayReport.getDatetimestart() ? DateUtils.getDateToDay(new Date()) : performanceDayReport.getDatetimestart());
			performanceDayReport
					.setDatetimeend(null == performanceDayReport.getDatetimeend() ? DateUtils.getDateToDay(new Date()) : performanceDayReport.getDatetimeend());
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
}