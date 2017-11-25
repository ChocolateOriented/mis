/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.alibaba.fastjson.JSON;
import com.gamaxpay.commonutil.msf.BaseResponse;
import com.gamaxpay.commonutil.msf.JacksonConvertor;
import com.gamaxpay.commonutil.msf.ServiceAddress;
import com.mo9.risk.modules.dunning.bean.Mo9ResponseData;
import com.mo9.risk.modules.dunning.bean.dto.Mo9DeductOrder;
import com.mo9.risk.modules.dunning.dao.TMisDunningConfigureDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningDeductDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningDeductLogDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskLogDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.mo9.risk.modules.dunning.entity.TMisDunningTaskLog;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.enums.PayStatus;
import com.mo9.risk.util.MsfClient;
import com.mo9.risk.util.PostRequest;
import com.mo9.risk.util.RequestParamSign;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 催收代扣接口调用Service
 * @author shijlu
 * @version 2017-04-11
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningDeductCallService {
	
	@Autowired
	private TMisDunningDeductDao tMisDunningDeductDao;
	
	@Autowired
	private TMisDunningDeductLogDao tMisDunningDeductLogDao;
	
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	
	@Autowired
	private TMisDunningTaskLogDao tMisDunningTaskLogDao;
	
	@Autowired
	private TMisDunningConfigureDao tMisDunningConfigureDao;

	@Autowired
	private TMisDunningOrderService orderService ;

	private static Logger logger = LoggerFactory.getLogger(TMisDunningDeductCallService.class);
	
	/**
	 * 保存代扣记录
	 * @param tMisDunningDeduct
	 * @return
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveDeductOrder(TMisDunningDeduct tMisDunningDeduct) {
		save(tMisDunningDeduct);
		saveDeductLog(tMisDunningDeduct);
	}
	
	/**
	 * 更新代扣记录的状态
	 * @param tMisDunningDeduct
	 * @return
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public boolean updateDeductStatus(TMisDunningDeduct tMisDunningDeduct) {
		String message = filterPaychannelName(tMisDunningDeduct.getPaychannel(), tMisDunningDeduct.getStatusdetail());
		tMisDunningDeduct.setStatusdetail(message);
		if (tMisDunningDeduct.getStatus() == PayStatus.succeeded) {
			tMisDunningDeduct.setFinishtime(new Date());
		}
		
		update(tMisDunningDeduct);
		saveDeductLog(tMisDunningDeduct);
		
		if (tMisDunningDeduct.getStatus() == PayStatus.failed && "system".equals(tMisDunningDeduct.getOperationtype())) {
			TRiskBuyerPersonalInfo buyerInfo = tMisDunningDeductDao.getBuyerInfoByDealcode(tMisDunningDeduct.getDealcode());
			if (buyerInfo == null || buyerInfo.getRealName() == null) {
				return true;
			}
			
			sendFailRemindSMS(buyerInfo, tMisDunningDeduct);
		}
		return true;
	}
	
	@Transactional(readOnly = false)
	public void save(TMisDunningDeduct tMisDunningDeduct) {
		tMisDunningDeduct.preInsert();
		tMisDunningDeductDao.insert(tMisDunningDeduct);
	}
	
	@Transactional(readOnly = false)
	public void update(TMisDunningDeduct tMisDunningDeduct) {
		tMisDunningDeduct.preUpdate();
		tMisDunningDeductDao.update(tMisDunningDeduct);
	}
	
	@Transactional(readOnly = false)
	private void saveDeductLog(TMisDunningDeduct tMisDunningDeduct) {
		tMisDunningDeduct.preInsert();
		tMisDunningDeductLogDao.insert(tMisDunningDeduct);
	}
	
	/**
	 * 调用先玩后付代扣下单接口
	 * @param tMisDunningDeduct
	 * @return
	 */
	public Mo9ResponseData submitOrderInMo9(TMisDunningDeduct tMisDunningDeduct) throws IOException {
		Mo9DeductOrder mo9Order = new Mo9DeductOrder(tMisDunningDeduct);
		
		String misUrl =  DictUtils.getDictValue("misUrl", "orderUrl", "");
		mo9Order.setNotifyUrl(misUrl + "dunning/tMisDunningDeduct/updateRecord");
		
		String privateKey = tMisDunningConfigureDao.get("deduct.privateKey");
		String sign = RequestParamSign.generateParamSign(mo9Order.toMap(), privateKey);
		mo9Order.setSign(sign);
		String mo9Url =  DictUtils.getDictValue("mo9Url", "orderUrl", "");
		String uri = mo9Url + "gateway/proxydeduct/new/pay.mhtml";
		logger.info("mo9代扣接口url：" + uri);

		String response = URLDecoder.decode(PostRequest.postRequest(uri, mo9Order.toMap()), "UTF-8");
		logger.info("mo9代扣接口url返回参数" + response);
		
		if (StringUtils.isBlank(response)) {
			return null;
		}
		
		Mo9ResponseData responseObj = JSON.parseObject(response, Mo9ResponseData.class);
		return responseObj;
	}
	
	/**
	 * 调用江湖救急接订单还款接口
	 * @param tMisDunningDeduct
	 * @return
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public boolean updateRepaymentStatus(TMisDunningDeduct tMisDunningDeduct) {
		String dealcode = tMisDunningDeduct.getDealcode();
		String paychannel = "bank";
		String paidType = tMisDunningDeduct.getPaytype();
		Double paidAmount = tMisDunningDeduct.getPayamount();
		String thirdCode = tMisDunningDeduct.getThirdCode();
		try {
			orderService.repayWithPersistence(dealcode, paychannel, BigDecimal.valueOf(paidAmount),thirdCode);
			
			tMisDunningDeduct.setRepaymentstatus(PayStatus.succeeded);
			update(tMisDunningDeduct);
			saveDeductLog(tMisDunningDeduct);
			
			//部分还款，生成部分还款任务日志
			if("partial".equals(paidType)){
				TMisDunningTaskLog dunningTaskLog = tMisDunningTaskDao.newfingTaskByDealcode(dealcode);
				dunningTaskLog.setBehaviorstatus("partial");
				dunningTaskLog.setCreateDate(new Date());
				dunningTaskLog.setCreateBy(new User("auto_admin"));
				tMisDunningTaskLogDao.insert(dunningTaskLog);
			}
			
			return true;
		} catch (Exception e) {
			logger.info("江湖救急接口返回失败：", e);
			tMisDunningDeduct.setRepaymentstatus(PayStatus.failed);
			tMisDunningDeduct.setRepaymentdetail(e.getMessage());
			update(tMisDunningDeduct);
			saveDeductLog(tMisDunningDeduct);
			return false;
		}
	}
	
	/**
	 * 查询先玩后付代扣订单的状态
	 * @param tMisDunningDeduct
	 * @return
	 */
	public Mo9ResponseData queryOrderStatusInMo9(TMisDunningDeduct tMisDunningDeduct) {
		Mo9DeductOrder queryOrder = new Mo9DeductOrder();
		queryOrder.setBizSys("mis.deduct");
		queryOrder.setInvoice(tMisDunningDeduct.getDeductcode());
		String privateKey = tMisDunningConfigureDao.get("deduct.privateKey");
		String sign = RequestParamSign.generateParamSign(queryOrder.toMap(), privateKey);
		queryOrder.setSign(sign);
		String mo9Url =  DictUtils.getDictValue("mo9Url", "orderUrl", "");
		String uri = mo9Url + "gateway/proxydeduct/new/query.mhtml";
		logger.info("mo9代扣查询接口url：" + uri);
		
		String response = null;
		try {
			response = URLDecoder.decode(PostRequest.postRequest(uri, queryOrder.toMap()), "UTF-8");
		} catch (IOException e) {
			logger.warn("代扣接口返回错误:" + e.getMessage());
			return null;
		}
		logger.info("mo9代扣查询接口url返回参数" + response);
		
		if (StringUtils.isBlank(response)) {
			return null;
		}
		Mo9ResponseData responseObj = JSON.parseObject(response, Mo9ResponseData.class);
		
		if (responseObj == null || responseObj.getData() == null) {
			return responseObj;
		}
		
		Mo9ResponseData.Mo9ResponseOrder responseOrder = responseObj.getData();
		
		//查单时，若reason为空则通过message补充reason字段
		String reason = responseOrder.getReason();
		if ("failed".equals(responseOrder.getOrderStatus()) && (reason == null || "".equals(reason)) && responseOrder.getMessage() != null) {
			//余额不足->NO_BALANCE
			if (responseOrder.getMessage().contains("余额不足")) {
				responseOrder.setReason("NO_BALANCE");
			}
		}
		return responseObj;
	}
	
	/**
	 * 发送代扣短信
	 * @param mobile
	 * @param templateName
	 * @param content
	 * @param productName
	 * @return
	 */
	public void sendDeductSMS(String mobile, String templateName, Map<String, Object> content, String productName) {
		Map<String, String> params = new HashMap<String, String>();
		//发送手机号
		params.put("mobile", mobile);
		//snc版本
		params.put("snc_version", "2.0");
		//业务名称
		params.put("biz_sys", "MIS");
		//发送类型
		params.put("biz_type", "dunning");
		//客户端产品名称
		params.put("product_name", productName);
		//模板名称
		params.put("template_name", templateName);
		//模板参数
		params.put("template_data", new JacksonConvertor().serialize(content));
		//模板标识
		params.put("template_tags", "CN");
		try {
			BaseResponse response = MsfClient.instance().requestFromServer(ServiceAddress.SNC_SMS, params, BaseResponse.class);
			if (response != null && response.getHeader() != null) {
				logger.info("发送代扣短信：" + response.getHeader().getDesc());
			} else {
				logger.info("发送代扣短信无响应");
			}
		} catch (Exception e) {
			logger.info("代扣短信发送失败：" + e.getMessage());
		}
	}
	
	/**
	 * 代扣失败发送提醒短信
	 * @param buyerInfo
	 * @param tMisDunningDeduct
	 * @return
	 */
	public void sendFailRemindSMS(TRiskBuyerPersonalInfo buyerInfo, TMisDunningDeduct tMisDunningDeduct) {
		Map<String, Object> content = new HashMap<String, Object>();
		String sex = buyerInfo.getSex();
		String reason = "其他原因";
		String platform = "mo9";
		String productName = "mo9wallet";
		String product = "mo9信用钱包";
		String templateName = DictUtils.getDictLabel("sms_template", "batch_deduct", "");
		
		if (StringUtils.isBlank(templateName)) {
			logger.info("代扣短信模板名不存在");
			return;
		}
		
		if ("男".equals(sex)) {
			sex = "先生";
		} else if ("女".equals(sex)) {
			sex = "女士";
		} else {
			sex = "先生/女士";
		}
		
		if ("NO_BALANCE".equals(tMisDunningDeduct.getReason())) {
			reason = "余额不足";
		}
		
		String finProduct = buyerInfo.getFinProduct();
		if (finProduct != null) {
			if (finProduct.contains("feishudai") || finProduct.contains("loanMarket")) {
				product = "飞鼠贷";
				platform = product;
				productName = "feishudai";
			} else if (finProduct.contains("xiguadai")) {
				product = "西瓜贷";
			} else {
				//default
			}
		}
		
		content.put("realName", buyerInfo.getRealName());
		content.put("sex", sex);
		content.put("reason", reason);
		content.put("platform", platform);
		content.put("product", product);
		logger.info("发送代扣短信参数：" + content);
		sendDeductSMS(buyerInfo.getMobile(), templateName, content, productName);
	}
	
	/**
	 * 过滤渠道真实名称
	 */
	private String filterPaychannelName(String channel, String data) {
		if (data == null) {
			return "";
		}
		
		String realname = DictUtils.getDictLabel(channel, "pay_channel_realname", "");
		
		String result = data.replace(realname, "");
		return result;
	}
}