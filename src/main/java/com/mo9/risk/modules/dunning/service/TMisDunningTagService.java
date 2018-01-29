/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
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

	public static final ConcurrentHashMap<String, Long> dealcodeTagType = new ConcurrentHashMap<String, Long>();

	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	@Autowired
	private TMisDunningTagDao tMisDunningTagDao;
	
	/**
	 * 保存标签
	 * @param tMisDunningTag
	 * @return
	 */
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
	
	/**
	 * 编辑标签
	 * @param tMisDunningTag
	 * @return
	 */
	@Transactional(readOnly = false)
	public int editTag(TMisDunningTag tMisDunningTag) {
		String peopleId = UserUtils.getUser().getId();
		TMisDunningPeople people = tMisDunningPeopleService.get(peopleId);
		tMisDunningTag.setPeopleid(peopleId);
		tMisDunningTag.setPeoplename(people == null ? "" : people.getNickname());
		return updateTag(tMisDunningTag);
	}
	
	/**
	 * 删除标签
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public void closeTag(TMisDunningTag tMisDunningTag) {
		tMisDunningTag.preUpdate();
		tMisDunningTag.setDelFlag(TMisDunningTag.DEL_FLAG_DELETE);
		int num = tMisDunningTagDao.upadateDelflag(tMisDunningTag);
		if (num > 0) {
			List<TMisDunningTag> list = tMisDunningTagDao.select(tMisDunningTag);
			for (TMisDunningTag misDunningTag : list){
				dao.saveTagHistory(tMisDunningTag);
			}
		}
//		tMisDunningTag.setDelFlag(TMisDunningTag.DEL_FLAG_DELETE);
//		updateTag(tMisDunningTag);
	}
	
	/**
	 * 更新标签
	 * @param tMisDunningTag
	 * @return
	 */
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
	
	/**
	 * 校验是否所有标签类型已添加
	 * @param buyerid
	 * @return
	 */
	@Transactional
	public boolean preCheckExist(String buyerid) {
		List<String> existTags = dao.getExistTagByBuyerid(buyerid);
		
		if (existTags == null) {
			return true;
		}
		
		return TagType.values().length > existTags.size();
	}
	/**
	 * 保证标签唯一
	 * @param buyerid
	 * @param tagtype
	 * @return
	 */
	public boolean typeExist(TMisDunningTag tMisDunningTag) {
		
		return  StringUtils.isBlank(dao.typeExist(tMisDunningTag));
	}
	@Transactional(readOnly = false)
	public void closeRemark(TMisDunningTag tMisDunningTag) {
		tMisDunningTag.setDelFlag(TMisDunningTag.DEL_FLAG_DELETE);
		updateTag(tMisDunningTag);
	}
}