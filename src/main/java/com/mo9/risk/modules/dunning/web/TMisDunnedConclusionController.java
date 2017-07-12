/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.util.HashMap;
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
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.TMisDunnedConclusion;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.service.TMisDunnedConclusionService;

/**
 * 电催结论Controller
 * @author shijlu
 * @version 2017-03-06
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunnedConclusion")
public class TMisDunnedConclusionController extends BaseController {

	@Autowired
	private TMisDunnedConclusionService tMisDunnedConclusionService;
	
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	
	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisDunnedConclusion tMisDunnedConclusion, HttpServletRequest request, HttpServletResponse response,String mobileSelf, Model model) {
		String dealcode = request.getParameter("dealcode");
		String buyerId = request.getParameter("buyerId");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		String hasContact = request.getParameter("hasContact");
		String dunningCycle = request.getParameter("dunningCycle");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		tMisDunnedConclusion.setBuyerid(Integer.parseInt(buyerId));
		Page<TMisDunnedConclusion> page = tMisDunnedConclusionService.findPage(new Page<TMisDunnedConclusion>(request, response), tMisDunnedConclusion);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		boolean ispayoff = false;
		if(null != task){
			ispayoff = task.getIspayoff();
		}else{
			ispayoff = true;
		}

		model.addAttribute("page", page);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("ispayoff", ispayoff);
		model.addAttribute("hasContact", hasContact);
		model.addAttribute("dunningCycle", dunningCycle);
		model.addAttribute("mobileSelf", mobileSelf);
		return "modules/dunning/tMisDunningTaskConclusionList";
	}

	@RequiresPermissions("dunning:tMisContantRecord:edit")
	@RequestMapping(value = "saveRecord")
	@ResponseBody
	public String saveRecord(TMisDunnedConclusion tMisDunnedConclusion, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		if (!beanValidator(model, tMisDunnedConclusion)){
			return form(tMisDunnedConclusion, model);
		}
		String dealcode = request.getParameter("dealcode");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");

		boolean result = tMisDunnedConclusionService.saveRecord(tMisDunnedConclusion, dealcode, dunningtaskdbid);
		if (!result) {
			addMessage(redirectAttributes, "保存记录失败");
			return "NO";
		}
		addMessage(redirectAttributes, "保存记录成功");
		return "OK";
	}

	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = "form")
	public String form(TMisDunnedConclusion tMisDunnedConclusion, Model model) {
		model.addAttribute("tMisDunnedConclusion", tMisDunnedConclusion);
		return "modules/dunning/tMisDunnedConclusionForm";
	}

	@RequiresPermissions("dunning:tMisContantRecord:edit")
	@RequestMapping(value = "save")
	public String save(TMisDunnedConclusion tMisDunnedConclusion, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunnedConclusion)){
			return form(tMisDunnedConclusion, model);
		}
		tMisDunnedConclusionService.save(tMisDunnedConclusion);
		addMessage(redirectAttributes, "保存催收结论成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunnedConclusion/?repage";
	}
	
	@RequiresPermissions("dunning:tMisContantRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisDunnedConclusion tMisDunnedConclusion, RedirectAttributes redirectAttributes) {
		tMisDunnedConclusionService.delete(tMisDunnedConclusion);
		addMessage(redirectAttributes, "删除催收结论成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunnedConclusion/?repage";
	}

	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = "nextfollowdate")
	@ResponseBody
	public Map<String, String> getNextFollowDate(HttpServletRequest request, HttpServletResponse response) {
		return tMisDunnedConclusionService.getFollowDateConfig();
	}
}