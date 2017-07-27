/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
		BigDecimal cp1new = new BigDecimal(12).subtract(new BigDecimal(9)).divide(new BigDecimal(3),2,BigDecimal.ROUND_HALF_UP); 
		System.out.println(cp1new);
		
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
//	@Scheduled(cron = "0 0 7 * * ?")
	@Transactional(readOnly = false)
	public void automigrationRateGetDataDB() {
		
		try {
			int maxcycle = tMisMigrationRateReportDao.getMaxcycle();
			Date datetimestart = null;
			Date datetimeend = null;
			Date Yesterday = getDate(-1);
			switch (getTomorrowDaysOfMonth()) {
				case 30:
					switch (getTomorrow()) {
					case 1:
						datetimestart = getDate(1);
						datetimeend = getDate(15);
						tMisMigrationRateReportDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
						migrationRateGetData();
						insertMigrationRateReportDB(Yesterday);
						return;
					case 16:
						datetimestart = getDate(1);
						datetimeend = getMonthLastDayDate();
						tMisMigrationRateReportDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
						migrationRateGetData();
						insertMigrationRateReportDB(Yesterday);
						return;
					default:
						migrationRateGetData();
						insertMigrationRateReportDB(Yesterday);
						return;
					}
					
				case 31:
					switch (getTomorrow()) {
					case 1:
						datetimestart = getDate(1);
						datetimeend = getDate(16);
						tMisMigrationRateReportDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
						migrationRateGetData();
						insertMigrationRateReportDB(Yesterday);
						return;
					case 17:
						datetimestart = getDate(1);
						datetimeend = getMonthLastDayDate();
						tMisMigrationRateReportDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
						migrationRateGetData();
						insertMigrationRateReportDB(Yesterday);
						return;
					default:
						migrationRateGetData();
						insertMigrationRateReportDB(Yesterday);
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
	@Transactional(readOnly = false)
	public void migrationRateGetData() {
		try {
			Date today = getDate(0);
			Date Yesterday = getDate(-1);
			
			//执行一系列的迁徙率数据获取
			tMisMigrationRateReportDao.householdsUpdateHaveBeenCollectDealcode();
			tMisMigrationRateReportDao.householdsInsertOverOneDay(40,Yesterday,today);
			tMisMigrationRateReportDao.householdsInsertStatisticalData();
			tMisMigrationRateReportDao.householdsUpdateOverOneDay();
			tMisMigrationRateReportDao.householdsUpdatePayoffQ1();
			tMisMigrationRateReportDao.householdsUpdatePayoffQ2();
			tMisMigrationRateReportDao.householdsUpdatePayoffQ3();
			tMisMigrationRateReportDao.householdsUpdatePayoffQ4();
			tMisMigrationRateReportDao.householdsUpdateQ2();
			tMisMigrationRateReportDao.householdsUpdateQ3();
			tMisMigrationRateReportDao.householdsUpdateQ4();
			
			//迁徙率关于本金 
			tMisMigrationRateReportDao.principalInsertStatisticalData();
			tMisMigrationRateReportDao.principalUpdateOverOneDay();
			tMisMigrationRateReportDao.principalUpdatePayoffQ1();
			tMisMigrationRateReportDao.principalUpdatePayoffQ2();
			tMisMigrationRateReportDao.principalUpdatePayoffQ3();
			tMisMigrationRateReportDao.principalUpdatePayoffQ4();
			tMisMigrationRateReportDao.principalUpdateQ2();
			tMisMigrationRateReportDao.principalUpdateQ3();
			tMisMigrationRateReportDao.principalUpdateQ4();
			
		} catch (Exception e) {
			logger.warn("迁徙率数据表更新任务失败"+ new Date());
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		}
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
	
    
    
	/**
     * 迁徙率计算insertDB
     */
//	@Scheduled(cron = "0 0 7 * * ?")
	@Transactional(readOnly = false)
	public void insertMigrationRateReportDB(Date Yesterday) {
		try {
			
			TMisMigrationRateReport migrationRateReport = new TMisMigrationRateReport();
//			Date Yesterday = getDate(-1);
			TmpMoveCycle tmpMoveCycle = tMisMigrationRateReportDao.getTmpMoveCycleByDatetime(Yesterday);
//			new BigDecimal(12).subtract(new BigDecimal(9)).divide(new BigDecimal(3),2,BigDecimal.ROUND_HALF_UP); 
			
			if( null != tmpMoveCycle ){
				
				migrationRateReport.setCycle(String.valueOf(tmpMoveCycle.getCycle()));
				migrationRateReport.setDatetime(Yesterday);
				
				TmpMoveCycle tmpMoveCycleBefore1 = tMisMigrationRateReportDao.getTmpMoveCycleByCycle(tmpMoveCycle.getCycle() - 1);  // 本周期C-P1的前一个周期
				TmpMoveCycle tmpMoveCycleBefore2 = tMisMigrationRateReportDao.getTmpMoveCycleByCycle(tmpMoveCycle.getCycle() - 2);  // 本周期C-P1的前二个周期
				TmpMoveCycle tmpMoveCycleBefore3 = tMisMigrationRateReportDao.getTmpMoveCycleByCycle(tmpMoveCycle.getCycle() - 3);  // 本周期C-P1的前三个周期
				
				// C-P1  户数迁徙  C-P1（每日）= 期末余额/期初余额
				QianxilvNew qianxilvnew1 = tMisMigrationRateReportDao.getSumQ1QianxilvNewByCycleDatetime(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend());
				BigDecimal cp1new = calculate(qianxilvnew1.getQ1(), qianxilvnew1.getPayoffq1(), qianxilvnew1.getOrderduedate(), 2);
				
				migrationRateReport.setCp1new(cp1new);
				
				// C-P2  户数迁徙   C-P2（每日）=（C-P1）*（P1-P2）
				QianxilvNew qianxilvnew1Before1 = tMisMigrationRateReportDao.getSumQ1QianxilvNewByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal cp1newBefore1 = calculate(qianxilvnew1Before1.getQ1(), qianxilvnew1Before1.getPayoffq1(), qianxilvnew1Before1.getOrderduedate(), 2);
				QianxilvNew qianxilvnew2 = tMisMigrationRateReportDao.getSumQ2QianxilvNewByCycleDatetime(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend());
				BigDecimal p1p2new = calculate(qianxilvnew2.getQ2(), qianxilvnew2.getPayoffq2(), qianxilvnew2.getQ2(), 2);
				
				BigDecimal cp2new = cp1newBefore1.multiply(p1p2new);
				migrationRateReport.setCp2new(cp2new);
				
				// C-P3  户数迁徙 C-P3（每日）=（C-P1）*（P1-P2）*（P2-P3）
				QianxilvNew qianxilvnew1Before2 = tMisMigrationRateReportDao.getSumQ1QianxilvNewByCycleDatetime(tmpMoveCycleBefore2.getDatetimestart(), tmpMoveCycleBefore2.getDatetimeend());
				BigDecimal cp1newBefore2 = calculate(qianxilvnew1Before2.getQ1(), qianxilvnew1Before2.getPayoffq1(), qianxilvnew1Before2.getOrderduedate(), 2);
				QianxilvNew qianxilvnew2Before1 = tMisMigrationRateReportDao.getSumQ2QianxilvNewByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal p1p2newBefore1 = calculate(qianxilvnew2Before1.getQ2(), qianxilvnew2Before1.getPayoffq2(), qianxilvnew2Before1.getQ2(), 2);
				QianxilvNew qianxilvnew3 = tMisMigrationRateReportDao.getSumQ3QianxilvNewByCycleDatetime(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend());
				BigDecimal p2p3new = calculate(qianxilvnew3.getQ3(), qianxilvnew3.getPayoffq3(), qianxilvnew3.getQ3(), 2);
				
				BigDecimal cp3new = cp1newBefore2.multiply(p1p2newBefore1).multiply(p2p3new);
				migrationRateReport.setCp3new(cp3new);
				
				// C-P4 户数迁徙 C-P4（每日）=（C-P1）*（P1-P2）*（P2-P3）*（P3-P4）
				QianxilvNew qianxilvnew1Before3 = tMisMigrationRateReportDao.getSumQ1QianxilvNewByCycleDatetime(tmpMoveCycleBefore3.getDatetimestart(), tmpMoveCycleBefore3.getDatetimeend());
				BigDecimal cp1newBefore3 = calculate(qianxilvnew1Before3.getQ1(), qianxilvnew1Before3.getPayoffq1(), qianxilvnew1Before3.getOrderduedate(), 2);
				QianxilvNew qianxilvnew2Before2 = tMisMigrationRateReportDao.getSumQ2QianxilvNewByCycleDatetime(tmpMoveCycleBefore2.getDatetimestart(), tmpMoveCycleBefore2.getDatetimeend());
				BigDecimal p1p2newBefore2 = calculate(qianxilvnew2Before2.getQ2(), qianxilvnew2Before2.getPayoffq2(), qianxilvnew2Before2.getQ2(), 2);
				QianxilvNew qianxilvnew3Before1 = tMisMigrationRateReportDao.getSumQ3QianxilvNewByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal p2p3newBefore1 = calculate(qianxilvnew3Before1.getQ3(), qianxilvnew3Before1.getPayoffq3(), qianxilvnew3Before1.getQ3(), 2);
				QianxilvNew qianxilvnew4 = tMisMigrationRateReportDao.getSumQ4QianxilvNewByCycleDatetime(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend());
				BigDecimal p3p4new = calculate(qianxilvnew4.getQ4(), qianxilvnew4.getPayoffq4(), qianxilvnew4.getQ4(), 2);
				
				BigDecimal cp4new = cp1newBefore3.multiply(p1p2newBefore2).multiply(p2p3newBefore1).multiply(p3p4new);
				migrationRateReport.setCp4new(cp4new);
				
				
				// C-P1  本金迁徙 C-P1（每日）= 期末余额/期初余额
				QianxilvCorpu qianxilvcorpu1 = tMisMigrationRateReportDao.getSumQ1QianxilvCorpuByCycleDatetime(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend());
				BigDecimal cp1corpu = calculate(qianxilvcorpu1.getQ1(), qianxilvcorpu1.getPayoffq1(), qianxilvcorpu1.getOrderduedate(), 2);
				migrationRateReport.setCp1corpus(cp1corpu);
				
				// C-P2  本金迁徙
				QianxilvCorpu qianxilvcorpu1Before1 = tMisMigrationRateReportDao.getSumQ1QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal cp1corpuBefore1 = calculate(qianxilvcorpu1Before1.getQ1(), qianxilvcorpu1Before1.getPayoffq1(), qianxilvcorpu1Before1.getOrderduedate(), 2);
				QianxilvCorpu qianxilvcorpu2 = tMisMigrationRateReportDao.getSumQ2QianxilvCorpuByCycleDatetime(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend());
				BigDecimal p1p2corpu = calculate(qianxilvcorpu2.getQ2(), qianxilvcorpu2.getPayoffq2(), qianxilvcorpu2.getQ2(), 2);
				
				BigDecimal cp2corpu = cp1corpuBefore1.multiply(p1p2corpu);
				migrationRateReport.setCp2corpus(cp2corpu);
				
				// C-P3  本金迁徙
				QianxilvCorpu qianxilvcorpu1Before2 = tMisMigrationRateReportDao.getSumQ1QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore2.getDatetimestart(), tmpMoveCycleBefore2.getDatetimeend());
				BigDecimal cp1corpuBefore2 = calculate(qianxilvcorpu1Before2.getQ1(), qianxilvcorpu1Before2.getPayoffq1(), qianxilvcorpu1Before2.getOrderduedate(), 2);
				QianxilvCorpu qianxilvcorpu2Before1 = tMisMigrationRateReportDao.getSumQ2QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal p1p2corpuBefore1 = calculate(qianxilvcorpu2Before1.getQ2(), qianxilvcorpu2Before1.getPayoffq2(), qianxilvcorpu2Before1.getQ2(), 2);
				QianxilvCorpu qianxilvcorpu3 = tMisMigrationRateReportDao.getSumQ3QianxilvCorpuByCycleDatetime(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend());
				BigDecimal p2p3corpus = calculate(qianxilvcorpu3.getQ3(), qianxilvcorpu3.getPayoffq3(), qianxilvcorpu3.getQ3(), 2);
				
				BigDecimal cp3corpu = cp1corpuBefore2.multiply(p1p2corpuBefore1).multiply(p2p3corpus);
				migrationRateReport.setCp3corpus(cp3corpu);
				
				// C-P4  本金迁徙
				QianxilvCorpu qianxilvcorpu1Before3 = tMisMigrationRateReportDao.getSumQ1QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore3.getDatetimestart(), tmpMoveCycleBefore3.getDatetimeend());
				BigDecimal cp1corpuBefore3 = calculate(qianxilvcorpu1Before3.getQ1(), qianxilvcorpu1Before3.getPayoffq1(), qianxilvcorpu1Before3.getOrderduedate(), 2);
				
				QianxilvCorpu qianxilvcorpu2Before2 = tMisMigrationRateReportDao.getSumQ2QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore2.getDatetimestart(), tmpMoveCycleBefore2.getDatetimeend());
				BigDecimal p1p2corpuBefore2 = calculate(qianxilvcorpu2Before2.getQ2(), qianxilvcorpu2Before2.getPayoffq2(), qianxilvcorpu2Before2.getQ2(), 2);
				
				QianxilvCorpu qianxilvcorpu3Before1 = tMisMigrationRateReportDao.getSumQ3QianxilvCorpuByCycleDatetime(tmpMoveCycleBefore1.getDatetimestart(), tmpMoveCycleBefore1.getDatetimeend());
				BigDecimal p2p3corpuBefore1 = calculate(qianxilvcorpu3Before1.getQ3(), qianxilvcorpu3Before1.getPayoffq3(), qianxilvcorpu3Before1.getQ3(), 2);
				
				QianxilvCorpu qianxilvcorpu4 = tMisMigrationRateReportDao.getSumQ4QianxilvCorpuByCycleDatetime(tmpMoveCycle.getDatetimestart(), tmpMoveCycle.getDatetimeend());
				BigDecimal p3p4corpu = calculate(qianxilvcorpu4.getQ4(), qianxilvcorpu4.getPayoffq4(), qianxilvcorpu4.getQ4(), 2);
				
				BigDecimal cp4corpu = cp1corpuBefore3.multiply(p1p2corpuBefore2).multiply(p2p3corpuBefore1).multiply(p3p4corpu);
				migrationRateReport.setCp4corpus(cp4corpu);
				
			}
			
			
		} catch (Exception e) {
			logger.warn("迁徙率计算insertDB失败"+ new Date());
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		}
		
	}
	
	
	
}