/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.dao.TMisDunningOrderDao;
import com.mo9.risk.modules.dunning.dao.TMisRequestRecordDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisRequestRecord;
import com.mo9.risk.modules.dunning.manager.RiskOrderManager;
import com.mo9.risk.util.MailSender;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
	private RiskOrderManager orderManager;
	@Autowired
	private TMisDunningForRiskService orderForRiskService;
	@Autowired
	private TMisRequestRecordDao requestRecordDao;


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
	 * @Description 重试回调江湖救急接口
	 * @return boolean 是否成功
	 */
	public boolean tryRepairAbnormalOrder(String dealcode, String paychannel, String paytype, BigDecimal payamount, String thirdCode) {
		for (int tryTime = 0; tryTime < 3; tryTime++) {
			try {
				orderManager.repay(dealcode, paychannel, paytype, payamount, thirdCode);
			} catch (Exception e) {
				logger.info("重试回调江湖救急接口发生错误", e);
				continue;
			}
			return true;
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
	 * @Description 还款,
	 * 响应状态不确定(IOException)的还款请求处理:
	 * 		不抛出异常
	 * 		请求持久化
	 * 		等待定时重调, 再次确认请求是否被正确处理
	 * @param dealcode
	 * @param paychannel
	 * @param paytype
	 * @param payamount
	 * @param thirdCode
	 * @return void
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	public String repayWithPersistence(String dealcode, String paychannel, String paytype, BigDecimal payamount, String thirdCode) {
		//记录请求, 若失败等待重试
		DecimalFormat format = new DecimalFormat("0.00");
		TMisRequestRecord requestRecord = new TMisRequestRecord();
		requestRecord.setFrom(TMisDunningOrderService.class.getSimpleName());
		requestRecord.setTarget("riskOrderRepay");
		Map<String,String> param = new HashMap<String, String>();
		param.put("dealcode",dealcode);
		param.put("paychannel",paychannel);
		param.put("paytype",paytype);
		param.put("payamount",format.format(payamount));
		param.put("thirdCode",thirdCode);
		requestRecord.setParam(JSON.toJSONString(param));
		requestRecord.preInsert();
		requestRecordDao.insert(requestRecord);

		String msg = "";
		try {
			msg = orderManager.repay(dealcode,paychannel,paytype,payamount,thirdCode);
		}catch (IOException e) {
			logger.info("订单"+dealcode+"调用江湖救急接口发生网络异常,等待重试",e);
			return msg;
		}catch (RuntimeException e){//发生业务异常也删除
			requestRecordDao.delete(requestRecord);
			throw e;
		}
		//成功则删除
		requestRecordDao.delete(requestRecord);
		return msg;
	}

	/**
	 * @Description 查询状态不缺定的订单请求并执行重调
	 * @param
	 * @return void
	 */
	@Scheduled(cron = "0 50 * * * ?")
	@Transactional
	public void autoRepairAbnormalRepayRequest() {
		//查询发生异常的请求记录
		TMisRequestRecord queryRecord = new TMisRequestRecord();
		queryRecord.setTarget("riskOrderRepay");
		queryRecord.setFrom(TMisDunningOrderService.class.getSimpleName());
		List<TMisRequestRecord> requestRecords = requestRecordDao.findList(queryRecord);

		//重调
		for (TMisRequestRecord record : requestRecords) {
			JSONObject param = JSON.parseObject(record.getParam());
			String dealcode = param.getString("dealcode");
			String paychannel = param.getString("paychannel");
			String paytype = param.getString("paytype");
			BigDecimal payamount = new BigDecimal(param.getString("payamount"));
			String thirdCode = param.getString("thirdCode");

			boolean success = this.tryRepairAbnormalOrder(dealcode, paychannel, paytype,payamount, thirdCode);
			if (success){//成功则删除
				requestRecordDao.delete(record);
			}else {//失败发送邮件
				this.sendAbnormalOrderEmail("定时重试",paychannel,dealcode,payamount,"请求状态不确定订单,重调失败");
			}
		}
	}
}
