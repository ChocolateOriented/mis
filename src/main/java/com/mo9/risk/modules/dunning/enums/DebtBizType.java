package com.mo9.risk.modules.dunning.enums;

/**
 * Created by jxli on 2017/12/20.
 */
public enum DebtBizType {
	JHJJ("mo9"),
	JHJJ_JRZX("weixin36");//mo9金融中心

	DebtBizType(String desc) {
		this.desc = desc;
	}

	private final String desc;

	public String getDesc() {
		return desc;
	}
}
