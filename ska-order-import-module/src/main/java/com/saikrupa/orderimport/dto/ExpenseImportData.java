package com.saikrupa.orderimport.dto;

import java.util.Date;

public class ExpenseImportData {
	private Date expenseDate;
	private int vendorCode;
	private Double amount;
	private Date paymentDate;
	private int paymentMode;
	private String remarks;
	private int rowIndex;
	private int expenseCategoryCode;

	public Date getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}

	public int getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(int vendorCode) {
		this.vendorCode = vendorCode;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public int getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(int paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getExpenseCategoryCode() {
		return expenseCategoryCode;
	}

	public void setExpenseCategoryCode(int expenseCategoryCode) {
		this.expenseCategoryCode = expenseCategoryCode;
	}

}
