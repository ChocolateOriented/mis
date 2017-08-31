/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.bean.QianxilvCorpu;
import com.mo9.risk.modules.dunning.bean.QianxilvNew;
import com.mo9.risk.modules.dunning.bean.TmpMoveCycle;
import com.mo9.risk.modules.dunning.dao.TMisMigrationRateReportDao;
import com.mo9.risk.modules.dunning.entity.TMisMigrationRateReport;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;

/**
 * 迁徙率Service
 * @author 徐盛
 * @version 2017-07-24
 */
@Service
@Transactional(readOnly = true)
public class TMisMigrationRateReportService extends CrudService<TMisMigrationRateReportDao, TMisMigrationRateReport> {
	
	@Autowired
	private TMisMigrationRateReportDao tMisMigrationRateReportDao;
	

	public TMisMigrationRateReport get(String id) {
		return super.get(id);
	}
	
	public List<TMisMigrationRateReport> findList(TMisMigrationRateReport tMisMigrationRateReport) {
		return super.findList(tMisMigrationRateReport);
	}
	
	public Page<TMisMigrationRateReport> findPage(Page<TMisMigrationRateReport> page, TMisMigrationRateReport tMisMigrationRateReport) {
		return super.findPage(page, tMisMigrationRateReport);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisMigrationRateReport tMisMigrationRateReport) {
		super.save(tMisMigrationRateReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisMigrationRateReport tMisMigrationRateReport) {
		super.delete(tMisMigrationRateReport);
	}
	
	
	/**
	 * 获取昨天月份的天数
	 * @param date
	 * @return
	 */
    public static int getYesterDaysOfMonth() {  
        Calendar calendar = Calendar.getInstance();  
        calendar.add(Calendar.DATE,-1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
    }  
	
	/**
	 * 返回昨天日期号
	 * @param date
	 * @return
	 */
    public static int getYesterday() {  
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.DATE,-1);
    	int datenum = c.get(Calendar.DATE);
		return datenum;
    } 
	
	public static void main(String[] args) {
//		Calendar cal=Calendar.getInstance();
//		cal.add(Calendar.DATE,8);
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MILLISECOND, 0);
//		Date time = cal.getTime();
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(time));
//		System.out.println(getMonthLastDayDate());
//		BigDecimal cp1new = new BigDecimal(12).subtract(new BigDecimal(9)).divide(new BigDecimal(3),2,BigDecimal.ROUND_HALF_UP); 
//		System.out.println(cp1new);
		
		Calendar c = Calendar.getInstance();
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

