package com.thinkgem.jeesite.modules.weeklyreport.bean;

import java.math.BigDecimal;

import com.thinkgem.jeesite.util.NumberUtil;

public class SCashLoanDailyReportOrderWeekBean {
	
	private String week;		// week
	private String weekdesc;		// weekdesc
	
	// 本期累计
	private BigDecimal remitorderamount;		// remitorderamount
	private BigDecimal remitorderamountpercent;		// remitorderamountpercent
	private BigDecimal delayedorderamount;		// delayedorderamount
	private BigDecimal delayedorderamountpercent;		// delayedorderamountpercent
	
	// 新增交易用户 b-
	private BigDecimal newuserapp;		// newuserapp
	private BigDecimal newuserapppercent;		// newuserapppercent
	private BigDecimal newuserwechat;		// newuserwechat
	private BigDecimal newuserwechatpercent;		// newuserwechatpercent
	
	//放贷笔数
//	private BigDecimal remitorder;		// remitorder
//	private BigDecimal remitorderpercent;		// remitorderpercent
//	private BigDecimal delayedorder;		// delayedorder
//	private BigDecimal delayedorderpercent;		// delayedorderpercent
	private BigDecimal NewUserNewOrders;
	private BigDecimal NewUserNewOrdersPercent;
	private BigDecimal OldUserNewOrders;
	private BigDecimal OldUserNewOrdersPercent;
	private BigDecimal OldUserDelayOrders;
	private BigDecimal OldUserDelayOrdersPercent;
	private BigDecimal Order7DaysPercent500;
	private BigDecimal Order7DaysPercent1000;
	private BigDecimal Order7DaysPercent1500;
	private BigDecimal Order14DaysPercent500;
	private BigDecimal Order14DaysPercent1000;
	private BigDecimal Order14DaysPercent1500;
	
	//本期收入
	private BigDecimal costamount;		// costamount
	private BigDecimal costamountpercent;		// costamountpercent
	private BigDecimal delayedamount;		// delayedamount
	private BigDecimal delayedamountpercent;		// delayedamountpercent
	private BigDecimal overdueamount;		// overdueamount
	private BigDecimal overdueamountpercent;		// overdueamountpercent
	
	// 减免
	private BigDecimal couponamount;		// couponamount
//	private BigDecimal couponamountpercent;		// couponamountpercent
	private BigDecimal pressamount;		// pressamount
//	private BigDecimal pressamountpercent;		// pressamountpercent
	private BigDecimal waitingamount;		// waitingamount
//	private BigDecimal waitingamountpercent;		// waitingamountpercent
	
	
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getWeekdesc() {
		return weekdesc;
	}
	public void setWeekdesc(String weekdesc) {
		this.weekdesc = weekdesc;
	}
	
	public BigDecimal getRemitorderamount() {
		return remitorderamount;
	}
	public String getRemitorderamountText() {
		return null != this.remitorderamount ? NumberUtil.formatTosepara(this.remitorderamount) : "";
	}
	public void setRemitorderamount(BigDecimal remitorderamount) {
		this.remitorderamount = remitorderamount;
	}
	
	public BigDecimal getRemitorderamountpercent() {
		return remitorderamountpercent;
	}
	public String getRemitorderamountpercentText() {
		return null != this.remitorderamountpercent ? NumberUtil.formatTosepara(this.remitorderamountpercent) + "%": "";
	}
	public void setRemitorderamountpercent(BigDecimal remitorderamountpercent) {
		this.remitorderamountpercent = remitorderamountpercent;
	}
	
	public BigDecimal getDelayedorderamount() {
		return delayedorderamount;
	}
	public String getDelayedorderamountText() {
		return null != this.delayedorderamount ? NumberUtil.formatTosepara(this.delayedorderamount) : "";
	}
	public void setDelayedorderamount(BigDecimal delayedorderamount) {
		this.delayedorderamount = delayedorderamount;
	}
	
