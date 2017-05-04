/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningDeductDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningDeductLogDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.Mo9ResponseData;
import com.mo9.risk.modules.dunning.entity.PayChannelInfo;
import com.mo9.risk.modules.dunning.entity.PayStatus;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 催收代扣Service
 * @author shijlu
 * @version 2017-04-11
 */
@Service
@Transactional(readOnly = true)
@Lazy(false)
public class TMisDunningDeductService extends CrudService<TMisDunningDeductDao, TMisDunningDeduct> {
	
	@Autowired
	private TMisDunningPeopleDao tMisDunningPeopleDao;
	
	@Autowired
	private TMisDunningDeductDao tMisDunningDeductDao;
	
	@Autowired
	private TMisDunningDeductLogDao tMisDunningDeductLogDao;
	
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoDao;
	
	@Autowired
	private TMisDunningDeductCallService tMisDunningDeductCallService;
	
	private static Logger logger = Logger.getLogger(TMisDunningDeductService.class);

	@Override
	public TMisDunningDeduct get(String deductcode) {
		return super.get(deductcode);
	}

	@Override
	public List<TMisDunningDeduct> findList(TMisDunningDeduct tMisDunningDeduct) {
		return super.findList(tMisDunningDeduct);
	}

	@Override
	public Page<TMisDunningDeduct> findPage(Page<TMisDunningDeduct> page, TMisDunningDeduct tMisDunningDeduct) {
		page.setOrderBy("dbid desc");
		return super.findPage(page, tMisDunningDeduct);
	}

	@Transactional(readOnly = false)
	public String saveRecord(TMisDunningDeduct tMisDunningDeduct) {
		
		tMisDunningDeduct.setDeductcode(generateDeductCode(tMisDunningDeduct.getDealcode()));
		tMisDunningDeduct.setStarttime(new Date());
		tMisDunningDeduct.setStatus(PayStatus.submitted);
		
		save(tMisDunningDeduct);
		saveDeductLog(tMisDunningDeduct); 
		return tMisDunningDeduct.getDeductcode();
	}
	
	@Transactional(readOnly = false)
	public boolean updateRecord(TMisDunningDeduct tMisDunningDeduct) {
		TMisDunningDeduct currentRecord = get(tMisDunningDeduct.getDeductcode());
		if (currentRecord.getStatus() != PayStatus.submitted) {
			return true;
		}
		
		currentRecord.setStatus(tMisDunningDeduct.getStatus());
		currentRecord.setStatusdetail(tMisDunningDeduct.getStatusdetail());
		currentRecord.setReason(tMisDunningDeduct.getReason());
		User user = new User("auto_admin");
		user.setName("auto_admin");
		currentRecord.setCreateBy(user);
		currentRecord.setUpdateBy(user);
		
		boolean deductStatus = tMisDunningDeductCallService.updateDeductStatus(currentRecord);
		if (deductStatus && tMisDunningDeduct.getStatus() == PayStatus.succeeded) {
			tMisDunningDeductCallService.updateRepaymentStatus(currentRecord);
		}
		
		//异步回调时只关注代扣状态
		return deductStatus;
	}
	
