package com.mo9.risk.modules.dunning.manager;

import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
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
	public List<TRiskBuyerContactRecords> findByByMobile(String mobile){
		return null ;
	}
}
