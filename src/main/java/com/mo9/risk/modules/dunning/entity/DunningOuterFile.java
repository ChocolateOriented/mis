package com.mo9.risk.modules.dunning.entity;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

public class DunningOuterFile {
	/**
	 * --------------------------催款任务状态----------------------------------------
	 * ------
	 */
	public static final String STATUS_DUNNING = "dunning"; // 代表催款任务正在催收中
	public static final String STATUS_EXPIRED = "expired"; // 代表催款任务超出催收周期并未催回
	public static final String STATUS_FINISHED = "finished"; // 代表催款任务的订单在催收周期内已还清
	public static final String STATUS_TRANSFER = "transfer"; // 代表催款任务在催收周期内转移给了另一个同周期催款用户
	/**
	 * --------------------------单状态 payoff 已还清
	 * payment----------------------------------------------
	 */
	public static final String STATUS_PAYOFF = "payoff"; // 已还清
	public static final String STATUS_PAYMENT = "payment"; // 未还清

	private Integer buyerid;
	private String dealcode; // # 订单编号
	private String realname; // # 姓名
	private String mobile; // # 手机号码
	private String idcard; // 身份证号码
	private String sex; // 性别
	private Double amount; // 本金
	private Double costamount; // 手续费
	private Integer delaydays; // # 逾期天数
	private Double overdueamount; // 逾期费
	private Double balance; // 部分还款累计金额
	private Double remainammount; // 剩余待还金额
	private Double modifyamount; // 减免金额
	private Date remittime; // 贷款日期
	private Integer days; // 贷款期限
	private Date repaymenttime; // 最后还款日
	private String companyname; // # 公司名称
	private String companytel; // # 公司电话
	private String linkname1; // 联系人1姓名
	private String linkrelation1; // 联系人1关系
	private String linktel1; // 联系人1电话
	private String linkname2; // 联系人2姓名
	private String linkrelation2; // 联系人2关系
	private String linktel2; // 联系人2电话
	private String name; // 催收员
//	private Data createDate;
	private List<String> orders; // 订单号（参数）
	
//	private List<String> outerOrders;

//	public List<String> getOuterOrders() {
//		return outerOrders;
//	}
//	public void setOuterOrders(List<String> outerOrders) {
//		this.outerOrders = outerOrders;
//	}

	public DunningOuterFile() {
		super();
	}

	public Integer getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(Integer buyerid) {
		this.buyerid = buyerid;
	}

