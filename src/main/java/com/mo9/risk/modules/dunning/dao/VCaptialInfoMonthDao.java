/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.VCaptialInfoMonth;

/**
 * 资金成本月报DAO接口
 * @author 徐盛
 * @version 2016-09-25
 */
@MyBatisDao
public interface VCaptialInfoMonthDao extends CrudDao<VCaptialInfoMonth> {
	
}