package com.mo9.risk.modules.dunning.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mo9.risk.modules.dunning.entity.TMisDunningLetter;
import com.mo9.risk.modules.dunning.service.TMisDunningLetterService;
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
	
}