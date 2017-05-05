/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisBtnStatisticsDao;
import com.mo9.risk.modules.dunning.entity.TMisBtnStatistics;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 按钮统计Service
 * @author shijlu
 * @version 2017-04-11
 */
@Service
@Transactional(readOnly = true)
public class TMisBtnStatisticsService extends CrudService<TMisBtnStatisticsDao, TMisBtnStatistics> {
	
	@Override
	public TMisBtnStatistics get(String id) {
		return super.get(id);
	}

	@Override
	public List<TMisBtnStatistics> findList(TMisBtnStatistics tMisBtnStatistics) {
		return super.findList(tMisBtnStatistics);
	}

	@Override
	public Page<TMisBtnStatistics> findPage(Page<TMisBtnStatistics> page, TMisBtnStatistics tMisBtnStatistics) {
		page.setOrderBy("dbid desc");
		return super.findPage(page, tMisBtnStatistics);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void save(TMisBtnStatistics tMisBtnStatistics) {
		super.save(tMisBtnStatistics);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void delete(TMisBtnStatistics tMisBtnStatistics) {
		super.delete(tMisBtnStatistics);
	}

}