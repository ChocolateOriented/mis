/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.gamaxpay.commonutil.web.GetRequest;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.AppLoginLog;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.DunningOuterFile;
import com.mo9.risk.modules.dunning.entity.DunningSmsTemplate;
import com.mo9.risk.modules.dunning.entity.OrderHistory;
import com.mo9.risk.modules.dunning.entity.PerformanceDayReport;
import com.mo9.risk.modules.dunning.entity.PerformanceMonthReport;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.SmsTemp;
import com.mo9.risk.modules.dunning.entity.TMisDunnedConclusion;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisPaid;
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory;
import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyer2contacts;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerWorkinfo;
import com.mo9.risk.modules.dunning.service.TBuyerContactService;
import com.mo9.risk.modules.dunning.service.TMisContantRecordService;
import com.mo9.risk.modules.dunning.service.TMisDunnedConclusionService;
import com.mo9.risk.modules.dunning.service.TMisDunnedHistoryService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.modules.dunning.service.TMisReliefamountHistoryService;
import com.mo9.risk.modules.dunning.service.TMisRemittanceConfirmService;
import com.mo9.risk.modules.dunning.service.TRiskBuyer2contactsService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerContactRecordsService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerPersonalInfoService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerWorkinfoService;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

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
	private TMisDunnedHistoryService tMisDunnedHistoryService;
	
	@Autowired
	private TMisReliefamountHistoryService tMisReliefamountHistoryService;
	
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
	private TMisDunnedConclusionService tMisDunnedConclusionService;
	@Autowired
	private TMisDunningGroupService tMisDunningGroupService;
	
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
		Page<DunningOrder> page = tMisDunningTaskService.findOrderPageList(new Page<DunningOrder>(request, response), dunningOrder); 
		//催收小组列表
		model.addAttribute("groupList", tMisDunningGroupService.findList(new TMisDunningGroup()));
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
		model.addAttribute("page", page);
		return "modules/dunning/tMisDunningTaskList";
	}
	
	/**
	 * 加载群发催收短信页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "collectionGroupSms")
	public String collectionGroupSms( Model model) {
//		System.out.println("");
		model.addAttribute("smsTemplates", DunningSmsTemplate.values());
		return "modules/dunning/dialog/dialogCollectionGroupSms";
	}
	
	
	/**
	 * 群发短信获取模板
	 * @param tMisDunningTask
	 * @param model
	 * @param redirectAttributes
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
			
//			String message =  tMisContantRecordService.getDunningSmsTemplate
//					(new DunningOrder(
//							strTemplate[0],
//								"".equals(strTemplate[1]) ?  Double.parseDouble(strTemplate[1]) : 0 , 
//									"".equals(strTemplate[2]) ?  Integer.parseInt(strTemplate[2]) : 0 )
//					,DunningSmsTemplate.valueOf(smsTemplate));
			String message =  tMisContantRecordService.getDunningSmsTemplate("【XXX】" ,new DunningOrder("xxx", 0D, 0), DunningSmsTemplate.valueOf(smsTemplate));
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 群发短信
	 * @param tMisDunningTask
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "groupSmsSend")
	@ResponseBody
	public String groupSmsSend(String ordersStr,String smsTemplate ,Model model,String taskidStr) {
		String[] strOrders = ordersStr.split(",");
		Set<String> set = new HashSet<String>(Arrays.asList(strOrders));
		Iterator<String> it = set.iterator();  
		while (it.hasNext()) {
			String[] strTemplate = it.next().split("#");
			if(!"".equals(strTemplate[0]) && !"".equals(strTemplate[1]) && !"".equals(strTemplate[2])){
//				strTemplate[3];   获取手机号码
				
				/**
				 *  发送短信
				 *//*
				Map<String,String> params = new HashMap<String,String>();
				params.put("mobile", strTemplate[3]);
				params.put("message",message);
				MsfClient.instance().requestFromServer(ServiceAddress.SNC_SMS, params, BaseResponse.class);*/
				
				
				
				String dealcode = strTemplate[0];
				TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
				if (order == null) {
					logger.warn("订单不存在，订单号：" + dealcode);
					continue;
				}
				
				String platformExt = order.getPlatformExt();
				String route = "【mo9】";
				if (platformExt != null && platformExt.contains("feishudai")) {
					route = "[飞鼠贷]";
				}
				String message =  tMisContantRecordService.getDunningSmsTemplate
						(route, new DunningOrder(strTemplate[0],Double.parseDouble(strTemplate[1]) * 100, Integer.parseInt(strTemplate[2])),DunningSmsTemplate.valueOf(smsTemplate));
				
				Map<String,Object> parameter = new HashMap<String,Object>();
				parameter.put("STATUS_DUNNING", "dunning");
				parameter.put("DEALCODE", dealcode);
				TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(parameter);
				
				if (task == null) {
					logger.warn("任务不存在，订单号：" + dealcode);
					continue;
				}
				TMisContantRecord t =new TMisContantRecord();
				t.setSmstemp(TMisContantRecord.SmsTemp.valueOf(smsTemplate));
				t.setContactstype(TMisContantRecord.ContactsType.SELF);
				t.setContanttarget(strTemplate[3]);
				t.setContanttype(TMisContantRecord.ContantType.sms);
				t.setContent(message);
				tMisContantRecordService.saveRecord(task,order, t,null);
			}
		}
		String[] taskids = taskidStr.split(",");
		tMisDunningTaskDao.updatedunningtimeList(Arrays.asList(taskids));
		return "OK";
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
                    e.printStackTrace();
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
            e.printStackTrace();
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
	/*催收留案功能-留案保存 Patch 0001 by GQWU at 2016-11-9 end*/
	
