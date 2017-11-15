package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * CTI响应基类
 */
public class CallCenterBaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String errorCode;	//错误码

	private String errorMsg;	//错误信息

	public String getErrorCode() {
		return errorCode;
	}

	@JSONField(name="error_code")
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	@JSONField(name="error_msg")
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
