/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisReliefamountHistoryDao;
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 减免记录Service
 * @author 徐盛
 * @version 2016-08-05
 */
@Service
@Transactional(readOnly = true)
public class TMisReliefamountHistoryService extends CrudService<TMisReliefamountHistoryDao, TMisReliefamountHistory> {

	@Autowired
	private TMisReliefamountHistoryDao tMisReliefamountHistoryDao;
	
	public TMisReliefamountHistory get(String id) {
		return super.get(id);
	}
	
	public List<TMisReliefamountHistory> findList(TMisReliefamountHistory tMisReliefamountHistory) {
		return super.findList(tMisReliefamountHistory);
	}
	
	public Page<TMisReliefamountHistory> findPage(Page<TMisReliefamountHistory> page, TMisReliefamountHistory tMisReliefamountHistory) {
		return super.findPage(page, tMisReliefamountHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisReliefamountHistory tMisReliefamountHistory) {
		super.save(tMisReliefamountHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisReliefamountHistory tMisReliefamountHistory) {
		super.delete(tMisReliefamountHistory);
	}
	
	public List<TMisReliefamountHistory> findListByDealcode(String code){
		return tMisReliefamountHistoryDao.findListByDealcode(code);
	}
	
}