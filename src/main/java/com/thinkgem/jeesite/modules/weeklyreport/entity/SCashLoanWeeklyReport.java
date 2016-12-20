/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 现金贷款周报Entity
 * @author 徐盛
 * @version 2016-06-06
 */
public class SCashLoanWeeklyReport extends DataEntity<SCashLoanWeeklyReport> {
	
	private static final long serialVersionUID = 1L;
	private String intervaldatetime;		// 时间区间 例如2016 5/27 ~ 6/2
	private Integer ordernum;		// 累积放款订单数, 不包括延期
	private Integer ordernumincludedelay;		// 累积放款订单数, 包括延期
	private Integer ordernumnewuser;		// 本周期内, 累积放 款订单笔数中, 新用户订单笔数                                1  2
	private Integer ordernumolduserincludedelay;		// 本周期内, 累积放款订单笔数中, 老用户订单笔数(含延期)      1
	private Integer ordernumolduser;		// 本周期内, 累积放款订单笔数中, 老用户订单笔数(不含延期)                2
	private Integer ordernumpayoffincludedelay;		// 自上线之日起, 累积还清订单笔数(含延期)
	private Integer ordernumpayoff;		// 自上线之日起, 累积还清订单笔数(不含延期)
	private Integer ordernumpayoffperiodincludedelay;		// 本周期内, 累积已还清订单笔数(含延期)
	private Integer ordernumpayoffperiod;		// 本周期内, 累积已还清订单笔数(不含延期)
	private Integer ordernumoverdue;		// 当前处于逾期状态的订单笔数
	private Integer ordernumoutsource;		// 当前处于委外状态的订单笔数
	private Integer ordernumneworderperiod;		// 本次统计周期内, 新增申请订单笔数
	private Integer ordernumneworderperiodappincludedelay;		// 本次统计周期内, 新增申请订单笔数中,订单来源为APP的笔数(含延期)
	private Double ordernumneworderperiodapppercentincludedelay;		// 本次统计周期内, 新增申请订单笔数中,订单来源为APP的占比(含延期)
	private Integer ordernumneworderperiodapp;		// 本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为APP的笔数(不含延期)
	private Double ordernumneworderperiodapppercent;		// 本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为APP的占比(不含延期)
	private Integer ordernumneworderperiodwechatincludedelay;		// 本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的笔数(含延期)
	private Double ordernumneworderperiodwechatpercentincludedelay;		// 本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的占比(含延期)
	private Integer ordernumneworderperiodwechat;		// 本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的笔数(不含延期)
	private Double ordernumneworderperiodwechatpercent;		// 本次统计周期内, 新增(包含哪些状态)申请订单笔数中, 订单来源为Wechat的占比(不含延期)
	private Integer ordernumneworderperiodamount500includedelay;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为500元的笔数(含延期)
	private Double ordernumneworderperiodamount500percentincludedelay;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为500元的占比(含延期)
	private Integer ordernumneworderperiodamount500;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为500元的笔数(不含延期)
	private Double ordernumneworderperiodamount500percent;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为500元的占比(不含延期)
	private Integer ordernumneworderperiodamount1000includedelay;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的笔数(含延期)
	private Double ordernumneworderperiodamount1000percentincludedelay;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的占比(含延期)
	private Integer ordernumneworderperiodamount1000;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的笔数(不含延期)
	private Double ordernumneworderperiodamount1000percent;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为1000元的占比(不含延期)
	private Integer ordernumneworderperiodamount1500includedelay;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的笔数(含延期)
	private Double ordernumneworderperiodamount1500percentincludedelay;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的占比(含延期)
	private Integer ordernumneworderperiodamount1500;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的笔数(不含延期)
	private Double ordernumneworderperiodamount1500percent;		// 本次统计周期内，新增申请订单笔数中，订单申请金额为1500元的占比(不含延期)
	private Integer ordernumneworderperiodinterval7includedelay;		// 本次统计周期内，新增申请订单笔数中，订单借款时长为7天的笔数(含延期)
	private Double ordernumneworderperiodinterval7percentincludedelay;		// 本次统计周期内，新增申请订单笔数中，订单借款时长为7天的占比(含延期)
	private Integer ordernumneworderperiodinterval7;		// 本次统计周期内，新增申请订单笔数中，订单借款时长为7天的笔数(不含延期)
	private Double ordernumneworderperiodinterval7percent;		// 本次统计周期内，新增申请订单笔数中，订单借款时长为7天的占比(不含延期)
	private Integer ordernumneworderperiodinterval14includedelay;		// 本次统计周期内，新增申请订单笔数中，订单借款时长为14天的笔数(含延期)
	private Double ordernumneworderperiodinterval14percentincludedelay;		// 本次统计周期内，新增申请订单笔数中，订单借款时长为14天的占比(含延期)
	private Integer ordernumneworderperiodinterval14;		// 本次统计周期内，新增申请订单笔数中，订单借款时长为14天的笔数(不含延期)
	private Double ordernumneworderperiodinterval14percent;		// 本次统计周期内，新增申请订单笔数中，订单借款时长为14天的占比(不含延期)
	private Integer singleusernum;		// 自产品上线之日起，累计放款独立用户数
	private Double amountnotrecovered;		// 到本次统计周期截止日次日0点之前，未收回的全部贷款金额
	private Double amountnotrecoveredoutsource;		// 到本次统计周期截止日次日0点之前，委派第三方进行催收，并且未在本方系统中结算的全部金额
	private Double amountperiodincreasedincludedelay;		// 本次统计周期内，新增贷款余额(含延期)
	private Double amountperiodincreased;		// 本次统计周期内，新增贷款余额(不含延期)
	private Double amountallincome;		// 总收益
	private Double amountcreditincome;		// 信审收益
	private Double amountdelayincome;		// 延期收益
	private Double amountoverdueincome;		// 逾期收益
	private Double amountoutsourceincome;		// 委外收益
	private Double ordercancelperiodpercentapp;		// 统计周期内App订单取消率
	private Double ordercancelperioadpercentwechat;		// 统计周期内微信订单取消率
	private Date createtime;		// createtime
	
