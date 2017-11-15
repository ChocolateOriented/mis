package com.mo9.risk.modules.dunning.manager;

/**
 * 接口调用失败异常
 */
public class ApiFailException extends Exception {

	private static final long serialVersionUID = 1L;

	public ApiFailException() {
		super();
	}

	public ApiFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiFailException(String message) {
		super(message);
	}

	public ApiFailException(Throwable cause) {
		super(cause);
	}

}
