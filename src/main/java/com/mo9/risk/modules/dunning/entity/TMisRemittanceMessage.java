/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
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
	private String dealcode;
	private String remark;    // 备注
	private String remittanceSerialNumber;// 汇款流水号
	private AccountStatus accountStatus;// 入账状态
	private RemittanceTag remittanceTag;		//入账标签

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

	public String getRemittanceName() {
		return remittanceName;
	}

	public void setRemittanceName(String remittanceName) {
		this.remittanceName = remittanceName;
	}

	public Date getRemittanceTime() {
		return remittanceTime;
	}

	public void setRemittanceTime(Date remittanceTime) {
		this.remittanceTime = remittanceTime;
	}

	public Double getRemittanceAmount() {
		return remittanceAmount;
	}

	public void setRemittanceAmount(Double remittanceAmount) {
		this.remittanceAmount = remittanceAmount;
	}

	public String getRemittanceChannel() {
		return remittanceChannel;
	}

	public void setRemittanceChannel(String remittanceChannel) {
		this.remittanceChannel = remittanceChannel;
	}

	public String getRemittanceAccount() {
		return remittanceAccount;
	}

	public void setRemittanceAccount(String remittanceAccount) {
		this.remittanceAccount = remittanceAccount;
	}

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

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemittanceSerialNumber() {
		return remittanceSerialNumber;
	}

	public void setRemittanceSerialNumber(String remittanceSerialNumber) {
		this.remittanceSerialNumber = remittanceSerialNumber;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	public RemittanceTag getRemittanceTag() {
		return remittanceTag;
	}

	public void setRemittanceTag(
			RemittanceTag remittanceTag) {
		this.remittanceTag = remittanceTag;
	}

	@Override
	public String toString() {
		return "TMisRemittanceMessage{" +
				"dbid='" + dbid + '\'' +
				", remittanceName='" + remittanceName + '\'' +
				", remittanceTime=" + remittanceTime +
				", remittanceAmount=" + remittanceAmount +
				", remittanceChannel='" + remittanceChannel + '\'' +
				", remittanceAccount='" + remittanceAccount + '\'' +
				", financialUser='" + financialUser + '\'' +
				", financialTime=" + financialTime +
				", dealcode='" + dealcode + '\'' +
				", remark='" + remark + '\'' +
				", remittanceSerialNumber='" + remittanceSerialNumber + '\'' +
				", accountStatus=" + accountStatus +
				", remittanceTag=" + remittanceTag +
				'}';
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
	/**
	 * @Description 还款标签
	 * @author jxli
	 * @version 2017/6/23
	 */
	public enum RemittanceTag{
		REPAYMENT_SELF,//本人还款
		REPAYMENT_THIRD//第三方还款
	}

	/**
	 * 入账状态
	 * @author jwchi
	 */
	public enum AccountStatus {
		NOT_AUDIT("未查账"),
		COMPLETE_AUDIT("已查账"),
		FINISH("已完成");
		AccountStatus(String desc) {
			this.desc = desc;
		}

		public final String desc;

		public String getDesc() {
			return desc;
		}
		
	}
	
}