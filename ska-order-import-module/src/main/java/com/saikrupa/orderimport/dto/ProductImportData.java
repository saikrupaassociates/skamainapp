package com.saikrupa.orderimport.dto;

import java.util.Date;

public class ProductImportData {
	private String code;
	private Date entryDate;
	private double quantityAdded;
	private double quantityRejected;
	private int machineCode;
	private int rowIndex;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public double getQuantityAdded() {
		return quantityAdded;
	}

	public void setQuantityAdded(double quantityAdded) {
		this.quantityAdded = quantityAdded;
	}

	public double getQuantityRejected() {
		return quantityRejected;
	}

	public void setQuantityRejected(double quantityRejected) {
		this.quantityRejected = quantityRejected;
	}

	public int getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(int machineCode) {
		this.machineCode = machineCode;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
}
