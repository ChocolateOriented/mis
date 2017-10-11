/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.bean.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 发起退款接口对象
 * @author jxli
 * @version 2017-09-22
 */
public class RefundLaunchDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String refundCode;		// 退款号
	private String amount;		// 退款金额
	private String remittanceSerialNumber;		// 对应汇款流水
	private String remittanceChannel;		// 对应汇款渠道

	public RefundLaunchDto() {
		super();
	}

	@NotEmpty(message = "退款号不能为空")
	@Length(min=1, max=128, message="退款号长度必须介于 1 和 128 之间")
	public String getRefundCode() {
		return refundCode;
	}

	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}

	@NotEmpty(message = "汇款流水不能为空")
	@Length(min=1, max=64, message="对应汇款流水长度必须介于 1 和 64 之间")
	public String getRemittanceSerialNumber() {
		return remittanceSerialNumber;
	}

	public void setRemittanceSerialNumber(String remittanceSerialNumber) {
		this.remittanceSerialNumber = remittanceSerialNumber;
	}

	@NotEmpty(message = "汇款渠道不能为空")
	@Length(min=1, max=20, message="对应汇款渠道长度必须介于 1 和 20 之间")
	public String getRemittanceChannel() {
		return remittanceChannel;
	}

	public void setRemittanceChannel(String remittanceChannel) {
		this.remittanceChannel = remittanceChannel;
	}

	@NotEmpty(message = "退款金额不能为空")
	@Min(value = 0,message = "退款金额必须大于0")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "RefundProcessDto{" +
				"refundCode='" + refundCode + '\'' +
				", amount=" + amount +
				", remittanceSerialNumber='" + remittanceSerialNumber + '\'' +
				", remittanceChannel='" + remittanceChannel + '\'' +
				'}';
	}
}