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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportDay;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderAfterLoanPushing;
import com.thinkgem.jeesite.modules.weeklyreport.service.SRiskOrderOutReportDayService;

/**
 * 委外日报表Controller
 * @author 徐盛
 * @version 2016-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/sRiskOrderOutReportDay")
public class SRiskOrderOutReportDayController extends BaseController {

	@Autowired
	private SRiskOrderOutReportDayService sRiskOrderOutReportDayService;
	
	@ModelAttribute
	public SRiskOrderOutReportDay get(@RequestParam(required=false) String id) {
		SRiskOrderOutReportDay entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sRiskOrderOutReportDayService.get(id);
		}
		if (entity == null){
			entity = new SRiskOrderOutReportDay();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderOutReportDay:view")
	@RequestMapping(value = {"list", ""})
	public String list(SRiskOrderOutReportDay sRiskOrderOutReportDay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SRiskOrderOutReportDay> page = sRiskOrderOutReportDayService.findPage(new Page<SRiskOrderOutReportDay>(request, response), sRiskOrderOutReportDay); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/sRiskOrderOutReportDayList";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("weeklyreport:sRiskOrderOutReportDay:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SRiskOrderOutReportDay newtest, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "riskOrderOutReportDay"+DateUtils.getDate("yyyy-MM-ddHHmmss")+".xlsx";
            List<SRiskOrderOutReportDay> page = sRiskOrderOutReportDayService.findList(newtest);
    		new ExportExcel("委外日报表", SRiskOrderOutReportDay.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/weeklyreport/sRiskOrderOutReportDay/list?repage";
    }
	

	@RequiresPermissions("weeklyreport:sRiskOrderOutReportDay:view")
	@RequestMapping(value = "form")
	public String form(SRiskOrderOutReportDay sRiskOrderOutReportDay, Model model) {
		model.addAttribute("sRiskOrderOutReportDay", sRiskOrderOutReportDay);
		return "modules/weeklyreport/sRiskOrderOutReportDayForm";
	}

	@RequiresPermissions("weeklyreport:sRiskOrderOutReportDay:edit")
	@RequestMapping(value = "save")
	public String save(SRiskOrderOutReportDay sRiskOrderOutReportDay, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sRiskOrderOutReportDay)){
			return form(sRiskOrderOutReportDay, model);
		}
		sRiskOrderOutReportDayService.save(sRiskOrderOutReportDay);
		addMessage(redirectAttributes, "保存委外日报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderOutReportDay/?repage";
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderOutReportDay:edit")
	@RequestMapping(value = "delete")
	public String delete(SRiskOrderOutReportDay sRiskOrderOutReportDay, RedirectAttributes redirectAttributes) {
		sRiskOrderOutReportDayService.delete(sRiskOrderOutReportDay);
		addMessage(redirectAttributes, "删除委外日报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderOutReportDay/?repage";
	}

}