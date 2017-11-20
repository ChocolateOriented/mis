/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.alibaba.fastjson.JSON;
import com.gamaxpay.commonutil.Cipher.Md5Encrypt;
import com.gamaxpay.commonutil.web.PostRequest;
import com.mo9.risk.modules.dunning.entity.*;
import com.mo9.risk.modules.dunning.service.*;
import com.mo9.risk.util.DateUtils;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 
 * @author 徐盛
 * @version 2016-07-12
 */
/**
 * @Description 委外催收任务Controller
 * @author LiJingXiang
 * @version 2017年4月27日
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningOuterTask")
public class TMisDunningOuterTaskController extends BaseController {

	private static final String riskUrl =  DictUtils.getDictValue("riskclone","orderUrl","");

	@Autowired
	private TMisDunningConfigureService tMisDunningConfigureService;

	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;
	@Autowired
	private TMisDunningGroupService tMisDunningGroupService;
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoDao;
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	@Autowired
	private TBuyerContactService tBuyerContactService;
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	@Autowired
	private TMisChangeCardRecordService tMisChangeCardRecordService;
	@Autowired
	private TMisDunningDeductService tMisDunningDeductService;
	@Autowired
	private TMisDunningTagService tMisDunningTagService;
	@Autowired
	private TMisDunningScoreCardService tMisDunningScoreCardService;
	
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
	
	@RequiresPermissions("dunning:tMisDunningOuterTask:view")
	@RequestMapping(value = {"findOrderPageList", ""})
	public String findOrderPageList(DunningOrder dunningOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		//默认条件查询未还清订单
		if (dunningOrder.getStatus() == null){
			dunningOrder.setStatus(DunningOrder.STATUS_PAYMENT);
		}

		Page<DunningOrder> page = tMisDunningTaskService.findOuterOrderPageList(new Page<DunningOrder>(request, response), dunningOrder);
		//催收小组列表
		MobileResult[] values = MobileResult.values();
		List<MobileResult> mobileResultList = Arrays.asList(values);
		model.addAttribute("mobileResultList", mobileResultList);
		model.addAttribute("groupList", tMisDunningGroupService.findList(new TMisDunningGroup()));
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
		model.addAttribute("page", page);
		return "modules/dunning/tMisDunningOuterTaskList";
	}

	/**
	 * 导出数据
	 */
	@RequiresPermissions("dunning:tMisDunningOuterTask:view")
	@RequestMapping(value = "exportOuterFile", method = RequestMethod.POST)
	public String exportOuterFile(@RequestParam("outerOrders") ArrayList<String> outerOrders, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		String redirectUrl = "redirect:" + adminPath + "/dunning/tMisDunningOuterTask/findOrderPageList?repage";

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
			String fileName = "OutData" + DateUtils.getDate("yyyy-MM-dd") + ".xlsx";
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

		/**
		 * 加载用户信息页面1
		 * @param tMisDunningTask
		 * @param model
		 * @return
		 */
	@RequiresPermissions("dunning:tMisDunningOuterTask:view")
	@RequestMapping(value = "pageFather")
	public String pageFather(String buyerId, String dealcode,String dunningtaskdbid,HttpServletRequest request, HttpServletResponse response,Model model) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		
		//boolean isDelayable = order.getPayCode() == null || !order.getPayCode().startsWith(TMisDunningOrder.CHANNEL_KAOLA);
		
		TBuyerContact tBuyerContact = new TBuyerContact();
		tBuyerContact.setBuyerId(buyerId);
		tBuyerContact.setDealcode(dealcode);
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
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
			TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getNewBuyerInfoByDealcode(dealcode);
			model.addAttribute("personalInfo", personalInfo);
			model.addAttribute("overdueDays",personalInfo.getOverdueDays());
			model.addAttribute("mobileSelf",personalInfo.getMobile());
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
			String controlDay=DictUtils.getDictValue("overdueDay", "controlPage", "1");
			model.addAttribute("controlDay", controlDay);
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
			
			TMisDunningScoreCard tMisDunningScoreCard = tMisDunningScoreCardService.getScoreCardByDealcode(dealcode);
			model.addAttribute("score", tMisDunningScoreCard == null ? "" : tMisDunningScoreCard.getGrade());
		} catch (Exception e) {
			logger.info("切换只读库查询失败" ,e);
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		//model.addAttribute("isDelayable", isDelayable);
		
		
		return "modules/dunning/tMisDunningTaskFather";
	}
	

	/**
	 * 加载委外手动分案页面
	 * @param dunningcycle
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningOuterTask:view")
	@RequestMapping(value = "dialogOutDistribution")
	public String dialogOutDistribution( Model model,String dunningcycle) {
		try {
			TMisDunningGroup tMisDunningGroup = new TMisDunningGroup();
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findPeopleByDistributionDunningcycle(dunningcycle);
			model.addAttribute("dunningPeoples", dunningPeoples);
			model.addAttribute("dunningcycle", dunningcycle);
			model.addAttribute("groupList", tMisDunningGroupService.findList(tMisDunningGroup));
			model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
		} catch (Exception e) {
			logger.info("加载委外手动分案页面失败",e);
			return "views/error/500";
		}
		return "modules/dunning/dialog/dialogOutDistribution";
	}

	/**
	 * 获取手动分案催收人员
	 * @param request
	 */
	@RequiresPermissions("dunning:tMisDunningTask:directorview")
	@RequestMapping(value = "dialogDistributionPeople")
	@ResponseBody
	public List<TMisDunningPeople> DistributionPeople(HttpServletRequest request){
		List<TMisDunningPeople> dunningpeople = null;
		String[] dunningcycle = request.getParameterValues("dunningcycle[]");
		String[] type = request.getParameterValues("type[]");
		String[] auto = request.getParameterValues("auto[]");
		String name = request.getParameter("name");

		if ((dunningcycle == null || dunningcycle.length == 0) && (type == null || type.length == 0)
				&& (auto == null || auto.length == 0) && StringUtils.isEmpty(name)) {
			return new ArrayList<TMisDunningPeople>();
		}

		String dunningpeoplename=request.getParameter("dunningpeoplename");
		try{
			dunningpeople=tMisDunningPeopleService.findPeopleByCycleTypeAutoName(dunningcycle,type,auto,name,dunningpeoplename);
		}catch (Exception e){
			logger.info("",e);
			return null;
		}
		return dunningpeople;
	}

	/**
	 * 委外手动分案
	 * @param orders
	 * @param dunningcycle
	 * @param outsourcingenddate
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningOuterTask:view")
	@RequestMapping(value = "outDistributionSave")
	@ResponseBody
	public String outDistributionSave(String orders,String dunningcycle, HttpServletRequest request,Date outsourcingenddate) {
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
			String outAssignmes = tMisDunningTaskService.outAssign(dealcodes, dunningcycle,newdunningpeopleids,outsourcingenddate);
			mes = "OK,手动勾选"+dealcodes.size()+"条订单," + outAssignmes;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			logger.warn("订单已还款更新任务失败"+ new Date());
			return "分配异常，失败";
		}
		return  mes;
	}
	/**
	 * 系统短信定时任务分配
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "autoSend")
	@ResponseBody
	public String autoSend() {
		tMisDunningTaskService.autoSmsSend();
		logger.info("系统短信定时任务分配成功");
		return "OK";
	}
	
	
	/**
	 * 原催收逾期列表页面
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = {"oldfindOrderPageList", ""})
	public String oldfindOrderPageList(DunningOrder dunningOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DunningOrder> page = tMisDunningTaskService.findOrderPageList(new Page<DunningOrder>(request, response), dunningOrder); 
		//催收小组列表
		model.addAttribute("groupList", tMisDunningGroupService.findList(new TMisDunningGroup()));
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
		model.addAttribute("page", page);
		return "modules/dunning/oldTmisDunningTaskList";
	}
	
	/**
	 * 测试号码清洗
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "numberClean")
	public void numberClean() {
		tMisDunningTaskService.numberCleanResult();
	}
	
	/**
	 * 测试号码清洗回调
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "numberCleanBackTest")
	public void numberCleanBackTest() {
		tMisDunningTaskService.callBackTest();
	}
	//测试自动电催结论
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "diancui1")
	@ResponseBody
	public  String  dianCui1(TmisDunningNumberClean tmisDunningNumberClean,HttpServletRequest request){
		
		tMisDunningTaskService.autoTelConclusion1();
		logger.info(new Date()+"电催结论(Q0,Q1)成功");
		return "OK";
	}
	//测试自动电催结论
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "diancui2")
	@ResponseBody
	public  String  dianCui2(TmisDunningNumberClean tmisDunningNumberClean,HttpServletRequest request){
		
		tMisDunningTaskService.autoTelConclusion2();
		logger.info(new Date()+"电催结论(Q2,Q3,Q4)上午");
		return "OK";
	}
	//测试自动电催结论
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "diancui3")
	@ResponseBody
	public  String  dianCui3(TmisDunningNumberClean tmisDunningNumberClean,HttpServletRequest request){
		
		tMisDunningTaskService.autoTelConclusion3();
		logger.info(new Date()+"电催结论(Q2,Q3,Q4)下午");
		return "OK";
	}

	/**
	 * 获取江湖救急该笔订单状态
	 *
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "orderStatus")
	@ResponseBody
	public String getOrderStatus(String dealcode) throws IOException {

		String mes="";
		try {
			String privateKey = tMisDunningConfigureService.getConfigureValue("orderRepay.privateKey");
			String url = riskUrl +"riskportal/limit/order/findByDealcode.do";
			HashMap<String,String> tRiskOrder = new HashMap<String,String>();
			tRiskOrder.put("dealcode",dealcode);
			String sign = Md5Encrypt.sign(tRiskOrder, privateKey);
			tRiskOrder.put("sign", sign);
			String res= PostRequest.postRequest(url,tRiskOrder);
			logger.info(dealcode+"江湖救急订单还款接口参数" + res);

			if (StringUtils.isBlank(res)) {
				throw new ServiceException("订单接口回调失败");
			}

			TRiskOrder tRiskOrder1= JSON.parseObject(res,TRiskOrder.class);
			if(("payoff").equals(tRiskOrder1.getStatus())){
				tMisDunningTaskService.asyncUpdate(dealcode,tRiskOrder1.getStatus());
				return "payoff";
			}
			if(("payment").equals(tRiskOrder1.getStatus())){
				return "payment";
			}
		}catch (Exception e){
			logger.warn(e.getMessage());
			return "";
		}

		return mes;
	}
}