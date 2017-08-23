/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningTagDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTag;
import com.mo9.risk.modules.dunning.enums.TagType;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 用户标签Service
 * @author shijlu
 * @version 2017-08-22
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningTagService extends CrudService<TMisDunningTagDao, TMisDunningTag> {
	
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	@Transactional(readOnly = false)
	public String saveTag(TMisDunningTag tMisDunningTag) {
		String peopleId = UserUtils.getUser().getId();
		TMisDunningPeople people = tMisDunningPeopleService.get(peopleId);
		tMisDunningTag.setPeopleid(peopleId);
		tMisDunningTag.setPeoplename(people == null ? "" : people.getNickname());
		
		this.save(tMisDunningTag);
		dao.saveTagHistory(tMisDunningTag);
		return tMisDunningTag.getId();
	}
	
	@Transactional(readOnly = false)
	public int editTag(TMisDunningTag tMisDunningTag) {
		String peopleId = UserUtils.getUser().getId();
		TMisDunningPeople people = tMisDunningPeopleService.get(peopleId);
		tMisDunningTag.setPeopleid(peopleId);
		tMisDunningTag.setPeoplename(people == null ? "" : people.getNickname());
		return updateTag(tMisDunningTag);
	}
	
	@Transactional(readOnly = false)
	public void closeTag(String id) {
		TMisDunningTag tMisDunningTag = new TMisDunningTag();
		tMisDunningTag.setId(id);
		tMisDunningTag.setDelFlag(TMisDunningTag.DEL_FLAG_DELETE);
		updateTag(tMisDunningTag);
	}
	
	@Transactional(readOnly = false)
	public int updateTag(TMisDunningTag tMisDunningTag) {
		tMisDunningTag.preUpdate();
		int num = dao.update(tMisDunningTag);
		if (num > 0) {
			tMisDunningTag = get(tMisDunningTag.getId());
			dao.saveTagHistory(tMisDunningTag);
		}
		
		return num;
	}
	
	@Transactional
	public boolean preCheckExist(String dealcode) {
		List<String> existTags = dao.getExistTagByDealcode(dealcode);
		
		if (existTags == null) {
			return true;
		}
		
		return TagType.values().length > existTags.size();
	}
}