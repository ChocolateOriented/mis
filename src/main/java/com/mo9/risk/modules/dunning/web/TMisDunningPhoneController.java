/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mo9.risk.modules.dunning.bean.CallCenterCallinInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterCalloutInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterPageResponse;
import com.mo9.risk.modules.dunning.bean.CallCenterQueryCallInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterWebSocketMessage;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.service.TMisAgentInfoService;
import com.mo9.risk.modules.dunning.service.TMisDunningPhoneService;
import com.mo9.risk.util.WebSocketSessionUtil;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 软电话Controller
 * @author jwchi
 * @version 2017-09-12
 */
@Controller
public class TMisDunningPhoneController extends BaseController {
	@Autowired
	private TMisDunningPhoneService phoneService;
	
	@Autowired
	private TMisAgentInfoService tMisAgentInfoService;
	
		//从首页跳转软电话页面
	@RequestMapping(value = "${adminPath}/dunning/tMisDunningPhone/judgeAgent")
	@RequiresPermissions("dunning:phone:view")
	@ResponseBody
	public boolean judgeAgent(Model model,HttpServletRequest request, HttpServletResponse response) {
		String userId = UserUtils.getUser().getId();
		TMisAgentInfo info = tMisAgentInfoService.getInfoByPeopleId(userId);
		return info != null && info.getAgent() != null;
	}
	@RequestMapping(value = "${adminPath}/dunning/tMisDunningPhone/phones")
	@RequiresPermissions("dunning:phone:view")
	public String phones(Model model,HttpServletRequest request, HttpServletResponse response) {
		String userId = UserUtils.getUser().getId();
		TMisAgentInfo info = tMisAgentInfoService.getInfoByPeopleId(userId);
		String url =  DictUtils.getDictValue("websocketUrl", "callcenter", "");
		model.addAttribute("userId", userId);
		model.addAttribute("agent", info.getAgent());
		model.addAttribute("queue", info.getQueue());
		model.addAttribute("direct", info.getDirect());
		model.addAttribute("url", url);
		return "modules/dunning/tMisDunningKeyPhone";
	}
	/**
	 * 个人呼出信息获取
	 * @param callCenterQueryCallInfo
	 */
	@RequestMapping(value = "f/numberPhone/callout")
	public  String  callout(CallCenterQueryCallInfo callCenterQueryCallInfo,HttpServletRequest request, HttpServletResponse response,Model model){
		if(StringUtils.isEmpty(callCenterQueryCallInfo.getPage())){
			callCenterQueryCallInfo.setPage("1");
			callCenterQueryCallInfo.setPagesize("30");
		}
		CallCenterPageResponse<CallCenterCalloutInfo> callOutInfo = phoneService.callOutInfo(callCenterQueryCallInfo);
		if(callOutInfo!=null&&callOutInfo.getData()!=null){
			Page<CallCenterCalloutInfo> page=new Page<CallCenterCalloutInfo>(request, response);
			page.setCount(callOutInfo.getData().getTotal());
			page.setPageNo(callOutInfo.getData().getPage());
			page.setPageSize(callOutInfo.getData().getPageSize());
			page.setList(callOutInfo.getData().getResults());
			model.addAttribute("page",page);
			model.addAttribute("call","callout");
		}
		return "/modules/dunning/tMisDunningcallingPhoneInfo";
	}
	/**
	 * 个人呼入信息获取
	 * @param callCenterQueryCallInfo
	 */
	@RequestMapping(value = "f/numberPhone/callin")
	public  String  callin(CallCenterQueryCallInfo callCenterQueryCallInfo,HttpServletRequest request, HttpServletResponse response,Model model){
		if(StringUtils.isEmpty(callCenterQueryCallInfo.getPage())){
			callCenterQueryCallInfo.setPage("1");
			callCenterQueryCallInfo.setPagesize("30");
		}
		CallCenterPageResponse<CallCenterCallinInfo> callinInfo = phoneService.callinInfo(callCenterQueryCallInfo);
		if(callinInfo!=null&&callinInfo.getData()!=null){
			Page<CallCenterCallinInfo> page=new Page<CallCenterCallinInfo>(request, response);
			page.setCount(callinInfo.getData().getTotal());
			page.setPageNo(callinInfo.getData().getPage());
			page.setPageSize(callinInfo.getData().getPageSize());
			page.setList(callinInfo.getData().getResults());
			model.addAttribute("page",page);
			model.addAttribute("call","callin");
		}
		return "/modules/dunning/tMisDunningcallingPhoneInfo";
	}
	/**
	 * 从父页面点击号码跳转软电话页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "${adminPath}/dunning/tMisDunningPhone/fatherPageTOPhonePage")
	@ResponseBody
	public boolean fatherPageTOPhonePage(String target, String peopleId, String name, HttpServletRequest request, HttpServletResponse response) {
		Session session = WebSocketSessionUtil.get(peopleId);
		if(session == null){
			return false;
		}
		CallCenterWebSocketMessage message = new CallCenterWebSocketMessage();
		TMisAgentInfo agentInfo = tMisAgentInfoService.getInfoByPeopleId(peopleId);
		if (agentInfo == null || StringUtils.isEmpty(agentInfo.getAgent())) {
			return false;
		}
		String agent = agentInfo.getAgent();
		message.setAgent(agent);
		
		String dialTarget = target;
		if (!phoneService.isLocalMobile(target)) {
			dialTarget = "0" + dialTarget;
		}
		dialTarget = "9" + dialTarget;
		
		message.setTarget(dialTarget);
		message.setPeopleId(peopleId);
		message.setName(name);
		message.setOperation("originate");
		phoneService.originate(message);
		return true;
	}
}