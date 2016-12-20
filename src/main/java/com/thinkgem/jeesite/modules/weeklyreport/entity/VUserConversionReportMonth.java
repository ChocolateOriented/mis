/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 用户月报表Entity
 * @author 徐盛
 * @version 2016-07-21
 */
public class VUserConversionReportMonth extends DataEntity<VUserConversionReportMonth> {
	
	private static final long serialVersionUID = 1L;
	private Integer month;		// 今天是几月
	private Long newinstall;		// 新增安装量
	private BigDecimal newinstallpercent;		// 安装量增长率
	private BigDecimal registpercent;		// 新增到注册
	private BigDecimal firstorderpercent;		// 注册到订单
	private BigDecimal artificialpercent;		// 订单到人工
	private BigDecimal remitpercent;		// 人工通过率
//	private BigDecimal waitingtimeavg01;		// 1-4 首单平均等待时间
//	private BigDecimal waitingtimeavg02;		// 5-7 首单平均等待时间
	private String waitingtimeavg01;	
	private String waitingtimeavg02;	
	private BigDecimal newremituser;		// 新增放款用户数
	private Integer allremituser;		// 累计放款用户数
	private BigDecimal validuserpercent;		// 有效用户比
	private BigDecimal prioruserpercent;		// 优质用户比
	private BigDecimal activeuserpercent;		// 活跃用户比
	private BigDecimal canceltodaypercent;		// 当日取消率
	private BigDecimal canceltomorrowpercent;		// 次日取消率
	
	public VUserConversionReportMonth() {
		super();
	}

	public VUserConversionReportMonth(String id){
		super(id);
	}

