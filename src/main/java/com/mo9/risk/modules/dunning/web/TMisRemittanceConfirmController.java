/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskLogDao;
import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisPaid;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm.ConfirmFlow;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceConfirm.RemittanceTag;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.service.TMisDunningOrderService;
import com.mo9.risk.modules.dunning.service.TMisRemittanceConfirmService;
import com.mo9.risk.modules.dunning.service.TRiskBuyerPersonalInfoService;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 汇款确认信息Controller
 * @author 徐盛
 * @version 2016-09-12
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisRemittanceConfirm")
public class TMisRemittanceConfirmController extends BaseController {

	@Autowired
	private TMisRemittanceConfirmService tMisRemittanceConfirmService;
	@Autowired
	private TMisDunningOrderService tMisDunningOrderService;



	@ModelAttribute
	public TMisRemittanceConfirm get(@RequestParam(required=false) String id) {
		TMisRemittanceConfirm entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisRemittanceConfirmService.get(id);
		}
		if (entity == null){
			entity = new TMisRemittanceConfirm();
		}
		return entity;
	}

	/**
	 * 加载催收新增汇款信息页面
	 * @param buyerId
	 * @param dealcode
	 * @param dunningtaskdbid
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:insertForm")
	@RequestMapping(value = "insertRemittanceConfirmForm")
	public String insertRemittanceConfirmForm(String buyerId,String dealcode,String dunningtaskdbid,boolean hasContact,String mobileSelf, Model model) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("hasContact", hasContact);
		model.addAttribute("mobileSelf", mobileSelf);
		return "modules/dunning/insertRemittanceConfirmForm";
	}

	/**
	 * 保存催收新增汇款信息
	 * @param buyerId
	 * @param dealcode
	 * @param dunningtaskdbid
	 * @param tMisRemittanceConfirm
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:insertForm")
	@RequestMapping(value = "insertRemittanceConfirm")
	public String insertRemittanceConfirm(String buyerId,String dealcode,String dunningtaskdbid,TMisRemittanceConfirm tMisRemittanceConfirm, Model model, RedirectAttributes redirectAttributes) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
			if (personalInfo != null) {
				tMisRemittanceConfirm.setName(personalInfo.getRealName());
				tMisRemittanceConfirm.setMobile(personalInfo.getMobile());
			}
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		tMisRemittanceConfirm.setConfirmstatus(TMisRemittanceConfirm.CONFIRMSTATUS_CH_SUBMIT);
		tMisRemittanceConfirm.setConfirmFlow(ConfirmFlow.CHECK);
		tMisRemittanceConfirm.preUpdate();
		if(tMisRemittanceConfirmService.getSerialnumber(tMisRemittanceConfirm.getSerialnumber()) > 0 && null != tMisRemittanceConfirm.getSerialnumber() && !tMisRemittanceConfirm.getSerialnumber().isEmpty()){
			addMessage(redirectAttributes, "提示！汇款唯一标示重复,保存失败");
		}else{
			tMisRemittanceConfirmService.save(tMisRemittanceConfirm);
			addMessage(redirectAttributes, "保存汇款确认信息成功");
		}
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisRemittanceConfirm/insertRemittanceConfirmForm?buyerId="+buyerId+"&dealcode="+dealcode+"&dunningtaskdbid="+dunningtaskdbid;
	}

	/**
	 * 汇款列表
	 * @param tMisRemittanceConfirm
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:view")
	@RequestMapping(value = {"remittanceConfirmList", ""})
	public String remittanceConfirmList(String ro,TMisRemittanceConfirm tMisRemittanceConfirm, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(null != ro && !"".equals(ro)){
			tMisRemittanceConfirm.setConfirmstatus(ro);
		}
		tMisRemittanceConfirm.setCreateBy(UserUtils.getUser());
		for (Role r : UserUtils.getUser().getRoleList()){
			if(("财务专员").equals(r.getName())  || ("系统管理员").equals(r.getName())  || ("风控总监").equals(r.getName()) ){
				tMisRemittanceConfirm.setCreateBy(null);
				break;
			}
		}
		Page<TMisRemittanceConfirm> page = tMisRemittanceConfirmService.findPage(new Page<TMisRemittanceConfirm>(request, response), tMisRemittanceConfirm);
		model.addAttribute("page", page);
		return "modules/dunning/tMisRemittanceConfirmList";
	}

	/**
	 * 加载用户信息页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:view")
	@RequestMapping(value = "remittanceConfirmCustomerDetail")
	public String remittanceConfirmCustomerDetail(String buyerId, String dealcode,String dunningtaskdbid,Model model) {
		if(buyerId==null||dealcode==null ||"".equals(buyerId)||"".equals(dealcode) ){
			return "views/error/500";
		}
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
			model.addAttribute("personalInfo", personalInfo);
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		return "modules/dunning/tMisRemittanceConfirmCustomerDetail";
	}

	/**
	 * 加载详情页面
	 * @param tMisRemittanceConfirm
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:view")
	@RequestMapping(value = "remittanceConfirmDetail")
	public String remittanceConfirmDetail(TMisRemittanceConfirm tMisRemittanceConfirm, Model model) {
		model.addAttribute("tMisRemittanceConfirm", tMisRemittanceConfirm);
		return "modules/dunning/tMisRemittanceConfirmDetail";
	}

	/**
	 * 加载催收汇款修改页面
	 * @param tMisRemittanceConfirm
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:edit")
	@RequestMapping(value = "remittanceConfirmForm")
	public String remittanceConfirmForm(TMisRemittanceConfirm tMisRemittanceConfirm, Model model) {
		model.addAttribute("filePath1",null != tMisRemittanceConfirm ? !"".equals(tMisRemittanceConfirm.getReceivablesImg1())?  tMisRemittanceConfirm.getReceivablesImg1():""  : "");
		model.addAttribute("filePath2",null != tMisRemittanceConfirm ? !"".equals(tMisRemittanceConfirm.getReceivablesImg2())?  tMisRemittanceConfirm.getReceivablesImg2():""  : "");
		model.addAttribute("tMisRemittanceConfirm", tMisRemittanceConfirm);
		return "modules/dunning/tMisRemittanceConfirmForm";
	}

	/**
	 * 修改催收汇款
	 * @param tMisRemittanceConfirm
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:edit")
	@RequestMapping(value = "remittanceUpdate")
	public String remittanceUpdate(TMisRemittanceConfirm tMisRemittanceConfirm, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisRemittanceConfirm)){
			return remittanceConfirmForm(tMisRemittanceConfirm, model);
		}
		String oldSerialnumber = (null != get(tMisRemittanceConfirm.getId()).getSerialnumber() ? get(tMisRemittanceConfirm.getId()).getSerialnumber() : "");
		if( !oldSerialnumber.equals(tMisRemittanceConfirm.getSerialnumber()) && tMisRemittanceConfirmService.getSerialnumber(tMisRemittanceConfirm.getSerialnumber()) > 0 ){
			addMessage(redirectAttributes, "提示！汇款唯一标示重复,保存失败");
		}else{
			tMisRemittanceConfirmService.remittanceUpdate(tMisRemittanceConfirm);
			addMessage(redirectAttributes, "保存催收汇款信息成功");
		}
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisRemittanceConfirm/remittanceConfirmList?repage";
	}

	/**
	 * 加载财务汇款页面
	 * @param tMisRemittanceConfirm
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:financialEdit")
	@RequestMapping(value = "remittanceFinancialConfirmForm")
	public String remittanceFinancialConfirmForm(TMisRemittanceConfirm tMisRemittanceConfirm, Model model) {
		model.addAttribute("filePath3",null != tMisRemittanceConfirm ? !"".equals(tMisRemittanceConfirm.getFinancialImg1())?  tMisRemittanceConfirm.getFinancialImg1():""  : "");
		model.addAttribute("filePath4",null != tMisRemittanceConfirm ? !"".equals(tMisRemittanceConfirm.getFinancialImg2())?  tMisRemittanceConfirm.getFinancialImg2():""  : "");
		model.addAttribute("tMisRemittanceConfirm", tMisRemittanceConfirm);
		return "modules/dunning/tMisRemittanceFinancialConfirmForm";
	}

	/**
	 * 财务汇款确认
	 * @param tMisRemittanceConfirm
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:financialEdit")
	@RequestMapping(value = "financialUpdate")
	public String financialUpdate(TMisRemittanceConfirm tMisRemittanceConfirm, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tMisRemittanceConfirm)){
			return remittanceConfirmForm(tMisRemittanceConfirm, model);
		}
		String oldFinancialserialnumber = (null != get(tMisRemittanceConfirm.getId()).getFinancialserialnumber() ? get(tMisRemittanceConfirm.getId()).getFinancialserialnumber() : "");
		if( !oldFinancialserialnumber.equals(tMisRemittanceConfirm.getFinancialserialnumber()) && tMisRemittanceConfirmService.getFinancialserialnumber(tMisRemittanceConfirm.getFinancialserialnumber()) > 0  ){
			addMessage(redirectAttributes, "提示！汇款唯一标示重复,保存失败");
		}else{
			tMisRemittanceConfirmService.financialUpdate(tMisRemittanceConfirm);
			addMessage(redirectAttributes, "保存财务汇款确认成功");
		}
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisRemittanceConfirm/remittanceConfirmList?ro=ch_submit";
	}

	/**
	 * 财务打回汇款
	 * @param tMisRemittanceConfirm
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:financialEdit")
	@RequestMapping(value = "financialReturn")
	public String financialReturn(TMisRemittanceConfirm tMisRemittanceConfirm, RedirectAttributes redirectAttributes) {
		if (StringUtils.isNotBlank(tMisRemittanceConfirm.getId())) {
			tMisRemittanceConfirmService.financialReturn(tMisRemittanceConfirm);
			addMessage(redirectAttributes, "汇款信息打回成功");
		} else {
			addMessage(redirectAttributes, "汇款信息打回失败");
		}
		return "redirect:" + Global.getAdminPath()+"/dunning/tMisRemittanceConfirm/remittanceConfirmList?ro=ch_submit";
	}

	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;
	@Autowired
	private TMisDunningTaskLogDao dunningTaskLogDao;
	@Autowired
	private TRiskBuyerPersonalInfoService personalInfoDao;

	/**
	* <pre>
	* 500/1000元      1500元
    * 第一次延期	40        80
    * 第二次延期	80        160
    * 第三次延期	120       240
    * 以此类推 	    +40       +80
    * </pre>
	* @return 续期服务费
	*/
	public static BigDecimal getDefaultDelayAmount(TMisDunningOrder order, int existDelayNumber){
		boolean amt1500 = order.getAmount().compareTo(new BigDecimal(1500)) >= 0? true : false;
		int base = amt1500? 80 : 40;
		return new BigDecimal((existDelayNumber + 1) * base);
	}

	/**
	 * 加载催收还款页面
	 * @param tMisDunningTask
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:edit")
	@RequestMapping(value = "collectionConfirmpay")
	public String collectionConfirmpay(TMisRemittanceConfirm tMisRemittanceConfirm, Model model,HttpServletRequest request, HttpServletResponse response) {
		String buyerId = request.getParameter("buyerId");
		String dealcode = request.getParameter("dealcode");
		if(buyerId==null||dealcode==null||"".equals(buyerId)||"".equals(dealcode)){
			return "views/error/500";
		}
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);

		//boolean isDelayable = order.getPayCode() == null || !order.getPayCode().startsWith(TMisDunningOrder.CHANNEL_KAOLA);

		Map<String,Object> maps = new HashMap<String,Object>();
		maps.put("buyerId",buyerId);

		TMisDunningTask task = null;
		TMisDunningOrder order = null;
		try {
			DynamicDataSource.setCurrentLookupKey("dataSource_read");
			task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
			if (task == null) {
				logger.warn("任务不存在，订单号：" + dealcode);
				return "views/error/500";
			}

			order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
			if (order == null) {
				logger.warn("订单不存在，订单号：" + dealcode);
				return "views/error/500";
			}
			TRiskBuyerPersonalInfo personalInfo = personalInfoDao.getBuyerInfoByDealcode(dealcode);
			model.addAttribute("personalInfo", personalInfo);
		} catch (Exception e) {
			logger.info("切换只读库查询失败：" + e.getMessage());
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}

		model.addAttribute("task", task);

		List<TMisRemittanceConfirm> relatedList = tMisRemittanceConfirmService.findRelatedList(tMisRemittanceConfirm);
		boolean hasRelatedRecord = false;
		BigDecimal remittanceamount = BigDecimal.valueOf(0d);
		String financialremittancechannel = "";
		if (relatedList != null && relatedList.size() > 0) {
			financialremittancechannel = relatedList.get(0).getFinancialremittancechannel();

			if (relatedList.size() > 1) {
				hasRelatedRecord = true;
				int index = 0;
				for (int i = 0; i < relatedList.size(); i++) {
					if (relatedList.get(i).getId().equals(tMisRemittanceConfirm.getId())) {
						index = i;
					}
					Double amount = relatedList.get(i).getAccountamount();
					remittanceamount = remittanceamount.add(BigDecimal.valueOf(amount == null ? 0d : amount));
				}
				relatedList.remove(index);
			}
		}
//		BigDecimal delayAmount = new BigDecimal(0l);
//		if(personalInfo != null && StringUtils.isNotBlank(personalInfo.getOverdueDays())){
//			if(Integer.valueOf(personalInfo.getOverdueDays()) <= Integer.parseInt(DictUtils.getDictValue("overdueday", "overdueday", "14")) ){
//				
//				BigDecimal cpAmt = new BigDecimal(0L);
//				if(order.getCreditAmount() != null && 
//					(
//						(order.getCouponId() != null && order.getCouponId() > 0)
//						||(order.getSubCostAmount()!=null&&order.getSubCostAmount().compareTo(BigDecimal.ZERO)>0)
//					)
//				){
//					cpAmt = order.getAmount().subtract(order.getCreditAmount());
//				}
//				
////				BigDecimal defaultInterestAmount = TMisDunningTaskController.getDefaultDelayAmount(order);
//				//续期费用 = 7天或者14天续期费用 +续期手续费用（20元或者30元）+逾期费 + 订单手续费
////				delayAmount = order.getCostAmount().add(defaultInterestAmount).subtract(cpAmt).add(order.getOverdueAmount());
//				//续期费用 = 7天或者14天续期费用 +续期手续费用（20元或者30元）+逾期费 + 订单手续费  - 减免费用
////				delayAmount = order.getCostAmount().add(defaultInterestAmount).subtract(cpAmt).add(order.getOverdueAmount()).subtract(order.getReliefflag() == 1 ? order.getReliefamount() : new BigDecimal(0));
//				int existDelayNumber = tMisRemittanceConfirmService.getExistDelayNumber(order.getRootorderid());
////				delayAmount = getDefaultDelayAmount(order, existDelayNumber);
//				delayAmount = order.getCostAmount().add(getDefaultDelayAmount(order, existDelayNumber)).add(order.getOverdueAmount()).subtract(order.getReliefflag() == 1 ? order.getReliefamount() : new BigDecimal(0));
//			}
//		}
		model.addAttribute("platform", order.getPlatform());
		model.addAttribute("tMisRemittanceConfirm", tMisRemittanceConfirm);
		//model.addAttribute("delayAmount", delayAmount);
		model.addAttribute("hasRelatedRecord", hasRelatedRecord);
		model.addAttribute("relatedList", relatedList);
		model.addAttribute("remittanceamount", remittanceamount.doubleValue());
		model.addAttribute("financialremittancechannel", financialremittancechannel);
		//model.addAttribute("isDelayable", isDelayable);
		int result = tMisRemittanceConfirmService.getResult(dealcode);
		model.addAttribute("result", result);
		return "modules/dunning/dialog/dialogCollectionConfirmpay";
	}



	/**
	 * 完成订单状态
	 * @return
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:edit")
	@RequestMapping(value = "confrimPayStatus")
	@ResponseBody
	public String confrimPayStatus(TMisPaid paid,String confirmid,String accountamount,String platform, String isMergeRepayment, String[] relatedId) {
		String dealcode = paid.getDealcode();
		String paychannel = paid.getPaychannel();

		String paidType = paid.getPaidType();
		String paidAmount = paid.getPaidAmount();
		String delayDay = paid.getDelayDay();
		if(!"1".equals(isMergeRepayment) && Double.parseDouble(paidAmount) > (null != accountamount && !"".equals(accountamount) ? Double.parseDouble(accountamount) : Double.parseDouble("0"))){
			return "还款金额不应该大于财务的到账金额";
		}
		if(StringUtils.isBlank(delayDay)){
			paid.setDelayDay("0");
		}
		if( StringUtils.isBlank(paidAmount) || StringUtils.isBlank(dealcode)){
			return "错误，用户或者订单不存在";
		}
		if(StringUtils.isBlank(paidType) || StringUtils.isBlank(paychannel)){
			return "错误，代付类型或代付渠道不存在";
		}
		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		if (order == null) {
			return ("错误，用户或者订单不存在");
		}
		if(order.status.equals("payoff")){
			return "错误，订单已还清";
		}
		String msg ;
		try {
			msg = tMisRemittanceConfirmService.checkConfirm(paid,isMergeRepayment,confirmid,platform,relatedId);
		} catch (IOException e) {
			logger.warn("还款错误",e);
			return "error";
		}
		return  "ok:" + msg;
	}

	/**
	 * @Description 查账流程--入账
	 * @param remittanceConfirmId 汇款确认id
	 * @param paytype 还款类型
	 * @param remittanceTag 还款标签
	 * @return java.lang.String
	 */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:auditConfrim")
	@RequestMapping(value = "auditConfrim")
	@ResponseBody
	public String auditConfrim(String remittanceConfirmId,String paytype,RemittanceTag remittanceTag){
		if (StringUtils.isBlank(remittanceConfirmId)){
			return "汇款确认id不能空";
		}
		if (StringUtils.isBlank(paytype)){
			return "还款类型不能为空";
		}
		TMisRemittanceConfirm confirm = tMisRemittanceConfirmService.get(remittanceConfirmId);
		if (confirm == null){
			return "未查询到汇确认信息";
		}
		if (!ConfirmFlow.AUDIT.equals(confirm.getConfirmFlow()) && !TMisRemittanceConfirm.CONFIRMSTATUS_COMPLETE_AUDIT.equals(confirm.getConfirmstatus())){
			return "错误，汇确认信息未完成查账";
		}
		DunningOrder order = tMisDunningOrderService.findOrderByDealcode(confirm.getDealcode());
		if (order == null) {
			return ("错误，用户或者订单不存在");
		}
		if(order.getStatus().equals(DunningOrder.STATUS_PAYOFF)){
			return "错误，订单已还清";
		}
		if (remittanceTag == null){
			if (confirm.getRemittanceTag() == null){
				return "还款标签不能为空";
			}
		}else {//若入账标记还款标签,则以入账时为准
			confirm.setRemittanceTag(remittanceTag);
		}
		//若还款类型为还清则判断  还款金额<应催金额
		if (TMisRemittanceConfirm.PAYTYPE_LOAN.equals(paytype)){
			if(confirm.getRemittanceamount() < order.getRemainAmmount() ){
				return "金额不匹配，入账失败";
			}
		}
		confirm.setPaytype(paytype);
		try {
			tMisRemittanceConfirmService.auditConfrim(confirm);
		} catch (Exception e) {
			logger.warn("入账失败",e);
			return "入账失败, 服务器网络异常";
		}
		return "success";
	}

	private static String getResponseData(String urlStr, String dataInfo) throws Exception {
			HttpURLConnection conn = null;
			StringBuffer result = new StringBuffer();
			try {
				URL url = new URL(urlStr);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(10000);
				OutputStream out = conn.getOutputStream();
				DataOutputStream data = new DataOutputStream(out);
				data.write(dataInfo.getBytes("UTF-8"));
				data.flush();
				data.close();
				out.close();

				InputStream input = conn.getInputStream();
				InputStreamReader inputReader = new InputStreamReader(input);
				BufferedReader reader = new BufferedReader(inputReader);
				String inputLine = null;
				while ((inputLine = reader.readLine()) != null) {
					result.append(inputLine);
				}
				reader.close();
				inputReader.close();
				input.close();
			} finally {
				conn.disconnect();
			}
			return result.toString();
	}


   	public static String convertStreamToString(InputStream is) {
           BufferedReader reader = new BufferedReader(new InputStreamReader(is));
           StringBuilder sb = new StringBuilder();
           String line = null;
           try {
               while ((line = reader.readLine()) != null) {
                   sb.append(line + "\n");
               }
           } catch (IOException e) {
               e.printStackTrace();
           } finally {
               try {
                   is.close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
           }
           return sb.toString();
       }

   	@RequiresPermissions("dunning:tMisRemittanceConfirm:view")
	@RequestMapping(value = "/uploadImage")
    public String uploadImage(String formurl,String filenum,Model model,TMisRemittanceConfirm tMisRemittanceConfirm, String buyerId,String dealcode,String dunningtaskdbid,
    					 HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String UPLOAD_PATH = "/home/gamaxwin/deploy/img/mo9Debt/";
//		String UPLOAD_PATH = "C:/apache-tomcat-7.0.65.war/webapps/remittanceimg/";//这个是图片保存在服务器上的地址目录
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = null;
		model.addAttribute("filePath1",null != tMisRemittanceConfirm ? !"".equals(tMisRemittanceConfirm.getReceivablesImg1())?  tMisRemittanceConfirm.getReceivablesImg1():""  : "");
		model.addAttribute("filePath2",null != tMisRemittanceConfirm ? !"".equals(tMisRemittanceConfirm.getReceivablesImg2())?  tMisRemittanceConfirm.getReceivablesImg2():""  : "");
		model.addAttribute("filePath3",null != tMisRemittanceConfirm ? !"".equals(tMisRemittanceConfirm.getFinancialImg1())?  tMisRemittanceConfirm.getFinancialImg1():""  : "");
		model.addAttribute("filePath4",null != tMisRemittanceConfirm ? !"".equals(tMisRemittanceConfirm.getFinancialImg2())?  tMisRemittanceConfirm.getFinancialImg2():""  : "");
		if("file1".equals(filenum)){
			file = mRequest.getFile("file1");
			String fileName = new Date().getTime()+"ReceivablesImg1"+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
			String filePath = uploadFile(file,fileName,UPLOAD_PATH, request);
			model.addAttribute("filePath1", filePath);
		}
		if ("file2".equals(filenum)){
			file = mRequest.getFile("file2");
			String fileName = new Date().getTime()+"ReceivablesImg2"+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
			String filePath = uploadFile(file,fileName,UPLOAD_PATH, request);
			model.addAttribute("filePath2", filePath);
		}
		if ("file3".equals(filenum)){
			file = mRequest.getFile("file3");
			String fileName = new Date().getTime()+"FinancialImg1"+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
			String filePath = uploadFile(file,fileName,UPLOAD_PATH, request);
			model.addAttribute("filePath3", filePath);
		}
		if ("file4".equals(filenum)){
			file = mRequest.getFile("file4");
			String fileName = new Date().getTime()+"FinancialImg2"+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
			String filePath = uploadFile(file,fileName,UPLOAD_PATH, request);
			model.addAttribute("filePath4", filePath);
		}
        model.addAttribute("tMisRemittanceConfirm", tMisRemittanceConfirm);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
        model.addAttribute("uploadpath", UPLOAD_PATH);
        return "modules/dunning/" + formurl;
    }

	/**
	 * 文件上传
	 * @param file
	 * @param fileName
	 * @param uploadPath
	 * @param request
	 * @throws IOException
	 */
    public static String uploadFile(MultipartFile file,String fileName,String uploadPath,HttpServletRequest request) throws IOException {
        File tempFile = new File(uploadPath,String.valueOf(fileName));
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        if (!tempFile.exists()) {
            tempFile.createNewFile();
        }
        file.transferTo(tempFile);
        return "downloadImage?fileName=" + tempFile.getName()+"&filePath="+uploadPath;
    }

    /**
     * 二进制
     * @param fileName
     * @param filePath
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/downloadImage")
    public void downloadImage(String fileName,String filePath,HttpServletResponse response) throws IOException {
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setContentType("image/jpg;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            os.write(FileUtils.readFileToByteArray(getFile(fileName,filePath)));
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public static File getFile(String fileName,String filePath) {
        return new File(filePath, fileName);
    }

    /**
     * 软删除汇款信息
     * @param tMisRemittanceConfirm
     * @param redirectAttributes
     * @return
     */
	@RequiresPermissions("dunning:tMisRemittanceConfirm:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisRemittanceConfirm tMisRemittanceConfirm, RedirectAttributes redirectAttributes) {
		tMisRemittanceConfirmService.delete(tMisRemittanceConfirm);
		addMessage(redirectAttributes, "删除汇款确认信息成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisRemittanceConfirm/?repage";
	}


}