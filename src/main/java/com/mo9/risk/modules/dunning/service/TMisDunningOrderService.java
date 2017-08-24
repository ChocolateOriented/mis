/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisDunningOrderDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.mo9.risk.modules.dunning.enums.PayStatus;
import com.mo9.risk.modules.dunning.manager.RiskOrderManager;
import com.mo9.risk.util.MailSender;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 订单
 * @author jxli
 * @version 2017/7/6
 */
@Service
@Lazy(false)
@Transactional(readOnly = true)
public class TMisDunningOrderService extends BaseService{

	@Autowired
	private TMisDunningOrderDao tMisDunningOrderDao;
	@Autowired
	private TMisRemittanceConfirmService confirmService;
	@Autowired
	private TMisDunningDeductService deductService;
	@Autowired
	private RiskOrderManager orderManager;

	/***
	 * @Description  通过订单号查询订单
	 * @param dealcode
	 * @return com.mo9.risk.modules.dunning.entity.DunningOrder
	 */
	public DunningOrder findOrderByDealcode(String dealcode) {
		return tMisDunningOrderDao.findOrderByDealcode(dealcode);
	}

	/**
	 * @Description 查询未还款订单, 包含借款人信息
	 * @param queryOrder
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	public DunningOrder findPaymentOrderMsg(DunningOrder queryOrder){
		List<DunningOrder> orders = tMisDunningOrderDao.findPaymentOrderMsg(queryOrder);
		if (null == orders || orders.size() == 0){
			return null ;
		}
		return orders.get(0);
	}

	/**
	 * @Description 通过手机查询未还款订单, 包含借款人信息
	 * @param mobile
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	public DunningOrder findPaymentOrderMsgByMobile(String mobile){
		DunningOrder queryOrder = new DunningOrder();
		queryOrder.setMobile(mobile);
		return findPaymentOrderMsg(queryOrder);
	}

	/**
	 * @Description 通过订单号查询未还款订单, 包含借款人信息
	 * @param dealcode
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	public DunningOrder findPaymentOrderMsgByDealcode(String dealcode) {
		DunningOrder queryOrder = new DunningOrder();
		queryOrder.setDealcode(dealcode);
		return findPaymentOrderMsg(queryOrder);
	}

	/**
	 * @Description 查询未还款订单详情包含借款人, 催收任务信息
	 * @param order
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	DunningOrder findPaymentOrderDetail(DunningOrder order){
		List<DunningOrder> dunningOrders = tMisDunningOrderDao.findPaymentOrderDetail(order);
		if (dunningOrders != null && dunningOrders.size() > 0) {
			return dunningOrders.get(0);
		}
		return null;
	}

	/**
	 * @return com.mo9.risk.modules.dunning.entity.DunningOrder
	 * @Description 通过电话查询未还款订单详情, 包含借款人, 催收任务信息
	 */
	public DunningOrder findPaymentOrderDetailByMobile(String mobile) {
		DunningOrder order = new DunningOrder();
		order.setMobile(mobile);
		return findPaymentOrderDetail(order);
	}

	/**
	 * 定时检查催收已确认还清, 但是江湖救急处理异常的订单, 尝试重新调用3次还清接口, 若还是不行发送邮件
	 * 每6小时触发一次, 临时解决方案
	 */
	@Scheduled(cron = "0 0 */6 * * ?")
	public void checkAbnormalOrder(){
		logger.info("开始检查状态异常订单");
		//尝试修复对公入账异常订单
		try {
			this.tryRepairAbnormalRemittanceConfirm();
		}catch (Exception e){
			logger.info("尝试修复对公入账异常订单发生错误",e);
		}

		//尝试修复代扣异常订单
		DynamicDataSource.setCurrentLookupKey("dataSource");
		try {
			this.tryRepairAbnormalDeduct();
		} catch (Exception e){
			logger.info("尝试修复代扣异常订单发生错误",e);
		}
	}

	/**
	 * @Description 尝试修复代扣异常订单
	 * @param
	 * @return java.util.List<java.lang.String>
	 */
	private void tryRepairAbnormalDeduct() {
		//查询代扣异常订单
		List<TMisDunningDeduct> abnormalDeducts = deductService.findAbnormalDeduct();

		//切换江湖救急库, 查询订单状态也为未还清的订单, 过滤未同步订单
		List<String> shouldPayoffOrderDelcodes = new ArrayList<String>();
		for (TMisDunningDeduct deduct: abnormalDeducts) {
			shouldPayoffOrderDelcodes.add(deduct.getDealcode());
		}
		DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");
		int successCount = 0;
		StringBuilder failRepairMsg = new StringBuilder();
		List<String> abnormalOrders = confirmService.findAbnormalOrderFromRisk(shouldPayoffOrderDelcodes);
		DynamicDataSource.setCurrentLookupKey("dataSource");
		if (abnormalOrders == null || abnormalOrders.size() == 0) {
			return ;
		}

		//调用接口
		logger.info("代扣状态异常订单:" + abnormalOrders.size() + "条");
		for (TMisDunningDeduct deduct : abnormalDeducts) {
			if (!abnormalOrders.contains(deduct.getDealcode())) {
				continue;
			}
			boolean success = this.tryRepairAbnormalOrder(deduct.getDealcode(), "bank", "代扣", new BigDecimal(deduct.getPayamount()));
			if (success) {
				successCount++;
				//更新代扣
				deduct.setRepaymentstatus(PayStatus.succeeded);
				deductService.updateWithNewTransactional(deduct);
				logger.debug("代扣信息:" + deduct.getId() + "订单" + deduct.getDealcode() + "修复成功");
				continue;
			}
			failRepairMsg.append("<p>订单号: "+deduct.getDealcode()+", 手机号: "+deduct.getMobile()+", 交易金额: "+deduct.getPayamount()+"</p>");
		}
		logger.info("代扣状态异常订单成功修复:" + successCount + "条");

		this.sendAbnormalOrderEmail("代扣入账失败",failRepairMsg);
	}

