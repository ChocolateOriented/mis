package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningOrganizationDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrganization;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.thinkgem.jeesite.common.service.CrudService;

@Service
@Transactional(readOnly = true)
public class TMisDunningOrganizationService extends CrudService<TMisDunningOrganizationDao, TMisDunningOrganization> {

	/**
	 * 查询机构监理列表
	 * @param tMisDunningOrganization
	 * @return
	 */
	public List<TMisDunningPeople> findOrganizationSupervisorList(TMisDunningOrganization tMisDunningOrganization) {
		return dao.findOrganizationSupervisorList(tMisDunningOrganization);
	}
}
