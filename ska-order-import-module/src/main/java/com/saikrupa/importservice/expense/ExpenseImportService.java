package com.saikrupa.importservice.expense;

import java.util.List;

import com.saikrupa.app.dto.ExpenseData;
import com.saikrupa.orderimport.dto.ExpenseImportData;

public interface ExpenseImportService {
	public List<ExpenseImportData> getExpenseDataFromFile(final String filePath);
	public List<ExpenseData> importExpenses(List<ExpenseImportData> expenses);
}
