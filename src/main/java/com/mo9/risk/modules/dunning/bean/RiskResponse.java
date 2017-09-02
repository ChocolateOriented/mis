package com.mo9.risk.modules.dunning.bean;

/**
 * Created by jxli on 2017/9/2.
 * 江湖救急响应结果
 */
public class RiskResponse {
	public final static String RESULT_CODE_SUCCESS = "200";//响应码_成功
	public final static String RESULT_CODE_ERROR = "500";//响应码_失败

	public String resultCode;//响应吗
	public String result ;//响应结果
	public String resultMsg;//信息

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	@Override
	public String toString() {
		return "RiskResponse{" +
				"resultCode='" + resultCode + '\'' +
				", result='" + result + '\'' +
				", resultMsg='" + resultMsg + '\'' +
				'}';
	}
}
