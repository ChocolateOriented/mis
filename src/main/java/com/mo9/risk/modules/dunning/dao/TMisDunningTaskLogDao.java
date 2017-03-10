/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningTaskLog;

/**
 * 催收任务logDAO接口
 * @author 徐盛
 * @version 2017-03-01
 */
@MyBatisDao
public interface TMisDunningTaskLogDao extends CrudDao<TMisDunningTaskLog> {
	
	
	/**
	 * 批量保存任务日志Log
	 * @param TMisDunningTaskLog
	 * @return
	 */
	public int batchInsertTaskLog(@Param("list")List<TMisDunningTaskLog> list);
	
	
}