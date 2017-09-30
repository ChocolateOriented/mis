package com.mo9.risk.modules.dunning.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.bean.dto.ApiResponse;
import com.mo9.risk.modules.dunning.bean.dto.RefundLaunchDto;
import com.mo9.risk.modules.dunning.entity.TMisDunningRefund.RefundStatus;
import com.mo9.risk.modules.dunning.service.TMisDunningConfigureService;
import com.mo9.risk.modules.dunning.service.TMisDunningRefundService;
import com.mo9.risk.util.RequestParamSign;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jxli on 2017/9/22.
 * 退款接口, 供客服平台调
 */
@Controller
@RequestMapping(value = "api/refund")
public class RefundApi extends BaseController {

	@Autowired
	private TMisDunningRefundService refundService;
	@Autowired
	private TMisDunningConfigureService tMisDunningConfigureService;

	/**
	 * @Description  发起退款
	 * @param sign
	 * @param data 期望值如下
	 * {
				"refundCode":"7853275162962",
				"remittanceChannel":"alipay",
				"remittanceSerialNumber":"26762462264029504165664282158437",
				"amount":2222.1
			}
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse
	 */
	@RequestMapping(value = "launch",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse launch(@RequestHeader String sign, @RequestBody String data) {
		logger.debug("发起退款"+data);
		if (!hasValidSign(sign,data)){
			logger.info(data+"发起退款签名错误");
			return this.errorSignResponse();
		}

		RefundLaunchDto launchDto = JSON.parseObject(data,RefundLaunchDto.class);
		try {
			BeanValidators.validateWithException(validator, launchDto);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			logger.info(launchDto.getRefundCode()+"发起退款参数错误"+list,ex);
			return new ApiResponse(ApiResponse.CODE_ERROR_PARAM_CHECK,"参数验证错误");
		}

		BigDecimal amount = new BigDecimal(launchDto.getAmount());
		//保留两位小数, 四舍五入
		amount.setScale(2,BigDecimal.ROUND_HALF_UP);
		refundService.launch(launchDto.getRefundCode(),amount,launchDto.getRemittanceSerialNumber(),launchDto.getRemittanceChannel());

		logger.debug(launchDto.getRefundCode()+"发起退款成功");
		return this.successResponse();
	}

	/**
	 * @Description  退款中, 批量操作
	 * @param sign
	 * @param data 期望值如下
	 *{
			"auditTime": 1506652802266,
			"auditor": "财务审核人X",
			"refundCodes": [
				"7853275162962",
				"78532751629621",
				"78532751629622"
			]
		}
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse<java.lang.String>
	 */
	@RequestMapping(value = "process", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse<String> process(@RequestHeader String sign, @RequestBody String data) {
		logger.debug("退款中处理"+data);
		if (!hasValidSign(sign,data)){
			logger.info(data+"退款中签名错误");
			return this.errorSignResponse();
		}

		JSONObject dataJson = JSON.parseObject(data);
		List<String> refundCodes = JSONArray.parseArray(dataJson.getString("refundCodes"),String.class);
		if (null == refundCodes || refundCodes.size()==0){
			return new ApiResponse(ApiResponse.CODE_ERROR_PARAM_CHECK,"refundCodes不能为空");
		}

		String auditor = dataJson.getString("auditor");
		Date auditTime = null;
		Long auditTimestamp = dataJson.getLong("auditTime");
		if (auditTimestamp != null){
			auditTime = new Date(auditTimestamp);
		}

		try {
			refundService.process(auditor, auditTime, refundCodes);
		}catch (ServiceException e){
			logger.info(refundCodes+"处理失败",e);
			return new ApiResponse(ApiResponse.CODE_ERROR_UNKNOWN, e.getMessage());
		}
		logger.debug(refundCodes+"退款中成功");
		return successResponse();
	}

	/**
	 * @Description 确认退款, 已查账的汇款确认信息软删除
	 * @param sign
	 * @param data 期望值如下
	 *{
			"refundCode": "7853275162962",
			"refundTime": 1506652802266
		}
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse<java.lang.String>
	 */
	@RequestMapping(value = "finish", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse finish(@RequestHeader String sign, @RequestBody String data){
		logger.debug(data+"确认退款处理中");
		if (!hasValidSign(sign,data)){
			logger.info(data+"确认退款签名错误");
			return this.errorSignResponse();
		}
		JSONObject jsonObject =	JSONObject.parseObject(data);
		String refundCode = jsonObject.getString("refundCode");
		if (StringUtils.isBlank(refundCode)){
			logger.debug(data+"参数错误,refundCode不能为空");
			return new ApiResponse(ApiResponse.CODE_ERROR_PARAM_CHECK, "refundCode不能为空");
		}

		Date refundTime = null;
		Long refundTimestamp = jsonObject.getLong("refundTime");
		if (refundTimestamp != null){
			refundTime = new Date(refundTimestamp);
		}

		try {
			refundService.finish(refundCode, refundTime);
		}catch (ServiceException e){
			logger.info(refundCode+"确认退款签失败",e);
			return new ApiResponse(ApiResponse.CODE_ERROR_UNKNOWN,e.getMessage());
		}

		return this.successResponse();
	}

	/**
	 * @Description 取消退款
	 * @param data 期望值
	 * 	{
				"refundCode":"7853275162962"
			}
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse<java.lang.String>
	 */
	@RequestMapping(value = "cancel",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse cancel(@RequestHeader String sign, @RequestBody String data){
		return this.invalidRefund(sign,data,RefundStatus.CANCEL);
	}

	/**
	 * @Description 退款失败
	 * @param data 期望值
	 * 	{
				"refundCode":"7853275162962"
			}
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse<java.lang.String>
	 */
	@RequestMapping(value = "fail",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse<String> fail(@RequestHeader String sign, @RequestBody String data){
		return this.invalidRefund(sign,data,RefundStatus.FAIL);
	}

	/**
	 * @Description 拒绝退款
	 * @param data 期望值
	 * 	{
				"refundCode":"7853275162962"
			}
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse<java.lang.String>
	 */
	@RequestMapping(value = "refused",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApiResponse<String> refused(@RequestHeader String sign, @RequestBody String data){
		return this.invalidRefund(sign,data,RefundStatus.REFUSED);
	}

	/**
	 * @Description 无效退款处理
	 * @param sign
	 * @param data
	 * @param refundStatus
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse
	 */
	private ApiResponse invalidRefund(String sign, String data,RefundStatus refundStatus){
		logger.debug(data+refundStatus.desc+"处理中");
		if (!hasValidSign(sign,data)){
			logger.info(data+refundStatus.desc+"签名错误");
			return this.errorSignResponse();
		}
		JSONObject jsonObject =	JSONObject.parseObject(data);
		String refundCode = jsonObject.getString("refundCode");
		if (StringUtils.isBlank(refundCode)){
			logger.info(refundCode+refundStatus.desc+"参数错误,refundCode不能为空");
			return new ApiResponse(ApiResponse.CODE_ERROR_PARAM_CHECK, "refundCode不能为空");
		}

		try {
			refundService.invalidRefund(refundCode,refundStatus);
		}catch (ServiceException e){
			logger.info(refundCode+refundStatus.desc+"处理失败",e);
			return new ApiResponse(ApiResponse.CODE_ERROR_UNKNOWN, e.getMessage());
		}

		logger.debug(refundCode+refundStatus.desc+"处理成功");
		return this.successResponse();
	}


	/**
	 * @Description 验证签名
	 * @return boolean
	 */
	private boolean hasValidSign(String paramSign,String data){

		if (StringUtils.isBlank(paramSign)){
			return false;
		}
		String privateKey = tMisDunningConfigureService.getConfigureValue("refund.privateKey");
		String validSign = RequestParamSign.generateParamSign(data, privateKey);
		if (paramSign.toString().equals(validSign)){
			return true;
		}
		return false;
	}

	/**
	 * @Description 成功
	 * @param
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse
	 */
	private ApiResponse successResponse() {
		return new ApiResponse(ApiResponse.CODE_SUCCESS,ApiResponse.MESSAGE_SUCCESS);
	}

	/**
	 * @Description 签名错误
	 * @param
	 * @return com.mo9.risk.modules.dunning.bean.dto.ApiResponse<java.lang.String>
	 */
	private ApiResponse<String> errorSignResponse(){
		return new ApiResponse(ApiResponse.CODE_ERROR_SIGN_CHECK,"签名验证错误");
	}

}
