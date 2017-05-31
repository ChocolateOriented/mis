/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 汇款确认信息日志Entity
 * @author shijlu
 * @version 2017-05-26
 */
public class TMisRemittanceConfirmLog extends DataEntity<TMisRemittanceConfirmLog> { 
	private static final long serialVersionUID = 1L;
	private Integer dbid;		// dbid
	private String remittancename;			// 汇款人
	private Date   remittancetime;			// 汇款时间
	private Double remittanceamount;		// 汇款金额
	private String remittancechannel;		// 汇款渠道
	private String ReceivablesImg1; 		// 催收上传图片
	private String ReceivablesImg2; 		// 催收上传图片
	private Double payamount;				// 还款金额
	private String paytype;					// 还款类型
	private String remark;					// 备注
	private String serialnumber;			// 汇款流水号
	private String financialremittancename; // 汇款人
	private Date accounttime;				// 到账时间
	private Double accountamount;			// 到账金额
	private String financialremittancechannel;		// 汇款渠道
	private String FinancialImg1; 			// 财务上传图片
	private String FinancialImg2; 			// 财务上传图片
	private String financialremark;					// 备注
	private String financialserialnumber;	// 财务汇款流水号
	private String confirmstatus;		// 确认状态
	private String dealcode;		// 订单号
	private String Invalid;			// 删除标志
	private String buyerId;
	private String name;
	private String mobile;
	private Date beginupdatetime;
	private Date endupdatetime;
	private String comfirmid;		//汇款确认信息id
	
	public TMisRemittanceConfirmLog() {
		super();
	}

	public TMisRemittanceConfirmLog(String id){
		super(id);
	}
	
	public TMisRemittanceConfirmLog(TMisRemittanceConfirm tMisRemittanceConfirm){
		this.remittancename = tMisRemittanceConfirm.getRemittancename();
		this.remittancetime = tMisRemittanceConfirm.getRemittancetime();
		this.remittanceamount = tMisRemittanceConfirm.getRemittanceamount();
		this.remittancechannel = tMisRemittanceConfirm.getRemittancechannel();
		this.ReceivablesImg1 = tMisRemittanceConfirm.getReceivablesImg1();
		this.ReceivablesImg2 = tMisRemittanceConfirm.getReceivablesImg2();
		this.payamount = tMisRemittanceConfirm.getPayamount();
		this.paytype = tMisRemittanceConfirm.getPaytype();
		this.remark = tMisRemittanceConfirm.getRemark();
		this.serialnumber = tMisRemittanceConfirm.getSerialnumber();
		this.financialremittancename = tMisRemittanceConfirm.getFinancialremittancename();
		this.accounttime = tMisRemittanceConfirm.getAccounttime();
		this.accountamount = tMisRemittanceConfirm.getAccountamount();
		this.financialremittancechannel = tMisRemittanceConfirm.getFinancialremittancechannel();
		this.FinancialImg1 = tMisRemittanceConfirm.getFinancialImg1();
		this.FinancialImg2 = tMisRemittanceConfirm.getFinancialImg2();
		this.financialremark = tMisRemittanceConfirm.getFinancialremark();
		this.financialserialnumber = tMisRemittanceConfirm.getFinancialserialnumber();
		this.confirmstatus = tMisRemittanceConfirm.getConfirmstatus();
		this.dealcode = tMisRemittanceConfirm.getDealcode();
		this.Invalid = tMisRemittanceConfirm.getInvalid();
		this.buyerId = tMisRemittanceConfirm.getBuyerId();
		this.name = tMisRemittanceConfirm.getName();
		this.mobile = tMisRemittanceConfirm.getMobile();
		this.beginupdatetime = tMisRemittanceConfirm.getBeginupdatetime();
		this.endupdatetime = tMisRemittanceConfirm.getEndupdatetime();
		this.comfirmid = tMisRemittanceConfirm.getId();
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public String getRemittancename() {
		return remittancename;
	}

	public void setRemittancename(String remittancename) {
		this.remittancename = remittancename;
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

	public String getRemittancechannel() {
		return remittancechannel;
	}

	public void setRemittancechannel(String remittancechannel) {
		this.remittancechannel = remittancechannel;
	}

	public String getReceivablesImg1() {
		return ReceivablesImg1;
	}

	public void setReceivablesImg1(String receivablesImg1) {
		ReceivablesImg1 = receivablesImg1;
	}

	public String getReceivablesImg2() {
		return ReceivablesImg2;
	}

	public void setReceivablesImg2(String receivablesImg2) {
		ReceivablesImg2 = receivablesImg2;
	}

	public Double getPayamount() {
		return payamount;
	}

	public void setPayamount(Double payamount) {
		this.payamount = payamount;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFinancialremittancename() {
		return financialremittancename;
	}

	public void setFinancialremittancename(String financialremittancename) {
		this.financialremittancename = financialremittancename;
	}

	public Date getAccounttime() {
		return accounttime;
	}

	public void setAccounttime(Date accounttime) {
		this.accounttime = accounttime;
	}

	public Double getAccountamount() {
		return accountamount;
	}

	public void setAccountamount(Double accountamount) {
		this.accountamount = accountamount;
	}

	public String getFinancialremittancechannel() {
		return financialremittancechannel;
	}

	public void setFinancialremittancechannel(String financialremittancechannel) {
		this.financialremittancechannel = financialremittancechannel;
	}

	public String getFinancialImg1() {
		return FinancialImg1;
	}

	public void setFinancialImg1(String financialImg1) {
		FinancialImg1 = financialImg1;
	}

	public String getFinancialImg2() {
		return FinancialImg2;
	}

	public void setFinancialImg2(String financialImg2) {
		FinancialImg2 = financialImg2;
	}

	public String getFinancialremark() {
		return financialremark;
	}

	public void setFinancialremark(String financialremark) {
		this.financialremark = financialremark;
	}

	public String getConfirmstatus() {
		return confirmstatus;
	}

	public void setConfirmstatus(String confirmstatus) {
		this.confirmstatus = confirmstatus;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getBeginupdatetime() {
		return beginupdatetime;
	}

	public void setBeginupdatetime(Date beginupdatetime) {
		this.beginupdatetime = beginupdatetime;
	}

	public Date getEndupdatetime() {
		return endupdatetime;
	}

	public void setEndupdatetime(Date endupdatetime) {
		this.endupdatetime = endupdatetime;
	}

	public String getInvalid() {
		return Invalid;
	}

	public void setInvalid(String invalid) {
		Invalid = invalid;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getFinancialserialnumber() {
		return financialserialnumber;
	}

	public void setFinancialserialnumber(String financialserialnumber) {
		this.financialserialnumber = financialserialnumber;
	}

	public String getComfirmid() {
		return comfirmid;
	}

	public void setComfirmid(String comfirmid) {
		this.comfirmid = comfirmid;
	}

}