/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisSendMsgInfo;
import com.mo9.risk.modules.dunning.entity.TelNumberBean;
import com.mo9.risk.modules.dunning.service.TMisContantRecordService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerPersonalInfoService;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;


/**
 * 催收任务联系记录Controller
 * @author ycheng
 * @version 2016-07-15
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisContantRecord")
public class TMisContantRecordController extends BaseController {

	private static Logger logger = Logger.getLogger(TMisContantRecordController.class);
	
	@Autowired
	private TMisContantRecordService tMisContantRecordService;
	
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	
	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;
	
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoDao;
	
	@ModelAttribute
	public TMisContantRecord get(@RequestParam(required=false) String id) {
		TMisContantRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisContantRecordService.get(id);
		}
		if (entity == null){
			entity = new TMisContantRecord();
		}
		return entity;
	}
	
	/**
	 * 根据联系对象返回电话号码集合
	 * @return
	 */
	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = "getTelInfos")
	@ResponseBody
	public List<TMisSendMsgInfo>  getTelInfos(String buyerId,String type){
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			List<TMisSendMsgInfo> list = tMisContantRecordService.getTelInfos(buyerId,type.toLowerCase());
			return list;
		} catch (Exception e) {
			logger.info(e);
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");  
		}
		return new ArrayList<TMisSendMsgInfo>();
	}
	
	/**
	 * 根据联系对象返回电话号码集合
	 * @return
	 */
	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = "dialogTelInfos")
	public  String dialogTelInfos(String buyerId,String type,String dialogType, Model model,String dealcode){
		List<TMisSendMsgInfo> list = null;
//		List<Map<String,Integer>>  map = tMisContantRecordService.findSmsNum(dealcode);
		List<TelNumberBean> telNumberBeans = null;
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			list = tMisContantRecordService.getTelInfos(buyerId,type.toLowerCase());
			telNumberBeans = tMisContantRecordService.findSmsNum(dealcode);
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		Map<String,Integer> map = new HashMap<String, Integer>();
		if (telNumberBeans != null) {
			for(TelNumberBean telNumberBean : telNumberBeans){
				String[] str = telNumberBean.getTel().split(",");
				if(str.length > 1){
					for(String tel : str){
						map.put(tel, telNumberBean.getCount());
					}
				}else{
					map.put(telNumberBean.getTel(), telNumberBean.getCount());
				}
			}
		}
		model.addAttribute("map", map);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("tMisSendMsgInfos", list);
		model.addAttribute("dialogType", dialogType);
		return "modules/dunning/dialog/dialogTelInfos";
	}
	
	
	/**
	 * 催收历史list
	 * @param tMisContantRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisContantRecord tMisContantRecord, HttpServletRequest request, HttpServletResponse response,String mobileSelf, Model model) {
		
		String dealcode = request.getParameter("dealcode");
		String buyerId = request.getParameter("buyerId");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		String dunningCycle = request.getParameter("dunningCycle");
		String overdueDays = request.getParameter("overdueDays");
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		
		tMisContantRecord.setBuyerid(Integer.parseInt(buyerId));
		Page<TMisContantRecord> page = null;
		/*TMisDunningTask task = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);*/
		TMisDunningOrder order = null;
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			page = tMisContantRecordService.findPage(new Page<TMisContantRecord>(request, response, 50), tMisContantRecord);
			//task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
			order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return "views/error/500";
			}
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		if (page != null) {
			List<TMisContantRecord> t = page.getList();
			for(TMisContantRecord record :t){
				//操作类型汉字话
				if(record.getContanttype() != null){
					record.setContanttypestr(record.getContanttype().getDesc());
				}
				//电话应答状态汉字话
//				if(record.getTelstatus()  != null){
//					record.setTelstatusstr(record.getTelstatus().getDesc());
//				}
				
				//联系人类型汉字话
				if(record.getContactstype()  != null){
					record.setContactstypestr(record.getContactstype().getDesc());
				}
				
				//短信类型汉字话
				if(record.getSmstemp() != null){
					record.setSmstempstr(record.getSmstemp().getDesc());
				}
				//取催收人姓名
//				if(record.getDunningpeoplename() != null){
//					if("sys".equals(record.getDunningpeoplename())){
//						record.setDunningpeoplename("定时发送");
//					}else{
//						TMisDunningPeople people = tMisDunningPeopleService.get(record.getDunningpeoplename());
//						record.setDunningpeoplename(null != people ? people.getName():"");
//					}
//				}
			}
		}
