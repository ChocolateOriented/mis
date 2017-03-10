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
import com.mo9.risk.modules.dunning.entity.TMisDunningTaskLog;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskLogService;

/**
 * 催收任务logController
 * @author 徐盛
 * @version 2017-03-01
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningTaskLog")
public class TMisDunningTaskLogController extends BaseController {

	@Autowired
	private TMisDunningTaskLogService tMisDunningTaskLogService;
	
	@ModelAttribute
	public TMisDunningTaskLog get(@RequestParam(required=false) String id) {
		TMisDunningTaskLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisDunningTaskLogService.get(id);
		}
		if (entity == null){
			entity = new TMisDunningTaskLog();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:tMisDunningTaskLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisDunningTaskLog tMisDunningTaskLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisDunningTaskLog> page = tMisDunningTaskLogService.findPage(new Page<TMisDunningTaskLog>(request, response), tMisDunningTaskLog); 
		model.addAttribute("page", page);
		return "modules/dunning/tMisDunningTaskLogList";
	}

	@RequiresPermissions("dunning:tMisDunningTaskLog:view")
	@RequestMapping(value = "form")
	public String form(TMisDunningTaskLog tMisDunningTaskLog, Model model) {
		model.addAttribute("tMisDunningTaskLog", tMisDunningTaskLog);
		return "modules/dunning/tMisDunningTaskLogForm";
	}

	@RequiresPermissions("dunning:tMisDunningTaskLog:edit")
	@RequestMapping(value = "save")
	public String save(TMisDunningTaskLog tMisDunningTaskLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunningTaskLog)){
			return form(tMisDunningTaskLog, model);
		}
		tMisDunningTaskLogService.save(tMisDunningTaskLog);
		addMessage(redirectAttributes, "保存催收任务log成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningTaskLog/?repage";
	}
	
	@RequiresPermissions("dunning:tMisDunningTaskLog:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisDunningTaskLog tMisDunningTaskLog, RedirectAttributes redirectAttributes) {
		tMisDunningTaskLogService.delete(tMisDunningTaskLog);
		addMessage(redirectAttributes, "删除催收任务log成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningTaskLog/?repage";
	}

}