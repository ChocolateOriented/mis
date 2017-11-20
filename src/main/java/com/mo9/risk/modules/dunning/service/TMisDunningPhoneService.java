package com.mo9.risk.modules.dunning.service;

import java.io.IOException;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.mo9.risk.modules.dunning.bean.CallCenterAgentInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterAgentState;
import com.mo9.risk.modules.dunning.bean.CallCenterCallinInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterCalling;
import com.mo9.risk.modules.dunning.bean.CallCenterCalloutInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterModifyAgent;
import com.mo9.risk.modules.dunning.bean.CallCenterPageResponse;
import com.mo9.risk.modules.dunning.bean.CallCenterQueryCallInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterWebSocketMessage;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.manager.CallCenterManager;
import com.mo9.risk.util.WebSocketSessionUtil;
import com.thinkgem.jeesite.common.utils.IdGen;

/**
 * 
 * 软电话service
 * @author jwchi
 *
 */

@Service
@Transactional(readOnly = true)
public class TMisDunningPhoneService {
	
	@Autowired
	private CallCenterManager callCenterManager;
	
	@Autowired
	private TMisAgentInfoService tMisAgentInfoService;
	
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoService;
	
	@Autowired
	private TMisCallingRecordService tMisCallingRecordService;
	
	private static final Logger logger = LoggerFactory.getLogger(TMisDunningPhoneService.class);
	
	/**
	 * 坐席状态变更
	 */
	@Transactional(readOnly = false)
	public CallCenterWebSocketMessage changeAgentStatus(CallCenterWebSocketMessage msg) {
		CallCenterModifyAgent action = new CallCenterModifyAgent();
	    action.setAgent(msg.getAgent());
	    action.setStatus(msg.getOperation());
		try {
			callCenterManager.changeAgentStatus(action);
			msg.setResult("success");
			tMisAgentInfoService.updateStatus(msg);
		} catch (Exception e) {
			msg.setResult("error");
			logger.info("变更坐席状态失败：" + e);
		}
		try {
			WebSocketSessionUtil.sendMessage(JSON.toJSONString(msg), msg.getPeopleId());
		} catch (IOException e) {
			logger.info("WebSocket发送变更坐席状态结果消息失败：" + e);
		}
		return msg;
	}
	
	/**
	 * 发起呼叫
	 * @param action
	 * @return
	 */
	@Transactional(readOnly = false)
	public CallCenterWebSocketMessage originate(CallCenterWebSocketMessage msg) {
		CallCenterCalling action = new CallCenterCalling();
		action.setAgent(msg.getAgent());
		action.setTarget(msg.getTarget());
		action.setAutoAnswer(true);
		action.setCustomerno(generateCustomno());
		try {
			callCenterManager.originate(action);
			msg.setResult("success");
		} catch (Exception e) {
			msg.setResult("error");
			logger.info("发起呼叫失败：" + e);
		}
		try {
			WebSocketSessionUtil.sendMessage(JSON.toJSONString(msg), msg.getPeopleId());
		} catch (IOException e) {
			logger.info("WebSocket发送呼叫结果消息失败：" + e);
		}
		return msg;
	}
	
	/**
	 * 呼叫中断
	 */
	@Transactional(readOnly = false)
	public CallCenterWebSocketMessage hangup(CallCenterWebSocketMessage msg) {
		CallCenterCalling action = new CallCenterCalling();
		action.setAgent(msg.getAgent());
		try {
			callCenterManager.hangup(action);
			msg.setResult("success");
		} catch (Exception e) {
			msg.setResult("error");
			logger.info("呼叫中断失败：" + e);
			
			//挂断失败时，若坐席接听状态为空闲，则发送空闲消息
			String state = TMisAgentInfoService.callStatus.get(msg.getAgent());
			if (state == null || CallCenterAgentState.AVAILABLE.equals(state)) {
				msg.setOperation(CallCenterAgentState.AVAILABLE);
				msg.setResult("success");
			}
		}
		try {
			WebSocketSessionUtil.sendMessage(JSON.toJSONString(msg), msg.getPeopleId());
		} catch (IOException e) {
			logger.info("WebSocket发送呼叫中断结果消息失败：" + e);
		}
		return msg;
	}
	
