/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class BankCardInfo extends DataEntity<BankCardInfo> {

	private static final long serialVersionUID = 1L;
	
	private String bin;		//银行识别码
	
	private Integer cardlen;		//银行卡号长度
	
	private String orgcode;		//机构代码
	
	private String bankname;		//银行名称
	
	private String cardname;		//卡名
	
	private String cardtype;		//银行卡类型

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public Integer getCardlen() {
		return cardlen;
	}

	public void setCardlen(Integer cardlen) {
		this.cardlen = cardlen;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

}
