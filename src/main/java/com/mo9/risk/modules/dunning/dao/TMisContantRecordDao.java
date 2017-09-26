/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TelNumberBean;
import com.mo9.risk.modules.dunning.entity.TmisDunningSmsTemplate;

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
	
	/**
	 * 
	 * 批量保存
	 * @param list
	 */
	public int saveList(@Param("list") List<TMisContantRecord> tcontList);
	


	public void updateList(TMisContantRecord tMisContantRecord);

	/**
	 * 自动电催结论
	 * @param date
	 * @return
	 */
	public List<TMisContantRecord> findautoTelConclusion(@Param("cycleAndTime")String cycleAndTime);

	/**
	 * n:3;2电催结论
	 * @param dunningpeopleid
	 * @param dealcode
	 * @return 
	 */
	public List<TMisContantRecord> findDirTelConculsion(@Param("dunningpeopleId")String dunningpeopleId, @Param("dealcode")String dealcode, @Param("dunningCycle")String dunningCycle);

	
}