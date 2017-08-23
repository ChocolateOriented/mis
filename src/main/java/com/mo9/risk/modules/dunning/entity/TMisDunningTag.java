/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.mo9.risk.modules.dunning.enums.TagType;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 用户标签Entity
 * @author shijlu
 * @version 2017-08-22
 */
public class TMisDunningTag extends DataEntity<TMisDunningTag> {

	private static final long serialVersionUID = 1L;

	private String dbid;		//dbid

	private String dealcode;		//催收订单号

	private String buyerid;		//用户id

	private TagType tagtype;		//标签类型
	
	private OccupationType occupation;		//职业
	
	private String remark;		//备注

	private String peopleid;		//催收人员id

	private String peoplename;		//催收人员姓名

	public TMisDunningTag() {
		super();
	}

	public TMisDunningTag(String id){
		super(id);
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(String buyerid) {
		this.buyerid = buyerid;
	}

	public TagType getTagtype() {
		return tagtype;
	}

	public void setTagtype(TagType tagtype) {
		this.tagtype = tagtype;
	}

	public String getTagtypeDesc() {
		return this.tagtype.getDesc();
	}


	public OccupationType getOccupation() {
		return occupation;
	}

	public void setOccupation(OccupationType occupation) {
		this.occupation = occupation;
	}

	public String getOccupationDesc() {
		return this.occupation == null ? "" : this.occupation.getDesc();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPeopleid() {
		return peopleid;
	}

	public void setPeopleid(String peopleid) {
		this.peopleid = peopleid;
	}

	public String getPeoplename() {
		return peoplename;
	}

	public void setPeoplename(String peoplename) {
		this.peoplename = peoplename;
	}

	public enum OccupationType {

		PublicSecurityOrgan("公检法"),
		Journalist("记者");

		private String desc;

		private OccupationType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
		
	}
}