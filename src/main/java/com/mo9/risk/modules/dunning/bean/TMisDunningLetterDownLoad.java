package com.mo9.risk.modules.dunning.bean;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 东港信函下载
 * @author jwchi
 *
 */
public class TMisDunningLetterDownLoad {

	//60+订单号
	private String dealcode;
	//借款人姓名
	private String realname;
	//应催金额
	private Double creditamount;
	//户籍地址
	private String ocrAddr; 
	//截止日期(发送邮件用)
	private Date sendLetterDate;
	
	@ExcelField(title="订单", type=1, align=2, sort=30)
	public String getDealcode() {
		return dealcode;
	}
	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	@ExcelField(title="姓名", type=1, align=2, sort=20)
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	@ExcelField(title="欠款金额", type=1, align=2, sort=40)
	public Double getCreditamount() {
		return creditamount;
	}
	public void setCreditamount(Double creditamount) {
		this.creditamount = creditamount;
	}
	@ExcelField(title="户籍地址", type=1, align=2, sort=10)
	public String getOcrAddr() {
		return ocrAddr;
	}
	public void setOcrAddr(String ocrAddr) {
		this.ocrAddr = ocrAddr;
	}
	@ExcelField(title="截止日期", type=1, align=2, sort=50)
	public Date getSendLetterDate() {
		return sendLetterDate;
	}
	public void setSendLetterDate(Date sendLetterDate) {
		this.sendLetterDate = sendLetterDate;
	}
}