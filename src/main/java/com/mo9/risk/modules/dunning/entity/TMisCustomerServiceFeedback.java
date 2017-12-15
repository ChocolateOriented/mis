package com.mo9.risk.modules.dunning.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

/**
 * 客服问题Entity
 * Created by qtzhou on 2017/10/26.
 */
public class TMisCustomerServiceFeedback extends DataEntity<TMisCustomerServiceFeedback> {

    private static final long serialVersionUID = 1L;

    public static final String RESOLVED = "RESOLVED"; //已解决
    public static final String UNRESOLVED = "UNRESOLVED";  //未解决
    public static final String ORDER_DEDUCT="ORDER_DEDUCT";//订单代扣
    public static final String WRITE_OFF="WRITE_OFF";//催销账
    public static final String COMPLAIN_SHAKE="COMPLAIN_SHAKE"; //投诉催收
    public static final String CONSULT_REPAY="CONSULT_REPAY";//协商还款
    public static final String CONTACT_REMARK="CONTACT_REMARK" ;//备注联系方式
    public static final String pending="pending";//待完成
    public static final String lending="lending";//待放款
    public static final String payment="payment";//待还款(未还清)
    public static final String overdue="overdue";//逾期
    public static final String payoff="payoff";//已还清 完成
    public static final String cancel="cancel";//订单取消 完成
    public static final String reject="reject";//拒绝 完成
    public static final String loan="loan";//普通订单
    public static final String loan_partial="loan_partial";//普通订单(部分)
    public static final String partial="partial";//部分还款订单

    private String dealcode; // 订单编号
    private String type; // 订单类型
    private String status; // 当前催收时的订单状态
    private String problemstatus;//客服反馈的问题状态 solved 已解决 unsolved 未解决
    private String hashtag;//推送标签类别
    private String problemdescription;//问题描述
        private String pushpeople;//推送人
    private String operate;//操作
    private String handlingresult;//处理结果
    private Integer rootorderid;//主订单编号
    private String uname;//用户名
    private Date pushTime;//消息推送时间
    private Date nearPushTime;//最近推送时间
    private Date farPushTime;//之前推送时间
    private String keyword;//页面关键字查询
    private Integer buyerId;//订单用户ID
    private String dunningpeopleid;//催收人员id
    private String nickname;//操作人花名
    private TMisDunningPeople dunningPeople;
    private List<String> groupIds;
    private String readFlag;	// 本人阅读状态
    @Length(min=1, max=64, message="催收订单号长度必须介于 1 和 64 之间")
    public String getDealcode() {
        return dealcode;
    }

    public void setDealcode(String dealcode) {
        this.dealcode = dealcode;
    }

    public String getType() {
        return type;
    }

    @ExcelField(title="订单类型", type=1, align=2, sort=9)
    public String getOrderTypeText() {
        return loan.equals(this.type) ?  "普通订单" :
                loan_partial.equals(this.type) ?  "部分还款订单" :
                partial.equals(this.type) ?  "部分还款订单" :
                        "";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderStatusText() {
        return payoff.equals(this.status) ?  "已还清" :
                payment.equals(this.status) ?  "未还清" :
                overdue.equals(this.status) ?  "未还清" :
                partial.equals(this.status) ?  "未还清" :
                pending.equals(this.status) ?  "" :
                lending.equals(this.status) ?  "" :
                cancel.equals(this.status) ?  "" :
                reject.equals(this.status) ?  "" :
                        "";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProblemstatus() {
        return problemstatus;
    }

    @ExcelField(title="问题状态", type=1, align=2, sort=9)
    public String getStatusText() {
        return RESOLVED.equals(this.problemstatus) ?  "已解决" :
                UNRESOLVED.equals(this.problemstatus) ?  "未解决" : "";
    }

    public void setProblemstatus(String problemstatus) {
        this.problemstatus = problemstatus;
    }

    @Length(min=1, max=128, message="催收订单号长度必须介于 1 和 128 之间")
    public String getHashtag() {
        return hashtag;
    }

    @ExcelField(title="标签类型", type=1, align=2, sort=9)
    public String getTagText() {
        List<String> tagList = JSON.parseObject(this.hashtag, new TypeReference<List<String>>() {});
        if (tagList == null || tagList.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String tag : tagList) {
            String tagText = ORDER_DEDUCT.equals(tag) ? "订单代扣" :
                    WRITE_OFF.equals(tag) ?  "催销账" :
                            COMPLAIN_SHAKE.equals(tag) ? "投诉催收" :
                                    CONSULT_REPAY.equals(tag) ? "协商还款" :
                                            CONTACT_REMARK.equals(tag) ? "备注联系方式" : "";
            builder.append(tagText).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    @Length(min=1, max=256, message="催收订单号长度必须介于 1 和 256 之间")
    public String getProblemdescription() {
        return problemdescription;
    }

    public void setProblemdescription(String problemdescription) {
        this.problemdescription = problemdescription;
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
    public Integer getRootorderid() {
        return rootorderid;
    }

    public void setRootorderid(Integer rootorderid) {
        this.rootorderid = rootorderid;
    }


    @Length(min=1, max=128, message="用户长度必须介于 1 和 128 之间")
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public void setNearPushTime(Date nearPushTime) {
        this.nearPushTime = null != nearPushTime ? DateUtils.endDate(nearPushTime) : nearPushTime;
    }

    public Date getNearPushTime() {
        return nearPushTime;
    }

    public Date getFarPushTime() {
        return farPushTime;
    }

    public void setFarPushTime(Date farPushTime) {
        this.farPushTime = farPushTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeywordText(String uname,String dealcode,String tagText,String statusText,String pushpeople) {
        this.keyword = uname+","+dealcode+","+tagText+","+statusText+","+pushpeople;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public TMisDunningPeople getDunningPeople() {
        return dunningPeople;
    }

    public void setDunningPeople(TMisDunningPeople dunningPeople) {
        this.dunningPeople = dunningPeople;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public void setDunningpeopleid(String dunningpeopleid) {
        this.dunningpeopleid = dunningpeopleid;
    }

    public String getDunningpeopleid() {
        return dunningpeopleid;
    }

    @Length(min=0, max=1, message="阅读标记（0：未读；1：已读）长度必须介于 0 和 1 之间")
    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
