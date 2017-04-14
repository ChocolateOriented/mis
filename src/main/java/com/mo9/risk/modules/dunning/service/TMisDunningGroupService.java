package com.mo9.risk.modules.dunning.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningGroupDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.service.CrudService;

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
}
