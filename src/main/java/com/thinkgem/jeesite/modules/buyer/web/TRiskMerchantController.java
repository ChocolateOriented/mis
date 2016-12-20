/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.buyer.web;

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
import com.thinkgem.jeesite.modules.buyer.entity.TRiskMerchant;
import com.thinkgem.jeesite.modules.buyer.service.TRiskMerchantService;

/**
 * 用户报表Controller
 * @author 徐盛
 * @version 2016-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/buyer/tRiskMerchant")
public class TRiskMerchantController extends BaseController {

	@Autowired
	private TRiskMerchantService tRiskMerchantService;
	
	@ModelAttribute
	public TRiskMerchant get(@RequestParam(required=false) String id) {
		TRiskMerchant entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tRiskMerchantService.get(id);
		}
		if (entity == null){
			entity = new TRiskMerchant();
		}
		return entity;
	}
	
	@RequiresPermissions("buyer:tRiskMerchant:view")
	@RequestMapping(value = {"list", ""})
	public String list(TRiskMerchant tRiskMerchant, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TRiskMerchant> page = tRiskMerchantService.findPage(new Page<TRiskMerchant>(request, response), tRiskMerchant); 
		model.addAttribute("page", page);
		return "modules/buyer/tRiskMerchantList";
	}

	@RequiresPermissions("buyer:tRiskMerchant:view")
	@RequestMapping(value = "form")
	public String form(TRiskMerchant tRiskMerchant, Model model) {
		model.addAttribute("tRiskMerchant", tRiskMerchant);
		return "modules/buyer/tRiskMerchantForm";
	}

	@RequiresPermissions("buyer:tRiskMerchant:edit")
	@RequestMapping(value = "save")
	public String save(TRiskMerchant tRiskMerchant, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tRiskMerchant)){
			return form(tRiskMerchant, model);
		}
		tRiskMerchantService.save(tRiskMerchant);
		addMessage(redirectAttributes, "保存用户报表成功");
		return "redirect:"+Global.getAdminPath()+"/buyer/tRiskMerchant/?repage";
	}
	
	@RequiresPermissions("buyer:tRiskMerchant:edit")
	@RequestMapping(value = "delete")
	public String delete(TRiskMerchant tRiskMerchant, RedirectAttributes redirectAttributes) {
		tRiskMerchantService.delete(tRiskMerchant);
		addMessage(redirectAttributes, "删除用户报表成功");
		return "redirect:"+Global.getAdminPath()+"/buyer/tRiskMerchant/?repage";
	}

}