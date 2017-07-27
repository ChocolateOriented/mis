/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.bean.QianxilvCorpu;
import com.mo9.risk.modules.dunning.bean.QianxilvNew;
import com.mo9.risk.modules.dunning.bean.TmpMoveCycle;
import com.mo9.risk.modules.dunning.entity.TMisMigrationRateReport;

/**
 * 迁徙率DAO接口
 * @author 徐盛
 * @version 2017-07-24
 */
@MyBatisDao
public interface TMisMigrationRateReportDao extends CrudDao<TMisMigrationRateReport> {
	
	
	//迁徙率关于户数
	//更新已经采集的逾期订单
	public void householdsUpdateHaveBeenCollectDealcode();
	//采集今天逾期一天的数据
	public void householdsInsertOverOneDay(@Param("cycle")int cycle,@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	//采集新的迁徙率的统计数据
	public void householdsInsertStatisticalData();
	
	//更新逾期1天的当天到期的订单数
	public void householdsUpdateOverOneDay();
	//更新Q1数据
	public void householdsUpdateQ1(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	//更新PayoffQ1	
	public void householdsUpdatePayoffQ1();
	//更新PayoffQ2
	public void householdsUpdatePayoffQ2();
	//更新PayoffQ3
	public void householdsUpdatePayoffQ3();
	//更新PayoffQ4
	public void householdsUpdatePayoffQ4();
	//更新Q2
	public void householdsUpdateQ2();
	//更新Q3
	public void householdsUpdateQ3();
	//更新Q4
	public void householdsUpdateQ4();
	
	
	//迁徙率关于本金
	//采集新的迁徙率的统计数据
	public void principalInsertStatisticalData();
	//更新逾期1天的当天到期的订单数
	public void principalUpdateOverOneDay();
	//更新Q1数据
	public void principalUpdateQ1(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	//更新PayoffQ1	
	public void principalUpdatePayoffQ1();
	//更新PayoffQ2
	public void principalUpdatePayoffQ2();
	//更新PayoffQ3
	public void principalUpdatePayoffQ3();
	//更新PayoffQ4
	public void principalUpdatePayoffQ4();
	//更新Q2
	public void principalUpdateQ2();
	//更新Q3
	public void principalUpdateQ3();
	//更新Q4
	public void principalUpdateQ4();
	
	
	/**
	 * 最大週期数
	 * @return
	 */
	public int getMaxcycle();
	
	/**
	 * 插入TmpMoveCycle
	 * @param cycle
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public int insertTmpMoveCycle(@Param("cycle")int cycle,@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	
	/**
	 * 根据日期查询cycle队列
	 * @param datetime
	 * @return
	 */
	public TmpMoveCycle getTmpMoveCycleByDatetime(@Param("datetime")Date datetime);
	
	
	/**
	 * 根据cycle查询cycle队列
	 * @param datetime
	 * @return
	 */
	public TmpMoveCycle getTmpMoveCycleByCycle(@Param("cycle")Integer cycle);
	
	/**
	 * 查询户数
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public List<QianxilvNew> getQianxilvNew();
	
	
	/**
	 * 查询本金
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public List<QianxilvCorpu> getQianxilvCorpu();
	
	
	/**
	 * 根据周期时间段查询Q1户数总和
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public QianxilvNew getSumQ1QianxilvNewByCycleDatetime(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	
	
	/**
	 * 根据周期时间段查询Q1本金总和
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public QianxilvCorpu getSumQ1QianxilvCorpuByCycleDatetime(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	
	
	/**
	 * 根据周期时间段查询Q2户数总和
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public QianxilvNew getSumQ2QianxilvNewByCycleDatetime(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	
	
	/**
	 * 根据周期时间段查询Q2本金总和
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public QianxilvCorpu getSumQ2QianxilvCorpuByCycleDatetime(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	
	/**
	 * 根据周期时间段查询Q3户数总和
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public QianxilvNew getSumQ3QianxilvNewByCycleDatetime(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	
	
	/**
	 * 根据周期时间段查询Q3本金总和
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public QianxilvCorpu getSumQ3QianxilvCorpuByCycleDatetime(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	
	
	/**
	 * 根据周期时间段查询Q4户数总和
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public QianxilvNew getSumQ4QianxilvNewByCycleDatetime(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	
	
	/**
	 * 根据周期时间段查询Q4本金总和
	 * @param datetimestart
	 * @param datetimeend
	 * @return
	 */
	public QianxilvCorpu getSumQ4QianxilvCorpuByCycleDatetime(@Param("datetimestart")Date datetimestart,@Param("datetimeend")Date datetimeend);
	
	
}