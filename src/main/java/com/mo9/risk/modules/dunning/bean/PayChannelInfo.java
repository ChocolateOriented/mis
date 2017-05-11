/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;

public class PayChannelInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static String[] specialChannels = {"yilianpay"};
	
	private String channelid;		//渠道id
	
	private String channelname;		//渠道名称
	
	private boolean isusable;		//是否可用
	
	private Double successrate;		//成功率

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public boolean getIsusable() {
		return isusable;
	}

	public void setIsusable(boolean isusable) {
		this.isusable = isusable;
	}

	public Double getSuccessrate() {
		return successrate;
	}

	public void setSuccessrate(Double successrate) {
		this.successrate = successrate;
	}
	
	/**
	 * 判断是否为特定渠道需要验证余额不足情况
	 * @return
	 */
	public boolean needCheckBalance() {
		if (this.channelid == null || "".equals(channelid)) {
			return false;
		}
		
		for (int i = 0; i < specialChannels.length; i++) {
			if (specialChannels[i].equals(this.channelid)) {
				return true;
			}
		}
		
		return false;
	}
}
