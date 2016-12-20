/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.weeklyreport.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 基础佣金费率表Entity
 * @author 徐盛
 * @version 2016-11-08
 */
public class TMisDunningOuterFee extends DataEntity<TMisDunningOuterFee> {
	
	private static final long serialVersionUID = 1L;
	private String dunningpeopleid;		// 委外方ID
	private String dunningpeoplename;
	private Integer dunningdaybegin;		// 起始时间段
	private Integer dunningdayend;		// 截止时间段
	private Double dunningfee;		// 委外佣金费率
	private Date datetimebegin;		// 当前费率执行起始时间
	private Date datetimeend;		// 当前费率执行截止时间
	
	public TMisDunningOuterFee() {
		super();
	}

	public TMisDunningOuterFee(String id){
		super(id);
	}

	@Length(min=0, max=255, message="委外方ID长度必须介于 0 和 255 之间")
	public String getDunningpeopleid() {
		return dunningpeopleid;
	}

	public void setDunningpeopleid(String dunningpeopleid) {
		this.dunningpeopleid = dunningpeopleid;
	}
	
	public Integer getDunningdaybegin() {
		return dunningdaybegin;
	}

	public void setDunningdaybegin(Integer dunningdaybegin) {
		this.dunningdaybegin = dunningdaybegin;
	}
	
	public Integer getDunningdayend() {
		return dunningdayend;
	}

	public void setDunningdayend(Integer dunningdayend) {
		this.dunningdayend = dunningdayend;
	}
	
	public Double getDunningfee() {
		return dunningfee;
	}

	public void setDunningfee(Double dunningfee) {
		this.dunningfee = dunningfee;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDatetimebegin() {
		return datetimebegin;
	}

	public void setDatetimebegin(Date datetimebegin) {
		this.datetimebegin = datetimebegin;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDatetimeend() {
		return datetimeend;
	}

	public void setDatetimeend(Date datetimeend) {
		this.datetimeend = datetimeend;
	}

	public String getDunningpeoplename() {
		return dunningpeoplename;
	}

	public void setDunningpeoplename(String dunningpeoplename) {
		this.dunningpeoplename = dunningpeoplename;
	}
	
	
	
}