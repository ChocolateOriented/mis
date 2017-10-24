/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.alibaba.fastjson.JSON;
import com.mo9.risk.modules.dunning.bean.CallCenterAgentInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterAgentState;
import com.mo9.risk.modules.dunning.bean.CallCenterBaseNotice;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.service.TMisAgentInfoService;
import com.mo9.risk.modules.dunning.service.TMisDunningPhoneService;

/**
 * CTI回调Controller
 */
@Controller
public class TMisDunningCTICallBackController extends BaseController {
	
	@Autowired
	private TMisDunningPhoneService tMisDunningPhoneService;
	
	@Autowired
	private TMisAgentInfoService tMisAgentInfoService;
	
	@RequestMapping(value = "dunning/CTI/notice")
	@ResponseBody
	public String notice(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isBlank(body)) {
			return "NO";
		}
		
		CallCenterBaseNotice notice = null;
		
		try {
			body = URLDecoder.decode(body, "utf-8");
			notice = JSON.parseObject(body, CallCenterBaseNotice.class);
			logger.info("CTI回调：" + body);
		} catch (UnsupportedEncodingException e) {
			return "NO";
		}
		
		if (notice == null) {
			return "NO";
		}
		String type = notice.getType();
		String data = notice.getData();
		
		if (StringUtils.isBlank(type) || StringUtils.isBlank(data)) {
			return "NO";
		}
		
		if (CallCenterBaseNotice.EXTENSION_STATE_NOTICE.equals(type)) {
			CallCenterAgentInfo noticeInfo = JSON.parseObject(data, CallCenterAgentInfo.class);
			if (noticeInfo != null && (CallCenterAgentState.ONCALL.equals(noticeInfo.getState())
					|| CallCenterAgentState.AVAILABLE.equals(noticeInfo.getState()))) {
				TMisAgentInfo agentInfo = tMisAgentInfoService.getInfoByExtension(noticeInfo.getExtension());
				if (agentInfo != null) {
					noticeInfo.setName(agentInfo.getAgent());
					tMisDunningPhoneService.changeState(noticeInfo);
				}
			}
		} else if (CallCenterBaseNotice.STATE_NOTICE.equals(type)) {
			CallCenterAgentInfo noticeInfo = JSON.parseObject(data, CallCenterAgentInfo.class);
			if (noticeInfo != null && CallCenterAgentState.RECEIVING.equals(noticeInfo.getState())) {
				tMisDunningPhoneService.changeState(noticeInfo);
			}
		} else {
			//not care
		}
		
		return "OK";
	}
}