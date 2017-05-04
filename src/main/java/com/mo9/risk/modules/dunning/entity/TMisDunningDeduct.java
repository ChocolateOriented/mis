/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.mo9.risk.modules.dunning.enums.PayStatus;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 代扣记录Entity
 * @author shijlu
 * @version 2017-04-11
 */
public class TMisDunningDeduct extends DataEntity<TMisDunningDeduct> {

	private static final long serialVersionUID = 1L;

	private String dbid;		//dbid

	private String dealcode;		//催收订单号

	private String deductcode;		//扣款流水号

	private Date starttime;		//扣款发起时间

	private String buyername;		//姓名

	private String idcard;		//身份证号

	private String bankname;		//扣款银行

	private String bankcard;		//扣款卡号

	private String mobile;		//预留手机号

	private String paytype;		//扣款类型

	private String delaydays;		//续期天数

	private Double payamount;		//扣款金额

	private String paychannel;		//扣款渠道

	private Date finishtime;		//扣款成功时间

	private PayStatus status;		//扣款状态

	private String statusdetail;		//状态详细

	private String reason;		//状态原因

	private PayStatus repaymentstatus;		//还款状态

	private String repaymentdetail;		//还款状态明细
    
	public TMisDunningDeduct() {
		super();
	}

	public TMisDunningDeduct(String id){
		super(id);
	}

	@Length(min=1, max=11, message="dbid长度必须介于 1 和 11 之间")
	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	
	@Length(min=1, max=64, message="催收订单号长度必须介于 1 和 64 之间")
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getDeductcode() {
		return deductcode;
	}

	public void setDeductcode(String deductcode) {
		this.deductcode = deductcode;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public String getBuyername() {
		return buyername;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getDelaydays() {
		return delaydays;
	}

	public void setDelaydays(String delaydays) {
		this.delaydays = delaydays;
	}

	public String getPaytypeText() {
		return "loan".equals(this.paytype) ?  "全款" :
			"delay".equals(this.paytype) ?  "续期" + this.delaydays :
			"partial".equals(this.paytype) ?  "部分" : "";
	}

	public Double getPayamount() {
		return payamount;
	}

	public void setPayamount(Double payamount) {
		this.payamount = payamount;
	}

	public String getPayamountText() {
		return payamount == null ? "" : NumberUtil.formatTosepara(payamount);
	}

	public String getPaychannel() {
		return paychannel;
	}

	public void setPaychannel(String paychannel) {
		this.paychannel = paychannel;
	}

	public Date getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}
	
	public PayStatus getStatus() {
		return status;
	}
	
	public void setStatus(PayStatus status) {
		this.status = status;
	}

	public String getStatusdetail() {
		return statusdetail;
	}

	public void setStatusdetail(String statusdetail) {
		this.statusdetail = statusdetail;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public PayStatus getRepaymentstatus() {
		return repaymentstatus;
	}

	public void setRepaymentstatus(PayStatus repaymentstatus) {
		this.repaymentstatus = repaymentstatus;
	}

	public String getRepaymentdetail() {
		return repaymentdetail;
	}

	public void setRepaymentdetail(String repaymentdetail) {
		this.repaymentdetail = repaymentdetail;
	}

}