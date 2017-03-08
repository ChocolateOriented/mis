/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.bean;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 财务日报Entity
 * @author 徐盛
 * @version 2016-06-23
 */
public class SCashLoanMonthReportBean extends DataEntity<SCashLoanMonthReportBean> {
	
	private static final long serialVersionUID = 1L;
	private String month;		// month
	private Double remitamount;		// 交易总额 - 本期累计(本次统计周期内, 累几放贷金额)                    :::::Text  formatTosepara---
	private Double monthincreased;		// 交易总额 - 月增长率 增长率 = 本期累计交易总额 / 上期累计交易总额        :::::Text  *100% 
	private Integer newuser;		// 交易总额 - 新增交易用户 - 本次统计周期内, 新增的放款独立用户数			:::::Text formatTosepara---
	private Integer neworder;		// 交易总额 - 放贷笔数 - 本次统计周期内, 新增的放贷笔数, 包括延期笔数		:::::Text  formatTosepara---
	private Double incomeamount;		// 收入 - 本期收入 - 本次统计周期内, 实际收到的 信审收益 + 延期手续费 + 逾期费 + 委外(部分还款不计入)      :::::Text formatTosepara---
	private Double monthincomeincreased; //    ------------------------------------------------------------- :::::Text  formatTosepara + %
	private Double monthincomepercent; //    -----------------------------------------------------------------:::::Text  formatTosepara + %
	private Double loancost;		// 支付渠道费 - 放款成本 - 放款成本 = 本期放贷笔数(不包括延期) &times; 2		:::::Text formatTosepara---
	private Double repaycost;		// 支付渠道费 - 还款成本 - 使用各支付渠道所产生的支付渠道费						:::::Text formatTosepara---
	private Double creditsumcost;		// 征信成本 - 本期总成本 - 本次统计周期内, 使用第三方征信数据所产生的费用总和                         ====================手入      :::::Text formatTosepara---
	private Double MonthCreditAvgCost;  // 征信成本 - 人均成本 - 人均成本 = 本期总成本 / 本期新增交易用户数                    :::::Text  formatTosepara + %
	private Double couponcost;		// 市场推广 - 免息券 - 本次统计周期内, 被消耗掉的免息券及其他抵扣金额的总和		:::::Text formatTosepara---
	private Double mediacost;		// 市场推广 - 媒体宣传费 - 以我方收到的发票的日期为准                                                    			=====================手入	:::::Text formatTosepara---
	private Double debatamount;		// 坏账 - 目前累计 - 自产品上线之日起, 累计坏账								:::::Text formatTosepara---
	private Double monthdebatamount;		// 坏账 - 月本期累计 - 本期累计 = 本期目前累计坏账 -  上期目前累计坏账		:::::Text formatTosepara---
	private Double monthdebatpercent;   // 月坏账 - 坏账率 - 坏账率 = 目前累计 / 自产品上线之日起的累计交易总额		:::::Text formatTosepara + %
	private Double entrustcommission;		// 坏账 - 委外佣金 - 委外回款所扣除的佣金总金额, 佣金月结                          :::::Text formatTosepara------------------------------------- 按月-=====================手入
	private Double grossprofile;		// 毛利 - 毛利 - 毛利 = 收入 - 支付渠道费 - 征信成本 - 市场推广 - 坏账本期累计 - 委外佣金		:::::Text formatTosepara---
	private Double MonthGrossProfilePercent; // 毛利 - 毛利率 - 毛利率 = 毛利 / 收入		:::::Text formatTosepara + %
	private Double monthrepayavgcost;     //  月平均还款费率
	private Double MonthSingleRemitAvgAmount;
	
	
	private Double cashcostamount;  // 资金成本
	private Date beginCreatetime;		// 开始 createtime
	private Date endCreatetime;		// 结束 createtime
	
	
	private Double debatreturnamount;
	
	public SCashLoanMonthReportBean() {
		super();
	}

