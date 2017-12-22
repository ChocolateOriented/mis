/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 催收员案件活动日报Entity
 * @author 李敬翔
 * @version 2017-05-10
 */
public class SMisDunningProductivePowerDailyReport extends DataEntity<SMisDunningProductivePowerDailyReport> {
	
	private static final long serialVersionUID = 1L;
	private Date createTime;		// 日期时间
	private String dunningPeopleName;		// 催收员姓名
	private String dunningCycle;		// 催收员队列
	private String taskCycle;		// 分案队列
	private Integer totalUser;		// 客户数
	private BigDecimal overdueAmount;		// 逾期总额
	private BigDecimal corpusAmount;		// 本金总额
	private Integer dealUser;		// 处理账户数
	private Integer dealNumber;		// 处理次数
	private Integer effectiveUser;		// 有效账户数
	private Integer ptpUser;		// 承诺还款户数
	private Integer ptpAmount;		// 承诺还款
	private BigDecimal ptpCorpusAmount;		// 承诺还款本金
	private BigDecimal effectiveUserPercent;		// 有效处理占比_户数
	private BigDecimal effectiveAmountPercent;		// 有效处理占比_逾期金额
	private BigDecimal effectiveCorpusAmountPercent;		// 有效处理占比_本金
	private BigDecimal ptpUserPercent;		// 承诺还款占比_户数
	private BigDecimal ptpAmountPercent;		// 承诺还款占比_逾期金额
	private BigDecimal ptpCorpusAmountPerent;		// 承诺还款占比_本金
	private Integer payoffUser;		// 还款户数
	private BigDecimal payoffCorpusAmount;		// 还款的总本金
	private BigDecimal payoffAmount;		// 还款总金额
	private String groupId ; //催收员所属组
	private Date beginCreateTime;		// 开始 日期时间
	private Date endCreateTime;		// 结束 日期时间
	
	private List<TMisDunningGroup> queryGroups;		//查询的组s
	private List<String> pNames;
	
	public SMisDunningProductivePowerDailyReport() {
		super();
	}

