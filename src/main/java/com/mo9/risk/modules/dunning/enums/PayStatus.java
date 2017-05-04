package com.mo9.risk.modules.dunning.enums;

public enum PayStatus {
	submitted("提交中"),
	succeeded("成功"),
	failed("失败");
	
	private String desc;

	private PayStatus(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