	public SCashLoanMonthReportBean(String id){
		super(id);
	}

	
	@Length(min=0, max=8, message="month长度必须介于 0 和 8 之间")
	@ExcelField(title="日期", type=1, align=2, sort=1)
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	
	public Double getRemitamount() {
		return remitamount;
	}
	@ExcelField(title="本期累计", type=1, align=2, sort=2)
	public String getRemitamountText() {
		return null != this.remitamount ? NumberUtil.formatTosepara(this.remitamount) : "";
	}
	public void setRemitamount(Double remitamount) {
		this.remitamount = remitamount;
	}
	
	
	public Double getMonthincreased() {
		return monthincreased;
	}
	@ExcelField(title="月增长率", type=1, align=2, sort=3)
	public String getMonthincreasedText() {
		return null != this.monthincreased ? NumberUtil.formatTosepara(this.monthincreased) + "%" : "";
	}
	public void setMonthincreased(Double monthincreased) {
		this.monthincreased = monthincreased;
	}
	
	public Integer getNewuser() {
		return newuser;
	}
	@ExcelField(title="新增交易用户", type=1, align=2, sort=4)
	public String getNewuserText() {
		return null != this.newuser ? NumberUtil.formatTosepara(this.newuser) : "";
	}
	public void setNewuser(Integer newuser) {
		this.newuser = newuser;
	}
	
	public Integer getNeworder() {
		return neworder;
	}
	@ExcelField(title="放贷笔数", type=1, align=2, sort=5)
	public String getNeworderText() {
		return null != this.neworder ? NumberUtil.formatTosepara(this.neworder) : "";
	}
	public void setNeworder(Integer neworder) {
		this.neworder = neworder;
	}
	
	public Double getIncomeamount() {
		return incomeamount;
	}
	@ExcelField(title="本期收入", type=1, align=2, sort=6)
	public String getIncomeamountText() {
		return null != this.incomeamount ? NumberUtil.formatTosepara(this.incomeamount) : "";
	}
	public void setIncomeamount(Double incomeamount) {
		this.incomeamount = incomeamount;
	}
	
	
	public Double getLoancost() {
		return loancost;
	}
	@ExcelField(title="放款成本", type=1, align=2, sort=7)
	public String getLoancostText() {
		return null != this.loancost ? NumberUtil.formatTosepara(this.loancost) : "";
	}
	public void setLoancost(Double loancost) {
		this.loancost = loancost;
	}
	
	public Double getRepaycost() {
		return repaycost;
	}
	@ExcelField(title="还款成本", type=1, align=2, sort=8)
	public String getRepaycostText() {
		return null != this.repaycost ? NumberUtil.formatTosepara(this.repaycost) : "";
	}
	public void setRepaycost(Double repaycost) {
		this.repaycost = repaycost;
	}
	
	public Double getCreditsumcost() {
		return creditsumcost;
	}
	@ExcelField(title="本期总成本", type=1, align=2, sort=9)
	public String getCreditsumcostText() {
		return null != this.creditsumcost ? NumberUtil.formatTosepara(this.creditsumcost) : "";
	}
	public void setCreditsumcost(Double creditsumcost) {
		this.creditsumcost = creditsumcost;
	}
	
	public Double getMonthCreditAvgCost() {
		return MonthCreditAvgCost;
	}
	@ExcelField(title="人均成本", type=1, align=2, sort=10)
	public String getMonthCreditAvgCostText() {
		return null != this.MonthCreditAvgCost ? NumberUtil.formatTosepara(this.MonthCreditAvgCost): "";
	}
	public void setMonthCreditAvgCost(Double monthCreditAvgCost) {
		MonthCreditAvgCost = monthCreditAvgCost;
	}
	
	public Double getCouponcost() {
		return couponcost;
	}
	@ExcelField(title="免息券", type=1, align=2, sort=11)
	public String getCouponcostText() {
		return null != this.couponcost ? NumberUtil.formatTosepara(this.couponcost) : "";
	}
	public void setCouponcost(Double couponcost) {
		this.couponcost = couponcost;
	}
	
	public Double getMediacost() {
		return mediacost;
	}
	@ExcelField(title="媒体宣传费", type=1, align=2, sort=12)
	public String getMediacostText() {
		return null != this.mediacost ? NumberUtil.formatTosepara(this.mediacost) : "";
	}
	public void setMediacost(Double mediacost) {
		this.mediacost = mediacost;
	}
	
	public Double getDebatamount() {
		return debatamount;
	}
	@ExcelField(title="坏账-目前累计", type=1, align=2, sort=13)
	public String getDebatamountText() {
		return null != this.debatamount ? NumberUtil.formatTosepara(this.debatamount) : "";
	}
	public void setDebatamount(Double debatamount) {
		this.debatamount = debatamount;
	}
	
	
	public Double getMonthdebatamount() {
		return monthdebatamount;
	}
	@ExcelField(title="坏账-月本期累计", type=1, align=2, sort=14)
	public String getMonthdebatamountText() {
		return null != this.monthdebatamount ? NumberUtil.formatTosepara(this.monthdebatamount) : "";
	}
	public void setMonthdebatamount(Double monthdebatamount) {
		this.monthdebatamount = monthdebatamount;
	}
	
