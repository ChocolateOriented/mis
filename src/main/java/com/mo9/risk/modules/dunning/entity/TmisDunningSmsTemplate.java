package com.mo9.risk.modules.dunning.entity;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;




/**
 * 短信模板entity
 * @author jwchi
 *
 */
public class TmisDunningSmsTemplate  extends DataEntity<TmisDunningSmsTemplate>  {

	private static final long serialVersionUID = 1L;
	
	
	
	private Integer  dbid;  
	
	

	private String  templateName;  //短信模板名称
	
	private String  englishTemplateName;  //英文短信模板名称
	
	private String  sendMethod;		//发送方式
	
	private String  sendTime;		//发送时间
	
	private String  smsType;        //短信类型
	
	private String  acceptType;		//接受号码类型	
	
	private String  smsCotent;   //模板内容
	
	private Integer  numbefore;  //可逾期天数设定参数
	
	private Integer  numafter;   //可逾期天数设定参数
	
	private String  sendReason;   //可逾期天数设定参数
	
	//用来做软删
	private String invalid ;


	public String getInvalid() {
		return invalid;
	}


	public void setInvalid(String invalid) {
		this.invalid = invalid;
	}


	public String getEnglishTemplateName() {
		return englishTemplateName;
	}


	public void setEnglishTemplateName(String englishTemplateName) {
		this.englishTemplateName = englishTemplateName;
	}


	public String getSendReason() {
		return sendReason;
	}


	public void setSendReason(String sendReason) {
		this.sendReason = sendReason;
	}




	public Integer getDbid() {
		return dbid;
	}


	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}
	
	public void setSmsCotent(String smsCotent) {
		this.smsCotent = smsCotent;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getSendMethod() {
		return sendMethod;
	}

	public void setSendMethod(String sendMethod) {
		this.sendMethod = sendMethod;
	}

	

	public String getSendTime() {
		return sendTime;
	}
	public Date getSendTimeDate() {
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		try {
			if(this.sendTime!=null&&!"".equals(this.sendTime)){
			Date parse = sdf.parse(this.sendTime);
			return parse;
			}
		} catch (ParseException e) {
			return null;
		}
		return null;
	}


	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}


	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public String getAcceptType() {
		return acceptType;
	}

	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public TmisDunningSmsTemplate(){
		
	}
	

	public String getSmsCotent() {
		return smsCotent;
	}


	public Integer getNumbefore() {
		return numbefore;
	}


	public void setNumbefore(Integer numbefore) {
		this.numbefore = numbefore;
	}


	public Integer getNumafter() {
		return numafter;
	}


	public void setNumafter(Integer numafter) {
		this.numafter = numafter;
	}





	
}
