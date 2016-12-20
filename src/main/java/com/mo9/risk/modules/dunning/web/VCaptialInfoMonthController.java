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
import com.mo9.risk.modules.dunning.entity.VCaptialInfoMonth;
import com.mo9.risk.modules.dunning.service.VCaptialInfoMonthService;

/**
 * 资金成本月报Controller
 * @author 徐盛
 * @version 2016-09-25
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/vCaptialInfoMonth")
public class VCaptialInfoMonthController extends BaseController {

	@Autowired
	private VCaptialInfoMonthService vCaptialInfoMonthService;
	
	@ModelAttribute
	public VCaptialInfoMonth get(@RequestParam(required=false) String id) {
		VCaptialInfoMonth entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = vCaptialInfoMonthService.get(id);
		}
		if (entity == null){
			entity = new VCaptialInfoMonth();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:vCaptialInfoMonth:view")
	@RequestMapping(value = {"list", ""})
	public String list(VCaptialInfoMonth vCaptialInfoMonth, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VCaptialInfoMonth> page = vCaptialInfoMonthService.findPage(new Page<VCaptialInfoMonth>(request, response), vCaptialInfoMonth); 
		model.addAttribute("page", page);
		return "modules/dunning/vCaptialInfoMonthList";
	}

	@RequiresPermissions("dunning:vCaptialInfoMonth:view")
	@RequestMapping(value = "form")
	public String form(VCaptialInfoMonth vCaptialInfoMonth, Model model) {
		model.addAttribute("vCaptialInfoMonth", vCaptialInfoMonth);
		return "modules/dunning/vCaptialInfoMonthForm";
	}

	@RequiresPermissions("dunning:vCaptialInfoMonth:edit")
	@RequestMapping(value = "save")
	public String save(VCaptialInfoMonth vCaptialInfoMonth, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, vCaptialInfoMonth)){
			return form(vCaptialInfoMonth, model);
		}
		vCaptialInfoMonthService.save(vCaptialInfoMonth);
		addMessage(redirectAttributes, "保存资金成本月报成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/vCaptialInfoMonth/?repage";
	}
	
	@RequiresPermissions("dunning:vCaptialInfoMonth:edit")
	@RequestMapping(value = "delete")
	public String delete(VCaptialInfoMonth vCaptialInfoMonth, RedirectAttributes redirectAttributes) {
		vCaptialInfoMonthService.delete(vCaptialInfoMonth);
		addMessage(redirectAttributes, "删除资金成本月报成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/vCaptialInfoMonth/?repage";
	}

}