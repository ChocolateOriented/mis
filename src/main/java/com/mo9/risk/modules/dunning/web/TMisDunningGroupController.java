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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;

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
	
	@ModelAttribute
	public void groupType(Model model) {
		Map<String, String> groupTypes = new HashMap<String, String>();
		groupTypes.put(TMisDunningGroup.GROUP_TYPE_SELF,"自营");
		groupTypes.put(TMisDunningGroup.GROUP_TYPE_OUT_SEAT,"外包坐席");
		groupTypes.put(TMisDunningGroup.GROUP_TYPE_OUT_COMMISSION,"委外佣金");
		model.addAttribute("groupTypes", groupTypes) ;
	}
	
//	@RequiresPermissions("")//TODO
	@RequestMapping(value={"list",""})
	public String list(TMisDunningGroup tMisDunningGroup ,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisDunningGroup> page = tMisDunningGroupService.findPage(new Page<TMisDunningGroup>(request, response), tMisDunningGroup);
		model.addAttribute("page", page);
		return "modules/dunning/tMisDunningGroupList";
	}
	
	/**
	 * 加载编辑催收小组页面
	 */
//	@RequiresPermissions("dunning:tMisDunningPeople:view")//TODO
	@RequestMapping(value = "form")
	public String form(TMisDunningGroup tMisDunningGroup, Model model) {
		model.addAttribute("tMisDunningGroup", tMisDunningGroup);
		return "modules/dunning/tMisDunningGroupForm";
	}
	
	
	/**
	 * 保存或编辑催收小组
	 */
//	@RequiresPermissions("dunning:tMisDunningPeople:view")//TODO
	@RequestMapping(value = "save")
	public String save(TMisDunningGroup tMisDunningGroup, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunningGroup)){
			return form(tMisDunningGroup, model);
		}
		//getIsNewRecord
		logger.debug("保存催收小组:"+tMisDunningGroup.toString());
		tMisDunningGroupService.save(tMisDunningGroup);
		addMessage(redirectAttributes, "保存催收人员成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningGroup/?repage";
	}
	
	/**
	 * 删除催收小组
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "delete")
	public String delete(@RequestParam String id , RedirectAttributes redirectAttributes) {
		tMisDunningGroupService.delete(id);
		addMessage(redirectAttributes, "删除催收人员成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningPeople/?repage";
	}
	
	
}
