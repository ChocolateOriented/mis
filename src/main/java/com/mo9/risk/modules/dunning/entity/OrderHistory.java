package com.mo9.risk.modules.dunning.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.util.NumberUtil;

public class OrderHistory {

	private Integer orderid; // 订单id
	private String dealcode; // 订单编号
	private String ordertype; // 订单类型
	private Double creditamount; // 订单金额
	private Integer days; // 借款期限
	private String createtime; // 订单提交时间
	private String remittime; // 订单放款时间
	private Date repaymenttime; // 到期还款日期
	private Integer delaydays; // 逾期天数
	private Double overdueamount; // 逾期费
	private Double defaultinterestamount; // 续期费
	private String status; // 订单状态
	private Date payofftime; // 还清日期
	private Double amount; // 还款金额
	private Integer rootorderid; // 父订单编号
	private String roodealcode;

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public Double getCreditamount() {
		return creditamount;
	}

	public String getCreditamountText() {
		return null != this.creditamount ? NumberUtil
				.formatTosepara(this.creditamount) : "";
	}

	public void setCreditamount(Double creditamount) {
		this.creditamount = creditamount;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	public String getRemittime() {
		return remittime;
	}

	public void setRemittime(String remittime) {
		this.remittime = remittime;
	}

	public Date getRepaymenttime() {
		return repaymenttime;
	}

	public void setRepaymenttime(Date repaymenttime) {
		this.repaymenttime = repaymenttime;
	}

	public Integer getDelaydays() {
		return delaydays;
	}

	public void setDelaydays(Integer delaydays) {
		this.delaydays = delaydays;
	}

	public Double getOverdueamount() {
		return overdueamount;
	}
	public String getOverdueamountText() {
		return null != this.overdueamount ? NumberUtil
				.formatTosepara(this.overdueamount) : "";
	}
	public void setOverdueamount(Double overdueamount) {
		this.overdueamount = overdueamount;
	}

	public Double getDefaultinterestamount() {
		return defaultinterestamount;
	}
	public String getDefaultinterestamountText() {
		return null != this.defaultinterestamount ? NumberUtil
				.formatTosepara(this.defaultinterestamount) : "";
	}
	public void setDefaultinterestamount(Double defaultinterestamount) {
		this.defaultinterestamount = defaultinterestamount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPayofftime() {
		return payofftime;
	}

	public void setPayofftime(Date payofftime) {
		this.payofftime = payofftime;
	}

	public Double getAmount() {
		return amount;
	}
	public String getAmountText() {
		return null != this.amount ? NumberUtil
				.formatTosepara(this.amount) : "";
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getRootorderid() {
		return rootorderid;
	}

	public void setRootorderid(Integer rootorderid) {
		this.rootorderid = rootorderid;
	}

	public String getRoodealcode() {
		return roodealcode;
	}

	public void setRoodealcode(String roodealcode) {
		this.roodealcode = roodealcode;
	}

}
