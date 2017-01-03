/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 资金流日报Entity
 * @author 徐盛
 * @version 2016-11-18
 */
public class SCashFlowReport extends DataEntity<SCashFlowReport> {
	
	private static final long serialVersionUID = 1L;
	private Date createtime;		// 统计时间
	private Long payamount;		// 放款金额
	private Long payoffamount;		// 还款金额
	private Long diffamount;		// 放还差额
	private Long gpayamount;		// 先玩后付充值金额
	private Long kdpayamount;		//口袋理财放款金额
	private Long mo9payamount;		//Mo9放款金额
	private Long manualpayamount;	//人工放款金额
	
	private Date beginCreatetime;		// 开始 datetime
	private Date endCreatetime;		// 结束 datetime
	
	private Integer repaynumber;		// 到期订单数
	private Integer paynumbers;		// 放款订单数
	
	public SCashFlowReport() {
		super();
	}

	public SCashFlowReport(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="日期", type=1, align=2, sort=1)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@ExcelField(title="放款总额", type=1, align=2, sort=2)
	public Long getPayamount() {
		return payamount;
	}
	public void setPayamount(Long payamount) {
		this.payamount = payamount;
	}
	
	@ExcelField(title="口袋放款", type=1, align=2, sort=3)
	public Long getKdpayamount() {
		return kdpayamount;
	}
	public void setKdpayamount(Long kdpayamount) {
		this.kdpayamount = kdpayamount;
	}
	
	@ExcelField(title="mo9放款", type=1, align=2, sort=4)
	public Long getMo9payamount() {
		return mo9payamount;
	}
	public void setMo9payamount(Long mo9payamount) {
		this.mo9payamount = mo9payamount;
	}
	
	
	@ExcelField(title="人工放款", type=1, align=2, sort=5)
	public Long getManualpayamount() {
		return manualpayamount;
	}
	public void setManualpayamount(Long manualpayamount) {
		this.manualpayamount = manualpayamount;
	}
	
	@ExcelField(title="还款金额", type=1, align=2, sort=6)
	public Long getPayoffamount() {
		return payoffamount;
	}
	public void setPayoffamount(Long payoffamount) {
		this.payoffamount = payoffamount;
	}
	
	@ExcelField(title="放还差额", type=1, align=2, sort=7)
	public Long getDiffamount() {
		return diffamount;
	}
	public void setDiffamount(Long diffamount) {
		this.diffamount = diffamount;
	}
	
	@ExcelField(title="先玩后付充值金额", type=1, align=2, sort=8)
	public Long getGpayamount() {
		return gpayamount;
	}
	public void setGpayamount(Long gpayamount) {
		this.gpayamount = gpayamount;
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