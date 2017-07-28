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
	
	
}