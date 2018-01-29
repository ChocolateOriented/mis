/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;


import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.enums.DebtBizType;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import java.util.Date;
import java.util.List;
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
	 * 根据催收队列查询催收人员
	 * @param dunningcycle
	 * @return
	 */
	public List<TMisDunningPeople> findPeopleByDunningcycle(String dunningcycle);
	
	/**
	 * 根据周期查询催收人员按金额排序
	 * @param dunningcycle
	 * @return
	 */
	public List<TMisDunningPeople> findPeopleSumcorpusamountByDunningcycle(@Param("dunningcycle")String dunningcycle,@Param("begindatetime")Date begindatetime,@Param("enddatetime")Date enddatetime,@Param("debtbiztypeValue")String debtbiztypeValue,@Param("debtbiztypeDescription")String debtbiztypeDescription);
	
	/**
	 * 批量更新完成的任务
	 * @param pids userid dunningcycle
	 * @return
	 */
	public int batchUpdateDunningcycle(@Param("pids")List<String> pids,@Param("userid")String userid,@Param("dunningcycle")String dunningcycle);
	
	
	/**
	 * 根据id集合查询对象
	 * @param dunningcycle
	 * @return
	 */
	public List<TMisDunningPeople> findPeoplesByids(@Param("pids")List<String> pids,@Param("dunningcycle")String dunningcycle);

	/**
	 * @Description: 查询用户花名及Id列表 , 支持通过GroupId与nickname查询
	 * @param tMisDunningPeople
	 * @return
	 * @return: List<TMisDunningPeople>
	 */
	public List<TMisDunningPeople> findOptionList(TMisDunningPeople tMisDunningPeople);
	
	
	
	/**
	 * 根据催收队列查询催收人员-手动分案
	 * @param dunningcycle
	 * @return
	 */
	public List<TMisDunningPeople> findPeopleByDistributionDunningcycle(String dunningcycle);

	public Boolean checkNicknameUnique(TMisDunningPeople queryPeople);

	/**
	 * @Description: 根据id更新催收人员姓名
	 * @param dunningPeople
	 * @return
	 */
	public int updatePeopleNameById(TMisDunningPeople dunningPeople);

	public List<TMisDunningPeople> findAgentPeopleList();

	/**
	 * @Description:根据催收队列,催收员类型,分案状态,催收小组查询催收人员
	 * @param dunningcycle
	 * @param type
	 * @param auto
	 * @param name
	 * @param organizationName 
	 */
	public List<TMisDunningPeople> findPeopleByCycleTypeAutoName(@Param("dunningcycle")String[] dunningcycle, @Param("type")String[] type, @Param("auto")String[] auto,
			@Param("name")String name ,@Param("dunningpeoplename")String dunningpeoplename, @Param("bizType")String bizType, @Param("organizationName")String organizationName);
	
	/**
	 * 批量更新小组分配,自动分配
	 * @param ids
	 * @param userId
	 * @param tMisDunningPeople
	 * @return
	 */
	public int operationUpdate(@Param("ids")List<String> ids,@Param("userId") String userId, @Param("tMisDunningPeople")TMisDunningPeople tMisDunningPeople);
	
	/**
	 * 校验数据库的账号和所属组和花名
	 * @param tMisDunningPeople
	 * @return
	 */
	public TMisDunningPeople validateBatchAccountAndGroup(TMisDunningPeople tMisDunningPeople);

	/**
	 * 删除催收人关联的产品
	 * @param id
	 */
	void deleteBizTypeByPeopleId(String id);

	/**
	 * @Description 批量插入催收员关联的产品 
	 * @param bizTypes
	 * @return void
	 */
	void batchInsertPeopleBizTypes(@Param("bizTypes")List<DebtBizType> bizTypes,@Param("peopleId")String peopleId);
}