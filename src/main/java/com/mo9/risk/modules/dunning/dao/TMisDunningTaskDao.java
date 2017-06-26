/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.entity.AppLoginLog;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.DunningOuterFile;
import com.mo9.risk.modules.dunning.entity.DunningOuterFileLog;
import com.mo9.risk.modules.dunning.entity.DunningPeriod;
import com.mo9.risk.modules.dunning.entity.DunningUserInfo;
import com.mo9.risk.modules.dunning.entity.OrderHistory;
import com.mo9.risk.modules.dunning.entity.PartialOrder;
import com.mo9.risk.modules.dunning.entity.PerformanceDayReport;
import com.mo9.risk.modules.dunning.entity.PerformanceMonthReport;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisDunningTaskLog;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

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
	 * 查询数据列表-优化版
	 * @param entity
	 * @return
	 */
	public List<DunningOrder> newfindOrderPageList(DunningOrder dunningOrder);
	
	
	/**
	 * 查询委外任务数据列表
	 * @param entity
	 * @return
	 */
	public List<DunningOrder> findOuterOrderPageList(DunningOrder dunningOrder);
	
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
	
	
	//===========================================新分案方法====================================================================================
	
	/**
	 * 查询已过期的任务和到期时应催金额
	 * @return
	 */
	public List<TMisDunningTask> newfindExpiredTask();
	
	
	/**
	 * 批量更新过期任务
	 * @param ids
	 * @return
	 */
	public int batchUpdateExpiredTask(List<TMisDunningTask> dunningTasks);
	
	/**
	 * 根据催收周期查询延期任务
	 * @param dunningcycle
	 * @return
	 */
	public List<TMisDunningTaskLog> newfindDelayTaskByDunningcycle(@Param("dunningtaskstatus")String dunningtaskstatus,@Param("dunningcycle")String dunningcycle,@Param("begin")String begin,@Param("end")String end );
	
	/**
	 * 根据逾期天数查询未生成任务task的订单
	 * @param day
	 * @return
	 */
	public List<TMisDunningTaskLog> newfingDelayOrderByNotTask(@Param("day")String day);
	
	/**
	 * 根据订单状态和任务状态查询订单任务Log
	 * @param status
	 * @param dunningtaskstatus
	 * @return
	 */
	public List<TMisDunningTaskLog> newfingTaskByOrderStatusandTaskstatus(@Param("status")String status,@Param("dunningtaskstatus")String dunningtaskstatus);
	
	/**
	 * 批量添加任务
	 * @param ids
	 * @return
	 */
	public int batchinsertTask(List<TMisDunningTask> dunningTasks);

	
	/**
	 * 批量更新完成的任务
	 * @param ids
	 * @return
	 */
	public int batchUpdatePayoffTask(List<TMisDunningTask> dunningTasks);
	
	
	/**
	 * 根据订单号查询任务log
	 * @param dealcode
	 * @return
	 */
	public TMisDunningTaskLog newfingTaskByDealcode(String dealcode);
	
	
	/**
	 * 根据订单号查询任务log
	 * @param dealcode
	 * @return
	 */
	public List<TMisDunningTaskLog> newfingTasksByDealcodes(@Param("dealcodes")List<String> dealcodes,@Param("dunningcycle")String dunningcycle);
	
	
	/**
	 * 批量更新手动分配任务
	 * @param ids
	 * @return
	 */
	public int batchUpdateDistributionTask(List<TMisDunningTask> dunningTasks);
	
	
	/**
	 * 批量更新委外手动分配任务
	 * @param ids
	 * @return
	 */
	public int batchUpdateOutDistributionTask(List<TMisDunningTask> dunningTasks);
	
	/**
	 * 查询需要系统发送的订单
	 * @param numafter 
	 * @param numbefore 
	 * @return
	 */
	public List<DunningOrder> findallAtuoSms(@Param("numbefore")Integer numbefore,@Param("numafter") Integer numafter);
	
	/**
	 * 查询预提醒新进入正在催收案件buyerid
	 * @return
	 */
	public Vector<String> findBuyeridByNewTask(@Param("day")String day);
	
	/**
	 * 查询用户身份证影像资料
	 * @return
	 */
	public String findBuyerIdCardImg(String buyerid);
	
	/**
	 * atuoq0  催收预提醒订单的优质老用户（历史逾期1天内还清）
	 * @return
	 */
	public List<String> findAtuoQ0Dealcode(@Param("day")String day,@Param("payoffday")String payoffday);
	
}