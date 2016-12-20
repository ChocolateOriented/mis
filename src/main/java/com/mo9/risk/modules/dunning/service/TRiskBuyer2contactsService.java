/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TRiskBuyer2contactsDao;
import com.mo9.risk.modules.dunning.entity.TRiskBuyer2contacts;

/**
 * 联系人信息Service
 * @author beargao
 * @version 2016-07-15
 */
@Service
@Transactional(readOnly = true)
public class TRiskBuyer2contactsService {

	@Autowired
	private TRiskBuyer2contactsDao tRiskBuyer2contactsDao;

	public List<TRiskBuyer2contacts> getContactsByBuyerId(@Param("buyerId")String buyerId,@Param("dealcode")String dealcode){
		return tRiskBuyer2contactsDao.getContactsByBuyerId(buyerId,dealcode);
	}
}