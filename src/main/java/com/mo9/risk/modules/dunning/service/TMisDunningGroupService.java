package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.entity.TMisDunningOrganization;
import java.util.ArrayList;
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

	/**
	 * @Description 根据用户权限查询小组
	 * @param group
	 * @param user
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisDunningGroup>
	 */
	public List<TMisDunningGroup> findAuthorizedGroups(TMisDunningGroup group, User user) {
		if (group == null){
			group = new TMisDunningGroup();
		}
		int permissions = TMisDunningTaskService.getPermissions();
		//催收员
		if (permissions == TMisDunningTaskService.DUNNING_COMMISSIONER_PERMISSIONS){
			return new ArrayList<>();
		}
		this.addAuthorizedCondition(group,user);
		return dao.findAuthorizedGroups(group);
	}

	/**
	 * @Description 用户有权限查询的小组的id
	 * @param user
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisDunningGroup>
	 *   null代表不控制
	 *   size = 0 代表没有可查的组
	 */
	public List<String> findAllAuthorizedGroupIds(User user) {
		TMisDunningGroup	group = new TMisDunningGroup();
		//催收员
		int permissions = TMisDunningTaskService.getPermissions();
		if (permissions == TMisDunningTaskService.DUNNING_COMMISSIONER_PERMISSIONS){
			return null;
		}
		if (permissions == TMisDunningTaskService.DUNNING_INNER_PERMISSIONS || permissions == TMisDunningTaskService.DUNNING_SUPERVISOR) {//组长
			this.addAuthorizedCondition(group,user);
			return dao.findAuthorizedGroupsIds(group);
		}else {//其他角色不做控制
			return null;
		}
	}

	/**
	 * @Description 根据用户角色添加查询条件
	 * @param group
	 * @param user
	 * @return void
	 */
	private void addAuthorizedCondition(TMisDunningGroup group, User user){
		int permissions = TMisDunningTaskService.getPermissions();

		if (permissions == TMisDunningTaskService.DUNNING_INNER_PERMISSIONS) {//组长
			group.setLeader(user);
		}else if (permissions == TMisDunningTaskService.DUNNING_SUPERVISOR) {//监理
			TMisDunningOrganization organization = group.getOrganization();
			if (organization == null){
				organization = new TMisDunningOrganization();
				group.setOrganization(organization);
			}
			organization.setSupervisor(user);
		}
	}
}
