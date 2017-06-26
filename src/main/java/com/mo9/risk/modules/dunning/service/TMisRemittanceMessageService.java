/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisRemittanceMessageDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.mo9.risk.util.RegexUtil;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 财务确认汇款信息Service
 * @author 徐盛
 * @version 2016-08-11
 */
@Service
@Transactional(readOnly = true)
public class TMisRemittanceMessageService extends CrudService<TMisRemittanceMessageDao, TMisRemittanceMessage> {
	@Autowired
	private TMisRemittanceMessageDao misRemittanceMessageDao;

	public TMisRemittanceMessage get(String id) {
		return super.get(id);
	}
	
	public List<TMisRemittanceMessage> findList(TMisRemittanceMessage tMisRemittanceMessage) {
		return super.findList(tMisRemittanceMessage);
	}
	
	public Page<TMisRemittanceMessage> findPage(Page<TMisRemittanceMessage> page, TMisRemittanceMessage tMisRemittanceMessage) {
		return super.findPage(page, tMisRemittanceMessage);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisRemittanceMessage tMisRemittanceMessage) {
		super.save(tMisRemittanceMessage);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisRemittanceMessage tMisRemittanceMessage) {
		super.delete(tMisRemittanceMessage);
	}
	
	@Transactional(readOnly = false)
	public void insert(TMisRemittanceMessage tMisRemittanceMessage) {
		tMisRemittanceMessage.preInsert();
		dao.insert(tMisRemittanceMessage);
	}
	
	public TMisRemittanceMessage  findRemittanceMesListByDealcode(String code){
		return misRemittanceMessageDao.findRemittanceMesListByDealcode(code);
	}

	@Transactional(readOnly = false)
	public int fileUpload(LinkedList<TMisRemittanceMessage> tMisRemittanceList) {
		int  same=0;

		List<TMisRemittanceMessage> trMList=misRemittanceMessageDao.findByList(tMisRemittanceList);
		same=trMList.size();
		if(trMList.size()>0&&trMList!=null){
			boolean removeAll = tMisRemittanceList.removeAll(trMList);
		}
		if(tMisRemittanceList.size()>0&&tMisRemittanceList!=null){
			int saveNum = misRemittanceMessageDao.saveList(tMisRemittanceList);

		}
		return same;
	}


	@Transactional(readOnly = false)
	public void autoAudit(List<TMisRemittanceMessage> remittanceMessages){
		if (null == remittanceMessages || remittanceMessages.size()== 0 ){
			logger.info("汇款信息为空, 未进行自动查账");
			return;
		}
		/**
		 * TODO 借款信息匹配
		 * 若汇款信息备注中有手机号, 则使用备注手机号匹配未还款订单
		 * 备注中无手机号的 + 备注手机号未匹配成功的汇款信息, 若账号为手机号使用账号进行匹配
		 */
		List<TMisRemittanceMessage> containMobileInRemark = new ArrayList<TMisRemittanceMessage>(remittanceMessages.size());
		HashMap<String,TMisRemittanceMessage> containMobileInRemarkMap = new HashMap<String, TMisRemittanceMessage>(remittanceMessages.size());
		List<TMisRemittanceMessage> notContainMobilInRemark = new ArrayList<TMisRemittanceMessage>();
		//获取备注中包含手机号的汇款信息
		for (TMisRemittanceMessage remittanceMessage: remittanceMessages ) {
			if (null == remittanceMessage) {
				continue;
			}
			//TODO 只针对未查账汇款
//			if (未查账 != remittanceMessages.getAccountStatus()) {
//				continue;
//			}

			//检查备注中是否包含手机号
			String remark = remittanceMessage.getRemark();
			String mobile = RegexUtil.getStringValueByRegex(RegexUtil.REGEX_CONTAIN_MOBILE,remark);
			if (StringUtils.isNoneBlank(mobile)){//备注包含手机号
				containMobileInRemarkMap.put(mobile,remittanceMessage);
				containMobileInRemark.add(remittanceMessage);
				continue;
			}
			//备注不包含手机号
			notContainMobilInRemark.add(remittanceMessage);
		}

		List<TMisRemittanceMessage> Match = this.matchOrderWithMobil(containMobileInRemarkMap);
		//批量
		//检查改电话能否匹配到订单
		//在未还清订单的用户电话中查看是否有该电话,若有则返回汇款id和订单号用于更新
//		List<TMisRemittanceMessage> matchedRemittanceMessages = new ArrayList<TMisRemittanceMessage>(hasMobileRemittanceMessages.size());
		//批量更新
		/**
		 * TODO 还款标签
		 * 备注中姓名与借款人姓名比对
		 * 	不等>不打标签
		 * 	相等>账户名与借款人姓名比对
		 * 		相等>本人还款
		 * 		不相等>第三方还款
		 */
		//本人还款
		//第三方还款
		//不确定
	}

	@Transactional(readOnly = false)
	public List<TMisRemittanceMessage> matchOrderWithMobil(HashMap<String, TMisRemittanceMessage> containMobileMap) {
		if (null == containMobileMap){
			return null;
		}
		Set<String> mobiles = containMobileMap.keySet();
		if (mobiles.size()==0){
			return null;
		}
		//通过手机号批量查询未还款订单的用户信息
		//TODO 确认下查询未还款的语句
		List<DunningOrder> orders = this.findPaymentOrderByMobile(mobiles);
		if (orders ==null || orders.size()==0){
			return null;
		}

		//匹配订单
		List<TMisRemittanceMessage> successMatchList = new ArrayList<TMisRemittanceMessage>(orders.size());
		for (DunningOrder order: orders) {
			if (order == null){
				continue;
			}
			String mobile = order.getMobile();
			TMisRemittanceMessage remittanceMessage = containMobileMap.get(mobile);
			if (remittanceMessage == null){
				continue;
			}

			remittanceMessage.setDealcode(order.getDealcode());
			successMatchList.add(remittanceMessage);
		}
		//TODO 批量更新
		return successMatchList;
	}

	/**
	 * @Description 通过手机号查询未还款订单
	 * @param mobiles
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.DunningOrder>
	 */
	public List<DunningOrder> findPaymentOrderByMobile(Set<String> mobiles) {
		return dao.findPaymentOrderByMobile(mobiles);
	}

}