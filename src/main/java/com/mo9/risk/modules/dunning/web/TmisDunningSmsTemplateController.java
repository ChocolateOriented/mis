package com.mo9.risk.modules.dunning.web;


import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.drew.lang.StringUtil;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TmisDunningSmsTemplateDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TmisDunningSmsTemplate;
import com.mo9.risk.modules.dunning.service.TMisContantRecordService;
import com.mo9.risk.modules.dunning.service.TmisDunningSmsTemplateService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
/**
 * 短信模板
 * 
 * @author jwchi
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/TmisDunningSmsTemplate")

public class TmisDunningSmsTemplateController extends BaseController{

	
	@Autowired
	private TmisDunningSmsTemplateService tstService;
	
	@Autowired
	private TmisDunningSmsTemplateDao tstDao;
	@Autowired
	private TMisDunningTaskDao tdtDao;
	
	
	
	
	
	
	
	
	/**
	 * 显示所有的模板
	 * @param dunningOrder
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping("list")
	public String findPageList(TmisDunningSmsTemplate tstemplate, HttpServletRequest request, HttpServletResponse response, Model model){
		Page<TmisDunningSmsTemplate> page = tstService.findOrderPageList(new Page<TmisDunningSmsTemplate>(request, response), tstemplate);
		
		model.addAttribute("page",page);
		return "modules/dunning/tMisDunningSmsTemplate";
	}
	
	/**
	 * 
	 * 加载添加短信模板的页面
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping("addTemplate")
	public String addTemplate(){
		
		return "modules/dunning/dialog/dialogAddSmsTemplate";
	}
	
	
	
	
	/**
	 * 
	 * 添加短信模板
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping("addSmsTemplate")
	@ResponseBody
	public String addSmsTemplate( TmisDunningSmsTemplate tSmsTemplate, String sendTimeString, Model model ){
		
		if("".equals(tSmsTemplate.getNumbefore())||tSmsTemplate.getNumbefore()==null){
			tSmsTemplate.setNumbefore(-9999);
		}
		if("".equals(tSmsTemplate.getNumafter())||tSmsTemplate.getNumafter()==null){
			tSmsTemplate.setNumafter(9999);
		}
		
		tstService.save(tSmsTemplate);
		
		
		return "OK";
	}
	
	
	/**
	 * 
	 * 加载修改短信模板的页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping("changeTemplate")
	public String smsChangeTemplate( String id,Model model){
		
		TmisDunningSmsTemplate tSTemplate = tstService.get(id);
         		 
		  model.addAttribute("tSTemplate", tSTemplate);
		 
		return "modules/dunning/dialog/dialogSmsTemplate";
	}
	/**
	 * 保存修改后短信模板
	 * @param tDunningSmsTemplate
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping("saveSmsTemplate")
	@ResponseBody
	public String saveSmsTemplate( TmisDunningSmsTemplate tDunningSmsTemplate,Model model){
		
		//更新短信模板和催收历史中含短信模板信息
	       tstService.updateList(tDunningSmsTemplate);
		return "OK";
	}
	
	
	
	/**
	 * 删除短信模板
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping("deleteTemplate")
	@ResponseBody
	public String deleteTemplate(String id,Model model){
		
		TmisDunningSmsTemplate tSTemplate = tstService.get(id);
		
		if(tSTemplate==null){
			return "改模板不存在";
		}
		tstService.delete(tSTemplate);
		
		return "OK";
	}
	
	/**
	 * 添加时保证模板名唯一
	 * @param templateName
	 * @param model
	 * @return
	 */
	
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping("findName")
	@ResponseBody
	public String findName( String templateName,Model model){
		
		TmisDunningSmsTemplate template = tstDao.getByName(templateName);
		
		if(template!=null){
			model.addAttribute("tSTemplate", template);
			return "false";
		}
		return "OK";
	}
	
	/**
	 * 
	 * 通过名字获取短信模板对象
	 * @param templateName
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningTask:view")
	@RequestMapping("getTemplateByName")
	@ResponseBody
	public Map<String,Object> getTemplateByName( String templateName,String contactType,String dealcode,String dunningtaskdbid,Model model){
		Map< String, Object> tMap=new HashMap();
		TMisDunningOrder order = tdtDao.findOrderByDealcode(dealcode);
		TMisDunningTask task = tdtDao.get(dunningtaskdbid);
		TmisDunningSmsTemplate template = tstDao.getByName(templateName);
		String cousmscotent = tstService.cousmscotent(template.getSmsCotent(), order, task);
		template.setSmsCotent(cousmscotent);
		tMap.put("tSTemplate", template);
		tMap.put("contactType", contactType);
		return tMap;
	}
	
}
