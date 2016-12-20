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
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportWeek;
import com.thinkgem.jeesite.modules.weeklyreport.service.SRiskOrderOutReportWeekService;

/**
 * 委外周报表Controller
 * @author 徐盛
 * @version 2016-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/sRiskOrderOutReportWeek")
public class SRiskOrderOutReportWeekController extends BaseController {

	@Autowired
	private SRiskOrderOutReportWeekService sRiskOrderOutReportWeekService;
	
	@ModelAttribute
	public SRiskOrderOutReportWeek get(@RequestParam(required=false) String id) {
		SRiskOrderOutReportWeek entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sRiskOrderOutReportWeekService.get(id);
		}
		if (entity == null){
			entity = new SRiskOrderOutReportWeek();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderOutReportWeek:view")
	@RequestMapping(value = {"list", ""})
	public String list(SRiskOrderOutReportWeek sRiskOrderOutReportWeek, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SRiskOrderOutReportWeek> page = sRiskOrderOutReportWeekService.findPage(new Page<SRiskOrderOutReportWeek>(request, response), sRiskOrderOutReportWeek); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/sRiskOrderOutReportWeekList";
	}
	
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("weeklyreport:sRiskOrderOutReportWeek:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SRiskOrderOutReportWeek newtest, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "riskOrderOutReportWeek"+DateUtils.getDate("yyyy-MM-ddHHmmss")+".xlsx";
            List<SRiskOrderOutReportWeek> page = sRiskOrderOutReportWeekService.findList(newtest);
    		new ExportExcel("委外周报表", SRiskOrderOutReportWeek.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/weeklyreport/sRiskOrderOutReportWeek/list?repage";
    }
	

	@RequiresPermissions("weeklyreport:sRiskOrderOutReportWeek:view")
	@RequestMapping(value = "form")
	public String form(SRiskOrderOutReportWeek sRiskOrderOutReportWeek, Model model) {
		model.addAttribute("sRiskOrderOutReportWeek", sRiskOrderOutReportWeek);
		return "modules/weeklyreport/sRiskOrderOutReportWeekForm";
	}

	@RequiresPermissions("weeklyreport:sRiskOrderOutReportWeek:edit")
	@RequestMapping(value = "save")
	public String save(SRiskOrderOutReportWeek sRiskOrderOutReportWeek, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sRiskOrderOutReportWeek)){
			return form(sRiskOrderOutReportWeek, model);
		}
		sRiskOrderOutReportWeekService.save(sRiskOrderOutReportWeek);
		addMessage(redirectAttributes, "保存委外周报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderOutReportWeek/?repage";
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderOutReportWeek:edit")
	@RequestMapping(value = "delete")
	public String delete(SRiskOrderOutReportWeek sRiskOrderOutReportWeek, RedirectAttributes redirectAttributes) {
		sRiskOrderOutReportWeekService.delete(sRiskOrderOutReportWeek);
		addMessage(redirectAttributes, "删除委外周报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderOutReportWeek/?repage";
	}

}