/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 催收配置DAO接口
 * @author shijlu
 * @version 2017-04-11
 */
@MyBatisDao
public interface TMisDunningConfigureDao {

	public String get(String key);
}