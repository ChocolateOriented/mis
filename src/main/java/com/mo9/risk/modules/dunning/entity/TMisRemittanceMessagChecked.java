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
	private static final long serialVersionUID = 1L;
	private String remittanceConfirmId;
	// # 借款人姓名
	private String realName;
	// # 汇款人姓名
	private String remittanceName;
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
	//入账类型
	private  String payType;
	// 入账时间
	private Date remittancetime;
	//收入(+元)
	private Double remittanceamount;
	//对方账户
	private String remittanceaccount;
	// 备注
	private String remark;
	// 上传人
	private String financialUser;	
	
	// 入账人
	private String completePeople;	
	// buyer_id
	private String buyerId;
	// dunningtaskdbid
	private String dunningtaskdbid;

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
	public Date getRemittancetime() {
		return remittancetime;
	}
	public void setRemittancetime(Date remittancetime) {
		this.remittancetime = remittancetime;
	}
	public Double getRemittanceamount() {
		return remittanceamount;
	}
	public void setRemittanceamount(Double remittanceamount) {
		this.remittanceamount = remittanceamount;
	}
	public String getRemittanceaccount() {
		return remittanceaccount;
	}
	public void setRemittanceaccount(String remittanceaccount) {
		this.remittanceaccount = remittanceaccount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFinancialUser() {
		return financialUser;
	}
	public void setFinancialUser(String financialUser) {
		this.financialUser = financialUser;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getCompletePeople() {
		return completePeople;
	}
	public void setCompletePeople(String completePeople) {
		this.completePeople = completePeople;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getDunningtaskdbid() {
		return dunningtaskdbid;
	}
	public void setDunningtaskdbid(String dunningtaskdbid) {
		this.dunningtaskdbid = dunningtaskdbid;
	}

	public String getRemittanceName() {
		return remittanceName;
	}
	public void setRemittanceName(String remittanceName) {
		this.remittanceName = remittanceName;
	}
	public String getRemittanceConfirmId() {
		return remittanceConfirmId;
	}

	public void setRemittanceConfirmId(String remittanceConfirmId) {
		this.remittanceConfirmId = remittanceConfirmId;
	}
}