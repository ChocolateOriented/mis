/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
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
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportMonth;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportMonthDetail;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderOutReportWeek;
import com.thinkgem.jeesite.modules.weeklyreport.service.SRiskOrderOutReportMonthService;

/**
 * 委外月报表Controller
 * @author 徐盛
 * @version 2016-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/sRiskOrderOutReportMonth")
public class SRiskOrderOutReportMonthController extends BaseController {

	@Autowired
	private SRiskOrderOutReportMonthService sRiskOrderOutReportMonthService;
	
	@ModelAttribute
	public SRiskOrderOutReportMonth get(@RequestParam(required=false) String id) {
		SRiskOrderOutReportMonth entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sRiskOrderOutReportMonthService.get(id);
		}
		if (entity == null){
			entity = new SRiskOrderOutReportMonth();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderOutReportMonth:view")
	@RequestMapping(value = {"list", ""})
	public String list(SRiskOrderOutReportMonth sRiskOrderOutReportMonth, HttpServletRequest request, HttpServletResponse response, Model model) {
		String month = null != sRiskOrderOutReportMonth.getDatetime() ? DateUtils.formatDate(sRiskOrderOutReportMonth.getDatetime() , "yyyyMM") : null;
		sRiskOrderOutReportMonth.setCreatetime(month);
		Page<SRiskOrderOutReportMonth> page = sRiskOrderOutReportMonthService.findPage(new Page<SRiskOrderOutReportMonth>(request, response), sRiskOrderOutReportMonth); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/sRiskOrderOutReportMonthList";
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
    public String exportFile(SRiskOrderOutReportMonth newtest, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "riskOrderOutReportMonth"+DateUtils.getDate("yyyy-MM-ddHHmmss")+".xlsx";
            List<SRiskOrderOutReportMonth> page = sRiskOrderOutReportMonthService.findList(newtest);
    		new ExportExcel("委外月报表", SRiskOrderOutReportMonth.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/weeklyreport/sRiskOrderOutReportMonth/list?repage";
    }
	
	
	@RequiresPermissions("weeklyreport:sRiskOrderOutReportMonth:view")
	@RequestMapping(value = "detail")
	public String detail(SRiskOrderOutReportMonth sRiskOrderOutReportMonth, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SRiskOrderOutReportMonthDetail> list = sRiskOrderOutReportMonthService.findOutReportMonthDetail( sRiskOrderOutReportMonth); 
		model.addAttribute("list", list);
		return "modules/weeklyreport/dialog/dialogOutReportDetail";
	}

	@RequiresPermissions("weeklyreport:sRiskOrderOutReportMonth:view")
	@RequestMapping(value = "form")
	public String form(SRiskOrderOutReportMonth sRiskOrderOutReportMonth, Model model) {
		model.addAttribute("sRiskOrderOutReportMonth", sRiskOrderOutReportMonth);
		return "modules/weeklyreport/sRiskOrderOutReportMonthForm";
	}

	@RequiresPermissions("weeklyreport:sRiskOrderOutReportMonth:edit")
	@RequestMapping(value = "save")
	public String save(SRiskOrderOutReportMonth sRiskOrderOutReportMonth, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sRiskOrderOutReportMonth)){
			return form(sRiskOrderOutReportMonth, model);
		}
		sRiskOrderOutReportMonthService.save(sRiskOrderOutReportMonth);
		addMessage(redirectAttributes, "保存委外月报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderOutReportMonth/?repage";
	}
	
	@RequiresPermissions("weeklyreport:sRiskOrderOutReportMonth:edit")
	@RequestMapping(value = "delete")
	public String delete(SRiskOrderOutReportMonth sRiskOrderOutReportMonth, RedirectAttributes redirectAttributes) {
		sRiskOrderOutReportMonthService.delete(sRiskOrderOutReportMonth);
		addMessage(redirectAttributes, "删除委外月报表成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sRiskOrderOutReportMonth/?repage";
	}

}