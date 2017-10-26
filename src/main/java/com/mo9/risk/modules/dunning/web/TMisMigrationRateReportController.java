/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
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
		
		Date datetimeEnd = tMisMigrationRateReport.getDatetimeEnd();
		Date dateChoice=null;
		if(null!=datetimeEnd){
			SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat sd2=new SimpleDateFormat("yyyy-MM-dd");
			String monthUpDown = tMisMigrationRateReport.getMonthUpDown();
			String date="";
			if("up".equals(monthUpDown)){
				 date=sd.format(datetimeEnd)+"-10";
			}
			if("down".equals(monthUpDown)){
				 date=sd.format(datetimeEnd)+"-20";
			}
			try {
				dateChoice=sd2.parse(date);
			} catch (ParseException e) {
				logger.warn("时间转换失败");
				return null;
			}
		}
		tMisMigrationRateReport.setDatetimeEnd(dateChoice);
		DynamicDataSource.setCurrentLookupKey("temporaryDataSource");
		Page<TMisMigrationRateReport> page ;
		try{
			
			 page = tMisMigrationRateReportService.findPage(new Page<TMisMigrationRateReport>(request, response), tMisMigrationRateReport); 
		}finally{
		DynamicDataSource.setCurrentLookupKey("dataSource");
		}
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
	 * 跳转迁徙率数据图表页面
	 */
	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = "migratechart")
	public String migratechart(TMisMigrationRateReport tMisMigrationRateReport, Model model) {
		model.addAttribute("migrate", tMisMigrationRateReport.getMigrate());
		return "modules/dunning/tMisMigrationRateChart";
	}
	/**
	 * 获取迁徙率的数据
	 */
	@RequiresPermissions("dunning:tMisMigrationRateReport:view")
	@RequestMapping(value = "migrationGetdata")
	@ResponseBody
	public Map<String, Object> migrationGetData(TMisMigrationRateReport tMisMigrationRateReport) {
		
		Map<String, Object> migrationData=new HashMap<String, Object>();
		//横坐标集合（1-16）
		List<Integer>  cycle=new ArrayList<Integer>();
		for (int i = 1; i < 17; i++) {
			cycle.add(i);
		}
		migrationData.put("cycle", cycle);
		
		//获取4个队列数据数据
		List<TMisMigrationRateReport> findMigrateChartList=null;
		try{
			
			DynamicDataSource.setCurrentLookupKey("temporaryDataSource");
			findMigrateChartList = tMisMigrationRateReportService.findMigrateChartList(tMisMigrationRateReport);
		}catch (Exception e) {
			logger.warn("迁徙查询.切换江湖救急只读库失败");
			return null;
		}finally{
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		if(null==findMigrateChartList){
			logger.warn("迁徙查询失败");
			return null;
		}
		List<TMisMigrationData>  smdList1=new ArrayList<TMisMigrationData>();
		List<TMisMigrationData>  smdList2=new ArrayList<TMisMigrationData>();
		List<TMisMigrationData>  smdList3=new ArrayList<TMisMigrationData>();
		List<TMisMigrationData>  smdList4=new ArrayList<TMisMigrationData>();
		List<String>  qtime1=new ArrayList<String>();
		List<String>  qtime2=new ArrayList<String>();
		List<String>  qtime3=new ArrayList<String>();
		List<String>  qtime4=new ArrayList<String>();
		int min1=0,max1=0,min2=0,max2=0,min3=0,max3=0,min4=0,max4=0,split1=0,split2=0,split3=0,split4=0;
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
			
			
			if( i==0){
				min1=findMigrateChartList.get(0).getCp1().intValue();
				max1=findMigrateChartList.get(0).getCp1().intValue();
				min2=findMigrateChartList.get(0).getCp2().intValue();
				max2=findMigrateChartList.get(0).getCp2().intValue();
				min3=findMigrateChartList.get(0).getCp3().intValue();
				max3=findMigrateChartList.get(0).getCp3().intValue();
				min4=findMigrateChartList.get(0).getCp4().intValue();
				max4=findMigrateChartList.get(0).getCp4().intValue();
			}
			int y=0;
			while( findMigrateChartList.size()>i){
				if(y==0){
					cycleTime1.add(findMigrateChartList.get(i).getCp1());
					if(i!=0){
						if(findMigrateChartList.get(i).getCp1().intValue()<min1){
							min1=findMigrateChartList.get(i).getCp1().intValue();
						}
						if(findMigrateChartList.get(i).getCp1().intValue()>max1){
							max1=findMigrateChartList.get(i).getCp1().intValue();
						}
					}
					if(null!=cycleTime2){
						cycleTime2.add(findMigrateChartList.get(i).getCp2());
						if(i!=0){
							if(findMigrateChartList.get(i).getCp2().intValue()<min2){
								min2=findMigrateChartList.get(i).getCp2().intValue();
							}
							if(findMigrateChartList.get(i).getCp2().intValue()>max2){
								max2=findMigrateChartList.get(i).getCp2().intValue();
							}
						}
					}
					if(null!=cycleTime3){
						cycleTime3.add(findMigrateChartList.get(i).getCp3());
						if(i!=0){
							if(findMigrateChartList.get(i).getCp3().intValue()<min3){
								min3=findMigrateChartList.get(i).getCp3().intValue();
							}
							if(findMigrateChartList.get(i).getCp3().intValue()>max3){
								max3=findMigrateChartList.get(i).getCp3().intValue();
							}
						}
					}
					if(null!=cycleTime4){
						cycleTime4.add(findMigrateChartList.get(i).getCp4());
						if(i!=0){
							if(findMigrateChartList.get(i).getCp4().intValue()<min4){
								min4=findMigrateChartList.get(i).getCp4().intValue();
							}
							if(findMigrateChartList.get(i).getCp4().intValue()>max4){
								max4=findMigrateChartList.get(i).getCp4().intValue();
							}
						}
					}
				}
				if(y>=1){
					if(findMigrateChartList.get(i).getCycle().equals(findMigrateChartList.get(i-1).getCycle())){
						
						cycleTime1.add(findMigrateChartList.get(i).getCp1());
						if(i!=0){
							if(findMigrateChartList.get(i).getCp1().intValue()<min1){
								min1=findMigrateChartList.get(i).getCp1().intValue();
							}
							if(findMigrateChartList.get(i).getCp1().intValue()>max1){
								max1=findMigrateChartList.get(i).getCp1().intValue();
							}
						}
						if(null!=cycleTime2){
							cycleTime2.add(findMigrateChartList.get(i).getCp2());
							if(i!=0){
								if(findMigrateChartList.get(i).getCp2().intValue()<min2){
									min2=findMigrateChartList.get(i).getCp2().intValue();
								}
								if(findMigrateChartList.get(i).getCp2().intValue()>max2){
									max2=findMigrateChartList.get(i).getCp2().intValue();
								}
							}
						}
						if(null!=cycleTime3){
							cycleTime3.add(findMigrateChartList.get(i).getCp3());
							if(i!=0){
								if(findMigrateChartList.get(i).getCp3().intValue()<min3){
									min3=findMigrateChartList.get(i).getCp3().intValue();
								}
								if(findMigrateChartList.get(i).getCp3().intValue()>max3){
									max3=findMigrateChartList.get(i).getCp3().intValue();
								}
							}
						}
						if(null!=cycleTime4){
							cycleTime4.add(findMigrateChartList.get(i).getCp4());
							if(i!=0){
								if(findMigrateChartList.get(i).getCp4().intValue()<min4){
									min4=findMigrateChartList.get(i).getCp4().intValue();
								}
								if(findMigrateChartList.get(i).getCp4().intValue()>max4){
									max4=findMigrateChartList.get(i).getCp4().intValue();
								}
							}
						}
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
		split1=(max1+1-min1)/1;
		if(split1>20){
			split1=20;
			max1=split1*1+min1-1;
		}
		split2=(max2+1-min2)*10/5;
		if(split2>20){
			split2=20;
			max2=split2*5/10+min2-1;
		}
		split3=(max3+1-min3)*10/2;
		if(split3>20){
			split3=20;
			max3=split3*2/10+min3-1;
		}
		split4=(max4+1-min4)*10/1;
		if(split4>20){
			split4=20;
			max4=split4/10+min4-1;
		}
		migrationData.put("min1", min1);
		migrationData.put("max1", max1+1);
		migrationData.put("min2", min2);
		migrationData.put("max2", max2+1);
		migrationData.put("min3", min3);
		migrationData.put("max3", max3+1);
		migrationData.put("min4", min4);
		migrationData.put("max4", max4+1);
		migrationData.put("split1", split1);
		migrationData.put("split2", split2);
		migrationData.put("split3", split3);
		migrationData.put("split4", split4);
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
		try {
			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");  
			tMisMigrationRateReportService.autoInsertTmpMoveCycleDB();
		} catch (Exception e) {
			logger.info("",e);
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");  
		}
	}
	
	
	/**
	 * 关于迁徙率数据的获取
	 */
//	@RequiresPermissions("dunning:tMisDunningTask:adminview")
//	@RequestMapping(value = "autoMigrationRateGetData")
//	@ResponseBody
//	public void autoMigrationRateGetData() {
//		try {
//			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");
//			tMisMigrationRateReportService.autoMigrationRateGetData();
//		} catch (Exception e) {
//			logger.info("",e);
//		} finally {
//			DynamicDataSource.setCurrentLookupKey("dataSource");
//		}
//	}
	
	/**
	 * 获取计算后的迁徙率数据
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "autoInsertMigrationRateReportDB")
	@ResponseBody
	public void autoInsertMigrationRateReportDB(Date yesterday) {
		try {
			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");  
			tMisMigrationRateReportService.autoInsertMigrationRateReportDB(yesterday);
		} catch (Exception e) {
			logger.info("",e);
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");  
		}
	}
	
	/**
	 * 获取计算后的迁徙率数据
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "autoInsertHistoryMigrationRateReportDB")
	@ResponseBody
	public void autoInsertHistoryMigrationRateReportDB() {
		Calendar c = Calendar.getInstance();
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");  
			//开始时间必须小于结束时间
			Date beginDate = dateFormat1.parse("2017-03-01");
			Date endDate = dateFormat1.parse("2017-10-18");
			Date date = beginDate;
			while (!date.equals(endDate)) {
				System.out.println(dateFormat1.format(date));
				tMisMigrationRateReportService.autoInsertMigrationRateReportDB(date);
				c.setTime(date);
				c.add(Calendar.DATE, 1); // 日期加1天
				date = c.getTime();
			}
		} catch (ParseException e) {
			logger.info("",e);
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");  
		}
	}
	
//	autoInsertTmpMoveCycleDB
//	autoMigrationRateGetData
//	autoInsertMigrationRateReportDB
	
	/**
	 * 迁徙导出日报
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "migrateExport")
	public String migrateExport(TMisMigrationRateReport tMisMigrationRateReport,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Date datetimeEnd = tMisMigrationRateReport.getDatetimeEnd();
		Date dateChoice=null;
		if(null!=datetimeEnd){
			SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat sd2=new SimpleDateFormat("yyyy-MM-dd");
			String monthUpDown = tMisMigrationRateReport.getMonthUpDown();
			String date="";
			if("up".equals(monthUpDown)){
				 date=sd.format(datetimeEnd)+"-10";
			}
			if("down".equals(monthUpDown)){
				 date=sd.format(datetimeEnd)+"-20";
			}
			try {
				dateChoice=sd2.parse(date);
			} catch (ParseException e) {
				logger.warn("时间转换失败");
				return null;
			}
		}
		tMisMigrationRateReport.setDatetimeEnd(dateChoice);
		try {
            String fileName = "迁徙率日报表" + DateUtils.getDate("yyyy/MM/dd")+".xlsx";
            List<TMisMigrationRateReport> migratelist = tMisMigrationRateReportService.findList(tMisMigrationRateReport); 
            new ExportExcel("迁徙日报表", TMisMigrationRateReport.class).setDataList(migratelist).write(response, fileName).dispose();
            return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/dunning/tMisMigrationRateReport/list";
    }

}