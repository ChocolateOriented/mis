/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class PhoneInfo extends DataEntity<PhoneInfo> {

	private static final long serialVersionUID = 1L;
	
	private String phoneNumber;
	
	private String preNumber;
	
	private String province;
	
	private String city;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPreNumber() {
		return preNumber;
	}

	public void setPreNumber(String preNumber) {
		this.preNumber = preNumber;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
