package com.mo9.risk.modules.dunning.bean;

/**
 * CTI坐席接听状态
 */
public class CallCenterAgentState {

	/**
	 * 等待接听(仅来电挂断后通知)
	 * @see CallCenterAgentState#AVAILABLE
	 */
	public static final String WAITING = "Waiting";

	/**
	 * 响铃中(仅来电响铃通知)
	 */
	public static final String RECEIVING = "Receiving";

	/**
	 * 接听状态(仅来电接听时通知)
	 * @see CallCenterAgentState#ONCALL
	 */
	public static final String IN_A_QUEUE_CALL = "In a queue call";

	/**
	 * 响铃中(主叫、来电响铃通知)
	 */
	public static final String RINGING = "ringing";

	/**
	 * 通话
	 */
	public static final String ONCALL = "oncall";

	/**
	 * 空闲状态
	 */
	public static final String AVAILABLE = "available";

}
