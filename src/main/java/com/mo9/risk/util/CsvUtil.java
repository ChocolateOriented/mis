package com.mo9.risk.util;

import com.csvreader.CsvWriter;
import com.thinkgem.jeesite.common.utils.excel.ExcelFieldParser;
import com.thinkgem.jeesite.common.utils.excel.ExcelFieldParser.ExcelFieldNode;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			for (E e : data) {
				if (null == e) {
					continue;
				}

				String[] line = new String[annotationList.size()];
				for (int i = 0; i < annotationList.size(); i++) {
					//获取字段值
					ExcelFieldNode node = annotationList.get(i);
					String val = parser.getStringValue(e, node);
					line[i] = val;
				}

				csvWriter.writeRecord(line);
			}
		} finally {
			csvWriter.flush();
			csvWriter.close();
		}
	}
}
