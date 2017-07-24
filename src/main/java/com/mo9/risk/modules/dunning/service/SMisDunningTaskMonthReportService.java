/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.SMisDunningTaskMonthReportDao;
import com.mo9.risk.modules.dunning.entity.SMisDunningTaskMonthReport;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.util.CsvUtil;
import com.mo9.risk.util.MailSender;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 催收月绩效Service
 * @author 徐盛
 * @version 2016-11-30
 */
@Service
@Lazy(false)
@Transactional(readOnly = true)
public class SMisDunningTaskMonthReportService extends CrudService<SMisDunningTaskMonthReportDao, SMisDunningTaskMonthReport> {
	@Autowired
	private SMisDunningTaskMonthReportDao sMisDunMRDao;
	
	public SMisDunningTaskMonthReport get(String id) {
		return super.get(id);
	}
	
	public List<SMisDunningTaskMonthReport> findList(SMisDunningTaskMonthReport sMisDunningTaskMonthReport) {
		return super.findList(sMisDunningTaskMonthReport);
	}
	
	public Page<SMisDunningTaskMonthReport> findPage(Page<SMisDunningTaskMonthReport> page, SMisDunningTaskMonthReport sMisDunningTaskMonthReport) {
		return super.findPage(page, sMisDunningTaskMonthReport);
	}
	
	@Transactional(readOnly = false)
	public void save(SMisDunningTaskMonthReport sMisDunningTaskMonthReport) {
		super.save(sMisDunningTaskMonthReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(SMisDunningTaskMonthReport sMisDunningTaskMonthReport) {
		super.delete(sMisDunningTaskMonthReport);
	}

	/**
	 * @return void
	 * @Description 自动邮件
	 */
	@Scheduled(cron = "0 0 8 * * ?")
	public void autoSendMail() {
		String receiver = DictUtils.getDictValue("e_mail", "month_report_receiver", "");
		if (StringUtils.isBlank(receiver)){
			logger.warn("自动发送月报失败, 未配置收件人邮箱");
			return;
		}
		logger.info("自动发送月报邮件至"+receiver);

		//获取T-1日期
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day - 1);
		String yesterday = DateUtils.formatDate(calendar.getTime());

		//创建邮件
		MailSender mailSender = new MailSender(receiver);
		mailSender.setSubject("绩效月报-" + yesterday);

		//获取截止到当前的月报
		SMisDunningTaskMonthReport query = new SMisDunningTaskMonthReport();
		query.setCreateDate(new Date());
		List<SMisDunningTaskMonthReport> data = super.findList(query);

		if (data == null || data.size()==0){//无数据
			logger.info("月报表数据为空");
			mailSender.setContent("数据未更新");

		}else {
			//生成cvs
			String fileName = "performanceMonthReport" + yesterday + ".csv";
			//表头230每条数据约为135字节
			ByteArrayOutputStream os = new ByteArrayOutputStream(230+135*data.size());
			try {
				CsvUtil.export(os, SMisDunningTaskMonthReport.class, data);
			} catch (IOException e) {
				logger.warn("月报表自动邮件创建csv失败", e);
				return;
			}

			//添加附件cvs
			DataSource dataSource;
			try {
				dataSource = new ByteArrayDataSource(new ByteArrayInputStream(os.toByteArray()), "text/csv");
			} catch (IOException e) {
				logger.warn("月报表自动邮件添加附件失败", e);
				return;
			}
			mailSender.addAttachSource(dataSource, fileName);
		}

		//发送
		try {
			mailSender.sendMail();
			logger.debug("月报邮件发送成功");
		} catch (MessagingException e) {
			logger.warn("月报表自动邮件发送失败", e);
		}
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
		System.out.println(getMonthLastDayDate());
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
	public void autoMigrationRateDB() {
		
		try {
			int maxcycle = sMisDunMRDao.getMaxcycle();
			Date datetimestart = null;
			Date datetimeend = null;
			switch (getTomorrowDaysOfMonth()) {
				case 30:
					switch (getTomorrow()) {
					case 1:
						datetimestart = getDate(1);
						datetimeend = getDate(15);
						sMisDunMRDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
						migrationRateGetData();
						
						return;
					case 16:
						datetimestart = getDate(1);
						datetimeend = getMonthLastDayDate();
						sMisDunMRDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
						migrationRateGetData();
						return;
					default:
						migrationRateGetData();
						return;
					}
					
				case 31:
					switch (getTomorrow()) {
					case 1:
						datetimestart = getDate(1);
						datetimeend = getDate(16);
						sMisDunMRDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
						migrationRateGetData();
						return;
					case 17:
						datetimestart = getDate(1);
						datetimeend = getMonthLastDayDate();
						sMisDunMRDao.insertTmpMoveCycle(maxcycle, datetimestart, datetimeend);
						migrationRateGetData();
						return;
					default:
						migrationRateGetData();
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
			sMisDunMRDao.householdsUpdateHaveBeenCollectDealcode();
			sMisDunMRDao.householdsInsertOverOneDay(40,Yesterday,today);
			sMisDunMRDao.householdsInsertStatisticalData(Yesterday,today);
			sMisDunMRDao.householdsUpdatePayoffQ1();
			sMisDunMRDao.householdsUpdatePayoffQ2();
			sMisDunMRDao.householdsUpdatePayoffQ3();
			sMisDunMRDao.householdsUpdatePayoffQ4();
			sMisDunMRDao.householdsUpdateQ2();
			sMisDunMRDao.householdsUpdateQ3();
			sMisDunMRDao.householdsUpdateQ4();
			sMisDunMRDao.householdsUpdateOverOneDay();
			
			//迁徙率关于本金 
			sMisDunMRDao.principalInsertStatisticalData(Yesterday,today);
			sMisDunMRDao.principalUpdatePayoffQ1();
			sMisDunMRDao.principalUpdatePayoffQ2();
			sMisDunMRDao.principalUpdatePayoffQ3();
			sMisDunMRDao.principalUpdatePayoffQ4();
			sMisDunMRDao.principalUpdateQ2();
			sMisDunMRDao.principalUpdateQ3();
			sMisDunMRDao.principalUpdateQ4();
			sMisDunMRDao.principalUpdateOverOneDay();
			
		} catch (Exception e) {
			logger.warn("迁徙率数据表更新任务失败"+ new Date());
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		}
	}

	
}