	public BigDecimal getDelayedorderamountpercent() {
		return delayedorderamountpercent;
	}
	public String getDelayedorderamountpercentText() {
		return null != this.delayedorderamountpercent ? NumberUtil.formatTosepara(this.delayedorderamountpercent) + "%": "";
	}
	public void setDelayedorderamountpercent(BigDecimal delayedorderamountpercent) {
		this.delayedorderamountpercent = delayedorderamountpercent;
	}
	
	public BigDecimal getNewuserapp() {
		return newuserapp;
	}
	public String getNewuserappText() {
		return null != this.newuserapp ? NumberUtil.formatTosepara(this.newuserapp) : "";
	}
	public void setNewuserapp(BigDecimal newuserapp) {
		this.newuserapp = newuserapp;
	}
	
	public BigDecimal getNewuserapppercent() {
		return newuserapppercent;
	}
	public String getNewuserapppercentText() {
		return null != this.newuserapppercent ? NumberUtil.formatTosepara(this.newuserapppercent) + "%": "";
	}
	public void setNewuserapppercent(BigDecimal newuserapppercent) {
		this.newuserapppercent = newuserapppercent;
	}
	
	public BigDecimal getNewuserwechat() {
		return newuserwechat;
	}
	public String getNewuserwechatText() {
		return null != this.newuserwechat ? NumberUtil.formatTosepara(this.newuserwechat) : "";
	}
	public void setNewuserwechat(BigDecimal newuserwechat) {
		this.newuserwechat = newuserwechat;
	}
	
	public BigDecimal getNewuserwechatpercent() {
		return newuserwechatpercent;
	}
	public String getNewuserwechatpercentText() {
		return null != this.newuserwechatpercent ? NumberUtil.formatTosepara(this.newuserwechatpercent) + "%": "";
	}
	public void setNewuserwechatpercent(BigDecimal newuserwechatpercent) {
		this.newuserwechatpercent = newuserwechatpercent;
	}
	
//	public BigDecimal getRemitorder() {
//		return remitorder;
//	}
//	public String getRemitorderText() {
//		return null != this.remitorder ? NumberUtil.formatTosepara(this.remitorder) : "";
//	}
//	public void setRemitorder(BigDecimal remitorder) {
//		this.remitorder = remitorder;
//	}
//	
//	public BigDecimal getRemitorderpercent() {
//		return remitorderpercent;
//	}
//	public String getRemitorderpercentText() {
//		return null != this.remitorderpercent ? NumberUtil.formatTosepara(this.remitorderpercent) + "%": "";
//	}
//	public void setRemitorderpercent(BigDecimal remitorderpercent) {
//		this.remitorderpercent = remitorderpercent;
//	}
//	
//	public BigDecimal getDelayedorder() {
//		return delayedorder;
//	}
//	public String getDelayedorderText() {
//		return null != this.delayedorder ? NumberUtil.formatTosepara(this.delayedorder) : "";
//	}
//	public void setDelayedorder(BigDecimal delayedorder) {
//		this.delayedorder = delayedorder;
//	}
//	
//	public BigDecimal getDelayedorderpercent() {
//		return delayedorderpercent;
//	}
//	public String getDelayedorderpercentText() {
//		return null != this.delayedorderpercent ? NumberUtil.formatTosepara(this.delayedorderpercent) + "%": "";
//	}
//	public void setDelayedorderpercent(BigDecimal delayedorderpercent) {
//		this.delayedorderpercent = delayedorderpercent;
//	}
	
	public BigDecimal getCostamount() {
		return costamount;
	}
	public String getCostamountText() {
		return null != this.costamount ? NumberUtil.formatTosepara(this.costamount) : "";
	}
	public void setCostamount(BigDecimal costamount) {
		this.costamount = costamount;
	}
	
	public BigDecimal getCostamountpercent() {
		return costamountpercent;
	}
	public String getCostamountpercentText() {
		return null != this.costamountpercent ? NumberUtil.formatTosepara(this.costamountpercent) + "%": "";
	}
	public void setCostamountpercent(BigDecimal costamountpercent) {
		this.costamountpercent = costamountpercent;
	}
	
