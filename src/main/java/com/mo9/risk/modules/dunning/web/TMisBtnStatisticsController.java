/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.web.BaseController;
import com.mo9.risk.modules.dunning.entity.TMisBtnStatistics;
import com.mo9.risk.modules.dunning.service.TMisBtnStatisticsService;

/**
 * 按钮统计Controller
 * @author shijlu
 * @version 2017-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisBtnStatistics")
public class TMisBtnStatisticsController extends BaseController {

	@Autowired
	private TMisBtnStatisticsService tMisBtnStatisticsService;
	
	@RequiresPermissions("dunning:tMisDunningDeduct:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public String saveCard(TMisBtnStatistics tMisBtnStatistics, HttpServletRequest request, HttpServletResponse response) {
		tMisBtnStatisticsService.save(tMisBtnStatistics);
		return "OK";
	}

}