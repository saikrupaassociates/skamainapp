package com.saikrupa.app.ui.models;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.saikrupa.app.dto.InvestmentData;
import com.saikrupa.app.dto.InvestorData;

public class InvestorTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnNames = { "Name", "Primary Contact", "Total Investment", "Currency" };

	private List<InvestorData> investorDataList;

	public InvestorTableModel(List<InvestorData> investorDataList) {
		this.investorDataList = investorDataList;	
	}

	

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return investorDataList.size();
	}

	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		InvestorData data = investorDataList.get(row);
		if (col == 0) {
			return data.getName();
		} else if (col == 1) {
			if(data.getPrimaryContact() == null || data.getPrimaryContact().trim().length() < 1) {
				return "N/A";
			}			
			return data.getPrimaryContact();
		} else if (col == 2) {
			return String.format("%,.2f", getTotalInvestment(data));
		} else if (col == 3) {
			return "INR";
		}
		return "---";
	}
	
	private Double getTotalInvestment(InvestorData data) {
		double amount = 0;
		for(InvestmentData investment : data.getInvestments()) {
			amount = amount + investment.getAmount();
		}
		return Double.valueOf(amount);
	}

	public void setValueAt(Object value, int row, int col) {
		
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}



	public List<InvestorData> getInvestorDataList() {
		return investorDataList;
	}



	public void setInvestorDataList(List<InvestorData> investorDataList) {
		this.investorDataList = investorDataList;
	}

	
}
