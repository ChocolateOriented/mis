/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 公司信息Entity
 * @author beargao
 * @version 2016-07-15
 */
public class TRiskBuyerWorkinfo extends DataEntity<TRiskBuyerWorkinfo> {
	
	private static final long serialVersionUID = 1L;
	private String companyName;		// 公司名称
	private String companyAddress;		// 公司地址
	private String companyTel;		// 公司电话
	private String companyPosition;		// 职务
	private String buyerId;		// 用户id
	private String ownerName;		// owner_name
	private String areaCode;		// area_code
	private String areaDesc;		// area_desc
	private String address;		// address
	private String remark;		// remark
	private String imgFiled;		// img_filed
	private Date createTime;		// create_time
	private String companyTel2;		// company_tel2
	private String workLife;		// work_life
	
//	private Integer smsNum1;
	private Integer telNum1;
	private String dealcode;
//	private Integer smsNum2;
	private Integer telNum2;
	
	public TRiskBuyerWorkinfo() {
		super();
	}

	public TRiskBuyerWorkinfo(String id){
		super(id);
	}

	@Length(min=1, max=50, message="公司名称长度必须介于 1 和 50 之间")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Length(min=1, max=50, message="公司地址长度必须介于 1 和 50 之间")
	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	
	@Length(min=1, max=50, message="公司电话长度必须介于 1 和 50 之间")
	public String getCompanyTel() {
		return companyTel;
	}

	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}
	
	@Length(min=1, max=50, message="职务长度必须介于 1 和 50 之间")
	public String getCompanyPosition() {
		return companyPosition;
	}

	public void setCompanyPosition(String companyPosition) {
		this.companyPosition = companyPosition;
	}
	
	@Length(min=0, max=10, message="用户id长度必须介于 0 和 10 之间")
	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	
	@Length(min=0, max=200, message="owner_name长度必须介于 0 和 200 之间")
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	@Length(min=0, max=200, message="area_code长度必须介于 0 和 200 之间")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	@Length(min=0, max=200, message="area_desc长度必须介于 0 和 200 之间")
	public String getAreaDesc() {
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}
	
	@Length(min=0, max=200, message="address长度必须介于 0 和 200 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=5000, message="remark长度必须介于 0 和 5000 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Length(min=0, max=10, message="img_filed长度必须介于 0 和 10 之间")
	public String getImgFiled() {
		return imgFiled;
	}

	public void setImgFiled(String imgFiled) {
		this.imgFiled = imgFiled;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=50, message="company_tel2长度必须介于 0 和 50 之间")
	public String getCompanyTel2() {
		return companyTel2;
	}

	public void setCompanyTel2(String companyTel2) {
		this.companyTel2 = companyTel2;
	}
	
	@Length(min=0, max=2, message="work_life长度必须介于 0 和 2 之间")
	public String getWorkLife() {
		return workLife;
	}

	public void setWorkLife(String workLife) {
		this.workLife = workLife;
	}

	public Integer getTelNum1() {
		return telNum1;
	}

	public void setTelNum1(Integer telNum1) {
		this.telNum1 = telNum1;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public Integer getTelNum2() {
		return telNum2;
	}

	public void setTelNum2(Integer telNum2) {
		this.telNum2 = telNum2;
	}
	
	
	
}