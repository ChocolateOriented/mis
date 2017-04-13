package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * @Description 催收小组DAO接口
 * @author LiJingXiang
 * @version 2017年4月11日
 */
@MyBatisDao
public interface TMisDunningGroupDao extends CrudDao<TMisDunningGroup> {
	
}
