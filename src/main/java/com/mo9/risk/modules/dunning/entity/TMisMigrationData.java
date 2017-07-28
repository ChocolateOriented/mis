package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.DataEntity;


/**
 * 短信模板entity
 * @author jwchi
 *
 */
public class TMisMigrationData  {

	
	private String name;
	private  final String TYPE="line";
	
	private List<BigDecimal> data;
	
	public String getName() {
		return name;
	}
	
	public List<BigDecimal> getData() {
		return data;
	}



	public void setData(List<BigDecimal> data) {
		this.data = data;
	}



	public void setName(String name) {
		this.name = name;
	}
	public String getTYPE() {
		return TYPE;
	}
	
	
	
	
	

}
