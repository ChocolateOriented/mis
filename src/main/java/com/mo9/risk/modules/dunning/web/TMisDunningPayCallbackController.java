/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.web.BaseController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.bean.Mo9ResponseData;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.mo9.risk.modules.dunning.enums.PayStatus;
import com.mo9.risk.modules.dunning.service.TMisDunningConfigureService;
import com.mo9.risk.modules.dunning.service.TMisDunningDeductService;
import com.mo9.risk.util.RequestParamSign;

/**
 * 催收代扣回调Controller
 * @author shijlu
 * @version 2017-04-11
 */
@Controller
public class TMisDunningPayCallbackController extends BaseController {
	
	private static Logger logger = Logger.getLogger(TMisDunningDeductService.class);

	@Autowired
	private TMisDunningDeductService tMisDunningDeductService;
	
	@Autowired
	private TMisDunningConfigureService tMisDunningConfigureService;
	
	@RequestMapping(value = "dunning/tMisDunningDeduct/updateRecord")
	@ResponseBody
	public String updateRecord(HttpServletRequest request, HttpServletResponse response) {
		String data = request.getParameter("object");
		
		if (data == null || "".equals(data)) {
			logger.info("代扣回调报文错误");
			return "OK";
		}
		
		String decodeData = null;
		try {
			decodeData = URLDecoder.decode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("代扣回调报文解析错误：" + data);
			return "OK";
		}
		logger.info("代扣回调报文:" + decodeData);
		
		JSONObject jsonObj = JSON.parseObject(decodeData);
		if (jsonObj == null || jsonObj.isEmpty()) {
			logger.info("代扣回调报文解析错误：" + decodeData);
			return "OK";
		}
		
		String requestSign = jsonObj.getString("sign");
		
		if (requestSign == null && "".equals(requestSign)) {
			logger.info("代扣回调报文验签错误：" + decodeData);
			return "OK";
		}
		
		jsonObj.remove("sign");
		String jsonStr = jsonObj.toJSONString();
		
		String privateKey = tMisDunningConfigureService.getConfigureValue("deduct.privateKey");
		String sign = RequestParamSign.generateParamSign(jsonStr, privateKey);
		
		if (!requestSign.equalsIgnoreCase(sign)) {
			logger.info("代扣回调报文验签错误：" + decodeData);
			return "OK";
		}
		
		Mo9ResponseData responseObj = JSON.parseObject(decodeData, Mo9ResponseData.class);
		
		if (responseObj == null || responseObj.getData() == null) {
			logger.info("代扣回调报文解析错误：" + decodeData);
			return "OK";
		}
		Mo9ResponseData.Mo9ResponseOrder responseOrder = responseObj.getData();
		
		TMisDunningDeduct tMisDunningDeduct = tMisDunningDeductService.get(responseOrder.getInvoice());
		
		if (tMisDunningDeduct == null) {
			logger.warn("先玩后付回调订单不存在，订单号：" + responseOrder.getInvoice());
			return "OK";
		}
		
		String orderStatus = responseOrder.getOrderStatus();
		if ("succeeded".equals(orderStatus)) {
			tMisDunningDeduct.setStatus(PayStatus.succeeded);
		} else if ("failed".equals(orderStatus) || "unmatched".equals(orderStatus)) {
			tMisDunningDeduct.setStatus(PayStatus.failed);
		} else {
			logger.info("代扣回调报文状态错误：" + decodeData);
			return "OK";
		}
		tMisDunningDeduct.setStatusdetail(responseOrder.getMessage());
		tMisDunningDeduct.setReason(responseOrder.getReason());
		tMisDunningDeduct.setChargerate(responseOrder.getChargeRate());
		
		tMisDunningDeductService.updateRecord(tMisDunningDeduct);
		return "OK";
	}

}