package com.mo9.risk.modules.dunning.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.thinkgem.jeesite.modules.sys.entity.User;
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
    private String updateBy;

    /**
     * 反馈记录更新时间
     */
    @NotNull(message = "问题解决时间不能为空")
    private Date updateDate;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }


    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getHandlingresult() {
        return handlingresult;
    }

    public void setHandlingresult(String handlingresult) {
        this.handlingresult = handlingresult;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

}
