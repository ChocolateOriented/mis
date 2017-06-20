package com.mo9.risk.modules.dunning.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mo9.risk.modules.dunning.entity.TMisRemittanceExcel;
import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 账目解析
 * 
 * @author jwchi
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningAccountAnalysis")
public class TMisDunningAccountAnalysisController extends BaseController{

	
	
	
	/**
	 * 跳转账目解析页面
	 * @return
	 */
	@RequestMapping(value="")
	public String AccountAnalysis(){
		
		return "modules/dunning/tMisDunningAccountAnalysis";
	}
	
	/**
	 * 上传文件
	 * @param file
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="fileUpload")
	public void fileUpload(MultipartFile file,String zhifuPay, RedirectAttributes redirectAttributes,HttpServletRequest request) throws Exception{
		System.out.println(file.getOriginalFilename());
		System.out.println(zhifuPay);
		ImportExcel ei = new ImportExcel(file, 1, 0);
		List<TMisRemittanceExcel> list = ei.getDataList(TMisRemittanceExcel.class);
		int i=1;
		for (TMisRemittanceExcel tMisRemittanceExcel : list) {
			System.out.println(tMisRemittanceExcel+"---"+i);
			i++;
			
		}
		  
      
	}
	
}
