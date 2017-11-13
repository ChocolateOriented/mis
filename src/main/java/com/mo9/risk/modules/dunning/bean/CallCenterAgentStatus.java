package com.mo9.risk.modules.dunning.bean;

/**
 * CTI坐席状态
 */
public class CallCenterAgentStatus {

	/**
	 * 坐席登出
	 */
	public static final String LOGGED_OUT = "Logged Out";

	/**
	 * 坐席空闲状态可接听电话
	 */
	public static final String AVAILABLE = "Available";

	/**
	 * 坐席空闲状态可接听电话，一旦接听完电话无等待直接转空闲
	 */
	public static final String AVAILABLE_ON_DEMAND = "Available (On Demand)";

	/**
	 * 休息状态
	 */
	public static final String ON_BREAK = "On Break";

}
