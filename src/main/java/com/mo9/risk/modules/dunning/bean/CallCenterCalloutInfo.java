package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * CTI呼出信息
 */
public class CallCenterCalloutInfo extends CallCenterCallInfo implements Serializable {

	private static final long serialVersionUID = 1L;

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

	@Override
	public long getCallTimestamp() {
		return channelCreateTime == null ? 0 : channelCreateTime;
	}

	@Override
	public long getRingTimestamp() {
		return channelCreateTime == null ? 0 : channelCreateTime;
	}

	@Override
	public long getStartTimestamp() {
		return channelAnswerTime == null ? 0 : channelAnswerTime;
	}

	@Override
	public long getEndTimestamp() {
		return channelAnswerTime == null ? 0 : channelAnswerTime;
	}

	@Override
	public long getFinishTimestamp() {
		return channelHangupTime == null ? 0 : channelHangupTime;
	}

}