	@ExcelField(title = "姓名", type = 1, align = 2, sort = 1)
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@ExcelField(title = "订单编号", type = 1, align = 2, sort = 2)
	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	@ExcelField(title = "手机号码", type = 1, align = 2, sort = 3)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@ExcelField(title = "身份证号码", type = 1, align = 2, sort = 4)
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	
	@ExcelField(title = "性别", type = 1, align = 2, sort = 5)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	
	public Double getAmount() {
		return amount;
	}
	@ExcelField(title = "本金", type = 1, align = 2, sort = 6)
	public String getAmountText() {
		return null != this.amount ? NumberUtil.formatTosepara(this.amount) : "";
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getCostamount() {
		return costamount;
	}
	@ExcelField(title = "手续费", type = 1, align = 2, sort = 7)
	public String getCostamountText() {
		return null != this.costamount ? NumberUtil.formatTosepara(this.costamount) : "";
	}
	public void setCostamount(Double costamount) {
		this.costamount = costamount;
	}

	@ExcelField(title = "逾期天数", type = 1, align = 2, sort = 8)
	public Integer getDelaydays() {
		return delaydays;
	}
	public void setDelaydays(Integer delaydays) {
		this.delaydays = delaydays;
	}

	public Double getOverdueamount() {
		return overdueamount;
	}
	@ExcelField(title = "逾期费", type = 1, align = 2, sort = 9)
	public String getOverdueamountText() {
		return null != this.overdueamount ? NumberUtil.formatTosepara(this.overdueamount) : "";
	}
	public void setOverdueamount(Double overdueamount) {
		this.overdueamount = overdueamount;
	}

	public Double getBalance() {
		return balance;
	}
	@ExcelField(title = "部分还款累计金额", type = 1, align = 2, sort = 10)
	public String getBalanceText() {
		return null != this.balance ? NumberUtil.formatTosepara(this.balance) : "";
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getRemainammount() {
		return remainammount;
	}
	@ExcelField(title = "剩余待还金额", type = 1, align = 2, sort = 11)
	public String getRemainammountText() {
		return null != this.remainammount ? NumberUtil.formatTosepara(this.remainammount) : "";
	}
	public void setRemainammount(Double remainammount) {
		this.remainammount = remainammount;
	}

	public Double getModifyamount() {
		return modifyamount;
	}
	@ExcelField(title = "减免金额", type = 1, align = 2, sort = 12)
	public String getModifyamountText() {
		return null != this.modifyamount ? NumberUtil.formatTosepara(this.modifyamount) : "";
	}
	public void setModifyamount(Double modifyamount) {
		this.modifyamount = modifyamount;
	}
	
	@ExcelField(title = "贷款日期", type = 1, align = 2, sort = 13)
	public Date getRemittime() {
		return remittime;
	}
	public void setRemittime(Date remittime) {
		this.remittime = remittime;
	}
	@ExcelField(title = "贷款期限", type = 1, align = 2, sort = 14)
	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
	@ExcelField(title = "最后还款日", type = 1, align = 2, sort = 15)
	public Date getRepaymenttime() {
		return repaymenttime;
	}

	public void setRepaymenttime(Date repaymenttime) {
		this.repaymenttime = repaymenttime;
	}
	@ExcelField(title = "公司名称", type = 1, align = 2, sort = 16)
	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	@ExcelField(title = "公司电话", type = 1, align = 2, sort = 17)
	public String getCompanytel() {
		return companytel;
	}

	public void setCompanytel(String companytel) {
		this.companytel = companytel;
	}
	@ExcelField(title = "联系人1姓名", type = 1, align = 2, sort = 18)
	public String getLinkname1() {
		return linkname1;
	}

	public void setLinkname1(String linkname1) {
		this.linkname1 = linkname1;
	}
	@ExcelField(title = "联系人1关系", type = 1, align = 2, sort = 19)
	public String getLinkrelation1() {
		return linkrelation1;
	}

	public void setLinkrelation1(String linkrelation1) {
		this.linkrelation1 = linkrelation1;
	}
	@ExcelField(title = "联系人1电话", type = 1, align = 2, sort = 20)
	public String getLinktel1() {
		return linktel1;
	}

	public void setLinktel1(String linktel1) {
		this.linktel1 = linktel1;
	}
	@ExcelField(title = "联系人2姓名", type = 1, align = 2, sort = 21)
	public String getLinkname2() {
		return linkname2;
	}

	public void setLinkname2(String linkname2) {
		this.linkname2 = linkname2;
	}
	@ExcelField(title = "联系人2关系", type = 1, align = 2, sort = 22)
	public String getLinkrelation2() {
		return linkrelation2;
	}

	public void setLinkrelation2(String linkrelation2) {
		this.linkrelation2 = linkrelation2;
	}
	@ExcelField(title = "联系人2电话", type = 1, align = 2, sort = 23)
	public String getLinktel2() {
		return linktel2;
	}

	public void setLinktel2(String linktel2) {
		this.linktel2 = linktel2;
	}
	@ExcelField(title = "催收人", type = 1, align = 2, sort = 24)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title = "导出日期", type = 1, align = 2, sort = 25)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return new Date();
	}

	public List<String> getOrders() {
		return orders;
	}

	public void setOrders(List<String> orders) {
		this.orders = orders;
	}

}
