/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.AlipayRemittanceExcel;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage.AccountStatus;
import com.mo9.risk.modules.dunning.service.TMisRemittanceMessageService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 财务确认汇款信息Controller
 * @author 徐盛
 * @version 2016-08-11
 */
@EnableAsync
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisRemittanceMessage")
public class TMisRemittanceMessageController extends BaseController {

	@Autowired
	private TMisRemittanceMessageService tMisRemittanceMessageService;

	@RequiresPermissions("dunning:tMisRemittanceMessage:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisRemittanceMessage tMisRemittanceMessage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisRemittanceMessage> page = tMisRemittanceMessageService.findPage(new Page<TMisRemittanceMessage>(request, response), tMisRemittanceMessage); 
		model.addAttribute("page", page);
		return "modules/dunning/tMisRemittanceMessageList";
	}

	/**
	 * 跳转账目解析页面
	 */
	@RequestMapping(value = "analysis")
	public String AccountAnalysis(Model model, String message) {
		model.addAttribute("message", message);
		return "modules/dunning/tMisDunningAccountAnalysis";
	}

	/**
	 * @return java.lang.String
	 * @Description 导入文件
	 */
	@RequestMapping(value = "fileUpload")
	public String fileUpload(MultipartFile file, String channel, RedirectAttributes redirectAttributes) {
		String redirectUrl = "redirect:analysis";
		if (null == file || StringUtils.isBlank(channel)){
			redirectAttributes.addAttribute("message", "参数错误");
			return redirectUrl;
		}
		//解析上传Excel
		List<AlipayRemittanceExcel> list ;
		logger.info("正在接析文件:" + file.getOriginalFilename());
		try {
			ImportExcel ei = new ImportExcel(file, 1, 0);
			list = ei.getDataList(AlipayRemittanceExcel.class);
			logger.info("完成接析文件:" + file.getOriginalFilename());
		} catch (Exception e) {
			redirectAttributes.addAttribute("message", "解析文件:" + file.getOriginalFilename() + ",发生错误");
			return redirectUrl;
		}

		//校验,保存汇款信息
		LinkedList<TMisRemittanceMessage> tMisRemittanceList = new LinkedList<TMisRemittanceMessage>();
		String errorMsg = tMisRemittanceMessageService.getValidRemittanceMessage(list,tMisRemittanceList);
		int total = tMisRemittanceList.size();
		int same = tMisRemittanceMessageService.saveUniqList(tMisRemittanceList,channel);
		//调用自动查账
		tMisRemittanceMessageService.autoAuditAfterFinancialtime(DateUtils.parseDate(DateUtils.getDate()));

		//上传结果信息
		StringBuilder message = new StringBuilder();
		if (StringUtils.isBlank(errorMsg)){
			message.append("上传完成");
		}else {
			message.append("上传失败");
		}
		message.append(String.format(",共导入%d/%d条数据",total-same,total));
		if(same>0){
			message.append(String.format(",重复%d条数据",same));
		}
		message.append(errorMsg);
		logger.info(message.toString());
		redirectAttributes.addAttribute("message",message.toString());
		return redirectUrl;
	}
	
	/**
	 * 跳转对公明细
	 */
	@RequestMapping(value = "detail")
	public String detail(TMisRemittanceMessage tMService,Date begindealtime,Date enddealtime,Model model,HttpServletRequest request, HttpServletResponse response) {
		Page<TMisRemittanceMessage> page = tMisRemittanceMessageService.findAcountPageList(new Page<TMisRemittanceMessage>(request, response), tMService,begindealtime,enddealtime);
		model.addAttribute("page",page);
		AccountStatus[] values = AccountStatus.values();
		List<AccountStatus> asList = Arrays.asList(values);
		model.addAttribute("statusList", asList);
		model.addAttribute("tMisRemittanceMessage", new TMisRemittanceMessage());
		return "modules/dunning/tMisDunningAccountDetail";
	}
}