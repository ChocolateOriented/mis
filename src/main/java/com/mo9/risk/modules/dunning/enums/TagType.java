package com.mo9.risk.modules.dunning.enums;

public enum TagType {

	SensitiveOcp("敏感职业"),
	Complaint("投诉或投诉倾向");

	private String desc;

	private TagType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
}
