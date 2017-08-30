/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisRemittanceConfirmDao;
import com.mo9.risk.modules.dunning.entity.TMisPaid;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.modules.sys.entity.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 汇款确认信息Service
 * @author 徐盛
 * @version 2016-09-12
 */
@Service
@Lazy(false)
@Transactional(readOnly = true)
public class TMisRemittanceConfirmService extends CrudService<TMisRemittanceConfirmDao, TMisRemittanceConfirm> {

	@Autowired
	private TMisRemittanceConfirmDao misRemittanceConfirmDao;
	@Autowired
	private TMisRemittanceConfirmLogService tMisRemittanceConfirmLogService;
	@Autowired
	private TMisDunningOrderService orderService ;
	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;

	public TMisRemittanceConfirm get(String id) {
		return super.get(id);
	}
	
	public List<TMisRemittanceConfirm> findList(TMisRemittanceConfirm tMisRemittanceConfirm) {
		return super.findList(tMisRemittanceConfirm);
	}
	
	public Page<TMisRemittanceConfirm> findPage(Page<TMisRemittanceConfirm> page, TMisRemittanceConfirm tMisRemittanceConfirm) {
		return super.findPage(page, tMisRemittanceConfirm);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisRemittanceConfirm tMisRemittanceConfirm) {
		super.save(tMisRemittanceConfirm);
		tMisRemittanceConfirmLogService.saveLog(tMisRemittanceConfirm);
	}
	
	@Transactional(readOnly = false)
	public void delete(TMisRemittanceConfirm tMisRemittanceConfirm) {
		super.delete(tMisRemittanceConfirm);
		tMisRemittanceConfirmLogService.saveLog(tMisRemittanceConfirm.getId());
	}
	
	public TMisRemittanceConfirm findRemittanceConfirmColumnByDealcode(String code){
		return misRemittanceConfirmDao.findRemittanceConfirmColumnByDealcode(code);
	}
	
