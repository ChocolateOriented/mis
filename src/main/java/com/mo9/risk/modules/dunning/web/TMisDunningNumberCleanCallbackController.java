package com.mo9.risk.modules.dunning.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mo9.risk.modules.dunning.entity.TmisDunningNumberClean;
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
	@RequestMapping(value = "numberBack")
	public  void  numberCleanBack(TmisDunningNumberClean tmisDunningNumberClean,HttpServletRequest request){
		if(StringUtils.isEmpty(tmisDunningNumberClean.getReqNo())||StringUtils.isEmpty(tmisDunningNumberClean.getTaskId())||
				StringUtils.isEmpty(tmisDunningNumberClean.getPhone())||StringUtils.isEmpty(tmisDunningNumberClean.getCheckResult())){
			logger.warn(new Date()+"必要参数 为空，该订单号码未清洗");
			return;
		}
		String checkResult=tmisDunningNumberClean.getCheckResult();
		checkResult= checkResult.substring(0, 1);
		if("0".equals(checkResult)||"1".equals(checkResult)){
			logger.warn(new Date()+"流水号为："+tmisDunningNumberClean.getReqNo()+"，回调成功");
			tMisDunningTaskService.numberCleanBack(tmisDunningNumberClean);
		}else{
			logger.warn(new Date()+"必要参数有误，该订单号码未清洗");
			return;
		}
	}
}