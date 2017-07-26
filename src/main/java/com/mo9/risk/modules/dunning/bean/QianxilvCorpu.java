package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class QianxilvCorpu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Date createtime; //'统计日期',
	private BigDecimal orderduedate;
	private BigDecimal q1;	//'Q1应催本金'
	private BigDecimal q2;	//'Q2应催本金'
	private BigDecimal q3;	//'Q3应催本金'
	private BigDecimal q4;	//'Q4应催本金'
	private BigDecimal payoffq1;	//'Q1还款本金'
	private BigDecimal payoffq2;	//'Q2还款本金'
	private BigDecimal payoffq3;	//'Q3还款本金'
	private BigDecimal payoffq4;	//'Q4还款本金'
	
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public BigDecimal getOrderduedate() {
		return orderduedate;
	}
	public void setOrderduedate(BigDecimal orderduedate) {
		this.orderduedate = orderduedate;
	}
	public BigDecimal getQ1() {
		return q1;
	}
	public void setQ1(BigDecimal q1) {
		this.q1 = q1;
	}
	public BigDecimal getQ2() {
		return q2;
	}
	public void setQ2(BigDecimal q2) {
		this.q2 = q2;
	}
	public BigDecimal getQ3() {
		return q3;
	}
	public void setQ3(BigDecimal q3) {
		this.q3 = q3;
	}
	public BigDecimal getQ4() {
		return q4;
	}
	public void setQ4(BigDecimal q4) {
		this.q4 = q4;
	}
	public BigDecimal getPayoffq1() {
		return payoffq1;
	}
	public void setPayoffq1(BigDecimal payoffq1) {
		this.payoffq1 = payoffq1;
	}
	public BigDecimal getPayoffq2() {
		return payoffq2;
	}
	public void setPayoffq2(BigDecimal payoffq2) {
		this.payoffq2 = payoffq2;
	}
	public BigDecimal getPayoffq3() {
		return payoffq3;
	}
	public void setPayoffq3(BigDecimal payoffq3) {
		this.payoffq3 = payoffq3;
	}
	public BigDecimal getPayoffq4() {
		return payoffq4;
	}
	public void setPayoffq4(BigDecimal payoffq4) {
		this.payoffq4 = payoffq4;
	}
	
	
	

}