	@Transactional(readOnly = false)
	@Override
	public void save(TMisDunningDeduct tMisDunningDeduct) {
		super.save(tMisDunningDeduct);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void delete(TMisDunningDeduct tMisDunningDeduct) {
		super.delete(tMisDunningDeduct);
	}
	
	@Transactional(readOnly = false)
	public void update(TMisDunningDeduct tMisDunningDeduct) {
		tMisDunningDeduct.preUpdate();
		tMisDunningDeductDao.update(tMisDunningDeduct);
	}
	
	@Transactional(readOnly = true)
	public List<PayChannelInfo> getPaychannelList(String bankname) {
		List<PayChannelInfo> channelList = tMisDunningDeductDao.getPayChannelList(bankname);
		
		if (channelList == null || channelList.size() == 0) {
			return channelList;
		}
		
		for (PayChannelInfo channel : channelList) {
			if (channel.needCheckBalance()) {
				int num = tMisDunningDeductDao.getNoBalanceDeductNumByChannel(channel.getChannelid());
				channel.setIsusable(num == 0);
			}
		}
		
		return channelList;
	}
	
	/**
	 * 生成订单流水号
	 * @param dealcode
	 * @param peopleDbId
	 * @return
	 */
	private String generateDeductCode(String dealcode) {
		TMisDunningPeople tMisDunningPeople = tMisDunningPeopleDao.get(UserUtils.getUser().getId());
		StringBuilder builder = new StringBuilder();
		String random = Integer.toHexString(new Random().nextInt(65536));
		builder.append(dealcode).append(new Date().getTime()).append("0000".substring(0, 4 - random.length())).append(random).append(tMisDunningPeople.getDbid());
		return builder.toString();
	}
	
	@Transactional(readOnly = false)
	private void saveDeductLog(TMisDunningDeduct tMisDunningDeduct) {
		tMisDunningDeduct.preInsert();
		tMisDunningDeductLogDao.insert(tMisDunningDeduct);
	}
	
	/**
	 * 切换数据源查询订单
	 * @param dealcode
	 * @return
	 */
	public TRiskBuyerPersonalInfo getBuyerInfoByDealcodeFromRisk(String dealcode) {
		try {
			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");
			return personalInfoDao.getNewBuyerInfoByDealcode(dealcode);
		} catch (Exception e) {
			logger.info("切换数据源查询订单异常：" + e.getMessage());
			return null;
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");  
		}
	}
	
	/**
	 * 验证银行支持的扣款渠道数
	 * @param bankname
	 * @return
	 */
	public boolean preCheckChannel(String bankname) {
		int cnt = tMisDunningDeductDao.getSupportedChannelCount(bankname);
		return cnt > 0;
	}
	
	/**
	 * 验证当前订单代扣处理状态
	 * @param dealcode
	 * @return
	 */
	public boolean preCheckStatus(String dealcode) {
		TMisDunningDeduct tMisDunningDeduct = tMisDunningDeductDao.getLatestDeductByDealcode(dealcode);
		if (tMisDunningDeduct == null || tMisDunningDeduct.getStatus() == PayStatus.failed) {
			return true;
		}
		if ("partial".equals(tMisDunningDeduct.getPaytype()) && tMisDunningDeduct.getStatus() == PayStatus.succeeded && tMisDunningDeduct.getRepaymentstatus() == PayStatus.succeeded) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 查询渠道代扣成功率
	 * @param paychannel
	 * @return
	 */
	public List<PayChannelInfo> getSuccessRateByChannel(String paychannel) {
		return tMisDunningDeductDao.getSuccessRateByChannel(paychannel);
	}
	
	//@Scheduled(cron = "0 0/30 * * * ?")
	@Transactional(readOnly = false)
	public void querySubmittedDeductStatus() {
		List<TMisDunningDeduct> deductRecords = tMisDunningDeductDao.getDeductListByStatus("submitted");
		
		if (deductRecords == null || deductRecords.size() == 0) {
			return;
		}
		
		try {
			for (TMisDunningDeduct record: deductRecords) {
				Mo9ResponseData responseData = tMisDunningDeductCallService.queryOrderStatusInMo9(record);
				
				if (responseData == null || responseData.getData() == null) {
					continue;
				}
				Mo9ResponseData.Mo9ResponseOrder responseOrder = responseData.getData();
				
				String orderStatus = responseOrder.getOrderStatus();
				if ("succeeded".equals(orderStatus)) {
					record.setStatus(PayStatus.succeeded);
					record.setStatusdetail(responseOrder.getMessage());
					record.setReason(responseOrder.getReason());
					updateRecord(record);
				} else if ("failed".equals(orderStatus) || "unmatched".equals(orderStatus)) {
					record.setStatus(PayStatus.failed);
					record.setStatusdetail(responseOrder.getMessage());
					record.setReason(responseOrder.getReason());
					updateRecord(record);
				} else {
					continue;
				}
	
			}
		} catch(Exception e) {
			logger.info("代扣定时任务查询失败:" + e.getMessage());
		}
	}

}