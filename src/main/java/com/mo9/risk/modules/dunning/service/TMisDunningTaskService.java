/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gamaxpay.commonutil.msf.BaseResponse;
import com.gamaxpay.commonutil.msf.ServiceAddress;
import com.mo9.risk.modules.dunning.dao.TMisDunnedHistoryDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TMisReliefamountHistoryDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.DunningOuterFile;
import com.mo9.risk.modules.dunning.entity.DunningOuterFileLog;
import com.mo9.risk.modules.dunning.entity.DunningPeriod;
import com.mo9.risk.modules.dunning.entity.OrderHistory;
import com.mo9.risk.modules.dunning.entity.PartialOrder;
import com.mo9.risk.modules.dunning.entity.PerformanceDayReport;
import com.mo9.risk.modules.dunning.entity.PerformanceMonthReport;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.ContactsType;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.ContantType;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.SmsTemp;
import com.mo9.risk.modules.dunning.entity.TMisDunnedHistory;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.util.MsfClient;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 催收任务Service
 * @author 徐盛
 * @version 2016-07-12
 */
@Service
@Transactional(readOnly = true)
@Lazy(false)
public class TMisDunningTaskService extends CrudService<TMisDunningTaskDao, TMisDunningTask> {

	public static final Integer DUNNING_FINANCIAL_PERMISSIONS = 1000;    //  财务权限
	public static final Integer DUNNING_ALL_PERMISSIONS = 111;           //  催收总监
	public static final Integer DUNNING_INNER_PERMISSIONS = 101;         //  内部催收主管
	public static final Integer DUNNING_OUTER_PERMISSIONS =  11;         //  委外催收主管
	public static final Integer DUNNING_COMMISSIONER_PERMISSIONS = 1;    //  催收专员
	
	private static Logger logger = Logger.getLogger(TMisDunningTaskService.class);

	@Autowired
	private TMisContantRecordService tMisContantRecordService;
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	@Autowired
	private TMisDunningPeopleDao tMisDunningPeopleDao;
	@Autowired
	private TMisDunnedHistoryDao tMisDunnedHistoryDao;
	
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoDao;
	
	@Autowired
	private TMisReliefamountHistoryDao tMisReliefamountHistoryDao;

	public TMisDunningTask get(String id) {
		return super.get(id);
	}

	public List<TMisDunningTask> findList(TMisDunningTask tMisDunningTask) {
		return super.findList(tMisDunningTask);
	}

	public Page<TMisDunningTask> findPage(Page<TMisDunningTask> page, TMisDunningTask tMisDunningTask) {
		return super.findPage(page, tMisDunningTask);
	}

