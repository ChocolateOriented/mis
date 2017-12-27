/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.enums.DebtBizType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup.GroupType;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
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
			tMisDunningPeople.setIsNewRecord(true);
		}
		super.save(tMisDunningPeople);

		//更新关联的产品
		this.updatePeopleBizTypes(tMisDunningPeople.getId(),tMisDunningPeople.getBizTypes());
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
	 * 根据周期队列查询催收人员
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
	@Transactional(readOnly = false)
	public int updatePeopleNameById(TMisDunningPeople dunningPeople) {
		return dao.updatePeopleNameById(dunningPeople);
	}
	
	public List<TMisDunningPeople> findAgentPeopleList() {
		return tMisDunningPeopleDao.findAgentPeopleList();
	}

	/**
	 * @Description:根据催收队列,催收员类型,分案状态,催收小组查询催收人员
	 * @param dunningcycle
	 * @param type
	 * @param auto
	 * @param name
	 */
	public List<TMisDunningPeople> findPeopleByCycleTypeAutoName(String[] dunningcycle, String[] type, String[] auto, String name, String dunningpeoplename, String bizType) {
		return tMisDunningPeopleDao.findPeopleByCycleTypeAutoName(dunningcycle, type, auto, name, dunningpeoplename, bizType);
	}
	/**
	 * 批量更新小组分配,自动分配
	 * @param ids
	 * @param userId
	 * @param tMisDunningPeople
	 * @return
	 */
	@Transactional(readOnly = false)
	public int operationUpdate(List<String> ids, String userId, TMisDunningPeople tMisDunningPeople) {
		return tMisDunningPeopleDao.operationUpdate(ids,userId,tMisDunningPeople);
	}
	/**
	 * 批量插入校验
	 * @param list
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean batchInsert(List<TMisDunningPeople> list,StringBuilder message)throws Exception {
		HashMap<String,String> validateMap=new HashMap<String,String>();
		for (int i = 0; i < list.size(); i++) {
			//校验不能为空
			if(StringUtils.isBlank(list.get(i).getName())||StringUtils.isBlank(list.get(i).getNickname())||StringUtils.isBlank(list.get(i).getGroupName())||
					StringUtils.isBlank(list.get(i).getDunningcycle())||StringUtils.isBlank(list.get(i).getAuto())){
				message.append("第"+(i+1)+"条必要字段内容为空,请检查");
				throw new ServiceException("第"+(i+1)+"条必要字段内容为空,请检查");
			}
			//校验账号和花名在文件中是唯一的
			if(validateMap==null||(StringUtils.isEmpty(validateMap.get(list.get(i).getName()))&&StringUtils.isEmpty(validateMap.get(list.get(i).getNickname())))){
				validateMap.put(list.get(i).getName(),list.get(i).getName());
				validateMap.put(list.get(i).getNickname(),list.get(i).getNickname());
			}else{
				message.append("第"+(i+1)+"条账号或者花名与上面数据重复,请检查");
				throw new ServiceException("第"+(i+1)+"条账号或者花名与上面数据重复,请检查");
			}
			//自动分配
			if(!("是".equals(list.get(i).getAuto())||"否".equals(list.get(i).getAuto()))){
				message.append("第"+(i+1)+"条是否自动分配值错误,请检查");
				throw new ServiceException("第"+(i+1)+"条是否自动分配值错误,请检查");
			}

			//产品名
			List<DebtBizType> debtBizTypes = new ArrayList<>();
			String[] bizTypeDescs = list.get(i).getBizTypesStr().split(",");
			for (int j = 0; j < bizTypeDescs.length; j++) {
				String bizTypeDesc = bizTypeDescs[j];
				boolean find = false;
				for (DebtBizType debtBizType: DebtBizType.values()) {
					if (debtBizType.getDesc().equals(bizTypeDesc)){
						debtBizTypes.add(debtBizType);
						find = true;
						break;
					}
				}
				if (!find){
					message.append("第"+(i+1)+"条产品列错误,请检查");
					throw new ServiceException("第"+(i+1)+"条产品列错误,请检查");
				}
			}
			
			//校验催收队列
			String[] cycle = list.get(i).getDunningcycle().split(",");
			for (int j = 0; j < cycle.length; j++) {
				if(!("Q0".equals(cycle[j])||"Q1".equals(cycle[j])||"Q2".equals(cycle[j])||
						"Q3".equals(cycle[j])||"Q4".equals(cycle[j])||"Q5".equals(cycle[j]))){
					message.append("第"+(i+1)+"条催收队列错误,请检查");
					throw new ServiceException("第"+(i+1)+"条催收队列错误,请检查");
				}
			}
			//校验数据库的账号和所属组和花名
			TMisDunningPeople tPeople= tMisDunningPeopleDao.validateBatchAccountAndGroup(list.get(i));
			if(tPeople==null){
				message.append("第"+(i+1)+"条账号不存在,请检查");
				throw new ServiceException("第"+(i+1)+"条账号不存在,请检查");
			}
			if(StringUtils.isNotEmpty(tPeople.getValidateId())){
				message.append("第"+(i+1)+"条已经是催收员了,请检查");
				throw new ServiceException("第"+(i+1)+"条已经是催收员了,请检查");
			}
			if(StringUtils.isEmpty(tPeople.getId())){
				message.append("第"+(i+1)+"条账号不存在,请检查");
				throw new ServiceException("第"+(i+1)+"条账号不存在,请检查");
			}
			if(tPeople.getGroup()==null||StringUtils.isEmpty(tPeople.getGroup().getId())){
				message.append("第"+(i+1)+"条所属组不存在,请检查");
				throw new ServiceException("第"+(i+1)+"条所属组不存在,请检查");
			}
			if(StringUtils.isNotEmpty(tPeople.getNickname())){
				message.append("第"+(i+1)+"条该花名已存在,请检查");
				throw new ServiceException("第"+(i+1)+"条该花名已存在,请检查");
			}
			TMisDunningPeople people=new TMisDunningPeople();
			people.setId(tPeople.getId());
			people.setName(tPeople.getName());
			people.setNickname(list.get(i).getNickname());
			people.setGroup(tPeople.getGroup());
			people.setAuto("是".equals(list.get(i).getAuto())?"t":"f");
			people.setDunningcycle(list.get(i).getDunningcycle());
			people.setBizTypes(debtBizTypes);
			//因为要是用User的ID,所以不调用父类save生成新id
			this.save(people);
		}
		return true;
	}

	/**
	 * @Description 更新催收人关联产品
	 * @param peopleId
	 * @param bizTypes
	 * @return void
	 */
	@Transactional(readOnly = false)
	public void updatePeopleBizTypes(String peopleId, List<DebtBizType> bizTypes) {
		dao.deleteBizTypeByPeopleId(peopleId);
		if (bizTypes == null || bizTypes.size() == 0){
			return;
		}
		dao.batchInsertPeopleBizTypes(bizTypes,peopleId);
	}

	/**
	 * @Description 批量更改催收人关联产品
	 * @param peopleids
	 * @param bizTypes
	 * @return void
	 */
	@Transactional(readOnly = false)
	public void batchUpdatepeopleBizTypes(List<String> peopleids, List<DebtBizType> bizTypes) {
		for (int i = 0; i < peopleids.size(); i++) {
			this.updatePeopleBizTypes(peopleids.get(i),bizTypes);
		}
	}
}