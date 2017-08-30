package com.mo9.risk.modules.dunning.entity;

import java.math.BigDecimal;
import java.util.List;
import com.thinkgem.jeesite.common.persistence.DataEntity;


/**
 * 迁徙图表实体类
 * @author jwchi
 *
 */
public class TMisMigrationRateMail  {

	
	private String name;
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

	public TMisMigrationRateMail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TMisMigrationRateMail(String name, List<BigDecimal> data) {
		super();
		this.name = name;
		this.data = data;
	}
	
	
	
	
	

}
