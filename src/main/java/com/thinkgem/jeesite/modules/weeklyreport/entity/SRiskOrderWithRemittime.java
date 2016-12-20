/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 月放款订单催回率Entity
 * @author 徐盛
 * @version 2016-11-30
 */
public class SRiskOrderWithRemittime extends DataEntity<SRiskOrderWithRemittime> {
	
	private static final long serialVersionUID = 1L;
	private String month;		// 统计月份
	private BigDecimal overduerate;		// 自然逾期率
	private BigDecimal within7daysrate;		// 1-7天催回率
	private BigDecimal within14daysrate;		// 8-14天催回率
	
	private BigDecimal with14DaysRate;			// 14天催回率
	private BigDecimal with35DaysRate;			// 35天催回率
	
	private BigDecimal within21daysrate;		// 15-21天催回率
	private BigDecimal within35daysrate;		// 22-35天催回率
	private BigDecimal over36daysrate;		// 36+催回率
	private BigDecimal mo9rate;		// Mo9催回率
	private BigDecimal allrate;		// 总催回率
	private BigDecimal mo9amount;		// Mo9催回金额
	private BigDecimal allamount;		// 总催回金额
	
	public SRiskOrderWithRemittime() {
		super();
	}

	public SRiskOrderWithRemittime(String id){
		super(id);
	}

	@Length(min=0, max=10, message="统计月份长度必须介于 0 和 10 之间")
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	public BigDecimal getOverduerate() {
		return overduerate;
	}

	public void setOverduerate(BigDecimal overduerate) {
		this.overduerate = overduerate;
	}
	
	public BigDecimal getWithin7daysrate() {
		return within7daysrate;
	}

	public void setWithin7daysrate(BigDecimal within7daysrate) {
		this.within7daysrate = within7daysrate;
	}
	
	public BigDecimal getWithin14daysrate() {
		return within14daysrate;
	}

	public void setWithin14daysrate(BigDecimal within14daysrate) {
		this.within14daysrate = within14daysrate;
	}
	
	public BigDecimal getWithin21daysrate() {
		return within21daysrate;
	}

	public void setWithin21daysrate(BigDecimal within21daysrate) {
		this.within21daysrate = within21daysrate;
	}
	
	public BigDecimal getWithin35daysrate() {
		return within35daysrate;
	}

	public void setWithin35daysrate(BigDecimal within35daysrate) {
		this.within35daysrate = within35daysrate;
	}
	
	public BigDecimal getOver36daysrate() {
		return over36daysrate;
	}

	public void setOver36daysrate(BigDecimal over36daysrate) {
		this.over36daysrate = over36daysrate;
	}
	
	public BigDecimal getMo9rate() {
		return mo9rate;
	}

	public void setMo9rate(BigDecimal mo9rate) {
		this.mo9rate = mo9rate;
	}
	
	public BigDecimal getAllrate() {
		return allrate;
	}

	public void setAllrate(BigDecimal allrate) {
		this.allrate = allrate;
	}
	
	public BigDecimal getMo9amount() {
		return mo9amount;
	}

	public void setMo9amount(BigDecimal mo9amount) {
		this.mo9amount = mo9amount;
	}
	
	public BigDecimal getAllamount() {
		return allamount;
	}

	public void setAllamount(BigDecimal allamount) {
		this.allamount = allamount;
	}

	public BigDecimal getWith14DaysRate() {
		return with14DaysRate;
	}

	public void setWith14DaysRate(BigDecimal with14DaysRate) {
		this.with14DaysRate = with14DaysRate;
	}

	public BigDecimal getWith35DaysRate() {
		return with35DaysRate;
	}

	public void setWith35DaysRate(BigDecimal with35DaysRate) {
		this.with35DaysRate = with35DaysRate;
	}
	
	
	
}