	/**
	 * 催收更新汇款数据
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int remittanceUpdate(TMisRemittanceConfirm entity){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CH_SUBMIT);
		int result = 0;
		result = misRemittanceConfirmDao.remittanceUpdate(entity);
		tMisRemittanceConfirmLogService.saveLog(entity.getId());
		return result;
	}
	
	/**
	 * 财务更新到账数据
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int financialUpdate(TMisRemittanceConfirm entity){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CW_SUBMIT);
		int result = 0;
		result = misRemittanceConfirmDao.financialUpdate(entity);
		tMisRemittanceConfirmLogService.saveLog(entity.getId());
		return result;
	}
	
	/**
	 * 财务打回到账数据
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int financialReturn(TMisRemittanceConfirm entity){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CW_RETURN);
		int result = 0;
		result = misRemittanceConfirmDao.financialReturn(entity);
		tMisRemittanceConfirmLogService.saveLog(entity.getId());
		return result;
	}
	
	/**
	 * 催收确认还款数据
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int confirmationUpdate(TMisRemittanceConfirm entity){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CH_CONFIRM);
		int result = 0;
		result = misRemittanceConfirmDao.confirmationUpdate(entity);
		tMisRemittanceConfirmLogService.saveLog(entity.getId());
		return result;
	}
	
	/**
	 * 催收确认合并还款数据
	 * @param entity
	 * @param relatedIds
	 * @return
	 */
	@Transactional(readOnly = false)
	public int confirmationMergeUpdate(TMisRemittanceConfirm entity, List<String> relatedIds){
		entity.preUpdate();
		entity.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CH_CONFIRM);
		Map<String, Object> param = new HashMap<String, Object>();
		relatedIds.add(entity.getId());
		param.put("ids", relatedIds);
		param.put("tMisRemittanceConfirm", entity);
		int result = 0;
		result = misRemittanceConfirmDao.confirmationMergeUpdate(param);
		tMisRemittanceConfirmLogService.saveLog(relatedIds);
		return result;
	}
	
	/**
	 * 返回是否是部分
	 * @param dealcode
	 * @return
	 */
	public int getResult(String dealcode){
		return misRemittanceConfirmDao.getResult(dealcode);
	}
	
	/**
	 * 根据催收唯一标示查询条数
	 * @param number
	 * @return
	 */
	public int getSerialnumber(String number){
		return misRemittanceConfirmDao.getSerialnumber(number);
	}
	
	/**
	 * 根据财务唯一标示查询条数
	 * @param number
	 * @return
	 */
	public int getFinancialserialnumber(String number){
		return misRemittanceConfirmDao.getFinancialserialnumber(number);
	}
	
	
	/**
	 * 获取延期次数
	 * @param orderid
	 * @return
	 */
	public int getExistDelayNumber(Integer orderid){
		return misRemittanceConfirmDao.getExistDelayNumber(orderid);
	}
	
	/**
	 * 获取关联的汇款记录
	 * @param tMisRemittanceConfirm
	 * @return
	 */
	public List<TMisRemittanceConfirm> findRelatedList(TMisRemittanceConfirm tMisRemittanceConfirm){
		return misRemittanceConfirmDao.findRelatedList(tMisRemittanceConfirm);
	}

	/**
	 * @Description 批量插入
	 * @param confirms
	 * @return void
	 */
	public void batchInsert(List<TMisRemittanceConfirm> confirms,User user) {
		if (null == confirms || confirms.size()==0){
			return;
		}
		for (TMisRemittanceConfirm remittanceConfirm: confirms) {
			remittanceConfirm.preInsert();
			remittanceConfirm.setUpdateBy(user);
			remittanceConfirm.setCreateBy(user);
		}
		dao.batchInsert(confirms);
		tMisRemittanceConfirmLogService.batchInsert(confirms,user);
	}

	/**
	 * @Description  提交审核流程--入账
	 * @param paid
	 * @param isMergeRepayment
	 * @param confirmid
	 * @param platform
	 * @param relatedId
	 * @return java.lang.String
	 */
	@Transactional
	public String checkConfirm(TMisPaid paid, String isMergeRepayment, String confirmid, String platform, String[] relatedId) throws IOException {
		String dealcode = paid.getDealcode();
		String paychannel = paid.getPaychannel();
		String remark = paid.getRemark();
		String paidType = paid.getPaidType();
		String paidAmount = paid.getPaidAmount();
		String delayDay = paid.getDelayDay();
		BigDecimal remittanceamount = new BigDecimal(paidAmount);

		if ("1".equals(isMergeRepayment)) {
			List<String> relatedIds = new ArrayList<String>(Arrays.asList(relatedId));
			this.confirmationMergeUpdate(new TMisRemittanceConfirm(confirmid, paidType, Double.parseDouble(paidAmount), TMisRemittanceConfirm.CONFIRMSTATUS_CH_CONFIRM), relatedIds);
		} else {
			this.confirmationUpdate(new TMisRemittanceConfirm(confirmid, paidType, Double.parseDouble(paidAmount), TMisRemittanceConfirm.CONFIRMSTATUS_CH_CONFIRM));
		}

		/**
		 * 部分还款，生成部分还款任务日志
		 */
		if("partial".equals(paidType)){
			tMisDunningTaskService.savePartialRepayLog(dealcode);
		}
		//回调江湖救急接口
		try {
			return orderService.repayWithCheack(dealcode, paychannel, remark, paidType, remittanceamount, delayDay);
		} catch (IOException e) {
			orderService.sendAbnormalOrderEmail(remark, paychannel, dealcode,remittanceamount,"网络异常:"+e.getMessage());
			throw e;
		}
	}

	/**
	 * @Description 提交查账流程--入账
	 * @param confirm
	 * @return void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void auditConfrim (TMisRemittanceConfirm confirm) throws IOException {
		//数据库加行锁控制并发
		logger.debug("正在获取锁TMisRemittanceConfirm:"+confirm.getId());
		TMisRemittanceConfirm lockedConfirm = this.getLockedRemittanceConfirm(confirm.getId());
		logger.debug("成功获取锁TMisRemittanceConfirm:"+confirm.getId());
		if (!TMisRemittanceConfirm.CONFIRMSTATUS_COMPLETE_AUDIT.equals(lockedConfirm.getConfirmstatus())){
			throw new ServiceException("汇款确认信息状态不为'已查账'");
		}

		//更新汇款确认信息
		confirm.preUpdate();
		confirm.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_FINISH);

		misRemittanceConfirmDao.auditConfrimUpdate(confirm);
		tMisRemittanceConfirmLogService.saveLog(confirm);

		String paidType = confirm.getPaytype();
		String dealcode = confirm.getDealcode();
		/**
		 * 部分还款，生成部分还款任务日志
		 */
		if("partial".equals(paidType)){
			tMisDunningTaskService.savePartialRepayLog(dealcode);
		}
		//回调江湖救急接口
		String delayDay = "7";
		String remark = "对公转账";
		String paychannel = confirm.getRemittancechannel();
		BigDecimal payamount =  new BigDecimal(confirm.getRemittanceamount());
		try {
			orderService.repayWithCheack(dealcode,paychannel,remark,paidType,payamount,delayDay);
		} catch (IOException e) {
			orderService.sendAbnormalOrderEmail(remark, paychannel, dealcode,payamount,"网络异常:"+e.getMessage());
			throw e;
		}
	}

	/**
	 * @Description 获取被锁的汇款确认信息
	 * @param id
	 * @return com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	private TMisRemittanceConfirm getLockedRemittanceConfirm(String id) {
		return dao.selectByIdForUpdate(id);
	}


	/**
	 * 定时检查催收已确认还清, 但是江湖救急处理异常的订单, 尝试重新调用3次还清接口, 若还是不行发送邮件
	 * 每6小时触发一次, 临时解决方案
	 */
	@Scheduled(cron = "0 0 */6 * * ?")
	public void tryRepairAbnormalRemittanceConfirm() {
		//查询对公入账异常订单
		List<TMisRemittanceConfirm> abnormalRemittanceConfirm = this.findAbnormalRemittanceConfirm();
		logger.info("对公交易状态异常订单:" + abnormalRemittanceConfirm.size() + "条");

		//切换江湖救急库, 查询订单状态也为未还清的订单, 过滤未同步订单
		List<String> shouldPayoffOrderDelcodes = new ArrayList<String>();
		for (TMisRemittanceConfirm remittanceConfirm : abnormalRemittanceConfirm) {
			shouldPayoffOrderDelcodes.add(remittanceConfirm.getDealcode());
		}

		int successCount = 0;
		List<String> abnormalOrders = orderService.findAbnormalOrderFromRisk(shouldPayoffOrderDelcodes);
		logger.info("江湖救急对公交易状态异常订单:" + abnormalOrders.size() + "条,"+abnormalOrders);
		if (abnormalOrders == null || abnormalOrders.size() == 0) {
			return ;
		}

		//调用接口
		String remark = "对公转账";

		for (TMisRemittanceConfirm remittanceConfirm : abnormalRemittanceConfirm) {
			if (!abnormalOrders.contains(remittanceConfirm.getDealcode())) {
				continue;
			}
			String dealcode = remittanceConfirm.getDealcode();
			String paychannel = remittanceConfirm.getRemittancechannel();
			BigDecimal payamount = new BigDecimal(remittanceConfirm.getRemittanceamount());

			boolean success = orderService.tryRepairAbnormalOrder(dealcode ,paychannel ,remark ,payamount);
			if (success) {
				successCount++;
				logger.debug("汇款信息:" + remittanceConfirm.getId() + "订单" + remittanceConfirm.getDealcode() + "修复成功");
				continue;
			}

			orderService.sendAbnormalOrderEmail(remark,paychannel,dealcode,payamount,"应还清订单, 自动修复失败");
		}
		logger.info("对公交易状态异常订单成功修复:" + successCount + "条");

	}

	/**
	 * @Description 查询对公入账异常订单, 还款类型为还清, 汇款确认状态为finish, 但订单状态为未还清
	 * @param
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm>
	 */
	public List<TMisRemittanceConfirm> findAbnormalRemittanceConfirm() {
		return dao.findAbnormalRemittanceConfirm();
	}

}