/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.buyer.entity;

import org.hibernate.validator.constraints.Length;
import java.util.List;
import com.google.common.collect.Lists;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户报表Entity
 * @author 徐盛
 * @version 2016-05-26
 */
public class TRiskMerchant extends DataEntity<TRiskMerchant> {
	
	private static final long serialVersionUID = 1L;
	private String creditMerchantName;		// 商户名
	private String creditMerchantKey;		// 商户唯一key
	private Integer logoFileId;		// logo文件id
	private List<MRiskBuyerReport> mRiskBuyerReportList = Lists.newArrayList();		// 子表列表
	
	public TRiskMerchant() {
		super();
	}

	public TRiskMerchant(String id){
		super(id);
	}

	@Length(min=0, max=50, message="商户名长度必须介于 0 和 50 之间")
	public String getCreditMerchantName() {
		return creditMerchantName;
	}

	public void setCreditMerchantName(String creditMerchantName) {
		this.creditMerchantName = creditMerchantName;
	}
	
	@Length(min=0, max=100, message="商户唯一key长度必须介于 0 和 100 之间")
	public String getCreditMerchantKey() {
		return creditMerchantKey;
	}

	public void setCreditMerchantKey(String creditMerchantKey) {
		this.creditMerchantKey = creditMerchantKey;
	}
	
	public Integer getLogoFileId() {
		return logoFileId;
	}

	public void setLogoFileId(Integer logoFileId) {
		this.logoFileId = logoFileId;
	}
	
	public List<MRiskBuyerReport> getMRiskBuyerReportList() {
		return mRiskBuyerReportList;
	}

	public void setMRiskBuyerReportList(List<MRiskBuyerReport> mRiskBuyerReportList) {
		this.mRiskBuyerReportList = mRiskBuyerReportList;
	}
}