/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 迁徙率Entity
 * @author 徐盛
 * @version 2017-07-24
 */
public class TMisMigrationRateReport extends DataEntity<TMisMigrationRateReport> {
	
	private static final long serialVersionUID = 1L;
	private Integer dbid;
	private Date datetime;		// datetime
	private BigDecimal cp1new;		// cp1new
	private BigDecimal cp2new;		// cp2new
	private BigDecimal cp3new;		// cp3new
	private BigDecimal cp4new;		// cp4new
	private BigDecimal cp1corpus;		// cp1corpus
	private BigDecimal cp2corpus;		// cp2corpus
	private BigDecimal cp3corpus;		// cp3corpus
	private BigDecimal cp4corpus;		// cp4corpus
	private String cycle;		// cycle
	
	
	public TMisMigrationRateReport() {
		super();
	}

	public TMisMigrationRateReport(String id){
		super(id);
	}
	
	

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	@Length(min=0, max=11, message="cp1new长度必须介于 0 和 11 之间")
	public BigDecimal getCp1new() {
		return cp1new;
	}

	public void setCp1new(BigDecimal cp1new) {
		this.cp1new = cp1new;
	}
	
	@Length(min=0, max=11, message="cp2new长度必须介于 0 和 11 之间")
	public BigDecimal getCp2new() {
		return cp2new;
	}

	public void setCp2new(BigDecimal cp2new) {
		this.cp2new = cp2new;
	}
	
	@Length(min=0, max=11, message="cp3new长度必须介于 0 和 11 之间")
	public BigDecimal getCp3new() {
		return cp3new;
	}

	public void setCp3new(BigDecimal cp3new) {
		this.cp3new = cp3new;
	}
	
	@Length(min=0, max=11, message="cp4new长度必须介于 0 和 11 之间")
	public BigDecimal getCp4new() {
		return cp4new;
	}

	public void setCp4new(BigDecimal cp4new) {
		this.cp4new = cp4new;
	}
	
	public BigDecimal getCp1corpus() {
		return cp1corpus;
	}

	public void setCp1corpus(BigDecimal cp1corpus) {
		this.cp1corpus = cp1corpus;
	}
	
	public BigDecimal getCp2corpus() {
		return cp2corpus;
	}

	public void setCp2corpus(BigDecimal cp2corpus) {
		this.cp2corpus = cp2corpus;
	}
	
	public BigDecimal getCp3corpus() {
		return cp3corpus;
	}

	public void setCp3corpus(BigDecimal cp3corpus) {
		this.cp3corpus = cp3corpus;
	}
	
	public BigDecimal getCp4corpus() {
		return cp4corpus;
	}

	public void setCp4corpus(BigDecimal cp4corpus) {
		this.cp4corpus = cp4corpus;
	}
	
	@Length(min=0, max=11, message="cycle长度必须介于 0 和 11 之间")
	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}


	
	
	
}