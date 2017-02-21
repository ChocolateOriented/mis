/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdbc.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TRiskBuyerContactRecordsDao;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.JedisUtils;

/**
 * 通话记录Service
 * @author beragao
 * @version 2016-07-15
 */
@Service
@Transactional(readOnly = true)
public class TRiskBuyerContactRecordsService {
	
	@Autowired
	private TRiskBuyerContactRecordsDao tRiskBuyerContactRecordsDao;
	
	public Page<TRiskBuyerContactRecords> findPage(Page<TRiskBuyerContactRecords> page, TRiskBuyerContactRecords tRiskBuyerContactRecords) {
		tRiskBuyerContactRecords.setPage(page);
		page.setList(tRiskBuyerContactRecordsDao.findList(tRiskBuyerContactRecords));
		return page;
	}
	
	private static int cacheSeconds = 1209600 ;
	
	/**
	 * redis 分页缓存
	 * @param page
	 * @param tRiskBuyerContactRecords
	 * @param buyerId
	 * @return
	 */
	public Page<TRiskBuyerContactRecords> findPageByRediscache(Page<TRiskBuyerContactRecords> page, TRiskBuyerContactRecords tRiskBuyerContactRecords,String buyerId) {
		String pageKey =   "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ tRiskBuyerContactRecords.getBuyerId() + "_PageNo:" + page.getPageNo() + "_PageSize:" + page.getPageSize() ;
		String pagecount = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_listSize_buyerId:"+ tRiskBuyerContactRecords.getBuyerId();
		if(null == JedisUtils.getObjectMap(pageKey)){
			int index = 1;
			int PageNo = 1;
			Map<String, Object> map2 = new HashMap<String, Object>();
//			List<TRiskBuyerContactRecords> contactRecordsList = tRiskBuyerContactRecordsDao.findBuyerContactRecordsListByBuyerId(tRiskBuyerContactRecords);
			DbUtils dbUtils = new DbUtils();
			List<TRiskBuyerContactRecords> contactRecordsList = null;
			try {
				contactRecordsList = dbUtils.findBuyerContactRecordsListByBuyerId(tRiskBuyerContactRecords.getBuyerId());
				System.out.println("通话记录读取条数："+contactRecordsList.size());
			} catch (Exception e) {
				System.out.println("通讯失败：buyerid-"+tRiskBuyerContactRecords.getBuyerId());
				e.printStackTrace();
			}
			if(!contactRecordsList.isEmpty()){
				for(TRiskBuyerContactRecords records : contactRecordsList){
					map2.put(String.valueOf(index), records);
					if(index % page.getPageSize() == 0){
						String key = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ tRiskBuyerContactRecords.getBuyerId() + "_PageNo:" + PageNo + "_PageSize:" + page.getPageSize() ;
						JedisUtils.setObjectMap( key , map2 , cacheSeconds);
						map2 = new HashMap<String, Object>();
						PageNo += 1;
						index = 0;
					}
					index += 1;
				}
				// 最后一页
				double lastpageNo =  Math.ceil((double)contactRecordsList.size() / (double)page.getPageSize()) + 1;
				if(lastpageNo  > PageNo){
					String key = "TRiskBuyerContactRecords_findBuyerContactRecordsListByBuyerId_buyerId:"+ tRiskBuyerContactRecords.getBuyerId() + "_PageNo:" + PageNo + "_PageSize:" + page.getPageSize() ;
					JedisUtils.setObjectMap( key , map2 , cacheSeconds);
				}
			}else{
				map2.put("0", new TRiskBuyerContactRecords());
				JedisUtils.setObjectMap( pageKey , map2 , cacheSeconds);
			}
			JedisUtils.setObject(pagecount, contactRecordsList.size(), cacheSeconds);
		}
		
		List<TRiskBuyerContactRecords> list = new ArrayList<TRiskBuyerContactRecords>();
		/* 获取redis缓存总条数  */
		long count = Long.valueOf(JedisUtils.getObject(pagecount).toString()); 
		if(count>0){
//			Map<Integer, Object> map = new TreeMap<Integer, Object>(JedisUtils.getObjectMap2(pageKey));
			Map<String, Object> map = JedisUtils.getObjectMap(pageKey);
			
			
			/* 获取操作记录  */
			Map<String, TRiskBuyerContactRecords> contantNumberMap =  getCountyHashMap(tRiskBuyerContactRecordsDao.findContantNumberListBydealcode(tRiskBuyerContactRecords));
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				TRiskBuyerContactRecords value = (TRiskBuyerContactRecords) entry.getValue();
				if(contantNumberMap.containsKey(value.getTel())){
					int smsNum = contantNumberMap.get(value.getTel()).getSmsNum();
					int telNum = contantNumberMap.get(value.getTel()).getTelNum();
					value.setSmsNum(smsNum);
					value.setTelNum(telNum);
				}else{
					value.setSmsNum(0);
					value.setTelNum(0);
				}
				list.add(value);
//				list.add(Integer.parseInt(entry.getKey()), value);
//				String key = entry.getKey().toString();
			}
		}
//		ListSortUtil<TRiskBuyerContactRecords> sortList = new ListSortUtil<TRiskBuyerContactRecords>();  
//		sortList.sort(list, "sumtime", "asc");
        Collections.sort(list, new Comparator<TRiskBuyerContactRecords>(){  
            public int compare(TRiskBuyerContactRecords o1, TRiskBuyerContactRecords o2) {  
                if(o1.getSumtime() > o2.getSumtime()){  
                    return -1;  
                }  
                if(o1.getSumtime() == o2.getSumtime()){  
                    return 0;  
                }  
                return 1;  
            }  
        }); 
//		Collections.sort(list.get(index));
		page.setCount(count);
		tRiskBuyerContactRecords.setPage(page);
		page.setList(list);
		return page;
	}
	
	
	/**
	 * list conversion map 
	 * @param list
	 * @return
	 */
	public static Map<String, TRiskBuyerContactRecords> getCountyHashMap(List<TRiskBuyerContactRecords> list) {
	    Map<String, TRiskBuyerContactRecords> map = new HashMap<String, TRiskBuyerContactRecords>();
		for(TRiskBuyerContactRecords entry : list){
			map.put(entry.getTel(), entry);
		}
	    return map;
	}
	
	/**
	 * 通话记录redis缓存查询
	 * @param tRiskBuyerContactRecords
	 * @return
	 */
	public List<TRiskBuyerContactRecords> findBuyerContactRecordsListByBuyerId(TRiskBuyerContactRecords tRiskBuyerContactRecords){
		return tRiskBuyerContactRecordsDao.findBuyerContactRecordsListByBuyerId(tRiskBuyerContactRecords);
	}
	
}