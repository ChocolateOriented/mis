/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.DunningOuterFile;
import com.mo9.risk.util.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
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
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.modules.dunning.entity.TMisChangeCardRecord;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningScoreCard;
import com.mo9.risk.modules.dunning.entity.TMisDunningTag;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.service.TBuyerContactService;
import com.mo9.risk.modules.dunning.service.TMisChangeCardRecordService;
import com.mo9.risk.modules.dunning.service.TMisDunningDeductService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.mo9.risk.modules.dunning.service.TMisDunningScoreCardService;
import com.mo9.risk.modules.dunning.service.TMisDunningTagService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerPersonalInfoService;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

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
		Page<TBuyerContact> contactPage = new Page<TBuyerContact>(request, response);
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getNewBuyerInfoByDealcode(dealcode);
			model.addAttribute("personalInfo", personalInfo);
			contactPage = tBuyerContactService.findPage(contactPage, tBuyerContact);
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
			
			boolean deductable = tMisDunningDeductService.preCheckChannel(tMisChangeCardRecord.getBankname());
			//根据资方和逾期天数判断是否开启代扣
			boolean daikouStatus = tMisDunningTaskService.findOrderByPayCode(order);
			model.addAttribute("changeCardRecord", tMisChangeCardRecord);
			model.addAttribute("deductable", deductable && daikouStatus);
			model.addAttribute("daikouStatus", daikouStatus);
			
			TMisDunningTag tMisDunningTag = new TMisDunningTag();
			tMisDunningTag.setDealcode(dealcode);
			List<TMisDunningTag> tags = tMisDunningTagService.findList(tMisDunningTag);
			model.addAttribute("tags", tags);
			
			TMisDunningScoreCard tMisDunningScoreCard = tMisDunningScoreCardService.getScoreCardByDealcode(dealcode);
			model.addAttribute("score", tMisDunningScoreCard == null ? "" : tMisDunningScoreCard.getGrade());
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		//model.addAttribute("isDelayable", isDelayable);
		
		boolean hasContact = false;
		if(contactPage.getCount() > 0L){
			hasContact = true;
		}
		model.addAttribute("hasContact", hasContact);
		
		return "modules/dunning/tMisDunningTaskFather";
	}
	

	/**
	 * 加载委外手动分配页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningOuterTask:view")
	@RequestMapping(value = "dialogOutDistribution")
	public String dialogOutDistribution( Model model,String orders,String dunningcycle) {
		try {
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleService.findPeopleByDistributionDunningcycle(dunningcycle);
			model.addAttribute("dunningPeoples", dunningPeoples);
			model.addAttribute("dunningcycle", dunningcycle);
		} catch (Exception e) {
			logger.info("加载委外手动分配页面失败",e);
			return "views/error/500";
		}
		return "modules/dunning/dialog/dialogOutDistribution";
	}
	
	
	/**
	 * 委外手动分配
	 * @param tMisDunningTask
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningOuterTask:view")
	@RequestMapping(value = "outDistributionSave")
	@ResponseBody
	public String outDistributionSave(String orders,String dunningcycle, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,Date outsourcingenddate) {
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
//			tMisDunningTaskService.outAssign(dealcodes, dunningcycle,newdunningpeopleids,outsourcingenddate);
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

	
}