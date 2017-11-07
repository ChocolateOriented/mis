package com.mo9.risk.modules.dunning.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.entity.TRiskOrder;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jxli on 2017/10/18.
 */
public class orderMqSyn {
	private static final String data = "{\"amount\":0.10,\"audit_flow\":\"-\",\"buyer_id\":1477554,\"buyer_merchant_id\":\"\",\"channel\":\"yilianh5pay\",\"cost_amount\":0.00,\"coupons_id\":\"\",\"create_time\":1506669605000,\"credit_amount\":0.10,\"days\":14,\"dealcode\":\"1506669605488\",\"device\":\"E136C100CA5B4DB1A07F668C085D0A89\",\"first_order\":false,\"ip2long\":0,\"is_delay\":\"0\",\"item_name\":\"test.json\",\"merchant_id\":7,\"modify_amount\":0.00,\"modify_flag\":0,\"modify_operator1\":0,\"modify_operator2\":0,\"modify_operator3\":0,\"outerfiletime\":\"\",\"overdue_amount\":0.00,\"partner_id\":\"\",\"pay_code\":\"unspay_PDAEQNNHAPAQKOOB\",\"payer_msg\":\"[{\\\"id_card_no\\\":\\\"310108197211232444\\\",\\\"id_card_type\\\":\\\"居民身份证\\\",\\\"invest_amount\\\":0.01,\\\"name\\\":\\\"章菁\\\",\\\"phone\\\":\\\"18621058860\\\"}]||PDAEQNNHAPAQKOOB\",\"payoff_time\":1506669840000,\"platform\":\"app\",\"platform_ext\":\"ios_xinyongqianbao\",\"remark\":\"test.json\",\"remit_time\":1505460070000,\"repayment_time\":1507792870000,\"sessionId\":\"224808b8-06be-4b96-88a9-c633bcc9ac2d\",\"status\":\"payoff\",\"sub_cost_amount\":0.00,\"third_code\":\"MO91709291523\",\"type\":\"loan\",\"update_time\":1506669841000}";

	@org.junit.Test
	public void checkField()throws Exception {
		JSONObject remarkJson = JSON.parseObject(data);
		List<Field> fields = Arrays.asList(TRiskOrder.class.getDeclaredFields());
		System.out.println("实体字段个数"+fields.size());
		System.out.println("Json字段个数"+remarkJson.keySet().size());
		System.out.println("实体缺少字段");
		for (String key : remarkJson.keySet()) {
			//key转消息,去下划线
			String neefField = key.replaceAll("_", "").toLowerCase();
			boolean hasfield = false;
			for (Field field : fields) {
				String fieldName = field.getName().toLowerCase();
				if (fieldName.equals(neefField)) {
					hasfield = true;
					break;
				}
			}
			if (!hasfield) {
				System.out.println(key);
			}
		}

		System.out.println("Json缺少字段");
		for (Field field : fields) {

			String fieldName = field.getName().toLowerCase();
			//key转消息,去下划线
			boolean hasfield = false;
			for (String key : remarkJson.keySet()) {
				String neefField = key.replaceAll("_", "").toLowerCase();
				if (fieldName.equals(neefField)) {
					hasfield = true;
					break;
				}
			}
			if (!hasfield) {
				System.out.println(fieldName);
			}

		}
	}


	@org.junit.Test
	public void test2Json()throws Exception {
		TRiskOrder order = JSON.parseObject(data, TRiskOrder.class);
		//空字符串会被解析为integer值为null, 解析为String值为空字符串
		System.out.println(order.getBuyerMerchantId());
		int i = -2146946611 % 2;
		System.out.println( i );

//		while (true){
//			System.out.println(i++);
//		}
	}
}
