/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanMonthReportBean;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanWeekReportBean;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.alibaba.fastjson.JSON;
import com.mo9.risk.modules.dunning.entity.TMisMigrationData;
import com.mo9.risk.modules.dunning.entity.TMisMigrationData;
import com.mo9.risk.modules.dunning.entity.TMisMigrationRateReport;
import com.mo9.risk.modules.dunning.service.TMisMigrationRateReportService;

/**
 * 迁徙率Controller
 * @author 徐盛
 * @version 2017-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisMigrationRateReport")
public class TMisMigrationRateReportController extends BaseController {

	@Autowired
	private TMisMigrationRateReportService tMisMigrationRateReportService;
	
	@ModelAttribute
	public TMisMigrationRateReport get(@RequestParam(required=false) String id) {
		TMisMigrationRateReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisMigrationRateReportService.get(id);
		}
		if (entity == null){
			entity = new TMisMigrationRateReport();
		}
		return entity;
	}
	/**
	 * 迁徙率数据的日报
	 */
	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisMigrationRateReport tMisMigrationRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisMigrationRateReport> page = tMisMigrationRateReportService.findPage(new Page<TMisMigrationRateReport>(request, response), tMisMigrationRateReport); 
		model.addAttribute("page", page);
		return "modules/dunning/tMisMigrationRateReportList";
	}

	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = "form")
	public String form(TMisMigrationRateReport tMisMigrationRateReport, Model model) {
		model.addAttribute("tMisMigrationRateReport", tMisMigrationRateReport);
		return "modules/dunning/tMisMigrationRateReportForm";
	}

	@RequiresPermissions("dunning:tMisMigrationRateReport:edit")
	@RequestMapping(value = "save")
	public String save(TMisMigrationRateReport tMisMigrationRateReport, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisMigrationRateReport)){
			return form(tMisMigrationRateReport, model);
		}
		tMisMigrationRateReportService.save(tMisMigrationRateReport);
		addMessage(redirectAttributes, "保存迁徙率成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisMigrationRateReport/?repage";
	}
	
