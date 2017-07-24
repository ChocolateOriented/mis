package com.mo9.risk.util;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.thinkgem.jeesite.common.utils.excel.ExcelFieldParser;
import com.thinkgem.jeesite.common.utils.excel.ExcelFieldParser.ExcelFieldNode;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jxli
 * @version 2017/6/5
 * @Description Csv导出工具, 针对使用ExcelField注解的类型数据
 */
public class CsvUtil {

	private static Logger logger = LoggerFactory.getLogger(ExcelFieldParser.class);

	/**
	 * @return void
	 * @Description 导出CSV数据
	 */
	public static <E> void export(OutputStream os, Class clz, List<E> data)
			throws IOException {
		if (null == os || null == clz || null == data) {
			throw new IllegalArgumentException();
		}
		//使用GBK编码兼容Excel
		CsvWriter csvWriter = new CsvWriter(os, ',', Charset.forName("GBK"));
		ExcelFieldParser parser = new ExcelFieldParser(clz, 1);

		List<ExcelFieldNode> annotationList = parser.init();
		//填写表头
		List<String> header = parser.getHeaderList();
		try {
			csvWriter.writeRecord(header.toArray(new String[header.size()]));
			logger.debug("csv表头:" + header);
			//填写表数据
			for (E entity : data) {
				if (null == entity) {
					continue;
				}

				String[] line = new String[annotationList.size()];
				for (int i = 0; i < annotationList.size(); i++) {
					//获取字段值
					ExcelFieldNode node = annotationList.get(i);
					String val = parser.getFieldStringValue(entity, node);
					line[i] = val;
				}

				csvWriter.writeRecord(line);
			}
		} finally {
			csvWriter.flush();
			csvWriter.close();
		}
	}

	/**
	 * @Description  导入Csv, Excel中文使用GBK编码
	 * @param file
	 * @param clz
	 * @param headerNum
	 * @return java.util.List<E>
	 */
	public static<E> List<E> importCsv(MultipartFile file,Class<E> clz,int headerNum)
			throws IllegalAccessException, IOException, InstantiationException {
		return importCsv(file,clz, Charset.forName("GBK"), headerNum);
	}
	/**
	 * @Description  导入Csv, 针对使用ExcelField注解的类型数据
	 * @param file 文件
	 * @param clz 目标数据类型
	 * @param charset 文件编码
	 * @param headerNum 标题行号从1开始, 0表示没标题
	 * @return java.util.List<E>
	 */
	public static<E> List<E> importCsv(MultipartFile file,Class<E> clz,Charset charset,int headerNum)
			throws IllegalAccessException, InstantiationException, IOException {
		if (null == file || null == clz || null == charset) {
			throw new IllegalArgumentException();
		}
		List<E> data = new ArrayList<E>();
		CsvReader csvReader = null;
		try {
			csvReader = new CsvReader(file.getInputStream(), charset);
			ExcelFieldParser parser = new ExcelFieldParser(clz, 2);
			List<ExcelFieldNode> annotationList = parser.init();

			for (int row = 0; csvReader.readRecord(); row++) {
				if (row < headerNum){//跳过标题
					continue;
				}
				//每一行对应一个实体
				E entity = (E)clz.newInstance();

				for (int column = 0; column < csvReader.getColumnCount(); column++) {
					//约定注解排序与数据排序相同
					ExcelFieldNode node = annotationList.get(column);
					try {
						String val = csvReader.get(column);
						parser.setStringValue2Field(val, node, entity);
					} catch (Exception e) {
						logger.info("解析csv文件[" + row + "," + column + "]时发生错误", e);
					}
				}
				data.add(entity);
			}
		} finally {
			if (null != csvReader) {
				csvReader.close();
			}
		}
		return data;
	}
}
