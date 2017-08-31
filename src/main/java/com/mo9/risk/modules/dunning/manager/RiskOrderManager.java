package com.mo9.risk.modules.dunning.manager;

import com.mo9.risk.util.GetRequest;
import com.mo9.risk.util.PostRequest;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import org.activiti.engine.impl.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 江湖救急订单接口
 * Created by jxli on 2017/7/6.
 */
@Service
public class RiskOrderManager {
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private final String riskUrl =  DictUtils.getDictValue("riskclone","orderUrl","");

	/**
	 * @Description 江湖救急订单还款接口
	 * @param dealcode 订单号
	 * @param paychannel 还款渠道
	 * @param remark 备注
	 * @param paytype 还款类型
	 * @param payamount 还款金额
	 * @param delayDay 续期天数
	 * @return java.lang.String 成功信息
	 */
	public String repay (String dealcode, String paychannel, String remark, String paytype, BigDecimal payamount, String delayDay) throws IOException {
		if (!StringUtils.isBlank(remark)) {
			remark = remark.replace('\r', ' ');
			remark = remark.replace('\n', ' ');
		}else{
			remark = paychannel;
		}
		DecimalFormat df1 = new DecimalFormat("0.00");
		String url = riskUrl + "riskportal/limit/order/v1.0/payForStaffType/" +dealcode+ "/" +paychannel+ "/" +remark+ "/" +paytype+ "/" +df1.format(payamount)+ "/" +delayDay;

		logger.info("接口url：" + url);
		String res = java.net.URLDecoder.decode(GetRequest.getRequest(url, new HashMap<String, String>()), "utf-8");
		logger.info(dealcode+"还款接口返回参数" + res);

		if (StringUtils.isBlank(res)) {
			throw new ServiceException("订单接口回调失败");
		}
		JSONObject repJson = new JSONObject(res);
		String resultCode =  repJson.has("resultCode") ? String.valueOf(repJson.get("resultCode")) : "";
		if(StringUtils.isBlank(resultCode) || !"200".equals(resultCode)) {
			//抛异常回滚
			String msg =  repJson.has("resultMsg") ? String.valueOf(repJson.get("resultMsg")) : "";
			logger.info(dealcode+"订单接口回调失败,失败信息: " + repJson.toString());
			throw new ServiceException(msg);
		}
		return repJson.has("datas") ? String.valueOf(repJson.get("datas")) : "";
	}
	
//	https://riskclone.mo9.com/scorecard/api/route/v1/scApplicationCol?scVersion=V0_1&mobile={{mobile}}&dealcode={{dealcode}}&buyerId={{buyerId}}&orderId={{orderId}}&old=true
	/**
	 * @Description 江湖救急评分卡接口
	 * @param mobile 手机号
	 * @param dealcode 订单号
	 * @param buyerId 用户id
	 * @param orderId 订单id
	 * @return java.lang.String 评分
	 */
	public String scApplicationCol(String mobile, String dealcode, String buyerId, String orderId) throws IOException {

		String url = riskUrl + "scorecard/api/route/v1/scApplicationCol?scVersion=V0_1&mobile=" +mobile+ "&dealcode=" +dealcode+ "&buyerId=" +buyerId+ "&orderId=" +orderId+ "&old=true";

		logger.debug("接口url：" + url);
		String res = java.net.URLDecoder.decode(PostRequest.postRequest(url, new HashMap<String, String>()), "utf-8");
		logger.debug("接口url返回参数" + res);

		if (StringUtils.isBlank(res)) {
			throw new ServiceException("获取评分卡信息失败");
		}
		JSONObject repJson = new JSONObject(res);
		String resultCode =  repJson.has("code") ? String.valueOf(repJson.get("code")) : "";
		if(StringUtils.isBlank(resultCode) || !"200".equals(resultCode)) {
			//抛异常回滚
			String msg =  repJson.has("message") ? String.valueOf(repJson.get("message")) : "";
			logger.debug("获取评分卡信息失败,失败信息: " + repJson.toString());
			throw new ServiceException(msg);
		}
		logger.debug(dealcode + "&" + mobile +"返回分数信息:" + repJson.get("data"));
		return repJson.has("data") ? String.valueOf(repJson.get("data")) : "";
	}

}
