/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisRemittanceConfirmDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningTaskLog;
import com.mo9.risk.modules.dunning.entity.TMisPaid;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.mo9.risk.modules.dunning.manager.RiskOrderManager;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 汇款确认信息Service
 * @author 徐盛
 * @version 2016-09-12
 */
@Service
@Transactional(readOnly = true)
public class TMisRemittanceConfirmService extends CrudService<TMisRemittanceConfirmDao, TMisRemittanceConfirm> {

	@Autowired
	private TMisRemittanceConfirmDao misRemittanceConfirmDao;
	@Autowired
	private TMisRemittanceConfirmLogService tMisRemittanceConfirmLogService;
	@Autowired
	private RiskOrderManager riskOrderManager ;
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
//		if(platform.equals("app")){
//			//do nothing
//		}else{
//			remittanceamount = remittanceamount.multiply(BigDecimal.valueOf(100));
//		}

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
		return riskOrderManager.repay(dealcode, paychannel, remark, paidType, remittanceamount, delayDay);
}

	/**
	 * @Description 提交查账流程--入账
	 * @param confirm
	 * @return void
	 */
	@Transactional
	public void auditConfrim(TMisRemittanceConfirm confirm) throws IOException {
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
		riskOrderManager.repay(dealcode,confirm.getRemittancechannel(),confirm.getRemark(),paidType, new BigDecimal(confirm.getRemittanceamount()),delayDay);
	}
}