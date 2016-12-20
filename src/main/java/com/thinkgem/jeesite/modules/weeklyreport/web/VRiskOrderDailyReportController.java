/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.web;

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
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderDailyReport;
import com.thinkgem.jeesite.modules.weeklyreport.service.VRiskOrderDailyReportService;

/**
 * 现金贷日报表Controller
 * @author 徐盛
 * @version 2016-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/dailyreport/vRiskOrderDailyReport")
public class VRiskOrderDailyReportController extends BaseController {

	@Autowired
	private VRiskOrderDailyReportService vRiskOrderDailyReportService;
	
	@ModelAttribute
	public VRiskOrderDailyReport get(@RequestParam(required=false) String id) {
		VRiskOrderDailyReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = vRiskOrderDailyReportService.get(id);
		}
		if (entity == null){
			entity = new VRiskOrderDailyReport();
		}
		return entity;
	}
	
	@RequiresPermissions("dailyreport:vRiskOrderDailyReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(VRiskOrderDailyReport vRiskOrderDailyReport, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<VRiskOrderDailyReport> page = vRiskOrderDailyReportService.findPage(new Page<VRiskOrderDailyReport>(request, response), vRiskOrderDailyReport); 
		Page<VRiskOrderDailyReport> page = vRiskOrderDailyReportService.findPage(new Page<VRiskOrderDailyReport>(request, response, 31), vRiskOrderDailyReport); 
		VRiskOrderDailyReport sumReport = new VRiskOrderDailyReport();
		for(VRiskOrderDailyReport dailyReport : page.getList()){
			sumReport.setNewusernum((null != sumReport.getNewusernum() ? sumReport.getNewusernum():0)  + dailyReport.getNewusernum());
			sumReport.setNewuserordernum((null != sumReport.getNewuserordernum() ? sumReport.getNewuserordernum():0) + dailyReport.getNewuserordernum());
			sumReport.setNewuserwaitremitnum((null != sumReport.getNewuserwaitremitnum() ? sumReport.getNewuserwaitremitnum():0) + dailyReport.getNewuserwaitremitnum());
			sumReport.setOlduserordernum((null != sumReport.getOlduserordernum() ? sumReport.getOlduserordernum():0) + dailyReport.getOlduserordernum());
			sumReport.setPlatformappnum((null != sumReport.getPlatformappnum() ? sumReport.getPlatformappnum():0) + dailyReport.getPlatformappnum());
			sumReport.setPlatformwechatnum((null != sumReport.getPlatformwechatnum() ? sumReport.getPlatformwechatnum():0) + dailyReport.getPlatformwechatnum());
			sumReport.setRemitordernum((null != sumReport.getRemitordernum() ? sumReport.getRemitordernum():0) + dailyReport.getRemitordernum());
			sumReport.setExpireordernum((null != sumReport.getExpireordernum() ? sumReport.getExpireordernum():0) + dailyReport.getExpireordernum());
			sumReport.setPayoffordernum((null != sumReport.getPayoffordernum() ? sumReport.getPayoffordernum():0) + dailyReport.getPayoffordernum());
			sumReport.setAmountincome((null != sumReport.getAmountincome() ? sumReport.getAmountincome():0) + dailyReport.getAmountincome());
			sumReport.setOverdueincome((null != sumReport.getOverdueincome() ? sumReport.getOverdueincome():0) + dailyReport.getOverdueincome());
			sumReport.setPendingordernum((null != sumReport.getPendingordernum() ? sumReport.getPendingordernum():0) + dailyReport.getPendingordernum());
		}
		model.addAttribute("sumReport", sumReport);
		model.addAttribute("page", page);
		return "modules/weeklyreport/vRiskOrderDailyReportList";
	}

	@RequiresPermissions("dailyreport:vRiskOrderDailyReport:view")
	@RequestMapping(value = "form")
	public String form(VRiskOrderDailyReport vRiskOrderDailyReport, Model model) {
		model.addAttribute("vRiskOrderDailyReport", vRiskOrderDailyReport);
		return "modules/weeklyreport/vRiskOrderDailyReportForm";
	}

	@RequiresPermissions("dailyreport:vRiskOrderDailyReport:edit")
	@RequestMapping(value = "save")
	public String save(VRiskOrderDailyReport vRiskOrderDailyReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, vRiskOrderDailyReport)){
			return form(vRiskOrderDailyReport, model);
		}
		vRiskOrderDailyReportService.save(vRiskOrderDailyReport);
		addMessage(redirectAttributes, "保存现金贷日报表成功");
		return "redirect:"+Global.getAdminPath()+"/dailyreport/vRiskOrderDailyReport/?repage";
	}
	
	@RequiresPermissions("dailyreport:vRiskOrderDailyReport:edit")
	@RequestMapping(value = "delete")
	public String delete(VRiskOrderDailyReport vRiskOrderDailyReport, RedirectAttributes redirectAttributes) {
		vRiskOrderDailyReportService.delete(vRiskOrderDailyReport);
		addMessage(redirectAttributes, "删除现金贷日报表成功");
		return "redirect:"+Global.getAdminPath()+"/dailyreport/vRiskOrderDailyReport/?repage";
	}

	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("new2:newtest:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(VRiskOrderDailyReport riskOrderDailyReport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<VRiskOrderDailyReport> page = vRiskOrderDailyReportService.findPage(new Page<VRiskOrderDailyReport>(request, response, -1), riskOrderDailyReport);
    		new ExportExcel("用户数据", VRiskOrderDailyReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/dailyreport/vRiskOrderDailyReport/?repage";
    }

	
	
}