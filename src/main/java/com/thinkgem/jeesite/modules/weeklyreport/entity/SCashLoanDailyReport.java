/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 财务日报Entity
 * @author 徐盛
 * @version 2016-06-23
 */
public class SCashLoanDailyReport extends DataEntity<SCashLoanDailyReport> {
	
	private static final long serialVersionUID = 1L;
	private Date createtime;		// createtime
	private String month;		// month
	private String week;		// week
	private String weekdesc;     //  week-----------------------------------------------------------------------------------------------
	
	private Double remitamount;		// 交易总额 - 本期累计(本次统计周期内, 累几放贷金额)                    :::::Text  formatTosepara---
	//week month
	private Double weekincreased;		// 交易总额 - 周增长率 增长率 = 本期累计交易总额 / 上期累计交易总额        :::::Text  *100% 
	private Double monthincreased;		// 交易总额 - 月增长率 增长率 = 本期累计交易总额 / 上期累计交易总额        :::::Text  *100% 
	
	private Integer newuser;		// 交易总额 - 新增交易用户 - 本次统计周期内, 新增的放款独立用户数			:::::Text formatTosepara---
	private Integer neworder;		// 交易总额 - 放贷笔数 - 本次统计周期内, 新增的放贷笔数, 包括延期笔数		:::::Text  formatTosepara---
	private Double incomeamount;		// 收入 - 本期收入 - 本次统计周期内, 实际收到的 信审收益 + 延期手续费 + 逾期费 + 委外(部分还款不计入)      :::::Text formatTosepara---
	//week month
	private Double incomeincreased;		// 收入 - 收入增长率 - 收入增长率 = (本期收入 - 上期收入) / 上期收入       :::::Text  formatTosepara + %
	private Double monthincomeincreased; //    ------------------------------------------------------------- :::::Text  formatTosepara + %
	private Double incomepercent;		// 收入 - 收入比 - 收入比 = 本期收入 / 上上周交易总额					:::::Text  formatTosepara + %
	private Double monthincomepercent; //    -----------------------------------------------------------------:::::Text  formatTosepara + %
	
	private Double loancost;		// 支付渠道费 - 放款成本 - 放款成本 = 本期放贷笔数(不包括延期) &times; 2		:::::Text formatTosepara---
	private Double repaycost;		// 支付渠道费 - 还款成本 - 使用各支付渠道所产生的支付渠道费						:::::Text formatTosepara---
	private Double creditsumcost;		// 征信成本 - 本期总成本 - 本次统计周期内, 使用第三方征信数据所产生的费用总和                         ====================手入      :::::Text formatTosepara---
	
	private Double creditavgcost;		// 征信成本 - 人均成本 - 人均成本 = 本期总成本 / 本期新增交易用户数                    :::::Text  formatTosepara + %
	private Double MonthCreditAvgCost;  // 征信成本 - 人均成本 - 人均成本 = 本期总成本 / 本期新增交易用户数                    :::::Text  formatTosepara + %
	
	private Double couponcost;		// 市场推广 - 免息券 - 本次统计周期内, 被消耗掉的免息券及其他抵扣金额的总和		:::::Text formatTosepara---
	private Double mediacost;		// 市场推广 - 媒体宣传费 - 以我方收到的发票的日期为准                                                    			=====================手入	:::::Text formatTosepara---
	private Double debatamount;		// 坏账 - 目前累计 - 自产品上线之日起, 累计坏账								:::::Text formatTosepara---
	//week month
	private Double weekdebatamount;		// 坏账 - 周本期累计 - 本期累计 = 本期目前累计坏账 - 上期目前累计坏账		:::::Text formatTosepara---
	private Double monthdebatamount;		// 坏账 - 月本期累计 - 本期累计 = 本期目前累计坏账 -  上期目前累计坏账		:::::Text formatTosepara---

	private Double debatpercent;		// 周坏账 - 坏账率 - 坏账率 = 目前累计 / 自产品上线之日起的累计交易总额		:::::Text formatTosepara + %
	private Double monthdebatpercent;   // 月坏账 - 坏账率 - 坏账率 = 目前累计 / 自产品上线之日起的累计交易总额		:::::Text formatTosepara + %

