/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import org.apache.ibatis.annotations.Param;

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
	 * 催收更新合并还款数据
	 * @param param
	 * @return
	 */
	public int confirmationMergeUpdate(Map<String, Object> param);
	
	/**
	 * 财务更新到账数据
	 * @param entity
	 * @return
	 */
	public int financialUpdate(TMisRemittanceConfirm entity);
	
	/**
	 * 财务打回到账数据
	 * @param entity
	 * @return
	 */
	public int financialReturn(TMisRemittanceConfirm entity);
	
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
	
	/**
	 * 获取延期次数
	 * @param orderid
	 * @return
	 */
	public int getExistDelayNumber(Integer orderid);
	
	/**
	 * 获取关联的汇款记录
	 * @param tMisRemittanceConfirm
	 * @return
	 */
	public List<TMisRemittanceConfirm> findRelatedList(TMisRemittanceConfirm tMisRemittanceConfirm);

	/**
	 * @Description 批量插入
	 * @param confirms
	 * @return void
	 */
	void batchInsert(@Param("list")List<TMisRemittanceConfirm> confirms);

	/**
	 * @Description 查账流程--入账
	 * @param confirm
	 * @return void
	 */
	void auditConfrimUpdate(TMisRemittanceConfirm confirm);
}