	public SMisDunningProductivePowerDailyReport(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="日期时间",  type=1, align=2, sort=1)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=24, message="催收员姓名长度必须介于 0 和 24 之间")
	@ExcelField(title="催收员姓名",  type=1, align=2, sort=2)
	public String getDunningPeopleName() {
		return dunningPeopleName;
	}

	public void setDunningPeopleName(String dunningPeopleName) {
		this.dunningPeopleName = dunningPeopleName;
	}
	
	@Length(min=0, max=16, message="催收员队列长度必须介于 0 和 16 之间")
	@ExcelField(title="催收员队列",  type=1, align=2, sort=3)
	public String getDunningCycle() {
		return dunningCycle;
	}

	public void setDunningCycle(String dunningCycle) {
		this.dunningCycle = dunningCycle;
	}
	
	@Length(min=0, max=8, message="分案队列长度必须介于 0 和 8 之间")
	@ExcelField(title="案件队列",  type=1, align=2, sort=4)
	public String getTaskCycle() {
		return taskCycle;
	}

	public void setTaskCycle(String taskCycle) {
		this.taskCycle = taskCycle;
	}
	
	@ExcelField(title="客户数",  type=1, align=2, sort=5)
	public Integer getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(Integer totalUser) {
		this.totalUser = totalUser;
	}
	
	@ExcelField(title="逾期总额",  type=1, align=2, sort=6)
	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
	
	@ExcelField(title="本金总额",  type=1, align=2, sort=7)
	public BigDecimal getCorpusAmount() {
		return corpusAmount;
	}

	public void setCorpusAmount(BigDecimal corpusAmount) {
		this.corpusAmount = corpusAmount;
	}
	
	@ExcelField(title="处理账户数",  type=1, align=2, sort=8)
	public Integer getDealUser() {
		return dealUser;
	}

	public void setDealUser(Integer dealUser) {
		this.dealUser = dealUser;
	}
	
	@ExcelField(title="处理次数",  type=1, align=2, sort=9)
	public Integer getDealNumber() {
		return dealNumber;
	}

	public void setDealNumber(Integer dealNumber) {
		this.dealNumber = dealNumber;
	}
	
	@ExcelField(title="有效账户数",  type=1, align=2, sort=10)
	public Integer getEffectiveUser() {
		return effectiveUser;
	}

	public void setEffectiveUser(Integer effectiveUser) {
		this.effectiveUser = effectiveUser;
	}
	
	@ExcelField(title="承诺还款户数",  type=1, align=2, sort=11)
	public Integer getPtpUser() {
		return ptpUser;
	}

	public void setPtpUser(Integer ptpUser) {
		this.ptpUser = ptpUser;
	}
	
	@ExcelField(title="承诺还款",  type=1, align=2, sort=12)
	public Integer getPtpAmount() {
		return ptpAmount;
	}

	public void setPtpAmount(Integer ptpAmount) {
		this.ptpAmount = ptpAmount;
	}
	
	@ExcelField(title="承诺还款本金",  type=1, align=2, sort=13)
	public BigDecimal getPtpCorpusAmount() {
		return ptpCorpusAmount;
	}

	public void setPtpCorpusAmount(BigDecimal ptpCorpusAmount) {
		this.ptpCorpusAmount = ptpCorpusAmount;
	}
	
	@ExcelField(title="有效处理占比_户数",  type=1, align=2, sort=14)
	public BigDecimal getEffectiveUserPercent() {
		return effectiveUserPercent;
	}

	public void setEffectiveUserPercent(BigDecimal effectiveUserPercent) {
		this.effectiveUserPercent = effectiveUserPercent;
	}
	
	@ExcelField(title="有效处理占比_逾期金额",  type=1, align=2, sort=15)
	public BigDecimal getEffectiveAmountPercent() {
		return effectiveAmountPercent;
	}

	public void setEffectiveAmountPercent(BigDecimal effectiveAmountPercent) {
		this.effectiveAmountPercent = effectiveAmountPercent;
	}
	
	@ExcelField(title="有效处理占比_本金",  type=1, align=2, sort=16)
	public BigDecimal getEffectiveCorpusAmountPercent() {
		return effectiveCorpusAmountPercent;
	}

	public void setEffectiveCorpusAmountPercent(BigDecimal effectiveCorpusAmountPercent) {
		this.effectiveCorpusAmountPercent = effectiveCorpusAmountPercent;
	}
	
	@ExcelField(title="承诺还款占比_户数",  type=1, align=2, sort=17)
	public BigDecimal getPtpUserPercent() {
		return ptpUserPercent;
	}

	public void setPtpUserPercent(BigDecimal ptpUserPercent) {
		this.ptpUserPercent = ptpUserPercent;
	}
	
	@ExcelField(title="承诺还款占比_逾期金额",  type=1, align=2, sort=18)
	public BigDecimal getPtpAmountPercent() {
		return ptpAmountPercent;
	}

	public void setPtpAmountPercent(BigDecimal ptpAmountPercent) {
		this.ptpAmountPercent = ptpAmountPercent;
	}
	
	@ExcelField(title="承诺还款占比_本金",  type=1, align=2, sort=19)
	public BigDecimal getPtpCorpusAmountPerent() {
		return ptpCorpusAmountPerent;
	}

	public void setPtpCorpusAmountPerent(BigDecimal ptpCorpusAmountPerent) {
		this.ptpCorpusAmountPerent = ptpCorpusAmountPerent;
	}
	
	@ExcelField(title="还款户数",  type=1, align=2, sort=20)
	public Integer getPayoffUser() {
		return payoffUser;
	}

	public void setPayoffUser(Integer payoffUser) {
		this.payoffUser = payoffUser;
	}
	
	@ExcelField(title="还款的总本金",  type=1, align=2, sort=21)
	public BigDecimal getPayoffCorpusAmount() {
		return payoffCorpusAmount;
	}

	public void setPayoffCorpusAmount(BigDecimal payoffCorpusAmount) {
		this.payoffCorpusAmount = payoffCorpusAmount;
	}
	
	@ExcelField(title="还款总金额",  type=1, align=2, sort=22)
	public BigDecimal getPayoffAmount() {
		return payoffAmount;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setPayoffAmount(BigDecimal payoffAmount) {
		this.payoffAmount = payoffAmount;
	}
	
	public Date getBeginCreateTime() {
		return beginCreateTime;
	}

	public void setBeginCreateTime(Date beginCreateTime) {
		this.beginCreateTime = beginCreateTime;
	}
	
	public Date getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime =  null != endCreateTime ? DateUtils.endDate(endCreateTime) : endCreateTime;
	}

	public List<TMisDunningGroup> getQueryGroups() {
		return queryGroups;
	}

	public void setQueryGroups(List<TMisDunningGroup> queryGroups) {
		this.queryGroups = queryGroups;
	}

	public List<String> getpNames() {
		return pNames;
	}

	public void setpNames(List<String> pNames) {
		this.pNames = pNames;
	}
}

