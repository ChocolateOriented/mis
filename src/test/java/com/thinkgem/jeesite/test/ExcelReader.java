package com.thinkgem.jeesite.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @Description Excel读取工具
 * 	Workbook:Excel对象 ,工作簿(sheet)集合
 * 	使用Workbook.getSheetAt(index)或者Workbook.getSheet(name)可以获取对应工作簿(sheet)
 *  	
 *  Sheet工作簿,使用sheet.getRow(rownum)获取对应的行
 *  	
 *  Row 行,使用row.getLastCellNum()获取最后一的单元格位置  row.getCell(j)获取对应单元格
 *  	
 *  Cell 单元格
 * @author LiJingXiang
 * @version 2017年4月21日
 */
public class ExcelReader {
	private static final DataFormatter defultFormatter = new DataFormatter();

	/**
	 * @Description: 读取第一页所有行
	 * @return: List<Row>
	 */
	public static List<Row> readExcelFirtPage(String filePath) {
		Workbook workbook = loadExcel(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		return readLine(sheet);
	}

	/**
	 * @Description: 读取excel文件
	 * @param filePath:本地文件路径
	 * @return
	 * @return: Workbook
	 */
	public static Workbook loadExcel(String filePath) {
		FileInputStream inStream = null;
		Workbook workBook = null;
		try {
			inStream = new FileInputStream(new File(filePath));
			workBook = WorkbookFactory.create(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return workBook;
	}

	/**
	 * @Description: 获取一张表中所有的行 ,只是简单的转换为List
	 * @param sheet
	 * @return
	 * @return: List<Row>:行集合
	 */
	public static List<Row> readLine(Sheet sheet) {
		int rowNum = sheet.getLastRowNum() + 1;
		List<Row> rows = new ArrayList<Row>(rowNum);
		for (int i = 0; i < rowNum; i++) {
			Row row = sheet.getRow(i);
			rows.add(row);
		}
		return rows;
	}
	
	/**
	 * @Description: 获取一张行中所有的单元格
	 * @param row
	 * @return
	 * @return: List<Cell>
	 */
	public static List<String> readLine(Row row , DataFormatter dateFormatter) {
		int cellNum = row.getLastCellNum() + 1;
		List<String> cells = new ArrayList<String>(cellNum);
		for (int i = 0; i < cellNum; i++) {
			Cell cell = row.getCell(i);
			String data = getCellValue(cell, dateFormatter) ;
			cells.add(data);
		}
		return cells;
	}
	
	// 获取单元格的值
	public static String getCellValue(Cell cell) {
		return getCellValue(cell, defultFormatter);
	}

	public static String getCellValue(Cell cell, DataFormatter dateFormatter) {
		if (dateFormatter == null) {
			dateFormatter = defultFormatter;
		}
		String cellValue = "";
		if (cell != null) {
			// 判断单元格数据的类型，不同类型调用不同的方法
			switch (cell.getCellType()) {
			// 数值类型
			case Cell.CELL_TYPE_NUMERIC:
				// 进一步判断 ，单元格格式是日期格式
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = dateFormatter.formatCellValue(cell);
				} else {
					// 数值
					Double value = cell.getNumericCellValue();
//					System.out.println( value );
					Integer intValue = value.intValue();
					DecimalFormat   df   =   new   DecimalFormat( "####"); 
					//保留两位小数且不用科学计数法，并使用千分位 
//					cellValue = value - intValue == 0 ? String.valueOf(df.format(intValue)) : String.valueOf(df.format(value));
//					System.out.println( "===========================" );
					cellValue = String.valueOf(df.format(value));
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			// 判断单元格是公式格式，需要做一种特殊处理来得到相应的值
			case Cell.CELL_TYPE_FORMULA: {
				try {
					cellValue = String.valueOf(cell.getNumericCellValue());
				} catch (IllegalStateException e) {
					cellValue = String.valueOf(cell.getRichStringCellValue());
				}

			}
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				cellValue = "";
				break;
			default:
				cellValue = cell.toString().trim();
				break;
			}
		}
		return cellValue.trim();
	}
}
