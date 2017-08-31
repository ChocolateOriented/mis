/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisDunningOrderDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.thinkgem.jeesite.common.service.BaseService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 因切换江湖救急库需要在不同service中开启新的事务
 * @author jxli
 * @version 2017/7/6
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningOrderForRiskService extends BaseService{

	@Autowired
	private TMisDunningOrderDao tMisDunningOrderDao;
	/**
	 * @Description 使用新事务(必须使用不同service), 查询未还清订单
	 * @param deselcodes
	 * @return java.util.List<java.lang.String>
	 */
	@Transactional(readOnly = true,propagation = Propagation.REQUIRES_NEW)
	public List<String> findPaymentOrederWithNewTransactional(List<String> deselcodes) {
		if (deselcodes == null || deselcodes.size() == 0){
			return new ArrayList<String>();
		}
		return tMisDunningOrderDao.findPaymentOreder(deselcodes);
	}

	/**
	 * @Description 使用新事务(必须使用不同service), 查询订单
	 * @param dealcode
	 * @return java.util.List<java.lang.String>
	 */
	@Transactional(readOnly = true,propagation = Propagation.REQUIRES_NEW)
	public DunningOrder findOrderByDealcodeWithNewTransactional(String dealcode) {
		return tMisDunningOrderDao.findOrderByDealcode(dealcode);
	}
}
