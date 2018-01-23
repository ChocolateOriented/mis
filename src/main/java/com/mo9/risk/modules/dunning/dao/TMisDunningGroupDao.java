package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * @Description 催收小组DAO接口
 * @author LiJingXiang
 * @version 2017年4月11日
 */
@MyBatisDao
public interface TMisDunningGroupDao extends CrudDao<TMisDunningGroup> {

	/**
	 * @Description: 查询所有用户
	 * @return
	 * @return: List<User>
	 */
	List<User> findUserList();
	
	/**
	 * @Description: 保存分配催收小组
	 * @return
	 */
	int saveDistribution(TMisDunningGroup tMisDunningGroup);

	/**
	 * @Description: 查询监理下的小组
	 * @return
	 */
	List<String> findSupervisorGroupList(TMisDunningGroup tMisDunningGroup);

	List<String> findIdsByLeader(User user);
}
