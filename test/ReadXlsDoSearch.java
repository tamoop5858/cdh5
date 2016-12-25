package com.apdplat.demo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excelファイルに読み込み
 * */
public class ReadXlsDoSearch {

	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_XLSX = "xlsx";

	private Workbook getWorkbook(String filePath) throws IOException {
		Workbook workbook = null;
		InputStream is = new FileInputStream(filePath);
		if (filePath.endsWith(EXTENSION_XLS)) {
			workbook = new HSSFWorkbook(is);
		} else if (filePath.endsWith(EXTENSION_XLSX)) {
			workbook = new XSSFWorkbook(is);
		}
		return workbook;
	}

	public void myExcel(String filePath) {

		Workbook workbook = null;

		try {
			workbook = this.getWorkbook(filePath);

			Sheet sheet = workbook.getSheetAt(0);
			int firstRowIndex = sheet.getFirstRowNum();
			int lastRowIndex = sheet.getLastRowNum();

			// 读取数据行
			for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
				Row currentRow = sheet.getRow(rowIndex);
				int lastColumnIndex = currentRow.getLastCellNum();
				if ("GET".equals(String.valueOf(currentRow.getCell(1)))) {
					EdinetSearcher searcher = new EdinetSearcher();
					searcher.search(String.valueOf(currentRow.getCell(2)),
							new File("source\\result\\output" + rowIndex + ".txt"));
				} else if ("POST".equals(String.valueOf(currentRow.getCell(1)))) {
					String key = null;
					String value = null;
					HashMap<String, String> params = new HashMap<String, String>();
					for (int columnIndex = 3; columnIndex <= lastColumnIndex - 1; columnIndex++) {
						Cell currentCell = currentRow.getCell(columnIndex);
						String str = String.valueOf(currentCell);
						if (!str.trim().equals("") ){
							if(columnIndex % 2 != 0){
								key = str;
							}else{
								value = str;
								params.put(key, value);
							}
						}else{
							break;
						}
					}
					
					NameValuePair[] pairs = buildNameValuePairs(params);

					NegaSearcher searcher = new NegaSearcher();
					searcher.search(String.valueOf(currentRow.getCell(2)),
							new File("source\\result\\output" + rowIndex + ".txt"), pairs);

				}
				System.out.println("URL"+ rowIndex +"実施完了");
			}
			System.out.println("全部実施完了");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private NameValuePair[] buildNameValuePairs(Map<String, String> params) {
		Object[] keys = params.keySet().toArray();
		NameValuePair[] pairs = new NameValuePair[keys.length];

		for (int i = 0; i < keys.length; i++) {
			String key = (String) keys[i];
			pairs[i] = new NameValuePair(key, params.get(key));
		}

		return pairs;
	}

	public static void main(String[] args) {
		String path = "source\\Input.xlsx";
		ReadXlsDoSearch doSearch = new ReadXlsDoSearch();
		doSearch.myExcel(path);
	}

}
