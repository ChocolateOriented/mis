package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jxguo on 2018/1/2.
 */
public class TMisCallingQualityTest extends DataEntity<TMisCallingQualityTest> {

    private String uuid;		//uuid

    private String productCategory;  //产品类型

    private Date startTime;		//通话开始时间

    private Date endTime;		//通话结束时间

    private Date finishTime;	//呼叫完成时间

    private Integer durationTime;		//通话时长  单位：s

    private String dealcode;		//订单号

    private String peopleId;		//催收人员id

    private String peopleName;		//催收人员姓名

    private TMisCallingRecord.CallType callType;		//呼叫类型

    private String sensitiveWordNumber;  //敏感词个数

    private String sensitiveWord;    //敏感词

    private String targetNumber;		//通话号码

    private String targetName;		//通话人姓名

    private int score;               //质检分数

    private String relation;         //关系

    private String callContent;       //通话内容

    private TMisDunningPeople dunningPeople;

    private List<String> groupIds;

    private String groupId;

    public enum CallType {

        in("呼入"),
        out("呼出");

        private String desc;

        private CallType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }

    @ExcelField(title="呼叫时间",  type=1, align=2, sort=1)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @ExcelField(title="通话时长",  type=1, align=2, sort=5)
    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }
    @ExcelField(title="订单编号",  type=1, align=2, sort=6)
    public String getDealcode() {
        return dealcode;
    }

    public void setDealcode(String dealcode) {
        this.dealcode = dealcode;
    }

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }
    @ExcelField(title="通话人",  type=1, align=2, sort=4)
    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }
    @ExcelField(title="敏感词个数",  type=1, align=2, sort=6)
    public String getSensitiveWordNumber() {
        return sensitiveWordNumber;
    }

    public void setSensitiveWordNumber(String sensitiveWordNumber) {
        this.sensitiveWordNumber = sensitiveWordNumber;
    }
    @ExcelField(title="客户电话",  type=1, align=2, sort=2)
    public String getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(String targetNumber) {
        this.targetNumber = targetNumber;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAudioUrl() {
        if (this.uuid == null || this.startTime == null || this.callType == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return this.callType.toString() + "/" + dateFormat.format(this.startTime) + "/" + this.uuid + ".wav";
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getSensitiveWord() {
        return sensitiveWord;
    }

    public void setSensitiveWord(String sensitiveWord) {
        this.sensitiveWord = sensitiveWord;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    @ExcelField(title="质检分数",  type=1, align=2, sort=7)
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCallContent() {
        return callContent;
    }

    public void setCallContent(String callContent) {
        this.callContent = callContent;
    }
    @ExcelField(title="呼出类型",  type=1, align=2, sort=3)
    public TMisCallingRecord.CallType getCallType() {
        return callType;
    }

    public void setCallType(TMisCallingRecord.CallType callType) {
        this.callType = callType;
    }
}
