package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

public class TMisDunningOrganization extends DataEntity<TMisDunningOrganization> {

	private static final long serialVersionUID = 1L;

	private Integer dbid;

	private String name;	//机构名称

	private User supervisor;	//机构监理

	public TMisDunningOrganization(String id) {
		super(id);
	}

	public TMisDunningOrganization() {
		super();
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(User supervisor) {
		this.supervisor = supervisor;
	}

}
