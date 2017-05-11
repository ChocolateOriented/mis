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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.mo9.risk.modules.dunning.bean.PayChannelInfo;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.service.TMisDunningDeductService;
import com.mo9.risk.modules.dunning.service.TMisDunningDeductCallService;

/**
 * 催收代扣Controller
 * @author shijlu
 * @version 2017-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningDeduct")
public class TMisDunningDeductController extends BaseController {

	@Autowired
	private TMisDunningDeductService tMisDunningDeductService;
	
	@Autowired
	private TMisDunningDeductCallService tMisDunningDeductCallService;
	
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	
	@RequiresPermissions("dunning:tMisDunningDeduct:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisDunningDeduct tMisDunningDeduct, HttpServletRequest request, HttpServletResponse response,String mobileSelf, Model model) {
		String dealcode = request.getParameter("dealcode");
		String buyerId = request.getParameter("buyerId");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		String hasContact = request.getParameter("hasContact");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		Page<TMisDunningDeduct> page = tMisDunningDeductService.findPage(new Page<TMisDunningDeduct>(request, response), tMisDunningDeduct);
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

		model.addAttribute("page", page);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("ispayoff", ispayoff);
		model.addAttribute("hasContact", hasContact);
		model.addAttribute("mobileSelf", mobileSelf);
		return "modules/dunning/tMisDunningTaskDeductList";
	}

	@RequiresPermissions("dunning:tMisDunningDeduct:edit")
	@RequestMapping(value = "saveRecord")
	@ResponseBody
	public Map<String, String> saveRecord(TMisDunningDeduct tMisDunningDeduct, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<String, String>();
		String dealcode = request.getParameter("dealcode");
		
		if (StringUtils.isBlank(tMisDunningDeduct.getBankcard()) || StringUtils.isBlank(tMisDunningDeduct.getBuyername())
				|| StringUtils.isBlank(tMisDunningDeduct.getIdcard()) || StringUtils.isBlank(tMisDunningDeduct.getMobile())
				|| StringUtils.isBlank(dealcode) || tMisDunningDeduct.getPayamount() == null) {
			result.put("result", "NO");
			result.put("msg", "参数错误");
			return result;
		}
		
		DunningOrder order = tMisDunningDeductService.getRiskOrderByDealcodeFromRisk(dealcode);
		
		if (order == null) {
			result.put("result", "NO");
			result.put("msg", "订单不存在");
			return result;
		}
		
		if ("payoff".equals(order.getStatus())) {
			result.put("result", "NO");
			result.put("msg", "订单已还清");
			return result;
		}
		
		if (tMisDunningDeduct.getPayamount().equals(0D)) {
			result.put("result", "NO");
			result.put("msg", "扣款金额不应等于0");
			return result;
		}
		
		if (order.getCreditamount() == null || tMisDunningDeduct.getPayamount() > order.getCreditamount()) {
			result.put("result", "NO");
			result.put("msg", "扣款金额不应大于应催金额");
			return result;
		}
		
		tMisDunningDeduct.setDealcode(dealcode);
		String deductcode = tMisDunningDeductService.saveRecord(tMisDunningDeduct);
		if (deductcode == null || "".equals(deductcode)) {
			result.put("result", "NO");
			result.put("msg", "保存记录失败");
			return result;
		}
		
		result = tMisDunningDeductCallService.submitOrderInMo9(tMisDunningDeduct);
		result.put("deductcode", deductcode);
		
		return result;
	}
	
	@RequiresPermissions("dunning:tMisDunningDeduct:view")
	@RequestMapping(value = "get")
	@ResponseBody
	public TMisDunningDeduct get(TMisDunningDeduct tMisDunningDeduct, HttpServletRequest request, HttpServletResponse response) {
		TMisDunningDeduct result = tMisDunningDeductService.get(tMisDunningDeduct.getDeductcode());
		return result;
	}

	@RequiresPermissions("dunning:tMisDunningDeduct:view")
	@RequestMapping(value = "form")
	public String form(TMisDunningDeduct tMisDunningDeduct, Model model) {
		model.addAttribute("tMisDunningDeduct", tMisDunningDeduct);
		return "modules/dunning/tMisDunningDeductForm";
	}

	@RequiresPermissions("dunning:tMisDunningDeduct:edit")
	@RequestMapping(value = "save")
	public String save(TMisDunningDeduct tMisDunningDeduct, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunningDeduct)){
			return form(tMisDunningDeduct, model);
		}
		tMisDunningDeductService.save(tMisDunningDeduct);
		addMessage(redirectAttributes, "保存记录成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningDeduct/?repage";
	}
	
	@RequiresPermissions("dunning:tMisDunningDeduct:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisDunningDeduct tMisDunningDeduct, RedirectAttributes redirectAttributes) {
		tMisDunningDeductService.delete(tMisDunningDeduct);
		addMessage(redirectAttributes, "删除记录成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningDeduct/?repage";
	}
	
	@RequiresPermissions("dunning:tMisDunningDeduct:view")
	@RequestMapping(value = "preCheck")
	@ResponseBody
	public Map<String, String> preCheck(String dealcode, String bankName, String bankCard, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<String, String>();
		
		if (!tMisDunningDeductService.preCheckDunningPeople()) {
			result.put("result", "NO");
			result.put("msg", "当前登录账号不支持代扣");
			return result;
		}
		
		List<PayChannelInfo> payChannelList = tMisDunningDeductService.getSupportedChannel(bankName);
		
		if (payChannelList == null || payChannelList.size() == 0) {
			result.put("result", "NO");
			result.put("msg", "当前银行卡暂不支持代扣");
			return result;
		}
		
		//验证支持的渠道中是否都存在余额不足的限制
		boolean balanceFlag = false;
		for (PayChannelInfo payChannel : payChannelList) {
			//至少有一个渠道可用
			if (payChannel.needCheckBalance()) {
				balanceFlag = true;
				boolean balance = tMisDunningDeductService.preCardBalance(bankCard);
				if (balance) {
					balanceFlag = false;
					break;
				}
			} else {
				balanceFlag = false;
				break;
			}
		}
		
		if (balanceFlag) {
			result.put("result", "NO");
			result.put("msg", "该银行卡当前不能发起代扣");
			return result;
		}
		
		if (!tMisDunningDeductService.preCheckStatus(dealcode)) {
			result.put("result", "NO");
			result.put("msg", "当前订单代扣处理中，请勿发起代扣");
			return result;
		}
		
		result.put("result", "OK");
		result.put("msg", "OK");
		return result;
	}
	
	@RequiresPermissions("dunning:tMisDunningDeduct:view")
	@RequestMapping(value = "getSuccessRateByChannel")
	@ResponseBody
	public List<PayChannelInfo> getSuccessRateByChannel(String paychannel, HttpServletRequest request, HttpServletResponse response) {
		return tMisDunningDeductService.getSuccessRateByChannel(paychannel);
	}

}