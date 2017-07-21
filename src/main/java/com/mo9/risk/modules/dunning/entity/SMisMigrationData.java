package com.mo9.risk.modules.dunning.entity;

import java.util.List;
import com.thinkgem.jeesite.common.persistence.DataEntity;


/**
 * 短信模板entity
 * @author jwchi
 *
 */
public class SMisMigrationData  {

	
	private String name;
	private  final String TYPE="line";
	
	private List<Integer> data;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getData() {
		return data;
	}
	public void setData(List<Integer> data) {
		this.data = data;
	}
	public String getTYPE() {
		return TYPE;
	}
	
	
	
	
	

}
