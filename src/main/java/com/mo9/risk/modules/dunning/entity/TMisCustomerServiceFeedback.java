package com.mo9.risk.modules.dunning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 客服问题Entity
 * Created by qtzhou on 2017/10/26.
 */
public class TMisCustomerServiceFeedback extends DataEntity<TMisCustomerServiceFeedback> {

    private static final long serialVersionUID = 1L;

    public static final String PROBLEM_STATUS_SOLVED = "solved"; //已解决
    public static final String PROBLEM_STATUS_UNSOLVED = "unsolved";  //未解决

    private String dealcode; // 订单编号
    private String ordertype; // 订单类型
    private String orderstatus; // 当前催收时的订单状态
    private String problemstatus;//客服反馈的问题状态 solved 已解决 unsolved 未解决
    private String hashtag;//推送标签类别
    private String problemdescriotion;//问题描述
    private String pushpeople;//推送人
    private String operate;//操作
    private String handlingresult;//处理结果
    private String rootorderid;//主订单编号
    private TRiskOrder tRiskOrder;

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

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getProblemstatus() {
        return problemstatus;
    }

    @ExcelField(title="订单状态", type=1, align=2, sort=9)
    public String getStatusText() {
        return PROBLEM_STATUS_SOLVED.equals(this.problemstatus) ?  "已解决" :
                PROBLEM_STATUS_UNSOLVED.equals(this.problemstatus) ?  "未解决" : "";
    }

    public void setProblemstatus(String problemstatus) {
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
    public String getRootorderid() {
        return rootorderid;
    }

    public void setRootorderid(String rootorderid) {
        this.rootorderid = rootorderid;
    }

    public TRiskOrder gettRiskOrder() {
        return tRiskOrder;
    }

    public void settRiskOrder(TRiskOrder tRiskOrder) {
        this.tRiskOrder = tRiskOrder;
    }
}
