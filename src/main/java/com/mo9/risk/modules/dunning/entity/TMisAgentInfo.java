/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class TMisAgentInfo extends DataEntity<TMisAgentInfo> {

	private static final long serialVersionUID = 1L;

	private String dbid;		//dbid

	private String agent;		//坐席

	private String extension;		//分机

	private String direct;	//直线号码

	private String queue;		//队列

	private String peopleId;		//催收人员id

	private String status;		//登录状态
	
	private String nickName;		//催收员花名

	public TMisAgentInfo() {
		super();
	}

	public TMisAgentInfo(String id){
		super(id);
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

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}