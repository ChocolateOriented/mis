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
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderWithRepaymenttime;
import com.thinkgem.jeesite.modules.weeklyreport.service.SRiskOrderWithRepaymenttimeService;

/**
 * 月逾期订单催回率Controller
 * @author 徐盛
 * @version 2016-11-30
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/sRiskOrderWithRepaymenttime")
public class SRiskOrderWithRepaymenttimeController extends BaseController {

	@Autowired
	private SRiskOrderWithRepaymenttimeService sRiskOrderWithRepaymenttimeService;
	
	@ModelAttribute
	public SRiskOrderWithRepaymenttime get(@RequestParam(required=false) String id) {
		SRiskOrderWithRepaymenttime entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sRiskOrderWithRepaymenttimeService.get(id);
		}
		if (entity == null){
			entity = new SRiskOrderWithRepaymenttime();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderWithRepaymenttime:view")
	@RequestMapping(value = {"list", ""})
	public String list(SRiskOrderWithRepaymenttime sRiskOrderWithRepaymenttime, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SRiskOrderWithRepaymenttime> page = sRiskOrderWithRepaymenttimeService.findPage(new Page<SRiskOrderWithRepaymenttime>(request, response), sRiskOrderWithRepaymenttime); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/sRiskOrderWithRepaymenttimeList";
	}

	@RequiresPermissions("weeklyreport:sRiskOrderWithRepaymenttime:view")
	@RequestMapping(value = "form")
	public String form(SRiskOrderWithRepaymenttime sRiskOrderWithRepaymenttime, Model model) {
		model.addAttribute("sRiskOrderWithRepaymenttime", sRiskOrderWithRepaymenttime);
		return "modules/weeklyreport/sRiskOrderWithRepaymenttimeForm";
	}

	@RequiresPermissions("weeklyreport:sRiskOrderWithRepaymenttime:edit")
	@RequestMapping(value = "save")
	public String save(SRiskOrderWithRepaymenttime sRiskOrderWithRepaymenttime, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sRiskOrderWithRepaymenttime)){
			return form(sRiskOrderWithRepaymenttime, model);
		}
		sRiskOrderWithRepaymenttimeService.save(sRiskOrderWithRepaymenttime);
		addMessage(redirectAttributes, "保存月逾期订单催回率成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderWithRepaymenttime/?repage";
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderWithRepaymenttime:edit")
	@RequestMapping(value = "delete")
	public String delete(SRiskOrderWithRepaymenttime sRiskOrderWithRepaymenttime, RedirectAttributes redirectAttributes) {
		sRiskOrderWithRepaymenttimeService.delete(sRiskOrderWithRepaymenttime);
		addMessage(redirectAttributes, "删除月逾期订单催回率成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderWithRepaymenttime/?repage";
	}

}