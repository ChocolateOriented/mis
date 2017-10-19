/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
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
	
	@Autowired
	private TRiskBuyerWorkinfoService tRiskBuyerWorkinfoService;
	
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
				return results;
			}
			
		} catch (Exception e) {
			logger.info("调用江湖救急通讯录接口失败：" + e.getMessage());
		}
		//异常或接口无数据时查询mis库
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
				fillContactRelation(tBuyerContacts, buyerId);
			}
			
		} catch (Exception e) {
			logger.info("调用江湖救急通讯录接口失败：" + e.getMessage());
		}
		
		//异常或接口无数据时查询mis库
		if (tBuyerContacts == null || tBuyerContacts.size() == 0) {
			TBuyerContact tBuyerContact = new TBuyerContact();
			tBuyerContact.setDealcode(dealcode);
			tBuyerContact.setBuyerId(buyerId);
			tBuyerContacts = tBuyerContactDao.findList(tBuyerContact);
		}
		fillWorkRelation(tBuyerContacts, buyerId);
		matchRelativeKeyword(tBuyerContacts);
		return tBuyerContacts;
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
	
	/**
	 * 补充通讯录联系人关系
	 * @param tBuyerContacts
	 * @param buyerId
	 * @return
	 */
	private void fillContactRelation(List<TBuyerContact> tBuyerContacts, String buyerId) {
		List<TBuyerContact> contactRelations = tBuyerContactDao.getContactRelation(tBuyerContacts, buyerId);
		
		if (contactRelations == null || contactRelations.size() == 0) {
			return;
		}
		
		for (TBuyerContact contact : tBuyerContacts) {
			if (contact.getContactMobile() == null) {
				continue;
			}
			
			for (TBuyerContact relation : contactRelations) {
				if (contact.getContactMobile().equals(relation.getContactMobile())) {
					contact.setRcname(relation.getRcname());
					contact.setFamilyrelation(relation.getFamilyrelation());
					break;
				}
			}
		}
	}
	
	/**
	 * 补充通讯录单位关系
	 * @param tBuyerContacts
	 * @param buyerId
	 * @return
	 */
	private void fillWorkRelation(List<TBuyerContact> tBuyerContacts, String buyerId) {
		if (tBuyerContacts == null || tBuyerContacts.isEmpty()) {
			return;
		}
		
		TMisSendMsgInfo tMisSendMsgInfo = tRiskBuyerWorkinfoService.getWorkTelInfoByBuyerId(buyerId);
		String worktel = null;
		String subWorktel = null;
		
		if (tMisSendMsgInfo == null || (worktel = tMisSendMsgInfo.getTel()) == null) {
			return;
		}
		
		int inx = worktel.indexOf('_');
		if (inx < 0 || inx == worktel.length() - 1) {
			subWorktel = "";
		} else {
			subWorktel = worktel.substring(inx + 1);
		}
		
		for (TBuyerContact contact : tBuyerContacts) {
			if (contact.getContactMobile() == null || StringUtils.isNotEmpty(contact.getFamilyrelation())) {
				continue;
			}
			
			if (contact.getContactMobile().equals(worktel)) {
				contact.setFamilyrelation(tMisSendMsgInfo.getRelation());
				contact.setRcname(tMisSendMsgInfo.getName());
				continue;
			}
			
			if (contact.getContactMobile().equals(subWorktel)) {
				contact.setFamilyrelation(tMisSendMsgInfo.getRelation());
				contact.setRcname(tMisSendMsgInfo.getName());
				continue;
			}
		}
	}
	
	/**
	 * 匹配亲戚关键词
	 * @param tBuyerContacts
	 * @return
	 */
	private void matchRelativeKeyword(List<TBuyerContact> tBuyerContacts) {
		if (tBuyerContacts == null || tBuyerContacts.isEmpty()) {
			return;
		}
		
		List<Dict> dicts = DictUtils.getDictList("relative_keyword");
		if (dicts == null || dicts.isEmpty()) {
			return;
		}
		
		//初始化关键词map
		Map<Character, Object> keywords = new HashMap<Character, Object>(128);
		for (Dict dict : dicts) {
			String value = dict.getValue();
			String[] wordArr = value.split(",");
			if (wordArr == null || wordArr.length == 0) {
				continue;
			}
			for (int i = 0; i < wordArr.length; i++) {
				String word = wordArr[i];
				if (StringUtils.isEmpty(word)) {
					continue;
				}
				addKeywordMap(keywords, wordArr[i]);
			}
		}
		
		//初始化例外关键词map
		List<Dict> excludeDicts = DictUtils.getDictList("relative_exclude_keyword");
		Map<Character, Object> excludeKeywords = new HashMap<Character, Object>(32);
		if (excludeDicts != null) {
			for (Dict excludeDict : excludeDicts) {
				String value = excludeDict.getValue();
				String[] wordArr = value.split(",");
				if (wordArr == null || wordArr.length == 0) {
					continue;
				}
				for (int i = 0; i < wordArr.length; i++) {
					String word = wordArr[i];
					if (StringUtils.isEmpty(word)) {
						continue;
					}
					addKeywordMap(excludeKeywords, wordArr[i]);
				}
			}
		}
		
		for (TBuyerContact contact : tBuyerContacts) {
			String name = contact.getContactName();
			contact.setRelativeMatch(false);
			if (name == null) {
				continue;
			}
			//大于5字不匹配
			if (name.length() > 5) {
				continue;
			}
			//如果匹配例外关键词，则不匹配亲戚关键词
			boolean matchExclude = isMatchKeywordMap(excludeKeywords, name);
			if (!matchExclude) {
				boolean match = isMatchKeywordMap(keywords, name);
				contact.setRelativeMatch(match);
			}
		}
	}
	
	/**
	 * 添加关键词map
	 * @param keywordMap
	 * @param word
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void addKeywordMap(Map<Character, Object> keywordMap, String word) {
		char end = (char) 1; 
		Map<Character, Object> nodeMap = keywordMap;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			Map<Character, Object> map = (Map<Character, Object>) nodeMap.get(c);
			if (map == null) {
				map = new HashMap<Character, Object>();
				nodeMap.put(c, map);
				nodeMap = map;
			} else {
				nodeMap = map;
			}
		}
		nodeMap.put(end, null);
	}
	
	/**
	 * 判断是否匹配关键词
	 * @param keywordMap
	 * @param word
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isMatchKeywordMap(Map<Character, Object> keywordMap, String word) {
		char end = (char) 1; 
		Map<Character, Object> nodeMap = null;
		for (int i = 0; i < word.length(); i++) {
			nodeMap = keywordMap;
			for (int j = i; j < word.length(); j++) {
				Map<Character, Object> map = (Map<Character, Object>) nodeMap.get(word.charAt(j));
				if (map == null) {
					break;
				}
				if (map.containsKey(end)) {
					return true;
				}
				nodeMap = map;
			}
		}
		
		return false;
	}
}