/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.gamaxpay.commonutil.web.GetRequest;
import com.mo9.risk.modules.dunning.bean.PayChannelInfo;
import com.mo9.risk.modules.dunning.bean.SerialRepay;
import com.mo9.risk.modules.dunning.bean.SerialRepay.RepayWay;
import com.mo9.risk.modules.dunning.dao.TMisDunningLetterDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningOrderDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.AppLoginLog;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecovery;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecoveryHistoryRecord;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.DunningOuterFile;
import com.mo9.risk.modules.dunning.entity.DunningSmsTemplate;
import com.mo9.risk.modules.dunning.entity.MemberInfo;
import com.mo9.risk.modules.dunning.entity.MobileResult;
import com.mo9.risk.modules.dunning.entity.NumberCleanResult;
import com.mo9.risk.modules.dunning.entity.OrderHistory;
import com.mo9.risk.modules.dunning.entity.PerformanceMonthReport;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.modules.dunning.entity.TMisChangeCardRecord;
import com.mo9.risk.modules.dunning.entity.TMisDunnedConclusion;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningLetter;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrganization;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningScoreCard;
import com.mo9.risk.modules.dunning.entity.TMisDunningTag;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisPaid;
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory;
import com.mo9.risk.modules.dunning.entity.TRiskBuyer2contacts;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerWorkinfo;
import com.mo9.risk.modules.dunning.entity.TRiskOrder;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueType;
import com.mo9.risk.modules.dunning.entity.TmisDunningSmsTemplate;
import com.mo9.risk.modules.dunning.enums.DebtBizType;
import com.mo9.risk.modules.dunning.enums.PayStatus;
import com.mo9.risk.modules.dunning.enums.TagType;
import com.mo9.risk.modules.dunning.manager.RiskOrderManager;
import com.mo9.risk.modules.dunning.service.MemberInfoService;
import com.mo9.risk.modules.dunning.service.RiskQualityInfoService;
import com.mo9.risk.modules.dunning.service.TBuyerContactService;
import com.mo9.risk.modules.dunning.service.TMisChangeCardRecordService;
import com.mo9.risk.modules.dunning.service.TMisContantRecordService;
import com.mo9.risk.modules.dunning.service.TMisDunningDeductService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningInformationRecoveryService;
import com.mo9.risk.modules.dunning.service.TMisDunningOrganizationService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.mo9.risk.modules.dunning.service.TMisDunningTagService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.modules.dunning.service.TMisReliefamountHistoryService;
import com.mo9.risk.modules.dunning.service.TMisRemittanceConfirmService;
import com.mo9.risk.modules.dunning.service.TRiskBuyer2contactsService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerContactRecordsService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerPersonalInfoService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerWorkinfoService;
import com.mo9.risk.modules.dunning.service.TaskIssueService;
import com.mo9.risk.modules.dunning.service.TmisDunningSmsTemplateService;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jdbc.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 催收任务Controller
 * @author 徐盛
 * @version 2016-07-12
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningTask")
public class TMisDunningTaskController extends BaseController {

	private static final Logger actionlog = Logger.getLogger("com.mo9.cuishou.liulan");
	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;

	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	@Autowired
	private TRiskBuyerWorkinfoService tRiskBuyerWorkinfoService;
	
	@Autowired
	private TRiskBuyer2contactsService tRiskBuyer2contactsService;
	
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoDao;
	
	@Autowired
	private TBuyerContactService tBuyerContactService;
	
	@Autowired
	private TRiskBuyerContactRecordsService tRiskBuyerContactRecordsService;
	
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	
	@Autowired
	private TMisContantRecordService tMisContantRecordService;
	
	@Autowired
	private TMisRemittanceConfirmService tMisRemittanceConfirmService;
	
	@Autowired
	private TMisDunningGroupService tMisDunningGroupService;
	
	@Autowired
	private TMisChangeCardRecordService tMisChangeCardRecordService;
	
	@Autowired
	private TMisDunningDeductService tMisDunningDeductService;
	
	@Autowired
	private TmisDunningSmsTemplateService tstService;

	@Autowired
	private TMisDunningTagService tMisDunningTagService ;
	
	@Autowired
	private RiskQualityInfoService riskQualityInfoService;

	@Autowired
	private MemberInfoService memberInfoService;

	@Autowired
	private TMisDunningInformationRecoveryService tMisDunningInformationRecoveryService;
	@Autowired
	private RiskOrderManager orderManager;
	@Autowired
	private TMisDunningOrganizationService tMisDunningOrganizationService;
	@Autowired
	private TMisDunningOrderDao tMisDunningOrderDao;
	@Autowired
	private TMisReliefamountHistoryService tMisReliefamountHistoryService;
	@Autowired
	private TaskIssueService taskIssueService;
	@Autowired
	private TMisDunningLetterDao tMisDunningLetterDao;

	private JedisUtils jedisUtils = new JedisUtils();
	 
