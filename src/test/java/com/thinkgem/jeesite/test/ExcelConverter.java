package com.thinkgem.jeesite.test;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public abstract class ExcelConverter {
	Integer curentRowNum = null ;
	Integer totalRowNum = null ;
	
	public void convert(String filePath){
		List<Row> rows = ExcelReader.readExcelFirtPage(filePath);
		totalRowNum = rows.size() ;
		for (int i = 0; i < totalRowNum; i++) {
			curentRowNum = i ;
			Row row = rows.get(i);
			onStartReadRow(i);
			for (int j = 0; j <= row.getLastCellNum(); j++) {
				Cell cell = row.getCell(j);
				String date = ExcelReader.getCellValue(cell);
				onReadData(j,date);
			}
			onEndReadRow(i);
		}
	}
	
	protected boolean isLastRow() {
		if (curentRowNum == null) {
			return false ;
		}
		return curentRowNum.equals(totalRowNum);
	}
	
	protected boolean isFirstRow() {
		return new Integer(0).equals(curentRowNum);
	}
	
	protected abstract void onStartReadRow(int rowNum);
	protected abstract void onReadData(int columnNum, String data);
	protected abstract void onEndReadRow(int rowNum);
}