/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 * 汇款确认信息Entity
 * @author 徐盛
 * @version 2016-09-12
 */
public class TMisRemittanceConfirm extends DataEntity<TMisRemittanceConfirm> {
	/**
	 * --------------CHECK确认流程(提交>审核)汇款状态--------------
	 */
	public static final String CONFIRMSTATUS_CH_SUBMIT = "ch_submit";  //代表催收已提交财务待确认
	public static final String CONFIRMSTATUS_CW_SUBMIT = "cw_submit";  //代表财务已提交催收待确认
	public static final String CONFIRMSTATUS_CW_RETURN = "cw_return";  //代表财务已打回催收待重新提交
	public static final String CONFIRMSTATUS_CH_CONFIRM = "ch_confirm";  //代表催收最终已确认

	/**
	 * --------------CHECK确认流程(提交>审核)汇款状态--------------
	 */
	public static final String CONFIRMSTATUS_COMPLETE_AUDIT = "complete_audit";  //已查账
	public static final String CONFIRMSTATUS_FINISH = "finish"; //已完成

	/**
	 * 还款类型
	 */
	public static final String PAYTYPE_LOAN = "loan"; //还清
	public static final String PAYTYPE_PARTIAL = "partial"; //部分还款

	private static final long serialVersionUID = 1L;
	private Integer dbid;		// dbid
	
	private String remittancename;			// 汇款人
	private Date   remittancetime;			// 汇款时间
	private Double remittanceamount;		// 汇款金额
	private String remittancechannel;		// 汇款渠道
	private String ReceivablesImg1; 		// 催收上传图片
	private String ReceivablesImg2; 		// 催收上传图片
	private Double payamount;				// 还款金额
	private String paytype;					// 还款类型
	private String remark;					// 备注
	private String serialnumber;			// 汇款流水号
	
	private String financialremittancename; // 汇款人
	private Date accounttime;				// 到账时间
	private Double accountamount;			// 到账金额
	private String financialremittancechannel;		// 汇款渠道
	private String FinancialImg1; 			// 财务上传图片
	private String FinancialImg2; 			// 财务上传图片
	private String financialremark;					// 备注
	private String financialserialnumber;	// 财务汇款流水号

	private ConfirmFlow confirmFlow;//确认流程
	private String confirmstatus;		// 确认状态
	private RemittanceTag remittanceTag;		//入账标签
	private String dealcode;		// 订单号
	private String Invalid;			// 删除标志


	/**
	 * 用户id
	 */
	public String buyerId;
	public String name;
	public String mobile;
	
	private Date beginupdatetime;
	private Date endupdatetime;
	
	
	public TMisRemittanceConfirm() {
		super();
	}

	public TMisRemittanceConfirm(String id){
		super(id);
	}
	
	public TMisRemittanceConfirm(String id,String paytype,Double payamount,String confirmstatus){
		this.id = id ;
		this.paytype = paytype ;
		this.payamount = payamount ;
		this.confirmstatus = confirmstatus ;
	}

	@NotNull(message="dbid不能为空")
	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public String getRemittancename() {
		return remittancename;
	}

