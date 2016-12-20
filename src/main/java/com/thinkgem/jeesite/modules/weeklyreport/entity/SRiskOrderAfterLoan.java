/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 贷后风险Entity
 * @author 徐盛
 * @version 2016-06-16
 */
public class SRiskOrderAfterLoan extends DataEntity<SRiskOrderAfterLoan> {
	
	private static final long serialVersionUID = 1L;
	private Integer orderapply;		// 累计申请笔数
	private Integer orderloan;		// 累计贷款笔数
	private BigDecimal captialloan;		// 累计贷款本金
	private BigDecimal captialremit;		// 累计放款本金
	private BigDecimal capitalremitpaid;		// 累计已还放款本金
	private BigDecimal capitalremitunpaid;		// 累计未还放款本金
	private BigDecimal captialloanpaid;		// 累计已还本金
	private BigDecimal commissionamount;		// 累计贷款手续费
	private BigDecimal commissionamountpaid;		// 累计已还手续费
	private BigDecimal couponamountpaid;		// 已使用抵用券金额
	private BigDecimal capitaldelay;		// 累计延期本金
	private BigDecimal delaypaid;		// 累计已收延期费用
	private BigDecimal overdueamount;		// 累计逾期费用
	private BigDecimal overdueamountpaid;		// 累计已还逾期费用
	private BigDecimal reliefamount;		// 减免金额
	private BigDecimal receivedamount;		// 待收总额
	private BigDecimal receivedoverdueamount;		// 逾期总金额
	private BigDecimal receivedoverdueamount7;		// 逾期1-7天金额
	private BigDecimal receivedoverdueamount14;		// 逾期8-14天金额
	private BigDecimal receivedoverdueamount21;		// 逾期15-21天金额
	private BigDecimal receivedoverdueamount28;		// 逾期22-28天金额
	private BigDecimal receivedoverdueamount29;		// 逾期29-35天金额
	private BigDecimal receivedoverdueamount119;    // 逾期36-119天金额
	private BigDecimal receivedoverdueamount120;	// 逾期120天以上金额
	private Date createtime;		// createtime
	private Date beginDatetime;		// 开始 datetime
	private Date endDatetime;		// 结束 datetime
	
	private BigDecimal partialcapital; // 部分还款本金
	private BigDecimal  partialcost;	//部分还款手续费
	private BigDecimal  partialoverdue;	//部分还款延期费
	
	public BigDecimal getPartialcapital() {
		return partialcapital;
	}
	@ExcelField(title="部分还款本金",  type=1, align=2, sort=26)
	public String getPartialcapitalText() {
		return null != this.partialcapital ? NumberUtil.formatTosepara(this.partialcapital)  : "";
	}
	public void setPartialcapital(BigDecimal partialcapital) {
		this.partialcapital = partialcapital;
	}
	
	public BigDecimal getPartialcost() {
		return partialcost;
	}
	@ExcelField(title="部分还款手续费",  type=1, align=2, sort=27)
	public String getPartialcostText() {
		return null != this.partialcost ? NumberUtil.formatTosepara(this.partialcost)  : "";
	}
	public void setPartialcost(BigDecimal partialcost) {
		this.partialcost = partialcost;
	}
	
	public BigDecimal getPartialoverdue() {
		return partialoverdue;
	}
	@ExcelField(title="部分还款延期费",  type=1, align=2, sort=28)
	public String getPartialoverdueText() {
		return null != this.partialoverdue ? NumberUtil.formatTosepara(this.partialoverdue)  : "";
	}
	public void setPartialoverdue(BigDecimal partialoverdue) {
		this.partialoverdue = partialoverdue;
	}
	
	public SRiskOrderAfterLoan() {
		super();
	}

	public SRiskOrderAfterLoan(String id){
		super(id);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="日期",  type=1, align=2, sort=1)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@ExcelField(title="累计申请笔数",  type=1, align=2, sort=2)
	public Integer getOrderapply() {
		return orderapply;
	}

	public void setOrderapply(Integer orderapply) {
		this.orderapply = orderapply;
	}
	
	@ExcelField(title="累计贷款笔数",  type=1, align=2, sort=3)
	public Integer getOrderloan() {
		return orderloan;
	}

	public void setOrderloan(Integer orderloan) {
		this.orderloan = orderloan;
	}
	
	@ExcelField(title="累计贷款本金",  type=1, align=2, sort=4)
	public BigDecimal getCaptialloan() {
		return captialloan;
	}
	public void setCaptialloan(BigDecimal captialloan) {
		this.captialloan = captialloan;
	}
	
	@ExcelField(title="累计放款本金",  type=1, align=2, sort=5)
	public BigDecimal getCaptialremit() {
		return captialremit;
	}
	public void setCaptialremit(BigDecimal captialremit) {
		this.captialremit = captialremit;
	}
	
	@ExcelField(title="累计已还放款本金",  type=1, align=2, sort=6)
	public BigDecimal getCapitalremitpaid() {
		return capitalremitpaid;
	}
	public void setCapitalremitpaid(BigDecimal capitalremitpaid) {
		this.capitalremitpaid = capitalremitpaid;
	}
	
	@ExcelField(title="累计未还放款本金",  type=1, align=2, sort=7)
	public BigDecimal getCapitalremitunpaid() {
		return capitalremitunpaid;
	}
	public void setCapitalremitunpaid(BigDecimal capitalremitunpaid) {
		this.capitalremitunpaid = capitalremitunpaid;
	}
	
