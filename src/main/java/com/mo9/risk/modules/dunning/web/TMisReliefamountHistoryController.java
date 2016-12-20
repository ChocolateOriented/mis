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
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory;
import com.mo9.risk.modules.dunning.service.TMisReliefamountHistoryService;

/**
 * 减免记录Controller
 * @author 徐盛
 * @version 2016-08-05
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisReliefamountHistory")
public class TMisReliefamountHistoryController extends BaseController {

	@Autowired
	private TMisReliefamountHistoryService tMisReliefamountHistoryService;
	
	@ModelAttribute
	public TMisReliefamountHistory get(@RequestParam(required=false) String id) {
		TMisReliefamountHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisReliefamountHistoryService.get(id);
		}
		if (entity == null){
			entity = new TMisReliefamountHistory();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:tMisReliefamountHistory:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisReliefamountHistory tMisReliefamountHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisReliefamountHistory> page = tMisReliefamountHistoryService.findPage(new Page<TMisReliefamountHistory>(request, response), tMisReliefamountHistory); 
		model.addAttribute("page", page);
		return "modules/dunning/tMisReliefamountHistoryList";
	}

	@RequiresPermissions("dunning:tMisReliefamountHistory:view")
	@RequestMapping(value = "form")
	public String form(TMisReliefamountHistory tMisReliefamountHistory, Model model) {
		model.addAttribute("tMisReliefamountHistory", tMisReliefamountHistory);
		return "modules/dunning/tMisReliefamountHistoryForm";
	}

	@RequiresPermissions("dunning:tMisReliefamountHistory:edit")
	@RequestMapping(value = "save")
	public String save(TMisReliefamountHistory tMisReliefamountHistory, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisReliefamountHistory)){
			return form(tMisReliefamountHistory, model);
		}
		tMisReliefamountHistoryService.save(tMisReliefamountHistory);
		addMessage(redirectAttributes, "保存减免记录成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisReliefamountHistory/?repage";
	}
	
	@RequiresPermissions("dunning:tMisReliefamountHistory:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisReliefamountHistory tMisReliefamountHistory, RedirectAttributes redirectAttributes) {
		tMisReliefamountHistoryService.delete(tMisReliefamountHistory);
		addMessage(redirectAttributes, "删除减免记录成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisReliefamountHistory/?repage";
	}

}