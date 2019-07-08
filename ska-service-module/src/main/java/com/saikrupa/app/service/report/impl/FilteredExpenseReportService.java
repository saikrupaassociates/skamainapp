package com.saikrupa.app.service.report.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.saikrupa.app.dto.ExpenseData;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.dto.FilterParameter;
import com.saikrupa.app.service.report.common.CellValueType;
import com.saikrupa.app.service.report.component.ReportTableDataCell;
import com.saikrupa.app.service.report.component.ReportTableHeaderCell;

public class FilteredExpenseReportService extends AbstractReportService {
	
	private List<ExpenseData> expenses;
	private ReportSelectionData expenseReportSelectionData;

	public FilteredExpenseReportService(List<ExpenseData> expenses, ReportSelectionData expenseReportSelectionData) {
		setExpenses(expenses);
		setExpenseReportSelectionData(expenseReportSelectionData);
	}
	
	public void saveReport(final String reportName) {
		Document pdfDocument = createBlankDocument(reportName);
		pdfDocument.close();
	}

	@Override
	public void createPageTableContent(Document document) throws DocumentException {
		PdfPTable dataTable = new PdfPTable(6);
		dataTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		dataTable.getDefaultCell().setBorder(2);

		dataTable.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
		dataTable.setWidthPercentage(100F);
		float[] columnWidths = new float[]{10f, 15f, 30f, 15f, 15f, 15f};
		dataTable.setWidths(columnWidths);
		
		ReportTableHeaderCell header_no = new ReportTableHeaderCell("#");		
		ReportTableHeaderCell header_date = new ReportTableHeaderCell("Date");
		ReportTableHeaderCell header_category = new ReportTableHeaderCell("Category");
		
		ReportTableHeaderCell header_paidTo = new ReportTableHeaderCell("Paid To");
		ReportTableHeaderCell header_amount = new ReportTableHeaderCell("Amount");
		ReportTableHeaderCell header_mode = new ReportTableHeaderCell("Mode");
		
		dataTable.addCell(header_no);
		dataTable.addCell(header_date);
		dataTable.addCell(header_category);
		dataTable.addCell(header_paidTo);
		dataTable.addCell(header_amount);
		dataTable.addCell(header_mode);

		
		for(ExpenseData data : getExpenses()) {
			ReportTableDataCell data_no = new ReportTableDataCell(String.valueOf(data.getCode()), CellValueType.TEXT);			
			ReportTableDataCell data_date = new ReportTableDataCell(data.getExpenseDate(), CellValueType.DATE);
			ReportTableDataCell data_category = new ReportTableDataCell(String.valueOf(data.getExpenseType().getName()), CellValueType.TEXT);
			
			ReportTableDataCell data_paidTo = new ReportTableDataCell(String.valueOf(data.getVendor().getName()), CellValueType.TEXT);
			ReportTableDataCell data_amount = new ReportTableDataCell(data.getAmount(), CellValueType.AMOUNT);
			ReportTableDataCell data_mode = new ReportTableDataCell(data.getPaymentData().getName(), CellValueType.TEXT);
			
			dataTable.addCell(data_no);
			dataTable.addCell(data_date);
			dataTable.addCell(data_category);
			dataTable.addCell(data_paidTo);
			dataTable.addCell(data_amount);
			dataTable.addCell(data_mode);
		}
		
		PdfPTable consolidationTable = new PdfPTable(5);
		ReportTableHeaderCell totalExpense = new ReportTableHeaderCell("Total Expense Amount");
		ReportTableHeaderCell cashPayment = new ReportTableHeaderCell("Cash Payment");
		ReportTableHeaderCell accountPayment = new ReportTableHeaderCell("On Account Payment");
		ReportTableHeaderCell chequePayment = new ReportTableHeaderCell("Cheque Payment");
		
		ReportTableDataCell valueTotal = new ReportTableDataCell(getExpenseTotalMap().get("TOTAL_EXPENSE_VALUE"), CellValueType.AMOUNT, true);
		ReportTableDataCell valueCash = new ReportTableDataCell(getExpenseTotalMap().get("TOTAL_EXPENSE_CASH"), CellValueType.AMOUNT, true);
		ReportTableDataCell valueOnAccount = new ReportTableDataCell(getExpenseTotalMap().get("TOTAL_EXPENSE_AC"), CellValueType.AMOUNT, true);
		ReportTableDataCell valueCheque = new ReportTableDataCell(getExpenseTotalMap().get("TOTAL_EXPENSE_CHEQUE"), CellValueType.AMOUNT, true);
		
		consolidationTable.addCell(totalExpense);
		valueTotal.setColspan(4);
		consolidationTable.addCell(valueTotal);
		
		consolidationTable.addCell(cashPayment);
		valueCash.setColspan(4);
		consolidationTable.addCell(valueCash);
		
		consolidationTable.addCell(accountPayment);
		valueOnAccount.setColspan(4);
		consolidationTable.addCell(valueOnAccount);
		
		consolidationTable.addCell(chequePayment);
		valueCheque.setColspan(4);
		consolidationTable.addCell(valueCheque);
		
		PdfPTable outerTable = new PdfPTable(1);
		outerTable.setWidthPercentage(100F);
		outerTable.addCell(consolidationTable);
		outerTable.addCell(dataTable);		
		document.add(outerTable);
	}
	
	private Map<String, Double> getExpenseTotalMap() {
		double totalValue = 0;
		double totalValueCash = 0;
		double totalValueOnAccount = 0;
		double totalValueCheque = 0;
		
		for(ExpenseData data : getExpenses()) {
			totalValue = totalValue + data.getAmount();
			if(data.getPaymentData().getCode().equalsIgnoreCase("1000")) { //Cash
				totalValueCash = totalValueCash + data.getAmount();
			} else if(data.getPaymentData().getCode().equalsIgnoreCase("1001")) { //On Account
				totalValueOnAccount = totalValueOnAccount + data.getAmount();
			} else if(data.getPaymentData().getCode().equalsIgnoreCase("1002")) { // Cheque
				totalValueCheque = totalValueCheque + data.getAmount();
			}
		}
		HashMap<String, Double> map = new HashMap<String, Double>();
		map.put("TOTAL_EXPENSE_VALUE", totalValue);
		map.put("TOTAL_EXPENSE_CASH", totalValueCash);
		map.put("TOTAL_EXPENSE_AC", totalValueOnAccount);
		map.put("TOTAL_EXPENSE_CHEQUE", totalValueCheque);
		return map;
			
	}

	@Override
	public void addMetaData(Document document) {
		document.addTitle("Expense Report - Filtered Expenses");
		
	}

	@Override
	public String getPageTitle() {
		ReportSelectionData data = getExpenseReportSelectionData();
		List<FilterParameter> filters = data.getFilters();
		StringBuilder b = new StringBuilder();
		b.append("Expense Report - By : ");		
		for(FilterParameter filter : filters) {
			b.append(filter.getFilterName()+"/");
		}
		String title = b.toString();
		title = title.substring(0, title.length()-1);
		return title;
	}

	public List<ExpenseData> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<ExpenseData> expenses) {
		this.expenses = expenses;
	}

	public ReportSelectionData getExpenseReportSelectionData() {
		return expenseReportSelectionData;
	}

	public void setExpenseReportSelectionData(ReportSelectionData expenseReportSelectionData) {
		this.expenseReportSelectionData = expenseReportSelectionData;
	}
}
