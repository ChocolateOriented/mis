package com.mo9.risk.modules.dunning.bean.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;

/**
 * 接收客服问题消息对象
 * Created by qtzhou on 2017/11/3.
 */
public class TMisCustomerServicefeedbackDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String feedbackRecordId;//问题id
    private String loanDealCode;//订单编号
    private String loanOrderType;//订单类型
    private String loanStatus;//订单状态
    private String feedbackStatus;//问题状态
    private String labels;//推送标签
    private String description;//问题描述
    private String userName;//用户名
    private Date   eventId;//消息推送时间


    private String recorderName;//推送人

    public TMisCustomerServicefeedbackDto() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotEmpty(message = "问题ID不能为空")
    @Length(min=1, max=20, message="问题ID长度必须介于 1 和 20 之间")
    public String getFeedbackRecordId() {
        return feedbackRecordId;
    }

    public void setFeedbackRecordId(String feedbackRecordId) {
        this.feedbackRecordId = feedbackRecordId;
    }

    @NotEmpty(message = "问题状态不能为空")
    @Length(min=1, max=128, message="问题状态长度必须介于 1 和 128 之间")
    public String getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(String feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @NotEmpty(message = "问题推送人不能为空")
    @Length(min=1, max=128, message="问题推送人长度必须介于 1 和 128 之间")
    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    @NotEmpty(message = "订单编号不能为空")
    @Length(min=1, max=128, message="订单编号长度必须介于 1 和 128 之间")
    public String getLoanDealCode() {
        return loanDealCode;
    }

    public void setLoanDealCode(String loanDealCode) {
        this.loanDealCode = loanDealCode;
    }

    @NotEmpty(message = "订单类型不能为空")
    @Length(min=1, max=128, message="订单类型长度必须介于 1 和 128 之间")
    public String getLoanOrderType() {
        return loanOrderType;
    }

    public void setLoanOrderType(String loanOrderType) {
        this.loanOrderType = loanOrderType;
    }

    @NotEmpty(message = "订单状态不能为空")
    @Length(min=1, max=128, message="订单状态长度必须介于 1 和 128 之间")
    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    @Override
    public String toString() {
        return "TMisCustomerServicefeedbackDto{" +
                ",feedbackRecordId=" + feedbackRecordId +'\'' +
                ",loanDealCode=" + loanDealCode +'\'' +
                ",loanOrderType=" + loanOrderType +'\'' +
                ",loanStatus=" + loanStatus +'\'' +
                ",description='" + description + '\'' +
                ",feedbackStatus='" + feedbackStatus + '\'' +
                ",labels='" + labels + '\'' +
                ",recorderName='" + recorderName + '\'' +
                ",userName='" + userName + '\'' +
                ",eventId='" + eventId + '\'' +
                '}';
    }

    @NotEmpty(message = "用户名不能为空")
    @Length(min=1, max=128, message="用户名长度必须介于 1 和 128 之间")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getEventId() {
        return eventId;
    }

    public void setEventId(Date eventId) {
        this.eventId = eventId;
    }
}
