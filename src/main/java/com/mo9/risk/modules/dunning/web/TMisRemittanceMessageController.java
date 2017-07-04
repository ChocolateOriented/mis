/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.AlipayRemittanceExcel;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessagChecked;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.mo9.risk.modules.dunning.service.TMisRemittanceMessageService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 财务确认汇款信息Controller
 * @author 徐盛
 * @version 2016-08-11
 */
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
	public String accountAnalysis(Model model, String message) {
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
	public String detail(TMisRemittanceMessage tMisRemittanceMessage,Model model,HttpServletRequest request, HttpServletResponse response) {
		Page<TMisRemittanceMessage> page = tMisRemittanceMessageService.findAcountPageList(new Page<TMisRemittanceMessage>(request, response), tMisRemittanceMessage);
		model.addAttribute("page",page);
		return "modules/dunning/tMisDunningAccountDetail";
	}
	@ModelAttribute
	public TMisRemittanceMessage get(@RequestParam(required=false) String id) {
		TMisRemittanceMessage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisRemittanceMessageService.get(id);
		}
		if (entity == null){
			entity = new TMisRemittanceMessage();
		}
		return entity;
	}

	@ModelAttribute
	public TMisRemittanceMessagChecked getMessagChecked() {
		TMisRemittanceMessagChecked entity = null;

		entity = new TMisRemittanceMessagChecked();

		return entity;
	}

	/**
	 * 跳转查账入账
	 */
	@RequestMapping(value = "confirmList")
	public String accountTotal(TMisRemittanceMessagChecked tMisRemittanceMessagChecked,String childPage,Model model,HttpServletRequest request, HttpServletResponse response) {
		Page<TMisRemittanceMessagChecked> page = tMisRemittanceMessageService.findMessagList(new Page<TMisRemittanceMessagChecked>(request, response), tMisRemittanceMessagChecked,childPage);
		HttpSession session = request.getSession();
		session.setAttribute("page", page);
		model.addAttribute("childPage",childPage);
		return "modules/dunning/tMisDunningAccountTotal";
	}
	/**
	 * 跳转已查账
	 */
	@RequestMapping(value = "checked")
	public String accountChecked(String child,TMisRemittanceMessagChecked tMisRemittanceMessagChecked,Model model,HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isNotEmpty(child)){
			Page<TMisRemittanceMessagChecked> page = tMisRemittanceMessageService.findMessagList(new Page<TMisRemittanceMessagChecked>(request, response), tMisRemittanceMessagChecked,"checked");
			model.addAttribute("pagechecked",page);
		}else{
		HttpSession session = request.getSession();
		model.addAttribute("pagechecked",session.getAttribute("page"));
		}
		return "modules/dunning/tMisDunningAccountChecked";
	}
	/**
	 * 跳转已完成
	 */
	@RequestMapping(value = "completed")
	public String accountCompleted(String child, TMisRemittanceMessagChecked tMisRemittanceMessagChecked, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isNotEmpty(child)) {
			Page<TMisRemittanceMessagChecked> page = tMisRemittanceMessageService.findMessagList(new Page<TMisRemittanceMessagChecked>(request, response), tMisRemittanceMessagChecked,"completed");
			model.addAttribute("pagecompleted",page);
		} else {
			HttpSession session = request.getSession();
			model.addAttribute("pagecompleted", session.getAttribute("page"));
		}

		return "modules/dunning/tMisDunningAccountCompleted";
	}
	/**
	 * 悬乎流水号显示详细信息
	 */
	@RequestMapping(value = "accountDetail")
	public String accountDetail(String child, TMisRemittanceMessagChecked tMisRemittanceMessagChecked, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		return "modules/dunning/dialog/dialogAddSmsTemplate";
	}

	/**
	 * 通过电话查询订单
	 */
	@RequestMapping(value = "findOrderByMobile")
	@ResponseBody
	public DunningOrder findOrderByMobile(String mobile){
		return tMisRemittanceMessageService.findPaymentOrderByMobile(mobile);
	}

	/**
	 * 查询汇款信息通过渠道与流水号
	 */
	@RequestMapping(value = "findRemittance")
	@ResponseBody
	public TMisRemittanceConfirm findRemittance(String remittanceChannel,String remittanceSerialNumber){
		List<TMisRemittanceConfirm> remittanceConfirmList = tMisRemittanceMessageService.findNotFinish(remittanceChannel,remittanceSerialNumber);
		if (remittanceConfirmList !=null && remittanceConfirmList.size()>0){
			return remittanceConfirmList.get(0);
		}
		return null;
	}

	/**
	 * 手工查账
	 */
	@RequestMapping(value = "handleAudit")
	@ResponseBody
	public String handleAudit(TMisRemittanceConfirm remittanceConfirm){
		if (remittanceConfirm == null){
			return "参数不能为空";
		}
		if (StringUtils.isBlank(remittanceConfirm.getSerialnumber())){
			return  "流水号不能为空";
		}
		if (StringUtils.isBlank(remittanceConfirm.getRemittancechannel())){
			return  "渠道不能为空";
		}
		if (StringUtils.isBlank(remittanceConfirm.getDealcode())){
			return  "订单号不能为空";
		}
		boolean success = tMisRemittanceMessageService.handleAudit(remittanceConfirm);
		if (success){
			return "success";
		}
		return "查账失败";
	}
}