package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningGroupDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.User;

@Service
@Transactional(readOnly = true)
public class TMisDunningGroupService extends CrudService<TMisDunningGroupDao,TMisDunningGroup> {
	@Override
	@Transactional(readOnly = false)
	public void delete(TMisDunningGroup entity) {
		entity.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
		entity.preUpdate();
		super.delete(entity);
	}

	/**
	 * @Description: 查询所有用户(用于添加组长)
	 * @return
	 * @return: List<User>
	 */
	public List<User> findUserList() {
		return dao.findUserList();
	}

	/**
	 * @Description: 保存分配催收小组
	 * @return
	 */
	@Transactional(readOnly = false)
	public int saveDistribution(TMisDunningGroup tMisDunningGroup) {
		tMisDunningGroup.preUpdate();
		return dao.saveDistribution(tMisDunningGroup);
	}

	/**
	 * @Description: 重置已分配小组
	 * @return
	 */
	@Transactional(readOnly = false)
	public int resetGroupOrganization(TMisDunningGroup tMisDunningGroup) {
		tMisDunningGroup.preUpdate();
		tMisDunningGroup.setOrganization(null);
		return dao.saveDistribution(tMisDunningGroup);
	}

	/**
	 * @Description: 查询监理下的小组
	 * @return
	 */
	public List<String> findSupervisorGroupList(TMisDunningGroup tMisDunningGroup) {
		return dao.findSupervisorGroupList(tMisDunningGroup);
	}

	/**
	 * @Description  通过组长查询组
	 * @param leader
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisDunningGroup>
	 */
	public List<String> findIdsByLeader(User leader){
		return dao.findIdsByLeader(leader);
	}
}
