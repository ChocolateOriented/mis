/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 催收任务联系记录Entity
 * @author ycheng
 * @version 2016-07-15
 */
public class TMisContantRecord extends DataEntity<TMisContantRecord> {

	private static final long serialVersionUID = 1L;
	private String dbid;		// dbid
	private String taskid;		// 催收任务Id，指向对应的催收任务
	private String dealcode;		// 催收订单号
	private boolean orderstatus;		// 当前催收时的订单状态
	private Integer overduedays;		// 当前催收时的订单逾期天数
	private String remark;		// 备注
	private Date dunningtime;		// 催收动作发生时间
	private ContantType contanttype;		// 操作类型 1)短信2)电话
	private String contanttypestr;
	private String contanttarget;		// 应答对象
	private String content;		// 催收短信内容
	private ContactsType contactstype;		// 催收联系人类型
	private String contactstypestr;
	private SmsTemp smstemp;		// 短信模板
	private String templateName;		// 新短信模板名称
	private String smsType;		// 短信类型，文字或语音
	private String smsTemplateId;		// 短信模板id
	private String smstempstr;
	private Boolean iseffective;		//是否有效联络
	private String telstatus;		//电话应答状态
	private String field1;		// field1
	private String dunningpeoplename;		//催收人员id
	private String peoplename;  //催收人员
	private Date repaymenttime; //应该还款日期
	private Date promisepaydate;		//承诺还款日
	private String contactsname;		//联系人姓名
	private String conclusionid;		//电催结论id
	private String dunningCycle;		//订单所属队列
	private String buyerId;		//订单用户id
	private String newOperateName;		//操作人用花名表示
	
	private Integer buyerid;
	
	/*private String createby;		// createby
	private Date createdate;		// createdate
	private String updateby;		// updateby
	private Date updatedate;		// updatedate
*/	

	/**
     * 催收后电话的状态
     */
    public enum TelStatus
    {
        TNIS("号码停机"),
        NOPK("手机拒接"),
//        BUSY("电话忙音"),
        NOBD("查无此人"),
        NOEX("号码不存在"),
        MEMO("一般纪要"),
        BDQJ("客户离职 "),
//        NOAS("无人应答 "),
        LMS("留口信"),
        //PTP("承诺还款"),
        OPTP("他人代偿"),
        CAIN("客户回电"),
//        STOP("暂缓催收"),
        FEAD("费用调整"),
//        OFF("电话关机"),
        LDTX("来电提醒"),
        OTHER("其他"),
        QT("其TA"),
        //逾期催收增加催收结论流程及相关优化  变更枚举项
//        PTP("承诺还款（PTP）"),
        RTP("拒绝还款（RTP）"),
        WTP("有还款意愿"),
        WTR("有代偿意愿"),
        CMIN("沟通中"),
        PYD("已还款"),
        NSA("非本人接听"),
        NSN("非本人号码"),
        OOC("完全失联"),
        HOOC("半失联"),
       //新增的枚举
        ALPA("承诺还款"),
	   	 BUSY("半失联"),
	   	 CUT("半失联"),
	   	 FEE("承诺还款"),
	   	 INSY("无还款诚意"),
	   	 KNOW("沟通中"),
	   	 LOOO("完全失联"),
	   	 MESF("完全失联"),
	   	 NOAS("半失联"),
	   	 MESS("沟通中"),
	   	 NOSE("完全失联"),
	   	 OFF("半失联"),
	   	 NOTK("半失联"),
	   	 PTP("承诺还款"),
	   	 PTPX("承诺还款"),
	   	 STOP("完全失联");
        
        private String desc;
        TelStatus(String desc)
        {
            this.desc = desc;
        }

        public String getDesc()
        {
            return this.desc;
        }
    }
    
    
    /**
     *  联系人类型
     */
    public enum ContactsType {
        SELF("本人"),
		MARRIED("夫妻"),
		PARENT("父母"),
		CHILDREN("子女"),
		RELATIVES("亲戚"),
		WORKMATE("同事"),
		WORKTEL("工作电话"),
		CANTACT("通讯录"),
		FRIEND("朋友"),
		COMMUNCATE("通话记录");
        private String desc;
        ContactsType(String desc)
        {
            this.desc = desc;
        }

        public String getDesc()
        {
            return this.desc;
        }
    }
    
    /**
     *  短信类型
     */
    public enum SmsTemp {
    	ST_0("未逾期"),
    	ST__1_7("逾期1-7"),
    	ST_8_14("逾期8-14"),
    	ST_15_21("逾期15-21"),
    	ST_22_35("逾期22-35"),
    	ST_15_PLUS("逾期15+");
    	
        private String desc;
    	SmsTemp(String desc)
        {
            this.desc = desc;
        }

        public String getDesc()
        {
            return this.desc;
        }
    }
    
    /**
     *  联系方式类型
     */
    public enum ContantType {

