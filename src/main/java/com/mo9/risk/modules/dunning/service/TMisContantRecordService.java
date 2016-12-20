/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamaxpay.commonutil.msf.BaseResponse;
import com.gamaxpay.commonutil.msf.ServiceAddress;
import com.mo9.risk.modules.dunning.dao.TBuyerContactDao;
import com.mo9.risk.modules.dunning.dao.TMisContantRecordDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyer2contactsDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerContactRecordsDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerPersonalInfoDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerWorkinfoDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.DunningSmsTemplate;
import com.mo9.risk.modules.dunning.entity.DunningUserInfo;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.TelStatus;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.ContantType;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TelNumberBean;
import com.mo9.risk.util.MsfClient;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 催收任务联系记录Service
 * @author ycheng
 * @version 2016-07-15
 */
@Service
@Transactional(readOnly = true)
public class TMisContantRecordService extends CrudService<TMisContantRecordDao, TMisContantRecord> {

	@Autowired
	private TRiskBuyerPersonalInfoDao tRiskBuyerPersonalInfoDao;
	@Autowired
	private TRiskBuyerWorkinfoDao tRiskBuyerWorkinfoDao;
	@Autowired
	private TBuyerContactDao  tBuyerContactDao;	
	@Autowired
	private TRiskBuyerContactRecordsDao tRiskBuyerContactRecordsDao;	
	@Autowired
	private TRiskBuyer2contactsDao tRiskBuyer2contactsDao;
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoDao;
	@Autowired
	private TMisDunningTaskDao dunningTaskDao;
	
	private static Logger logger = Logger.getLogger(TMisContantRecordService.class);

	private DecimalFormat decimalFormat = new DecimalFormat("####.00"); //数字格式化

	@Resource
	private TMisDunningTaskDao tMisDunningTaskDao;

	public TMisContantRecord get(String id) {
		return super.get(id);
	}
	
	public List<TMisContantRecord> findList(TMisContantRecord tMisContantRecord) {
		return super.findList(tMisContantRecord);
	}
	
