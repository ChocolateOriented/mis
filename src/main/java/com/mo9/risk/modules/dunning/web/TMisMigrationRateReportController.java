/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

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
import com.mo9.risk.modules.dunning.entity.TMisMigrationRateReport;
import com.mo9.risk.modules.dunning.service.TMisMigrationRateReportService;

/**
 * 迁徙率Controller
 * @author 徐盛
 * @version 2017-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisMigrationRateReport")
public class TMisMigrationRateReportController extends BaseController {

	@Autowired
	private TMisMigrationRateReportService tMisMigrationRateReportService;
	
	@ModelAttribute
	public TMisMigrationRateReport get(@RequestParam(required=false) String id) {
		TMisMigrationRateReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisMigrationRateReportService.get(id);
		}
		if (entity == null){
			entity = new TMisMigrationRateReport();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisMigrationRateReport tMisMigrationRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisMigrationRateReport> page = tMisMigrationRateReportService.findPage(new Page<TMisMigrationRateReport>(request, response), tMisMigrationRateReport); 
		model.addAttribute("page", page);
		return "modules/dunning/tMisMigrationRateReportList";
	}

	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = "form")
	public String form(TMisMigrationRateReport tMisMigrationRateReport, Model model) {
		model.addAttribute("tMisMigrationRateReport", tMisMigrationRateReport);
		return "modules/dunning/tMisMigrationRateReportForm";
	}

	@RequiresPermissions("dunning:tMisMigrationRateReport:edit")
	@RequestMapping(value = "save")
	public String save(TMisMigrationRateReport tMisMigrationRateReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisMigrationRateReport)){
			return form(tMisMigrationRateReport, model);
		}
		tMisMigrationRateReportService.save(tMisMigrationRateReport);
		addMessage(redirectAttributes, "保存迁徙率成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisMigrationRateReport/?repage";
	}
	
//	@RequiresPermissions("dunning:tMisMigrationRateReport:edit")
//	@RequestMapping(value = "delete")
//	public String delete(TMisMigrationRateReport tMisMigrationRateReport, RedirectAttributes redirectAttributes) {
//		tMisMigrationRateReportService.delete(tMisMigrationRateReport);
//		addMessage(redirectAttributes, "删除迁徙率成功");
//		return "redirect:"+Global.getAdminPath()+"/dunning/tMisMigrationRateReport/?repage";
//	}
	
	

}