/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;

/**
 * 通讯录信息DAO接口
 * @author beargao
 * @version 2016-07-15
 */
@MyBatisDao
public interface TBuyerContactDao extends CrudDao<TBuyerContact> {
	
	public List<TBuyerContact> findList(TBuyerContact tBuyerContact);
	
	public List<TMisSendMsgInfo> getContactsByBuyerId(String buyerId);
}