/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TelNumberBean;

/**
 * 催收任务联系记录DAO接口
 * @author ycheng
 * @version 2016-07-15
 */
@MyBatisDao
public interface TMisContantRecordDao extends CrudDao<TMisContantRecord> {
	
	/**
	 * 根据订单号返回手机号码发送短信次数
	 * @param dealcode
	 * @return
	 */
	public List<TelNumberBean> findSmsNum(String dealcode);
	
	
	/**
	 * 根据订单号&手机号&催收类型查询操作详情
	 * @param contantRecord
	 * @return
	 */
	public List<TMisContantRecord> findDetailByDealcodeandTel(TMisContantRecord contantRecord);
	
	
}