/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.TelStatus;
import com.mo9.risk.modules.dunning.enums.DunningOverdueReason;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 电催结论记录Entity
 * @author shijlu
 * @version 2016-03-06
 */
public class TMisDunnedConclusion extends DataEntity<TMisDunnedConclusion> {

	private static final long serialVersionUID = 1L;
	private String dbid;		// dbid
	private String taskid;		// 催收任务Id，指向对应的催收任务
	private String dealcode;		// 催收订单号
	private Date repaymenttime;		//应该还款日期
	private Boolean orderstatus;		// 当前催收时的订单状态
	private Boolean iseffective;		//是否有效联络
	private DunningOverdueReason overduereason;		//逾期原因
	private String overduereasonstr;
	private TelStatus resultcode;		//结果代码
	private String resultcodestr;
	private Date promisepaydate;		//承诺还款日
	private Date nextfollowdate;		//下次跟进日期
	private String remark;		//备注
	private String dunningpeopleid;		//催收人员id
	private Date dunningtime;		//催收动作发生时间
	private String dunningpeoplename;  //催收人员
	private String dunningcycle;		// 催收周期(队列)
	private List<String> actions;		//关联的actionId
	
	private Integer buyerid;
    
	public TMisDunnedConclusion() {
		super();
	}

	public TMisDunnedConclusion(String id){
		super(id);
	}

	@Length(min=1, max=11, message="dbid长度必须介于 1 和 11 之间")
	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	
	@Length(min=1, max=255, message="催收任务Id，指向对应的催收任务长度必须介于 1 和 255 之间")
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	@Length(min=1, max=64, message="催收订单号长度必须介于 1 和 64 之间")
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public Date getRepaymenttime() {
		return repaymenttime;
	}

	public void setRepaymenttime(Date repaymenttime) {
		this.repaymenttime = repaymenttime;
	}

	public Boolean getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(Boolean orderstatus) {
		this.orderstatus = orderstatus;
	}

	public Boolean getIseffective() {
		return iseffective;
	}

	public void setIseffective(Boolean iseffective) {
		this.iseffective = iseffective;
	}

	public DunningOverdueReason getOverduereason() {
		return overduereason;
	}

	public void setOverduereason(DunningOverdueReason overduereason) {
		if (overduereason == null) {
			return;
		}
		this.overduereason = overduereason;
		this.overduereasonstr = overduereason.getDesc();
	}

	public String getOverduereasonstr() {
		return overduereasonstr;
	}

	public void setOverduereasonstr(String overduereasonstr) {
		this.overduereasonstr = overduereasonstr;
	}

	public TelStatus getResultcode() {
		return resultcode;
	}

	public void setResultcode(TelStatus resultcode) {
		this.resultcode = resultcode;
		this.resultcodestr = resultcode.getDesc();
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

	public String getResultcodestr() {
		return resultcodestr;
	}

	public void setResultcodestr(String resultcodestr) {
		this.resultcodestr = resultcodestr;
	}

	@Length(min=0, max=1000, message="备注长度必须介于 0 和 1000 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDunningtime() {
		return dunningtime;
	}

	public void setDunningtime(Date dunningtime) {
		this.dunningtime = dunningtime;
	}

	@Length(min=0, max=128, message="dunningpeopleid长度必须介于 0 和 128 之间")
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

	public Integer getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(Integer buyerid) {
		this.buyerid = buyerid;
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}

}