/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 联系人信息Entity
 * @author beargao
 * @version 2016-07-15
 */
public class TRiskBuyer2contacts extends DataEntity<TRiskBuyer2contacts> {
	
	private static final long serialVersionUID = 1L;
	private String buyerId;		// 用户id
	private String buyerContactId;		// buyer_contact_id
	private Date createTime;		// create_time
	private String familyRelation;		// family_relation
	private String type;		// type
	private String name;
	private String tel;
	private String addres;
	private String contactType;
	private Integer smsNum;
	private Integer telNum;
	private String dealcode;
	
	
	public TRiskBuyer2contacts() {
		super();
	}

	public TRiskBuyer2contacts(String id){
		super(id);
	}

	@Length(min=0, max=10, message="用户id长度必须介于 0 和 10 之间")
	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	
	@Length(min=0, max=10, message="buyer_contact_id长度必须介于 0 和 10 之间")
	public String getBuyerContactId() {
		return buyerContactId;
	}

	public void setBuyerContactId(String buyerContactId) {
		this.buyerContactId = buyerContactId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=50, message="family_relation长度必须介于 0 和 50 之间")
	public String getFamilyRelation() {
		return familyRelation;
	}

	public void setFamilyRelation(String familyRelation) {
		this.familyRelation = familyRelation;
	}
	
	@Length(min=0, max=50, message="type长度必须介于 0 和 50 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=50, message="name长度必须介于 0 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=50, message="tel长度必须介于 0 和 50 之间")
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Length(min=0, max=300, message="addres长度必须介于 0 和 300 之间")
	public String getAddres() {
		return addres;
	}

	public void setAddres(String addres) {
		this.addres = addres;
	}

	@Length(min=0, max=50, message="contact_type长度必须介于 0 和 50 之间")
	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
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
	
	
}