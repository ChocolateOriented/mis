package com.mo9.risk.modules.dunning.bean;

/**
 * CTI请求呼入呼出数据
 */
public class CallCenterQueryCallInfo extends CallCenterBaseAction {

	private static final long serialVersionUID = 1L;

	private String queue;	//队列信息

	private String starttime;	//开始时间	格式：yyyyMMddhhmmss

	private String endtime;	//开始时间	格式：yyyyMMddhhmmss

	private String caller;	//呼入号码

	private String sessionid;	//会话id

	private String customerno;	//自定义编号

	private String pagesize;	//每页显示条数

	private String page;	//当前页数

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

}
