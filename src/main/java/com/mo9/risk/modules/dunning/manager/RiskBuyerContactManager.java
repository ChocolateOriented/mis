package com.mo9.risk.modules.dunning.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.bean.RiskResponse;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.mo9.risk.util.GetRequest;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	private static final String riskUrl =  DictUtils.getDictValue("riskclone","orderUrl","");

	/**
	 * @Description 江湖救急通讯录接口
	 * @param mobile 手机号
	 * @return 通讯录
	 */
	public List<TBuyerContact> getBuyerContactInfo(String mobile) throws IOException {
		String url = riskUrl + "riskbehavior/inner/queryContactInfoByMobile.do";

		logger.debug("获取通讯录接口url：" + url);
		String res = GetRequest.getRequest(url,"mobile="+mobile, 3000);
		logger.debug(url+"返回参数" + res);

		if (StringUtils.isBlank(res)) {
			throw new ServiceException("获取通讯录信息失败");
		}
		RiskResponse response = JSON.parseObject(res, RiskResponse.class);
		if(!RiskResponse.RESULT_CODE_SUCCESS.equals(response.getResultCode())) {
			logger.info("获取通讯录信息失败,失败信息: " + response.getResultMsg());
			throw new ServiceException(response.getResultMsg());
		}

		String datas = response.getDatas();
		if (StringUtils.isBlank(datas)) {
			return new ArrayList<TBuyerContact>();
		}
		JSONObject dataJson = JSON.parseObject(datas);
		List<TBuyerContact> contacts = JSON.parseArray(dataJson.getString("contact"),TBuyerContact.class);
		return contacts == null? new ArrayList<TBuyerContact>() : contacts;
	}

	/**
	 * @Description 江湖救急通话记录接口
	 * @param mobile
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords>
	 */
	public List<TRiskBuyerContactRecords> findContactRecordsByMobile(String mobile) throws IOException {
		if (StringUtils.isBlank(mobile)){
			throw new IllegalArgumentException("电话不能为空");
		}
		String url = riskUrl + "riskbehavior/inner/queryCalllogsInfoByMobile.do";
		logger.debug("获取通话记录URL"+url);
		String res = GetRequest.getRequest(url, "mobile="+mobile,5000);
		logger.debug(url+"响应结果"+res);

		if (StringUtils.isBlank(res)) {
			throw new ServiceException("通讯录接口响应异常");
		}
		RiskResponse result = JSON.parseObject(res,RiskResponse.class);
		if (!RiskResponse.RESULT_CODE_SUCCESS.equals(result.getResultCode())){
			logger.info("获取"+mobile+"通讯录失败,失败信息: " +result.getResultMsg());
			throw new ServiceException(result.getResultMsg());
		}
		String datas = result.getDatas();
		if (StringUtils.isBlank(datas)) {
			return new ArrayList<TRiskBuyerContactRecords>();
		}
		JSONObject dataJson = JSON.parseObject(datas);
		List<TRiskBuyerContactRecords> records = JSON.parseArray(dataJson.getString("callLog"),TRiskBuyerContactRecords.class);
		if (records ==null){
			return new ArrayList<TRiskBuyerContactRecords>();
		}

		//使用通话时长排序, 降序
		Collections.sort(records, new Comparator<TRiskBuyerContactRecords>(){
			public int compare(TRiskBuyerContactRecords o1, TRiskBuyerContactRecords o2) {
				int sumtime1 = 0;
				int sumtime2 = 0;
				if (o1 !=null && o1.getSumtime()!=null){
					sumtime1 = o1.getSumtime();
				}
				if (o2 != null && o2.getSumtime()!=null){
					sumtime2 = o2.getSumtime();
				}
				return sumtime2 - sumtime1;
			}
		});
		return records;
	}
}
