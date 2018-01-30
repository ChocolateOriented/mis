package com.mo9.risk.modules.dunning.service;

import com.alibaba.fastjson.JSON;
import com.gamaxpay.commonutil.msf.BaseResponse;
import com.mo9.risk.modules.dunning.entity.FeedbackEvent;
import com.mo9.risk.modules.dunning.entity.MisMqMessage;
import com.mo9.risk.modules.dunning.entity.TaskIssue;
import com.thinkgem.jeesite.common.service.BaseService;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by qtzhou on 2017/11/9.
 * 问题处理结果消息回传
 */
public class FeedbackSendService extends BaseService {

    private MisMqMessageService misMqMessageService;
    private String feedbackTopic;
    private String feedbackTag;

    @Transactional
    public BaseResponse createFeedBackRecord(TaskIssue taskIssue) {
        BaseResponse response = new BaseResponse();

        /**
         * 发送客服消息
         * 新建事件
         */
        FeedbackEvent event =this.newFeedbackEvent(taskIssue);

        /**
         * 新建消息
         */
        Date now = new Date();
        MisMqMessage misMqMessage=new MisMqMessage();
        misMqMessage.setKey( DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
        misMqMessage.setBody(JSON.toJSONString(event));
        misMqMessage.setTopic(feedbackTopic);
        misMqMessage.setTag(feedbackTag);
        misMqMessage.setCreateTime(now);
        misMqMessage.setUpdateTime(now);
        String key=misMqMessage.getKey();
        String body=misMqMessage.getBody();
        misMqMessageService.send(feedbackTopic,feedbackTag,key,body);

        return response;
    }

    private FeedbackEvent newFeedbackEvent (TaskIssue taskIssue) {

        FeedbackEvent event = new FeedbackEvent();
        Date now = new Date();
        event.setEventId(DateFormatUtils.format(now,"yyyy-MM-dd HH:mm:ss"));
        event.setFeedbackId(taskIssue.getId());
        event.setHandlingresult(taskIssue.getHandlingResult());
        event.setUpdateBy(taskIssue.getUpdateBy().getName());
        event.setUpdateDate(taskIssue.getUpdateDate());
        return event;
    }

    public void setMisMqMessageService(MisMqMessageService misMqMessageService) {
        this.misMqMessageService = misMqMessageService;
    }

    public void setFeedbackTopic(String feedbackTopic) {
        this.feedbackTopic = feedbackTopic;
    }

    public void setFeedbackTag(String feedbackTag) {
        this.feedbackTag = feedbackTag;
    }
}

