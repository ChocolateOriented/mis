package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * CTI呼入信息
 */
public class CallCenterCallinInfo extends CallCenterCallInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sessionid;	//会话id

	private String caller;	//来电号码

	private String callerRdnis;	//号码来源

	private Long agentOfferTime;	//坐席振铃时间  单位s

	private Long agentStartTime;	//坐席开始接听时间  单位s

	private Long agentEndTime;	//坐席接听结束时间  单位s

	private Long agentFailTime;	//坐席接听失败时间  单位s

	private String queue;	//队列名称

	private Long inQueueTime;	//入队列时间  单位s

	private Long outQueueTime;	//出队列时间  单位s

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getCallerRdnis() {
		return callerRdnis;
	}

	@JSONField(name="caller_rdnis")
	public void setCallerRdnis(String callerRdnis) {
		this.callerRdnis = callerRdnis;
	}

	public Long getAgentOfferTime() {
		return agentOfferTime;
	}

	@JSONField(name="agent_offer_time")
	public void setAgentOfferTime(Long agentOfferTime) {
		this.agentOfferTime = agentOfferTime;
	}

	public Long getAgentStartTime() {
		return agentStartTime;
	}

	@JSONField(name="agent_start_time")
	public void setAgentStartTime(Long agentStartTime) {
		this.agentStartTime = agentStartTime;
	}

	public Long getAgentEndTime() {
		return agentEndTime;
	}

	@JSONField(name="agent_end_time")
	public void setAgentEndTime(Long agentEndTime) {
		this.agentEndTime = agentEndTime;
	}

	public Long getAgentFailTime() {
		return agentFailTime;
	}

	@JSONField(name="agent_fail_time")
	public void setAgentFailTime(Long agentFailTime) {
		this.agentFailTime = agentFailTime;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public Long getInQueueTime() {
		return inQueueTime;
	}

	@JSONField(name="in_queue_time")
	public void setInQueueTime(Long inQueueTime) {
		this.inQueueTime = inQueueTime;
	}

	public Long getOutQueueTime() {
		return outQueueTime;
	}

	@JSONField(name="out_queue_time")
	public void setOutQueueTime(Long outQueueTime) {
		this.outQueueTime = outQueueTime;
	}

	@Override
	public long getCallTimestamp() {
		return inQueueTime == null ? 0 : inQueueTime;
	}

	@Override
	public long getRingTimestamp() {
		return agentOfferTime == null ? 0 : agentOfferTime;
	}

	@Override
	public long getStartTimestamp() {
		return agentStartTime == null ? 0 : agentStartTime;
	}

	@Override
	public long getEndTimestamp() {
		return agentEndTime == null ? 0 : agentEndTime;
	}

	@Override
	public long getFinishTimestamp() {
		return outQueueTime == null ? 0 : outQueueTime;
	}

}
