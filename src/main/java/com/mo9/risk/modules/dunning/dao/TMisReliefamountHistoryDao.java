/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory;

/**
 * 减免记录DAO接口
 * @author 徐盛
 * @version 2016-08-05
 */
@MyBatisDao
public interface TMisReliefamountHistoryDao extends CrudDao<TMisReliefamountHistory> {
	
	public List<TMisReliefamountHistory> findListByDealcode(String code);
}