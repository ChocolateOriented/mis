/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;

import com.mo9.risk.modules.dunning.bean.PayChannelInfo;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;

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
	 * 获取已提交的扣款记录
	 * @param status
	 * @return
	 */
	public List<TMisDunningDeduct> getSubmittedDeductList();
	
	/**
	 * 根据订单号查询最新的扣款记录
	 * @param dealcode
	 * @return
	 */
	public TMisDunningDeduct getLatestDeductByDealcode(String dealcode);
	
	/**
	 * 查询银行卡在给定渠道当天余额不足的代扣记录数
	 * @param tMisDunningDeduct
	 * @return
	 */
	public int getNoBalanceDeductNum(TMisDunningDeduct tMisDunningDeduct);
	
	/**
	 * 根据登录账号查询催收人id
	 * @param users
	 * @return
	 */
	public List<String> getPeopleIdByUsers(List<String> users);
	
	/**
	 * 根据代扣范围查询代扣订单号
	 * @param dunningOrder
	 * @return
	 */
	public List<String> getDealcodeByScope(DunningOrder dunningOrder);
	
	/**
	 * 根据订单号查询用户信息
	 * @param dealcode
	 * @return
	 */
	public TRiskBuyerPersonalInfo getBuyerInfoByDealcode(String dealcode);

	/**
	 * @Description 查询订单状态异常的代扣信息
	 * @param
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisDunningDeduct>
	 */
	List<TMisDunningDeduct> findAbnormalDeduct();
}