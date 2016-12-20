/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 催收历史Entity
 * @author 徐盛
 * @version 2016-07-12
 */
public class TMisDunnedHistory extends DataEntity<TMisDunnedHistory> {
	
	private static final long serialVersionUID = 1L;
	private Integer dbid;		// dbid
	private String taskid;		// 催收任务Id，指向对应的催收任务
	private Integer amount;		// 催回金额
	private Date dunnedtime;		// 催回时间
	private boolean ispayoff;		// 是否还清
	private Integer overduedays;		// 当前逾期天数
	private Integer reliefamount;		// 减免金额
	private String field1;		// field1
	private String partialdealcode;		//部分还款的订单号
	
//	private String createby;		// createby
//	private Date createdate;		// createdate
//	private String updateby;		// updateby
//	private Date updatedate;		// updatedate
	
	public TMisDunnedHistory() {
		super();
	}

	public TMisDunnedHistory(String id){
		super(id);
	}

	@NotNull(message="dbid不能为空")
	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}
	
	@Length(min=1, max=255, message="催收任务Id，指向对应的催收任务长度必须介于 1 和 255 之间")
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@NotNull(message="催回金额不能为空")
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="催回时间不能为空")
	public Date getDunnedtime() {
		return dunnedtime;
	}

	public void setDunnedtime(Date dunnedtime) {
		this.dunnedtime = dunnedtime;
	}
	
//	@Length(min=1, max=1, message="是否还清长度必须介于 1 和 1 之间")
	public boolean getIspayoff() {
		return ispayoff;
	}

	public void setIspayoff(boolean ispayoff) {
		this.ispayoff = ispayoff;
	}
	
	public Integer getOverduedays() {
		return overduedays;
	}

	public void setOverduedays(Integer overduedays) {
		this.overduedays = overduedays;
	}
	
	public Integer getReliefamount() {
		return reliefamount;
	}

	public void setReliefamount(Integer reliefamount) {
		this.reliefamount = reliefamount;
	}
	
	@Length(min=0, max=128, message="field1长度必须介于 0 和 128 之间")
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	@Length(min=0, max=128, message="贷款订单号长度必须介于 0 和 128 之间")
	public String getPartialdealcode() {
		return partialdealcode;
	}

	public void setPartialdealcode(String partialdealcode) {
		this.partialdealcode = partialdealcode;
	}
	
//	@Length(min=0, max=64, message="createby长度必须介于 0 和 64 之间")
//	public String getCreateby() {
//		return createby;
//	}
//
//	public void setCreateby(String createby) {
//		this.createby = createby;
//	}
//	
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	public Date getCreatedate() {
//		return createdate;
//	}
//
//	public void setCreatedate(Date createdate) {
//		this.createdate = createdate;
//	}
//	
//	@Length(min=0, max=64, message="updateby长度必须介于 0 和 64 之间")
//	public String getUpdateby() {
//		return updateby;
//	}
//
//	public void setUpdateby(String updateby) {
//		this.updateby = updateby;
//	}
//	
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	public Date getUpdatedate() {
//		return updatedate;
//	}
//
//	public void setUpdatedate(Date updatedate) {
//		this.updatedate = updatedate;
//	}
	
}