	private Double entrustcommission;		// 坏账 - 委外佣金 - 委外回款所扣除的佣金总金额, 佣金月结                          :::::Text formatTosepara------------------------------------- 按月-=====================手入
	private Double grossprofile;		// 毛利 - 毛利 - 毛利 = 收入 - 支付渠道费 - 征信成本 - 市场推广 - 坏账本期累计 - 委外佣金		:::::Text formatTosepara---
	
	private Double grossprofilepercent;		 // 毛利 - 毛利率 - 毛利率 = 毛利 / 收入		:::::Text formatTosepara + %
	private Double MonthGrossProfilePercent; // 毛利 - 毛利率 - 毛利率 = 毛利 / 收入		:::::Text formatTosepara + %

	private Double sumremitamount;        // 累计交易总额
	private Double weekrepayavgcost;      //  周平均还款费率
	private Double monthrepayavgcost;     //  月平均还款费率
	
	private Date beginCreatetime;		// 开始 createtime
	private Date endCreatetime;		// 结束 createtime
	
	private Double WeekSingleRemitAvgAmount;
	private Double MonthSingleRemitAvgAmount;
	
	private Double cashcostamount;  // 资金成本
	
	private Double debatreturnamount;
	
	public SCashLoanDailyReport() {
		super();
	}

