/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 财务确认汇款信息Entity
 * @author 徐盛
 * @version 2016-08-11
 */
public class TMisRemittanceMessage extends DataEntity<TMisRemittanceMessage> {
	
	private static final long serialVersionUID = 1L;
	private String dbid;		// dbid
	private String remittancename;		// 汇款人姓名
	private Date remittancetime;		// 汇款时间
	private Double remittanceamount;		// 金额
	private String remittancechannel;		// 汇款渠道
	private String remittanceaccount;		// 汇款帐号
	private String financialuser;		// 财务确认人
	private Date financialtime;		// 财务确认时间
	private String remittanceimg;		// 汇款图片
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

	@Length(min=1, max=11, message="dbid长度必须介于 1 和 11 之间")
//	@ExcelField(title="登录名", align=2, sort=30)
	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	
	@Length(min=0, max=128, message="汇款人姓名长度必须介于 0 和 128 之间")
	@ExcelField(title="对方名称",type=0, align=2, sort=5)
	public String getRemittancename() {
		return remittancename;
	}

	public void setRemittancename(String remittancename) {
		this.remittancename = remittancename;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="入账时间",type=0, align=2, sort=2)
	public Date getRemittancetime() {
		return remittancetime;
	}

	public void setRemittancetime(Date remittancetime) {
		this.remittancetime = remittancetime;
	}
	
	public Double getRemittanceamount() {
		return remittanceamount;
	}
	
	@ExcelField(title="收入（+元）",type=0, align=2, sort=4)
	public String getRemittanceamountText() {
		return null != this.remittanceamount ? NumberUtil.formatTosepara(this.remittanceamount) : "";
	}
	public void setRemittanceamount(Double remittanceamount) {
		this.remittanceamount = remittanceamount;
	}
	
	@Length(min=0, max=64, message="汇款渠道长度必须介于 0 和 64 之间")
	@ExcelField(title="支付渠道",type=0, align=2, sort=3)
	public String getRemittancechannel() {
		return remittancechannel;
	}

	public void setRemittancechannel(String remittancechannel) {
		this.remittancechannel = remittancechannel;
	}
	
	@Length(min=0, max=64, message="汇款帐号长度必须介于 0 和 64 之间")
	@ExcelField(title="对方账户",type=0, align=2, sort=6)
	public String getRemittanceaccount() {
		return remittanceaccount;
	}

	public void setRemittanceaccount(String remittanceaccount) {
		this.remittanceaccount = remittanceaccount;
	}
	
	@Length(min=0, max=128, message="财务确认人长度必须介于 0 和 128 之间")
	@ExcelField(title="上传人",type=0, align=2, sort=10)
	public String getFinancialuser() {
		return financialuser;
	}

	public void setFinancialuser(String financialuser) {
		this.financialuser = financialuser;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="上传时间",type=0, align=2, sort=9)
	public Date getFinancialtime() {
		return financialtime;
	}

	public void setFinancialtime(Date financialtime) {
		this.financialtime = financialtime;
	}
	
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	@ExcelField(title="备注",type=0, align=2, sort=7)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ExcelField(title="交易流水号",type=0, align=2, sort=1)
	public String getRemittanceSerialNumber() {
		return remittanceSerialNumber;
	}

	public void setRemittanceSerialNumber(String remittanceSerialNumber) {
		this.remittanceSerialNumber = remittanceSerialNumber;
	}

	@ExcelField(title="入账状态",type=0, align=2, sort=8)
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	
	public String getRemittanceimg() {
		return remittanceimg;
	}

	public void setRemittanceimg(String remittanceimg) {
		this.remittanceimg = remittanceimg;
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
				", remittancename='" + remittancename + '\'' +
				", remittancetime=" + remittancetime +
				", remittanceamount=" + remittanceamount +
				", remittancechannel='" + remittancechannel + '\'' +
				", remittanceaccount='" + remittanceaccount + '\'' +
				", financialuser='" + financialuser + '\'' +
				", financialtime=" + financialtime +
				", remittanceimg='" + remittanceimg + '\'' +
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

		if (remittancechannel != null ? !remittancechannel.equals(that.remittancechannel)
				: that.remittancechannel != null) {
			return false;
		}
		return remittanceSerialNumber != null ? remittanceSerialNumber.equals(that.remittanceSerialNumber)
				: super.equals(that);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (remittancechannel != null ? remittancechannel.hashCode() : 0);
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