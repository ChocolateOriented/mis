/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 委外日报表Entity
 * @author 徐盛
 * @version 2016-12-09
 */
public class SRiskOrderOutReportDay extends DataEntity<SRiskOrderOutReportDay> {
	
	private static final long serialVersionUID = 1L;
	private Date createtime;		// 按日统计
	private String dunningpeoplename;		// 催收方名称
	private Integer dunningordernum;		// 委外单数
	private BigDecimal dunningordercapitalamount;		// 委外本金
	private BigDecimal dunningorderamount;		// 委外金额
	private Integer repayordernum;		// 应催单数
	private BigDecimal repayordercapitalamount;		// 应催本金
	private BigDecimal repayorderamount;		// 应催金额
	private Integer payoffordernum;		// 催回单数
	private BigDecimal payofforderamount;		// 催回金额
	private BigDecimal basiccommission;		// 基础佣金
	
	private Date beginDatetime;		// 开始 datetime
	private Date endDatetime;		// 结束 datetime
	
	public SRiskOrderOutReportDay() {
		super();
	}

	public SRiskOrderOutReportDay(String id){
		super(id);
	}

	@ExcelField(title="日期",  type=1, align=2, sort=1)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
	@ExcelField(title="催收方名称",  type=1, align=2, sort=2)
	public String getDunningpeoplename() {
		return dunningpeoplename;
	}
	public void setDunningpeoplename(String dunningpeoplename) {
		this.dunningpeoplename = dunningpeoplename;
	}
	
	@ExcelField(title="委外单数",  type=1, align=2, sort=3)
	public Integer getDunningordernum() {
		return dunningordernum;
	}
	public void setDunningordernum(Integer dunningordernum) {
		this.dunningordernum = dunningordernum;
	}
	
	@ExcelField(title="委外本金",  type=1, align=2, sort=4)
	public BigDecimal getDunningordercapitalamount() {
		return dunningordercapitalamount;
	}
	public void setDunningordercapitalamount(BigDecimal dunningordercapitalamount) {
		this.dunningordercapitalamount = dunningordercapitalamount;
	}
	
	@ExcelField(title="委外金额",  type=1, align=2, sort=5)
	public BigDecimal getDunningorderamount() {
		return dunningorderamount;
	}
	public void setDunningorderamount(BigDecimal dunningorderamount) {
		this.dunningorderamount = dunningorderamount;
	}
	
	@ExcelField(title="应催单数",  type=1, align=2, sort=6)
	public Integer getRepayordernum() {
		return repayordernum;
	}
	public void setRepayordernum(Integer repayordernum) {
		this.repayordernum = repayordernum;
	}
	
	@ExcelField(title="应催本金",  type=1, align=2, sort=7)
	public BigDecimal getRepayordercapitalamount() {
		return repayordercapitalamount;
	}
	public void setRepayordercapitalamount(BigDecimal repayordercapitalamount) {
		this.repayordercapitalamount = repayordercapitalamount;
	}
	
	@ExcelField(title="应催金额",  type=1, align=2, sort=8)
	public BigDecimal getRepayorderamount() {
		return repayorderamount;
	}
	public void setRepayorderamount(BigDecimal repayorderamount) {
		this.repayorderamount = repayorderamount;
	}
	
	@ExcelField(title="催回单数",  type=1, align=2, sort=9)
	public Integer getPayoffordernum() {
		return payoffordernum;
	}
	public void setPayoffordernum(Integer payoffordernum) {
		this.payoffordernum = payoffordernum;
	}
	
	@ExcelField(title="催回金额",  type=1, align=2, sort=10)
	public BigDecimal getPayofforderamount() {
		return payofforderamount;
	}
	public void setPayofforderamount(BigDecimal payofforderamount) {
		this.payofforderamount = payofforderamount;
	}
	
	@ExcelField(title="基础佣金",  type=1, align=2, sort=11)
	public BigDecimal getBasiccommission() {
		return basiccommission;
	}
	public void setBasiccommission(BigDecimal basiccommission) {
		this.basiccommission = basiccommission;
	}

	public Date getBeginDatetime() {
		return beginDatetime;
	}

	public void setBeginDatetime(Date beginDatetime) {
		this.beginDatetime = beginDatetime;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}
	
	
	
}