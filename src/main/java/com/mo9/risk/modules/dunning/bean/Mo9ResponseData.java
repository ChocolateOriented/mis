/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;

/**
 * 调用mo9代扣响应报文Entity
 * @author shijlu
 * @version 2017-04-11
 */
public class Mo9ResponseData implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private String status;		//请求状态
	
	private String reason;		//状态原因
	
	private String message;		//信息
	
	private String sign;		//签名
	
	private Mo9ResponseOrder data;		//返回的订单数据
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Mo9ResponseOrder getData() {
		return data;
	}

	public void setData(Mo9ResponseOrder data) {
		this.data = data;
	}

	public class Mo9ResponseOrder implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private String invoice;		//请求发起方订单号
		
		private String dealCode;		//请求接收方订单号
		
		private String dealAmount;		//交易金额
		
		private String orderStatus;		//当前订单状态
		
		private String reason;		//订单状态原因
		
		private String message;		//信息

		public String getInvoice() {
			return invoice;
		}

		public void setInvoice(String invoice) {
			this.invoice = invoice;
		}

		public String getDealCode() {
			return dealCode;
		}

		public void setDealCode(String dealCode) {
			this.dealCode = dealCode;
		}

		public String getDealAmount() {
			return dealAmount;
		}

		public void setDealAmount(String dealAmount) {
			this.dealAmount = dealAmount;
		}

		public String getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
	}

}