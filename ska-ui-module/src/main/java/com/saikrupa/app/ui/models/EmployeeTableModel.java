package com.saikrupa.app.ui.models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.saikrupa.app.dto.EmployeeData;
import com.saikrupa.app.dto.EmployeeSalaryData;
import com.saikrupa.app.util.DateUtil;

public class EmployeeTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnNames = {"Code", "Name", "Contact No", "Joining Date", "Salary" };

	private List<EmployeeData> employeeDataList;

	public EmployeeTableModel(List<EmployeeData> employeeDataList) {
		setEmployeeDataList(employeeDataList);
	}

	

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return employeeDataList.size();
	}

	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		EmployeeData data = employeeDataList.get(row);
		if (col == 0) {
			return data.getCode();
		} else if (col == 1) {
			return data.getName();
		} else if (col == 2) {
			return data.getPrimaryContactNo();
		} else if (col == 3) {
			return DateUtil.convertToString("dd-MMM-yyyy", data.getJoiningDate());
		} else if (col == 4) {
			return getCurrentSalary(data);
		}
		return "---";
	}

	public void setValueAt(Object value, int row, int col) {
		
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}



	public List<EmployeeData> getEmployeeDataList() {
		return employeeDataList;
	}

	public void setEmployeeDataList(List<EmployeeData> employeeDataList) {
		this.employeeDataList = employeeDataList;
	}
	
	private double getCurrentSalary(EmployeeData employee) {
		if(!employee.isActive() || employee.getRevisions().isEmpty()) {
			return Double.valueOf(0).doubleValue();
		}
		Date currentDate = Calendar.getInstance().getTime();
		
		for(EmployeeSalaryData revision : employee.getRevisions()) {
			boolean checked = false;
			Date effectiveFromDate = revision.getEffectiveFrom();
			Date effectiveTillDate = revision.getEffectiveTill();
			
			if(effectiveFromDate.compareTo(currentDate) < 0 && effectiveTillDate.compareTo(currentDate) > 0) {
				/**
				 * Current Date falls between revision effective Start Date and Till Date
				 */
				checked = true;
			}
			if(revision.isCurrentRevision() && checked) {
				return revision.getSalary();
			}
						
		}
		return 0;
	}

	

}
