package com.mo9.risk.modules.dunning.sendMessage;
import com.mo9.risk.modules.dunning.sendMessage.pojo.MessageEntity;
import com.mo9.risk.modules.dunning.sendMessage.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
/**
 * Created by qtzhou on 2017/11/9.
 * 消息重发任务
 */
@Component
public class FeedBackMessageResendTask {

    private static final Logger logger = LoggerFactory.getLogger(FeedBackMessageResendTask.class);

    @Autowired
    MessageService messageService;

    @Autowired
    FeedbackMessageProducer feedbackMessageProducer;

    /**
     *
     * 每10分钟查询一次
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void feedBackMessageResendTask () {
        logger.info("开始执行问题处理结果推送消息重发定时任务.................................");

        List<MessageEntity> messages = messageService.getResendMessages();

        Date now = new Date();
        if (messages != null && !messages.isEmpty()) {
            logger.info("待重发消息共[{}]条.................................", messages.size());
            for (MessageEntity message : messages) {
                String messageId = feedbackMessageProducer.sendMessage(message);
                /**
                 * 消息发送后处理
                 */
                MessageEntity updateMessage = new MessageEntity();
                updateMessage.setId(message.getId());
                /**
                 * 发送次数更新
                 */
                updateMessage.setTimes(message.getTimes() + 1);
                /**
                 * 下次发送时间更新
                 */
                Date nextSendTime = messageService.generateNextSendTime(now, message.getTimes());
                updateMessage.setNextSendTime(nextSendTime);
                /**
                 * messageId 部位空表示消息发送成功
                 */
                if (messageId != null) {
                    updateMessage.setMessageId(messageId);
                    updateMessage.setStatus(MessageStatusEnum.SUCCESS);
                }

                /**
                 * 保存消息
                 */
                messageService.updateMessage(updateMessage);
            }
        } else {
            logger.info("待重发消息共[0]条.................................");
        }
        logger.info("结束执行问题处理结果推送消息重发定时任务.................................");
    }
}
