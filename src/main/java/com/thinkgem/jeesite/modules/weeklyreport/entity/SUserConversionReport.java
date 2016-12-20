/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户报表Entity
 * @author 徐盛
 * @version 2016-07-20
 */
public class SUserConversionReport extends DataEntity<SUserConversionReport> {
	
	private static final long serialVersionUID = 1L;
	private Date createtime;		// createtime
	private String weekday;		// 今天是周几
	private String week;		// 今天是第几周
	private String weekdesc;		// 周的描述显示
	private String month;		// 今天是几月
	private Integer newapp;		// APP新增装机量
	private Integer newwechat;		// 新增微信装机量
	private String newregist;		// 新增注册人数
	private String firstorder;		// 本期首单 - 首笔资料全部提交的订单
	private String artificialorder;		// 本期人工 - 最终落入人工审核订单数
	private String remitorder01;		// 本期新增放款用户数
	private String remitorder02;		// remitorder02
	private String difftime01;		// 首单平均等待时间 周一～周四
	private String difftime02;		// 首单平均等待时间 周五～周日
	private String canceltoday;		// 首单当日取消订单数
	private String canceltomorrow;		// 首单次日取消订单数
	private String newremituser;		// 新增放款用户数
	private String allremituser;		// 累计放款用户数
	private String validuser;		// 有效用户数
	private String prioruser;		// 优质用户数
	private String activeuser;		// 活跃用户数
	
	private Date beginCreatetime;		// 开始 datetime
	private Date endCreatetime;		// 结束 datetime
	
	public SUserConversionReport() {
		super();
	}

	public SUserConversionReport(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Length(min=0, max=11, message="今天是周几长度必须介于 0 和 11 之间")
	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	
	@Length(min=0, max=11, message="今天是第几周长度必须介于 0 和 11 之间")
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
	
	@Length(min=0, max=50, message="周的描述显示长度必须介于 0 和 50 之间")
	public String getWeekdesc() {
		return weekdesc;
	}

	public void setWeekdesc(String weekdesc) {
		this.weekdesc = weekdesc;
	}
	
	@Length(min=0, max=11, message="今天是几月长度必须介于 0 和 11 之间")
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	public Integer getNewapp() {
		return newapp;
	}

	public void setNewapp(Integer newapp) {
		this.newapp = newapp;
	}
	
	public Integer getNewwechat() {
		return newwechat;
	}

	public void setNewwechat(Integer newwechat) {
		this.newwechat = newwechat;
	}
	
	@Length(min=0, max=11, message="新增注册人数长度必须介于 0 和 11 之间")
	public String getNewregist() {
		return newregist;
	}

	public void setNewregist(String newregist) {
		this.newregist = newregist;
	}
	
	@Length(min=0, max=11, message="本期首单 - 首笔资料全部提交的订单长度必须介于 0 和 11 之间")
	public String getFirstorder() {
		return firstorder;
	}

	public void setFirstorder(String firstorder) {
		this.firstorder = firstorder;
	}
	
	@Length(min=0, max=11, message="本期人工 - 最终落入人工审核订单数长度必须介于 0 和 11 之间")
	public String getArtificialorder() {
		return artificialorder;
	}

	public void setArtificialorder(String artificialorder) {
		this.artificialorder = artificialorder;
	}
	
	@Length(min=0, max=11, message="本期新增放款用户数长度必须介于 0 和 11 之间")
	public String getRemitorder01() {
		return remitorder01;
	}

	public void setRemitorder01(String remitorder01) {
		this.remitorder01 = remitorder01;
	}
	
	@Length(min=0, max=11, message="remitorder02长度必须介于 0 和 11 之间")
	public String getRemitorder02() {
		return remitorder02;
	}

	public void setRemitorder02(String remitorder02) {
		this.remitorder02 = remitorder02;
	}
	
	@Length(min=0, max=11, message="首单平均等待时间 周一～周四长度必须介于 0 和 11 之间")
	public String getDifftime01() {
		return difftime01;
	}

	public void setDifftime01(String difftime01) {
		this.difftime01 = difftime01;
	}
	
	@Length(min=0, max=11, message="首单平均等待时间 周五～周日长度必须介于 0 和 11 之间")
	public String getDifftime02() {
		return difftime02;
	}

	public void setDifftime02(String difftime02) {
		this.difftime02 = difftime02;
	}
	
	@Length(min=0, max=11, message="首单当日取消订单数长度必须介于 0 和 11 之间")
	public String getCanceltoday() {
		return canceltoday;
	}

	public void setCanceltoday(String canceltoday) {
		this.canceltoday = canceltoday;
	}
	
	@Length(min=0, max=11, message="首单次日取消订单数长度必须介于 0 和 11 之间")
	public String getCanceltomorrow() {
		return canceltomorrow;
	}

	public void setCanceltomorrow(String canceltomorrow) {
		this.canceltomorrow = canceltomorrow;
	}
	
	@Length(min=0, max=11, message="新增放款用户数长度必须介于 0 和 11 之间")
	public String getNewremituser() {
		return newremituser;
	}

	public void setNewremituser(String newremituser) {
		this.newremituser = newremituser;
	}
	
	@Length(min=0, max=11, message="累计放款用户数长度必须介于 0 和 11 之间")
	public String getAllremituser() {
		return allremituser;
	}

	public void setAllremituser(String allremituser) {
		this.allremituser = allremituser;
	}
	
	@Length(min=0, max=11, message="有效用户数长度必须介于 0 和 11 之间")
	public String getValiduser() {
		return validuser;
	}

	public void setValiduser(String validuser) {
		this.validuser = validuser;
	}
	
	@Length(min=0, max=11, message="优质用户数长度必须介于 0 和 11 之间")
	public String getPrioruser() {
		return prioruser;
	}

	public void setPrioruser(String prioruser) {
		this.prioruser = prioruser;
	}
	
	@Length(min=0, max=11, message="活跃用户数长度必须介于 0 和 11 之间")
	public String getActiveuser() {
		return activeuser;
	}

	public void setActiveuser(String activeuser) {
		this.activeuser = activeuser;
	}

	public Date getBeginCreatetime() {
		return beginCreatetime;
	}

	public void setBeginCreatetime(Date beginCreatetime) {
		this.beginCreatetime = beginCreatetime;
	}

	public Date getEndCreatetime() {
		return endCreatetime;
	}

	public void setEndCreatetime(Date endCreatetime) {
		this.endCreatetime = endCreatetime;
	}
	
	
	
}