/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 通讯录信息Entity
 * @author beargao
 * @version 2016-07-15
 */
public class TBuyerContact extends DataEntity<TBuyerContact> {
	
	private static final long serialVersionUID = 1L;
	private String contactName;		// 联系人姓名
	private String contactMobile;		// 手机号码
	private String buyerId;		// buyer_id
	private Date createTime;		// create_time
	private String familyrelation;
	private Integer smsNum;
	private Integer telNum;
	private String dealcode;
	private String rcname;
	private boolean relativeMatch;		//是否匹配亲戚关键字
	
	public TBuyerContact() {
		super();
	}

	public TBuyerContact(String id){
		super(id);
	}

	@Length(min=0, max=50, message="联系人姓名长度必须介于 0 和 50 之间")
	public String getContactName() {
		return contactName;
	}

	@JSONField(name="contact_name")
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@Length(min=0, max=50, message="手机号码长度必须介于 0 和 50 之间")
	public String getContactMobile() {
		return contactMobile;
	}

	@JSONField(name="contact_mobile")
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	
	@Length(min=0, max=10, message="buyer_id长度必须介于 0 和 10 之间")
	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getFamilyrelation() {
		return familyrelation;
	}

	public void setFamilyrelation(String familyrelation) {
		this.familyrelation = familyrelation;
	}

	public Integer getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(Integer smsNum) {
		this.smsNum = smsNum;
	}

	public Integer getTelNum() {
		return telNum;
	}

	public void setTelNum(Integer telNum) {
		this.telNum = telNum;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getRcname() {
		return rcname;
	}

	public void setRcname(String rcname) {
		this.rcname = rcname;
	}

	public boolean getRelativeMatch() {
		return relativeMatch;
	}

	public void setRelativeMatch(boolean relativeMatch) {
		this.relativeMatch = relativeMatch;
	}

}