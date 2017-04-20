/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 逾期未还订单视图Entity
 * 
 * @author beargao
 * @version 2016-07-13
 */
public class TRiskBuyerPersonalInfo extends DataEntity<TRiskBuyerPersonalInfo> {

	private static final long serialVersionUID = 1L;
	private String dealcode; // 逾期订单号
	private Date createTime; // 创建时间
	private Date remitTime; // 放款时间
	private Date repaymentTime; // 订单应还款时间
	private String buyerId; // 借款人id
	private String mobile; // 借款人手机
	private String realName;            // 借款人姓名
	private String idcard; // 借款人身份证号
	private String days; // 借款周期
	private String overdueDays; // 逾期天数
	private String corpusAmount; // 本金
	private String costAmout; // 手续费
	private String amount; // 订单金额
	private String overdueAmount; // 逾期罚息金额
	private String discountAmount; // 折扣金额
	private String balance; // 已还金额
	private String modifyAmount; // 催收减免金额
	private String creditAmount; // 实际应还金额
	private String delayCount; // 续期次数
	private Date payOffTime; // 还清时间
	private String sex; //性别
	private String mobileCity; // 手机归属地
	private String marital; // 婚姻状况
	private String livingAddr; // 居住地址
	private String ocrAddr; // 证件地址
	private String remitBankNo; // 放款银行卡号
	private String remitBankName; // 放款银行名
	private String repaymentAmount; // 还款总额
	
	private String finProduct;//借款的金融产品
	private Integer rpayStatus;//还款日期；1表示还款前一天，0表示还款当天。
	
	
	public String getFinProduct() {
		return finProduct;
	}

	public void setFinProduct(String finProduct) {
		this.finProduct = finProduct;
	}

	public Integer getRpayStatus() {
		return rpayStatus;
	}

	public void setRpayStatus(Integer rpayStatus) {
		this.rpayStatus = rpayStatus;
	}

	public TRiskBuyerPersonalInfo() {
		super();
	}

	public TRiskBuyerPersonalInfo(String id) {
		super(id);
	}

	@Length(min = 0, max = 11, message = "逾期订单号长度必须介于 0 和 11 之间")
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPayOffTime() {
		return payOffTime;
	}

	public void setPayOffTime(Date payOffTime) {
		this.payOffTime = payOffTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRemitTime() {
		return remitTime;
	}

	public void setRemitTime(Date remitTime) {
		this.remitTime = remitTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRepaymentTime() {
		return repaymentTime;
	}

	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	@Length(min = 0, max = 11, message = "借款人id长度必须介于 0 和 11 之间")
	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	@Length(min = 0, max = 13, message = "借款人手机长度必须介于 0 和 13 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(min = 0, max = 30, message = "借款人姓名长度必须介于 0 和 30 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Length(min = 0, max = 18, message = "借款人身份证号长度必须介于 0 和 18 之间")
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Length(min = 0, max = 11, message = "借款周期长度必须介于 0 和 11 之间")
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	@Length(min = 0, max = 11, message = "逾期天数长度必须介于 0 和 11 之间")
	public String getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(String overdueDays) {
		this.overdueDays = overdueDays;
	}

	@Length(min = 0, max = 11, message = "本金长度必须介于 0 和 11 之间")
	public String getCorpusAmount() {
		return corpusAmount;
	}

	public void setCorpusAmount(String corpusAmount) {
		this.corpusAmount = corpusAmount;
	}

	@Length(min = 0, max = 11, message = "手续费长度必须介于 0 和 11 之间")
	public String getCostAmout() {
		return costAmout;
	}

	public void setCostAmout(String costAmout) {
		this.costAmout = costAmout;
	}

	@Length(min = 0, max = 11, message = "订单金额长度必须介于 0 和 11 之间")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Length(min = 0, max = 11, message = "逾期罚息金额长度必须介于 0 和 11 之间")
	public String getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(String overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	@Length(min = 0, max = 11, message = "折扣金额长度必须介于 0 和 11 之间")
	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	@Length(min = 0, max = 11, message = "已还金额长度必须介于 0 和 11 之间")
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Length(min = 0, max = 11, message = "催收减免金额长度必须介于 0 和 11 之间")
	public String getModifyAmount() {
		return modifyAmount;
	}

	public void setModifyAmount(String modifyAmount) {
		this.modifyAmount = modifyAmount;
	}

	@Length(min = 0, max = 11, message = "实际应还金额长度必须介于 0 和 11 之间")
	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	@Length(min = 0, max = 11, message = "续期次数必须介于 0 和 11 之间")
	public String getDelayCount() {
		return delayCount;
	}

	public void setDelayCount(String delayCount) {
		this.delayCount = delayCount;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobileCity() {
		return mobileCity;
	}

	public void setMobileCity(String mobileCity) {
		this.mobileCity = mobileCity;
	}

	public String getMarital() {
		return marital;
	}

	public void setMarital(String marital) {
		this.marital = marital;
	}

	public String getLivingAddr() {
		return livingAddr;
	}

	public void setLivingAddr(String livingAddr) {
		this.livingAddr = livingAddr;
	}

	public String getOcrAddr() {
		return ocrAddr;
	}

	public void setOcrAddr(String ocrAddr) {
		this.ocrAddr = ocrAddr;
	}

	public String getRemitBankNo() {
		return remitBankNo;
	}

	public void setRemitBankNo(String remitBankNo) {
		this.remitBankNo = remitBankNo;
	}

	public String getRemitBankName() {
		return remitBankName;
	}

	public void setRemitBankName(String remitBankName) {
		this.remitBankName = remitBankName;
	}

	public String getRepaymentAmount() {
		return repaymentAmount;
	}

	public void setRepaymentAmount(String repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}
	
	

}