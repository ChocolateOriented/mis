package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.bean.TaskIssuePage;
import com.mo9.risk.modules.dunning.entity.TaskIssue;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueStatus;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueType;
import com.mo9.risk.modules.dunning.service.TaskIssueService;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description 案件待解决问题通知
 * @author jxli
 * @version 2018/1/26
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/taskIssue")
public class TaskIssueController extends BaseController {

	@Autowired
	private TaskIssueService taskIssueService;

	/**
	 * 问题案件通知列表
	 */
	@RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
	@RequestMapping(value = {"notify", ""})
	public String notifyList(@ModelAttribute("taskIssue") TaskIssuePage taskIssue, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			Page<TaskIssuePage> page = taskIssueService.notifyList(new Page<TaskIssuePage>(request, response, 20), taskIssue);
			model.addAttribute("page", page);
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}

		model.addAttribute("issueType", IssueType.values());
		model.addAttribute("issueStatus", IssueStatus.values());
		return "modules/oa/notifyList";
	}

	/**
	 * 问题案件通知详情
	 */
	@RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
	@RequestMapping(value = "detailsDialog")
	public String detailsDialog(String id, Boolean read, Model model) {
		if (StringUtils.isBlank(id)) {
			return "views/error/500";
		}
		TaskIssue taskIssue = taskIssueService.get(id);
		model.addAttribute("taskIssue", taskIssue);
		if (!read) {
			taskIssueService.addReadRecordIfneedRemind(taskIssue, UserUtils.getUser());
		}
		return "modules/dunning/dialog/tMisCustomerJboxNotify";
	}

	/**
	 * 待解决问题通知数
	 */
	@RequestMapping(value = "remindIssueCount")
	@ResponseBody
	public Integer remindIssueCount() {
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			String userid = UserUtils.getUser().getId();
			Integer count = taskIssueService.remindIssueCount(userid);
			return count;
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
	}
	/**
	 * @return java.lang.String
	 * @Description 客服问题列表
	 */
	@RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
	@RequestMapping(value = {"feedbackList", ""})
	public String feedbackList(String dealcode, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			Page<TaskIssue> page = taskIssueService.findFeedbackList(new Page<TaskIssue>(request, response), dealcode);
			model.addAttribute("page", page);
			model.addAttribute("dealcode", dealcode);
			return "modules/dunning/tMisCustomerServiceFeedbackList";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
	}

	/**
	 * 客服问题解决弹框
	 */
	@RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
	@RequestMapping(value = "manualResolutionDialog")
	public String manualResolutionDialog(String id, Model model) {
		model.addAttribute("id", id);
		return "modules/dunning/dialog/tMisCustomerJboxResult";
	}

	/**
	 * 操作待解决结果
	 */
	@RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
	@RequestMapping(value = "resultSave")
	@ResponseBody
	public String resultSave(String id, String handlingresult) {
		User user = UserUtils.getUser();
		taskIssueService.manualResolution(id, handlingresult, user);
		return "OK";
	}

}