	@ModelAttribute
	public TMisDunningTask get(@RequestParam(required=false) String id) {
		TMisDunningTask entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisDunningTaskService.get(id);
		}
		if (entity == null){
			entity = new TMisDunningTask();
		}
		return entity;
	}
	
	@ModelAttribute("dunningOrder")
	public DunningOrder getDunningOrder(@RequestParam(required=false) String id) {
		DunningOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisDunningTaskService.getDunningOrder(id);
		}
		if (entity == null){
			entity = new DunningOrder();
		}
		return entity;
	}
	
	
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = {"findOrderPageList", ""})
	public String findOrderPageList(DunningOrder dunningOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		//默认条件查询未还清订单
		if (dunningOrder.getStatus() == null){
			dunningOrder.setStatus(DunningOrder.STATUS_PAYMENT);
		}
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			Page<DunningOrder> page = tMisDunningTaskService.newfindOrderPageList(new Page<DunningOrder>(request, response), dunningOrder,UserUtils.getUser());
			model.addAttribute("page", page);
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		model.addAttribute("mobileResultMap", MobileResult.getActions());
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
		model.addAttribute("bizTypes", DebtBizType.values());
		return "modules/dunning/tMisDunningTaskList";
	}

	/**
	 * 群发短信获取模板
	 * @param
	 * @param ordersStr
	 * @param smsTemplate
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "getDunningSmsTemplate")
	@ResponseBody
	public String getDunningSmsTemplate(String ordersStr,String smsTemplate) {
		try {
			if(StringUtils.isNotBlank(ordersStr)){
				String[] strTemplate = ordersStr.split(",")[0].split("#");
			}
			String message =  tMisContantRecordService.getDunningSmsTemplate("【XXX】" ,new DunningOrder("xxx", 0D, 0), DunningSmsTemplate.valueOf(smsTemplate));
			return message;
		} catch (Exception e) {
			logger.info("",e);
		}
		return null;
	}

	
	/*催收留案功能-委外任务截止时间设置 Patch 0002 by GQWU at 2016-11-25 start*/
	@RequiresPermissions("dunning:tMisDunningTask:directorview")
	@RequestMapping(value = "dialogSetOuterOrdersDeadline")
	public String dialogSetOuterOrdersDeadline( Model model,HttpServletRequest request) {

		String dealcodes = request.getParameter("dealcodes");
		String[] dealcodeArr = dealcodes.split(",");
		String message = "";
		if (dealcodeArr == null || dealcodeArr.length == 0) {
			message = "提交订单为空！";
			model.addAttribute("message", message);
			return "modules/dunning/dialog/dialogSetOuterOrdersDeadline";
		}

		List<DunningOrder> dunningOrders = tMisDunningTaskService.getOrdersByDealcodes(dealcodeArr);
		if(dunningOrders == null || dunningOrders.size() < 1){
			message = "提交订单无效";
			model.addAttribute("message", message);
			return "modules/dunning/dialog/dialogSetOuterOrdersDeadline";
		}
		
		Iterator<DunningOrder> iterator = dunningOrders.iterator();
		DunningOrder dunningOrder = new DunningOrder();
		
		while(iterator.hasNext()){
			dunningOrder = iterator.next();
			if(dunningOrder.getDunningpeopletype().equals("inner")){
				message = "请全部选择委外订单！";
				model.addAttribute("message", message);
				return "modules/dunning/dialog/dialogSetOuterOrdersDeadline";
			}
		}

		model.addAttribute("date", new Date());
		model.addAttribute("message", message);
		model.addAttribute("dealcodesLength", dealcodeArr.length);
		model.addAttribute("dealcodes", dealcodes);
		
		return "modules/dunning/dialog/dialogSetOuterOrdersDeadline";
	}
	/*催收留案功能-委外任务截止时间设置 Patch 0002 by GQWU at 2016-11-25 end*/
	
	/*催收留案功能-委外任务截止时间设置 Patch 0002 by GQWU at 2016-11-25 start*/
	@RequiresPermissions("dunning:tMisDunningTask:directorview")
	@RequestMapping(value = "outerOrderDeadlineSave")
	@ResponseBody
	public String outerOrderDeadlineSave( Date deadline, String[] dealcodes,Model model,HttpServletRequest request) {

		String mes = "保存失败！";
		
		if(deadline == null){
			return mes = "截止日期为空";
		} 
		if(dealcodes==null || dealcodes.length == 0){
			return mes = "订单编号为空;";
		}
		
		if (tMisDunningTaskService.updateDunningTimeByDealcodes(dealcodes, deadline) == 0) {
			return mes;
		} else {
			String dealcodesStr = "";
			for (int i=0; i<dealcodes.length; i++){
				dealcodesStr += dealcodes[i] + ";";
			}
			String username = "<用户：" + UserUtils.getUser().getLoginName()+">";
			String action = "<行为：" + "修改委外订单->" + dealcodesStr + "的截止日期为->"+ deadline + ">";
			String date = "<修改时间：" + new Date().toString()+ ">\r\n";
			actionlog.info(username+action+date);
			return "OK";
		}
		
	}
	/*催收留案功能-委外任务截止时间设置 Patch 0002 by GQWU at 2016-11-25 end*/
	
	
	/*催收留案功能-留案加载 Patch 0001 by GQWU at 2016-11-9 start*/
	@RequiresPermissions("dunning:tMisDunningTask:directorview")
	@RequestMapping(value = "dialogDeferDunningDeadline")
	public String dialogDeferDunningDeadline( Model model,HttpServletRequest request) {

		String dealcodes = request.getParameter("dealcodes");
		String[] dealcodeArr = dealcodes.split(",");
		String message = "";
		if (dealcodeArr == null || dealcodeArr.length == 0) {
			message = "提交订单为空！";
			model.addAttribute("message", message);
			return "modules/dunning/dialog/dialogDeferDunningDeadline";
		}

		List<DunningOrder> dunningOrders = tMisDunningTaskService.getOrdersByDealcodes(dealcodeArr);
		if(dunningOrders == null || dunningOrders.size() < 1){
			message = "提交订单无效";
			model.addAttribute("message", message);
			return "modules/dunning/dialog/dialogDeferDunningDeadline";
		}
		
		Iterator<DunningOrder> iterator = dunningOrders.iterator();
		DunningOrder dunningOrder = new DunningOrder();
		int diffDay;
		Date maxDeadline = addDate(dunningOrders.get(0).getDeadline(),62);
		Date deadline = new Date ();
		try {
			while(iterator.hasNext()){
				dunningOrder = iterator.next();
				if(dunningOrder.getDunningpeopletype().equals("inner")){
					diffDay = dateDiff(dunningOrder.getRepaymenttime(), new Date());
					if (diffDay < 30 || diffDay > 35){
						message = "内催订单留案要求订单逾期天数在30~35天之间！";
						model.addAttribute("message", message);
						return "modules/dunning/dialog/dialogDeferDunningDeadline";
					}
					diffDay = dateDiff(dunningOrder.getRepaymenttime(), dunningOrder.getDeadline());
					if (diffDay >= 42) {
						message = "内催订单留案要求订单催收截止日期不超过应还款日期42天！";
						model.addAttribute("message", message);
						return "modules/dunning/dialog/dialogDeferDunningDeadline";
					}
					deadline = addDate(dunningOrder.getRepaymenttime(), 42);
					if (maxDeadline.after(deadline)) {
						maxDeadline = deadline;
					}
					
				} else if (dunningOrder.getDunningpeopletype().equals("outer")){
					deadline = addDate(dunningOrder.getDeadline(), 62);
					if (maxDeadline.after(deadline)) {
						maxDeadline = deadline;
					}
				} else {
					message = "内催订单留案要求订单催收截止日期不超过应还款日期42天！";
					model.addAttribute("message", message);
					return "modules/dunning/dialog/dialogDeferDunningDeadline";
				}
			}
		} catch (ParseException e) {
			logger.warn("催收留案， 到期还款日或催收截止日日期格式错误。");
		}
		

		model.addAttribute("maxDeadline", maxDeadline);
		model.addAttribute("message", message);
		model.addAttribute("dealcodesLength", dealcodeArr.length);
		model.addAttribute("dealcodes", dealcodes);
		
		return "modules/dunning/dialog/dialogDeferDunningDeadline";
	}
	
	
	//临时工具方法,应统一提取至工具类
	private int dateDiff (Date startDate, Date endDate) throws ParseException{
		SimpleDateFormat fmt = new SimpleDateFormat ("yyyyMMdd");
		startDate = fmt.parse(fmt.format(startDate));
		endDate = fmt.parse(fmt.format(endDate));
		return new Long((endDate.getTime() - startDate.getTime())/1000/3600/24).intValue();
	};
	
	private Date addDate (Date date, int segment) {

		logger.warn("vergin = "+date.toString());
		logger.warn("segment = " + segment);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, segment);
		logger.warn("after = "+c.getTime().toString());
		return c.getTime();
	}
	
	private Boolean saveAction (String username, String action, String date){
		String path = "";
		Boolean bool = false;
		File file = new File(path);
		try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                //创建文件成功后，写入内容到文件里
                FileInputStream fis = null;
                InputStreamReader isr = null;
                BufferedReader br = null;
                FileOutputStream fos  = null;
                PrintWriter pw = null;
                try {
                    //将文件读入输入流
                	String filein = username + action + date +"\r\n";//新写入的行，换行
                    String temp  = "";
                    fis = new FileInputStream(file);
                    isr = new InputStreamReader(fis);
                    br = new BufferedReader(isr);
                    StringBuffer buffer = new StringBuffer();
                    
                    //文件原有内容
                    while((temp =br.readLine())!= null){
                        buffer.append(temp);
                        // 行与行之间的分隔符 相当于“\n”
                        buffer = buffer.append(System.getProperty("line.separator"));
                    }
                    buffer.append(filein);
                    
                    fos = new FileOutputStream(file);
                    pw = new PrintWriter(fos);
                    pw.write(buffer.toString().toCharArray());
                    pw.flush();
                    bool = true;
                } catch (Exception e) {
                    // TODO: handle exception
									logger.info("",e);
                }finally {
                    //不要忘记关闭
                    if (pw != null) {
                        pw.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                    if (isr != null) {
                        isr.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
        } catch (Exception e) {
					logger.info("",e);
        }
		return bool;
	};
	
	
	/*催收留案功能-留案加载 Patch 0001 by GQWU at 2016-11-9 end*/
	
	/*催收留案功能-留案保存 Patch 0001 by GQWU at 2016-11-9 start*/
	@RequiresPermissions("dunning:tMisDunningTask:directorview")
	@RequestMapping(value = "dunningDeadlineSave")
	@ResponseBody
	public String dunningDeadlineSave(Date deferDate, String[] dealcodes,HttpServletRequest request) {
		
		String mes = "保存失败！";
		
		if(deferDate == null){
			return mes = "留案日期为空";
		} 
		if(dealcodes==null || dealcodes.length == 0){
			return mes = "订单编号为空;";
		}
		if (tMisDunningTaskService.updateDunningTimeByDealcodes(dealcodes, deferDate) == 0) {
			return mes;
		} else {
			String dealcodesStr = "";
			for (int i=0; i<dealcodes.length; i++){
				dealcodesStr += dealcodes[i]+ ";";
			}
			String username = "<用户：" + UserUtils.getUser().getLoginName()+">";
			String action = "<行为：" + "催收留案订单->" + dealcodesStr + "截止日期修改为->"+ deferDate + ">";
			String date = "<修改时间：" + new Date().toString()+ ">\r\n";
			actionlog.info(username+action+date);
			return "OK";
		}
	}
	
	/**
	 * 加载手动分案页面
	 * @param dunningcycle
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:distribution")
	@RequestMapping(value = "dialogDistribution")
	public String dialogDistribution(Model model,String dunningcycle, String bizType) {
		try {
			TMisDunningGroup tMisDunningGroup = new TMisDunningGroup();
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findPeopleByDistributionDunningcycle(dunningcycle);
			List<TMisDunningOrganization> organizations = tMisDunningOrganizationService.findList(null);
			model.addAttribute("organizations", organizations);
			model.addAttribute("dunningPeoples", dunningPeoples);
			model.addAttribute("dunningcycle", dunningcycle);
			model.addAttribute("bizType", DebtBizType.valueOf(bizType));
			model.addAttribute("groupList", tMisDunningGroupService.findList(tMisDunningGroup));
			model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
		} catch (Exception e) {
			logger.info("加载手动分案页面失败",e);
			return "views/error/500";
		}
		return "modules/dunning/dialog/dialogDistribution";
	}

	/**
	 * 获取手动分案催收人员
	 * @param request
	 */
	@RequiresPermissions("dunning:tMisDunningTask:distribution")
	@RequestMapping(value = "dialogDistributionPeople")
	@ResponseBody
	public List<TMisDunningPeople> DistributionPeople(HttpServletRequest request){
		List<TMisDunningPeople> dunningpeople = null;
		String[] dunningcycle = request.getParameterValues("dunningcycle[]");
		String[] type = request.getParameterValues("type[]");
		String[] auto = request.getParameterValues("auto[]");
		String name = request.getParameter("name");
		String bizTypeOther = request.getParameter("bizTypeOther");
		String[] bizTypes = request.getParameterValues("bizType[]");
		String bizTypeString="";
		 if(bizTypes!=null && bizTypes.length>0){
			 bizTypeString= StringUtils.join(bizTypes, ",");
			 
		 }
		String organizationName = request.getParameter("organizationName");
		if ((dunningcycle == null || dunningcycle.length == 0) && (type == null || type.length == 0)
				&& (auto == null || auto.length == 0) && StringUtils.isEmpty(name) &&(bizTypes == null || bizTypes.length == 0) ) {
			return new ArrayList<TMisDunningPeople>(); 
		}

		String dunningpeoplename = request.getParameter("dunningpeoplename");
		try{
			dunningpeople = tMisDunningPeopleService.findPeopleByCycleTypeAutoName(dunningcycle, type, auto, name, dunningpeoplename, bizTypeString,organizationName,bizTypeOther);
		}catch (Exception e){
			logger.info("",e);
			return null;
		}
		return dunningpeople;
	}

	/**
	 * 手动分案
	 * @param dunningcycle
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:distribution")
	@RequestMapping(value = "distributionSave")
	@ResponseBody
	public String distributionSave(String orders,String dunningcycle, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String mes = "";
		try {
			if(null == orders || null == dunningcycle ||"".equals(orders) || "".equals(dunningcycle)  ){
				return "订单或队列不能为空";
			}
			List<String> dealcodes = new ArrayList<String>();
			for(String string :Arrays.asList(orders.split(","))){
				if(!"".equals(string.split("#")[0])){
					dealcodes.add(string.split("#")[0]);
				}
			}
			List<String> newdunningpeopleids = Arrays.asList(request.getParameterValues("newdunningpeopleids"));
			String assignmes = tMisDunningTaskService.assign(dealcodes, dunningcycle,newdunningpeopleids);
			mes = "OK,手动勾选"+dealcodes.size()+"条订单," + assignmes;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			logger.warn("订单已还款更新任务失败"+ new Date());
			return "分配异常，失败";
		}
		return  mes;
	}

	/**
	 * 自动分配
	 * @param
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "autoAssign")
	@ResponseBody
	public String autoAssign( Model model, RedirectAttributes redirectAttributes) {
		try {
			tMisDunningTaskService.autoAssign();
		} catch (Exception e) {
			logger.info("",e);
			return e.getMessage().toString();
		}
		addMessage(redirectAttributes, "自动分配成功");
		return "OK";
	}
	
	/**
	 * 新订单任务
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "autoAssignNewOrder")
	@ResponseBody
	public String autoAssignNewOrder( Model model, RedirectAttributes redirectAttributes) {
		try {
			tMisDunningTaskService.autoAssignNewOrder();
		} catch (Exception e) {
			logger.info("",e);
			return e.getMessage().toString();
		}
		addMessage(redirectAttributes, "新订单任务");
		return "OK";
	}
	
	/**
	 * 自动扫描还款
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "autoRepayment")
	@ResponseBody
	public String autoRepayment( Model model, RedirectAttributes redirectAttributes) {
		try {
			tMisDunningTaskService.autoRepayment();
		} catch (Exception e) {
			logger.info("",e);
			return e.getMessage().toString();
		}
		addMessage(redirectAttributes, "自动扫描还款");
		return "OK";
	}

	/**
	 * @Description 获取黑名单联系人信息
	 * @param redirectAttributes
	 * @return java.lang.String
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "acquireBlackRelationNum")
	@ResponseBody
	public String acquireBlackRelationNum( RedirectAttributes redirectAttributes) {
		riskQualityInfoService.refreshBlacklistRelation();
		addMessage(redirectAttributes, "黑名单联系人信息成功");
		return "OK";
	}

	
	/**
	 * 导出委外数据
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:exportFile")
	@RequestMapping(value = "exportOuterFile", method = RequestMethod.POST)
	public String exportOuterFile(@RequestParam("outerOrders") ArrayList<String> outerOrders, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		String redirectUrl = "redirect:" + adminPath + "/dunning/tMisDunningTask/findOrderPageList?repage";

		if (outerOrders == null || outerOrders.size() == 0) {
			addMessage(redirectAttributes, "导出数据为空");
			return redirectUrl;
		}

		List<DunningOuterFile> dunningOuterFiles = tMisDunningTaskService.exportOuterFile(outerOrders);
		if (null == dunningOuterFiles || dunningOuterFiles.isEmpty()) {
			addMessage(redirectAttributes, "未导出数据！");
			return redirectUrl;
		}

		tMisDunningTaskService.savefileLog(new Date(), outerOrders, dunningOuterFiles);
		addMessage(redirectAttributes, "委外数据生成成功");
		try {
			String fileName = "OutData" + DateUtils.getDate("yyyy-MM-dd HHmmss") + ".xlsx";
			new ExportExcel("委外数据", DunningOuterFile.class).setDataList(dunningOuterFiles).write(response, fileName).dispose();
			//  切换数据源更新order表
			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");
			tMisDunningTaskService.updateOuterfiletime(new Date(), outerOrders);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息：" + e.getMessage());
			return redirectUrl;
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		return null;
	}


	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "form")
	public String form(TMisDunningTask tMisDunningTask, Model model) {
		model.addAttribute("tMisDunningTask", tMisDunningTask);
		return "modules/dunning/tMisDunningTaskForm";
	}
	
	
	/**
	 * 加载用户信息页面1
	 * @param status buyerId dealcode dunningtaskdbid
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "pageFather")
	public String pageFather(String status, String dealcode,Model model) {

		if (dealcode == null || "".equals(dealcode)) {
			return "views/error/500";
		}

		TRiskBuyerPersonalInfo personalInfo = null;
		TMisDunningOrder order = null;
		TMisDunningTask task = null;
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			boolean ispayoff = false;
			if (order != null && "payoff".equals(order.getStatus())) {
				ispayoff = true;
			}
			model.addAttribute("ispayoff", ispayoff);
			task = tMisDunningTaskService.findDunningTaskByDealcode(dealcode);
			if (task == null) {
				logger.warn("任务不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			model.addAttribute("dunningCycle", task.getDunningcycle());

			personalInfo = personalInfoDao.getNewBuyerInfoByDealcode(dealcode);
			model.addAttribute("personalInfo", personalInfo);
			if (personalInfo.getOverdueDays() != null) {
				model.addAttribute("overdueDays", Integer.parseInt(personalInfo.getOverdueDays()));
			}

			model.addAttribute("mobileSelf", personalInfo.getMobile());
		} catch (Exception e) {
			logger.info("切换只读库查询失败", e);
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		String buyerId = personalInfo.getBuyerId();
		model.addAttribute("dunningtaskdbid", task.getId());
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("status", status);
		//model.addAttribute("isDelayable", isDelayable);

		boolean ispayoff = false;
		if (order != null && "payoff".equals(order.getStatus())) {
			ispayoff = true;
		}
		model.addAttribute("ispayoff", ispayoff);
		TMisChangeCardRecord tMisChangeCardRecord = tMisChangeCardRecordService.getCurrentBankCard(dealcode);

		if (tMisChangeCardRecord == null) {
			tMisChangeCardRecord = new TMisChangeCardRecord();
			if (personalInfo != null) {
				tMisChangeCardRecord.setBankcard(personalInfo.getRemitBankNo());
				tMisChangeCardRecord.setBankname(personalInfo.getRemitBankName());
				tMisChangeCardRecord.setIdcard(personalInfo.getIdcard());
				tMisChangeCardRecord.setMobile(personalInfo.getMobile());
			}
		}
		//根据逾期天数控制子页面显示;
		String controlDay = DictUtils.getDictValue("overdueDay", "controlPage", "1");
		model.addAttribute("controlDay", Integer.parseInt(controlDay));
		boolean deductable = tMisDunningDeductService.preCheckChannel(tMisChangeCardRecord.getBankname());
		//根据资方和逾期天数判断是否开启代扣
		boolean daikouStatus = tMisDunningTaskService.findOrderByPayCode(order);
		model.addAttribute("changeCardRecord", tMisChangeCardRecord);
		model.addAttribute("deductable", deductable && daikouStatus);
		model.addAttribute("daikouStatus", daikouStatus);

		TMisDunningTag tMisDunningTag = new TMisDunningTag();
		tMisDunningTag.setBuyerid(buyerId);
		List<TMisDunningTag> tags = tMisDunningTagService.findList(tMisDunningTag);
		model.addAttribute("tags", tags);

		Map<TagType,List<TMisDunningTag>> mapTag = new HashMap<>();
		for(TMisDunningTag t : tags){
            if(!mapTag.containsKey(t.getTagtype())){
                List<TMisDunningTag> tagList =  new ArrayList<>();
                tagList.add(t);
                mapTag.put(t.getTagtype(),tagList);
            }else {
                List<TMisDunningTag> tMisDunningTags = mapTag.get(t.getTagtype());
                tMisDunningTags.add(t);
                mapTag.put(t.getTagtype(),tMisDunningTags);
            }
        }
        model.addAttribute("mapTag",mapTag);
		//model.addAttribute("tags", tags);

		TMisDunningScoreCard tMisDunningScoreCard = riskQualityInfoService.getScoreCardByDealcode(dealcode);
		model.addAttribute("score", tMisDunningScoreCard == null ? "" : tMisDunningScoreCard.getGrade());

		User user = UserUtils.getUser();
		model.addAttribute("userId", user.getId());

		MemberInfo memberInfo = memberInfoService.getMemberInfo(dealcode);
		model.addAttribute("memberInfo", memberInfo);

		boolean hasReliefApply = false;
		TMisReliefamountHistory reliefamountHistory = tMisReliefamountHistoryService.getValidApply(dealcode, task.getDunningpeopleid());
		if (reliefamountHistory != null) {
			hasReliefApply = true;
		}
		model.addAttribute("hasReliefApply", hasReliefApply);
		//该订单是否有信函发送
		TMisDunningLetter letter = tMisDunningLetterDao.findLetterByDealcode(dealcode);
		model.addAttribute("letter", letter);
		return "modules/dunning/tMisDunningTaskFather";
	}
	
	/**
	 * 展示用户影像资料
	 * @param
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "showBuyerIdCardImg")
	public void showBuyerIdCardImg(HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String fileId = tMisDunningTaskService.findBuyerImg(buyerId);
		String riskadminUrl = DictUtils.getDictValue("riskportal", "orderUrl", "");
		String url = riskadminUrl + "file/showPic.a?fileId=" + fileId;
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		try {
			URL httpUrl = new URL(url);
			connection = (HttpURLConnection) httpUrl.openConnection();
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "image/jpg");
			connection.setRequestProperty("accept", "*/*");
			connection.connect();

			input = connection.getInputStream();
			response.setContentType("image/jpg");
			response.setHeader("Content-disposition", "filename=img_" + fileId + ".jpg");
			output = response.getOutputStream();
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = input.read(buf)) != -1) {
				output.write(buf, 0, len);
			}
			output.flush();
		} catch (IOException e) {
			logger.info("读取影像资料失败:" + e.getMessage());
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (IOException e) {
				//nothing to do
			}
		}
	}

	/**
	 * 加载用户信息页面
	 * @param buyerId
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "customerDetails")
	public String customerDetails(String overdueDays,String buyerId, String dealcode,String thisCreditAmount,String dunningtaskdbid,String dunningCycle,String  mobileSelf,Model model) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		try{
			DynamicDataSource.setCurrentLookupKey("dataSource_read");  
			TRiskBuyerWorkinfo workinfo =
					tRiskBuyerWorkinfoService.getWorkInfoByBuyerId(buyerId,dealcode).isEmpty()? 
							new TRiskBuyerWorkinfo() : tRiskBuyerWorkinfoService.getWorkInfoByBuyerId(buyerId,dealcode).get(0);
							
			List<TRiskBuyer2contacts> buyer2Contacts = tRiskBuyer2contactsService.getContactsByBuyerId(buyerId,dealcode); 
	//		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
	
			TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return null;
			}
			/*Map<String,Object> params = new HashMap<String,Object>();
			params.put("STATUS_DUNNING", "dunning");
			params.put("DEALCODE", dealcode);
			TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);*/
			boolean ispayoff = false;
			if (order != null && "payoff".equals(order.getStatus())) {
				ispayoff = true;
			}
			model.addAttribute("workInfo", workinfo);
			model.addAttribute("contacts", buyer2Contacts);
			model.addAttribute("dunningtaskdbid", dunningtaskdbid);
			model.addAttribute("buyerId", buyerId);
			model.addAttribute("dealcode", dealcode);
			model.addAttribute("mobileSelf", mobileSelf);
			
			model.addAttribute("ispayoff", ispayoff);

			model.addAttribute("dunningCycle", dunningCycle);
			model.addAttribute("overdueDays", overdueDays);
			model.addAttribute("thisCreditAmount",thisCreditAmount);
		} catch (Exception e) {
			logger.info("",e);
			return "error";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");  
		}
		return "modules/dunning/tMisDunningTaskCustomerDetails";
	}
	
	/**
	 * 加载通讯录页面
	 * @param mobileSelf
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "communicationDetails")
	public String communicationDetails(HttpServletRequest request, HttpServletResponse response,String mobileSelf, Model model) {
	    String thisCreditAmount = request.getParameter("thisCreditAmount");
		String dealcode = request.getParameter("dealcode");
		String buyerId = request.getParameter("buyerId");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		String dunningCycle = request.getParameter("dunningCycle");
		String overdueDays = request.getParameter("overdueDays");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		TBuyerContact tBuyerContact = new TBuyerContact();
		tBuyerContact.setBuyerId(buyerId);
		tBuyerContact.setDealcode(dealcode);
		List<TBuyerContact> contacts = null;
		//Page<TBuyerContact> contactPage = new Page<TBuyerContact>(request, response);
		/*TMisDunningTask task = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);*/
		TMisDunningOrder order = null;
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			contacts = tBuyerContactService.getBuyerContacts(dealcode, mobileSelf, buyerId);
			//contactPage = tBuyerContactService.findPage(contactPage, tBuyerContact);
			//task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		
