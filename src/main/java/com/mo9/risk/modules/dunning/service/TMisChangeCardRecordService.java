/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisChangeCardRecordDao;
import com.mo9.risk.modules.dunning.entity.BankCardInfo;
import com.mo9.risk.modules.dunning.entity.TMisChangeCardRecord;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 换卡记录Service
 * @author shijlu
 * @version 2017-04-11
 */
@Service
@Transactional(readOnly = true)
public class TMisChangeCardRecordService extends CrudService<TMisChangeCardRecordDao, TMisChangeCardRecord> {
	
	@Autowired
	private TMisChangeCardRecordDao tMisChangeCardRecordDao;
	
	@Override
	public TMisChangeCardRecord get(String id) {
		return super.get(id);
	}

	@Override
	public List<TMisChangeCardRecord> findList(TMisChangeCardRecord tMisChangeCardRecord) {
		return super.findList(tMisChangeCardRecord);
	}

	@Override
	public Page<TMisChangeCardRecord> findPage(Page<TMisChangeCardRecord> page, TMisChangeCardRecord tMisChangeCardRecord) {
		page.setOrderBy("dbid desc");
		return super.findPage(page, tMisChangeCardRecord);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void save(TMisChangeCardRecord tMisChangeCardRecord) {
		super.save(tMisChangeCardRecord);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void delete(TMisChangeCardRecord tMisDunnedConclusion) {
		super.delete(tMisDunnedConclusion);
	}

	public TMisChangeCardRecord getCurrentBankCard(String dealcode) {
		return tMisChangeCardRecordDao.getCurrentBankCard(dealcode);
	}
	
	public List<String> getAllChannelBank() {
		return tMisChangeCardRecordDao.getAllChannelBank();
	}

	public BankCardInfo getBankByCard(String bin) {
		return tMisChangeCardRecordDao.getBankByCard(bin);
	}
}