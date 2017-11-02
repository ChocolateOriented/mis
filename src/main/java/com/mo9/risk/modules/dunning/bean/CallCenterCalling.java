package com.mo9.risk.modules.dunning.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * CTI呼叫请求
 * <blockquote><pre>
 * 发起呼叫,呼叫中断,呼叫保持,取消保持
 * 呼叫转移,呼叫监听,呼叫强插</pre></blockquote>
 */
public class CallCenterCalling extends CallCenterBaseAction {

	private static final long serialVersionUID = 1L;

	private String name;	//分机名称

	private String target;	//目标号码

	private String otherNumber;	//目标号码(校验字段)

	private Boolean autoAnswer;	//是否自动应答

	private Boolean video;	//是否视频呼叫

	private String customerno;	//自定义编号

	private String mode;	//呼叫模式

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@JSONField(name="other_number")
	public String getOtherNumber() {
		return otherNumber;
	}

	public void setOtherNumber(String otherNumber) {
		this.otherNumber = otherNumber;
	}

	@JSONField(name="auto_answer")
	public Boolean getAutoAnswer() {
		return autoAnswer;
	}

	public void setAutoAnswer(Boolean autoAnswer) {
		this.autoAnswer = autoAnswer;
	}

	public Boolean getVideo() {
		return video;
	}

	public void setVideo(Boolean video) {
		this.video = video;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
