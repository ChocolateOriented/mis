package com.mo9.risk.modules.dunning.service;

import com.alibaba.fastjson.JSON;
import com.gamaxpay.commonutil.msf.BaseResponse;
import com.mo9.mqclient.IMqProducer;
import com.mo9.risk.modules.dunning.entity.MisMqMessage;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.mo9.risk.modules.dunning.entity.FeedbackEvent;
import com.mo9.risk.modules.dunning.service.MisMqMessageService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by qtzhou on 2017/11/9.
 * 问题处理结果消息回传
 */
public class FeedbackSendService extends MisMqMessageService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackSendService.class);

    @Autowired
    private MisMqMessageService misMqMessageService;

    @Autowired
    private IMqProducer mqProducer;

    private String feedbackTopic;

    private String feedbackTag;
    private String iMqProducer;

    public String getFeedbackTopic() {
        return feedbackTopic;
    }

    public void setFeedbackTopic(String feedbackTopic) {
        this.feedbackTopic = feedbackTopic;
    }

    public String getFeedbackTag() {
        return feedbackTag;
    }

    public void setFeedbackTag(String feedbackTag) {
        this.feedbackTag = feedbackTag;
    }

    @Transactional
    public BaseResponse createFeedBackRecord(TMisCustomerServiceFeedback feedback) {
        BaseResponse response = new BaseResponse();

        /**
         * 发送客服消息
         * 新建事件
         */
        FeedbackEvent event =this.newFeedbackEvent(feedback);

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

    private FeedbackEvent newFeedbackEvent (TMisCustomerServiceFeedback feedback) {

        FeedbackEvent event = new FeedbackEvent();
        Date now = new Date();
        event.setEventId(DateFormatUtils.format(now,"yyyy-MM-dd HH:mm:ss"));
        event.setFeedbackId(feedback.getId());
        event.setHandlingresult(feedback.getHandlingresult());
        event.setUpdateBy(feedback.getUpdateBy().getName());
        event.setUpdateDate(feedback.getUpdateDate());
        return event;
    }

    public void setIMqProducer(String IMqProducer) {
        iMqProducer = IMqProducer;
    }

    public String getIMqProducer() {
        return iMqProducer;
    }

    public void setMisMqMessageService(String MisMqMessageService) {
    }

    public MisMqMessageService getMisMqMessageService() {
        return misMqMessageService;
    }

    public IMqProducer getMqProducer() {
        return mqProducer;
    }

    public void setMqProducer(IMqProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

}

