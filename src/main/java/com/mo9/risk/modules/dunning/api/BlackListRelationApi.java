package com.mo9.risk.modules.dunning.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.bean.BlackListRelation;
import com.mo9.risk.modules.dunning.bean.dto.ApiResponse;
import com.mo9.risk.modules.dunning.service.RiskQualityInfoService;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jxli on 2017/9/22.
 * 黑名单信息
 */
@Controller
@RequestMapping(value = "api/blackList")
public class BlackListRelationApi extends BaseController {

	/**
	 * @Description 更新黑名单联系人信息
	 * @param data
	 * {
			"userMobiles":[
				"13002065421",
				"13002065421"
				],
			"blackThirdMerchant":"mo9",
			"blackMobile":"13002065421"
		}
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse<java.lang.String>
	 */
	@RequestMapping(value = "refreshRelationNum",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse<String> refresh(@RequestBody String data){
		logger.info("收到黑名单联系人信息"+data);

		JSONObject dataJson = JSON.parseObject(data);
		JSONArray userMobiles = dataJson.getJSONArray("userMobiles");
		if (userMobiles == null || userMobiles.size() == 0){
			return new ApiResponse<>(ApiResponse.CODE_ERROR_PARAM_CHECK,"用户号码不能为空");
		}
		String blackThirdMerchant = dataJson.getString("blackThirdMerchant");

		for (int i = 0; i < userMobiles.size(); i++) {
			String mobile = userMobiles.getString(i);
			String relationJson = JedisUtils.get(RiskQualityInfoService.BLACK_LIST_CACHE_PREFIX+mobile);
			if (StringUtils.isBlank(relationJson)){
				logger.info("未缓存黑名单联系人信息"+mobile);
				continue;
			}
			BlackListRelation relationNum = JSON.parseObject(relationJson, BlackListRelation.class);
			int num = relationNum.getNum() + 1;
			relationNum.setNum(num);

			if ("third".equals(blackThirdMerchant)){
				int numFromThird = relationNum.getNumFromThird() + 1;
				relationNum.setNumFromThird(numFromThird);

			}else if ("mo9".equals(blackThirdMerchant)){
				int numFromMo9 = relationNum.getNumFromMo9() + 1;
				relationNum.setNumFromMo9(numFromMo9);

			}else {
				int numUnknow = relationNum.getNumUnknow() + 1;
				relationNum.setNumUnknow(numUnknow);

			}
			JedisUtils.set(RiskQualityInfoService.BLACK_LIST_CACHE_PREFIX+mobile, JSONObject.toJSONString(relationNum),0);
		}

		return new ApiResponse(ApiResponse.CODE_SUCCESS,ApiResponse.MESSAGE_SUCCESS);
	}
}