	/**
	 * 呼叫保持
	 */
	public CallCenterWebSocketMessage hold(CallCenterWebSocketMessage msg) {
		CallCenterCalling action = new CallCenterCalling();
		action.setAgent(msg.getAgent());
		try {
			callCenterManager.hold(action);
			msg.setResult("success");
		} catch (Exception e) {
			msg.setResult("error");
			logger.info("呼叫保持失败：" + e);
		}
		try {
			WebSocketSessionUtil.sendMessage(JSON.toJSONString(msg), msg.getPeopleId());
		} catch (IOException e) {
			logger.info("WebSocket发送呼叫保持结果消息失败：" + e);
		}
		return msg;
	}
	
	/**
	 * 取消保持
	 */
	public CallCenterWebSocketMessage holdOff(CallCenterWebSocketMessage msg) {
		CallCenterCalling action = new CallCenterCalling();
		action.setAgent(msg.getAgent());
		try {
			callCenterManager.holdOff(action);
			msg.setResult("success");
		} catch (Exception e) {
			msg.setResult("error");
			logger.info("取消保持失败：" + e);
		}
		try {
			WebSocketSessionUtil.sendMessage(JSON.toJSONString(msg), msg.getPeopleId());
		} catch (IOException e) {
			logger.info("WebSocket发送取消保持结果消息失败：" + e);
		}
		return msg;
	}
	
	/**
	 * 接听应答
	 */
	public CallCenterWebSocketMessage answer(CallCenterWebSocketMessage msg) {
		CallCenterCalling action = new CallCenterCalling();
		action.setAgent(msg.getAgent());
		try {
			callCenterManager.answer(action);
			msg.setResult("success");
		} catch (Exception e) {
			msg.setResult("error");
			logger.info("接听应答失败：" + e);
		}
		try {
			WebSocketSessionUtil.sendMessage(JSON.toJSONString(msg), msg.getPeopleId());
		} catch (IOException e) {
			logger.info("WebSocket发送接听应答消息失败：" + e);
		}
		return msg;
	}
	
	/**
	 * 变更接听状态
	 */
	@Transactional(readOnly = false)
	public void changeState(CallCenterAgentInfo noticeInfo) {
		TMisAgentInfo agentInfo = tMisAgentInfoService.getInfoByAgent(noticeInfo.getName());
		if (agentInfo == null || agentInfo.getPeopleId() == null) {
			return;
		}
		
		Session session = WebSocketSessionUtil.get(agentInfo.getPeopleId());
		if (session == null || !session.isOpen()) {
			return;
		}
		
		String agent = noticeInfo.getName();
		CallCenterWebSocketMessage msg = new CallCenterWebSocketMessage();
		msg.setAgent(agent);
		msg.setOperation(noticeInfo.getState());
		if (CallCenterAgentState.RINGING.equals(noticeInfo.getState())) {
			String target = noticeInfo.getTarget();
			msg.setTarget(target);
			TRiskBuyerPersonalInfo personInfo = personalInfoService.getBuyerInfoByMobile(msg.getTarget());
			msg.setName(personInfo == null ? null : personInfo.getRealName());
		}
		try {
			session.getBasicRemote().sendText(JSON.toJSONString(msg));
		} catch (IOException e) {
			logger.info("WebSocket发送变更接听状态消息失败：" + e);
		}
	}
	
	/**
	 * 获取呼出信息
	 */
	public CallCenterPageResponse<CallCenterCalloutInfo> callOutInfo(CallCenterQueryCallInfo action) {
		CallCenterPageResponse<CallCenterCalloutInfo> cbr = null;
		try {
			cbr = callCenterManager.calloutInfo(action);
		} catch (Exception e) {
			logger.info("获取呼出信息失败," + e);
		}
		return cbr;
	}
	
	/**
	 * 获取呼入信息
	 */
	public CallCenterPageResponse<CallCenterCallinInfo> callinInfo(CallCenterQueryCallInfo action) {
		CallCenterPageResponse<CallCenterCallinInfo> cbr = null;
		try {
			cbr = callCenterManager.callinInfo(action);
		} catch (Exception e) {
			logger.info("获取呼入信息失败," + e);
		}
		return cbr;
	}
	
	
	/**
	 * 生成唯一客户编号
	 */
	private String generateCustomno() {
		return "mo9." + IdGen.uuid();
	}
	
	/**
	 * 判断是否为本地手机号
	 */
	public boolean isLocalMobile(String mobile) {
		String location = tMisCallingRecordService.queryMobileLocation(mobile);
		return "上海".equals(location);
	}
}
