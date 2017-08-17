package com.mo9.risk.modules.dunning.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping(value = "/number")
public class TMisDunningNumberCleanCallbackController extends BaseController {
	 @Autowired
		private TMisDunningTaskService tMisDunningTaskService;
	/**
	 * 号码清洗回调
	 * @param reqNo
	 * @param check_result
	 * @param request
	 */
	@RequestMapping(value = "numberCleanBack")
	public  void  numberCleanBack(String reqNo,String checkResult,HttpServletRequest request){
		if(StringUtils.isEmpty(reqNo)||StringUtils.isEmpty(checkResult)){
			logger.warn(new Date()+"订单号为:"+reqNo+"回调失败");
			return;
		}
//		tMisDunningTaskService.numberCleanBack(reqNo,checkResult);
	}
}
