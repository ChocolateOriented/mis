/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.mo9.risk.modules.dunning.dao.TMisRemittanceMessageDao;

/**
 * 财务确认汇款信息Service
 * @author 徐盛
 * @version 2016-08-11
 */
@Service
@Transactional(readOnly = true)
public class TMisRemittanceMessageService extends CrudService<TMisRemittanceMessageDao, TMisRemittanceMessage> {
	@Autowired
	private TMisRemittanceMessageDao misRemittanceMessageDao;

	public TMisRemittanceMessage get(String id) {
		return super.get(id);
	}
	
	public List<TMisRemittanceMessage> findList(TMisRemittanceMessage tMisRemittanceMessage) {
		return super.findList(tMisRemittanceMessage);
	}
	
	public Page<TMisRemittanceMessage> findPage(Page<TMisRemittanceMessage> page, TMisRemittanceMessage tMisRemittanceMessage) {
		return super.findPage(page, tMisRemittanceMessage);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisRemittanceMessage tMisRemittanceMessage) {
		super.save(tMisRemittanceMessage);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisRemittanceMessage tMisRemittanceMessage) {
		super.delete(tMisRemittanceMessage);
	}
	
	@Transactional(readOnly = false)
	public void insert(TMisRemittanceMessage tMisRemittanceMessage) {
		tMisRemittanceMessage.preInsert();
		dao.insert(tMisRemittanceMessage);
	}
	
	public TMisRemittanceMessage  findRemittanceMesListByDealcode(String code){
		return misRemittanceMessageDao.findRemittanceMesListByDealcode(code);
	}
	
	@Transactional(readOnly = false)
	public int fileUpload(LinkedList<TMisRemittanceMessage> tMisRemittanceList) {
		int  same=0;
		
		List<TMisRemittanceMessage> trMList=misRemittanceMessageDao.findByList(tMisRemittanceList);
		same=trMList.size();
		if(trMList.size()>0&&trMList!=null){
			boolean removeAll = tMisRemittanceList.removeAll(trMList);
		}
		if(tMisRemittanceList.size()>0&&tMisRemittanceList!=null){
			int saveNum = misRemittanceMessageDao.saveList(tMisRemittanceList);
			
		}
		return same;
	}
	
}