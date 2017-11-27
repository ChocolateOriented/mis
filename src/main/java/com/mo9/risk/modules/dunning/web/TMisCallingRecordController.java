/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.DunningPhoneReportFile;
import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.service.TMisCallingRecordService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningOrderService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.mo9.risk.modules.dunning.service.TMisDunningPhoneService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CTI回调Controller
 */
@Controller
@RequestMapping(value="${adminPath}/dunning/tMisCallingRecord")
public class TMisCallingRecordController extends BaseController {
	
	@Autowired
	private TMisCallingRecordService tMisCallingRecordService;
	
	@Autowired
	private TMisDunningOrderService tMisDunningOrderService;
	
	@Autowired
	private TMisDunningTaskService tMisDunningTaskService;
	
	@Autowired
	private TMisDunningGroupService tMisDunningGroupService;

	@Autowired
	private TMisDunningPeopleService tMisDunningPeopleService;

	@RequiresPermissions("dunning:tMisCallingRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(@ModelAttribute("tMisCallingRecord") TMisCallingRecord tMisCallingRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisCallingRecord> page = tMisCallingRecordService.findPage(new Page<TMisCallingRecord>(request, response), tMisCallingRecord);
		for (TMisCallingRecord callingRecord : page.getList()){
			String number = callingRecord.getTargetNumber();
			String realNumber = TMisDunningPhoneService.filterCtiCallInfoNumber(number);
			callingRecord.setTargetNumber(realNumber);
		}
		//催收小组列表
		TMisDunningGroup tMisDunningGroup = new TMisDunningGroup();
		int permissions = TMisDunningTaskService.getPermissions();
		boolean supervisorLimit = false;
		if (permissions == TMisDunningTaskService.DUNNING_INNER_PERMISSIONS) {
			tMisDunningGroup.setLeader(UserUtils.getUser());
			supervisorLimit = true;
		}
		if (permissions == TMisDunningTaskService.DUNNING_SUPERVISOR) {
			tMisDunningGroup.setSupervisor(UserUtils.getUser());
			supervisorLimit = true;
		}
		model.addAttribute("groupList", tMisDunningGroupService.findList(tMisDunningGroup));
		model.addAttribute("supervisorLimit", supervisorLimit);
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes) ;
		model.addAttribute("page", page);
		String url = DictUtils.getDictValue("ctiUrl", "callcenter", "") + "audio/";
		model.addAttribute("ctiUrl", url);
		model.addAttribute("userId", UserUtils.getUser().getId());
		return "modules/dunning/tMisCallingRecordList";
	}
	
	@RequiresPermissions("dunning:tMisCallingRecord:view")
	@RequestMapping(value = "gotoTask")
	public String gotoTask(TMisCallingRecord tMisCallingRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		String dealcode = tMisCallingRecord.getDealcode();
		if (tMisCallingRecord.getDealcode() == null) {
			return "views/error/500";
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("DEALCODE", dealcode);
		DunningOrder order = tMisDunningOrderService.findOrderByDealcode(dealcode);
		TMisDunningTask task = tMisDunningTaskService.findDunningTaskByDealcode(params);
		
		if (order == null || task == null) {
			return "views/error/500";
		}
		
		return "redirect:" + adminPath + "/dunning/tMisDunningTask/pageFather?buyerId="
			+ order.getBuyerid() + "&dealcode=" + dealcode + "&dunningtaskdbid=" + task.getId() + "&status=" + order.getStatus();
	}

	/**
	 * 下载通话记录
	 * @param audioUrl
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequiresPermissions("dunning:tMisCallingRecord:view")
	@RequestMapping(value = "audioDownload")
	public void audioDownload(@RequestParam(value = "audioUrl") String audioUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String realPath = audioUrl;
		String fileName = realPath.substring(realPath.lastIndexOf("/") + 1);
		response.reset(); //清空response
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);
		OutputStream out = response.getOutputStream();
		URL url = new URL(realPath);
		BufferedInputStream input = new BufferedInputStream(url.openStream());
		try {
			response.setContentType("audio/wav");
			int n = 0;
			byte b[] = new byte[1024];
			while ((n = input.read(b)) != -1) {
				out.write(b, 0, n);
			}
			out.flush();
		} catch (Exception e) {
			logger.info("下载电话音频文件失败" + e);
		} finally {
			if (input != null) {
				input.close();
			}
			if (out != null) {
				out.close();
			}
		}

	}


	/**
	 * 查询软电话日常报表
	 */
	@RequiresPermissions("dunning:tMisCallingRecord:viewReport")
	@RequestMapping(value = "getPhoneCallingReport")
	public String getPhoneCallingReport(DunningPhoneReportFile dunningPhoneReportFile, TMisDunningPeople dunningPeople, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<TMisDunningPeople> dunningPeoples = null;
		List<TMisDunningGroup> groups = new ArrayList<TMisDunningGroup>();
		if (dunningPhoneReportFile.getDatetimestart() == null || "".equals(dunningPhoneReportFile.getDatetimestart())){
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);//正数可以得到当前时间+n天，负数可以得到当前时间-n天
			calendar.set(Calendar.HOUR_OF_DAY, 00);
			calendar.set(Calendar.MINUTE, 00);
			date = calendar.getTime();
			calendar.setTime(date);
			dunningPhoneReportFile.setDatetimestart(date);
		}
		if (dunningPhoneReportFile.getDatetimeend() == null || "".equals(dunningPhoneReportFile.getDatetimeend())){
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);//正数可以得到当前时间+n天，负数可以得到当前时间-n天
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			date = calendar.getTime();
			calendar.setTime(date);
			dunningPhoneReportFile.setDatetimeend(date);
		}
		TMisDunningGroup tMisDunningGroup = new TMisDunningGroup();
		int permissions = TMisDunningTaskService.getPermissions();
		boolean supervisorLimit = false;
		boolean dunningCommissioner = false;
		//催收专员
		if (permissions == TMisDunningTaskService.DUNNING_COMMISSIONER_PERMISSIONS){
			TMisDunningPeople dp = new TMisDunningPeople();
			dunningPeoples = tMisDunningPeopleService.findList(dp);
			groups.add(dunningPeoples.get(0).getGroup());
			dunningCommissioner = true;
		}else {
			//催收主管
			if (permissions == TMisDunningTaskService.DUNNING_INNER_PERMISSIONS) {
				tMisDunningGroup.setLeader(UserUtils.getUser());
				supervisorLimit = true;
			}
			//催收监管
			if (permissions == TMisDunningTaskService.DUNNING_SUPERVISOR) {
				tMisDunningGroup.setSupervisor(UserUtils.getUser());
				supervisorLimit = true;
			}
			 groups = tMisDunningGroupService.findList(tMisDunningGroup);
			StringBuffer stringBuffer = new StringBuffer("");
			for (TMisDunningGroup group : groups) {
				stringBuffer.append("," + group.getId());
			}
			List<String> groupIds = Arrays.asList(stringBuffer.toString().substring(1).split(","));
			tMisDunningGroup.setGroupIds(groupIds);
			System.out.println(dunningPhoneReportFile.getPeopleId());
			if (dunningPhoneReportFile.getGroupId() == null || "".equals(dunningPhoneReportFile.getGroupId())) {
				dunningPhoneReportFile.setGroup(tMisDunningGroup);
			}
			dunningPeople.setGroup(tMisDunningGroup);
			dunningPeoples = tMisDunningPeopleService.findList(dunningPeople);
			if (dunningPhoneReportFile.getPeopleName() == null || "".equals(dunningPhoneReportFile.getPeopleName())) {
				List<String> peopleIds = new ArrayList<String>(dunningPeoples.size());
				dunningPhoneReportFile.setPeopleIds(peopleIds);
				for (TMisDunningPeople people : dunningPeoples) {
					dunningPhoneReportFile.getPeopleIds().add(people.getId());
				}
			}
		}
		Page<DunningPhoneReportFile> page = tMisCallingRecordService.exportStatementFile(new Page<DunningPhoneReportFile>(request, response), dunningPhoneReportFile);
		model.addAttribute("groupTypes", TMisDunningGroup.groupTypes);
		model.addAttribute("groupList", groups);
		model.addAttribute("page", page);
		model.addAttribute("supervisorLimit", supervisorLimit);
		model.addAttribute("dunningCommissioner", dunningCommissioner);
		model.addAttribute("dunningPhoneReportFile", dunningPhoneReportFile);
		return "modules/dunning/performancePhoneCallingReportList";
	}


	@RequiresPermissions("dunning:tMisCallingRecord:edit")
	@RequestMapping(value = "sync")
	@ResponseBody
	public String sync(Date syncDate, HttpServletRequest request, HttpServletResponse response) {
		if (syncDate == null) {
			return "error";
		}
		tMisCallingRecordService.syncCallRecordManual(syncDate);
		return "success";
	}
}