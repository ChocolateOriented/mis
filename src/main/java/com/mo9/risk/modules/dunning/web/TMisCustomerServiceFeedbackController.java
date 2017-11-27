package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.service.FeedbackSendService;
import com.mo9.risk.modules.dunning.service.TMisCustomerServiceFeedbackService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;
import com.thinkgem.jeesite.modules.oa.service.OaNotifyService;
import com.thinkgem.jeesite.modules.sys.entity.User;
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

/**
 * 客服问题推送
 * Created by qtzhou on 2017/10/31.
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisCustomerServiceFeedback")
public class TMisCustomerServiceFeedbackController extends BaseController {

    @Autowired
    private TMisCustomerServiceFeedbackService tMisCustomerServiceFeedbackService;

    @Autowired
    private FeedbackSendService feedbackSendService;

    @Autowired
    private OaNotifyService oaNotifyService;

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
    @RequestMapping(value = {"feedbackList",""})
    public String feedbackList(TMisCustomerServiceFeedback tMisCustomerServiceFeedback, HttpServletRequest request, HttpServletResponse response, Model model){

        String buyerId = request.getParameter("buyerId");
        if(buyerId==null||"".equals(buyerId)){
            return "views/error/500";
        }

        Page<TMisCustomerServiceFeedback> page = tMisCustomerServiceFeedbackService.feedbackList(new Page<TMisCustomerServiceFeedback>(request, response), tMisCustomerServiceFeedback);
        model.addAttribute("page", page);
        model.addAttribute("buyerId", buyerId);
        return "modules/dunning/tMisCustomerServiceFeedbackList";
    }


    /**
     * 待解决弹框
     *
     */
    @RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
    @RequestMapping(value ="jboxResult")
    public String jboxResult(HttpServletRequest request, Model model){
        String id=request.getParameter("id");
        TMisCustomerServiceFeedback tMisCustomerServiceFeedback=null;
        try{
            tMisCustomerServiceFeedback=tMisCustomerServiceFeedbackService.get(id);
        }catch (Exception e){
            logger.info("",e);
            return null;
        }
        model.addAttribute("id", id);
        return "modules/dunning/tMisCustomerJboxResult";
    }

    /**
     * 操作待解决结果
     *
     */
    @RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
    @RequestMapping(value ="resultSave")
    @ResponseBody
    public TMisCustomerServiceFeedback resultSave(TMisCustomerServiceFeedback tMisCustomerServiceFeedback, HttpServletRequest request){

        TMisCustomerServiceFeedback feedback=null;
        try{
            tMisCustomerServiceFeedbackService.updateFeedback(tMisCustomerServiceFeedback);
            /**
             * 消息发送，数据库更新失败时不会发送
             */
            feedback = tMisCustomerServiceFeedback;
            feedbackSendService.createFeedBackRecord(feedback);
        }catch (Exception e){
            logger.info("",e);
            return null;
        }

        return feedback;
    }

    /**
     * 问题案件通知列表
     * @param
     */
    @RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
    @RequestMapping(value = {"notify", ""})
    public String NotifyList(String colorChoice,TMisCustomerServiceFeedback tMisCustomerServiceFeedback, HttpServletRequest request, HttpServletResponse response, Model model){
        if (StringUtils.isEmpty(tMisCustomerServiceFeedback.getProblemstatus())){
            tMisCustomerServiceFeedback.setProblemstatus("UNRESOLVED");
        }

        Page<TMisCustomerServiceFeedback> page = tMisCustomerServiceFeedbackService.NotifyList(new Page<TMisCustomerServiceFeedback>(request, response,20), tMisCustomerServiceFeedback);
        model.addAttribute("page", page);
        model.addAttribute("color", colorChoice);
        return "modules/oa/notifyList";
    }

    /**
     * 客服问题通知截图弹框
     *
     */
    @RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
    @RequestMapping(value ="feedbackJbox")
    public String jboxNotify(TMisCustomerServiceFeedback customerServiceFeedback, HttpServletRequest request, Model model){

        TMisCustomerServiceFeedback tMisCustomerServiceFeedback=null;
        try{
            tMisCustomerServiceFeedback=tMisCustomerServiceFeedbackService.findCodeStatusTagDesPeople(customerServiceFeedback);
            if("0".equals(tMisCustomerServiceFeedback.getReadFlag())){
                tMisCustomerServiceFeedback.setReadFlag("1");
                tMisCustomerServiceFeedbackService.updateFeedback(tMisCustomerServiceFeedback);
                model.addAttribute("custNotify", "desc");
            }
        }catch (Exception e){
            logger.info("加载反馈通知截图失败",e);
            return null;
        }
        model.addAttribute("tMisCustomerServiceFeedback", tMisCustomerServiceFeedback);
        return "modules/dunning/tMisCustomerJboxNotify";
    }

    /**
     * 获取客服通知数
     *
     */
    @RequestMapping(value = "custServiceCount")
    @ResponseBody
    public String custServiceCount(TMisCustomerServiceFeedback tMisCustomerServiceFeedback, Model model){

        tMisCustomerServiceFeedback.setReadFlag("0");
        String userid=UserUtils.getUser().getId();
        tMisCustomerServiceFeedback.setDunningpeopleid(userid);
        return String.valueOf(tMisCustomerServiceFeedbackService.findCustServiceCount(tMisCustomerServiceFeedback));
    }
}
