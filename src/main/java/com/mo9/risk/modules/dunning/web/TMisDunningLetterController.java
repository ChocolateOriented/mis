package com.mo9.risk.modules.dunning.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.TMisDunningLetter;
import com.mo9.risk.modules.dunning.entity.TMisDunningLetter.SendResult;
import com.mo9.risk.modules.dunning.service.TMisDunningLetterService;
import com.mo9.risk.util.CsvUtil;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 东港信函Controller
 * @author chijw
 * @version 2017-12-07
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningLetter")
public class TMisDunningLetterController extends BaseController {

	@Autowired
	TMisDunningLetterService tMisDunningLetterService;
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	@ModelAttribute
	public TMisDunningLetter get(@RequestParam(required=false) String id) {
		TMisDunningLetter entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisDunningLetterService.get(id);
		}
		if (entity == null){
			entity = new TMisDunningLetter();
		}
		return entity;
	}
	
	@RequestMapping(value = {"list", ""})
	public String findPageList(TMisDunningLetter tMisDunningLetter, HttpServletRequest request, HttpServletResponse response, Model model){
		Page<TMisDunningLetter> page = tMisDunningLetterService.findPageList(new Page<TMisDunningLetter>(request, response), tMisDunningLetter);
		model.addAttribute("page",page);
		SendResult[] values = SendResult.values();
		model.addAttribute("sendResults",values);
		return "modules/dunning/tMisDunningletter";
	}
	/**
	 * 同步待发送数据
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@ResponseBody
	@RequestMapping(value = "synDealcode")
	public String synDealcode(HttpServletRequest request, HttpServletResponse response) {
		tMisDunningLetterService.synDealcode();
		return "Ok";
	}
	/**
	 * 补发下载邮件
	 */
	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@ResponseBody
	@RequestMapping(value = "downLoadMail")
	public void downLoadMail( String identity ,HttpServletRequest request, HttpServletResponse response) {
		tMisDunningLetterService.sendMail(identity);
	}
	/**
	 * 发送信函
	 */
//	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "sendLetterDealcodes")
	public String sendLetters(@RequestParam("sendLetterDealcodes") List<String> sendLetterDealcodes,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String redirectUrl = "redirect:" + adminPath + "/dunning/tMisDunningLetter/list?repage";
		boolean	 sendBol=tMisDunningLetterService.sendLetters(sendLetterDealcodes);
		if(sendBol){
			addMessage(redirectAttributes,"发送信函成功");
		}else{
			addMessage(redirectAttributes,"发送信函发生失败");
		}
		return redirectUrl;
    }
	/**
	 * 信函导出
	 */
//	@RequiresPermissions("dunning:tMisDunningTask:adminview")
	@RequestMapping(value = "exportFile")
	public String migrateExport(TMisDunningLetter tMisDunningLetter,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		String redirectUrl = "redirect:" + adminPath + "/dunning/tMisDunningLetter/list?repage";
		String fileName = "信函管理" + DateUtils.getDate("yyyy/MM/dd")+".xlsx";
		List<TMisDunningLetter> letterList = tMisDunningLetterService.findList(tMisDunningLetter); 
		if (null == letterList || letterList.isEmpty()) {
			addMessage(redirectAttributes, "未导出数据！");
			return redirectUrl;
		}
		try {
			new ExportExcel("信函管理", TMisDunningLetter.class).setDataList(letterList).write(response, fileName).dispose();
		} catch (Exception e) {
			logger.warn("导出失败！失败信息："+e);
			addMessage(redirectAttributes, "导出失败！失败信息："+e);
		}
		return redirectUrl;
	}
	/**
	 * 导入
	 * @param peopleids
	 * @param model
	 * @return
	 */
	@RequiresPermissions("dunning:tMisDunningPeople:edit")
	@RequestMapping(value = "fileUpload")
	public String fileUpload( Model model,MultipartFile file, RedirectAttributes redirectAttributes) {
		String redirectUrl = "redirect:" + adminPath + "/dunning/tMisDunningLetter/list?repage";
		//解析上传Excel或csv
		List<TMisDunningLetter> list ;
		String filename = file.getOriginalFilename();
		logger.info("正在接析文件:" +filename) ;
		try {
			if (StringUtils.endsWith(filename,".csv")){
				list = CsvUtil.importCsv(file,TMisDunningLetter.class,0);
			}else {
				ImportExcel ei = new ImportExcel(file,0, 0);
				list = ei.getDataList(TMisDunningLetter.class);
			}
			logger.info("完成接析文件:" + file.getOriginalFilename());
		} catch (Exception e) {
			logger.info("解析式发生错误",e);
			addMessage(redirectAttributes, "解析文件:" + file.getOriginalFilename() + ",发生失败"+e.getMessage());
			return redirectUrl;
		}
		if(list==null||list.size()<1){
			addMessage(redirectAttributes, "解析文件:" + file.getOriginalFilename() + ",发生失败内容为空");
			return redirectUrl;
		}
		StringBuilder message=new StringBuilder();
		boolean validsInsert=tMisDunningLetterService.batchUpdate(list,message);
		if(!validsInsert){
			addMessage(redirectAttributes,  "解析文件:" + file.getOriginalFilename() + ",发生失败."+message.toString());
			return redirectUrl;
		}
		logger.info("导入成功,文件:" + file.getOriginalFilename());
		addMessage(redirectAttributes,  "导入成功.");
		return redirectUrl;
	}
}