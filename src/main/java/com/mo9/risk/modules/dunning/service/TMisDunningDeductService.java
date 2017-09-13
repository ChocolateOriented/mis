/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.bean.Mo9ResponseData;
import com.mo9.risk.modules.dunning.bean.PayChannelInfo;
import com.mo9.risk.modules.dunning.dao.TMisDunningDeductDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.entity.BankCardInfo;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningDeduct;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.enums.PayStatus;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.jdbc.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 催收代扣Service
 * @author shijlu
 * @version 2017-04-11
 */
@Service
@Transactional(readOnly = true)
@Lazy(false)
public class TMisDunningDeductService extends CrudService<TMisDunningDeductDao, TMisDunningDeduct> {
	
	public static final ConcurrentHashMap<String, Long> dealcodeMap = new ConcurrentHashMap<String, Long>();
	
	@Autowired
	private TMisDunningPeopleDao tMisDunningPeopleDao;
	
	@Autowired
	private TMisDunningDeductDao tMisDunningDeductDao;
	
	@Autowired
	private TMisDunningDeductCallService tMisDunningDeductCallService;
	
	@Autowired
	private TMisChangeCardRecordService tMisChangeCardRecordService;
	
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoService;

	@Autowired
	private TMisDunningOrderService orderService;
	
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
		
		tMisDunningDeductCallService.saveDeductOrder(tMisDunningDeduct);
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
		currentRecord.setChargerate(tMisDunningDeduct.getChargerate());
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
	 * @param bankCard
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
	 * 获取当前支持的扣款渠道
	 * @param bankCardInfo
	 * @return
	 */
	public List<PayChannelInfo> getSupportedChannel(BankCardInfo bankCardInfo) {
		String bankname = null;
		if (bankCardInfo == null || (bankname = bankCardInfo.getBankname()) == null || "".equals(bankname)) {
			return new ArrayList<PayChannelInfo>();
		}
		return tMisDunningDeductDao.getSupportedChannel(bankname);
	}
	
	/**
	 * 生成订单流水号
	 * @param dealcode
	 * @return
	 */
	private String generateDeductCode(String dealcode) {
		String userId = UserUtils.getUser().getId();
		TMisDunningPeople tMisDunningPeople = null;
		if (userId != null) {
			tMisDunningPeople = tMisDunningPeopleDao.get(UserUtils.getUser().getId());
		}
		String peopleDbid = tMisDunningPeople == null ? "0" : String.valueOf(tMisDunningPeople.getDbid());
		StringBuilder builder = new StringBuilder();
		String random = Integer.toHexString(new Random().nextInt(65536));
		builder.append(dealcode).append(new Date().getTime()).append("0000".substring(0, 4 - random.length())).append(random).append(peopleDbid);
		return builder.toString();
	}
	
