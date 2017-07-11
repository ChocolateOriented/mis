package com.mo9.risk.modules.dunning.manager;

import com.mo9.risk.util.GetRequest;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.math.BigDecimal;
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

	/**
	 * @Description 江湖救急订单还款接口
	 * @param dealcode 订单号
	 * @param paychannel 还款渠道
	 * @param remark 备注
	 * @param paytype 还款类型
	 * @param Payamount 还款金额
	 * @param delayDay 续期天数
	 * @return org.activiti.engine.impl.util.json.JSONObject
	 */
	public JSONObject repay (String dealcode, String paychannel, String remark, String paytype, BigDecimal Payamount, String delayDay) throws IOException {
		if (!StringUtils.isBlank(remark)) {
			remark = remark.replace('\r', ' ');
			remark = remark.replace('\n', ' ');
		}else{
			remark = paychannel;
		}

		String riskUrl =  DictUtils.getDictValue("riskclone","orderUrl","");
		String url = riskUrl + "riskportal/limit/order/v1.0/payForStaffType/" +dealcode+ "/" +paychannel+ "/" +remark+ "/" +paytype+ "/" +Payamount.toString()+ "/" +delayDay;

		logger.info("接口url：" + url);
		String res = java.net.URLDecoder.decode(GetRequest.getRequest(url, new HashMap<String, String>()), "utf-8");
		logger.info("接口url返回参数" + res);

		if (StringUtils.isNotBlank(res)) {
			return  new JSONObject(res);
		}
		return new JSONObject();
	}
}
