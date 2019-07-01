package com.saikrupa.app.dao;

import java.sql.Timestamp;
import java.util.List;

import com.saikrupa.app.dto.DeliveryData;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.PaymentEntryData;

public interface OrderDAO {
	public List<OrderData> findAllOrders();
	public List<OrderData> findOrdersByCustomer(Integer customerCode);
	public List<OrderData> findOrdersByCustomerByDateRange(Integer customerCode, Timestamp startDate, Timestamp endDate);
	public List<OrderData> searchOrderWithFilter(final String whereCondition, Object[] params);
	public DeliveryData findDeliveryDetailForEntry(OrderEntryData entry);	
	public List<PaymentEntryData> getPaymentEntries(OrderEntryData entry);
	public OrderData findOrderByCode(final int orderCode);
}
