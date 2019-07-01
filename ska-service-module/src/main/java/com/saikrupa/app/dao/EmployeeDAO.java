package com.saikrupa.app.dao;

import java.util.List;

import com.saikrupa.app.dto.EmployeeData;
import com.saikrupa.app.dto.EmployeeSalaryData;

public interface EmployeeDAO {
	public List<EmployeeData> getAllEmployees();
	public EmployeeData findEmployeeByCode(final String code);
	public List<EmployeeSalaryData> findRevisionsByEmployee(EmployeeData employee);
	
}
