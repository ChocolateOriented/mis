/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessagChecked;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @Description 订单
 * @author jxli
 * @version 2017/7/6
 */
@MyBatisDao
public interface TMisDunningOrderDao extends BaseDao {

	List<DunningOrder> findPaymentOrderByMobile(String mobile);

	List<DunningOrder> findPaymentOrder(DunningOrder order);

	DunningOrder findOrderByDealcode(String dealcode);
}