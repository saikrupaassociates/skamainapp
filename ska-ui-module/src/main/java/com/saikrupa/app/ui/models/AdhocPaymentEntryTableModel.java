package com.saikrupa.app.ui.models;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.util.DateUtil;

public class AdhocPaymentEntryTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnNames = { "#", "Amount Payable", "Amount Paid", "Balance Amount", "Payment Date", "Payment Mode","Cheque / DD No",
			"Adjusted" };

	private List<PaymentEntryData> paymentEntryList;

	public AdhocPaymentEntryTableModel(List<PaymentEntryData> paymentEntryList) {
		this.paymentEntryList = paymentEntryList;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return paymentEntryList.size();
	}

	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		PaymentEntryData data = paymentEntryList.get(row);
		if (col == 0) {
			return data.getEntryNumber();
		} else if (col == 1) {
			return String.format("%,.2f", data.getPayableAmount());
		} else if (col == 2) {
			return String.format("%,.2f", data.getAmount());
		} else if (col == 3) {
			return String.format("%,.2f", data.getPayableAmount() - data.getAmount());
		} else if (col == 4) {
			return DateUtil.convertToString("dd-MMM-yyyy", data.getPaymentDate());
		} else if (col == 5) {
			
			return (data.getPaymentMode() != null ? data.getPaymentMode().getName() : "N/A");
		} else if (col == 6) {
			return (data.getChequeNumber());			
		} else if (col == 7) {
			return (data.isAdjusted() ? "Yes" : "No");
		}
		return "---";
	}

	public void setValueAt(Object value, int row, int col) {

	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public List<PaymentEntryData> getPaymentEntryList() {
		return paymentEntryList;
	}

	public void setPaymentEntryList(List<PaymentEntryData> paymentEntryList) {
		this.paymentEntryList = paymentEntryList;
	}

}
