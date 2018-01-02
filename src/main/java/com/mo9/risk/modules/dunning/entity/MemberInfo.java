package com.mo9.risk.modules.dunning.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员信息
 * Created by cyuan on 2017/12/28.
 */
public class MemberInfo implements Serializable {

    private static final long serialVersionUID = 2835637131989745806L;

    private String mobile;//用户手机号

    private Integer numberOfTime;//剩余使用次数
    private String memberType; //会员类型
    private String useType;//会员卡状态 使用中/已失效
    private Date startTime;//会员开始时间
    private Date overdueTime;//会员过期时间
    private String remark;//备注
    private String validDate;//有效期

    //有效期
    public String getValidDate() {
        return  "bronze".equals(this.memberType.split("_")[1]) ? "7" : "30";
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    //会员类型文本
    public String getMemberTypeText(){
        return "diamond".equals(this.memberType.split("_")[0]) ? "钻石vip会员" :
                "gold".equals(this.memberType.split("_")[0]) ? "黄金vip会员" :"";

    }
    //会员状态文本
    public String getUseTypeText(){
        return "used".equals(this.useType) ? "使用中" : "已失效";
    }
    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }


    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    //会费标准
    public String getMemberStandard(){
        return "bronze".equals(this.memberType.split("_")[1])?"单次卡" :
                "silver".equals(this.memberType.split("_")[1])?"双次卡":
                "gold".equals(this.memberType.split("_")[1])?"月卡":"";
    }
    //具体会员费
    public String getMemberStandardRate(){
        return "gold_gold".equals(this.memberType)?"288" :
               "diamond_bronze".equals(this.memberType)?"199":
               "diamond_silver".equals(this.memberType)?"368":
               "diamond_gold".equals(this.memberType)?"499":"";
    }

    public Date getStartTime() {
        return startTime;
    }



    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getOverdueTime() {
        return overdueTime;
    }

    public void setOverdueTime(Date overdueTime) {
        this.overdueTime = overdueTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Integer getNumberOfTime() {
        return numberOfTime;
    }

    public void setNumberOfTime(Integer numberOfTime) {
        this.numberOfTime = numberOfTime;
    }


}
