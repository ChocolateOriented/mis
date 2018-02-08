package com.mo9.risk.modules.dunning.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.TMisDunningOrganization;
import com.mo9.risk.modules.dunning.service.TMisDunningOrganizationService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping(value="${adminPath}/dunning/tMisDunningOrganization")
public class TMisDunningOrganizationController extends BaseController {
	
	@Autowired
	private TMisDunningOrganizationService tMisDunningOrganizationService;
	
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;

	/**
	 * 加载编辑催收机构页面
	 */
	@RequiresPermissions("dunning:TMisDunningOrganization:edit")
	@RequestMapping(value = "form")
	public String form(@ModelAttribute("tMisDunningOrganization") TMisDunningOrganization tMisDunningOrganization, Model model, HttpServletRequest request) {
		String opr = request.getParameter("opr");
		model.addAttribute("peoples", tMisDunningPeopleService.findOptionList(null));
		model.addAttribute("opr", opr);
		
		if ("edit".equals(opr)) {
			List<TMisDunningOrganization> organizations = tMisDunningOrganizationService.findList(null);
			model.addAttribute("organizations", organizations);
		}
		return "modules/dunning/dialog/tMisDunningOrganizationForm";
	}
	
	/**
	 * 添加催收机构
	 */
	@RequiresPermissions("dunning:TMisDunningOrganization:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(TMisDunningOrganization tMisDunningOrganization, Model model, RedirectAttributes redirectAttributes) {
		tMisDunningOrganizationService.save(tMisDunningOrganization);
		return "OK";
	}

}
