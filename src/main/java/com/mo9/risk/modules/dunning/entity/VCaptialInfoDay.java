/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 资金成本日报Entity
 * @author 徐盛
 * @version 2016-09-25
 */
public class VCaptialInfoDay extends DataEntity<VCaptialInfoDay> {
	
	private static final long serialVersionUID = 1L;
	private Date createtime;		// createtime
	private BigDecimal amount;		// 交易金额
	private BigDecimal txnAmount;		// 收入
	private BigDecimal feePercent;		// 收入比
	private BigDecimal distributorAmount;		// 渠道商消费金额
	private Double totalAmount;		// 总成本
	private Double channelfeeAmount;		// 渠道费
	private Double channelfeePercent;		// 渠道费占比
	private BigDecimal mobiAmount;		// 摩币返利
	private Double mobiPercent;		// 摩币返利占比
	private BigDecimal smsAmount;		// 短信费
	private Double smsPercent;		// 短信费占比
	
	private Date beginCreatetime;
	private Date endCreatetime;
	
	
	
	public Date getBeginCreatetime() {
		return beginCreatetime;
	}

	public void setBeginCreatetime(Date beginCreatetime) {
		this.beginCreatetime = beginCreatetime;
	}

	public Date getEndCreatetime() {
		return endCreatetime;
	}

	public void setEndCreatetime(Date endCreatetime) {
		this.endCreatetime = endCreatetime;
	}

	public VCaptialInfoDay() {
		super();
	}

	public VCaptialInfoDay(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public String getAmountText() {
		return null != this.amount ? NumberUtil.formatTosepara(this.amount) : "";
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getTxnAmount() {
		return txnAmount;
	}
	public String getTxnAmountText() {
		return null != this.txnAmount ? NumberUtil.formatTosepara(this.txnAmount) : "";
	}
	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}
	
	public BigDecimal getFeePercent() {
		return feePercent;
	}
	public String getFeePercentText() {
		return null != this.feePercent ? NumberUtil.formatTosepara(this.feePercent) + "%" : "";
	}
	public void setFeePercent(BigDecimal feePercent) {
		this.feePercent = feePercent;
	}
	
	public BigDecimal getDistributorAmount() {
		return distributorAmount;
	}
	public String getDistributorAmountText() {
		return null != this.distributorAmount ? NumberUtil.formatTosepara(this.distributorAmount) : "";
	}
	public void setDistributorAmount(BigDecimal distributorAmount) {
		this.distributorAmount = distributorAmount;
	}
	
	public Double getTotalAmount() {
		return totalAmount;
	}
	public String getTotalAmountText() {
		return null != this.totalAmount ? NumberUtil.formatTosepara(this.totalAmount) : "";
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public Double getChannelfeeAmount() {
		return channelfeeAmount;
	}
	public String getChannelfeeAmountText() {
		return null != this.channelfeeAmount ? NumberUtil.formatTosepara(this.channelfeeAmount) : "";
	}
	public void setChannelfeeAmount(Double channelfeeAmount) {
		this.channelfeeAmount = channelfeeAmount;
	}
	
	public Double getChannelfeePercent() {
		return channelfeePercent;
	}
	public String getChannelfeePercentText() {
		return null != this.channelfeePercent ? NumberUtil.formatTosepara(this.channelfeePercent) + "%" : "";
	}
	public void setChannelfeePercent(Double channelfeePercent) {
		this.channelfeePercent = channelfeePercent;
	}
	
	public BigDecimal getMobiAmount() {
		return mobiAmount;
	}
	public String getMobiAmountText() {
		return null != this.mobiAmount ? NumberUtil.formatTosepara(this.mobiAmount) : "";
	}
	public void setMobiAmount(BigDecimal mobiAmount) {
		this.mobiAmount = mobiAmount;
	}
	
	public Double getMobiPercent() {
		return mobiPercent;
	}
	public String getMobiPercentText() {
		return null != this.mobiPercent ? NumberUtil.formatTosepara(this.mobiPercent)+ "%" : "";
	}
	public void setMobiPercent(Double mobiPercent) {
		this.mobiPercent = mobiPercent;
	}
	
	public BigDecimal getSmsAmount() {
		return smsAmount;
	}
	public String getSmsAmountText() {
		return null != this.smsAmount ? NumberUtil.formatTosepara(this.smsAmount) : "";
	}
	public void setSmsAmount(BigDecimal smsAmount) {
		this.smsAmount = smsAmount;
	}
	
	public Double getSmsPercent() {
		return smsPercent;
	}
	public String getSmsPercentText() {
		return null != this.smsPercent ? NumberUtil.formatTosepara(this.smsPercent) + "%" : "";
	}
	public void setSmsPercent(Double smsPercent) {
		this.smsPercent = smsPercent;
	}
	
}