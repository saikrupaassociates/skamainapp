package com.saikrupa.orderimport.dto;

public class CustomerData {

	private String name;
	private long contactNumber;
	private int code;
	private boolean existingCustomer;

	public CustomerData() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String toString() {
		return "Customer [" + code + ", " + name + ", " + contactNumber + "]";
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isExistingCustomer() {
		return existingCustomer;
	}

	public void setExistingCustomer(boolean existingCustomer) {
		this.existingCustomer = existingCustomer;
	}

}
