package com.mo9.risk.modules.dunning.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

public class PerformanceMonthReport extends DataEntity<PerformanceMonthReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1929897142411859138L;

	private Date datetime; // 日期
	private Integer begin;
	private Integer end;
	private String personnel; // 催收员
	private Double creditamount;// 应催金额
	private Double payamount; // 还款金额
	private Long creditorder; // 应催订单
	private Long payorder; // 还清订单
	private Double payoffpercentage; // 催回率
	private Long telnum;
	private Long smsnum;
	private Double payoffCapital; // 还款本金
	private Double profileAmount; // 还款利润
	
	private Date datetime_start; // 时间参数
	private Date datetime_end; // 时间参数
	
	
//	@ExcelField(title="日期", type=1, align=2, sort=1)
	public Date getDatetime() {
		return datetime;
	}
	@ExcelField(title="日期", type=1, align=2, sort=1)
	public String getDatetimeText() {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM"); 
		return df.format(new Date(datetime.getTime()));
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	@ExcelField(title="催收员", type=1, align=2, sort=2)
	public String getPersonnel() {
		return personnel;
	}

	public void setPersonnel(String personnel) {
		this.personnel = personnel;
	}
	
	public Double getCreditamount() {
		return creditamount;
	}
	@ExcelField(title="应催金额", type=1, align=2, sort=3)
	public String getCreditamountText() {
		return null != this.creditamount ? NumberUtil.formatTosepara(this.creditamount) : "";
	}
	public void setCreditamount(Double creditamount) {
		this.creditamount = creditamount;
	}
	
	
	public Double getPayamount() {
		return payamount;
	}
	@ExcelField(title="还款金额", type=1, align=2, sort=4)
	public String getPayamountText() {
		return null != this.payamount ? NumberUtil.formatTosepara(this.payamount) : "";
	}
	public void setPayamount(Double payamount) {
		this.payamount = payamount;
	}
	
	@ExcelField(title="应催订单", type=1, align=2, sort=5)
	public Long getCreditorder() {
		return creditorder;
	}
	public void setCreditorder(Long creditorder) {
		this.creditorder = creditorder;
	}
	
	@ExcelField(title="还清订单", type=1, align=2, sort=6)
	public Long getPayorder() {
		return payorder;
	}
	public void setPayorder(Long payorder) {
		this.payorder = payorder;
	}
	
	public Double getPayoffpercentage() {
		return payoffpercentage;
	}
	@ExcelField(title="催回率", type=1, align=2, sort=7)
	public String getPayoffpercentageText() {
		return null != this.payoffpercentage ? NumberUtil.formatTosepara(this.payoffpercentage) + "%" : "";
	}
	public void setPayoffpercentage(Double payoffpercentage) {
		this.payoffpercentage = payoffpercentage;
	}
	
	@ExcelField(title="电话量", type=1, align=2, sort=8)
	public Long getTelnum() {
		return telnum;
	}
	public void setTelnum(Long telnum) {
		this.telnum = telnum;
	}
	
	@ExcelField(title="短信量", type=1, align=2, sort=9)
	public Long getSmsnum() {
		return smsnum;
	}
	public void setSmsnum(Long smsnum) {
		this.smsnum = smsnum;
	}

	public Date getDatetime_start() {
		return datetime_start;
	}
	public void setDatetime_start(Date datetime_start) {
		this.datetime_start = datetime_start;
	}

	public Date getDatetime_end() {
		return datetime_end;
	}
	public void setDatetime_end(Date datetime_end) {
		this.datetime_end = datetime_end;
	}
	
	public Double getPayoffCapital() {
		return payoffCapital;
	}
	@ExcelField(title="还款本金", type=1, align=2, sort=10)
	public String getPayoffCapitalText() {
		return null != this.payoffCapital ? NumberUtil.formatTosepara(this.payoffCapital) : "";
	}
	public void setPayoffCapital(Double payoffCapital) {
		this.payoffCapital = payoffCapital;
	}
	
	public Double getProfileAmount() {
		return profileAmount;
	}
	@ExcelField(title="还款利润", type=1, align=2, sort=11)
	public String getProfileAmountText() {
		return null != this.profileAmount ? NumberUtil.formatTosepara(this.profileAmount) : "";
	}
	public void setProfileAmount(Double profileAmount) {
		this.profileAmount = profileAmount;
	}
	
	@ExcelField(title="催收周期begin", type=1, align=2, sort=12)
	public Integer getBegin() {
		return begin;
	}
	public void setBegin(Integer begin) {
		this.begin = begin;
	}
	
	@ExcelField(title="催收周期end", type=1, align=2, sort=13)
	public Integer getEnd() {
		return end;
	}
	public void setEnd(Integer end) {
		this.end = end;
	}
	
	

}