	/**
	 * 切换数据源查询订单
	 * @param dealcode
	 * @return
	 */
	public TMisDunningOrder getRiskOrderByDealcodeFromRisk(String dealcode) {
		String sql = "select dealcode, status, pay_code, CASE WHEN o.status = 'payoff' THEN 0 ELSE (IFNULL(o.credit_amount, 0) + IFNULL(o.overdue_amount, 0) - CASE WHEN o.modify_flag = 1 THEN IFNULL(o.modify_amount, 0) ELSE 0 END - IFNULL(o.balance, 0)) END as \"creditamount\" "
				+ "from t_risk_order o where o.dealcode = ?";
		
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
			
			TMisDunningOrder order = new TMisDunningOrder();
			String deal = (String) record.get("dealcode");
			String status = (String) record.get("status");
			BigDecimal creditamount = (BigDecimal) record.get("creditamount");
			String paycode = (String) record.get("pay_code");
			order.setDealcode(deal);
			order.setStatus(status);
			if (creditamount == null) {
				order.setCreditAmount(new BigDecimal("0"));
			} else {
				order.setCreditAmount(creditamount);
			}
			order.setPayCode(paycode);
			
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
	 * 验证银行支持的扣款渠道数
	 * @param bankCardInfo
	 * @return
	 */
	public boolean preCheckChannel(BankCardInfo bankCardInfo) {
		String bankname = null;
		if (bankCardInfo == null || (bankname = bankCardInfo.getBankname()) == null || "".equals(bankname)) {
			return false;
		}
		return preCheckChannel(bankname);
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
	
	/**
	 * 提交代扣订单
	 * @param tMisDunningDeduct
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, String> submitOrder(TMisDunningDeduct tMisDunningDeduct) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			Mo9ResponseData responseObj = tMisDunningDeductCallService.submitOrderInMo9(tMisDunningDeduct);
			
			if (responseObj == null) {
				result.put("result", "NO");
				result.put("msg", "系统错误");
				return result;
			}
			String status = responseObj.getStatus();
			
			if ("SUCCEEDED".equals(status)) {
				return dealResponseOrder(tMisDunningDeduct, responseObj.getData());
			} else if ("FAILED".equals(status)) {
				tMisDunningDeduct.setStatus(PayStatus.failed);
				tMisDunningDeduct.setStatusdetail(responseObj.getMessage());
				tMisDunningDeduct.setReason(responseObj.getReason());
				tMisDunningDeductCallService.updateDeductStatus(tMisDunningDeduct);
				
				result.put("result", "NO");
				result.put("msg", tMisDunningDeduct.getStatusdetail());
				return result;
			} else {
				//状态不明确时，尝试查单获取状态
				Mo9ResponseData queryResponse = tMisDunningDeductCallService.queryOrderStatusInMo9(tMisDunningDeduct);
				
				if (queryResponse == null || !"SUCCEEDED".equals(queryResponse.getStatus())) {
					result.put("result", "WARN");
					result.put("msg", "系统繁忙，请稍后查询");
					return result;
				}

				return dealResponseOrder(tMisDunningDeduct, responseObj.getData());
			}
			
		} catch (IOException e) {
			logger.info("mo9代扣接口url返回异常" + e.getMessage());
			
			result.put("result", "WARN");
			result.put("msg", "系统繁忙，请稍后查询");
			return result;
		}
	}
	
	/**
	 * 处理先玩后付返回订单状态
	 * @param tMisDunningDeduct
	 * @param responseOrder
	 * @return
	 */
	private Map<String, String> dealResponseOrder(TMisDunningDeduct tMisDunningDeduct, Mo9ResponseData.Mo9ResponseOrder responseOrder) {
		Map<String, String> result = new HashMap<String, String>();
		
		if (responseOrder == null) {
			result.put("result", "NO");
			result.put("msg", "系统错误");
			return result;
		}
		
		tMisDunningDeduct.setStatusdetail(responseOrder.getMessage());
		tMisDunningDeduct.setReason(responseOrder.getReason());
		tMisDunningDeduct.setChargerate(responseOrder.getChargeRate());
		
		if ("submitted".equals(responseOrder.getOrderStatus()) || "padding".equals(responseOrder.getOrderStatus())) {
			result.put("result", "OK");
			result.put("msg", "下单成功");
			return result;
		} else if ("succeeded".equals(responseOrder.getOrderStatus())) {
			tMisDunningDeduct.setStatus(PayStatus.succeeded);
			tMisDunningDeductCallService.updateDeductStatus(tMisDunningDeduct);
			tMisDunningDeductCallService.updateRepaymentStatus(tMisDunningDeduct);
			
			result.put("result", "OK");
			result.put("msg", "下单成功");
			return result;
		} else {
			tMisDunningDeduct.setStatus(PayStatus.failed);
			tMisDunningDeductCallService.updateDeductStatus(tMisDunningDeduct);
			
			result.put("result", "NO");
			if ("NO_BALANCE".equals(responseOrder.getReason())) {
				result.put("msg", "余额不足，本日请勿重复发起扣款");
			} else {
				result.put("msg", tMisDunningDeduct.getStatusdetail());
			}
			return result;
		}
	}
	
	/**
	 * 定时向先玩后付查询提交中订单状态
	 */
	@Scheduled(cron = "0 0/30 * * * ?")
	@Transactional(readOnly = false)
	public void querySubmittedDeductStatus() {
		List<TMisDunningDeduct> deductRecords = tMisDunningDeductDao.getSubmittedDeductList();
		
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
				} else if ("failed".equals(orderStatus) || "unmatched".equals(orderStatus)) {
					record.setStatus(PayStatus.failed);
				} else {
					continue;
				}
				
				record.setStatusdetail(responseOrder.getMessage());
				record.setReason(responseOrder.getReason());
				record.setChargerate(responseOrder.getChargeRate());
				updateRecord(record);
			}
		} catch(Exception e) {
			logger.info("代扣定时任务查询失败:" + e.getMessage());
		}
	}
	
	@Scheduled(cron = "0 0 15 * * ?")
	@Transactional(readOnly = false)
	public void batchDeduct() {
		logger.info("批量代扣开始");
		String scope = DictUtils.getDictLabel("scope", "batch_deduct", "");
		if ("".equals(scope) || "-".equals(scope)) {
			logger.info("批量代扣结束，批量代扣关闭");
			return;
		}
		
		DunningOrder dunningOrder = new DunningOrder();
		dunningOrder.setOverduedays(0);
		
		if (!"all".equals(scope)) {
			List<String> users = Arrays.asList(scope.split(","));
			List<String> peopleIds = tMisDunningDeductDao.getPeopleIdByUsers(users);
			
			if (peopleIds == null || peopleIds.size() == 0) {
				logger.info("批量代扣结束，没有查询到当前账号");
				return;
			}
			TMisDunningPeople dunningPeople = new TMisDunningPeople();
			dunningPeople.setQueryIds(peopleIds);
			dunningOrder.setDunningPeople(dunningPeople);
		}
		List<String> targetDealcode = tMisDunningDeductDao.getDealcodeByScope(dunningOrder);
		
		if (targetDealcode == null || targetDealcode.size() == 0) {
			logger.info("批量代扣结束，没有查询到需要代扣的订单");
			return;
		}
		
		//初始化渠道优先级列表
		List<Map<String, Integer>> channelTable = new ArrayList<Map<String,Integer>>();
		Map<String, Integer> channelLevelMap = new HashMap<String, Integer>();
		initChannelPriority(channelTable, channelLevelMap);
		
		//获取逾期0天可代扣的资方
		List<Dict> capitals = DictUtils.getDictList("captial_deduct");
		List<String> deductableCapital = new ArrayList<String>();
		for (Dict dict : capitals) {
			if ("0".equals(dict.getValue())) {
				deductableCapital.add(dict.getLabel());
			}
		}
		
		int total = targetDealcode.size();
		int skip = 0;
		int error = 0;
		int submit = 0;
		
		for (String dealcode : targetDealcode) {
			TMisDunningOrder order = getRiskOrderByDealcodeFromRisk(dealcode);
			if (order == null || "payoff".equals(order.getStatus())) {
				logger.info("批量代扣跳过，订单号：" + dealcode + "，订单已还清");
				skip++;
				continue;
			}
			
			//当前订单资方是否可发起批量代扣
			if (!checkBatchDeductCapital(order.getPayCode(), deductableCapital)) {
				logger.info("批量代扣跳过，订单号：" + dealcode + "，当前资方不可发起代扣");
				skip++;
				continue;
			}
			
			long timestamp = System.currentTimeMillis();
			Long value = dealcodeMap.putIfAbsent(dealcode, timestamp);
			
			//已存在订单号，有效期3分钟
			if (value != null && timestamp - value < 180 * 1000) {
				logger.info("批量代扣跳过，订单号：" + dealcode + "，当前订单代扣处理中");
				skip++;
				continue;
			}
			
			try {
				//当前订单代扣处理中
				if (!preCheckStatus(dealcode)) {
					logger.info("批量代扣跳过，订单号：" + dealcode + "，当前订单代扣处理中");
					skip++;
					continue;
				}
				//获取用户信息
				TRiskBuyerPersonalInfo peopleInfo = personalInfoService.getNewBuyerInfoByDealcode(dealcode);
				if (peopleInfo == null) {
					logger.info("批量代扣跳过，订单号：" + dealcode + "，用户信息不存在");
					skip++;
					continue;
				}

				//获取支持的渠道
				BankCardInfo bankCardInfo = tMisChangeCardRecordService.getBankByCard(peopleInfo.getRemitBankNo());
				List<PayChannelInfo> supportChannel = getSupportedChannel(bankCardInfo);
				if (supportChannel == null || supportChannel.size() == 0) {
					logger.info("批量代扣跳过，订单号：" + dealcode + "，当前银行卡不支持代扣");
					skip++;
					continue;
				}

				peopleInfo.setRemitBankName(bankCardInfo.getBankname());
				peopleInfo.setCreditAmount(order.getCreditAmount().toString());
				TMisDunningDeduct tMisDunningDeduct = createSysDeductOrder(peopleInfo);
				String payChannel = chooseChannel(supportChannel, channelTable, channelLevelMap);
				if (payChannel == null) {
					logger.info("批量代扣跳过，订单号：" + dealcode + "，当前渠道不支持批量代扣");
					skip++;
					continue;
				}
				tMisDunningDeduct.setPaychannel(payChannel);
				
				saveRecord(tMisDunningDeduct);
				submitOrder(tMisDunningDeduct);
				submit++;
			} catch (Exception e) {
				logger.info("批量代扣失败，订单号：" + dealcode + "，" + e.getMessage());
				error++;
			} finally {
				dealcodeMap.remove(dealcode);
			}
		}
		
		logger.info(MessageFormat.format("批量代扣结束，合计{0}条，跳过{1}条，失败{2}条，提交{3}条", total, skip, error, submit));
	}
	
	/**
	 * 初始化渠道信息用于渠道路由选择
	 * @param channelTable
	 * @param channelLevelMap
	 * @return
	 */
	private void initChannelPriority(List<Map<String, Integer>> channelTable, Map<String, Integer> channelLevelMap) {
		String priority = DictUtils.getDictLabel("priority", "batch_deduct", "");
		if (StringUtils.isBlank(priority)) {
			return;
		}
		
		String[] levelArr = priority.split(";");
		int i = 0;
		for (String level : levelArr) {
			String[] channels = level.split(",");
			Map<String, Integer> channelMap = new HashMap<String, Integer>();
			for (String channel : channels) {
				channelMap.put(channel, 0);
				channelLevelMap.put(channel, i);
			}
			channelTable.add(channelMap);
			i++;
		}
	}
	
	/**
	 * 根据渠道优先级选择渠道，相同优先级平均选择
	 * @param supportChannel
	 * @param channelTable
	 * @param channelLevelMap
	 * @return
	 */
	private String chooseChannel(List<PayChannelInfo> supportChannel, List<Map<String, Integer>> channelTable,  Map<String, Integer> channelLevelMap) {
		int level = Integer.MAX_VALUE;
		int cnt = Integer.MAX_VALUE;
		String result = null;
		
		for (PayChannelInfo channel : supportChannel) {
			String channelId = channel.getChannelid();
			Integer idx = channelLevelMap.get(channelId);
			if (idx == null) {
				continue;
			}
			
			if (idx > level) {
				continue;
			}
			
			Map<String, Integer> channelCntMap = channelTable.get(idx);
			Integer channelCnt = channelCntMap.get(channelId);
			if (channelCnt == null) {
				channelCntMap.put(channelId, 0);
				channelCnt = 0;
			}
			
			if (idx == level) {
				if (channelCnt < cnt) {
					cnt = channelCnt;
					result = channelId;
				}
			} else {
				level = idx;
				cnt = channelCnt;
				result = channelId;
			}
		}
		
		if (result != null) {
			channelTable.get(level).put(result, ++cnt);
		}
		return result;
	}
	
	/**
	 * 根据用户信息生成系统代扣订单
	 * @param peopleInfo
	 * @return
	 */
	private TMisDunningDeduct createSysDeductOrder(TRiskBuyerPersonalInfo peopleInfo) {
		TMisDunningDeduct deduct = new TMisDunningDeduct();
		deduct.setDealcode(peopleInfo.getDealcode());
		deduct.setBankcard(peopleInfo.getRemitBankNo());
		deduct.setBankname(peopleInfo.getRemitBankName());
		deduct.setIdcard(peopleInfo.getIdcard());
		deduct.setBuyername(peopleInfo.getRealName());
		deduct.setMobile(peopleInfo.getMobile());
		deduct.setPaytype("loan");
		BigDecimal payamount = new BigDecimal(peopleInfo.getCreditAmount());
		deduct.setPayamount(payamount.doubleValue());
		User user = new User();
		user.setName("sys");
		deduct.setCreateBy(user);
		deduct.setUpdateBy(user);
		deduct.setOperationtype("system");
		
		return deduct;
	}
	
	/**
	 * 验证订单的资方是否可批量代扣
	 * @param paycode
	 * @param deductableCapital
	 * @return
	 */
	private boolean checkBatchDeductCapital(String paycode, List<String> deductableCapital) {
		if (deductableCapital == null || deductableCapital.size() == 0) {
			return true;
		}
		
		if (StringUtils.isBlank(paycode)) {
			return false;
		}
		for (String capital : deductableCapital) {
			if (paycode.contains(capital)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @Description 尝试修复代扣异常订单, 当江湖救急响应码可靠时, 可以删除
	 * @param
	 * @return java.util.List<java.lang.String>
	 */
	@Scheduled(cron = "0 0 5,11,17,23 * * ?")
	@Transactional
	public void tryRepairAbnormalDeduct() {
		//查询代扣异常订单
		List<TMisDunningDeduct> abnormalDeducts = this.findAbnormalDeduct();
		logger.info("代扣状态异常订单:" + abnormalDeducts.size() + "条");

		//切换江湖救急库, 查询订单状态也为未还清的订单, 过滤未同步订单
		List<String> shouldPayoffOrderDelcodes = new ArrayList<String>();
		for (TMisDunningDeduct deduct: abnormalDeducts) {
			shouldPayoffOrderDelcodes.add(deduct.getDealcode());
		}
		int successCount = 0;
		List<String> abnormalOrders = orderService.findAbnormalOrderFromRisk(shouldPayoffOrderDelcodes);
		logger.info("江湖救急代扣状态异常订单:" + abnormalOrders.size() + "条",abnormalOrders);
		if (abnormalOrders == null || abnormalOrders.size() == 0) {
			return ;
		}

		//调用接口
		String paychannel = "bank";

		for (TMisDunningDeduct deduct : abnormalDeducts) {
			if (!abnormalOrders.contains(deduct.getDealcode())) {
				continue;
			}
			String dealcode = deduct.getDealcode();
			BigDecimal payamount = new BigDecimal(deduct.getPayamount());

			boolean success = orderService.tryRepairAbnormalOrder(dealcode,paychannel, DunningOrder.PAYTYPE_LOAN,payamount ,deduct.getThirdCode());
			if (success) {
				successCount++;
				//更新代扣
				deduct.setRepaymentstatus(PayStatus.succeeded);
				this.update(deduct);
				logger.debug("代扣信息:" + deduct.getId() + "订单" + deduct.getDealcode() + "修复成功");
				continue;
			}
			orderService.sendAbnormalOrderEmail("代扣",paychannel,dealcode,payamount,"应还清订单, 自动修复失败");
		}
		logger.info("代扣状态异常订单成功修复:" + successCount + "条");
	}
	/**
	 * @Description 查询代扣异常订单, 扣款类型为全款, 扣款状态为成功, 但是订单状态为未还清
	 * @param
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisDunningDeduct>
	 */
	public List<TMisDunningDeduct> findAbnormalDeduct() {
		return dao.findAbnormalDeduct();
	}
}