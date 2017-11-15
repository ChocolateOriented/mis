package com.mo9.risk.modules.dunning.manager;

import com.alibaba.fastjson.JSON;
import com.gamaxpay.commonutil.Cipher.Md5Encrypt;
import com.mo9.risk.modules.dunning.bean.RiskResponse;
import com.mo9.risk.modules.dunning.service.TMisDunningConfigureService;
import com.mo9.risk.util.PostRequest;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

}