//		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
//		model.addAttribute("personalInfo", personalInfo);
		
		//model.addAttribute("contactPage", contactPage);
		model.addAttribute("contacts", contacts);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("mobileSelf", mobileSelf);
		
		boolean ispayoff = false;
		if (order != null && "payoff".equals(order.getStatus())) {
			ispayoff = true;
		}
		model.addAttribute("ispayoff", ispayoff);
		model.addAttribute("dunningCycle", dunningCycle);
		model.addAttribute("overdueDays", overdueDays);
		model.addAttribute("thisCreditAmount",thisCreditAmount);
		return "modules/dunning/tMisDunningTaskCommunication";
	}
	
	/**
	 * 加载通话记录页面
	 * @param mobileSelf
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "communicationRecord")
	public String communicationRecord(HttpServletRequest request, HttpServletResponse response,String mobileSelf, Model model) {
	    String thisCreditAmount = request.getParameter("thisCreditAmount");
		String dealcode = request.getParameter("dealcode");
		String buyerId = request.getParameter("buyerId");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		String dunningCycle = request.getParameter("dunningCycle");
		String overdueDays = request.getParameter("overdueDays");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		TRiskBuyerContactRecords  tRiskBuyerContactRecords = new TRiskBuyerContactRecords();
		tRiskBuyerContactRecords.setBuyerId(buyerId);
		tRiskBuyerContactRecords.setDealcode(dealcode);
		Page<TRiskBuyerContactRecords> communicationsPage = new Page<TRiskBuyerContactRecords>(request, response, 50);		
//		communicationsPage = tRiskBuyerContactRecordsService.findPage(communicationsPage, tRiskBuyerContactRecords);
		communicationsPage = tRiskBuyerContactRecordsService.findPageByRediscache(communicationsPage, tRiskBuyerContactRecords,buyerId,mobileSelf);
//		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
//		model.addAttribute("personalInfo", personalInfo);
		model.addAttribute("communicationsPage", communicationsPage);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("mobileSelf", mobileSelf);
		
		/*Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);*/
		
		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		if (order == null) {
			logger.warn("订单不存在，订单号：" + dealcode);
			return "views/error/500";
		}
		boolean ispayoff = false;
		if (order != null && "payoff".equals(order.getStatus())) {
			ispayoff = true;
		}
		model.addAttribute("ispayoff", ispayoff);
		model.addAttribute("dunningCycle", dunningCycle);
		model.addAttribute("overdueDays", overdueDays);
		model.addAttribute("thisCreditAmount",thisCreditAmount);
		return "modules/dunning/tMisDunningTaskCommunicationRecord";
	}
	
	/**
	 * 加载历史借款信息页面
	 * @param buyerId dealcode dunningCycle overdueDays dunningtaskdbid
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "orderHistoryList")
	public String orderHistoryList( String buyerId,String dealcode,String dunningCycle,String thisCreditAmount, String overdueDays,String dunningtaskdbid,HttpServletRequest request, HttpServletResponse response,String mobileSelf, Model model) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		List<OrderHistory> orderHistories = tMisDunningTaskService.findOrderHistoryList(buyerId);
		model.addAttribute("orderHistories", orderHistories);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		
//		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
//		model.addAttribute("personalInfo", personalInfo);
		
		/*Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);*/
		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		if (order == null) {
			logger.warn("订单不存在，订单号：" + dealcode);
			return "views/error/500";
		}
		boolean ispayoff = false;
		if (order != null && "payoff".equals(order.getStatus())) {
			ispayoff = true;
		}
		model.addAttribute("ispayoff", ispayoff);
		model.addAttribute("dunningCycle", dunningCycle);
		model.addAttribute("overdueDays", overdueDays);
		model.addAttribute("mobileSelf", mobileSelf);
		model.addAttribute("thisCreditAmount",thisCreditAmount);
		return "modules/dunning/tMisDunningOrderHistoryList";
	}

