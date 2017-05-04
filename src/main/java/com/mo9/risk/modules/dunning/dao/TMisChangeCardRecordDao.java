/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.BankCardInfo;
import com.mo9.risk.modules.dunning.entity.TMisChangeCardRecord;

/**
 * 换卡记录DAO接口
 * @author shijlu
 * @version 2017-04-11
 */
@MyBatisDao
public interface TMisChangeCardRecordDao extends CrudDao<TMisChangeCardRecord> {
	
	/**
	 * 获取当前的扣款银行卡信息
	 * @param tMisChangeCardRecord
	 * @return
	 */
	public TMisChangeCardRecord getCurrentBankCard(String dealcode);
	
	/**
	 * 获取所有渠道支持的银行
	 * @return
	 */
	public List<String> getAllChannelBank();

	/**
	 * 根据卡号查询银行银行
	 * @param bankCard
	 * @return
	 */
	public BankCardInfo getBankByCard(String bankCard);
}