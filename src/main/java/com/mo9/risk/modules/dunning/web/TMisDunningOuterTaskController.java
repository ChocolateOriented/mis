/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.service.TBuyerContactService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerPersonalInfoService;
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

	private static final Logger actionlog = Logger.getLogger("com.mo9.cuishou.liulan");
	
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
		Page<DunningOrder> page = tMisDunningTaskService.findOuterOrderPageList(new Page<DunningOrder>(request, response), dunningOrder); 
		//催收小组列表
		model.addAttribute("groupList", tMisDunningGroupService.findList(new TMisDunningGroup()));
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
		model.addAttribute("page", page);
		return "modules/dunning/tMisDunningOuterTaskList";
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
}