//	/**
//	 * 加载手动分配页面
//	 * @param tMisDunningTask
//	 * @param model
//	 * @return
//	 */
//	@RequiresPermissions("dunning:tMisDunningTask:directorview")
//	@RequestMapping(value = "dialogDistribution")
//	public String dialogDistribution( Model model,String orders,String[] overduedays) {
//		try {
//			if(null==overduedays || overduedays.length == 0){
//				model.addAttribute("mes", "订单逾期天数异常");
//				return "modules/dunning/dialog/dialogDistribution";
//			}
//			List<TMisDunningPeople> peopleGroupby = tMisDunningPeopleService.findDunningPeopleGroupby();
//			Map<String, TMisDunningPeople> map = new HashMap<String,TMisDunningPeople>();
//			Set<String> set = new HashSet<String>(Arrays.asList(overduedays));
//			for(String string : set){
//				Integer overdueday = Integer.parseInt(string);
//				for(TMisDunningPeople dunningPeople : peopleGroupby){
//					if(dunningPeople.getBegin() <= overdueday && dunningPeople.getEnd() >= overdueday){
//						map.put(String.valueOf(dunningPeople.getBegin())+String.valueOf(dunningPeople.getEnd()), new TMisDunningPeople(dunningPeople.getBegin(), dunningPeople.getEnd()));
//					}
//				}
//			}
//			if(map.isEmpty()){
//				model.addAttribute("mes", "催收人员周期不存在");
//				return "modules/dunning/dialog/dialogDistribution";
//			}
//			if(map.size() > 1){
//				model.addAttribute("mes", "请选择是同一周期的订单");
//				return "modules/dunning/dialog/dialogDistribution";
//			}
//			List<String> mapKeyList = new ArrayList<String>(map.keySet());    
//			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findPeopleBybeginEnd(map.get(mapKeyList.get(0)));
//			model.addAttribute("dunningPeoples", dunningPeoples);
//			model.addAttribute("orders", orders);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "views/error/500";
//		}
//		return "modules/dunning/dialog/dialogDistribution";
//	}
	
	/**
	 * 加载手动分配页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:directorview")
	@RequestMapping(value = "dialogDistribution")
	public String dialogDistribution( Model model,String orders,String dunningcycle) {
		try {
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findPeopleByDistributionDunningcycle(dunningcycle);
			model.addAttribute("dunningPeoples", dunningPeoples);
			model.addAttribute("orders", orders);
			model.addAttribute("dunningcycle", dunningcycle);
		} catch (Exception e) {
			e.printStackTrace();
			return "views/error/500";
		}
		return "modules/dunning/dialog/dialogDistribution";
	}
	
	public static void main(String[] args) {
//		String[] arr = {"123","456","789","123","12"}; 
//		List<String> list = Arrays.asList(arr); 
//		Map<String, String> map = MapUtils.putAll(new HashMap<String, String>(), arr); 
//		for(String map2 : map.keySet()){
//			System.out.println("key= "+ map2 + " and value= " + map.get(map2));
//		}
	}
	
	/**
	 * 手动分配
	 * @param tMisDunningTask
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:directorview")
	@RequestMapping(value = "distributionSave")
	@ResponseBody
	public String distributionSave(String orders,String dunningcycle, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String mes = "";
		try {
			if(null == orders || null == dunningcycle ||"".equals(orders) || "".equals(dunningcycle)  ){
				return "订单或队列不能为空";
			}
//			String[] newdunningpeopleids = request.getParameterValues("newdunningpeopleids");
			List<String> dealcodes =  Arrays.asList(orders.split(","));
			List<String> newdunningpeopleids = Arrays.asList(request.getParameterValues("newdunningpeopleids"));
			tMisDunningTaskService.assign(dealcodes, dunningcycle,newdunningpeopleids);
			mes = "OK,手动均分"+dealcodes.size()+"条订单成功";
		} catch (Exception e) {
			logger.warn(e.getMessage());
			logger.warn("订单已还款更新任务失败"+ new Date());
			return "分配异常，失败";
		}
		return  mes;
	}
	
//	/**
//	 * 手动分配
//	 * @param tMisDunningTask
//	 * @param model
//	 * @param redirectAttributes
//	 * @return
//	 */
//	@RequiresPermissions("dunning:tMisDunningTask:directorview")
//	@RequestMapping(value = "distributionSave")
//	@ResponseBody
//	public String distributionSave(String orders, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
//		boolean f = false;
//		String mes = "";
//		String[] newdunningpeopleids = request.getParameterValues("newdunningpeopleids");
//		String[] str = orders.split(",");
//		Set<String> set = new HashSet<String>(Arrays.asList(str));
//		Iterator<String> it = set.iterator(); 
//		int i = 0;
//		while (it.hasNext()) {  
//			String dealcode = it.next();  
//			f = tMisDunningTaskService.assign(dealcode, newdunningpeopleids[i]);
//			mes += "订单:" + dealcode + ",";
//			i++;
//			if (i >= newdunningpeopleids.length) i = 0;
//		}
//		mes += "未分配?请核实";
//		return f ? "OK" : mes;
//	}
	
	
	
	/**
	 * 自动分配
	 * @param tMisDunningTask
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
			e.printStackTrace();
			return e.getMessage().toString();
		}
		addMessage(redirectAttributes, "自动分配成功");
		return "OK";
	}
	
	/**
	 * 新订单任务
	 * @param tMisDunningTask
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
			e.printStackTrace();
			return e.getMessage().toString();
		}
		addMessage(redirectAttributes, "新订单任务");
		return "OK";
	}
	
	/**
	 * 自动扫描还款
	 * @param tMisDunningTask
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
			e.printStackTrace();
			return e.getMessage().toString();
		}
		addMessage(redirectAttributes, "自动扫描还款");
		return "OK";
	}
	
	
	/**
	 * 导出催收任务数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
//	@RequiresPermissions("dunning:tMisDunningTask:exportFile")
//    @RequestMapping(value = "exportFile", method=RequestMethod.POST)
//    public String exportFile(DunningOrder order, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//		try {
//            String fileName = "OutSourcing" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
//            List<DunningOrder> page = tMisDunningTaskService.findOrderList(order);
//    		new ExportExcel("贷后还款情况报表", DunningOrder.class).setDataList(page).write(response, fileName).dispose();
//    		return null;
//		} catch (Exception e) {
//			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
//		}
//		return "redirect:" + adminPath + "/dunning/tMisDunningTask/findOrderPageList?repage";
//    }
	
	/**
	 * 导出委外数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:exportFile")
    @RequestMapping(value = "exportOuterFile", method=RequestMethod.POST)
    public String exportOuterFile(String[] outerOrders ,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//          String[] strTemplate = ordersStr.split(",")[0].split("#");
		try {
			String fileName = "OutData" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<String> orders = new ArrayList<String>();
            Map<String, String> map = new HashMap<String, String>();
            for(String str : Arrays.asList(outerOrders)){
            	orders.add(str.split("=")[0]);
            	map.put(str.split("=")[0], str.split("=")[1]);
            }
            List<DunningOuterFile> dunningOuterFiles = tMisDunningTaskService.exportOuterFile(orders);
            if(null != dunningOuterFiles && !dunningOuterFiles.isEmpty()){
            	for(DunningOuterFile dunningOuterFile : dunningOuterFiles){
            		dunningOuterFile.setName(map.get(dunningOuterFile.getDealcode()));
            	}
            	tMisDunningTaskService.savefileLog(new Date(),orders,dunningOuterFiles);
            	addMessage(redirectAttributes, "委外数据生成成功");
//            	response.sendRedirect(adminPath +"/dunning/tMisDunningTask/findOrderPageList?repage");
            	new ExportExcel("委外数据", DunningOuterFile.class).setDataList(dunningOuterFiles).write(response, fileName).dispose();
            	
            	//  切换数据源更新order表
            	DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");  
            	tMisDunningTaskService.updateOuterfiletime(new Date(),orders);
            	return null;
            	
            }else{
            	addMessage(redirectAttributes, "未导出数据！");
            }
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");  
		}
		return "redirect:" + adminPath + "/dunning/tMisDunningTask/findOrderPageList?repage";
    }
	

	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "form")
	public String form(TMisDunningTask tMisDunningTask, Model model) {
		model.addAttribute("tMisDunningTask", tMisDunningTask);
		return "modules/dunning/tMisDunningTaskForm";
	}
	
	
	/**
	 * 加载用户信息页面1
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "pageFather")
	public String pageFather(String buyerId, String dealcode,String dunningtaskdbid,HttpServletRequest request, HttpServletResponse response,Model model) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		if (order == null) {
			logger.warn("订单不存在，订单号：" + dealcode);
			return "views/error/500";
		}
		
		//boolean isDelayable = order.getPayCode() == null || !order.getPayCode().startsWith(TMisDunningOrder.CHANNEL_KAOLA);
		
		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getNewBuyerInfoByDealcode(dealcode);
		model.addAttribute("personalInfo", personalInfo);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		//model.addAttribute("isDelayable", isDelayable);
		
		TBuyerContact tBuyerContact = new TBuyerContact();
		tBuyerContact.setBuyerId(buyerId);
		tBuyerContact.setDealcode(dealcode);
		Page<TBuyerContact> contactPage = new Page<TBuyerContact>(request, response);
		contactPage = tBuyerContactService.findPage(contactPage, tBuyerContact);
		boolean hasContact = false;
		if(contactPage.getCount() > 0L){
			hasContact = true;
		}
		model.addAttribute("hasContact", hasContact);
		
		return "modules/dunning/tMisDunningTaskFather";
	}
	
	/**
	 * 加载用户信息页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "customerDetails")
	public String customerDetails(String buyerId, String dealcode,String dunningtaskdbid,boolean hasContact,Model model) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
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
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		boolean ispayoff = false;
		if(null != task){
			ispayoff = task.getIspayoff();
		}else{
			ispayoff = true;
		}
		model.addAttribute("workInfo", workinfo);
		model.addAttribute("contacts", buyer2Contacts);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		
		model.addAttribute("ispayoff", ispayoff);
		
		model.addAttribute("hasContact", hasContact);
		
		return "modules/dunning/tMisDunningTaskCustomerDetails";
	}
	
	/**
	 * 加载通讯录页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "communicationDetails")
	public String communicationDetails(HttpServletRequest request, HttpServletResponse response, Model model) {		
		
		String dealcode = request.getParameter("dealcode");
		String buyerId = request.getParameter("buyerId");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		String hasContact = request.getParameter("hasContact");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		TBuyerContact tBuyerContact = new TBuyerContact();
		tBuyerContact.setBuyerId(buyerId);
		tBuyerContact.setDealcode(dealcode);
		Page<TBuyerContact> contactPage = new Page<TBuyerContact>(request, response);
		contactPage = tBuyerContactService.findPage(contactPage, tBuyerContact); 
		
//		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
//		model.addAttribute("personalInfo", personalInfo);
		
		model.addAttribute("contactPage", contactPage);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		boolean ispayoff = false;
		if(null != task){
			ispayoff = task.getIspayoff();
		}else{
			ispayoff = true;
		}
		model.addAttribute("ispayoff", ispayoff);
		model.addAttribute("hasContact", hasContact);
		return "modules/dunning/tMisDunningTaskCommunication";
	}
	
	/**
	 * 加载通话记录页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "communicationRecord")
	public String communicationRecord(HttpServletRequest request, HttpServletResponse response, Model model) {		
		String dealcode = request.getParameter("dealcode");
		String buyerId = request.getParameter("buyerId");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		String hasContact = request.getParameter("hasContact");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		TRiskBuyerContactRecords  tRiskBuyerContactRecords = new TRiskBuyerContactRecords();
		tRiskBuyerContactRecords.setBuyerId(buyerId);
		tRiskBuyerContactRecords.setDealcode(dealcode);
		Page<TRiskBuyerContactRecords> communicationsPage = new Page<TRiskBuyerContactRecords>(request, response);		
//		communicationsPage = tRiskBuyerContactRecordsService.findPage(communicationsPage, tRiskBuyerContactRecords);
		communicationsPage = tRiskBuyerContactRecordsService.findPageByRediscache(communicationsPage, tRiskBuyerContactRecords,buyerId);
//		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
//		model.addAttribute("personalInfo", personalInfo);
		model.addAttribute("communicationsPage", communicationsPage);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		boolean ispayoff = false;
		if(null != task){
			ispayoff = task.getIspayoff();
		}else{
			ispayoff = true;
		}
		model.addAttribute("ispayoff", ispayoff);
		model.addAttribute("hasContact", hasContact);
		return "modules/dunning/tMisDunningTaskCommunicationRecord";
	}
	
	/**
	 * 加载历史借款信息页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "orderHistoryList")
	public String orderHistoryList( String buyerId,String dealcode,boolean hasContact,String dunningtaskdbid,HttpServletRequest request, HttpServletResponse response, Model model) {		
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
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		boolean ispayoff = false;
		if(null != task){
			ispayoff = task.getIspayoff();
		}else{
			ispayoff = true;
		}
		model.addAttribute("ispayoff", ispayoff);
		model.addAttribute("hasContact", hasContact);
		return "modules/dunning/tMisDunningOrderHistoryList";
	}
	

	
	/**
	 * 加载登录日志页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "apploginlogList")
	public String apploginlogList(String buyerId,String dealcode,boolean hasContact,String dunningtaskdbid,String mobile,HttpServletRequest request, HttpServletResponse response, Model model) {		
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||mobile==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)||"".equals(mobile)){
			return "views/error/500";
		}
		try {
			DbUtils dbUtils = new DbUtils();
			List<AppLoginLog> appLoginLogs = dbUtils.getApploginlog(mobile);
			model.addAttribute("appLoginLogs", appLoginLogs);
//			DynamicDataSource.setCurrentLookupKey("dataSource2");  
//			List<AppLoginLog> appLoginLogs = tMisDunningTaskService.findApploginlog(mobile);
//			DynamicDataSource.setCurrentLookupKey("dataSource");  
			model.addAttribute("dunningtaskdbid", dunningtaskdbid);
			model.addAttribute("buyerId", buyerId);
			model.addAttribute("dealcode", dealcode);
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("STATUS_DUNNING", "dunning");
			params.put("DEALCODE", dealcode);
			TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
			boolean ispayoff = false;
			if(null != task){
				ispayoff = task.getIspayoff();
			}else{
				ispayoff = true;
			}
			model.addAttribute("ispayoff", ispayoff);
			model.addAttribute("hasContact", hasContact);
		} catch (Exception e) {
			e.printStackTrace();
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
		
		String selfMobile = "";
		List<TMisSendMsgInfo> list = tMisContantRecordService.getTelInfos(buyerId,"self");
		if(list != null && list.size() > 0){
			selfMobile = list.get(0).getTel();
		}
		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
		if(personalInfo == null){
			return "views/error/500";
		}
		Integer odds = Integer.valueOf(personalInfo.getOverdueDays());
		String smsT = "";
		boolean chooseSelf = true;
		if(odds == 0 || odds < 0){
			smsT = "ST_0";
		}
		else if(0 < odds && odds < 8){
			smsT = "ST__1_7";
		}else if(7 < odds && odds < 15){
			smsT = "ST_8_14";
		}else if(14 < odds && odds < 22){
			smsT = "ST_15_21";
		}else if(21 < odds && odds < 36){
			smsT = "ST_22_35";
		}else{
			chooseSelf = false;
			smsT = "ST_15_PLUS";
			selfMobile = "";
		}
		
		
		TMisContantRecord t = new TMisContantRecord();
		t.setSmstemp(SmsTemp.valueOf(smsT));
		String smsContext = "";
		smsContext = tMisContantRecordService.smsGetTemp(task,order, t);
		
		String contactMobile = request.getParameter("contactMobile");
		String contactstype = request.getParameter("contactstype");
		model.addAttribute("contactstype", null != contactstype && !"undefined".equals(contactMobile) ? contactstype.toUpperCase() : chooseSelf ? "SELF" : "" );
		model.addAttribute("selfMobile", null != contactMobile && !"undefined".equals(contactMobile) ? contactMobile:"" );
		
		model.addAttribute("smsContext", smsContext);
		model.addAttribute("smsT", smsT);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
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
	public String collectionTel(TMisDunningTask tMisDunningTask, Model model,HttpServletRequest request, HttpServletResponse response) {
//		model.addAttribute("tMisDunningTask", tMisDunningTask);
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		String contactMobile = request.getParameter("contactMobile");
		String contactstype = request.getParameter("contactstype");
		
		model.addAttribute("contactMobile", null != contactMobile && !"undefined".equals(contactMobile) ? contactMobile:"" );
		model.addAttribute("contactstype", null != contactstype && !"undefined".equals(contactMobile) ? contactstype.toUpperCase() :"");
		
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		return "modules/dunning/dialog/dialogCollectionTel";
	}
	
	/**
	 * 加载电催结论页面
	 * @param tMisDunningConclusion
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
		
		//String remark = tMisDunnedConclusionService.getDefalutRemark(tMisDunnedConclusion);
		//Map<String, String> next = tMisDunnedConclusionService.getFollowDateConfig();
		
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		//model.addAttribute("remark", remark);
		model.addAttribute("actions", actionsArr);
		//model.addAttribute("nextdate", JSON.toJSONString(next));
		return "modules/dunning/dialog/dialogCollectionTelConclusion";
	}
	
	/**
	 * 加载催收调整金额页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "collectionAmount")
	public String collectionAmount(TMisDunningTask tMisDunningTask, Model model,HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		List<TMisReliefamountHistory> list = tMisReliefamountHistoryService.findListByDealcode(dealcode);
		model.addAttribute("list", list);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		return "modules/dunning/dialog/dialogCollectionAmount";
	}
	
	/**
	 * 保存减免金额
	 * @param task
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "savefreeCreditAmount")
	@ResponseBody
	public String savefreeCreditAmount(TMisDunningTask task,Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
//		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		String amount = request.getParameter("amount");
		Integer freeCreditAmount = 0;
		for (Role r : UserUtils.getUser().getRoleList()){
			if(("减免无上限").equals(r.getName())){
				freeCreditAmount = 1;
				break;
			}
		}
		if(freeCreditAmount != 1 && Double.parseDouble(amount) > 50){
			return "减免金额不能大于50元";
		}
		String dealcode = request.getParameter("dealcode");
		try {
			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");  
//			List<AppLoginLog> appLoginLogs = tMisDunningTaskDao.findApploginlog("18616297272");
//			System.out.println(appLoginLogs.size());
			tMisDunningTaskService.updateOrderModifyAmount(dealcode, amount);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
			
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");  
		}
		tMisDunningTaskService.savefreeCreditAmount(dealcode, task, amount);
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
		
		Map<String,Object> maps = new HashMap<String,Object>();
		maps.put("buyerId",buyerId);
		
		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
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
	 * 加载催收还款页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
//	@RequiresPermissions("dunning:tMisDunningTask:view")
//	@RequestMapping(value = "collectionConfirmpay")
//	public String collectionConfirmpay(TMisDunningTask tMisDunningTask, Model model,HttpServletRequest request, HttpServletResponse response) {
//		String buyerId = request.getParameter("buyerId");
//		String dealcode = request.getParameter("dealcode");
//		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
//			return "views/error/500";
//		}
//		model.addAttribute("buyerId", buyerId);
//		model.addAttribute("dealcode", dealcode);
//		
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("STATUS_DUNNING", "dunning");
//		params.put("DEALCODE", dealcode);
//		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
//		if (task == null) {
//			logger.warn("任务不存在，订单号：" + dealcode);
//			return "views/error/500";
//		}
//		
//		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
//		if (order == null) {
//			logger.warn("订单不存在，订单号：" + dealcode);
//			return "views/error/500";
//		}
//		
//		Map<String,Object> maps = new HashMap<String,Object>();
//		maps.put("buyerId",buyerId);
//		
//		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
//		model.addAttribute("personalInfo", personalInfo);
//		
//		model.addAttribute("task", task);
//		
//		BigDecimal delayAmount = new BigDecimal(0l);
//		if(personalInfo != null && StringUtils.isNotBlank(personalInfo.getOverdueDays())){
//			if(Integer.valueOf(personalInfo.getOverdueDays()) < 15){
//				
//				BigDecimal cpAmt = new BigDecimal(0L);
//				if(order.getCreditAmount() != null && 
//					(
//						(order.getCouponId() != null && order.getCouponId() > 0)
//						||(order.getSubCostAmount()!=null&&order.getSubCostAmount().compareTo(BigDecimal.ZERO)>0)
//					)
//				){
//					cpAmt = order.getAmount().subtract(order.getCreditAmount());
//				}
//				
//				BigDecimal defaultInterestAmount = getDefaultDelayAmount(order);
//				
//				//续期费用 = 7天或者14天续期费用 +续期手续费用（20元或者30元）+逾期费 + 订单手续费
//				delayAmount = order.getCostAmount().add(defaultInterestAmount).subtract(cpAmt).add(order.getOverdueAmount());
//			}
//		}
//		
//		model.addAttribute("delayAmount", delayAmount);
//		return "modules/dunning/dialog/dialogCollectionConfirmpay";
//	}
	
	/**
	 * 完成订单状态
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
//	@RequiresPermissions("dunning:tMisDunningTask:view")
//	@RequestMapping(value = "confrimPayStatus")
//	@ResponseBody
//	public String confrimPayStatus(TMisPaid paid) {
//		String dealcode = paid.getDealcode();
//		String paychannel = paid.getPaychannel();
//		String remark = paid.getRemark();
//		String paidType = paid.getPaidType();
//		String paidAmount = paid.getPaidAmount();
//		String delayDay = paid.getDelayDay();
//		if(StringUtils.isBlank(delayDay)){
//			delayDay = "0";
//		}
//		if( StringUtils.isBlank(paidAmount) || StringUtils.isBlank(dealcode)){
//			return "错误，用户或者订单不存在";
//		}
//		if(StringUtils.isBlank(paidType) || StringUtils.isBlank(paychannel)){
//			return "错误，代付类型或代付渠道不存在";
//		}
//		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
//		if (order == null) {
//			return ("错误，用户或者订单不存在");
//		}
//		if(order.status.equals("payoff")){
//			return "错误，订单已还清";
//		}
//		
//		String riskUrl =  DictUtils.getDictValue("riskclone","orderUrl","");
//		String url = riskUrl + "riskportal/limit/order/v1.0/payForStaffType/" +dealcode+ "/" +paychannel+ "/" +remark+ "/" +paidType+ "/" +paidAmount+ "/" +delayDay;
//		logger.info("接口url：" + url);
//		String str = "";
//		try {
//			String res =  java.net.URLEncoder.encode(GetRequest.getRequest(url, new HashMap<String,String>()), "utf-8");
//			logger.info("接口url返回参数" + res.getBytes("UTF-8"));
//			if(StringUtils.isNotBlank(res)){
//				JSONObject repJson = new JSONObject(res);
//				String resultCode =  repJson.has("resultCode") ? String.valueOf(repJson.get("resultCode")) : "";
//				if(StringUtils.isNotBlank(resultCode) && "200".equals(resultCode)){
//					str = repJson.has("datas") ? String.valueOf(repJson.get("datas")) : "";
//					logger.info("返回成功" + str);
////					tMisRemittanceConfirmService.confirmationUpdate(entity)
//					return "OK";
//				}else{
//					return repJson.has("datas") ? String.valueOf(repJson.get("datas")) : "";
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return "";
//	}
	

	/**
	 * 完成代付
	 * @param tMisDunningTask
	 * @param model
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
			e.printStackTrace();
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
	 * @param tMisDunningTask
	 * @param model
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return "modules/dunning/performanceMonthReportList";
	}
	
	
	/**
	 * 导出催收绩效月表
	 * @param user
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
	 * 催收绩效日表
	 * @param performanceMonthReport
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:viewReport")
	@RequestMapping(value = {"findPerformanceDayReport", ""})
	public String findPerformanceDayReport(PerformanceDayReport performanceDayReport,TMisDunningPeople dunningPeople, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			performanceDayReport.setDatetimestart(null == performanceDayReport.getDatetimestart()  ? DateUtils.getDateToDay(new Date()) : performanceDayReport.getDatetimestart());
			performanceDayReport.setDatetimeend(null == performanceDayReport.getDatetimeend()  ? DateUtils.getDateToDay(new Date()) : performanceDayReport.getDatetimeend());
			Page<PerformanceDayReport> page = tMisDunningTaskService.findPerformanceDayReport(new Page<PerformanceDayReport>(request, response), performanceDayReport); 
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findList(dunningPeople);
			model.addAttribute("dunningPeoples", dunningPeoples);
			model.addAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/dunning/performanceDayReportList";
	}
	
	/**
	 * 催收日表
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:viewReport")
    @RequestMapping(value = "performanceDayReportExport", method=RequestMethod.POST)
    public String performanceDayReportExport(PerformanceDayReport performanceDayReport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			performanceDayReport.setDatetimestart(null == performanceDayReport.getDatetimestart()  ? DateUtils.getDateToDay(new Date()) : performanceDayReport.getDatetimestart());
			performanceDayReport.setDatetimeend(null == performanceDayReport.getDatetimeend()  ? DateUtils.getDateToDay(new Date()) : performanceDayReport.getDatetimeend());
            String fileName = "performanceDayReport" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<PerformanceDayReport> page = tMisDunningTaskService.findPerformanceDayReport(performanceDayReport);
    		new ExportExcel("导出催收日表", PerformanceDayReport.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/dunning/tMisDunningTask/findPerformanceDayReport?repage";
    }
	

}