package com.mo9.risk.modules.dunning.bean;

/**
 * CTI请求修改坐席
 */
public class CallCenterModifyAgent extends CallCenterBaseAction {

	private static final long serialVersionUID = 1L;

	private String status;	//状态

	public String getStatus() {
		return status;
	}

	/**
	 * @see CallCenterAgentStatus
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
