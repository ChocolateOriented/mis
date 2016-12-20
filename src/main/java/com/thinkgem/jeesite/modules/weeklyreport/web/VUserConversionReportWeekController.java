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
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanWeekReportBean;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VUserConversionReportWeek;
import com.thinkgem.jeesite.modules.weeklyreport.service.VUserConversionReportWeekService;

/**
 * 用户周报表Controller
 * @author 徐盛
 * @version 2016-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/vUserConversionReportWeek")
public class VUserConversionReportWeekController extends BaseController {

	@Autowired
	private VUserConversionReportWeekService vUserConversionReportWeekService;
	
	@ModelAttribute
	public VUserConversionReportWeek get(@RequestParam(required=false) String id) {
		VUserConversionReportWeek entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = vUserConversionReportWeekService.get(id);
		}
		if (entity == null){
			entity = new VUserConversionReportWeek();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:vUserConversionReportWeek:view")
	@RequestMapping(value = {"list", ""})
	public String list(VUserConversionReportWeek vUserConversionReportWeek, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VUserConversionReportWeek> page = vUserConversionReportWeekService.findPage(new Page<VUserConversionReportWeek>(request, response), vUserConversionReportWeek); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/vUserConversionReportWeekList";
	}
	
	/**
	 * 导出周数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("weeklyreport:vUserConversionReportWeek:view")
    @RequestMapping(value = "userConversionExportFile", method=RequestMethod.POST)
    public String userConversionExportFile(VUserConversionReportWeek userConversionReportWeek, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "weekReportExportFile" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<VUserConversionReportWeek> weeklist = vUserConversionReportWeekService.findList(userConversionReportWeek); 
            new ExportExcel("用户周报表", VUserConversionReportWeek.class).setDataList(weeklist).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/weeklyreport/vUserConversionReportWeek?repage";
    }

	@RequiresPermissions("weeklyreport:vUserConversionReportWeek:view")
	@RequestMapping(value = "form")
	public String form(VUserConversionReportWeek vUserConversionReportWeek, Model model) {
		model.addAttribute("vUserConversionReportWeek", vUserConversionReportWeek);
		return "modules/weeklyreport/vUserConversionReportWeekForm";
	}


}