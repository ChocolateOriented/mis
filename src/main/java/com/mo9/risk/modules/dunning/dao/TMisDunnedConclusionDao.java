/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TMisDunnedConclusion;

/**
 * 电催结论DAO接口
 * @author shijlu
 * @version 2017-03-06
 */
@MyBatisDao
public interface TMisDunnedConclusionDao extends CrudDao<TMisDunnedConclusion> {

	/**
	 * 根据电话号码去重查询电催action
	 * @param param
	 * @return
	 */
	public List<TMisContantRecord> findTelActionContacts(TMisDunnedConclusion tMisDunnedConclusion);

	/**
	 * 更新电催action关联的电催结论
	 * @param tMisDunnedConclusion
	 * @return
	 */
	public int updateTelAction(TMisDunnedConclusion tMisDunnedConclusion);

}