/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisRemittanceConfirmDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 汇款确认信息Service
 * @author 徐盛
 * @version 2016-09-12
 */
@Service
@Transactional(readOnly = true)
public class TMisRemittanceConfirmService extends CrudService<TMisRemittanceConfirmDao, TMisRemittanceConfirm> {

	@Autowired
	private TMisRemittanceConfirmDao misRemittanceConfirmDao;
	
	public TMisRemittanceConfirm get(String id) {
		return super.get(id);
	}
	
	public List<TMisRemittanceConfirm> findList(TMisRemittanceConfirm tMisRemittanceConfirm) {
		return super.findList(tMisRemittanceConfirm);
	}
	
	public Page<TMisRemittanceConfirm> findPage(Page<TMisRemittanceConfirm> page, TMisRemittanceConfirm tMisRemittanceConfirm) {
		return super.findPage(page, tMisRemittanceConfirm);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisRemittanceConfirm tMisRemittanceConfirm) {
		super.save(tMisRemittanceConfirm);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisRemittanceConfirm tMisRemittanceConfirm) {
		super.delete(tMisRemittanceConfirm);
	}
	
	public TMisRemittanceConfirm findRemittanceConfirmColumnByDealcode(String code){
		return misRemittanceConfirmDao.findRemittanceConfirmColumnByDealcode(code);
	}
	
	/**
	 * 催收更新汇款数据
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int remittanceUpdate(TMisRemittanceConfirm entity){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CH_SUBMIT);
		return misRemittanceConfirmDao.remittanceUpdate(entity);
	}
	
	/**
	 * 财务更新到账数据
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int financialUpdate(TMisRemittanceConfirm entity){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CW_SUBMIT);
		return misRemittanceConfirmDao.financialUpdate(entity);
	}
	
	/**
	 * 催收确认还款数据
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int confirmationUpdate(TMisRemittanceConfirm entity){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CH_CONFIRM);
		return misRemittanceConfirmDao.confirmationUpdate(entity);
	}
	
	/**
	 * 返回是否是部分
	 * @param dealcode
	 * @return
	 */
	public int getResult(String dealcode){
		return misRemittanceConfirmDao.getResult(dealcode);
	}
	
	/**
	 * 根据催收唯一标示查询条数
	 * @param number
	 * @return
	 */
	public int getSerialnumber(String number){
		return misRemittanceConfirmDao.getSerialnumber(number);
	}
	
	/**
	 * 根据财务唯一标示查询条数
	 * @param number
	 * @return
	 */
	public int getFinancialserialnumber(String number){
		return misRemittanceConfirmDao.getFinancialserialnumber(number);
	}
	
	
	/**
	 * 获取延期次数
	 * @param orderid
	 * @return
	 */
	public int getExistDelayNumber(Integer orderid){
		return misRemittanceConfirmDao.getExistDelayNumber(orderid);
	}
	
}