		try {
			//开始时间必须小于结束时间
			Date beginDate = dateFormat1.parse("2014-05-01");
			Date endDate = dateFormat1.parse("2014-06-25");
			Date date = beginDate;
			while (!date.equals(endDate)) {
			System.out.println(dateFormat1.format(date));
			c.setTime(date);
			c.add(Calendar.DATE, 1); // 日期加1天
			date = c.getTime();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**  
	* 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指  
	* 定精度，以后的数字四舍五入。  
	* @param v1 被除数  
	* @param v2 除数  
	* @param scale 表示表示需要精确到小数点以后几位。  
	* @return 两个参数的商  
	*/  
	public static double div(double v1,double v2,int scale){   
		if(scale<0){   
			throw new IllegalArgumentException(   
			"The scale must be a positive integer or zero");   
		}   
		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();   
	}    
	
	/**  
	* 提供精确的小数位四舍五入处理。  
	* @param v 需要四舍五入的数字  
	* @param scale 小数点后保留几位  
	* @return 四舍五入后的结果  
	*/  
	public static double round(double v,int scale){   
		if(scale<0){   
			throw new IllegalArgumentException("The scale must be a positive integer or zero");   
		}   
		BigDecimal b = new BigDecimal(Double.toString(v));   
		BigDecimal one = new BigDecimal("1");   
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();   
	}   
  
	
	/**
	 * BigDecimal 值计算（ 先- 后/ ）
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param scale
	 * @return
	 */
	public static BigDecimal calculate(BigDecimal b1,BigDecimal b2,BigDecimal b3,int scale){   
		if(scale<0){   
			throw new IllegalArgumentException(   
			"The scale must be a positive integer or zero");   
		}
//		BigDecimal cp1new = qianxilvnew1.getQ1().subtract(qianxilvnew1.getPayoffq1()).divide(qianxilvnew1.getOrderduedate(),2,BigDecimal.ROUND_HALF_UP); 
		BigDecimal value = b1.subtract(b2).divide(b3,scale,BigDecimal.ROUND_HALF_UP); 
		return value;   
	} 

	/**
	 * 根據參數返回日期 
	 * @param amount
	 * @return
	 */
	public static Date getDate(int amount){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,amount);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date time = cal.getTime();
		return time;
	}
	
	public static Date getMonthLastDayDate(){
		Calendar cal=Calendar.getInstance();
//		cal.add(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
		Date time = cal.getTime();
		return time;
	}
	
	
	/**
	 * 获取明天月份的天数
	 * @param date
	 * @return
	 */
    public static int getTomorrowDaysOfMonth() {  
        Calendar calendar = Calendar.getInstance();  
        calendar.add(Calendar.DATE,1);
//        calendar.setTime(date);  
//        System.out.println(calendar.get(Calendar.DATE));
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
    }  
	
	/**
	 * 返回明天日期号
	 * @param date
	 * @return
	 */
    public static int getTomorrow() {  
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.DATE,1);
    	int datenum = c.get(Calendar.DATE);
		return datenum;
    }  
	
    
    /**
     * 每天定时更新迁徙率DB
     */
//	@Scheduled(cron = "0 15 0 * * ?")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void autoInsertTmpMoveCycleDB() {
		