	public BigDecimal getDelayedamount() {
		return delayedamount;
	}
	public String getDelayedamountText() {
		return null != this.delayedamount ? NumberUtil.formatTosepara(this.delayedamount) : "";
	}
	public void setDelayedamount(BigDecimal delayedamount) {
		this.delayedamount = delayedamount;
	}
	
	public BigDecimal getDelayedamountpercent() {
		return delayedamountpercent;
	}
	public String getDelayedamountpercentText() {
		return null != this.delayedamountpercent ? NumberUtil.formatTosepara(this.delayedamountpercent) + "%": "";
	}
	public void setDelayedamountpercent(BigDecimal delayedamountpercent) {
		this.delayedamountpercent = delayedamountpercent;
	}
	
	public BigDecimal getOverdueamount() {
		return overdueamount;
	}
	public String getOverdueamountText() {
		return null != this.overdueamount ? NumberUtil.formatTosepara(this.overdueamount) : "";
	}
	public void setOverdueamount(BigDecimal overdueamount) {
		this.overdueamount = overdueamount;
	}
	
	public BigDecimal getOverdueamountpercent() {
		return overdueamountpercent;
	}
	public String getOverdueamountpercentText() {
		return null != this.overdueamountpercent ? NumberUtil.formatTosepara(this.overdueamountpercent) + "%": "";
	}
	public void setOverdueamountpercent(BigDecimal overdueamountpercent) {
		this.overdueamountpercent = overdueamountpercent;
	}
	
	public BigDecimal getCouponamount() {
		return couponamount;
	}
	public String getCouponamountText() {
		return null != this.couponamount ? NumberUtil.formatTosepara(this.couponamount) : "";
	}
	public void setCouponamount(BigDecimal couponamount) {
		this.couponamount = couponamount;
	}
	
	public BigDecimal getPressamount() {
		return pressamount;
	}
	public String getPressamountText() {
		return null != this.pressamount ? NumberUtil.formatTosepara(this.pressamount) : "";
	}
	public void setPressamount(BigDecimal pressamount) {
		this.pressamount = pressamount;
	}
	
	public BigDecimal getWaitingamount() {
		return waitingamount;
	}
	public String getWaitingamountText() {
		return null != this.waitingamount ? NumberUtil.formatTosepara(this.waitingamount) : "";
	}
	public void setWaitingamount(BigDecimal waitingamount) {
		this.waitingamount = waitingamount;
	}
	
//	新加
	public BigDecimal getNewUserNewOrders() {
		return NewUserNewOrders;
	}
	public String getNewUserNewOrdersText() {
		return null != this.NewUserNewOrders ? NumberUtil.formatTosepara(this.NewUserNewOrders) : "";
	}
	public void setNewUserNewOrders(BigDecimal newUserNewOrders) {
		NewUserNewOrders = newUserNewOrders;
	}
	
	public BigDecimal getNewUserNewOrdersPercent() {
		return NewUserNewOrdersPercent;
	}
	public String getNewUserNewOrdersPercentText() {
		return null != this.NewUserNewOrdersPercent ? NumberUtil.formatTosepara(this.NewUserNewOrdersPercent) + "%": "";
	}
	public void setNewUserNewOrdersPercent(BigDecimal newUserNewOrdersPercent) {
		NewUserNewOrdersPercent = newUserNewOrdersPercent;
	}
	
	public BigDecimal getOldUserNewOrders() {
		return OldUserNewOrders;
	}
	public String getOldUserNewOrdersText() {
		return null != this.OldUserNewOrders ? NumberUtil.formatTosepara(this.OldUserNewOrders) : "";
	}
	public void setOldUserNewOrders(BigDecimal oldUserNewOrders) {
		OldUserNewOrders = oldUserNewOrders;
	}
	
	public BigDecimal getOldUserNewOrdersPercent() {
		return OldUserNewOrdersPercent;
	}
	public String getOldUserNewOrdersPercentText() {
		return null != this.OldUserNewOrdersPercent ? NumberUtil.formatTosepara(this.OldUserNewOrdersPercent) + "%": "";
	}
	public void setOldUserNewOrdersPercent(BigDecimal oldUserNewOrdersPercent) {
		OldUserNewOrdersPercent = oldUserNewOrdersPercent;
	}
	
