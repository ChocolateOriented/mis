package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

import java.util.Date;
import java.util.List;

/**
 * Created by jxguo on 2017/12/18.
 */
public class DunningPhoneDetailReportFile extends DataEntity<com.mo9.risk.modules.dunning.entity.DunningPhoneDetailReportFile>{

        private String logiName;
        //日期
        private String dateTime;
        //小组
        private String groupName;
        //姓名
        private String dunningName;
        //坐席
        private String agent;
        //花名
        private String nickname;
        //机构
        private String department;
        //队列
        private String queue;
        //签入时间
        private String firstCheckIn;
        //签出时间
        private String lastCheckOut;
        //在线时长
        private String ontime;
        //小休时长
        private String breaktime = "0";
        //拨打电话量
        private String callingAmount = "0";
        //处理案件量
        private String dealCaseAmount = "0";
        //接通量
        private String connectAmout = "0";

        //通话时长
        private String callDuration = "0";

        //查询结束时间
        private Date datetimestart;
        //查询截止时间
        private Date datetimeend;

        //查询在线时长start
        private String onTimeStart;

        //查询在线时长end
        private String onTimeEnd;

        //查询小休时长start
        private String breaktimeStart;

        //查询小休时长end
        private String breaktimeEnd;

        //查询通话时长start
        private String callDurationStart;

        //查询通话时长end
        private String callDurationEnd;

        //自动分案
        private String auto;


        private TMisDunningGroup group;

        private String groupId;

        private List<String> peopleIds;

        private String peopleName;

        private String peopleId;

        //分机号
         private String extension;

        @ExcelField(title = "日期", type = 1, align = 2, sort = 1)
        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
        @ExcelField(title = "小组", type = 1, align = 2, sort = 2)
        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        @ExcelField(title = "姓名", type = 1, align = 2, sort = 3)
        public String getDunningName() {
            return dunningName;
        }

        public void setDunningName(String dunningName) {
            this.dunningName = dunningName;
        }
        @ExcelField(title = "坐席ID", type = 1, align = 2, sort = 4)
        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        @ExcelField(title = "花名", type = 1, align = 2, sort = 5)
        public String getExtension() {
            return extension;
        }

    public void setExtension(String extension) {
        this.extension = extension;
    }


        @ExcelField(title = "机构", type = 1, align = 2, sort = 6)
        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }
        @ExcelField(title = "队列", type = 1, align = 2, sort = 7)
        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }
        @ExcelField(title = "签入时间", type = 1, align = 2, sort = 8)
        public String getFirstCheckIn() {
            return firstCheckIn;
        }

        public void setFirstCheckIn(String firstCheckIn) {
            this.firstCheckIn = firstCheckIn;
        }

        @ExcelField(title = "签出时间", type = 1, align = 2, sort = 9)
        public String getLastCheckOut() {
            return lastCheckOut;
        }

        public void setLastCheckOut(String lastCheckOut) {
            this.lastCheckOut = lastCheckOut;
        }
        @ExcelField(title = "在线时长", type = 1, align = 2, sort = 10)
        public String getOntime() {
            return ontime;
        }

        public void setOntime(String ontime) {
            this.ontime = ontime;
        }
        @ExcelField(title = "小休时长", type = 1, align = 2, sort = 11)
        public String getBreaktime() {
            return breaktime;
        }

        public void setBreaktime(String breaktime) {
            this.breaktime = breaktime;
        }
        @ExcelField(title = "拨打电话量", type = 1, align = 2, sort = 12)
        public String getCallingAmount() {
            return callingAmount;
        }

        public void setCallingAmount(String callingAmount) {
            this.callingAmount = callingAmount;
        }
        @ExcelField(title = "处理案件量", type = 1, align = 2, sort = 13)
        public String getDealCaseAmount() {
            return dealCaseAmount;
        }

        public void setDealCaseAmount(String dealCaseAmount) {
            this.dealCaseAmount = dealCaseAmount;
        }
        @ExcelField(title = "接通量", type = 1, align = 2, sort = 14)
        public String getConnectAmout() {
            return connectAmout;
        }

        public void setConnectAmout(String connectAmout) {
            this.connectAmout = connectAmout;
        }
        @ExcelField(title = "通话时长", type = 1, align = 2, sort = 15)
        public String getCallDuration() {
            return callDuration;
        }

        public void setCallDuration(String callDuration) {
            this.callDuration = callDuration;
        }

        public Date getDatetimestart() {
            return datetimestart;
        }

        public void setDatetimestart(Date datetimestart) {
            this.datetimestart = datetimestart;
        }

        public Date getDatetimeend() {
            return datetimeend;
        }

        public void setDatetimeend(Date datetimeend) {
            this.datetimeend = datetimeend;
        }

        public TMisDunningGroup getGroup() {
            return group;
        }

        public void setGroup(TMisDunningGroup group) {
            this.group = group;
        }

        public String getOnTimeStart() {
            return onTimeStart;
        }

        public void setOnTimeStart(String onTimeStart) {
            this.onTimeStart = onTimeStart;
        }

        public String getOnTimeEnd() {
            return onTimeEnd;
        }

        public void setOnTimeEnd(String onTimeEnd) {
            this.onTimeEnd = onTimeEnd;
        }

        public String getBreaktimeStart() {
            return breaktimeStart;
        }

        public void setBreaktimeStart(String breaktimeStart) {
            this.breaktimeStart = breaktimeStart;
        }

        public String getBreaktimeEnd() {
            return breaktimeEnd;
        }

        public void setBreaktimeEnd(String breaktimeEnd) {
            this.breaktimeEnd = breaktimeEnd;
        }

        public String getCallDurationStart() {
            return callDurationStart;
        }

        public void setCallDurationStart(String callDurationStart) {
            this.callDurationStart = callDurationStart;
        }

        public String getCallDurationEnd() {
            return callDurationEnd;
        }

        public void setCallDurationEnd(String callDurationEnd) {
            this.callDurationEnd = callDurationEnd;
        }

        public String getLogiName() {
            return logiName;
        }

        public void setLogiName(String logiName) {
            this.logiName = logiName;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public List<String> getPeopleIds() {
            return peopleIds;
        }

        public void setPeopleIds(List<String> peopleIds) {
            this.peopleIds = peopleIds;
        }

        public String getPeopleName() {
            return peopleName;
        }

        public void setPeopleName(String peopleName) {
            this.peopleName = peopleName;
        }

        public String getPeopleId() {
            return peopleId;
        }

        public void setPeopleId(String peopleId) {
            this.peopleId = peopleId;
        }

        public String getAuto() {
            return auto;
        }

        public void setAuto(String auto) {
            this.auto = auto;
        }

}
