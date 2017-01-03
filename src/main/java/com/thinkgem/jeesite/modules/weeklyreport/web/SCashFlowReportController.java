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
import com.thinkgem.jeesite.modules.weeklyreport.entity.SCashFlowReport;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderAfterLoan;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderDailyReport;
import com.thinkgem.jeesite.modules.weeklyreport.service.SCashFlowReportService;

/**
 * 资金流日报Controller
 * @author 徐盛
 * @version 2016-11-18
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/sCashFlowReport")
public class SCashFlowReportController extends BaseController {

	@Autowired
	private SCashFlowReportService sCashFlowReportService;
	
	@ModelAttribute
	public SCashFlowReport get(@RequestParam(required=false) String id) {
		SCashFlowReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sCashFlowReportService.get(id);
		}
		if (entity == null){
			entity = new SCashFlowReport();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:sCashFlowReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(SCashFlowReport sCashFlowReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SCashFlowReport> page = sCashFlowReportService.findPage(new Page<SCashFlowReport>(request, response), sCashFlowReport); 
		model.addAttribute("page", page);
		SCashFlowReport sumCashFlowReport = new SCashFlowReport();
		for(SCashFlowReport report : page.getList()){
			sumCashFlowReport.setPayamount((null != sumCashFlowReport.getPayamount() ? sumCashFlowReport.getPayamount():0)  + (null != report.getPayamount() ? report.getPayamount() : 0) );
			sumCashFlowReport.setPayoffamount((null != sumCashFlowReport.getPayoffamount() ? sumCashFlowReport.getPayoffamount():0) + (null != report.getPayoffamount()? report.getPayoffamount() : 0) );
			sumCashFlowReport.setDiffamount((null != sumCashFlowReport.getDiffamount() ? sumCashFlowReport.getDiffamount():0) + (null != report.getDiffamount()? report.getDiffamount() : 0) );
			sumCashFlowReport.setGpayamount((null != sumCashFlowReport.getGpayamount() ? sumCashFlowReport.getGpayamount():0) + (null != report.getGpayamount()? report.getGpayamount() : 0) );
			sumCashFlowReport.setKdpayamount((null != sumCashFlowReport.getKdpayamount() ? sumCashFlowReport.getKdpayamount():0) + (null != report.getKdpayamount()? report.getKdpayamount() : 0) );
			sumCashFlowReport.setMo9payamount((null != sumCashFlowReport.getMo9payamount() ? sumCashFlowReport.getMo9payamount():0) + (null != report.getMo9payamount()? report.getMo9payamount() : 0) );
			sumCashFlowReport.setManualpayamount((null != sumCashFlowReport.getManualpayamount() ? sumCashFlowReport.getManualpayamount():0) + (null != report.getManualpayamount()? report.getManualpayamount() : 0) );
		}
		model.addAttribute("sumCashFlowReport", sumCashFlowReport);
		return "modules/weeklyreport/sCashFlowReportList";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("repaymentcondition:vRiskOrderAfterLoan:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SCashFlowReport newtest, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "SCashFlowReport"+DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<SCashFlowReport> page = sCashFlowReportService.findList(newtest);
    		new ExportExcel("资金流日报", SCashFlowReport.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/weeklyreport/sCashFlowReport/list?repage";
    }


	@RequiresPermissions("weeklyreport:sCashFlowReport:view")
	@RequestMapping(value = "form")
	public String form(SCashFlowReport sCashFlowReport, Model model) {
		model.addAttribute("sCashFlowReport", sCashFlowReport);
		return "modules/weeklyreport/sCashFlowReportForm";
	}

	@RequiresPermissions("weeklyreport:sCashFlowReport:edit")
	@RequestMapping(value = "save")
	public String save(SCashFlowReport sCashFlowReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sCashFlowReport)){
			return form(sCashFlowReport, model);
		}
		sCashFlowReportService.save(sCashFlowReport);
		addMessage(redirectAttributes, "保存资金流日报成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sCashFlowReport/?repage";
	}
	
	@RequiresPermissions("weeklyreport:sCashFlowReport:edit")
	@RequestMapping(value = "delete")
	public String delete(SCashFlowReport sCashFlowReport, RedirectAttributes redirectAttributes) {
		sCashFlowReportService.delete(sCashFlowReport);
		addMessage(redirectAttributes, "删除资金流日报成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sCashFlowReport/?repage";
	}

}