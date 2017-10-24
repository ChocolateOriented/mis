package com.mo9.risk.modules.dunning.service;

import java.io.IOException;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.alibaba.fastjson.JSON;
import com.mo9.risk.modules.dunning.bean.CallCenterAgentStatus;
import com.mo9.risk.modules.dunning.bean.CallCenterBaseAction;
import com.mo9.risk.modules.dunning.bean.CallCenterWebSocketMessage;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.util.WebSocketSessionUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;

@ServerEndpoint(value = "/ws/phone/{userId}/{agent}/{status}", configurator=SpringConfigurator.class)
public class TMisDunningPhoneWebsocketService {

	private static Logger log = LoggerFactory.getLogger(TMisDunningPhoneWebsocketService.class);
	
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	@Autowired
	TMisDunningPhoneService phoneService;
	
	/**
	 * 打开连接时触发
	 * @param userId
	 * @param agent
	 * @param status
	 */
	@OnOpen
	public void onOpen(@PathParam("userId") String userId, @PathParam("agent") String agent, @PathParam("status") String status, Session session) {
		TMisDunningPeople people = tMisDunningPeopleService.get(userId);
		if (people == null || StringUtils.isBlank(agent) || !agent.equals(people.getAgent())) {
			return;
		}
		
		CallCenterWebSocketMessage msg = new CallCenterWebSocketMessage();
		msg.setAgent(agent);
		msg.setPeopleId(userId);
		msg.setOperation(status);
		Session sessionOld = WebSocketSessionUtil.put(userId,session);
		if(sessionOld != null) {
			try {
				sessionOld.close();
			} catch (IOException e) {
				log.info("关闭失败");
			}
		}
		phoneService.changeAgentStatus(msg);
	}

	/**
	 * 收到客户端消息时触发
	 * @param userId
	 * @param agent
	 * @param message
	 * @return
	 */
	@OnMessage
	public void onMessage(@PathParam("userId") String userId, @PathParam("agent") String agent, String message) {
		CallCenterWebSocketMessage msgObj = JSON.parseObject(message, CallCenterWebSocketMessage.class);
		String operation = null;
		if (msgObj == null || (operation = msgObj.getOperation()) == null) {
			return;
		}
		msgObj.setAgent(agent);
		msgObj.setPeopleId(userId);
		
		try {
			//坐席状态变更针对在线和小休的切换
			if (CallCenterAgentStatus.AVAILABLE.equals(operation) || CallCenterAgentStatus.ON_BREAK.equals(operation)) {
				phoneService.changeAgentStatus(msgObj);
				return;
			}
			//点击呼叫
			if (CallCenterBaseAction.ORIGINATE.equals(operation)) {
				phoneService.originate(msgObj);
				return;
			}
			//点击挂断
			if (CallCenterBaseAction.HANGUP.equals(operation)) {
				phoneService.hangup(msgObj);
				return;
			}
			//点击保持
			if (CallCenterBaseAction.HOLD.equals(operation)) {
				phoneService.hold(msgObj);
				return;
			}
			//点击取消保持
			if (CallCenterBaseAction.HOLD_OFF.equals(operation)) {
				phoneService.holdOff(msgObj);
				return;
			}
			//点击接听应答
			if (CallCenterBaseAction.ANSWER.equals(operation)) {
				phoneService.answer(msgObj);
				return;
			}
		} catch (Exception e) {
			log.warn("websocket处理错误：" + e);
			msgObj.setResult("error");
			try {
				WebSocketSessionUtil.sendMessage(JSON.toJSONString(msgObj), msgObj.getPeopleId());
			} catch (IOException e1) {
				log.info("WebSocket发送错误消息失败：" + e1.getMessage());
			}
		}
	}

	/**
	 * 异常时触发
	 * @param userId
	 * @param agent
	 * @param throwable
	 * @param session
	 */
	@OnError
	public void onError(@PathParam("userId") String userId, @PathParam("agent") String agent, Throwable throwable, Session session) {
		log.info("连接异常:" + throwable);
	}

	/**
	 * 关闭连接时触发
	 * @param userId
	 * @param agent
	 * @param session
	 */
	@OnClose
	public void onClose(@PathParam("userId") String userId, @PathParam("agent") String agent, Session session) {
		CallCenterWebSocketMessage msgObj = new CallCenterWebSocketMessage();
		msgObj.setAgent(agent);
		msgObj.setPeopleId(userId);
		msgObj.setOperation(CallCenterAgentStatus.LOGGED_OUT);
		CallCenterWebSocketMessage result = phoneService.changeAgentStatus(msgObj);
		if (!"success".equals(result.getResult())) {
			log.info("关闭连接时坐席离线失败:" + result.getMsg());
		}
		
		WebSocketSessionUtil.remove(userId);
	}

}
