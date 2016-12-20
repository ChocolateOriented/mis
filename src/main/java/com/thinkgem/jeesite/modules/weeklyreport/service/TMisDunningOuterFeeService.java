/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.modules.weeklyreport.entity.TMisDunningOuterFee;
import com.thinkgem.jeesite.modules.weeklyreport.bean.SCashLoanDailyReportOrderWeekBean;
import com.thinkgem.jeesite.modules.weeklyreport.dao.TMisDunningOuterFeeDao;

/**
 * 基础佣金费率表Service
 * @author 徐盛
 * @version 2016-11-08
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningOuterFeeService extends CrudService<TMisDunningOuterFeeDao, TMisDunningOuterFee> {
	
	@Autowired
	private TMisDunningOuterFeeDao tMisDunningOuterFeeDao;

	public TMisDunningOuterFee get(String id) {
		return super.get(id);
	}
	
	public List<TMisDunningOuterFee> findList(TMisDunningOuterFee tMisDunningOuterFee) {
		return super.findList(tMisDunningOuterFee);
	}
	
	public Page<TMisDunningOuterFee> findPage(Page<TMisDunningOuterFee> page, TMisDunningOuterFee tMisDunningOuterFee) {
		return super.findPage(page, tMisDunningOuterFee);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisDunningOuterFee tMisDunningOuterFee) {
		super.save(tMisDunningOuterFee);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisDunningOuterFee tMisDunningOuterFee) {
		super.delete(tMisDunningOuterFee);
	}
	
	/**
	 * 查询基础佣金费率的last的信息
	 * @return
	 */
	public TMisDunningOuterFee findListOneByNewFee(TMisDunningOuterFee outerFee){
		return tMisDunningOuterFeeDao.findListOneByNewFee(outerFee);
	}
	
	
	/**
	 * 保存并保存前一条的费率执行结束时间
	 * @param insertOuterFee
	 * @param updateOuterFee
	 */
	@Transactional(readOnly = false)
	public void saveAndupdate(TMisDunningOuterFee insertOuterFee,TMisDunningOuterFee updateOuterFee) {
		try{
			tMisDunningOuterFeeDao.insert(insertOuterFee);
			tMisDunningOuterFeeDao.update(updateOuterFee);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * 修改
	 * @param tMisDunningOuterFee
	 */
	@Transactional(readOnly = false)
	public void update(TMisDunningOuterFee tMisDunningOuterFee) {
		tMisDunningOuterFeeDao.update(tMisDunningOuterFee);
	}
	
	
}