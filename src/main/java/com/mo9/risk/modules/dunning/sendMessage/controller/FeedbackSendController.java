package com.mo9.risk.modules.dunning.sendMessage.controller;

import com.alibaba.fastjson.JSON;
import com.gamaxpay.commonutil.msf.BaseResponse;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.mo9.risk.modules.dunning.sendMessage.FeedbackMessageProducer;
import com.mo9.risk.modules.dunning.sendMessage.MessageStatusEnum;
import com.mo9.risk.modules.dunning.sendMessage.event.FeedbackEvent;
import com.mo9.risk.modules.dunning.sendMessage.pojo.MessageEntity;
import com.mo9.risk.modules.dunning.sendMessage.service.MessageService;
import com.thinkgem.jeesite.common.service.ServiceException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by qtzhou on 2017/11/9.
 * 问题处理结果消息回传
 */
@Controller
@RequestMapping(value="/feedback")
public class FeedbackSendController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackSendController.class);

    @Autowired
    private FeedbackMessageProducer feedbackMessageProducer;

    @Autowired
    private MessageService messageService;

    @Value("${aliyun.mq.feedback.topic}")
    private String feedbackTopic;

    @Value("${aliyun.mq.feedback.tag}")
    private String feedbackTag;

    private TMisCustomerServiceFeedback feedback;

    @ResponseBody
    @RequestMapping(value = "/create_feedback", method = RequestMethod.POST)
    public BaseResponse createFeedBackRecord() {
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
                MessageEntity message = new MessageEntity();
                message.setMessageKey(event.getEventType() + "-" + DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
                message.setMessage(JSON.toJSONString(event));
                message.setTopic(feedbackTopic);
                message.setTag(feedbackTag);
                message.setCreateTime(now);
                message.setUpdateTime(now);
                message.setTimes(1);
                Date nextSendTime = messageService.generateNextSendTime(now, message.getTimes());
                message.setNextSendTime(nextSendTime);

                /**
                 * 发送消息
                 */
                String messageId = feedbackMessageProducer.sendMessage(message);

                /**
                 * 消息发送后处理
                 */
                if (messageId != null) {
                    message.setMessageId(messageId);
                    message.setStatus(MessageStatusEnum.SUCCESS);
                } else {
                    message.setStatus(MessageStatusEnum.FAILED);
                }

                /**
                 * 保存消息
                 */
                messageService.saveMessage(message);
        return response;
    }

    private FeedbackEvent newFeedbackEvent (TMisCustomerServiceFeedback feedback) {

        FeedbackEvent event = new FeedbackEvent();
        Date now = new Date();
        event.setEventId(DateFormatUtils.format(now,"yyyy-MM-dd HH:mm:ss"));
        event.setEventTime(feedback.getUpdateTime());
        event.setFeedbackId(feedback.getId());
        event.setHandlingresult(feedback.getHandlingresult());
        event.setCreateTime(feedback.getCreateTime());
        return event;
    }
}
