package com.mo9.risk.modules.dunning.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class AppLoginLog extends DataEntity<DunningOrder>{

 
	private static final long serialVersionUID = -593251729808506854L;
	
	private Integer dbid;
	private String mobile;
	private String localMobile;
	private String deviceModel;
	private String mo9ProductName;
	private String marketName;
	private Date createTime;
	
	public AppLoginLog(){
		
	}

 

	public Integer getDbid() {
		return dbid;
	}
	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLocalMobile() {
		return localMobile;
	}

	public void setLocalMobile(String localMobile) {
		this.localMobile = localMobile;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getMo9ProductName() {
		return mo9ProductName;
	}

	public void setMo9ProductName(String mo9ProductName) {
		this.mo9ProductName = mo9ProductName;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	

}
