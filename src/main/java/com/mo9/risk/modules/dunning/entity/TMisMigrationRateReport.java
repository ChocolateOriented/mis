/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.util.NumberUtil;

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
	private String monthUpDown;		// 上半月下半月标志
	
	private Date datetimeStart ;//周期对应的开始时间
	private Date datetimeEnd ;//周期对应的结束时间
	
	public Date getDatetimeStart() {
		return datetimeStart;
	}

	public void setDatetimeStart(Date datetimeStart) {
		this.datetimeStart = datetimeStart;
	}

	public Date getDatetimeEnd() {
		return datetimeEnd;
	}
	@ExcelField(title="周期", type=1, align=2, sort=2)
	public String getDatetimeEndText() {
		if(null==datetimeEnd){
			return "";
		}
		SimpleDateFormat sd=new SimpleDateFormat("yyyy年MM月");
		String dateString = sd.format(datetimeEnd);
		SimpleDateFormat sd2=new SimpleDateFormat("dd");
		String format = sd2.format(datetimeEnd);
		if(Integer.parseInt(format)<20){
			dateString+="上";
		}
		if(Integer.parseInt(format)>20){
			dateString+="下";
		}
		return dateString;
	}
	
	public void setDatetimeEnd(Date datetimeEnd) {
		this.datetimeEnd = datetimeEnd;
	}

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
	@ExcelField(title="日期", type=1, align=2, sort=1)
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
	@ExcelField(title="户数迁徙cp1", type=1, align=2, sort=3)
	public String getCp1newText() {
		return null != this.cp1new ? NumberUtil.formatToseparaString(this.cp1new.multiply(new BigDecimal(100))) + "%" : "";
	}

	public void setCp1new(BigDecimal cp1new) {
		this.cp1new = cp1new;
	}
	
	@Length(min=0, max=11, message="cp2new长度必须介于 0 和 11 之间")
	public BigDecimal getCp2new() {
		return cp2new;
	}
	@ExcelField(title="户数迁徙cp2", type=1, align=2, sort=4)
	public String getCp2newText() {
		return null != this.cp2new ? NumberUtil.formatToseparaString(this.cp2new.multiply(new BigDecimal(100))) + "%" : "";
	}

	public void setCp2new(BigDecimal cp2new) {
		this.cp2new = cp2new;
	}
	
	@Length(min=0, max=11, message="cp3new长度必须介于 0 和 11 之间")
	public BigDecimal getCp3new() {
		return cp3new;
	}
	@ExcelField(title="户数迁徙cp3", type=1, align=2, sort=5)
	public String getCp3newText() {
		return null != this.cp3new ? NumberUtil.formatToseparaString(this.cp3new.multiply(new BigDecimal(100))) + "%" : "";
	}

	public void setCp3new(BigDecimal cp3new) {
		this.cp3new = cp3new;
	}
	
	@Length(min=0, max=11, message="cp4new长度必须介于 0 和 11 之间")
	public BigDecimal getCp4new() {
		return cp4new;
	}
	@ExcelField(title="户数迁徙cp4", type=1, align=2, sort=6)
	public String getCp4newText() {
		return null != this.cp4new ? NumberUtil.formatToseparaString(this.cp4new.multiply(new BigDecimal(100))) + "%" : "";
	}

	public void setCp4new(BigDecimal cp4new) {
		this.cp4new = cp4new;
	}
	
	public BigDecimal getCp1corpus() {
		return cp1corpus;
	}
	@ExcelField(title="本金迁徙cp1", type=1, align=2, sort=7)
	public String getCp1corpusText() {
		return null != this.cp1corpus ? NumberUtil.formatToseparaString(this.cp1corpus.multiply(new BigDecimal(100))) + "%" : "";
	}

	public void setCp1corpus(BigDecimal cp1corpus) {
		this.cp1corpus = cp1corpus;
	}
	
	public BigDecimal getCp2corpus() {
		return cp2corpus;
	}
	@ExcelField(title="本金迁徙cp2", type=1, align=2, sort=8)
	public String getCp2corpusText() {
		return null != this.cp2corpus ? NumberUtil.formatToseparaString(this.cp2corpus.multiply(new BigDecimal(100))) + "%" : "";
	}

	public void setCp2corpus(BigDecimal cp2corpus) {
		this.cp2corpus = cp2corpus;
	}
	
	public BigDecimal getCp3corpus() {
		return cp3corpus;
	}
	@ExcelField(title="本金迁徙cp3", type=1, align=2, sort=9)
	public String getCp3corpusText() {
		return null != this.cp3corpus ? NumberUtil.formatToseparaString(this.cp3corpus.multiply(new BigDecimal(100))) + "%" : "";
	}

	public void setCp3corpus(BigDecimal cp3corpus) {
		this.cp3corpus = cp3corpus;
	}
	
	public BigDecimal getCp4corpus() {
		return cp4corpus;
	}
	@ExcelField(title="本金迁徙cp4", type=1, align=2, sort=10)
	public String getCp4corpusText() {
		return null != this.cp4corpus ? NumberUtil.formatToseparaString(this.cp4corpus.multiply(new BigDecimal(100))) + "%" : "";
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

	public String getMonthUpDown() {
		return monthUpDown;
	}

	public void setMonthUpDown(String monthUpDown) {
		this.monthUpDown = monthUpDown;
	}


	
	
	
}