package com.mo9.risk.modules.dunning.sendMessage;
import com.mo9.mqclient.MqMessage;
import com.mo9.mqclient.MqSendResult;
import com.mo9.mqclient.impl.aliyun.AliyunMqProducer;
import com.mo9.risk.modules.dunning.sendMessage.pojo.MessageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by qtzhou on 2017/11/9.
 */
@Service
public class FeedbackMessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackMessageProducer.class);

    private AliyunMqProducer producer;

    /**
     * 阿里云MQ服务配置
     */
    @Value("${aliyun.mq.onsaddr}")
    private String onsAddr;

    @Value("${aliyun.mq.accesskey}")
    private String accessKey;

    @Value("${aliyun.mq.secretkey}")
    private String secretKey;

    /**
     * feedback创建状态广播producerid
     */
    @Value("${aliyun.mq.feedback.pid}")
    private String feedbackProducerId;

    @Value("${aliyun.mq.send.timeout}")
    private int sendTimeoutMills;

    /*@PostConstruct
    public void init()
    {
        logger.info("初始化消息producer...........................");
        this.producer = new AliyunMqProducer();
        producer.setProductId(this.feedbackProducerId);
        producer.setAccessKey(this.accessKey);
        producer.setSecretKey(this.secretKey);
        producer.setOnsAddr(this.onsAddr);
        producer.setSendMsgTimeoutMillis(sendTimeoutMills);
        this.producer.init();
    }*/

    public String sendMessage (MessageEntity message) {
        MqSendResult sendResult;
        String messageId = null;
        try {
            MqMessage mqMessage = new MqMessage(message.getTopic(), message.getTag(), message.getMessageKey(), message.getMessage());
            sendResult = this.producer.send(mqMessage);
            messageId = sendResult.getMessageId();
            logger.info("发送消息成功，消息ID:{},topic:{},tag:{},消息内容:{}", messageId, message.getTopic(), message.getTag(), message.getMessage());
        } catch (Exception e) {
            logger.error("发送消息异常，消息ID:{},topic:{},tag:{},消息内容:{}", messageId, message.getTopic(), message.getTag(), message.getMessage());
        }
        return messageId;
    }
}
