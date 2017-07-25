/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.mo9.risk.modules.dunning.entity.SMisDunningTaskMonthReport;
import com.mo9.risk.modules.dunning.entity.SMisMigrationData;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.service.SMisDunningTaskMonthReportService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 催收月绩效Controller
 * @author 徐盛
 * @version 2016-11-30
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/sMisDunningTaskMonthReport")
public class SMisDunningTaskMonthReportController extends BaseController {

	@Autowired
	private SMisDunningTaskMonthReportService sMisDunningTaskMonthReportService;
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	@ModelAttribute
	public SMisDunningTaskMonthReport get(@RequestParam(required=false) String id) {
		SMisDunningTaskMonthReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sMisDunningTaskMonthReportService.get(id);
		}
		if (entity == null){
			entity = new SMisDunningTaskMonthReport();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:sMisDunningTaskMonthReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(SMisDunningTaskMonthReport sMisDunningTaskMonthReport, HttpServletRequest request,TMisDunningPeople dunningPeople, HttpServletResponse response, Model model) {
		
		String month = DateUtils.formatDate(null == sMisDunningTaskMonthReport.getDatetime()  ? new Date() : sMisDunningTaskMonthReport.getDatetime() , "yyyyMM");
		sMisDunningTaskMonthReport.setMonths(month);
		Page<SMisDunningTaskMonthReport> page = sMisDunningTaskMonthReportService.findPage(new Page<SMisDunningTaskMonthReport>(request, response), sMisDunningTaskMonthReport); 
		model.addAttribute("page", page);
		List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findList(dunningPeople);
		model.addAttribute("dunningPeoples", dunningPeoples);
		return "modules/dunning/sMisDunningTaskMonthReportList";
	}
	
	/**
	 * 导出用户数据
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:sMisDunningTaskMonthReport:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SMisDunningTaskMonthReport newtest, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "performanceMonthReport"+DateUtils.getDate("yyyy-MM-ddHHmmss")+".xlsx";
            String month = DateUtils.formatDate(null == newtest.getDatetime()  ? new Date() : newtest.getDatetime() , "yyyyMM");
            newtest.setMonths(month);
            List<SMisDunningTaskMonthReport> page = sMisDunningTaskMonthReportService.findList(newtest);
    		new ExportExcel("月绩效报表", SMisDunningTaskMonthReport.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/dunning/sMisDunningTaskMonthReport/list?repage";
    }
	

	@RequiresPermissions("dunning:sMisDunningTaskMonthReport:view")
	@RequestMapping(value = "form")
	public String form(SMisDunningTaskMonthReport sMisDunningTaskMonthReport, Model model) {
		model.addAttribute("sMisDunningTaskMonthReport", sMisDunningTaskMonthReport);
		return "modules/dunning/sMisDunningTaskMonthReportForm";
	}

	@RequiresPermissions("dunning:sMisDunningTaskMonthReport:edit")
	@RequestMapping(value = "save")
	public String save(SMisDunningTaskMonthReport sMisDunningTaskMonthReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sMisDunningTaskMonthReport)){
			return form(sMisDunningTaskMonthReport, model);
		}
		sMisDunningTaskMonthReportService.save(sMisDunningTaskMonthReport);
		addMessage(redirectAttributes, "保存催收月绩效成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/sMisDunningTaskMonthReport/?repage";
	}
	
	@RequiresPermissions("dunning:sMisDunningTaskMonthReport:edit")
	@RequestMapping(value = "delete")
	public String delete(SMisDunningTaskMonthReport sMisDunningTaskMonthReport, RedirectAttributes redirectAttributes) {
		sMisDunningTaskMonthReportService.delete(sMisDunningTaskMonthReport);
		addMessage(redirectAttributes, "删除催收月绩效成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/sMisDunningTaskMonthReport/?repage";
	}
	/**
	 * 贷后报表测试用例
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "test")
	public String testReportPage() {
		return "modules/dunning/sMisDunningMigrationRateTest";
		
	}
	/**
	 * 关于测试迁徙率数据的获取
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "testMigration")
	@ResponseBody
	public void testMigration() {
		sMisDunningTaskMonthReportService.migrationRateGetData();
	}
	
	/**
	 * 迁徙率数据的日报
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "migrationdata")
	public String migrationReport() {
		
		return "modules/dunning/sMisDunningMigrationReport";
	}
	/**
	 * 迁徙率户数的图形表
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "migrationRate")
	public String migrationData() {
		
		return "modules/dunning/sMisDunningMigrationRate";
	}
	/**
	 * 获取户数迁徙率的数据
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "migrationGetdata")
	@ResponseBody
	public Map<String, Object> migrationGetData() {
		//获取户数迁徙_c-p1
		Map<String, Object> migrationData=new HashMap<String, Object>();
		
		//横坐标集合（1-16）
		List<Integer>  cycle=new ArrayList<Integer>();
		for (int i = 1; i < 17; i++) {
			cycle.add(i);
		}
		migrationData.put("cycle", cycle);
		
		//Q1同时展示5个周期的时间
		List<String>  qtime1=new ArrayList<String>();
		qtime1.add("201705_16");
		qtime1.add("201705_16+");
		qtime1.add("201706_15");
		qtime1.add("201706_15+");
		qtime1.add("201707_15");
		migrationData.put("qtime1", qtime1);
		
		//Q2同时展示4个周期的时间
		List<String>  qtime2=new ArrayList<String>();
		qtime2.add("201705_16");
		qtime2.add("201705_16+");
		qtime2.add("201706_15");
		qtime2.add("201706_15+");
		migrationData.put("qtime2", qtime2);
		//Q3同时展示3个周期的时间
		List<String>  qtime3=new ArrayList<String>();
		qtime3.add("201705_16");
		qtime3.add("201705_16+");
		qtime3.add("201706_15");
		migrationData.put("qtime3", qtime3);
		//Q4同时展示4个周期的时间
		List<String>  qtime4=new ArrayList<String>();
		qtime4.add("201705_16");
		qtime4.add("201705_16+");
		migrationData.put("qtime4", qtime4);
		
		//获取c-p1的5个周期数据
		List<SMisMigrationData>  smdList1=new ArrayList<SMisMigrationData>();
		for (int i = 0; i <qtime1.size(); i++) {
			SMisMigrationData smd1=new SMisMigrationData();
			smd1.setName(qtime1.get(i));
			List<Integer>  cycleTime1=new ArrayList<Integer>();
			for (int y= 5*(i+1)+2; y< 5*(i+1)+18; y++) {
				cycleTime1.add(y);
			} 
			smd1.setData(cycleTime1);
			smdList1.add(smd1);
			
		}
		
		migrationData.put("smdList1", smdList1);
		
		List<SMisMigrationData>  smdList2=new ArrayList<SMisMigrationData>();
		for (int i = 0; i <qtime2.size(); i++) {
			SMisMigrationData smd1=new SMisMigrationData();
			smd1.setName(qtime1.get(i));
			List<Integer>  cycleTime1=new ArrayList<Integer>();
			for (int y= 7*(i+1)+2; y< 7*(i+1)+18; y++) {
				cycleTime1.add(y);
			}
			smd1.setData(cycleTime1);
			smdList2.add(smd1);
			
		}
		
		migrationData.put("smdList2", smdList2);
		
		List<SMisMigrationData>  smdList3=new ArrayList<SMisMigrationData>();
		for (int i = 0; i <qtime3.size(); i++) {
			SMisMigrationData smd1=new SMisMigrationData();
			smd1.setName(qtime1.get(i));
			List<Integer>  cycleTime1=new ArrayList<Integer>();
			for (int y= 9*(i+1)+2; y< 9*(i+1)+18; y++) {
				cycleTime1.add(y);
			}
			smd1.setData(cycleTime1);
			smdList3.add(smd1);
			
		}
		
		migrationData.put("smdList3", smdList3);
		
		List<SMisMigrationData>  smdList4=new ArrayList<SMisMigrationData>();
		for (int i = 0; i <qtime4.size(); i++) {
			SMisMigrationData smd1=new SMisMigrationData();
			smd1.setName(qtime1.get(i));
			List<Integer>  cycleTime1=new ArrayList<Integer>();
			for (int y= 11*(i+1)+2; y< 11*(i+1)+18; y++) {
				cycleTime1.add(y);
			}
			smd1.setData(cycleTime1);
			smdList4.add(smd1);
			
		}
		
		migrationData.put("smdList4", smdList4);
		
		

		
		System.out.println(JSON.toJSONString(migrationData));
		return migrationData;
	}

}