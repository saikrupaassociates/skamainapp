package com.saikrupa.app.dto;

import java.util.List;

public class Customer2OrderData {
	private CustomerData customer;
	private List<OrderData> orders;

	public CustomerData getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerData customer) {
		this.customer = customer;
	}

	public List<OrderData> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderData> orders) {
		this.orders = orders;
	}
}