	@ExcelField(title="日期", type=1, align=2, sort=1)
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}

	public Long getNewinstall() {
		return newinstall;
	}
	@ExcelField(title="新增安装量", type=1, align=2, sort=2)
	public String getNewinstallText() {
		return null != this.newinstall ? NumberUtil.formatTosepara(this.newinstall)  : "";
	}
	public void setNewinstall(Long newinstall) {
		this.newinstall = newinstall;
	}
	
	@NotNull(message="安装量增长率不能为空")
	public BigDecimal getNewinstallpercent() {
		return newinstallpercent;
	}
	@ExcelField(title="安装量增长率", type=1, align=2, sort=3)
	public String getNewinstallpercentText() {
		return null != this.newinstallpercent ? NumberUtil.formatTosepara(this.newinstallpercent)  + "%" : "";
	}
	public void setNewinstallpercent(BigDecimal newinstallpercent) {
		this.newinstallpercent = newinstallpercent;
	}
	
	public BigDecimal getRegistpercent() {
		return registpercent;
	}
	@ExcelField(title="新增到注册", type=1, align=2, sort=4)
	public String getRegistpercentText() {
		return null != this.registpercent ? NumberUtil.formatTosepara(this.registpercent) + "%" : "";
	}
	public void setRegistpercent(BigDecimal registpercent) {
		this.registpercent = registpercent;
	}
	
	public BigDecimal getFirstorderpercent() {
		return firstorderpercent;
	}
	@ExcelField(title="注册到订单", type=1, align=2, sort=5)
	public String getFirstorderpercentText() {
		return null != this.firstorderpercent ? NumberUtil.formatTosepara(this.firstorderpercent)  + "%" : "";
	}
	public void setFirstorderpercent(BigDecimal firstorderpercent) {
		this.firstorderpercent = firstorderpercent;
	}
	
	public BigDecimal getArtificialpercent() {
		return artificialpercent;
	}
	@ExcelField(title="订单到人工", type=1, align=2, sort=6)
	public String getArtificialpercentText() {
		return null != this.artificialpercent ? NumberUtil.formatTosepara(this.artificialpercent)+ "%" : "";
	}
	public void setArtificialpercent(BigDecimal artificialpercent) {
		this.artificialpercent = artificialpercent;
	}
	
	public BigDecimal getRemitpercent() {
		return remitpercent;
	}
	@ExcelField(title="人工通过率", type=1, align=2, sort=7)
	public String getRemitpercentText() {
		return null != this.remitpercent ? NumberUtil.formatTosepara(this.remitpercent)  + "%" : "";
	}
	public void setRemitpercent(BigDecimal remitpercent) {
		this.remitpercent = remitpercent;
	}
	
	@ExcelField(title="1-4首单平均等待时间", type=1, align=2, sort=8)
	public String getWaitingtimeavg01() {
		return waitingtimeavg01;
	}
	public void setWaitingtimeavg01(String waitingtimeavg01) {
		this.waitingtimeavg01 = waitingtimeavg01;
	}

	@ExcelField(title="5-7首单平均等待时间", type=1, align=2, sort=9)
	public String getWaitingtimeavg02() {
		return waitingtimeavg02;
	}
	public void setWaitingtimeavg02(String waitingtimeavg02) {
		this.waitingtimeavg02 = waitingtimeavg02;
	}

	public BigDecimal getNewremituser() {
		return newremituser;
	}
	@ExcelField(title="新增放款用户数", type=1, align=2, sort=10)
	public String getNewremituserText() {
		return null != this.newremituser ? NumberUtil.formatTosepara(this.newremituser) : "";
	}
	public void setNewremituser(BigDecimal newremituser) {
		this.newremituser = newremituser;
	}
	
	public Integer getAllremituser() {
		return allremituser;
	}
	@ExcelField(title="累计放款用户数", type=1, align=2, sort=11)
	public String getAllremituserText() {
		return null != this.allremituser ? NumberUtil.formatTosepara(this.allremituser) : "";
	}
	public void setAllremituser(Integer allremituser) {
		this.allremituser = allremituser;
	}
	
	public BigDecimal getValiduserpercent() {
		return validuserpercent;
	}
	@ExcelField(title="有效用户比", type=1, align=2, sort=12)
	public String getValiduserpercentText() {
		return null != this.validuserpercent ? NumberUtil.formatTosepara(this.validuserpercent)  + "%" : "";
	}
	public void setValiduserpercent(BigDecimal validuserpercent) {
		this.validuserpercent = validuserpercent;
	}
	
	public BigDecimal getPrioruserpercent() {
		return prioruserpercent;
	}
	@ExcelField(title="优质用户比", type=1, align=2, sort=13)
	public String getPrioruserpercentText() {
		return null != this.prioruserpercent ? NumberUtil.formatTosepara(this.prioruserpercent)  + "%" : "";
	}
	public void setPrioruserpercent(BigDecimal prioruserpercent) {
		this.prioruserpercent = prioruserpercent;
	}
	
	public BigDecimal getActiveuserpercent() {
		return activeuserpercent;
	}
	@ExcelField(title="活跃用户比", type=1, align=2, sort=14)
	public String getActiveuserpercentText() {
		return null != this.activeuserpercent ? NumberUtil.formatTosepara(this.activeuserpercent)  + "%" : "";
	}
	public void setActiveuserpercent(BigDecimal activeuserpercent) {
		this.activeuserpercent = activeuserpercent;
	}
	
	public BigDecimal getCanceltodaypercent() {
		return canceltodaypercent;
	}
	@ExcelField(title="当日取消率", type=1, align=2, sort=15)
	public String getCanceltodaypercentText() {
		return null != this.canceltodaypercent ? NumberUtil.formatTosepara(this.canceltodaypercent) + "%"  : "";
	}
	public void setCanceltodaypercent(BigDecimal canceltodaypercent) {
		this.canceltodaypercent = canceltodaypercent;
	}
	
	public BigDecimal getCanceltomorrowpercent() {
		return canceltomorrowpercent;
	}
	@ExcelField(title="次日取消率", type=1, align=2, sort=16)
	public String getCanceltomorrowpercentText() {
		return null != this.canceltomorrowpercent ? NumberUtil.formatTosepara(this.canceltomorrowpercent)  + "%" : "";
	}
	public void setCanceltomorrowpercent(BigDecimal canceltomorrowpercent) {
		this.canceltomorrowpercent = canceltomorrowpercent;
	}
	
}