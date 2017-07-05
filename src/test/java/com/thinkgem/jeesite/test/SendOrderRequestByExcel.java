package com.thinkgem.jeesite.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

public class SendOrderRequestByExcel {
	public static void main(String[] args) {
		ExcelConverter excelConverter = new ExcelConverter() {
			StringBuilder lineData = new StringBuilder();
			String orderCode = "" ;
			String chanel = "alipay" ;
			String type = "delay" ;
			String amount = "" ;
			String day = "" ;
			int cRow = 0;
			@Override
			protected void onReadData(int columnNum, String data) {
				if (cRow == 0) {
					return ;
				}
//				String 
//				if (columnNum == 2 ) {
//					lineData.append("'" + data + "'" );
//					if (cRow != 103) {
//						lineData.append("," );
//						return ;
//					}
//				}
				
				switch (columnNum) {
				case 2:
					orderCode = data ;
					break;
				case 3:
					day = data ;
					break;
				case 4:
					amount = data ;
					break;

				default:
					break;
				}
//				lineData.append("[" + columnNum + "列:" + data + "]");
			}

			@Override
			protected void onStartReadRow(int rowNum) {
				
				cRow = rowNum;
//				if (cRow == 0) {
//					lineData.append("(");
//					return ;
//				}
				
//				System.out.println("第" + rowNum + "行");
				lineData = new StringBuilder();
			}

			@Override
			protected void onEndReadRow(int rowNum) {
//				cRow = rowNum;
//				if (cRow == 103) {
//					lineData.append(")");
//					System.out.println(lineData);
//					return ;
//				}
				
				lineData.append("https://risk.mo9.com/riskportal/limit/order/v1.0/payForStaffType/"+orderCode+"/"+chanel+"/dz/"+type+"/"+amount+"/"+day);
				System.out.println(lineData.toString());
				httpRequest(lineData.toString());
//				System.out.println();
			}
		};
		excelConverter.convert("C:/续期表.xlsx");
	}

	public static void httpRequest(String requestUrl) {
		BufferedReader bReader = null;
		try {
			URL url = new URL(requestUrl);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream in = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			bReader = new BufferedReader(reader);
			while (bReader.read() != -1) {
				System.out.println(bReader.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void testHttpRequest() throws Exception {
		httpRequest(
				"https://riskclone.mo9.com/riskportal/limit/order/v1.0/payForStaffType/1489123275207/alipay/dz/delay/184/14");
	}

}
