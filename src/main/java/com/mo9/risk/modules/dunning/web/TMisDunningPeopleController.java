/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.enums.DebtBizType;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.util.CsvUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 催收人员Controller
 * @author 徐盛
 * @version 2016-07-12
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningPeople")
public class TMisDunningPeopleController extends BaseController {

	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	@Autowired
	private TMisDunningGroupService tMisDunningGroupService;
	
	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	@ModelAttribute
	public TMisDunningPeople get(@RequestParam(required=false) String id) {
		TMisDunningPeople entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisDunningPeopleService.get(id);
		}
		if (entity == null){
			entity = new TMisDunningPeople();
		}
		return entity;
	}
	
	/**
	 * @Description: 催收小组集合
	 */
	@ModelAttribute
	public void groupList(Model model) {
		//催收小组列表
		model.addAttribute("groupList", tMisDunningGroupService.findList(new TMisDunningGroup()));
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
	}
	
	/**
	 * 催收人员列表
	 * @param tMisDunningPeople
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisDunningPeople tMisDunningPeople, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisDunningPeople> page = tMisDunningPeopleService.findPage(new Page<TMisDunningPeople>(request, response), tMisDunningPeople); 
		model.addAttribute("page", page);
		model.addAttribute("bizTypes", DebtBizType.values());
		return "modules/dunning/tMisDunningPeopleList";
	}
	
	/**
	 * 催收人员选择列表	
	 */
	@RequestMapping(value="optionList",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<TMisDunningPeople> optionList(TMisDunningPeople tMisDunningPeople) {
		return tMisDunningPeopleService.findOptionList(tMisDunningPeople);
	}
	
	/**
	 * 加载编辑催收人员页面
	 * @param tMisDunningPeople
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:view")
	@RequestMapping(value = "form")
	public String form(TMisDunningPeople tMisDunningPeople, Model model) {
		List<User> users = tMisDunningPeopleService.findUserList();
		model.addAttribute("users",users);
		model.addAttribute("tMisDunningPeople", tMisDunningPeople);
		model.addAttribute("bizTypes", DebtBizType.values());
		return "modules/dunning/dialog/tMisDunningPeopleForm";
	}

	/**
	 * 保存或编辑催收人员
	 * @param tMisDunningPeople
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(TMisDunningPeople tMisDunningPeople, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisDunningPeople)){
			return form(tMisDunningPeople, model);
		}

		tMisDunningPeopleService.save(tMisDunningPeople);
		return "OK";
	}
	
	/**
	 * 删除催收人员
	 * @param tMisDunningPeople
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisDunningPeople tMisDunningPeople, RedirectAttributes redirectAttributes) {
		if(null != tMisDunningPeople.getId()){
			int count = tMisDunningTaskService.findDunningCount(tMisDunningPeople.getId());
			if(count > 0){
				addMessage(redirectAttributes, "必须先清空此催收人员的任务!");
				return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningPeople/?repage";
			}
		}
		tMisDunningPeopleService.delete(tMisDunningPeople);
		addMessage(redirectAttributes, "删除催收人员成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningPeople/?repage";
	}
	
	/**
	 * 加载手动分案页面
	 * @param peopleids
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "dialogDunningcycle")
	public String dialogDunningcycle( Model model,String peopleids) {
		try {
			model.addAttribute("peopleids", peopleids);
		} catch (Exception e) {
			logger.info("加载手动分配页面失败",e);
			return "views/error/500";
		}
		return "modules/dunning/dialog/dialogDunningcycle";
	}
	
	
	/**
	 * 手动分案
	 * @param peopleids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "distributioncycleSave")
	@ResponseBody
	public String distributioncycleSave(String[] peopleids,String dunningcycle, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		try {
			List<String> ids = Arrays.asList(peopleids); 
			if(ids.isEmpty()){
				String mes = "请选择催收队列";
				return mes;
			}
			tMisDunningPeopleService.batchUpdateDunningcycle(ids, UserUtils.getUser().getId(), dunningcycle);
		} catch (Exception e) {
			logger.info("手动分配发生错误",e);
		}
		return "OK";
	}
	
	/**
	 * @Description: 检测花名是否唯一
	 * @param nickname
	 * @param id
	 * @return
	 * @return: Boolean
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:view")
	@RequestMapping(value = "isUniqueNickname")
	@ResponseBody
	public Boolean isUniqueNickname(String nickname,String id){
		return tMisDunningPeopleService.checkNicknameUnique(nickname,id);
	}
	
	/**
	 * 
	 * 验证座机号是否正确
	 * 
	 * @param extensionNumber
	 * 
	 * @return
	 * 
	 */

	@RequiresPermissions("dunning:tMisDunningPeople:view")
	@RequestMapping(value = "extensionNumberYanZheng")
	@ResponseBody
	public Boolean valideNumber(String extensionNumber) {
		if (StringUtils.isEmpty(extensionNumber)) {
			return false;
		}
		boolean yanZhengNumber = tMisDunningTaskService.yanZhengNumber(extensionNumber, 2);
		if (!yanZhengNumber) {
			return false;
		}
		return true;

	}
	
	/**
	 * 加载分配小组和自动分配等页面
	 * @param peopleids
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "dialogOperationPeoPle")
	public String dialogOperationPeoPle( Model model,String peopleids,String operateId) {
		try {
			model.addAttribute("peopleids", peopleids);
			model.addAttribute("operateId", operateId);
		} catch (Exception e) {
			logger.info("加载分配小组和自动分配等页面失败",e);
			return "views/error/500";
		}
		return "modules/dunning/dialog/dialogOperationPeoPle";
	}

	/**
	 * 加载分配产品页面
	 * @param peopleids
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "dialogPeopleBizTypes")
	public String dialogPeopleBizTypes( Model model,String peopleids) {
		model.addAttribute("peopleids", peopleids);
		model.addAttribute("bizTypes",DebtBizType.values());
		return "modules/dunning/dialog/dialogPeoPleBizTypes";
	}

	/**
	 * 批量分配小组和自动分配等
	 * @param peopleids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "operationSave")
	@ResponseBody
	public String operationSave(TMisDunningPeople tMisDunningPeople,String[] peopleids, HttpServletRequest request) {
		
		try {
			List<String> ids = Arrays.asList(peopleids); 
			if(ids.isEmpty()){
				String mes = "请选择催收员";
				return mes;
			}
			tMisDunningPeopleService.operationUpdate(ids, UserUtils.getUser().getId(),tMisDunningPeople);
		} catch (Exception e) {
			logger.info("手动批量分配发生错误",e);
		}
		return "OK";
	}

	/**
	 * 批量分配产品
	 * @param peopleids
	 * @param bizTypes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "batchUpdatepeopleBizTypes")
	@ResponseBody
	public String batchUpdatepeopleBizTypes(DebtBizType[] bizTypes, String[] peopleids) {
		List<String> peopleidList = Arrays.asList(peopleids);
		if (null == peopleidList || peopleidList.isEmpty()) {
			String mes = "请选择催收员";
			return mes;
		}
		tMisDunningPeopleService.batchUpdatepeopleBizTypes(peopleidList, Arrays.asList(bizTypes));
		return "OK";
	}
	/**
	 * 批量添加催收员
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "fileUpload")
	public String fileUpload( Model model,MultipartFile file, RedirectAttributes redirectAttributes) {
		//解析上传Excel或csv
		List<TMisDunningPeople> list ;
		String filename = file.getOriginalFilename();
		logger.info("正在接析文件:" +filename) ;
		try {
			if (StringUtils.endsWith(filename,".csv")){
				list = CsvUtil.importCsv(file,TMisDunningPeople.class,3);
			}else {
				ImportExcel ei = new ImportExcel(file, 1, 0);
				list = ei.getDataList(TMisDunningPeople.class);
			}
			logger.info("完成接析文件:" + file.getOriginalFilename());
		} catch (Exception e) {
			logger.info("解析式发生错误",e);
			addMessage(redirectAttributes, "解析文件:" + file.getOriginalFilename() + ",发生失败");
			return "redirect:form";
		}
		if(list==null||list.size()<1){
			addMessage(redirectAttributes, "解析文件:" + file.getOriginalFilename() + ",发生失败内容为空");
			return "redirect:form";
		}
		StringBuilder message=new StringBuilder();
		boolean validsInsert;
		try {
			validsInsert = tMisDunningPeopleService.batchInsert(list,message);
		} catch (Exception e) {
			validsInsert=false;
		}
		if(!validsInsert){
			addMessage(redirectAttributes,  "解析文件:" + file.getOriginalFilename() + ",发生失败."+message.toString());
			return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningPeople/?repage";
		}
		logger.info("导入成功,文件:" + file.getOriginalFilename());
		addMessage(redirectAttributes,  "导入成功.");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisDunningPeople/?repage";
	}
}