/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup.GroupType;
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
			//因为要是用User的ID,所以不调用父类save生成新id
			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(user.getId())){
				tMisDunningPeople.setUpdateBy(user);
				tMisDunningPeople.setCreateBy(user);
			}
			Date now = new Date();
			tMisDunningPeople.setUpdateDate(now);
			tMisDunningPeople.setCreateDate(now);
			
			dao.insert(tMisDunningPeople);
		}else{
			tMisDunningPeople.preUpdate();
			dao.update(tMisDunningPeople);
		}
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
		TMisDunningGroup dunningGroup = tMisDunningPeople.getGroup() ;
		if (dunningGroup == null) {
			dunningGroup = new TMisDunningGroup() ;
			tMisDunningPeople.setGroup(dunningGroup);
		}
		List<GroupType> queryTypes = dunningGroup.getQueryTypes() ;
		if (queryTypes == null) {
			queryTypes = new ArrayList<GroupType>() ;
			dunningGroup.setQueryTypes(queryTypes);
		}
		
		int permissions = TMisDunningTaskService.getPermissions();
		//内部催收主管可查看自营
		if(TMisDunningTaskService.DUNNING_INNER_PERMISSIONS == permissions){
			queryTypes.add(GroupType.selfSupport);
		}
		//外部催收主管可查看外包坐席及委外佣金
		if(TMisDunningTaskService.DUNNING_OUTER_PERMISSIONS == permissions){
			queryTypes.add(GroupType.outsourceCommission);
			queryTypes.add(GroupType.outsourceSeat);
		}
		//催收专员只能查看自己
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
	
	
	/**
	 * 根据周期查询催收人员
	 * @param dunningcycle
	 * @return
	 */
	public List<TMisDunningPeople> findPeopleByDunningcycle(String dunningcycle){
		return tMisDunningPeopleDao.findPeopleByDunningcycle(dunningcycle);
	}
	
	
	/**
	 * 批量更新完成的任务
	 * @param
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchUpdateDunningcycle(List<String> pids,String userid,String dunningcycle){
		return tMisDunningPeopleDao.batchUpdateDunningcycle(pids, userid, dunningcycle);
	}

	/**
	 * @Description: 查询用户花名及Id列表 , 支持通过GroupId与nickname查询
	 * @param tMisDunningPeople
	 * @return
	 * @return: List<TMisDunningPeople>
	 */
	public List<TMisDunningPeople> findOptionList(TMisDunningPeople tMisDunningPeople) {
		return tMisDunningPeopleDao.findOptionList(tMisDunningPeople);
	}
	
	/**
	 * 根据周期查询催收人员-手动分配
	 * @param dunningcycle
	 * @return
	 */
	public List<TMisDunningPeople> findPeopleByDistributionDunningcycle(String dunningcycle){
		return tMisDunningPeopleDao.findPeopleByDistributionDunningcycle(dunningcycle);
	}

	/**
	 * @Description: 检查花名唯一性
	 * @param nickname
	 * @param id
	 * @return
	 * @return: Boolean
	 */
	public Boolean checkNicknameUnique(String nickname, String id) {
		if (StringUtils.isBlank(nickname)) {
			return false ;
		}
		TMisDunningPeople queryPeople = new TMisDunningPeople() ;
		queryPeople.setId(id);
		queryPeople.setNickname(nickname);
		return dao.checkNicknameUnique(queryPeople);
	}
	
	/**
	 * @Description: 根据id更新催收人员姓名
	 * @param dunningPeople
	 * @return
	 */
	public int updatePeopleNameById(TMisDunningPeople dunningPeople) {
		return dao.updatePeopleNameById(dunningPeople);
	}
}