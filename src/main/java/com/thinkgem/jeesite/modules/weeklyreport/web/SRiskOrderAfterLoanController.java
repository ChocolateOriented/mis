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
import com.thinkgem.jeesite.modules.weeklyreport.entity.SRiskOrderAfterLoan;
import com.thinkgem.jeesite.modules.weeklyreport.service.SRiskOrderAfterLoanService;

/**
 * 贷后风险Controller
 * @author 徐盛
 * @version 2016-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/afterloan/sRiskOrderAfterLoan")
public class SRiskOrderAfterLoanController extends BaseController {

	@Autowired
	private SRiskOrderAfterLoanService sRiskOrderAfterLoanService;
	
	@ModelAttribute
	public SRiskOrderAfterLoan get(@RequestParam(required=false) String id) {
		SRiskOrderAfterLoan entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sRiskOrderAfterLoanService.get(id);
		}
		if (entity == null){
			entity = new SRiskOrderAfterLoan();
		}
		return entity;
	}
	
	@RequiresPermissions("afterloan:sRiskOrderAfterLoan:view")
	@RequestMapping(value = {"list", ""})
	public String list(SRiskOrderAfterLoan sRiskOrderAfterLoan, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SRiskOrderAfterLoan> page = sRiskOrderAfterLoanService.findPage(new Page<SRiskOrderAfterLoan>(request, response), sRiskOrderAfterLoan); 
		model.addAttribute("page", page);
		return "modules/weeklyreport/sRiskOrderAfterLoanList";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("afterloan:sRiskOrderAfterLoan:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SRiskOrderAfterLoan sRiskOrderAfterLoan, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "RiskOrderAfterLoan"+DateUtils.getDate("yyyy-MM-ddHHmmss")+".xlsx";
            List<SRiskOrderAfterLoan> page = sRiskOrderAfterLoanService.findList(sRiskOrderAfterLoan);
    		new ExportExcel("贷后风险情况报表", SRiskOrderAfterLoan.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/afterloan/sRiskOrderAfterLoan/list?repage";
    }
	
	

	@RequiresPermissions("afterloan:sRiskOrderAfterLoan:view")
	@RequestMapping(value = "form")
	public String form(SRiskOrderAfterLoan sRiskOrderAfterLoan, Model model) {
		model.addAttribute("sRiskOrderAfterLoan", sRiskOrderAfterLoan);
		return "modules/weeklyreport/sRiskOrderAfterLoanForm";
	}

	@RequiresPermissions("afterloan:sRiskOrderAfterLoan:edit")
	@RequestMapping(value = "save")
	public String save(SRiskOrderAfterLoan sRiskOrderAfterLoan, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sRiskOrderAfterLoan)){
			return form(sRiskOrderAfterLoan, model);
		}
		sRiskOrderAfterLoanService.save(sRiskOrderAfterLoan);
		addMessage(redirectAttributes, "保存贷后风险成功");
		return "redirect:"+Global.getAdminPath()+"/afterloan/sRiskOrderAfterLoan/?repage";
	}
	
	@RequiresPermissions("afterloan:sRiskOrderAfterLoan:edit")
	@RequestMapping(value = "delete")
	public String delete(SRiskOrderAfterLoan sRiskOrderAfterLoan, RedirectAttributes redirectAttributes) {
		sRiskOrderAfterLoanService.delete(sRiskOrderAfterLoan);
		addMessage(redirectAttributes, "删除贷后风险成功");
		return "redirect:"+Global.getAdminPath()+"/afterloan/sRiskOrderAfterLoan/?repage";
	}

}