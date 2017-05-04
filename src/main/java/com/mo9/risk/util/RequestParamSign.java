package com.mo9.risk.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.thinkgem.jeesite.common.security.Digests;

public class RequestParamSign {

	/**
	 * 生成请求参数签名
	 * <p><blockquote><pre>
	 * key升序排序后,拼接字符串(不包含空值)
	 * key1=value1&key2=value2 + privateKey
	 * 计算字符串的MD5值</pre></blockquote>
	 * @param param
	 * @param privateKey
	 * @return sign
	 */
	public static String generateParamSign(Map<String, String> param, String privateKey) {
		StringBuilder paramBuilder = new StringBuilder();
		Map<String, String> sortedParam = new TreeMap<String, String>(param);
		for (Entry<String, String> entry : sortedParam.entrySet()) {
			String value = entry.getValue();
			if (value == null || "".equals(value)) {
				continue;
			}
			paramBuilder.append(entry.getKey()).append("=").append(value).append("&");
		}
		
		if (paramBuilder.length() > 0) {
			paramBuilder.deleteCharAt(paramBuilder.length() - 1);
		}
		
		if (privateKey != null) {
			paramBuilder.append(privateKey);
		}
		byte[] result = null;
		
		try {
			result = Digests.md5(paramBuilder.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			result = Digests.md5(paramBuilder.toString().getBytes());
		}
		
		return byteArrayToHexString(result);
		
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
		
		try {
			result = Digests.md5(paramStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			result = Digests.md5(paramStr.getBytes());
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

}
