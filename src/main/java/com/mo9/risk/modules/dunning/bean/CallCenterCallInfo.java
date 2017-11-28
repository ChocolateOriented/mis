package com.mo9.risk.modules.dunning.bean;

/**
 * CTI呼叫信息抽象类
 */
public abstract class CallCenterCallInfo {

	private String id;	//记录id

	private String agent;	//接听坐席

	private String extension;	//分机号码

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

	/**
	 * 获取呼叫发起时间
	 */
	public abstract long getCallTimestamp();

	/**
	 * 获取响铃时间
	 */
	public abstract long getRingTimestamp();

	/**
	 * 获取通话开始时间
	 */
	public abstract long getStartTimestamp();

	/**
	 * 获取通话结束时间
	 */
	public abstract long getEndTimestamp();

	/**
	 * 获取呼叫结束时间
	 */
	public abstract long getFinishTimestamp();
}
