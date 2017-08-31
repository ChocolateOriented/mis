/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 分案demoEntity
 * @author 徐盛
 * @version 2017-08-28
 */
public class Demotable extends DataEntity<Demotable> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// name
	private Integer dealcodenum;		// dealcodenum
	private BigDecimal dealcodeamount;		// dealcodeamount
	private Date datetime;		// datetime
	private Integer peopleId;
	private String cycle;
	
	public Demotable() {
		super();
	}

	public Demotable(String id){
		super(id);
	}

	@Length(min=0, max=64, message="name长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getDealcodenum() {
		return dealcodenum;
	}

	public void setDealcodenum(Integer dealcodenum) {
		this.dealcodenum = dealcodenum;
	}
	
	public BigDecimal getDealcodeamount() {
		return dealcodeamount;
	}

	public void setDealcodeamount(BigDecimal dealcodeamount) {
		this.dealcodeamount = dealcodeamount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public Integer getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(Integer peopleId) {
		this.peopleId = peopleId;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	
	
	
}