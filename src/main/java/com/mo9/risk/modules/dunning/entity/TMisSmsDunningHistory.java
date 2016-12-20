/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 短信催收记录Entity
 * @author 徐盛
 * @version 2016-07-12
 */
public class TMisSmsDunningHistory extends DataEntity<TMisSmsDunningHistory> {

    private static final long serialVersionUID = 1L;
    private Integer dbid;		// dbid
    private String taskid;		// 催收任务Id，指向对应的催收任务
    private String dealcode;		// 催收订单号
    private String orderstatus;		// 当前催收时的订单状态
    private Integer overduedays;		// 当前催收时的订单逾期天数
    private String remark;		// 备注
    private Date dunningtime;		// 催收动作发生时间
    private String mobile;		// 手机号码
    private String content;		// 催收短信内容
    private String contactstype;		// 催收联系人类型
    private String field1;		// field1
//	private String createby;		// createby
//	private Date createdate;		// createdate
//	private String updateby;		// updateby
//	private Date updatedate;		// updatedate

    public TMisSmsDunningHistory() {
        super();
    }

    public TMisSmsDunningHistory(String id){
        super(id);
    }

    @NotNull(message="dbid不能为空")
    public Integer getDbid() {
        return dbid;
    }

    public void setDbid(Integer dbid) {
        this.dbid = dbid;
    }

    @Length(min=1, max=255, message="催收任务Id，指向对应的催收任务长度必须介于 1 和 255 之间")
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    @Length(min=1, max=64, message="催收订单号长度必须介于 1 和 64 之间")
    public String getDealcode() {
        return dealcode;
    }

    public void setDealcode(String dealcode) {
        this.dealcode = dealcode;
    }

    @Length(min=0, max=128, message="当前催收时的订单状态长度必须介于 0 和 128 之间")
    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public Integer getOverduedays() {
        return overduedays;
    }

    public void setOverduedays(Integer overduedays) {
        this.overduedays = overduedays;
    }

    @Length(min=0, max=255, message="备注长度必须介于 0 和 255 之间")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDunningtime() {
        return dunningtime;
    }

    public void setDunningtime(Date dunningtime) {
        this.dunningtime = dunningtime;
    }

    @Length(min=0, max=64, message="手机号码长度必须介于 0 和 64 之间")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Length(min=0, max=128, message="催收短信内容长度必须介于 0 和 128 之间")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Length(min=0, max=64, message="催收联系人类型长度必须介于 0 和 64 之间")
    public String getContactstype() {
        return contactstype;
    }

    public void setContactstype(String contactstype) {
        this.contactstype = contactstype;
    }

    @Length(min=0, max=128, message="field1长度必须介于 0 和 128 之间")
    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
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
