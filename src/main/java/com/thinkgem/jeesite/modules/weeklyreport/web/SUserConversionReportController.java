/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SUserConversionReport;
import com.thinkgem.jeesite.modules.weeklyreport.service.SUserConversionReportService;

/**
 * 用户报表Controller
 * @author 徐盛
 * @version 2016-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/sUserConversionReport")
public class SUserConversionReportController extends BaseController {

	@Autowired
	private SUserConversionReportService sUserConversionReportService;
	
	@ModelAttribute
	public SUserConversionReport get(@RequestParam(required=false) String id) {
		SUserConversionReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sUserConversionReportService.get(id);
		}
		if (entity == null){
			entity = new SUserConversionReport();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:sUserConversionReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(SUserConversionReport sUserConversionReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SUserConversionReport> page = sUserConversionReportService.findPage(new Page<SUserConversionReport>(request, response), sUserConversionReport); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/sUserConversionReportList";
	}

	@RequiresPermissions("weeklyreport:sUserConversionReport:view")
	@RequestMapping(value = "form")
	public String form(SUserConversionReport sUserConversionReport, Model model) {
		model.addAttribute("sUserConversionReport", sUserConversionReport);
		return "modules/weeklyreport/sUserConversionReportForm";
	}

	@RequiresPermissions("weeklyreport:sUserConversionReport:edit")
	@RequestMapping(value = "save")
	public String save(SUserConversionReport sUserConversionReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sUserConversionReport)){
			return form(sUserConversionReport, model);
		}
		sUserConversionReportService.save(sUserConversionReport);
		addMessage(redirectAttributes, "保存用户报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sUserConversionReport/?repage";
	}
	
	@RequiresPermissions("weeklyreport:sUserConversionReport:edit")
	@RequestMapping(value = "delete")
	public String delete(SUserConversionReport sUserConversionReport, RedirectAttributes redirectAttributes) {
		sUserConversionReportService.delete(sUserConversionReport);
		addMessage(redirectAttributes, "删除用户报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sUserConversionReport/?repage";
	}

}