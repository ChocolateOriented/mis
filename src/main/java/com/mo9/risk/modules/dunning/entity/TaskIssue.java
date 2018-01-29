package com.mo9.risk.modules.dunning.entity;

import com.alibaba.fastjson.JSONArray;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jxli on 2018/1/24.
 * 案件待解决问题
 */
public class TaskIssue extends DataEntity<TaskIssue> {
	private String dealcode;
	private IssueStatus status;
	private Set<IssueType> issueTypes;//问题类型
	private Set<IssueType> handlingIssueTypes;//已解决问题
	private String handlingResult;//处理结果
	private RemindingType remindingType;//提醒类型
	private IssueChannel issueChannel;//问题来源
	private String updateRole;//更新角色
	private String description;//问题信息
	private String userName;//用户名
	private String recorderName;//创建人名

	public enum IssueChannel {
		CUSTOMER_SERVICE("客服系统"),
		LOCAL_SYSTEM("系统消息");

		IssueChannel(String desc) {
			this.desc = desc;
		}

		private final String desc;

		public String getDesc() {
			return desc;
		}
	}

	public enum RemindingType {
		NONE("不提醒"),
		DUNNING_PEOPLE("提醒催收员");

		RemindingType(String desc) {
			this.desc = desc;
		}

		private final String desc;

		public String getDesc() {
			return desc;
		}
	}

	public enum IssueType {
		ORDER_DEDUCT("订单代扣"),
		WRITE_OFF("催收帐"),
		COMPLAIN_SHAKE("投诉催收"),
		CONSULT_REPAY("协商还款"),
		CONTACT_REMARK("备注联系方式"),
		RELIEF_REQUEST("减免申请");

		IssueType(String desc) {
			this.desc = desc;
		}

		private final String desc;

		public String getDesc() {
			return desc;
		}
	}

	public enum IssueStatus {
		RESOLVED("已解决"),
		UNRESOLVED("未解决");

		IssueStatus(String desc) {
			this.desc = desc;
		}

		private final String desc;

		public String getDesc() {
			return desc;
		}
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

	public String getHandlingResult() {
		return handlingResult;
	}

	public void setHandlingResult(String handlingResult) {
		this.handlingResult = handlingResult;
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

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
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

	public String getIssueTypesToJson() {
		if (issueTypes == null) {
			return null;
		}
		return JSONArray.toJSONString(issueTypes);
	}

	public String getHandlingIssueTypesToJson() {
		if (handlingIssueTypes == null) {
			return null;
		}
		return JSONArray.toJSONString(handlingIssueTypes);
	}

	public String getIssueTypes2Text(){
		if (issueTypes == null) {
			return null;
		}
		StringBuilder issueTypesText = new StringBuilder();
		for (IssueType type : issueTypes) {
		    issueTypesText.append(type.getDesc()+",");
		}
		return issueTypesText.toString();
	}
}