	public BigDecimal getOldUserDelayOrders() {
		return OldUserDelayOrders;
	}
	public String getOldUserDelayOrdersText() {
		return null != this.OldUserDelayOrders ? NumberUtil.formatTosepara(this.OldUserDelayOrders) : "";
	}
	public void setOldUserDelayOrders(BigDecimal oldUserDelayOrders) {
		OldUserDelayOrders = oldUserDelayOrders;
	}
	
	public BigDecimal getOldUserDelayOrdersPercent() {
		return OldUserDelayOrdersPercent;
	}
	public String getOldUserDelayOrdersPercentText() {
		return null != this.OldUserDelayOrdersPercent ? NumberUtil.formatTosepara(this.OldUserDelayOrdersPercent) + "%": "";
	}
	public void setOldUserDelayOrdersPercent(BigDecimal oldUserDelayOrdersPercent) {
		OldUserDelayOrdersPercent = oldUserDelayOrdersPercent;
	}
	
	
	// 500 - 1000 - 1500
	public BigDecimal getOrder7DaysPercent500() {
		return Order7DaysPercent500;
	}
	public String getOrder7DaysPercent500Text() {
		return null != this.Order7DaysPercent500 ? NumberUtil.formatTosepara(this.Order7DaysPercent500) + "%": "";
	}
	public void setOrder7DaysPercent500(BigDecimal order7DaysPercent500) {
		Order7DaysPercent500 = order7DaysPercent500;
	}
	
	public BigDecimal getOrder7DaysPercent1000() {
		return Order7DaysPercent1000;
	}
	public String getOrder7DaysPercent1000Text() {
		return null != this.Order7DaysPercent1000 ? NumberUtil.formatTosepara(this.Order7DaysPercent1000) + "%": "";
	}
	public void setOrder7DaysPercent1000(BigDecimal order7DaysPercent1000) {
		Order7DaysPercent1000 = order7DaysPercent1000;
	}
	
	public BigDecimal getOrder7DaysPercent1500() {
		return Order7DaysPercent1500;
	}
	public String getOrder7DaysPercent1500Text() {
		return null != this.Order7DaysPercent1500 ? NumberUtil.formatTosepara(this.Order7DaysPercent1500) + "%": "";
	}
	public void setOrder7DaysPercent1500(BigDecimal order7DaysPercent1500) {
		Order7DaysPercent1500 = order7DaysPercent1500;
	}
	
	public BigDecimal getOrder14DaysPercent500() {
		return Order14DaysPercent500;
	}
	public String getOrder14DaysPercent500Text() {
		return null != this.Order14DaysPercent500 ? NumberUtil.formatTosepara(this.Order14DaysPercent500) + "%": "";
	}
	public void setOrder14DaysPercent500(BigDecimal order14DaysPercent500) {
		Order14DaysPercent500 = order14DaysPercent500;
	}
	
	public BigDecimal getOrder14DaysPercent1000() {
		return Order14DaysPercent1000;
	}
	public String getOrder14DaysPercent1000Text() {
		return null != this.Order14DaysPercent1000 ? NumberUtil.formatTosepara(this.Order14DaysPercent1000) + "%": "";
	}
	public void setOrder14DaysPercent1000(BigDecimal order14DaysPercent1000) {
		Order14DaysPercent1000 = order14DaysPercent1000;
	}
	
	public BigDecimal getOrder14DaysPercent1500() {
		return Order14DaysPercent1500;
	}
	public String getOrder14DaysPercent1500Text() {
		return null != this.Order14DaysPercent1500 ? NumberUtil.formatTosepara(this.Order14DaysPercent1500) + "%": "";
	}
	public void setOrder14DaysPercent1500(BigDecimal order14DaysPercent1500) {
		Order14DaysPercent1500 = order14DaysPercent1500;
	}
	
	
	
}
