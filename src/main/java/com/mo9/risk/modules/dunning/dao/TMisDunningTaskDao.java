/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;


import com.mo9.risk.modules.dunning.entity.*;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.entity.PartialOrder;

/**
 * 催收任务DAO接口
 * @author 徐盛
 * @version 2016-07-12
 */
@MyBatisDao
public interface TMisDunningTaskDao extends CrudDao<TMisDunningTask> {
	
	public void generateTast();

	public List<TMisDunningTask> findPayoffDunningTask(Map<String,Object> params);

	public List<TMisDunningTask> findExpiredTask(Map<String,Object> params);
	
	public List<DunningPeriod> findDunningPeriod(Map<String,Object> params);

	public List<Map<String,Object>> findDunningTaskByPeople(Map<String,Object> params);

	public List<TMisDunningOrder> findNeedDunningOrder(Map<String,Object> params);

	public TMisDunningOrder findOrderByDealcode(String dealcode);

	public TMisDunningTask findDunningTaskByDealcode(Map<String,Object> params);

	public DunningUserInfo findDunningUserInfo(String dealcode);

	public int updateOrder(TMisDunningOrder order);
	
	public DunningOrder getOrders(String id);
	
	/**
	 * 记录催收操作记录时间
	 * @param ids
	 * @return
	 */
	public int updatedunningtime(String id);
	
	/**
	 * 记录催收操作记录时间
	 * @param ids
	 * @return
	 */
	public int updatedunningtimeList(List<String> ids);
	
	/**
	 * 修改委外导出时间
	 * @param order
	 * @return @Param("week")
	 */
	public int updateOuterfiletime(@Param("outerfiletime")Date outerfiletime, @Param("dealcodes")List<String> dealcodes);
	
	/*催收留案功能-留案提交 Patch 0001 by GQWU at 2016-11-10 start*/
	public int updateDunningTimeByDealcodes(@Param("dealcodes")String[] dealcodes,@Param("deferDate")Date deferDate);
	/*催收留案功能-留案提交 Patch 0001 by GQWU at 2016-11-10 end*/
	
	/*催收留案功能-订单查询 Patch 0001 by GQWU at 2016-11-25 start*/
	/*仅查询留案逻辑所需部分数据，非逾期订单完整数据*/
	public List<DunningOrder> findOrdersByDealcodes (@Param("dealcodes")String[] dealcodes);
	/*催收留案功能-订单查询 Patch 0001 by GQWU at 2016-11-25 end*/
	
	/**
	 * 委外导出记录Log
	 * @param fileLog
	 * @return
	 */
	public int batchInsert(@Param("list")List<DunningOuterFileLog> list);
	
	/**
	 * 查询数据列表
	 * @param entity
	 * @return
	 */
	public List<DunningOrder> findOrderPageList(DunningOrder dunningOrder);
	
	/**
	 * 查询全部task任务
	 * @return
	 */
	public List<DunningOrder> findAllTaskList();
	
	/**
	 * 委外导出数据
	 * @param outerFile
	 * @return
	 */
	public List<DunningOuterFile> exportOuterFile(List<String> orders);
	
	/**
	 * 查询订单历史借款记录
	 * @param orderHistory
	 * @return
	 */
	public List<OrderHistory> findOrderHistoryList(String buyerid);

	/**
	 *  查询部分还款订单
	 * @return
	 */
	public List<PartialOrder> findPartialOrders();
	
	/**
	 * 保存最后电话记录
	 * @return
	 */
	public int updateTelRemark(@Param("telremark")String telremark, @Param("id")String id);
	
	/**
	 * 催收绩效月报
	 * @param performanceMonthReport
	 * @return
	 */
	public List<PerformanceMonthReport> findPerformanceMonthReport(PerformanceMonthReport performanceMonthReport);
	
	/**
	 * 催收绩效月报
	 * @param performanceMonthReport
	 * @return
	 */
	public List<PerformanceDayReport> findPerformanceDayReport(PerformanceDayReport performanceDayReport);
	
	/**
	 * 查询催收人员正在催收的任务量
	 * @param dunningpeopleid
	 * @return
	 */
	public int findDunningCount(@Param("dunningpeopleid")String dunningpeopleid);
	
	
	/**
	 * 修改部分还款订单的减免
	 * @param reliefamount
	 * @param rootprderid
	 * @return
	 */
	public int updateOrderPartial(@Param("reliefamount")BigDecimal reliefamount,@Param("rootorderid")Integer rootorderid);
	
	
	/**
	 * 查询全部正在催收的task任务buyerid
	 * @return
	 */
	public List<DunningOrder> findDunningBuyerid();
	
	
	public List<AppLoginLog> findApploginlog(String mobile);
	
}