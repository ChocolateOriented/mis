/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.mo9.risk.modules.dunning.enums.DebtBizType;
import com.thinkgem.jeesite.common.utils.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 催收人员Entity
 * 
 * @author 徐盛
 * @version 2016-07-12
 */
public class TMisDunningPeople extends DataEntity<TMisDunningPeople> {
	/**
	 * ------------------------------催收人员---------------------------------------
	 * ---
	 */
	public static final String PEOPLE_TYPE_INNER = "inner"; // 内部催收人员
	public static final String PEOPLE_TYPE_OUTER = "outer"; // 委外公司

	private static final long serialVersionUID = 2L;
	private Integer dbid; // dbid
	private String name; // 催收人员名称
	@Deprecated
	private String dunningpeopletype; // 人员类型,已经添加组类型 , 此字段以后删除
	private BigDecimal rate; // 单笔费率 ,大于1为单笔固定费率，小于1大于0为单笔百分比费率
	private Integer begin; // 逾期周期起始
	private Integer end; // 逾期周期截至
	private String auto; // 是否自动
	private String field1; // field1

	private String Invalid;
	private String dunningcycle;
	private String dunningcycle2;

	private TMisDunningGroup group;
	private String nickname;
	private User user;
	private List<DebtBizType> bizTypes;

	private String bizTypesStr;

	private List<String> queryIds;// 用于催收人查询

	private String extensionNumber;// 催收员分机号

	private	String sumcorpusamount; // 分案金额


	private	String groupName; // 所属组中文名.导入用
	private	String validateId; // 校验是否已经是催收员.导入用


	public TMisDunningPeople() {
		super();
	}

	public TMisDunningPeople(String id) {
		super(id);
	}

	public TMisDunningPeople(Integer begin, Integer end) {
		this.begin = begin;
		this.end = end;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}


	@Length(min = 1, max = 64, message = "催收人员名称不能为空")
	@ExcelField(title="催收人员账号", align=2, sort=10)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Deprecated
	@Length(min = 0, max = 32, message = "人员类型长度必须介于 0 和 32 之间")
	public String getDunningpeopletype() {
		return dunningpeopletype;
	}

	@Deprecated
	public String getDunningpeopletypeText() {
		return PEOPLE_TYPE_INNER.equals(this.dunningpeopletype) ? "内部催收"
				: PEOPLE_TYPE_OUTER.equals(this.dunningpeopletype) ? "委外公司" : "";
	}

	@Deprecated
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

	@Length(min = 0, max = 128, message = "field1长度必须介于 0 和 128 之间")
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}
	@ExcelField(title="是否自动分配", align=2, sort=50)
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
	@ExcelField(title="催收队列", align=2, sort=40)
	public String getDunningcycle() {
		return dunningcycle;
	}

	// public String getDunningcycleText() {
	// StringBuffer buffer = new StringBuffer(" ");
	// String[] str = this.dunningcycle.split(",");
	// for(String lable : Arrays.asList(str)){
	// String scheduledBut =
	// DictUtils.getDictDescription(lable,"dunningCycle1","");
	// buffer.append(scheduledBut).append(" ");
	// }
	// return buffer.toString();
	// }
	public void setDunningcycle(String dunningcycle) {
		this.dunningcycle = dunningcycle;
		this.setDunningcycle2();
	}
	public void setDunningcycle2() {
		 if(this.dunningcycle != null && this.dunningcycle.contains(",")){
			 StringBuffer buffer = new StringBuffer(" ");
			 String[] str = this.dunningcycle.split(",");
			 if(str != null && str.length > 0){
				 for (int i = str.length-1; i >=0 ; i--) {
					 if(i==0){
						 buffer.append(str[i]);
					 }else{
						 buffer.append(str[i]+","); 
					 }
				 }
				this.dunningcycle2=buffer.toString().trim();
			 }
		 }
	}
	 public String getDunningcycle2() {
		 return dunningcycle2;
	 }
	public TMisDunningGroup getGroup() {
		return group;
	}

	public void setGroup(TMisDunningGroup group) {
		this.group = group;
	}

	@ExcelField(title="所属组", align=2, sort=30)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Length(min = 0, max = 64, message = "花名长度必须介于 0 和 64 之间")
	@ExcelField(title="催收人员花名", align=2, sort=20)
	public String getNickname() {
		return nickname;
	}

	@ExcelField(title="产品", align=2, sort=60)
	public String getBizTypesStr() {
		return bizTypesStr;
	}

	public void setBizTypesStr(String bizTypesStr) {
		this.bizTypesStr = bizTypesStr;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getQueryIds() {
		return queryIds;
	}

	public void setQueryIds(List<String> queryIds) {
		this.queryIds = queryIds;
	}

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getSumcorpusamount() {
		return sumcorpusamount;
	}

	public void setSumcorpusamount(String sumcorpusamount) {
		this.sumcorpusamount = sumcorpusamount;
	}

	public String getValidateId() {
		return validateId;
	}

	public void setValidateId(String validateId) {
		this.validateId = validateId;
	}

	public List<DebtBizType> getBizTypes() {
		return bizTypes;
	}

	public void setBizTypes(List<DebtBizType> bizTypes) {
		this.bizTypes = bizTypes;
	}

	//列表SQL使用
	public void setBizTypesText(String bizTypesText) {
		if (StringUtils.isBlank(bizTypesText)) {
			return;
		}
		String[] bizs = bizTypesText.split(",");
		if (bizs == null){
			return;
		}
		bizTypes = new ArrayList<>(bizs.length);
		for (int i = 0; i < bizs.length; i++) {
			bizTypes.add(DebtBizType.valueOf(bizs[i]));
		}
	}
}
