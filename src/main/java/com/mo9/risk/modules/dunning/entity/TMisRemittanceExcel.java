/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

/**
 * 财务确认汇款信息Entity
 * @author 徐盛
 * @version 2016-08-11
 */
public class TMisRemittanceExcel extends DataEntity<TMisRemittanceExcel> {
	
	private static final long serialVersionUID = 1L;
	
	//序号
	private String dbid;	
	// 入账时间
	private String remittancetime;
	// 支付宝交易号
	private String alipayRemittanceNumber ;  
	//支付宝流水号
	private String alipaySerialNumber;  
	//商户订单号
	private String commercialOrderNumber;  
	//财务类型
	private String serveType;  
	//收入(+元)
	private Double remittanceamount;
	//支出(-元)
	private Double outPay;
	//账户余额（元）
	private Double payBalance;
	//服务费
	private Double serverPay;
	//支付渠道
	private String remittancechannel;
	//签约产品
	private String conProduct;
	//对方账户
	private String remittanceaccount;	
	//对方名称
	private String remittancename;	
	//银行订单号
	private String blankOrder;	
	//商品名称
	private String productName;	
	// 备注
	private String remark;
	
	@ExcelField(title="序号",type=0, align=2, sort=10)
	public String getDbid() {
		return dbid;
	}
	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	@ExcelField(title="入账时间",type=0, align=2, sort=20)
	public String getRemittancetime() {
		return remittancetime;
	}
	public void setRemittancetime(String remittancetime) {
		this.remittancetime = remittancetime;
	}
	@ExcelField(title="支付宝交易号",type=0, align=2, sort=30)
	public String getAlipayRemittanceNumber() {
		return alipayRemittanceNumber;
	}
	public void setAlipayRemittanceNumber(String alipayRemittanceNumber) {
		this.alipayRemittanceNumber = alipayRemittanceNumber;
	}
	@ExcelField(title="支付宝流水号",type=0, align=2, sort=40)
	public String getAlipaySerialNumber() {
		return alipaySerialNumber;
	}
	public void setAlipaySerialNumber(String alipaySerialNumber) {
		this.alipaySerialNumber = alipaySerialNumber;
	}
	@ExcelField(title="商户订单号",type=0, align=2, sort=50)
	public String getCommercialOrderNumber() {
		return commercialOrderNumber;
	}
	public void setCommercialOrderNumber(String commercialOrderNumber) {
		this.commercialOrderNumber = commercialOrderNumber;
	}
	@ExcelField(title="财务类型",type=0, align=2, sort=60)
	public String getServeType() {
		return serveType;
	}
	public void setServeType(String serveType) {
		this.serveType = serveType;
	}
	public String getRemittanceamountText() {
		return  null != this.remittanceamount ? NumberUtil.formatTosepara(this.remittanceamount) : "";
	}
	@ExcelField(title="收入(+元)",type=0, align=2, sort=70)
	public Double getRemittanceamount() {
		return remittanceamount;
	}
	public void setRemittanceamount(Double remittanceamount) {
		this.remittanceamount = remittanceamount;
	}
	public String getOutPayText() {
		return null != this.outPay ? NumberUtil.formatTosepara(this.outPay) : "";
	}
	@ExcelField(title="支出(+元)",type=0, align=2, sort=80)
	public Double getOutPay() {
		return outPay;
	}
	public void setOutPay(Double outPay) {
		this.outPay = outPay;
	}
	public String getPayBalanceText() {
		return null != this.payBalance ? NumberUtil.formatTosepara(this.payBalance) : "";
	}
	@ExcelField(title="账户余额（元）",type=0, align=2, sort=90)
	public Double getPayBalance() {
		return payBalance;
	}
	public void setPayBalance(Double payBalance) {
		this.payBalance = payBalance;
	}
	public String getServerPayText() {
		return null != this.serverPay ? NumberUtil.formatTosepara(this.serverPay) : "";
	}
	@ExcelField(title="服务费（元）",type=0, align=2, sort=100)
	public Double getServerPay() {
		return serverPay;
	}
	public void setServerPay(Double serverPay) {
		this.serverPay = serverPay;
	}
	@ExcelField(title="支付渠道",type=0, align=2, sort=110)
	public String getRemittancechannel() {
		return remittancechannel;
	}
	public void setRemittancechannel(String remittancechannel) {
		this.remittancechannel = remittancechannel;
	}
	@ExcelField(title="签约产品",type=0, align=2, sort=120)
	public String getConProduct() {
		return conProduct;
	}
	public void setConProduct(String conProduct) {
		this.conProduct = conProduct;
	}
	@ExcelField(title="对方账户",type=0, align=2, sort=130)
	public String getRemittanceaccount() {
		return remittanceaccount;
	}
	public void setRemittanceaccount(String remittanceaccount) {
		this.remittanceaccount = remittanceaccount;
	}
	@ExcelField(title="对方名称",type=0, align=2, sort=140)
	public String getRemittancename() {
		return remittancename;
	}
	public void setRemittancename(String remittancename) {
		this.remittancename = remittancename;
	}
	@ExcelField(title="银行订单号",type=0, align=2, sort=150)
	public String getBlankOrder() {
		return blankOrder;
	}
	public void setBlankOrder(String blankOrder) {
		this.blankOrder = blankOrder;
	}
	@ExcelField(title="商品名称",type=0, align=2, sort=160)
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@ExcelField(title="备注",type=0, align=2, sort=170)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "TMisRemittanceExcel [dbid=" + dbid + ", remittancetime=" + remittancetime + ", alipayRemittanceNumber="
				+ alipayRemittanceNumber + ", alipaySerialNumber=" + alipaySerialNumber + ", commercialOrderNumber="
				+ commercialOrderNumber + ", serveType=" + serveType + ", remittanceamount=" + remittanceamount
				+ ", outPay=" + outPay + ", payBalance=" + payBalance + ", serverPay=" + serverPay
				+ ", remittancechannel=" + remittancechannel + ", conProduct=" + conProduct + ", remittanceaccount="
				+ remittanceaccount + ", remittancename=" + remittancename + ", blankOrder=" + blankOrder
				+ ", productName=" + productName + ", remark=" + remark + "]";
	}
	
	
	
	
	
	
}