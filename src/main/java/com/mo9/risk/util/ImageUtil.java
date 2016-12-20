package com.mo9.risk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mo9.risk.modules.dunning.entity.TMisRemittanceMessage;

public class ImageUtil {

	private static File file = null;

	/**
	 * 从本地文件读取图像的二进制流
	 * 
	 * @param infile
	 * @return
	 */
	public static FileInputStream getImageByte(String infile) {
		FileInputStream imageByte = null;
		file = new File(infile);
		try {
			imageByte = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return imageByte;
	}

	/**
	 * 将图片流读出为图片
	 * 
	 * @param inputStream
	 * @param path
	 */
	public static void readBlob(InputStream inputStream, String path) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, len);
			}
			inputStream.close();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * unicode 转字符串
	 */
	public static String unicode2String(String unicode) {

		StringBuffer string = new StringBuffer();

		String[] hex = unicode.split("\\\\u");

		for (int i = 1; i < hex.length; i++) {

			// 转换出每一个代码点
			int data = Integer.parseInt(hex[i], 16);

			// 追加成string
			string.append((char) data);
		}

		return string.toString();
	}
	
	

	/**
	 * 字符串转换unicode
	 */
	public static String string2Unicode(String string) {
	 
	    StringBuffer unicode = new StringBuffer();
	 
	    for (int i = 0; i < string.length(); i++) {
	 
	        // 取出每一个字符
	        char c = string.charAt(i);
	 
	        // 转换为unicode
	        unicode.append("\\u" + Integer.toHexString(c));
	    }
	 
	    return unicode.toString();
	}


	public void ss() {

		TMisRemittanceMessage message = new TMisRemittanceMessage();
		// message.setRemittanceimg(getImageByte(""));
	}

}