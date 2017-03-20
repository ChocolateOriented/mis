package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  催收订单，从RiskOrder映射而来，用于作为映射数据
 * Created by sun on 2016/7/21.
 */
public class TMisDunningOrder {
	
	public String platform;
	
	public String platformExt;
	/**
	 * id
	 */
	public Integer id;
	/**
	 * id
	 */
	public Integer rootorderid;

	/**
	 * 订单号
	 */
	public String dealcode;
	/**
	 *  订单总金额
	 */
	public BigDecimal amount;
	/**
	 * 手续费
	 */
	public BigDecimal fee;
	/**
	 *  逾期费率
	 */
	public BigDecimal overdueRate = new BigDecimal("0.01");
	/**
	 *  还款日期
	 */
	public Date repaymentDate;
	/***
	 *  减免金额
	 */
	public BigDecimal reliefamount;
	
	
	public int reliefflag;
	/**
	 *  逾期罚息
	 */
	public BigDecimal overdueAmount;
	/**
	 * 订单状态
	 */
	public String status;
	
	public BigDecimal costAmount;//订单手续费总金额
	
	public BigDecimal creditAmount;//使用信用额度
	
	public Long couponId = 0L;//抵用券ID
	
	public BigDecimal subCostAmount = BigDecimal.ZERO;//减免费用总金额
	
	public BigDecimal defaultInterestAmount;//订单续期产生罚息金额和手续费
	
	public String getDealcode() {
		return dealcode;
	}
	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public BigDecimal getOverdueRate() {
		return overdueRate;
	}
	public void setOverdueRate(BigDecimal overdueRate) {
		this.overdueRate = overdueRate;
	}
	public Date getRepaymentDate() {
		return repaymentDate;
	}
	public void setRepaymentDate(Date repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	public BigDecimal getReliefamount() {
		return reliefamount;
	}
	public void setReliefamount(BigDecimal reliefamount) {
		this.reliefamount = reliefamount;
	}
	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}
	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}
	public BigDecimal getSubCostAmount() {
		return subCostAmount == null? BigDecimal.ZERO : subCostAmount;
	}
	public void setSubCostAmount(BigDecimal subCostAmount) {
		this.subCostAmount = subCostAmount;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public Long getCouponId() {
		return couponId;
	}
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public BigDecimal getDefaultInterestAmount() {
		return defaultInterestAmount;
	}
	public void setDefaultInterestAmount(BigDecimal defaultInterestAmount) {
		this.defaultInterestAmount = defaultInterestAmount;
	}
	public int getReliefflag() {
		return reliefflag;
	}
	public void setReliefflag(int reliefflag) {
		this.reliefflag = reliefflag;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRootorderid() {
		return rootorderid;
	}
	public void setRootorderid(Integer rootorderid) {
		this.rootorderid = rootorderid;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPlatformExt() {
		return platformExt;
	}
	public void setPlatformExt(String platformExt) {
		this.platformExt = platformExt;
	}
	

}
