/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.bean;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 财务日报Entity
 * @author 徐盛
 * @version 2016-06-23
 */
public class SCashLoanWeekReportBean extends DataEntity<SCashLoanWeekReportBean> {
	
	private static final long serialVersionUID = 1L;
	private String week;		// week
	private String weekdesc;     //  week-----------------------------------------------------------------------------------------------
	private Double remitamount;		// 交易总额 - 本期累计(本次统计周期内, 累几放贷金额)                    :::::Text  formatTosepara---
	private Double weekincreased;		// 交易总额 - 周增长率 增长率 = 本期累计交易总额 / 上期累计交易总额        :::::Text  *100% 
	private Integer newuser;		// 交易总额 - 新增交易用户 - 本次统计周期内, 新增的放款独立用户数			:::::Text formatTosepara---
	private Integer neworder;		// 交易总额 - 放贷笔数 - 本次统计周期内, 新增的放贷笔数, 包括延期笔数		:::::Text  formatTosepara---
	private Double incomeamount;		// 收入 - 本期收入 - 本次统计周期内, 实际收到的 信审收益 + 延期手续费 + 逾期费 + 委外(部分还款不计入)      :::::Text formatTosepara---
	private Double incomeincreased;		// 收入 - 收入增长率 - 收入增长率 = (本期收入 - 上期收入) / 上期收入       :::::Text  formatTosepara + %
	private Double incomepercent;		// 收入 - 收入比 - 收入比 = 本期收入 / 上上周交易总额					:::::Text  formatTosepara 
	private Double loancost;		// 支付渠道费 - 放款成本 - 放款成本 = 本期放贷笔数(不包括延期) &times; 2		:::::Text formatTosepara---
	private Double repaycost;		// 支付渠道费 - 还款成本 - 使用各支付渠道所产生的支付渠道费						:::::Text formatTosepara---
	private Double creditsumcost;		// 征信成本 - 本期总成本 - 本次统计周期内, 使用第三方征信数据所产生的费用总和                         ====================手入      :::::Text formatTosepara---
	private Double creditavgcost;		// 征信成本 - 人均成本 - 人均成本 = 本期总成本 / 本期新增交易用户数                    :::::Text  formatTosepara 
	private Double couponcost;		// 市场推广 - 免息券 - 本次统计周期内, 被消耗掉的免息券及其他抵扣金额的总和		:::::Text formatTosepara---
	private Double mediacost;		// 市场推广 - 媒体宣传费 - 以我方收到的发票的日期为准                                                    			=====================手入	:::::Text formatTosepara---
	private Double debatamount;		// 坏账 - 目前累计 - 自产品上线之日起, 累计坏账								:::::Text formatTosepara---
	private Double weekdebatamount;		// 坏账 - 周本期累计 - 本期累计 = 本期目前累计坏账 - 上期目前累计坏账		:::::Text formatTosepara---
	private Double debatpercent;		// 周坏账 - 坏账率 - 坏账率 = 目前累计 / 自产品上线之日起的累计交易总额		:::::Text formatTosepara + %
	private Double grossprofile;		// 毛利 - 毛利 - 毛利 = 收入 - 支付渠道费 - 征信成本 - 市场推广 - 坏账本期累计 - 委外佣金		:::::Text formatTosepara---
	private Double grossprofilepercent;		 // 毛利 - 毛利率 - 毛利率 = 毛利 / 收入		:::::Text formatTosepara + %
	private Double weekrepayavgcost;      //  周平均还款费率
	private Double WeekSingleRemitAvgAmount;
	
	private Date beginCreatetime;		// 开始 createtime
	private Date endCreatetime;		// 结束 createtime
	
	public SCashLoanWeekReportBean() {
		super();
	}

	public SCashLoanWeekReportBean(String id){
		super(id);
	}

	
	@Length(min=0, max=30, message="week长度必须介于 0 和 30 之间")
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
	
