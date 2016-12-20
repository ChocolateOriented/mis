/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 催收月绩效Entity
 * @author 徐盛
 * @version 2016-11-30
 */
public class SMisDunningTaskMonthReport extends DataEntity<SMisDunningTaskMonthReport> {
	
	private static final long serialVersionUID = 1L;
	private String month;		// 月份
	private String dunningpeoplename;		// 催收人姓名
	private String dunningperiod;		// 催收周期
	private Integer dunningtasknum;		// 催收任务数
	private Integer dunningtaskfinished;		// 完成催收任务数
	private Double dunningtaskrate;		// 催回率
	private Integer dunningtelnum;		// 电话数
	private Integer dunningsmsnum;		// 短信数
	private BigDecimal dunningrepaymentamount;		// 应催金额
	private BigDecimal dunningpayoffamount;		// 催回金额
	private BigDecimal dunningpayoffcapital;		// 催回本金
	private BigDecimal dunningprofitamount;		// 催回利润
	
	private Date datetime; 
	
	public SMisDunningTaskMonthReport() {
		super();
	}

	public SMisDunningTaskMonthReport(String id){
		super(id);
	}

	@Length(min=0, max=10, message="月份长度必须介于 0 和 10 之间")
	@ExcelField(title="日期", type=1, align=2, sort=1)
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	@Length(min=0, max=20, message="催收人姓名长度必须介于 0 和 20 之间")
	@ExcelField(title="催收人姓名", type=1, align=2, sort=2)
	public String getDunningpeoplename() {
		return dunningpeoplename;
	}
	public void setDunningpeoplename(String dunningpeoplename) {
		this.dunningpeoplename = dunningpeoplename;
	}
	
	@Length(min=0, max=20, message="催收周期长度必须介于 0 和 20 之间")
	@ExcelField(title="催收周期", type=1, align=2, sort=3)
	public String getDunningperiod() {
		return dunningperiod;
	}
	public void setDunningperiod(String dunningperiod) {
		this.dunningperiod = dunningperiod;
	}
	
	@ExcelField(title="催收任务数", type=1, align=2, sort=4)
	public Integer getDunningtasknum() {
		return dunningtasknum;
	}
	public void setDunningtasknum(Integer dunningtasknum) {
		this.dunningtasknum = dunningtasknum;
	}
	
	@ExcelField(title="完成催收任务数", type=1, align=2, sort=5)
	public Integer getDunningtaskfinished() {
		return dunningtaskfinished;
	}
	public void setDunningtaskfinished(Integer dunningtaskfinished) {
		this.dunningtaskfinished = dunningtaskfinished;
	}
	
	@ExcelField(title="催回率", type=1, align=2, sort=6)
	public Double getDunningtaskrate() {
		return dunningtaskrate;
	}
	public String getDunningtaskrateText() {
		return null != this.dunningtaskrate ? NumberUtil.formatTosepara(this.dunningtaskrate) + "%" : "";
	}
	public void setDunningtaskrate(Double dunningtaskrate) {
		this.dunningtaskrate = dunningtaskrate;
	}
	
	@ExcelField(title="电话数", type=1, align=2, sort=7)
	public Integer getDunningtelnum() {
		return dunningtelnum;
	}
	public void setDunningtelnum(Integer dunningtelnum) {
		this.dunningtelnum = dunningtelnum;
	}
	
	@ExcelField(title="短信数", type=1, align=2, sort=8)
	public Integer getDunningsmsnum() {
		return dunningsmsnum;
	}
	public void setDunningsmsnum(Integer dunningsmsnum) {
		this.dunningsmsnum = dunningsmsnum;
	}
	
	@ExcelField(title="应催金额", type=1, align=2, sort=9)
	public BigDecimal getDunningrepaymentamount() {
		return dunningrepaymentamount;
	}
	public void setDunningrepaymentamount(BigDecimal dunningrepaymentamount) {
		this.dunningrepaymentamount = dunningrepaymentamount;
	}
	
	@ExcelField(title="催回金额", type=1, align=2, sort=10)
	public BigDecimal getDunningpayoffamount() {
		return dunningpayoffamount;
	}
	public void setDunningpayoffamount(BigDecimal dunningpayoffamount) {
		this.dunningpayoffamount = dunningpayoffamount;
	}
	
	@ExcelField(title="催回本金", type=1, align=2, sort=11)
	public BigDecimal getDunningpayoffcapital() {
		return dunningpayoffcapital;
	}
	public void setDunningpayoffcapital(BigDecimal dunningpayoffcapital) {
		this.dunningpayoffcapital = dunningpayoffcapital;
	}
	
	@ExcelField(title="催回利润", type=1, align=2, sort=12)
	public BigDecimal getDunningprofitamount() {
		return dunningprofitamount;
	}
	public void setDunningprofitamount(BigDecimal dunningprofitamount) {
		this.dunningprofitamount = dunningprofitamount;
	}

	
	
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
}