/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.thinkgem.jeesite.modules.sys.entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@Autowired
	private TMisRemittanceConfirmLogService tMisRemittanceConfirmLogService;
	
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
		tMisRemittanceConfirmLogService.saveLog(tMisRemittanceConfirm);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisRemittanceConfirm tMisRemittanceConfirm) {
		super.delete(tMisRemittanceConfirm);
		tMisRemittanceConfirmLogService.saveLog(tMisRemittanceConfirm.getId());
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
		int result = 0;
		result = misRemittanceConfirmDao.remittanceUpdate(entity);
		tMisRemittanceConfirmLogService.saveLog(entity.getId());
		return result;
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
		int result = 0;
		result = misRemittanceConfirmDao.financialUpdate(entity);
		tMisRemittanceConfirmLogService.saveLog(entity.getId());
		return result;
	}
	
	/**
	 * 财务打回到账数据
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int financialReturn(TMisRemittanceConfirm entity){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CW_RETURN);
		int result = 0;
		result = misRemittanceConfirmDao.financialReturn(entity);
		tMisRemittanceConfirmLogService.saveLog(entity.getId());
		return result;
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
		int result = 0;
		result = misRemittanceConfirmDao.confirmationUpdate(entity);
		tMisRemittanceConfirmLogService.saveLog(entity.getId());
		return result;
	}
	
	/**
	 * 催收确认合并还款数据
	 * @param entity
	 * @param relatedIds
	 * @return
	 */
	@Transactional(readOnly = false)
	public int confirmationMergeUpdate(TMisRemittanceConfirm entity, List<String> relatedIds){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CH_CONFIRM);
		Map<String, Object> param = new HashMap<String, Object>();
		relatedIds.add(entity.getId());
		param.put("ids", relatedIds);
		param.put("tMisRemittanceConfirm", entity);
		int result = 0;
		result = misRemittanceConfirmDao.confirmationMergeUpdate(param);
		tMisRemittanceConfirmLogService.saveLog(relatedIds);
		return result;
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
	
	/**
	 * 获取关联的汇款记录
	 * @param tMisRemittanceConfirm
	 * @return
	 */
	public List<TMisRemittanceConfirm> findRelatedList(TMisRemittanceConfirm tMisRemittanceConfirm){
		return misRemittanceConfirmDao.findRelatedList(tMisRemittanceConfirm);
	}

	/**
	 * @Description 批量插入
	 * @param confirms
	 * @return void
	 */
	public void batchInsert(List<TMisRemittanceConfirm> confirms,User user) {
		if (null == confirms || confirms.size()==0){
			return;
		}
		for (TMisRemittanceConfirm remittanceConfirm: confirms) {
			remittanceConfirm.preInsert();
			remittanceConfirm.setUpdateBy(user);
			remittanceConfirm.setCreateBy(user);
		}
		dao.batchInsert(confirms);
		for (TMisRemittanceConfirm remittanceConfirm: confirms) {
			tMisRemittanceConfirmLogService.saveLog(remittanceConfirm);
		}
	}
}