/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderAfterLoan;

/**
 * 贷后还款情况DAO接口
 * @author 徐盛
 * @version 2016-06-20
 */
@MyBatisDao
public interface VRiskOrderAfterLoanDao extends CrudDao<VRiskOrderAfterLoan> {
	
}