	public SCashLoanDailyReport(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Length(min=0, max=8, message="month长度必须介于 0 和 8 之间")
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	@Length(min=0, max=30, message="week长度必须介于 0 和 30 之间")
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
	
	public Double getRemitamount() {
		return remitamount;
	}
	public String getRemitamountText() {
		return null != this.remitamount ? NumberUtil.formatToseparaInteger(this.remitamount) : "";
	}

	public void setRemitamount(Double remitamount) {
		this.remitamount = remitamount;
	}
	
	public Double getWeekincreased() {
		return weekincreased;
	}
	public String getWeekincreasedText() {
		return null != this.weekincreased ? NumberUtil.formatTosepara(this.weekincreased) + "%" : "";
//		return weekincreased * 100 +"%";
	}

	public void setWeekincreased(Double weekincreased) {
		this.weekincreased = weekincreased;
	}
	
	public Double getMonthincreased() {
		return monthincreased;
	}
	public String getMonthincreasedText() {
		return null != this.monthincreased ? NumberUtil.formatTosepara(this.monthincreased) + "%" : "";
//		return monthincreased * 100 +"%";
	}

	public void setMonthincreased(Double monthincreased) {
		this.monthincreased = monthincreased;
	}
	
	public Integer getNewuser() {
		return newuser;
	}
	public String getNewuserText() {
		return null != this.newuser ? NumberUtil.formatToseparaInteger(this.newuser) : "";
	}
	public void setNewuser(Integer newuser) {
		this.newuser = newuser;
	}
	
	public Integer getNeworder() {
		return neworder;
	}
	public String getNeworderText() {
		return null != this.neworder ? NumberUtil.formatToseparaInteger(this.neworder) : "";
	}
	public void setNeworder(Integer neworder) {
		this.neworder = neworder;
	}
	
	public Double getIncomeamount() {
		return incomeamount;
	}
	public String getIncomeamountText() {
		return null != this.incomeamount ? NumberUtil.formatToseparaInteger(this.incomeamount) : "";
	}
	public void setIncomeamount(Double incomeamount) {
		this.incomeamount = incomeamount;
	}
	
	public Double getIncomeincreased() {
		return incomeincreased;
	}
	
	public String getIncomeincreasedText() {
		return null != this.incomeincreased ? NumberUtil.formatTosepara(this.incomeincreased) + "%" : "";
	}
	
	public void setIncomeincreased(Double incomeincreased) {
		this.incomeincreased = incomeincreased;
	}
	
	public Double getIncomepercent() {
		return incomepercent;
	}
	
	public String getIncomepercentText() {
		return null != this.incomepercent ? NumberUtil.formatToseparaInteger(this.incomepercent) : "";
	}

	public void setIncomepercent(Double incomepercent) {
		this.incomepercent = incomepercent;
	}
	
	public Double getLoancost() {
		return loancost;
	}
	public String getLoancostText() {
		return null != this.loancost ? NumberUtil.formatToseparaInteger(this.loancost) : "";
	}
	public void setLoancost(Double loancost) {
		this.loancost = loancost;
	}
	
	public Double getRepaycost() {
		return repaycost;
	}
	public String getRepaycostText() {
		return null != this.repaycost ? NumberUtil.formatToseparaInteger(this.repaycost) : "";
	}
	public void setRepaycost(Double repaycost) {
		this.repaycost = repaycost;
	}
	
	public Double getCreditsumcost() {
		return creditsumcost;
	}
	public String getCreditsumcostText() {
		return null != this.creditsumcost ? NumberUtil.formatToseparaInteger(this.creditsumcost) : "";
	}
	public void setCreditsumcost(Double creditsumcost) {
		this.creditsumcost = creditsumcost;
	}
	
	public Double getCreditavgcost() {
		return creditavgcost;
	}
	public String getCreditavgcostText() {
		return null != this.creditavgcost ? NumberUtil.formatToseparaInteger(this.creditavgcost) : "";
	}
	public void setCreditavgcost(Double creditavgcost) {
		this.creditavgcost = creditavgcost;
	}
	
	public Double getMonthCreditAvgCost() {
		return MonthCreditAvgCost;
	}
	public String getMonthCreditAvgCostText() {
		return null != this.MonthCreditAvgCost ? NumberUtil.formatToseparaInteger(this.MonthCreditAvgCost): "";
	}
	public void setMonthCreditAvgCost(Double monthCreditAvgCost) {
		MonthCreditAvgCost = monthCreditAvgCost;
	}
	
	public Double getCouponcost() {
		return couponcost;
	}
	public String getCouponcostText() {
		return null != this.couponcost ? NumberUtil.formatToseparaInteger(this.couponcost) : "";
	}
	public void setCouponcost(Double couponcost) {
		this.couponcost = couponcost;
	}
	
	public Double getMediacost() {
		return mediacost;
	}
	public String getMediacostText() {
		return null != this.mediacost ? NumberUtil.formatToseparaInteger(this.mediacost) : "";
	}
	public void setMediacost(Double mediacost) {
		this.mediacost = mediacost;
	}
	
	public Double getDebatamount() {
		return debatamount;
	}
	public String getDebatamountText() {
		return null != this.debatamount ? NumberUtil.formatToseparaInteger(this.debatamount) : "";
	}
	public void setDebatamount(Double debatamount) {
		this.debatamount = debatamount;
	}
	
	public Double getWeekdebatamount() {
		return weekdebatamount;
	}
	public String getWeekdebatamountText() {
		return null != this.weekdebatamount ? NumberUtil.formatToseparaInteger(this.weekdebatamount) : "";
	}
	public void setWeekdebatamount(Double weekdebatamount) {
		this.weekdebatamount = weekdebatamount;
	}
	
	public Double getMonthdebatamount() {
		return monthdebatamount;
	}
	public String getMonthdebatamountText() {
		return null != this.monthdebatamount ? NumberUtil.formatToseparaInteger(this.monthdebatamount) : "";
	}
	public void setMonthdebatamount(Double monthdebatamount) {
		this.monthdebatamount = monthdebatamount;
	}
	
	public Double getDebatpercent() {
		return debatpercent;
	}
	public String getDebatpercentText() {
		return null != this.debatpercent ? NumberUtil.formatTosepara(this.debatpercent) + "%" : "";
	}
	public void setDebatpercent(Double debatpercent) {
		this.debatpercent = debatpercent;
	}
	
	public Double getEntrustcommission() {
		return entrustcommission;
	}
	public String getEntrustcommissionText() {
		return null != this.entrustcommission ? NumberUtil.formatToseparaInteger(this.entrustcommission) : "";
	}
	public void setEntrustcommission(Double entrustcommission) {
		this.entrustcommission = entrustcommission;
	}
	
	public Double getGrossprofile() {
		return grossprofile;
	}
	public String getGrossprofileText() {
		return null != this.grossprofile ? NumberUtil.formatToseparaInteger(this.grossprofile) : "";
	}
	public void setGrossprofile(Double grossprofile) {
		this.grossprofile = grossprofile;
	}
	
	public Double getGrossprofilepercent() {
		return grossprofilepercent;
	}
	public String getGrossprofilepercentText() {
		return null != this.grossprofilepercent ? NumberUtil.formatTosepara(this.grossprofilepercent) + "%" : "";
	}
	public void setGrossprofilepercent(Double grossprofilepercent) {
		this.grossprofilepercent = grossprofilepercent;
	}
	
	
	public Double getMonthGrossProfilePercent() {
		return MonthGrossProfilePercent;
	}
	public String getMonthGrossProfilePercentText() {
		return null != this.MonthGrossProfilePercent ? NumberUtil.formatTosepara(this.MonthGrossProfilePercent) + "%" : "";
	}
	public void setMonthGrossProfilePercent(Double monthGrossProfilePercent) {
		MonthGrossProfilePercent = monthGrossProfilePercent;
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

	public String getWeekdesc() {
		return weekdesc;
	}

	public void setWeekdesc(String weekdesc) {
		this.weekdesc = weekdesc;
	}

	public Double getMonthincomeincreased() {
		return monthincomeincreased;
	}
	public String getMonthincomeincreasedText() {
		return null != this.monthincomeincreased ? NumberUtil.formatTosepara(this.monthincomeincreased) + "%" : "";
	}
	public void setMonthincomeincreased(Double monthincomeincreased) {
		this.monthincomeincreased = monthincomeincreased;
	}

	public Double getMonthincomepercent() {
		return monthincomepercent;
	}
	public String getMonthincomepercentText() {
		return null != this.monthincomepercent ? NumberUtil.formatToseparaInteger(this.monthincomepercent) : "";
	}
	public void setMonthincomepercent(Double monthincomepercent) {
		this.monthincomepercent = monthincomepercent;
	}
	
	
	public Double getMonthdebatpercent() {
		return monthdebatpercent;
	}
	public String getMonthdebatpercentText() {
		return null != this.monthdebatpercent ? NumberUtil.formatTosepara(this.monthdebatpercent) + "%" : "";
	}
	public void setMonthdebatpercent(Double monthdebatpercent) {
		this.monthdebatpercent = monthdebatpercent;
	}

	
	public Double getSumremitamount() {
		return sumremitamount;
	}

	public void setSumremitamount(Double sumremitamount) {
		this.sumremitamount = sumremitamount;
	}

	public Double getWeekrepayavgcost() {
		return weekrepayavgcost;
	}
	public String getWeekrepayavgcostText() {
		return null != this.weekrepayavgcost ? NumberUtil.formatTosepara(this.weekrepayavgcost) + "%" : "";
	}
	public void setWeekrepayavgcost(Double weekrepayavgcost) {
		this.weekrepayavgcost = weekrepayavgcost;
	}

	public Double getMonthrepayavgcost() {
		return monthrepayavgcost;
	}
	public String getMonthrepayavgcostText() {
		return null != this.monthrepayavgcost ? NumberUtil.formatTosepara(this.monthrepayavgcost) + "%" : "";
	}
	public void setMonthrepayavgcost(Double monthrepayavgcost) {
		this.monthrepayavgcost = monthrepayavgcost;
	}

	
	public Double getWeekSingleRemitAvgAmount() {
		return WeekSingleRemitAvgAmount;
	}
	public String getWeekSingleRemitAvgAmountText() {
		return null != this.WeekSingleRemitAvgAmount ? NumberUtil.formatToseparaInteger(this.WeekSingleRemitAvgAmount) : "";
	}
	public void setWeekSingleRemitAvgAmount(Double weekSingleRemitAvgAmount) {
		WeekSingleRemitAvgAmount = weekSingleRemitAvgAmount;
	}

	public Double getMonthSingleRemitAvgAmount() {
		return MonthSingleRemitAvgAmount;
	}
	public String getMonthSingleRemitAvgAmountText() {
		return null != this.MonthSingleRemitAvgAmount ? NumberUtil.formatToseparaInteger(this.MonthSingleRemitAvgAmount) : "";
	}
	public void setMonthSingleRemitAvgAmount(Double monthSingleRemitAvgAmount) {
		MonthSingleRemitAvgAmount = monthSingleRemitAvgAmount;
	}

	
	public Double getCashcostamount() {
		return cashcostamount;
	}
	public String getCashcostamountText() {
		return null != this.cashcostamount ? NumberUtil.formatToseparaInteger(this.cashcostamount) : "";
	}
	public void setCashcostamount(Double cashcostamount) {
		this.cashcostamount = cashcostamount;
	}

	public Double getDebatreturnamount() {
		return debatreturnamount;
	}
	public String getDebatreturnamountText() {
		return null != this.debatreturnamount ? NumberUtil.formatToseparaInteger(this.debatreturnamount) : "";
	}
	public void setDebatreturnamount(Double debatreturnamount) {
		this.debatreturnamount = debatreturnamount;
	}




	
		
}