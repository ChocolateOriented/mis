/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TRiskBuyerContactRecordsDao;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 通话记录Service
 * @author beragao
 * @version 2016-07-15
 */
@Service
@Transactional(readOnly = true)
public class TRiskBuyerContactRecordsService {
	
	@Autowired
	private TRiskBuyerContactRecordsDao tRiskBuyerContactRecordsDao;
	
	public Page<TRiskBuyerContactRecords> findPage(Page<TRiskBuyerContactRecords> page, TRiskBuyerContactRecords tRiskBuyerContactRecords) {
		tRiskBuyerContactRecords.setPage(page);
		page.setList(tRiskBuyerContactRecordsDao.findList(tRiskBuyerContactRecords));
		return page;
	}
	
}