/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mo9.risk.modules.dunning.bean.CallCenterCallinInfo;
import com.mo9.risk.modules.dunning.bean.CallCenterCalloutInfo;
import com.thinkgem.jeesite.common.persistence.DataEntity;

public class TMisCallingRecord extends DataEntity<TMisCallingRecord> {

	private static final long serialVersionUID = 1L;

	private String dbid;		//dbid

	private String agent;		//坐席

	private String peopleId;		//催收人员id

	private String peopleName;		//催收人员姓名

	private CallType callType;		//呼叫类型

	private String extensionNumber;		//分机号码

	private String targetNumber;		//通话号码

	private String sessionId;		//呼叫sessionId

	private Date callTime;	//呼叫创建时间

	private Date ringTime;	//响铃时间

	private Date startTime;		//通话开始时间

	private Date endTime;		//通话结束时间

	private Date finishTime;	//呼叫完成时间

	private Integer durationTime;		//通话时长  单位：s

	private String audioId;		//音频文件id

	private String dealcode;		//订单号

	private Date beginCallTime;

	private Date endCallTime;

	private TMisDunningPeople dunningPeople;

	private List<String> groupIds;

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

	public TMisCallingRecord() {
		super();
	}

	public TMisCallingRecord(String id){
		super(id);
	}

	public TMisCallingRecord(CallCenterCallinInfo callinInfo){
		this.agent = callinInfo.getAgent();
		this.callType = CallType.in;
		this.extensionNumber = callinInfo.getExtension();
		this.sessionId = callinInfo.getSessionid();
		this.targetNumber = callinInfo.getCaller();
		Long create = callinInfo.getInQueueTime();
		Long ring = callinInfo.getAgentOfferTime();
		Long start = callinInfo.getAgentStartTime();
		Long end = callinInfo.getAgentEndTime();
		Long finish = callinInfo.getOutQueueTime();
		this.callTime = create == null || create == 0 ? null : new Date(create * 1000);
		this.ringTime = ring == null || ring == 0 ? null : new Date(ring * 1000);
		this.startTime = start == null || start == 0 ? null : new Date(start * 1000);
		this.endTime = end == null || end == 0 ? null : new Date(end * 1000);
		this.finishTime = finish == null || finish == 0 ? null : new Date(finish * 1000);
		this.durationTime = start == null || end == null ? 0 : (int) (end - start);
		this.audioId = callinInfo.getSessionid();
	}

	public TMisCallingRecord(CallCenterCalloutInfo calloutInfo){
		this.agent = calloutInfo.getAgent();
		this.callType = CallType.out;
		this.extensionNumber = calloutInfo.getExtension();
		this.sessionId = calloutInfo.getCustomerno();
		this.targetNumber = calloutInfo.getTarget();
		Long create = calloutInfo.getChannelCreateTime();
		Long ring = create;
		Long start = calloutInfo.getChannelAnswerTime();
		Long end = calloutInfo.getChannelHangupTime();
		Long finish = end;
		this.callTime = create == null || create == 0 ? null : new Date(create * 1000);
		this.ringTime = ring == null || ring == 0 ? null : new Date(ring * 1000);
		this.startTime = start == null || start == 0 ? null : new Date(start * 1000);
		this.endTime = end == null || end == 0 ? null : new Date(end * 1000);
		this.finishTime = finish == null || finish == 0 ? null : new Date(finish * 1000);
		this.durationTime = start == null || end == null ? 0 : (int) (end - start);
		this.audioId = calloutInfo.getEuuid();
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public CallType getCallType() {
		return callType;
	}

	public void setCallType(CallType callType) {
		this.callType = callType;
	}

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getTargetNumber() {
		return targetNumber;
	}

	public void setTargetNumber(String targetNumber) {
		this.targetNumber = targetNumber;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public String getCallTimeText() {
		if (this.callTime == null) {
			return "";
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		if (c.getTime().compareTo(this.callTime) <= 0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			return "今天 " + dateFormat.format(this.callTime);
		}
		c.add(Calendar.DATE, -1);
		if (c.getTime().compareTo(this.callTime) <= 0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			return "昨天 " + dateFormat.format(this.callTime);
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(this.callTime);
	}

	public Date getRingTime() {
		return ringTime;
	}

	public void setRingTime(Date ringTime) {
		this.ringTime = ringTime;
	}

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

	public Integer getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(Integer durationTime) {
		this.durationTime = durationTime;
	}

	public String getDurationTimeText() {
		if (this.durationTime == null) {
			return "";
		}
		
		int hour = this.durationTime / 3600;
		int minute = this.durationTime % 3600 / 60;
		int second = this.durationTime % 3600 % 60;
		
		String timeText = "";
		if (hour > 0) {
			timeText = hour + "时";
		}
		if (minute > 0) {
			timeText = timeText + minute + "分";
		}
		if (second > 0) {
			timeText = timeText + second + "秒";
		}
		return timeText;
	}

	public String getCallingState() {
		if (this.callType == null) {
			return "";
		}
		if (this.startTime != null) {
			return "接通";
		}
		if (this.callType == CallType.out) {
			return "未接";
		}
		if (this.callType == CallType.in) {
			return this.ringTime == null ? "队列中放弃" : "坐席未接";
		}
		return "";
	}

	public String getAudioId() {
		return audioId;
	}

	public void setAudioId(String audioId) {
		this.audioId = audioId;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getAudioUrl() {
		if (this.audioId == null || this.startTime == null || this.callType == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return this.callType.toString() + "/" + dateFormat.format(this.startTime) + "/" + this.audioId + ".wav";
	}

	public Date getBeginCallTime() {
		return beginCallTime;
	}

	public void setBeginCallTime(Date beginCallTime) {
		this.beginCallTime = beginCallTime;
	}

	public Date getEndCallTime() {
		return endCallTime;
	}

	public void setEndCallTime(Date endCallTime) {
		this.endCallTime = endCallTime;
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

}