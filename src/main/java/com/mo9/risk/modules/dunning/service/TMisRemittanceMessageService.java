/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisRemittanceMessageDao;
import com.mo9.risk.modules.dunning.entity.AlipayRemittanceExcel;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage.AccountStatus;
import com.mo9.risk.util.RegexUtil;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 财务确认汇款信息Service
 * @author 徐盛
 * @version 2016-08-11
 */
@Service
@Transactional(readOnly = true)
public class TMisRemittanceMessageService extends CrudService<TMisRemittanceMessageDao, TMisRemittanceMessage> {
	@Autowired
	private TMisRemittanceMessageDao misRemittanceMessageDao;
	@Autowired
	protected Validator validator;

	public TMisRemittanceMessage get(String id) {
		return super.get(id);
	}
	
	public List<TMisRemittanceMessage> findList(TMisRemittanceMessage tMisRemittanceMessage) {
		return super.findList(tMisRemittanceMessage);
	}
	
	public Page<TMisRemittanceMessage> findPage(Page<TMisRemittanceMessage> page, TMisRemittanceMessage tMisRemittanceMessage) {
		return super.findPage(page, tMisRemittanceMessage);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisRemittanceMessage tMisRemittanceMessage) {
		super.save(tMisRemittanceMessage);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisRemittanceMessage tMisRemittanceMessage) {
		super.delete(tMisRemittanceMessage);
	}
	
	@Transactional(readOnly = false)
	public void insert(TMisRemittanceMessage tMisRemittanceMessage) {
		tMisRemittanceMessage.preInsert();
		dao.insert(tMisRemittanceMessage);
	}
	
	public TMisRemittanceMessage  findRemittanceMesListByDealcode(String code){
		return misRemittanceMessageDao.findRemittanceMesListByDealcode(code);
	}

	/**
	 * @Description 去重并保存汇款信息
	 * @param tMisRemittanceList
	 * @return int
	 */
	@Transactional(readOnly = false)
	public int saveUniqList(LinkedList<TMisRemittanceMessage> tMisRemittanceList,String channel) {
		List<TMisRemittanceMessage> trMList=misRemittanceMessageDao.findBySerialNumbers(tMisRemittanceList,channel);
		int same=trMList.size();
		if(trMList.size()>0&&trMList!=null){
			tMisRemittanceList.removeAll(trMList);
		}
		if(tMisRemittanceList.size()>0&&tMisRemittanceList!=null){
			misRemittanceMessageDao.saveList(tMisRemittanceList);
		}
		return same;
	}

	/**
	 * @Description 自动查账, 借款订单与汇款信息匹配
	 * 若汇款信息备注中有手机号, 则使用备注手机号匹配未还款订单
	 * 备注中无手机号的 + 备注手机号未匹配成功的汇款信息, 若账号为手机号使用账号进行匹配
	 * @param remittanceMessages
	 *   需要查账的汇款信息
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage>
	 *   匹配成功的汇款信息
	 */
	@Transactional(readOnly = false)
	public List<TMisRemittanceMessage> autoAudit(List<TMisRemittanceMessage> remittanceMessages){
		if (null == remittanceMessages || remittanceMessages.size()== 0 ){
			logger.info("汇款信息为空, 未进行自动查账");
			return null;
		}
		//获取备注中包含手机号的汇款信息
		HashMap<String,TMisRemittanceMessage> containMobileInRemarkMap = new HashMap<String, TMisRemittanceMessage>(remittanceMessages.size());
		for (TMisRemittanceMessage remittanceMessage: remittanceMessages ) {
			if (null == remittanceMessage) {
				continue;
			}
			//只针对未查账订单
			if (AccountStatus.NOT_AUDIT != remittanceMessage.getAccountStatus()) {
				continue;
			}
			//检查备注中是否包含手机号
			String remark = remittanceMessage.getRemark();
			String mobile = RegexUtil.getStringValueByRegex(RegexUtil.REGEX_CONTAIN_MOBILE,remark);
			if (StringUtils.isNoneBlank(mobile)){//备注包含手机号
				containMobileInRemarkMap.put(mobile,remittanceMessage);
			}
		}
		List<TMisRemittanceMessage> successMatchByRemark = this.matchOrderWithMobil(containMobileInRemarkMap);

		//使用作为账号的手机号匹配(备注匹配失败 + 备注中不包含手机号)
		Collection<TMisRemittanceMessage> containMobileInRemark = containMobileInRemarkMap.values();
		remittanceMessages.removeAll(containMobileInRemark);
		containMobileInRemark.removeAll(successMatchByRemark);
		List<TMisRemittanceMessage> checkRemittanceAccountList = new ArrayList<TMisRemittanceMessage>(remittanceMessages.size()+containMobileInRemark.size());
		checkRemittanceAccountList.addAll(remittanceMessages);
		checkRemittanceAccountList.addAll(containMobileInRemark);

		HashMap<String,TMisRemittanceMessage> accountIsMobileMap = new HashMap<String, TMisRemittanceMessage>(remittanceMessages.size());
		for (TMisRemittanceMessage remittanceMessage : checkRemittanceAccountList) {
			//只针对未查账订单
			if (AccountStatus.NOT_AUDIT != remittanceMessage.getAccountStatus()) {
				continue;
			}
			//检查账号是否使手机号
			String account = remittanceMessage.getRemittanceaccount();
			String mobile = RegexUtil.getStringValueByRegex(RegexUtil.REGEX_MOBILE,account);
			if (StringUtils.isNotBlank(mobile)){
				accountIsMobileMap.put(mobile,remittanceMessage);
			}
		}
		List<TMisRemittanceMessage> successMatchByAccount = this.matchOrderWithMobil(containMobileInRemarkMap);

		List<TMisRemittanceMessage> successMatch = new ArrayList<TMisRemittanceMessage>(successMatchByAccount.size()+successMatchByRemark.size());
		successMatch.addAll(successMatchByRemark);
		successMatch.addAll(successMatchByAccount);
		return successMatch ;

		/**
		 * TODO 还款标签
		 * 备注中姓名与借款人姓名比对
		 * 	不等>不打标签
		 * 	相等>账户名与借款人姓名比对
		 * 		相等>本人还款
		 * 		不相等>第三方还款
		 */
		//本人还款
		//第三方还款
		//不确定
	}

