package com.mo9.risk.modules.dunning.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gamaxpay.commonutil.Cipher.Md5Encrypt;
import com.mo9.risk.modules.dunning.bean.RiskResponse;
import com.mo9.risk.modules.dunning.bean.SerialRepay;
import com.mo9.risk.modules.dunning.bean.SerialRepay.RepayWay;
import com.mo9.risk.modules.dunning.enums.PayStatus;
import com.mo9.risk.modules.dunning.service.TMisDunningConfigureService;
import com.mo9.risk.util.PostRequest;
import com.mo9.risk.util.RequestParamSign;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	@Autowired
	private TMisDunningConfigureService tMisDunningConfigureService;
	private static final String riskUrl =  DictUtils.getDictValue("riskclone","orderUrl","");
	private static final List<String> SUCCESS_CODES = Arrays.asList("00001","00002","-8");//成功响应码
	private static final List<String> SERVER_BUSY_CODES = Arrays.asList("-11","-12","-101","-102");//系统繁忙响应码


	/**
	 * @Description  江湖救急订单还款接口
	 * @param dealcode 订单号
	 * @param paychannel 还款渠道
	 * @param payamount 还款金额
	 * @param thirdCode 还款业务名 + 业务唯一标识
	 * @return java.lang.String
	 */
	public String repay (String dealcode, String paychannel, BigDecimal payamount, String thirdCode)
			throws IOException, ApiFailException {
		String privateKey = tMisDunningConfigureService.getConfigureValue("orderRepay.privateKey");
		DecimalFormat format = new DecimalFormat("0.00");

		HashMap<String,String> params = new HashMap<String,String>();
		params.put("orderNo", dealcode);
		params.put("channel", paychannel);
		params.put("amount", format.format(payamount));
		params.put("thirdCode", thirdCode);
		String sign = Md5Encrypt.sign(params, privateKey);
		params.put("sign", sign);

		String url = riskUrl + "riskportal/limit/order/v1.0/oneForMis.a";

		logger.info("还款接口url：" + url);
		String res = PostRequest.postRequest(url, params);
		logger.info(dealcode+"还款接口返回参数" + res);

		if (StringUtils.isBlank(res)) {
			throw new ServiceException("订单接口回调失败");
		}
		RiskResponse result = JSON.parseObject(res, RiskResponse.class);
		String resultMsg = result.getResultMsg();
		String code = result.getResultCode();
		if (SERVER_BUSY_CODES.contains(code)){
			throw new ApiFailException("订单接口回调失败, 系统繁忙"+result.toString());
		}
		if (!SUCCESS_CODES.contains(code)){
			logger.info(dealcode+"订单接口回调失败,失败信息: " + result.toString());
			throw new ServiceException(resultMsg);
		}

		return resultMsg;
	}

	private static final String mo9Url = DictUtils.getDictValue("mo9Url","orderUrl","");
	/**
	 * @Description 查询代收还款流水
	 * @param dealcode
	 * @return java.util.List<com.mo9.risk.modules.dunning.bean.SerialRepay>
	 */
	public List<SerialRepay> findSerialRepayMsg(String dealcode) throws IOException {
		String privateKey = tMisDunningConfigureService.getConfigureValue("refund.privateKey");
		JSONObject params = new JSONObject();
		params.put("loanDealCode", dealcode);
		String sign = RequestParamSign.generateParamSign(params.toJSONString(), privateKey);

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("sign",sign);
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);

		String url = mo9Url + "flashApi/refund/public_list_collect_repay_by_deal_code";
		String res = PostRequest.postRequest(url,params.toJSONString(),headers,2000);
		logger.debug(dealcode + "还款流水"+ res);
		if (StringUtils.isBlank(res)){
			return new ArrayList<>();
		}
		JSONObject reult = JSON.parseObject(res);
		JSONArray reapyList = JSONArray.parseArray(reult.getString("data"));
		if (reapyList == null){
			return new ArrayList<>();
		}

		List<SerialRepay> data = new ArrayList<>();
		for (int i = 0; i < reapyList.size(); i++) {
			JSONObject repay = reapyList.getJSONObject(i);
			SerialRepay repayObj = repay.toJavaObject(SerialRepay.class);
			//还款方式
			JSONObject repayWay = repay.getJSONObject("repayWay");
			if (repayWay != null){
				String key = repayWay.getString("key");
				try {
					repayObj.setRepayWay(RepayWay.valueOf(key));
				}catch (IllegalArgumentException e){
					logger.info("未知还款方式:" + key, e);
				}
			}
			//还款状态
			String repayStatus = repay.getString("repayStatus");
			if ("0".equals(repayStatus)){
				repayObj.setRepayStatus(PayStatus.failed);
			}
			if ("2".equals(repayStatus)){
				repayObj.setRepayStatus(PayStatus.succeeded);
			}

			data.add(repayObj);
		}
		return data;
	}
}
