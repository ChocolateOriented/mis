package com.thinkgem.jeesite.modules.weeklyreport.bean;

import java.math.BigDecimal;

import com.thinkgem.jeesite.util.NumberUtil;

public class SCashLoanDailyReportRepayWeekBean {
	
	private String week;
	
	private String channelname;
	private String subchannelname;
	
	private BigDecimal feerate;
	
	private BigDecimal repayamount;
	private BigDecimal repayamountpercent;
	
	private long repaycount;
	private BigDecimal repaycountpercent;
	
	
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}
	public String getSubchannelname() {
		return subchannelname;
	}
	public void setSubchannelname(String subchannelname) {
		this.subchannelname = subchannelname;
	}
	
	public BigDecimal getFeerate() {
		return feerate;
	}
	public String getFeerateText() {
		return null != this.feerate ? NumberUtil.formatTosepara(this.feerate)+ "%": "";
	}
	public void setFeerate(BigDecimal feerate) {
		this.feerate = feerate;
	}
	
	public BigDecimal getRepayamount() {
		return repayamount;
	}
	public String getRepayamountText() {
		return null != this.repayamount ? NumberUtil.formatTosepara(this.repayamount): "";
	}
	public void setRepayamount(BigDecimal repayamount) {
		this.repayamount = repayamount;
	}
	
	public BigDecimal getRepayamountpercent() {
		return repayamountpercent;
	}
	public String getRepayamountpercentText() {
		return null != this.repayamountpercent ? NumberUtil.formatTosepara(this.repayamountpercent)+ "%": "";
	}
	public void setRepayamountpercent(BigDecimal repayamountpercent) {
		this.repayamountpercent = repayamountpercent;
	}
	
	public long getRepaycount() {
		return repaycount;
	}
	public String getRepaycountText() {
		return 0 != this.repaycount ? NumberUtil.formatTosepara(this.repaycount): "0";
	}
	public void setRepaycount(long repaycount) {
		this.repaycount = repaycount;
	}
	
	public BigDecimal getRepaycountpercent() {
		return repaycountpercent;
	}
	public String getRepaycountpercentText() {
		return null != this.repaycountpercent ? NumberUtil.formatTosepara(this.repaycountpercent)+ "%": "";
	}
	public void setRepaycountpercent(BigDecimal repaycountpercent) {
		this.repaycountpercent = repaycountpercent;
	}

	
	
	
}
