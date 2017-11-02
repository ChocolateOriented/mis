/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 催收测试Controller
 * @author shijlu
 * @version 2017-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningTest")
public class TMisDunningTestController extends BaseController {
	
	@RequestMapping(value = "/query")
	@RequiresPermissions("dunning:tMisDunningTest:view")
	@ResponseBody
	public String query(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		String value = request.getParameter("value");
		return DictUtils.getDictLabel(value, type, "");
	}
	
	@RequestMapping(value = "/queryAll")
	@RequiresPermissions("dunning:tMisDunningTest:view")
	@ResponseBody
	public List<Dict> queryAll(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		return DictUtils.getDictList(type);
	}
	
	@RequestMapping(value = "/testPage")
	@RequiresPermissions("dunning:tMisDunningTest:view")
	public String testPage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/dunning/tMisDunningTestPage";
	}

}