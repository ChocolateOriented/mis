/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.SMisDunningTaskMonthReportDao;
import com.mo9.risk.modules.dunning.entity.SMisDunningTaskMonthReport;
import com.mo9.risk.util.CsvUtil;
import com.mo9.risk.util.MailSender;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
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
		String month = DateUtils.formatDate(new Date(), "yyyyMM");
		query.setMonths(month);
		List<SMisDunningTaskMonthReport> data = super.findList(query);

		if (data == null || data.size()==0){//无数据
			logger.info("月报表数据为空");
			mailSender.setContent("数据未更新");

		}else {
			//生成cvs
			String fileName = "performanceMonthReport" + yesterday + ".csv";
			//表头200每条数据约为120字节
			ByteArrayOutputStream os = new ByteArrayOutputStream(200+120*data.size());
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
}