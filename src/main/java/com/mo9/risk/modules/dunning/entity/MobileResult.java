package com.mo9.risk.modules.dunning.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 电话码
 * @author jwchi 2017/9/21
 *
 */
public class MobileResult {

	/**
	 * 行动码
	 */
	private static final Map<String, String> actions = new HashMap<String, String>();

	/**
	 * 结果码
	 */
	private static final Map<String, String> conclusions = new HashMap<String, String>();

	static {
		actions.put("ALPA", "声称已还");
		actions.put("BUSY", "电话占线");
		actions.put("CUT", "一接就挂");
		actions.put("FEE", "费用减免");
		actions.put("INSY", "无还款诚意");
		actions.put("KNOW", "愿意还款");
		actions.put("LOOO", "空号");
		actions.put("MESF", "传真");
		actions.put("NOAS", "无人接听");
		actions.put("MESS", "转告");
		actions.put("NOSE", "查无此人");
		actions.put("OFF", "关机");
		actions.put("NOTK", "无法转告");
		actions.put("PTP", "承诺还款(本人)");
		actions.put("PTPX", "承诺还款(第三方)");
		actions.put("STOP", "停机");
		
		conclusions.put("ALPA", "承诺还款");
		conclusions.put("BUSY", "半失联");
		conclusions.put("CUT", "半失联");
		conclusions.put("FEE", "承诺还款");
		conclusions.put("INSY", "无还款诚意");
		conclusions.put("KNOW", "沟通中");
		conclusions.put("LOOO", "完全失联");
		conclusions.put("MESF", "完全失联");
		conclusions.put("NOAS", "半失联");
		conclusions.put("MESS", "沟通中");
		conclusions.put("NOSE", "完全失联");
		conclusions.put("OFF", "半失联");
		conclusions.put("NOTK", "半失联");
		conclusions.put("PTP", "承诺还款");
		conclusions.put("PTPX", "承诺还款");
		conclusions.put("STOP", "完全失联");
	}

	/**
	 * @param actionCode 行动代码
	 * @return 行动代码描述
	 */
	public static String getActionDesc(String actionCode) {
		String actionDesc = actions.get(actionCode);
		return actionDesc == null ? "" : actionDesc;
	}

	/**
	 * @param actionCode 结果代码
	 * @return 结果代码描述
	 */
	public static String getConclusionDesc(String conclusionCode) {
		String conclusionDesc = actions.get(conclusionCode);
		return conclusionDesc == null ? "" : conclusionDesc;
	}

	/**
	 * @return 行动码<代码, 描述>列表
	 */
	public static Map<String, String> getActions() {
		return actions;
	}

	/**
	 * @return 结果码<代码, 描述>列表
	 */
	public static Map<String, String> getConclusions() {
		return conclusions;
	}
	
}
