/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

/**
 * 催收任务Entity
 * @author 徐盛
 * @version 2016-07-12
 */
public class TMisDunningTask extends DataEntity<TMisDunningTask> {

	/**
	 *  --------------------------催款任务状态----------------------------------------------
	 */
	public static final String STATUS_DUNNING = "dunning"; //代表催款任务正在催收中
	public static final String STATUS_EXPIRED = "expired";  //代表催款任务超出催收周期并未催回
	public static final String STATUS_END = "end"; //代表催款任务被结束，但并没有被还清
	public static final String STATUS_FINISHED = "finished"; //代表催款任务的订单在催收周期内已还清
	public static final String STATUS_TRANSFER = "transfer"; //代表催款任务在催收周期内转移给了另一个同周期催款用户


	private static final long serialVersionUID = 1L;
	private Integer dbid;		// dbid
	private String dunningpeopleid;		// 催讨人员id
	
	private String dealcode;		// 贷款订单号
	private Integer capitalamount;		// 贷款本金 单位 分
	private Date begin;		// 任务起始时间
	private Date deadline;		// 任务截至时间
	private Date end;		// 任务结束时间
	private Integer dunningperiodbegin;		// 催讨周期-逾期周期起始
	private Integer dunningperiodend;		// 催讨周期-逾期周期截至
	private Integer dunnedamount;		// 催回金额
	
	private boolean ispayoff;		// 任务所对应的订单是否还清
	
	private Integer reliefamount;		// 减免金额 单位 分
	private String beforeTask;		//上阶段流转任务id
	private String beforeDunningPeople;		//上阶段流转催讨人员id

	private Integer dunningAmounOnEnd; //结束时应催金额
	

	private String dunningtaskstatus;		// 催款任务状态 详见催款任务状态注释

	private String field1;		// field1
	private Date repaymentTime; //应该还款日期，新增字段
	private String telremark;   //电话记录
//	private String createby;		// createby
//	private Date createdate;		// createdate
//	private String updateby;		// updateby
//	private Date updatedate;		// updatedate
	
	private Integer creditamountOnEnd;  // 任务结束时应催金额
	
// ======================================new==============================================================================
	private String dunningpeoplename;		// 催收人姓名
	private String dunningcycle;		// 催收周期(队列)
	
	private Date outsourcingbegindate;		// 委外手分时间
	private Date outsourcingenddate;		// 委外截止时间
	
	private Date promisepaydate; 	// 到期还款时间
	private Date nextfollowdate; 	// 下次跟进时间
	
	public TMisDunningTask() {
		super();
	}

	public TMisDunningTask(String id){
		super(id);
	}

	@NotNull(message="dbid不能为空")
	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}
	
	public String getDunningpeopleid() {
		return dunningpeopleid;
	}

	public void setDunningpeopleid(String dunningpeopleid) {
		this.dunningpeopleid = dunningpeopleid;
	}

	@Length(min=0, max=128, message="贷款订单号长度必须介于 0 和 128 之间")
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	
	@NotNull(message="贷款本金 单位 分不能为空")
	public Integer getCapitalamount() {
		return capitalamount;
	}

	public void setCapitalamount(Integer capitalamount) {
		this.capitalamount = capitalamount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	public Integer getDunningperiodbegin() {
		return dunningperiodbegin;
	}

	public void setDunningperiodbegin(Integer dunningperiodbegin) {
		this.dunningperiodbegin = dunningperiodbegin;
	}
	
	public Integer getDunningperiodend() {
		return dunningperiodend;
	}

	public void setDunningperiodend(Integer dunningperiodend) {
		this.dunningperiodend = dunningperiodend;
	}
	
	@NotNull(message="催回金额不能为空")
	public Integer getDunnedamount() {
		return dunnedamount;
	}

	public void setDunnedamount(Integer dunnedamount) {
		this.dunnedamount = dunnedamount;
	}
	
//	@Length(min=0, max=1, message="任务所对应的订单是否还清长度必须介于 0 和 1 之间")
	public boolean  getIspayoff() {
		return ispayoff;
	}

	public void setIspayoff(boolean ispayoff) {
		this.ispayoff = ispayoff;
	}
	
	public Integer getReliefamount() {
		return reliefamount;
	}

	public void setReliefamount(Integer reliefamount) {
		this.reliefamount = reliefamount;
	}
	

	@Length(min=0, max=128, message="dunningtaskstatus长度必须介于 0 和 128 之间")
	public String getDunningtaskstatus() {
		return dunningtaskstatus;
	}

	public void setDunningtaskstatus(String dunningtaskstatus) {
		this.dunningtaskstatus = dunningtaskstatus;
	}

	@Length(min=0, max=128, message="field1长度必须介于 0 和 128 之间")
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRepaymentTime() {
		return repaymentTime;
	}

	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	public String getBeforeTask() {
		return beforeTask;
	}

	public void setBeforeTask(String beforeTask) {
		this.beforeTask = beforeTask;
	}

	public String getBeforeDunningPeople() {
		return beforeDunningPeople;
	}

	public void setBeforeDunningPeople(String beforeDunningPeople) {
		this.beforeDunningPeople = beforeDunningPeople;
	}

	public Integer getDunningAmounOnEnd() {
		return dunningAmounOnEnd;
	}

	public void setDunningAmounOnEnd(Integer dunningAmounOnEnd) {
		this.dunningAmounOnEnd = dunningAmounOnEnd;
	}

	public String getTelremark() {
		return telremark;
	}

	public void setTelremark(String telremark) {
		this.telremark = telremark;
	}
	
	public Integer getCreditamountOnEnd() {
		return creditamountOnEnd;
	}

	public void setCreditamountOnEnd(Integer creditamountOnEnd) {
		this.creditamountOnEnd = creditamountOnEnd;
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

	public Date getOutsourcingbegindate() {
		return outsourcingbegindate;
	}

	public void setOutsourcingbegindate(Date outsourcingbegindate) {
		this.outsourcingbegindate = outsourcingbegindate;
	}

	public Date getOutsourcingenddate() {
		return outsourcingenddate;
	}

	public void setOutsourcingenddate(Date outsourcingenddate) {
		this.outsourcingenddate = outsourcingenddate;
	}
	
	public Date getPromisepaydate() {
		return promisepaydate;
	}

	public void setPromisepaydate(Date promisepaydate) {
		this.promisepaydate = promisepaydate;
	}

	public Date getNextfollowdate() {
		return nextfollowdate;
	}

	public void setNextfollowdate(Date nextfollowdate) {
		this.nextfollowdate = nextfollowdate;
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

	
	//废弃方法
	/*public int getBalance(Date date)
	{
		//本金*1.1是为了加上手续费
		int days = (int)Math.ceil((date.getTime()-repaymentTime.getTime())/(24*60*60*1000));
		long overdueAmount = Math.round(this.capitalamount*1.1*days*0.01);
		return (int)((this.capitalamount * 1.1)+overdueAmount-this.reliefamount) ;
	}*/

	//废弃方法
	/**
	 *  计算订单当前应还金额
	 * @return
	 */
	/*public int getCurrentBalance() {
		Date now = new Date();
		return this.getBalance(now);
	}*/



	/**
	 *  获取当前逾期天数
	 * @return
	 */
	public int getCurrentOverdueDays()
	{
		Date now  = new Date();
		return TMisDunningTaskService.GetOverdueDay(repaymentTime);
//		return (int)((toDate(now).getTime() - toDate(repaymentTime).getTime()) / (24 * 60 * 60 * 1000));
	}

	private Date toDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}