package com.mo9.risk.modules.dunning.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.mo9.risk.modules.dunning.bean.CallCenterAgentInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterAgentState;
import com.mo9.risk.modules.dunning.bean.CallCenterCallInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterCallinInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterCalling;
import com.mo9.risk.modules.dunning.bean.CallCenterCalloutInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterModifyAgent;
import com.mo9.risk.modules.dunning.bean.CallCenterPageResponse;
import com.mo9.risk.modules.dunning.bean.CallCenterPageResponse.CallCenterPageData;
import com.mo9.risk.modules.dunning.bean.CallCenterQueryCallInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterWebSocketMessage;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.manager.CallCenterManager;
import com.mo9.risk.util.ListFilter;
import com.mo9.risk.util.WebSocketSessionUtil;
import com.thinkgem.jeesite.common.persistence.Page;
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
	 * @param msg
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
			msg.setLocation(tMisCallingRecordService.queryMobileLocation(msg.getTarget()));
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
	 * 获取全部呼叫信息
	 */
	public Page<TMisCallingRecord> callInfoAll(CallCenterQueryCallInfo action) {
		List<CallCenterCallInfo> infos = new ArrayList<CallCenterCallInfo>();
		Page<TMisCallingRecord> page = new Page<TMisCallingRecord>();
		String agent = action.getAgent();

		int pageNo = Integer.parseInt(action.getPage());
		int pageSize = Integer.parseInt(action.getPagesize());
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);

		int totalAll = 0;
		int totalIn = 0;
		int totalOut = 0;
		int totalPageIn = 0;
		int totalPageOut = 0;

		try {
			//呼入信息
			action.setPage("1");
			action.setAgent(null);
			CallCenterPageResponse<CallCenterCallinInfo> callinRes = callCenterManager.callinInfo(action);

			if (callinRes != null && callinRes.hasData()) {
				CallCenterPageData<CallCenterCallinInfo> callinPage = callinRes.getData();
				totalIn = callinPage.getTotal();
				totalPageIn = totalIn / pageSize;
				if (totalPageIn % pageSize > 0) {
					totalPageIn++;
				}

				infos.addAll(callinPage.getResults());
			}

			for (int i = 2; i <= pageNo && i <= totalPageIn; i++) {
				action.setPage(String.valueOf(i));
				CallCenterPageResponse<CallCenterCallinInfo> nextCallinRes = callCenterManager.callinInfo(action);
				if (nextCallinRes != null && nextCallinRes.hasData()) {
					CallCenterPageData<CallCenterCallinInfo> nextCallinPage = nextCallinRes.getData();
					infos.addAll(nextCallinPage.getResults());
				}
			}

			//呼出信息
			action.setPage("1");
			action.setQueue(null);
			action.setAgent(agent);
			CallCenterPageResponse<CallCenterCalloutInfo> calloutRes = callCenterManager.calloutInfo(action);
			if (calloutRes != null && calloutRes.hasData()) {
				CallCenterPageData<CallCenterCalloutInfo> calloutPage = calloutRes.getData();
				totalOut = calloutPage.getTotal();
				totalPageOut = totalOut / pageSize;
				if (totalPageOut % pageSize > 0) {
					totalPageOut++;
				}

				infos.addAll(calloutPage.getResults());
			}

			for (int i = 2; i <= pageNo && i <= totalPageOut; i++) {
				action.setPage(String.valueOf(i));
				CallCenterPageResponse<CallCenterCalloutInfo> nextCalloutRes = callCenterManager.calloutInfo(action);
				if (nextCalloutRes != null && nextCalloutRes.hasData()) {
					CallCenterPageData<CallCenterCalloutInfo> nextCalloutPage = nextCalloutRes.getData();
					infos.addAll(nextCalloutPage.getResults());
				}
			}

			List<TMisCallingRecord> records = callingRecordPageList(infos, pageNo, pageSize);

			totalAll = totalIn + totalOut;
			page.setCount(totalAll);
			page.setList(records);
		} catch (Exception e) {
			logger.info("获取全部呼叫信息失败," + e);
		}
		return page;
	}

	/**
	 * 获取未接呼叫信息
	 */
	public Page<TMisCallingRecord> callInfoBusy(CallCenterQueryCallInfo action) {
		List<CallCenterCallInfo> infos = new ArrayList<CallCenterCallInfo>();
		Page<TMisCallingRecord> page = new Page<TMisCallingRecord>();
		int pageNo = Integer.parseInt(action.getPage());
		int pageSize = Integer.parseInt(action.getPagesize());
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);

		int totalAll = 0;
		int totalOut = 0;
		int totalPageOut = 0;

		try {
			action.setPage("1");
			CallCenterPageResponse<CallCenterCalloutInfo> calloutRes = callCenterManager.calloutInfo(action);

			if (calloutRes != null && calloutRes.hasData()) {
				CallCenterPageData<CallCenterCalloutInfo> calloutPage = calloutRes.getData();
				totalOut = calloutPage.getTotal();
				totalPageOut = totalOut / pageSize;
				if (totalPageOut % pageSize > 0) {
					totalPageOut++;
				}

				infos.addAll(calloutPage.getResults());
			}

			for (int i = 2; i <= totalPageOut; i++) {
				action.setPage(String.valueOf(i));
				CallCenterPageResponse<CallCenterCalloutInfo> nextCalloutRes = callCenterManager.calloutInfo(action);
				if (nextCalloutRes != null && nextCalloutRes.hasData()) {
					CallCenterPageData<CallCenterCalloutInfo> nextCalloutPage = nextCalloutRes.getData();
					infos.addAll(nextCalloutPage.getResults());
				}
			}

			ListFilter<CallCenterCallInfo> filter = new ListFilter<CallCenterCallInfo>() {
				@Override
				public boolean accept(CallCenterCallInfo o) {
					return o.getStartTimestamp() == 0;
				}
			};

			List<CallCenterCallInfo> allList = filterCallInfoList(infos, filter);
			List<TMisCallingRecord> records = callingRecordPageList(allList, pageNo, pageSize);

			totalAll = allList.size();
			page.setCount(totalAll);
			page.setList(records);
		} catch (Exception e) {
			logger.info("获取未接呼叫信息失败," + e);
		}
		return page;
	}

	/**
	 * 获取队列中放弃呼叫信息
	 */
	public Page<TMisCallingRecord> callInfoQueueOff(CallCenterQueryCallInfo action) {
		List<CallCenterCallInfo> infos = new ArrayList<CallCenterCallInfo>();
		Page<TMisCallingRecord> page = new Page<TMisCallingRecord>();
		int pageNo = Integer.parseInt(action.getPage());
		int pageSize = Integer.parseInt(action.getPagesize());
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);

		int totalAll = 0;
		int totalIn = 0;
		int totalPageIn = 0;

		try {
			action.setPage("1");
			CallCenterPageResponse<CallCenterCallinInfo> callinRes = callCenterManager.callinInfo(action);

			if (callinRes != null && callinRes.hasData()) {
				CallCenterPageData<CallCenterCallinInfo> callinPage = callinRes.getData();
				totalIn = callinPage.getTotal();
				totalPageIn = totalIn / pageSize;
				if (totalPageIn % pageSize > 0) {
					totalPageIn++;
				}

				infos.addAll(callinPage.getResults());
			}

			for (int i = 2; i <= pageNo && i <= totalPageIn; i++) {
				action.setPage(String.valueOf(i));
				CallCenterPageResponse<CallCenterCallinInfo> nextCallinRes = callCenterManager.callinInfo(action);
				if (nextCallinRes != null && nextCallinRes.hasData()) {
					CallCenterPageData<CallCenterCallinInfo> nextCallinPage = nextCallinRes.getData();
					infos.addAll(nextCallinPage.getResults());
				}
			}

			ListFilter<CallCenterCallInfo> filter = new ListFilter<CallCenterCallInfo>() {
				@Override
				public boolean accept(CallCenterCallInfo o) {
					return o.getRingTimestamp() == 0;
				}
			};

			List<CallCenterCallInfo> allList = filterCallInfoList(infos, filter);
			List<TMisCallingRecord> records = callingRecordPageList(allList, pageNo, pageSize);

			totalAll = allList.size();
			page.setCount(totalAll);
			page.setList(records);
		} catch (Exception e) {
			logger.info("获取未接呼叫信息失败," + e);
		}
		return page;
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

	/**
	 * 获取呼叫信息的分页列表
	 * @param callInfos
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<CallCenterCallInfo> filterCallInfoList(List<CallCenterCallInfo> callInfos, ListFilter<CallCenterCallInfo> filter) {
		List<CallCenterCallInfo> acceptList = new ArrayList<CallCenterCallInfo>();
		for (CallCenterCallInfo info : callInfos) {
			if (filter.accept(info)) {
				acceptList.add(info);
			}
		}
		return acceptList;
	}

	/**
	 * 获取通话记录的分页列表并补充通话记录信息
	 * <p><pre>
	 * 过滤加拨号码
	 * 号码对应用户姓名
	 * 号码归属地</pre></p>
	 * @param records
	 * @return
	 */
	private List<TMisCallingRecord> callingRecordPageList(List<CallCenterCallInfo> callInfos, int pageNo, int pageSize) {
		Collections.sort(callInfos, new Comparator<CallCenterCallInfo>() {
			@Override
			public int compare(CallCenterCallInfo o1, CallCenterCallInfo o2) {
				return (int) (o2.getCallTimestamp() - o1.getCallTimestamp());
			}
		});

		int from = (pageNo - 1) * pageSize;
		int to = pageNo * pageSize;
		if (to > callInfos.size()) {
			to = callInfos.size();
		}
		List<CallCenterCallInfo> subList =  callInfos.subList(from, to);

		List<TMisCallingRecord> records = new ArrayList<TMisCallingRecord>();

		for (CallCenterCallInfo info : subList) {
			TMisCallingRecord record = null;
			if (info instanceof CallCenterCallinInfo) {
				record = new TMisCallingRecord((CallCenterCallinInfo) info);
			} else if (info instanceof CallCenterCalloutInfo) {
				record = new TMisCallingRecord((CallCenterCalloutInfo) info);
			} else {
				continue;
			}
			String number = record.getTargetNumber();
			if (number == null) {
				continue;
			}
			//过滤加拨号码
			String realNumber = filterCtiCallInfoNumber(number);
			record.setTargetNumber(realNumber);
			//号码对应用户姓名
			TRiskBuyerPersonalInfo personInfo = personalInfoService.getBuyerInfoByMobile(realNumber);
			String targetName = personInfo == null ? "" : personInfo.getRealName();
			record.setTargetName(targetName);
			//号码归属地
			String location = tMisCallingRecordService.queryMobileLocation(realNumber);
			record.setLocation(location);
			records.add(record);
		}
		return records;
	}

	/**
	 * 过滤CTI呼叫信息加拨号码
	 * @param target
	 * @return
	 */
	public static String filterCtiCallInfoNumber(String target) {
		if (target == null) {
			return target;
		}

		if (target.startsWith("179690")) {
			return target.substring(6);
		}

		if (target.startsWith("17969")) {
			return target.substring(5);
		}

		if (target.startsWith("01") && !target.startsWith("010") || target.startsWith("00")) {
			return target.substring(1);
		}
		return target;
	}

	/**
	 * 过滤CTI外呼加拨号码
	 * @param target
	 * @return
	 */
	public static String filterCtiDialNumber(String target) {
		if (target == null) {
			return target;
		}

		if (target.startsWith("90")) {
			return target.substring(2);
		}

		if (target.startsWith("9")) {
			return target.substring(1);
		}

		return target;
	}
}
