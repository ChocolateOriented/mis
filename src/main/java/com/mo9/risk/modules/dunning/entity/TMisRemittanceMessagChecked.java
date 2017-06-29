/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;

import java.util.Date;

/**
 * 财务确认汇款信息Entity
 * @author 徐盛
 * @version 2016-08-11
 */
public class TMisRemittanceMessagChecked extends DataEntity<TMisRemittanceMessagChecked> {
	
	// # 姓名
	private String realName;
	// # 手机号码
	private String mobile;
	// # 订单编号
	private String dealcode;
	//崔收入
	private String nickName;
	// # 欠款金额
	private Double amount;
	// #减免金额
	private Double modifyamount;
	// #应催金额
	private Double creditamount;
	//交易流水号
	private  String remittanceSerialNumber;
	//查账人
	private  String checkedPeople;
	//订单状态
	private  String orderStatus;
	//入账标签
	private  String remittanceTag;
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDealcode() {
		return dealcode;
	}
	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getModifyamount() {
		return modifyamount;
	}
	public void setModifyamount(Double modifyamount) {
		this.modifyamount = modifyamount;
	}
	public Double getCreditamount() {
		return creditamount;
	}
	public void setCreditamount(Double creditamount) {
		this.creditamount = creditamount;
	}
	public String getRemittanceSerialNumber() {
		return remittanceSerialNumber;
	}
	public void setRemittanceSerialNumber(String remittanceSerialNumber) {
		this.remittanceSerialNumber = remittanceSerialNumber;
	}
	public String getCheckedPeople() {
		return checkedPeople;
	}
	public void setCheckedPeople(String checkedPeople) {
		this.checkedPeople = checkedPeople;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getRemittanceTag() {
		return remittanceTag;
	}
	public void setRemittanceTag(String remittanceTag) {
		this.remittanceTag = remittanceTag;
	}
	
	
}