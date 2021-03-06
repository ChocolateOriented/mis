/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 通话记录Entity
 * @author beragao
 * @version 2016-07-15
 */
public class TRiskBuyerContactRecords extends DataEntity<TRiskBuyerContactRecords> {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String familyRelation;
	private String tel;
	private String location;		// location
	private String times;		// times
	private Integer smsNum;
	private Integer telNum;
	private String dealcode;
	private String rcname;
	private String contactType;		// contact_type
	private String buyerId;		// buyer_id
	private Integer number;
	private Integer sumtime;
//	private Integer cnt; //通话次数
//	private Integer duration;//通话时长(秒)

	private Integer inCnt;//被叫次数
	private Integer inDuration;//被呼叫时长(秒)
	
	private Integer effectiveActionNum;		//有效action次数
	
	public TRiskBuyerContactRecords() {
		super();
	}

	public TRiskBuyerContactRecords(String id){
		super(id);
	}

	@Length(min=1, max=10, message="buyer_id长度必须介于 1 和 10 之间")
	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	
	@Length(min=0, max=45, message="location长度必须介于 0 和 45 之间")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Length(min=0, max=45, message="contact_type长度必须介于 0 和 45 之间")
	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	
	@Length(min=0, max=10, message="times长度必须介于 0 和 10 之间")
	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	@Length(min=0, max=50, message="family_relation长度必须介于 0 和 50 之间")
	public String getFamilyRelation() {
		return familyRelation;
	}

	public void setFamilyRelation(String familyRelation) {
		this.familyRelation = familyRelation;
	}

	@Length(min=0, max=50, message="name长度必须介于 0 和 50 之间")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=50, message="tel长度必须介于 0 和 50 之间")
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(Integer smsNum) {
		this.smsNum = smsNum;
	}

	public Integer getTelNum() {
		return telNum;
	}

	public void setTelNum(Integer telNum) {
		this.telNum = telNum;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getRcname() {
		return rcname;
	}

	public void setRcname(String rcname) {
		this.rcname = rcname;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getSumtime() {
		return sumtime;
	}
	public void setSumtime(Integer sumtime) {
		this.sumtime = sumtime;
	}

	public void setCnt(Integer cnt) {
		this.number = cnt;
	}

	//通话时长, 用于接收通讯录接口JSON
	public void setDuration(Integer duration) {
		this.sumtime = duration;
		long hour=duration/3600;
		long minute=duration%3600/60;
		long second=duration%60;
		this.times = hour+"小时"+minute+"分"+second+"秒";
	}

	public Integer getInCnt() {
		return inCnt;
	}

	public void setInCnt(Integer inCnt) {
		this.inCnt = inCnt;
	}

	public Integer getInDuration() {
		return inDuration;
	}

	public void setInDuration(Integer inDuration) {
		this.inDuration = inDuration;
	}

	public Integer getOutCnt() {
		if (this.number == null || this.inCnt == null) {
			return null;
		}
		return this.number - this.inCnt;
	}

	public String getInTimes() {
		if (this.inDuration == null) {
			return null;
		}
		int hour = this.inDuration / 3600;
		int minute = this.inDuration % 3600 / 60;
		int second = this.inDuration % 60;
		return hour + "小时" + minute + "分" + second + "秒";
	}

	public String getOutTimes() {
		if (this.sumtime == null || this.inDuration == null) {
			return null;
		}
		int outDuration = this.sumtime - this.inDuration;
		int hour = outDuration / 3600;
		int minute = outDuration % 3600 / 60;
		int second = outDuration % 60;
		return hour + "小时" + minute + "分" + second + "秒";
	}

	public Integer getEffectiveActionNum() {
		return effectiveActionNum;
	}

	public void setEffectiveActionNum(Integer effectiveActionNum) {
		this.effectiveActionNum = effectiveActionNum;
	}

	@Override
	public String toString() {
		return "TRiskBuyerContactRecords{" +
				"name='" + name + '\'' +
				", familyRelation='" + familyRelation + '\'' +
				", tel='" + tel + '\'' +
				", location='" + location + '\'' +
				", times='" + times + '\'' +
				", smsNum=" + smsNum +
				", telNum=" + telNum +
				", dealcode='" + dealcode + '\'' +
				", rcname='" + rcname + '\'' +
				", contactType='" + contactType + '\'' +
				", buyerId='" + buyerId + '\'' +
				", number=" + number +
				", sumtime=" + sumtime +
				", inCnt=" + inCnt +
				", inDuration=" + inDuration +
				", effectiveActionNum=" + effectiveActionNum +
				'}';
	}
}