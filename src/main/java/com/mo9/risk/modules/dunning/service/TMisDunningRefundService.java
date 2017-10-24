/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisDunningRefundDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningRefund;
import com.mo9.risk.modules.dunning.entity.TMisDunningRefund.RefundStatus;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 退款记录Service
 * @author jxli
 * @version 2017-09-22
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningRefundService extends CrudService<TMisDunningRefundDao, TMisDunningRefund> {
	@Autowired
	TMisRemittanceConfirmService confirmService;

	public TMisDunningRefund get(String refundCode) {
		return super.get(refundCode);
	}
	
	public List<TMisDunningRefund> findList(TMisDunningRefund tMisDunningRefund) {
		return super.findList(tMisDunningRefund);
	}
	
	public Page<TMisDunningRefund> findPage(Page<TMisDunningRefund> page, TMisDunningRefund tMisDunningRefund) {
		return super.findPage(page, tMisDunningRefund);
	}
	
	@Transactional(readOnly = false)
	public void save(TMisDunningRefund tMisDunningRefund) {
		super.save(tMisDunningRefund);
	}

	/**
	 * @Description 发起退款
	 * @param refundCode 退款号
	 * @param amount 退款金额
	 * @param remittanceSerialNumber 对应汇款流水
	 * @param remittanceChannel 对应汇款渠道
	 * @return void
	 */
	@Transactional(readOnly = false)
	public void launch(String refundCode, BigDecimal amount, String remittanceSerialNumber, String remittanceChannel) {
		TMisDunningRefund oldRefund = this.get(refundCode);
		if (oldRefund !=null){//消息已处理
			return;
		}

		//记录新的退款消息
		TMisDunningRefund refund = new TMisDunningRefund();
		refund.setRefundCode(refundCode);
		refund.setAmount(amount);
		refund.setRemittanceSerialNumber(remittanceSerialNumber);
		refund.setRemittanceChannel(remittanceChannel);
		refund.setRefundStatus(RefundStatus.LAUNCH);
		dao.insert(refund);
	}

	/**
	 * @Description  退款中, 批量处理
	 * @param auditor
	 * @param auditTime
	 * @param refundCodes
	 * @return void
	 */
	@Transactional(readOnly = false)
	public void process(String auditor, Date auditTime, List<String> refundCodes) {
		for (String refundCode: refundCodes) {
			TMisDunningRefund oldRefund = this.get(refundCode);
			if (oldRefund == null){
				throw new ServiceException(refundCode+"无退款消息");
			}
			RefundStatus oldStatus = oldRefund.getRefundStatus();
			if (RefundStatus.PROCESS.equals(oldStatus)){//已处理
				continue;
			}
			if (!RefundStatus.LAUNCH.equals(oldStatus)){
				throw new ServiceException(refundCode+"已经处于"+oldStatus.desc);
			}

			oldRefund.setAuditor(auditor);
			oldRefund.setAuditTime(auditTime);
			oldRefund.setRefundStatus(RefundStatus.PROCESS);
			dao.update(oldRefund);
		}
	}

	/**
	 * @Description  无效退款
	 * @param refundCode
	 * @param refundStatus
	 * @return void
	 */
	@Transactional(readOnly = false)
	public void invalidRefund(String refundCode, RefundStatus refundStatus) {
		if (!TMisDunningRefund.INVALID_REFUND_STATUS_LIST.contains(refundStatus)){
			throw new IllegalArgumentException("参数退款状态不属于无效退款");
		}

		TMisDunningRefund refund = this.get(refundCode);
		if (refund == null) {
			throw new ServiceException("无退款中记录");
		}
		RefundStatus oldStatus = refund.getRefundStatus();
		if (refundStatus.equals(oldStatus)) {//已处理
			return;
		}
		if (RefundStatus.FINISH.equals(oldStatus)) {
			throw new ServiceException("失败, 该退款已完成");
		}
		refund.setRefundStatus(refundStatus);
		dao.update(refund);
	}

	/**
	 * @Description  已完成退款
	 * @param refundCode
	 * @param refundTime
	 * @return void
	 */
	@Transactional(readOnly = false)
	public void finish(String refundCode, Date refundTime) {
		TMisDunningRefund refund = this.get(refundCode);
		if (refund == null){
			throw new ServiceException("无退款中记录");
		}
		RefundStatus refundStatus = refund.getRefundStatus();
		if (RefundStatus.FINISH.equals(refundStatus)){//已处理
			return;
		}
		if (!RefundStatus.PROCESS.equals(refundStatus)){
			throw new ServiceException("退款记录处于"+refundStatus.desc);
		}

		refund.setRefundStatus(RefundStatus.FINISH);
		refund.setRefundTime(refundTime);
		dao.update(refund);

		String serialNumber = refund.getRemittanceSerialNumber();
		String channel = refund.getRemittanceChannel();
		if (StringUtils.isBlank(serialNumber)){
			return;
		}
		//删除已查账的汇款确认信息
		List<TMisRemittanceConfirm> confirms = confirmService.findCompleteAuditBySerialNumber(serialNumber,channel);
		for (TMisRemittanceConfirm  confirm: confirms) {
			logger.debug("删除汇款确认信息"+confirm.getId());
			confirmService.delete(confirm);
		}
	}

}