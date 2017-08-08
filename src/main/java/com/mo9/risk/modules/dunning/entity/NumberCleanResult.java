package com.mo9.risk.modules.dunning.entity;


/**
 * 号码清洗结果
 * @author jwchi
 *
 */
public enum NumberCleanResult {
	YXHM("有效号码"),
	BZFWQ("不在服务区"),
	KH("空号"),
	HMCW("号码错误"),
	GJ("关机"),
	TJ("停机"),
	WZ("未知");
	 
	
	private String numberResult;
	
	NumberCleanResult(String numberResult){
		
		this.numberResult=numberResult;
	}

	public String getNumberResult() {
		return numberResult;
	}
	
}
