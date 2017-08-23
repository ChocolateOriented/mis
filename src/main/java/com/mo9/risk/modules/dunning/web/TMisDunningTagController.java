/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.web.BaseController;
import com.mo9.risk.modules.dunning.entity.TMisDunningTag;
import com.mo9.risk.modules.dunning.service.TMisDunningTagService;

/**
 * 用户标签Controller
 * @author shijlu
 * @version 2017-08-22
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningTag")
public class TMisDunningTagController extends BaseController {

	@Autowired
	private TMisDunningTagService tMisDunningTagService;
	
	@RequiresPermissions("dunning:tMisDunningTag:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisDunningTag tMisDunningTag, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<TMisDunningTag> tags = tMisDunningTagService.findList(tMisDunningTag);
		model.addAttribute("tags", tags);
		return "modules/dunning/tMisDunningTaskDeductList";
	}

	@RequiresPermissions("dunning:tMisDunningTag:edit")
	@RequestMapping(value = "saveTag")
	@ResponseBody
	public Map<String, String> saveTag(TMisDunningTag tMisDunningTag, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<String, String>();
		boolean addable = tMisDunningTagService.preCheckExist(tMisDunningTag.getDealcode());
		
		if (!addable) {
			result.put("status", "NO");
			result.put("msg", "无法添加更多标签");
			return result;
		}
		String id = tMisDunningTagService.saveTag(tMisDunningTag);
		result.put("status", "OK");
		result.put("id", id);
		return result;
	}
	
	@RequiresPermissions("dunning:tMisDunningTag:edit")
	@RequestMapping(value = "editTag")
	@ResponseBody
	public Map<String, String> editTag(TMisDunningTag tMisDunningTag, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<String, String>();
		int num = tMisDunningTagService.editTag(tMisDunningTag);
		if (num == 0) {
			result.put("status", "NO");
			result.put("msg", "当前标签已被删除");
		} else {
			result.put("status", "OK");
			result.put("id", tMisDunningTag.getId());
		}
		return result;
	}
	
	@RequiresPermissions("dunning:tMisDunningTag:edit")
	@RequestMapping(value = "closeTag")
	@ResponseBody
	public String closeTag(TMisDunningTag tMisDunningTag, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		tMisDunningTagService.closeTag(tMisDunningTag.getId());
		return "OK";
	}
	
	@RequiresPermissions("dunning:tMisDunningTag:edit")
	@RequestMapping(value = "preCheck")
	@ResponseBody
	public String preCheck(TMisDunningTag tMisDunningTag, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		boolean exist = tMisDunningTagService.preCheckExist(tMisDunningTag.getDealcode());
		return exist ? "OK" : "NO";
	}
	
	@RequiresPermissions("dunning:tMisDunningTag:view")
	@RequestMapping(value = "get")
	@ResponseBody
	public TMisDunningTag get(TMisDunningTag tMisDunningTag, HttpServletRequest request, HttpServletResponse response) {
		TMisDunningTag result = tMisDunningTagService.get(tMisDunningTag.getId());
		return result;
	}

	@RequiresPermissions("dunning:tMisDunningTag:view")
	@RequestMapping(value = "form")
	public String form(TMisDunningTag tMisDunningTag, Model model) {
		model.addAttribute("tMisDunningTag", tMisDunningTag);
		return "modules/dunning/tMisDunningTagForm";
	}

	@RequiresPermissions("dunning:tMisDunningTag:edit")
	@RequestMapping(value = "save")
	public String save(TMisDunningTag tMisDunningTag, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunningTag)){
			return form(tMisDunningTag, model);
		}
		tMisDunningTagService.save(tMisDunningTag);
		addMessage(redirectAttributes, "保存记录成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningTag/?repage";
	}
	
	@RequiresPermissions("dunning:tMisDunningTag:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisDunningTag tMisDunningTag, RedirectAttributes redirectAttributes) {
		tMisDunningTagService.delete(tMisDunningTag);
		addMessage(redirectAttributes, "删除记录成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningTag/?repage";
	}

}