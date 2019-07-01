package com.saikrupa.app.service;

import java.sql.Connection;
import java.util.List;

import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.PaymentEntryData;

public interface PaymentService {
	public void addPaymentEntryForOrderEntry(OrderEntryData orderEntryData, Connection connection) throws Exception;
	public void addAdhocPayment(String customerCode, List<PaymentEntryData> paymentEntries, PaymentEntryData customerPayment);
	public void updateOrderPaymentsForOrder(OrderData orderData, List<PaymentEntryData> newEntries, List<PaymentEntryData> existingPayments);
	public void updateInventoryEntryForPayment(List<InventoryEntryData> entries);
	
	
}
