/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import java.util.Arrays;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 催收人员Entity
 * @author 徐盛
 * @version 2016-07-12
 */
public class TMisDunningPeople extends DataEntity<TMisDunningPeople> {
	/**
	 * ------------------------------催收人员------------------------------------------
	 */
	public static final String PEOPLE_TYPE_INNER = "inner";  //内部催收人员
	public static final String PEOPLE_TYPE_OUTER = "outer"; //委外公司

	private static final long serialVersionUID = 1L;
	private Integer dbid;		// dbid
	private String name;		// 催收人员名称
	private String dunningpeopletype;		// 人员类型
	private BigDecimal rate;		// 单笔费率 ,大于1为单笔固定费率，小于1大于0为单笔百分比费率
	private Integer begin;		// 逾期周期起始
	private Integer end;		// 逾期周期截至
	private String auto;       //是否自动
	private String field1;		// field1
	
	private String Invalid;
	
	private String dunningcycle;
//	private String createby;		// createby
//	private Date createdate;		// createdate
//	private String updateby;		// updateby
//	private Date updatedate;		// updatedate
	
	public TMisDunningPeople() {
		super();
	}

	public TMisDunningPeople(String id){
		super(id);
	}
	
	public TMisDunningPeople(Integer begin,Integer end){
		this.begin = begin;
		this.end = end;
	}

	public Integer getDbid() {
		return dbid;
	}
	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}
	
	@Length(min=1, max=64, message="催收人员名称不能为空")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=32, message="人员类型长度必须介于 0 和 32 之间")
	public String getDunningpeopletype() {
		return dunningpeopletype;
	}
	public String getDunningpeopletypeText() {
		return PEOPLE_TYPE_INNER.equals(this.dunningpeopletype) ?  "内部催收" : 
			PEOPLE_TYPE_OUTER.equals(this.dunningpeopletype) ?  "委外公司" : "";
	}
	public void setDunningpeopletype(String dunningpeopletype) {
		this.dunningpeopletype = dunningpeopletype;
	}
	
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	
	public Integer getBegin() {
		return begin;
	}

	public void setBegin(Integer begin) {
		this.begin = begin;
	}
	
	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}
	
	@Length(min=0, max=128, message="field1长度必须介于 0 和 128 之间")
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getAuto() {
		return auto;
	}

	public void setAuto(String auto) {
		this.auto = auto;
	}

	public String getInvalid() {
		return Invalid;
	}

	public void setInvalid(String invalid) {
		Invalid = invalid;
	}

	public String getDunningcycle() {
		return dunningcycle;
	}
//	public String getDunningcycleText() {
//		StringBuffer buffer = new StringBuffer(" ");
//		String[] str = this.dunningcycle.split(",");
//		for(String lable : Arrays.asList(str)){
//			String scheduledBut =  DictUtils.getDictDescription(lable,"dunningCycle1","");
//			buffer.append(scheduledBut).append(" ");
//		}
//		return buffer.toString();
//	}
	public void setDunningcycle(String dunningcycle) {
		this.dunningcycle = dunningcycle;
	}

	
	
//	@Length(min=0, max=64, message="createby长度必须介于 0 和 64 之间")
//	public String getCreateby() {
//		return createby;
//	}
//
//	public void setCreateby(String createby) {
//		this.createby = createby;
//	}
//	
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	public Date getCreatedate() {
//		return createdate;
//	}
//
//	public void setCreatedate(Date createdate) {
//		this.createdate = createdate;
//	}
//	
//	@Length(min=0, max=64, message="updateby长度必须介于 0 和 64 之间")
//	public String getUpdateby() {
//		return updateby;
//	}
//
//	public void setUpdateby(String updateby) {
//		this.updateby = updateby;
//	}
//	
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	public Date getUpdatedate() {
//		return updatedate;
//	}
//
//	public void setUpdatedate(Date updatedate) {
//		this.updatedate = updatedate;
//	}
	
}