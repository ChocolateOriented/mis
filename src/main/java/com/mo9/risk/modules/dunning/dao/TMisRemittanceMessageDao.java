/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;

/**
 * 财务确认汇款信息DAO接口
 * @author 徐盛
 * @version 2016-08-11
 */
@MyBatisDao
public interface TMisRemittanceMessageDao extends CrudDao<TMisRemittanceMessage> {
	
	public TMisRemittanceMessage findRemittanceMesListByDealcode(String code);

	public List<TMisRemittanceMessage> findByList(@Param("list") List<TMisRemittanceMessage> tMisRemittanceList);

	public int saveList(@Param("list") List<TMisRemittanceMessage> tMisRemittanceList);
	
}