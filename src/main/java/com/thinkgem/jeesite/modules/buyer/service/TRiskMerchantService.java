/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.buyer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.buyer.entity.TRiskMerchant;
import com.thinkgem.jeesite.modules.buyer.dao.TRiskMerchantDao;
import com.thinkgem.jeesite.modules.buyer.entity.MRiskBuyerReport;
import com.thinkgem.jeesite.modules.buyer.dao.MRiskBuyerReportDao;

/**
 * 用户报表Service
 * @author 徐盛
 * @version 2016-05-26
 */
@Service
@Transactional(readOnly = true)
public class TRiskMerchantService extends CrudService<TRiskMerchantDao, TRiskMerchant> {

	@Autowired
	private MRiskBuyerReportDao mRiskBuyerReportDao;
	
	public TRiskMerchant get(String id) {
		TRiskMerchant tRiskMerchant = super.get(id);
		tRiskMerchant.setMRiskBuyerReportList(mRiskBuyerReportDao.findList(new MRiskBuyerReport(tRiskMerchant)));
		return tRiskMerchant;
	}
	
	public List<TRiskMerchant> findList(TRiskMerchant tRiskMerchant) {
		return super.findList(tRiskMerchant);
	}
	
	public Page<TRiskMerchant> findPage(Page<TRiskMerchant> page, TRiskMerchant tRiskMerchant) {
		return super.findPage(page, tRiskMerchant);
	}
	
	@Transactional(readOnly = false)
	public void save(TRiskMerchant tRiskMerchant) {
		super.save(tRiskMerchant);
		for (MRiskBuyerReport mRiskBuyerReport : tRiskMerchant.getMRiskBuyerReportList()){
			if (mRiskBuyerReport.getId() == null){
				continue;
			}
			if (MRiskBuyerReport.DEL_FLAG_NORMAL.equals(mRiskBuyerReport.getDelFlag())){
				if (StringUtils.isBlank(mRiskBuyerReport.getId())){
					mRiskBuyerReport.setMerchantId(tRiskMerchant);
					mRiskBuyerReport.preInsert();
					mRiskBuyerReportDao.insert(mRiskBuyerReport);
				}else{
					mRiskBuyerReport.preUpdate();
					mRiskBuyerReportDao.update(mRiskBuyerReport);
				}
			}else{
				mRiskBuyerReportDao.delete(mRiskBuyerReport);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TRiskMerchant tRiskMerchant) {
		super.delete(tRiskMerchant);
		mRiskBuyerReportDao.delete(new MRiskBuyerReport(tRiskMerchant));
	}
	
}