package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.DunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.service.FeedbackSendService;
import com.mo9.risk.modules.dunning.service.TMisCustomerServiceFeedbackService;
import com.mo9.risk.modules.dunning.service.TMisDunningOrderService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.util.WebSocketSessionUtil;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
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
import java.util.HashMap;
import java.util.Map;

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
    private TMisDunningOrderService tMisDunningOrderService;

    @Autowired
    private TMisDunningTaskService tMisDunningTaskService;

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

        try{
            DynamicDataSource.setCurrentLookupKey("dataSource_read");
            Page<TMisCustomerServiceFeedback> page = tMisCustomerServiceFeedbackService.feedbackList(new Page<TMisCustomerServiceFeedback>(request, response), tMisCustomerServiceFeedback);
            model.addAttribute("page", page);
            model.addAttribute("buyerId", buyerId);
            return "modules/dunning/tMisCustomerServiceFeedbackList";
        }catch (Exception e){
            logger.info("加载反馈通知截图失败,或者切换只读库查询失败",e);
            return null;
        }finally {
            DynamicDataSource.setCurrentLookupKey("dataSource");
        }


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
            DynamicDataSource.setCurrentLookupKey("dataSource_read");
            tMisCustomerServiceFeedback=tMisCustomerServiceFeedbackService.get(id);
        }catch (Exception e){
            logger.info("加载反馈通知截图失败,或者切换只读库查询失败",e);
            return null;
        }finally {
            DynamicDataSource.setCurrentLookupKey("dataSource");
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
    public String notifyList(String colorChoice, @ModelAttribute("tMisCustomerServiceFeedback") TMisCustomerServiceFeedback tMisCustomerServiceFeedback, HttpServletRequest request, HttpServletResponse response, Model model){
        if (StringUtils.isEmpty(tMisCustomerServiceFeedback.getProblemstatus())){
            tMisCustomerServiceFeedback.setProblemstatus("");
        }
        if("已解决".equals(tMisCustomerServiceFeedback.getKeyword())){
            tMisCustomerServiceFeedback.setInnerKeyWord("RESOLVED");
        }else if("未解决".equals(tMisCustomerServiceFeedback.getKeyword())){
            tMisCustomerServiceFeedback.setInnerKeyWord("UNRESOLVED");
        }else {
            tMisCustomerServiceFeedback.setInnerKeyWord("null");
        }
        try {
            DynamicDataSource.setCurrentLookupKey("dataSource_read");
            Page<TMisCustomerServiceFeedback> page = tMisCustomerServiceFeedbackService.notifyList(new Page<TMisCustomerServiceFeedback>(request, response,20), tMisCustomerServiceFeedback);
            model.addAttribute("page", page);
            model.addAttribute("color", colorChoice);
        } catch (Exception e) {
            logger.info("切换只读库查询失败", e);
            return "views/error/500";
        } finally {
            DynamicDataSource.setCurrentLookupKey("dataSource");
        }

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
            DynamicDataSource.setCurrentLookupKey("dataSource_read");
            tMisCustomerServiceFeedback=tMisCustomerServiceFeedbackService.findCodeStatusTagDesPeople(customerServiceFeedback);
            if("0".equals(tMisCustomerServiceFeedback.getReadFlag())){
                tMisCustomerServiceFeedback.setReadFlag("1");
                tMisCustomerServiceFeedbackService.updateFeedback(tMisCustomerServiceFeedback);
                model.addAttribute("custNotify", "desc");
            }
        }catch (Exception e){
            logger.info("加载反馈通知截图失败或者切换只读库查询失败",e);
            return null;
        }finally {
            DynamicDataSource.setCurrentLookupKey("dataSource");
        }
        model.addAttribute("tMisCustomerServiceFeedback", tMisCustomerServiceFeedback);
        return "modules/dunning/tMisCustomerJboxNotify";
    }

    /**
     * 如果非催收专员客服通知弹框
     * @param customerServiceFeedback
     * @param request
     * @param model
     * @return
     */
    @RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
    @RequestMapping(value ="feedbackJbox2")
    public String jboxNotify2(TMisCustomerServiceFeedback customerServiceFeedback, HttpServletRequest request, Model model){

        TMisCustomerServiceFeedback tMisCustomerServiceFeedback=null;
        try{
            DynamicDataSource.setCurrentLookupKey("dataSource_read");
            tMisCustomerServiceFeedback=tMisCustomerServiceFeedbackService.findCodeStatusTagDesPeople(customerServiceFeedback);
        }catch (Exception e){
            logger.info("加载反馈通知截图失败,或者切换只读库查询失败",e);
            return null;
        }finally {
            DynamicDataSource.setCurrentLookupKey("dataSource");
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
    public String custServiceCount(TMisCustomerServiceFeedback tMisCustomerServiceFeedback){

    	try {
    		DynamicDataSource.setCurrentLookupKey("dataSource_read");

	        tMisCustomerServiceFeedback.setReadFlag("0");
	        String userid=UserUtils.getUser().getId();
	        tMisCustomerServiceFeedback.setDunningpeopleid(userid);
            return  String.valueOf(tMisCustomerServiceFeedbackService.findCustServiceCount(tMisCustomerServiceFeedback));
		} catch (Exception e) {
			logger.info("切换只读库查询失败", e);
			return "views/error/500";
		} finally {
			DynamicDataSource.setCurrentLookupKey("dataSource");
		}
        
    }
    /**
     * 跳转到订单详情页面
     * @param tMisCallingRecord
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("dunning:tMisCustomerServiceFeedback:view")
    @RequestMapping(value = "gotoTaskOrder")
    public String gotoTask(@RequestParam("dealcode") String dealcode, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            DynamicDataSource.setCurrentLookupKey("dataSource_read");
        if (dealcode == null) {
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
        } catch (Exception e) {
            logger.info("切换只读库查询失败", e);
            return "views/error/500";
        } finally {
            DynamicDataSource.setCurrentLookupKey("dataSource");
        }
    }
}
