package com.saikrupa.app.dao;

import java.util.List;
import java.util.Map;

import com.saikrupa.app.dto.ExpenseData;
import com.saikrupa.app.dto.ReportSelectionData;

public interface ExpenseDAO {
	public List<ExpenseData> findAllExpenses();
	public ExpenseData findExpenseByCode(String expenseCode);
	public List<ExpenseData> searchExpenseWithFilter(Map<String, String> params);
	public List<ExpenseData> searchExpenseWithFilter(ReportSelectionData selection);
}
