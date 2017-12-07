package com.mo9.risk.modules.dunning.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

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
	//发送时间
	private Date  sendDate;
	//结果更新时间
	private Date resultDate;
	//借款人姓名
	private String realname;
	//欠款金额
	private Double amount;
	//逾期天数
	private Integer overduedays;
	//户籍地址
	private String ocrAddr; 
	
	private String beginOverduedays;
	private String endOverduedays;
	
	public String getDealcode() {
		return dealcode;
	}
	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	public SendResult getSendResult() {
		return sendResult;
	}
	public void setSendResult(SendResult sendResult) {
		this.sendResult = sendResult;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
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
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getOverduedays() {
		return overduedays;
	}
	public void setOverduedays(Integer overduedays) {
		this.overduedays = overduedays;
	}
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
	public String getSendResultText(){
		return this.sendResult.getDesc();
	}
	

}

enum SendResult{
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