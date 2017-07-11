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
	 * @Description 查询未还款订单, 包含借款人信息
	 * @param queryOrder
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	public DunningOrder findPaymentOrderMsg(DunningOrder queryOrder){
		List<DunningOrder> orders = tMisDunningOrderDao.findPaymentOrderMsg(queryOrder);
		if (null == orders || orders.size() == 0){
			return null ;
		}
		return orders.get(0);
	}

	/**
	 * @Description 通过手机查询未还款订单, 包含借款人信息
	 * @param mobile
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	public DunningOrder findPaymentOrderMsgByMobile(String mobile){
		DunningOrder queryOrder = new DunningOrder();
		queryOrder.setMobile(mobile);
		return findPaymentOrderMsg(queryOrder);
	}

	/**
	 * @Description 通过订单号查询未还款订单, 包含借款人信息
	 * @param dealcode
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	public DunningOrder findPaymentOrderMsgByDealcode(String dealcode) {
		DunningOrder queryOrder = new DunningOrder();
		queryOrder.setDealcode(dealcode);
		return findPaymentOrderMsg(queryOrder);
	}

	/**
	 * @Description 查询未还款订单详情包含借款人, 催收任务信息
	 * @param order
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	DunningOrder findPaymentOrderDetail(DunningOrder order){
		List<DunningOrder> dunningOrders = tMisDunningOrderDao.findPaymentOrderDetail(order);
		if (dunningOrders != null && dunningOrders.size() > 0) {
			return dunningOrders.get(0);
		}
		return null;
	}

	/**
	 * @return com.mo9.risk.modules.dunning.entity.DunningOrder
	 * @Description 通过电话查询未还款订单详情, 包含借款人, 催收任务信息
	 */
	public DunningOrder findPaymentOrderDetailByMobile(String mobile) {
		DunningOrder order = new DunningOrder();
		order.setMobile(mobile);
		return findPaymentOrderDetail(order);
	}

}
