package com.mo9.risk.modules.dunning.api;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

/**
 * Created by jxli on 2017/9/22.
 */
public class RefundTestDemo {

	private static final String key = "57caef73ce1b7448275797444e78e68815459953d7feb48ddaf7f57862af3e30";
	private static final String baseUrl = "https://riskclone.mo9.com/mis";
//	private static final String baseUrl = "http://localhost:8080";
	private String refundCode = "7853275162962";
	/**
	 * @Description 申请退款测试
	 * @param
	 * @return void
	 */
	@org.junit.Test
	public void testLaunch()throws Exception {
		String url = baseUrl + "/api/refund/launch";

		JSONObject params = new JSONObject();
		params.put("refundCode", refundCode);
		params.put("remittanceChannel", "alipay");
		params.put("remittanceSerialNumber", "20170607200040011100300029285005");
		params.put("amount", 21);

//		params.put("refundCode",refundCode+1);
//		params.put("remittanceChannel", "alipay");
//		params.put("remittanceSerialNumber", "32938713691314784140531222218741");
//		params.put("amount", 21);

//		params.put("refundCode",refundCode+2);
//		params.put("remittanceChannel", "alipay");
//		params.put("remittanceSerialNumber", "20170607200040011100300029285003");
//		params.put("amount", 211);

		String dataJson = params.toJSONString();
		System.out.println(dataJson);

		String sign = this.generateParamSign(dataJson, key);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.put("sign",sign);

		this.post(url,dataJson,headers);
	}

	/**
	 * @Description 拒绝退款测试
	 * @param
	 * @return void
	 */
	@org.junit.Test
	public void testRefused()throws Exception {
		String url = baseUrl + "/api/refund/refused";

		JSONObject params = new JSONObject();
		params.put("refundCode",refundCode);

		String dataJson = params.toJSONString();
		System.out.println(dataJson);

		String sign = this.generateParamSign(dataJson, key);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.put("sign",sign);

		this.post(url,dataJson,headers);
	}
	/**
	 * @Description 取消退款测试
	 * @param
	 * @return void
	 */
	@org.junit.Test
	public void testCancel()throws Exception {
		String url = baseUrl + "/api/refund/cancel";

		JSONObject params = new JSONObject();
		params.put("refundCode",refundCode);

		String dataJson = params.toJSONString();
		System.out.println(dataJson);

		String sign = this.generateParamSign(dataJson, key);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.put("sign",sign);

		this.post(url,dataJson,headers);
	}

	/**
	 * @Description 退款中测试
	 * @param
	 * @return void
	 */
	@org.junit.Test
	public void testProcess() throws Exception {
		String url = "http://localhost:8080/api/refund/process";
		JSONObject params = new JSONObject();
		List<String> refundCodes = new ArrayList<String>();
		refundCodes.add(refundCode);
		refundCodes.add(refundCode+1);
		refundCodes.add(refundCode+2);
		params.put("refundCodes",refundCodes);
		params.put("auditor","财务审核人X");
		params.put("auditTime",new Date().getTime());

		String dataJson = params.toJSONString();
		System.out.println(dataJson);
		String sign = this.generateParamSign(dataJson, key);

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("sign",sign);
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);

		this.post(url,dataJson,headers);
	}

	/**
	 * @Description 退款失败测试
	 * @param
	 * @return void
	 */
	@org.junit.Test
	public void testfail() throws Exception {
		String url = baseUrl + "/api/refund/fail";

		JSONObject params = new JSONObject();
		params.put("refundCode",refundCode);

		String dataJson = params.toJSONString();
		System.out.println(dataJson);

		String sign = this.generateParamSign(dataJson, key);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.put("sign",sign);

		this.post(url,dataJson,headers);
	}

	/**
	 * @Description 完成退款测试
	 * @param
	 * @return void
	 */
	@org.junit.Test
	public void testFinish() throws Exception {
		String url = "http://localhost:8080/api/refund/finish";
		JSONObject params = new JSONObject();
		params.put("refundCode",refundCode);
//		params.put("refundCode",refundCode+2);
		params.put("refundTime",new Date().getTime());

		String dataJson = params.toJSONString();
		System.out.println(dataJson);

		String sign = this.generateParamSign(dataJson, key);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.put("sign",sign);

		this.post(url,dataJson,headers);
	}

	/**
	 * 生成请求参数签名
	 * <p><blockquote><pre>
	 * 拼接字符串paramStr + privateKey
	 * 计算字符串的MD5值</pre></blockquote>
	 * @param paramStr
	 * @param privateKey
	 * @return sign
	 */
	public static String generateParamSign(String paramStr, String privateKey) {

		if (privateKey != null) {
			paramStr = paramStr + privateKey;
		}
		byte[] result = null;

		final String MD5 = "MD5";
		try {
			MessageDigest digest = MessageDigest.getInstance(MD5);
			result = digest.digest(paramStr.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArrayToHexString(result);
	}

	private static String byteArrayToHexString(byte[] input) {
		if (input == null) {
			return "";
		}
		StringBuilder signBuilder = new StringBuilder();

		for (int i = 0; i < input.length; i++) {
			int tmp = input[i] & 0xff;
			if (tmp < 16) {
				signBuilder.append("0");
			}
			signBuilder.append(Integer.toHexString(tmp));
		}
		return signBuilder.toString();
	}

	private static void post(String url, String json , HashMap<String,String> headers)throws Exception{
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);

		for (Map.Entry<String,String> header: headers.entrySet()) {
			conn.setRequestProperty(header.getKey(),header.getValue());
		}

		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		StringBuffer param = new StringBuffer(json);

		out.write(param.toString());
		out.flush();
		out.close();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuilder resp = new StringBuilder();
		String inputLine = reader.readLine();
		while (inputLine != null) {
			resp.append(inputLine);
			inputLine = reader.readLine();
		}
		System.out.println( resp );
	}
}
