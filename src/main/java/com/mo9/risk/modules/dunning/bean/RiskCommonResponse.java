package com.mo9.risk.modules.dunning.bean;


/**
 * Created by jxli on 2017/9/2.
 * 江湖救急响应结果
 */
public class RiskCommonResponse extends RiskBaseResponse{

	public String datas;

	public String getDatas() {
		return datas;
	}

	public void setDatas(String datas) {
		this.datas = datas;
	}

	@Override
	public String toString() {
		return "RiskContactRecordResponse{" +
				"datas=" + datas +
				", resultCode='" + resultCode + '\'' +
				", result='" + result + '\'' +
				", resultMsg='" + resultMsg + '\'' +
				'}';
	}
}
