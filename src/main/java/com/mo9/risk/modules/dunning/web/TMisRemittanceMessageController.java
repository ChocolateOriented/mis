/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.TMisRemittanceExcel;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage.AccountStatus;
import com.mo9.risk.modules.dunning.service.TMisRemittanceMessageService;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
	public String AccountAnalysis(HttpServletRequest request, HttpServletResponse response,
			Model model, String message) {
		model.addAttribute("message", message);
		return "modules/dunning/tMisDunningAccountAnalysis";
	}

	/**
	 * @return java.lang.String
	 * @Description 导入文件
	 */
	@RequestMapping(value = "fileUpload")
	public String fileUpload(MultipartFile file, String channel, RedirectAttributes redirectAttributes) {
		ImportExcel ei = null;
		List<TMisRemittanceExcel> list = null;
		logger.info("正在接析文件:" + file.getOriginalFilename());
		try {
			ei = new ImportExcel(file, 1, 0);
			list = ei.getDataList(TMisRemittanceExcel.class);
			logger.info("完成接析文件:" + file.getOriginalFilename());
		} catch (Exception e) {
			redirectAttributes.addAttribute("message", "解析文件:" + file.getOriginalFilename() + ",发生错误");
			return "redirect:" + adminPath + "/dunning/tMisRemittanceMessage/analysis";
		}

//		tMisRemittanceMessageService.saveBy
		LinkedList<TMisRemittanceMessage> tMisRemittanceList = new LinkedList<TMisRemittanceMessage>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//记录解析成功的条数
		int sucess = 0;
		//记录解析失败的条数
		int fail = 0;
		//记录重复的数据
		int same = 0;
		Date uploadDate = new Date();
		for (TMisRemittanceExcel trExcel : list) {
			if (!"转账".equals(trExcel.getServeType())) {
				continue;
			}
			try{
				BeanValidators.validateWithException(validator, trExcel);
			}catch(ConstraintViolationException ex){
				fail++;
				continue;
			}

			Date parseTime = null;
			try {
				parseTime = sd.parse(trExcel.getRemittancetime());
			} catch (ParseException e) {
				logger.warn("时间格式错误:流水号为" + trExcel.getAlipaySerialNumber());
				fail++;
				continue;
			}
			TMisRemittanceMessage trMessage = new TMisRemittanceMessage();
			trMessage.setRemittancetime(parseTime);
			trMessage.setRemittanceSerialNumber(trExcel.getAlipaySerialNumber());
			trMessage.setRemittancechannel(channel);
			trMessage.setRemittanceamount(trExcel.getRemittanceamount());
			trMessage.setRemittancename(trExcel.getRemittancename());
			trMessage.setRemittanceaccount(trExcel.getRemittanceaccount());
			trMessage.setRemark(trExcel.getRemark());
			trMessage.setAccountStatus(AccountStatus.NOT_AUDIT);
			trMessage.setFinancialtime(uploadDate);
			trMessage.setFinancialuser(UserUtils.getUser().getName());
			trMessage.preInsert();
			tMisRemittanceList.add(trMessage);
			sucess++;
		}

		same = tMisRemittanceMessageService.fileUpload(tMisRemittanceList,channel);
		String message = "";
		if (same == 0) {
			if (fail == 0) {
				message = String.format("上传完成，共导入%d/%d条数据", sucess, sucess);
				redirectAttributes.addAttribute("message", message);
			}

			if (fail > 0) {
				message = String.format("上传失败，共导入%d/%d条数据", sucess, sucess + fail);
				redirectAttributes.addAttribute("message", message);

			}
		}
		if (same > 0) {
			if (fail == 0) {
				message = String.format("上传完成，共导入%d/%d条数据,重复%d条数据", sucess - same, sucess, same);
				redirectAttributes.addAttribute("message", message);
			}

			if (fail > 0) {
				message = String.format("上传失败，共导入%d/%d条数据,重复%d条数据", sucess - same, sucess + fail, same);
				redirectAttributes.addAttribute("message", message);

			}
		}

		return "redirect:" + adminPath + "/dunning/tMisRemittanceMessage/analysis";
	}
}