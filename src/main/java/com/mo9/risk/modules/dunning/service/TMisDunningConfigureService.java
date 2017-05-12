/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningConfigureDao;

/**
 * 催收配置Service
 * @author shijlu
 * @version 2017-04-11
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningConfigureService {

	@Autowired
	private TMisDunningConfigureDao tMisDunningConfigureDao;

	public String getConfigureValue(String key) {
		return tMisDunningConfigureDao.get(key);
	}
}