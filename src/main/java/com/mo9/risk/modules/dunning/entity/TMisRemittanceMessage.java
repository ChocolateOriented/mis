/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import java.util.Date;

/**
 * 财务确认汇款信息Entity
 * @author 徐盛
 * @version 2016-08-11
 */
public class TMisRemittanceMessage extends DataEntity<TMisRemittanceMessage> {
	
	private static final long serialVersionUID = 1L;
	private String dbid;		// dbid
	private String remittanceName;		// 汇款人姓名
	private Date remittanceTime;		// 汇款时间
	private Double remittanceAmount;		// 金额
	private String remittanceChannel;		// 汇款渠道
	private String remittanceAccount;		// 汇款帐号
	private String financialUser;		// 财务确认人
	private Date financialTime;		// 财务确认时间
	private String remark;    // 备注
	private String remittanceSerialNumber;// 汇款流水号
	private String accountStatus;//用来做更新汇款信息

	private Date begindealtime;//用来查询开始时间
	private Date enddealtime;//用来查询的结束时间
	
	public Date getBegindealtime() {
		return begindealtime;
	}

	public void setBegindealtime(Date begindealtime) {
		this.begindealtime = begindealtime;
	}

	public Date getEnddealtime() {
		return enddealtime;
	}

	public void setEnddealtime(Date enddealtime) {
		this.enddealtime =  null != enddealtime ? DateUtils.endDate(enddealtime) : enddealtime;
	}

	public TMisRemittanceMessage() {
		super();
	}

	public TMisRemittanceMessage(String id){
		super(id);
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	@ExcelField(title="汇款人名称", type=1, align=2, sort=5)
	public String getRemittanceName() {
		return remittanceName;
	}

	public void setRemittanceName(String remittanceName) {
		this.remittanceName = remittanceName;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="交易时间", type=1, align=2, sort=2)
	public Date getRemittanceTime() {
		return remittanceTime;
	}

	public void setRemittanceTime(Date remittanceTime) {
		this.remittanceTime = remittanceTime;
	}

	@ExcelField(title="交易金额（元）", type=1, align=2, sort=4)
	public Double getRemittanceAmount() {
		return remittanceAmount;
	}

	public void setRemittanceAmount(Double remittanceAmount) {
		this.remittanceAmount = remittanceAmount;
	}

	public String getRemittanceChannel() {
		return remittanceChannel;
	}

	@ExcelField(title="交易渠道", type=1, align=2, sort=3)
	public String getRemittanceChannelText() {
		if ("alipay".equals(remittanceChannel)){
			return "支付宝";
		}
		return remittanceChannel;
	}

	public void setRemittanceChannel(String remittanceChannel) {
		this.remittanceChannel = remittanceChannel;
	}

	@ExcelField(title="汇款人账户", type=1, align=2, sort=6)
	public String getRemittanceAccount() {
		return remittanceAccount;
	}

	public void setRemittanceAccount(String remittanceAccount) {
		this.remittanceAccount = remittanceAccount;
	}

	@ExcelField(title="上传人", type=1, align=2, sort=10)
	public String getFinancialUser() {
		return financialUser;
	}

	public void setFinancialUser(String financialUser) {
		this.financialUser = financialUser;
	}

	public Date getFinancialTime() {
		return financialTime;
	}

	public void setFinancialTime(Date financialTime) {
		this.financialTime = financialTime;
	}

	@ExcelField(title="备注", type=1, align=2, sort=7)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ExcelField(title="交易流水号", type=1, align=2, sort=1)
	public String getRemittanceSerialNumber() {
		return remittanceSerialNumber;
	}

	public void setRemittanceSerialNumber(String remittanceSerialNumber) {
		this.remittanceSerialNumber = remittanceSerialNumber;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	@ExcelField(title="入账状态", type=1, align=2, sort=8)
	public String getAccountStatusText() {
		if (TMisRemittanceConfirm.CONFIRMSTATUS_COMPLETE_AUDIT.equals(accountStatus)){
			return "已查账";
		}else if (TMisRemittanceConfirm.CONFIRMSTATUS_FINISH.equals(accountStatus)){
			return "已完成";
		}else if (null == accountStatus){
			return "未查账";
		}
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	@Override
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="上传时间", type=1, align=2, sort=9)
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public String toString() {
		return "TMisRemittanceMessage [dbid=" + dbid + ", remittanceName=" + remittanceName + ", remittanceTime="
				+ remittanceTime + ", remittanceAmount=" + remittanceAmount + ", remittanceChannel=" + remittanceChannel
				+ ", remittanceAccount=" + remittanceAccount + ", financialUser=" + financialUser + ", financialTime="
				+ financialTime + ", remark=" + remark + ", remittanceSerialNumber=" + remittanceSerialNumber
				+ ", accountStatus=" + accountStatus + ", begindealtime=" + begindealtime + ", enddealtime="
				+ enddealtime + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TMisRemittanceMessage that = (TMisRemittanceMessage) o;

		if (remittanceChannel != null ? !remittanceChannel.equals(that.remittanceChannel)
				: that.remittanceChannel != null) {
			return false;
		}
		return remittanceSerialNumber != null ? remittanceSerialNumber.equals(that.remittanceSerialNumber)
				: super.equals(that);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (remittanceChannel != null ? remittanceChannel.hashCode() : 0);
		result = 31 * result + (remittanceSerialNumber != null ? remittanceSerialNumber.hashCode() : 0);
		return result;
	}

}