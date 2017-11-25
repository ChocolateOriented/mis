package com.mo9.risk.modules.dunning.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.service.TMisAgentInfoService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 坐席配置
 * 
 * @author jwchi
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisAgentInfo")

public class TMisAgentInfoController extends BaseController{

	@Autowired
	private TMisAgentInfoService tMisAgentInfoService;
	
	
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	@ModelAttribute
	public TMisAgentInfo get(@RequestParam(required=false) String id) {
		TMisAgentInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisAgentInfoService.get(id);
		}
		if (entity == null){
			entity = new TMisAgentInfo();
		}
		return entity;
	}
	
	/**
	 * 显示所有的坐席配置
	 * @param dunningOrder
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisAgentInfo:view")
	@RequestMapping(value = {"list", ""})
	public String findPageList(TMisAgentInfo tmisAgentInfo, HttpServletRequest request, HttpServletResponse response, Model model){
		Page<TMisAgentInfo> page = tMisAgentInfoService.findPageList(new Page<TMisAgentInfo>(request, response), tmisAgentInfo);
		
		model.addAttribute("page",page);
		return "modules/dunning/TMisAgentInfoList";
	}
	
	/**
	 * 跳转坐席添加或修改页面
	 * @param tmisAgentInfo
	 * @return
	 */
	@RequiresPermissions("dunning:tMisAgentInfo:view")
	@RequestMapping(value={"form"})
	public String  agentForm(TMisAgentInfo tmisAgentInfo, Model model){
		model.addAttribute("tmisAgentInfo", tmisAgentInfo);
		return "modules/dunning/TMisAgentInfoForm";
	}
	/**
	 *添加或修改坐席
	 * @param tmisAgentInfo
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisAgentInfo:edit")
	@RequestMapping(value={"save"})
	public String changeOrAddAgent(TMisAgentInfo tmisAgentInfo, RedirectAttributes redirectAttributes){
		addMessage(redirectAttributes, StringUtils.isEmpty(tmisAgentInfo.getId())?"添加坐席成功":"修改坐席成功");
		tMisAgentInfoService.save(tmisAgentInfo);
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisAgentInfo";
	}
	/**
	 * 删除
	 * @param tmisAgentInfo
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisAgentInfo:edit")
	@RequestMapping(value={"delete"})
	public String  deleteAgent(TMisAgentInfo tmisAgentInfo, RedirectAttributes redirectAttributes){
		tMisAgentInfoService.delete(tmisAgentInfo);
		addMessage(redirectAttributes, "删除坐席成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisAgentInfo";
	}
	/**
	 * 跳转绑定页面
	 * @param tmisAgentInfo
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisAgentInfo:edit")
	@RequestMapping(value={"bindPage"})
	public String  bindPage(String id,Model model){
		List<TMisDunningPeople> users = tMisDunningPeopleService.findAgentPeopleList();
		model.addAttribute("users",users);
		model.addAttribute("id",id);
		return "modules/dunning/dialog/dialogBindAgent";
	}
	/**
	 *绑定坐席
	 * @param tmisAgentInfo
	 * @param redirectAttributes
	 * @return	
	 */
	@RequiresPermissions("dunning:tMisAgentInfo:edit")
	@RequestMapping(value={"bindAgent"})
	@ResponseBody
	public boolean  bindAgent(TMisAgentInfo tmisAgentInfo, RedirectAttributes redirectAttributes){
		tMisAgentInfoService.save(tmisAgentInfo);
		return true;
	}
	/**
	 *取消绑定坐席
	 * @param tmisAgentInfo
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisAgentInfo:edit")
	@RequestMapping(value={"unbind"})
	public String  unbind(TMisAgentInfo tmisAgentInfo, RedirectAttributes redirectAttributes){
		tmisAgentInfo.setPeopleId(null);
		tMisAgentInfoService.save(tmisAgentInfo);
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisAgentInfo";
	}
	/**
	 * 添加或修改时校验
	 * @param tmisAgentInfo
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value={"validateAgent"})
	@ResponseBody
	public Boolean  validateAgent(TMisAgentInfo tmisAgentInfo, RedirectAttributes redirectAttributes){
		return tMisAgentInfoService.validateAgent(tmisAgentInfo);
	}
	@RequestMapping(value={"validateQueue"})
	@ResponseBody
	public Boolean  validateQueue(TMisAgentInfo tmisAgentInfo, RedirectAttributes redirectAttributes){
		return tMisAgentInfoService.validateQueue(tmisAgentInfo);
	}
	@RequestMapping(value={"validateExtension"})
	@ResponseBody
	public Boolean  validateExtension(TMisAgentInfo tmisAgentInfo, RedirectAttributes redirectAttributes){
		return tMisAgentInfoService.validateExtension(tmisAgentInfo);
	}
	@RequestMapping(value={"validateDirect"})
	@ResponseBody
	public Boolean  validateDirect(TMisAgentInfo tmisAgentInfo, RedirectAttributes redirectAttributes){
		return tMisAgentInfoService.validateDirect(tmisAgentInfo);
	}
}
