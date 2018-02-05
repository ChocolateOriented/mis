package com.mo9.risk.modules.dunning.service;

import static com.mo9.risk.modules.dunning.service.TMisDunningTaskService.DUNNING_ALL_PERMISSIONS;
import static com.mo9.risk.modules.dunning.service.TMisDunningTaskService.DUNNING_COMMISSIONER_PERMISSIONS;
import static com.mo9.risk.modules.dunning.service.TMisDunningTaskService.DUNNING_INNER_PERMISSIONS;
import static com.mo9.risk.modules.dunning.service.TMisDunningTaskService.DUNNING_OUTER_PERMISSIONS;
import static com.mo9.risk.modules.dunning.service.TMisDunningTaskService.DUNNING_SUPERVISOR;
import static com.mo9.risk.modules.dunning.service.TMisDunningTaskService.getPermissions;

import com.mo9.risk.modules.dunning.bean.TaskIssuePage;
import com.mo9.risk.modules.dunning.dao.TaskIssueDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TaskIssue;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueChannel;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueStatus;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueType;
import com.mo9.risk.modules.dunning.entity.TaskIssue.RemindingType;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客服问题service
 * Created by qtzhou on 2017/10/31.
 */
@Service
@Transactional(readOnly = true)
public class TaskIssueService extends CrudService<TaskIssueDao, TaskIssue> {

	@Autowired
	TMisDunningGroupService tMisDunningGroupService;
	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;
	@Autowired
	private TMisDunningPeopleService peopleService;
	@Autowired
	private FeedbackSendService feedbackSendService;


	@Override
	@Transactional
	public void save(TaskIssue entity) {
		if (entity.getId() !=null && dao.get(entity) == null){
			entity.setIsNewRecord(true);
		}
		super.save(entity);
	}

	public Page<TaskIssuePage> notifyList(Page<TaskIssuePage> page, TaskIssuePage taskIssue) {
		int permissions = getPermissions();
		List<String> allowedGroupIds = new ArrayList<String>();
		if (DUNNING_COMMISSIONER_PERMISSIONS == permissions) {
			taskIssue.setDunningpeopleid(UserUtils.getUser().getId());
		}
		if (DUNNING_INNER_PERMISSIONS == permissions) {
			taskIssue.setDunningpeopleid(null);
			allowedGroupIds.addAll(tMisDunningGroupService.findIdsByLeader(UserUtils.getUser()));
			taskIssue.setGroupIds(allowedGroupIds);
		}
		if (DUNNING_OUTER_PERMISSIONS == permissions) {
			taskIssue.setDunningpeopleid(null);
		}
		if (DUNNING_SUPERVISOR == permissions) {
			TMisDunningGroup group = new TMisDunningGroup();
			group.setSupervisor(UserUtils.getUser());
			List<String> groupIds = tMisDunningGroupService.findSupervisorGroupList(group);
			allowedGroupIds.addAll(groupIds);
			taskIssue.setGroupIds(allowedGroupIds);
		}
		if (DUNNING_ALL_PERMISSIONS == permissions) {
			taskIssue.setDunningpeopleid(null);
		}
		taskIssue.setCurrentUserId(UserUtils.getUser().getId());
		taskIssue.setPage(page);
		page.setOrderBy("createDate DESC");
		page.setList(dao.notifyList(taskIssue));
		return page;
	}

	/**
	 * @return void
	 * @Description 插入浏览日志
	 */
	@Transactional
	public void addReadRecordIfneedRemind(TaskIssue taskIssue, User user) {
		String dealcode = taskIssue.getDealcode();
		RemindingType remindingType = taskIssue.getRemindingType();
		if (user == null){
			return;
		}
		switch (remindingType) {
			case NONE:
				return;
			case DUNNING_PEOPLE:
				TMisDunningTask task = tMisDunningTaskService.findDunningTaskByDealcode(dealcode);
				if (task == null ||!user.getId().equals(task.getDunningpeopleid())) {
					return;
				}
				dao.addReadRecord(taskIssue.getId(), user.getId());
				return;
			default:
				return;
		}
	}

	//查询客服消息列表
	public Page<TaskIssue> findFeedbackList(Page<TaskIssue> page, String dealcode) {
		TaskIssue taskIssue = new TaskIssue();
		taskIssue.setDealcode(dealcode);
		taskIssue.setIssueChannel(IssueChannel.CUSTOMER_SERVICE);
		taskIssue.setPage(page);
		page.setOrderBy("status DESC");
		page.setList(dao.findList(taskIssue));
		return page;
	}

