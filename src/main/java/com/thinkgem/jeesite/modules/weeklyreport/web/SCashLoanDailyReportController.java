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
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportOrderMonthBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportOrderWeekBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportRepayMonthBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportRepayWeekBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanMonthReportBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanWeekReportBean;
import com.thinkgem.jeesite.modules.weeklyreport.entity.SCashLoanDailyReport;
import com.thinkgem.jeesite.modules.weeklyreport.service.SCashLoanDailyReportService;

/**
 * 财务日报Controller
 * @author 徐盛
 * @version 2016-06-23
 */
@Controller
@RequestMapping(value = "${adminPath}/financdailyreport/sCashLoanDailyReport")
public class SCashLoanDailyReportController extends BaseController {

	@Autowired
	private SCashLoanDailyReportService sCashLoanDailyReportService;
	
	@ModelAttribute
	public SCashLoanDailyReport get(@RequestParam(required=false) String id) {
		SCashLoanDailyReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sCashLoanDailyReportService.get(id);
		}
		if (entity == null){
			entity = new SCashLoanDailyReport();
		}
		return entity;
	}
	
//	@ModelAttribute
//	public SCashLoanDailyReport get(@RequestParam(required=false) SCashLoanDailyReport cashLoanDailyReport) {
////		SCashLoanDailyReport entity = null;
//		SCashLoanDailyReport entity = sCashLoanDailyReportService.get(cashLoanDailyReport);
//		return entity;
//	}
	
	/**
	 * 导出周数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:weekview")
    @RequestMapping(value = "weekReportExportFile", method=RequestMethod.POST)
    public String weekReportExportFile(SCashLoanWeekReportBean sCashLoanWeekReportBean, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "weekReportExportFile" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<SCashLoanWeekReportBean> weeklist = sCashLoanDailyReportService.weekReportExportFile(sCashLoanWeekReportBean); 
            new ExportExcel("财务周报表", SCashLoanWeekReportBean.class).setDataList(weeklist).write(response, fileName).dispose();
//            List<SCashLoanDailyReport> monthlist = sCashLoanDailyReportService.findmonthList(sCashLoanDailyReport); 
//            new ExportExcel("贷后还款情况报表", DunningOrder.class).setDataList(monthlist).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/financdailyreport/sCashLoanDailyReport/weeklist?repage";
    }
	
	/**
	 * 导出月数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:monthview")
    @RequestMapping(value = "monthReportExportFile", method=RequestMethod.POST)
    public String monthReportExportFile(SCashLoanMonthReportBean sCashLoanMonthReportBean, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "monthReportExportFile" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<SCashLoanMonthReportBean> monthlist = sCashLoanDailyReportService.monthReportExportFile(sCashLoanMonthReportBean); 
            new ExportExcel("财务月报表", SCashLoanMonthReportBean.class).setDataList(monthlist).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/financdailyreport/sCashLoanDailyReport/monthlist?repage";
    }
	
	/**
	 * 日
	 * @param sCashLoanDailyReport
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:dailyview")
	@RequestMapping(value = {"list", ""})
	public String list(SCashLoanDailyReport sCashLoanDailyReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SCashLoanDailyReport> page = sCashLoanDailyReportService.findPage(new Page<SCashLoanDailyReport>(request, response), sCashLoanDailyReport); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/sCashLoanDailyReportList";
	}
	/**
	 * 周
	 * @param sCashLoanDailyReport
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:weekview")
	@RequestMapping(value = {"weeklist"})
	public String weeklist(SCashLoanDailyReport sCashLoanDailyReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SCashLoanDailyReport> list = sCashLoanDailyReportService.findweekList(sCashLoanDailyReport); 
		model.addAttribute("weeklist", list);
		SCashLoanWeekReportBean sumWeekReportBean = new SCashLoanWeekReportBean();
		for(SCashLoanDailyReport bean : list){
			sumWeekReportBean.setRemitamount((null != sumWeekReportBean.getRemitamount() ? sumWeekReportBean.getRemitamount():0)  + (null != bean.getRemitamount() ? bean.getRemitamount():0));
			sumWeekReportBean.setNewuser((null != sumWeekReportBean.getNewuser() ? sumWeekReportBean.getNewuser():0)  +(null != bean.getNewuser() ? bean.getNewuser():0) );
			sumWeekReportBean.setNeworder((null != sumWeekReportBean.getNeworder() ? sumWeekReportBean.getNeworder():0)  + (null != bean.getNeworder() ? bean.getNeworder():0));
			sumWeekReportBean.setIncomeamount((null != sumWeekReportBean.getIncomeamount() ? sumWeekReportBean.getIncomeamount():0)  + (null != bean.getIncomeamount() ? bean.getIncomeamount():0));
			sumWeekReportBean.setLoancost((null != sumWeekReportBean.getLoancost() ? sumWeekReportBean.getLoancost():0)  +(null != bean.getLoancost() ? bean.getLoancost():0) );
			sumWeekReportBean.setRepaycost((null != sumWeekReportBean.getRepaycost() ? sumWeekReportBean.getRepaycost():0)  + (null != bean.getRepaycost() ? bean.getRepaycost():0));
			sumWeekReportBean.setCreditsumcost((null != sumWeekReportBean.getCreditsumcost() ? sumWeekReportBean.getCreditsumcost():0)  + (null != bean.getCreditsumcost() ? bean.getCreditsumcost():0));
			sumWeekReportBean.setCouponcost((null != sumWeekReportBean.getCouponcost() ? sumWeekReportBean.getCouponcost():0)  + (null != bean.getCouponcost() ? bean.getCouponcost():0));
			sumWeekReportBean.setMediacost((null != sumWeekReportBean.getMediacost() ? sumWeekReportBean.getMediacost():0)  +(null != bean.getMediacost() ? bean.getMediacost():0) );
			sumWeekReportBean.setDebatamount((null != sumWeekReportBean.getDebatamount() ? sumWeekReportBean.getDebatamount():0)  +(null != bean.getDebatamount() ? bean.getDebatamount():0) );
			sumWeekReportBean.setWeekdebatamount((null != sumWeekReportBean.getWeekdebatamount() ? sumWeekReportBean.getWeekdebatamount():0)  +(null != bean.getWeekdebatamount() ? bean.getWeekdebatamount():0) );
			sumWeekReportBean.setGrossprofile((null != sumWeekReportBean.getGrossprofile() ? sumWeekReportBean.getGrossprofile():0)  +(null != bean.getGrossprofile() ? bean.getGrossprofile():0));
			sumWeekReportBean.setWeekSingleRemitAvgAmount((null != sumWeekReportBean.getWeekSingleRemitAvgAmount() ? sumWeekReportBean.getWeekSingleRemitAvgAmount():0)  +(null != bean.getWeekSingleRemitAvgAmount() ? bean.getWeekSingleRemitAvgAmount():0) );
			sumWeekReportBean.setIncomepercent((null != sumWeekReportBean.getIncomepercent() ? sumWeekReportBean.getIncomepercent():0)  +(null != bean.getIncomepercent() ? bean.getIncomepercent():0) );
			sumWeekReportBean.setCreditavgcost((null != sumWeekReportBean.getCreditavgcost() ? sumWeekReportBean.getCreditavgcost():0)  + (null != bean.getCreditavgcost() ? bean.getCreditavgcost():0));
			sumWeekReportBean.setCashcostamount((null != sumWeekReportBean.getCashcostamount() ? sumWeekReportBean.getCashcostamount():0)  + (null != bean.getCashcostamount() ? bean.getCashcostamount():0));
			sumWeekReportBean.setDebatreturnamount((null != sumWeekReportBean.getDebatreturnamount() ? sumWeekReportBean.getDebatreturnamount():0)  + (null != bean.getDebatreturnamount() ? bean.getDebatreturnamount():0));
		}
		model.addAttribute("sumWeekReportBean", sumWeekReportBean);
		return "modules/weeklyreport/sCashLoanWeekReportList";
	}
	/**
	 * 月
	 * @param sCashLoanDailyReport
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:monthview")
	@RequestMapping(value = {"monthlist"})
	public String monthlist(SCashLoanDailyReport sCashLoanDailyReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SCashLoanDailyReport> list = sCashLoanDailyReportService.findmonthList(sCashLoanDailyReport); 
		model.addAttribute("monthlist", list);
		SCashLoanMonthReportBean sumMonthReportBean = new SCashLoanMonthReportBean();
		for(SCashLoanDailyReport bean : list){
			sumMonthReportBean.setRemitamount((null != sumMonthReportBean.getRemitamount() ? sumMonthReportBean.getRemitamount():0)  + (null != bean.getRemitamount() ? bean.getRemitamount():0));
			sumMonthReportBean.setNewuser((null != sumMonthReportBean.getNewuser() ? sumMonthReportBean.getNewuser():0)  + (null != bean.getNewuser() ? bean.getNewuser():0));
			sumMonthReportBean.setNeworder((null != sumMonthReportBean.getNeworder() ? sumMonthReportBean.getNeworder():0)  + (null != bean.getNeworder() ? bean.getNeworder():0));
			sumMonthReportBean.setIncomeamount((null != sumMonthReportBean.getIncomeamount() ? sumMonthReportBean.getIncomeamount():0)  + (null != bean.getIncomeamount() ? bean.getIncomeamount():0));
			sumMonthReportBean.setLoancost((null != sumMonthReportBean.getLoancost() ? sumMonthReportBean.getLoancost():0)  +(null != bean.getLoancost() ? bean.getLoancost():0) );
			sumMonthReportBean.setRepaycost((null != sumMonthReportBean.getRepaycost() ? sumMonthReportBean.getRepaycost():0)  + (null != bean.getRepaycost() ? bean.getRepaycost():0));
			sumMonthReportBean.setCreditsumcost((null != sumMonthReportBean.getCreditsumcost() ? sumMonthReportBean.getCreditsumcost():0)  + (null != bean.getCreditsumcost() ? bean.getCreditsumcost():0));
			sumMonthReportBean.setCouponcost((null != sumMonthReportBean.getCouponcost() ? sumMonthReportBean.getCouponcost():0)  +(null != bean.getCouponcost() ? bean.getCouponcost():0) );
			sumMonthReportBean.setMediacost((null != sumMonthReportBean.getMediacost() ? sumMonthReportBean.getMediacost():0)  +(null != bean.getMediacost() ? bean.getMediacost():0) );
			sumMonthReportBean.setDebatamount((null != sumMonthReportBean.getDebatamount() ? sumMonthReportBean.getDebatamount():0)  + (null != bean.getDebatamount() ? bean.getDebatamount():0));
			sumMonthReportBean.setGrossprofile((null != sumMonthReportBean.getGrossprofile() ? sumMonthReportBean.getGrossprofile():0)  + (null != bean.getGrossprofile() ? bean.getGrossprofile():0));
			sumMonthReportBean.setMonthincomepercent((null != sumMonthReportBean.getMonthincomepercent() ? sumMonthReportBean.getMonthincomepercent():0)  + (null != bean.getMonthincomepercent() ? bean.getMonthincomepercent():0));
			sumMonthReportBean.setMonthdebatamount((null != sumMonthReportBean.getMonthdebatamount() ? sumMonthReportBean.getMonthdebatamount():0)  + (null != bean.getMonthdebatamount() ? bean.getMonthdebatamount():0));
			sumMonthReportBean.setMonthSingleRemitAvgAmount((null != sumMonthReportBean.getMonthSingleRemitAvgAmount() ? sumMonthReportBean.getMonthSingleRemitAvgAmount():0)  +(null != bean.getMonthSingleRemitAvgAmount() ? bean.getMonthSingleRemitAvgAmount():0) );
			sumMonthReportBean.setMonthCreditAvgCost((null != sumMonthReportBean.getMonthCreditAvgCost() ? sumMonthReportBean.getMonthCreditAvgCost():0)  +(null != bean.getMonthCreditAvgCost() ? bean.getMonthCreditAvgCost():0) );
			sumMonthReportBean.setEntrustcommission((null != sumMonthReportBean.getEntrustcommission() ? sumMonthReportBean.getEntrustcommission():0)  + (null != bean.getEntrustcommission() ? bean.getEntrustcommission():0));
			sumMonthReportBean.setCashcostamount((null != sumMonthReportBean.getCashcostamount() ? sumMonthReportBean.getCashcostamount():0)  + (null != bean.getCashcostamount() ? bean.getCashcostamount():0));
			sumMonthReportBean.setDebatreturnamount((null != sumMonthReportBean.getDebatreturnamount() ? sumMonthReportBean.getDebatreturnamount():0)  + (null != bean.getDebatreturnamount() ? bean.getDebatreturnamount():0));
		}
		model.addAttribute("sumMonthReportBean", sumMonthReportBean);
		return "modules/weeklyreport/sCashLoanMonthReportList";
	}

	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:dailyview")
	@RequestMapping(value = "form")
	public String form(SCashLoanDailyReport sCashLoanDailyReport, Model model) {
		model.addAttribute("sCashLoanDailyReport", sCashLoanDailyReport);
		return "modules/weeklyreport/sCashLoanDailyReportForm";
	}

	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:edit")
	@RequestMapping(value = "save")
	public String save(SCashLoanDailyReport sCashLoanDailyReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sCashLoanDailyReport)){
			return form(sCashLoanDailyReport, model);
		}
		sCashLoanDailyReportService.save(sCashLoanDailyReport);
		addMessage(redirectAttributes, "保存财务日报成功");
		return "redirect:"+Global.getAdminPath()+"/financdailyreport/sCashLoanDailyReport/?repage";
	}
	
	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:delete")
	@RequestMapping(value = "delete")
	public String delete(SCashLoanDailyReport sCashLoanDailyReport, RedirectAttributes redirectAttributes) {
		sCashLoanDailyReportService.delete(sCashLoanDailyReport);
		addMessage(redirectAttributes, "删除财务日报成功");
		return "redirect:"+Global.getAdminPath()+"/financdailyreport/sCashLoanDailyReport/?repage";
	}
	
	
	/**
	 * 周报详情
	 * @param request
	 * @param sCashLoanDailyReport
	 * @param model
	 * @return
	 */
	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:weekdetails")
	@RequestMapping(value = "weekdetails")
	public String weekdetails(HttpServletRequest request,SCashLoanDailyReport sCashLoanDailyReport, Model model) {
		String url = "modules/weeklyreport/dialog/dialog";
		String type = request.getParameter("type");
		SCashLoanDailyReportOrderWeekBean weekBean = null;
		if(("Remitamount").equals(type)){
			weekBean = sCashLoanDailyReportService.findWeekDetails(sCashLoanDailyReport.getWeek());
        	model.addAttribute("detailsBean", weekBean);
        	return url + type;
		}
		if(("Newuser").equals(type)){
			weekBean = sCashLoanDailyReportService.findWeekDetails(sCashLoanDailyReport.getWeek());
        	model.addAttribute("detailsBean", weekBean);
        	return url + type;
		}
		if(("Neworder").equals(type)){
			weekBean = sCashLoanDailyReportService.findWeekDetails(sCashLoanDailyReport.getWeek());
        	model.addAttribute("detailsBean", weekBean);
        	return url + type;
		}
		if(("Couponcost").equals(type)){
			weekBean = sCashLoanDailyReportService.findWeekDetails(sCashLoanDailyReport.getWeek());
        	model.addAttribute("detailsBean", weekBean);
        	return url + type;
		}
		if(("Incomeamount").equals(type)){
			weekBean = sCashLoanDailyReportService.findWeekDetails(sCashLoanDailyReport.getWeek());
        	model.addAttribute("detailsBean", weekBean);
        	return url + type;
		}
		if(("Repaycost").equals(type)){
			List<SCashLoanDailyReportRepayWeekBean> repayweekBeans = sCashLoanDailyReportService.findWeekRepayDetails(sCashLoanDailyReport.getWeek());
        	model.addAttribute("repayBeans", repayweekBeans);
        	return url + type;
		}
		return "error/404";
	}
	
	/**
	 * 月报详情
	 * @param request
	 * @param sCashLoanDailyReport
	 * @param model
	 * @return
	 */
	@RequiresPermissions("financdailyreport:sCashLoanDailyReport:monthdetails")
	@RequestMapping(value = "monthdetails")
	public String monthdetails(HttpServletRequest request,SCashLoanDailyReport sCashLoanDailyReport, Model model) {
		String url = "modules/weeklyreport/dialog/dialog";
		String type = request.getParameter("type");
		SCashLoanDailyReportOrderMonthBean monthBean = null;
		if(("Remitamount").equals(type)){
        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
        	model.addAttribute("detailsBean", monthBean);
        	return url + type;
		}
		if(("Newuser").equals(type)){
        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
        	model.addAttribute("detailsBean", monthBean);
        	return url + type;
		}
		if(("Neworder").equals(type)){
        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
        	model.addAttribute("detailsBean", monthBean);
        	return url + type;
		}
		if(("Couponcost").equals(type)){
        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
        	model.addAttribute("detailsBean", monthBean);
        	return url + type;
		}
		if(("Incomeamount").equals(type)){
        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
        	model.addAttribute("detailsBean", monthBean);
        	return url + type;
		}
		if(("Repaycost").equals(type)){
        	List<SCashLoanDailyReportRepayMonthBean> repayMonthBeans = sCashLoanDailyReportService.findMonthRepayDetails(sCashLoanDailyReport.getMonth());
        	model.addAttribute("repayBeans", repayMonthBeans);
        	return url + type;
		}
		return "error/404";
//		switch (type) {
//	        case "Remitamount":
//	        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
//	        	model.addAttribute("detailsBean", monthBean);
//	        	return url + type;
//	        case "Newuser":
//	        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
//	        	model.addAttribute("detailsBean", monthBean);
//	        	return url + type;
//	        case "Neworder":
//	        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
//	        	model.addAttribute("detailsBean", monthBean);
//	        	return url + type;
//	        case "Couponcost":
//	        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
//	        	model.addAttribute("detailsBean", monthBean);
//	        	return url + type;
//	        case "Incomeamount":
//	        	monthBean = sCashLoanDailyReportService.findMonthDetails(sCashLoanDailyReport.getMonth());
//	        	model.addAttribute("detailsBean", monthBean);
//	        	return url + type;
//	        	
//	        case "Repaycost":
//	        	List<SCashLoanDailyReportRepayMonthBean> repayMonthBeans = sCashLoanDailyReportService.findMonthRepayDetails(sCashLoanDailyReport.getMonth());
//	        	model.addAttribute("repayBeans", repayMonthBeans);
//	        	return url + type;
//	        	
//	        default :
//	        	return "error/404";
//        }
	}
	
	

}