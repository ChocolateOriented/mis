package com.mo9.risk.modules.dunning.bean;

/**
 * CTI数据响应
 */
public class CallCenterResponseData<T> extends CallCenterBaseResponse {

	private static final long serialVersionUID = 1L;

	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
