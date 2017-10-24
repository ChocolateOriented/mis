/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * RiskOrder映射Entity
 * 因TMisDunningOrder字段设计有问题(命名与作用域)且不全, 但使用较多不易修改, 所以新增此类
 * @author jxli
 * @version 2017-10-18
 */
public class TRiskOrder{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer merchantId;		// merchant_id
	private Integer buyerId;		// buyer_id
	private String partnerId;		// partner_id
	private BigDecimal amount;		// 订单总金额
	private String type;		// type
	private String itemName;		// item_name
	private BigDecimal balance;		// 已还金额
	private String dealcode;		// 订单号
	private Date createTime;		// create_time
	private Integer buyerMerchantId;		// buyer_merchant_id
	private String status;		// status
	private Date updateTime;		// update_time
	private Date repaymentTime;		// repayment_time
	private BigDecimal creditAmount;		// 使用信用额度
	private String payerid;		// payerid
	private String device;		// device
	private Integer ip2long;		// ip2long
	private Integer firstOrder;		// first_order
	private BigDecimal costAmount;		// cost_amount
	private BigDecimal overdueAmount;		// 逾期费
	private String days;		// days
	private Date remitTime;		// remit_time
	private Date payoffTime;		// payoff_time
	private Integer version;		// version
	private String msg;		// msg
	private Integer rootOrderId;		// root_order_id
	private String thirdCode;		// third_code
	private String channel;		// channel
	private String isDelay;		// is_delay
	private String remark;		// remark
	private String sessionid;		// sessionid
	private String payCode;		// 订单放款渠道
	private Integer couponsId;		// 抵用券ID
	private String platform;		// platform
	private String platformExt;		// platform_ext
	private BigDecimal defaultInterestAmount;		// default_interest_amount
	private Integer delayDays;		// delay_days
	private BigDecimal modifyAmount;		// modify_amount
	private Integer modifyFlag;		// modify_flag
	private Integer modifyOperator1;		// modify_operator1
	private Integer modifyOperator2;		// modify_operator2
	private Integer modifyOperator3;		// modify_operator3
	private BigDecimal subCostAmount;		// 减免费用总金额
	private Date outerfiletime;		// 委外导出时间
	private String auditFlow;		// audit_flow
	private Double scAppl;		// sc_appl
	private String scApplver;		// sc_applver
	private String payerMsg;		// payer_msg

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	public Integer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}
	
	@Length(min=0, max=45, message="partner_id长度必须介于 0 和 45 之间")
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Length(min=0, max=20, message="type长度必须介于 0 和 20 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=45, message="item_name长度必须介于 0 和 45 之间")
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	@Length(min=0, max=45, message="dealcode长度必须介于 0 和 45 之间")
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getBuyerMerchantId() {
		return buyerMerchantId;
	}

	public void setBuyerMerchantId(Integer buyerMerchantId) {
		this.buyerMerchantId = buyerMerchantId;
	}
	
	@Length(min=0, max=50, message="status长度必须介于 0 和 50 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="update_time不能为空")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRepaymentTime() {
		return repaymentTime;
	}

	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}
	
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	
	@Length(min=0, max=45, message="payerid长度必须介于 0 和 45 之间")
	public String getPayerid() {
		return payerid;
	}

	public void setPayerid(String payerid) {
		this.payerid = payerid;
	}
	
	@Length(min=0, max=45, message="device长度必须介于 0 和 45 之间")
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	
	public Integer getIp2long() {
		return ip2long;
	}

	public void setIp2long(Integer ip2long) {
		this.ip2long = ip2long;
	}
	
	public Integer getFirstOrder() {
		return firstOrder;
	}

	public void setFirstOrder(Integer firstOrder) {
		this.firstOrder = firstOrder;
	}
	
	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}
	
	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
	
	@Length(min=0, max=10, message="days长度必须介于 0 和 10 之间")
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRemitTime() {
		return remitTime;
	}

	public void setRemitTime(Date remitTime) {
		this.remitTime = remitTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPayoffTime() {
		return payoffTime;
	}

	public void setPayoffTime(Date payoffTime) {
		this.payoffTime = payoffTime;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Length(min=0, max=50, message="msg长度必须介于 0 和 50 之间")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Integer getRootOrderId() {
		return rootOrderId;
	}

	public void setRootOrderId(Integer rootOrderId) {
		this.rootOrderId = rootOrderId;
	}
	
	@Length(min=0, max=100, message="third_code长度必须介于 0 和 100 之间")
	public String getThirdCode() {
		return thirdCode;
	}

	public void setThirdCode(String thirdCode) {
		this.thirdCode = thirdCode;
	}
	
	@Length(min=0, max=50, message="channel长度必须介于 0 和 50 之间")
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	@Length(min=0, max=2, message="is_delay长度必须介于 0 和 2 之间")
	public String getIsDelay() {
		return isDelay;
	}

	public void setIsDelay(String isDelay) {
		this.isDelay = isDelay;
	}
	
	@Length(min=0, max=200, message="remark长度必须介于 0 和 200 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Length(min=0, max=50, message="sessionid长度必须介于 0 和 50 之间")
	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	@Length(min=0, max=100, message="pay_code长度必须介于 0 和 100 之间")
	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	
	public Integer getCouponsId() {
		return couponsId;
	}

	public void setCouponsId(Integer couponsId) {
		this.couponsId = couponsId;
	}
	
	@Length(min=0, max=50, message="platform长度必须介于 0 和 50 之间")
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	@Length(min=0, max=128, message="platform_ext长度必须介于 0 和 128 之间")
	public String getPlatformExt() {
		return platformExt;
	}

	public void setPlatformExt(String platformExt) {
		this.platformExt = platformExt;
	}
	
	public BigDecimal getDefaultInterestAmount() {
		return defaultInterestAmount;
	}

	public void setDefaultInterestAmount(BigDecimal defaultInterestAmount) {
		this.defaultInterestAmount = defaultInterestAmount;
	}
	
	public Integer getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(Integer delayDays) {
		this.delayDays = delayDays;
	}
	
	public BigDecimal getModifyAmount() {
		return modifyAmount;
	}

	public void setModifyAmount(BigDecimal modifyAmount) {
		this.modifyAmount = modifyAmount;
	}
	
	public Integer getModifyFlag() {
		return modifyFlag;
	}

	public void setModifyFlag(Integer modifyFlag) {
		this.modifyFlag = modifyFlag;
	}
	
	public Integer getModifyOperator1() {
		return modifyOperator1;
	}

	public void setModifyOperator1(Integer modifyOperator1) {
		this.modifyOperator1 = modifyOperator1;
	}
	
	public Integer getModifyOperator2() {
		return modifyOperator2;
	}

	public void setModifyOperator2(Integer modifyOperator2) {
		this.modifyOperator2 = modifyOperator2;
	}
	
	public Integer getModifyOperator3() {
		return modifyOperator3;
	}

	public void setModifyOperator3(Integer modifyOperator3) {
		this.modifyOperator3 = modifyOperator3;
	}
	
	public BigDecimal getSubCostAmount() {
		return subCostAmount;
	}

	public void setSubCostAmount(BigDecimal subCostAmount) {
		this.subCostAmount = subCostAmount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOuterfiletime() {
		return outerfiletime;
	}

	public void setOuterfiletime(Date outerfiletime) {
		this.outerfiletime = outerfiletime;
	}
	
	@Length(min=0, max=64, message="audit_flow长度必须介于 0 和 64 之间")
	public String getAuditFlow() {
		return auditFlow;
	}

	public void setAuditFlow(String auditFlow) {
		this.auditFlow = auditFlow;
	}
	
	public Double getScAppl() {
		return scAppl;
	}

	public void setScAppl(Double scAppl) {
		this.scAppl = scAppl;
	}
	
	@Length(min=0, max=128, message="sc_applver长度必须介于 0 和 128 之间")
	public String getScApplver() {
		return scApplver;
	}

	public void setScApplver(String scApplver) {
		this.scApplver = scApplver;
	}
	
	@Length(min=0, max=2000, message="payer_msg长度必须介于 0 和 2000 之间")
	public String getPayerMsg() {
		return payerMsg;
	}

	public void setPayerMsg(String payerMsg) {
		this.payerMsg = payerMsg;
	}
	
}