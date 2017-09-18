/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * @Description 请求事件本地持久化, 用于消息重试
 * @author jxli
 * @version 2017/9/11
 */
public class TMisRequestRecord extends DataEntity<TMisRequestRecord> {

	private static final long serialVersionUID = 1L;

	private String target;//请求目标

	private String param;//参数

	private String from;//请求来源

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return "TMisRequestRecord{" +
				"target='" + target + '\'' +
				", param='" + param + '\'' +
				", from='" + from + '\'' +
				'}';
	}
}
