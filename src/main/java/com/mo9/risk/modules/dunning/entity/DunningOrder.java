package com.mo9.risk.modules.dunning.entity;

import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

public class DunningOrder  extends DataEntity<DunningOrder>{
	private static final long serialVersionUID = -133278239542076425L;
	/**
	 *  --------------------------催款任务状态----------------------------------------------
	 */
	public static final String STATUS_DUNNING = "dunning"; //代表催款任务正在催收中
	public static final String STATUS_EXPIRED = "expired";  //代表催款任务超出催收周期并未催回
	public static final String STATUS_FINISHED = "finished"; //代表催款任务的订单在催收周期内已还清
	public static final String STATUS_TRANSFER = "transfer"; //代表催款任务在催收周期内转移给了另一个同周期催款用户
	
	/**
	 *  --------------------------单状态 payoff 已还清 payment----------------------------------------------
	 */
	public static final String STATUS_PAYOFF = "payoff"; //已还清
	public static final String STATUS_PAYMENT = "payment";  //未还清

	/**
	 * 还款类型
	 */
	public static final String PAYTYPE_LOAN = "loan"; //还清
	public static final String PAYTYPE_PARTIAL = "partial"; //部分还款

	private Integer buyerid;
	private String realname;// # 姓名
	private String dealcode;// # 订单编号
	private String mobile;// # 手机号码
	private Double amount;// # 欠款金额
	private Double balance;// # 还款金额
	private Double corpusamount;// # 本金
	@Deprecated
	private Double creditamount;// # 应催金额 改用字段remainAmmount
	private Double remainAmmount;//剩余待还金额,应催金额
	private Date repaymenttime;// # 到期还款日期
	private Integer overduedays;// # 逾期天数
	private String status;// # 订单状态 payoff 已还清 payment
	private String dunningpeoplename;// # 催收人  改用催收人花名
	private Date payofftime;// # 还清日期
	private String dunningtaskstatus;// # 任务状态
	private Date beginPayofftime;		// 开始还清日期
	private Date endPayofftime;		// 结束还清日期
	private Date beginRepaymenttime;		// 开始到期还款日期
	private Date endRepaymenttime;		// 结束到期还款日期
	private String beginOverduedays;// # 逾期天数
	private String endOverduedays;// # 逾期天数
	private String dunningpeopleid;// # 催收人id
	private String dunningtaskdbid;   //任务id
//	private List<String> names;// # 催收人 催收任务查询使用 , 改用id查询
	private Date outerfiletime;  // 委外导出时间
	private String telremark;   //电话记录
	private Date deadline;/*催收留案功能-增加催收截止日字段 Patch 0001 by GQWU at 2016-11-8*/ 
	private String dunningpeopletype;/*催收留案功能-用于条件验证 Patch 0001 by GQWU at 2016-11-23*/ 
	
	private Date beginOuterfiletime;		// 开始到期还款日期
	private Date endOuterfiletime;		// 结束到期还款日期
	
	private Date dunningtime;
	
	private String dunningcycle;		// 催收周期(队列)
	
	private TMisDunningPeople dunningPeople ;
	private List<String> groupIds;

	private Date outsourcingBeginDate ; //委外开始时间
	private Date beginOutsourcingBeginDate ; //查询开始-委外开始时间
	private Date endOutsourcingBeginDate ; //查询结束-委外开始时间
	
	private Date outsourcingEndDate ; //委外截止时间
	private Date beginOutsourcingEndDate ; //查询开始-委外截止时间
	private Date endOutsourcingEndDate ; //查询结束-委外截止时间
	
	private String platformExt ; //金融产品
	private String extensionNumber ; //fengjihao


	private Date promisepaydate; 	// 到期还款时间
	private Date beginpromisepaydate ; //查询开始-到期还款时间
	private Date endpromisepaydate ; //查询结束-到期还款时间
	private Date nextfollowdate; 	// 下次跟进时间
	private Date beginnextfollowdate ; //查询开始-下次跟进时间
	private Date endnextfollowdate ; //查询结束-下次跟进时间
	private NumberCleanResult numberCleanResult ; //号码清洗结果
	
	private String quality;
	
	private Boolean taskOverdue;	//过期未跟案件
	
	private Date latestlogintime;	//最近登录时间
	private Date beginlatestlogintime;	//最近登录时间
	private Date endlatestlogintime;	//最近登录时间
	
	private Integer beginAmount;		//查询开始-欠款金额
	private Integer endAmount;		//查询结束-欠款金额
	
