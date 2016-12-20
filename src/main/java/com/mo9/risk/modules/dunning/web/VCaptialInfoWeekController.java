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
import com.mo9.risk.modules.dunning.entity.VCaptialInfoWeek;
import com.mo9.risk.modules.dunning.service.VCaptialInfoWeekService;

/**
 * 资金成本周报Controller
 * @author 徐盛
 * @version 2016-09-25
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/vCaptialInfoWeek")
public class VCaptialInfoWeekController extends BaseController {

	@Autowired
	private VCaptialInfoWeekService vCaptialInfoWeekService;
	
	@ModelAttribute
	public VCaptialInfoWeek get(@RequestParam(required=false) String id) {
		VCaptialInfoWeek entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = vCaptialInfoWeekService.get(id);
		}
		if (entity == null){
			entity = new VCaptialInfoWeek();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:vCaptialInfoWeek:view")
	@RequestMapping(value = {"list", ""})
	public String list(VCaptialInfoWeek vCaptialInfoWeek, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VCaptialInfoWeek> page = vCaptialInfoWeekService.findPage(new Page<VCaptialInfoWeek>(request, response), vCaptialInfoWeek); 
		model.addAttribute("page", page);
		return "modules/dunning/vCaptialInfoWeekList";
	}

	@RequiresPermissions("dunning:vCaptialInfoWeek:view")
	@RequestMapping(value = "form")
	public String form(VCaptialInfoWeek vCaptialInfoWeek, Model model) {
		model.addAttribute("vCaptialInfoWeek", vCaptialInfoWeek);
		return "modules/dunning/vCaptialInfoWeekForm";
	}

	@RequiresPermissions("dunning:vCaptialInfoWeek:edit")
	@RequestMapping(value = "save")
	public String save(VCaptialInfoWeek vCaptialInfoWeek, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, vCaptialInfoWeek)){
			return form(vCaptialInfoWeek, model);
		}
		vCaptialInfoWeekService.save(vCaptialInfoWeek);
		addMessage(redirectAttributes, "保存资金成本周报成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/vCaptialInfoWeek/?repage";
	}
	
	@RequiresPermissions("dunning:vCaptialInfoWeek:edit")
	@RequestMapping(value = "delete")
	public String delete(VCaptialInfoWeek vCaptialInfoWeek, RedirectAttributes redirectAttributes) {
		vCaptialInfoWeekService.delete(vCaptialInfoWeek);
		addMessage(redirectAttributes, "删除资金成本周报成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/vCaptialInfoWeek/?repage";
	}

}