		try {
			int maxcycle = tMisMigrationRateReportDao.getMaxcycle() + 1;
			Date datetimestart = null;
			Date datetimeend = null;
//			Date Yesterday = getDate(-1);
			switch (getTomorrowDaysOfMonth()) {
				case 30:
					switch (getTomorrow()) {
					case 1:
						datetimestart = getDate(1);
						datetimeend = getDate(15);
						tMisMigrationRateReportDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
//						migrationRateGetData();
//						insertMigrationRateReportDB(Yesterday);
						return;
					case 16:
						datetimestart = getDate(1);
						datetimeend = getMonthLastDayDate();
						tMisMigrationRateReportDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
//						migrationRateGetData();
//						insertMigrationRateReportDB(Yesterday);
						return;
					default:
//						migrationRateGetData();
//						insertMigrationRateReportDB(Yesterday);
						return;
					}
					
				case 31:
					switch (getTomorrow()) {
					case 1:
						datetimestart = getDate(1);
						datetimeend = getDate(16);
						tMisMigrationRateReportDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
//						migrationRateGetData();
//						insertMigrationRateReportDB(Yesterday);
						return;
					case 17:
						datetimestart = getDate(1);
						datetimeend = getMonthLastDayDate();
						tMisMigrationRateReportDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
//						migrationRateGetData();
//						insertMigrationRateReportDB(Yesterday);
						return;
					default:
//						migrationRateGetData();
//						insertMigrationRateReportDB(Yesterday);
						return;
					}
					
				case 28:
					return;
				default:
					return;
			}
		} catch (Exception e) {
			logger.warn("每天定时更新迁徙率DB任务失败"+ new Date());
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * @return void
	 * @Description 迁徙率数据的表更新
	 */
//	@Scheduled(cron = "0 18 0 * * ?")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void autoMigrationRateGetData() {
		try {
			logger.info( new Date()+"开始迁徙数据采集:");
			Date today = getDate(0);
			Date Yesterday = getDate(-1);
			int maxcycle = tMisMigrationRateReportDao.getMaxcycle();
			//执行一系列的迁徙率数据获取
			tMisMigrationRateReportDao.householdsUpdateHaveBeenCollectDealcode();
			logger.info("户数：更新已经采集的逾期订单 执行完成。");
			tMisMigrationRateReportDao.householdsInsertOverOneDay(maxcycle,Yesterday,today);
			logger.info("户数：采集今天逾期一天的数据 执行完成。");
			tMisMigrationRateReportDao.householdsInsertStatisticalData();
			logger.info("户数：采集新的迁徙率的统计数据 执行完成。");
			tMisMigrationRateReportDao.householdsUpdateOverOneDay();
			logger.info("户数：更新逾期1天的当天到期的订单数 执行完成。");
			tMisMigrationRateReportDao.householdsUpdateQ1(Yesterday, today);
			logger.info("户数：更新Q1数据 执行完成。");
			tMisMigrationRateReportDao.householdsUpdatePayoffQ1();
			logger.info("户数：更新PayoffQ1 执行完成。");
			tMisMigrationRateReportDao.householdsUpdatePayoffQ2();
			logger.info("户数：更新PayoffQ2 执行完成。");
			tMisMigrationRateReportDao.householdsUpdatePayoffQ3();
			logger.info("户数：更新PayoffQ3 执行完成。");
			tMisMigrationRateReportDao.householdsUpdatePayoffQ4();
			logger.info("户数：更新PayoffQ4 执行完成。");
			tMisMigrationRateReportDao.householdsUpdateQ2();
			logger.info("户数：更新Q2 执行完成。");
			tMisMigrationRateReportDao.householdsUpdateQ3();
			logger.info("户数：更新Q3  执行完成。");
			tMisMigrationRateReportDao.householdsUpdateQ4();
			logger.info("户数：更新Q4  执行完成。");
			
			//迁徙率关于本金 
			tMisMigrationRateReportDao.principalInsertStatisticalData();
			logger.info("本金：采集新的迁徙率的统计数据   执行完成。");
			tMisMigrationRateReportDao.principalUpdateOverOneDay();
			logger.info("本金：更新逾期1天的当天到期的订单数   执行完成。");
			tMisMigrationRateReportDao.principalUpdateQ1(Yesterday, today);
			logger.info("本金：更新Q1数据   执行完成。");
			tMisMigrationRateReportDao.principalUpdatePayoffQ1();
			logger.info("本金：更新PayoffQ1   执行完成。");
			tMisMigrationRateReportDao.principalUpdatePayoffQ2();
			logger.info("本金：更新PayoffQ2   执行完成。");
			tMisMigrationRateReportDao.principalUpdatePayoffQ3();
			logger.info("本金：更新PayoffQ3   执行完成。");
			tMisMigrationRateReportDao.principalUpdatePayoffQ4();
			logger.info("本金：更新PayoffQ4   执行完成。");
			tMisMigrationRateReportDao.principalUpdateQ2();
			logger.info("本金：更新Q2   执行完成。");
			tMisMigrationRateReportDao.principalUpdateQ3();
			logger.info("本金：更新Q3   执行完成。");
			tMisMigrationRateReportDao.principalUpdateQ4();
			logger.info("本金：更新Q4   执行完成。");
			logger.info( new Date()+"迁徙数据采集完成。");
		} catch (Exception e) {
			logger.warn("迁徙率数据表更新任务失败"+ new Date());
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		}
	}

	
//	/**
//     * 迁徙率计算insertDB
//     */
//	@Scheduled(cron = "0 35 0 * * ?")
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//	public void autoInsertMigrationRateReportDB_job() {
//		Date Yesterday = getDate(-1);
//		autoInsertMigrationRateReportDB(Yesterday);
//	}
	
	
	/**
     * 迁徙率计算insertDB
     */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void autoInsertMigrationRateReportDB(Date Yesterday) {
		try {
//			if(null == Yesterday){
//				Yesterday = getDate(-1);
//			}
			TMisMigrationRateReport migrationRateReport = new TMisMigrationRateReport();
			TmpMoveCycle tmpMoveCycle = tMisMigrationRateReportDao.getTmpMoveCycleByDatetime(Yesterday);
//			new BigDecimal(12).subtract(new BigDecimal(9)).divide(new BigDecimal(3),2,BigDecimal.ROUND_HALF_UP); 
			
			if( null != tmpMoveCycle ){
				
				migrationRateReport.setCycle(String.valueOf(tmpMoveCycle.getCycle()));
				migrationRateReport.setDatetime(Yesterday);
				migrationRateReport.setCreateDate(new Date());
				
				TmpMoveCycle tmpMoveCycleBefore1 = tMisMigrationRateReportDao.getTmpMoveCycleByCycle(tmpMoveCycle.getCycle() - 1);  // 前一个周期
				TmpMoveCycle tmpMoveCycleBefore2 = tMisMigrationRateReportDao.getTmpMoveCycleByCycle(tmpMoveCycle.getCycle() - 2);  // 前二个周期
				TmpMoveCycle tmpMoveCycleBefore3 = tMisMigrationRateReportDao.getTmpMoveCycleByCycle(tmpMoveCycle.getCycle() - 3);  // 前三个周期
				
				// C-P1  户数迁徙  C-P1（每日）= 期末余额/期初余额
				QianxilvNew qianxilvnew1 = tMisMigrationRateReportDao.getSumQ1QianxilvNewByYesterday(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend(),Yesterday);
				BigDecimal cp1new = calculate(qianxilvnew1.getQ1(), qianxilvnew1.getPayoffq1(), qianxilvnew1.getOrderduedate(), 4);
				
				migrationRateReport.setCp1new(cp1new);
				
				// C-P2  户数迁徙   C-P2（每日）=（C-P1）*（P1-P2）
				QianxilvNew qianxilvnew1Before1 = tMisMigrationRateReportDao.getSumQ1QianxilvNewByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal cp1newBefore1 = calculate(qianxilvnew1Before1.getQ1(), qianxilvnew1Before1.getPayoffq1(), qianxilvnew1Before1.getOrderduedate(), 4);
				QianxilvNew qianxilvnew2 = tMisMigrationRateReportDao.getSumQ2QianxilvNewByCycleDatetime(tmpMoveCycle.getDatetimestart(), Yesterday);
				BigDecimal p1p2new = calculate(qianxilvnew2.getQ2(), qianxilvnew2.getPayoffq2(), qianxilvnew2.getQ2(), 4);
				
				BigDecimal cp2new = cp1newBefore1.multiply(p1p2new);
				migrationRateReport.setCp2new(cp2new);
				
				// C-P3  户数迁徙 C-P3（每日）=（C-P1）*（P1-P2）*（P2-P3）
				QianxilvNew qianxilvnew1Before2 = tMisMigrationRateReportDao.getSumQ1QianxilvNewByCycleDatetime(tmpMoveCycleBefore2.getDatetimestart(), tmpMoveCycleBefore2.getDatetimeend());
				BigDecimal cp1newBefore2 = calculate(qianxilvnew1Before2.getQ1(), qianxilvnew1Before2.getPayoffq1(), qianxilvnew1Before2.getOrderduedate(), 4);
				QianxilvNew qianxilvnew2Before1 = tMisMigrationRateReportDao.getSumQ2QianxilvNewByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal p1p2newBefore1 = calculate(qianxilvnew2Before1.getQ2(), qianxilvnew2Before1.getPayoffq2(), qianxilvnew2Before1.getQ2(), 4);
				QianxilvNew qianxilvnew3 = tMisMigrationRateReportDao.getSumQ3QianxilvNewByCycleDatetime(tmpMoveCycle.getDatetimestart(), Yesterday);
				BigDecimal p2p3new = calculate(qianxilvnew3.getQ3(), qianxilvnew3.getPayoffq3(), qianxilvnew3.getQ3(), 4);
				
				BigDecimal cp3new = cp1newBefore2.multiply(p1p2newBefore1).multiply(p2p3new);
				migrationRateReport.setCp3new(cp3new);
				
				// C-P4 户数迁徙 C-P4（每日）=（C-P1）*（P1-P2）*（P2-P3）*（P3-P4）
				QianxilvNew qianxilvnew1Before3 = tMisMigrationRateReportDao.getSumQ1QianxilvNewByCycleDatetime(tmpMoveCycleBefore3.getDatetimestart(), tmpMoveCycleBefore3.getDatetimeend());
				BigDecimal cp1newBefore3 = calculate(qianxilvnew1Before3.getQ1(), qianxilvnew1Before3.getPayoffq1(), qianxilvnew1Before3.getOrderduedate(), 4);
				QianxilvNew qianxilvnew2Before2 = tMisMigrationRateReportDao.getSumQ2QianxilvNewByCycleDatetime(tmpMoveCycleBefore2.getDatetimestart(), tmpMoveCycleBefore2.getDatetimeend());
				BigDecimal p1p2newBefore2 = calculate(qianxilvnew2Before2.getQ2(), qianxilvnew2Before2.getPayoffq2(), qianxilvnew2Before2.getQ2(), 4);
				QianxilvNew qianxilvnew3Before1 = tMisMigrationRateReportDao.getSumQ3QianxilvNewByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal p2p3newBefore1 = calculate(qianxilvnew3Before1.getQ3(), qianxilvnew3Before1.getPayoffq3(), qianxilvnew3Before1.getQ3(), 4);
				QianxilvNew qianxilvnew4 = tMisMigrationRateReportDao.getSumQ4QianxilvNewByCycleDatetime(tmpMoveCycle.getDatetimestart(), Yesterday);
				BigDecimal p3p4new = calculate(qianxilvnew4.getQ4(), qianxilvnew4.getPayoffq4(), qianxilvnew4.getQ4(), 4);
				
				BigDecimal cp4new = cp1newBefore3.multiply(p1p2newBefore2).multiply(p2p3newBefore1).multiply(p3p4new);
				migrationRateReport.setCp4new(cp4new);
				
				
				// C-P1  本金迁徙 C-P1（每日）= 期末余额/期初余额
				QianxilvCorpu qianxilvcorpu1 = tMisMigrationRateReportDao.getSumQ1QianxilvCorpuByYesterday(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend(),Yesterday);
				BigDecimal cp1corpu = calculate(qianxilvcorpu1.getQ1(), qianxilvcorpu1.getPayoffq1(), qianxilvcorpu1.getOrderduedate(), 4);
				migrationRateReport.setCp1corpus(cp1corpu);
				
				// C-P2  本金迁徙
				QianxilvCorpu qianxilvcorpu1Before1 = tMisMigrationRateReportDao.getSumQ1QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal cp1corpuBefore1 = calculate(qianxilvcorpu1Before1.getQ1(), qianxilvcorpu1Before1.getPayoffq1(), qianxilvcorpu1Before1.getOrderduedate(), 4);
				QianxilvCorpu qianxilvcorpu2 = tMisMigrationRateReportDao.getSumQ2QianxilvCorpuByCycleDatetime(tmpMoveCycle.getDatetimestart(), Yesterday);
				BigDecimal p1p2corpu = calculate(qianxilvcorpu2.getQ2(), qianxilvcorpu2.getPayoffq2(), qianxilvcorpu2.getQ2(), 4);
				
				BigDecimal cp2corpu = cp1corpuBefore1.multiply(p1p2corpu);
				migrationRateReport.setCp2corpus(cp2corpu);
				
				// C-P3  本金迁徙
				QianxilvCorpu qianxilvcorpu1Before2 = tMisMigrationRateReportDao.getSumQ1QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore2.getDatetimestart(), tmpMoveCycleBefore2.getDatetimeend());
				BigDecimal cp1corpuBefore2 = calculate(qianxilvcorpu1Before2.getQ1(), qianxilvcorpu1Before2.getPayoffq1(), qianxilvcorpu1Before2.getOrderduedate(), 4);
				QianxilvCorpu qianxilvcorpu2Before1 = tMisMigrationRateReportDao.getSumQ2QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal p1p2corpuBefore1 = calculate(qianxilvcorpu2Before1.getQ2(), qianxilvcorpu2Before1.getPayoffq2(), qianxilvcorpu2Before1.getQ2(), 4);
				QianxilvCorpu qianxilvcorpu3 = tMisMigrationRateReportDao.getSumQ3QianxilvCorpuByCycleDatetime(tmpMoveCycle.getDatetimestart(), Yesterday);
				BigDecimal p2p3corpus = calculate(qianxilvcorpu3.getQ3(), qianxilvcorpu3.getPayoffq3(), qianxilvcorpu3.getQ3(), 4);
				
				BigDecimal cp3corpu = cp1corpuBefore2.multiply(p1p2corpuBefore1).multiply(p2p3corpus);
				migrationRateReport.setCp3corpus(cp3corpu);
				
				// C-P4  本金迁徙
				QianxilvCorpu qianxilvcorpu1Before3 = tMisMigrationRateReportDao.getSumQ1QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore3.getDatetimestart(), tmpMoveCycleBefore3.getDatetimeend());
				BigDecimal cp1corpuBefore3 = calculate(qianxilvcorpu1Before3.getQ1(), qianxilvcorpu1Before3.getPayoffq1(), qianxilvcorpu1Before3.getOrderduedate(), 4);
				
				QianxilvCorpu qianxilvcorpu2Before2 = tMisMigrationRateReportDao.getSumQ2QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore2.getDatetimestart(), tmpMoveCycleBefore2.getDatetimeend());
				BigDecimal p1p2corpuBefore2 = calculate(qianxilvcorpu2Before2.getQ2(), qianxilvcorpu2Before2.getPayoffq2(), qianxilvcorpu2Before2.getQ2(), 4);
				
