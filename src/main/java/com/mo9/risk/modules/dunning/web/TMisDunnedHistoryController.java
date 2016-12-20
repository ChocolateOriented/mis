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
import com.mo9.risk.modules.dunning.entity.TMisDunnedHistory;
import com.mo9.risk.modules.dunning.service.TMisDunnedHistoryService;

/**
 * 催收历史Controller
 * @author 徐盛
 * @version 2016-07-12
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunnedHistory")
public class TMisDunnedHistoryController extends BaseController {

	@Autowired
	private TMisDunnedHistoryService tMisDunnedHistoryService;
	
	@ModelAttribute
	public TMisDunnedHistory get(@RequestParam(required=false) String id) {
		TMisDunnedHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisDunnedHistoryService.get(id);
		}
		if (entity == null){
			entity = new TMisDunnedHistory();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:tMisDunnedHistory:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisDunnedHistory tMisDunnedHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisDunnedHistory> page = tMisDunnedHistoryService.findPage(new Page<TMisDunnedHistory>(request, response), tMisDunnedHistory); 
		model.addAttribute("page", page);
		return "modules/dunning/tMisDunnedHistoryList";
	}

	@RequiresPermissions("dunning:tMisDunnedHistory:view")
	@RequestMapping(value = "form")
	public String form(TMisDunnedHistory tMisDunnedHistory, Model model) {
		model.addAttribute("tMisDunnedHistory", tMisDunnedHistory);
		return "modules/dunning/tMisDunnedHistoryForm";
	}

	@RequiresPermissions("dunning:tMisDunnedHistory:edit")
	@RequestMapping(value = "save")
	public String save(TMisDunnedHistory tMisDunnedHistory, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunnedHistory)){
			return form(tMisDunnedHistory, model);
		}
		tMisDunnedHistoryService.save(tMisDunnedHistory);
		addMessage(redirectAttributes, "保存催收历史成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunnedHistory/?repage";
	}
	
	@RequiresPermissions("dunning:tMisDunnedHistory:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisDunnedHistory tMisDunnedHistory, RedirectAttributes redirectAttributes) {
		tMisDunnedHistoryService.delete(tMisDunnedHistory);
		addMessage(redirectAttributes, "删除催收历史成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunnedHistory/?repage";
	}

}