package com.mo9.risk.modules.dunning.bean.dto;

import java.io.Serializable;

/**
 * Created by jxli on 2017/9/2.
 * 贷后接口响应对象, 参考文档 http://192.168.1.53/docs/error_code.html
 */
public class ApiResponse<T> implements Serializable {
	//响应码
	public final static String CODE_SUCCESS = "0";//// 成功
	public final static String CODE_ERROR_SIGN_CHECK = "13";//签名校验错误
	public final static String CODE_ERROR_PARAM_CHECK = "20";//请求参数格式检查错误
	public final static String CODE_ERROR_UNKNOWN = "9999";//未知错误

	public final static String MESSAGE_SUCCESS = "SUCCESS";

	public ApiResponse() {
	}

	public ApiResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ApiResponse(String code, String message, T datas) {
		this.code = code;
		this.message = message;
		this.datas = datas;
	}

	public String code;//响应吗
	public String message;//信息
	public T datas;//数据

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getDatas() {
		return datas;
	}

	public void setDatas(T datas) {
		this.datas = datas;
	}
}
