/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.buyer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.buyer.dao.MRiskBuyerReportDao;
import com.thinkgem.jeesite.modules.buyer.entity.MRiskBuyerReport;

/**
 * 用户报表Service
 * @author 徐盛
 * @version 2016-05-26
 */
@Service
@Transactional(readOnly = true)
public class MRiskBuyerReportService extends CrudService<MRiskBuyerReportDao, MRiskBuyerReport> {

	public MRiskBuyerReport get(String id) {
		return super.get(id);
	}
	
	public List<MRiskBuyerReport> findList(MRiskBuyerReport mRiskBuyerReport) {
		return super.findList(mRiskBuyerReport);
	}
	
	public Page<MRiskBuyerReport> findPage(Page<MRiskBuyerReport> page, MRiskBuyerReport mRiskBuyerReport) {
		return super.findPage(page, mRiskBuyerReport);
	}
	
	@Transactional(readOnly = false)
	public void save(MRiskBuyerReport mRiskBuyerReport) {
		super.save(mRiskBuyerReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(MRiskBuyerReport mRiskBuyerReport) {
		super.delete(mRiskBuyerReport);
	}
	
}