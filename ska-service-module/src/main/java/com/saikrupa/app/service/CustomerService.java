package com.saikrupa.app.service;

import java.util.List;

import com.saikrupa.app.dto.AddressData;
import com.saikrupa.app.dto.CustomerData;

public interface CustomerService {
	public List<CustomerData> getAllCustomers();
	public AddressData getCustomerDeliveryAddressByCode(final String customerCode);
	public CustomerData getCustomerByCode(final String customerCode);
	public List<CustomerData> getAllCustomersByOrders();
	public List<String> getDeliveryLocationByCustomer(final String customerCode);
	public List<CustomerData> findCustomersBySearchString(final String text);
}
