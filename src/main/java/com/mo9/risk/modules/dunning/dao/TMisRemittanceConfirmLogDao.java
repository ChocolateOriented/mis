/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirmLog;

/**
 * 汇款确认信息日志DAO接口
 * @author shijlu
 * @version 2017-05-26
 */
@MyBatisDao
public interface TMisRemittanceConfirmLogDao extends CrudDao<TMisRemittanceConfirmLog> {

}