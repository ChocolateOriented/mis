/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jboss.logging.Logger;
import org.jdbc.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.bean.Mo9ResponseData;
import com.mo9.risk.modules.dunning.bean.PayChannelInfo;
import com.mo9.risk.modules.dunning.dao.TMisDunningDeductDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningDeductLogDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.enums.PayStatus;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
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
	
	/**
	 * 获取扣款渠道列表
	 * @param bankname
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<PayChannelInfo> getPaychannelList(String bankname, String bankCard) {
		List<PayChannelInfo> channelList = tMisDunningDeductDao.getPayChannelList(bankname);
		
		if (channelList == null || channelList.size() == 0) {
			return channelList;
		}
		
		for (PayChannelInfo channel : channelList) {
			if (!channel.getIsusable()) {
				continue;
			}
			
			boolean balance = preCardBalanceLimit(channel.getChannelid(), bankCard);
			channel.setIsusable(balance);
		}
		
		return channelList;
	}
	
	/**
	 * 获取当前支持的扣款渠道
	 * @param bankname
	 * @return
	 */
	public List<PayChannelInfo> getSupportedChannel(String bankname) {
		return tMisDunningDeductDao.getSupportedChannel(bankname);
	}
	
	/**
	 * 生成订单流水号
	 * @param dealcode
	 * @param peopleDbId
	 * @return
	 */
	private String generateDeductCode(String dealcode) {
		TMisDunningPeople tMisDunningPeople = tMisDunningPeopleDao.get(UserUtils.getUser().getId());
		String peopleDbid = tMisDunningPeople == null ? "0" : String.valueOf(tMisDunningPeople.getDbid());
		StringBuilder builder = new StringBuilder();
		String random = Integer.toHexString(new Random().nextInt(65536));
		builder.append(dealcode).append(new Date().getTime()).append("0000".substring(0, 4 - random.length())).append(random).append(peopleDbid);
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
	public DunningOrder getRiskOrderByDealcodeFromRisk(String dealcode) {
		String sql = "select dealcode as \"dealcode\", status as \"status\", CASE WHEN o.status = 'payoff' THEN 0 ELSE (IFNULL(o.credit_amount, 0) + IFNULL(o.overdue_amount, 0) - CASE WHEN o.modify_flag = 1 THEN IFNULL(o.modify_amount, 0) ELSE 0 END - IFNULL(o.balance, 0)) END as \"creditamount\" "
				+ "from t_risk_order o where o.dealcode = ? limit 1";
		
		DbUtils dbUtils = new DbUtils();
		dbUtils.setJndiName("java:comp/env/jdbc/jeesite2");
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealcode);
		try {
			List<Map<String, Object>> result = dbUtils.getQueryList(sql, paramList);
			if (result == null || result.size() == 0) {
				return null;
			}
			Map<String, Object> record = result.get(0);
			
			DunningOrder order = new DunningOrder();
			String deal = (String) record.get("dealcode");
			String status = (String) record.get("status");
			BigDecimal creditamount = (BigDecimal) record.get("creditamount");
			order.setDealcode(deal);
			order.setStatus(status);
			if (creditamount == null) {
				order.setCreditamount(0D);
			} else {
				order.setCreditamount(creditamount.doubleValue());
			}
			
			return order;
		} catch (Exception e) {
			logger.info("切换数据源查询订单异常：" + e.getMessage());
			return null;
		}
	}
	
	/**
	 * 验证当前登陆用户是否为催收人员
	 * @return
	 */
	public boolean preCheckDunningPeople() {
		TMisDunningPeople tMisDunningPeople = tMisDunningPeopleDao.get(UserUtils.getUser().getId());
		return tMisDunningPeople != null;
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
	 * 验证银行卡是否有余额不足的限制
	 * @param paychannel
	 * @param bankcard
	 * @return
	 */
	public boolean preCardBalanceLimit(String paychannel, String bankcard) {
		String value =  DictUtils.getDictValue(paychannel, "channel_nobalance", "");
		if (value == null || "".equals(value)) {
			return true;
		}
		int limit = Integer.parseInt(value);
		TMisDunningDeduct tMisDunningDeduct = new TMisDunningDeduct();
		tMisDunningDeduct.setBankcard(bankcard);
		tMisDunningDeduct.setPaychannel(paychannel);
		int cnt = tMisDunningDeductDao.getNoBalanceDeductNum(tMisDunningDeduct);
		return cnt < limit;
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
	
	@Scheduled(cron = "0 0/30 * * * ?")
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