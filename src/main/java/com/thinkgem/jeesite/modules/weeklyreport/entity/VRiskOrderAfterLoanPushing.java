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
 * 贷后催款情况报表Entity
 * @author 徐盛
 * @version 2016-06-20
 */
public class VRiskOrderAfterLoanPushing extends DataEntity<VRiskOrderAfterLoanPushing> {
	
	private static final long serialVersionUID = 1L;
	private Date datetime;		// datetime
	private Long orderpayoffover7;		// orderpayoffover7
	private Long orderover7;		// orderover7
	private Long orderpayoffover14;		// orderpayoffover14
	private Long orderover14;		// orderover14
	private Long orderpayoffover21;		// orderpayoffover21
	private Long orderover21;		// orderover21
	private Long orderpayoffover35;		// orderpayoffover35
	private Long orderover35;		// orderover35
	private Long orderpayoffover36;		// orderpayoffover36
	private Long orderover36;		// orderover36
	private BigDecimal repaymentamount;		// repaymentamount
	private Date beginDatetime;		// 开始 datetime
	private Date endDatetime;		// 结束 datetime
	
	
	// 计算回率 
	@ExcelField(title="1-7天催回率", type=1, align=2, sort=2)
	public String getOrderpayoffover7Percentage() {
		return 0 != this.orderpayoffover7 && 0 != this.orderover7 ? NumberUtil.getNumberFormatPercentage(this.orderpayoffover7,this.orderover7) : "0%";
	}
	@ExcelField(title="8-14天催回率", type=1, align=2, sort=3)
	public String getOrderpayoffover14Percentage() {
		return 0 != this.orderpayoffover14 && 0 != this.orderover14 ? NumberUtil.getNumberFormatPercentage(this.orderpayoffover14,this.orderover14) : "0%";
	}
	@ExcelField(title="15-21天催回率", type=1, align=2, sort=4)
	public String getOrderpayoffover21Percentage() {
		return 0 != this.orderpayoffover21 && 0 != this.orderover21 ? NumberUtil.getNumberFormatPercentage(this.orderover21,this.orderover21) : "0%";
	}
	@ExcelField(title="22-35天催回率", type=1, align=2, sort=5)
	public String getOrderpayoffover35Percentage() {
		return 0 != this.orderpayoffover35 && 0 != this.orderover35 ? NumberUtil.getNumberFormatPercentage(this.orderpayoffover35,this.orderover35) : "0%";
	}
	@ExcelField(title="36+天催回率", type=1, align=2, sort=6)
	public String getOrderpayoffover36Percentage() {
		return 0 != this.orderpayoffover36 && 0 != this.orderover36 ? NumberUtil.getNumberFormatPercentage(this.orderpayoffover36,this.orderover36) : "0%";
	}
	@ExcelField(title="总催回率", type=1, align=2, sort=7)
	public String getOrderpayoffoverSumPercentage() {
		Long orderpayoffoverSum = orderpayoffover7 + orderpayoffover14 + orderpayoffover21 + orderpayoffover35 + orderpayoffover36;
		Long orderoverSum = orderover7 + orderover14 + orderover21 + orderover35 + orderover36;
		return 0 != orderpayoffoverSum && 0 != orderoverSum ? NumberUtil.getNumberFormatPercentage(orderpayoffoverSum,orderoverSum) : "0%";
	}
	
	
	public VRiskOrderAfterLoanPushing() {
		super();
	}

	public VRiskOrderAfterLoanPushing(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="日期",  type=1, align=2, sort=1)
	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	public Long getOrderpayoffover7() {
		return orderpayoffover7;
	}

	public void setOrderpayoffover7(Long orderpayoffover7) {
		this.orderpayoffover7 = orderpayoffover7;
	}
	
	public Long getOrderover7() {
		return orderover7;
	}

	public void setOrderover7(Long orderover7) {
		this.orderover7 = orderover7;
	}
	
	public Long getOrderpayoffover14() {
		return orderpayoffover14;
	}

	public void setOrderpayoffover14(Long orderpayoffover14) {
		this.orderpayoffover14 = orderpayoffover14;
	}
	
	public Long getOrderover14() {
		return orderover14;
	}

	public void setOrderover14(Long orderover14) {
		this.orderover14 = orderover14;
	}
	
	public Long getOrderpayoffover21() {
		return orderpayoffover21;
	}

	public void setOrderpayoffover21(Long orderpayoffover21) {
		this.orderpayoffover21 = orderpayoffover21;
	}
	
	public Long getOrderover21() {
		return orderover21;
	}

	public void setOrderover21(Long orderover21) {
		this.orderover21 = orderover21;
	}
	
	public Long getOrderpayoffover35() {
		return orderpayoffover35;
	}

	public void setOrderpayoffover35(Long orderpayoffover35) {
		this.orderpayoffover35 = orderpayoffover35;
	}
	
	public Long getOrderover35() {
		return orderover35;
	}

	public void setOrderover35(Long orderover35) {
		this.orderover35 = orderover35;
	}
	
	public Long getOrderpayoffover36() {
		return orderpayoffover36;
	}

	public void setOrderpayoffover36(Long orderpayoffover36) {
		this.orderpayoffover36 = orderpayoffover36;
	}
	
	public Long getOrderover36() {
		return orderover36;
	}

	public void setOrderover36(Long orderover36) {
		this.orderover36 = orderover36;
	}
	
	@ExcelField(title="催回金额",type=1,  align=2, sort=8)
	public BigDecimal getRepaymentamount() {
		return repaymentamount;
	}

	public void setRepaymentamount(BigDecimal repaymentamount) {
		this.repaymentamount = repaymentamount;
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