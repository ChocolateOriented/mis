package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.utils.DateUtils;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

public class PerformanceDayReport extends DataEntity<PerformanceDayReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1929897142411859138L;

//	private Date datetime; // 日期
	private Date datetimestart; // 时间参数
	private Date datetimeend; // 时间参数
	private String dunningCycle;//催收队列
//	private Integer begin;
//	private Integer end;
	private String personnel; // 催收员
	private Double payamount; // 还款金额
	private Long payorder; // 还清订单
	private Long telnum;
	private Long smsnum;
	private TMisDunningGroup group;

	
//	@ExcelField(title="日期", type=1, align=2, sort=1)
//	public Date getDatetime() {
//		return datetime;
//	}
//	public void setDatetime(Date datetime) {
//		this.datetime = datetime;
//	}

	
	@ExcelField(title="起始日", type=1, align=2, sort=1)
	public Date getDatetimestart() {
		return datetimestart;
	}
	public void setDatetimestart(Date datetimestart) {
		this.datetimestart = datetimestart;
	}
	
	@ExcelField(title="结束日", type=1, align=2, sort=2)
	public Date getDatetimeend() {
		return datetimeend;
	}
	public void setDatetimeend(Date datetimeend) {
		this.datetimeend =  null != datetimeend ? DateUtils.endDate(datetimeend) : datetimeend;
	}
	
	@ExcelField(title="催收员", type=1, align=2, sort=3)
	public String getPersonnel() {
		return personnel;
	}

	public void setPersonnel(String personnel) {
		this.personnel = personnel;
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
	@ExcelField(title="还清订单", type=1, align=2, sort=5)
	public Long getPayorder() {
		return payorder;
	}

	public void setPayorder(Long payorder) {
		this.payorder = payorder;
	}
	@ExcelField(title="电话量", type=1, align=2, sort=6)
	public Long getTelnum() {
		return telnum;
	}

	public void setTelnum(Long telnum) {
		this.telnum = telnum;
	}
	@ExcelField(title="短信量", type=1, align=2, sort=7)
	public Long getSmsnum() {
		return smsnum;
	}

	public void setSmsnum(Long smsnum) {
		this.smsnum = smsnum;
	}
	@ExcelField(title="催收队列", type=1, align=2, sort=8)
	public String getDunningCycle() {
		return dunningCycle;
	}
	public void setDunningCycle(String dunningCycle) {
		this.dunningCycle = dunningCycle;
	}
	public TMisDunningGroup getGroup() {
		return group;
	}
	public void setGroup(TMisDunningGroup group) {
		this.group = group;
	}

//	@ExcelField(title="周期begin", type=1, align=2, sort=8)
//	public Integer getBegin() {
//		return begin;
//	}
//	public void setBegin(Integer begin) {
//		this.begin = begin;
//	}
//	
//	@ExcelField(title="周期end", type=1, align=2, sort=9)
//	public Integer getEnd() {
//		return end;
//	}
//	public void setEnd(Integer end) {
//		this.end = end;
//	}

}
