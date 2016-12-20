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
import com.thinkgem.jeesite.modules.weeklyreport.entity.VUserConversionReportMonth;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VUserConversionReportWeek;
import com.thinkgem.jeesite.modules.weeklyreport.service.VUserConversionReportMonthService;

/**
 * 用户月报表Controller
 * @author 徐盛
 * @version 2016-07-21
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/vUserConversionReportMonth")
public class VUserConversionReportMonthController extends BaseController {

	@Autowired
	private VUserConversionReportMonthService vUserConversionReportMonthService;
	
	@ModelAttribute
	public VUserConversionReportMonth get(@RequestParam(required=false) String id) {
		VUserConversionReportMonth entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = vUserConversionReportMonthService.get(id);
		}
		if (entity == null){
			entity = new VUserConversionReportMonth();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:vUserConversionReportMonth:view")
	@RequestMapping(value = {"list", ""})
	public String list(VUserConversionReportMonth vUserConversionReportMonth, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VUserConversionReportMonth> page = vUserConversionReportMonthService.findPage(new Page<VUserConversionReportMonth>(request, response), vUserConversionReportMonth); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/vUserConversionReportMonthList";
	}


	/**
	 * 导出周数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("weeklyreport:vUserConversionReportMonth:view")
    @RequestMapping(value = "userMonthConversionExportFile", method=RequestMethod.POST)
    public String userMonthConversionExportFile(VUserConversionReportMonth userConversionReportMonth, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "monthReportExportFile" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<VUserConversionReportMonth> monthlist = vUserConversionReportMonthService.findList(userConversionReportMonth); 
            new ExportExcel("用户月报表", VUserConversionReportMonth.class).setDataList(monthlist).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/weeklyreport/vUserConversionReportMonth?repage";
    }
	
	@RequiresPermissions("weeklyreport:vUserConversionReportMonth:view")
	@RequestMapping(value = "form")
	public String form(VUserConversionReportMonth vUserConversionReportMonth, Model model) {
		model.addAttribute("vUserConversionReportMonth", vUserConversionReportMonth);
		return "modules/weeklyreport/vUserConversionReportMonthForm";
	}

	@RequiresPermissions("weeklyreport:vUserConversionReportMonth:edit")
	@RequestMapping(value = "save")
	public String save(VUserConversionReportMonth vUserConversionReportMonth, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, vUserConversionReportMonth)){
			return form(vUserConversionReportMonth, model);
		}
		vUserConversionReportMonthService.save(vUserConversionReportMonth);
		addMessage(redirectAttributes, "保存用户月报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/vUserConversionReportMonth/?repage";
	}
	
	@RequiresPermissions("weeklyreport:vUserConversionReportMonth:edit")
	@RequestMapping(value = "delete")
	public String delete(VUserConversionReportMonth vUserConversionReportMonth, RedirectAttributes redirectAttributes) {
		vUserConversionReportMonthService.delete(vUserConversionReportMonth);
		addMessage(redirectAttributes, "删除用户月报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/vUserConversionReportMonth/?repage";
	}

}