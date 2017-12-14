package com.mo9.risk.modules.dunning.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 东港信函
 * @author jwchi
 *
 */
public class TMisDunningLetter extends DataEntity<TMisDunningLetter> {

	private static final long serialVersionUID = 1L;
	
	private Integer dbId;
	//60+订单号
	private String dealcode;
	//发函结果
	private SendResult sendResult;
	private String sendResultSting;//导入
	//发送时间
	private Date  sendDate;
	private Date  sendBeginDate;
	private Date  sendEndDate;
	//结果更新时间
	private Date resultDate;
	private Date resultBeginDate;
	private Date resultEndDate;
	//借款人姓名
	private String realname;
	//应催金额
	private Double creditamount;
	//逾期天数
	private Integer overduedays;
	//户籍地址
	private String ocrAddr; 
	//邮编
	private String postCode; 
	//截止日期(发送邮件用)
	private Date sendLetterDate;
	//下载标识(下载邮件用)
	private String downLoadFlag;
	
	private String beginOverduedays;
	
	private String endOverduedays;
	
	@ExcelField(title="订单", type=0, align=2, sort=10)
	public String getDealcode() {
		return dealcode;
	}
	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	public SendResult getSendResult() {
		return sendResult;
	}
	@ExcelField(title="发函结果", type=1, align=2, sort=30)
	public String getSendResultText() {
		return sendResult.getDesc();
	}
	public void setSendResult(SendResult sendResult) {
		this.sendResult = sendResult;
	}
	@ExcelField(title="状态", type=2, align=2, sort=335)
	public String getSendResultSting() {
		return sendResultSting;
	}
	public void setSendResultSting(String sendResultSting) {
		this.sendResultSting = sendResultSting;
	}
	@Length(min = 6, max = 6, message = "中国邮编是6位")
	@ExcelField(title="邮编", type=0, align=2, sort=5)
	public String getPostCode() {
		return postCode ;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	@ExcelField(title="信函发送时间", type=1, align=2, sort=70)
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	@ExcelField(title="结果更新时间", type=1, align=2, sort=80)
	public Date getResultDate() {
		return resultDate;
	}
	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
	}
	public Integer getDbId() {
		return dbId;
	}
	public void setDbId(Integer dbId) {
		this.dbId = dbId;
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
	@ExcelField(title="逾期天数", type=1, align=2, sort=50)
	public Integer getOverduedays() {
		return overduedays;
	}
	public void setOverduedays(Integer overduedays) {
		this.overduedays = overduedays;
	}
	@ExcelField(title="户籍地址", type=1, align=2, sort=60)
	public String getOcrAddr() {
		return ocrAddr;
	}
	public void setOcrAddr(String ocrAddr) {
		this.ocrAddr = ocrAddr;
	}
	public String getBeginOverduedays() {
		return beginOverduedays;
	}
	public void setBeginOverduedays(String beginOverduedays) {
		this.beginOverduedays = beginOverduedays;
	}
	public String getEndOverduedays() {
		return endOverduedays;
	}
	public void setEndOverduedays(String endOverduedays) {
		this.endOverduedays = endOverduedays;
	}

	public Date getSendBeginDate() {
		return sendBeginDate;
	}
	public void setSendBeginDate(Date sendBeginDate) {
		this.sendBeginDate = sendBeginDate;
	}
	public Date getSendEndDate() {
		return sendEndDate;
	}
	public void setSendEndDate(Date sendEndDate) {
		this.sendEndDate = sendEndDate;
	}
	public Date getResultBeginDate() {
		return resultBeginDate;
	}
	public void setResultBeginDate(Date resultBeginDate) {
		this.resultBeginDate = resultBeginDate;
	}
	public Date getResultEndDate() {
		return resultEndDate;
	}
	public void setResultEndDate(Date resultEndDate) {
		this.resultEndDate = resultEndDate;
	}
	public Date getSendLetterDate() {
		return sendLetterDate;
	}
	public void setSendLetterDate(Date sendLetterDate) {
		this.sendLetterDate = sendLetterDate;
	}
	public String getDownLoadFlag() {
		return downLoadFlag;
	}
	public void setDownLoadFlag(String downLoadFlag) {
		this.downLoadFlag = downLoadFlag;
	}


	public enum SendResult{
		BESEND("待发送"),
		BEPOST("待寄出"),
		POSTED("已寄出"),
		BACKED("已退回");
		private String desc;
		SendResult(String desc){
			this.desc=desc;
		}
		public String getDesc() {
			return desc;
		}
		
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
}
	
}