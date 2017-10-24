package com.mo9.risk.modules.dunning.manager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mo9.risk.modules.dunning.bean.CallCenterModifyAgent;
import com.mo9.risk.modules.dunning.bean.CallCenterAgentInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterBaseAction;
import com.mo9.risk.modules.dunning.bean.CallCenterCallinInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterCalloutInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterCalling;
import com.mo9.risk.modules.dunning.bean.CallCenterPageResponse;
import com.mo9.risk.modules.dunning.bean.CallCenterQueryCallInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterResponseData;
import com.mo9.risk.modules.dunning.bean.CallCenterBaseResponse;
import com.mo9.risk.util.PostRequest;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

@Service
public class CallCenterManager {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private final String url = DictUtils.getDictValue("ctiUrl", "callcenter", "https://www.web-rtc.top:8443/") + "cti/cpi";
	
	public <T extends CallCenterBaseResponse> T commonAction(CallCenterBaseAction action, Class<T> clazz) throws IOException {
		return commonAction(action, (Type) clazz);
	}
	
	public <T extends CallCenterBaseResponse> T commonAction(CallCenterBaseAction action, TypeReference<T> type) throws IOException {
		return commonAction(action, type.getType());
	}
	
	public <T extends CallCenterBaseResponse> T commonAction(CallCenterBaseAction action, Type type) throws IOException {
		String res = URLDecoder.decode(PostRequest.postRequest(url, action.toMap()), "utf-8");
		T response = JSON.parseObject(res, type);
		if (response != null && "0".equals(response.getErrorCode())) {
			logger.info("CTI接口失败,cmd=" + action.getCmd() + ",error_code=" + response.getErrorCode() + ",error_msg=" + response.getErrorMsg());
		}
		return response;
	}
	
	/**
	 * 坐席绑定分机号码
	 * @param action
	 * @return
	 */
	public CallCenterBaseResponse bindExtension(CallCenterBaseAction action) throws IOException {
		action.setCmd(CallCenterBaseAction.AGENT_BIND_EXTENSION);
		return commonAction(action, CallCenterBaseResponse.class);
	}
	
	/**
	 * 坐席取消绑定分机号码
	 * @param action
	 * @return
	 */
	public CallCenterBaseResponse unbindExtension(CallCenterBaseAction action) throws IOException {
		action.setCmd(CallCenterBaseAction.AGENT_UNBIND_EXTENSION);
		return commonAction(action, CallCenterBaseResponse.class);
	}
	
	/**
	 * 坐席状态变更
	 * @param action
	 * @return
	 */
	public CallCenterBaseResponse changeAgentStatus(CallCenterModifyAgent action) throws IOException {
		action.setCmd(CallCenterBaseAction.AGENT_STATUS_CHANGE);
		return commonAction(action, CallCenterBaseResponse.class);
	}
	
	/**
	 * 发起呼叫
	 * @param action
	 * @return
	 */
	public CallCenterBaseResponse originate(CallCenterCalling action) throws IOException {
		action.setCmd(CallCenterBaseAction.ORIGINATE);
		return commonAction(action, CallCenterBaseResponse.class);
	}
	
	/**
	 * 呼叫中断
	 * @param action
	 * @return
	 */
	public CallCenterBaseResponse hangup(CallCenterCalling action) throws IOException {
		action.setCmd(CallCenterBaseAction.HANGUP);
		return commonAction(action, CallCenterBaseResponse.class);
	}
	
	/**
	 * 呼叫保持
	 * @param action
	 * @return
	 */
	public CallCenterBaseResponse hold(CallCenterCalling action) throws IOException {
		action.setCmd(CallCenterBaseAction.HOLD);
		return commonAction(action, CallCenterBaseResponse.class);
	}
	
	/**
	 * 取消呼叫保持
	 * @param action
	 * @return
	 */
	public CallCenterBaseResponse holdOff(CallCenterCalling action) throws IOException {
		action.setCmd(CallCenterBaseAction.HOLD_OFF);
		return commonAction(action, CallCenterBaseResponse.class);
	}
	
	/**
	 * 应答
	 * @param action
	 * @return
	 */
	public CallCenterBaseResponse answer(CallCenterCalling action) throws IOException {
		action.setCmd(CallCenterBaseAction.ANSWER);
		return commonAction(action, CallCenterBaseResponse.class);
	}
	
	/**
	 * 获取坐席
	 * @param action
	 * @return
	 */
	public CallCenterResponseData<CallCenterAgentInfo> agentInfo(CallCenterBaseAction action) throws IOException {
		action.setCmd(CallCenterBaseAction.AGENT_INFO);
		return commonAction(action,  new TypeReference<CallCenterResponseData<CallCenterAgentInfo>>() {});
	}
	
	/**
	 * 分机信息
	 * @param action
	 * @return
	 */
	public CallCenterResponseData<CallCenterAgentInfo> extensionInfo(CallCenterBaseAction action) throws IOException {
		action.setCmd(CallCenterBaseAction.EXTENSION_INFO);
		return commonAction(action,  new TypeReference<CallCenterResponseData<CallCenterAgentInfo>>() {});
	}
	
	/**
	 * 获取呼入信息
	 * @param action
	 * @return
	 */
	public CallCenterPageResponse<CallCenterCallinInfo> callinInfo(CallCenterQueryCallInfo action) throws IOException {
		action.setCmd(CallCenterBaseAction.CALLIN_INFO);
		return commonAction(action, new TypeReference<CallCenterPageResponse<CallCenterCallinInfo>>() {});
	}
	
	/**
	 * 获取呼出信息
	 * @param action
	 * @return
	 */
	public CallCenterPageResponse<CallCenterCalloutInfo> calloutInfo(CallCenterQueryCallInfo action) throws IOException {
		action.setCmd(CallCenterBaseAction.CALLOUT_INFO);
		return commonAction(action, new TypeReference<CallCenterPageResponse<CallCenterCalloutInfo>>() {});
	}

}
