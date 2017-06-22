package com.mo9.risk.modules.dunning.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.AccountStatus;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceExcel;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.modules.dunning.service.TMisRemittanceMessageService;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 账目解析
 * 
 * @author jwchi
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningAccountAnalysis")
public class TMisDunningAccountAnalysisController extends BaseController {
	
	private static Logger logger = Logger.getLogger(TMisDunningTaskService.class);

	@Autowired
	private TMisRemittanceMessageService tMisRemittanceMessageService;

	/**
	 * 跳转账目解析页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "")
	public String AccountAnalysis(HttpServletRequest request, HttpServletResponse response,Model model,String message) {
		model.addAttribute("message", message);
		return "modules/dunning/tMisDunningAccountAnalysis";
	}

	/**
	 * 导入文件
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "fileUpload")
	public String fileUpload(MultipartFile file, String aliPay, RedirectAttributes redirectAttributes) {
		ImportExcel ei = null;
		List<TMisRemittanceExcel> list = null;
		//记录解析成功的条数
		int sucess=0;
		//记录解析失败的条数
		int fail=0;
		//记录重复的数据
		int  same=0;
		logger.info("正在接析文件:"+file.getOriginalFilename());
		try {
			ei = new ImportExcel(file, 1, 0);
			list = ei.getDataList(TMisRemittanceExcel.class);
			logger.info("完成接析文件:"+file.getOriginalFilename());
		} catch (Exception e) {
			logger.warn("解析文件:"+file.getOriginalFilename()+",发生错误");
		}
		LinkedList<TMisRemittanceMessage> tMisRemittanceList = new LinkedList<TMisRemittanceMessage>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (TMisRemittanceExcel trExcel : list) {
			TMisRemittanceMessage trMessage = new TMisRemittanceMessage();
			if ("转账".equals(trExcel.getServeType())) {
				//判断解析的Excel中必须字段为空.说明本条数据解析失败.
				if(StringUtils.isEmpty(trExcel.getAlipaySerialNumber())||StringUtils.isEmpty(trExcel.getAlipayRemittanceNumber())||
				   StringUtils.isEmpty(trExcel.getRemittancetime())||StringUtils.isEmpty(trExcel.getRemittanceamountText())||	
				   StringUtils.isEmpty(trExcel.getRemittanceaccount())||StringUtils.isEmpty(trExcel.getRemittancename())
				  ){
					fail++;
					continue;
				}
				
				Date parseTime = null;
				try {
					parseTime = sd.parse(trExcel.getRemittancetime());
				} catch (ParseException e) {
					logger.warn("时间转发发生错误:流水号为"+trExcel.getAlipaySerialNumber());
					fail++;
					continue;
				}
				trMessage.setRemittancetime(parseTime);
				trMessage.setAlipaySerialNumber(trExcel.getAlipaySerialNumber());
				trMessage.setRemittancechannel(aliPay);
				trMessage.setRemittanceamount(trExcel.getRemittanceamount());
				trMessage.setRemittancename(trExcel.getRemittancename());
				trMessage.setRemittanceaccount(trExcel.getRemittanceaccount());
				trMessage.setRemark(trExcel.getRemark());
				trMessage.setAccountStatus(AccountStatus.WCZ);
				trMessage.setFinancialtime(new Date());
				trMessage.setFinancialuser(UserUtils.getUser().getName());
				trMessage.preInsert();
				tMisRemittanceList.add(trMessage);
				sucess++;
			}
		}
		same=tMisRemittanceMessageService.fileUpload(tMisRemittanceList);
		String message="";
		if(same==0){
			if(fail==0){
				message = String.format("上传完成，共导入%d/%d条数据", sucess,sucess);
				redirectAttributes.addAttribute("message",message);
			}
			
			if(fail>0){
				message = String.format("上传失败，共导入%d/%d条数据", sucess,sucess+fail);
				redirectAttributes.addAttribute("message",message);
				
			}
		}
		if(same>0){
			if(fail==0){
				message = String.format("上传完成，共导入%d/%d条数据,重复%d条数据", sucess-same,sucess,same);
				redirectAttributes.addAttribute("message",message);
			}
			
			if(fail>0){
				message= String.format("上传失败，共导入%d/%d条数据,重复%d条数据", sucess-same,sucess+fail,same);
				redirectAttributes.addAttribute("message",message);
				
			}
		}
		 System.out.println(message);
		
		
		return "redirect:" + adminPath + "/dunning/tMisDunningAccountAnalysis";
	}

}
