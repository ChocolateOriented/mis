package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * CTI呼入信息
 */
public class CallCenterCallinInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;	//记录id

	private String sessionid;	//会话id

	private String agent;	//接听坐席

	private String extension;	//接听分机

	private String caller;	//来电号码

	private String callerRdnis;	//号码来源

	private Long agentOfferTime;	//坐席振铃时间  单位s

	private Long agentStartTime;	//坐席开始接听时间  单位s

	private Long agentEndTime;	//坐席接听结束时间  单位s

	private Long agentFailTime;	//坐席接听失败时间  单位s

	private String queue;	//队列名称

	private Long inQueueTime;	//入队列时间  单位s

	private Long outQueueTime;	//出队列时间  单位s

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
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
	//来电时间
	public String getStartTime(){
		String start="";
		if(inQueueTime!=null&&inQueueTime!=0){
			SimpleDateFormat sd=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			start=sd.format(new Date(inQueueTime*1000));
		}
		return start;
	}
	//通话时长
	public String getCallTotalTime(){
		String times="";
		if(agentStartTime!=null&&agentStartTime!=0){
			Long totalTime=agentEndTime-agentStartTime;
			
			Long hour=totalTime/3600;
			Long minutes=totalTime%3600/60;
			Long second=totalTime%3600%60;
			times=String.valueOf(hour)+"时"+String.valueOf(minutes)+"分"+String.valueOf(second)+"秒";
		}
		return times;
	}
}
