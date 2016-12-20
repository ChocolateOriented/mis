/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderAfterLoan;
import com.thinkgem.jeesite.modules.weeklyreport.service.VRiskOrderAfterLoanService;

/**
 * 贷后还款情况Controller
 * @author 徐盛
 * @version 2016-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/repaymentcondition/vRiskOrderAfterLoan")
public class VRiskOrderAfterLoanController extends BaseController {

	@Autowired
	private VRiskOrderAfterLoanService vRiskOrderAfterLoanService;
	
	@ModelAttribute
	public VRiskOrderAfterLoan get(@RequestParam(required=false) String id) {
		VRiskOrderAfterLoan entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = vRiskOrderAfterLoanService.get(id);
		}
		if (entity == null){
			entity = new VRiskOrderAfterLoan();
		}
		return entity;
	}
	
	@RequiresPermissions("repaymentcondition:vRiskOrderAfterLoan:view")
	@RequestMapping(value = {"list", ""})
	public String list(VRiskOrderAfterLoan vRiskOrderAfterLoan, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VRiskOrderAfterLoan> page = vRiskOrderAfterLoanService.findPage(new Page<VRiskOrderAfterLoan>(request, response), vRiskOrderAfterLoan); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/vRiskOrderAfterLoanList";
	}

	@RequiresPermissions("repaymentcondition:vRiskOrderAfterLoan:view")
	@RequestMapping(value = "form")
	public String form(VRiskOrderAfterLoan vRiskOrderAfterLoan, Model model) {
		model.addAttribute("vRiskOrderAfterLoan", vRiskOrderAfterLoan);
		return "modules/weeklyreport/vRiskOrderAfterLoanForm";
	}

	@RequiresPermissions("repaymentcondition:vRiskOrderAfterLoan:edit")
	@RequestMapping(value = "save")
	public String save(VRiskOrderAfterLoan vRiskOrderAfterLoan, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, vRiskOrderAfterLoan)){
			return form(vRiskOrderAfterLoan, model);
		}
		vRiskOrderAfterLoanService.save(vRiskOrderAfterLoan);
		addMessage(redirectAttributes, "保存贷后还款情况成功");
		return "redirect:"+Global.getAdminPath()+"/repaymentcondition/vRiskOrderAfterLoan/?repage";
	}
	
	@RequiresPermissions("repaymentcondition:vRiskOrderAfterLoan:edit")
	@RequestMapping(value = "delete")
	public String delete(VRiskOrderAfterLoan vRiskOrderAfterLoan, RedirectAttributes redirectAttributes) {
		vRiskOrderAfterLoanService.delete(vRiskOrderAfterLoan);
		addMessage(redirectAttributes, "删除贷后还款情况成功");
		return "redirect:"+Global.getAdminPath()+"/repaymentcondition/vRiskOrderAfterLoan/?repage";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("repaymentcondition:vRiskOrderAfterLoan:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(VRiskOrderAfterLoan newtest, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "RiskOrderAfterLoan"+DateUtils.getDate("yyyy-MM-dd HHmmss")+".xlsx";
            List<VRiskOrderAfterLoan> page = vRiskOrderAfterLoanService.findList(newtest);
    		new ExportExcel("贷后还款情况报表", VRiskOrderAfterLoan.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/repaymentcondition/vRiskOrderAfterLoan/list?repage";
    }

}