package com.saikrupa.app.dto;

import java.util.Date;

public class EmployeeSalaryData {
	
	private String code;
	private EmployeeData employee;
	private double salary;
	private Date effectiveFrom;
	private Date effectiveTill;
	private boolean currentRevision;
	
	
	public EmployeeSalaryData() {
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public EmployeeData getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeData employee) {
		this.employee = employee;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTill() {
		return effectiveTill;
	}

	public void setEffectiveTill(Date effectiveTill) {
		this.effectiveTill = effectiveTill;
	}

	public boolean isCurrentRevision() {
		return currentRevision;
	}

	public void setCurrentRevision(boolean currentRevision) {
		this.currentRevision = currentRevision;
	}

}
