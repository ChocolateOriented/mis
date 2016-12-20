/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;

/**
 * 汇款确认信息DAO接口
 * @author 徐盛
 * @version 2016-09-12
 */
@MyBatisDao
public interface TMisRemittanceConfirmDao extends CrudDao<TMisRemittanceConfirm> {
	
	
	public TMisRemittanceConfirm findRemittanceConfirmColumnByDealcode(String code);
	
	/**
	 * 催收更新汇款数据
	 * @param entity
	 * @return
	 */
	public int remittanceUpdate(TMisRemittanceConfirm entity);
	
	/**
	 * 催收更新还款数据
	 * @param entity
	 * @return
	 */
	public int confirmationUpdate(TMisRemittanceConfirm entity);
	
	/**
	 * 财务更新到账数据
	 * @param entity
	 * @return
	 */
	public int financialUpdate(TMisRemittanceConfirm entity);
	
	/**
	 * 返回是否是部分
	 * @param dealcode
	 * @return
	 */
	public int getResult(String dealcode);
	
	/**
	 * 根据催收唯一标示查询条数
	 * @param number
	 * @return
	 */
	public int getSerialnumber(String number);
	
	/**
	 * 根据财务唯一标示查询条数
	 * @param number
	 * @return
	 */
	public int getFinancialserialnumber(String number);
	
}