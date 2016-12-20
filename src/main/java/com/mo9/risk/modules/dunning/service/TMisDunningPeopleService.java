/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 催收人员Service
 * @author 徐盛
 * @version 2016-07-12
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningPeopleService extends CrudService<TMisDunningPeopleDao, TMisDunningPeople> {

	@Autowired
	private TMisDunningPeopleDao tMisDunningPeopleDao;
	
	public TMisDunningPeople get(String id) {
		return super.get(id);
	}
	
	public List<TMisDunningPeople> findList(TMisDunningPeople tMisDunningPeople) {
		return super.findList(this.getDunningPeople(tMisDunningPeople));
	}
	
	public List<TMisDunningPeople> findAllList(TMisDunningPeople tMisDunningPeople) {
		return dao.findAllList(tMisDunningPeople);
	}
	
	public Page<TMisDunningPeople> findPage(Page<TMisDunningPeople> page, TMisDunningPeople tMisDunningPeople) {
		return super.findPage(page, this.getDunningPeople(tMisDunningPeople));
	}
	
	//根据人员类型 查找催收人员
	public List<TMisDunningPeople> findDunningPeopleByType(String peopleType){
		return tMisDunningPeopleDao.findDunningPeopleByType(peopleType);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisDunningPeople tMisDunningPeople) {
		if(null == tMisDunningPeople.getDbid()){
			dao.insert(tMisDunningPeople);
		}else{
			dao.update(tMisDunningPeople);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisDunningPeople tMisDunningPeople) {
		super.delete(tMisDunningPeople);
	}
	
	/**
	 * 查询用户
	 * @return
	 */
	public List<User> findUserList(){
		return tMisDunningPeopleDao.findUserList();
	}
	
	/**
	 * 根据权限查询催收人员
	 * @param tMisDunningPeople
	 * @return
	 */
//	public List<TMisDunningPeople> findListByPermissions(TMisDunningPeople tMisDunningPeople) {
//		return tMisDunningPeopleDao.findList(this.getDunningPeople(tMisDunningPeople));
//	}
	
	/**
	 * 获取权限
	 * @param tMisDunningPeople
	 * @return
	 */
	public TMisDunningPeople getDunningPeople(TMisDunningPeople tMisDunningPeople){
		int permissions = TMisDunningTaskService.getPermissions();
//		if(TMisDunningTaskService.DUNNING_ALL_PERMISSIONS == permissions){
//			tMisDunningPeople.setDunningpeopletype("");
//		}
		if(TMisDunningTaskService.DUNNING_INNER_PERMISSIONS == permissions){
			tMisDunningPeople.setDunningpeopletype("inner");
		}
		if(TMisDunningTaskService.DUNNING_OUTER_PERMISSIONS == permissions){
			tMisDunningPeople.setDunningpeopletype("outer");
		}
		if(TMisDunningTaskService.DUNNING_COMMISSIONER_PERMISSIONS == permissions){
			tMisDunningPeople.setId(UserUtils.getUser().getId());
		}
		return tMisDunningPeople;
	}
	
	/**
	 * 根据催收Id对催收周期进行分组
	 * @param ids
	 * @return
	 */
	public List<TMisDunningPeople> findDunningPeopleCycleByIds(List<String> ids){
		return tMisDunningPeopleDao.findDunningPeopleCycleByIds(ids);
	}
	
	/**
	 * 催收周期分组
	 * @return
	 */
	public List<TMisDunningPeople> findDunningPeopleGroupby(){
		return tMisDunningPeopleDao.findDunningPeopleGroupby();
	}
	
	/**
	 * 根据催收周期查询催收人员
	 * @param dunningPeople
	 * @return
	 */
	public List<TMisDunningPeople> findPeopleBybeginEnd(TMisDunningPeople dunningPeople){
		return tMisDunningPeopleDao.findPeopleBybeginEnd(dunningPeople);
	}
	
}