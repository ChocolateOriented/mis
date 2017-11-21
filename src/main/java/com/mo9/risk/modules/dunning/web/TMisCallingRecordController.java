/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mo9.risk.modules.dunning.entity.*;
import com.mo9.risk.modules.dunning.service.*;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@RequiresPermissions("dunning:tMisCallingRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(@ModelAttribute("tMisCallingRecord") TMisCallingRecord tMisCallingRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisCallingRecord> page = tMisCallingRecordService.findPage(new Page<TMisCallingRecord>(request, response), tMisCallingRecord);
		for (TMisCallingRecord callingRecord : page.getList()){
			if (callingRecord.getTargetNumber() != null && !"".equals(callingRecord.getTargetNumber())){
				if (callingRecord.getTargetNumber().startsWith("0")){
					callingRecord.setTargetNumber(callingRecord.getTargetNumber().substring(1));
				}
				else if (callingRecord.getTargetNumber().startsWith("9")){
					callingRecord.setTargetNumber(callingRecord.getTargetNumber().substring(1));
				}
				else if (callingRecord.getTargetNumber().startsWith("179690")){
					callingRecord.setTargetNumber(callingRecord.getTargetNumber().substring(6));
				}
				else if (callingRecord.getTargetNumber().startsWith("17969")){
					callingRecord.setTargetNumber(callingRecord.getTargetNumber().substring(5));
				}
			}
		}
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
		model.addAttribute("userId", UserUtils.getUser().getId());
		return "modules/dunning/tMisCallingRecordList";
	}
	
	@RequiresPermissions("dunning:tMisCallingRecord:view")
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

	/**
	 * 下载通话记录
	 * @param audioUrl
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequiresPermissions("dunning:tMisCallingRecord:view")
	@RequestMapping(value = "audioDownload")
	public void audioDownload(@RequestParam(value = "audioUrl") String audioUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String realPath = audioUrl;
		String fileName = realPath.substring(realPath.lastIndexOf("/") + 1);
		response.reset(); //清空response
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);
		OutputStream out = response.getOutputStream();
		URL url = new URL(realPath);
		BufferedInputStream input = new BufferedInputStream(url.openStream());
		try {
			response.setContentType("audio/wav");
			int n = 0;
			byte b[] = new byte[1024];
			while ((n = input.read(b)) != -1)
			{
				out.write(b, 0, n);
			}
			out.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			if(input != null) {
				input.close();
			}
			if(out != null) {
				out.close();
			}
		}

	}
	
	@RequiresPermissions("dunning:tMisCallingRecord:edit")
	@RequestMapping(value = "sync")
	@ResponseBody
	public String sync(Date syncDate, HttpServletRequest request, HttpServletResponse response) {
		if (syncDate == null) {
			return "error";
		}
		tMisCallingRecordService.syncCallRecordManual(syncDate);
		return "success";
	}
}