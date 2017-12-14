package com.mo9.risk.modules.dunning.api;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mo9.risk.modules.dunning.bean.TMisDunningLetterDownLoad;
import com.mo9.risk.modules.dunning.entity.TMisDunningLetter;
import com.mo9.risk.modules.dunning.service.TMisDunningLetterService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping(value = "/letter")
public class TMisDunningLetterDownLoadController extends BaseController {
	@Autowired
	TMisDunningLetterService tMisDunningLetterService;
	/**
	 * 号码清洗回调
	 * @param reqNo
	 * @param check_result
	 * @param request
	 */
	@RequestMapping(value = "downLoad")
	public  void  numberCleanBack(String identity,HttpServletRequest request, HttpServletResponse response){
		String fileName = "信函" + DateUtils.getDate("yyyy/MM/dd")+".xlsx";
		List<TMisDunningLetterDownLoad> letterList = tMisDunningLetterService.lettersDownLoad(identity); 
		
		if (null == letterList || letterList.isEmpty()) {
			logger.warn("未查到需要发送信函的订单!");
			return ;
		}
		try {
			new ExportExcel("", TMisDunningLetterDownLoad.class).setDataList(letterList).write(response, fileName).dispose();
		} catch (Exception e) {
			logger.warn("下载失败！失败信息："+e);
		}
	}
}