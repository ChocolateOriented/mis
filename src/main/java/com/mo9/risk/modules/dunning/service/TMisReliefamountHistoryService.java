/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory.ReliefamountStatus;
import com.mo9.risk.modules.dunning.entity.TaskIssue;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueChannel;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueStatus;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueType;
import com.mo9.risk.modules.dunning.entity.TaskIssue.RemindingType;
import com.thinkgem.jeesite.modules.sys.entity.User;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisReliefamountHistoryDao;
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 减免记录Service
 *
 * @author 徐盛
 * @version 2016-08-05
 */
@Service
@Transactional(readOnly = true)
public class TMisReliefamountHistoryService extends CrudService<TMisReliefamountHistoryDao, TMisReliefamountHistory> {

	@Autowired
	private TMisReliefamountHistoryDao tMisReliefamountHistoryDao;
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	@Autowired
	private TaskIssueService taskIssueService;

	public TMisReliefamountHistory get(String id) {
		return super.get(id);
	}

	public List<TMisReliefamountHistory> findList(TMisReliefamountHistory tMisReliefamountHistory) {
		return super.findList(tMisReliefamountHistory);
	}

	public Page<TMisReliefamountHistory> findPage(Page<TMisReliefamountHistory> page, TMisReliefamountHistory tMisReliefamountHistory) {
		return super.findPage(page, tMisReliefamountHistory);
	}

	@Transactional(readOnly = false)
	public void save(TMisReliefamountHistory tMisReliefamountHistory) {
		super.save(tMisReliefamountHistory);
	}

	@Transactional(readOnly = false)
	public void delete(TMisReliefamountHistory tMisReliefamountHistory) {
		super.delete(tMisReliefamountHistory);
	}

	public List<TMisReliefamountHistory> findPageListByDealcode(String code) {
		return tMisReliefamountHistoryDao.findPageListByDealcode(code);
	}


	@Transactional(readOnly = false)
	public boolean savefreeCreditAmount(TMisReliefamountHistory tfHistory, String userId, String taskId) {
		/**
		 *  保存此订单当前任务的减免金额
		 */
		TMisDunningTask task = tMisDunningTaskDao.get(taskId);
		task.setReliefamount((int) (Double.parseDouble(tfHistory.getReliefamount()) * 100));
		tMisDunningTaskDao.update(task);

		tfHistory.setCheckUserId(userId);
		tfHistory.setCheckTime(new Date());
		tfHistory.setStatus(ReliefamountStatus.AGREE);
		this.save(tfHistory);
		return true;
	}

	/**
	 * @return void
	 * @Description 申请减免
	 */
	@Transactional(readOnly = false)
	public void applyfreeCreditAmount(TMisReliefamountHistory tfHistory, String userId) {
		tfHistory.setApplyTime(new Date());
		tfHistory.setApplyUserId(userId);
		tfHistory.setStatus(ReliefamountStatus.APPLY);
		this.save(tfHistory);
	}

	/**
	 * @return com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory
	 * @Description 查询申请中的减免
	 */
	public TMisReliefamountHistory selectOneApplyByDealcode(String dealcode) {
		TMisReliefamountHistory reliefamountHistory = new TMisReliefamountHistory();
		reliefamountHistory.setStatus(ReliefamountStatus.APPLY);
		reliefamountHistory.setDealcode(dealcode);
		List<TMisReliefamountHistory> reliefamountHistories = this.findList(reliefamountHistory);
		if (reliefamountHistories == null || reliefamountHistories.size() == 0) {
			return null;
		}
		return reliefamountHistories.get(0);
	}

	/**
	 * @return void
	 * @Description 拒绝减免申请
	 */
	@Transactional(readOnly = false)
	public void refuseFreeCreditAmount(TMisReliefamountHistory tfHistory, String userId) {
		tfHistory.setCheckTime(new Date());
		tfHistory.setCheckUserId(userId);
		tfHistory.setStatus(ReliefamountStatus.REFUSE);
		this.save(tfHistory);
	}

	/**
	 * @return com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory
	 * @Description 若申请人与案件负责人不符则拒绝, 相符则获取
	 */
	@Transactional(readOnly = false)
	public TMisReliefamountHistory getValidApply(String dealcode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		String dunningpeopleid = task.getDunningpeopleid();

		return this.getValidApply(dealcode, dunningpeopleid);
	}

	/**
	 * @return com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory
	 * @Description 若申请人与案件负责人不符则拒绝, 相符则获取
	 */
	@Transactional(readOnly = false)
	public TMisReliefamountHistory getValidApply(String dealcode, String dunningpeopleid) {
		TMisReliefamountHistory reliefamountHistory = selectOneApplyByDealcode(dealcode);
		if (reliefamountHistory == null) {
			return null;
		}
		if (dunningpeopleid == null || !dunningpeopleid.equals(reliefamountHistory.getApplyUserId())) {
			this.refuseFreeCreditAmount(reliefamountHistory, "1");
			return null;
		}
		return reliefamountHistory;
	}

	/**
	 * @return void
	 * @Description 创建未解决问题通知
	 */
	@Transactional
	public void creatTaskIssue(TMisReliefamountHistory tfHistory, User user) {
		TaskIssue taskIssue = new TaskIssue();
		taskIssue.setStatus(IssueStatus.UNRESOLVED);
		taskIssue.setIssueChannel(IssueChannel.LOCAL_SYSTEM);
		taskIssue.setRemindingType(RemindingType.NONE);
		Set<IssueType> issueTypes = new HashSet<>();
		issueTypes.add(IssueType.RELIEF_REQUEST);
		taskIssue.setIssueTypes(issueTypes);
		taskIssue.setDealcode(tfHistory.getDealcode());
		taskIssue.setDescription(String
				.format("减免金额:%s, 减免原因 %s, 备注: %s", tfHistory.getReliefamount(), tfHistory.getDerateReason().getDerateReasonName(), tfHistory.getRemarks()));
		taskIssue.setRecorderName(user.getName());
		taskIssue.setCreateDate(new Date());
		taskIssue.setUpdateRole("申请人");
		taskIssueService.save(taskIssue);
	}

	/**
	 * @return void
	 * @Description 处理为解决问题通知
	 */
	@Transactional
	public void taskIssueResolution(TMisReliefamountHistory tfHistory, User user, String handlingResult) {
		taskIssueService.autoResolution(tfHistory.getDealcode(), IssueType.RELIEF_REQUEST, handlingResult, user, "审批人");
	}
}