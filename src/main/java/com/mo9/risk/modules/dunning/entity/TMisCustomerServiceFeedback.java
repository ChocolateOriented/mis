package com.mo9.risk.modules.dunning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 客服问题Entity
 * Created by qtzhou on 2017/10/26.
 */
public class TMisCustomerServiceFeedback extends DataEntity<TMisCustomerServiceFeedback> {

    private static final long serialVersionUID = 1L;

    private String dealcode; // 订单编号
    private String ordertype; // 订单类型
    private Boolean orderstatus; // 当前催收时的订单状态
    private Boolean problemstatus;//客服反馈的问题状态
    private String hashtag;//推送标签类别
    private String problemdescriotion;//问题描述
    private Date dunningtime;//操作时间
    private String pushpeople;//推送人
    private String operate;//操作
    private String handlingresult;//处理结果
    private String roodealcode;//主订单编号

    @Length(min=1, max=64, message="催收订单号长度必须介于 1 和 64 之间")
    public String getDealcode() {
        return dealcode;
    }

    public void setDealcode(String dealcode) {
        this.dealcode = dealcode;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public Boolean getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(Boolean orderstatus) {
        this.orderstatus = orderstatus;
    }

    public Boolean getProblemstatus() {
        return problemstatus;
    }

    public void setProblemstatus(Boolean problemstatus) {
        this.problemstatus = problemstatus;
    }

    @Length(min=1, max=128, message="催收订单号长度必须介于 1 和 128 之间")
    public String getHashTag() {
        return hashtag;
    }

    public void setHashTag(String hashtag) {
        this.hashtag = hashtag;
    }

    @Length(min=1, max=256, message="催收订单号长度必须介于 1 和 256 之间")
    public String getProblemdescriotion() {
        return problemdescriotion;
    }

    public void setProblemdescriotion(String problemdescriotion) {
        this.problemdescriotion = problemdescriotion;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDunningtime() {
        return dunningtime;
    }

    public void setDunningtime(Date dunningtime) {
        this.dunningtime = dunningtime;
    }

    @Length(min=1, max=128, message="催收订单号长度必须介于 1 和 128 之间")
    public String getPushpeople() {
        return pushpeople;
    }


    public void setPushpeople(String pushpeople) {
        this.pushpeople = pushpeople;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    @Length(min=0, max=1000, message="处理结果长度必须介于 0 和 1000 之间")
    public String getHandlingresult() {
        return handlingresult;
    }

    public void setHandlingresult(String handlingresult) {
        this.handlingresult = handlingresult;
    }

    @Length(min=1, max=128, message="催收订单号长度必须介于 1 和 128 之间")
    public String getRoodealcode() {
        return roodealcode;
    }

    public void setRoodealcode(String roodealcode) {
        this.roodealcode = roodealcode;
    }
}
