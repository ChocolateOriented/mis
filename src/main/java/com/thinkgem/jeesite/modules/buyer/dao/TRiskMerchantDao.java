/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.buyer.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.buyer.entity.TRiskMerchant;

/**
 * 用户报表DAO接口
 * @author 徐盛
 * @version 2016-05-26
 */
@MyBatisDao
public interface TRiskMerchantDao extends CrudDao<TRiskMerchant> {
	
}