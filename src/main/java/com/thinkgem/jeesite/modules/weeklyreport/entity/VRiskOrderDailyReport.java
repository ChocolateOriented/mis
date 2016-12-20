/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 现金贷日报表Entity
 * @author 徐盛
 * @version 2016-06-13
 */
public class VRiskOrderDailyReport extends DataEntity<VRiskOrderDailyReport> {
	
	private static final long serialVersionUID = 1L;
	private Date createtime;		// createtime统计日期
	private Long newusernum;		// NewUserNum int comment '新增用户数',
	private Long newuserordernum;		// NewUserOrderNum int comment '新用户订单数',
	private Long newuserwaitremitnum;  // NewUserOrderNum int comment '新用户待审核数',
	
	private Long olduserordernum;		// OldUserOrderNum int comment '老用户订单数',
	private Long platformappnum;		// PlatformAppNum int comment '订单来源是APP',
	private Long platformwechatnum;		// PlatformWechatNum int comment '订单来源是WECHAT',
	private Long remitordernum;		// RemitOrderNum int comment '成功放款订单数',
	private Long expireordernum;		// ExpireOrderNum int comment '当日到期订单数',
	private Long payoffordernum;		// PayoffOrderNum int comment '当日还款订单数',
	private Double amountincome;		// AmountIncome decimal(12,2) comment '当日贷款收益, 含手续费和延期费',
	private Double overdueincome;		// OverdueIncome decimal(12,2) comment '当日逾期费收益, 2016-06-07 17:30之前的订单 IFNULL(delay_days,0) = 0收取延期费; 2016-06-07 17:30之后的订单, 全部收取延期费'
	private Long pendingordernum;		// pendingOrderNum int comment '待审核订单数'
	private Date beginDatetime;		// 开始 datetime
	private Date endDatetime;		// 结束 datetime

	public VRiskOrderDailyReport() {
		super();
	}

	public VRiskOrderDailyReport(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public Long getNewusernum() {
		return newusernum;
	}

	public void setNewusernum(Long newusernum) {
		this.newusernum = newusernum;
	}
	
	public Long getNewuserordernum() {
		return newuserordernum;
	}

	public void setNewuserordernum(Long newuserordernum) {
		this.newuserordernum = newuserordernum;
	}
	

	public Long getNewuserwaitremitnum() {
		return newuserwaitremitnum;
	}

	public void setNewuserwaitremitnum(Long newuserwaitremitnum) {
		this.newuserwaitremitnum = newuserwaitremitnum;
	}

	public Long getOlduserordernum() {
		return olduserordernum;
	}

	public void setOlduserordernum(Long olduserordernum) {
		this.olduserordernum = olduserordernum;
	}
	
	public Long getPlatformappnum() {
		return platformappnum;
	}

	public void setPlatformappnum(Long platformappnum) {
		this.platformappnum = platformappnum;
	}
	
	public Long getPlatformwechatnum() {
		return platformwechatnum;
	}

	public void setPlatformwechatnum(Long platformwechatnum) {
		this.platformwechatnum = platformwechatnum;
	}
	
	public Long getRemitordernum() {
		return remitordernum;
	}

	public void setRemitordernum(Long remitordernum) {
		this.remitordernum = remitordernum;
	}
	
	public Long getExpireordernum() {
		return expireordernum;
	}

	public void setExpireordernum(Long expireordernum) {
		this.expireordernum = expireordernum;
	}
	
	public Long getPayoffordernum() {
		return payoffordernum;
	}

	public void setPayoffordernum(Long payoffordernum) {
		this.payoffordernum = payoffordernum;
	}
	
	public Double getAmountincome() {
		return amountincome;
	}

	public String getAmountincomeText() {
		return !"".equals(this.amountincome.toString()) ? NumberUtil.formatToseparaInteger(this.amountincome) : this.amountincome.toString();
	}
	
	public void setAmountincome(Double amountincome) {
		this.amountincome = amountincome;
	}
	
	public Double getOverdueincome() {
		return overdueincome;
	}
	
	public String getOverdueincomeText() {
		return !"".equals(this.overdueincome.toString()) ? NumberUtil.formatToseparaInteger(this.overdueincome) : this.overdueincome.toString();
	}

	public void setOverdueincome(Double overdueincome) {
		this.overdueincome = overdueincome;
	}
	
	public Long getPendingordernum() {
		return pendingordernum;
	}

	public void setPendingordernum(Long pendingordernum) {
		this.pendingordernum = pendingordernum;
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

	
}