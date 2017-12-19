/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.gamaxpay.commonutil.msf.BaseResponse;
import com.gamaxpay.commonutil.msf.JacksonConvertor;
import com.gamaxpay.commonutil.msf.ServiceAddress;
import com.mo9.risk.modules.dunning.bean.BlackListRelation;
import com.mo9.risk.modules.dunning.bean.TmpMoveCycle;
import com.mo9.risk.modules.dunning.dao.TMisContantRecordDao;
import com.mo9.risk.modules.dunning.dao.TMisDunnedHistoryDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskLogDao;
import com.mo9.risk.modules.dunning.dao.TMisReliefamountHistoryDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerPersonalInfoDao;
import com.mo9.risk.modules.dunning.dao.TmisDunningSmsTemplateDao;
import com.mo9.risk.modules.dunning.entity.AppLoginLog;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.DunningOuterFile;
import com.mo9.risk.modules.dunning.entity.DunningOuterFileLog;
import com.mo9.risk.modules.dunning.entity.DunningPeriod;
import com.mo9.risk.modules.dunning.entity.OrderHistory;
import com.mo9.risk.modules.dunning.entity.PartialOrder;
import com.mo9.risk.modules.dunning.entity.PerformanceMonthReport;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.ContactsType;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.ContantType;
import com.mo9.risk.modules.dunning.entity.TMisDunnedConclusion;
import com.mo9.risk.modules.dunning.entity.TMisDunnedHistory;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisDunningTaskLog;
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.entity.TmisDunningNumberClean;
import com.mo9.risk.modules.dunning.entity.TmisDunningSmsTemplate;
import com.mo9.risk.util.DateUtils;
import com.mo9.risk.util.MsfClient;
import com.mo9.risk.util.RegexUtil;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.util.ListSortUtil;
import com.thinkgem.jeesite.util.NumberUtil;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdbc.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 催收任务Service
 * @author 徐盛
 * @version 2016-07-12
 */
@Service
@Transactional(readOnly = true)
@Lazy(false)
public class TMisDunningTaskService extends CrudService<TMisDunningTaskDao, TMisDunningTask> {
	private static final Map<String, Integer> telconclusion=new HashMap<String, Integer>();
	static{
		telconclusion.put("ALPA", 1);
		telconclusion.put("FEE", 1);
		telconclusion.put("PTP", 1);
		telconclusion.put("PTPX", 1);
		telconclusion.put("KNOW", 2);
		telconclusion.put("MESS", 2);
		telconclusion.put("INSY", 3);
		telconclusion.put("BUSY", 4);
		telconclusion.put("CUT", 4);
		telconclusion.put("NOAS", 4);
		telconclusion.put("OFF", 4);
		telconclusion.put("NOTK", 4);
		telconclusion.put("LOOO", 5);
		telconclusion.put("MESF", 5);
		telconclusion.put("NOSE", 5);
		telconclusion.put("STOP", 5);
	}
	public static final Integer DUNNING_FINANCIAL_PERMISSIONS = 1000;    //  财务权限
	public static final Integer DUNNING_SUPERVISOR = 10000;              //  催收监理
	public static final Integer DUNNING_ALL_PERMISSIONS = 111;           //  风控总监
	public static final Integer DUNNING_INNER_PERMISSIONS = 101;         //  催收主管
	public static final Integer DUNNING_OUTER_PERMISSIONS =  11;         //  委外催收主管
	public static final Integer DUNNING_COMMISSIONER_PERMISSIONS = 1;    //  催收专员
//	public static final Integer DUNNING_SUPERVISION_MANAGER_PERMISSIONS =  100001;    //  催收监理经理
//	public static final Integer DUNNING_SUPERVISION_PERMISSIONS =  10001;    //  催收监理
//	public static final Integer DUNNING_GROUP_PERMISSIONS =   1001;         //  催收
	
	public static final String  AUTOQ0_ID_1 = "autoQ0_id_1";
	public static final String  AUTOQ0_NAME_1 = "autoQ0机器人1号";
	public static final String QUALITY_GOOD = "y";
	public static final String QUALITY_ORDINARY = "n";
	
	public static final String  C0 = "Q0";      //  提醒0-0
	public static final String  C_P1 = "Q1";	 
	public static final String  P1_P2 = "Q2";	 
	public static final String  P2_P3 = "Q3";	 
	public static final String  P3_P4 = "Q4";	 
	public static final String  P4_P5 = "Q5";	 
	
	private static Logger logger = Logger.getLogger(TMisDunningTaskService.class);

	@Autowired
	private TMisContantRecordService tMisContantRecordService;
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	@Autowired
	private TMisDunningTaskLogDao tMisDunningTaskLogDao;
	@Autowired
	private TMisDunningPeopleDao tMisDunningPeopleDao;
	@Autowired
	private TMisDunningGroupService tMisDunningGroupService;
	@Autowired
	private TMisDunnedHistoryDao tMisDunnedHistoryDao;
	
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoDao;
	
	@Autowired
	private TMisReliefamountHistoryDao tMisReliefamountHistoryDao;

	@Autowired
	private TRiskBuyerPersonalInfoDao tpersonalInfoDao;
	
	@Autowired
	private TMisContantRecordDao tcontDao;
	@Autowired
	private TmisDunningSmsTemplateDao tdstDao;
	@Autowired
	private TmisDunningSmsTemplateService tdstService;
	@Autowired
	private TaskScheduler scheduler;
	
	@Autowired
	private TMisDunningTaskSupportService tMisDunningTaskSupportService;
	@Autowired
	private TMisDunnedConclusionService tMisDunnedConclusionService;
	@Autowired
	private RiskQualityInfoService riskQualityInfoService;


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
	
	public TMisDunningTask findDunningTaskByDealcode(Map<String,Object> params) {
		return tMisDunningTaskDao.findDunningTaskByDealcode(params);
	}
	
