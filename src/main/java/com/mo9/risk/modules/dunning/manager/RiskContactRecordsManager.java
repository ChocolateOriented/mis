package com.mo9.risk.modules.dunning.manager;

import com.alibaba.fastjson.JSON;
import com.mo9.risk.modules.dunning.bean.RiskContactRecordResponse;
import com.mo9.risk.modules.dunning.bean.RiskResponse;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.mo9.risk.util.GetRequest;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jxli on 2017/9/2.
 */
public class RiskContactRecordsManager {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private final String riskUrl =  DictUtils.getDictValue("riskclone","orderUrl","");

	/**
	 * @Description 通过电话查询通话记录
	 * @param mobile
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords>
	 */
	public List<TRiskBuyerContactRecords> findByByMobile(String mobile) throws IOException {
		if (StringUtils.isBlank(mobile)){
			throw new IllegalArgumentException("电话不能为空");
		}

		String url = riskUrl + "riskbehavior/inner/queryCalllogsInfoByMobile.do?mobile="+mobile;
		String res = GetRequest.getRequest(url, "");

		if (StringUtils.isBlank(res)) {
			throw new ServiceException("通讯录接口响应异常");
		}
		RiskContactRecordResponse result = JSON.parseObject(res,RiskContactRecordResponse.class);
		if (!RiskResponse.RESULT_CODE_SUCCESS.equals(result.getResultCode())){
			//抛异常回滚
			logger.info("获取通讯录失败,失败信息: " +result.getResultMsg());
			throw new ServiceException(result.getResultMsg());
		}
		return result.getDatas().getCallLog();
	}
}
