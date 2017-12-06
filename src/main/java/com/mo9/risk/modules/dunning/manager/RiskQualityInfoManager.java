package com.mo9.risk.modules.dunning.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.bean.BlackListRelation;
import com.mo9.risk.modules.dunning.bean.RiskResponse;
import com.mo9.risk.util.GetRequest;
import com.mo9.risk.util.PostRequest;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by jxli on 2017/9/7.
 */
@Service
public class RiskQualityInfoManager {
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static final String riskUrl =  DictUtils.getDictValue("riskclone","orderUrl","");

//	https://riskclone.mo9.com/scorecard/api/route/v1/scApplicationCol?scVersion=V0_1&mobile={{mobile}}&dealcode={{dealcode}}&buyerId={{buyerId}}&orderId={{orderId}}&old=true
	/**
	 * @Description 江湖救急评分卡接口
	 * @param mobile 手机号
	 * @param dealcode 订单号
	 * @param buyerId 用户id
	 * @param orderId 订单id
	 * @param oldOrder 是否老订单
	 * @return java.lang.String 评分
	 */
	public String scApplicationCol(String mobile, String dealcode, String buyerId, String orderId, boolean oldOrder) throws IOException {
		String url = riskUrl + "scorecard/api/route/v1/scApplicationCol?scVersion=V0_1";
		Map<String,String> param = new HashMap<String, String>();
		param.put("mobile",mobile);
		param.put("dealcode",dealcode);
		param.put("buyerId",buyerId);
		param.put("orderId",orderId);
		param.put("old",oldOrder+"");

		logger.debug("评分卡接口url：" + url);
		String res = PostRequest.postRequest(url,param,5000);
		logger.debug(url + "返回参数" + res);

		if (StringUtils.isBlank(res)) {
			throw new ServiceException("获取评分卡信息失败");
		}

		JSONObject repJson = JSON.parseObject(res);
		String resultCode =  repJson.containsKey("code") ? repJson.getString("code") : "";
		if(!RiskResponse.RESULT_CODE_SUCCESS.equals(resultCode)) {
			String msg =  repJson.containsKey("message") ? String.valueOf(repJson.get("message")) : "";
			logger.debug("获取评分卡信息失败,失败信息: " + repJson.toString());
			throw new ServiceException(msg);
		}
		logger.debug(dealcode + "&" + mobile +"返回分数信息:" + repJson.get("data"));
		return repJson.containsKey("data") ? repJson.getString("data") : "";
	}

	/**
	 * @Description 黑名单关系接口
	 * @param mobile 手机号
	 * @return java.lang.String 评分
	 */
	public BlackListRelation blackListRelation(String mobile) throws IOException, ApiFailException {
		if (StringUtils.isBlank(mobile)){
			throw new IllegalArgumentException("手机号不能为空");
		}
		String url = riskUrl + "relagraph/api/route/v1/queryBlackContactNum";
		Map<String,String> param = new HashMap<String, String>();
		param.put("mobile",mobile);

		String res = GetRequest.getRequest(url,param);
		logger.debug(url + "返回参数" + res);

		if (StringUtils.isBlank(res)) {
			throw new ServiceException("获取黑名单关系接口");
		}
		JSONObject repJson = JSON.parseObject(res);
		String data = repJson.getString("data");
		if (StringUtils.isBlank(data)){
				logger.info("黑名单关系接口发生异常,"+res);
				throw new ApiFailException("获取失败");
		}
		BlackListRelation relation = JSON.parseObject(data,BlackListRelation.class);
		return relation;
	}


}
