package com.mo9.risk.modules.dunning.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mo9.risk.modules.dunning.bean.SerialRepay;
import com.mo9.risk.modules.dunning.bean.SerialRepay.RepayWay;
import com.mo9.risk.util.RequestParamSign;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.http.MediaType;

/**
 * Created by jxli on 2017/11/6.
 */
public class SerialRepayTestDemo {

	@Test
	public void testListRepay() {

		String url = "https://new.mo9.com/flashApi/refund/public_list_collect_repay_by_deal_code";
//		String url = "http://zhangx.local.mo9.com/flash/refund/public_list_collect_repay_by_deal_code";
		JSONObject params = new JSONObject();
//		params.put("loanDealCode", "15000129811");
		params.put("loanDealCode", "1500012981117");

		HashMap<String, String> headers = new HashMap<String, String>();
		String key = "57caef73ce1b7448275797444e78e68815459953d7feb48ddaf7f57862af3e30";
		String sign = RequestParamSign.generateParamSign(params.toJSONString(), key);
		headers.put("sign", sign);
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		String post = post(url, params.toJSONString(), headers);

		System.out.println(post);
//		JSONObject reult = JSON.parseObject(null);
		JSONObject reult = JSON.parseObject(post);
		JSONArray reapyList = JSONArray.parseArray(reult.getString("data"));

		List<SerialRepay> data = new ArrayList<>();
		for (int i = 0; i < reapyList.size(); i++) {
			JSONObject repay = reapyList.getJSONObject(i);
			SerialRepay repayObj = repay.toJavaObject(SerialRepay.class);
			JSONObject repayWay = repay.getJSONObject("repayWay");
			if (repayWay != null) {
				System.out.println(repayWay.getString("key"));
				repayObj.setRepayWay(RepayWay.valueOf(repayWay.getString("key")));
			}
			data.add(repayObj);
		}
		System.out.println(data);
	}

	private static String post(String url, String json, HashMap<String, String> headers) {
		StringBuilder resp = null;
		try {
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			for (Map.Entry<String, String> header : headers.entrySet()) {
				conn.setRequestProperty(header.getKey(), header.getValue());
			}

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			StringBuffer param = new StringBuffer(json);

			out.write(param.toString());
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			resp = new StringBuilder();
			String inputLine = reader.readLine();
			while (inputLine != null) {
				resp.append(inputLine);
				inputLine = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp.toString();
	}

	@Test
	public void testEnum() {
		String a = "a";
		RepayWay.valueOf(a);			}

}