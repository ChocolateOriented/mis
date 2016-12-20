/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 财务确认汇款信息Entity
 * @author 徐盛
 * @version 2016-08-11
 */
public class TMisRemittanceMessage extends DataEntity<TMisRemittanceMessage> {
	
	private static final long serialVersionUID = 1L;
	private String dbid;		// dbid
	private String remittancename;		// 汇款人姓名
	private Date remittancetime;		// 汇款时间
	private Double remittanceamount;		// 金额
	private String remittancechannel;		// 汇款渠道
	private String remittanceaccount;		// 汇款帐号
	private String financialuser;		// 财务确认人
	private Date financialtime;		// 财务确认时间
	private String remittanceimg;		// 汇款图片
	private String dealcode;
	private String remark;    // 
	
	public TMisRemittanceMessage() {
		super();
	}

	public TMisRemittanceMessage(String id){
		super(id);
	}

	@Length(min=1, max=11, message="dbid长度必须介于 1 和 11 之间")
	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	
	@Length(min=0, max=128, message="汇款人姓名长度必须介于 0 和 128 之间")
	public String getRemittancename() {
		return remittancename;
	}

	public void setRemittancename(String remittancename) {
		this.remittancename = remittancename;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
	
	@Length(min=0, max=64, message="汇款渠道长度必须介于 0 和 64 之间")
	public String getRemittancechannel() {
		return remittancechannel;
	}

	public void setRemittancechannel(String remittancechannel) {
		this.remittancechannel = remittancechannel;
	}
	
	@Length(min=0, max=64, message="汇款帐号长度必须介于 0 和 64 之间")
	public String getRemittanceaccount() {
		return remittanceaccount;
	}

	public void setRemittanceaccount(String remittanceaccount) {
		this.remittanceaccount = remittanceaccount;
	}
	
	@Length(min=0, max=128, message="财务确认人长度必须介于 0 和 128 之间")
	public String getFinancialuser() {
		return financialuser;
	}

	public void setFinancialuser(String financialuser) {
		this.financialuser = financialuser;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFinancialtime() {
		return financialtime;
	}

	public void setFinancialtime(Date financialtime) {
		this.financialtime = financialtime;
	}
	
	public String getRemittanceimg() {
		return remittanceimg;
	}

	public void setRemittanceimg(String remittanceimg) {
		this.remittanceimg = remittanceimg;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	
	
}