	private Boolean firstOrder;		//初次订单
	private String blackListRelaNum; //黑名单联系人数量
	private Integer blackListNumFromMo9;//mo9黑名单数量
	private Integer blackListNumFromThird;//第三方黑名单数量

	
	public String getPlatformExt() {
		return platformExt;
	}

	public void setPlatformExt(String platformExt) {
		this.platformExt = platformExt;
	}
	
	

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public Date getBeginOutsourcingBeginDate() {
		return beginOutsourcingBeginDate;
	}

	public void setBeginOutsourcingBeginDate(Date beginOutsourcingBeginDate) {
		this.beginOutsourcingBeginDate = beginOutsourcingBeginDate;
	}

	public Date getEndOutsourcingBeginDate() {
		return endOutsourcingBeginDate;
	}

	public void setEndOutsourcingBeginDate(Date endOutsourcingBeginDate) {
		this.endOutsourcingBeginDate =  null != endOutsourcingBeginDate ? DateUtils.endDate(endOutsourcingBeginDate) : endOutsourcingBeginDate;
	}

	public Date getBeginOutsourcingEndDate() {
		return beginOutsourcingEndDate;
	}

	public void setBeginOutsourcingEndDate(Date beginOutsourcingEndDate) {
		this.beginOutsourcingEndDate = beginOutsourcingEndDate;
	}

	public Date getEndOutsourcingEndDate() {
		return endOutsourcingEndDate;
	}

	public void setEndOutsourcingEndDate(Date endOutsourcingEndDate) {
		this.endOutsourcingEndDate =  null != endOutsourcingEndDate ? DateUtils.endDate(endOutsourcingEndDate) : endOutsourcingEndDate;
	}

	@ExcelField(title="分按日期", type=1, align=2, sort=11)
	public Date getOutsourcingBeginDate() {
		return outsourcingBeginDate;
	}

	public void setOutsourcingBeginDate(Date outsourcingBeginDate) {
		this.outsourcingBeginDate = outsourcingBeginDate;
	}

	@ExcelField(title="催收截止日期", type=1, align=2, sort=12)
	public Date getOutsourcingEndDate() {
		return outsourcingEndDate;
	}

	public void setOutsourcingEndDate(Date outsourcingEndDate) {
		this.outsourcingEndDate = outsourcingEndDate;
	}

	public DunningOrder() {
		super();
	}

	public DunningOrder(String id){
		super(id);
	}
	
	public DunningOrder(String dealcode, Double creditamount,
			Integer overduedays) {
		super();
		this.dealcode = dealcode;
		this.creditamount = creditamount;
		this.overduedays = overduedays;
	}

	public Integer getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(Integer buyerid) {
		this.buyerid = buyerid;
	}

