package com.mo9.risk.modules.dunning.entity;


/**
 * 代付基础类 Created by sun on 2016/7/21.
 */
public class TMisPaid {

	/**
	 * 代付金额
	 */
	public String paidAmount;
	/**
	 * 用户id
	 */
	public String buyerId;
	/**
	 * 订单号
	 */
	public String dealcode;
	/**
	 * 代付类型
	 */
	public String paidType;
	/**
	 * 续期天数
	 */
	public String delayDay;
	/**
	 * 代付渠道
	 */
	public String paychannel;
	/**
	 * 跳转url
	 */
	public String redirectUrl;
	/**
	 * 备注
	 */
	public String remark;
	/**
	 * 返回状态码
	 */
	public String code;
	/**
	 * 返回信息
	 */
	public String msg;

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getPaidType() {
		return paidType;
	}

	public void setPaidType(String paidType) {
		this.paidType = paidType;
	}

	public String getDelayDay() {
		return delayDay;
	}

	public void setDelayDay(String delayDay) {
		this.delayDay = delayDay;
	}

	public String getPaychannel() {
		return paychannel;
	}

	public void setPaychannel(String paychannel) {
		this.paychannel = paychannel;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
