/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningRefund;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 退款记录DAO接口
 * @author jxli
 * @version 2017-09-22
 */
@MyBatisDao
public interface TMisDunningRefundDao extends CrudDao<TMisDunningRefund> {

	/**
	 * @Description 查询退款中与完成退款的退款记录
	 * @param remittanceSerialNumber
	 * @param remittanceChannel
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisDunningRefund>
	 */
	List<TMisDunningRefund> findValidBySerialNumber(@Param("remittanceSerialNumber")String remittanceSerialNumber, @Param("remittanceChannel")String remittanceChannel);
}