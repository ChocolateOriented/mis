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
	 * 迁徙率数据的图表
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "migrationdata")
	public String migrationData() {
		
		return "modules/dunning/sMisDunningMigrationRate";
	}
	/**
	 * 获取迁徙率数据的图表
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "migrationGetdata")
	@ResponseBody
	public Map<String, Object> migrationGetData() {
		
		Map<String, Object> migrationData=new HashMap<String, Object>();
		
		List<String>  cycle=new ArrayList<String>();
		cycle.add("一周");
		cycle.add("二周");
		cycle.add("三周");
		cycle.add("四周");
		cycle.add("五周");
		migrationData.put("cycle", cycle);
		
		List<String>  mirg=new ArrayList<String>();
		mirg.add("户数Q1");
		mirg.add("户数Q2");
		mirg.add("户数Q3");
		mirg.add("户数Q4");
		mirg.add("本金Q1");
		mirg.add("本金Q2");
		mirg.add("本金Q3");
		mirg.add("本金Q4");
		migrationData.put("mirg", mirg);
		
		List<SMisMigrationData>  smdList=new ArrayList<SMisMigrationData>();
		
		SMisMigrationData smd1=new SMisMigrationData();
		smd1.setName("户数Q1");
		List<Integer>  hQ1=new ArrayList<Integer>();
		hQ1.add(15);
		hQ1.add(40);
		hQ1.add(30);
		hQ1.add(40);
		hQ1.add(50);
		smd1.setData(hQ1);
		smdList.add(smd1);
		
		
		SMisMigrationData smd2=new SMisMigrationData();
		smd2.setName("户数Q2");
		List<Integer>  hQ2=new ArrayList<Integer>();
		hQ2.add(32);
		hQ2.add(52);
		hQ2.add(12);
		hQ2.add(82);
		smd2.setData(hQ2);
		smdList.add(smd2);
		
		
		SMisMigrationData smd3=new SMisMigrationData();
		smd3.setName("户数Q3");
		List<Integer>  hQ3=new ArrayList<Integer>();
		hQ3.add(43);
		hQ3.add(73);
		hQ3.add(93);
		smd3.setData(hQ3);
		smdList.add(smd3);
		
		
		SMisMigrationData smd4=new SMisMigrationData();
		smd4.setName("户数Q4");
		List<Integer>  hQ4=new ArrayList<Integer>();
		hQ4.add(74);
		hQ4.add(34);
		smd4.setData(hQ4);
		smdList.add(smd4);
		
		
		SMisMigrationData smd5=new SMisMigrationData();
		smd5.setName("本金Q1");
		List<Integer>  hQ5=new ArrayList<Integer>();
		hQ5.add(25);
		hQ5.add(55);
		hQ5.add(15);
		hQ5.add(85);
		hQ5.add(95);
		smd5.setData(hQ5);
		smdList.add(smd5);
		
		
		SMisMigrationData smd6=new SMisMigrationData();
		smd6.setName("本金Q2");
		List<Integer>  hQ6=new ArrayList<Integer>();
		hQ6.add(26);
		hQ6.add(66);
		hQ6.add(16);
		hQ6.add(56);
		smd6.setData(hQ6);
		smdList.add(smd6);
		
		
		SMisMigrationData smd7=new SMisMigrationData();
		smd7.setName("本金Q3");
		List<Integer>  hQ7=new ArrayList<Integer>();
		hQ7.add(70);
		hQ7.add(20);
		hQ7.add(90);
		smd7.setData(hQ7);
		smdList.add(smd7);
		
		
		SMisMigrationData smd8=new SMisMigrationData();
		smd8.setName("本金Q4");
		List<Integer>  hQ8=new ArrayList<Integer>();
		hQ8.add(90);
		hQ8.add(10);
		smd8.setData(hQ8);
		smdList.add(smd8);
		
		migrationData.put("smdList", smdList);
		
		System.out.println(JSON.toJSONString(migrationData));
		return migrationData;
	}

}