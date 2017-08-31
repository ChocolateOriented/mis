/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 评分卡Entity
 * @author shijlu
 * @version 2017-08-29
 */
public class TMisDunningScoreCard extends DataEntity<TMisDunningScoreCard> {

	private static final long serialVersionUID = 1L;

	private String dbid;		//dbid

	private String dealcode;		//催收订单号

	private String buyerid;		//用户id

	private String buyername;		//姓名

	private String mobile;		//预留手机号

	private Double score;		//分数

	private String grade;		//等级

	public TMisDunningScoreCard() {
		super();
	}

	public TMisDunningScoreCard(String id){
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

	public String getBuyername() {
		return buyername;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

}