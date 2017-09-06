package com.mo9.risk.modules.dunning.bean;

/**
 * Created by jxli on 2017/9/4.
 */
public class MigrateChange {

	String currentVlue ;//当前
	String lastVlue;
	String change;

	public MigrateChange(String currentVlue, String lastVlue, String change) {
		this.currentVlue = currentVlue;
		this.lastVlue = lastVlue;
		this.change = change;
	}

	public String getCurrentVlue() {
		return currentVlue;
	}

	public void setCurrentVlue(String currentVlue) {
		this.currentVlue = currentVlue;
	}

	public String getLastVlue() {
		return lastVlue;
	}

	public void setLastVlue(String lastVlue) {
		this.lastVlue = lastVlue;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}
}