	@ExcelField(title="累计已还本金",  type=1, align=2, sort=8)
	public BigDecimal getCaptialloanpaid() {
		return captialloanpaid;
	}
	public void setCaptialloanpaid(BigDecimal captialloanpaid) {
		this.captialloanpaid = captialloanpaid;
	}
	
	@ExcelField(title="累计贷款手续费",  type=1, align=2, sort=9)
	public BigDecimal getCommissionamount() {
		return commissionamount;
	}
	public void setCommissionamount(BigDecimal commissionamount) {
		this.commissionamount = commissionamount;
	}
	
	@ExcelField(title="累计已还手续费",  type=1, align=2, sort=10)
	public BigDecimal getCommissionamountpaid() {
		return commissionamountpaid;
	}
	public void setCommissionamountpaid(BigDecimal commissionamountpaid) {
		this.commissionamountpaid = commissionamountpaid;
	}
	
	@ExcelField(title="已使用抵用券金额",  type=1, align=2, sort=11)
	public BigDecimal getCouponamountpaid() {
		return couponamountpaid;
	}
	public void setCouponamountpaid(BigDecimal couponamountpaid) {
		this.couponamountpaid = couponamountpaid;
	}
	
	@ExcelField(title="累计延期本金",  type=1, align=2, sort=12)
	public BigDecimal getCapitaldelay() {
		return capitaldelay;
	}
	public void setCapitaldelay(BigDecimal capitaldelay) {
		this.capitaldelay = capitaldelay;
	}
	
	@ExcelField(title="累计已收延期费用",  type=1, align=2, sort=13)
	public BigDecimal getDelaypaid() {
		return delaypaid;
	}
	public void setDelaypaid(BigDecimal delaypaid) {
		this.delaypaid = delaypaid;
	}
	
	@ExcelField(title="累计逾期费用",  type=1, align=2, sort=14)
	public BigDecimal getOverdueamount() {
		return overdueamount;
	}
	public void setOverdueamount(BigDecimal overdueamount) {
		this.overdueamount = overdueamount;
	}
	
	@ExcelField(title="累计已还逾期费用",  type=1, align=2, sort=15)
	public BigDecimal getOverdueamountpaid() {
		return overdueamountpaid;
	}
	public void setOverdueamountpaid(BigDecimal overdueamountpaid) {
		this.overdueamountpaid = overdueamountpaid;
	}
	
	@ExcelField(title="减免金额",  type=1, align=2, sort=16)
	public BigDecimal getReliefamount() {
		return reliefamount;
	}
	public void setReliefamount(BigDecimal reliefamount) {
		this.reliefamount = reliefamount;
	}
	
	@ExcelField(title="待收总额",  type=1, align=2, sort=17)
	public BigDecimal getReceivedamount() {
		return receivedamount;
	}
	public void setReceivedamount(BigDecimal receivedamount) {
		this.receivedamount = receivedamount;
	}
	
	@ExcelField(title="逾期总金额",  type=1, align=2, sort=18)
	public BigDecimal getReceivedoverdueamount() {
		return receivedoverdueamount;
	}
	public void setReceivedoverdueamount(BigDecimal receivedoverdueamount) {
		this.receivedoverdueamount = receivedoverdueamount;
	}
	
	@ExcelField(title="逾期1-7天金额",  type=1, align=2, sort=19)
	public BigDecimal getReceivedoverdueamount7() {
		return receivedoverdueamount7;
	}
	public void setReceivedoverdueamount7(BigDecimal receivedoverdueamount7) {
		this.receivedoverdueamount7 = receivedoverdueamount7;
	}
	
	@ExcelField(title="逾期8-14天金额",  type=1, align=2, sort=20)
	public BigDecimal getReceivedoverdueamount14() {
		return receivedoverdueamount14;
	}
	public void setReceivedoverdueamount14(BigDecimal receivedoverdueamount14) {
		this.receivedoverdueamount14 = receivedoverdueamount14;
	}
	
	@ExcelField(title="逾期15-21天金额",  type=1, align=2, sort=21)
	public BigDecimal getReceivedoverdueamount21() {
		return receivedoverdueamount21;
	}
	public void setReceivedoverdueamount21(BigDecimal receivedoverdueamount21) {
		this.receivedoverdueamount21 = receivedoverdueamount21;
	}
	
	@ExcelField(title="逾期22-28天金额",  type=1, align=2, sort=22)
	public BigDecimal getReceivedoverdueamount28() {
		return receivedoverdueamount28;
	}
	public void setReceivedoverdueamount28(BigDecimal receivedoverdueamount28) {
		this.receivedoverdueamount28 = receivedoverdueamount28;
	}
	
	@ExcelField(title="逾期29-35天金额",  type=1, align=2, sort=23)
	public BigDecimal getReceivedoverdueamount29() {
		return receivedoverdueamount29;
	}
	public void setReceivedoverdueamount29(BigDecimal receivedoverdueamount29) {
		this.receivedoverdueamount29 = receivedoverdueamount29;
	}
	
	@ExcelField(title="逾期36-119天金额",  type=1, align=2, sort=24)
	public BigDecimal getReceivedoverdueamount119() {
		return receivedoverdueamount119;
	}
	public void setReceivedoverdueamount119(BigDecimal receivedoverdueamount119) {
		this.receivedoverdueamount119 = receivedoverdueamount119;
	}

	@ExcelField(title="逾期120天以上金额",  type=1, align=2, sort=25)
	public BigDecimal getReceivedoverdueamount120() {
		return receivedoverdueamount120;
	}
	public void setReceivedoverdueamount120(BigDecimal receivedoverdueamount120) {
		this.receivedoverdueamount120 = receivedoverdueamount120;
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