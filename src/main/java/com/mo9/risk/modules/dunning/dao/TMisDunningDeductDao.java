/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;

import com.mo9.risk.modules.dunning.bean.PayChannelInfo;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;

/**
 * 催收代扣DAO接口
 * @author shijlu
 * @version 2017-04-11
 */
@MyBatisDao
public interface TMisDunningDeductDao extends CrudDao<TMisDunningDeduct> {
	
	/**
	 * 获取所有的扣款渠道信息
	 * @param bankname
	 * @return
	 */
	public List<PayChannelInfo> getPayChannelList(String bankname);
	
	/**
	 * 获取支持的扣款渠道数
	 * @param bankname
	 * @return
	 */
	public int getSupportedChannelCount(String bankname);
	
	/**
	 * 获取支持的扣款渠道
	 * @param bankname
	 * @return
	 */
	public List<PayChannelInfo> getSupportedChannel(String bankname);
	
	/**
	 * 获取渠道扣款成功率
	 * @param paychannel
	 * @return
	 */
	public List<PayChannelInfo> getSuccessRateByChannel(String paychannel);
	
	/**
	 * 根据状态获取扣款记录
	 * @return
	 */
	public List<TMisDunningDeduct> getDeductListByStatus(String status);
	
	/**
	 * 根据订单号查询最新的扣款记录
	 * @param dealcode
	 * @return
	 */
	public TMisDunningDeduct getLatestDeductByDealcode(String dealcode);
	
	/**
	 * 查询银行卡当天是否有余额不足的代扣记录
	 * @param dealcode
	 * @return
	 */
	public int getNoBalanceDeductNumByCard(String bankcard);

}