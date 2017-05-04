/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.thinkgem.jeesite.common.utils.Reflections;

/**
 * 调用mo9代扣接口订单Entity
 * @author shijlu
 * @version 2017-04-11
 */
public class Mo9DeductOrder implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private String bizSys;		//业务系统

	private String sign;		//签名

	private String invoice;		//订单号

	private String usrName;		//开户名

	private String mobile;		//预留手机

	private String cardNo;		//银行卡号

	private String idCard;		//开户身份证

	private String deductAmt;		//扣款金额

	private String openBank;		//开户银行

	private String subBank;		//开户支行

	private String prov;		//开户省份

	private String city;		//开户城市

	private String purpose;		//扣款目的

	private String attach;		//附加信息

	private String notifyUrl;		//通知入口

	private String extraInvoiceBak;		//江湖救急单号

	private String channel;		//代扣渠道

	public Mo9DeductOrder(TMisDunningDeduct tMisDunningDeduct) {
		this.bizSys = "mis.deduct";
		this.invoice = tMisDunningDeduct.getDeductcode();
		this.usrName = tMisDunningDeduct.getBuyername();
		this.mobile = tMisDunningDeduct.getMobile();
		this.cardNo = tMisDunningDeduct.getBankcard();
		this.idCard = tMisDunningDeduct.getIdcard();
		this.deductAmt = String.valueOf(tMisDunningDeduct.getPayamount());
		this.openBank = tMisDunningDeduct.getBankname();
		this.subBank = "";
		this.prov = "";
		this.city = "";
		this.purpose = "代扣";
		this.attach = "";
		this.extraInvoiceBak = tMisDunningDeduct.getDealcode();
		this.channel = tMisDunningDeduct.getPaychannel();
	}

	public Mo9DeductOrder() {
		super();
	}

	public String getBizSys() {
		return bizSys;
	}

	public void setBizSys(String bizSys) {
		this.bizSys = bizSys;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getDeductAmt() {
		return deductAmt;
	}

	public void setDeductAmt(String deductAmt) {
		this.deductAmt = deductAmt;
	}

	public String getOpenBank() {
		return openBank;
	}

	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}

	public String getSubBank() {
		return subBank;
	}

	public void setSubBank(String subBank) {
		this.subBank = subBank;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getExtraInvoiceBak() {
		return extraInvoiceBak;
	}

	public void setExtraInvoiceBak(String extraInvoiceBak) {
		this.extraInvoiceBak = extraInvoiceBak;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			
			Object value = Reflections.invokeGetter(this, field.getName());
			if (value != null && !"".equals(value.toString())) {
				map.put(field.getName(), value.toString());
			}
		}
		
		return map;
	}
}