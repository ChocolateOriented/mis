/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.SMisDunningProductivePowerDailyReport;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.service.SMisDunningProductivePowerDailyReportService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 催收员案件活动日报Controller
 * @author 李敬翔
 * @version 2017-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/sMisDunningProductivePowerDailyReport")
public class SMisDunningProductivePowerDailyReportController extends BaseController {

	@Autowired
	private SMisDunningProductivePowerDailyReportService productivePowerDailyReportService;
	@Autowired
	private TMisDunningGroupService groupService;
	
	/**
	 * @Description: 催收员案件活动日报
	 * @param performanceDayReport
	 * @param dunningPeople
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @return: String
	 */
	@RequiresPermissions("dunning:sMisDunningProductivePowerDailyReport:view")
	@RequestMapping(value = {"list",""})
	public String findDunningProductivePowerDailyReport(SMisDunningProductivePowerDailyReport smMisDunningProductivePowerDailyReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Page<SMisDunningProductivePowerDailyReport> page = productivePowerDailyReportService.findPage(new Page<SMisDunningProductivePowerDailyReport>(request, response), smMisDunningProductivePowerDailyReport);
			model.addAttribute("page", page);
			model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
			
			//催收小组列表,若无限制则查询所有小组
			List<TMisDunningGroup> groups = smMisDunningProductivePowerDailyReport.getQueryGroups() ;
			if (groups == null || groups.size() == 0) {
				groups = groupService.findList(new TMisDunningGroup()) ;
			}
			model.addAttribute("groupList", groups);
			
			//若只管理一个小组则默认选中
			if (groups.size() == 1) {
				smMisDunningProductivePowerDailyReport.setGroupId(groups.get(0).getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/dunning/sMisDunningProductivePowerDailyReportList";
	}
	
	/**
	 * @Description: 催收员案件活动日报导出
	 * @param performanceDayReport
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 * @return: String
	 */
	@RequiresPermissions("dunning:sMisDunningProductivePowerDailyReport:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String dunningProductivePowerDailyReportExport(SMisDunningProductivePowerDailyReport smMisDunningProductivePowerDailyReport, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "performanceDayReport" + DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<SMisDunningProductivePowerDailyReport> page = productivePowerDailyReportService.findList(smMisDunningProductivePowerDailyReport);
    		new ExportExcel("催收员案件活动日报", SMisDunningProductivePowerDailyReport.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/dunning/sMisDunningProductivePowerDailyReport?repage";
    }

}