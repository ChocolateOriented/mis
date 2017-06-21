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
	private String months;		// 月份
	private String monthdesc;	// 
	private String name;		// 催收人姓名
	private String dunningcycle;   // 催收员队列
	private String taskdunningcycle; // 案件队列
	private Integer dunningordernumber;//应催订单数
	private Integer finishedordernumber;//催回订单数
	private BigDecimal dunningcorpusamount;//应催本金
	private BigDecimal finishedcorpusamount;//催回本金
	private BigDecimal finishedAndDelayAmount;//催回本金+延期收益
	private BigDecimal amount;//应催金额
	private BigDecimal creditamount;//催回金额
	private BigDecimal finishedanddelaycorpusamount; //催回本金（含续期）
	private Date datetime; 
	private Integer unfinishedtask;//未还款任务数
	private BigDecimal unfinishedcorpusamount;//未还款本金
	
	public SMisDunningTaskMonthReport() {
		super();
	}

	public SMisDunningTaskMonthReport(String id){
		super(id);
	}

	@ExcelField(title="月份", type=1, align=2, sort=1)
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	
	@ExcelField(title="月份", type=1, align=2, sort=2)
	public String getMonthdesc() {
		return monthdesc;
	}
	public void setMonthdesc(String monthdesc) {
		this.monthdesc = monthdesc;
	}
	
	@ExcelField(title="催收人姓名", type=1, align=2, sort=3)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="催收员队列", type=1, align=2, sort=4)
	public String getDunningcycle() {
		return dunningcycle;
	}
	public void setDunningcycle(String dunningcycle) {
		this.dunningcycle = dunningcycle;
	}
	
	@ExcelField(title="案件队列", type=1, align=2, sort=5)
	public String getTaskdunningcycle() {
		return taskdunningcycle;
	}
	public void setTaskdunningcycle(String taskdunningcycle) {
		this.taskdunningcycle = taskdunningcycle;
	}

	@ExcelField(title="应催订单数", type=1, align=2, sort=6)
	public Integer getDunningordernumber() {
		return dunningordernumber;
	}
	public void setDunningordernumber(Integer dunningordernumber) {
		this.dunningordernumber = dunningordernumber;
	}
	
	@ExcelField(title="Q0剩余应催订单数", type=1, align=2, sort=7)
	public Integer getUnfinishedtask() {
		return unfinishedtask;
	}
	public void setUnfinishedtask(Integer unfinishedtask) {
		this.unfinishedtask = unfinishedtask;
	}

	@ExcelField(title="催回订单数", type=1, align=2, sort=8)
	public Integer getFinishedordernumber() {
		return finishedordernumber;
	}
	public void setFinishedordernumber(Integer finishedordernumber) {
		this.finishedordernumber = finishedordernumber;
	}

	@ExcelField(title="应催本金", type=1, align=2, sort=9)
	public BigDecimal getDunningcorpusamount() {
		return dunningcorpusamount;
	}
	public void setDunningcorpusamount(BigDecimal dunningcorpusamount) {
		this.dunningcorpusamount = dunningcorpusamount;
	}

	@ExcelField(title="Q0剩余应催本金", type=1, align=2, sort=10)
	public BigDecimal getUnfinishedcorpusamount() {
		return unfinishedcorpusamount;
	}

	public void setUnfinishedcorpusamount(BigDecimal unfinishedcorpusamount) {
		this.unfinishedcorpusamount = unfinishedcorpusamount;
	}

	@ExcelField(title="催回本金", type=1, align=2, sort=11)
	public BigDecimal getFinishedcorpusamount() {
		return finishedcorpusamount;
	}
	public void setFinishedcorpusamount(BigDecimal finishedcorpusamount) {
		this.finishedcorpusamount = finishedcorpusamount;
	}
	
	@ExcelField(title="催回本金+延期收益", type=1, align=2, sort=12)
	public BigDecimal getFinishedAndDelayAmount() {
		return finishedAndDelayAmount;
	}

	public void setFinishedAndDelayAmount(BigDecimal finishedAndDelayAmount) {
		this.finishedAndDelayAmount = finishedAndDelayAmount;
	}

	@ExcelField(title="应催金额", type=1, align=2, sort=13)
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@ExcelField(title="催回金额", type=1, align=2, sort=14)
	public BigDecimal getCreditamount() {
		return creditamount;
	}
	public void setCreditamount(BigDecimal creditamount) {
		this.creditamount = creditamount;
	}
	
	@ExcelField(title="催回本金(含续期)", type=1, align=2, sort=15)
	public BigDecimal getFinishedanddelaycorpusamount() {
		return finishedanddelaycorpusamount;
	}
	public void setFinishedanddelaycorpusamount(
			BigDecimal finishedanddelaycorpusamount) {
		this.finishedanddelaycorpusamount = finishedanddelaycorpusamount;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

}