	private Page<TMisDunningTask> findPayoffDunningTask(Page page) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", page);
		page.setList(this.tMisDunningTaskDao.findPayoffDunningTask(params));
		return page;
	}
	
	private Page<TRiskBuyerPersonalInfo> getBuyerListByRepaymentTime(Page page) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", page);
		page.setList(this.personalInfoDao.getBuyerListByRepaymentTime(params));
		return page;
	}

	private Page<TMisDunningTask> findExpiredTask(Page page) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", page);
		params.put("STATUS_DUNNING", TMisDunningTask.STATUS_DUNNING);
		page.setList(this.tMisDunningTaskDao.findExpiredTask(params));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(TMisDunningTask tMisDunningTask) {
		super.save(tMisDunningTask);
	}

	@Transactional(readOnly = false)
	public void delete(TMisDunningTask tMisDunningTask) {
		super.delete(tMisDunningTask);
	}

	@Transactional(readOnly = false)
	public void generateTast() {
		tMisDunningTaskDao.generateTast();
	}

	public TMisDunningOrder getTMisDunningOrder(String dealcode){
		return tMisDunningTaskDao.findOrderByDealcode(dealcode);
	}
	
	@Transactional(readOnly = false)
	public boolean savefreeCreditAmount(String dealcode,TMisDunningTask task,String amount) {
		try {
			/**
			 *  根据订单号保存订单减免金额
			 */
			TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			order.setReliefamount(new BigDecimal(amount));
			tMisDunningTaskDao.updateOrder(order);
			tMisDunningTaskDao.updateOrderPartial(new BigDecimal(amount), order.getId());
			/**
			 *  保存此订单当前任务的减免金额
			 */
			task.setReliefamount((int)(Double.parseDouble(amount) * 100));
			tMisDunningTaskDao.update(task);
			/**
			 *  保存减免记录
			 */
			TMisReliefamountHistory reliefamountHistory = new TMisReliefamountHistory();
			reliefamountHistory.preInsert();
			reliefamountHistory.setDealcode(dealcode);
			reliefamountHistory.setReliefamount(amount);
			tMisReliefamountHistoryDao.insert(reliefamountHistory);
			return true;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 系统自动发送催收短信开始，应还日期为今天
	 */
//	@Scheduled(cron = "0 0 9 * * ?")  //每天上午八点执行
	@Transactional
	public void autoSmsSend() {
		logger.info("系统自动发送催收短信开始，应还日期为今天"+new Date());
		
		this.sendSms();
		
		logger.info("系统自动发送催收短信结束，应还日期为今天"+new Date());
	}
	
	
	/**
	 * 自动分配催收任务
	 * 业务流程：
	 * 1. 检索已经还清的催收任务（关联的贷款订单已还，还款时间在任务的起始时间和截至时间或结束时间内 且任务状态为还款中）
	 * 将符合条件的催款任务设置为结束
	 * 2. 检索已经过期的催款任务，设置状态为到期
	 * 3.按催款周期分类分配 没有对应正在催款的订单
	 * 算法： 按逾期周期把逾期订单分堆（1-14，15-35等）
	 * 每堆的订单，先取按分配后人员任务数的平均数
	 * 然后平均分配，如在单次当前任务数在平均数以上的人员不分配，在平均一下的再分配
	 */
	@Transactional
	@Scheduled(cron = "0 0 2 * * ?")  //每天上午两点执行
	public void autoAssign() {
		/**
		 *  STEP1  检索已经还清的催收任务（关联的贷款订单已还，还款时间在任务的起始时间和截至时间或结束时间内 且任务状态为还款中）
		 * 				将符合条件的催款任务设置为结束
		 */
		this.updatePayoffTask();
		/**
		 *  STEP2
		 */
		this.updateExpiredTask();
		/**
		 *  查找现在内部人员所有催收账期
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("PEOPLE_TYPE_INNER", TMisDunningPeople.PEOPLE_TYPE_INNER);
		List<DunningPeriod> periods = this.tMisDunningTaskDao.findDunningPeriod(params);
		logger.info("系统共有" + periods.size() + "个催款账期");
		for (DunningPeriod period : periods) {
			logger.info("开始分配" + period.begin + "天到" + period.end + "天催收任务");
			this.assignByPeriod(period);
		}
	}

	/**
	 * 手动分配催款任务
	 * 业务流程：
	 * 1.检查任务是否能分配给当前用户
	 * 1）订单是否已经正在还款期或逾期
	 * 2）订单的逾期周期是否符合该用户的催款周期
	 * 2. 分配
	 * 1.如订单没有对应的催款任务，则生成新的催款任务
	 * 2.如订单有对应的催款任务，则先结束之前的催款任务，并生成新的。
	 * 注意，需要检查是否是同期转移
	 *
	 * @param dealcode
	 * @param peopleId
	 */
	@Transactional(readOnly = false)
	public boolean assign(String dealcode, String peopleId) {

		TMisDunningPeople people = this.tMisDunningPeopleDao.get(peopleId);
		if(people == null)
		{
			 logger.warn("催收人员不存在："+peopleId);
			 return false;
		}

		TMisDunningOrder order = this.tMisDunningTaskDao.findOrderByDealcode(dealcode);
		if(order == null)
		{
			logger.warn("分配订单不存在:"+dealcode);
			return false;
		}
		if(!"payment".equalsIgnoreCase(order.status))
		{
			logger.warn("催款订单状态错误，状态："+order.status);
			return false;
		}
	 
		Date now = new Date();
//		long overduedays = (toDate(now).getTime()-toDate(order.repaymentDate).getTime())/(24*60*60*1000);
		long overduedays = TMisDunningTaskService.GetOverdueDay(order.repaymentDate);
		logger.info(MessageFormat.format("订单逾期{0}天，催款人<{1}>负责催收{2}天到{3}天",overduedays,people.getName(),people.getBegin(),people.getEnd()));
//		if(!(overduedays>=people.getBegin() && overduedays<=people.getEnd()))
//		{
//			logger.warn(MessageFormat.format("订单的逾期周期不符合该用户的催款周期，订单逾期{0}天，催款人<{1}>负责催收{2}天到{3}天",overduedays,people.getName(),people.getBegin(),people.getEnd()));
//			return false;
//		}

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("STATUS_DUNNING",TMisDunningTask.STATUS_DUNNING);
		params.put("DEALCODE",dealcode);
		String beforeTaskId = null;
		String beforePeopleId = null;
		TMisDunningTask task = this.tMisDunningTaskDao.findDunningTaskByDealcode(params);
		
//		if(TMisDunningTask.STATUS_TRANSFER.equals(task.getDunningtaskstatus())){
//			logger.warn(MessageFormat.format("催款人<{0}>的订单任务状态为同期转移",people.getName()));
//			return false;
//		}
		
		if(task != null)
		{
			beforeTaskId = task.getId();
			beforePeopleId = task.getDunningpeopleid();
			String status = TMisDunningTask.STATUS_END;
			if(task.getDunningpeopleid().equals(peopleId))
			{
				logger.warn("该任务已经被分配给该用户");
				return false;
			}
			if(task.getDunningperiodbegin().equals(people.getBegin())
					&& task.getDunningperiodend().equals(people.getEnd()))
			{
				status = TMisDunningTask.STATUS_TRANSFER;
				logger.info(MessageFormat.format("催款任务:{0} 同期转移给催款人<{1}>",task.getId(),people.getName()));

			}
			logger.info(MessageFormat.format("催款任务:{0} 重新分配给催款人<{1}>,账期为{2}天到{3}天",task.getId(),people.getName(),people.getBegin(),people.getEnd()));
	        task.setDunningtaskstatus(status);
			task.setEnd(now);
			task.setUpdateDate(now);
			TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
			if(personalInfo == null){
				logger.info(MessageFormat.format("手动分配personalInfo为空，Dealcode:===>", dealcode));
				return false;
			}
			task.setDunningAmounOnEnd((int)Double.valueOf(personalInfo.getCreditAmount()).doubleValue());
			task.preUpdate();
//			task.setDunningpeopleid(peopleId)
			this.tMisDunningTaskDao.update(task);

		}
		else
		{
			logger.info(MessageFormat.format("订单:{0} 分配给催款人<{1}>,账期为{2}天到{3}天",order.dealcode,people.getName(),people.getBegin(),people.getEnd()));
		}
		DunningPeriod period = new DunningPeriod();
		period.begin = people.getBegin();
		period.end = people.getEnd();
		try{
			this.createDunningTask(people, order, period,beforeTaskId,beforePeopleId);
			return true;
		} catch (Exception e) {
			logger.error(MessageFormat.format("分配订单：{0} 给催收人：{1}发生错误", order.dealcode, people.getName()) ,e);
		}
		return false;
	}

	/**
	 * 催收还款
	 * 业务流程：
	 * 在订单已还的情况，会调用该方法，标记催收任务已还
	 * 1) 检查对应订单的状态是否已经还清
	 * 2）新建催回记录
	 * 3）将订单状态改为结束
	 * 注意：暂时业务中没有部分还款的情况，我们暂时忽略该场景
	 *
	 * @param task
	 */

	@Transactional(readOnly = false)
	public boolean repayment(TMisDunningTask task) {

		task = this.tMisDunningTaskDao.get(task.getId());
		if(task == null)
		{
			logger.warn("任务不存在,id:"+task.getId());
			return false;
		}
		String dealcode = task.getDealcode();
		TMisDunningOrder order = this.tMisDunningTaskDao.findOrderByDealcode(dealcode);
		if(order == null)
		{
			logger.warn("还款订单不存在，订单号："+dealcode);
			return false;
		}
		if(!"payoff".equalsIgnoreCase(order.status))
		{
			logger.warn("订单状态错误，状态:"+order.status+" 订单号："+dealcode);
			return false;
		}
		Date now = new Date();
		//新建催回记录
		TMisDunnedHistory history = new TMisDunnedHistory();
		history.preInsert();
		history.setTaskid(task.getId());
		history.setAmount((order.amount.add(order.overdueAmount).subtract(order.reliefamount)).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
		history.setDunnedtime(now);
		history.setIspayoff(true);
//		history.setOverduedays((int)(toDate(now).getTime() - toDate(order.repaymentDate).getTime()) / (24 * 60 * 60 * 1000));
		history.setOverduedays((int)TMisDunningTaskService.GetOverdueDay(order.repaymentDate));
		history.setReliefamount(order.reliefamount.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
		this.tMisDunnedHistoryDao.insert(history);

		task.preUpdate();
		task.setDunningtaskstatus(TMisDunningTask.STATUS_FINISHED);
		task.setDunnedamount(history.getAmount());
		task.setIspayoff(true);
		task.setEnd(now);
		this.tMisDunningTaskDao.update(task);
		return true;
	}

	/**
	 *  定时自动扫描还款
	 */
	@Scheduled(cron = "0 0/15 * * * ?") //每十五分钟执行一次
	@Transactional(readOnly = false)
	public void autoRepayment()
	{
		String scheduledBut =  DictUtils.getDictValue("autoRepayment","Scheduled","");
		if(scheduledBut.equals("true")){
			logger.info(MessageFormat.format("自动扫描还款正在運行=========>{0}", new Date()));
			this.updatePartialTask();
			this.updatePayoffTask();
		}
	}
	/**
	 * 检索已经还清的催收任务（关联的贷款订单已还，还款时间在任务的起始时间和截至时间或结束时间内 且任务状态为还款中）
	 * 将符合条件的催款任务设置为结束
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void updatePayoffTask() {
		//分页处理，单页处理100条
		Page<TMisDunningTask> page = new Page<TMisDunningTask>(1, 100);
		int pageNo = 1; //页码
		do {
			page.setPageNo(pageNo);
			page = this.findPayoffDunningTask(page);
			logger.info(MessageFormat.format("检索已还清催款任务，第{0}页,共{1}条", page.getPageNo(), page.getList().size()));
			int i = 1;
			for (TMisDunningTask task : page.getList()) {
				logger.info(MessageFormat.format("正在處理第幾條===>{0}", i++ ) );
				boolean result = this.repayment(task);
				if (!result) {
					//如果处理还款不成功，日志记录
					logger.info("批量更新已还催款任务错误，任务号：" + task.getId());
				} else {
					logger.info("批量更新已还催款任务成功，任务号：" + task.getId());
				}

			}
			pageNo++; //翻页
			page.setList(null);
		}
		while (page != null && !page.isLastPage());

	}
	
	
	
	/**
	 * 自动发送催收短信，到期还款日为今日
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	private void sendSms() {
		//分页处理，单页处理100条
		Page<TRiskBuyerPersonalInfo> page = new Page<TRiskBuyerPersonalInfo>(1, 100);
		int pageNo = 1; //页码
		do {
			page.setPageNo(pageNo);
			page = this.getBuyerListByRepaymentTime(page);
			logger.info(MessageFormat.format("检索已还清催款任务，第{0}页,共{1}条", page.getPageNo(), page.getList().size()));
			for (TRiskBuyerPersonalInfo dealInfo : page.getList()) {
				Double amount = new Double(0);
				amount = Double.valueOf(dealInfo.getCreditAmount()).doubleValue() /100D;
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String repaymentTime = sdf.format(dealInfo.getRepaymentTime());
				
				String msg = MessageFormat.format("【mo9】江湖救急用户，您的借款即将到期，还款金额{0}元，最后还款日{1}，按时还款有助您提高借款额度。",amount,repaymentTime);
				
				try{
					/**
					 *  发送短信
					 */
					Map<String,String> params = new HashMap<String,String>();
					params.put("mobile", dealInfo.getMobile());
					params.put("message",msg);
					MsfClient.instance().requestFromServer(ServiceAddress.SNC_SMS, params, BaseResponse.class);
					logger.info("给用户:"+dealInfo.getMobile()+"发送短信成功，内容:"+msg);
					
					TMisContantRecord dunning = new TMisContantRecord();
					dunning.setTaskid(null);
					dunning.setDealcode(dealInfo.getDealcode());
					dunning.setOrderstatus(false);
					dunning.setOverduedays(Integer.valueOf(dealInfo.getOverdueDays()));
					dunning.setDunningtime(new Date());
					dunning.setContanttype(ContantType.sms);
					dunning.setContanttarget(dealInfo.getMobile());
					//短信内容
					dunning.setContent(msg);
					dunning.setSmstemp(SmsTemp.ST_0);
					dunning.setTelstatus(null);
					dunning.setContactstype(ContactsType.SELF);
					dunning.setDunningpeoplename("sys");
					dunning.setRepaymenttime(dealInfo.getRepaymentTime());
					dunning.setRemark(null);
					
					tMisContantRecordService.save(dunning);
				}catch (Exception e) {
					logger.info("发送短信失败:"+e);
					logger.info("给用户:"+dealInfo.getMobile()+"发送短信失败，内容:"+msg);
				}
			}
			pageNo++; //翻页
			page.setList(null);
		}
		while (page != null && !page.isLastPage());
	}

	/**
	 * 检索已经过期的催款任务，设置状态为到期
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void updateExpiredTask() {
		//分页处理，单页处理100条
		Page<TMisDunningTask> page = new Page<TMisDunningTask>(1, 9999);  // 临时处理后期待优化 
		int pageNo = 1; //页码
		do {
			page.setPageNo(pageNo);
			page = this.findExpiredTask(page);
			logger.info(MessageFormat.format("检索已过期催款任务，第{0}页,共{1}条", page.getPageNo(), page.getList().size()));

			int i = 1;
			for (TMisDunningTask task : page.getList()) {
				logger.info(MessageFormat.format("正在處理第幾條===>{0}", i++ ) );
				TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(task.getDealcode());
				if(personalInfo == null){
					logger.error(MessageFormat.format("自动分配personalInfo为空，Dealcode:===>{0}", task.getDealcode()));
					continue;
				}
				Date now = new Date();
				task.setEnd(now);
				task.setDunningtaskstatus(TMisDunningTask.STATUS_EXPIRED);
				task.setDunningAmounOnEnd((int)Double.valueOf(personalInfo.getCreditAmount()).doubleValue());
				this.save(task);
				logger.info("更新过期任务状态：" + task.getId());
			}
			pageNo++; //翻页
			page.setList(null);
		}
		while (page != null && !page.isLastPage());
	}

	/**
	 * 根据催款账期分配催收任务
	 *
	 * @param period
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void assignByPeriod(DunningPeriod period) {
		/**
		 *  获取对应催收账期的人员
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("BEGIN", period.begin);
		params.put("END", period.end);
//		params.put("PEOPLE_TYPE", TMisDunningPeople.PEOPLE_TYPE_INNER);
		List<TMisDunningPeople> peoples = this.tMisDunningPeopleDao.findByPeriod(params);
		if (peoples == null || peoples.isEmpty()) {
			logger.info(MessageFormat.format("{0}天到{1}天催收账期没有分配对应的催收人员", period.begin, period.end));
			return;
		}

		/**
		 * 获取待催收任务
		 */
		params = new HashMap<String, Object>();
		params.put("BEGIN", period.begin);
		params.put("END", period.end);
		List<TMisDunningOrder> dunningOrders = this.tMisDunningTaskDao.findNeedDunningOrder(params);
		if (dunningOrders == null || dunningOrders.isEmpty()) {
			logger.info(MessageFormat.format("账期{0}天到{1}天没有需要催收订单", period.begin, period.end));
			return;
		}
		logger.info(MessageFormat.format("账期{0}天到{1}天共有{2}条需要催收订单", period.begin, period.end, dunningOrders.size()));
//		System.out.println(JSON.toJSONString(dunningOrders));

		/**
		 *  获取人员的正在催收任务数
		 */
		params = new HashMap<String, Object>();
		params.put("BEGIN", period.begin);
		params.put("END", period.end);
		params.put("STATUS_DUNNING", TMisDunningTask.STATUS_DUNNING);
		List<Map<String, Object>> taskCounts = this.tMisDunningTaskDao.findDunningTaskByPeople(params);
		//计算该催款周期任务的总数
		int totalCount = 0;
		//计算最大待催人员订单数
		int maxPersonDunningTasks = 0;
		Map<String, Integer> peopleTaskCount = new HashMap<String, Integer>(); //催款人员对应当前任务表
		for (Map<String, Object> taskCount : taskCounts) {
			int task_count = null != taskCount.get("task_count") ?  ((Long)taskCount.get("task_count")).intValue(): 0;
			String people = (String) taskCount.get("people");
			peopleTaskCount.put(people, task_count);
			logger.info(MessageFormat.format("催款人员:{0}在账期{1}天到{2}天上任有{3}条任务在催款", people, period.begin, period.end, task_count));
			totalCount += task_count;
			if(task_count>maxPersonDunningTasks)
			{
				maxPersonDunningTasks=task_count;
			}
		}
		logger.info("个人最大在库订单数为"+maxPersonDunningTasks+"条");

		/**
		 * 分配算法
		 * 1.从最小在库数人员开始分配
		 * 2.剩余订单平均分配
		 */
		 Iterator<TMisDunningOrder> iterator  =  dunningOrders.iterator();
		 while(iterator.hasNext())
		 {
			 /**
			  * 获取任务最少的催收人
			  */
			 List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(peopleTaskCount.entrySet());
			 if(list.isEmpty() || list.size() == 0)
			 {
				 logger.info("周期中催收人任务数为空");
				 break;
			 }
			 Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				 @Override
				 public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
					 return o1.getValue()-o2.getValue();
				 }
			 });
			 String peopleId = list.get(0).getKey(); //当前在库任务最少的催收人
			 Integer taskCount = list.get(0).getValue();
			 if(taskCount>=maxPersonDunningTasks)
			 {
				 logger.info("补齐任务分配时,所有人的任务达到个人最大在库订单数,当前在库订单" + taskCount + "条，所以不再分配");
				 break;
			 }
			 TMisDunningOrder order = iterator.next();
			 TMisDunningPeople people = this.tMisDunningPeopleDao.get(peopleId);
			 if(people == null)
			 {
				 logger.info("催收人员;"+peopleId+",不存在");
				 continue;
			 }
			 //分配
			 try {

				 int taskcount = peopleTaskCount.containsKey(people.getId()) ? peopleTaskCount.get(people.getId()) : 0; //当前用户任务数
				 TMisDunningTask task = this.createDunningTask(people, order, period,null,null);
				 //分配后
				 taskcount++;  //任务数+1
				 peopleTaskCount.put(people.getId(), taskcount);
				 logger.info(MessageFormat.format("补齐分配订单：{0} 给催收人：{1} 进行催收,当前任务数:{2}", order.dealcode, people.getName(),taskcount));
				 iterator.remove();

			 } catch (Exception e) {
				 logger.error(MessageFormat.format("补齐分配补齐订单：{0} 给催收人：{1}发生错误", order.dealcode, people.getName()) ,e);
			 }


		 }

		int i = 0;
		for (TMisDunningOrder order : dunningOrders) {
			{
				/**
				 * 平均分配
				 */
				TMisDunningPeople people = peoples.get(i); //催款人员
				int taskcount = peopleTaskCount.containsKey(people.getId()) ? peopleTaskCount.get(people.getId()) : 0; //当前用户任务数
				//分配
				try {
					TMisDunningTask task = this.createDunningTask(people, order, period,null,null);
					//分配后
					taskcount++;  //任务数+1
					peopleTaskCount.put(people.getId(), taskcount);
					logger.info(MessageFormat.format("平均分配订单：{0} 给催收人：{1}进行催收,当前任务数:{2}", order.dealcode, people.getName(),taskcount));

				} catch (Exception e) {
					logger.error(MessageFormat.format("平均分配订单：{0} 给催收人：{1}发生错误", order.dealcode, people.getName()) ,e);
				}
				i++;
				if (i >= peoples.size()) {
					i = 0;
				}
		}

//		int avg = (int) Math.ceil((totalCount + dunningOrders.size()) / (double) peoples.size()); //分配后每个催收人员任务数的平均值
//		logger.info("催款人员平均任务数为" + avg);
//
//		int i = 0;
//		for (TMisDunningOrder order : dunningOrders) {
//			TMisDunningPeople people = peoples.get(i); //催款人员
//			int taskcount = peopleTaskCount.containsKey(people.getId()) ? peopleTaskCount.get(people.getId()) : 0; //当前用户任务数
//			if (taskcount >= avg) {
//				//如果当前用户的任务大于平均数，则不分配
//				logger.info("人员" + people.getName() + "任务达到平均数" + avg + "，所以不再分配");
//			} else {
//				//分配
//				try {
//					TMisDunningTask task = this.createDunningTask(people, order, period,null,null);
//					logger.info(MessageFormat.format("分配订单：{0} 给催收人：{1} 进行催收", order.dealcode, people.getName()));
//					//分配后
//					taskcount++;  //任务数+1
//					peopleTaskCount.put(people.getId(), taskcount);
//				} catch (Exception e) {
//					logger.error(MessageFormat.format("分配订单：{0} 给催收人：{1}发生错误", order.dealcode, people.getName()) ,e);
//				}
//			}
//			i++;
//			if (i >= peoples.size()) {
//				i = 0;
//			}
//		}

//		System.out.println(JSON.toJSONString(taskCounts));


//
//		for(TMisDunningPeople people : peoples)
//		{
//
//		}
		}
	}

	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	private void assignByPeriod(DunningPeriod period) {
////		List<Map<String, Object>> taskCounts = this.tMisDunningTaskDao.findDunningTaskByPeople(params);
//		/**
//		 * 分配算法
//		 * 1.按个人最大在库订单数补齐任务
//		 * 2.剩余订单平均分配
//		 */
//		 List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(this.tMisDunningTaskDao.findDunningTaskByPeople2(params));   
//         Comparator<Map.Entry<String, Integer>> comparator =   new Comparator<Map.Entry<String, Integer>>() {  
//            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {  
//                return o1.getValue()-o2.getValue();  
//            }  
//         };  
//         Collections.sort(list, comparator);  
//         for(Map.Entry<String, Integer> entry : list) {  
//            System.out.println(entry.getKey() + ":" + entry.getValue());  
//            
//         } 
//		
////		TMisDunningTask task = this.createDunningTask(people, order, period,null,null);
//
//	}


	/**
	 * 分配任务
	 *
	 * @param people
	 * @param order
	 * @param period
	 * @return
	 */
	private TMisDunningTask createDunningTask(TMisDunningPeople people, TMisDunningOrder order, DunningPeriod period,String beforTaskId,String beforePeopleId) throws Exception{
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();

		TMisDunningTask task = new TMisDunningTask();
		task.preInsert();
//		entity.preUpdate();
		task.setDunningpeopleid(people.getId());
		task.setDealcode(order.dealcode);
		BigDecimal fee = new BigDecimal(0l);
		if(order.fee != null){
			fee = order.fee;
		}
		task.setCapitalamount(order.amount.subtract(fee).multiply(new BigDecimal("100")).intValue());
		task.setBegin(toDate(now));
		calendar.setTime(toDate(order.repaymentDate));
		calendar.add(Calendar.DAY_OF_YEAR, period.end + 1);
		Date deadline = toDate(calendar.getTime());
		task.setDeadline(deadline);
		task.setEnd(null);
		task.setDunningperiodbegin(period.begin);
		task.setDunningperiodend(period.end);
		task.setDunnedamount(0);
		task.setIspayoff(false);
		task.setReliefamount(0);
		task.setBeforeTask(beforTaskId);
		task.setBeforeDunningPeople(beforePeopleId);
		task.setDunningtaskstatus(TMisDunningTask.STATUS_DUNNING);
		task.setRepaymentTime(new java.sql.Date(order.repaymentDate.getTime()));
//		task.setCreateDate(now);
//		task.setUpdateDate(now);
		this.tMisDunningTaskDao.insert(task);
		return task;
	}


	private static Date toDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public DunningOrder getDunningOrder(String id) {
		return dao.getOrders(id);
	}
	
	
	/**
	 * 获取催收权限
	 * @return
	 */
	public static int getPermissions(){
		int permissions = 1;   			// 催收专员
		for (Role r : UserUtils.getUser().getRoleList()){
			if(("委外主管").equals(r.getName())  &&  !r.getDataScope().equals(Role.DATA_SCOPE_SELF)){
				permissions += 10;
			}
			if(("催收主管").equals(r.getName())  &&  !r.getDataScope().equals(Role.DATA_SCOPE_SELF)){
				permissions += 100;
			}
			if(("财务主管").equals(r.getName())  &&  !r.getDataScope().equals(Role.DATA_SCOPE_SELF)){
				permissions = 1000;
			}
		}
		return permissions;
	}
	/*催收留案功能-留案提交 Patch 0001 by GQWU at 2016-11-9 start*/
	@Transactional(readOnly = false)
	public int updateDunningTimeByDealcodes(String[] dealcodes, Date deferDate) {
		
		return dao.updateDunningTimeByDealcodes(dealcodes, deferDate);
	}
	/*催收留案功能-留案提交 Patch 0001 by GQWU at 2016-11-9 end*/
	/*催收留案功能-获取留案可能最大日期范围 Patch 0001 by GQWU at 2016-11-25 start*/
	//获取订单不完整数据，仅用于留案验证
	public List<DunningOrder> getOrdersByDealcodes(String[] dealcodes){
		
		return dao.findOrdersByDealcodes(dealcodes);
		
	}
	
	
	/*催收留案功能-留案规则验证 Patch 0001 by GQWU at 2016-11-25 end*/
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DunningOrder> findOrderPageList(Page<DunningOrder> page, DunningOrder entity) {
//		entity.getSqlMap().put("dsf", dataScopeFilter(entity.getCurrentUser(), "o", "a"));
		int permissions = getPermissions();
		if(DUNNING_ALL_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
		}
		if(DUNNING_INNER_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
//			entity.setOverduedays(36);
//			entity.getSqlMap().put("overdueday", " AND overduedays < ");
		}
		if(DUNNING_OUTER_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
//			entity.setOverduedays(36);
//			entity.getSqlMap().put("overdueday", " AND overduedays >= ");
		}
		if(DUNNING_COMMISSIONER_PERMISSIONS == permissions){
			if(null == entity.getStatus()){
				entity.setStatus("payment");
			}
			entity.setDunningpeopleid(UserUtils.getUser().getId());
		}
		if(DUNNING_FINANCIAL_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
//			entity.setOverduedays(36);
//			entity.getSqlMap().put("overdueday", " AND overduedays >= ");
		}
		if(null != entity.getStatus() && entity.getStatus().equals("payoff")){
			entity.getSqlMap().put("orderbyMap", " payofftime DESC ");
		}else{
			entity.getSqlMap().put("orderbyMap", " status,date_FORMAT(repaymenttime, '%Y-%m-%d') DESC,creditamount DESC ");
		}
		entity.setPage(page);
		page.setList(dao.findOrderPageList(entity));
		return page;
	}
	
	/**
	 * 查询催收列表数据
	 * @param entity
	 * @return
	 */
	public List<DunningOrder> findOrderList(DunningOrder entity){
		int permissions = getPermissions();
		if(DUNNING_ALL_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
		}
		if(DUNNING_INNER_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
//			entity.setOverduedays(36);
//			entity.getSqlMap().put("overdueday", " AND overduedays < ");
		}
		if(DUNNING_OUTER_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
//			entity.setOverduedays(36);
//			entity.getSqlMap().put("overdueday", " AND overduedays >= ");
		}
		if(DUNNING_COMMISSIONER_PERMISSIONS == permissions){
			entity.setDunningpeopleid(UserUtils.getUser().getId());
		}
		if(DUNNING_FINANCIAL_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
//			entity.setOverduedays(36);
//			entity.getSqlMap().put("overdueday", " AND overduedays >= ");
		}
		return dao.findOrderPageList(entity);
	}

	/**
	 * 委外导出数据
	 * @param outerFile
	 * @return
	 */
	public List<DunningOuterFile> exportOuterFile(List<String> orders){
		return dao.exportOuterFile(orders);
	}
	
	/**
	 * 修改委外导出时间
	 * @param order
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateOuterfiletime(Date outerfiletime,List<String> dealcodes,List<DunningOuterFile> dunningOuterFiles){
		try {
			/**
			 * 修改委外导出时间
			 */
			dao.updateOuterfiletime(outerfiletime,dealcodes);
			List<DunningOuterFileLog> files = new ArrayList<DunningOuterFileLog>();
			for(DunningOuterFile outerFile : dunningOuterFiles){
				DunningOuterFileLog fileLog = new DunningOuterFileLog();
				fileLog.setDealcode(outerFile.getDealcode());
				fileLog.setRealname(outerFile.getRealname());
				fileLog.setMobile(outerFile.getMobile());
				fileLog.setDunningpeoplename(outerFile.getName());
				fileLog.setCreatedate(outerfiletime);
				files.add(fileLog);
			}
			
			dao.batchInsert(files);
			return true;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 查询订单历史借款记录
	 * @param orderHistory
	 * @return
	 */
	public List<OrderHistory> findOrderHistoryList(String buyerid){
		return dao.findOrderHistoryList(buyerid);
	}
	
	/**
	 *  部分还款
	 *  1.检索催款任务周期内且小于催款任务更新时间发生部分还款
	 *  2.计算部分还款计算，并生成相应的催款任务历史
	 *  3.计算剩余还款金额，更新还款任务
	 *  
	 *  
	 *  
	 *  1)根据sql抽出来
	 *  2）遍历任务期间的生成history ，并标记
	 *  
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	private void updatePartialTask(){
		try {
			//检索催款任务周期内且小于催款任务更新时间发生部分还款
			List<PartialOrder> partialOrders = this.tMisDunningTaskDao.findPartialOrders();
			for(PartialOrder partialOrder : partialOrders)
			{
				
				//循环处理部分还款订单
				String taskId= partialOrder.taskId;
				int repayAmount = partialOrder.repayAmount;
				Date repayTime = partialOrder.repayTime;
				String repayDealcode = partialOrder.repayDealcode;
				String parentDealStatus = partialOrder.parentDealStatus;
				logger.info("开始处理部分还款订单，一共:"+partialOrders.size()+"条");
				logger.info("开始处理部分还款订单，dealcode:"+repayDealcode+",父订单状态："+parentDealStatus);
				
				TMisDunnedHistory tmisHistory  = new TMisDunnedHistory();
				tmisHistory.setPartialdealcode(repayDealcode);
				List<TMisDunnedHistory> historyList =  tMisDunnedHistoryDao.findList(tmisHistory);
				if(historyList == null || historyList.size() > 0){
					logger.info("此部分还款订单已经记录，dealcode:"+repayDealcode);
					continue;
				}
				int overDueDays = 0;
				TMisDunningTask tmisTask = new TMisDunningTask();
				tmisTask = tMisDunningTaskDao.get(taskId);
				
				if(tmisTask != null){
					logger.info("开始处理催收任务，taskId:"+taskId);
					//如果这笔部分还款订单的状态为payoff，则父订单状态也应该为payoff
					if("payoff".equals(parentDealStatus)){
						tmisTask.preUpdate();
						tmisTask.setDunningtaskstatus(TMisDunningTask.STATUS_FINISHED);
						tmisTask.setDunnedamount(tmisTask.getDunnedamount() + repayAmount);
						tmisTask.setIspayoff(true);
						tmisTask.setEnd(new Date());
						this.tMisDunningTaskDao.update(tmisTask);
					}else{
						tmisTask.setId(taskId);
						tmisTask.preUpdate();
						tmisTask.setDunnedamount(tmisTask.getDunnedamount() + repayAmount);
						tmisTask.setIspayoff(false);
						this.tMisDunningTaskDao.update(tmisTask);
					}
					logger.info("更新催收任务成功,taskId:"+taskId);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String nowDate = formatter.format(new Date());
					String repaymentTime = formatter.format(tmisTask.getRepaymentTime());
					try {
						//逾期天数
						overDueDays = daysBetween(repaymentTime, nowDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					logger.info("错误，催收任务为null");
					return;
				}
				
				//新建催回记录
				TMisDunnedHistory history = new TMisDunnedHistory();
				history.preInsert();
				history.setTaskid(taskId);
				history.setAmount(repayAmount);
				history.setDunnedtime(repayTime);
				history.setPartialdealcode(repayDealcode);
				if("payoff".equals(parentDealStatus)){
					history.setIspayoff(true);
				}else{
					history.setIspayoff(false);
				}
				history.setOverduedays(overDueDays);
				history.setReliefamount(0);
				this.tMisDunnedHistoryDao.insert(history);
				logger.info("新建催收历史成功,taskId:"+taskId);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/** 
	*字符串的日期格式的计算 
	*/  
	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	
	
	/**
	 * 计算逾期天数，不满一天按一天计算
	 * @param repaymentDate 还款日
	 * @return
	 */
	public static int GetOverdueDay(Date repaymentDate)
	{
		Date now = new Date();
		long timeSub = toDate(now).getTime()-toDate(repaymentDate).getTime();
		double dayTimes = 24*60*60*1000d;
		return (int)Math.floor(timeSub/dayTimes);
	}
	
	

	/**
	 * 催收绩效月报
	 * @param performanceMonthReport
	 * @return
	 */
	public List<PerformanceMonthReport> findPerformanceMonthReport(PerformanceMonthReport performanceMonthReport){
		return tMisDunningTaskDao.findPerformanceMonthReport(performanceMonthReport);
	}
	
	/**
	 * 催收绩效月报-分页
	 * @param performanceMonthReport
	 * @return
	 */
	public Page<PerformanceMonthReport> findPerformanceMonthReport(Page<PerformanceMonthReport> page, PerformanceMonthReport entity) {
		entity.setPage(page);
		page.setList(dao.findPerformanceMonthReport(entity));
		return page;
	}
	
	/**
	 * 催收日报表
	 * @param performanceMonthReport
	 * @return
	 */
	public List<PerformanceDayReport> findPerformanceDayReport(PerformanceDayReport performanceMonthReport){
		return tMisDunningTaskDao.findPerformanceDayReport(performanceMonthReport);
	}
	
	/**
	 * 催收日报表-分页
	 * @param performanceMonthReport
	 * @return
	 */
	public Page<PerformanceDayReport> findPerformanceDayReport(Page<PerformanceDayReport> page, PerformanceDayReport entity) {
		entity.setPage(page);
		page.setList(dao.findPerformanceDayReport(entity));
		return page;
	}
	
	/**
	 * 记录催收操作记录时间
	 * @param ids
	 * @return
	 */
	public int updatedunningtimeList(List<String> ids){
		return dao.updatedunningtimeList(ids);
	}
	
	/**
	 * 查询催收人员正在催收的任务量
	 * @param dunningpeopleid
	 * @return
	 */
	public int findDunningCount(@Param("dunningpeopleid")String dunningpeopleid){
		return dao.findDunningCount(dunningpeopleid);
	}
	
	
//	public static void main(String[] args) {
//		String s = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:277759_PageNo:1_PageSize:30";
//		String s = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_listSize_buyerId:277759";
//		System.out.println(s.substring(s.indexOf(":")+1).split("_")[0]);
//		double lastpageNo =  Math.ceil((double)15 / (double)30);
//		System.out.println(lastpageNo);
//	}
	
	
	/**
	 *  定时清理任务缓存
	 */
	@Scheduled(cron = "0 10 5 * * ?")  //每天上午四点十分
	@Transactional(readOnly = false)
	public void delDunningTaskJedis(){
		
		String scheduledBut =  DictUtils.getDictValue("delDunningTaskJedis","Scheduled","false");
		if(scheduledBut.equals("true")){
			logger.info("redis开始清理已还清任务缓存:" + new Date());
	//		String keys = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:*";
			String keys = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_*";
			
			Map<String, String> mapAllBuyerid = new HashMap<String, String>(); // 缓存中全部的buyerid
			try {
				Set<String> redisAllKeys = JedisUtils.getSets(keys);
				logger.info("redis缓存Keys数量:"+ redisAllKeys.size() + "");
				for(String redisKeys : redisAllKeys){
//					System.out.println(redisKeys.substring(redisKeys.indexOf(":")+1).split("_")[0]);
					String buyerId = redisKeys.substring(redisKeys.indexOf(":")+1).split("_")[0];
					mapAllBuyerid.put(buyerId, buyerId);
				}
				
				Map<String, String> mapDunningBuyerid = new HashMap<String, String>(); // 缓存中正在催收的buyerid
				List<DunningOrder> list = dao.findDunningBuyerid();
				for(DunningOrder dunningOrder : list){
					if(mapAllBuyerid.containsKey(dunningOrder.getBuyerid().toString())){
						// 在催收中 task dunning
						mapDunningBuyerid.put(dunningOrder.getBuyerid().toString(), dunningOrder.getBuyerid().toString());
					}
				}
				
				for(String redisKeys : redisAllKeys){
					String buyerId = redisKeys.substring(redisKeys.indexOf(":")+1).split("_")[0];
					if(!mapDunningBuyerid.containsKey(buyerId)){  // 不在催收的buyerid
						logger.info("redis删除无用缓存Keys:"+ redisKeys+ "");
						JedisUtils.del(redisKeys);
					}
				}
				logger.info("redis清理完成:" + new Date());
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("异常", e);
			}
		}
	}
	
//	public void delDunningTaskJedis(){
//		String scheduledBut =  DictUtils.getDictValue("autoRepayment","Scheduled","");
//		if(scheduledBut.equals("true")){
//			logger.info(MessageFormat.format("自动定时更新任务缓存", new Date()));
//			List<DunningOrder>  dunningOrders = dao.findAllTaskList();
//			int result = (int) (1+Math.random()*(10-1+1));
//			JedisUtils.setObject("aaadddd", "成功放入缓存+" + String.valueOf(result), 0);
//			JedisUtils.setObject("allTaskList", dunningOrders, 0);
//			List<DunningOrder> s =  (List<DunningOrder>) JedisUtils.getObject("allTaskList");
//			System.out.println(s.size());
//			String aa =  (String) JedisUtils.getObject("aaadddd");
//			System.out.println(aa);
//			Map<String, Object> map = new HashMap<String, Object>();
//			int i = 0;
//			for(DunningOrder dunningOrder : dunningOrders){
//				map.put(i + "", dunningOrder);
//				i += 1;
//			}
//			JedisUtils.setObjectMap("hashList", map , 0);
//			System.out.println("s");
//		}
//	}
	
	
//	/**
//	 * 根据手机号码查询登录Log
//	 * @param mobile
//	 * @return
//	 */
//	public List<AppLoginLog> getApploginlog(String mobile){
//		try {
//			return dbUtils.getApploginlog(mobile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	
	
//	public static final void main(String[] args)throws Exception
//	{
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date now = new Date();
////		int days = (int)(toDate(now).getTime() - toDate(sdf.parse("2016-08-15 12:08:06")).getTime()) / (24 * 60 * 60 * 1000);
//		int days = TMisDunningTaskService.GetOverdueDay(sdf.parse("2016-08-15 11:41:21"));
////		int days = daysBetween("2016-08-15 11:41:06","2016-08-17 16:24:00");
//		System.out.println(days);
//	}
	
}