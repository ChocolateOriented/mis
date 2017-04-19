package com.mo9.risk.modules.dunning.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * @Description 催收小组Controller
 * @author LiJingXiang
 * @version 2017年4月11日
 */
@Controller
@RequestMapping(value="${adminPath}/dunning/tMisDunningGroup")
public class TMisDunningGroupController extends BaseController {
	@Autowired
	private TMisDunningGroupService tMisDunningGroupService ;
	
	/**
	 * @Description: 催收小组集合
	 */
	@ModelAttribute
	public void groupType(Model model) {
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
	}
	
	/**
	 * @Description: 小组列表
	 */
	@RequiresPermissions("dunning:TMisDunningGroup:view")
	@RequestMapping(value={"list",""})
	public String list(TMisDunningGroup tMisDunningGroup ,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisDunningGroup> page = tMisDunningGroupService.findPage(new Page<TMisDunningGroup>(request, response), tMisDunningGroup);
		model.addAttribute("page", page);
		return "modules/dunning/tMisDunningGroupList";
	}
	
	/**
	 * @Description: 修改小组
	 */
	@RequiresPermissions("dunning:TMisDunningGroup:edit")
	@RequestMapping(value = "edit")
	public String edit(@RequestParam(required=true) String id , Model model , RedirectAttributes redirectAttributes) {
		if (StringUtils.isBlank(id)) {
			addMessage(redirectAttributes, "请选择要修改的小组");
			return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningGroup/?repage";
		}
		
		TMisDunningGroup tMisDunningGroup = tMisDunningGroupService.get(id) ;
		model.addAttribute("TMisDunningGroup", tMisDunningGroup);
		logger.debug("修改小组:"+tMisDunningGroup);
		return this.form(tMisDunningGroup, model) ;
	}
	
	/**
	 * @Description: 加载编辑催收小组页面
	 */
	@RequiresPermissions("dunning:TMisDunningGroup:edit")
	@RequestMapping(value = "form")
	public String form(TMisDunningGroup tMisDunningGroup , Model model) {
		logger.debug("加载编辑催收小组页面");
		model.addAttribute("users", tMisDunningGroupService.findUserList()) ;
		return "modules/dunning/tMisDunningGroupForm";
	}
	
	
	/**
	 * @Description: 保存或编辑催收小组
	 */
	@RequiresPermissions("dunning:TMisDunningGroup:edit")
	@RequestMapping(value = "save")
	public String save(TMisDunningGroup tMisDunningGroup, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunningGroup)){
			return form(tMisDunningGroup, model);
		}
		
		tMisDunningGroupService.save(tMisDunningGroup);
		logger.debug("保存催收小组:"+tMisDunningGroup.toString());
		addMessage(redirectAttributes, "保存催收小组成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningGroup/?repage";
	}
	
	/**
	 * @Description: 删除催收小组
	 */
	@RequiresPermissions("dunning:TMisDunningGroup:edit")
	@RequestMapping(value = "delete")
	public String delete(@RequestParam(required=true) String id , RedirectAttributes redirectAttributes) {
		logger.debug("删除催收小组:"+id);
		tMisDunningGroupService.delete(new TMisDunningGroup(id));
		addMessage(redirectAttributes, "删除催收小组成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningGroup/?repage";
	}
	
	
}
