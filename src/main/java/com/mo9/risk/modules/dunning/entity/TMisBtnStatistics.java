/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class TMisBtnStatistics extends DataEntity<TMisBtnStatistics> {

	private static final long serialVersionUID = 1L;
	
	private String buyerid;
	
	private String buyername;
	
	private String dealcode;	//订单号
	
	private String pagename;		//页面名称
	
	private String btnid;		//按钮id属性
	
	private String btnname;		//按钮名称

	public String getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(String buyerid) {
		this.buyerid = buyerid;
	}

	public String getBuyername() {
		return buyername;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getBtnid() {
		return btnid;
	}

	public void setBtnid(String btnid) {
		this.btnid = btnid;
	}

	public String getBtnname() {
		return btnname;
	}

	public void setBtnname(String btnname) {
		this.btnname = btnname;
	}
	
}