	/**
	 * @return java.lang.Integer
	 * @Description 待解决问题通知数
	 */
	public Integer remindIssueCount(String userid) {
		//目前只对催收员做提醒
		int permissions = getPermissions();
		if (DUNNING_COMMISSIONER_PERMISSIONS != permissions) {
			return null;
		}
		return dao.dunningPeopleRemindCount(userid);
	}

	/**
	 * @return void
	 * @Description 手工解决问题
	 */
	@Transactional
	public void manualResolution(String id, String handlingresult, User user) {
		TaskIssue taskIssue = new TaskIssue();
		taskIssue.setId(id);
		taskIssue.setStatus(IssueStatus.RESOLVED);
		taskIssue.setHandlingResult(handlingresult);
		taskIssue.setUpdateRole("解决人");
		taskIssue.setIssueChannel(IssueChannel.CUSTOMER_SERVICE);
		//更新人若是有花名则使用花名
		String updateUserName = user.getName();
		if (user != null && StringUtils.isNotBlank(user.getId())) {
			TMisDunningPeople people = peopleService.get(user.getId());
			if (people != null && StringUtils.isNotBlank(people.getNickname())) {
				updateUserName = people.getNickname();
			}
		}
		this.update(taskIssue, updateUserName);
	}

	@Transactional
	private void update(TaskIssue taskIssue, String updateUserName) {
		User updateBy = new User();
		updateBy.setName(updateUserName);
		taskIssue.setUpdateDate(new Date());
		taskIssue.setUpdateBy(updateBy);
		dao.update(taskIssue);
		//若已完成客服反馈问题, 则通知客服系统
		if (IssueStatus.RESOLVED.equals(taskIssue.getStatus()) && IssueChannel.CUSTOMER_SERVICE.equals(taskIssue.getIssueChannel())) {
			feedbackSendService.createFeedBackRecord(taskIssue);
		}
	}

	/**
	 * @return void
	 * @Description 系统自动解决问题
	 */
	@Transactional
	public void autoResolution(String dealcode, IssueType issueType, String handlingResult, User user, String updateRole) {
		//查询该订单所有为解决问题
		TaskIssue queryIssue = new TaskIssue();
		queryIssue.setStatus(IssueStatus.UNRESOLVED);
		queryIssue.setDealcode(dealcode);
		List<TaskIssue> issues = this.findList(queryIssue);

		for (TaskIssue issue : issues) {
			Set<IssueType> issueTypese = issue.getIssueTypes();
			//若未解决问题包不含处理的类型则跳过
			if (!issueTypese.contains(issueType)) {
				continue;
			}
			Set<IssueType> handlingIssueTypes = issue.getHandlingIssueTypes();
			if (handlingIssueTypes == null){
				handlingIssueTypes = new HashSet<>();
			}
			if (handlingIssueTypes.contains(issueType)) {
				continue;
			}
			handlingIssueTypes.add(issueType);
			issue.setHandlingIssueTypes(handlingIssueTypes);

			String oldHandlingResult = issue.getHandlingResult();
			if (StringUtils.isNotBlank(oldHandlingResult)) {
				handlingResult = oldHandlingResult + "," + handlingResult;
			}
			issue.setHandlingResult(handlingResult);
			issue.setUpdateRole(updateRole);
			//全部处理完成
			if (handlingIssueTypes.containsAll(issueTypese)) {
				issue.setStatus(IssueStatus.RESOLVED);
			}
			//如果没有用户, 则选该案件催收员
			String updateUserName = user.getName();
			try {
				if (user == null) {
					TMisDunningTask task = tMisDunningTaskService.findDunningTaskByDealcode(dealcode);
					TMisDunningPeople people = peopleService.get(task.getDunningpeopleid());
					updateUserName = people.getNickname();
				}
				this.update(issue, updateUserName);
			} catch (Exception e) {
				logger.info("案件问题通知自动解决失败", e);
			}
		}
	}

	/**
	 * @return void
	 * @Description 系统自动解决问题
	 */
	public void autoResolution(String dealcode, IssueType issueType, String handlingResult, User user) {
		this.autoResolution(dealcode, issueType, handlingResult, user, "解决人");
	}
}
