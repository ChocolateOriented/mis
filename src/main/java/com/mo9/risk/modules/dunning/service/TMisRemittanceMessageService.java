/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisRemittanceMessageDao;
import com.mo9.risk.modules.dunning.entity.AlipayRemittanceExcel;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessagChecked;
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
public class TMisRemittanceMessageService extends
		CrudService<TMisRemittanceMessageDao, TMisRemittanceMessage> {

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

	public Page<TMisRemittanceMessage> findPage(Page<TMisRemittanceMessage> page,
			TMisRemittanceMessage tMisRemittanceMessage) {
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

	public TMisRemittanceMessage findRemittanceMesListByDealcode(String code) {
		return misRemittanceMessageDao.findRemittanceMesListByDealcode(code);
	}

	/**
	 * @Description 去重并保存汇款信息
	 * @param tMisRemittanceList
	 * @return int
	 */
	@Transactional(readOnly = false)
	public int saveUniqList(LinkedList<TMisRemittanceMessage> tMisRemittanceList, String channel) {
		List<TMisRemittanceMessage> trMList = misRemittanceMessageDao
				.findBySerialNumbers(tMisRemittanceList, channel);
		List<TMisRemittanceMessage> updateordList = new ArrayList<TMisRemittanceMessage>();
		List<TMisRemittanceMessage> updateNewList = new ArrayList<TMisRemittanceMessage>();
		if (trMList.size() > 0 && trMList != null) {
			for (TMisRemittanceMessage tMisRemittanceMessage : trMList) {
				if (AccountStatus.NOT_AUDIT.equals(tMisRemittanceMessage.getAccountStatus())) {
					updateordList.add(tMisRemittanceMessage);
				}
			}
		}
		if (updateordList.size() > 0 && updateordList != null) {
//			for (TMisRemittanceMessage ordMessage : updateordList) {
//				for (TMisRemittanceMessage tMisRemittanceMessage : tMisRemittanceList) {
//					updateNewList.add(tMisRemittanceMessage);
//				}
//			}
			for (int i = 0; i < updateordList.size(); i++) {
				for (int j = 0; j < tMisRemittanceList.size(); j++) {
					if (updateordList.get(i).getRemittanceSerialNumber()
							.equals(tMisRemittanceList.get(j).getRemittanceSerialNumber())) {
						updateNewList.add(tMisRemittanceList.get(j));
					}
				}
			}

		}
		tMisRemittanceList.removeAll(trMList);
		if (tMisRemittanceList.size() > 0 && tMisRemittanceList != null) {
			misRemittanceMessageDao.saveList(tMisRemittanceList);
		}
		if (updateNewList.size() > 0 && updateNewList != null) {
			//更新数据相同但是状态为未查账的.
			misRemittanceMessageDao.updateList(updateNewList, channel);
		}

		int updateNum = updateordList.size();
		int sameNum = trMList.size() - updateNum;
		return trMList.size();
	}

	/**
	 * @Description 财务上传时间在参数之后的汇款自动查账
	 * @param date
	 * @return void
	 */
	@Transactional(readOnly = false)
	public void autoAuditAfterFinancialtime(Date date) {
		List<TMisRemittanceMessage> list = dao.findAfterFinancialTimeNotAuditList(date);
		this.autoAudit(list);
	}

	/**
	 * @Description 自动查账, 借款订单与汇款信息匹配
	 * 若汇款信息备注中有手机号, 则使用备注手机号匹配未还款订单
	 * 备注中无手机号的 + 备注手机号未匹配成功的汇款信息, 若账号为手机号使用账号进行匹配
	 * @param remittanceMessages
	 *   需要查账的汇款信息
	 */
	@Transactional(readOnly = false)
	public void autoAudit(List<TMisRemittanceMessage> remittanceMessages) {
		if (null == remittanceMessages || remittanceMessages.size() == 0) {
			logger.info("汇款信息为空, 未进行自动查账");
			return;
		}
		logger.info("开始自动匹配,汇款信息共" + remittanceMessages.size() + "条");
		//获取备注中包含手机号的汇款信息
		HashMap<String, TMisRemittanceMessage> containMobileInRemarkMap = new HashMap<String, TMisRemittanceMessage>(
				remittanceMessages.size());
		for (TMisRemittanceMessage remittanceMessage : remittanceMessages) {
			if (null == remittanceMessage) {
				continue;
			}
			//TODO 区分未查账信息
			//只针对未查账订单
//			if (AccountStatus.NOT_AUDIT != remittanceMessage.getAccountStatus()) {
//				continue;
//			}
			//检查备注中是否包含手机号
			String remark = remittanceMessage.getRemark();
			String mobile = RegexUtil.getStringValueByRegex(RegexUtil.REGEX_CONTAIN_MOBILE, remark);
			if (StringUtils.isNoneBlank(mobile)) {//备注包含手机号
				containMobileInRemarkMap.put(mobile, remittanceMessage);
			}
		}
		List<TMisRemittanceMessage> successMatchByRemark = this
				.matchOrderWithMobil(containMobileInRemarkMap);
		logger.info("通过备注匹配成功:" + successMatchByRemark.size() + "条");

		//使用作为账号的手机号匹配(备注匹配失败 + 备注中不包含手机号)
		Collection<TMisRemittanceMessage> containMobileInRemark = containMobileInRemarkMap.values();
		remittanceMessages.removeAll(containMobileInRemark);
		containMobileInRemark.removeAll(successMatchByRemark);
		List<TMisRemittanceMessage> checkRemittanceAccountList = new ArrayList<TMisRemittanceMessage>(
				remittanceMessages.size() + containMobileInRemark.size());
		checkRemittanceAccountList.addAll(remittanceMessages);
		checkRemittanceAccountList.addAll(containMobileInRemark);

		HashMap<String, TMisRemittanceMessage> accountIsMobileMap = new HashMap<String, TMisRemittanceMessage>(
				remittanceMessages.size());
		for (TMisRemittanceMessage remittanceMessage : checkRemittanceAccountList) {
			//只针对未查账订单
//			if (AccountStatus.NOT_AUDIT != remittanceMessage.getAccountStatus()) {
//				continue;
//			}
			//检查账号是否使手机号
			String account = remittanceMessage.getRemittanceAccount();
			String mobile = RegexUtil.getStringValueByRegex(RegexUtil.REGEX_MOBILE, account);
			if (StringUtils.isNotBlank(mobile)) {
				accountIsMobileMap.put(mobile, remittanceMessage);
			}
		}
		List<TMisRemittanceMessage> successMatchByAccount = this
				.matchOrderWithMobil(accountIsMobileMap);
		logger.info("通过账号匹配成功:" + successMatchByAccount.size() + "条");
	}

	/**
	 * @Description 通过电话匹配订单
	 * @param containMobileMap
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage>
	 */
	@Transactional(readOnly = false)
	public List<TMisRemittanceMessage> matchOrderWithMobil(
			HashMap<String, TMisRemittanceMessage> containMobileMap) {
		if (null == containMobileMap) {
			return new ArrayList<TMisRemittanceMessage>();
		}
		Set<String> mobiles = containMobileMap.keySet();
		if (mobiles.size() == 0) {
			return new ArrayList<TMisRemittanceMessage>();
		}
		//通过手机号批量查询未还款订单的用户信息
		List<DunningOrder> orders = dao.findPaymentOrderByMobiles(new ArrayList<String>(mobiles));
		if (orders == null || orders.size() == 0) {
			return new ArrayList<TMisRemittanceMessage>();
		}

		//匹配订单
		List<TMisRemittanceMessage> successMatchList = new ArrayList<TMisRemittanceMessage>(
				orders.size());
		for (DunningOrder order : orders) {
			if (order == null) {
				continue;
			}
			String mobile = order.getMobile();
			TMisRemittanceMessage remittanceMessage = containMobileMap.get(mobile);
			if (remittanceMessage == null) {
				continue;
			}
			logger.debug(
					"订单:" + order.getDealcode() + "与汇款信息" + remittanceMessage.getRemittanceSerialNumber()
							+ "匹配成功");
			//TODO 查账完成生成comfirm数据
//			remittanceMessage.setDealcode(order.getDealcode());
//			remittanceMessage.setAccountStatus(AccountStatus.COMPLETE_AUDIT);
//			this.autoAddTemittanceTag(remittanceMessage, order);
//			successMatchList.add(remittanceMessage);
		}

//		dao.batchUpdateMatched(successMatchList);
		return successMatchList;
	}

	/**
	 * @Description 自动对还款行为打标签
	 * @param remittanceMessage
	 * @param order
	 * @return void
	 */
	private void autoAddTemittanceTag(TMisRemittanceMessage remittanceMessage, DunningOrder order) {
		String realName = order.getRealname();
		if (StringUtils.isBlank(realName)) {
			return;
		}
		//TODO 入账标签打在comfirm表上
		//如果账户姓名等于借款人姓名则标记本人还款
		String remittanceName = remittanceMessage.getRemittanceName();
		if (realName.equals(remittanceName)) {
//			remittanceMessage.setRemittanceTag(RemittanceTag.REPAYMENT_SELF);
			return;
		}
		//账户名不等, 若备注中包含借款人姓名则标记第三方还款
		String remark = remittanceMessage.getRemark();
		if (StringUtils.isBlank(remark)) {
			return;
		}
		if (remark.contains(realName)) {
//			remittanceMessage.setRemittanceTag(RemittanceTag.REPAYMENT_THIRD);
		}
	}

	/**
	 * @Description 获取有效支付宝汇款信息
	 * @param srcData
	 * @param validData
	 * @return java.lang.String
	 */
	public String getValidRemittanceMessage(List<AlipayRemittanceExcel> srcData,
			List<TMisRemittanceMessage> validData) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//记录解析失败的条数
		int fail = 0;

		Date uploadDate = new Date();
		StringBuilder errorMsg = new StringBuilder();
		for (AlipayRemittanceExcel trExcel : srcData) {
			if (!"转账".equals(trExcel.getServeType())) {
				continue;
			}
			try {
				BeanValidators.validateWithException(validator, trExcel);
			} catch (ConstraintViolationException ex) {
				errorMsg.append("数据不符合规范:流水号为" + trExcel.getAlipaySerialNumber() + "。");
				fail++;
				continue;
			}

			Date parseTime = null;
			try {
				parseTime = sd.parse(trExcel.getRemittancetime());
			} catch (ParseException e) {
				errorMsg.append("时间格式错误:流水号为" + trExcel.getAlipaySerialNumber() + "。");
				fail++;
				continue;
			}

			TMisRemittanceMessage trMessage = new TMisRemittanceMessage();
			trMessage.setRemittanceTime(parseTime);
			trMessage.setRemittanceSerialNumber(trExcel.getAlipaySerialNumber());
			trMessage.setRemittanceChannel("alipay");
			trMessage.setRemittanceAmount(trExcel.getRemittanceamount());
			trMessage.setRemittanceName(trExcel.getRemittancename());
			trMessage.setRemittanceAccount(trExcel.getRemittanceaccount());
			trMessage.setRemark(trExcel.getRemark());
			trMessage.setFinancialTime(uploadDate);
			trMessage.setFinancialUser(UserUtils.getUser().getName());
			trMessage.preInsert();
			validData.add(trMessage);
		}
		return fail > 0 ? ",失败" + fail + "条,失败原因:" + errorMsg : "";
	}

	/**
	 * 查询所有的对公明细
	 * @return
	 */
	public Page<TMisRemittanceMessage> findAcountPageList(Page<TMisRemittanceMessage> page,
			TMisRemittanceMessage entity) {
		entity.setPage(page);
		page.setList(dao.findAccountPageList(entity));
		return page;
	}

	/**
	 * 查询已查账的所有数据
	 * @param page
	 * @return
	 */
	public Page<TMisRemittanceMessagChecked> findMessagCheckedList(
			Page<TMisRemittanceMessagChecked> page,
			TMisRemittanceMessagChecked entity) {
		entity.setPage(page);
		return page;
	}

	/**
	 * @Description 通过电话查询未还款订单
	 * @param mobile
	 * @return com.mo9.risk.modules.dunning.entity.DunningOrder
	 */
	public DunningOrder findPaymentOrderByMobile(String mobile) {
		List<DunningOrder> dunningOrders = dao.findPaymentOrderByMobile(mobile);
		if (dunningOrders != null && dunningOrders.size() > 0) {
			return dunningOrders.get(0);
		}
		return null;
	}

	/**
	 * @Description  查找未完成的汇款
	 * @param remittanceChannel
	 * @param remittanceSerialNumber
	 * @return com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage
	 */
	public List<TMisRemittanceMessage> findNotFinish(String remittanceChannel, String remittanceSerialNumber) {
		return dao.findNotFinish(remittanceChannel,remittanceSerialNumber);
	}
}