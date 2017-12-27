package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.validator.constraints.Length;

/**
 * @Description 催收小组,区分不同性质的催收人员
 * @author LiJingXiang
 * @version 2017年4月11日
 */
public class TMisDunningGroup extends DataEntity<TMisDunningGroup> {

	private static final long serialVersionUID = 1L;

	public static final Map<GroupType, String> groupTypes;

	static {
		groupTypes = new HashMap<GroupType, String>(GroupType.values().length);
		for (GroupType groupType : GroupType.values()) {
			groupTypes.put(groupType, groupType.desc);
		}
	}

	private Integer dbid;
	private String name; // 组名
	private GroupType type; // 组类型
	private User leader; // 组长
	private User supervisor; //监理

	private List<GroupType> queryTypes;
	
	private List<String> groupIds;

	private int numberOfPeople = 0;

	public TMisDunningGroup(String id) {
		super(id);
	}

	public TMisDunningGroup() {
		super();
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	@Length(min = 1, max = 64, message = "组名长度在1-64之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public GroupType getType() {
		return type;
	}

	public void setType(GroupType type) {
		this.type = type;
	}

	public List<GroupType> getQueryTypes() {
		return queryTypes;
	}

	public void setQueryTypes(List<GroupType> queryTypes) {
		this.queryTypes = queryTypes;
	}

	public User getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(User supervisor) {
		this.supervisor = supervisor;
	}

	public List<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	public int getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	@Override
	public String toString() {
		return "TMisDunningGroup [dbid=" + dbid + ", name=" + name + ", type=" + getType() + ", leader="
				+ leader + "]";
	}

	public enum GroupType {
		selfSupport("自营"),
		outsourceSeat("外包坐席"),
		outsourceCommission("委外佣金");

		GroupType(String desc) {
			this.desc = desc;
		}

		public final String desc;
	}
}
