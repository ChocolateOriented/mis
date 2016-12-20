/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderAfterLoanPushing;

/**
 * 贷后催款情况报表DAO接口
 * @author 徐盛
 * @version 2016-06-20
 */
@MyBatisDao
public interface VRiskOrderAfterLoanPushingDao extends CrudDao<VRiskOrderAfterLoanPushing> {
	
	
	public List<String> findPushingTotal(Map<String, String> map);
	
}