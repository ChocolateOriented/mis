/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import java.util.List;
import java.util.Set;

/**
 * 财务确认汇款信息DAO接口
 * @author 徐盛
 * @version 2016-08-11
 */
@MyBatisDao
public interface TMisRemittanceMessageDao extends CrudDao<TMisRemittanceMessage> {
	
	public TMisRemittanceMessage findRemittanceMesListByDealcode(String code);

	List<DunningOrder> findPaymentOrderByMobile(Set<String> mobiles);
}