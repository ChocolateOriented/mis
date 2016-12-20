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
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderWithRemittime;
import com.thinkgem.jeesite.modules.weeklyreport.service.SRiskOrderWithRemittimeService;

/**
 * 月放款订单催回率Controller
 * @author 徐盛
 * @version 2016-11-30
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/sRiskOrderWithRemittime")
public class SRiskOrderWithRemittimeController extends BaseController {

	@Autowired
	private SRiskOrderWithRemittimeService sRiskOrderWithRemittimeService;
	
	@ModelAttribute
	public SRiskOrderWithRemittime get(@RequestParam(required=false) String id) {
		SRiskOrderWithRemittime entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sRiskOrderWithRemittimeService.get(id);
		}
		if (entity == null){
			entity = new SRiskOrderWithRemittime();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderWithRemittime:view")
	@RequestMapping(value = {"list", ""})
	public String list(SRiskOrderWithRemittime sRiskOrderWithRemittime, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SRiskOrderWithRemittime> page = sRiskOrderWithRemittimeService.findPage(new Page<SRiskOrderWithRemittime>(request, response), sRiskOrderWithRemittime); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/sRiskOrderWithRemittimeList";
	}

	@RequiresPermissions("weeklyreport:sRiskOrderWithRemittime:view")
	@RequestMapping(value = "form")
	public String form(SRiskOrderWithRemittime sRiskOrderWithRemittime, Model model) {
		model.addAttribute("sRiskOrderWithRemittime", sRiskOrderWithRemittime);
		return "modules/weeklyreport/sRiskOrderWithRemittimeForm";
	}

	@RequiresPermissions("weeklyreport:sRiskOrderWithRemittime:edit")
	@RequestMapping(value = "save")
	public String save(SRiskOrderWithRemittime sRiskOrderWithRemittime, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sRiskOrderWithRemittime)){
			return form(sRiskOrderWithRemittime, model);
		}
		sRiskOrderWithRemittimeService.save(sRiskOrderWithRemittime);
		addMessage(redirectAttributes, "保存月放款订单催回率成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderWithRemittime/?repage";
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderWithRemittime:edit")
	@RequestMapping(value = "delete")
	public String delete(SRiskOrderWithRemittime sRiskOrderWithRemittime, RedirectAttributes redirectAttributes) {
		sRiskOrderWithRemittimeService.delete(sRiskOrderWithRemittime);
		addMessage(redirectAttributes, "删除月放款订单催回率成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderWithRemittime/?repage";
	}

}