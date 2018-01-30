/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import java.util.Date;
import org.hibernate.validator.constraints.Length
		;

/**
 * 减免记录Entity
 * @author 徐盛
 * @version 2016-08-05
 */
public class TMisReliefamountHistory extends DataEntity<TMisReliefamountHistory> {
	
	private static final long serialVersionUID = 1L;
	private String dbid;		// dbid
	private String dealcode;		// 订单号
	private String reliefamount;		// 减免金额
	private DerateReason derateReason;		//减免原因
	private ReliefamountStatus status; //减免状态
	private String applyUserId;//申请人ID
	private Date applyTime;//申请时间
	private String checkUserId;//审核人ID
	private Date checkTime;//审核时间

	private String applyUserName;//催收员花名
	private String checkUserName;//审批人账号名

	public DerateReason getDerateReason() {
		return derateReason;
	}

	public void setDerateReason(DerateReason derateReason) {
		this.derateReason = derateReason;
	}

	public TMisReliefamountHistory() {
		super();
	}

	public TMisReliefamountHistory(String id){
		super(id);
	}

	@Length(min=1, max=11, message="dbid长度必须介于 1 和 11 之间")
	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	
	@Length(min=0, max=128, message="订单号长度必须介于 0 和 128 之间")
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	
	public String getReliefamount() {
		return reliefamount;
	}

	public void setReliefamount(String reliefamount) {
		this.reliefamount = reliefamount;
	}

	public ReliefamountStatus getStatus() {
		return status;
	}

	public void setStatus(ReliefamountStatus status) {
		this.status = status;
	}

	public String getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getCheckUserId() {
		return checkUserId;
	}

	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getCheckUserName() {
		return checkUserName;
	}

	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}

	public enum ReliefamountStatus{
		APPLY("申请"),
		REFUSE("拒绝"),
		AGREE("同意");

		ReliefamountStatus(String desc) {
			this.desc = desc;
		}

		public final String desc;

		public String getDesc() {
			return desc;
		}
	}
}