	/**
	 * 操作order表(切换数据源提取出update方法)
	 */
	@Transactional(readOnly = false)
	public boolean updateOrderModifyAmount(String dealcode,String amount) {
		try {
			/**
			 *  根据订单号保存订单减免金额
			 */
			TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			order.setReliefamount(new BigDecimal(amount));
			tMisDunningTaskDao.updateOrder(order);
			tMisDunningTaskDao.updateOrderPartial(new BigDecimal(amount), order.getId());
			return true;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	
	@Transactional(readOnly = false)
	public boolean savefreeCreditAmount(String dealcode,TMisDunningTask task,String amount
			,TMisReliefamountHistory tfHistory) {
		try {
			/**
			 *  根据订单号保存订单减免金额
			 */
//			TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
//			order.setReliefamount(new BigDecimal(amount));
//			tMisDunningTaskDao.updateOrder(order);
//			tMisDunningTaskDao.updateOrderPartial(new BigDecimal(amount), order.getId());
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
			reliefamountHistory.setDerateReason(tfHistory.getDerateReason());
			reliefamountHistory.setRemarks(tfHistory.getRemarks());
			tMisReliefamountHistoryDao.insert(reliefamountHistory);
			return true;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
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
//	@Transactional
//	@Scheduled(cron = "0 0 2 * * ?")  //每天上午两点执行
//	public void autoAssign() {
//		/**
//		 *  STEP1  检索已经还清的催收任务（关联的贷款订单已还，还款时间在任务的起始时间和截至时间或结束时间内 且任务状态为还款中）
//		 * 				将符合条件的催款任务设置为结束
//		 */
//		this.updatePayoffTask();
//		/**
//		 *  STEP2
//		 */
//		this.updateExpiredTask();
//		/**
//		 *  查找现在内部人员所有催收账期
//		 */
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("PEOPLE_TYPE_INNER", TMisDunningPeople.PEOPLE_TYPE_INNER);
//		List<DunningPeriod> periods = this.tMisDunningTaskDao.findDunningPeriod(params);
//		logger.info("系统共有" + periods.size() + "个催款账期");
//		for (DunningPeriod period : periods) {
//			logger.info("开始分配" + period.begin + "天到" + period.end + "天催收任务");
//			this.assignByPeriod(period);
//		}
//	}

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
//	@Transactional(readOnly = false)
//	public boolean assign(String dealcode, String peopleId) {
//		TMisDunningPeople people = this.tMisDunningPeopleDao.get(peopleId);
//		if(people == null)
//		{
//			 logger.warn("催收人员不存在："+peopleId);
//			 return false;
//		}
//
//		TMisDunningOrder order = this.tMisDunningTaskDao.findOrderByDealcode(dealcode);
//		if(order == null)
//		{
//			logger.warn("分配订单不存在:"+dealcode);
//			return false;
//		}
//		if(!"payment".equalsIgnoreCase(order.status))
//		{
//			logger.warn("催款订单状态错误，状态："+order.status);
//			return false;
//		}
//	 
//		Date now = new Date();
////		long overduedays = (toDate(now).getTime()-toDate(order.repaymentDate).getTime())/(24*60*60*1000);
//		long overduedays = TMisDunningTaskService.GetOverdueDay(order.repaymentDate);
//		logger.info(MessageFormat.format("订单逾期{0}天，催款人<{1}>负责催收{2}天到{3}天",overduedays,people.getName(),people.getBegin(),people.getEnd()));
////		if(!(overduedays>=people.getBegin() && overduedays<=people.getEnd()))
////		{
////			logger.warn(MessageFormat.format("订单的逾期周期不符合该用户的催款周期，订单逾期{0}天，催款人<{1}>负责催收{2}天到{3}天",overduedays,people.getName(),people.getBegin(),people.getEnd()));
////			return false;
////		}
//
//		Map<String,Object> params = new HashMap<String, Object>();
//		params.put("STATUS_DUNNING",TMisDunningTask.STATUS_DUNNING);
//		params.put("DEALCODE",dealcode);
//		String beforeTaskId = null;
//		String beforePeopleId = null;
//		TMisDunningTask task = this.tMisDunningTaskDao.findDunningTaskByDealcode(params);
//		
////		if(TMisDunningTask.STATUS_TRANSFER.equals(task.getDunningtaskstatus())){
////			logger.warn(MessageFormat.format("催款人<{0}>的订单任务状态为同期转移",people.getName()));
////			return false;
////		}
//		
//		if(task != null)
//		{
//			beforeTaskId = task.getId();
//			beforePeopleId = task.getDunningpeopleid();
//			String status = TMisDunningTask.STATUS_END;
//			if(task.getDunningpeopleid().equals(peopleId))
//			{
//				logger.warn("该任务已经被分配给该用户");
//				return false;
//			}
//			if(task.getDunningperiodbegin().equals(people.getBegin())
//					&& task.getDunningperiodend().equals(people.getEnd()))
//			{
//				status = TMisDunningTask.STATUS_TRANSFER;
//				logger.info(MessageFormat.format("催款任务:{0} 同期转移给催款人<{1}>",task.getId(),people.getName()));
//
//			}
//			logger.info(MessageFormat.format("催款任务:{0} 重新分配给催款人<{1}>,账期为{2}天到{3}天",task.getId(),people.getName(),people.getBegin(),people.getEnd()));
//	        task.setDunningtaskstatus(status);
//			task.setEnd(now);
//			task.setUpdateDate(now);
//			TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
//			if(personalInfo == null){
//				logger.info(MessageFormat.format("手动分配personalInfo为空，Dealcode:===>", dealcode));
//				return false;
//			}
//			task.setDunningAmounOnEnd((int)Double.valueOf(personalInfo.getCreditAmount()).doubleValue());
//			task.preUpdate();
////			task.setDunningpeopleid(peopleId)
//			this.tMisDunningTaskDao.update(task);
//
//		}
//		else
//		{
//			logger.info(MessageFormat.format("订单:{0} 分配给催款人<{1}>,账期为{2}天到{3}天",order.dealcode,people.getName(),people.getBegin(),people.getEnd()));
//		}
//		DunningPeriod period = new DunningPeriod();
//		period.begin = people.getBegin();
//		period.end = people.getEnd();
//		try{
//			this.createDunningTask(people, order, period,beforeTaskId,beforePeopleId);
//			return true;
//		} catch (Exception e) {
//			logger.error(MessageFormat.format("分配订单：{0} 给催收人：{1}发生错误", order.dealcode, people.getName()) ,e);
//		}
//		return false;
//	}

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

//	@Transactional(readOnly = false)
//	public boolean repayment(TMisDunningTask task) {
//
//		task = this.tMisDunningTaskDao.get(task.getId());
//		if(task == null)
//		{
//			logger.warn("任务不存在,id:"+task.getId());
//			return false;
//		}
//		String dealcode = task.getDealcode();
//		TMisDunningOrder order = this.tMisDunningTaskDao.findOrderByDealcode(dealcode);
//		if(order == null)
//		{
//			logger.warn("还款订单不存在，订单号："+dealcode);
//			return false;
//		}
//		if(!"payoff".equalsIgnoreCase(order.status))
//		{
//			logger.warn("订单状态错误，状态:"+order.status+" 订单号："+dealcode);
//			return false;
//		}
//		Date now = new Date();
//		//新建催回记录
//		TMisDunnedHistory history = new TMisDunnedHistory();
//		history.preInsert();
//		history.setTaskid(task.getId());
//		history.setAmount((order.amount.add(order.overdueAmount).subtract(order.reliefamount)).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
//		history.setDunnedtime(now);
//		history.setIspayoff(true);
////		history.setOverduedays((int)(toDate(now).getTime() - toDate(order.repaymentDate).getTime()) / (24 * 60 * 60 * 1000));
//		history.setOverduedays((int)TMisDunningTaskService.GetOverdueDay(order.repaymentDate));
//		history.setReliefamount(order.reliefamount.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
//		this.tMisDunnedHistoryDao.insert(history);
//
//		task.preUpdate();
//		task.setDunningtaskstatus(TMisDunningTask.STATUS_FINISHED);
//		task.setDunnedamount(history.getAmount());
//		task.setIspayoff(true);
//		task.setEnd(now);
//		this.tMisDunningTaskDao.update(task);
//		return true;
//	}

//	/**
//	 *  定时自动扫描还款
//	 */
//	@Scheduled(cron = "0 0/15 * * * ?") //每十五分钟执行一次
//	@Transactional(readOnly = false)
//	public void autoRepayment(){
//		String scheduledBut =  DictUtils.getDictValue("autoRepayment","Scheduled","");
//		if(scheduledBut.equals("true")){
//			logger.info(MessageFormat.format("自动扫描还款正在運行=========>{0}", new Date()));
//			this.updatePartialTask();
//			this.updatePayoffTask();
//		}
//	}
	/**
	 * 检索已经还清的催收任务（关联的贷款订单已还，还款时间在任务的起始时间和截至时间或结束时间内 且任务状态为还款中）
	 * 将符合条件的催款任务设置为结束
	 */
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	private void updatePayoffTask() {
//		//分页处理，单页处理100条
//		Page<TMisDunningTask> page = new Page<TMisDunningTask>(1, 100);
//		int pageNo = 1; //页码
//		do {
//			page.setPageNo(pageNo);
//			page = this.findPayoffDunningTask(page);
//			logger.info(MessageFormat.format("检索已还清催款任务，第{0}页,共{1}条", page.getPageNo(), page.getList().size()));
//			int i = 1;
//			for (TMisDunningTask task : page.getList()) {
//				logger.info(MessageFormat.format("正在處理第幾條===>{0}", i++ ) );
//				boolean result = this.repayment(task);
//				if (!result) {
//					//如果处理还款不成功，日志记录
//					logger.info("批量更新已还催款任务错误，任务号：" + task.getId());
//				} else {
//					logger.info("批量更新已还催款任务成功，任务号：" + task.getId());
//				}
//
//			}
//			pageNo++; //翻页
//			page.setList(null);
//		}
//		while (page != null && !page.isLastPage());
//
//	}
	
	
	
	/**
	 * 自动发送催收短信，到期还款日为今日
	 */
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//	private void sendSms() {
//		//分页处理，单页处理100条
//		Page<TRiskBuyerPersonalInfo> page = new Page<TRiskBuyerPersonalInfo>(1, 100);
//		int pageNo = 1; //页码
//		do {
//			page.setPageNo(pageNo);
//			page = this.getBuyerListByRepaymentTime(page);
//			logger.info(MessageFormat.format("检索已还清催款任务，第{0}页,共{1}条", page.getPageNo(), page.getList().size()));
//			for (TRiskBuyerPersonalInfo dealInfo : page.getList()) {
//				Double amount = new Double(0);
//				amount = Double.valueOf(dealInfo.getCreditAmount()).doubleValue() /100D;
//				
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				String repaymentTime = sdf.format(dealInfo.getRepaymentTime());
//				
//				String msg = MessageFormat.format("【mo9】江湖救急用户，您的借款即将到期，还款金额{0}元，最后还款日{1}，按时还款有助您提高借款额度。",amount,repaymentTime);
//				
//				try{
//					/**
//					 *  发送短信
//					 */
//					Map<String,String> params = new HashMap<String,String>();
//					params.put("mobile", dealInfo.getMobile());
//					params.put("message",msg);
//					MsfClient.instance().requestFromServer(ServiceAddress.SNC_SMS, params, BaseResponse.class);
//					logger.info("给用户:"+dealInfo.getMobile()+"发送短信成功，内容:"+msg);
//					
//					TMisContantRecord dunning = new TMisContantRecord();
//					dunning.setTaskid(null);
//					dunning.setDealcode(dealInfo.getDealcode());
//					dunning.setOrderstatus(false);
//					dunning.setOverduedays(Integer.valueOf(dealInfo.getOverdueDays()));
//					dunning.setDunningtime(new Date());
//					dunning.setContanttype(ContantType.sms);
//					dunning.setContanttarget(dealInfo.getMobile());
//					//短信内容
//					dunning.setContent(msg);
//					dunning.setSmstemp(SmsTemp.ST_0);
//					dunning.setTelstatus(null);
//					dunning.setContactstype(ContactsType.SELF);
//					dunning.setDunningpeoplename("sys");
//					dunning.setRepaymenttime(dealInfo.getRepaymentTime());
//					dunning.setRemark(null);
//					
//					tMisContantRecordService.save(dunning);
//				}catch (Exception e) {
//					logger.info("发送短信失败:"+e);
//					logger.info("给用户:"+dealInfo.getMobile()+"发送短信失败，内容:"+msg);
//				}
//			}
//			pageNo++; //翻页
//			page.setList(null);
//		}
//		while (page != null && !page.isLastPage());
//	}

	/**
	 * 检索已经过期的催款任务，设置状态为到期
	 */
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	private void updateExpiredTask() {
//		//分页处理，单页处理100条
//		Page<TMisDunningTask> page = new Page<TMisDunningTask>(1, 9999);  // 临时处理后期待优化 
//		int pageNo = 1; //页码
//		do {
//			page.setPageNo(pageNo);
//			page = this.findExpiredTask(page);
//			logger.info(MessageFormat.format("检索已过期催款任务，第{0}页,共{1}条", page.getPageNo(), page.getList().size()));
//
//			int i = 1;
//			for (TMisDunningTask task : page.getList()) {
//				logger.info(MessageFormat.format("正在處理第幾條===>{0}", i++ ) );
//				TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(task.getDealcode());
//				if(personalInfo == null){
//					logger.error(MessageFormat.format("自动分配personalInfo为空，Dealcode:===>{0}", task.getDealcode()));
//					continue;
//				}
//				Date now = new Date();
//				task.setEnd(now);
//				task.setDunningtaskstatus(TMisDunningTask.STATUS_EXPIRED);
//				task.setDunningAmounOnEnd((int)Double.valueOf(personalInfo.getCreditAmount()).doubleValue());
//				this.save(task);
//				logger.info("更新过期任务状态：" + task.getId());
//			}
//			pageNo++; //翻页
//			page.setList(null);
//		}
//		while (page != null && !page.isLastPage());
//	}

	/**
	 * 根据催款账期分配催收任务
	 *
	 * @param period
	 */
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	private void assignByPeriod(DunningPeriod period) {
//		/**
//		 *  获取对应催收账期的人员
//		 */
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("BEGIN", period.begin);
//		params.put("END", period.end);
////		params.put("PEOPLE_TYPE", TMisDunningPeople.PEOPLE_TYPE_INNER);
//		List<TMisDunningPeople> peoples = this.tMisDunningPeopleDao.findByPeriod(params);
//		if (peoples == null || peoples.isEmpty()) {
//			logger.info(MessageFormat.format("{0}天到{1}天催收账期没有分配对应的催收人员", period.begin, period.end));
//			return;
//		}
//
//		/**
//		 * 获取待催收任务
//		 */
//		params = new HashMap<String, Object>();
//		params.put("BEGIN", period.begin);
//		params.put("END", period.end);
//		List<TMisDunningOrder> dunningOrders = this.tMisDunningTaskDao.findNeedDunningOrder(params);
//		if (dunningOrders == null || dunningOrders.isEmpty()) {
//			logger.info(MessageFormat.format("账期{0}天到{1}天没有需要催收订单", period.begin, period.end));
//			return;
//		}
//		logger.info(MessageFormat.format("账期{0}天到{1}天共有{2}条需要催收订单", period.begin, period.end, dunningOrders.size()));
////		System.out.println(JSON.toJSONString(dunningOrders));
//
//		/**
//		 *  获取人员的正在催收任务数
//		 */
//		params = new HashMap<String, Object>();
//		params.put("BEGIN", period.begin);
//		params.put("END", period.end);
//		params.put("STATUS_DUNNING", TMisDunningTask.STATUS_DUNNING);
//		List<Map<String, Object>> taskCounts = this.tMisDunningTaskDao.findDunningTaskByPeople(params);
//		//计算该催款周期任务的总数
//		int totalCount = 0;
//		//计算最大待催人员订单数
//		int maxPersonDunningTasks = 0;
//		Map<String, Integer> peopleTaskCount = new HashMap<String, Integer>(); //催款人员对应当前任务表
//		for (Map<String, Object> taskCount : taskCounts) {
//			int task_count = null != taskCount.get("task_count") ?  ((Long)taskCount.get("task_count")).intValue(): 0;
//			String people = (String) taskCount.get("people");
//			peopleTaskCount.put(people, task_count);
//			logger.info(MessageFormat.format("催款人员:{0}在账期{1}天到{2}天上任有{3}条任务在催款", people, period.begin, period.end, task_count));
//			totalCount += task_count;
//			if(task_count>maxPersonDunningTasks)
//			{
//				maxPersonDunningTasks=task_count;
//			}
//		}
//		logger.info("个人最大在库订单数为"+maxPersonDunningTasks+"条");
//
//		/**
//		 * 分配算法
//		 * 1.从最小在库数人员开始分配
//		 * 2.剩余订单平均分配
//		 */
//		 Iterator<TMisDunningOrder> iterator  =  dunningOrders.iterator();
//		 while(iterator.hasNext())
//		 {
//			 /**
//			  * 获取任务最少的催收人
//			  */
//			 List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(peopleTaskCount.entrySet());
//			 if(list.isEmpty() || list.size() == 0)
//			 {
//				 logger.info("周期中催收人任务数为空");
//				 break;
//			 }
//			 Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
//				 @Override
//				 public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//					 return o1.getValue()-o2.getValue();
//				 }
//			 });
//			 String peopleId = list.get(0).getKey(); //当前在库任务最少的催收人
//			 Integer taskCount = list.get(0).getValue();
//			 if(taskCount>=maxPersonDunningTasks)
//			 {
//				 logger.info("补齐任务分配时,所有人的任务达到个人最大在库订单数,当前在库订单" + taskCount + "条，所以不再分配");
//				 break;
//			 }
//			 TMisDunningOrder order = iterator.next();
//			 TMisDunningPeople people = this.tMisDunningPeopleDao.get(peopleId);
//			 if(people == null)
//			 {
//				 logger.info("催收人员;"+peopleId+",不存在");
//				 continue;
//			 }
//			 //分配
//			 try {
//
//				 int taskcount = peopleTaskCount.containsKey(people.getId()) ? peopleTaskCount.get(people.getId()) : 0; //当前用户任务数
//				 TMisDunningTask task = this.createDunningTask(people, order, period,null,null);
//				 //分配后
//				 taskcount++;  //任务数+1
//				 peopleTaskCount.put(people.getId(), taskcount);
//				 logger.info(MessageFormat.format("补齐分配订单：{0} 给催收人：{1} 进行催收,当前任务数:{2}", order.dealcode, people.getName(),taskcount));
//				 iterator.remove();
//
//			 } catch (Exception e) {
//				 logger.error(MessageFormat.format("补齐分配补齐订单：{0} 给催收人：{1}发生错误", order.dealcode, people.getName()) ,e);
//			 }
//
//
//		 }
//
//		int i = 0;
//		for (TMisDunningOrder order : dunningOrders) {
//			{
//				/**
//				 * 平均分配
//				 */
//				TMisDunningPeople people = peoples.get(i); //催款人员
//				int taskcount = peopleTaskCount.containsKey(people.getId()) ? peopleTaskCount.get(people.getId()) : 0; //当前用户任务数
//				//分配
//				try {
//					TMisDunningTask task = this.createDunningTask(people, order, period,null,null);
//					//分配后
//					taskcount++;  //任务数+1
//					peopleTaskCount.put(people.getId(), taskcount);
//					logger.info(MessageFormat.format("平均分配订单：{0} 给催收人：{1}进行催收,当前任务数:{2}", order.dealcode, people.getName(),taskcount));
//
//				} catch (Exception e) {
//					logger.error(MessageFormat.format("平均分配订单：{0} 给催收人：{1}发生错误", order.dealcode, people.getName()) ,e);
//				}
//				i++;
//				if (i >= peoples.size()) {
//					i = 0;
//				}
//		}
//
//		}
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
			if(("催收监理").equals(r.getName())  &&  !r.getDataScope().equals(Role.DATA_SCOPE_SELF)){
				permissions = 10000;
				break;
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
			entity.getSqlMap().put("orderbyMap", " status,date_FORMAT(repaymenttime, '%Y-%m-%d') DESC,creditamount DESC,dealcode DESC");
		}
		entity.setPage(page);
		page.setList(dao.findOrderPageList(entity));
		return page;
	}
	
	/**
	 * 查询分页数据-优化版
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DunningOrder> newfindOrderPageList(Page<DunningOrder> page, DunningOrder entity) {
		int permissions = getPermissions();
		List<String> allowedGroupIds = new ArrayList<String>();
		if(DUNNING_ALL_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
		}
		if (DUNNING_INNER_PERMISSIONS == permissions) {
			entity.setDunningpeopleid(null);
			allowedGroupIds.addAll(tMisDunningGroupService.findIdsByLeader(UserUtils.getUser()));
			entity.setGroupIds(allowedGroupIds);
		}
		if(DUNNING_OUTER_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
		}
		if(DUNNING_COMMISSIONER_PERMISSIONS == permissions){
			if(null == entity.getStatus()){
				entity.setStatus("payment");
			}
			entity.setDunningpeopleid(UserUtils.getUser().getId());
		}
		if(DUNNING_FINANCIAL_PERMISSIONS == permissions){
			entity.setDunningpeopleid(null);
		}
		if (DUNNING_SUPERVISOR == permissions) {
			TMisDunningGroup group = new TMisDunningGroup();
			group.setSupervisor(UserUtils.getUser());
			List<String> groupIds = tMisDunningGroupService.findSupervisorGroupList(group);
			allowedGroupIds.addAll(groupIds);
			entity.setGroupIds(allowedGroupIds);
		}

		if(null != entity.getStatus() && entity.getStatus().equals("payoff")){
			entity.getSqlMap().put("orderbyMap", " o.payoff_time DESC ");
		}else{
			entity.getSqlMap().put("orderbyMap", " t.CreateDate DESC,t.CapitalAmount DESC,t.Dealcode DESC");
		}

		entity.setPage(page);

		//不使用分页插件,使用自定义的count语句
		page.setUsePaginationInterceptor(false);
		page.setCount(dao.newfindOrderPageCount(entity));

		List<DunningOrder> orders = dao.newfindOrderPageList(entity);
		page.setList(orders);
		
		double creditamount = 0;
		double corpusamount = 0;
		if (orders != null && orders.size() != 0) {
			for (DunningOrder order : orders) {
				//分页金额统计
				creditamount += order.getCreditamount() == null ? 0 : order.getCreditamount();
				corpusamount += order.getCorpusamount() == null ? 0 : order.getCorpusamount();
				//添加黑名单关系
				this.appendBlackNodeNum(order);
			}
		}
		String message = "，本金 " + NumberUtil.formatTosepara(corpusamount) + " 元，金额 " + NumberUtil.formatTosepara(creditamount) + " 元";
		page.setMessage(message);

		return page;
	}

	/**
	 * @Description  添加黑名单关系
	 * @param order
	 * @return void
	 */
	private void appendBlackNodeNum(DunningOrder order) {
		String mobile = order.getMobile();
		if (StringUtils.isBlank(mobile)){
			return;
		}

		try {
			BlackListRelation relation = riskQualityInfoService.getBlackListRelationByMobile(mobile);
			if (relation == null){
				order.setBlackListRelaNum(null);
				return;
			}
			order.setBlackListRelaNum(relation.getNum());
			order.setBlackListNumFromMo9(relation.getNumFromMo9());
			order.setBlackListNumFromThird(relation.getNumFromThird());
			order.setBlackListNumUnknow(relation.getNumUnknow());
		} catch (Exception e) {
			order.setBlackListRelaNum(null);
			logger.info("黑名单关系获取失败",e);
		}
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
	 * 修改委外导出时间-order表(切换数据源提取出update方法)
	 * @param order
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateOuterfiletime(Date outerfiletime,List<String> dealcodes){
		try {
			dao.updateOuterfiletime(outerfiletime,dealcodes);
			return true;
		} catch (Exception e) {
			logger.warn("切换updateOrderDataSource数据源更新order表异常，时间:"+ new Date());
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 修改委外导出时间
	 * @param order
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean savefileLog(Date outerfiletime,List<String> dealcodes,List<DunningOuterFile> dunningOuterFiles){
		try {
//			/**
//			 * 修改委外导出时间
//			 */
//			dao.updateOuterfiletime(outerfiletime,dealcodes);
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
						logger.info(e);
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
	 * @param entity
	 * @return
	 */
	public Page<PerformanceMonthReport> findPerformanceMonthReport(Page<PerformanceMonthReport> page, PerformanceMonthReport entity) {
		entity.setPage(page);
		page.setList(dao.findPerformanceMonthReport(entity));
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
	 *  定时清理任务已还清通话记录缓存
	 */
	@Scheduled(cron = "0 0 3 * * ?")  //每天上午四点
	@Transactional(readOnly = false)
	public void delDunningTaskJedis(){
		
		String scheduledBut =  DictUtils.getDictValue("delDunningTaskJedis","Scheduled","false");
		if(scheduledBut.equals("true")){
			logger.info("redis开始清理已还清通话记录任务缓存:" + new Date());
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
				logger.info("redis清理已还清通话记录完成:" + new Date());
			} catch (Exception e) {
				logger.warn("redis清理已还清通话记录异常", e);
			}
		}
	}
	
	public List<AppLoginLog> findApploginlog(String mobile){
		return dao.findApploginlog(mobile);
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
	
	
	
//=============================================================  新分案方法   ====================================================================================
	
	/**
	 * 获取每个队列的催收人员集合
	 * @return
	 */
	public Map<String, List<TMisDunningPeople>> getDunningcyclePeopleLists(){
		Map<String, List<TMisDunningPeople>> map = new HashMap<String, List<TMisDunningPeople>>();
		String type = getDunningCycleType();
		List<Dict> dicts = DictUtils.getDictList(type);
		for(Dict dict : dicts){
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleDao.findPeopleByDunningcycle(dict.getLabel());
			map.put(dict.getLabel(), dunningPeoples);
		}
		return map;
	}
	
	/**
	 * 订单已还款更新任务
	 */
	public void newUpdatePayoffTask(){
		try {
			List<TMisDunningTaskLog> dunningTaskLogs = tMisDunningTaskDao.newfingTaskByOrderStatusandTaskstatus("payoff", TMisDunningTask.STATUS_DUNNING);
			if(!dunningTaskLogs.isEmpty()){
				List<TMisDunningTask> dunningTasks = new ArrayList<TMisDunningTask>();
				for(TMisDunningTaskLog taskLog : dunningTaskLogs){
					//延期订单完成
					if(null != taskLog.getDelaydays() && null != taskLog.getDelayamount() &&  taskLog.getDelaydays() > 0 &&  taskLog.getDelayamount()  > 0){
						taskLog.setBehaviorstatus("delay");
						taskLog.setCreateDate(new Date());
						taskLog.setCreateBy(new User("auto_admin"));
					}else{
						taskLog.setBehaviorstatus("finished");
						taskLog.setCreateDate(new Date());
						taskLog.setCreateBy(new User("auto_admin"));
						//			dunningTaskLog.setCreateBy(UserUtils.getUser());
					}
					/**  任务task修改  */
					TMisDunningTask dunningTask = new TMisDunningTask();
					dunningTask.setId(taskLog.getTaskid());
					dunningTask.setUpdateBy(new User("auto_admin"));
					dunningTask.setDunningtaskstatus(TMisDunningTask.STATUS_FINISHED);
					dunningTasks.add(dunningTask);
					logger.info(taskLog.getDunningcycle() +"队列" +taskLog.getDunningpeoplename() + "催收人员的订单"+taskLog.getDealcode()+"状态为" +taskLog.getBehaviorstatus() + new Date());
				}
				/**  更新完成的任务   */
				tMisDunningTaskDao.batchUpdatePayoffTask(dunningTasks);
				/**  保存完成的任务Log   */
				tMisDunningTaskLogDao.batchInsertTaskLog(dunningTaskLogs);
			}else{
				logger.info("没有需要订单已还款更新任务"+ new Date());
			}
		} catch (Exception e) {
			logger.warn("订单已还款更新任务失败"+ new Date());
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 *  定时自动扫描还款
	 */
	@Scheduled(cron = "0 0/15 * * * ?") //每十五分钟执行一次
	@Transactional(readOnly = false)
	public void autoRepayment(){
		String scheduledBut =  DictUtils.getDictValue("autoRepayment","Scheduled","");
		if(scheduledBut.equals("true")){
			logger.info(MessageFormat.format("自动扫描还款正在運行=========>{0}", new Date()));
			this.newUpdatePayoffTask();
		}
	}
	
	
	
	/**
	 *  新自动分案
	 */
	@Transactional(readOnly = false)
	@Scheduled(cron = "0 18 2 * * ?") 
	public void autoAssign() {
		switch (getDaysOfMonth(new Date())) {
			/**
			 *  小月月分案规则
			 */
			case 30:
				switch (getDays()) {
				case 1:
					/**  Q0,Q1-Q4分案   */
					this.autoAssign_Q1_Q4();
					/**  0-0提醒分案  */
//					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,C0,"> 0");
					return;
				case 16:
					/**  Q0,Q1-Q4分案  */
					this.autoAssign_Q1_Q4();
					/**  0-0提醒分案  */
//					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,C0,"> 0");
					return;
				default:
//					String sqlMap = "NOT BETWEEN "+ this.getCycleDict_Q0().get("begin") + " AND  " + this.getCycleDict_Q0().get("end");
					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,C0,this.getCycleDict_Q0().get("begin"),this.getCycleDict_Q0().get("end"));
					return;
				}
			/**
			 *  大月分案规则		`
			 */
			case 31:
				switch (getDays()) {
				case 1:
					/**  Q0,Q2-Q4分案 */
					this.autoAssign_Q1_Q4();
					/**  0-0提醒分案 */
//					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,C0,"> 0");
					return;
				case 17:
					/**  Q0,Q1-Q4分案 */
					this.autoAssign_Q1_Q4();
					/**  0-0提醒分案	*/
//					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,C0,"> 0");
					return;
				default:
//					String sqlMap = "NOT BETWEEN "+ this.getCycleDict_Q0().get("begin") + " AND  " + this.getCycleDict_Q0().get("end");
					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,C0,this.getCycleDict_Q0().get("begin"),this.getCycleDict_Q0().get("end"));
					return;
				}
			/**
			 *  二月分案规则
			 */
			case 28:
				switch (getDays()) {
				case 1:
					/**  Q0,Q2-Q4分案 */
					this.autoAssign_Q1_Q4();
					return;
				case 14:
					/**  Q0,Q1-Q4分案 */
					this.autoAssign_Q1_Q4();
					return;
				default:
					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,C0,this.getCycleDict_Q0().get("begin"),this.getCycleDict_Q0().get("end"));
					return;
				}
			default:
				return;
		}
	}
	
	
	/**
	 *  Q1-Q4分案 
	 */
	public void autoAssign_Q1_Q4(){
	    /**
	     * 选择催收周期段type
	     */
		String type = getDunningCycleType();
		List<Dict> dicts = DictUtils.getDictList(type);
		/**
		 * 倒叙Q5-Q0
		 */
		ListSortUtil.sort(dicts, "label", "desc");
		
		for(Dict dict : dicts){
			if(!dict.getLabel().equals(P4_P5) && !dict.getLabel().equals(P3_P4)){
				String begin = dict.getValue().split("_")[0];
				String end = dict.getValue().split("_")[1];
				
				if(!("").equals(begin) && !("").equals(end)){
					/**
					 * 逾期分配
					 */
//					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,dict.getLabel(),"NOT BETWEEN "+begin+" AND  "+end);
					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,dict.getLabel(),begin,end);
					
				}else{
					logger.warn(dict.getLabel() + "队列" +dict.getValue() + "周期异常,未分案"+ new Date());
				}
			}else{
				logger.info(dict.getLabel() +"队列" +dict.getValue() + "周期不做分案操作-"+ new Date());
			}
		}
	}
	
	
	/**
	 *  Q1-Q4分案-大月 (1号Q1不分案)
	 */
	@Transactional(readOnly = false)
	public void autoAssign_Q0_W3(){
		/**
	     * 选择催收周期段type
	     */
		String type = getDunningCycleType();
		List<Dict> dicts = DictUtils.getDictList(type);
		for(Dict dict : dicts){
			dict.getValue();
//			if(){
//				
//			}
		}
		
		ListSortUtil.sort(dicts, "label", "desc");
		
		
//		/**
//	     * 获取委外w1队列区间
//	     */
//		String w1 = DictUtils.getDictValue("", "", "");
//		
//		for(Dict dict : dicts){
//			
//			if(w1.equals(dict.getValue())){
//				
//				String begin = dict.getValue().split("_")[0];
//				String end = dict.getValue().split("_")[1];
//				if(!("").equals(begin) && !("").equals(end)){
//					/**
//					 * 逾期分配
//					 */
//					this.autoAssignCycle(TMisDunningTask.STATUS_DUNNING,dict.getLabel(),begin,end);
//				}else{
//					logger.warn(dict.getLabel() + "队列" +dict.getValue() + "周期异常,未分案"+ new Date());
//				}
//				
//			}else{
//				
//				logger.info(dict.getLabel() +"队列" +dict.getValue() + "周期不做分案操作-"+ new Date());
//				
//			}
//			
//		}
	}
	
	public TmpMoveCycle getTmpMoveCycle(String cycle){
		TmpMoveCycle tmpMoveCycle = new TmpMoveCycle();
		try {
//			String Q0 =  DictUtils.getDictValue("Q0", "dunningCycle1", "-1_0");
			if("Q0".equals(cycle)){
				tmpMoveCycle.setDatetimestart(DateUtils.getDate(-1));
				tmpMoveCycle.setDatetimeend(DateUtils.getDate(-1));
				return tmpMoveCycle;
			}else{
				switch (getDaysOfMonth(new Date())) {
				case 30:
					if(getDays() < 16){
						tmpMoveCycle.setDatetimestart(DateUtils.getMonthFirstDayDate());
						tmpMoveCycle.setDatetimeend(DateUtils.getDate(0));
						return tmpMoveCycle;
					}else{
						tmpMoveCycle.setDatetimestart(DateUtils.getDateOfMonth(16));
						tmpMoveCycle.setDatetimeend(DateUtils.getDate(0));
						return tmpMoveCycle;
					}
				case 31:
					if(getDays() < 17){
						tmpMoveCycle.setDatetimestart(DateUtils.getMonthFirstDayDate());
						tmpMoveCycle.setDatetimeend(DateUtils.getDate(0));
						return tmpMoveCycle;
					}else{
						tmpMoveCycle.setDatetimestart(DateUtils.getDateOfMonth(17));
						tmpMoveCycle.setDatetimeend(DateUtils.getDate(0));
						return tmpMoveCycle;
					}
				case 28:
					if(getDays() < 14){
						tmpMoveCycle.setDatetimestart(DateUtils.getMonthFirstDayDate());
						tmpMoveCycle.setDatetimeend(DateUtils.getDate(0));
						return tmpMoveCycle;
					}else{
						tmpMoveCycle.setDatetimestart(DateUtils.getDateOfMonth(14));
						tmpMoveCycle.setDatetimeend(DateUtils.getDate(0));
						return tmpMoveCycle;
					}
				default:
					tmpMoveCycle.setDatetimestart(DateUtils.getDate(-1));
					tmpMoveCycle.setDatetimeend(DateUtils.getDate(-1));
					return tmpMoveCycle;
				}
			}
		} catch (Exception e) {
			logger.warn("tmpMoveCycle返回失败默认赋值昨天日期"+ new Date());
			logger.error("错误信息"+e.getMessage());
			tmpMoveCycle.setDatetimestart(DateUtils.getDate(-1));
			tmpMoveCycle.setDatetimeend(DateUtils.getDate(-1));
			return tmpMoveCycle;
		}
	}
	
	/**
	 * 过期自动分案
	 */
	@Transactional(readOnly = false)
	public void autoAssignCycle(String dunningtaskstatus, String dunningcycle,String begin,String end ) {
		try {
	//		List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleDao.findPeopleByDunningcycle(C0);
	// 		 ==========================================提醒队列逾期分配begin ==========================================
			/** 周期中的过期任务读取出日志  */
			List<TMisDunningTaskLog>  outDunningTaskLogs = tMisDunningTaskDao.newfindDelayTaskByDunningcycle(dunningtaskstatus,dunningcycle,begin,end);
			logger.info("newfindDelayTaskByDunningcycle-查询"+dunningcycle+"队列过期任务数" +outDunningTaskLogs.size()  + "条-"  + new Date());
			if(!outDunningTaskLogs.isEmpty()){
		
				Map<String, TMisDunningTaskLog> inDunningTaskLogsMap = new HashMap<String, TMisDunningTaskLog>();
				/** 需要修改的任务 */
				Map<String, List<TMisDunningTask>> mapCycleTaskNum = new HashMap<String, List<TMisDunningTask>>();
	
				for(TMisDunningTaskLog dunningTaskLog : outDunningTaskLogs){
					/**
					 * log 催收周期过期移出记录
					 */
					dunningTaskLog.setBehaviorstatus("out");
					dunningTaskLog.setCreateDate(new Date());
					dunningTaskLog.setCreateBy(new User("auto_admin"));
		//			dunningTaskLog.setCreateBy(UserUtils.getUser());
					/**
					 * 本次迁徙该移入的周期段
					 */
					Dict dict = this.getCycleDict2(dunningTaskLog.getOverduedays());
					if(null == dict){
						dunningTaskLog.setBehaviorstatus("out_error");
						logger.warn("行为状态out_error：逾期"+dunningTaskLog.getOverduedays() +"天，无法对应周期队列，dealcode:" + dunningTaskLog.getDealcode() + "任务taskID:" + dunningTaskLog.getTaskid()+"不做分配");
						continue;
					}
					/**
					 * 任务task修改
					 */
					TMisDunningTask dunningTask = new TMisDunningTask();
					dunningTask.setId(dunningTaskLog.getTaskid());
					dunningTask.setDunningcycle(dict.getLabel());
					dunningTask.setDunningperiodbegin(Integer.parseInt(dict.getValue().split("_")[0]));
					dunningTask.setDunningperiodend(Integer.parseInt(dict.getValue().split("_")[1]));
					dunningTask.setUpdateBy(new User("auto_admin"));
					dunningTask.setCapitalamount(dunningTaskLog.getCorpusamount());
					/**
					 * 每个周期的任务集合
					 */
					if (mapCycleTaskNum.containsKey(dict.getLabel())) {
						mapCycleTaskNum.get(dict.getLabel()).add(dunningTask);
					} else {
						List<TMisDunningTask> mapTasks = new ArrayList<TMisDunningTask>();
						mapTasks.add(dunningTask);
						mapCycleTaskNum.put(dict.getLabel(), mapTasks);
					}
					inDunningTaskLogsMap.put(dunningTaskLog.getTaskid(), dunningTaskLog);
				}
				/** 
				 * 保存移出任务Log
				 */
				tMisDunningTaskLogDao.batchInsertTaskLog(outDunningTaskLogs);
				
				/**  移入的任务Log集合   */
				List<TMisDunningTaskLog> inDunningTaskLogs = new ArrayList<TMisDunningTaskLog>();
				/**
				 * 循环队列的任务集合
				 */
				for (Map.Entry<String, List<TMisDunningTask>> entry : mapCycleTaskNum.entrySet()) {
					/**
					 * 根据队列找出催收人员集合
					 */
//					List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleDao.findPeopleByDunningcycle(entry.getKey());
					
					/**
					 * 根据周期查询催收人员按金额排序
					 */
					TmpMoveCycle tmpMoveCycle = this.getTmpMoveCycle(entry.getKey());
					List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleDao.findPeopleSumcorpusamountByDunningcycle(entry.getKey(),tmpMoveCycle.getDatetimestart(),tmpMoveCycle.getDatetimeend());
					
					/**
					 *  平均分配队列集合的催收人员
					 */
					List<TMisDunningTask> tasks = entry.getValue();
//					int j = 0;
					for(int i= 0 ; i < tasks.size() ; i++ ){  
						TMisDunningTask dunningTask = (TMisDunningTask)tasks.get(i);
						/**  平均分配法    */
						int j = i % dunningPeoples.size();                            			 // 平均分配法
						
						/**  蛇形分配法    */
//						if (i / dunningPeoples.size() % 2 == 0) {
//							j = i % dunningPeoples.size();
//						} else {
//							j = dunningPeoples.size() - 1 - i % dunningPeoples.size();
//						}
						System.out.println("姓名"+dunningPeoples.get(j).getName()+ "-周期总金额" + dunningPeoples.get(j).getSumcorpusamount()+"-分配金额"+dunningTask.getCapitalamount());
						
						
						/**  任务催收人员添加    */
						dunningTask.setDunningpeopleid(dunningPeoples.get(j).getId());
						dunningTask.setDunningpeoplename(dunningPeoples.get(j).getName());
		//				dunningTask.setDunningtaskstatus(dunningtaskstatus);
						/**  任务log 催收人员添加    */
						if(inDunningTaskLogsMap.containsKey(dunningTask.getId())){
							inDunningTaskLogsMap.get(dunningTask.getId()).setBehaviorstatus("in");
							inDunningTaskLogsMap.get(dunningTask.getId()).setDunningpeopleid(dunningTask.getDunningpeopleid());
							inDunningTaskLogsMap.get(dunningTask.getId()).setDunningpeoplename(dunningTask.getDunningpeoplename());
							inDunningTaskLogsMap.get(dunningTask.getId()).setDunningcycle(dunningTask.getDunningcycle());
						}else{
							inDunningTaskLogsMap.put(dunningTask.getId(), 
									new TMisDunningTaskLog(dunningTask.getDealcode(), 
											dunningTask.getDunningpeopleid(),
											dunningTask.getDunningpeoplename(),
											dunningTask.getDunningcycle(),
											"in_warn"));
							logger.warn("行为状态in_warn：任务taskID:" +dunningTask.getId() + "移入" + dunningTask.getDunningcycle() + "队列" +dunningTask.getDunningpeoplename() +"数据缺失" );
		//					continue;
						}
						
						inDunningTaskLogs.add(inDunningTaskLogsMap.get(dunningTask.getId()));
					}
					/**
					 * 批量更新每个队列的任务集合
					 */
					tMisDunningTaskDao.batchUpdateExpiredTask(tasks);
				}
				/** 
				 * 保存移入任务Log
				 */
				tMisDunningTaskLogDao.batchInsertTaskLog(inDunningTaskLogs);
	//		=====================================提醒队列逾期分配end===================================================
			}else{
				logger.info(dunningcycle + "队列没有过期任务！" + new Date());
			}
		} catch (Exception e) {
			logger.error(dunningcycle + "队列分配任务失败,全部事务回滚");
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		} finally {
			logger.info(dunningcycle + "队列任务结束" + new Date());
		}
		
	}
	
	/**
	 *  新增未生成催收任务(task)的订单
	 */
	@Transactional(readOnly = false)
	@Scheduled(cron = "0 22 2 * * ?") 
	public void autoAssignNewOrder() {
		try {
			/**
			 * 根据逾期天数查询未生成任务task的订单
			 */
			String begin_Q0 = this.getCycleDict_Q0().get("begin");
			logger.info("newfingDelayOrderByNotTask_day:"+begin_Q0  + new Date());
			List<TMisDunningTaskLog>  newDunningTaskLogs = tMisDunningTaskDao.newfingDelayOrderByNotTask(begin_Q0);
			logger.info("newfingDelayOrderByNotTask-查询新的逾期周期订单并生成任务" +newDunningTaskLogs.size()  + "条-"  + new Date());
//			Map<String, List<TMisDunningPeople>> cyclePeoplemMap = this.getDunningcyclePeopleLists();
			
			if(!newDunningTaskLogs.isEmpty()){
				Map<String, List<TMisDunningTask>> mapCycleTaskNum = new HashMap<String, List<TMisDunningTask>>();
				Map<String, TMisDunningTaskLog> inDunningTaskLogsMap = new HashMap<String, TMisDunningTaskLog>();
				
				/**  * auto Q0 队列 begin */
				String autoQ0 = DictUtils.getDictValue("autoQ0","Scheduled","false");
				String autoQ0Tag = DictUtils.getDictValue("autoQ0Tag","Scheduled","false");
				Map<String, String> atuoQ0DealcodeMap = new HashMap<String, String>();
				List<TMisDunningTask> atuoDunningTasks = new ArrayList<TMisDunningTask>();
				List<TMisDunningTaskLog> atuoQ0DunningTaskLogs = new ArrayList<TMisDunningTaskLog>();
				if(autoQ0.equals("true") || autoQ0Tag.equals("true")){
					List<String> atuoQ0Dealcodes = tMisDunningTaskDao.findAtuoQ0Dealcode(begin_Q0,"1");
					logger.info("findAtuoQ0Dealcode-autoQ0查询历史借款逾期小于1天的用户订单" +atuoQ0Dealcodes.size()  + "条"  + new Date());
					for(String atuoQ0Dealcode : atuoQ0Dealcodes){
						atuoQ0DealcodeMap.put(atuoQ0Dealcode, atuoQ0Dealcode);
					}
				}
				/** * auto Q0 队列 end  */
				
				for(TMisDunningTaskLog dunningTaskLog : newDunningTaskLogs){
					/**
					 * 本次迁徙该移入的周期段
					 */
					Dict dict = this.getCycleDict2(dunningTaskLog.getOverduedays());
					if(null == dict){
						logger.warn("行为状态out_error：逾期"+dunningTaskLog.getOverduedays() +"天，无法对应周期队列，dealcode:" + dunningTaskLog.getDealcode() + "任务taskID:" + dunningTaskLog.getTaskid()+"不做分配");
						continue;
					}
					logger.info("逾期天数:"+dunningTaskLog.getOverduedays()+ "对应队列："+ dict.getLabel()  + new Date());
					
					/**  * auto Q0 队列 begin  */
					if(autoQ0.equals("true") && C0.equals(dict.getLabel()) && atuoQ0DealcodeMap.containsKey(dunningTaskLog.getDealcode())){
//						logger.info("autoQ0查询历史借款逾期小于1天的用户订单"  + new Date());
						TMisDunningTask autoDunningTask = this.autoCreateNewDunningTask(dunningTaskLog, dict);
						TMisDunningTaskLog autoDunningTaskLog = this.autoCreateNewDunningTaskLog(dunningTaskLog, autoDunningTask);
						atuoDunningTasks.add(autoDunningTask);
						atuoQ0DunningTaskLogs.add(autoDunningTaskLog);
						continue;
					}
					/** * auto Q0 队列 end  */
					
					TMisDunningTask dunningTask = null;
					
					/**  * auto Q0 优质用户标记  begin  */
					if(autoQ0Tag.equals("true") && C0.equals(dict.getLabel()) && atuoQ0DealcodeMap.containsKey(dunningTaskLog.getDealcode())){
						/**
						 * 创建任务 (标示优质用户)
						 */
						dunningTask = this.createNewDunningTask(dunningTaskLog,dict,QUALITY_GOOD);
						/**  * auto Q0 优质用户标记  end  */
					} else {
						/**
						 * 创建任务  (标示普通用户)
						 */
						dunningTask = this.createNewDunningTask(dunningTaskLog,dict,QUALITY_ORDINARY);
					}
					/**
					 * 每个周期的任务集合
					 */
					if (mapCycleTaskNum.containsKey(dict.getLabel())) {
						mapCycleTaskNum.get(dict.getLabel()).add(dunningTask);
					} else {
						List<TMisDunningTask> mapTasks = new ArrayList<TMisDunningTask>();
						mapTasks.add(dunningTask);
						mapCycleTaskNum.put(dict.getLabel(), mapTasks);
					}
					inDunningTaskLogsMap.put(dunningTask.getId(), dunningTaskLog);
				}
				
				/**  * auto Q0 队列 begin  */
				if(autoQ0.equals("true") && !atuoDunningTasks.isEmpty() && !atuoQ0DunningTaskLogs.isEmpty()){
					tMisDunningTaskDao.batchinsertTask(atuoDunningTasks);
					logger.info("保存autoQ0任务" + atuoDunningTasks.size()  + "条"  + new Date());
					tMisDunningTaskLogDao.batchInsertTaskLog(atuoQ0DunningTaskLogs);
					logger.info("保存autoQ0任务log" + atuoQ0DunningTaskLogs.size()  + "条"  + new Date());
				}
				/** * auto Q0 队列 end  */
				
				/**  新增任务Log集合   */
				List<TMisDunningTaskLog> inDunningTaskLogs = new ArrayList<TMisDunningTaskLog>();
				/** 
				 * 循环队列任务
				 */
				for (Map.Entry<String, List<TMisDunningTask>> entry : mapCycleTaskNum.entrySet()) {
					/**
					 * 根据队列找出催收人员集合
					 */
//					List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleDao.findPeopleByDunningcycle(entry.getKey().toString());
					/**
					 * 根据周期查询催收人员按金额排序
					 */
					TmpMoveCycle tmpMoveCycle = this.getTmpMoveCycle(entry.getKey());
					List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleDao.findPeopleSumcorpusamountByDunningcycle(entry.getKey(),tmpMoveCycle.getDatetimestart(),tmpMoveCycle.getDatetimeend());
					
					/**
					 * 平均分配队列集合的催收人员
					 */
					List<TMisDunningTask> tasks = entry.getValue();
					logger.info("共"+ mapCycleTaskNum.entrySet().size()+"个队列，正在分配"+entry.getKey().toString()+"队列"+tasks.size()+"条，此队列有"+dunningPeoples.size()+"个催收员" + new Date());
					
//					int j = 0;
					for(int i= 0 ; i < tasks.size() ; i++ ){  
						TMisDunningTask dunningTask = (TMisDunningTask)tasks.get(i);
						int j = i % dunningPeoples.size();
						/**  蛇形分配法    */
//						if (i / dunningPeoples.size() % 2 == 0) {
//							j = i % dunningPeoples.size();
//						} else {
//							j = dunningPeoples.size() - 1 - i % dunningPeoples.size();
//						}
						System.out.println("姓名"+dunningPeoples.get(j).getName()+ "-周期总金额" + dunningPeoples.get(j).getSumcorpusamount()+"-分配金额"+dunningTask.getCapitalamount());
						
						/**  任务催收人员添加    */
						dunningTask.setDunningpeopleid(dunningPeoples.get(j).getId());
						dunningTask.setDunningpeoplename(dunningPeoples.get(j).getName());
						/**  任务log 催收人员添加    */
						if(inDunningTaskLogsMap.containsKey(dunningTask.getId())){
							inDunningTaskLogsMap.get(dunningTask.getId()).setTaskid(dunningTask.getId());
							inDunningTaskLogsMap.get(dunningTask.getId()).setBehaviorstatus("in");
							inDunningTaskLogsMap.get(dunningTask.getId()).setDunningpeopleid(dunningTask.getDunningpeopleid());
							inDunningTaskLogsMap.get(dunningTask.getId()).setDunningpeoplename(dunningTask.getDunningpeoplename());
							inDunningTaskLogsMap.get(dunningTask.getId()).setDunningcycle(dunningTask.getDunningcycle());
							inDunningTaskLogsMap.get(dunningTask.getId()).setCreateDate(new Date());
							inDunningTaskLogsMap.get(dunningTask.getId()).setCreateBy(new User("auto_admin"));
						}else{
							inDunningTaskLogsMap.put(dunningTask.getId(), 
									new TMisDunningTaskLog(dunningTask.getDealcode(), 
											dunningTask.getDunningpeopleid(),
											dunningTask.getDunningpeoplename(),
											dunningTask.getDunningcycle(),
											"in_warn"));
							logger.warn("行为状态in_warn：任务taskID:" +dunningTask.getId() + "移入" + dunningTask.getDunningcycle() + "队列" +dunningTask.getDunningpeoplename() +"数据缺失" );
						}
						inDunningTaskLogs.add(inDunningTaskLogsMap.get(dunningTask.getId()));
					}
					/**  批量保存每个队列的任务集合    */
					tMisDunningTaskDao.batchinsertTask(tasks);
					logger.info("分配"+entry.getKey().toString()+"队列"+tasks.size()+"条，平均分配成功" + new Date());
				}
				/** 
				 * 保存移入任务Log
				 */
				tMisDunningTaskLogDao.batchInsertTaskLog(inDunningTaskLogs);
				logger.info("任务日志记录完毕" + new Date());
			}else{
				logger.info("newfingDelayOrderByNotTask-没有新的逾期周期订单任务" + new Date());
			}
		} catch (Exception e) {
			logger.error("新增未生成催收任务(task)的订单失败,全部事务回滚");
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		} finally {
			logger.info("新增未生成催收任务(task)的订单任务结束" + new Date());
		}
	}
	
	/**
	 * 创建autoQ0任务
	 * @return TMisDunningTask
	 */
	private TMisDunningTask autoCreateNewDunningTask(TMisDunningTaskLog autoTaskLog,Dict dict) throws Exception{
		Date now = new Date();
		TMisDunningTask autoTask = new TMisDunningTask();
		autoTask.setId(IdGen.uuid());
		autoTask.setDunningpeopleid(AUTOQ0_ID_1);
		autoTask.setDunningpeoplename(AUTOQ0_NAME_1);
		autoTask.setDealcode(autoTaskLog.getDealcode());
		autoTask.setCapitalamount(autoTaskLog.getCorpusamount());
		autoTask.setBegin(toDate(now));
		autoTask.setEnd(null);
		autoTask.setDunningcycle(dict.getLabel());
		int min = !("").equals(dict.getValue().split("_")[0]) ?  Integer.parseInt(dict.getValue().split("_")[0]) : 0 ;
		int max = !("").equals(dict.getValue().split("_")[1]) ?  Integer.parseInt(dict.getValue().split("_")[1]) : 0 ;
		autoTask.setDunningperiodbegin(min);
		autoTask.setDunningperiodend(max);
		autoTask.setDunnedamount(0);
		autoTask.setIspayoff(false);
		autoTask.setReliefamount(0);
		autoTask.setDunningtaskstatus(TMisDunningTask.STATUS_DUNNING);
		autoTask.setRepaymentTime(new java.sql.Date(autoTaskLog.getRepaymenttime().getTime()));
		autoTask.setCreateBy(new User("auto_admin"));
		autoTask.setCreateDate(new Date());
		return autoTask;
	}
	
	/**
	 * 创建autoQ0任务log
	 * @return TMisDunningTasklog
	 */
	private TMisDunningTaskLog autoCreateNewDunningTaskLog(TMisDunningTaskLog autoTaskLog,TMisDunningTask autoDunningTask) throws Exception{
		autoTaskLog.setTaskid(autoDunningTask.getId());
		autoTaskLog.setBehaviorstatus("in");
		autoTaskLog.setDunningpeopleid(autoDunningTask.getDunningpeopleid());
		autoTaskLog.setDunningpeoplename(autoDunningTask.getDunningpeoplename());
		autoTaskLog.setDunningcycle(autoDunningTask.getDunningcycle());
		autoTaskLog.setCreateDate(new Date());
		autoTaskLog.setCreateBy(new User("auto_admin"));
		return autoTaskLog;
	}
	
	/**
	 * 根据逾期天数，返回该月该日的归属队列
	 * @param current
	 * @return
	 */
//	public Dict getCycleDict(int current){
//		Dict cycleDict = null;
//		/**  选择催收周期段类型   */
//		String type = getDunningCycleType();
//		List<Dict> dicts = DictUtils.getDictList(type);
//		int cyclemax = 0;
//		for(Dict dict : dicts){
//			if(("dunningCycle2").equals(type) && dict.getLabel().equals(C_P1)){
//				continue;
//			}
//			int min = !("").equals(dict.getValue().split("_")[0]) ?  Integer.parseInt(dict.getValue().split("_")[0]) : 0 ;
//			int max = !("").equals(dict.getValue().split("_")[1]) ?  Integer.parseInt(dict.getValue().split("_")[1]) : 0 ;
//			if(rangeInDefined(current, min, max)){
//				cycleDict = dict;
//			}
//			if(max > cyclemax){
//				cyclemax = max;
//			}
//		}
//		/**  逾期天数大于全部周期段时放入Q5    */
//		if(null == cycleDict && current > cyclemax){
//			String val = DictUtils.getDictValue("Q5", type, "47_61");
//			cycleDict = new Dict();
//			cycleDict.setType(type);
//			cycleDict.setValue(val);
//			cycleDict.setLabel("Q5");
//		}
//		return cycleDict;
//	}
	
	/**
	 * 查询Q0队列的周期区间
	 * @return
	 */
	public Map<String, String> getCycleDict_Q0(){
		Map<String, String> map = new HashMap<String, String>();
		String value =  DictUtils.getDictValue("Q0", "dunningCycle1", "-1_0");
		String begin = value.split("_")[0];
		String end = value.split("_")[1];
		map.put("begin", begin);
		map.put("end", end);
		return map;
	}
	
	/**
	 * 是否在此区间
	 * @param current
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean rangeInDefined(int current, int min, int max)  
    {  
        return Math.max(min, current) == Math.min(current, max);  
    }  
	
    /**
     * 选择催收周期段类型
     * @return
     */
	public static String getDunningCycleType() {
		switch (getDaysOfMonth(new Date())) {
		case 30:
			switch (getDays()) {
			case 1:
				return "dunningCycle1";
			case 16:
				return "dunningCycle1";
			default:
				return "dunningCycle1";
			}
		case 31:
			switch (getDays()) {
			case 1:
				return "dunningCycle2";
			case 17:
				return "dunningCycle1";
			default:
				return "dunningCycle1";
			}
		case 28:
			switch (getDays()) {
			case 1:
				return "dunningCycle3";
			case 14:
				return "dunningCycle1";
			default:
				return "dunningCycle1";
			}
		default:
			return "";
			
		}
	}  
	
	/**
	 * 根据逾期天数，返回该月该日的归属队列
	 * @param current
	 * @return
	 */
	public Dict getCycleDict2(int current){
		Dict cycleDict = null;
		/**  选择催收周期段类型   */
		String type = getDunningCycleType();
		List<Dict> dicts = DictUtils.getDictList(type);
		int cyclemax = 0;
		for(Dict dict : dicts){
			if(("dunningCycle2").equals(type) && dict.getLabel().equals(C_P1)){
				continue;
			}		
			int min = !("").equals(dict.getValue().split("_")[0]) ?  Integer.parseInt(dict.getValue().split("_")[0]) : 0 ;
			int max = !("").equals(dict.getValue().split("_")[1]) ?  Integer.parseInt(dict.getValue().split("_")[1]) : 0 ;
			
			int day = 0;

			if(("dunningCycle1").equals(type) && !dict.getLabel().equals(C0)){
				switch (getDaysOfMonth(new Date())) {
				case 30:
					day = getDays() % 15 == 0 ? 15 - 1 : getDays() % 15 - 1;
					break;
				case 31:
					day = (getDays()-1) % 15 == 0 ? 15 - 1 : (getDays()-1) % 15 - 1;
					break;
				case 28:
					day = (getDays()+2) % 15 == 0 ? 15 - 1 : (getDays()+2) % 15 - 1;
					break;
				default:
					break;
				}
				if(("dunningCycle1").equals(type) && dict.getLabel().equals(C_P1)){
					max += day;
				}else{
					min += day;
					max += day;
				}
			}
			if(rangeInDefined(current, min, max)){
				cycleDict = dict;
			}
			if(max > cyclemax){
				cyclemax = max;
			}
		}
		/**  逾期天数大于全部周期段时放入Q5    */
		if(null == cycleDict && current > cyclemax){
			String val = DictUtils.getDictValue("Q5", type, "48_62");
			cycleDict = new Dict();
			cycleDict.setType(type);
			cycleDict.setValue(val);
			cycleDict.setLabel("Q5");
		}
		return cycleDict;
	}
	
	/**
	 * 创建任务
	 * @param people
	 * @param order
	 * @param period
	 * @return
	 */
	private TMisDunningTask createNewDunningTask(TMisDunningTaskLog taskLog,Dict dict,String quality) throws Exception{
		Date now = new Date();
		TMisDunningTask task = new TMisDunningTask();
		task.setId(IdGen.uuid());
//		task.setDunningpeopleid(taskLog.getDunningpeopleid());
//		task.setDunningpeoplename(taskLog.getDunningpeoplename());
		task.setDealcode(taskLog.getDealcode());
		task.setCapitalamount(taskLog.getCorpusamount());
		task.setBegin(toDate(now));
		task.setEnd(null);
		task.setDunningcycle(dict.getLabel());
		int min = !("").equals(dict.getValue().split("_")[0]) ?  Integer.parseInt(dict.getValue().split("_")[0]) : 0 ;
		int max = !("").equals(dict.getValue().split("_")[1]) ?  Integer.parseInt(dict.getValue().split("_")[1]) : 0 ;
		task.setDunningperiodbegin(min);
		task.setDunningperiodend(max);
		task.setDunnedamount(0);
		task.setIspayoff(false);
		task.setReliefamount(0);
		task.setDunningtaskstatus(TMisDunningTask.STATUS_DUNNING);
		task.setRepaymentTime(new java.sql.Date(taskLog.getRepaymenttime().getTime()));
		task.setCreateBy(new User("auto_admin"));
		task.setCreateDate(new Date());
		task.setQuality(quality);
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(toDate(order.repaymentDate));
//		calendar.add(Calendar.DAY_OF_YEAR, period.end + 1);
//		Date deadline = toDate(calendar.getTime());
//		task.setDeadline(deadline);
//		task.setBeforeTask(beforTaskId);
//		task.setBeforeDunningPeople(beforePeopleId);
//		this.tMisDunningTaskDao.insert(task);
		return task;
	}
	
	/**
	 * 判断月份天数
	 * @param date
	 * @return
	 */
    public static int getDaysOfMonth(Date date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
    }  
   
    /**
	 * 返回今天号
	 * @param date
	 * @return
	 */
    public static int getDays() {  
    	Calendar c = Calendar.getInstance();
    	int datenum = c.get(Calendar.DATE);
		return datenum;
    }  
    
    /**
     * 手动分案
     * @param dealcode
     * @param peopleId
     */
    @Transactional(readOnly = false)
    public String assign(List<String> dealcodes,String dunningcycle,List<String> newdunningpeopleids ){ 	
    	try {
			/**  查询手动分配订单任务Log   */
			List<TMisDunningTaskLog>  assignDunningTaskLogs = tMisDunningTaskDao.newfingTasksByDealcodes(dealcodes,dunningcycle);
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleDao.findPeoplesByids(newdunningpeopleids,dunningcycle);
			logger.info("newfingTasksByDealcodes-查询手动分配订单任务Log " +assignDunningTaskLogs.size()  + "条-"  + new Date());
			
			List<TMisDunningTask> tasks = new ArrayList<TMisDunningTask>();
			List<TMisDunningTaskLog> inDunningTaskLogs = new ArrayList<TMisDunningTaskLog>();
			List<TMisDunningTaskLog> outDunningTaskLogs = new ArrayList<TMisDunningTaskLog>();
			
			if(!assignDunningTaskLogs.isEmpty()){
//				for(TMisDunningTaskLog dunningTaskLog : outDunningTaskLogs){
				for(int i= 0 ; i < assignDunningTaskLogs.size() ; i++ ){  
					TMisDunningTaskLog outdunningTaskLog = (TMisDunningTaskLog)assignDunningTaskLogs.get(i);
					outdunningTaskLog.setBehaviorstatus("out");
					outdunningTaskLog.setCreateDate(new Date());
					outdunningTaskLog.setCreateBy(UserUtils.getUser());
					outDunningTaskLogs.add(outdunningTaskLog);
				}
				/** 
				 * 保存移出任务Log
				 */
				tMisDunningTaskLogDao.batchInsertTaskLog(outDunningTaskLogs);
				
				for(int i= 0 ; i < assignDunningTaskLogs.size() ; i++ ){  
					TMisDunningTaskLog indunningTaskLog = (TMisDunningTaskLog)assignDunningTaskLogs.get(i);
					/**
					 * 任务task修改
					 */
					int j = i % dunningPeoples.size();  
					TMisDunningTask dunningTask = new TMisDunningTask();
					dunningTask.setId(indunningTaskLog.getTaskid());
					dunningTask.setDunningpeopleid(dunningPeoples.get(j).getId());
					dunningTask.setDunningpeoplename(dunningPeoples.get(j).getName());
					dunningTask.setUpdateBy(UserUtils.getUser());
					dunningTask.setDunningcycle(dunningcycle);
					tasks.add(dunningTask);
					/**
					 * log 催收周期过期移入记录
					 */
					indunningTaskLog.setBehaviorstatus("in");
					indunningTaskLog.setDunningpeopleid(dunningTask.getDunningpeopleid());
					indunningTaskLog.setDunningpeoplename(dunningTask.getDunningpeoplename());
					inDunningTaskLogs.add(indunningTaskLog);
				}	
				/** 
				 * 修改手动分配任务
				 */
				tMisDunningTaskDao.batchUpdateDistributionTask(tasks);
				/** 
				 * 保存移入任务Log
				 */
				tMisDunningTaskLogDao.batchInsertTaskLog(inDunningTaskLogs);

			}else{
				logger.info(dunningcycle + "队列没有手动分配任务！" + new Date());
			}
			return "实际均分未还款订单" + assignDunningTaskLogs.size() + "条";
		} catch (Exception e) {
			logger.error(dunningcycle + "队列手动分配任务失败,全部事务回滚");
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		} finally {
			logger.info(dunningcycle + "队列手动分配任务结束" + new Date());
		}
    	
    }
    
    /**
     * 委外手动分案
     * @param dealcode
     * @param peopleId
     */
    @Transactional(readOnly = false)
    public String outAssign(List<String> dealcodes,String dunningcycle,List<String> newdunningpeopleids ,Date outsourcingenddate){ 	
    	try {
			/**  查询手动分配订单任务Log   */
			List<TMisDunningTaskLog>  assignDunningTaskLogs = tMisDunningTaskDao.newfingTasksByDealcodes(dealcodes,dunningcycle);
			List<TMisDunningPeople> dunningPeoples = tMisDunningPeopleDao.findPeoplesByids(newdunningpeopleids,dunningcycle);
			logger.info("newfingTasksByDealcodes-查询手动分配订单任务Log " +assignDunningTaskLogs.size()  + "条-"  + new Date());
			
			List<TMisDunningTask> tasks = new ArrayList<TMisDunningTask>();
			List<TMisDunningTaskLog> inDunningTaskLogs = new ArrayList<TMisDunningTaskLog>();
			List<TMisDunningTaskLog> outDunningTaskLogs = new ArrayList<TMisDunningTaskLog>();
			
			if(!assignDunningTaskLogs.isEmpty()){
				for(int i= 0 ; i < assignDunningTaskLogs.size() ; i++ ){  
					TMisDunningTaskLog outdunningTaskLog = (TMisDunningTaskLog)assignDunningTaskLogs.get(i);
					outdunningTaskLog.setBehaviorstatus("out");
					outdunningTaskLog.setCreateDate(new Date());
					outdunningTaskLog.setCreateBy(UserUtils.getUser());
					outDunningTaskLogs.add(outdunningTaskLog);
				}
				/** 
				 * 保存移出任务Log
				 */
				tMisDunningTaskLogDao.batchInsertTaskLog(outDunningTaskLogs);
				
				for(int i= 0 ; i < assignDunningTaskLogs.size() ; i++ ){  
					TMisDunningTaskLog indunningTaskLog = (TMisDunningTaskLog)assignDunningTaskLogs.get(i);
					/**
					 * 任务task修改
					 */
					int j = i % dunningPeoples.size();  
					TMisDunningTask dunningTask = new TMisDunningTask();
					dunningTask.setId(indunningTaskLog.getTaskid());
					dunningTask.setDunningpeopleid(dunningPeoples.get(j).getId());
					dunningTask.setDunningpeoplename(dunningPeoples.get(j).getName());
					dunningTask.setUpdateBy(UserUtils.getUser());
					dunningTask.setDunningcycle(dunningcycle);
					dunningTask.setOutsourcingbegindate(new Date());
					dunningTask.setOutsourcingenddate(outsourcingenddate);
					tasks.add(dunningTask);
					/**
					 * log 催收周期过期移入记录
					 */
					indunningTaskLog.setBehaviorstatus("in");
					indunningTaskLog.setDunningpeopleid(dunningTask.getDunningpeopleid());
					indunningTaskLog.setDunningpeoplename(dunningTask.getDunningpeoplename());
					inDunningTaskLogs.add(indunningTaskLog);
				}	
				/** 
				 * 修改手动分配任务
				 */
				tMisDunningTaskDao.batchUpdateOutDistributionTask(tasks);
				/** 
				 * 保存移入任务Log
				 */
				tMisDunningTaskLogDao.batchInsertTaskLog(inDunningTaskLogs);

			}else{
				logger.info(dunningcycle + "队列没有手动分配任务！" + new Date());
			}
			return "实际均分未还款订单" + assignDunningTaskLogs.size() + "条";
		} catch (Exception e) {
			logger.error(dunningcycle + "队列手动分配任务失败,全部事务回滚");
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		} finally {
			logger.info(dunningcycle + "队列手动分配任务结束" + new Date());
		}
    	
    }

	/**
	 * 委外手动留案
	 */
	@Transactional(readOnly = false)
	public String outExtensionAssign(List<String> dealcodes, String dunningcycle, Date outsourcingenddate){
		try {
			/**  查询task判断是否已经分案   */
			List<TMisDunningTask>  assignDunningTask = tMisDunningTaskDao.findDunningTaskByDealcodeOfList(dealcodes);
			List<TMisDunningTask> tasks = new ArrayList<TMisDunningTask>();

			if(!assignDunningTask.isEmpty()){

				for(int i= 0 ; i < assignDunningTask.size() ; i++ ){
					TMisDunningTask indunningTask = (TMisDunningTask)assignDunningTask.get(i);
					if (indunningTask.getOutsourcingbegindate() == null){
						throw new RuntimeException("有未成功分案的案子，无法手工留案");
					}
					/**
					 * 任务task修改
					 */
					TMisDunningTask dunningTask = new TMisDunningTask();
					dunningTask.setId(indunningTask.getId());
					dunningTask.setUpdateBy(UserUtils.getUser());
					dunningTask.setOutsourcingenddate(outsourcingenddate);
					dunningTask.setExtensionDate(outsourcingenddate);
					tasks.add(dunningTask);

				}
				/**
				 * 修改手动修改任务
				 */
				tMisDunningTaskDao.batchUpdateOutExtensionTask(tasks);

			}else{
				logger.info("没有手动留案任务！" + new Date());
			}
			return "实际留案未还款订单" + assignDunningTask.size() + "条";
		} catch (RuntimeException e){
			logger.error("选中的案子中有未成功分案的案子，无法手工留案");
			logger.error("手动留案任务失败,全部事务回滚");
			return "选中的案子中有未成功分案的案子，手工留案操作失败";
		} catch (Exception e) {
			logger.error("手动留案任务失败,全部事务回滚");
			logger.error("错误信息"+e.getMessage());
			throw new ServiceException(e);
		} finally {
			logger.info("手动留案任务结束" + new Date());
		}

	}

    
//	/**
//	 * 根据逾期天数，返回该月该日的归属队列
//	 * @param current
//	 * @return
//	 */
//	public Map<String, String> getCycleMap(int current){
//		Map<String, String> cycleMap = new HashMap<String, String>();
//		String type = getDunningCycleType();
//		List<Dict> dicts = DictUtils.getDictList(type);
//		int cyclemax = 0;
//		for(Dict dict : dicts){
//			int min = !("").equals(dict.getValue().split("_")[0]) ?  Integer.parseInt(dict.getValue().split("_")[0]) : 0 ;
//			int max = !("").equals(dict.getValue().split("_")[1]) ?  Integer.parseInt(dict.getValue().split("_")[1]) : 0 ;
//			if(rangeInDefined(current, min, max)){
//				cycleMap.put(dict.getValue(), dict.getLabel());
//			}
//			if(max > cyclemax){
//				cyclemax = max;
//			}
//		}
//		if(cycleMap.isEmpty() && current > cyclemax){
//			String val = DictUtils.getDictValue("Q5", type, "Q5");
//			cycleMap.put(val, "Q5");
//		}
//		return cycleMap;
//	}
    
   
	/**
	 * 检索已经过期的催款任务，设置状态为到期
	 */
//	public boolean newUpdateExpiredTask(){
//		try {
//			List<TMisDunningTask> dunningTasks = tMisDunningTaskDao.newfindExpiredTask();
//			for (TMisDunningTask task : dunningTasks) {
//		//		Date now = new Date();
//		//		task.setEnd(now);
//				task.setDunningtaskstatus(TMisDunningTask.STATUS_EXPIRED);
//				task.setDunningAmounOnEnd(task.getCreditamountOnEnd());
//		//		this.save(task);
//				logger.info("更新过期任务状态,taskID：" + task.getId()+ "订单号:" + task.getDealcode() + "催收人员ID:" + task.getDunningpeopleid());
//			}
//			tMisDunningTaskDao.batchUpdateExpiredTask(dunningTasks);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
    
    
	
	public static void main(String[] args) {
//		String type = getDunningCycleType();
//		List<Dict> dicts = DictUtils.getDictList(type);
//		List<Dict> list = new ArrayList<Dict>();
//		Dict dict1 = new Dict();
//		dict1.setLabel("Q1");
//		Dict dict2 = new Dict();
//		dict2.setLabel("Q2");
//		Dict dict3 = new Dict();
//		dict3.setLabel("Q3");
//		Dict dict4 = new Dict();
//		dict4.setLabel("Q4");
//		Dict dict5 = new Dict();
//		dict5.setLabel("Q5");
//		list.add(dict1);
//		list.add(dict2);
//		list.add(dict3);
//		list.add(dict4);
//		list.add(dict5);
//		for(int i= 0 ; i < list.size() ; i++ ){  
//			Dict dict = (Dict)list.get(i);
//			System.out.println(dict.getLabel());
//			dict.setLabel("tt" + i);
//		}
//		int day = getDays() % 15 == 0 ? 15 - 1 : getDays()  % 15 - 1;
//		for(int i = 2; i <= 31 ; i ++){
//			System.out.print(i+"号");
//			int s = (i-1) % 15 == 0 ? 15 - 1 : (i-1)  % 15 - 1;
//			System.out.println(s);
//		}
//		for(int i = 1; i <= 30 ; i ++){
//			System.out.print(i+"号");
//			int s = i % 15 == 0 ? 15 - 1 : i  % 15 - 1;
//			System.out.println(s);
//		}
		
		
		Calendar calendar = Calendar.getInstance();  
        int year = 2018;  
        int month = 1;  
        int date = 1;  
        calendar.set(year, month, date);  
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("Max Day: " + maxDay);
        int minDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        System.out.println("Min Day: " + minDay);
//        // 大月
//        for (int i = minDay +1; i <= maxDay; i++) {  
//            calendar.set(year, month, i);
//            System.out.println("Day: " + calendar.getTime().toLocaleString());
////        	int datenum = calendar.get(Calendar.DATE);
////        	System.out.println(datenum);
//            int s =(i-1) % 15 == 0 ? 15 - 1 : (i-1) % 15 - 1;
////        	int s = i % 15 == 0 ? 15 - 1 : i  % 15 - 1;
//            System.out.println(s);
//        }  
//        // 小月
//        for (int i = minDay ; i <= maxDay; i++) {  
//            calendar.set(year, month, i);
//            System.out.println("Day: " + calendar.getTime().toLocaleString());
////        	int datenum = calendar.get(Calendar.DATE);
////        	System.out.println(datenum);
////            int s =(i-1) % 15 == 0 ? 15 - 1 : (i-1) % 15 - 1;
//        	int s = i % 15 == 0 ? 15 - 1 : i  % 15 - 1;
//            System.out.println(s);
//        }  
        // 2月 
        for (int i = minDay ; i <= maxDay; i++) {  
            calendar.set(year, month, i);
            System.out.println("Day: " + calendar.getTime().toLocaleString());
            int s =(i+2) % 15 == 0 ? 15 - 1 : (i+2) % 15 - 1;
            System.out.println(s);
        } 
//      day = (getDays()-1) % 15 == 0 ? 15 - 1 : (getDays()-1) % 15 - 1;
		
//		ListSortUtil<Dict> sortList = new ListSortUtil<Dict>();  
//		sortList.sort(list, "label", "desc");  
//		for(Dict dict : list){
//			System.out.println(dict.getLabel());
//		}
//		if(actinMap.containsKey("UP")){
		//	actinMap.get("UP").add(vo);
		//}else{
		//	List<PlatViewVO> tList = new ArrayList<PlatViewVO>();
		//	tList.add(vo);
		//	actinMap.put("UP", tList);
		//}
//		 int current = 7;  
//	        if(rangeInDefined(current, 1, 10))  
//	            System.out.println(current + "在1——10之间");  
//		String s = "";
//		System.out.println(s.split("_")[0] == null);
	}
	
	
//	public static final void main(String[] args)throws Exception
//	{
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date now = new Date();
////		int days = (int)(toDate(now).getTime() - toDate(sdf.parse("2016-08-15 12:08:06")).getTime()) / (24 * 60 * 60 * 1000);
//		int days = TMisDunningTaskService.GetOverdueDay(sdf.parse("2016-08-15 11:41:21"));
////		int days = daysBetween("2016-08-15 11:41:06","2016-08-17 16:24:00");
//		System.out.println(days);
//	}
	/**
	 * 自动发送短信提醒，还款的前一天和当天9点
	 */
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//	public void autoSendMessageBefore(){
//		
//		List<TRiskBuyerPersonalInfo> autoSmsMessages = tpersonalInfoDao.getMessgeByRepaymentTime();
//		List <TMisContantRecord> tcList=new ArrayList<TMisContantRecord>();
//		for (TRiskBuyerPersonalInfo tAuto : autoSmsMessages) {
//			String route = "";
//			String msg="";
//			String sex="";
//			try{
//				
//				if(tAuto.getRealName()!=null&&tAuto.getRealName()!=""){
//					if("男".equals(tAuto.getSex())){
//						 sex="先生";
//					}
//					if("女".equals(tAuto.getSex()))	{
//						sex="女士";
//					}
//				}
//				
//				if(tAuto.getFinProduct()!=null&&tAuto.getFinProduct()!=""){
//				  if(tAuto.getFinProduct().contains("feishudai")){
//						route="飞鼠袋";
//				  }else{
//					  route = "mo9信用钱包";
//				  }
//				}	
//				
//		
//		String name=tAuto.getRealName();
//		
//		Double amount = new Double(0);
//		amount = Double.valueOf(tAuto.getCreditAmount()).doubleValue() /100D;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String repaymentTime = sdf.format(tAuto.getRepaymentTime());
//		
//		if(tAuto.getRpayStatus()==1){
//		 msg = MessageFormat.format("【mo9】{0}{1}，您{2}本期所需还款金额为{3}元，请您最晚于{4}请做好财务规划，及时还款哦。",name,sex,route,amount,repaymentTime);
//		}
//		if(tAuto.getRpayStatus()==0){
//			msg = MessageFormat.format("【mo9】{0}{1}，您{2}本期的应还账单为{3}元，最后还款日期为{4}请及时还款，若已还款请勿理会。 ",name,sex,route,amount,repaymentTime);
//		}
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("mobile", tAuto.getMobile());
//		params.put("message",msg);
//		Thread.sleep(100);
//		MsfClient.instance().requestFromServer(ServiceAddress.SNC_SMS, params, BaseResponse.class);
//		logger.info("给用户:"+tAuto.getMobile()+"发送短信成功，内容:"+msg);
//		}catch (Exception e) {
//			logger.info("发送短信失败:"+e);
//			logger.info("给用户:"+tAuto.getMobile()+"发送短信失败，内容:"+msg);
//		}	
//		TMisContantRecord dunning = new TMisContantRecord();
//		dunning.setTaskid(null);
//		dunning.setDealcode(tAuto.getDealcode());
//		dunning.setOrderstatus(false);
//		dunning.setOverduedays(Integer.valueOf(tAuto.getOverdueDays()));
//		dunning.setDunningtime(new Date());
//		dunning.setContanttype(ContantType.sms);
//		dunning.setContanttarget(tAuto.getMobile());
//		//短信内容
//		dunning.setContent(msg);
//		dunning.setSmstemp(SmsTemp.ST_0);
//		dunning.setTelstatus(null);
//		dunning.setContactstype(ContactsType.SELF);
//		dunning.setDunningpeoplename("sys");
//		dunning.setRepaymenttime(tAuto.getRepaymentTime());
//		dunning.setRemark(null);
//		dunning.preInsert();
//	    tcList.add(dunning);	
//	}
//		if(tcList!=null||!"".equals(tcList)){
//			tcontDao.saveList(tcList);
//		}
//  }
	
	/**
	 * 自动发送短信提醒，还款当天的13点
	 */
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//	public void autoSendMessageNow(){
//		
//		List<TRiskBuyerPersonalInfo> autoSmsMessages = tpersonalInfoDao.getMessgeByRepaymentTime();
//		List <TMisContantRecord> trList=new ArrayList<TMisContantRecord>();
//		for (TRiskBuyerPersonalInfo tAuto : autoSmsMessages) {
//			String route = "";
//			String msg="";
//			String sex="";
//			if(tAuto.getRpayStatus()==0){
//			try{
//				
//				if(tAuto.getRealName()!=null&&tAuto.getRealName()!=""){
//					if("男".equals(tAuto.getSex())){
//						 sex="先生";
//					}
//					if("女".equals(tAuto.getSex()))	{
//						sex="女士";
//					}
//				}
//				
//				if(tAuto.getFinProduct()!=null&&tAuto.getFinProduct()!=""){
//				  if(tAuto.getFinProduct().contains("feishudai")){
//						route="飞鼠袋";
//				  }else{
//					  route = "mo9信用钱包";
//				  }
//				}	
//				
//				String name=tAuto.getRealName();
//				Double amount = new Double(0);
//				amount = Double.valueOf(tAuto.getCreditAmount()).doubleValue() /100D;
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				String repaymentTime = sdf.format(tAuto.getRepaymentTime());
//				msg = MessageFormat.format("【mo9】{0}{1}，您{2}本期的应还账单为{3}元，最后还款日期为{4}请及时还款，若已还款请勿理会。 ",name,sex,route,amount,repaymentTime);
//				Map<String,String> params = new HashMap<String,String>();
//				params.put("mobile", tAuto.getMobile());
//				params.put("message",msg);
//				Thread.sleep(100);
//				MsfClient.instance().requestFromServer(ServiceAddress.SNC_SMS, params, BaseResponse.class);
//				logger.info("给用户:"+tAuto.getMobile()+"发送短信成功，内容:"+msg);
//			}catch (Exception e) {
//				logger.info("发送短信失败:"+e);
//				logger.info("给用户:"+tAuto.getMobile()+"发送短信失败，内容:"+msg);
//			}	
//			TMisContantRecord dunning = new TMisContantRecord();
//			dunning.setTaskid(null);
//			dunning.setDealcode(tAuto.getDealcode());
//			dunning.setOrderstatus(false);
//			dunning.setOverduedays(Integer.valueOf(tAuto.getOverdueDays()));
//			dunning.setDunningtime(new Date());
//			dunning.setContanttype(ContantType.sms);
//			dunning.setContanttarget(tAuto.getMobile());
//			//短信内容
//			dunning.setContent(msg);
//			dunning.setSmstemp(SmsTemp.ST_0);
//			dunning.setTelstatus(null);
//			dunning.setContactstype(ContactsType.SELF);
//			dunning.setDunningpeoplename("sys");
//			dunning.setRepaymenttime(tAuto.getRepaymentTime());
//			dunning.setRemark(null);
//			dunning.preInsert();
//		    trList.add(dunning);
//		}
//	  }
//		if(trList!=null||!"".equals(trList)){
//			tcontDao.saveList(trList);
//		}
//	}
//	final static String same_frame = DictUtils.getDictValue("短信链接", "sms_url", "https://www.mo9.com");
	
//	@Scheduled(cron = "0 0 9 * * ?")  
//	@Transactional
//   public void autoMessageBefore(){
//		this.autoSendMessageBefore();
//	}
//
//	@Scheduled(cron = "0 0 13 * * ?")  
//	@Transactional
//   public void autoMessageNow(){
//		this.autoSendMessageNow();
//	}

	/**
	 * @Description: 委外任务列表
	 * @param page
	 * @param entity
	 * @return
	 * @return: Page<DunningOrder>
	 */
	public Page<DunningOrder> findOuterOrderPageList(Page<DunningOrder> page, DunningOrder entity) {
		if (entity == null) {
			entity = new DunningOrder();
		}
		
		if(null != entity.getStatus() && entity.getStatus().equals("payoff")){
			entity.getSqlMap().put("orderbyMap", " payofftime DESC ");
		}else{
			entity.getSqlMap().put("orderbyMap", " date_FORMAT(repaymenttime, '%Y-%m-%d') DESC,creditamount DESC,dealcode DESC");
		}
		entity.setPage(page);
		page.setList(dao.findOuterOrderPageList(entity));
		return page;
	}

	/**
	 * @Description  委外任务列表导出
	 * @param entity
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	public List<DunningOrder> findOuterOrderPageList(DunningOrder entity) {
		if (entity == null) {
			entity = new DunningOrder();
		}

		if(null != entity.getStatus() && entity.getStatus().equals("payoff")){
			entity.getSqlMap().put("orderbyMap", " payofftime DESC ");
		}else{
			entity.getSqlMap().put("orderbyMap", " status,date_FORMAT(repaymenttime, '%Y-%m-%d') DESC,creditamount DESC,dealcode DESC");
		}
		return dao.findOuterOrderPageList(entity);
	}

	@Scheduled(cron = "0 10 0 * * ?")  
	@Transactional
	public void autoSmsSend() {
		List<TmisDunningSmsTemplate> tstList = tdstDao.findByAutoSend();// 查询所有系统模板
		SimpleDateFormat sb1 = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat sb2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		for (final TmisDunningSmsTemplate smsTemplate : tstList) {
			Date sendDate = null;
			try {
				String sendTime = smsTemplate.getSendTime();
				String format1 = sb1.format(new Date());
				format1 = format1 + "  " + sendTime;

				sendDate = sb2.parse(format1);
			} catch (Exception e) {
				logger.warn("模板:" + smsTemplate.getTemplateName() + ",自动发送短信失败");
				continue;
			}

			// TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
			scheduler.schedule(new Runnable() {
				@Override
				public void run() {
					Integer numafter = smsTemplate.getNumafter();
					Integer numbefore = smsTemplate.getNumbefore();
					List<DunningOrder> findallAtuoSms = tMisDunningTaskDao.findallAtuoSms(numbefore, numafter);// 查询所有需要催收的订单
					logger.info("系统发送,查询时间为" + sb2.format(new Date()) + ",查询催收任务通过条件逾期天数为" + numbefore + "天到" + numafter
							+ "天,短信模板为:" + smsTemplate.getTemplateName() + ",查询到的总条数为" + findallAtuoSms.size() + "条");
					// 跟踪短信发送的条数
					int i = 1;
					List<TMisContantRecord> trList = new ArrayList<TMisContantRecord>();
					for (DunningOrder dunningOrder : findallAtuoSms) {
						// Integer numafter = smsTemplate.getNumafter();
						// Integer numbefore = smsTemplate.getNumbefore();
						// if(overduedays>=numbefore&&overduedays<=numafter){
						// TMisDunningOrder order =
						// tMisDunningTaskDao.findOrderByDealcode(dunningOrder.getDealcode());
						// TMisDunningTask task =
						// tMisDunningTaskDao.get(dunningOrder.getDunningtaskdbid());
						String smsCotent = "";
						// 判断是否给该订单发送短信成功的字段
						String smsSendSueOrFle = "";
						try {

							TRiskBuyerPersonalInfo buyerInfeo = tpersonalInfoDao
									.getbuyerIfo(dunningOrder.getDealcode());
							if(null==buyerInfeo){
								throw new RuntimeException("发送短信失败");
							}
							smsCotent = tdstService.cousmscotent(smsTemplate.getSmsCotent(), buyerInfeo,
									dunningOrder.getPlatformExt(), dunningOrder.getDunningpeopleid());
							if (smsCotent.contains("$")) {
								throw new RuntimeException();
							}
							if ("wordText".equals(smsTemplate.getSmsType())) {
								Map<String, String> params = new HashMap<String, String>();
								params.put("mobile", dunningOrder.getMobile());// 发送手机号
								// snc版本 固定值:"2.0";
								params.put("snc_version", "2.0");
								// 业务名称 例："JHJJ","FXYL","XWHF","MIS";
								params.put("biz_sys", "MIS");
								// 发送类型 例："1","2","3","4"; 对应说明:验证码，营销，催收,系统
								params.put("biz_type", "dunning");
								// 客户端产品名称
								// 例："mo9wallet","feishudai","feishudaiPro"
								if (smsTemplate.getSmsCotent().contains("${platform}")) {
									if (null != dunningOrder.getPlatformExt()
											&& !"".equals(dunningOrder.getPlatformExt())) {
										if (dunningOrder.getPlatformExt().contains("feishudai")) {
											params.put("product_name", "feishudai");
										} else {
											params.put("product_name", "mo9wallet");
										}
									} else {
										params.put("product_name", "mo9wallet");
									}

								} else {
									params.put("product_name", "mo9wallet");
								}
								// 模板填充的map
								Map<String, Object> map = tMisContantRecordService.getCotentValue(
										smsTemplate.getSmsCotent(), buyerInfeo, dunningOrder.getPlatformExt(),
										dunningOrder.getDunningpeopleid());
								params.put("template_data", new JacksonConvertor().serialize(map));
								String englishTemplateName = smsTemplate.getEnglishTemplateName();
								params.put("template_name", englishTemplateName);// 模板名称
								params.put("template_tags", "CN");// 模板标识
								Thread.sleep(100);
								MsfClient.instance().requestFromServer(ServiceAddress.SNC_SMS, params,
										BaseResponse.class);
							}
							if ("voice".equals(smsTemplate.getSmsType())) {
								Map<String, String> params = new HashMap<String, String>();
								params.put("mobile", dunningOrder.getMobile());// 发送手机号
								params.put("style", "voiceContent");// 固定值
								// 模板填充的map
								Map<String, Object> map = tMisContantRecordService.getCotentValue(
										smsTemplate.getSmsCotent(), buyerInfeo, dunningOrder.getPlatformExt(),
										dunningOrder.getDunningpeopleid());
								params.put("template_data", new JacksonConvertor().serialize(map));
								String englishTemplateName = smsTemplate.getEnglishTemplateName();
								params.put("template_name", englishTemplateName);// 模板名称
								params.put("template_tags", "CN");// 模板标识
								// snc版本 固定值:"2.0";
								params.put("snc_version", "2.0");
								// 业务名称 例："JHJJ","FXYL","XWHF","MIS";
								params.put("biz_sys", "MIS");
								// 发送类型 例："1","2","3","4"; 对应说明:验证码，营销，催收,系统
								params.put("biz_type", "dunning");
								if (smsTemplate.getSmsCotent().contains("${platform}")) {
									if (null != dunningOrder.getPlatformExt()
											&& !"".equals(dunningOrder.getPlatformExt())) {
										if (dunningOrder.getPlatformExt().contains("feishudai")) {
											params.put("product_name", "feishudai");
										} else {
											params.put("product_name", "mo9wallet");
										}
									} else {
										params.put("product_name", "mo9wallet");
									}

								} else {
									params.put("product_name", "mo9wallet");
								}
								Thread.sleep(100);
								MsfClient.instance().requestFromServer(ServiceAddress.SNC_VOICE, params,
										BaseResponse.class);
							}
							
							logger.info(
									"系统发送,给订单号为:" + dunningOrder.getDealcode() + ",用户电话为:" + dunningOrder.getMobile()
											+ ",发送短信成功.模板名为:" + smsTemplate.getTemplateName() + ",该模板发送的第" + i + "条");
						} catch (Exception e) {

							logger.warn("发送短信失败:" , e);
							logger.warn("系统发送,给订单号为:" + dunningOrder.getDealcode() + ",用户电话为:"
									+ dunningOrder.getMobile() + ",发送短信失败.用户信息不全,模板名为:" + smsTemplate.getTemplateName()
									+ ",该模板发送的第" + i + "条");
							smsSendSueOrFle = "false";
						}
						// 跟踪短信发送的条数
						i += 1;
						// 保存到催收历史记录里
						TMisContantRecord dunning = new TMisContantRecord();
						dunning.setTaskid(null);
						dunning.setDealcode(dunningOrder.getDealcode());
						dunning.setOrderstatus(false);
						dunning.setOverduedays(dunningOrder.getOverduedays());
						dunning.setDunningtime(new Date());
						dunning.setContanttype(ContantType.sms);
						dunning.setContanttarget(dunningOrder.getMobile());
						// 短信内容
						dunning.setContent(smsCotent);
						dunning.setTemplateName(smsTemplate.getTemplateName());
						dunning.setSmsType(smsTemplate.getSmsType());
						dunning.setTelstatus(null);
						dunning.setContactstype(ContactsType.SELF);
						dunning.setDunningpeoplename("sys");
						dunning.setRepaymenttime(dunningOrder.getRepaymenttime());
						dunning.setRemark(null);
						if ("false".equals(smsSendSueOrFle)) {
							dunning.setRemark("短信发送失败,用户信息不全");
						}
						dunning.preInsert();
						trList.add(dunning);
					}
					// 批量保存
					if (null!=trList&&trList.size() > 0) {
						tMisContantRecordService.saveList(trList);

					}

				}

			}, new Date(sendDate.getTime()));
		}
	     
	 
	}

	public boolean findOrderByPayCode(TMisDunningOrder order) {
		String payCode = order.getPayCode();
		if(StringUtils.isEmpty(payCode)){
			return false;
		}
		
		List<Dict> captials = DictUtils.getDictList("captial_deduct");
		if (captials == null || captials.size() == 0) {
			return true;
		}
		
		int overdayas = TMisDunningTaskService.GetOverdueDay(order.getRepaymentDate());
		
		for (Dict captial : captials) {
			String captialName = captial.getLabel();
			int days = Integer.parseInt(captial.getValue());
			
			if (payCode.contains(captialName) && overdayas >= days) {
				return true;
			}
		}
		
		return false;
	}
	/**
	 * 验证手机号码和电话号码的格式是否对.第一个参数为号码.第二个参数为类型(1表示手机号码.2表示电话号码)
	 * @param phoneNumber
	 * @param type
	 * @return
	 */
	public boolean  yanZhengNumber(String phoneNumber,int type ){
		String regex="";
		if(StringUtils.isEmpty(phoneNumber)){
			return false;
		}
		if(type==1){
			 regex= RegexUtil.REGEX_MOBILE;
		}
		if(type==2){
			 regex = RegexUtil.REGEX_PHONE;
		}
		return Pattern.matches(regex, phoneNumber);
	}
	
	/**
	 * 查询用户影像资料
	 * @param buyerid
	 * @return
	 */
	public String findBuyerImg(String buyerid) {
		TRiskBuyerPersonalInfo personInfo = tMisDunningTaskDao.findBuyerImg(buyerid);
		
		if (personInfo == null) {
			return "";
		}
		return StringUtils.isBlank(personInfo.getImgFiled4()) ? personInfo.getImgFiled3() : personInfo.getImgFiled4(); 
	}

	/**
	 * @Description  保存部分还款日志
	 * @param dealcode
	 * @return void
	 */
	public void savePartialRepayLog(String dealcode) {
		TMisDunningTaskLog dunningTaskLog = tMisDunningTaskDao.newfingTaskByDealcode(dealcode);
		if (dunningTaskLog == null) {
			return;
		}
		dunningTaskLog.setBehaviorstatus("partial");
		dunningTaskLog.setCreateDate(new Date());
		dunningTaskLog.setCreateBy(new User("auto_admin"));
		tMisDunningTaskLogDao.insert(dunningTaskLog);
	}

	/**
	 * 定时更新用户最近登录时间
	 */
	@Transactional(readOnly = false)
	@Scheduled(cron = "0 0 0/2 * * ?")
	public void queryLatestLoginTime() {
		logger.info("定时更新最近登录时间开始");
		DunningOrder entity = new DunningOrder();
		entity.setStatus("payment");
		List<DunningOrder> dunningOrders = tMisDunningTaskDao.findDunningOrderInfo(entity);

		if (dunningOrders == null || dunningOrders.size() == 0) {
			logger.info("没有需要更新最近登录时间的任务");
			return;
		}

		try {
			for (DunningOrder order : dunningOrders) {
				String mobile = order.getMobile();
				if (mobile == null || "".equals(mobile)) {
					continue;
				}

				AppLoginLog appLoginLog = getLatestAppLoginLog(mobile);

				if (appLoginLog == null) {
					continue;
				}

				Date loginTime = appLoginLog.getCreateTime();
				if (loginTime != null && order.getLatestlogintime() == null ? true : loginTime.compareTo(order.getLatestlogintime()) != 0) {
					order.setLatestlogintime(loginTime);
					tMisDunningTaskSupportService.updateLatestLoginTime(order);
				}
			}
		} catch (Exception e) {
			logger.info("更新最近登录时间失败：" + e);
		}
		logger.info("定时更新最近登录时间结束");
	}

	/**
	 * 切换数据源查询最新登录记录
	 * @param mobile
	 * @return
	 */
	public AppLoginLog getLatestAppLoginLog(String mobile) {
		String sql = "SELECT mobile, localMobile, deviceModel, mo9ProductName, marketName, createTime FROM t_app_login_log WHERE mobile = ? ORDER BY createTime DESC LIMIT 1";

		DbUtils dbUtils = new DbUtils();
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(mobile);
		try {
			List<Map<String, Object>> result = dbUtils.getQueryList(sql, paramList);
			if (result == null || result.size() == 0) {
				return null;
			}
			Map<String, Object> record = result.get(0);

			String localMobile = (String) record.get("localMobile");
			String deviceModel = (String) record.get("deviceModel");
			String mo9ProductName = (String) record.get("mo9ProductName");
			String marketName = (String) record.get("marketName");
			Date createTime = (Date) record.get("createTime");

            AppLoginLog appLoginLog = new AppLoginLog();
            appLoginLog.setMobile(mobile);
            appLoginLog.setLocalMobile(localMobile);
            appLoginLog.setDeviceModel(deviceModel);
            appLoginLog.setMo9ProductName(mo9ProductName);
            appLoginLog.setMarketName(marketName);
            appLoginLog.setCreateTime(createTime);

			return appLoginLog;
		} catch (Exception e) {
			logger.info("查询最新登录记录异常：" + e.getMessage());
			throw new ServiceException(e);
		}
	}

	//号码清洗
		@Scheduled(cron = "0 0 8 * * ?")
		@Transactional(readOnly = false)
		public void numberCleanResult(){
			//是否开启清洗
			String startClean=DictUtils.getDictValue("startCleanNumber", "cleanNumber", "true");
			if("true".equals(startClean)){
				String overDayNumber=DictUtils.getDictValue("cleanDay", "cleanNumber", "-1");
				int  overday;
				try {
					  overday=Integer.parseInt(overDayNumber);
				} catch (Exception e) {
					logger.warn("请检查数据字典号码清洗的配置");
					return;
				}
				//清洗账号
				String account=DictUtils.getDictValue("account", "cleanNumber", "szsh2667");
				//清洗密码
				String password=DictUtils.getDictValue("password", "cleanNumber", "123456");
				//两次md5加密
				password = TMisDunningTaskService.pwdMD5(TMisDunningTaskService.pwdMD5(password));
				//调用地址
				String callUrl=DictUtils.getDictValue("callUrl", "cleanNumber", "http://tele-spider.cloud.zg4007.com/asynCheckTel");
				//回调地址
				String replyUrl=DictUtils.getDictValue("replyUrl", "cleanNumber", "http://mis.mo9.com/number/numberBack");
				//需要清洗的号码
				List<DunningOrder> numberList=tMisDunningTaskDao.findNumberByOverDay(overday);
				int fail=0;
				logger.info(new Date()+"开始号码清洗");
			    for (DunningOrder dunningOrder : numberList) {
			    	TmisDunningNumberClean tmisDunningNumberClean=new TmisDunningNumberClean();
			    	tmisDunningNumberClean.setAccount(account);
			    	tmisDunningNumberClean.setPassword(password);
			    	tmisDunningNumberClean.setReplyUrl(replyUrl);
			    	tmisDunningNumberClean.setDealcode(dunningOrder.getDealcode());
			    	tmisDunningNumberClean.setPhone(dunningOrder.getMobile());
			    	String  reqNo=TMisDunningTaskService.pwdMD5(dunningOrder.getDealcode()+System.currentTimeMillis());
			    	tmisDunningNumberClean.setReqNo(reqNo);
			    	tmisDunningNumberClean.setCreateByName("sys");
			    	tmisDunningNumberClean.setCallingPeriod("calling");
			    	tmisDunningNumberClean.preInsert();
			    	HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(callUrl+"?account="+account+ "&password="+password+"&phone="+dunningOrder.getMobile()+"&reqNo="+reqNo+"&replyUrl="+replyUrl);
					Document document=null;
					tmisDunningNumberClean.setStartTime(new Date());
					tMisDunningTaskSupportService.saveNumberList(tmisDunningNumberClean);
					try {
						HttpResponse response = httpClient.execute(httpGet);
						String entity = EntityUtils.toString(response.getEntity());
						document = DocumentHelper.parseText(entity);
					} catch (Exception e) {
						++fail;
						tmisDunningNumberClean.setState("false");
						tmisDunningNumberClean.setUpdateDate(new Date());
						tmisDunningNumberClean.setEndTime(new Date());
						tmisDunningNumberClean.setUpdateByName(tmisDunningNumberClean.getCreateByName());
						tMisDunningTaskDao.updateNumberResult(tmisDunningNumberClean);
						tmisDunningNumberClean.preInsert();
						tMisDunningTaskDao.saveNumberCleanLog(tmisDunningNumberClean);
						logger.warn(new Date()+"订单号为："+dunningOrder.getDealcode()+"号码清洗失败。");
						continue;

					}
						// 1.获取文档的根节点.
						if(null==document){
							logger.warn(new Date()+"订单号为："+dunningOrder.getDealcode()+"号码清洗失败。");
							continue;
						}
						Element root = document.getRootElement();
						List<Element> listError = root.elements("error");
						//如果失败的不会回调，要进行更新保存
						if(!"0".equals(listError.get(0).getText())){
							List<Element> listTaskId = root.elements("task_id");
							List<Element> listMessage = root.elements("message");
							tmisDunningNumberClean.setTaskId(listTaskId.get(0).getText());
							tmisDunningNumberClean.setError(listError.get(0).getText());
							tmisDunningNumberClean.setMessage(listMessage.get(0).getText());
							tmisDunningNumberClean.setEndTime(new Date());
							tmisDunningNumberClean.setUpdateDate(new Date());
							tmisDunningNumberClean.setUpdateByName(tmisDunningNumberClean.getCreateByName());
							tmisDunningNumberClean.setState("false");
							if("1".equals(listError.get(0).getText())||"2".equals(listError.get(0).getText())||"5".equals(listError.get(0).getText())){
								tMisDunningTaskDao.updateNumberResult(tmisDunningNumberClean);
								tmisDunningNumberClean.preInsert();
								tMisDunningTaskDao.saveNumberCleanLog(tmisDunningNumberClean);
								logger.warn(new Date()+",这次号码清洗失败,失败原因:"+listError.get(0).getText());
								return;
							}
							if("3".equals(listError.get(0).getText())||"4".equals(listError.get(0).getText())){
								++fail;
								tMisDunningTaskDao.updateNumberResult(tmisDunningNumberClean);
								tmisDunningNumberClean.preInsert();
								tMisDunningTaskDao.saveNumberCleanLog(tmisDunningNumberClean);
								logger.warn(new Date()+"订单号为："+dunningOrder.getDealcode()+"号码清洗失败。手机号码格式不对");
							}
						}
			    }
			    logger.info(new Date()+"本次号码清洗条数为："+numberList.size()+"条,失败为"+fail+"条");
			}


		}
		//回调函数
		@Transactional(readOnly = false)
		public void numberCleanBack(TmisDunningNumberClean tmisDunningNumberClean){
			TmisDunningNumberClean number=tMisDunningTaskDao.findNumberClean(tmisDunningNumberClean);
			if(null==number){
				logger.warn(new Date()+",流水号不存在，该订单号码未清洗过。");
				return;
			}
			if("00".equals(tmisDunningNumberClean.getCheckResult())||"01".equals(tmisDunningNumberClean.getCheckResult())||"02".equals(tmisDunningNumberClean.getCheckResult())||
					"03".equals(tmisDunningNumberClean.getCheckResult())||"12".equals(tmisDunningNumberClean.getCheckResult())){
				number.setCheckResult("YXHM");
			}
			if("04".equals(tmisDunningNumberClean.getCheckResult())){
				number.setCheckResult("BZFWQ");
			}
			if("05".equals(tmisDunningNumberClean.getCheckResult())){
				number.setCheckResult("KH");
			}
			if("06".equals(tmisDunningNumberClean.getCheckResult())){
				number.setCheckResult("HMCW");
			}
			if("10".equals(tmisDunningNumberClean.getCheckResult())){
				number.setCheckResult("GJ");
			}
			if("11".equals(tmisDunningNumberClean.getCheckResult())){
				number.setCheckResult("TJ");
			}
			if("07".equals(tmisDunningNumberClean.getCheckResult())||"08".equals(tmisDunningNumberClean.getCheckResult())||"09".equals(tmisDunningNumberClean.getCheckResult())){
				number.setCheckResult("WZ");
			}
			Date date=new Date();
			number.setEndTime(date);
			number.setAreaName(tmisDunningNumberClean.getAreaName());
			number.setAreaCode(tmisDunningNumberClean.getAreaCode());
			number.setPostCode(tmisDunningNumberClean.getPostCode());
			number.setMessage(tmisDunningNumberClean.getMessage());
			number.setTaskId(tmisDunningNumberClean.getTaskId());
			number.setCallingPeriod("callback");
			number.setState("true");
			number.setError("0");
			number.setUpdateByName(number.getCreateByName());
			number.setUpdateDate(date);
			tMisDunningTaskDao.updateNumberResult(number);
			number.preInsert();
			tMisDunningTaskDao.saveNumberCleanLog(number);
		}
		//模拟回调测试
		@Transactional(readOnly = false)
		public void callBackTest(){
			List<TmisDunningNumberClean> findBackTest = tMisDunningTaskDao.findBackTest();
			for (TmisDunningNumberClean clean : findBackTest) {
				clean.setAreaName("上海");
				clean.setAreaCode("人广");
				clean.setPostCode("123456");
				clean.setTaskId(TMisDunningTaskService.pwdMD5(clean.getDealcode()+System.currentTimeMillis()));
				clean.setMessage("回调清洗成功");
				clean.setCheckResult("01");
				this.numberCleanBack(clean);
			}
		}
		//md5加密
		public static String pwdMD5(String strs) {

			StringBuffer sb = new StringBuffer();
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				byte[] bs = digest.digest(strs.getBytes());

				for (byte b : bs) {
					int x = b & 255;
					String s = Integer.toHexString(x);
					if (x < 16) {
						sb.append("0");
					}
					sb.append(s);
				}
			} catch (Exception e) {
				logger.warn(new Date()+"号码清洗，加密失败");
				throw new RuntimeException();
			}
			return sb.toString();
		}

	
	@Autowired
	private TMisMigrationRateReportService tMisMigrationRateReportService;
	
//    /**
//     * 每天定时更新迁徙率DB
//     */
//	@Scheduled(cron = "0 16 0 * * ?")
//	@Transactional(readOnly = false)
//	public void autoInsertTmpMoveCycleDB() {
//		try {
//			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");  
//			tMisMigrationRateReportService.autoInsertTmpMoveCycleDB();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DynamicDataSource.setCurrentLookupKey("dataSource");  
//		}
//	}
//	
//	/**
//	 * @return void
//	 * @Description 迁徙率数据的表更新
//	 */
//	@Scheduled(cron = "0 18 0 * * ?")
//	@Transactional(readOnly = false)
//	public void autoMigrationRateGetData() {
//		try {
//			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");  
//			tMisMigrationRateReportService.autoMigrationRateGetData();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DynamicDataSource.setCurrentLookupKey("dataSource");  
//		}
//	}
	
	/**
	  * 迁徙率计算insertDB
	  */
	@Scheduled(cron = "0 0 8 * * ?")
	@Transactional(readOnly = false)
	public void autoInsertMigrationRateReportDB_job() {
		logger.info("开始今天迁徙计算");
		try {
			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");  
			Date Yesterday = TMisMigrationRateReportService.getDate(-1);
			tMisMigrationRateReportService.autoInsertMigrationRateReportDB(Yesterday);
			logger.info("迁徙计算结束");
		} catch (Exception e) {
			logger.info(e);
			logger.warn("迁徙计算失败");
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");  
		}
	}
	/**
	 * 自动做电催结论
	 */
	@Scheduled(cron = "0 10 20 * * ?")
	@Transactional(readOnly = false)
	public void autoTelConclusion1() {
		//获取昨天晚上20:10到今天晚上20:10的Q0和Q1队列的电催action(根据订单排序了,且只为该周期内的所对应的催收员的action)
		logger.info("Q0和Q1开始电催结论");
		SimpleDateFormat sd=new SimpleDateFormat("YYYY-MM-dd");
		String yesterday =sd.format(DateUtils.getBeforeDay())+" 20:10:00";
		String today =sd.format(new Date())+" 20:10:00";
		List<TMisContantRecord> recordList=tcontDao.findautoTelConclusion("cycleAndTime1",yesterday,today);
		logger.info("Q0和Q1共查到"+recordList.size());
		autoSaveTelConclusion(recordList);
		
	}
	/**
	 * 自动做电催结论
	 */
	@Scheduled(cron = "0 10 12 * * ?")
	@Transactional(readOnly = false)
	public void autoTelConclusion2() {
		logger.info("Q2和Q3和Q4开始中午电催结论");
		//获取昨天晚上20:10到今天12:10的Q2和Q3和Q4队列的电催action(根据订单排序了,且只为该周期内的所对应的催收员的action)
		SimpleDateFormat sd=new SimpleDateFormat("YYYY-MM-dd");
		String yesterday =sd.format(DateUtils.getBeforeDay())+" 20:10:00";
		String today =sd.format(new Date())+" 12:10:00";
		List<TMisContantRecord> recordList=tcontDao.findautoTelConclusion("cycleAndTime2",yesterday,today);
		logger.info("Q2和Q3和Q4中午共查到"+recordList.size());
		autoSaveTelConclusion(recordList);
		
	}
	/**
	 * 自动做电催结论
	 */
	@Scheduled(cron = "0 10 20 * * ?")
	@Transactional(readOnly = false)
	public void autoTelConclusion3() {
		//获取今天12:10到今天晚上20:10的Q2和Q3和Q4队列的电催action(根据订单排序了,且只为该周期内的所对应的催收员的action)
		logger.info("Q2和Q3和Q4开始晚上电催结论");
		SimpleDateFormat sd=new SimpleDateFormat("YYYY-MM-dd");
		String todayMonning =sd.format(new Date())+" 12:10:00";
		String today =sd.format(new Date())+" 20:10:00";
		List<TMisContantRecord> recordList=tcontDao.findautoTelConclusion("cycleAndTime3",todayMonning,today);
		logger.info("Q2和Q3和Q4晚上共查到"+recordList.size());
		autoSaveTelConclusion(recordList);
		
	}
	private void autoSaveTelConclusion(List<TMisContantRecord> recordList) {
		if(recordList==null||recordList.size()<=0){
			logger.info(new Date()+",今天无电催action");
			return;
		}
		Map<String, Object> recordTemp=new HashMap<String, Object>();
		List<String> actions=new ArrayList<String>();
		Set<String> actionMobile=new HashSet<String>();
		String decalode=recordList.get(0).getDealcode();
		String taskId=recordList.get(0).getTaskid();
		String dunningCycle=recordList.get(0).getDunningCycle();
		String dunningPeopleId=recordList.get(0).getDunningpeoplename();
		StringBuilder remark=new StringBuilder();
		int cycle=0;
		for (int i=0;i<recordList.size();i++) {
			//判断是否是同一个用户
			if(decalode.equals(recordList.get(i).getDealcode())){
				logger.info("订单号:"+decalode+",案件队列为:"+dunningCycle+"数据整理");
				//同一个用户所有做过电催结论的action的id
				actions.add(recordList.get(i).getId());
				//承诺还款时间
				if(recordTemp.get("promisDate")==null){
					if(recordList.get(i).getPromisepaydate()!=null){
						recordTemp.put("promisDate", recordList.get(i).getPromisepaydate());
					}
				}
				//电催结论备注
				if(++cycle<20){
					remark.append(recordList.get(i).getContactstype().getDesc()+"-"+recordList.get(i).getContactsname()+"-"+recordList.get(i).getContanttarget()+"-"+
							recordList.get(i).getTelstatus());
					if(StringUtils.isEmpty(recordList.get(i).getRemark())){
						remark.append(";");
					}else{
						remark.append("-"+recordList.get(i).getRemark()+";");
					}
				}
				//如果没取到表示是该订单的第一个结果码
				if(recordTemp.get("telStatus")==null){
					recordTemp.put("telStatus", recordList.get(i).getTelstatus());
				}else{
					//如果存的结果码是则按到优先顺序判断
					if(telconclusion.get(recordTemp.get("telStatus").toString())!=null){
						Integer priority = telconclusion.get(recordList.get(i).getTelstatus());
						if(priority!=null&&telconclusion.get(recordTemp.get("telStatus").toString())>priority){
							recordTemp.put("telStatus", recordList.get(i).getTelstatus());
						}
							
					}
				}
				//保存每个订单的电话号码
				actionMobile.add(recordList.get(i).getContanttarget());
				//遍历最后一个时保存电催结论
				if(i==recordList.size()-1){
					logger.info("最后一个订单号:"+decalode+",案件队列为:"+dunningCycle+"保存电催结论");
					boolean result = saveConclusion(actionMobile.size(),recordTemp, actions, decalode, taskId, remark,dunningCycle,dunningPeopleId);
					if (!result) {
						logger.info(decalode+"该订单电催结论失败.");
					}else{
						
						logger.info(decalode+"该订单电催结论成功.");
					}
					return;
				}
			}else{
				logger.info("订单号:"+decalode+",案件队列为:"+dunningCycle+"保存电催结论");
					//保存电催结论
					boolean result = saveConclusion(actionMobile.size(),recordTemp, actions, decalode, taskId, remark,dunningCycle,dunningPeopleId);
					if (!result) {
						logger.info(decalode+"该订单电催结论失败.");
					}else{
						
						logger.info(decalode+"该订单电催结论成功.");
					}
					if(actions!=null)
						actions.clear();
					if(actionMobile!=null)
						actionMobile.clear();
					if(recordTemp!=null)
						recordTemp.clear();
					decalode=recordList.get(i).getDealcode();
					taskId=recordList.get(i).getTaskid();
					dunningCycle=recordList.get(i).getDunningCycle();
					dunningPeopleId=recordList.get(i).getDunningpeoplename();
					remark.delete(0, remark.length());
					cycle=0;
					--i;
			}
			
		}
	}
	private boolean saveConclusion(int actionMobile,Map<String, Object> recordTemp, List<String> actions, String decalode, String taskId,
			StringBuilder remark,String dunningCycle,String dunningPeopleId) {
		// 就要给前一个用户做电催结论
		TMisDunnedConclusion tMisDunnedConclusion = new TMisDunnedConclusion();
		//承诺还款时间
		tMisDunnedConclusion.setPromisepaydate((Date) recordTemp.get("promisDate"));
		//是否有效联系
		String  statusConclusion=recordTemp.get("telStatus").toString();
		if(telconclusion.get(statusConclusion)==null){
			logger.info(new Date()+"没有这个结果码"+statusConclusion);
			return false;
		}
		if (telconclusion.get(statusConclusion) <= 3) {
			tMisDunnedConclusion.setIseffective(true);
		}else if(telconclusion.get(statusConclusion)==4){
			tMisDunnedConclusion.setIseffective(false);
			Date findDirCreate = tcontDao.findDirCreate(dunningPeopleId,decalode,dunningCycle);
			if(findDirCreate!=null){
				Date findIsEffective = tcontDao.findIsEffective(dunningPeopleId,decalode,dunningCycle,findDirCreate);
				if(findIsEffective==null){
					recordTemp.put("telStatus", "LOOO");
				}
			}
		}else{
			tMisDunnedConclusion.setIseffective(false);
		}
		//下次跟进时间
		String nextTelDate=DictUtils.getDictValue(statusConclusion, "dunning_result_code", "");
		if(StringUtils.isEmpty(nextTelDate)){
			logger.info(new Date()+"该结果码数据字典未配置");
			return false;
		}
		//若结论为完全失联,下次跟进日期根据两次电催结论计算之间催收拨打的电话号码数量确定。小于3个号码则为2天,其他不变
		if(telconclusion.get(statusConclusion)==5){
			if(actionMobile<3){
				nextTelDate="2";
			}
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, Integer.parseInt(nextTelDate));
		Date nextfollowDate = calendar.getTime();
		tMisDunnedConclusion.setNextfollowdate(nextfollowDate);
		//结果码
		tMisDunnedConclusion.setResultcode((String) recordTemp.get("telStatus"));
		tMisDunnedConclusion.setActions(actions);
		//备注
		tMisDunnedConclusion.setRemark(remark.toString());
		tMisDunnedConclusion.setDealcode(decalode);
		tMisDunnedConclusion.setTaskid(taskId);
		tMisDunnedConclusion.setConclusionType("auto");
		boolean result = tMisDunnedConclusionService.saveRecord(tMisDunnedConclusion, decalode, taskId);
		return result;
	}

}