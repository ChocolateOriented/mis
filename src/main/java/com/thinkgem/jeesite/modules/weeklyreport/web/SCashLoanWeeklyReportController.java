/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.thinkgem.jeesite.modules.weeklyreport.entity.SCashLoanWeeklyReport;
import com.thinkgem.jeesite.modules.weeklyreport.service.SCashLoanWeeklyReportService;
import com.thinkgem.jeesite.util.ListSortUtil;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 现金贷款周报Controller
 * @author 徐盛
 * @version 2016-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/weeklyreport/sCashLoanWeeklyReport")
public class SCashLoanWeeklyReportController extends BaseController {

	@Autowired
	private SCashLoanWeeklyReportService sCashLoanWeeklyReportService;
	
	@ModelAttribute
	public SCashLoanWeeklyReport get(@RequestParam(required=false) String id) {
		SCashLoanWeeklyReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sCashLoanWeeklyReportService.get(id);
		}
		if (entity == null){
			entity = new SCashLoanWeeklyReport();
		}
		return entity;
	}
	
	@RequiresPermissions("weeklyreport:sCashLoanWeeklyReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(SCashLoanWeeklyReport sCashLoanWeeklyReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SCashLoanWeeklyReport> page = sCashLoanWeeklyReportService.findPage(new Page<SCashLoanWeeklyReport>(request, response), sCashLoanWeeklyReport); 
		model.addAttribute("page", page);
		String reportType = request.getParameter("reportType");
		return "modules/weeklyreport/"+reportType;
	}

	@RequiresPermissions("weeklyreport:sCashLoanWeeklyReport:view")
	@RequestMapping(value = "form")
	public String form(SCashLoanWeeklyReport sCashLoanWeeklyReport, Model model) {
		model.addAttribute("sCashLoanWeeklyReport", sCashLoanWeeklyReport);
		return "modules/weeklyreport/sCashLoanWeeklyReportForm";
	}

	@RequiresPermissions("weeklyreport:sCashLoanWeeklyReport:edit")
	@RequestMapping(value = "save")
	public String save(SCashLoanWeeklyReport sCashLoanWeeklyReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sCashLoanWeeklyReport)){
			return form(sCashLoanWeeklyReport, model);
		}
		sCashLoanWeeklyReportService.save(sCashLoanWeeklyReport);
		addMessage(redirectAttributes, "保存现金贷款周报成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sCashLoanWeeklyReport/?repage";
	}
	
	@RequiresPermissions("weeklyreport:sCashLoanWeeklyReport:edit")
	@RequestMapping(value = "delete")
	public String delete(SCashLoanWeeklyReport sCashLoanWeeklyReport, RedirectAttributes redirectAttributes) {
		sCashLoanWeeklyReportService.delete(sCashLoanWeeklyReport);
		addMessage(redirectAttributes, "删除现金贷款周报成功");
		return "redirect:"+Global.getAdminPath()+"/weeklyreport/sCashLoanWeeklyReport/?repage";
	}
	
	
	@RequiresPermissions("weeklyreport:sCashLoanWeeklyReport:charts")
	@RequestMapping(value = {"cumulativeOrdersCharts" })
	public String cashLoanWeeklyReportCharts(SCashLoanWeeklyReport sCashLoanWeeklyReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SCashLoanWeeklyReport> page = sCashLoanWeeklyReportService.findPage(new Page<SCashLoanWeeklyReport>(request, response), sCashLoanWeeklyReport); 
		model.addAttribute("page", page);
		String chartsType = request.getParameter("chartsType");
		return "modules/weeklyreport/"+chartsType;
	}
	
	
	/**
	 * 增幅
	 * @param lastNum
	 * @param num2
	 * @return
	 */
	public String getAmplitudePercentage(double lastNum,double num2){
		String amplitudePercentage = "";
		if(lastNum != 0){
			amplitudePercentage = NumberUtil.getNumberFormatAmplitudePercentage(lastNum,num2);
		}else{
			amplitudePercentage = "-%";
		}
		return amplitudePercentage;
	}
	
	/**
	 * 累计订单数
	 * @return
	 */
	@RequestMapping(value = "findCumulativeOrders")
	@ResponseBody
	public Map<String, List<String>> findCumulativeOrders(HttpServletRequest request, HttpServletResponse response) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			String weekNumber = request.getParameter("weekNumber");
			List<SCashLoanWeeklyReport> list = sCashLoanWeeklyReportService.findOrdernumincludedelay(Integer.parseInt(weekNumber));
			if(null != list && !list.isEmpty()){
//				ListSortUtil<SCashLoanWeeklyReport> sortList = new ListSortUtil<SCashLoanWeeklyReport>();  
//				sortList.sort(list, "intervaldatetime", "asc");  
				List<String> intervaldatetime = new ArrayList<String>();
				List<String> ordernumincludedelay = new ArrayList<String>();
//				List<String> amplitudePercentages = new ArrayList<String>();
				String amplitudePercentage = "";
				double lastNum = 0.0;
				for (SCashLoanWeeklyReport sc : list) {
					amplitudePercentage = getAmplitudePercentage(lastNum, sc.getOrdernumincludedelay());
					lastNum = sc.getOrdernumincludedelay();
					
					intervaldatetime.add(sc.getIntervaldatetime());
					ordernumincludedelay.add(sc.getOrdernumincludedelay().toString());
//					amplitudePercentages.add(amplitudePercentage);
				}
				map.put("intervalDatetime", intervaldatetime);
				map.put("ordernumincludedelay", ordernumincludedelay);
//				map.put("amplitudePercentage", amplitudePercentages);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 新增订单数
	 * @return
	 */
	@RequestMapping(value = "findaddOrders")
	@ResponseBody
	public Map<String, List<String>> findaddOrders(HttpServletRequest request, HttpServletResponse response) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			String weekNumber = request.getParameter("weekNumber");
			List<SCashLoanWeeklyReport> list = sCashLoanWeeklyReportService.findOrdernumUser(Integer.parseInt(weekNumber));
			if(null != list && !list.isEmpty()){
//				ListSortUtil<SCashLoanWeeklyReport> sortList = new ListSortUtil<SCashLoanWeeklyReport>();  
//				sortList.sort(list, "intervaldatetime", "asc");  
				List<String> intervaldatetime = new ArrayList<String>();
				List<String> ordernumnewuser = new ArrayList<String>();
				List<String> ordernumolduserincludedelay = new ArrayList<String>();
				for (SCashLoanWeeklyReport sc : list) {
					intervaldatetime.add(sc.getIntervaldatetime());
					ordernumnewuser.add(sc.getOrdernumnewuser().toString());
					ordernumolduserincludedelay.add(sc.getOrdernumolduserincludedelay().toString());
				}
				map.put("intervalDatetime", intervaldatetime);
				map.put("newuser", ordernumnewuser);
				map.put("olduser", ordernumolduserincludedelay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 订单来源分布
	 * @return
	 */
	@RequestMapping(value = "findsourceOrders")
	@ResponseBody
	public Map<String, List<String>> findsourceOrders(HttpServletRequest request, HttpServletResponse response) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			String weekNumber = request.getParameter("weekNumber");
			List<SCashLoanWeeklyReport> list = sCashLoanWeeklyReportService.findOrdernumneworderperiodpercent(Integer.parseInt(weekNumber));
			if(null != list && !list.isEmpty()){
				ListSortUtil<SCashLoanWeeklyReport> sortList = new ListSortUtil<SCashLoanWeeklyReport>();  
				sortList.sort(list, "intervaldatetime", "asc");  
				double lastNum = 0.0;
				List<String> intervaldatetime = new ArrayList<String>();
				List<String> ordernumneworderperiodapppercentincludedelay = new ArrayList<String>();
				List<String> ordernumneworderperiodwechatpercentincludedelay = new ArrayList<String>();
				for (SCashLoanWeeklyReport sc : list) {
//					amplitudePercentage = getAmplitudePercentage(lastNum, sc.getOrdernumincludedelay());
					intervaldatetime.add(sc.getIntervaldatetime());
					ordernumneworderperiodapppercentincludedelay.add(sc.getOrdernumneworderperiodapppercentincludedelay().toString() + "-" + getAmplitudePercentage(lastNum, sc.getOrdernumneworderperiodapppercentincludedelay()));
					ordernumneworderperiodwechatpercentincludedelay.add(sc.getOrdernumneworderperiodwechatpercentincludedelay().toString());
					lastNum = sc.getOrdernumneworderperiodapppercentincludedelay();
					System.out.println(sc.getIntervaldatetime());
				}
				map.put("intervalDatetime", intervaldatetime);
				map.put("app", ordernumneworderperiodapppercentincludedelay);
				map.put("wechat", ordernumneworderperiodwechatpercentincludedelay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 订单金额分布
	 * @return
	 */
	@RequestMapping(value = "findmoneyOrders")
	@ResponseBody
	public Map<String, List<String>> findmoneyOrders(HttpServletRequest request, HttpServletResponse response) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			String weekNumber = request.getParameter("weekNumber");
			List<SCashLoanWeeklyReport> list = sCashLoanWeeklyReportService.findOrdernumneworderperiodamountpercent(Integer.parseInt(weekNumber));
			if(null != list && !list.isEmpty()){
				ListSortUtil<SCashLoanWeeklyReport> sortList = new ListSortUtil<SCashLoanWeeklyReport>();  
				sortList.sort(list, "intervaldatetime", "asc");  
				List<String> intervaldatetime = new ArrayList<String>();
				List<String> ordernumneworderperiodamount500percentincludedelay = new ArrayList<String>();
				List<String> ordernumneworderperiodamount1000percentincludedelay = new ArrayList<String>();
				List<String> ordernumneworderperiodamount1500percentincludedelay = new ArrayList<String>();
				for (SCashLoanWeeklyReport sc : list) {
					intervaldatetime.add(sc.getIntervaldatetime());
					ordernumneworderperiodamount500percentincludedelay.add(sc.getOrdernumneworderperiodamount500percentincludedelay().toString());
					ordernumneworderperiodamount1000percentincludedelay.add(sc.getOrdernumneworderperiodamount1000percentincludedelay().toString());
					ordernumneworderperiodamount1500percentincludedelay.add(sc.getOrdernumneworderperiodamount1500percentincludedelay().toString());
				}
				map.put("intervalDatetime", intervaldatetime);
				map.put("500p", ordernumneworderperiodamount500percentincludedelay);
				map.put("1000p", ordernumneworderperiodamount1000percentincludedelay);
				map.put("1500p", ordernumneworderperiodamount1500percentincludedelay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 总收益
	 * @return
	 */
	@RequestMapping(value = "findAmountallincome")
	@ResponseBody
	public Map<String, List<String>> findAmountallincome(HttpServletRequest request, HttpServletResponse response) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			String weekNumber = request.getParameter("weekNumber");
			List<SCashLoanWeeklyReport> list = sCashLoanWeeklyReportService.findAmountallincome(Integer.parseInt(weekNumber));
			if(null != list && !list.isEmpty()){
//				ListSortUtil<SCashLoanWeeklyReport> sortList = new ListSortUtil<SCashLoanWeeklyReport>();  
//				sortList.sort(list, "intervaldatetime", "asc");  
				List<String> intervaldatetime = new ArrayList<String>();
				List<String> amountallincome = new ArrayList<String>();
				for (SCashLoanWeeklyReport sc : list) {
					intervaldatetime.add(sc.getIntervaldatetime());
					amountallincome.add(sc.getAmountallincome().toString());
				}
				map.put("intervalDatetime", intervaldatetime);
				map.put("amountallincome", amountallincome);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 累计放款用户数
	 * @return
	 */
	@RequestMapping(value = "findSingleusernum")
	@ResponseBody
	public Map<String, List<String>> findSingleusernum(HttpServletRequest request, HttpServletResponse response) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			String weekNumber = request.getParameter("weekNumber");
			List<SCashLoanWeeklyReport> list = sCashLoanWeeklyReportService.findSingleusernum(Integer.parseInt(weekNumber));
			if(null != list && !list.isEmpty()){
				ListSortUtil<SCashLoanWeeklyReport> sortList = new ListSortUtil<SCashLoanWeeklyReport>();  
				sortList.sort(list, "intervaldatetime", "asc");  
				List<String> intervaldatetime = new ArrayList<String>();
				List<String> singleusernum = new ArrayList<String>();
				for (SCashLoanWeeklyReport sc : list) {
					intervaldatetime.add(sc.getIntervaldatetime());
					singleusernum.add(sc.getSingleusernum().toString());
				}
				map.put("intervalDatetime", intervaldatetime);
				map.put("singleusernum", singleusernum);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
//	/**
//	 * 订单来源分布
//	 * @return
//	 */
//	@RequestMapping(value = "findsourceOrders")
//	@ResponseBody
//	public Map<String, Object> findsourceOrders(HttpServletRequest request, HttpServletResponse response) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			String weekNumber = request.getParameter("weekNumber");
//			List<SCashLoanWeeklyReport> list = sCashLoanWeeklyReportService.findOrdernumneworderperiodpercent(Integer.parseInt(weekNumber));
//			if(null != list && !list.isEmpty()){
//				ListSortUtil<SCashLoanWeeklyReport> sortList = new ListSortUtil<SCashLoanWeeklyReport>();  
//				sortList.sort(list, "intervaldatetime", "asc");  
//				double lastNum = 0.0;
//				List<String> intervaldatetime = new ArrayList<String>();
//				Map<String, List<String>> appmap = new HashMap<String, List<String>>();
//				List<String> ordernumneworderperiodapppercentincludedelay = new ArrayList<String>();
//				List<String> ordernumneworderperiodapppercentincludedelayPercentage = new ArrayList<String>();
//				List<String> ordernumneworderperiodwechatpercentincludedelay = new ArrayList<String>();
//				for (SCashLoanWeeklyReport sc : list) {
////					amplitudePercentage = getAmplitudePercentage(lastNum, sc.getOrdernumincludedelay());
//					intervaldatetime.add(sc.getIntervaldatetime());
//					
//					ordernumneworderperiodapppercentincludedelay.add(sc.getOrdernumneworderperiodapppercentincludedelay().toString());
//					ordernumneworderperiodapppercentincludedelayPercentage.add(getAmplitudePercentage(lastNum, sc.getOrdernumneworderperiodapppercentincludedelay()));
//					
//					ordernumneworderperiodwechatpercentincludedelay.add(sc.getOrdernumneworderperiodwechatpercentincludedelay().toString());
//					lastNum = sc.getOrdernumneworderperiodapppercentincludedelay();
//					
//				}
//				appmap.put("1", ordernumneworderperiodapppercentincludedelay);
//				appmap.put("2", ordernumneworderperiodapppercentincludedelayPercentage);
//				
//				map.put("intervalDatetime", intervaldatetime);
//				map.put("app", ordernumneworderperiodapppercentincludedelay);
//				map.put("appces", ordernumneworderperiodapppercentincludedelayPercentage);
//				map.put("appPercentage", appmap);
//				map.put("wechat", ordernumneworderperiodwechatpercentincludedelay);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return map;
//	}
	

}