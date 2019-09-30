package com.saikrupa.importservice.expense.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.saikrupa.app.dto.ExpenseData;
import com.saikrupa.importservice.expense.ExpenseImportService;
import com.saikrupa.orderimport.dto.ExpenseImportData;

public class DefaultExpenseImportService implements ExpenseImportService {

	private static Logger LOG = Logger.getLogger(DefaultExpenseImportService.class);

	public List<ExpenseImportData> getExpenseDataFromFile(String filePath) {
		List<ExpenseImportData> expenseToImportList = new ArrayList<ExpenseImportData>();
		FileInputStream fileInputStream = null;

		try {
			fileInputStream = new FileInputStream(new File(filePath));
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(2);
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				if (row.getRowNum() == 0) {
					continue;
				}

				ExpenseImportData expense = new ExpenseImportData();
				expense.setRowIndex(row.getRowNum());

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case 0: // Expense Date
						Date entryDate = cell.getDateCellValue();
						Calendar cal = Calendar.getInstance();
						cal.setTime(entryDate);
						expense.setExpenseDate(entryDate);
						break;
					case 1: // EXPENSE_CATEGORY
						String expenseCategoryCode = cell.getStringCellValue().split("@")[0];
						expense.setExpenseCategoryCode(Integer.valueOf(expenseCategoryCode));
						break;
					case 2: // Paid to vendor
						String vendorText = cell.getStringCellValue().split("@")[0];
						expense.setVendorCode(Integer.valueOf(vendorText));
						break;
					case 3: // Amount
						expense.setAmount(cell.getNumericCellValue());
						break;
					case 4:// Payment Date
						Date paymentDate = cell.getDateCellValue();
						Calendar c1 = Calendar.getInstance();
						c1.setTime(paymentDate);
						expense.setPaymentDate(c1.getTime());
						break;
					case 5: // Payment Mode
						String paymentModeCode = cell.getStringCellValue().split("@")[0];
						expense.setPaymentMode(Integer.valueOf(paymentModeCode));
						break;
					case 6: // Remarks
						expense.setRemarks(cell.getStringCellValue());
						break;
					}
				}
				expenseToImportList.add(expense);
			}
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		LOG.info(" Expenses Collected : " + expenseToImportList.size());
		return expenseToImportList;
	}

	public List<ExpenseData> importExpenses(List<ExpenseImportData> expenses) {
		return Collections.emptyList();
	}

}
