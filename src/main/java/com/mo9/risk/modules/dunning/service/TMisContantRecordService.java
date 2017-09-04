/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.gamaxpay.commonutil.msf.BaseResponse;
import com.gamaxpay.commonutil.msf.JacksonConvertor;
import com.gamaxpay.commonutil.msf.ServiceAddress;
import com.mo9.risk.modules.dunning.dao.TBuyerContactDao;
import com.mo9.risk.modules.dunning.dao.TMisContantRecordDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyer2contactsDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerContactRecordsDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerPersonalInfoDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerWorkinfoDao;
import com.mo9.risk.modules.dunning.dao.TmisDunningSmsTemplateDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.DunningSmsTemplate;
import com.mo9.risk.modules.dunning.entity.DunningUserInfo;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord.ContantType;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.entity.TelNumberBean;
import com.mo9.risk.modules.dunning.entity.TmisDunningSmsTemplate;
import com.mo9.risk.modules.dunning.manager.RiskContactRecordsManager;
import com.mo9.risk.util.MsfClient;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
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
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 催收任务联系记录Service
 * 
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
	private TBuyerContactService  tBuyerContactService;
	@Autowired
	private TRiskBuyerContactRecordsDao tRiskBuyerContactRecordsDao;
	@Autowired
	private TRiskBuyer2contactsDao tRiskBuyer2contactsDao;
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoDao;
	@Autowired
	private TMisDunningTaskDao dunningTaskDao;
	@Autowired
	private TMisContantRecordDao tMisContantRecordDao;
	@Autowired
	private TMisDunningPeopleDao tmisPeopleDao;
	@Autowired
	private TmisDunningSmsTemplateDao tdstDao;
	@Autowired
	private RiskContactRecordsManager recordsManager;

	private static Logger logger = Logger.getLogger(TMisContantRecordService.class);

	private DecimalFormat decimalFormat = new DecimalFormat("####.00"); // 数字格式化

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
	public void saveList(List<TMisContantRecord> trList) {
		tMisContantRecordDao.saveList(trList);
	}

	@Transactional(readOnly = false)
	public void delete(TMisContantRecord tMisContantRecord) {
		super.delete(tMisContantRecord);
	}

	public List<TMisSendMsgInfo> getTelInfos(String buyerId, String type) {
		type = type.toLowerCase();
		if ("self".equals(type)) {// 本人
			return tRiskBuyerPersonalInfoDao.getSelfTelInfo(buyerId);
		} else if ("worktel".equals(type)) {// 工作电话
			return tRiskBuyerWorkinfoDao.getWorkTelByBuyerId(buyerId);
		} else if ("communcate".equals(type)) {// 通话记录
			return this.findCommunicateByBuyerId(buyerId);
		} else if ("cantact".equals(type)) {// 通讯录
			return tBuyerContactService.getContactsByBuyerId(buyerId);
		} else {// 夫妻(married),同事(workmate),父母(parent),子女(children),朋友(friend),亲戚(relatives)
			return tRiskBuyer2contactsDao.getSendMsgByBuyerIdAndType(buyerId, type);
		}
	}

	/**
	 * @Description 通过用户ID查询通讯录
	 * @param buyerId
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo>
	 */
	private List<TMisSendMsgInfo> findCommunicateByBuyerId(String buyerId) {
		//优先调用接口查询
		//通过buyerId查询手机号
		String mobile = tRiskBuyerPersonalInfoDao.findMobileByBuyerId(buyerId);
		if (StringUtils.isNotBlank(mobile)){
			List<TRiskBuyerContactRecords> contactRecords;
			try {
				contactRecords = recordsManager.findByByMobile(mobile);
				if (null != contactRecords && contactRecords.size() > 0) {
					return this.convertContactRecord2MsgInfo(contactRecords);
				}
			} catch (Exception e) {
				logger.info("获取"+mobile+"通讯录失败",e);
			}
		}
		//若未查找则查询数据库
		return tRiskBuyerContactRecordsDao.getCommunicateByBuyerId(buyerId);
	}

	/**
	 * @Description  将通话记录转为SendMsgInfo
	 * @param contactRecords
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo>
	 */
	private List<TMisSendMsgInfo> convertContactRecord2MsgInfo(List<TRiskBuyerContactRecords> contactRecords) {
		List<TMisSendMsgInfo> msgInfos = new ArrayList<TMisSendMsgInfo>(contactRecords.size());
		for (TRiskBuyerContactRecords records: contactRecords) {
			if (records == null){
				continue;
			}
			TMisSendMsgInfo msgInfo = new TMisSendMsgInfo();
			msgInfo.setMemo(records.getSumtime()+"");
			msgInfo.setTel(records.getTel());
			msgInfos.add(msgInfo);
		}
		return msgInfos;
	}

	/**
	 * 送并记录短信或者手机催款记录
	 * 
	 * @param task
	 * @param tMisContantRecord
	 * @return
	 */
	@Transactional(readOnly = false)
	public String smsGetTemp(TMisDunningTask task, TMisDunningOrder order, TMisContantRecord tMisContantRecord) {
		return getDunningSmsTemplate(task, order, DunningSmsTemplate.valueOf(tMisContantRecord.getSmstemp().name()));
	}

	/**
	 * 送并记录短信或者手机催款记录
	 * 
	 * @param task
	 * @param tMisContantRecord
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveRecord(TMisDunningTask task, TMisDunningOrder order, TMisContantRecord tMisContantRecord,
			String dunningtaskdbid) {

		String templateName = tMisContantRecord.getTemplateName();
		//判断是否给该订单发送短信成功的字段
		String smsSendSueOrFle="";
		try {
			if (tMisContantRecord.getContanttype() == TMisContantRecord.ContantType.sms) {
				/**
				 * 发送短信
				 */
				if(tMisContantRecord.getContent().contains("$")){
					throw new RuntimeException();
				}
				
				TmisDunningSmsTemplate tdsTmplate = tdstDao.getByName(templateName);
				if (tdsTmplate == null) {
					throw new RuntimeException("短信模板不存在");
				}
				tMisContantRecord.setSmsType(tdsTmplate.getSmsType());
				
				if ("wordText".equals(tMisContantRecord.getSmsType())) {
					Map<String, String> params = new HashMap<String, String>();
					// 发送手机号
					params.put("mobile", tMisContantRecord.getContanttarget());
					// snc版本 固定值:"2.0";
					params.put("snc_version", "2.0");
					// 业务名称 例："JHJJ","FXYL","XWHF","MIS";
					params.put("biz_sys", "MIS");
					// 发送类型 例："1","2","3","4"; 对应说明:验证码，营销，催收,系统
					params.put("biz_type", "dunning");
					// 客户端产品名称 例："mo9wallet","feishudai","feishudaiPro"
					if (tdsTmplate.getSmsCotent().contains("${platform}")) {
						if (null != order.getPlatformExt() && !"".equals(order.getPlatformExt())) {
							if (order.getPlatformExt().contains("feishudai")) {
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

					TMisDunningPeople tMisDunningPeople = tmisPeopleDao.get(task.getDunningpeopleid());
					TRiskBuyerPersonalInfo buyerInfeo = tRiskBuyerPersonalInfoDao.getbuyerIfo(order.getDealcode());
					if(null==buyerInfeo){
						throw new RuntimeException("发送短信失败");
					}
					Map<String, Object> map = this.getCotentValue(tdsTmplate.getSmsCotent(), buyerInfeo,
							order.getPlatformExt(), task.getDunningpeopleid(), tMisDunningPeople.getExtensionNumber());
					// 模板对应参数
					params.put("template_data", new JacksonConvertor().serialize(map));
					String englishTemplateName = tdsTmplate.getEnglishTemplateName();
					// 模板名称
					params.put("template_name", englishTemplateName);
					// 模板标识
					params.put("template_tags", "CN");
					MsfClient.instance().requestFromServer(ServiceAddress.SNC_SMS, params, BaseResponse.class);
				}
				if ("voice".equals(tMisContantRecord.getSmsType())) {

					Map<String, String> vparams = new HashMap<String, String>();
					vparams.put("mobile", tMisContantRecord.getContanttarget());// 发送手机号
					vparams.put("style", "voiceContent");// 固定值
					// 模板填充的map
					TMisDunningPeople tMisDunningPeople = tmisPeopleDao.get(task.getDunningpeopleid());
					TRiskBuyerPersonalInfo buyerInfeo = tRiskBuyerPersonalInfoDao.getbuyerIfo(order.getDealcode());
					if(null==buyerInfeo){
						throw new RuntimeException("发送短信失败");
					}
					Map<String, Object> map = this.getCotentValue(tdsTmplate.getSmsCotent(), buyerInfeo,
							order.getPlatformExt(), task.getDunningpeopleid(), tMisDunningPeople.getExtensionNumber());
					vparams.put("template_data", new JacksonConvertor().serialize(map));
					String englishTemplateName = tdsTmplate.getEnglishTemplateName();
					vparams.put("template_name", englishTemplateName);// 模板名称
					vparams.put("template_tags", "CN");// 模板标识
					// snc版本 固定值:"2.0";
					vparams.put("snc_version", "2.0");
					// 业务名称 例："JHJJ","FXYL","XWHF","MIS";
					vparams.put("biz_sys", "MIS");
					// 发送类型 例："1","2","3","4"; 对应说明:验证码，营销，催收,系统
					vparams.put("biz_type", "dunning");
					if (tdsTmplate.getSmsCotent().contains("${platform}")) {
						if (null != order.getPlatformExt() && !"".equals(order.getPlatformExt())) {
							if (order.getPlatformExt().contains("feishudai")) {
								vparams.put("product_name", "feishudai");
							} else {
								vparams.put("product_name", "mo9wallet");
							}
						} else {
							vparams.put("product_name", "mo9wallet");
						}

					} else {
						vparams.put("product_name", "mo9wallet");
					}
					MsfClient.instance().requestFromServer(ServiceAddress.SNC_VOICE, vparams, BaseResponse.class);
				}
				 logger.info("给用户:"+tMisContantRecord.getContanttarget()+"发送短信成功，模板为:"+templateName+",内容为:"+tMisContantRecord.getContent());
			}
		} catch (Exception e) {
			logger.warn("发送短信失败:" + e);
			logger.warn("给用户:" + tMisContantRecord.getContanttarget() +"发送短信成功，模板为:"+templateName+",内容为:"+ tMisContantRecord.getContent());
			smsSendSueOrFle="false";
		}
		try {
			TMisContantRecord dunning = new TMisContantRecord();
			// 任务id
			if (task == null) {
				dunning.setTaskid(null);
			} else {
				dunning.setTaskid(task.getId());
			}
			// 订单号
			dunning.setDealcode(order.dealcode);
			// 当前是否逾期
			if ("payoff".equals(order.status)) {
				dunning.setOrderstatus(true);
			} else {
				dunning.setOrderstatus(false);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String nowDate = formatter.format(new Date());
			String repaymentTime = formatter.format(order.repaymentDate);
			try {
				// 逾期天数
				dunning.setOverduedays(daysBetween(repaymentTime, nowDate));
			} catch (ParseException e) {
				logger.info("解析时间发生异常",e);
			}
			dunning.setDunningtime(new Date());
			// 联系对象
			dunning.setContanttarget(tMisContantRecord.getContanttarget());
			// 联系类型 短信，电话
			if (ContantType.sms == tMisContantRecord.getContanttype()) {
				dunning.setContanttype(ContantType.sms);
				// 短信内容
				dunning.setContent(tMisContantRecord.getContent());
				// dunning.setSmstemp(tMisContantRecord.getSmstemp());
				// 新的模板名
				dunning.setTemplateName(tMisContantRecord.getTemplateName());
				// 短信发送类型
				dunning.setSmsType(tMisContantRecord.getSmsType());
				// 保存短信id
				dunning.setSmsTemplateId(tMisContantRecord.getSmsTemplateId());
				dunning.setTelstatus(null);
			} else if (ContantType.tel == tMisContantRecord.getContanttype()) {
				dunning.setContanttype(ContantType.tel);
				dunning.setContent(null);
				dunning.setSmstemp(null);
				dunning.setTelstatus(tMisContantRecord.getTelstatus());
				if (null != task) {
					tMisDunningTaskDao.updateTelRemark(
							!"".equals(tMisContantRecord.getRemark()) ? tMisContantRecord.getRemark()
									: tMisContantRecord.getContanttarget() + tMisContantRecord.getTelstatus().getDesc(),
							task.getId());
				}
			}
			// 联系人对象类型
			dunning.setContactstype(tMisContantRecord.getContactstype());
			// 催讨人员id
			/*
			 * User user = UserUtils.getUser();
			 * if(StringUtils.isNotBlank(user.getId())){
			 * dunning.setDunningpeoplename(user.getName()+":"+Integer.valueOf(
			 * user.getId())); }
			 */
			dunning.setDunningpeoplename(task.getDunningpeopleid());

			// 应该还款时间
			dunning.setRepaymenttime(order.repaymentDate);
			dunning.setRemark(tMisContantRecord.getRemark());
			if("false".equals(smsSendSueOrFle)){
				dunning.setRemark("短信发送失败");
			}
			// 是否有效联络
			dunning.setIseffective(tMisContantRecord.getIseffective());
			// 承诺还款时间
			dunning.setPromisepaydate(tMisContantRecord.getPromisepaydate());
			// 联系人姓名
			dunning.setContactsname(tMisContantRecord.getContactsname());
			save(dunning);
			if (!"".equals(dunningtaskdbid) && null != dunningtaskdbid) {
				dunningTaskDao.updatedunningtime(dunningtaskdbid);
			}
		} catch (Exception e) {
//			throw new ServiceException(e);
			logger.warn("订单号为:"+order.getDealcode()+",保存到催收历史失败");
		}
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
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
	 * 根据催收任务获取对应催收短信模版的内容
	 * 
	 * @param task
	 * @param template
	 * @return
	 */
	public String getDunningSmsTemplate(TMisDunningTask task, TMisDunningOrder order, DunningSmsTemplate template) {
		List<String> args = new ArrayList<String>();

		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(task.getDealcode());

		String duningAmount = decimalFormat
				.format((int) Double.valueOf(personalInfo.getCreditAmount()).doubleValue() / 100D);// 应催金额
		int overdueDays = task.getCurrentOverdueDays();
		DunningUserInfo userInfo = this.tMisDunningTaskDao.findDunningUserInfo(task.getDealcode());

		String platformExt = order.getPlatformExt();

		String route = "【mo9】";
		if (platformExt != null && platformExt.contains("feishudai")) {
			route = "[飞鼠贷]";
		}

		args.add(route);
		switch (template) {
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
			args.add(userInfo != null ? userInfo.realName : "XXX");
			args.add(userInfo != null
					? userInfo.idCard.substring(userInfo.idCard.length() - 4, userInfo.idCard.length()) : "0000");
			args.add(Integer.toString(overdueDays));
			args.add(duningAmount);
			break;
		case ST_22_35:
			args.add(userInfo != null ? userInfo.realName : "XXX");
			args.add(userInfo != null
					? userInfo.idCard.substring(userInfo.idCard.length() - 4, userInfo.idCard.length()) : "0000");
			args.add(duningAmount);
			break;
		case ST_15_PLUS:
			args.add(userInfo != null ? userInfo.realName : "XXX");
			args.add(Integer.toString(overdueDays));
			args.add(duningAmount);
			args.add(userInfo != null ? userInfo.realName : "XXX");
			break;
		}
		return template.getContent(args.toArray(new String[] {}));
	}

	/**
	 * 根据催收任务获取对应催收短信模版的内容
	 * 
	 * @param DunningOrder
	 * @param template
	 * @return
	 */
	public String getDunningSmsTemplate(String route, DunningOrder order, DunningSmsTemplate template) {
		List<String> args = new ArrayList<String>();
		String duningAmount = decimalFormat.format(order.getCreditamount() / 100D);// 应催金额
		int overdueDays = order.getOverduedays();
		DunningUserInfo userInfo = this.tMisDunningTaskDao.findDunningUserInfo(order.getDealcode());

		args.add(route);

		switch (template) {
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
			args.add(userInfo != null ? userInfo.realName : "XXX");
			args.add(userInfo != null
					? userInfo.idCard.substring(userInfo.idCard.length() - 4, userInfo.idCard.length()) : "0000");
			args.add(Integer.toString(overdueDays));
			args.add(duningAmount);
			break;
		case ST_22_35:
			args.add(userInfo != null ? userInfo.realName : "XXX");
			args.add(userInfo != null
					? userInfo.idCard.substring(userInfo.idCard.length() - 4, userInfo.idCard.length()) : "0000");
			args.add(duningAmount);
			break;
		case ST_15_PLUS:
			args.add(userInfo != null ? userInfo.realName : "XXX");
			args.add(Integer.toString(overdueDays));
			args.add(duningAmount);
			args.add(userInfo != null ? userInfo.realName : "XXX");
			break;
		}
		return template.getContent(args.toArray(new String[] {}));
	}

	/**
	 * 根据订单号返回手机号码发送短信次数
	 * 
	 * @param dealcode
	 * @return
	 */
	public List<TelNumberBean> findSmsNum(String dealcode) {
		return dao.findSmsNum(dealcode);
	}

	/**
	 * 根据订单号&手机号&催收类型查询操作详情
	 * 
	 * @param contantRecord
	 * @return
	 */
	public List<TMisContantRecord> findDetailByDealcodeandTel(TMisContantRecord contantRecord) {
		return dao.findDetailByDealcodeandTel(contantRecord);
	}
	
    /**
     * 根据短信模板修改后更新催收历史记录
     * @param tDunningSmsTemplate
     */
//	@Transactional(readOnly = false)
//	public void updateTmisContatRwcord(TmisDunningSmsTemplate tDunningSmsTemplate) {
//		   TMisContantRecord tMisContantRecord=new TMisContantRecord();
//		   tMisContantRecord.setTemplateName(tDunningSmsTemplate.getTemplateName());
//		   tMisContantRecord.setSmsType(tDunningSmsTemplate.getSmsType());
//		   tMisContantRecord.setSmsTemplateId(tDunningSmsTemplate.getId());
//			tMisContantRecordDao.updateList(tMisContantRecord);
//			
//		
//	}

    /**
     * 
     * 获取短信填充的值
     * @param smsCotent
     * @return
     */
    public Map<String, Object> getCotentValue(String smsCotent,TRiskBuyerPersonalInfo buyerInfeo,String platformExt,String dunningpeopleid,String extensionNumber){
    	
//    	String dunningpeopleid = task.getDunningpeopleid();
    	
//    	TMisDunningPeople tMisDunningPeople = tmisPeopleDao.get(dunningpeopleid);
    	
//    	TRiskBuyerPersonalInfo buyerInfeo= tRiskBuyerPersonalInfoDao.getbuyerIfo(dealcode);
    	
    	SimpleDateFormat ss=new SimpleDateFormat("yyyy-MM-dd ");
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	
    	
    	if(smsCotent.contains("${platform}")){
    		if(null!=platformExt&&!"".equals(platformExt)){
	    		if(platformExt.contains("feishudai")){
	    			map.put("platform", "飞鼠贷");
	    		}else{
	    			map.put("platform", "mo9");
	    		}
    		}else{
    			map.put("platform", "mo9");
    		}
    		
    	}
    	
    	if(smsCotent.contains("${realName}")){
    		map.put("realName",buyerInfeo.getRealName() );
    	}
    	if(smsCotent.contains("${sex}")){
    		if("男".equals(buyerInfeo.getSex())){
        		map.put("sex","先生");
        	
        		}
        		if("女".equals(buyerInfeo.getSex())){
        			map.put("sex","女士");
        		}
    	}
    	if(smsCotent.contains("${idCard}")){
    		String idcard = buyerInfeo.getIdcard();
    		
    		StringBuilder sb=new StringBuilder(idcard);
    		sb= sb.replace(3, 15, "*********");
    		
    		map.put("idCard",sb.toString() );
    	}
    	if(smsCotent.contains("${creditamount}")){
    	String	creditAmount=String.valueOf((Double.valueOf(buyerInfeo.getCreditAmount()).doubleValue()/100D));
    		map.put("creditamount",creditAmount );
    	}
//    	if(null!=extensionNumber&&""!=extensionNumber){
        	if(smsCotent.contains("${extensionNumber}")){
        		map.put("extensionNumber",extensionNumber);
        	}
//    	}
    	if(smsCotent.contains("${creadateTime}")){
    		
    		map.put("creadateTime",ss.format(buyerInfeo.getCreateTime()) );
    	}
    	if(smsCotent.contains("${repaymentTime}")){
    		map.put("repaymentTime",ss.format(buyerInfeo.getRepaymentTime()) );
    	}
    	if(smsCotent.contains("${overduedays}")){
    		map.put("overduedays",buyerInfeo.getOverdueDays() );
    	}
    	if(smsCotent.contains("${todayDate}")){
    		map.put("todayDate",ss.format(new Date()) );
    	}
    	
    	return map;
    }	
    

}