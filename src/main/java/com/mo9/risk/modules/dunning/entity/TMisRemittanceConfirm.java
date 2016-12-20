/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 汇款确认信息Entity
 * @author 徐盛
 * @version 2016-09-12
 */
public class TMisRemittanceConfirm extends DataEntity<TMisRemittanceConfirm> {
	/**
	 * 代表催收已提交财务待确认
	 */
	public static final String CONFIRMSTATUS_CH_SUBMIT = "ch_submit"; 
	/**
	 *  代表财务已提交催收待确认
	 */
	public static final String CONFIRMSTATUS_CW_SUBMIT = "cw_submit";  
	/**
	 *  代表催收最终已确认
	 */
	public static final String CONFIRMSTATUS_CH_CONFIRM = "ch_confirm";    
	
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
	/**
	 * 用户id
	 */
	public String buyerId;
	public String name;
	public String mobile;
	
	private Date beginupdatetime;
	private Date endupdatetime;
	
	
	public TMisRemittanceConfirm() {
		super();
	}

	public TMisRemittanceConfirm(String id){
		super(id);
	}
	
	public TMisRemittanceConfirm(String id,String paytype,Double payamount,String confirmstatus){
		this.id = id ;
		this.paytype = paytype ;
		this.payamount = payamount ;
		this.confirmstatus = confirmstatus ;
	}

	@NotNull(message="dbid不能为空")
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
	public String getPaytypeText() {
		return "loan".equals(this.paytype) ?  "还款" : 
				"delay".equals(this.paytype) ?  "续期" :
				"partial".equals(this.paytype) ?  "部分还款" :"";
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
	public String getConfirmstatusText() {
		return CONFIRMSTATUS_CH_SUBMIT.equals(this.confirmstatus) ?  "催收已提交" : 
				  CONFIRMSTATUS_CW_SUBMIT.equals(this.confirmstatus) ?  "财务已确认" :
					 CONFIRMSTATUS_CH_CONFIRM.equals(this.confirmstatus) ?  "已完成" :"";
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

	
	
}