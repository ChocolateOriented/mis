/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import java.util.List;

/**
 * @Description 订单
 * @author jxli
 * @version 2017/7/6
 */
@MyBatisDao
public interface TMisDunningOrderDao extends BaseDao {

	List<DunningOrder> findPaymentOrderDetail(DunningOrder order);

	DunningOrder findOrderByDealcode(String dealcode);

	List<DunningOrder> findPaymentOrderMsg(DunningOrder queryOrder);

	List<String> findPaymentOreder(List<String> shouldPayoffOrderDelcodes);
}