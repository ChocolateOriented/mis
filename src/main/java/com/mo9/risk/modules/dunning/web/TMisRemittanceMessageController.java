/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.mo9.risk.modules.dunning.service.TMisRemittanceMessageService;
import com.mo9.risk.util.FileUpload;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 财务确认汇款信息Controller
 * @author 徐盛
 * @version 2016-08-11
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisRemittanceMessage")
public class TMisRemittanceMessageController extends BaseController {

	@Autowired
	private TMisRemittanceMessageService tMisRemittanceMessageService;
	
	@ModelAttribute
	public TMisRemittanceMessage get(@RequestParam(required=false) String id) {
		TMisRemittanceMessage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tMisRemittanceMessageService.get(id);
		}
		if (entity == null){
			entity = new TMisRemittanceMessage();
		}
		return entity;
	}
	
	@RequiresPermissions("dunning:tMisRemittanceMessage:view")
	@RequestMapping(value = {"list", ""})
	public String list(TMisRemittanceMessage tMisRemittanceMessage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TMisRemittanceMessage> page = tMisRemittanceMessageService.findPage(new Page<TMisRemittanceMessage>(request, response), tMisRemittanceMessage); 
		model.addAttribute("page", page);
		return "modules/dunning/tMisRemittanceMessageList";
	}

	@RequiresPermissions("dunning:tMisRemittanceMessage:view")
	@RequestMapping(value = "form")
	public String form( String buyerId,String dealcode,String dunningtaskdbid,boolean hasContact,TMisRemittanceMessage tMisRemittanceMessage, Model model, HttpServletRequest request,HttpServletResponse response) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
//		String tomcaturl = "http://localhost:8089/remittanceimg/";
//		String tomcaturl = "/home/gamaxwin/deploy/img/mo9Debt/";
		TMisRemittanceMessage misRemittanceMessage = tMisRemittanceMessageService.findRemittanceMesListByDealcode(dealcode);
		model.addAttribute("TMisRemittanceMessage", null != misRemittanceMessage ? misRemittanceMessage : new TMisRemittanceMessage());
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		model.addAttribute("filePath",null != misRemittanceMessage ?
				!"".equals(misRemittanceMessage.getRemittanceimg())?  misRemittanceMessage.getRemittanceimg():""  : "");
		model.addAttribute("hasContact", hasContact);
		return "modules/dunning/tMisRemittanceMessageForm";
	}

	@RequiresPermissions("dunning:tMisRemittanceMessage:edit")
	@RequestMapping(value = "save")
	public String save(String financialuser,String buyerId,String dealcode,String dunningtaskdbid, HttpServletRequest request, HttpServletResponse response, TMisRemittanceMessage tMisRemittanceMessage, Model model, RedirectAttributes redirectAttributes) {
		if(buyerId==null||dealcode==null||dunningtaskdbid==null||"".equals(buyerId)||"".equals(dealcode)||"".equals(dunningtaskdbid)){
			return "views/error/500";
		}
		tMisRemittanceMessageService.insert(tMisRemittanceMessage);
//		tMisRemittanceMessageService.save(tMisRemittanceMessage);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
		addMessage(redirectAttributes, "保存财务确认汇款信息成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisRemittanceMessage/form?buyerId="+buyerId+"&dealcode="+dealcode+"&dunningtaskdbid="+dunningtaskdbid;
	}
	
	
    @RequestMapping(value = "/uploadImage")  
    public String  upload(@RequestParam("file") MultipartFile file,Model model,TMisRemittanceMessage tMisRemittanceMessage, String buyerId,String dealcode,String dunningtaskdbid,
    					 HttpServletRequest request, HttpServletResponse response) throws IOException {
//    	String UPLOAD_PATH = "C:/apache-tomcat-7.0.65.war/webapps/remittanceimg/";//这个是图片保存在服务器上的地址目录
    	String UPLOAD_PATH = "/home/gamaxwin/deploy/img/mo9Debt/";
    	String fileName = new Date().getTime()+"RemittanceMesssage"+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')); 
        String filePath = FileUpload.uploadFile(file,fileName,UPLOAD_PATH, request);  
        model.addAttribute("TMisRemittanceMessage", tMisRemittanceMessage);
		model.addAttribute("dunningtaskdbid", dunningtaskdbid);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("dealcode", dealcode);
        model.addAttribute("filePath", filePath);
        model.addAttribute("uploadpath", UPLOAD_PATH);
        model.addAttribute("fileName", fileName);
        return "modules/dunning/tMisRemittanceMessageForm";
       
    } 
    
    @RequestMapping(value = "/downloadImage")  
    public void download(String fileName,String filePath,HttpServletResponse response) throws IOException {  
        OutputStream os = response.getOutputStream();  
        try {  
            response.reset();  
            response.setContentType("image/jpg;charset=utf-8");  
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);  
            os.write(FileUtils.readFileToByteArray(FileUpload.getFile(fileName,filePath)));  
            os.flush();  
        } finally {  
            if (os != null) {  
                os.close();  
            }  
        }  
    } 
	
	@RequiresPermissions("dunning:tMisRemittanceMessage:edit")
	@RequestMapping(value = "delete")
	public String delete(TMisRemittanceMessage tMisRemittanceMessage, RedirectAttributes redirectAttributes) {
		tMisRemittanceMessageService.delete(tMisRemittanceMessage);
		addMessage(redirectAttributes, "删除财务确认汇款信息成功");
		return "redirect:"+Global.getAdminPath()+"/dunning/tMisRemittanceMessage/?repage";
	}

}