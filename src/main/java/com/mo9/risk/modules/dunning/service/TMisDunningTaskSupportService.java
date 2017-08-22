/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TmisDunningNumberClean;

/**
 * 催收任务辅助Service
 * @author shijlu
 * @version 2017-08-11
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningTaskSupportService {
	
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	
	/**
	 * 更新最近登录时间
	 * @param order
	 * @return
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int updateLatestLoginTime(DunningOrder order) {
		return tMisDunningTaskDao.updateLatestLoginTime(order);
	}
	/**
	 * 号码清洗
	 * @param tmisDunningNumberClean
	 */
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public void saveNumberList(TmisDunningNumberClean tmisDunningNumberClean){
		tMisDunningTaskDao.saveNumberClean(tmisDunningNumberClean);
		tMisDunningTaskDao.saveNumberCleanLog(tmisDunningNumberClean);
	}

}