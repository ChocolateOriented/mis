/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.thinkgem.jeesite.modules.weeklyreport.entity.VRiskOrderAfterLoanPushing;
import com.thinkgem.jeesite.modules.weeklyreport.service.VRiskOrderAfterLoanPushingService;

/**
 * 贷后催款情况报表Controller
 * @author 徐盛
 * @version 2016-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/afterloanpushing/vRiskOrderAfterLoanPushing")
public class VRiskOrderAfterLoanPushingController extends BaseController {

	@Autowired
	private VRiskOrderAfterLoanPushingService vRiskOrderAfterLoanPushingService;
	
	@ModelAttribute
	public VRiskOrderAfterLoanPushing get(@RequestParam(required=false) String id) {
		VRiskOrderAfterLoanPushing entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = vRiskOrderAfterLoanPushingService.get(id);
		}
		if (entity == null){
			entity = new VRiskOrderAfterLoanPushing();
		}
		return entity;
	}
	
	@RequiresPermissions("afterloanpushing:vRiskOrderAfterLoanPushing:view")
	@RequestMapping(value = {"list", ""})
	public String list(VRiskOrderAfterLoanPushing vRiskOrderAfterLoanPushing, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<VRiskOrderAfterLoanPushing> page = vRiskOrderAfterLoanPushingService.findPage(new Page<VRiskOrderAfterLoanPushing>(request, response), vRiskOrderAfterLoanPushing); 
		model.addAttribute("page", page);
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("namet", "存储过程测试配置成功");
//		map.put("drCount", "123");
//		List<String> list = vRiskOrderAfterLoanPushingService.findPushingTotal(map);
		return "modules/weeklyreport/vRiskOrderAfterLoanPushingList";
	}

	@RequiresPermissions("afterloanpushing:vRiskOrderAfterLoanPushing:view")
	@RequestMapping(value = "form")
	public String form(VRiskOrderAfterLoanPushing vRiskOrderAfterLoanPushing, Model model) {
		model.addAttribute("vRiskOrderAfterLoanPushing", vRiskOrderAfterLoanPushing);
		return "modules/weeklyreport/vRiskOrderAfterLoanPushingForm";
	}

	@RequiresPermissions("afterloanpushing:vRiskOrderAfterLoanPushing:edit")
	@RequestMapping(value = "save")
	public String save(VRiskOrderAfterLoanPushing vRiskOrderAfterLoanPushing, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, vRiskOrderAfterLoanPushing)){
			return form(vRiskOrderAfterLoanPushing, model);
		}
		vRiskOrderAfterLoanPushingService.save(vRiskOrderAfterLoanPushing);
		addMessage(redirectAttributes, "保存贷后催款情况报表成功");
		return "redirect:"+Global.getAdminPath()+"/afterloanpushing/vRiskOrderAfterLoanPushing/?repage";
	}
	
	@RequiresPermissions("afterloanpushing:vRiskOrderAfterLoanPushing:edit")
	@RequestMapping(value = "delete")
	public String delete(VRiskOrderAfterLoanPushing vRiskOrderAfterLoanPushing, RedirectAttributes redirectAttributes) {
		vRiskOrderAfterLoanPushingService.delete(vRiskOrderAfterLoanPushing);
		addMessage(redirectAttributes, "删除贷后催款情况报表成功");
		return "redirect:"+Global.getAdminPath()+"/afterloanpushing/vRiskOrderAfterLoanPushing/?repage";
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("afterloanpushing:vRiskOrderAfterLoanPushing:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(VRiskOrderAfterLoanPushing newtest, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "RiskOrderAfterLoanPushing"+DateUtils.getDate("yyyy-MM-ddHHmmss")+".xlsx";
            List<VRiskOrderAfterLoanPushing> page = vRiskOrderAfterLoanPushingService.findList( newtest);
    		new ExportExcel("贷后催款情况报表", VRiskOrderAfterLoanPushing.class).setDataList(page).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/afterloanpushing/vRiskOrderAfterLoanPushing/list?repage";
    }

}