/**
 * @Description  还款流水信息
 * @param dealcode
 * @param model
 * @return java.lang.String
 */
@RequiresPermissions("dunning:tMisDunningTask:view")
@RequestMapping(value = "orderSerialRepayList")
public String orderHistoryList(SerialRepay serialRepay, String dealcode, Model model) {
	if (StringUtils.isBlank(dealcode)){
		return "views/error/404";
	}

	List<SerialRepay> repayList = new LinkedList<>();
	RepayWay needRepayWay = serialRepay.getRepayWay();

	//获取代收还款流水
	if (needRepayWay == null || needRepayWay.equals(RepayWay.AGENCY_COLLECT)){
		try {
			repayList.addAll(orderManager.findSerialRepayMsg(dealcode));
		} catch (IOException e) {
			model.addAttribute("message","获取代收流水失败, 请稍后刷新重试");
		}
	}
	//获取代扣还款流水
	if (needRepayWay == null || needRepayWay.equals(RepayWay.AGENCY_DEDUCT)){
		repayList.addAll(tMisDunningDeductService.findDeductSerialRepay(dealcode));
	}
	//获取线下还款流水(对公)
	if (needRepayWay == null || needRepayWay.equals(RepayWay.SELF_OFFLINE)){
		repayList.addAll(tMisRemittanceConfirmService.findRemittanceSerialRepay(dealcode));
	}

	//根据查询还款状态过滤流水
	PayStatus needPayStatus = serialRepay.getRepayStatus();
	if (needPayStatus != null){
		Iterator<SerialRepay> iterator = repayList.iterator();
		while (iterator.hasNext()) {
			SerialRepay repay = iterator.next();
			if (!needPayStatus.equals(repay.getRepayStatus())){
				iterator.remove();
			}
		}
	}

	//按还款时间倒序
	Collections.sort(repayList, new Comparator<SerialRepay>() {
		@Override
		public int compare(SerialRepay o1, SerialRepay o2) {
			Date o1RepayTime = o1.getRepayTime();
			long o1Timestamp ;
			if (o1RepayTime == null){
				o1Timestamp = 0l;
			}else {
				o1Timestamp = o1RepayTime.getTime();
			}

			Date o2RepayTime = o2.getRepayTime();
			long o2Timestamp ;
			if (o2RepayTime == null){
				o2Timestamp = 0l;
			}else {
				o2Timestamp = o2RepayTime.getTime();
			}

			if (o1Timestamp < o2Timestamp){
				return 1;
			}
			return -1;
		}
	});

	model.addAttribute("repayList",repayList);
	model.addAttribute("dealcode",dealcode);
	model.addAttribute("repayWays",RepayWay.values());
	return "modules/dunning/orderSerialRepayList";
}

	/**
	 * 加载登录日志页面
	 * @param buyerId dealcode dunningCycle overdueDays dunningtaskdbid
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "apploginlogList")
	public String apploginlogList(String buyerId,String thisCreditAmount,String dealcode,String dunningCycle,String overdueDays,String dunningtaskdbid,HttpServletRequest request, HttpServletResponse response,String mobileSelf, Model model) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||mobileSelf==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)||"".equals(mobileSelf)){
			return "views/error/500";
		}
		try {
			DbUtils dbUtils = new DbUtils();
			List<AppLoginLog> appLoginLogs = dbUtils.getApploginlog(mobileSelf);
			model.addAttribute("appLoginLogs", appLoginLogs);
//			DynamicDataSource.setCurrentLookupKey("dataSource2");  
//			List<AppLoginLog> appLoginLogs = tMisDunningTaskService.findApploginlog(mobile);
//			DynamicDataSource.setCurrentLookupKey("dataSource");  
			model.addAttribute("dunningtaskdbid", dunningtaskdbid);
			model.addAttribute("buyerId", buyerId);
			model.addAttribute("dealcode", dealcode);
			model.addAttribute("mobileSelf", mobileSelf);
			
			/*Map<String,Object> params = new HashMap<String,Object>();
			params.put("STATUS_DUNNING", "dunning");
			params.put("DEALCODE", dealcode);
			TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);*/
			TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			boolean ispayoff = false;
			if (order != null && "payoff".equals(order.getStatus())) {
				ispayoff = true;
			}
			model.addAttribute("ispayoff", ispayoff);
			model.addAttribute("dunningCycle", dunningCycle);
			model.addAttribute("overdueDays", overdueDays);
			model.addAttribute("thisCreditAmount",thisCreditAmount);
		} catch (Exception e) {
			logger.info("",e);
		}
		return "modules/dunning/tAppLoginLogList";
	}
	
	
	
	/**
	 * 加载催收短信页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "collectionSms")
	public String collectionSms(TMisDunningTask tMisDunningTask, Model model,HttpServletRequest request, HttpServletResponse response) {
//		model.addAttribute("tMisDunningTask", tMisDunningTask);
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		String mobileSelf = request.getParameter("mobileSelf");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		
		if (order == null) {
			logger.warn("订单不存在，订单号：" + dealcode);
			return null;
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		
		if (task == null) {
			logger.warn("任务不存在，订单号：" + dealcode);
			return null;
		}

		String contatType="";
		String contactMobile = request.getParameter("contactMobile");
		String contactstype = request.getParameter("contactstype");
		if(StringUtils.isNotBlank(contactstype)){
			contactstype=contactstype.toUpperCase();
			if(!contactstype.equals("SELF")){
				contatType ="others";
			}
			if(contactstype.equals("SELF")){
				contatType ="self";
			}
		}
		//获取相对应的 短信模板
		List<TmisDunningSmsTemplate> smsTemplateList = tstService.findSmsTemplate(contactstype,order,task);
		if(null!=smsTemplateList&&smsTemplateList.size()!=0)
		model.addAttribute("tSTemplate", smsTemplateList.get(0));
		model.addAttribute("smsTeplateList", smsTemplateList);
		model.addAttribute("contactstype", contactstype);
		model.addAttribute("selfMobile", null != contactMobile && !"undefined".equals(contactMobile) ? contactMobile:"" );
		model.addAttribute("mobileSelf", mobileSelf);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("contatType", contatType);
		return "modules/dunning/dialog/dialogCollectionSms";
	}
	
	/**
	 * 加载催收电话页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "collectionTel")
	public String collectionTel(TMisDunningTask tMisDunningTask, Model model, HttpServletRequest request, HttpServletResponse response) {

		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		String contactMobile = request.getParameter("contactMobile");
		String contactstype = request.getParameter("contactstype");
		//获取MobileResult中文名称并传回前端
		model.addAttribute("mobileResultMap", MobileResult.getActions());

		model.addAttribute("contactMobile", null != contactMobile && !"undefined".equals(contactMobile) ? contactMobile:"" );
		model.addAttribute("contactstype", null != contactstype && !"undefined".equals(contactMobile) ? contactstype.toUpperCase() :"");
		
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		return "modules/dunning/dialog/dialogCollectionTel";
	}
	
	/**
	 * 加载电催结论页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "collectionTelConclusion")
	public String collectionTelConclusion(TMisDunningTask tMisDunningTask, Model model, HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		String actions = request.getParameter("actions");
		String[] actionsArr = actions.split(",");
		
		TMisDunnedConclusion tMisDunnedConclusion = new TMisDunnedConclusion();
		
		tMisDunnedConclusion.setActions(Arrays.asList(actionsArr));
		tMisDunnedConclusion.setBuyerid(Integer.valueOf(buyerId));
		
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("actions", actionsArr);
		return "modules/dunning/dialog/dialogCollectionTelConclusion";
	}
	
	/**
	 * 加载敏感标签页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "collectionTag")
	public String collectionTag(TMisDunningTask tMisDunningTask, Model model, HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String tagName = request.getParameter("tagName");
		String tagOpr = "save";
		if (StringUtils.isNotBlank(tagName)) {
		    //新增备注回显
			tagOpr = "edit";
            TMisDunningTag tMisDunningTag1 = new TMisDunningTag();
            tMisDunningTag1.setBuyerid(buyerId);
            TagType tagType = TagType.valueOf(tagName);
            tMisDunningTag1.setTagtype(tagType);
            model.addAttribute("tMisDunningTag", tMisDunningTag1);
//            TMisDunningTag tMisDunningTag = tMisDunningTagService.get(tagId);
//			model.addAttribute("tagId", tagId);
//			model.addAttribute("tMisDunningTag", tMisDunningTag);
		} else {
		    //新增标签
			TMisDunningTag tMisDunningTag = new TMisDunningTag();
			tMisDunningTag.setBuyerid(buyerId);
			List<TMisDunningTag> tags = tMisDunningTagService.findList(tMisDunningTag);
			
			if(tags != null && tags.size() > 0) {
				Map<String, String> exist = new HashMap<String, String>();
				for (TMisDunningTag tag : tags) {
					exist.put(tag.getTagtype().name(), tag.getTagtype().getDesc());
				}
				model.addAttribute("exist", exist);
			}
		}
		
		if(buyerId == null || dealcode == null || "".equals(buyerId) || "".equals(dealcode)){
			return "views/error/500";
		}
		
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("tagOpr", tagOpr);
		return "modules/dunning/dialog/dialogCollectionTag";
	}

	/**
	 * 加载信息修复新增页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:informationRecovery")
	@RequestMapping(value = "collectionInformationAdd")
	public String collectionInformationAdd(TMisDunningTask tMisDunningTask, Model model,HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
			return "views/error/500";
		}
		model.addAttribute("buyerid", buyerId);
		model.addAttribute("dealCode",dealcode);
		model.addAttribute("method","add");

		return "modules/dunning/dialog/dialogInformationRecoveryAdd";
	}

	/**
	 * 保存信息修复信息
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:informationRecovery")
	@ResponseBody
	@RequestMapping(value = "informationSave")
	public String InformationSave(DunningInformationRecovery dunningInformationRecovery, Model model,HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerid");
		if(buyerId==null||"".equals(buyerId)){
			return "借贷人信息不完全，无法执行此操作";
		}
		dunningInformationRecovery.setBuyerId(Integer.valueOf(buyerId));
		tMisDunningInformationRecoveryService.saveInformationRecovery(dunningInformationRecovery);
		taskIssueService.autoResolution(dunningInformationRecovery.getDealCode(),IssueType.CONTACT_REMARK,"已添加",UserUtils.getUser());
		return "OK";
	}

	/**
	 * 加载信息修复修改页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:informationRecovery")
	@RequestMapping(value = "collectionInformationChange")
	public String collectionInformationChange	(Model model,HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String id = request.getParameter("id");
		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
			return "views/error/500";
		}
		DunningInformationRecovery dunningInformationRecovery = new DunningInformationRecovery();
		dunningInformationRecovery.setBuyerId(Integer.valueOf(buyerId));
		dunningInformationRecovery.setDealCode(dealcode);
		dunningInformationRecovery.setId(id);
		DunningInformationRecovery historyRecord = tMisDunningInformationRecoveryService.findInformationRecoveryList(dunningInformationRecovery).get(0);
		model.addAttribute("DunningInformationRecovery", historyRecord);
		model.addAttribute("method","update");
		model.addAttribute("buyerid",buyerId);
		model.addAttribute("dealCode",dealcode);
		model.addAttribute("id",id);
		return "modules/dunning/dialog/dialogInformationRecoveryAdd";
	}

	/**
	 * 信息修复保存修改页面数据
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:informationRecovery")
	@ResponseBody
	@RequestMapping(value = "informationUpdate")
	public String InformationUpdate(DunningInformationRecovery dunningInformationRecovery, Model model,HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerid");
		String dealcode = request.getParameter("dealCode");
		String id = request.getParameter("id");
		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
			return "借贷人信息不完全，无法执行此操作";
		}
		dunningInformationRecovery.setBuyerId(Integer.valueOf(buyerId));
		dunningInformationRecovery.setId(id);
		if (dunningInformationRecovery.getContactRelationship() ==null || "".equals(dunningInformationRecovery.getContactRelationship())
			||dunningInformationRecovery.getContactName() == null || "".equals(dunningInformationRecovery.getContactName())){
			dunningInformationRecovery.setContactName("");
			dunningInformationRecovery.setContactRelationship("");
		}
		tMisDunningInformationRecoveryService.updateInformationRecovery(dunningInformationRecovery);
		return "OK";
	}


	/**
	 * 加载信息修复-历史记录列表页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:informationRecovery")
	@RequestMapping(value = "collectionHistoryRecordList")
	public String collectionHistoryRecordList(TMisDunningTask tMisDunningTask, Model model,HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String id = request.getParameter("id");
		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
			return "views/error/500";
		}
		DunningInformationRecoveryHistoryRecord dunningInformationRecoveryHistoryRecord = new DunningInformationRecoveryHistoryRecord();
		dunningInformationRecoveryHistoryRecord.setBuyerId(Integer.valueOf(buyerId));
		dunningInformationRecoveryHistoryRecord.setDealCode(dealcode);
		dunningInformationRecoveryHistoryRecord.setId(id);
		List<DunningInformationRecoveryHistoryRecord> historyRecordList = tMisDunningInformationRecoveryService.findInformationRecoveryHistoryRecordList(dunningInformationRecoveryHistoryRecord);
		model.addAttribute("historyRecordList", historyRecordList);
		return "modules/dunning/dialog/dialogInformationRecoveryRecordList";
	}


	/**
	 * 加载信息修复-历史记录新增页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:informationRecovery")
	@RequestMapping(value = "collectionHistoryRecordAdd")
	public String collectionHistoryRecordAdd(DunningInformationRecoveryHistoryRecord dunningInformationRecoveryHistoryRecord, Model model, HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealCode = request.getParameter("dealcode");
		String id =request.getParameter("id");
		if(buyerId==null||dealCode==null||"".equals(buyerId)||"".equals(dealCode)){
			return "views/error/500";
		}
		model.addAttribute("buyerid", buyerId);
		model.addAttribute("dealCode",dealCode);
		model.addAttribute("id",id);
		return "modules/dunning/dialog/dialogInformationRecoveryRecordAdd";
	}

	/**
	 * 信息修复-保存历史记录新增

	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:informationRecovery")
	@ResponseBody
	@RequestMapping(value = "saveHistoryRemark")
	public String saveHistoryRemark(Model model , HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerid");
		String dealCode = request.getParameter("dealCode");
		String id = request.getParameter("id");
		String historyRemarks = request.getParameter("historyRemarks");
		if(buyerId==null||dealCode==null||"".equals(buyerId)||"".equals(dealCode)){
			return "借贷人信息不完全，无法执行此操作";
		}
		DunningInformationRecoveryHistoryRecord dunningInformationRecoveryHistoryRecord = new DunningInformationRecoveryHistoryRecord();
		dunningInformationRecoveryHistoryRecord.setBuyerId(Integer.valueOf(buyerId));
		dunningInformationRecoveryHistoryRecord.setDealCode(dealCode);
		dunningInformationRecoveryHistoryRecord.setHistoryRemark(historyRemarks);
		dunningInformationRecoveryHistoryRecord.setId(id);
		tMisDunningInformationRecoveryService.saveInformationRecoveryHistoryRecord(dunningInformationRecoveryHistoryRecord);
		return "OK";
	}

	/**
	 * 加载催收代付页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "collectionPaid")
	public String collectionPaid(TMisDunningTask tMisDunningTask, Model model,HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
			return "views/error/500";
		}
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		
		Map<String,Object> maps = new HashMap<String,Object>();
		maps.put("buyerId",buyerId);
		
		TRiskBuyerPersonalInfo personalInfo = null;
		TMisDunningTask task = null;
		TMisDunningOrder order = null;
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
			if (task == null) {
				logger.warn("任务不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			
			order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		model.addAttribute("personalInfo", personalInfo);
		
		model.addAttribute("task", task);
		
		BigDecimal delayAmount = new BigDecimal(0l);
		if(personalInfo != null && StringUtils.isNotBlank(personalInfo.getOverdueDays())){
			if(Integer.valueOf(personalInfo.getOverdueDays()) < 15){
				
				BigDecimal cpAmt = new BigDecimal(0L);
				if(order.getCreditAmount() != null && 
					(
						(order.getCouponId() != null && order.getCouponId() > 0)
						||(order.getSubCostAmount()!=null&&order.getSubCostAmount().compareTo(BigDecimal.ZERO)>0)
					)
				){
					cpAmt = order.getAmount().subtract(order.getCreditAmount());
				}
				
				BigDecimal defaultInterestAmount = getDefaultDelayAmount(order);
				
				//续期费用 = 7天或者14天续期费用 +续期手续费用（20元或者30元）+逾期费 + 订单手续费
//				delayAmount = order.getCostAmount().add(defaultInterestAmount).subtract(cpAmt).add(order.getOverdueAmount());
				//续期费用 = 7天或者14天续期费用 +续期手续费用（20元或者30元）+逾期费 + 订单手续费  - 减免费用
				delayAmount = order.getCostAmount().add(defaultInterestAmount).subtract(cpAmt).add(order.getOverdueAmount()).subtract(order.getReliefflag() == 1 ? order.getReliefamount() : new BigDecimal(0));
			}
		}
		
		model.addAttribute("delayAmount", delayAmount);
		return "modules/dunning/dialog/dialogCollectionPaid";
	}
	
	
	/**
	 * 加载催收代扣页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "collectionDeduct")
	public String collectionDeduct(Model model, HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
			return "views/error/500";
		}
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		//boolean isDelayable = order.isDelayable();
		
		TRiskBuyerPersonalInfo personalInfo = null;
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
			if (task == null) {
				logger.warn("任务不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			
			TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			personalInfo = personalInfoDao.getNewBuyerInfoByDealcode(dealcode);
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		TMisChangeCardRecord tMisChangeCardRecord = tMisChangeCardRecordService.getCurrentBankCard(dealcode);
		
		if (tMisChangeCardRecord != null && personalInfo != null) {
			personalInfo.setRemitBankNo(tMisChangeCardRecord.getBankcard());
			personalInfo.setRemitBankName(tMisChangeCardRecord.getBankname());
			personalInfo.setMobile(tMisChangeCardRecord.getMobile());
			personalInfo.setIdcard(tMisChangeCardRecord.getIdcard());
		}
		
		List<PayChannelInfo> payChannelList = tMisDunningDeductService.getPaychannelList(personalInfo.getRemitBankName(), personalInfo.getRemitBankNo());
		
		model.addAttribute("personalInfo", personalInfo);
		model.addAttribute("payChannelList", payChannelList);

		int result = tMisRemittanceConfirmService.getResult(dealcode);
		model.addAttribute("result", result);
		//model.addAttribute("isDelayable", isDelayable);
		return "modules/dunning/dialog/dialogCollectionDeduct";
	}
	

	/**
	 * 完成代付
	 * @param
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "paidDeal")
	@ResponseBody
	public TMisPaid paidDeal(TMisPaid paid) {
		String buyerId = paid.getBuyerId();
		String dealcode = paid.getDealcode();
		String paidAmount = paid.getPaidAmount();
		String paidType = paid.getPaidType();
		String paychannel = paid.getPaychannel();
		String delayDay = paid.getDelayDay();
		if(StringUtils.isBlank(delayDay)){
			delayDay = "0";
		}
		
		if(StringUtils.isBlank(buyerId) || StringUtils.isBlank(paidAmount) || StringUtils.isBlank(dealcode)){
			paid.setCode("9999");
			paid.setMsg("错误，用户或者订单不存在");
			return paid;
		}
		if(StringUtils.isBlank(paidType) || StringUtils.isBlank(paychannel)){
			paid.setCode("9999");
			paid.setMsg("错误，代付类型或代付渠道不存在");
			return paid;
		}
		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		
		if (order == null) {
			paid.setCode("9999");
			paid.setMsg("错误，用户或者订单不存在");
			return paid;
		}
		
		if(order.status.equals("payoff")){
			paid.setCode("9999");
			paid.setMsg("错误，订单已还清");
			return paid;
		}
		
		String riskUrl =  DictUtils.getDictValue("risk","domains","");
		String url = riskUrl + "riskportal/limit/order/v1.0/payForStaff/" +buyerId+ "/" +paidType+ "/" +delayDay+ "/" +paidAmount+ "/" +paychannel;
		logger.info("接口url：" + url);
		String payUrl = "";
		try {
			String res = GetRequest.getRequest(url, new HashMap<String,String>());
			logger.info("接口url返回参数" + res);
			if(StringUtils.isNotBlank(res)){
				JSONObject repJson = new JSONObject(res);
				String resultCode =  repJson.has("resultCode") ? String.valueOf(repJson.get("resultCode")) : "";
				if(StringUtils.isNotBlank(resultCode) && "200".equals(resultCode)){
					payUrl = repJson.has("datas") ? String.valueOf(repJson.get("datas")) : "";
					logger.info("支付跳转url：" + payUrl);
				}
			}
		} catch (IOException e) {
			logger.info("",e);
		}
		
		if(StringUtils.isNotBlank(payUrl)){
			paid.setCode("0000");
			paid.setMsg("返回成功");
			paid.setRedirectUrl(payUrl);
			return paid;
		}else{
			paid.setCode("9999");
			paid.setMsg("支付跳转url为空");
			return paid;
		}
	}

	
	/**
	 * 完成代付
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "confrimPay")
	@ResponseBody
	public TMisPaid confrimPay(TMisPaid paid) {
		String dealcode = paid.getDealcode();
		String paychannel = paid.getPaychannel();
		String delayDay = paid.getDelayDay();
		String remark = paid.getRemark();
		String paidType = paid.getPaidType();
		String paidAmount = paid.getPaidAmount();
		
		if(StringUtils.isBlank(delayDay)){
			delayDay = "0";
		}
		
		if( StringUtils.isBlank(paidAmount) || StringUtils.isBlank(dealcode)){
			paid.setCode("9999");
			paid.setMsg("错误，用户或者订单不存在");
			return paid;
		}
		if(StringUtils.isBlank(paidType) || StringUtils.isBlank(paychannel)){
			paid.setCode("9999");
			paid.setMsg("错误，代付类型或代付渠道不存在");
			return paid;
		}
		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		
		if (order == null) {
			paid.setCode("9999");
			paid.setMsg("错误，用户或者订单不存在");
			return paid;
		}
		
		if(order.status.equals("payoff")){
			paid.setCode("9999");
			paid.setMsg("错误，订单已还清");
			return paid;
		}
		
		String riskUrl =  DictUtils.getDictValue("risk","domains","");
		String url = riskUrl + "riskportal/limit/order/v1.0/transferForStaff/" +dealcode+ "/" +paychannel+ "/" +delayDay+ "/" +remark+ "/" +paidType+ "/" +paidAmount;
		logger.info("接口url：" + url);
		String datas = "";
		try {
			String res = GetRequest.getRequest(url, new HashMap<String,String>());
			logger.info("接口url返回参数" + res);
			if(StringUtils.isNotBlank(res)){
				JSONObject repJson = new JSONObject(res);
				String resultCode =  repJson.has("resultCode") ? String.valueOf(repJson.get("resultCode")) : "";
				if(StringUtils.isNotBlank(resultCode) && "200".equals(resultCode)){
					datas = repJson.has("datas") ? String.valueOf(repJson.get("datas")) : "";
					logger.info("返回参数" + datas);
				}
			}
		} catch (IOException e) {
			logger.info("",e);
		}
		
		if(StringUtils.isNotBlank(datas)){
			paid.setCode("0000");
			paid.setMsg("返回成功");
			paid.setRedirectUrl(datas);
			return paid;
		}else{
			paid.setCode("9999");
			paid.setMsg("调用接口失败");
			return paid;
		}
	}
	
	/**
	 * 保存
	 * @param tMisDunningTask
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:edit")
	@RequestMapping(value = "save")
	public String save(TMisDunningTask tMisDunningTask, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunningTask)){
			return form(tMisDunningTask, model);
		}
		tMisDunningTaskService.save(tMisDunningTask);
		addMessage(redirectAttributes, "保存催收任务成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningTask/?repage";
	}
	
	/**
	 * 删除
	 * @param tMisDunningTask
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisDunningTask tMisDunningTask, RedirectAttributes redirectAttributes) {
		tMisDunningTaskService.delete(tMisDunningTask);
		addMessage(redirectAttributes, "删除催收任务成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningTask/?repage";
	}
	
	//计算订单金额带来的续期手续费
	public static BigDecimal getDefaultDelayAmount(TMisDunningOrder order){
		return order.getAmount().compareTo(new BigDecimal(1500)) >= 0? new BigDecimal("30") : new BigDecimal("20");
	}
	
	
	/**
	 * 催收绩效月表
	 * @param performanceMonthReport
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:viewReport")
	@RequestMapping(value = {"findPerformanceMonthReport", ""})
	public String findPerformanceMonthReport(PerformanceMonthReport performanceMonthReport, TMisDunningPeople dunningPeople,HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Integer year = Integer.parseInt(DateUtils.formatDate(null == performanceMonthReport.getDatetime()  ? new Date() : performanceMonthReport.getDatetime() , "yyyy"));
			Integer month = Integer.parseInt(DateUtils.formatDate(null == performanceMonthReport.getDatetime()  ? new Date() : performanceMonthReport.getDatetime() , "MM"));
			performanceMonthReport.setDatetime_start(DateUtils.getDateFirstDayOfMonth(year, month));
			performanceMonthReport.setDatetime_end(DateUtils.getDateLastDayOfMonth(year, month));
			performanceMonthReport.setDatetime(null == performanceMonthReport.getDatetime()  ? DateUtils.getDateToMonth(new Date()) :  DateUtils.getDateToMonth(performanceMonthReport.getDatetime()));
			
			Page<PerformanceMonthReport> page = tMisDunningTaskService.findPerformanceMonthReport(new Page<PerformanceMonthReport>(request, response), performanceMonthReport); 
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findList(dunningPeople);
			model.addAttribute("dunningPeoples", dunningPeoples);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.info("",e);
		}
		return "modules/dunning/performanceMonthReportList";
	}
	
	
	/**
	 * 导出催收绩效月表
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:viewReport")
    @RequestMapping(value = "performanceMonthReportExport", method=RequestMethod.POST)
    public String performanceMonthReportExport(PerformanceMonthReport performanceMonthReport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			Integer year = Integer.parseInt(DateUtils.formatDate(null == performanceMonthReport.getDatetime()  ? new Date() : performanceMonthReport.getDatetime() , "yyyy"));
			Integer month = Integer.parseInt(DateUtils.formatDate(null == performanceMonthReport.getDatetime()  ? new Date() : performanceMonthReport.getDatetime() , "MM"));
			performanceMonthReport.setDatetime_start(DateUtils.getDateFirstDayOfMonth(year, month));
			performanceMonthReport.setDatetime_end(DateUtils.getDateLastDayOfMonth(year, month));
			performanceMonthReport.setDatetime(null == performanceMonthReport.getDatetime()  ? DateUtils.getDateToMonth(new Date()) :  DateUtils.getDateToMonth(performanceMonthReport.getDatetime()));
			
            String fileName = "performanceMonthReport" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<PerformanceMonthReport> page = tMisDunningTaskService.findPerformanceMonthReport(performanceMonthReport);
    		new ExportExcel("导出催收绩效月表", PerformanceMonthReport.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/dunning/tMisDunningTask/findPerformanceMonthReport?repage";
    }

	/**
	 * 信息修复
	 * @param tMisDunningTask
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:informationRecovery")
	@RequestMapping(value = "informationRecovery")
	public String informationRecoveryDetails(TMisDunningTask tMisDunningTask, Model model, HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
			return "views/error/500";
		}
		DunningInformationRecovery dunningInformationRecovery = new DunningInformationRecovery();
		dunningInformationRecovery.setBuyerId(Integer.valueOf(buyerId));
		dunningInformationRecovery.setDealCode(dealcode);
		List<DunningInformationRecovery> entityList = tMisDunningInformationRecoveryService.findInformationRecoveryList(dunningInformationRecovery);
		for (DunningInformationRecovery informationRecovery: entityList) {
			if (informationRecovery.getContactType() ==null || "".equals(informationRecovery.getContactType())){
				continue;
			}
			informationRecovery.setContactType(DunningInformationRecovery.ContactTypeENUM.
					valueOf(informationRecovery.getContactType()).getContactTypeName());
			if(informationRecovery.getContactRelationship() == null ||"".equals(informationRecovery.getContactRelationship())){
				continue;
			}
			informationRecovery.setContactRelationship(DunningInformationRecovery.ContactRelationshipENUM.
					valueOf(informationRecovery.getContactRelationship()).getContactRelationshipName());
		}
		model.addAttribute("entityList", entityList);
		return "modules/dunning/tMisDunningInformationRecovery";
	}
	
	
 	/**
 	 * 获取江湖救急该笔订单状态
 	 *
 	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "orderStatus")
	@ResponseBody
	public String getOrderStatus(String dealcode) throws IOException {
		try {
			TRiskOrder riskOrder = orderManager.findByDealcode(dealcode);
			if (("payoff").equals(riskOrder.getStatus())) {
				tMisDunningOrderDao.orderSynUpdate(riskOrder);
				taskIssueService.autoResolution(dealcode, IssueType.WRITE_OFF,"订单已还清",UserUtils.getUser());
				return "OK";
			}
			if (("payment").equals(riskOrder.getStatus())) {
				return "NO";
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return "";
		}

		return "";
	}
}