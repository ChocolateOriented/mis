/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.web;

import java.util.List;

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

import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.weeklyreport.entity.TMisDunningOuterFee;
import com.thinkgem.jeesite.modules.weeklyreport.service.TMisDunningOuterFeeService;

/**
 * 基础佣金费率表Controller
 * @author 徐盛
 * @version 2016-11-08
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/tMisDunningOuterFee")
public class TMisDunningOuterFeeController extends BaseController {

	@Autowired
	private TMisDunningOuterFeeService tMisDunningOuterFeeService;
	
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	
	@ModelAttribute
	public TMisDunningOuterFee get(@RequestParam(required=false) String id) {
		TMisDunningOuterFee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisDunningOuterFeeService.get(id);
		}
		if (entity == null){
			entity = new TMisDunningOuterFee();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:tMisDunningOuterFee:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisDunningOuterFee tMisDunningOuterFee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisDunningOuterFee> page = tMisDunningOuterFeeService.findPage(new Page<TMisDunningOuterFee>(request, response), tMisDunningOuterFee); 
		List<TMisDunningPeople> peoples = tMisDunningPeopleService.findDunningPeopleByType("outer");
		model.addAttribute("peoples", peoples);
		model.addAttribute("page", page);
		return "modules/weeklyreport/tMisDunningOuterFeeList";
	}

	@RequiresPermissions("weeklyreport:tMisDunningOuterFee:view")
	@RequestMapping(value = "form")
	public String form(TMisDunningOuterFee tMisDunningOuterFee, Model model) {
		model.addAttribute("tMisDunningOuterFee", tMisDunningOuterFee);
		List<TMisDunningPeople> peoples = tMisDunningPeopleService.findDunningPeopleByType("outer");
		model.addAttribute("peoples", peoples);
		return "modules/weeklyreport/tMisDunningOuterFeeForm";
	}

	@RequiresPermissions("weeklyreport:tMisDunningOuterFee:edit")
	@RequestMapping(value = "save")
	public String save(TMisDunningOuterFee tMisDunningOuterFee, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunningOuterFee)){
			return form(tMisDunningOuterFee, model);
		}
		TMisDunningOuterFee  dunningOuterFee = tMisDunningOuterFeeService.findListOneByNewFee(tMisDunningOuterFee);
		if(null != dunningOuterFee){
			dunningOuterFee.setDatetimeend(tMisDunningOuterFee.getDatetimebegin());
			tMisDunningOuterFeeService.saveAndupdate(tMisDunningOuterFee,dunningOuterFee);
		}else{
			tMisDunningOuterFeeService.save(tMisDunningOuterFee);
		}
		addMessage(redirectAttributes, "保存基础佣金费率表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/tMisDunningOuterFee/?repage";
	}
	
	@RequiresPermissions("weeklyreport:tMisDunningOuterFee:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisDunningOuterFee tMisDunningOuterFee, RedirectAttributes redirectAttributes) {
		tMisDunningOuterFeeService.delete(tMisDunningOuterFee);
		addMessage(redirectAttributes, "删除基础佣金费率表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/tMisDunningOuterFee/?repage";
	}

}