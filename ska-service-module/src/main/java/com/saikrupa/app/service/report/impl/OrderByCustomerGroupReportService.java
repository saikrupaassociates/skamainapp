package com.saikrupa.app.service.report.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.saikrupa.app.dto.Customer2OrderData;
import com.saikrupa.app.dto.CustomerData;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.dto.PaymentStatus;
import com.saikrupa.app.service.report.common.CellValueType;
import com.saikrupa.app.service.report.component.ReportTableDataCell;
import com.saikrupa.app.service.report.component.ReportTableHeaderCell;

public class OrderByCustomerGroupReportService extends AbstractReportService {
	
	private List<OrderData> orders;
	
	public OrderByCustomerGroupReportService(List<OrderData> orders) {
		setOrders(orders);
	}
	
	

	public void saveReport(String reportName) {
		Document pdfDocument = createBlankDocument(reportName);
		pdfDocument.close();
		
	}

	@Override
	public void createPageTableContent(Document document) throws DocumentException {
		Map<String, Customer2OrderData> reportMap = new HashMap<String, Customer2OrderData>();
		for(OrderData order : orders) {
			CustomerData customer = order.getCustomer();
			if(reportMap.containsKey(customer.getCode())) {
				Customer2OrderData c2oData = reportMap.get(customer.getCode());
				c2oData.getOrders().add(order);				
			} else {
				Customer2OrderData newData = new Customer2OrderData();
				newData.setCustomer(customer);
				List<OrderData> orderList = new ArrayList<OrderData>();
				orderList.add(order);
				newData.setOrders(orderList);
				reportMap.put(customer.getCode(), newData);
			}			
		}
		
		PdfPTable outerTable = new PdfPTable(1);
		outerTable.setWidthPercentage(100F);
		
		
		for (Map.Entry<String,Customer2OrderData> entry : reportMap.entrySet()) {
			if(entry.getValue().getOrders().isEmpty()) {
				continue;
			}
			PdfPTable consolidationTable = new PdfPTable(4);
			ReportTableHeaderCell customerCodeLabel = new ReportTableHeaderCell("Customer #");
			ReportTableHeaderCell customerNameLabel = new ReportTableHeaderCell("Customer Name");
			ReportTableHeaderCell customerContactLabel = new ReportTableHeaderCell("Contact Number");
			ReportTableHeaderCell totalOrderLabel = new ReportTableHeaderCell("Total Order Amount");
			ReportTableHeaderCell receivedPaymentLabel = new ReportTableHeaderCell("Payment Received");
			ReportTableHeaderCell pendingPaymentLabel = new ReportTableHeaderCell("Balance");			
			ReportTableHeaderCell orderCountLabel = new ReportTableHeaderCell("Order Count");			

			
			ReportTableDataCell customerCodeValue = new ReportTableDataCell(entry.getValue().getCustomer().getCode(), CellValueType.TEXT, true);
			ReportTableDataCell customerContactValue = new ReportTableDataCell(entry.getValue().getCustomer().getPrimaryContact(), CellValueType.TEXT, true);
			ReportTableDataCell customerNameValue = new ReportTableDataCell(entry.getValue().getCustomer().getName(), CellValueType.TEXT, true);
			
			Map<String, Double> orderMap = getOrderValues(entry.getValue().getOrders());			
			ReportTableDataCell totalOrderValue = new ReportTableDataCell(orderMap.get("TOTAL_ORDER_VALUE"), CellValueType.AMOUNT, true);
			ReportTableDataCell totalPaidValue = new ReportTableDataCell(orderMap.get("TOTAL_ORDER_PAID"), CellValueType.AMOUNT, true);
			ReportTableDataCell totalPendingValue = new ReportTableDataCell(orderMap.get("TOTAL_ORDER_PENDING"), CellValueType.AMOUNT, true);
			ReportTableDataCell orderCountValue = new ReportTableDataCell(String.valueOf(orderMap.get("TOTAL_ORDER_COUNT").intValue()), CellValueType.TEXT, true);
			
			consolidationTable.addCell(customerCodeLabel);			
			consolidationTable.addCell(customerCodeValue);
			
			
			consolidationTable.addCell(customerNameLabel);			
			consolidationTable.addCell(customerNameValue);
			
			
			
			consolidationTable.addCell(customerContactLabel);
			consolidationTable.addCell(customerContactValue);
			
			consolidationTable.addCell(orderCountLabel);
			consolidationTable.addCell(orderCountValue);

			
			
			consolidationTable.addCell(totalOrderLabel);
			consolidationTable.addCell(totalOrderValue);
			
			consolidationTable.addCell(receivedPaymentLabel);
			consolidationTable.addCell(totalPaidValue);			
		
			consolidationTable.addCell(pendingPaymentLabel);
			totalPendingValue.setColspan(3);
			consolidationTable.addCell(totalPendingValue);
			
			PdfPTable dataTable = new PdfPTable(10);
			dataTable.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.getDefaultCell().setBorder(2);

			dataTable.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
			dataTable.setWidthPercentage(100F);
			
			ReportTableHeaderCell header_OrderNo = new ReportTableHeaderCell("#");
			ReportTableHeaderCell header_OrderDate = new ReportTableHeaderCell("Date");
			ReportTableHeaderCell header_OrderBy = new ReportTableHeaderCell("Unit Price");
			ReportTableHeaderCell orderQuantity = new ReportTableHeaderCell("Quantity");
			ReportTableHeaderCell location = new ReportTableHeaderCell("Location");
			ReportTableHeaderCell orderDelivery = new ReportTableHeaderCell("Transport");
			ReportTableHeaderCell orderDiscount = new ReportTableHeaderCell("Discount");
			ReportTableHeaderCell amount = new ReportTableHeaderCell("Order Total");
			ReportTableHeaderCell paymentStatus = new ReportTableHeaderCell("Payment");
			ReportTableHeaderCell deliveryStatus = new ReportTableHeaderCell("Delivery");
			
			dataTable.addCell(header_OrderNo);
			dataTable.addCell(header_OrderDate);
			dataTable.addCell(header_OrderBy);
			dataTable.addCell(orderQuantity);
			dataTable.addCell(location);
			dataTable.addCell(orderDelivery);
			dataTable.addCell(orderDiscount);
			dataTable.addCell(amount);
			dataTable.addCell(paymentStatus);
			dataTable.addCell(deliveryStatus);	
			
			for(OrderData data : entry.getValue().getOrders()) {
				ReportTableDataCell data_OrderNo = new ReportTableDataCell(String.valueOf(data.getCode()), CellValueType.TEXT);
				ReportTableDataCell data_OrderDate = new ReportTableDataCell(data.getCreatedDate(), CellValueType.DATE);
				ReportTableDataCell data_unitPrice = new ReportTableDataCell(data.getOrderEntries().get(0).getPrice(), CellValueType.AMOUNT);
				ReportTableDataCell data_quantity = new ReportTableDataCell(Double.valueOf(data.getOrderEntries().get(0).getOrderedQuantity()), CellValueType.QUANTITY);
				ReportTableDataCell data_delivery = new ReportTableDataCell(Double.valueOf(data.getOrderEntries().get(0).getTransportationCost()), CellValueType.AMOUNT);
				ReportTableDataCell data_discount = new ReportTableDataCell(Double.valueOf(data.getOrderEntries().get(0).getDiscount()), CellValueType.AMOUNT);
				ReportTableDataCell data_amount = new ReportTableDataCell(data.getTotalPrice(), CellValueType.AMOUNT);			
				ReportTableDataCell data_paymentStatus = new ReportTableDataCell(data.getPaymentStatus().toString(), CellValueType.TEXT);
				ReportTableDataCell data_deliveryLocation = new ReportTableDataCell(data.getOrderEntries().get(0).getDeliveryAddress().getLine1(), CellValueType.TEXT);
				String delivery = data.getDeliveryStatus().toString();
				ReportTableDataCell data_deliveryStatus = new ReportTableDataCell(delivery, CellValueType.TEXT);
				data_deliveryStatus.setColspan(2);
				
				dataTable.addCell(data_OrderNo);
				dataTable.addCell(data_OrderDate);
				dataTable.addCell(data_unitPrice);
				dataTable.addCell(data_quantity);
				dataTable.addCell(data_deliveryLocation);
				dataTable.addCell(data_delivery);
				dataTable.addCell(data_discount);
				dataTable.addCell(data_amount);
				dataTable.addCell(data_paymentStatus);
				dataTable.addCell(data_deliveryStatus);
			}	

			
			outerTable.addCell(consolidationTable);
			outerTable.addCell(dataTable);
			
			PdfPTable dummyTable = new PdfPTable(4);
			
			dummyTable.setWidthPercentage(100F);
			ReportTableDataCell data_dummy = new ReportTableDataCell("              ", CellValueType.TEXT);
			data_dummy.setBorder(0);
			data_dummy.setColspan(4);
			dummyTable.addCell(data_dummy);
			outerTable.addCell(dummyTable);
		}
		document.add(outerTable);	
	}	
	
