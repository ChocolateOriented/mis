/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.service.TMisCallingRecordService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningOrderService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * CTI回调Controller
 */
@Controller
@RequestMapping(value="${adminPath}/dunning/tMisCallingRecord")
public class TMisCallingRecordController extends BaseController {
	
	@Autowired
	private TMisCallingRecordService tMisCallingRecordService;
	
	@Autowired
	private TMisDunningOrderService tMisDunningOrderService;
	
	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;
	
	@Autowired
	private TMisDunningGroupService tMisDunningGroupService;
	
	@RequestMapping(value = {"list", ""})
	public String list(@ModelAttribute("tMisCallingRecord") TMisCallingRecord tMisCallingRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisCallingRecord> page = tMisCallingRecordService.findPage(new Page<TMisCallingRecord>(request, response), tMisCallingRecord);
		//催收小组列表
		TMisDunningGroup tMisDunningGroup = new TMisDunningGroup();
		int permissions = TMisDunningTaskService.getPermissions();
		boolean supervisorLimit = false;
		if (permissions == TMisDunningTaskService.DUNNING_INNER_PERMISSIONS) {
			tMisDunningGroup.setLeader(UserUtils.getUser());
			supervisorLimit = true;
		}
		if (permissions == TMisDunningTaskService.DUNNING_SUPERVISOR) {
			tMisDunningGroup.setSupervisor(UserUtils.getUser());
			supervisorLimit = true;
		}
		model.addAttribute("groupList", tMisDunningGroupService.findList(tMisDunningGroup));
		model.addAttribute("supervisorLimit", supervisorLimit);
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
		model.addAttribute("page", page);
		String url = DictUtils.getDictValue("ctiUrl", "callcenter", "") + "audio/";
		model.addAttribute("ctiUrl", url);
		return "modules/dunning/tMisCallingRecordList";
	}
	
	@RequestMapping(value = "gotoTask")
	public String gotoTask(TMisCallingRecord tMisCallingRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		String dealcode = tMisCallingRecord.getDealcode();
		if (tMisCallingRecord.getDealcode() == null) {
			return "views/error/500";
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("DEALCODE", dealcode);
		DunningOrder order = tMisDunningOrderService.findOrderByDealcode(dealcode);
		TMisDunningTask task = tMisDunningTaskService.findDunningTaskByDealcode(params);
		
		if (order == null || task == null) {
			return "views/error/500";
		}
		
		return "redirect:" + adminPath + "/dunning/tMisDunningTask/pageFather?buyerId="
			+ order.getBuyerid() + "&dealcode=" + dealcode + "&dunningtaskdbid=" + task.getId() + "&status=" + order.getStatus();
	}

}