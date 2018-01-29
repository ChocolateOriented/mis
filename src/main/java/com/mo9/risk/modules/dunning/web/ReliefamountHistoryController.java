package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.DerateReason;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisReliefamountHistory;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.modules.dunning.service.TMisReliefamountHistoryService;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jxli on 2018/1/15.
 * 减免
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/reliefamount")
public class ReliefamountHistoryController extends BaseController {

	@Autowired
	private TMisReliefamountHistoryService tMisReliefamountHistoryService;
	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;

	/**
	 * 加载催收调整金额页面
	 * @param dealcode
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "reliefamountDialog")
	public String reliefamountDialog(String dealcode, Model model) {
		//获取所有的减免原因
		TMisReliefamountHistory tfHistory = tMisReliefamountHistoryService.getValidApply(dealcode);
		if (tfHistory == null){
			tfHistory = new TMisReliefamountHistory();
			tfHistory.setDealcode(dealcode);
		}
		double showAmount=	tMisDunningTaskService.findShowAmount(dealcode);
		model.addAttribute("showAmount",showAmount);
		model.addAttribute("tfHistory", tfHistory);
		model.addAttribute("derateReasonList", DerateReason.values());
		List<TMisReliefamountHistory> list = tMisReliefamountHistoryService.findPageListByDealcode(dealcode);
		model.addAttribute("list", list);
		return "modules/dunning/dialog/dialogCollectionAmount";
	}

	/**
	 * 保存减免申请
	 * @param tfHistory
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping(value = "applyFreeCreditAmount")
	@ResponseBody
	public String applyfreeCreditAmount(TMisReliefamountHistory tfHistory) {
		String dealcode = tfHistory.getDealcode();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskService.findDunningTaskByDealcode(params);
		if (task == null){
			return "无此催收任务";
		}
		String userId = UserUtils.getUser().getId();
		if (!userId.equals(task.getDunningpeopleid())){
			return "只有案件催收员本人可提出申请";
		}
		if (tMisReliefamountHistoryService.selectOneApplyByDealcode(dealcode) != null){
			return "已存在减免申请, 请等待处理";
		}
		tMisReliefamountHistoryService.applyfreeCreditAmount(tfHistory,userId);
		tMisReliefamountHistoryService.creatTaskIssue(tfHistory,UserUtils.getUser());
		return "OK";
	}

	/**
	 * 拒绝减免申请
	 * @param tfHistory
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:leaderview")
	@RequestMapping(value = "refuseFreeCreditAmount")
	@ResponseBody
	public String refuseFreeCreditAmount(TMisReliefamountHistory tfHistory) {
		String dealcode = tfHistory.getDealcode();
		String userId = UserUtils.getUser().getId();
		if (tMisReliefamountHistoryService.selectOneApplyByDealcode(dealcode) == null){
			return "无减免申请";
		}
		tMisReliefamountHistoryService.refuseFreeCreditAmount(tfHistory,userId);
		tMisReliefamountHistoryService.taskIssueResolution(tfHistory,UserUtils.getUser(),"拒绝减免");
		return "OK";
	}

	/**
	 * 保存减免金额
	 * @param tfHistory
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:leaderview")
	@RequestMapping(value = "savefreeCreditAmount")
	@ResponseBody
	public String savefreeCreditAmount(TMisReliefamountHistory tfHistory) {
		String amount = tfHistory.getReliefamount();
		Integer freeCreditAmount = 0;
		for (Role r : UserUtils.getUser().getRoleList()){
			if(("减免无上限").equals(r.getName())){
				freeCreditAmount = 1;
				break;
			}
		}
		String dealcode = tfHistory.getDealcode();
		double maxModifyAmount = tMisDunningTaskService.findMaxModifyAmount(dealcode);
		if(freeCreditAmount != 1 && Double.parseDouble(amount) > maxModifyAmount){
			return "减免金额过大,请检查!";
		}
		try {
			DynamicDataSource.setCurrentLookupKey("updateOrderDataSource");
			tMisDunningTaskService.updateOrderModifyAmount(dealcode, amount);
		} catch (Exception e) {
			logger.info("",e);
			return "error";

		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskService.findDunningTaskByDealcode(params);
		if (task == null){
			return "无此催收任务";
		}

		tMisReliefamountHistoryService.savefreeCreditAmount(tfHistory,UserUtils.getUser().getId() ,task.getId());
		tMisReliefamountHistoryService.taskIssueResolution(tfHistory,UserUtils.getUser(),"同意减免");
		return "OK";
	}

}
