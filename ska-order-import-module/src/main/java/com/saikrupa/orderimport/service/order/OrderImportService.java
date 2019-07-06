package com.saikrupa.orderimport.service.order;

import java.util.List;

import com.saikrupa.orderimport.dto.OrderData;

public interface OrderImportService {
	public List<OrderData> getOrderDataFromFile(final String filePath);
	public com.saikrupa.app.dto.OrderData createOrder(OrderData importOrderData);
}
