/**

 * 创建日期：2015-12-15下午07:54:53

 * 修改日期：

 * 作者：lcf

 *TODO

 *return

 */
package com.thinkgem.jeesite.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author dner
 * 
 */
public class NumberUtil {
	
	/**
	 * 格式化带宽数据
	 * @param data
	 * @return
	 */
	public static double getFromateDouble(double data) {
		DecimalFormat df = new DecimalFormat("0.000");
		return Double.parseDouble(df.format(data));
	}

	/**
	 * 会计数据显示格式
	 * @param data
	 * @return
	 */
	public static String formatTosepara(double data) {
		DecimalFormat df = new DecimalFormat("#,###.##");
		return df.format(data);
	}
	
	/**
	 * 会计数据显示格式
	 * @param data
	 * @return
	 */
	public static String formatToseparaInteger(double data) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(data);
	}
	
	/**
	 * 会计数据显示格式
	 * @param data
	 * @return
	 */
	public static String formatTosepara(BigDecimal data) {
		DecimalFormat df = new DecimalFormat("#,###.##");
		return df.format(data);
	}
	/**
	 * 会计数据显示格式
	 * @param data
	 * @return
	 */
	public static String formatTosepara(long data) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(data);
	}
	
	public static Double formatToseparaDouble(Double data) {
		DecimalFormat df = new DecimalFormat("#,###.00");
		return Double.parseDouble(df.format(data));
	}
	
	/**
	 * 百分比转换
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static String getNumberFormatPercentage(Object num1,Object num2){
		 float n1 = Float.parseFloat(num1.toString());
		 float n2 = Float.parseFloat(num2.toString());
		 NumberFormat numberFormat = NumberFormat.getInstance();  
		 numberFormat.setMaximumFractionDigits(2);  
		 String result = numberFormat.format(n1 / n2 * 100);  
		 return result + "%";
	}
	
	/**
	 * 增幅比例
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static String getNumberFormatAmplitudePercentage(Object num1,Object num2){
		 Double n1 = Double.parseDouble(num1.toString());
		 Double n2 = Double.parseDouble(num2.toString());
		 Double amplitude = (n1 - n2) / n2 * 100;
		 NumberFormat numberFormat = NumberFormat.getInstance();  
		 numberFormat.setMaximumFractionDigits(2);  
		 return  numberFormat.format(amplitude) + "%";
//		 float amplitude = n1 / n2 * 100 - 100;
//		 if(amplitude >= 0){
//			 return "+" + numberFormat.format(amplitude) + "%";
//		 }else{
//			 return "-" + numberFormat.format(amplitude) + "%";
//		 }
	}
	
	public static void main(String[] args) {
		System.out.println(NumberUtil.getNumberFormatAmplitudePercentage(134, 123));
	}
	
}
