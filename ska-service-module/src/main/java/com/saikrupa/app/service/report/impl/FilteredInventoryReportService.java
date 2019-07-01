package com.saikrupa.app.service.report.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.saikrupa.app.dto.FilterParameter;
import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.service.report.common.CellValueType;
import com.saikrupa.app.service.report.component.ReportTableDataCell;
import com.saikrupa.app.service.report.component.ReportTableHeaderCell;

public class FilteredInventoryReportService extends AbstractReportService {
	
	private List<InventoryEntryData> inventories;
	private ReportSelectionData reportSelectionData;

	public FilteredInventoryReportService(List<InventoryEntryData> inventories, ReportSelectionData reportSelectionData) {
		setInventories(inventories);
		setReportSelectionData(reportSelectionData);
	}
	
	public void saveReport(final String reportName) {
		Document pdfDocument = createBlankDocument(reportName);
		pdfDocument.close();
	}

	@Override
	public void createPageTableContent(Document document) throws DocumentException {
		PdfPTable dataTable = new PdfPTable(7);
		dataTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		dataTable.getDefaultCell().setBorder(2);

		dataTable.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
		dataTable.setWidthPercentage(100F);
//		float[] columnWidths = new float[]{10f, 15f, 30f, 15f, 15f, 15f};
//		dataTable.setWidths(columnWidths);
		
		ReportTableHeaderCell header_product = new ReportTableHeaderCell("Product");		
		ReportTableHeaderCell header_date = new ReportTableHeaderCell("Date");
		ReportTableHeaderCell header_opening = new ReportTableHeaderCell("Opening Balance");
		ReportTableHeaderCell header_added = new ReportTableHeaderCell("Added");
		ReportTableHeaderCell header_damaged = new ReportTableHeaderCell("Damaged");
		ReportTableHeaderCell header_closing = new ReportTableHeaderCell("Closing Balance");		
		ReportTableHeaderCell header_machine = new ReportTableHeaderCell("Machine");
		
		
		dataTable.addCell(header_product);
		dataTable.addCell(header_date);
		dataTable.addCell(header_opening);
		dataTable.addCell(header_added);
		dataTable.addCell(header_damaged);
		dataTable.addCell(header_closing);
		dataTable.addCell(header_machine);		

		
		for(InventoryEntryData data : getInventories()) {
			ReportTableDataCell data_productCode = new ReportTableDataCell(String.valueOf(data.getInventory().getProduct().getName()), CellValueType.TEXT);			
			ReportTableDataCell data_date = new ReportTableDataCell(data.getCreatedDate(), CellValueType.DATE);
			ReportTableDataCell data_opening = new ReportTableDataCell(data.getOpeningBalance(), CellValueType.QUANTITY);
			ReportTableDataCell data_added = new ReportTableDataCell(data.getAddedQuantity(), CellValueType.QUANTITY);
			ReportTableDataCell data_damaged = new ReportTableDataCell(data.getDamagedQuantity(), CellValueType.QUANTITY);
			ReportTableDataCell data_closing = new ReportTableDataCell(data.getClosingBalance(), CellValueType.QUANTITY);
			ReportTableDataCell data_machine = new ReportTableDataCell(data.getMachine().getName(), CellValueType.TEXT);			

			dataTable.addCell(data_productCode);
			dataTable.addCell(data_date);
			dataTable.addCell(data_opening);
			dataTable.addCell(data_added);
			dataTable.addCell(data_damaged);
			dataTable.addCell(data_closing);
			dataTable.addCell(data_machine);

		}
		
//		PdfPTable consolidationTable = new PdfPTable(5);
//		ReportTableHeaderCell totalExpense = new ReportTableHeaderCell("Total Expense Amount");
//		ReportTableHeaderCell cashPayment = new ReportTableHeaderCell("Cash Payment");
//		ReportTableHeaderCell accountPayment = new ReportTableHeaderCell("On Account Payment");
//		ReportTableHeaderCell chequePayment = new ReportTableHeaderCell("Cheque Payment");
//		
//		ReportTableDataCell valueTotal = new ReportTableDataCell(getExpenseTotalMap().get("TOTAL_EXPENSE_VALUE"), CellValueType.AMOUNT, true);
//		ReportTableDataCell valueCash = new ReportTableDataCell(getExpenseTotalMap().get("TOTAL_EXPENSE_CASH"), CellValueType.AMOUNT, true);
//		ReportTableDataCell valueOnAccount = new ReportTableDataCell(getExpenseTotalMap().get("TOTAL_EXPENSE_AC"), CellValueType.AMOUNT, true);
//		ReportTableDataCell valueCheque = new ReportTableDataCell(getExpenseTotalMap().get("TOTAL_EXPENSE_CHEQUE"), CellValueType.AMOUNT, true);
//		
//		consolidationTable.addCell(totalExpense);
//		valueTotal.setColspan(4);
//		consolidationTable.addCell(valueTotal);
//		
//		consolidationTable.addCell(cashPayment);
//		valueCash.setColspan(4);
//		consolidationTable.addCell(valueCash);
//		
//		consolidationTable.addCell(accountPayment);
//		valueOnAccount.setColspan(4);
//		consolidationTable.addCell(valueOnAccount);
//		
//		consolidationTable.addCell(chequePayment);
//		valueCheque.setColspan(4);
//		consolidationTable.addCell(valueCheque);
//		
//		PdfPTable outerTable = new PdfPTable(1);
//		outerTable.setWidthPercentage(100F);
//		outerTable.addCell(consolidationTable);
//		outerTable.addCell(dataTable);		
		document.add(dataTable);
	}
	
	private Map<String, Double> getExpenseTotalMap() {
		double totalValue = 0;
		double totalValueCash = 0;
		double totalValueOnAccount = 0;
		double totalValueCheque = 0;
		
//		for(ExpenseData data : getExpenses()) {
//			totalValue = totalValue + data.getAmount();
//			if(data.getPaymentData().getCode().equalsIgnoreCase("1000")) { //Cash
//				totalValueCash = totalValueCash + data.getAmount();
//			} else if(data.getPaymentData().getCode().equalsIgnoreCase("1001")) { //On Account
//				totalValueOnAccount = totalValueOnAccount + data.getAmount();
//			} else if(data.getPaymentData().getCode().equalsIgnoreCase("1002")) { // Cheque
//				totalValueCheque = totalValueCheque + data.getAmount();
//			}
//		}
		HashMap<String, Double> map = new HashMap<String, Double>();
		map.put("TOTAL_EXPENSE_VALUE", totalValue);
		map.put("TOTAL_EXPENSE_CASH", totalValueCash);
		map.put("TOTAL_EXPENSE_AC", totalValueOnAccount);
		map.put("TOTAL_EXPENSE_CHEQUE", totalValueCheque);
		return map;
			
	}

	@Override
	public void addMetaData(Document document) {
		document.addTitle("Order Report - Delivered :: Pending Payment");
		
	}

	@Override
	public String getPageTitle() {
		ReportSelectionData data = getReportSelectionData();
		if(data.getSelectionType() == 0) {
			return "Inventory Report - Consolidated";
		}		
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

	public List<InventoryEntryData> getInventories() {
		return inventories;
	}

	public void setInventories(List<InventoryEntryData> inventories) {
		this.inventories = inventories;
	}

	public ReportSelectionData getReportSelectionData() {
		return reportSelectionData;
	}

	public void setReportSelectionData(ReportSelectionData reportSelectionData) {
		this.reportSelectionData = reportSelectionData;
	}
}
