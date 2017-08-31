/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.entity.Demotable;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.service.DemotableService;

/**
 * 分案demoController
 * @author 
 * @version 2017-08-28
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/demotable")
public class DemotableController extends BaseController {

	@Autowired
	private DemotableService demotableService;
	
	@ModelAttribute
	public Demotable get(@RequestParam(required=false) String id) {
		Demotable entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = demotableService.get(id);
		}
		if (entity == null){
			entity = new Demotable();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:demotable:view")
	@RequestMapping(value = {"list", ""})
	public String list(Demotable demotable, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Demotable> page = demotableService.findPage(new Page<Demotable>(request, response), demotable); 
		model.addAttribute("page", page);
		return "modules/dunning/demotableList";
	}
	
	
	@Autowired
	private TMisDunningPeopleDao tMisDunningPeopleDao;
	
	
	
	
	/**
	 * 获取计算后的迁徙率数据
	 */
	@RequiresPermissions("dunning:demotable:view")
	@RequestMapping(value = "demo")
	@ResponseBody
	public void demo(Date day, String dealcodenum500, String dealcodenum1000,
			String dealcodenum1500, String dealcodenum2000,String cycle,String dealcodetype) {
//		day = new Date();
//		dealcodenum500 = "10";
//		dealcodenum1000 = "10";
//		dealcodenum1500 = "10";
//		dealcodenum2000 = "10";
//		cycle = "Q0";
//		dealcodetype = "dealcodenum";
		try {
			List<Demotable> demotables = new ArrayList<Demotable>();
			
//			List<TMisDunningPeople> dunningPeoples2 = new ArrayList<TMisDunningPeople>();
//			int dunningPeoples = Integer.parseInt(dunningPeoplesize);
//			for (int i = 0; i < dunningPeoples ; i++) {
//				TMisDunningPeople dunningPeople = new TMisDunningPeople();
//				dunningPeople.setName("催收员" + i);
//				dunningPeoples2.add(dunningPeople);
//			}
			List<Demotable> dunningPeoples = demotableService.findPeopleByDemo(dealcodetype,cycle);
			
			ArrayList<Integer> tasks = new ArrayList<Integer>();
			for (int i = 0; i < Integer.parseInt(dealcodenum2000); i++) {
				Integer mon = 2000;
				tasks.add(mon);
			}
			for (int i = 0; i < Integer.parseInt(dealcodenum1500); i++) {
				Integer mon = 1500;
				tasks.add(mon);
			}
			for (int i = 0; i < Integer.parseInt(dealcodenum1000); i++) {
				Integer mon = 1000;
				tasks.add(mon);
			}
			for (int i = 0; i < Integer.parseInt(dealcodenum500); i++) {
				Integer mon = 500;
				tasks.add(mon);
			}
			int j = 0;
			for (int i = 0; i < tasks.size(); i++) {
				if (i / dunningPeoples.size() % 2 == 0) {
					j = i % dunningPeoples.size();
				} else {
					j = dunningPeoples.size() - 1 - i % dunningPeoples.size();
				}
				System.out.println(j);
				Integer dealcodeamount = tasks.get(i);
				Demotable demotable = new Demotable();
				demotable.setId(dunningPeoples.get(j).getId());
				demotable.setName(dunningPeoples.get(j).getName());
				demotable.setDealcodenum(1);
				demotable.setDealcodeamount(BigDecimal.valueOf(dealcodeamount));
				demotable.setDatetime(day);
				demotables.add(demotable);
//				dunningTask.setDunningpeoplename(dunningPeoples.get(j).getName());
			}
			demotableService.batchinsertDemotable(demotables);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// DynamicDataSource.setCurrentLookupKey("dataSource");
		}
	}

	@RequiresPermissions("dunning:demotable:view")
	@RequestMapping(value = "form")
	public String form(Demotable demotable, Model model) {
		model.addAttribute("demotable", demotable);
		return "modules/dunning/demotableForm";
	}

	@RequiresPermissions("dunning:demotable:edit")
	@RequestMapping(value = "save")
	public String save(Demotable demotable, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, demotable)){
			return form(demotable, model);
		}
		demotableService.save(demotable);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/demotable/?repage";
	}
	
	@RequiresPermissions("dunning:demotable:edit")
	@RequestMapping(value = "delete")
	public String delete(Demotable demotable, RedirectAttributes redirectAttributes) {
		demotableService.delete(demotable);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/demotable/?repage";
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> tasks = new ArrayList<Integer>();
		for(int i= 0 ; i < 1870 ; i++ ){
			Integer mon = 2000;
			tasks.add(mon);
		}
		for(int i= 0 ; i < 1590 ; i++ ){
			Integer mon = 1500;
			tasks.add(mon);
		}
		for(int i= 0 ; i < 962 ; i++ ){
			Integer mon = 1000;
			tasks.add(mon);
		}
		for(int i= 0 ; i < 568 ; i++ ){
			Integer mon = 500;
			tasks.add(mon);
		}
		
		int dunningPeoples = 30;
		int j = 0;
	    for (int i = 0; i < tasks.size(); i++) {
	      if (i / dunningPeoples % 2 == 0) {
	        j = i % dunningPeoples;
	      } else {
	        j = dunningPeoples - 1 - i % dunningPeoples;
	      }
	      System.out.println(j);
	    }
	}
	

}