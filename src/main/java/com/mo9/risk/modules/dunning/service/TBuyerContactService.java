/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.mo9.risk.modules.dunning.entity.TBuyerContact;
import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.manager.RiskBuyerContactManager;
import com.mo9.risk.modules.dunning.dao.TBuyerContactDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerPersonalInfoDao;

/**
 * 通讯录信息Service
 * @author beargao
 * @version 2016-07-15
 */
@Service
@Transactional(readOnly = true)
public class TBuyerContactService {
	@Autowired
	private TBuyerContactDao tBuyerContactDao;
	
	@Autowired
	private TRiskBuyerPersonalInfoDao personalInfoDao;
	
	@Autowired
	private RiskBuyerContactManager riskBuyerContactManager;
	
	private static Logger logger = LoggerFactory.getLogger(TBuyerContactService.class);
	
	public Page<TBuyerContact> findPage(Page<TBuyerContact> page, TBuyerContact tBuyerContact) {
		tBuyerContact.setPage(page);
		page.setList(tBuyerContactDao.findList(tBuyerContact));
		return page;
	}
	
	/**
	 * 根据用户Id获取通讯录
	 * @param buyerId
	 * @return
	 */
	public List<TMisSendMsgInfo> getContactsByBuyerId(String buyerId) {
		String mobile = personalInfoDao.findMobileByBuyerId(buyerId);
		
		try {
			List<TBuyerContact> tBuyerContacts = riskBuyerContactManager.getBuyerContactInfo(mobile);
			List<TMisSendMsgInfo> results = new ArrayList<TMisSendMsgInfo>();
			if (tBuyerContacts != null && tBuyerContacts.size() != 0) {
				for (TBuyerContact contact : tBuyerContacts) {
					TMisSendMsgInfo tMisSendMsgInfo = new TMisSendMsgInfo();
					tMisSendMsgInfo.setTel(contact.getContactMobile());
					tMisSendMsgInfo.setName(contact.getContactName());
					results.add(tMisSendMsgInfo);
				}
			}
			
			return results;
		} catch (Exception e) {
			logger.info("调用江湖救急通讯录接口失败：" + e.getMessage());
		}
		return tBuyerContactDao.getContactsByBuyerId(buyerId);
	}
	
	/**
	 * 获取通讯录列表
	 * @param dealcode
	 * @param mobile
	 * @return
	 */
	public List<TBuyerContact> getBuyerContacts(String dealcode, String mobile, String buyerId) {
		List<TBuyerContact> tBuyerContacts = null;
		try {
			tBuyerContacts = riskBuyerContactManager.getBuyerContactInfo(mobile);
			if (tBuyerContacts != null && tBuyerContacts.size() != 0) {
				fillContantRecordCnt(tBuyerContacts, dealcode);
				return tBuyerContacts;
			}
			
		} catch (Exception e) {
			logger.info("调用江湖救急通讯录接口失败：" + e.getMessage());
		}
		
		//异常或接口无数据时查询mis库
		TBuyerContact tBuyerContact = new TBuyerContact();
		tBuyerContact.setDealcode(dealcode);
		tBuyerContact.setBuyerId(buyerId);
		return tBuyerContactDao.findList(tBuyerContact);
	}
	
    public List<TBuyerContact> getContantRecordCnt(List<TBuyerContact> tBuyerContact, String dealcode) {
    	return tBuyerContactDao.getContantRecordCnt(tBuyerContact, dealcode);
    }
	
	/**
	 * 补充通讯录催收电话短信数量
	 * @param tBuyerContacts
	 * @param dealcode
	 * @return
	 */
	private void fillContantRecordCnt(List<TBuyerContact> tBuyerContacts, String dealcode) {
		List<TBuyerContact> contactRecordCnts = tBuyerContactDao.getContantRecordCnt(tBuyerContacts, dealcode);
		
		if (contactRecordCnts == null || contactRecordCnts.size() == 0) {
			for (TBuyerContact contact : tBuyerContacts) {
				contact.setSmsNum(0);
				contact.setTelNum(0);
			}
			return;
		}
		
		for (TBuyerContact contact : tBuyerContacts) {
			if (contact.getContactMobile() == null) {
				continue;
			}
			
			contact.setSmsNum(0);
			contact.setTelNum(0);
			
			for (TBuyerContact recordCnt : contactRecordCnts) {
				if (contact.getContactMobile().equals(recordCnt.getContactMobile())) {
					contact.setSmsNum(recordCnt.getSmsNum());
					contact.setTelNum(recordCnt.getTelNum());
					break;
				}
			}
		}
	}
}