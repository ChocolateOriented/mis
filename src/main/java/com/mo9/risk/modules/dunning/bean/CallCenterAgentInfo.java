package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 坐席信息
 */
public class CallCenterAgentInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String name;	//坐席名称

	private String status;	//坐席普通状态

	private String state;	//坐席接听状态

	private String extension;	//分机号

	private String target;	//对方号码

	private String callInNum;	//来电号码

	private String callInSessionid;	//来电会话唯一id

	private String callOutNum;	//呼出号码

	private String callOutSessionid;	//呼出号码唯一id

	private String time;	//时间戳

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getCallInNum() {
		return callInNum;
	}

	@JSONField(name="call_in_num")
	public void setCallInNum(String callInNum) {
		this.callInNum = callInNum;
	}

	public String getCallInSessionid() {
		return callInSessionid;
	}

	@JSONField(name="call_in_sessionid")
	public void setCallInSessionid(String callInSessionid) {
		this.callInSessionid = callInSessionid;
	}

	public String getCallOutNum() {
		return callOutNum;
	}

	@JSONField(name="call_out_num")
	public void setCallOutNum(String callOutNum) {
		this.callOutNum = callOutNum;
	}

	public String getCallOutSessionid() {
		return callOutSessionid;
	}

	@JSONField(name="call_out_sessionid")
	public void setCallOutSessionid(String callOutSessionid) {
		this.callOutSessionid = callOutSessionid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