	@ExcelField(title="姓名", type=1, align=2, sort=1)
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	@ExcelField(title="订单编号", type=1, align=2, sort=2)
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}
	@ExcelField(title="手机号码", type=1, align=2, sort=3)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Double getAmount() {
		return amount;
	}
	@ExcelField(title="欠款金额", type=1, align=2, sort=4)
	public String getAmountText() {
		return null != this.amount ? NumberUtil.formatTosepara(this.amount) : "";
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Double getCorpusamount() {
		return corpusamount;
	}

	public void setCorpusamount(Double corpusamount) {
		this.corpusamount = corpusamount;
	}

	public Double getCreditamount() {
		return creditamount;
	}
	@ExcelField(title="应催金额", type=1, align=2, sort=5)
	public String getCreditamountText() {
		return null != this.creditamount ? NumberUtil.formatTosepara(this.creditamount) : "";
	}
	public void setCreditamount(Double creditamount) {
		this.creditamount = creditamount;
	}
	@ExcelField(title="到期还款日期", type=1, align=2, sort=7)
	public Date getRepaymenttime() {
		return repaymenttime;
	}

	public void setRepaymenttime(Date repaymenttime) {
		this.repaymenttime = repaymenttime;
	}
	@ExcelField(title="逾期天数", type=1, align=2, sort=8)
	public Integer getOverduedays() {
		return overduedays;
	}

	public void setOverduedays(Integer overduedays) {
		this.overduedays = overduedays;
	}
	public String getStatus() {
		return status;
	}
	@ExcelField(title="订单状态", type=1, align=2, sort=9)
	public String getStatusText() {
//		return null != this.monthincreased ? NumberUtil.formatTosepara(this.monthincreased) + "%" : "";
		return STATUS_PAYOFF.equals(this.status) ?  "已还清" : 
				STATUS_PAYMENT.equals(this.status) ?  "未还清" : "";
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="催收人", type=1, align=2, sort=10)
	public String getDunningpeoplename() {
		return dunningpeoplename;
	}
	public void setDunningpeoplename(String dunningpeoplename) {
		this.dunningpeoplename = dunningpeoplename;
	}

	@ExcelField(title="还清日期", type=1, align=2, sort=13)
	public Date getPayofftime() {
		return payofftime;
	}

	public void setPayofftime(Date payofftime) {
		this.payofftime = payofftime;
	}

	
	public String getDunningtaskstatus() {
		return dunningtaskstatus;
	}
//	@ExcelField(title="任务状态", type=1, align=2, sort=11)
	public String getDunningtaskstatusText() {
		return STATUS_DUNNING.equals(this.dunningtaskstatus) ?  "正在催收" : 
				STATUS_EXPIRED.equals(this.dunningtaskstatus) ?  "超出催收周期未催回" : 
				STATUS_FINISHED.equals(this.dunningtaskstatus) ?  "催收周期内已还清" : 
				STATUS_TRANSFER.equals(this.dunningtaskstatus) ?  "同期转移" : "";
	}
	public void setDunningtaskstatus(String dunningtaskstatus) {
		this.dunningtaskstatus = dunningtaskstatus;
	}

	public Date getBeginPayofftime() {
		return beginPayofftime;
	}

	public void setBeginPayofftime(Date beginPayofftime) {
		this.beginPayofftime = beginPayofftime;
	}

	public Date getEndPayofftime() {
		return endPayofftime;
	}

	public void setEndPayofftime(Date endPayofftime) {
//		this.endPayofftime = endPayofftime;
		this.endPayofftime =  null != endPayofftime ? DateUtils.endDate(endPayofftime) : endPayofftime;
	}

	
	public Date getBeginRepaymenttime() {
		return beginRepaymenttime;
	}

	public void setBeginRepaymenttime(Date beginRepaymenttime) {
		this.beginRepaymenttime = beginRepaymenttime;
	}

	public Date getEndRepaymenttime() {
		return endRepaymenttime;
	}

	public void setEndRepaymenttime(Date endRepaymenttime) {
//		this.endRepaymenttime = endRepaymenttime;
		this.endRepaymenttime =  null != endRepaymenttime ? DateUtils.endDate(endRepaymenttime) : endRepaymenttime;
	}

	public String getBeginOverduedays() {
		return beginOverduedays;
	}

	public void setBeginOverduedays(String beginOverduedays) {
		this.beginOverduedays = beginOverduedays;
	}

	public String getEndOverduedays() {
		return endOverduedays;
	}

	public void setEndOverduedays(String endOverduedays) {
		this.endOverduedays = endOverduedays;
	}

	public String getDunningpeopleid() {
		return dunningpeopleid;
	}

	public void setDunningpeopleid(String dunningpeopleid) {
		this.dunningpeopleid = dunningpeopleid;
	}

//	public List<String> getNames() {
//		return names;
//	}
//
//	public void setNames(List<String> names) {
//		this.names = names;
//	}

	public String getDunningtaskdbid() {
		return dunningtaskdbid;
	}

	public void setDunningtaskdbid(String dunningtaskdbid) {
		this.dunningtaskdbid = dunningtaskdbid;
	}

	public Date getOuterfiletime() {
		return outerfiletime;
	}

	public void setOuterfiletime(Date outerfiletime) {
		this.outerfiletime = outerfiletime;
	}

	public String getTelremark() {
		return telremark;
	}

	public void setTelremark(String telremark) {
		this.telremark = telremark;
	}

	public Date getBeginOuterfiletime() {
		return beginOuterfiletime;
	}

	public void setBeginOuterfiletime(Date beginOuterfiletime) {
		this.beginOuterfiletime = beginOuterfiletime;
	}

	public Date getEndOuterfiletime() {
		return endOuterfiletime;
	}

	public void setEndOuterfiletime(Date endOuterfiletime) {
//		this.endOuterfiletime = endOuterfiletime;
		this.endOuterfiletime =  null != endOuterfiletime ? DateUtils.endDate(endOuterfiletime) : endOuterfiletime;
	}

	public Date getDunningtime() {
		return dunningtime;
	}

	public void setDunningtime(Date dunningtime) {
		this.dunningtime = dunningtime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getDunningpeopletype() {
		return dunningpeopletype;
	}

	public void setDunningpeopletype(String dunningpeopletype) {
		this.dunningpeopletype = dunningpeopletype;
	}

	public String getDunningcycle() {
		return dunningcycle;
	}

	public void setDunningcycle(String dunningcycle) {
		this.dunningcycle = dunningcycle;
	}

	public TMisDunningPeople getDunningPeople() {
		return dunningPeople;
	}

	public void setDunningPeople(TMisDunningPeople dunningPeople) {
		this.dunningPeople = dunningPeople;
	}

	public List<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	@ExcelField(title="还款总额", type=1, align=2, sort=6)
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getRemainAmmount() {
		return remainAmmount;
	}

	public void setRemainAmmount(Double remainAmmount) {
		this.remainAmmount = remainAmmount;
	}

	public Date getPromisepaydate() {
		return promisepaydate;
	}

	public void setPromisepaydate(Date promisepaydate) {
		this.promisepaydate = promisepaydate;
	}

	public Date getBeginpromisepaydate() {
		return beginpromisepaydate;
	}

	public void setBeginpromisepaydate(Date beginpromisepaydate) {
		this.beginpromisepaydate = beginpromisepaydate;
	}

	public Date getEndpromisepaydate() {
		return endpromisepaydate;
	}

	public void setEndpromisepaydate(Date endpromisepaydate) {
		this.endpromisepaydate =  null != endpromisepaydate ? DateUtils.endDate(endpromisepaydate) : endpromisepaydate;
	}

	public Date getNextfollowdate() {
		return nextfollowdate;
	}

	public void setNextfollowdate(Date nextfollowdate) {
		this.nextfollowdate = nextfollowdate;
	}

	public Date getBeginnextfollowdate() {
		return beginnextfollowdate;
	}

	public void setBeginnextfollowdate(Date beginnextfollowdate) {
		this.beginnextfollowdate = beginnextfollowdate;
	}

	public Date getEndnextfollowdate() {
		return endnextfollowdate;
	}

	public void setEndnextfollowdate(Date endnextfollowdate) {
		this.endnextfollowdate =  null != endnextfollowdate ? DateUtils.endDate(endnextfollowdate) : endnextfollowdate;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public Boolean getTaskOverdue() {
		return taskOverdue;
	}

	public void setTaskOverdue(Boolean taskOverdue) {
		this.taskOverdue = taskOverdue;
	}

	public NumberCleanResult getNumberCleanResult() {
		return numberCleanResult;
	}

	public void setNumberCleanResult(NumberCleanResult numberCleanResult) {
		this.numberCleanResult = numberCleanResult;
	}

	public Date getLatestlogintime() {
		return latestlogintime;
	}

	public void setLatestlogintime(Date latestlogintime) {
		this.latestlogintime = latestlogintime;
	}

	public Date getBeginlatestlogintime() {
		return beginlatestlogintime;
	}

	public void setBeginlatestlogintime(Date beginlatestlogintime) {
		this.beginlatestlogintime = beginlatestlogintime;
	}

	public Date getEndlatestlogintime() {
		return endlatestlogintime;
	}

	public void setEndlatestlogintime(Date endlatestlogintime) {
		this.endlatestlogintime =  null != endlatestlogintime ? DateUtils.endDate(endlatestlogintime) : endlatestlogintime;
	}

	public Integer getBeginAmount() {
		return beginAmount;
	}

	public void setBeginAmount(Integer beginAmount) {
		this.beginAmount = beginAmount;
	}

	public Integer getEndAmount() {
		return endAmount;
	}

	public void setEndAmount(Integer endAmount) {
		this.endAmount = endAmount;
	}

	public Boolean getFirstOrder() {
		return firstOrder;
	}

	public void setFirstOrder(Boolean firstOrder) {
		this.firstOrder = firstOrder;
	}

	public String getBlackListRelaNum() {
		return blackListRelaNum;
	}

	public void setBlackListRelaNum(String blackListRelaNum) {
		this.blackListRelaNum = blackListRelaNum;
	}

	public Integer getBlackListNumFromMo9() {
		return blackListNumFromMo9;
	}

	public void setBlackListNumFromMo9(Integer blackListNumFromMo9) {
		this.blackListNumFromMo9 = blackListNumFromMo9;
	}

	public Integer getBlackListNumFromThird() {
		return blackListNumFromThird;
	}

	public void setBlackListNumFromThird(Integer blackListNumFromThird) {
		this.blackListNumFromThird = blackListNumFromThird;
	}
}
