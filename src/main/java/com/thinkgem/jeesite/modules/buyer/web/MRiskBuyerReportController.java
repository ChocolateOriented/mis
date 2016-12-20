/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.buyer.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.buyer.entity.MRiskBuyerReport;
import com.thinkgem.jeesite.modules.buyer.service.MRiskBuyerReportService;

/**
 * 用户报表Controller
 * @author 徐盛
 * @version 2016-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/buyerreport/mRiskBuyerReport")
public class MRiskBuyerReportController extends BaseController {

	@Autowired
	private MRiskBuyerReportService mRiskBuyerReportService;
	
	@ModelAttribute
	public MRiskBuyerReport get(@RequestParam(required=false) String id) {
		MRiskBuyerReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mRiskBuyerReportService.get(id);
		}
		if (entity == null){
			entity = new MRiskBuyerReport();
		}
		return entity;
	}
	
	@RequiresPermissions("buyerreport:mRiskBuyerReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(MRiskBuyerReport mRiskBuyerReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MRiskBuyerReport> page = mRiskBuyerReportService.findPage(new Page<MRiskBuyerReport>(request, response), mRiskBuyerReport); 
		model.addAttribute("page", page);
		return "modules/buyerreport/mRiskBuyerReportList";
	}

	@RequiresPermissions("buyerreport:mRiskBuyerReport:view")
	@RequestMapping(value = "form")
	public String form(MRiskBuyerReport mRiskBuyerReport, Model model) {
		model.addAttribute("mRiskBuyerReport", mRiskBuyerReport);
		return "modules/buyerreport/mRiskBuyerReportForm";
	}

	@RequiresPermissions("buyerreport:mRiskBuyerReport:edit")
	@RequestMapping(value = "save")
	public String save(MRiskBuyerReport mRiskBuyerReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mRiskBuyerReport)){
			return form(mRiskBuyerReport, model);
		}
		mRiskBuyerReportService.save(mRiskBuyerReport);
		addMessage(redirectAttributes, "保存用户报表成功");
		return "redirect:"+Global.getAdminPath()+"/buyerreport/mRiskBuyerReport/?repage";
	}
	
	@RequiresPermissions("buyerreport:mRiskBuyerReport:edit")
	@RequestMapping(value = "delete")
	public String delete(MRiskBuyerReport mRiskBuyerReport, RedirectAttributes redirectAttributes) {
		mRiskBuyerReportService.delete(mRiskBuyerReport);
		addMessage(redirectAttributes, "删除用户报表成功");
		return "redirect:"+Global.getAdminPath()+"/buyerreport/mRiskBuyerReport/?repage";
	}
	
	@RequiresPermissions("buyerreport:mRiskBuyerReport:view")
	@RequestMapping(value = "dialog")
	public String dialog(MRiskBuyerReport mRiskBuyerReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		request.getParameter("a");
		model.addAttribute("mRiskBuyerReport", mRiskBuyerReport);
		return "modules/buyerreport/dialog";
	}
	
	@RequiresPermissions("buyerreport:mRiskBuyerReport:edit")
	@RequestMapping(value = "dialogsave")
	@ResponseBody
	public String dialogSave(MRiskBuyerReport mRiskBuyerReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mRiskBuyerReport)){
			return form(mRiskBuyerReport, model);
		}
		mRiskBuyerReportService.save(mRiskBuyerReport);
		addMessage(redirectAttributes, "保存用户报表成功");
//		return "redirect:"+Global.getAdminPath()+"/buyerreport/mRiskBuyerReport/?repage";
		return "asd";
	}
	

}