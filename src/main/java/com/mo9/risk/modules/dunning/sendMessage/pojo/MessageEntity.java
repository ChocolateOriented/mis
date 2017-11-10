package com.mo9.risk.modules.dunning.sendMessage.pojo;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.mo9.risk.modules.dunning.sendMessage.MessageStatusEnum;
import com.mo9.risk.modules.dunning.sendMessage.dao.IEntity;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * Created by qtzhou on 2017/11/9.
 * 消息实体
 */
public class MessageEntity  extends DataEntity<MessageEntity> implements IEntity, Serializable {


    private String messageId;


    private String messageKey;


    private String topic;

    private String tag;

    private String message;

    private MessageStatusEnum status;

    private int times;

    private Date nextSendTime;

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnum status) {
        this.status = status;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public Date getNextSendTime() {
        return nextSendTime;
    }

    public void setNextSendTime(Date nextSendTime) {
        this.nextSendTime = nextSendTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
