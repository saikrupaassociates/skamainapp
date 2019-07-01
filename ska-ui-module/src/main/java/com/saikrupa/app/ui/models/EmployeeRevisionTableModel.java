package com.saikrupa.app.ui.models;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.saikrupa.app.dto.EmployeeSalaryData;
import com.saikrupa.app.dto.InvestmentData;
import com.saikrupa.app.dto.InvestorData;
import com.saikrupa.app.util.DateUtil;

public class EmployeeRevisionTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnNames = {"Revision #", "Date of Joining", "Salary", "Effective From", "Effective Till" };

	private List<EmployeeSalaryData> revisionDataList;

	public EmployeeRevisionTableModel(List<EmployeeSalaryData> revisionDataList) {
		this.revisionDataList = revisionDataList;	
	}

	

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return revisionDataList.size();
	}

	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		EmployeeSalaryData data = revisionDataList.get(row);
		if (col == 0) {
			return data.getCode();
		} else if (col == 1) {
			return DateUtil.convertToString("dd-MMM-yyyy", data.getEmployee().getJoiningDate());
		} else if (col == 2) {
			return String.format("%,.2f", data.getSalary());
		} else if (col == 3) {
			return DateUtil.convertToString("dd-MMM-yyyy", data.getEffectiveFrom());
		}  else if (col == 4) {
			return DateUtil.convertToString("dd-MMM-yyyy", data.getEffectiveTill());
		}
		return "---";
	}
	
	
	public void setValueAt(Object value, int row, int col) {
		
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}



	public List<EmployeeSalaryData> getRevisionDataList() {
		return revisionDataList;
	}



	public void setRevisionDataList(List<EmployeeSalaryData> revisionDataList) {
		this.revisionDataList = revisionDataList;
	}
}