//	@RequiresPermissions("dunning:tMisMigrationRateReport:edit")
//	@RequestMapping(value = "delete")
//	public String delete(TMisMigrationRateReport tMisMigrationRateReport, RedirectAttributes redirectAttributes) {
//		tMisMigrationRateReportService.delete(tMisMigrationRateReport);
//		addMessage(redirectAttributes, "删除迁徙率成功");
//		return "redirect:"+Global.getAdminPath()+"/dunning/tMisMigrationRateReport/?repage";
//	}
	
	/**
	 * 跳转迁徙率数据户数图表页面
	 */
	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = "migratechart")
	public String migratechart(TMisMigrationRateReport tMisMigrationRateReport, Model model) {
		return "modules/dunning/tMisMigrationRateChart";
	}

	/**
	 * 获取户数迁徙率户数的数据
	 */
	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = "migrationGetdata")
	@ResponseBody
	public Map<String, Object> migrationGetData(TMisMigrationRateReport tMisMigrationRateReport) {
		
		
		//获取户数迁徙
		Map<String, Object> migrationData=new HashMap<String, Object>();
		
		//横坐标集合（1-16）
		List<Integer>  cycle=new ArrayList<Integer>();
		for (int i = 1; i < 17; i++) {
			cycle.add(i);
		}
		migrationData.put("cycle", cycle);
		
		//获取4个队列数据数据
		List<TMisMigrationRateReport> findMigrateChartList = tMisMigrationRateReportService.findMigrateChartList(tMisMigrationRateReport);
		List<TMisMigrationData>  smdList1=new ArrayList<TMisMigrationData>();
		List<TMisMigrationData>  smdList2=new ArrayList<TMisMigrationData>();
		List<TMisMigrationData>  smdList3=new ArrayList<TMisMigrationData>();
		List<TMisMigrationData>  smdList4=new ArrayList<TMisMigrationData>();
		List<String>  qtime1=new ArrayList<String>();
		List<String>  qtime2=new ArrayList<String>();
		List<String>  qtime3=new ArrayList<String>();
		List<String>  qtime4=new ArrayList<String>();
		SimpleDateFormat sd=new SimpleDateFormat("YYYYMMdd");
		int i=0;
   mrfor:for (int j = 0; j < 5; j++) {
			
			
				TMisMigrationData smd1=new TMisMigrationData();
				List<BigDecimal>  cycleTime1=new ArrayList<BigDecimal>();
				
				TMisMigrationData smd2=null;
				List<BigDecimal>  cycleTime2=null;
				if(j<4){
					smd2=new TMisMigrationData();
					cycleTime2=new ArrayList<BigDecimal>();
				}
				
				TMisMigrationData smd3=null;
				List<BigDecimal>  cycleTime3=null;
				if(j<3){
					smd3=new TMisMigrationData();
					cycleTime3=new ArrayList<BigDecimal>();
				}
				
				TMisMigrationData smd4=null;
				List<BigDecimal>  cycleTime4=null;
				if(j<2){
					smd4=new TMisMigrationData();
					cycleTime4=new ArrayList<BigDecimal>();
				}
				
				
				int y=0;
			while( findMigrateChartList.size()>i){
				if(y==0){
					cycleTime1.add(findMigrateChartList.get(i).getCp1corpus());
					if(null!=cycleTime2)
					cycleTime2.add(findMigrateChartList.get(i).getCp2corpus());
					if(null!=cycleTime3)
					cycleTime3.add(findMigrateChartList.get(i).getCp3corpus());
					if(null!=cycleTime4)
					cycleTime4.add(findMigrateChartList.get(i).getCp4corpus());
				}
				if(y>=1){
					if(findMigrateChartList.get(i).getCycle().equals(findMigrateChartList.get(i-1).getCycle())){
						
						cycleTime1.add(findMigrateChartList.get(i).getCp1corpus());
						if(null!=cycleTime2)
						cycleTime2.add(findMigrateChartList.get(i).getCp2corpus());
						if(null!=cycleTime3)
						cycleTime3.add(findMigrateChartList.get(i).getCp3corpus());
						if(null!=cycleTime4)
						cycleTime4.add(findMigrateChartList.get(i).getCp4corpus());
						
					}else{
						String start = sd.format(findMigrateChartList.get(i-1).getDatetimeStart());
						String end = sd.format(findMigrateChartList.get(i-1).getDatetimeEnd());
						
						smd1.setData(cycleTime1);
						smd1.setName(start+"-"+end);
						smdList1.add(smd1);
						qtime1.add(start+"-"+end);
						
						if(null!=smd2){
							smd2.setData(cycleTime2);
							smd2.setName(start+"-"+end);
							smdList2.add(smd2);
							qtime2.add(start+"-"+end);
						}
						if(null!=cycleTime3){
							smd3.setData(cycleTime3);
							smd3.setName(start+"-"+end);
							smdList3.add(smd3);
							qtime3.add(start+"-"+end);
						}
						if(null!=smd4){
							smd4.setData(cycleTime4);
							smd4.setName(start+"-"+end);
							smdList4.add(smd4);
							qtime4.add(start+"-"+end);
						}
						if(findMigrateChartList.size()==(i+1))
						break mrfor;
						
						break;
					}
					
				}
				if(findMigrateChartList.size()==(i+1)){
					String start = sd.format(findMigrateChartList.get(i).getDatetimeStart());
					String end = sd.format(findMigrateChartList.get(i).getDatetimeEnd());
					
					smd1.setData(cycleTime1);
					smd1.setName(start+"-"+end);
					smdList1.add(smd1);
					qtime1.add(start+"-"+end);
					if(null!=smd2){
						smd2.setData(cycleTime2);
						smd2.setName(start+"-"+end);
						smdList2.add(smd2);
						qtime2.add(start+"-"+end);
					}
					if(null!=cycleTime3){
						smd3.setData(cycleTime3);
						smd3.setName(start+"-"+end);
						smdList3.add(smd3);
						qtime3.add(start+"-"+end);
					}
					if(null!=smd4){
						smd4.setData(cycleTime4);
						smd4.setName(start+"-"+end);
						smdList4.add(smd4);
						qtime4.add(start+"-"+end);
					}
					break mrfor;
				}
				i++;
				y++;
			}
	}
		migrationData.put("qtime1", qtime1);
		migrationData.put("qtime2", qtime2);
		migrationData.put("qtime3", qtime3);
		migrationData.put("qtime4", qtime4);
		migrationData.put("smdList1", smdList1);
		migrationData.put("smdList2", smdList2);
		migrationData.put("smdList3", smdList3);
		migrationData.put("smdList4", smdList4);
		
		
//		List<TMisMigrationData>  smdList2=new ArrayList<TMisMigrationData>();
//		for (int i = 0; i <qtime2.size(); i++) {
//			TMisMigrationData smd1=new TMisMigrationData();
//			smd1.setName(qtime1.get(i));
//			List<Integer>  cycleTime1=new ArrayList<Integer>();
//			for (int y= 7*(i+1)+2; y< 7*(i+1)+18; y++) {
//				cycleTime1.add(y);
//			}
//			smd1.setData(cycleTime1);
//			smdList2.add(smd1);
//			
//		}
//		
//		migrationData.put("smdList2", smdList2);
		
		return migrationData;
	}
	/**
	 * 跳转迁徙率数据本金图表页面
	 */
	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = "migrateAmountchart")
	public String migrateAmountChart(TMisMigrationRateReport tMisMigrationRateReport, Model model) {
		return "modules/dunning/tMisMigrationRateAmountChart";
	}
	
	/**
	 * 获取户数迁徙率户数的数据
	 */
	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = "migrationGetAmountdata")
	@ResponseBody
	public Map<String, Object> migrationGetAmountdata(TMisMigrationRateReport tMisMigrationRateReport) {
		
		
		//获取户数迁徙
		Map<String, Object> migrationData=new HashMap<String, Object>();
		
		//横坐标集合（1-16）
		List<Integer>  cycle=new ArrayList<Integer>();
		for (int i = 1; i < 17; i++) {
			cycle.add(i);
		}
		migrationData.put("cycle", cycle);
		
		//获取4个队列数据数据
		List<TMisMigrationRateReport> findMigrateChartList = tMisMigrationRateReportService.findMigrateChartList(tMisMigrationRateReport);
		List<TMisMigrationData>  smdList1=new ArrayList<TMisMigrationData>();
		List<TMisMigrationData>  smdList2=new ArrayList<TMisMigrationData>();
		List<TMisMigrationData>  smdList3=new ArrayList<TMisMigrationData>();
		List<TMisMigrationData>  smdList4=new ArrayList<TMisMigrationData>();
		List<String>  qtime1=new ArrayList<String>();
		List<String>  qtime2=new ArrayList<String>();
		List<String>  qtime3=new ArrayList<String>();
		List<String>  qtime4=new ArrayList<String>();
		SimpleDateFormat sd=new SimpleDateFormat("YYYYMMdd");
		int i=0;
		mrfor:for (int j = 0; j < 5; j++) {
			
			
			TMisMigrationData smd1=new TMisMigrationData();
			List<BigDecimal>  cycleTime1=new ArrayList<BigDecimal>();
			
			TMisMigrationData smd2=null;
			List<BigDecimal>  cycleTime2=null;
			if(j<4){
				smd2=new TMisMigrationData();
				cycleTime2=new ArrayList<BigDecimal>();
			}
			
			TMisMigrationData smd3=null;
			List<BigDecimal>  cycleTime3=null;
			if(j<3){
				smd3=new TMisMigrationData();
				cycleTime3=new ArrayList<BigDecimal>();
			}
			
			TMisMigrationData smd4=null;
			List<BigDecimal>  cycleTime4=null;
			if(j<2){
				smd4=new TMisMigrationData();
				cycleTime4=new ArrayList<BigDecimal>();
			}
			
			
			int y=0;
			while( findMigrateChartList.size()>i){
				if(y==0){
					cycleTime1.add(findMigrateChartList.get(i).getCp1new());
					if(null!=cycleTime2)
						cycleTime2.add(findMigrateChartList.get(i).getCp2new());
					if(null!=cycleTime3)
						cycleTime3.add(findMigrateChartList.get(i).getCp3new());
					if(null!=cycleTime4)
						cycleTime4.add(findMigrateChartList.get(i).getCp4new());
				}
				if(y>=1){
					if(findMigrateChartList.get(i).getCycle().equals(findMigrateChartList.get(i-1).getCycle())){
						
						cycleTime1.add(findMigrateChartList.get(i).getCp1new());
						if(null!=cycleTime2)
							cycleTime2.add(findMigrateChartList.get(i).getCp2new());
						if(null!=cycleTime3)
							cycleTime3.add(findMigrateChartList.get(i).getCp3new());
						if(null!=cycleTime4)
							cycleTime4.add(findMigrateChartList.get(i).getCp4new());
						
					}else{
						String start = sd.format(findMigrateChartList.get(i-1).getDatetimeStart());
						String end = sd.format(findMigrateChartList.get(i-1).getDatetimeEnd());
						
						smd1.setData(cycleTime1);
						smd1.setName(start+"-"+end);
						smdList1.add(smd1);
						qtime1.add(start+"-"+end);
						
						if(null!=smd2){
							smd2.setData(cycleTime2);
							smd2.setName(start+"-"+end);
							smdList2.add(smd2);
							qtime2.add(start+"-"+end);
						}
						if(null!=cycleTime3){
							smd3.setData(cycleTime3);
							smd3.setName(start+"-"+end);
							smdList3.add(smd3);
							qtime3.add(start+"-"+end);
						}
						if(null!=smd4){
							smd4.setData(cycleTime4);
							smd4.setName(start+"-"+end);
							smdList4.add(smd4);
							qtime4.add(start+"-"+end);
						}
						if(findMigrateChartList.size()==(i+1))
							break mrfor;
						
						break;
					}
					
				}
				if(findMigrateChartList.size()==(i+1)){
					String start = sd.format(findMigrateChartList.get(i).getDatetimeStart());
					String end = sd.format(findMigrateChartList.get(i).getDatetimeEnd());
					
					smd1.setData(cycleTime1);
					smd1.setName(start+"-"+end);
					smdList1.add(smd1);
					qtime1.add(start+"-"+end);
					if(null!=smd2){
						smd2.setData(cycleTime2);
						smd2.setName(start+"-"+end);
						smdList2.add(smd2);
						qtime2.add(start+"-"+end);
					}
					if(null!=cycleTime3){
						smd3.setData(cycleTime3);
						smd3.setName(start+"-"+end);
						smdList3.add(smd3);
						qtime3.add(start+"-"+end);
					}
					if(null!=smd4){
						smd4.setData(cycleTime4);
						smd4.setName(start+"-"+end);
						smdList4.add(smd4);
						qtime4.add(start+"-"+end);
					}
					break mrfor;
				}
				i++;
				y++;
			}
		}
		migrationData.put("qtime1", qtime1);
		migrationData.put("qtime2", qtime2);
		migrationData.put("qtime3", qtime3);
		migrationData.put("qtime4", qtime4);
		migrationData.put("smdList1", smdList1);
		migrationData.put("smdList2", smdList2);
		migrationData.put("smdList3", smdList3);
		migrationData.put("smdList4", smdList4);
		
		return migrationData;
	}

	/**
	 * 贷后报表测试用例
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "test")
	public String testReportPage() {
		return "modules/dunning/tMisDunningMigrationRateTest";
		
	}
	/**
	 * 关于迁徙率数据的获取
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "autoInsertTmpMoveCycleDB")
	@ResponseBody
	public void autoInsertTmpMoveCycleDB() {
		tMisMigrationRateReportService.autoInsertTmpMoveCycleDB();
	}
	
	/**
	 * 关于迁徙率数据的获取
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "autoMigrationRateGetData")
	@ResponseBody
	public void autoMigrationRateGetData() {
		tMisMigrationRateReportService.autoMigrationRateGetData();
	}
	
	/**
	 * 获取计算后的迁徙率数据
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "autoInsertMigrationRateReportDB")
	@ResponseBody
	public void autoInsertMigrationRateReportDB( Date yesterday) {
		tMisMigrationRateReportService.autoInsertMigrationRateReportDB();
	}
	
//	autoInsertTmpMoveCycleDB
//	autoMigrationRateGetData
//	autoInsertMigrationRateReportDB
	
	/**
	 * 迁徙导出日报
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "migrateExport")
	@ResponseBody
	public String migrateExport(TMisMigrationRateReport tMisMigrationRateReport,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "migrateExportFile" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<TMisMigrationRateReport> migratelist = tMisMigrationRateReportService.findList(tMisMigrationRateReport); 
            new ExportExcel("迁徙日报表", TMisMigrationRateReport.class).setDataList(migratelist).write(response, fileName).dispose();
           
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		 return "OK";
	}
	
	
}