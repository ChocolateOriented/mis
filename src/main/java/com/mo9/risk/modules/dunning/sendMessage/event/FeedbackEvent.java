package com.mo9.risk.modules.dunning.sendMessage.event;

import com.mo9.risk.modules.dunning.sendMessage.dao.EventType;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by qtzhou on 2017/11/9.
 * 回传消息事件
 */
public class FeedbackEvent {

    /**
     * 事件Id
     */
    @NotBlank(message = "事件Id不能为空")
    private String eventId;

    /**
     * 事件发送时间
     */
    @NotNull(message = "事件发生时间不能为空")
    private Date eventTime;

    /**
     * 反馈记录id
     */
    @NotNull(message = "问题id不能为空")
    private String feedbackId;

    @NotNull(message = "处理结果不能为空")
    private String handlingresult;

    /**
     * 反馈记录人
     */
    @NotNull(message = "操作人不能为空")
    private String createBy;

    /**
     * 反馈记录更新时间
     */
    @NotNull(message = "问题解决时间不能为空")
    private Date createTime;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy= createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getEventType() {
        return EventType.EVENT_TYPE_FEEDBACK;
    }

    public String getHandlingresult() {
        return handlingresult;
    }

    public void setHandlingresult(String handlingresult) {
        this.handlingresult = handlingresult;
    }

}
