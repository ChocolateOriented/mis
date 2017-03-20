package com.mo9.risk.modules.dunning.entity;

/**
 * 逾期原因
 * @author shijlu
 * @version 2017-03-06
 *
 */
public enum DunningOverdueReason {

	FTP("忘记还款"),
	ECOH("经济困难"),
	MULO("多头共债"),
	RODM("扣款原因"),
	NAL("不在本地"),
	ARGU("争议"),
	BKR("破产"),
	UNEM("失业"),
	DIS("疾病"),
	ARTD("被公安逮捕或入狱"),
	DEAD("死亡"),
	OTHER("其他原因");
	
	private String desc;

	private DunningOverdueReason(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
