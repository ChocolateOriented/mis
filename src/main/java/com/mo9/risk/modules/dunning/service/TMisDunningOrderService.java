/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisDunningOrderDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.thinkgem.jeesite.common.service.BaseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 订单
 * @author jxli
 * @version 2017/7/6
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningOrderService extends BaseService{

	@Autowired
	private TMisDunningOrderDao tMisDunningOrderDao;

	/***
	 * @Description  通过订单号查询订单
	 * @param dealcode
	 * @return com.mo9.risk.modules.dunning.entity.DunningOrder
	 */
	public DunningOrder findOrderByDealcode(String dealcode) {
		return tMisDunningOrderDao.findOrderByDealcode(dealcode);
	}

	/**
	 * @Description 通过手机查询未还款订单
	 * @param mobile
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	DunningOrder findPaymentOrderByMobile(String mobile){
		List<DunningOrder> orders = tMisDunningOrderDao.findPaymentOrderByMobile(mobile);
		if (null == orders || orders.size() == 0){
			return null ;
		}
		return orders.get(0);
	}

	/**
	 * @Description 查询未还款订单
	 * @param order
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	List<DunningOrder> findPaymentOrder(DunningOrder order){
		return tMisDunningOrderDao.findPaymentOrder(order);
	}
}