	@ExcelField(title="日期", type=1, align=2, sort=1)
	public String getWeekdesc() {
		return weekdesc;
	}
	public void setWeekdesc(String weekdesc) {
		this.weekdesc = weekdesc;
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
	
	public Double getWeekincreased() {
		return weekincreased;
	}
	@ExcelField(title="周增长率", type=1, align=2, sort=3)
	public String getWeekincreasedText() {
		return null != this.weekincreased ? NumberUtil.formatTosepara(this.weekincreased) + "%" : "";
	}

	public void setWeekincreased(Double weekincreased) {
		this.weekincreased = weekincreased;
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
	
	public Double getIncomeincreased() {
		return incomeincreased;
	}
	@ExcelField(title="收入增长率", type=1, align=2, sort=7)
	public String getIncomeincreasedText() {
		return null != this.incomeincreased ? NumberUtil.formatTosepara(this.incomeincreased) + "%" : "";
	}
	public void setIncomeincreased(Double incomeincreased) {
		this.incomeincreased = incomeincreased;
	}
	
	public Double getIncomepercent() {
		return incomepercent;
	}
	@ExcelField(title="收入比", type=1, align=2, sort=8)
	public String getIncomepercentText() {
		return null != this.incomepercent ? NumberUtil.formatTosepara(this.incomepercent) : "";
	}
	public void setIncomepercent(Double incomepercent) {
		this.incomepercent = incomepercent;
	}
	
	public Double getLoancost() {
		return loancost;
	}
	@ExcelField(title="放款成本", type=1, align=2, sort=9)
	public String getLoancostText() {
		return null != this.loancost ? NumberUtil.formatTosepara(this.loancost) : "";
	}
	public void setLoancost(Double loancost) {
		this.loancost = loancost;
	}
	
	public Double getRepaycost() {
		return repaycost;
	}
	@ExcelField(title="还款成本", type=1, align=2, sort=10)
	public String getRepaycostText() {
		return null != this.repaycost ? NumberUtil.formatTosepara(this.repaycost) : "";
	}
	public void setRepaycost(Double repaycost) {
		this.repaycost = repaycost;
	}
	
	public Double getCreditsumcost() {
		return creditsumcost;
	}
	@ExcelField(title="本期总成本", type=1, align=2, sort=11)
	public String getCreditsumcostText() {
		return null != this.creditsumcost ? NumberUtil.formatTosepara(this.creditsumcost) : "";
	}
	public void setCreditsumcost(Double creditsumcost) {
		this.creditsumcost = creditsumcost;
	}
	
	public Double getCreditavgcost() {
		return creditavgcost;
	}
	@ExcelField(title="人均成本", type=1, align=2, sort=12)
	public String getCreditavgcostText() {
		return null != this.creditavgcost ? NumberUtil.formatTosepara(this.creditavgcost) : "";
	}
	public void setCreditavgcost(Double creditavgcost) {
		this.creditavgcost = creditavgcost;
	}
	
	public Double getCouponcost() {
		return couponcost;
	}
	@ExcelField(title="免息券", type=1, align=2, sort=13)
	public String getCouponcostText() {
		return null != this.couponcost ? NumberUtil.formatTosepara(this.couponcost) : "";
	}
	public void setCouponcost(Double couponcost) {
		this.couponcost = couponcost;
	}
	
	public Double getMediacost() {
		return mediacost;
	}
	@ExcelField(title="媒体宣传费", type=1, align=2, sort=14)
	public String getMediacostText() {
		return null != this.mediacost ? NumberUtil.formatTosepara(this.mediacost) : "";
	}
	public void setMediacost(Double mediacost) {
		this.mediacost = mediacost;
	}
	
	public Double getDebatamount() {
		return debatamount;
	}
	@ExcelField(title="坏账-目前累计", type=1, align=2, sort=15)
	public String getDebatamountText() {
		return null != this.debatamount ? NumberUtil.formatTosepara(this.debatamount) : "";
	}
	public void setDebatamount(Double debatamount) {
		this.debatamount = debatamount;
	}
	
	public Double getWeekdebatamount() {
		return weekdebatamount;
	}
	@ExcelField(title="坏账-周本期累计", type=1, align=2, sort=16)
	public String getWeekdebatamountText() {
		return null != this.weekdebatamount ? NumberUtil.formatTosepara(this.weekdebatamount) : "";
	}
	public void setWeekdebatamount(Double weekdebatamount) {
		this.weekdebatamount = weekdebatamount;
	}
	
	
	public Double getDebatpercent() {
		return debatpercent;
	}
	@ExcelField(title="周坏账-坏账率", type=1, align=2, sort=17)
	public String getDebatpercentText() {
		return null != this.debatpercent ? NumberUtil.formatTosepara(this.debatpercent) + "%" : "";
	}
	public void setDebatpercent(Double debatpercent) {
		this.debatpercent = debatpercent;
	}
	
	public Double getGrossprofile() {
		return grossprofile;
	}
	@ExcelField(title="毛利", type=1, align=2, sort=18)
	public String getGrossprofileText() {
		return null != this.grossprofile ? NumberUtil.formatTosepara(this.grossprofile) : "";
	}
	public void setGrossprofile(Double grossprofile) {
		this.grossprofile = grossprofile;
	}
	
	public Double getGrossprofilepercent() {
		return grossprofilepercent;
	}
	@ExcelField(title="毛利率 ", type=1, align=2, sort=19)
	public String getGrossprofilepercentText() {
		return null != this.grossprofilepercent ? NumberUtil.formatTosepara(this.grossprofilepercent) + "%" : "";
	}
	public void setGrossprofilepercent(Double grossprofilepercent) {
		this.grossprofilepercent = grossprofilepercent;
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


	public Double getWeekrepayavgcost() {
		return weekrepayavgcost;
	}
	@ExcelField(title="周平均还款费率", type=1, align=2, sort=20)
	public String getWeekrepayavgcostText() {
		return null != this.weekrepayavgcost ? NumberUtil.formatTosepara(this.weekrepayavgcost) + "%" : "";
	}
	public void setWeekrepayavgcost(Double weekrepayavgcost) {
		this.weekrepayavgcost = weekrepayavgcost;
	}

	
	public Double getWeekSingleRemitAvgAmount() {
		return WeekSingleRemitAvgAmount;
	}
	@ExcelField(title="平均单笔放贷金额", type=1, align=2, sort=21)
	public String getWeekSingleRemitAvgAmountText() {
		return null != this.WeekSingleRemitAvgAmount ? NumberUtil.formatTosepara(this.WeekSingleRemitAvgAmount) : "";
	}
	public void setWeekSingleRemitAvgAmount(Double weekSingleRemitAvgAmount) {
		WeekSingleRemitAvgAmount = weekSingleRemitAvgAmount;
	}


	
		
}