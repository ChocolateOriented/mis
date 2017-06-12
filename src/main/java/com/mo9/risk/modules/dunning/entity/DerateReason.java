package com.mo9.risk.modules.dunning.entity;


/**
 * 减免原因类型
 * @author jwchi
 *
 */
public enum DerateReason {
	 RZSC("入账时差"),
	 DC("代偿"),
	 KHZJKN("客户资金困难"),
	 GZ("共债"),
	 TS("投诉"),
	 XTWT("系统问题"),
	 RY("入狱"),
	 SW("死亡"),
	 ZRZH("自然灾害"),
	 OTHER("其他");
 
	//减免原因
	private String derateReasonName;
	
	DerateReason(String derateReasonName){
		
		this.derateReasonName=derateReasonName;
	}

	public String getDerateReasonName() {
		return derateReasonName;
	}
	
}
