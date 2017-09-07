package com.mo9.risk.modules.dunning.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.bean.RiskCommonResponse;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.util.GetRequest;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by shijlu on 2017/9/2.
 */
@Service
public class RiskBuyerContactManager {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private final String riskUrl =  DictUtils.getDictValue("riskclone","orderUrl","");

	/**
	 * @Description 江湖救急通讯录接口
	 * @param mobile 手机号
	 * @return 通讯录
	 */
	public List<TBuyerContact> getBuyerContactInfo(String mobile) throws IOException {
		String url = riskUrl + "riskbehavior/inner/queryContactInfoByMobile.do?mobile=" + mobile;

		logger.debug("接口url：" + url);
		String res = java.net.URLDecoder.decode(GetRequest.getRequest(url, new HashMap<String, String>(), 3000), "utf-8");
		logger.debug("接口url返回参数" + res);

		if (StringUtils.isBlank(res)) {
			throw new ServiceException("获取通讯录信息失败");
		}
		RiskCommonResponse response = JSON.parseObject(res, RiskCommonResponse.class);
		String resultCode =  response.getResultCode();
		if(StringUtils.isBlank(resultCode) || !RiskCommonResponse.RESULT_CODE_SUCCESS.equals(resultCode)) {
			//抛异常回滚
			logger.info("获取通讯录信息失败,失败信息: " + response.getResultMsg());
			throw new ServiceException(response.getResultMsg());
		}
		String datas = response.getDatas();
		if (datas == null || StringUtils.isBlank(datas)) {
			return new ArrayList<TBuyerContact>();
		}
		
		JSONObject dataJson = JSON.parseObject(datas);
		String contasctStr = dataJson.getString("contact");
		return JSON.parseArray(contasctStr, TBuyerContact.class);
	}
}
