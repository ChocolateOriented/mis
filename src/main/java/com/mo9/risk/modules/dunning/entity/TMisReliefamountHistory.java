/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

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
	//减免原因
	private DerateReason derateReason;		
	
	
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
	
}