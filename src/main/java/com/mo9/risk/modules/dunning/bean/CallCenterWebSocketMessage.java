package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;

public class CallCenterWebSocketMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String operation;	//操作类型

	private String agent;	//坐席

	private String peopleId;	//催收员id

	private String target;	//通话号码

	private String name;	//通话对象姓名

	private String result;	//结果

	private String msg;	//结果信息

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
