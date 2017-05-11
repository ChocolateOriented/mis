/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.web.BaseController;
import com.mo9.risk.modules.dunning.entity.BankCardInfo;
import com.mo9.risk.modules.dunning.entity.TMisChangeCardRecord;
import com.mo9.risk.modules.dunning.service.TMisChangeCardRecordService;
import com.mo9.risk.modules.dunning.service.TMisDunningDeductService;

/**
 * 换卡Controller
 * @author shijlu
 * @version 2017-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisChangeCardRecord")
public class TMisChangeCardRecordController extends BaseController {

	@Autowired
	private TMisChangeCardRecordService tMisChangeCardRecordService;
	
	@Autowired
	private TMisDunningDeductService tMisDunningDeductService;

	/**
	 * 加载换卡页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningDeduct:view")
	@RequestMapping(value = "changeCard")
	public String collectionChangeCard(Model model, HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		String mobile = request.getParameter("mobile");
		String idcard = request.getParameter("idcard");
		String bankname = request.getParameter("bankname");
		String bankcard = request.getParameter("bankcard");
		String changeType = request.getParameter("changeType");
		
		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
			return "views/error/500";
		}
		
		List<String> bankList = tMisChangeCardRecordService.getAllChannelBank();
		
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("mobile", mobile);
		model.addAttribute("idcard", idcard);
		model.addAttribute("bankname", bankname);
		model.addAttribute("bankcard", bankcard);
		model.addAttribute("bankList", bankList);
		model.addAttribute("changeType", changeType);
		
		return "modules/dunning/dialog/dialogChangeCard";
	}
	
	@RequiresPermissions("dunning:tMisDunningDeduct:edit")
	@RequestMapping(value = "saveCard")
	@ResponseBody
	public String saveCard(TMisChangeCardRecord tMisChangeCardRecord, HttpServletRequest request, HttpServletResponse response) {
		tMisChangeCardRecordService.save(tMisChangeCardRecord);
		return "OK";
	}
	
	@RequiresPermissions("dunning:tMisDunningDeduct:view")
	@RequestMapping(value = "getBankByCard")
	@ResponseBody
	public BankCardInfo getBankByBin(String bankCard) {
		return tMisChangeCardRecordService.getBankByCard(bankCard);
	}
	
	@RequiresPermissions("dunning:tMisDunningDeduct:view")
	@RequestMapping(value = "preCheck")
	@ResponseBody
	public Map<String, String> preCheck(String dealcode, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<String, String>();
		
		if (!tMisDunningDeductService.preCheckStatus(dealcode)) {
			result.put("result", "NO");
			result.put("msg", "当前订单代扣处理中，请勿变更扣款信息");
			return result;
		}
		
		result.put("result", "OK");
		result.put("msg", "OK");
		return result;
	}
}