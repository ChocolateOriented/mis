/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 贷后还款情况Entity
 * @author 徐盛
 * @version 2016-06-20
 */
public class VRiskOrderAfterLoan extends DataEntity<VRiskOrderAfterLoan> {
	
	private static final long serialVersionUID = 1L;
	private Date datetime;		// 日期
	
	private Long orderexpirenum;		// 到期订单数
	private Long orderexpireamount;		// 应收金额
	
	private Long orderexpirebeforenum;		// 提前还款单数
	private Long orderexpirebeforeamount;		// 提前还款金额
	
	private Long orderexpireontimenum;		// 按时还款单数
	private Long orderexpireontimeamount;		// 按时还款金额
	
	private Long orderexpireoverduenum;		// 逾期还款单数
	private Long orderexpireoverdueamount;		// 逾期还款金额
	
	private Long orderexpirenorepay;  // 待还清
	
	private Long orderremitnum;		// 还款订单数
	private Long orderincomeamount;		// 还款收益
	
	private Long orderremitbeforenum;		// 提前还款单数
	private Long orderremitontimenum;		// 按时还款单数
	private Long orderremitoverduenum;		// 逾期还款单数
	
	private Long OrderUnrepayNum; // 未還訂單數
	
	private Date beginDatetime;		// 开始 datetime
	private Date endDatetime;		// 结束 datetime
	
	@ExcelField(title="提前还款占比",  type=1, align=2, sort=6)
	public String getOrderexpirebeforenumPercentage() {
		return 0 != this.orderexpirebeforenum && 0 != this.orderexpirenum ? NumberUtil.getNumberFormatPercentage(this.orderexpirebeforenum,this.orderexpirenum) : "0%";
	}
	@ExcelField(title="按时还款占比",  type=1, align=2, sort=9)
	public String getOrderexpireontimenumPercentage() {
		return 0 != this.orderexpireontimenum && 0 != this.orderexpirenum ? NumberUtil.getNumberFormatPercentage(this.orderexpireontimenum,this.orderexpirenum) : "0%";
	}
	@ExcelField(title="逾期还款占比",  type=1, align=2, sort=12)
	public String getOrderexpireoverduenumPercentage() {
		return 0 != this.orderexpireoverduenum && 0 != this.orderexpirenum ? NumberUtil.getNumberFormatPercentage(this.orderexpireoverduenum,this.orderexpirenum) : "0%";
	}
	
	@ExcelField(title="提前还款占比",  type=1, align=2, sort=16)
	public String getOrderremitbeforenumPercentage() {
		return 0 != this.orderremitbeforenum && 0 != this.orderremitnum ? NumberUtil.getNumberFormatPercentage(this.orderremitbeforenum,this.orderremitnum) : "0%";
	}
	@ExcelField(title="按时还款占比",  type=1, align=2, sort=18)
	public String getOrderremitontimenumPercentage() {
		return 0 != this.orderremitontimenum && 0 != this.orderremitnum ? NumberUtil.getNumberFormatPercentage(this.orderremitontimenum,this.orderremitnum) : "0%";
	}
	@ExcelField(title="逾期还款占比",  type=1, align=2, sort=20)
	public String getOrderremitoverduenumPercentage() {
		return 0 != this.orderremitoverduenum && 0 != this.orderremitnum ? NumberUtil.getNumberFormatPercentage(this.orderremitoverduenum,this.orderremitnum) : "0%";
	}
	
	public VRiskOrderAfterLoan() {
		super();
	}

	public VRiskOrderAfterLoan(String id){
		super(id);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="日期", type=1, align=2, sort=1)
	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	@NotNull(message="orderexpirenum不能为空")
	@ExcelField(title="到期订单数",  type=1, align=2, sort=2)
	public Long getOrderexpirenum() {
		return orderexpirenum;
	}

	public void setOrderexpirenum(Long orderexpirenum) {
		this.orderexpirenum = orderexpirenum;
	}
	
	@NotNull(message="orderexpireamount不能为空")
	@ExcelField(title="应收金额",  type=1, align=2, sort=3)
	public Long getOrderexpireamount() {
		return orderexpireamount;
	}

	public void setOrderexpireamount(Long orderexpireamount) {
		this.orderexpireamount = orderexpireamount;
	}
	
	@NotNull(message="orderexpirebeforenum不能为空")
	@ExcelField(title="提前还款单数",  type=1,  align=2, sort=4)
	public Long getOrderexpirebeforenum() {
		return orderexpirebeforenum;
	}