	/**
	 * @Description 通过电话匹配订单
	 * @param containMobileMap
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage>
	 */
	@Transactional(readOnly = false)
	public List<TMisRemittanceMessage> matchOrderWithMobil(HashMap<String, TMisRemittanceMessage> containMobileMap) {
		if (null == containMobileMap){
			return null;
		}
		Set<String> mobiles = containMobileMap.keySet();
		if (mobiles.size()==0){
			return null;
		}
		//通过手机号批量查询未还款订单的用户信息
		List<DunningOrder> orders = dao.findPaymentOrderByMobile(mobiles);
		if (orders ==null || orders.size()==0){
			return null;
		}

		//匹配订单
		List<TMisRemittanceMessage> successMatchList = new ArrayList<TMisRemittanceMessage>(orders.size());
		for (DunningOrder order: orders) {
			if (order == null){
				continue;
			}
			String mobile = order.getMobile();
			TMisRemittanceMessage remittanceMessage = containMobileMap.get(mobile);
			if (remittanceMessage == null){
				continue;
			}
			remittanceMessage.setDealcode(order.getDealcode());
			remittanceMessage.setAccountStatus(AccountStatus.COMPLETE_AUDIT);
			this.addTemittanceTag(remittanceMessage,order);
			successMatchList.add(remittanceMessage);
		}
		dao.batchUpdateMatched(successMatchList);
		return successMatchList;
	}

	/**
	 * @Description 对还款行为打标签
	 * @param remittanceMessage
	 * @param order
	 * @return void
	 */
	private void addTemittanceTag(TMisRemittanceMessage remittanceMessage, DunningOrder order) {

	}

	/**
	 * @Description  获取有效支付宝汇款信息
	 * @param srcData
	 * @param validData
	 * @return java.lang.String
	 */
	public String getValidRemittanceMessage(List<AlipayRemittanceExcel> srcData, List<TMisRemittanceMessage> validData) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//记录解析失败的条数
		int fail = 0;

		Date uploadDate = new Date();
		StringBuilder errorMsg = new StringBuilder();
		for (AlipayRemittanceExcel trExcel : srcData) {
			if (!"转账".equals(trExcel.getServeType())) {
				continue;
			}
			try{
				BeanValidators.validateWithException(validator, trExcel);
			}catch(ConstraintViolationException ex){
				errorMsg.append("数据不符合规范:流水号为" + trExcel.getAlipaySerialNumber()+"<br/>");
				fail++;
				continue;
			}

			Date parseTime = null;
			try {
				parseTime = sd.parse(trExcel.getRemittancetime());
			} catch (ParseException e) {
				errorMsg.append("时间格式错误:流水号为" + trExcel.getAlipaySerialNumber()+"<br/>");
				fail++;
				continue;
			}

			TMisRemittanceMessage trMessage = new TMisRemittanceMessage();
			trMessage.setRemittancetime(parseTime);
			trMessage.setRemittanceSerialNumber(trExcel.getAlipaySerialNumber());
			trMessage.setRemittancechannel("alipay");
			trMessage.setRemittanceamount(trExcel.getRemittanceamount());
			trMessage.setRemittancename(trExcel.getRemittancename());
			trMessage.setRemittanceaccount(trExcel.getRemittanceaccount());
			trMessage.setRemark(trExcel.getRemark());
			trMessage.setAccountStatus(AccountStatus.NOT_AUDIT);
			trMessage.setFinancialtime(uploadDate);
			trMessage.setFinancialuser(UserUtils.getUser().getName());
			trMessage.preInsert();
			validData.add(trMessage);
		}
		return fail > 0 ? ",失败"+fail+"条,失败原因:<br/>" + errorMsg : "";
	}
}