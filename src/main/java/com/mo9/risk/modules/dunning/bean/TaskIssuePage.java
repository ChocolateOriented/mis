package com.mo9.risk.modules.dunning.bean;

import com.alibaba.fastjson.JSONArray;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueChannel;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueStatus;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueType;
import com.mo9.risk.modules.dunning.entity.TaskIssue.RemindingType;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jxli on 2018/1/24.
 */
public class TaskIssuePage extends DataEntity<TaskIssuePage> {
	private String dealcode;
	private IssueStatus status;
	private Set<IssueType> issueTypes;//问题类型
	private Set<IssueType> handlingIssueTypes;//已解决问题
	private String handlingresult;//处理结果
	private RemindingType remindingType;//提醒类型
	private IssueChannel issueChannel;//问题来源
	private String updateRole;//更新角色
	private String description;//问题信息
	private String userName;//用户名
	private String recorderName;//创建人名
	//查询使用
	private String keyWord;//关键字
	private String dunningpeopleid;
	private List<String> groupIds;
	private IssueType issueType;
	private Date beginCreateDate;
	private Date endCreateDate;
	private boolean read;
	private String currentUserId;

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public IssueStatus getStatus() {
		return status;
	}

	public void setStatus(IssueStatus status) {
		this.status = status;
	}

	public Set<IssueType> getIssueTypes() {
		return issueTypes;
	}

	public void setIssueTypes(Set<IssueType> issueTypes) {
		this.issueTypes = issueTypes;
	}

	public Set<IssueType> getHandlingIssueTypes() {
		return handlingIssueTypes;
	}

	public void setHandlingIssueTypes(Set<IssueType> handlingIssueTypes) {
		this.handlingIssueTypes = handlingIssueTypes;
	}

	public String getHandlingresult() {
		return handlingresult;
	}

	public void setHandlingresult(String handlingresult) {
		this.handlingresult = handlingresult;
	}

	public RemindingType getRemindingType() {
		return remindingType;
	}

	public void setRemindingType(RemindingType remindingType) {
		this.remindingType = remindingType;
	}

	public IssueChannel getIssueChannel() {
		return issueChannel;
	}

	public void setIssueChannel(IssueChannel issueChannel) {
		this.issueChannel = issueChannel;
	}

	public String getUpdateRole() {
		return updateRole;
	}

	public void setUpdateRole(String updateRole) {
		this.updateRole = updateRole;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRecorderName() {
		return recorderName;
	}

	public void setRecorderName(String recorderName) {
		this.recorderName = recorderName;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getDunningpeopleid() {
		return dunningpeopleid;
	}

	public void setDunningpeopleid(String dunningpeopleid) {
		this.dunningpeopleid = dunningpeopleid;
	}

	public List<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	public IssueType getIssueType() {
		return issueType;
	}

	public void setIssueType(IssueType issueType) {
		this.issueType = issueType;
	}

	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}

	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate =  null != endCreateDate ? DateUtils.endDate(endCreateDate) : endCreateDate;
	}

	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	//IssueTypesJSON转set
	public void setIssueTypesByJson(String issueTypesJson) {
		List<String> issueStatuses = JSONArray.parseArray(issueTypesJson,String.class);
		this.issueTypes = new HashSet<>(issueStatuses.size());
		for (String issueStatuse : issueStatuses) {
			try {
				IssueType type = IssueType.valueOf(issueStatuse);
				issueTypes.add(type);
			}catch (Exception e){
				continue;
			}
		}
	}

	public void setHandlingIssueTypesByJson(String issueTypesJson) {
		List<String> issueStatuses = JSONArray.parseArray(issueTypesJson,String.class);
		this.handlingIssueTypes = new HashSet<>(issueStatuses.size());
		for (String issueStatuse : issueStatuses) {
			try {
				IssueType type = IssueType.valueOf(issueStatuse);
				handlingIssueTypes.add(type);
			}catch (Exception e){
				continue;
			}
		}
	}
}
