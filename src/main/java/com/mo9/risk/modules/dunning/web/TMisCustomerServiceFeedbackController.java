package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.mo9.risk.modules.dunning.service.TMisCustomerServiceFeedbackService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
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

/**
 * 客服问题推送
 * Created by qtzhou on 2017/10/31.
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisCustomerServiceFeedback")
public class TMisCustomerServiceFeedbackController extends BaseController {

    @Autowired
    private TMisCustomerServiceFeedbackService tMisCustomerServiceFeedbackService;

    @ModelAttribute
    public TMisCustomerServiceFeedback get(@RequestParam(required=false) String id) {
        TMisCustomerServiceFeedback entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = tMisCustomerServiceFeedbackService.get(id);
        }
        if (entity == null){
            entity = new TMisCustomerServiceFeedback();
        }
        return entity;
    }

    /**
     * 分页展示问题案件列表
     * @param
     */
    @RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
    @RequestMapping(value = {"findList", ""})
    public String findList(TMisCustomerServiceFeedback tMisCustomerServiceFeedback, HttpServletRequest request, HttpServletResponse response, Model model){

        //默认条件展示未解决问题消息案件
       /* if (tMisCustomerServiceFeedback.getProblemstatus() == null){
            tMisCustomerServiceFeedback.setProblemstatus(tMisCustomerServiceFeedback.PROBLEM_STATUS_SOLVED);
        }*/
        Page<TMisCustomerServiceFeedback> page = tMisCustomerServiceFeedbackService.findPage(new Page<TMisCustomerServiceFeedback>(request, response), tMisCustomerServiceFeedback);
        model.addAttribute("page", page);
        return "modules/dunning/tMisCustomerServiceFeedbackList";
    }
}
