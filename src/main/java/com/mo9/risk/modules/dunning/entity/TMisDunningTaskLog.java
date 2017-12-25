/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 催收任务logEntity
 * @author 徐盛
 * @version 2017-03-01
 */
public class TMisDunningTaskLog extends DataEntity<TMisDunningTaskLog> {
	
	private static final long serialVersionUID = 1L;
	private Integer dbid;		// dbid
	private String dealcode;		// 订单号
	private String dunningpeopleid;		// 催收人id
	private String dunningpeoplename;		// 催收人姓名
	private String dunningcycle;		// 催收周期(队列)
		private String behaviorstatus;		// 行为状态
	private String dealcodestatus;		// 订单状态
	private Date payofftime;		// 还清时间
	private Integer days;		// 借款时长
	private String realname;		// 用户姓名
	private String mobile;		// 手机号
	private Date repaymenttime;		// 到期还款日期
	private Integer overduedays;		// 逾期天数
	private Integer corpusamount;		// 本金
	private Integer costamout;		// 手续费
	private Integer creditamount;		// 实际应还金额 (当前应催金额)
	private Integer overdueamount;		// 逾期费
	private Integer modifyamount;		// 催收减免金额
		private Integer delayamount;		// 延期费
		private Integer delaydays;		// 延期天数
//	private String createby;		// 创建人
//	private Date createdate;		// 创建时间
//	private String updateby;		// 修改人
//	private Date updatedate;		// 修改时间
	
	private String taskid;	 // 任务ID
	
	private String platformext; // 渠道
//	private String debtbiztype; // 产品名


	public TMisDunningTaskLog() {
		super();
	}

	public TMisDunningTaskLog(String id){
		super(id);
	}
	
	public TMisDunningTaskLog(String dealcode,String dunningpeopleid,String dunningpeoplename,String dunningcycle,String behaviorstatus){
		this.dealcode = dealcode;
		this.dunningpeopleid = dunningpeopleid;
		this.dunningpeoplename = dunningpeoplename;
		this.dunningcycle = dunningcycle;
		this.behaviorstatus = behaviorstatus;
//		this.debtbiztype = debtbiztype;
	}

	@NotNull(message="dbid不能为空")
	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}
	
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	
	public String getDunningpeopleid() {
		return dunningpeopleid;
	}

	public void setDunningpeopleid(String dunningpeopleid) {
		this.dunningpeopleid = dunningpeopleid;
	}
	
	public String getDunningpeoplename() {
		return dunningpeoplename;
	}

	public void setDunningpeoplename(String dunningpeoplename) {
		this.dunningpeoplename = dunningpeoplename;
	}
	
	public String getDunningcycle() {
		return dunningcycle;
	}

	public void setDunningcycle(String dunningcycle) {
		this.dunningcycle = dunningcycle;
	}
	
	public String getBehaviorstatus() {
		return behaviorstatus;
	}

	public void setBehaviorstatus(String behaviorstatus) {
		this.behaviorstatus = behaviorstatus;
	}
	
	public String getDealcodestatus() {
		return dealcodestatus;
	}

	public void setDealcodestatus(String dealcodestatus) {
		this.dealcodestatus = dealcodestatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPayofftime() {
		return payofftime;
	}

	public void setPayofftime(Date payofftime) {
		this.payofftime = payofftime;
	}
	
	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
	
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRepaymenttime() {
		return repaymenttime;
	}

	public void setRepaymenttime(Date repaymenttime) {
		this.repaymenttime = repaymenttime;
	}
	
	public Integer getOverduedays() {
		return overduedays;
	}

	public void setOverduedays(Integer overduedays) {
		this.overduedays = overduedays;
	}
	
	public Integer getCorpusamount() {
		return corpusamount;
	}

	public void setCorpusamount(Integer corpusamount) {
		this.corpusamount = corpusamount;
	}
	

	
	public Integer getDelayamount() {
		return delayamount;
	}

	public void setDelayamount(Integer delayamount) {
		this.delayamount = delayamount;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getDelaydays() {
		return delaydays;
	}

	public void setDelaydays(Integer delaydays) {
		this.delaydays = delaydays;
	}

	public Integer getCostamout() {
		return costamout;
	}

	public void setCostamout(Integer costamout) {
		this.costamout = costamout;
	}

	public Integer getCreditamount() {
		return creditamount;
	}

	public void setCreditamount(Integer creditamount) {
		this.creditamount = creditamount;
	}

	public Integer getOverdueamount() {
		return overdueamount;
	}

	public void setOverdueamount(Integer overdueamount) {
		this.overdueamount = overdueamount;
	}

	public Integer getModifyamount() {
		return modifyamount;
	}

	public void setModifyamount(Integer modifyamount) {
		this.modifyamount = modifyamount;
	}

	public String getPlatformext() {
		return platformext;
	}

	public void setPlatformext(String platformext) {
		this.platformext = platformext;
	}

//	public String getDebtbiztype() {
//		return debtbiztype;
//	}
//
//	public void setDebtbiztype(String debtbiztype) {
//		this.debtbiztype = debtbiztype;
//	}

	

}