package com.saikrupa.app.ui.models;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.saikrupa.app.dto.CustomerData;

public class CustomerTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnNames = { "Customer Name", "Contact No"};

	private List<CustomerData> customerDataList;
	private boolean showAddress;

	public CustomerTableModel(List<CustomerData> customerDataList, boolean showAddress) {
		this.customerDataList = customerDataList;	
		this.showAddress = showAddress;
	}

	

	public int getColumnCount() {
		if(!isShowAddress()) {
			return columnNames.length - 1;
		}
		return columnNames.length;
		
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return customerDataList.size();
	}

	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		CustomerData data = customerDataList.get(row);
		if (col == 0) {
			return data.getName();
		} else if (col == 1) {
			if(data.getPrimaryContact().isEmpty()) {
				return "N/A";
			}			
			return data.getPrimaryContact();
		} 
		return "---";
	}


	public void setValueAt(Object value, int row, int col) {
		
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}



	public List<CustomerData> getCustomerDataList() {
		return customerDataList;
	}



	public void setCustomerDataList(List<CustomerData> customerDataList) {
		this.customerDataList = customerDataList;
	}



	public boolean isShowAddress() {
		return showAddress;
	}



	public void setShowAddress(boolean showAddress) {
		this.showAddress = showAddress;
	}

	

}
