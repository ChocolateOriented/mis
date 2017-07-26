package com.mo9.risk.modules.dunning.bean;

import java.io.Serializable;
import java.util.Date;

public class TmpMoveCycle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer cycle;
	private Date datetimestart;
	private Date datetimeend;
	
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCycle() {
		return cycle;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}

	public Date getDatetimestart() {
		return datetimestart;
	}

	public void setDatetimestart(Date datetimestart) {
		this.datetimestart = datetimestart;
	}

	public Date getDatetimeend() {
		return datetimeend;
	}

	public void setDatetimeend(Date datetimeend) {
		this.datetimeend = datetimeend;
	}

}
