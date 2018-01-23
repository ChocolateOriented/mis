package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import com.mo9.risk.modules.dunning.entity.TMisDunningOrganization;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 催收机构DAO接口
 * @author shijlu
 */
@MyBatisDao
public interface TMisDunningOrganizationDao extends CrudDao<TMisDunningOrganization> {

	/**
	 * 查询机构监理列表
	 * @param tMisDunningOrganization
	 * @return
	 */
	public List<TMisDunningPeople> findOrganizationSupervisorList(TMisDunningOrganization tMisDunningOrganization);
}
