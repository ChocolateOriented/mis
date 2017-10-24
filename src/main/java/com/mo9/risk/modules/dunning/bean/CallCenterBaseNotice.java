package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;

/**
 * CTI回调通知基类
 */
public class CallCenterBaseNotice implements Serializable {

	/**
	 * 坐席普通状态通知
	 */
	public static final String STATUS_NOTICE = "status_notice";

	/**
	 * 坐席接听状态通知
	 */
	public static final String STATE_NOTICE = "state_notice";

	/**
	 * 坐席绑定号码成功通知
	 */
	public static final String BIND_NOTICE = "bind_notice";

	/**
	 * 坐席解绑绑定状态通知
	 */
	public static final String UNBIND_NOTICE = "unbind_notice";

	/**
	 * 队列成员数变更通知
	 */
	public static final String QUEUECOUNT_NOTICE = "queuecount_notice";

	/**
	 * 话机在线状态变更
	 */
	public static final String EXTENSION_STATUS_NOTICE = "extension_status_notice";

	/**
	 * 话机通话状态变更
	 */
	public static final String EXTENSION_STATE_NOTICE = "extension_state_notice";

	private static final long serialVersionUID = 1L;

	private String type;

	private String data;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
