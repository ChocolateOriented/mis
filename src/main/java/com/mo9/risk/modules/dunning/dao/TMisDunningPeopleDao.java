/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;


import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.User;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 催收人员DAO接口
 * @author 徐盛
 * @version 2016-07-12
 */
@MyBatisDao
public interface TMisDunningPeopleDao extends CrudDao<TMisDunningPeople> {

    public List<TMisDunningPeople> findByPeriod(Map<String,Object> params);

	public List<TMisDunningPeople> findDunningPeopleByType(String peopleType);
	
	public List<User> findUserList();
	
	public List<TMisDunningPeople> findDunningPeopleCycleByIds(List<String> ids);
	
	public List<TMisDunningPeople> findDunningPeopleGroupby();
	
	public List<TMisDunningPeople> findPeopleBybeginEnd(TMisDunningPeople dunningPeople);
	
	/**
	 * 根据周期查询催收人员
	 * @param dunningcycle
	 * @return
	 */
	public List<TMisDunningPeople> findPeopleByDunningcycle(String dunningcycle);
	
	/**
	 * 批量更新完成的任务
	 * @param ids
	 * @return
	 */
	public int batchUpdateDunningcycle(@Param("pids")List<String> pids,@Param("userid")String userid,@Param("dunningcycle")String dunningcycle);
	

}