	public SCashLoanWeeklyReport() {
		super();
	}

	public SCashLoanWeeklyReport(String id){
		super(id);
	}

	@Length(min=0, max=20, message="时间区间 例如2016 5/27 ~ 6/2长度必须介于 0 和 20 之间")
	public String getIntervaldatetime() {
		return intervaldatetime;
	}

	public void setIntervaldatetime(String intervaldatetime) {
		this.intervaldatetime = intervaldatetime;
	}
	
	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}
	
	public Integer getOrdernumincludedelay() {
		return ordernumincludedelay;
	}

	public void setOrdernumincludedelay(Integer ordernumincludedelay) {
		this.ordernumincludedelay = ordernumincludedelay;
	}
	
	public Integer getOrdernumnewuser() {
		return ordernumnewuser;
	}

	public void setOrdernumnewuser(Integer ordernumnewuser) {
		this.ordernumnewuser = ordernumnewuser;
	}
	
	public Integer getOrdernumolduserincludedelay() {
		return ordernumolduserincludedelay;
	}

	public void setOrdernumolduserincludedelay(Integer ordernumolduserincludedelay) {
		this.ordernumolduserincludedelay = ordernumolduserincludedelay;
	}
	
	public Integer getOrdernumolduser() {
		return ordernumolduser;
	}

	public void setOrdernumolduser(Integer ordernumolduser) {
		this.ordernumolduser = ordernumolduser;
	}
	
	public Integer getOrdernumpayoffincludedelay() {
		return ordernumpayoffincludedelay;
	}

	public void setOrdernumpayoffincludedelay(Integer ordernumpayoffincludedelay) {
		this.ordernumpayoffincludedelay = ordernumpayoffincludedelay;
	}
	
	public Integer getOrdernumpayoff() {
		return ordernumpayoff;
	}

	public void setOrdernumpayoff(Integer ordernumpayoff) {
		this.ordernumpayoff = ordernumpayoff;
	}
	
	public Integer getOrdernumpayoffperiodincludedelay() {
		return ordernumpayoffperiodincludedelay;
	}

	public void setOrdernumpayoffperiodincludedelay(Integer ordernumpayoffperiodincludedelay) {
		this.ordernumpayoffperiodincludedelay = ordernumpayoffperiodincludedelay;
	}
	
	public Integer getOrdernumpayoffperiod() {
		return ordernumpayoffperiod;
	}

	public void setOrdernumpayoffperiod(Integer ordernumpayoffperiod) {
		this.ordernumpayoffperiod = ordernumpayoffperiod;
	}
	
	public Integer getOrdernumoverdue() {
		return ordernumoverdue;
	}

	public void setOrdernumoverdue(Integer ordernumoverdue) {
		this.ordernumoverdue = ordernumoverdue;
	}
	
	public Integer getOrdernumoutsource() {
		return ordernumoutsource;
	}

	public void setOrdernumoutsource(Integer ordernumoutsource) {
		this.ordernumoutsource = ordernumoutsource;
	}
	
	public Integer getOrdernumneworderperiod() {
		return ordernumneworderperiod;
	}

	public void setOrdernumneworderperiod(Integer ordernumneworderperiod) {
		this.ordernumneworderperiod = ordernumneworderperiod;
	}
	
	public Integer getOrdernumneworderperiodappincludedelay() {
		return ordernumneworderperiodappincludedelay;
	}

	public void setOrdernumneworderperiodappincludedelay(Integer ordernumneworderperiodappincludedelay) {
		this.ordernumneworderperiodappincludedelay = ordernumneworderperiodappincludedelay;
	}
	
	public Double getOrdernumneworderperiodapppercentincludedelay() {
		return ordernumneworderperiodapppercentincludedelay;
	}

	public void setOrdernumneworderperiodapppercentincludedelay(Double ordernumneworderperiodapppercentincludedelay) {
		this.ordernumneworderperiodapppercentincludedelay = ordernumneworderperiodapppercentincludedelay;
	}
	
	public Integer getOrdernumneworderperiodapp() {
		return ordernumneworderperiodapp;
	}

	public void setOrdernumneworderperiodapp(Integer ordernumneworderperiodapp) {
		this.ordernumneworderperiodapp = ordernumneworderperiodapp;
	}
	
	public Double getOrdernumneworderperiodapppercent() {
		return ordernumneworderperiodapppercent;
	}

	public void setOrdernumneworderperiodapppercent(Double ordernumneworderperiodapppercent) {
		this.ordernumneworderperiodapppercent = ordernumneworderperiodapppercent;
	}
	
	public Integer getOrdernumneworderperiodwechatincludedelay() {
		return ordernumneworderperiodwechatincludedelay;
	}

	public void setOrdernumneworderperiodwechatincludedelay(Integer ordernumneworderperiodwechatincludedelay) {
		this.ordernumneworderperiodwechatincludedelay = ordernumneworderperiodwechatincludedelay;
	}
	
	public Double getOrdernumneworderperiodwechatpercentincludedelay() {
		return ordernumneworderperiodwechatpercentincludedelay;
	}

	public void setOrdernumneworderperiodwechatpercentincludedelay(Double ordernumneworderperiodwechatpercentincludedelay) {
		this.ordernumneworderperiodwechatpercentincludedelay = ordernumneworderperiodwechatpercentincludedelay;
	}
	
	public Integer getOrdernumneworderperiodwechat() {
		return ordernumneworderperiodwechat;
	}

	public void setOrdernumneworderperiodwechat(Integer ordernumneworderperiodwechat) {
		this.ordernumneworderperiodwechat = ordernumneworderperiodwechat;
	}
	
	public Double getOrdernumneworderperiodwechatpercent() {
		return ordernumneworderperiodwechatpercent;
	}

	public void setOrdernumneworderperiodwechatpercent(Double ordernumneworderperiodwechatpercent) {
		this.ordernumneworderperiodwechatpercent = ordernumneworderperiodwechatpercent;
	}
	
	public Integer getOrdernumneworderperiodamount500includedelay() {
		return ordernumneworderperiodamount500includedelay;
	}

	public void setOrdernumneworderperiodamount500includedelay(Integer ordernumneworderperiodamount500includedelay) {
		this.ordernumneworderperiodamount500includedelay = ordernumneworderperiodamount500includedelay;
	}
	
	public Double getOrdernumneworderperiodamount500percentincludedelay() {
		return ordernumneworderperiodamount500percentincludedelay;
	}

	public void setOrdernumneworderperiodamount500percentincludedelay(Double ordernumneworderperiodamount500percentincludedelay) {
		this.ordernumneworderperiodamount500percentincludedelay = ordernumneworderperiodamount500percentincludedelay;
	}
	
	public Integer getOrdernumneworderperiodamount500() {
		return ordernumneworderperiodamount500;
	}

	public void setOrdernumneworderperiodamount500(Integer ordernumneworderperiodamount500) {
		this.ordernumneworderperiodamount500 = ordernumneworderperiodamount500;
	}
	
	public Double getOrdernumneworderperiodamount500percent() {
		return ordernumneworderperiodamount500percent;
	}

	public void setOrdernumneworderperiodamount500percent(Double ordernumneworderperiodamount500percent) {
		this.ordernumneworderperiodamount500percent = ordernumneworderperiodamount500percent;
	}
	
	public Integer getOrdernumneworderperiodamount1000includedelay() {
		return ordernumneworderperiodamount1000includedelay;
	}

	public void setOrdernumneworderperiodamount1000includedelay(Integer ordernumneworderperiodamount1000includedelay) {
		this.ordernumneworderperiodamount1000includedelay = ordernumneworderperiodamount1000includedelay;
	}
	
	public Double getOrdernumneworderperiodamount1000percentincludedelay() {
		return ordernumneworderperiodamount1000percentincludedelay;
	}

	public void setOrdernumneworderperiodamount1000percentincludedelay(Double ordernumneworderperiodamount1000percentincludedelay) {
		this.ordernumneworderperiodamount1000percentincludedelay = ordernumneworderperiodamount1000percentincludedelay;
	}
	
	public Integer getOrdernumneworderperiodamount1000() {
		return ordernumneworderperiodamount1000;
	}

	public void setOrdernumneworderperiodamount1000(Integer ordernumneworderperiodamount1000) {
		this.ordernumneworderperiodamount1000 = ordernumneworderperiodamount1000;
	}
	
	public Double getOrdernumneworderperiodamount1000percent() {
		return ordernumneworderperiodamount1000percent;
	}

	public void setOrdernumneworderperiodamount1000percent(Double ordernumneworderperiodamount1000percent) {
		this.ordernumneworderperiodamount1000percent = ordernumneworderperiodamount1000percent;
	}
	
	public Integer getOrdernumneworderperiodamount1500includedelay() {
		return ordernumneworderperiodamount1500includedelay;
	}

	public void setOrdernumneworderperiodamount1500includedelay(Integer ordernumneworderperiodamount1500includedelay) {
		this.ordernumneworderperiodamount1500includedelay = ordernumneworderperiodamount1500includedelay;
	}
	
	public Double getOrdernumneworderperiodamount1500percentincludedelay() {
		return ordernumneworderperiodamount1500percentincludedelay;
	}

	public void setOrdernumneworderperiodamount1500percentincludedelay(Double ordernumneworderperiodamount1500percentincludedelay) {
		this.ordernumneworderperiodamount1500percentincludedelay = ordernumneworderperiodamount1500percentincludedelay;
	}
	
	public Integer getOrdernumneworderperiodamount1500() {
		return ordernumneworderperiodamount1500;
	}

	public void setOrdernumneworderperiodamount1500(Integer ordernumneworderperiodamount1500) {
		this.ordernumneworderperiodamount1500 = ordernumneworderperiodamount1500;
	}
	
	public Double getOrdernumneworderperiodamount1500percent() {
		return ordernumneworderperiodamount1500percent;
	}

	public void setOrdernumneworderperiodamount1500percent(Double ordernumneworderperiodamount1500percent) {
		this.ordernumneworderperiodamount1500percent = ordernumneworderperiodamount1500percent;
	}
	
	public Integer getOrdernumneworderperiodinterval7includedelay() {
		return ordernumneworderperiodinterval7includedelay;
	}

	public void setOrdernumneworderperiodinterval7includedelay(Integer ordernumneworderperiodinterval7includedelay) {
		this.ordernumneworderperiodinterval7includedelay = ordernumneworderperiodinterval7includedelay;
	}
	
	public Double getOrdernumneworderperiodinterval7percentincludedelay() {
		return ordernumneworderperiodinterval7percentincludedelay;
	}

	public void setOrdernumneworderperiodinterval7percentincludedelay(Double ordernumneworderperiodinterval7percentincludedelay) {
		this.ordernumneworderperiodinterval7percentincludedelay = ordernumneworderperiodinterval7percentincludedelay;
	}
	
	public Integer getOrdernumneworderperiodinterval7() {
		return ordernumneworderperiodinterval7;
	}

	public void setOrdernumneworderperiodinterval7(Integer ordernumneworderperiodinterval7) {
		this.ordernumneworderperiodinterval7 = ordernumneworderperiodinterval7;
	}
	
	public Double getOrdernumneworderperiodinterval7percent() {
		return ordernumneworderperiodinterval7percent;
	}

	public void setOrdernumneworderperiodinterval7percent(Double ordernumneworderperiodinterval7percent) {
		this.ordernumneworderperiodinterval7percent = ordernumneworderperiodinterval7percent;
	}
	
	public Integer getOrdernumneworderperiodinterval14includedelay() {
		return ordernumneworderperiodinterval14includedelay;
	}

	public void setOrdernumneworderperiodinterval14includedelay(Integer ordernumneworderperiodinterval14includedelay) {
		this.ordernumneworderperiodinterval14includedelay = ordernumneworderperiodinterval14includedelay;
	}
	
	public Double getOrdernumneworderperiodinterval14percentincludedelay() {
		return ordernumneworderperiodinterval14percentincludedelay;
	}

	public void setOrdernumneworderperiodinterval14percentincludedelay(Double ordernumneworderperiodinterval14percentincludedelay) {
		this.ordernumneworderperiodinterval14percentincludedelay = ordernumneworderperiodinterval14percentincludedelay;
	}
	
	public Integer getOrdernumneworderperiodinterval14() {
		return ordernumneworderperiodinterval14;
	}

	public void setOrdernumneworderperiodinterval14(Integer ordernumneworderperiodinterval14) {
		this.ordernumneworderperiodinterval14 = ordernumneworderperiodinterval14;
	}
	
	public Double getOrdernumneworderperiodinterval14percent() {
		return ordernumneworderperiodinterval14percent;
	}

	public void setOrdernumneworderperiodinterval14percent(Double ordernumneworderperiodinterval14percent) {
		this.ordernumneworderperiodinterval14percent = ordernumneworderperiodinterval14percent;
	}
	
	public Integer getSingleusernum() {
		return singleusernum;
	}

	public void setSingleusernum(Integer singleusernum) {
		this.singleusernum = singleusernum;
	}
	
	public Double getAmountnotrecovered() {
		return amountnotrecovered;
	}
	// 会计显示
	public String getAmountnotrecoveredText() {
		return !"".equals(this.amountnotrecovered.toString()) ? NumberUtil.formatTosepara(this.amountnotrecovered) : this.amountnotrecovered.toString();
	}

	public void setAmountnotrecovered(Double amountnotrecovered) {
		this.amountnotrecovered = amountnotrecovered;
	}
	
	public Double getAmountnotrecoveredoutsource() {
		return amountnotrecoveredoutsource;
	}
	// 会计显示
	public String getAmountnotrecoveredoutsourceText() {
		return !"".equals(this.amountnotrecoveredoutsource.toString()) ? NumberUtil.formatTosepara(this.amountnotrecoveredoutsource) : this.amountnotrecoveredoutsource.toString();
	}

	public void setAmountnotrecoveredoutsource(Double amountnotrecoveredoutsource) {
		this.amountnotrecoveredoutsource = amountnotrecoveredoutsource;
	}
	
	public Double getAmountperiodincreasedincludedelay() {
		return amountperiodincreasedincludedelay;
	}
	// 会计显示
	public String getAmountperiodincreasedincludedelayText() {
//		return amountperiodincreasedincludedelay;
		return !"".equals(this.amountperiodincreasedincludedelay.toString()) ? NumberUtil.formatTosepara(this.amountperiodincreasedincludedelay) : this.amountperiodincreasedincludedelay.toString();
	}

	public void setAmountperiodincreasedincludedelay(Double amountperiodincreasedincludedelay) {
		this.amountperiodincreasedincludedelay = amountperiodincreasedincludedelay;
	}
	
	public Double getAmountperiodincreased() {
		return amountperiodincreased;
	}

	public void setAmountperiodincreased(Double amountperiodincreased) {
		this.amountperiodincreased = amountperiodincreased;
	}
	
	public Double getAmountallincome() {
		return amountallincome;
	}
	// 会计显示
	public String getAmountallincomeText() {
//		return amountallincome;
		return !"".equals(this.amountallincome.toString()) ? NumberUtil.formatTosepara(this.amountallincome) : this.amountallincome.toString();
	}
	public void setAmountallincome(Double amountallincome) {
		this.amountallincome = amountallincome;
	}
	
	public Double getAmountcreditincome() {
		return amountcreditincome;
	}
	// 会计显示
	public String getAmountcreditincomeText() {
//		return amountcreditincome;
		return !"".equals(this.amountcreditincome.toString()) ? NumberUtil.formatTosepara(this.amountcreditincome) : this.amountcreditincome.toString();
	}

	public void setAmountcreditincome(Double amountcreditincome) {
		this.amountcreditincome = amountcreditincome;
	}
	
	public Double getAmountdelayincome() {
		return amountdelayincome;
	}
	// 会计显示
	public String getAmountdelayincomeText() {
//		return amountdelayincome;
		return !"".equals(this.amountdelayincome.toString()) ? NumberUtil.formatTosepara(this.amountdelayincome) : this.amountdelayincome.toString();
	}

	public void setAmountdelayincome(Double amountdelayincome) {
		this.amountdelayincome = amountdelayincome;
	}
	
	public Double getAmountoverdueincome() {
		return amountoverdueincome;
	}
	// 会计显示
	public String getAmountoverdueincomeText() {
//		return amountoverdueincome;
		return !"".equals(this.amountoverdueincome.toString()) ? NumberUtil.formatTosepara(this.amountoverdueincome) : this.amountoverdueincome.toString();
	}
	public void setAmountoverdueincome(Double amountoverdueincome) {
		this.amountoverdueincome = amountoverdueincome;
	}
	
	public Double getAmountoutsourceincome() {
		return amountoutsourceincome;
	}
	// 会计显示
	public String getAmountoutsourceincomeText() {
//		return amountoutsourceincome;
		return !"".equals(this.amountoutsourceincome.toString()) ? NumberUtil.formatTosepara(this.amountoutsourceincome) : this.amountoutsourceincome.toString();
	}

	public void setAmountoutsourceincome(Double amountoutsourceincome) {
		this.amountoutsourceincome = amountoutsourceincome;
	}
	
	public Double getOrdercancelperiodpercentapp() {
		return ordercancelperiodpercentapp;
	}

	public void setOrdercancelperiodpercentapp(Double ordercancelperiodpercentapp) {
		this.ordercancelperiodpercentapp = ordercancelperiodpercentapp;
	}
	
	public Double getOrdercancelperioadpercentwechat() {
		return ordercancelperioadpercentwechat;
	}

	public void setOrdercancelperioadpercentwechat(Double ordercancelperioadpercentwechat) {
		this.ordercancelperioadpercentwechat = ordercancelperioadpercentwechat;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
}