package com.mo9.risk.modules.dunning.entity;

import java.util.Date;

public class DunningOuterFileLog {

	
	private String dealcode; // # 订单编号
	private String realname; // # 姓名
	private String mobile;// # 手机号码
	private String dunningpeoplename;// # 催收人
	private Date createdate;
	
	public String getDealcode() {
		return dealcode;
	}
	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDunningpeoplename() {
		return dunningpeoplename;
	}
	public void setDunningpeoplename(String dunningpeoplename) {
		this.dunningpeoplename = dunningpeoplename;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	
}