	public Page<TMisContantRecord> findPage(Page<TMisContantRecord> page, TMisContantRecord tMisContantRecord) {
		page.setOrderBy("dbid desc");
		return super.findPage(page, tMisContantRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisContantRecord tMisContantRecord) {
		super.save(tMisContantRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisContantRecord tMisContantRecord) {
		super.delete(tMisContantRecord);
	}
	
	public List<TMisSendMsgInfo> getTelInfos(String buyerId,String type){
		type = type.toLowerCase();
		if("self".equals(type)){//本人
			return tRiskBuyerPersonalInfoDao.getSelfTelInfo(buyerId);
		}else if("worktel".equals(type)){//工作电话
			return tRiskBuyerWorkinfoDao.getWorkTelByBuyerId(buyerId);
		}else if("communcate".equals(type)){//通话记录
			return tRiskBuyerContactRecordsDao.getCommunicateByBuyerId(buyerId);
		}else if("cantact".equals(type)){//通讯录
			return tBuyerContactDao.getContactsByBuyerId(buyerId);
		}else {//夫妻(married),同事(workmate),父母(parent),子女(children),朋友(friend),亲戚(relatives)
			return tRiskBuyer2contactsDao.getSendMsgByBuyerIdAndType(buyerId, type);
		}
	}
	
	/**
	 * 送并记录短信或者手机催款记录
	 * @param task
	 * @param tMisContantRecord
	 * @return
	 */
	@Transactional(readOnly = false)
	public String smsGetTemp(TMisDunningTask task,TMisDunningOrder order, TMisContantRecord tMisContantRecord) {
			return getDunningSmsTemplate(task, DunningSmsTemplate.valueOf(tMisContantRecord.getSmstemp().name()));
	}
	/**
	 * 送并记录短信或者手机催款记录
	 * @param task
	 * @param tMisContantRecord
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveRecord(TMisDunningTask task,TMisDunningOrder order, TMisContantRecord tMisContantRecord,String dunningtaskdbid) {

		try{
			if(tMisContantRecord.getContanttype() == TMisContantRecord.ContantType.sms){
				/**
				 *  发送短信
				 */
				Map<String,String> params = new HashMap<String,String>();
				params.put("mobile", tMisContantRecord.getContanttarget());
				params.put("message",tMisContantRecord.getContent());
				MsfClient.instance().requestFromServer(ServiceAddress.SNC_SMS, params, BaseResponse.class);
				logger.info("给用户:"+tMisContantRecord.getContanttarget()+"发送短信成功，内容:"+tMisContantRecord.getContent());
			}
		}catch (Exception e) {
			logger.info("发送短信失败:"+e);
			logger.info("给用户:"+tMisContantRecord.getContanttarget()+"发送短信失败，内容:"+tMisContantRecord.getContent());
		}
		try {
			TMisContantRecord dunning = new TMisContantRecord();
			//任务id
			if(task == null){
				dunning.setTaskid(null);
			}else{
				dunning.setTaskid(task.getId());
			}
			//订单号
			dunning.setDealcode(order.dealcode);
			//当前是否逾期
			if("payoff".equals(order.status)){
				dunning.setOrderstatus(true);
			}else{
				dunning.setOrderstatus(false);
			}
	
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String nowDate = formatter.format(new Date());
			String repaymentTime = formatter.format(order.repaymentDate);
			try {
				//逾期天数
				dunning.setOverduedays(daysBetween(repaymentTime, nowDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dunning.setDunningtime(new Date());
			//联系对象
			dunning.setContanttarget(tMisContantRecord.getContanttarget());
			//联系类型 短信，电话
			if (ContantType.sms ==tMisContantRecord.getContanttype()) {
				dunning.setContanttype(ContantType.sms);
				//短信内容
				dunning.setContent(tMisContantRecord.getContent());
				dunning.setSmstemp(tMisContantRecord.getSmstemp());
				dunning.setTelstatus(null);
			} else if (ContantType.tel == tMisContantRecord.getContanttype()) {
				dunning.setContanttype(ContantType.tel);
				dunning.setContent(null);
				dunning.setSmstemp(null);
				dunning.setTelstatus(tMisContantRecord.getTelstatus());
				if(null != task){
					tMisDunningTaskDao.updateTelRemark
					(!"".equals(tMisContantRecord.getRemark()) ? 
							tMisContantRecord.getRemark() : tMisContantRecord.getContanttarget()+  tMisContantRecord.getTelstatus().getDesc()
							, task.getId());
				}
			}
			//联系人对象类型
			dunning.setContactstype(tMisContantRecord.getContactstype());
			//催讨人员id
			/*User user = UserUtils.getUser();
			if(StringUtils.isNotBlank(user.getId())){
				dunning.setDunningpeoplename(user.getName()+":"+Integer.valueOf(user.getId()));
			}*/
			dunning.setDunningpeoplename(task.getDunningpeopleid());
			
			//应该还款时间
			dunning.setRepaymenttime(order.repaymentDate);
			dunning.setRemark(tMisContantRecord.getRemark());
			save(dunning);
			if(!"".equals(dunningtaskdbid) && null!=dunningtaskdbid){
				dunningTaskDao.updatedunningtime(dunningtaskdbid);
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
	 *  根据催收任务获取对应催收短信模版的内容
	 * @param task
	 * @param template
	 * @return
	 */
	public String getDunningSmsTemplate(TMisDunningTask task,DunningSmsTemplate template)
	{
		List<String> args = new ArrayList<String>();
		
		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(task.getDealcode());
		
		String duningAmount = decimalFormat.format((int)Double.valueOf(personalInfo.getCreditAmount()).doubleValue() / 100D);//应催金额
		int overdueDays = task.getCurrentOverdueDays();
		DunningUserInfo userInfo = this.tMisDunningTaskDao.findDunningUserInfo(task.getDealcode());

		switch (template)
		{
			case ST_0:
				args.add(duningAmount);
			break;
			case ST__1_7:
				args.add(duningAmount);
				break;
			case ST_8_14:
				args.add(duningAmount);
				break;
			case ST_15_21:
				args.add(userInfo !=null ?userInfo.realName : "XXX");
				args.add(userInfo !=null ?userInfo.idCard.substring(userInfo.idCard.length()-4,userInfo.idCard.length()) : "0000");
				args.add(Integer.toString(overdueDays));
				args.add(duningAmount);
				break;
			case ST_22_35:
				args.add(userInfo !=null ?userInfo.realName : "XXX");
				args.add(userInfo !=null ?userInfo.idCard.substring(userInfo.idCard.length()-4,userInfo.idCard.length()) : "0000");
				args.add(duningAmount);
				break;
			case ST_15_PLUS:
				args.add(userInfo !=null ?userInfo.realName : "XXX");
				args.add(Integer.toString(overdueDays));
				args.add(duningAmount);
				args.add(userInfo !=null ?userInfo.realName : "XXX");
				break;
		}
		return template.getContent(args.toArray(new String[]{}));
	}
	
	
	/**
	 *  根据催收任务获取对应催收短信模版的内容
	 * @param DunningOrder
	 * @param template
	 * @return
	 */
	public String getDunningSmsTemplate(DunningOrder order,DunningSmsTemplate template)
	{
		List<String> args = new ArrayList<String>();
		String duningAmount = decimalFormat.format(order.getCreditamount() / 100D);//应催金额
		int overdueDays = order.getOverduedays();
		DunningUserInfo userInfo = this.tMisDunningTaskDao.findDunningUserInfo(order.getDealcode());

		switch (template)
		{
			case ST_0:
				args.add(duningAmount);
				break;
			case ST__1_7:
				args.add(duningAmount);
				break;
			case ST_8_14:
				args.add(duningAmount);
				break;
			case ST_15_21:
				args.add(userInfo !=null ?userInfo.realName : "XXX");
				args.add(userInfo !=null ?userInfo.idCard.substring(userInfo.idCard.length()-4,userInfo.idCard.length()) : "0000");
				args.add(Integer.toString(overdueDays));
				args.add(duningAmount);
				break;
			case ST_22_35:
				args.add(userInfo !=null ?userInfo.realName : "XXX");
				args.add(userInfo !=null ?userInfo.idCard.substring(userInfo.idCard.length()-4,userInfo.idCard.length()) : "0000");
				args.add(duningAmount);
				break;
			case ST_15_PLUS:
				args.add(userInfo !=null ?userInfo.realName : "XXX");
				args.add(Integer.toString(overdueDays));
				args.add(duningAmount);
				args.add(userInfo !=null ?userInfo.realName : "XXX");
				break;
		}
		return template.getContent(args.toArray(new String[]{}));
	}
	
	
	/**
	 * 根据订单号返回手机号码发送短信次数
	 * @param dealcode
	 * @return
	 */
	public List<TelNumberBean> findSmsNum(String dealcode){
		return dao.findSmsNum(dealcode);
	}
	
	
	/**
	 * 根据订单号&手机号&催收类型查询操作详情
	 * @param contantRecord
	 * @return
	 */
	public List<TMisContantRecord> findDetailByDealcodeandTel(TMisContantRecord contantRecord){
		return dao.findDetailByDealcodeandTel(contantRecord);
	}
	
	
}