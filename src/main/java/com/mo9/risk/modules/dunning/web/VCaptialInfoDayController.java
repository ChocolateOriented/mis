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
import com.mo9.risk.modules.dunning.entity.VCaptialInfoDay;
import com.mo9.risk.modules.dunning.service.VCaptialInfoDayService;

/**
 * 资金成本日报Controller
 * @author 徐盛
 * @version 2016-09-25
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/vCaptialInfoDay")
public class VCaptialInfoDayController extends BaseController {

	@Autowired
	private VCaptialInfoDayService vCaptialInfoDayService;
	
	@ModelAttribute
	public VCaptialInfoDay get(@RequestParam(required=false) String id) {
		VCaptialInfoDay entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = vCaptialInfoDayService.get(id);
		}
		if (entity == null){
			entity = new VCaptialInfoDay();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:vCaptialInfoDay:view")
	@RequestMapping(value = {"list", ""})
	public String list(VCaptialInfoDay vCaptialInfoDay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VCaptialInfoDay> page = vCaptialInfoDayService.findPage(new Page<VCaptialInfoDay>(request, response), vCaptialInfoDay); 
		model.addAttribute("page", page);
		return "modules/dunning/vCaptialInfoDayList";
	}

	@RequiresPermissions("dunning:vCaptialInfoDay:view")
	@RequestMapping(value = "form")
	public String form(VCaptialInfoDay vCaptialInfoDay, Model model) {
		model.addAttribute("vCaptialInfoDay", vCaptialInfoDay);
		return "modules/dunning/vCaptialInfoDayForm";
	}

	@RequiresPermissions("dunning:vCaptialInfoDay:edit")
	@RequestMapping(value = "save")
	public String save(VCaptialInfoDay vCaptialInfoDay, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, vCaptialInfoDay)){
			return form(vCaptialInfoDay, model);
		}
		vCaptialInfoDayService.save(vCaptialInfoDay);
		addMessage(redirectAttributes, "保存资金成本日报成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/vCaptialInfoDay/?repage";
	}
	
	@RequiresPermissions("dunning:vCaptialInfoDay:edit")
	@RequestMapping(value = "delete")
	public String delete(VCaptialInfoDay vCaptialInfoDay, RedirectAttributes redirectAttributes) {
		vCaptialInfoDayService.delete(vCaptialInfoDay);
		addMessage(redirectAttributes, "删除资金成本日报成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/vCaptialInfoDay/?repage";
	}

}