        sms("短信"),
        tel("电话");
        private String desc;
        ContantType(String desc)
        {
            this.desc = desc;
        }

        public String getDesc()
        {
            return this.desc;
        }
    }

    
	public TMisContantRecord() {
		super();
	}

	public TMisContantRecord(String id){
		super(id);
	}

	@Length(min=1, max=11, message="dbid长度必须介于 1 和 11 之间")
	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
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
	
	//@Length(min=0, max=128, message="当前催收时的订单状态长度必须介于 0 和 128 之间")
	public boolean getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(boolean orderstatus) {
		this.orderstatus = orderstatus;
	}
	
	public Integer getOverduedays() {
		return overduedays;
	}

	public void setOverduedays(Integer overduedays) {
		this.overduedays = overduedays;
	}
	
//	@Length(min=0, max=255, message="备注长度必须介于 0 和 255 之间")
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
	
//	@Length(min=0, max=128, message="操作类型 1)短信2)电话长度必须介于 0 和 128 之间")
	public ContantType getContanttype() {
		return contanttype;
	}

	public void setContanttype(ContantType contanttype) {
		this.contanttype = contanttype;
	}
	
	public String getContanttarget() {
		return contanttarget;
	}

	public void setContanttarget(String contanttarget) {
		this.contanttarget = contanttarget;
	}
	
	@Length(min=0, max=255, message="催收短信内容长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	//@Length(min=0, max=64, message="催收联系人类型长度必须介于 0 和 64 之间")
	public ContactsType getContactstype() {
		return contactstype;
	}

	public void setContactstype(ContactsType contactstype) {
		this.contactstype = contactstype;
	}
	
	@Length(min=0, max=128, message="field1长度必须介于 0 和 128 之间")
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRepaymenttime() {
		return repaymenttime;
	}

	public void setRepaymenttime(Date repaymenttime) {
		this.repaymenttime = repaymenttime;
	}

	@Length(min=0, max=128, message="dunningpeoplename长度必须介于 0 和 128 之间")
	public String getDunningpeoplename() {
		return dunningpeoplename;
	}

	public void setDunningpeoplename(String dunningpeoplename) {
		this.dunningpeoplename = dunningpeoplename;
	}

	public Boolean getIseffective() {
		return iseffective;
	}

	public void setIseffective(Boolean iseffective) {
		this.iseffective = iseffective;
	}

	//@Length(min=0, max=128, message="telstatus长度必须介于 0 和 128 之间")
	public String getTelstatus() {
		return telstatus;
	}

	public void setTelstatus(String telstatus) {
		this.telstatus = telstatus;
	}
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

//	@Length(min=0, max=128, message="smstemp长度必须介于 0 和 128 之间")
	public SmsTemp getSmstemp() {
		return smstemp;
	}

	public void setSmstemp(SmsTemp smstemp) {
		this.smstemp = smstemp;
	}
	

	public String getContanttypestr() {
		return contanttypestr;
	}


	public void setContanttypestr(String contanttypestr) {
		this.contanttypestr = contanttypestr;
	}

	public String getContactstypestr() {
		return contactstypestr;
	}

	public void setContactstypestr(String contactstypestr) {
		this.contactstypestr = contactstypestr;
	}

	public String getSmstempstr() {
		return smstempstr;
	}

	public void setSmstempstr(String smstempstr) {
		this.smstempstr = smstempstr;
	}

	public String getTelstatusstr() {
		return MobileResult.getActionDesc(this.telstatus);
	}
	
	public String getPeoplename() {
		return peoplename;
	}

	public void setPeoplename(String peoplename) {
		this.peoplename = peoplename;
	}

	public Date getPromisepaydate() {
		return promisepaydate;
	}

	public void setPromisepaydate(Date promisepaydate) {
		this.promisepaydate = promisepaydate;
	}

	public String getContactsname() {
		return contactsname;
	}

	public void setContactsname(String contactsname) {
		this.contactsname = contactsname;
	}

	public String getConclusionid() {
		return conclusionid;
	}

	public void setConclusionid(String conclusionid) {
		this.conclusionid = conclusionid;
	}

	public Integer getBuyerid() {
		return buyerid;
	}

	public void setBuyerid(Integer buyerid) {
		this.buyerid = buyerid;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public String getSmsTemplateId() {
		return smsTemplateId;
	}

	public void setSmsTemplateId(String smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}

	public String getDunningCycle() {
		return dunningCycle;
	}

	public void setDunningCycle(String dunningCycle) {
		this.dunningCycle = dunningCycle;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getNewOperateName() {
		return newOperateName;
	}

	public void setNewOperateName(String newOperateName) {
		this.newOperateName = newOperateName;
	}
	
	
	/*@Length(min=0, max=64, message="createby长度必须介于 0 和 64 之间")
	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@Length(min=0, max=64, message="updateby长度必须介于 0 和 64 之间")
	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}*/
	
}