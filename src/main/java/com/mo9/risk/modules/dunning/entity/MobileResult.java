package com.mo9.risk.modules.dunning.entity;


/**
 * 电话码
 * @author jwchi 2017/9/21
 *
 */
public enum MobileResult {
	 ALPA("声称已还"),
	 BUSY("电话占线"),
	 CUT("一接就挂/呼入限制"),
	 FEE("费用减免"),
	 INSY("无还款成意"),
	 KNOW("愿意还款"),
	 LOOO("空号"),
	 MESF("传真"),
	 NOAS("无人接听"),
	 MESS("转告"),
	 NOSE("查无此人"),
	 OFF("关机"),
	 NOTK("无法转告"),
	 PTP("承诺还款(本人)"),
	 PTPX("承诺还款(第三方)"),
	 STOP("停机");
		//电话码说明
		private String mobileResultName;
		
		MobileResult(String mobileResultName){
			
			this.mobileResultName=mobileResultName;
		}

		public String getMobileResultName() {
			return mobileResultName;
		}
		
	
}
