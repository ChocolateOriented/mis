/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyer2contacts;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 联系人信息DAO接口
 * @author beargao
 * @version 2016-07-15
 */
@MyBatisDao
public interface TRiskBuyer2contactsDao {
	public List<TRiskBuyer2contacts> getContactsByBuyerId(@Param("buyerId")String buyerId,@Param("dealcode")String dealcode);
	
	public List<TMisSendMsgInfo> getSendMsgByBuyerIdAndType(String buyerId,String type);
	/**
	 * 查询该用户联系人的所有电话
	 * @param buyerId
	 * @return
	 */
	public Set<String> findContactMobile(@Param("buyerId")String buyerId);
	
}