package com.mo9.risk.util;

import com.thinkgem.jeesite.common.utils.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jxli on 2017/6/23.
 */
public class RegexUtil {

	/**
	 * email地址，格式：xxx@xxx.xxx.xxx
	 */
	public static final String REGEX_EMAIL = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";

	/**
	 * 居民身份证号码18位，第一位不能为0，最后一位可能是数字或字母，中间16位为数字 \d同[0-9]
	 */
	public static final String REGEX_ID_CARD = "[1-9]\\d{16}[a-zA-Z0-9]{1}";

	/**
	 * 手机号码, 如:15434564564, 支持国际格式: +8615434564564
	 */
	public static final String REGEX_MOBILE = "(\\+\\d+)?1[34578]\\d{9}$";

	/**
	 * 文本中包含手机号码,不支持国际格式
	 */
	public static final String REGEX_CONTAIN_MOBILE = "1[34578]\\d{9}";


	/**
	 * 固定电话号码格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447,020-85588447,85588447
	 */
	public static final String REGEX_PHONE = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";

	/**
	 * @Description 通过正则截取字符串
	 * @param regex
	 * @param str
	 * @return java.lang.String
	 */
	public static String getStringValueByRegex(String regex,String str){
		if (StringUtils.isBlank(regex)|| StringUtils.isBlank(str)){
			return null;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()){
			return matcher.group();
		}
		return null;
	}
}
