package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * Created by jxli on 2017/11/10.
 * 消息队列实体类
 */
public class MisMqMessage extends DataEntity<MisMqMessage> {
	private String topic;//消息主题
	private String tag;//消息标签(主题下的分类)
	private String key;//业务主键
	private String body;//消息体
	private String msgId;//消息ID

	public MisMqMessage(String topic, String tag, String key, String body) {
		this.topic = topic;
		this.tag = tag;
		this.key = key;
		this.body = body;
	}

	public MisMqMessage() {
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "MisMqMessage{" +
				"topic='" + topic + '\'' +
				", tag='" + tag + '\'' +
				", key='" + key + '\'' +
				", body='" + body + '\'' +
				", msgId='" + msgId + '\'' +
				'}';
	}
}
