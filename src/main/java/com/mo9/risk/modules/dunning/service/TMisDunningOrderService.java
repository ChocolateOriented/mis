/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisDunningOrderDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.manager.RiskOrderManager;
import com.mo9.risk.util.MailSender;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 订单
 * @author jxli
 * @version 2017/7/6
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningOrderService extends BaseService{

	@Autowired
	private TMisDunningOrderDao tMisDunningOrderDao;
	@Autowired
	private RiskOrderManager orderManager;
	@Autowired
	private TMisDunningForRiskService orderForRiskService;


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
	 * @Description 发送订单异常邮件
	 * @return void
	 */
	public void sendAbnormalOrderEmail(String remark,String paychannel, String dealcode,BigDecimal payamount ,String reason) {
		String subject = paychannel+ remark + "入账失败-"+dealcode ;
		StringBuilder failRepairMsg = new StringBuilder();
		failRepairMsg.append("<p>订单号: "+dealcode+", 交易金额: "+payamount.toString()+"</p>");
		failRepairMsg.append("<p>原因</p>");
		failRepairMsg.append("<p>"+reason+"</p>");

		String receiver = DictUtils.getDictValue("abnormal_orders_receiver", "sys_email", "");
		if (StringUtils.isBlank(receiver)){
			logger.info(subject+"邮件发送失败, 未配置收件人邮箱");
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
	 * @Description 尝试通过回调接口修复应还清的异常订单
	 * @param dealcode
	 * @param remittancechannel
	 * @param remark
	 * @param remittanceamount
	 * @return boolean 是否成功
	 */
	public boolean tryRepairAbnormalOrder(String dealcode, String remittancechannel, String remark, BigDecimal remittanceamount) {
		for (int tryTime = 0; tryTime < 3; tryTime++) {
			try {
				orderManager.repay(dealcode, remittancechannel, remark, DunningOrder.PAYTYPE_LOAN, remittanceamount, "7");
			}catch (Exception e) {
				continue;
			}
			//调用成功查看订单状态是否改变
			List<String> shouldPayoffOrderDelcodes = new ArrayList<String>();
			shouldPayoffOrderDelcodes.add(dealcode);
			List<String> abnormalOrders = this.findAbnormalOrderFromRisk(shouldPayoffOrderDelcodes);
			if (abnormalOrders.size() == 0){
				return true;
			}
		}
		return false;
	}

	/**
	 * @Description 查询江湖救急库异常订单
	 * @param shouldPayoffOrderDelcodes
	 * @return java.util.List<java.lang.String>
	 */
		public List<String> findAbnormalOrderFromRisk(List<String> shouldPayoffOrderDelcodes) {
			try {
				DynamicDataSource.setCurrentLookupKey("temporaryDataSource");
				return orderForRiskService.findPaymentOrederWithNewTransactional(shouldPayoffOrderDelcodes);
			} finally {
				DynamicDataSource.setCurrentLookupKey("dataSource");
			}
		}

	/***
	 * @Description 通过订单号查询订单
	 * @param dealcode
	 * @return com.mo9.risk.modules.dunning.entity.DunningOrder
	 */
	public DunningOrder findOrderByDealcodeFromRisk(String dealcode) {
		try {
			DynamicDataSource.setCurrentLookupKey("temporaryDataSource");
			return orderForRiskService.findOrderByDealcodeWithNewTransactional(dealcode);
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
	}

	/**
	 * @Description 江湖救急订单还款接口, 由于目前响应码不可靠, 成功后进行检查, 不符合预期则发送邮件
	 * @param dealcode 订单号
	 * @param paychannel 还款渠道
	 * @param remark 备注
	 * @param paytype 还款类型
	 * @param payamount 还款金额
	 * @param delayDay 续期天数
	 * @return java.lang.String 成功信息
	 */
	public String repayWithCheack (String dealcode, String paychannel, String remark, String paytype, BigDecimal payamount, String delayDay)
			throws IOException {
		//若是本分还款, 记录调用之前
		DunningOrder before = null;
		if (paytype.equals(DunningOrder.PAYTYPE_PARTIAL)){
			try {
				before = this.findOrderByDealcodeFromRisk(dealcode);
			} catch (Exception e) {
				logger.info("获取江湖救急订单失败", e);
			}
		}

		String result = orderManager.repay(dealcode,paychannel,remark,paytype,payamount,delayDay);
		//查询调用之后
		DunningOrder after = null;
		try {
			after = this.findOrderByDealcodeFromRisk(dealcode);
		} catch (Exception e) {
			logger.info("获取江湖救急订单失败", e);
		}
		if (after == null){
			return result;
		}
		String reason = "调用江湖救急接口后, 订单未发生变化";
		//还清订单检查状态
		boolean loanOrderChanged = DunningOrder.PAYTYPE_LOAN.equals(paytype) && DunningOrder.STATUS_PAYOFF.equals(after.getStatus());
		//部分还款检查应还金额
		boolean partialOrderChanged = DunningOrder.PAYTYPE_PARTIAL.equals(paytype) && before!=null && before.getRemainAmmount()!=null && !before.getRemainAmmount().equals(after.getRemainAmmount());
		boolean orderChanged = loanOrderChanged || partialOrderChanged;

		//订单没有改变则发送邮件
		if (!orderChanged){
			this.sendAbnormalOrderEmail(remark, paychannel,dealcode,payamount ,reason);
		}
		return result;
	}
}