	public void setOrderexpirebeforenum(Long orderexpirebeforenum) {
		this.orderexpirebeforenum = orderexpirebeforenum;
	}
	
	@NotNull(message="orderexpirebeforeamount不能为空")
	@ExcelField(title="提前还款金额",  type=1,  align=2, sort=5)
	public Long getOrderexpirebeforeamount() {
		return orderexpirebeforeamount;
	}

	public void setOrderexpirebeforeamount(Long orderexpirebeforeamount) {
		this.orderexpirebeforeamount = orderexpirebeforeamount;
	}
	
	@NotNull(message="orderexpireontimenum不能为空")
	@ExcelField(title="按时还款单数",  type=1,  align=2, sort=7)
	public Long getOrderexpireontimenum() {
		return orderexpireontimenum;
	}

	public void setOrderexpireontimenum(Long orderexpireontimenum) {
		this.orderexpireontimenum = orderexpireontimenum;
	}
	
	@NotNull(message="orderexpireontimeamount不能为空")
	@ExcelField(title="按时还款金额",  type=1,  align=2, sort=8)
	public Long getOrderexpireontimeamount() {
		return orderexpireontimeamount;
	}

	public void setOrderexpireontimeamount(Long orderexpireontimeamount) {
		this.orderexpireontimeamount = orderexpireontimeamount;
	}
	
	@NotNull(message="orderexpireoverduenum不能为空")
	@ExcelField(title="逾期还款单数",  type=1,  align=2, sort=10)
	public Long getOrderexpireoverduenum() {
		return orderexpireoverduenum;
	}

	public void setOrderexpireoverduenum(Long orderexpireoverduenum) {
		this.orderexpireoverduenum = orderexpireoverduenum;
	}
	
	@NotNull(message="orderexpireoverdueamount不能为空")
	@ExcelField(title="逾期还款金额",  type=1,  align=2, sort=11)
	public Long getOrderexpireoverdueamount() {
		return orderexpireoverdueamount;
	}

	public void setOrderexpireoverdueamount(Long orderexpireoverdueamount) {
		this.orderexpireoverdueamount = orderexpireoverdueamount;
	}
	
	public Long getOrderexpirenorepay() {
		return orderexpirenorepay;
	}
	public void setOrderexpirenorepay(Long orderexpirenorepay) {
		this.orderexpirenorepay = orderexpirenorepay;
	}
	
	@NotNull(message="orderremitnum不能为空")
	@ExcelField(title="还款订单数",  type=1,  align=2, sort=13)
	public Long getOrderremitnum() {
		return orderremitnum;
	}

	public void setOrderremitnum(Long orderremitnum) {
		this.orderremitnum = orderremitnum;
	}
	
	@NotNull(message="orderincomeamount不能为空")
	@ExcelField(title="还款收益",   type=1, align=2, sort=14)
	public Long getOrderincomeamount() {
		return orderincomeamount;
	}

	public void setOrderincomeamount(Long orderincomeamount) {
		this.orderincomeamount = orderincomeamount;
	}
	
	@NotNull(message="orderremitbeforenum不能为空")
	@ExcelField(title="提前还款单数",  type=1,  align=2, sort=15)
	public Long getOrderremitbeforenum() {
		return orderremitbeforenum;
	}

	public void setOrderremitbeforenum(Long orderremitbeforenum) {
		this.orderremitbeforenum = orderremitbeforenum;
	}
	
	@NotNull(message="orderremitontimenum不能为空")
	@ExcelField(title="按时还款单数",   type=1, align=2, sort=17)
	public Long getOrderremitontimenum() {
		return orderremitontimenum;
	}

	public void setOrderremitontimenum(Long orderremitontimenum) {
		this.orderremitontimenum = orderremitontimenum;
	}
	
	@NotNull(message="orderremitoverduenum不能为空")
	@ExcelField(title="逾期还款单数",  type=1,  align=2, sort=19)
	public Long getOrderremitoverduenum() {
		return orderremitoverduenum;
	}

	public void setOrderremitoverduenum(Long orderremitoverduenum) {
		this.orderremitoverduenum = orderremitoverduenum;
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
	
	@ExcelField(title="未還訂單數",  type=1, align=2, sort=21)
	public Long getOrderUnrepayNum() {
		return OrderUnrepayNum;
	}
	public void setOrderUnrepayNum(Long orderUnrepayNum) {
		OrderUnrepayNum = orderUnrepayNum;
	}
	
	
	
}