package com.mo9.risk.modules.dunning.entity;

import java.util.Date;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 号码清洗entity
 * @author jwchi
 *
 */
public class TmisDunningNumberClean  extends DataEntity<TmisDunningNumberClean>  {

	private static final long serialVersionUID = 1L;
	private Integer  dbid;  

	private String  dealcode;  //订单号
	
	private String  numCleanCode;  //清洗流水号
	
	private Date  startTime;		//清洗发起时间
	
	private Date  endTime;		//清洗结束时间
	
	private String  state;        //清洗成功与否的标志true为成功，false为失败
	
	private String  account;		//账号
	
	private String  password;   //密码
	
	private String  phone;  //清洗的号码
	
	private String  reqNo;  //流水号
	
	private String  replyUrl;  //异步回调的地址
	
	private String  taskId;  //清洗的任务Id
	
	private String  error;  //清洗的状态
	
	private String  areaName;  //号码归属地名称
	
	private String  areaCode;  //号码归属地区号
	
	private String  postCode;  //号码归属地邮编
	
	private String  checkResult;  //清洗的结果
	
	private String  message;   //清洗结果的描述
	
	private String  callingPeriod;   //清洗调用时期，calling表示调用期，callback表示回调期
	
	private String  createByName;   //清洗的人
	
	private String  updateByName;   //更新的人

	public String getDealcode() {
		return dealcode;
	}

	public void setDealcode(String dealcode) {
		this.dealcode = dealcode;
	}

	public String getNumCleanCode() {
		return numCleanCode;
	}

	public void setNumCleanCode(String numCleanCode) {
		this.numCleanCode = numCleanCode;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getReplyUrl() {
		return replyUrl;
	}

	public void setReplyUrl(String replyUrl) {
		this.replyUrl = replyUrl;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getCallingPeriod() {
		return callingPeriod;
	}

	public void setCallingPeriod(String callingPeriod) {
		this.callingPeriod = callingPeriod;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}

	
}