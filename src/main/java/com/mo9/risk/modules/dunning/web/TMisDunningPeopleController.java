/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;

/**
 * 催收人员Controller
 * @author 徐盛
 * @version 2016-07-12
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningPeople")
public class TMisDunningPeopleController extends BaseController {

	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	@ModelAttribute
	public TMisDunningPeople get(@RequestParam(required=false) String id) {
		TMisDunningPeople entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisDunningPeopleService.get(id);
		}
		if (entity == null){
			entity = new TMisDunningPeople();
		}
		return entity;
	}
	
	/**
	 * 催收人员列表
	 * @param tMisDunningPeople
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisDunningPeople tMisDunningPeople, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisDunningPeople> page = tMisDunningPeopleService.findPage(new Page<TMisDunningPeople>(request, response), tMisDunningPeople); 
		model.addAttribute("page", page);
		return "modules/dunning/tMisDunningPeopleList";
	}

	/**
	 * 加载编辑催收人员页面
	 * @param tMisDunningPeople
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:view")
	@RequestMapping(value = "form")
	public String form(TMisDunningPeople tMisDunningPeople, Model model) {
		List<User> users = tMisDunningPeopleService.findUserList();
		model.addAttribute("users",users);
		model.addAttribute("tMisDunningPeople", tMisDunningPeople);
		return "modules/dunning/tMisDunningPeopleForm";
	}
	
	
	/**
	 * 保存或编辑催收人员
	 * @param tMisDunningPeople
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "save")
	public String save(TMisDunningPeople tMisDunningPeople, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunningPeople)){
			return form(tMisDunningPeople, model);
		}
		if(null != tMisDunningPeople.getDbid()){
			TMisDunningPeople oldDunningPeople = get(tMisDunningPeople.getId());
			if(!oldDunningPeople.getBegin().equals(tMisDunningPeople.getBegin()) || !oldDunningPeople.getEnd().equals(tMisDunningPeople.getEnd())){
				int count = tMisDunningTaskService.findDunningCount(oldDunningPeople.getId());
				if(count > 0){
					addMessage(model, "请清空此催收人员的任务!");
					return form(tMisDunningPeople, model);
				}
			}
		}
		tMisDunningPeopleService.save(tMisDunningPeople);
		addMessage(redirectAttributes, "保存催收人员成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningPeople/?repage";
	}
	
	/**
	 * 删除催收人员
	 * @param tMisDunningPeople
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisDunningPeople tMisDunningPeople, RedirectAttributes redirectAttributes) {
		if(null != tMisDunningPeople.getId()){
			int count = tMisDunningTaskService.findDunningCount(tMisDunningPeople.getId());
			if(count > 0){
				addMessage(redirectAttributes, "请清空此催收人员的任务!");
				return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningPeople/?repage";
			}
		}
		tMisDunningPeopleService.delete(tMisDunningPeople);
		addMessage(redirectAttributes, "删除催收人员成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningPeople/?repage";
	}
	
	

}