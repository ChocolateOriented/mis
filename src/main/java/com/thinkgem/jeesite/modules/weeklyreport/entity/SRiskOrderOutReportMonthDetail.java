package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.math.BigDecimal;

public class SRiskOrderOutReportMonthDetail {

	private int id;
	private String createtime;		// 年月
	private String dunningpeoplename;		// 催收方名称
	private String dunningperiod;		//委外时段
	private Integer PayoffOrderNum;   //还款单数
	private BigDecimal PayoffOrderAmount;   //还款金额
	private BigDecimal BasicCommission;  //基础佣金
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getDunningpeoplename() {
		return dunningpeoplename;
	}
	public void setDunningpeoplename(String dunningpeoplename) {
		this.dunningpeoplename = dunningpeoplename;
	}
	public String getDunningperiod() {
		return dunningperiod;
	}
	public void setDunningperiod(String dunningperiod) {
		this.dunningperiod = dunningperiod;
	}
	public Integer getPayoffOrderNum() {
		return PayoffOrderNum;
	}
	public void setPayoffOrderNum(Integer payoffOrderNum) {
		PayoffOrderNum = payoffOrderNum;
	}
	public BigDecimal getPayoffOrderAmount() {
		return PayoffOrderAmount;
	}
	public void setPayoffOrderAmount(BigDecimal payoffOrderAmount) {
		PayoffOrderAmount = payoffOrderAmount;
	}
	public BigDecimal getBasicCommission() {
		return BasicCommission;
	}
	public void setBasicCommission(BigDecimal basicCommission) {
		BasicCommission = basicCommission;
	}
	
	
	
}