//		TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
//		model.addAttribute("personalInfo", personalInfo);
		model.addAttribute("page", page);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		
		boolean ispayoff = false;
		if (order != null && "payoff".equals(order.getStatus())) {
			ispayoff = true;
		}
		model.addAttribute("ispayoff", ispayoff);
		model.addAttribute("dunningCycle", dunningCycle);
		model.addAttribute("overdueDays", overdueDays);
		model.addAttribute("mobileSelf", mobileSelf);
		return "modules/dunning/tMisDunningTaskHistoryList";
	}

	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = "form")
	public String form(TMisContantRecord tMisContantRecord, Model model) {
		model.addAttribute("tMisContantRecord", tMisContantRecord);
		return "modules/dunning/tMisContantRecordForm";
	}

	@RequiresPermissions("dunning:tMisContantRecord:edit")
	@RequestMapping(value = "save")
	public String save(TMisContantRecord tMisContantRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisContantRecord)){
			return form(tMisContantRecord, model);
		}
		tMisContantRecordService.save(tMisContantRecord);
		addMessage(redirectAttributes, "保存保存记录成功成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisContantRecord/?repage";
	}
	
	
	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = "saveRecord")
	@ResponseBody
	public String saveRecord(TMisContantRecord tMisContantRecord, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		if (!beanValidator(model, tMisContantRecord)){
			return form(tMisContantRecord, model);
		}
		String dealcode = request.getParameter("dealcode");
		String dunningtaskdbid = request.getParameter("dunningtaskdbid");
		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		
		if (order == null) {
			logger.warn("订单不存在，订单号：" + dealcode);
			return null;
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		
		if (task == null) {
			logger.warn("任务不存在，订单号：" + dealcode);
			return null;
		}
		 int overdayas =  TMisDunningTaskService.GetOverdueDay(order.getRepaymentDate());
		if (tMisContantRecord.getContanttype() == TMisContantRecord.ContantType.sms) {
			TMisContantRecord tContantRecord=new TMisContantRecord();
			tContantRecord.setDealcode(dealcode);
			tContantRecord.setTemplateName( tMisContantRecord.getTemplateName());
			if(overdayas>1){
				tContantRecord.setContanttarget(tMisContantRecord.getContanttarget());
			}
			int countSmsSend=tMisContantRecordService.findCountSmsSend(tContantRecord);
			if(countSmsSend>=3){
				return "sendOut";
			}
			
		}
		tMisContantRecordService.saveRecord(task,order, tMisContantRecord,dunningtaskdbid);
		addMessage(redirectAttributes, "保存保存记录成功成功");
		return "OK";
	}
	
	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = "smsGetTemp")
	@ResponseBody
	public String smsGetTemp(TMisContantRecord tMisContantRecord, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		if (!beanValidator(model, tMisContantRecord)){
			return form(tMisContantRecord, model);
		}
		String dealcode = request.getParameter("dealcode");
		
		String smsTemp = "";
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return null;
			}
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("STATUS_DUNNING", "dunning");
			params.put("DEALCODE", dealcode);
			TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
			
			if (task == null) {
				logger.warn("任务不存在，订单号：" + dealcode);
				return null;
			}
			smsTemp = tMisContantRecordService.smsGetTemp(task,order, tMisContantRecord);
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return null;
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		return smsTemp;
	}
	
	
	@RequiresPermissions("dunning:tMisContantRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisContantRecord tMisContantRecord, RedirectAttributes redirectAttributes) {
		tMisContantRecordService.delete(tMisContantRecord);
		addMessage(redirectAttributes, "删除保存记录成功成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisContantRecord/?repage";
	}
	
//	public static void main(String[] args) {
//		/*Res<String> vo = Mo9RestTemplate.doGet("/limit/order/v1.0/payForStaff", new HashMap<String,Object>(), String.class);
//		String orderStr = vo.getObjects();*/
//		//String url = "https://riskclone.mo9.com/riskportal/limit/order/v1.0/payForStaff/" +"2600000"+ "/" +"loan"+ "/" +"0"+ "/" +"1100"+ "/" +"weixinpay";
//		String url = "http://ycheng.local.mo9.com/riskportal/limit/order/v1.0/payForStaff/198961/loan/0/0.01/alipay";
//		String payUrl = "";
//		try {
//			String res = GetRequest.getRequest("https://riskclone.mo9.com/riskportal/limit/order/v1.0/payForStaff/198961/loan/0/0.01/alipay", new HashMap<String,String>());
//			System.out.println(res);
//			if(StringUtils.isNotBlank(res)){
//				JSONObject repJson = new JSONObject(res);
//				String resultCode =  repJson.has("resultCode") ? String.valueOf(repJson.get("resultCode")) : "";
//				if(StringUtils.isNotBlank(resultCode) && "200".equals(resultCode)){
//					payUrl = repJson.has("datas") ? String.valueOf(repJson.get("datas")) : "";
//					System.out.println(payUrl);
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	
 
	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = "dialogSmsDetail")
	public  String dialogSmsDetail( TMisContantRecord tMisContantRecord,Model model){
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			List<TMisContantRecord>  list = tMisContantRecordService.findDetailByDealcodeandTel(tMisContantRecord);
			model.addAttribute("tmiscontantrecord", list);
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		return "modules/dunning/dialog/dialogSmsDetail";
	}
	
 
	@RequiresPermissions("dunning:tMisContantRecord:view")
	@RequestMapping(value = "dialogTelDetail")
	public  String dialogTelDetail( TMisContantRecord tMisContantRecord,Model model){
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			List<TMisContantRecord>  list = tMisContantRecordService.findDetailByDealcodeandTel(tMisContantRecord);
			model.addAttribute("tmiscontantrecord", list);
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		return "modules/dunning/dialog/dialogTelDetail";
	}
	
	
}