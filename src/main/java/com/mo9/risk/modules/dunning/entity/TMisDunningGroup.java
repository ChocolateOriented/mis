package com.mo9.risk.modules.dunning.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * @Description 催收小组,区分不同性质的催收人员
 * @author LiJingXiang
 * @version 2017年4月11日
 */
public class TMisDunningGroup extends DataEntity<TMisDunningGroup> {

	private static final long serialVersionUID = 1L;
	public static final String GROUP_TYPE_SELF = "selfSupport"; //自营
	public static final String GROUP_TYPE_OUT_SEAT = "outsourceSeat"; //外包坐席
	public static final String GROUP_TYPE_OUT_COMMISSION = "outsourceCommission"; //委外佣金

	private Integer dbid;
	private String name; //组名
	private String type; //组类型
	private User leader; //组长

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}
	
	@Length(min=1, max=64, message="组名长度在1-64之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=1, max=64, message="组长度在1-64之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@NotNull(message="组长不能为空")
	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	@Override
	public String toString() {
		return "TMisDunningGroup [dbid=" + dbid + ", name=" + name + ", type=" + type + ", leader=" + leader + "]";
	}
	
}
