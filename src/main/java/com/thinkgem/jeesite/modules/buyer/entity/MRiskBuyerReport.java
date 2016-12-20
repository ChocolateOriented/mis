/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.buyer.entity;

import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户报表Entity
 * @author 徐盛
 * @version 2016-05-26
 */
public class MRiskBuyerReport extends DataEntity<MRiskBuyerReport> {
	
	private static final long serialVersionUID = 1L;
	private TRiskMerchant merchantId;		// merchant_id 父类
	private Integer count;		// count
	
	public MRiskBuyerReport() {
		super();
	}

	public MRiskBuyerReport(String id){
		super(id);
	}

	public MRiskBuyerReport(TRiskMerchant merchantId){
		this.merchantId = merchantId;
	}

	@NotNull(message="merchant_id不能为空")
	public TRiskMerchant getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(TRiskMerchant merchantId) {
		this.merchantId = merchantId;
	}
	
	@NotNull(message="count不能为空")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}