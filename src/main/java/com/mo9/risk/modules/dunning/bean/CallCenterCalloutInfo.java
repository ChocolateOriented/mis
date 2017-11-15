package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * CTI呼出信息
 */
public class CallCenterCalloutInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;	//记录id

	private String agent;	//坐席号

	private String extension;	//分机号码

	private String target;	//目标号码

	private Long channelCreateTime;	//创建时间  单位s

	private Long channelAnswerTime;	//应答时间  单位s

	private Long channelHangupTime;	//关断时间  单位s

	private String euuid;	//分机uuid

	private String tuuid;	//目标uuid

	private String customerno;	//自定义编号

	private String agentState;

	public String getAgentState() {
		return agentState;
	}

	public void setAgentState(String agentState) {
		this.agentState = agentState;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Long getChannelCreateTime() {
		return channelCreateTime;
	}

	@JSONField(name="channelcreatetime")
	public void setChannelCreateTime(Long channelCreateTime) {
		this.channelCreateTime = channelCreateTime;
	}

	public Long getChannelAnswerTime() {
		return channelAnswerTime;
	}

	@JSONField(name="channelanswertime")
	public void setChannelAnswerTime(Long channelAnswerTime) {
		this.channelAnswerTime = channelAnswerTime;
	}

	public Long getChannelHangupTime() {
		return channelHangupTime;
	}

	@JSONField(name="channelhanguptime")
	public void setChannelHangupTime(Long channelHangupTime) {
		this.channelHangupTime = channelHangupTime;
	}

	public String getEuuid() {
		return euuid;
	}

	public void setEuuid(String euuid) {
		this.euuid = euuid;
	}

	public String getTuuid() {
		return tuuid;
	}

	public void setTuuid(String tuuid) {
		this.tuuid = tuuid;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}
	//呼叫时间
	public String getCallOutTime(){
		String start="";
		if(channelCreateTime!=null&&channelCreateTime!=0){
			SimpleDateFormat sd=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			start=sd.format(new Date(channelCreateTime*1000));
		}
		return start;
	}
	//通话时长
	public String getCallTotalTime(){
		String times="";
		if(channelAnswerTime!=null&&channelAnswerTime!=0){
			Long totalTime=channelHangupTime-channelAnswerTime;
			
			Long hour=totalTime/3600;
			Long minutes=totalTime%3600/60;
			Long second=totalTime%3600%60;
			times=String.valueOf(hour)+"时"+String.valueOf(minutes)+"分"+String.valueOf(second)+"秒";
		}
		return times;
	}
}
