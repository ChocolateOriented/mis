/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.util.json.JSONObject;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.gamaxpay.commonutil.web.PostRequest;
import com.mo9.risk.modules.dunning.bean.Mo9DeductOrder;
import com.mo9.risk.modules.dunning.bean.Mo9ResponseData;
import com.mo9.risk.modules.dunning.dao.TMisDunningConfigureDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningDeductDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningDeductLogDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskLogDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningTaskLog;
import com.mo9.risk.modules.dunning.enums.PayStatus;
import com.mo9.risk.util.GetRequest;
import com.mo9.risk.util.RequestParamSign;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

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
	
	private static Logger logger = Logger.getLogger(TMisDunningDeductCallService.class);
	
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
		return true;
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
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Map<String, String> submitOrderInMo9(TMisDunningDeduct tMisDunningDeduct) {
		Map<String, String> result = new HashMap<String, String>();
		Mo9DeductOrder mo9Order = new Mo9DeductOrder(tMisDunningDeduct);
		
		String misUrl =  DictUtils.getDictValue("misUrl", "orderUrl", "");
		mo9Order.setNotifyUrl(misUrl + "dunning/tMisDunningDeduct/updateRecord");
		
		String privateKey = tMisDunningConfigureDao.get("deduct.privateKey");
		String sign = RequestParamSign.generateParamSign(mo9Order.toMap(), privateKey);
		mo9Order.setSign(sign);
		String mo9Url =  DictUtils.getDictValue("mo9Url", "orderUrl", "");
		String uri = mo9Url + "gateway/proxydeduct/new/pay.mhtml";
		logger.info("mo9代扣接口url：" + uri);
		
		try {
			String response = URLDecoder.decode(PostRequest.postRequest(uri, mo9Order.toMap()), "UTF-8");
			logger.info("mo9代扣接口url返回参数" + response);
			
			if (StringUtils.isBlank(response)) {
				result.put("result", "NO");
				result.put("msg", "系统错误");
				return result;
			}
			
			Mo9ResponseData responseObj = JSON.parseObject(response, Mo9ResponseData.class);
			
			if (responseObj == null) {
				result.put("result", "NO");
				result.put("msg", "系统错误");
				return result;
			}
			String status = responseObj.getStatus();
			
			if ("SUCCEEDED".equals(status)) {
				Mo9ResponseData.Mo9ResponseOrder responseOrder = responseObj.getData();
				if (responseOrder == null) {
					result.put("result", "NO");
					result.put("msg", "系统错误");
					return result;
				}
				
				if ("submitted".equals(responseOrder.getOrderStatus()) || "padding".equals(responseOrder.getOrderStatus())) {
					result.put("result", "OK");
					result.put("msg", "下单成功");
					return result;
				} else if ("succeeded".equals(responseOrder.getOrderStatus())) {
					tMisDunningDeduct.setStatus(PayStatus.succeeded);
					tMisDunningDeduct.setStatusdetail(responseOrder.getMessage());
					tMisDunningDeduct.setReason(responseOrder.getReason());
					tMisDunningDeduct.setChargerate(responseOrder.getChargeRate());
					updateDeductStatus(tMisDunningDeduct);
					
					result.put("result", "OK");
					result.put("msg", "下单成功");
					return result;
				} else {
					tMisDunningDeduct.setStatus(PayStatus.failed);
					tMisDunningDeduct.setStatusdetail(responseOrder.getMessage());
					tMisDunningDeduct.setReason(responseOrder.getReason());
					tMisDunningDeduct.setChargerate(responseOrder.getChargeRate());
					updateDeductStatus(tMisDunningDeduct);
					
					result.put("result", "NO");
					if ("NO_BALANCE".equals(responseOrder.getReason())) {
						result.put("msg", "余额不足，本日请勿重复发起扣款");
					} else {
						result.put("msg", tMisDunningDeduct.getStatusdetail());
					}
					return result;
				}
				
			} else if ("FAILED".equals(status)) {
				tMisDunningDeduct.setStatus(PayStatus.failed);
				tMisDunningDeduct.setStatusdetail(responseObj.getMessage());
				tMisDunningDeduct.setReason(responseObj.getReason());
				updateDeductStatus(tMisDunningDeduct);
				
				result.put("result", "NO");
				result.put("msg", tMisDunningDeduct.getStatusdetail());
				return result;
			} else {
				//状态不明确时，尝试查单获取状态
				Mo9ResponseData queryResponse = queryOrderStatusInMo9(tMisDunningDeduct);
				
				if (queryResponse == null || !"SUCCEEDED".equals(queryResponse.getStatus())) {
					result.put("result", "WARN");
					result.put("msg", "系统繁忙，请稍后查询");
					return result;
				}
				
				Mo9ResponseData.Mo9ResponseOrder responseOrder = responseObj.getData();
				if (responseOrder == null) {
					result.put("result", "NO");
					result.put("msg", "系统错误");
					return result;
				}
				
				if ("submitted".equals(responseOrder.getOrderStatus()) || "padding".equals(responseOrder.getOrderStatus())) {
					result.put("result", "OK");
					result.put("msg", "下单成功");
					return result;
				} else if ("succeeded".equals(responseOrder.getOrderStatus())) {
					tMisDunningDeduct.setStatus(PayStatus.succeeded);
					tMisDunningDeduct.setStatusdetail(responseOrder.getMessage());
					tMisDunningDeduct.setReason(responseOrder.getReason());
					tMisDunningDeduct.setChargerate(responseOrder.getChargeRate());
					updateDeductStatus(tMisDunningDeduct);
					
					result.put("result", "OK");
					result.put("msg", "下单成功");
					return result;
				} else {
					tMisDunningDeduct.setStatus(PayStatus.failed);
					tMisDunningDeduct.setStatusdetail(responseOrder.getMessage());
					tMisDunningDeduct.setReason(responseOrder.getReason());
					tMisDunningDeduct.setChargerate(responseOrder.getChargeRate());
					updateDeductStatus(tMisDunningDeduct);
					
					result.put("result", "NO");
					result.put("msg", tMisDunningDeduct.getStatusdetail());
					return result;
				}
			}
			
		} catch (IOException e) {
			logger.info("mo9代扣接口url返回异常" + e.getMessage());
			
			result.put("result", "WARN");
			result.put("msg", "系统繁忙，请稍后查询");
			return result;
		}
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
		String remark = "代扣";
		String paidType = tMisDunningDeduct.getPaytype();
		Double paidAmount = tMisDunningDeduct.getPayamount();
		String delayDay = "7";

		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		
		BigDecimal bd = new BigDecimal(paidAmount);
		if(!"app".equals(order.getPlatform())){
			bd = bd.multiply(BigDecimal.valueOf(100));
		}
		String riskUrl =  DictUtils.getDictValue("riskclone", "orderUrl", "");
		String url = riskUrl + "riskportal/limit/order/v1.0/payForStaffType/" + dealcode + "/" + paychannel + "/" + remark + "/" + paidType + "/" + String.valueOf(bd.doubleValue()) + "/" + delayDay;
		logger.info("江湖救急接口url：" + url);
		String resultMsg = "";
		String resultCode = "";
		
		try {
			String res =  URLDecoder.decode(GetRequest.getRequest(url, new HashMap<String,String>()), "UTF-8");
			logger.info("江湖救急接口url返回参数" + res);
			if(StringUtils.isBlank(res)) {
				return false;
			}
			JSONObject repJson = new JSONObject(res);
			resultCode =  repJson.has("resultCode") ? String.valueOf(repJson.get("resultCode")) : "";
			resultMsg = repJson.has("resultMsg") ? String.valueOf(repJson.get("resultMsg")) : "";
			if("200".equals(resultCode)){
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
			} else {
				tMisDunningDeduct.setRepaymentstatus(PayStatus.failed);
				tMisDunningDeduct.setRepaymentdetail(resultMsg);
				update(tMisDunningDeduct);
				saveDeductLog(tMisDunningDeduct);
			}
			
			return true;
		} catch (IOException e) {
			logger.warn("江湖救急接口返回失败：" + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 查询先玩后付代扣订单的状态
	 * @param tMisDunningDeduct
	 * @return
	 */
	public Mo9ResponseData queryOrderStatusInMo9(TMisDunningDeduct deduct) {
		Mo9DeductOrder queryOrder = new Mo9DeductOrder();
		queryOrder.setBizSys("mis.deduct");
		queryOrder.setInvoice(deduct.getDeductcode());
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
		
		return responseObj;
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