	private Map<String, Double> getOrderTotalMap(List<OrderData> orders) {
		double totalValue = 0;
		double totalPaid = 0;		
		for(OrderData data : orders) {
			totalValue = totalValue + data.getTotalPrice();
			if(data.getPaymentStatus() == PaymentStatus.PAID || data.getPaymentStatus() == PaymentStatus.PARTIAL){
				totalPaid = totalPaid + data.getTotalPrice();
			}
		}
		HashMap<String, Double> map = new HashMap<String, Double>();
		map.put("TOTAL_ORDER_VALUE", totalValue);
		map.put("TOTAL_ORDER_PAID", totalPaid);
		map.put("TOTAL_ORDER_PENDING", totalValue - totalPaid);
		map.put("TOTAL_ORDER_COUNT", Double.valueOf(String.valueOf(orders.size())));
		return map;
	}
	
	private Map<String, Double> getOrderValues(List<OrderData> orderList) {
		Double total = 0.0;
		Double paid = 0.0;
		Double orderCount = 0.0;
		Double pending = 0.0;
		

		for (OrderData orderData : orderList) {
			orderCount = orderCount + 1;
			total = total + orderData.getTotalPrice();
			OrderEntryData entry = orderData.getOrderEntries().get(0);
			for (PaymentEntryData paymentEntry : entry.getPaymentEntries()) {
				if (paymentEntry.getPaymentStatus() == PaymentStatus.PAID
						|| paymentEntry.getPaymentStatus() == PaymentStatus.PARTIAL) {
					paid = paid + paymentEntry.getAmount();
				}
			}
		}
		pending = total - paid;
		HashMap<String, Double> map = new HashMap<String, Double>();
		map.put("TOTAL_ORDER_VALUE", total);
		map.put("TOTAL_ORDER_PAID", paid);
		map.put("TOTAL_ORDER_PENDING", pending);
		map.put("TOTAL_ORDER_COUNT", orderCount);
		return map;
		
	}
	

	@Override
	public void addMetaData(Document document) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPageTitle() {
		return "Order Report By Customers";
	}

	public List<OrderData> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderData> orders) {
		this.orders = orders;
	}

}