				QianxilvCorpu qianxilvcorpu3Before1 = tMisMigrationRateReportDao.getSumQ3QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal p2p3corpuBefore1 = calculate(qianxilvcorpu3Before1.getQ3(), qianxilvcorpu3Before1.getPayoffq3(), qianxilvcorpu3Before1.getQ3(), 4);
				
				QianxilvCorpu qianxilvcorpu4 = tMisMigrationRateReportDao.getSumQ4QianxilvCorpuByCycleDatetime(tmpMoveCycle.getDatetimestart(), Yesterday);
				BigDecimal p3p4corpu = calculate(qianxilvcorpu4.getQ4(), qianxilvcorpu4.getPayoffq4(), qianxilvcorpu4.getQ4(), 4);
				
				BigDecimal cp4corpu = cp1corpuBefore3.multiply(p1p2corpuBefore2).multiply(p2p3corpuBefore1).multiply(p3p4corpu);
				migrationRateReport.setCp4corpus(cp4corpu);
				
				tMisMigrationRateReportDao.insert(migrationRateReport);
			}
			
		} catch (Exception e) {
			logger.warn("迁徙率计算insertDB失败"+ new Date());
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		}
		
	}
	
	public List<TMisMigrationRateReport> findMigrateChartList(TMisMigrationRateReport tMisMigrationRateReport){
		
		return tMisMigrationRateReportDao.findMigrateChartList(tMisMigrationRateReport);
	}
	
	public List<TMisMigrationRateReport> newfindMigrateChartList(TMisMigrationRateReport tMisMigrationRateReport){
		
		return tMisMigrationRateReportDao.newfindMigrateChartList(tMisMigrationRateReport);
	}
	
}