package com.mo9.risk.modules.dunning.entity;


/**
 * 入账状态
 * @author jwchi
 *
 */
public enum AccountStatus {
	WCZ("未查账"),
	YCZ("已查账"),
	YWC("已完成");
	//入账状态
	private String accountStatus;
	
	AccountStatus(String accountStatus){
		
		this.accountStatus=accountStatus;
	}

	public String getAccountStatus() {
		return accountStatus;
	}
	
}
