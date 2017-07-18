/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisRemittanceMessageDao;
import com.mo9.risk.modules.dunning.entity.AlipayRemittanceExcel;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm.ConfirmFlow;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm.RemittanceTag;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessagChecked;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.mo9.risk.util.RegexUtil;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 财务确认汇款信息Service
 *
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
	private TMisRemittanceConfirmService remittanceConfirmService;
	@Autowired
	private TMisDunningOrderService dunningOrderService;
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
	 *
	 * @param tMisRemittanceList
	 * @param channel
	 * @param listNum
	 * @return
	 */
	@Transactional(readOnly = false)
	public String saveUniqList(LinkedList<TMisRemittanceMessage> tMisRemittanceList, String channel, List<Integer> listNum)
			throws Exception {
		int saveNum = 0;
		int totals = tMisRemittanceList.size();
		List<TMisRemittanceMessage> trMList = misRemittanceMessageDao.findBySerialNumbers(tMisRemittanceList, channel);
		List<TMisRemittanceMessage> updateordList = new ArrayList<TMisRemittanceMessage>();
		List<TMisRemittanceMessage> updateNewList = new ArrayList<TMisRemittanceMessage>();
		if (!CollectionUtils.isEmpty(trMList)) {
			for (TMisRemittanceMessage tMisRemittanceMessage : trMList) {
				if (StringUtils.isEmpty(tMisRemittanceMessage.getAccountStatus())) {
					updateordList.add(tMisRemittanceMessage);
				}
			}
		}
		if (!CollectionUtils.isEmpty(updateordList)) {
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
		if (!CollectionUtils.isEmpty(tMisRemittanceList)) {
			saveNum = misRemittanceMessageDao.saveList(tMisRemittanceList);
		}
		if (!CollectionUtils.isEmpty(updateNewList)) {
			//更新数据相同但是状态为未查账的.
			for (TMisRemittanceMessage tMisRemittanceMessage : updateNewList) {

				misRemittanceMessageDao.updateList(tMisRemittanceMessage, channel);
			}
		}

		int updateNum = updateNewList.size();
		int sameNum = totals - updateNum - saveNum;
		listNum.add(sameNum);
		listNum.add(updateNum);
		listNum.add(saveNum);
		return "重复" + sameNum + "条,更新 " + updateNum + "条。";
	}

	/**
	 * @return void
	 * @Description 财务上传时间在参数之后的汇款自动查账
	 */
	@Transactional(readOnly = false)
	public void autoAuditAfterFinancialtime(Date date) {
		List<TMisRemittanceMessage> list = dao.findAfterFinancialTimeNotAuditList(date);
		this.autoAudit(list);
	}

	/**
	 * @param remittanceMessages 需要查账的汇款信息
	 * @Description 自动查账, 借款订单与汇款信息匹配 若汇款信息备注中有手机号, 则使用备注手机号匹配未还款订单 备注中无手机号的 + 备注手机号未匹配成功的汇款信息, 若账号为手机号使用账号进行匹配
	 */
	@Transactional(readOnly = false)
	public void autoAudit(List<TMisRemittanceMessage> remittanceMessages) {
		if (null == remittanceMessages || remittanceMessages.size() == 0) {
			logger.info("汇款信息为空, 未进行自动查账");
			return;
		}
		logger.info("开始自动匹配,汇款信息共" + remittanceMessages.size() + "条");

		List<TMisRemittanceMessage> successMatch = new ArrayList<TMisRemittanceMessage>(remittanceMessages.size());
		List<TMisRemittanceConfirm> completeAudit = new ArrayList<TMisRemittanceConfirm>(remittanceMessages.size());

		//获取备注中包含手机号的汇款信息
		for (TMisRemittanceMessage remittanceMessage : remittanceMessages) {
			if (null == remittanceMessage) {
				continue;
			}
			//检查备注中是否包含手机号
			String remark = remittanceMessage.getRemark();
			String mobile = RegexUtil.getStringValueByRegex(RegexUtil.REGEX_CONTAIN_MOBILE, remark);
			//通过备注匹配
			TMisRemittanceConfirm remittanceConfirm = this.matchOrderWithMobil(remittanceMessage, mobile);
			if (remittanceConfirm == null) {
				continue;
			}
			successMatch.add(remittanceMessage);
			completeAudit.add(remittanceConfirm);
		}

		//使用作为账号的手机号匹配(备注匹配失败 + 备注中不包含手机号)
		remittanceMessages.removeAll(successMatch);
		for (TMisRemittanceMessage remittanceMessage : remittanceMessages) {
			//检查账号是否使手机号
			String account = remittanceMessage.getRemittanceAccount();
			String mobile = RegexUtil.getStringValueByRegex(RegexUtil.REGEX_MOBILE, account);
			//通过账号匹配
			TMisRemittanceConfirm remittanceConfirm = this.matchOrderWithMobil(remittanceMessage, mobile);
			if (remittanceConfirm == null) {
				continue;
			}
			successMatch.add(remittanceMessage);
			completeAudit.add(remittanceConfirm);
		}
		logger.info("匹配成功:" + successMatch.size() + "条");    //生成remittanceConfirm

		User user = new User();
		user.setName("sys");
		remittanceConfirmService.batchInsert(completeAudit, user);
	}

	/**
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage>
	 * @Description 通过电话匹配订单
	 */
	@Transactional(readOnly = false)
	private TMisRemittanceConfirm matchOrderWithMobil(TMisRemittanceMessage remittanceMessage, String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return null;
		}
		//查找订单
		DunningOrder order = dunningOrderService.findPaymentOrderMsgByMobile(mobile);
		if (order == null) {
			return null;
		}
		logger.debug(
				"订单:" + order.getDealcode() + "与汇款信息" + remittanceMessage.getRemittanceSerialNumber()
						+ "匹配成功");
		TMisRemittanceConfirm remittanceConfirm = this.createRemittanceConfirm(remittanceMessage, order);
		this.autoAddTemittanceTag(remittanceMessage, order, remittanceConfirm);
		return remittanceConfirm;
	}

	/**
	 * @return com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm
	 * @Description 生成汇款确认信息
	 */
	private TMisRemittanceConfirm createRemittanceConfirm(TMisRemittanceMessage remittanceMessage, DunningOrder order) {
		TMisRemittanceConfirm remittanceConfirm = new TMisRemittanceConfirm();
		remittanceConfirm.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_COMPLETE_AUDIT);
		remittanceConfirm.setConfirmFlow(ConfirmFlow.AUDIT);

		remittanceConfirm.setDealcode(order.getDealcode());
		remittanceConfirm.setMobile(order.getMobile());
		remittanceConfirm.setName(order.getRealname());
		remittanceConfirm.setBuyerId(String.valueOf(order.getBuyerid()));

		remittanceConfirm.setRemittancename(remittanceMessage.getRemittanceName());
		remittanceConfirm.setRemittancetime(remittanceMessage.getRemittanceTime());
		remittanceConfirm.setRemittanceamount(remittanceMessage.getRemittanceAmount());
		remittanceConfirm.setRemittancechannel(remittanceMessage.getRemittanceChannel());
		remittanceConfirm.setFinancialserialnumber(remittanceMessage.getRemittanceSerialNumber());

		return remittanceConfirm;
	}

	/**
	 * @return void
	 * @Description 自动对还款行为打标签
	 */
	private void autoAddTemittanceTag(TMisRemittanceMessage remittanceMessage, DunningOrder order,
			TMisRemittanceConfirm remittanceConfirm) {
		String realName = order.getRealname();
		if (StringUtils.isBlank(realName)) {
			return;
		}
		//如果账户姓名等于借款人姓名则标记本人还款
		String remittanceName = remittanceMessage.getRemittanceName();
		if (realName.equals(remittanceName)) {
			remittanceConfirm.setRemittanceTag(RemittanceTag.REPAYMENT_SELF);
			return;
		}
		//账户名不等,查看备注
		String remark = remittanceMessage.getRemark();
		if (StringUtils.isBlank(remark)) {
			return;
		}
		//若备注中包含借款人姓名则标记第三方还款
		if (remark.contains(realName)) {
			remittanceConfirm.setRemittanceTag(RemittanceTag.REPAYMENT_THIRD);
		}
	}

	/**
	 * @return java.lang.String
	 * @Description 获取有效支付宝汇款信息
	 */
	public String getValidRemittanceMessage(List<AlipayRemittanceExcel> srcData,
			List<TMisRemittanceMessage> validData, List<Integer> listNum) {
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
			trMessage.setRemittanceSerialNumber(trExcel.getAlipayRemittanceNumber());
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
		listNum.add(validData.size() + fail);
		listNum.add(fail);
		return fail > 0 ? "失败" + fail + "条,失败原因:" + errorMsg : "";
	}

	/**
	 * 查询所有的对公明细
	 */
	public Page<TMisRemittanceMessage> findAcountPageList(Page<TMisRemittanceMessage> page, TMisRemittanceMessage entity) {
		entity.setPage(page);
		page.setList(dao.findAccountPageList(entity));
		return page;
	}

	/**
	 * 查询已查账的huozhe yiwanchengde 所有数据
	 */
	public Page<TMisRemittanceMessagChecked> findMessagList(Page<TMisRemittanceMessagChecked> page, TMisRemittanceMessagChecked entity,
			String childPage) {
		entity.setPage(page);
		if ("completed".equals(childPage)) {
			page.setList(dao.findMessagCompletedList(entity));
		} else {
			page.setList(dao.findMessagCheckedList(entity));
		}
		return page;
	}

	/**
	 * @return com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage
	 * @Description 查找未完成的汇款
	 */
	public List<TMisRemittanceConfirm> findNotFinish(String remittanceChannel, String remittanceSerialNumber) {
		return dao.findNotFinish(remittanceChannel, remittanceSerialNumber);
	}

	/**
	 * @return boolean 成功true 失败false
	 * @Description 手工查账
	 */
	@Transactional
	public boolean handleAudit(TMisRemittanceConfirm remittanceConfirm) {
		//查询订单
		DunningOrder order = dunningOrderService.findPaymentOrderMsgByDealcode(remittanceConfirm.getDealcode());
		if (order == null) {
			return false;
		}

		//查询汇款信息
		TMisRemittanceMessage q_remittanceMessage = new TMisRemittanceMessage();
		q_remittanceMessage.setRemittanceSerialNumber(remittanceConfirm.getSerialnumber());
		q_remittanceMessage.setRemittanceChannel(remittanceConfirm.getRemittancechannel());
		List<TMisRemittanceMessage> remittanceMessages = this.findList(q_remittanceMessage);
		if (remittanceMessages == null || remittanceMessages.size() == 0) {
			return false;
		}
		TMisRemittanceMessage remittanceMessage = remittanceMessages.get(0);
		if (remittanceMessage == null) {
			return false;
		}

		//生成TMisRemittanceConfirm
		TMisRemittanceConfirm new_tMisRemittanceConfirm = this.createRemittanceConfirm(remittanceMessage, order);
		String id = remittanceConfirm.getId();
		if (StringUtils.isNotBlank(id)) {
			new_tMisRemittanceConfirm.setId(id);
		}
		new_tMisRemittanceConfirm.setRemittanceTag(remittanceConfirm.getRemittanceTag());
		remittanceConfirmService.save(new_tMisRemittanceConfirm);
		return true;
	}

	/**
	 * 获取汇款确认信息
	 */
	public TMisRemittanceMessagChecked findRemittanceMessagChecked(String remittanceConfirmId) {
		return dao.findRemittanceMessagChecked(remittanceConfirmId);
	}
}