/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.entity.Demotable;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;

/**
 * 分案demoDAO接口
 * @author 徐盛
 * @version 2017-08-28
 */
@MyBatisDao
public interface DemotableDao extends CrudDao<Demotable> {
	
	
	public int batchinsertDemotable(List<Demotable> demotables);
	
	
	
	/**
	 * @return
	 */
	public List<Demotable> findPeopleByDemo(@Param("dunningcycle")String dunningcycle,@Param("dealcodetype")String dealcodetype);
	
}