	/**
	 * @Description 尝试修复对公入账异常订单
	 * @param
	 * @return java.util.List<java.lang.String>
	 */
	private void tryRepairAbnormalRemittanceConfirm() {
		//查询对公入账异常订单
		List<TMisRemittanceConfirm> abnormalRemittanceConfirm = confirmService.findAbnormalRemittanceConfirm();

		//切换江湖救急库, 查询订单状态也为未还清的订单, 过滤未同步订单
		List<String> shouldPayoffOrderDelcodes = new ArrayList<String>();
		for (TMisRemittanceConfirm remittanceConfirm : abnormalRemittanceConfirm) {
			shouldPayoffOrderDelcodes.add(remittanceConfirm.getDealcode());
		}

		DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");
		int successCount = 0;
		StringBuilder failRepairMsg = new StringBuilder();
		List<String> abnormalOrders = confirmService.findAbnormalOrderFromRisk(shouldPayoffOrderDelcodes);
		DynamicDataSource.setCurrentLookupKey("dataSource");
		if (abnormalOrders == null || abnormalOrders.size() == 0) {
			return ;
		}

		//调用接口
		logger.info("对公交易状态异常订单:" + abnormalOrders.size() + "条");
		for (TMisRemittanceConfirm remittanceConfirm : abnormalRemittanceConfirm) {
			if (!abnormalOrders.contains(remittanceConfirm.getDealcode())) {
				continue;
			}
			boolean success = this.tryRepairAbnormalOrder(remittanceConfirm.getDealcode(), remittanceConfirm.getRemittancechannel(), remittanceConfirm.getRemark(), new BigDecimal(remittanceConfirm.getRemittanceamount()));
			if (success) {
				successCount++;
				logger.debug("汇款信息:" + remittanceConfirm.getId() + "订单" + remittanceConfirm.getDealcode() + "修复成功");
				continue;
			}
			failRepairMsg.append("<p>订单号: "+remittanceConfirm.getDealcode()+", 手机号: "+remittanceConfirm.getMobile()+", 交易金额: "+remittanceConfirm.getRemittanceamount()+"</p>");
		}
		logger.info("对公交易状态异常订单成功修复:" + successCount + "条");

		this.sendAbnormalOrderEmail("对公交易入账失败",failRepairMsg);
	}

	/**
	 * @Description 发送订单异常邮件
	 * @param subject
	 * @param failRepairMsg
	 * @return void
	 */
	private void sendAbnormalOrderEmail(String subject, StringBuilder failRepairMsg) {
		//若有未修复异常订单则发送邮件
		if (failRepairMsg.length() == 0){
			return;
		}
		String receiver = DictUtils.getDictValue("abnormal_orders_receiver", "sys_email", "");
		if (StringUtils.isBlank(receiver)){
			logger.warn(subject+"发送失败, 未配置收件人邮箱");
			return;
		}
		MailSender mailSender = new MailSender(receiver);
		mailSender.setSubject(subject + DateUtils.getDate());
		mailSender.setContent(failRepairMsg.toString());
		//发送
		try {
			mailSender.sendMail();
		} catch (MessagingException e) {
			logger.warn(subject + "发送失败", e);
		}
	}

	/**
	 * @Description 尝试通过回调接口修复异常订单
	 * @param dealcode
	 * @param remittancechannel
	 * @param remark
	 * @param remittanceamount
	 * @return boolean 是否成功
	 */
	private boolean tryRepairAbnormalOrder(String dealcode, String remittancechannel, String remark, BigDecimal remittanceamount) {
		for (int tryTime = 0; tryTime < 3; tryTime++) {
			try {
				orderManager.repay(dealcode, remittancechannel, remark, "loan", remittanceamount, "7");
			} catch (Exception e) {
				continue;
			}
			//调用成功查看订单状态是否改变
			List<String> shouldPayoffOrderDelcodes = new ArrayList<String>();
			shouldPayoffOrderDelcodes.add(dealcode);
			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");
			List<String> abnormalOrders = confirmService.findAbnormalOrderFromRisk(shouldPayoffOrderDelcodes);
			DynamicDataSource.setCurrentLookupKey("dataSource");
			if (abnormalOrders.size() == 0){
				return true;
			}
		}
		return false;
	}

}
