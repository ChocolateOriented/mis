/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.DemotableDao;
import com.mo9.risk.modules.dunning.entity.Demotable;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 分案demoService
 * @author 徐盛
 * @version 2017-08-28
 */
@Service
@Transactional(readOnly = true)
public class DemotableService extends CrudService<DemotableDao, Demotable> {
	
	@Autowired
	private DemotableDao demotableDao;

	public Demotable get(String id) {
		return super.get(id);
	}
	
	public List<Demotable> findList(Demotable demotable) {
		return super.findList(demotable);
	}
	
	public Page<Demotable> findPage(Page<Demotable> page, Demotable demotable) {
		return super.findPage(page, demotable);
	}
	
	@Transactional(readOnly = false)
	public void save(Demotable demotable) {
		super.save(demotable);
	}
	
	@Transactional(readOnly = false)
	public void delete(Demotable demotable) {
		super.delete(demotable);
	}
	
	@Transactional(readOnly = false)
	public int batchinsertDemotable(List<Demotable> demotables){
		return demotableDao.batchinsertDemotable(demotables);
	}
	
	/**
	 * @return
	 */
	public List<Demotable> findPeopleByDemo(@Param("dunningcycle")String dunningcycle,@Param("dealcodetype")String dealcodetype){
		return demotableDao.findPeopleByDemo(dunningcycle,dealcodetype);
	}
	
}