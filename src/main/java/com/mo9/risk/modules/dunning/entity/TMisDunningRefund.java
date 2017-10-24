/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.validator.constraints.Length;

/**
 * 退款记录Entity
 * @author jxli
 * @version 2017-09-22
 */
public class TMisDunningRefund extends DataEntity<TMisDunningRefund> {

	private static final long serialVersionUID = 1L;
	private String refundCode;		// 退款号
	private BigDecimal amount;		// 退款金额
	private String remittanceSerialNumber;		// 对应汇款流水
	private String remittanceChannel;		// 对应汇款渠道
	private RefundStatus refundStatus;		// 退款状态
	private String auditor;		// 审核人
	private Date auditTime;		// 审核时间
	private Date refundTime;		// 退款时间

	public TMisDunningRefund() {
		super();
	}

	public TMisDunningRefund(String id){
		super(id);
	}

	@Length(min=1, max=128, message="退款号长度必须介于 1 和 128 之间")
	public String getRefundCode() {
		return refundCode;
	}

	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Length(min=0, max=64, message="对应汇款流水长度必须介于 0 和 64 之间")
	public String getRemittanceSerialNumber() {
		return remittanceSerialNumber;
	}

	public void setRemittanceSerialNumber(String remittanceSerialNumber) {
		this.remittanceSerialNumber = remittanceSerialNumber;
	}

	@Length(min=0, max=20, message="对应汇款渠道长度必须介于 0 和 20 之间")
	public String getRemittanceChannel() {
		return remittanceChannel;
	}

	public void setRemittanceChannel(String remittanceChannel) {
		this.remittanceChannel = remittanceChannel;
	}

	public RefundStatus getRefundStatus() {
		return refundStatus;
	}
	public String getRefundStatusText() {
		return refundStatus.desc;
	}

	public void setRefundStatus(RefundStatus refundStatus) {
		this.refundStatus = refundStatus;
	}

	@Length(min=0, max=64, message="审核人长度必须介于 0 和 64 之间")
	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	@Override
	public String toString() {
		return "TMisDunningRefund{" +
				"refundCode='" + refundCode + '\'' +
				", amount=" + amount +
				", remittanceSerialNumber='" + remittanceSerialNumber + '\'' +
				", remittanceChannel='" + remittanceChannel + '\'' +
				", refundStatus=" + refundStatus +
				", auditor='" + auditor + '\'' +
				", auditTime=" + auditTime +
				", refundTime=" + refundTime +
				'}';
	}

	public enum RefundStatus{
		LAUNCH("发起退款"),
		CANCEL("取消退款"),
		REFUSED("拒绝退款"),
		PROCESS("退款中"),
		FINISH("已退款"),
		FAIL("退款失败");

		RefundStatus(String desc) {
			this.desc = desc;
		}

		public final String desc;

		public String getDesc() {
			return desc;
		}
	}

	//对入账无影响退款状态
	public static final List<RefundStatus> INVALID_REFUND_STATUS_LIST = Arrays.asList(RefundStatus.CANCEL, RefundStatus.REFUSED, RefundStatus.FAIL);
	//有影响退款状态
	public static final List<RefundStatus> VALID_REFUND_STATUS_LIST = Arrays.asList(RefundStatus.LAUNCH, RefundStatus.PROCESS, RefundStatus.FINISH);
}