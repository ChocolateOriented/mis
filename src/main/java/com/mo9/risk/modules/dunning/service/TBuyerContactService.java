/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.modules.dunning.dao.TBuyerContactDao;

/**
 * 通讯录信息Service
 * @author beargao
 * @version 2016-07-15
 */
@Service
@Transactional(readOnly = true)
public class TBuyerContactService {
	@Autowired
	private TBuyerContactDao tBuyerContactDao;
	
	public Page<TBuyerContact> findPage(Page<TBuyerContact> page, TBuyerContact tBuyerContact) {
		tBuyerContact.setPage(page);
		page.setList(tBuyerContactDao.findList(tBuyerContact));
		return page;
	}
	
}