	public Double getEntrustcommission() {
		return entrustcommission;
	}
	@ExcelField(title="坏账-委外佣金", type=1, align=2, sort=15)
	public String getEntrustcommissionText() {
		return null != this.entrustcommission ? NumberUtil.formatTosepara(this.entrustcommission) : "";
	}
	public void setEntrustcommission(Double entrustcommission) {
		this.entrustcommission = entrustcommission;
	}
	
	public Double getGrossprofile() {
		return grossprofile;
	}
	@ExcelField(title="毛利", type=1, align=2, sort=16)
	public String getGrossprofileText() {
		return null != this.grossprofile ? NumberUtil.formatTosepara(this.grossprofile) : "";
	}
	public void setGrossprofile(Double grossprofile) {
		this.grossprofile = grossprofile;
	}
	
	
	public Double getMonthGrossProfilePercent() {
		return MonthGrossProfilePercent;
	}
	@ExcelField(title="毛利率", type=1, align=2, sort=17)
	public String getMonthGrossProfilePercentText() {
		return null != this.MonthGrossProfilePercent ? NumberUtil.formatTosepara(this.MonthGrossProfilePercent) + "%" : "";
	}
	public void setMonthGrossProfilePercent(Double monthGrossProfilePercent) {
		MonthGrossProfilePercent = monthGrossProfilePercent;
	}
	

	public Double getMonthincomeincreased() {
		return monthincomeincreased;
	}
	@ExcelField(title="月收入增长率", type=1, align=2, sort=18)
	public String getMonthincomeincreasedText() {
		return null != this.monthincomeincreased ? NumberUtil.formatTosepara(this.monthincomeincreased) + "%" : "";
	}
	public void setMonthincomeincreased(Double monthincomeincreased) {
		this.monthincomeincreased = monthincomeincreased;
	}

	public Double getMonthincomepercent() {
		return monthincomepercent;
	}
	@ExcelField(title="月单笔收入", type=1, align=2, sort=19)
	public String getMonthincomepercentText() {
		return null != this.monthincomepercent ? NumberUtil.formatTosepara(this.monthincomepercent) : "";
	}
	public void setMonthincomepercent(Double monthincomepercent) {
		this.monthincomepercent = monthincomepercent;
	}
	
	
	public Double getMonthdebatpercent() {
		return monthdebatpercent;
	}
	@ExcelField(title="月坏账-坏账率", type=1, align=2, sort=20)
	public String getMonthdebatpercentText() {
		return null != this.monthdebatpercent ? NumberUtil.formatTosepara(this.monthdebatpercent) + "%" : "";
	}
	public void setMonthdebatpercent(Double monthdebatpercent) {
		this.monthdebatpercent = monthdebatpercent;
	}

	public Double getMonthrepayavgcost() {
		return monthrepayavgcost;
	}
	@ExcelField(title="月平均还款费率", type=1, align=2, sort=21)
	public String getMonthrepayavgcostText() {
		return null != this.monthrepayavgcost ? NumberUtil.formatTosepara(this.monthrepayavgcost) + "%" : "";
	}
	public void setMonthrepayavgcost(Double monthrepayavgcost) {
		this.monthrepayavgcost = monthrepayavgcost;
	}

	

	public Double getMonthSingleRemitAvgAmount() {
		return MonthSingleRemitAvgAmount;
	}
	@ExcelField(title="平均单笔放贷金额", type=1, align=2, sort=22)
	public String getMonthSingleRemitAvgAmountText() {
		return null != this.MonthSingleRemitAvgAmount ? NumberUtil.formatTosepara(this.MonthSingleRemitAvgAmount) : "";
	}
	public void setMonthSingleRemitAvgAmount(Double monthSingleRemitAvgAmount) {
		MonthSingleRemitAvgAmount = monthSingleRemitAvgAmount;
	}

	
	public Double getCashcostamount() {
		return cashcostamount;
	}
	@ExcelField(title="资金成本 ", type=1, align=2, sort=23)
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