	public void setRemittancename(String remittancename) {
		this.remittancename = remittancename;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRemittancetime() {
		return remittancetime;
	}

	public void setRemittancetime(Date remittancetime) {
		this.remittancetime = remittancetime;
	}

	public Double getRemittanceamount() {
		return remittanceamount;
	}

	public void setRemittanceamount(Double remittanceamount) {
		this.remittanceamount = remittanceamount;
	}

	public String getRemittancechannel() {
		return remittancechannel;
	}

	public void setRemittancechannel(String remittancechannel) {
		this.remittancechannel = remittancechannel;
	}

	public String getReceivablesImg1() {
		return ReceivablesImg1;
	}

	public void setReceivablesImg1(String receivablesImg1) {
		ReceivablesImg1 = receivablesImg1;
	}

	public String getReceivablesImg2() {
		return ReceivablesImg2;
	}

	public void setReceivablesImg2(String receivablesImg2) {
		ReceivablesImg2 = receivablesImg2;
	}

	public Double getPayamount() {
		return payamount;
	}

	public void setPayamount(Double payamount) {
		this.payamount = payamount;
	}

	public String getPaytype() {
		return paytype;
	}
	public String getPaytypeText() {
		return "loan".equals(this.paytype) ?  "还款" : 
				"delay".equals(this.paytype) ?  "续期" :
				"partial".equals(this.paytype) ?  "部分还款" :"";
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFinancialremittancename() {
		return financialremittancename;
	}

	public void setFinancialremittancename(String financialremittancename) {
		this.financialremittancename = financialremittancename;
	}

	public Date getAccounttime() {
		return accounttime;
	}

	public void setAccounttime(Date accounttime) {
		this.accounttime = accounttime;
	}

	public Double getAccountamount() {
		return accountamount;
	}

	public void setAccountamount(Double accountamount) {
		this.accountamount = accountamount;
	}

	public String getFinancialremittancechannel() {
		return financialremittancechannel;
	}

	public void setFinancialremittancechannel(String financialremittancechannel) {
		this.financialremittancechannel = financialremittancechannel;
	}

	public String getFinancialImg1() {
		return FinancialImg1;
	}

	public void setFinancialImg1(String financialImg1) {
		FinancialImg1 = financialImg1;
	}

	public String getFinancialImg2() {
		return FinancialImg2;
	}

	public void setFinancialImg2(String financialImg2) {
		FinancialImg2 = financialImg2;
	}

	public String getFinancialremark() {
		return financialremark;
	}

	public void setFinancialremark(String financialremark) {
		this.financialremark = financialremark;
	}

	public String getConfirmstatus() {
		return confirmstatus;
	}
	public String getConfirmstatusText() {
		return CONFIRMSTATUS_CH_SUBMIT.equals(this.confirmstatus) ?  "催收已提交" :
				  CONFIRMSTATUS_CW_SUBMIT.equals(this.confirmstatus) ?  "财务已确认" :
					 CONFIRMSTATUS_CW_RETURN.equals(this.confirmstatus) ?  "财务已打回" :
						CONFIRMSTATUS_CH_CONFIRM.equals(this.confirmstatus) ?  "已完成" :"";
	}
	public void setConfirmstatus(String confirmstatus) {
		this.confirmstatus = confirmstatus;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getBeginupdatetime() {
		return beginupdatetime;
	}

	public void setBeginupdatetime(Date beginupdatetime) {
		this.beginupdatetime = beginupdatetime;
	}

	public Date getEndupdatetime() {
		return endupdatetime;
	}

	public void setEndupdatetime(Date endupdatetime) {
		this.endupdatetime = endupdatetime;
	}

	public String getInvalid() {
		return Invalid;
	}

	public void setInvalid(String invalid) {
		Invalid = invalid;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getFinancialserialnumber() {
		return financialserialnumber;
	}

	public void setFinancialserialnumber(String financialserialnumber) {
		this.financialserialnumber = financialserialnumber;
	}

	public ConfirmFlow getConfirmFlow() {
		return confirmFlow;
	}

	public void setConfirmFlow(ConfirmFlow confirmFlow) {
		this.confirmFlow = confirmFlow;
	}

	public RemittanceTag getRemittanceTag() {
		return remittanceTag;
	}

	public void setRemittanceTag(
			RemittanceTag remittanceTag) {
		this.remittanceTag = remittanceTag;
	}

	@Override
	public String toString() {
		return "TMisRemittanceConfirm{" +
				"dbid=" + dbid +
				", remittancename='" + remittancename + '\'' +
				", remittancetime=" + remittancetime +
				", remittanceamount=" + remittanceamount +
				", remittancechannel='" + remittancechannel + '\'' +
				", ReceivablesImg1='" + ReceivablesImg1 + '\'' +
				", ReceivablesImg2='" + ReceivablesImg2 + '\'' +
				", payamount=" + payamount +
				", paytype='" + paytype + '\'' +
				", remark='" + remark + '\'' +
				", serialnumber='" + serialnumber + '\'' +
				", financialremittancename='" + financialremittancename + '\'' +
				", accounttime=" + accounttime +
				", accountamount=" + accountamount +
				", financialremittancechannel='" + financialremittancechannel + '\'' +
				", FinancialImg1='" + FinancialImg1 + '\'' +
				", FinancialImg2='" + FinancialImg2 + '\'' +
				", financialremark='" + financialremark + '\'' +
				", financialserialnumber='" + financialserialnumber + '\'' +
				", confirmFlow=" + confirmFlow +
				", confirmstatus='" + confirmstatus + '\'' +
				", remittanceTag=" + remittanceTag +
				", dealcode='" + dealcode + '\'' +
				", Invalid='" + Invalid + '\'' +
				", buyerId='" + buyerId + '\'' +
				", name='" + name + '\'' +
				", mobile='" + mobile + '\'' +
				", beginupdatetime=" + beginupdatetime +
				", endupdatetime=" + endupdatetime +
				'}';
	}

	public enum ConfirmFlow{
		CHECK,//提交>审核
		AUDIT//上传>查账
	}

	/**
	 * @Description 还款标签
	 * @author jxli
	 * @version 2017/6/23
	 */
	public enum RemittanceTag{
		REPAYMENT_SELF("本人还款"),
		REPAYMENT_THIRD("第三方还款");

		RemittanceTag(String desc) {
			this.desc = desc;
		}

		public final String desc;

		public String getDesc() {
			return desc;
		}
	}
}