package com.saikrupa.app.service.report.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.dto.PaymentStatus;
import com.saikrupa.app.service.report.common.CellValueType;
import com.saikrupa.app.service.report.component.ReportTableDataCell;
import com.saikrupa.app.service.report.component.ReportTableHeaderCell;

public class CustomerOrderReportService extends AbstractReportService {

	private List<OrderData> orders;

	public CustomerOrderReportService(List<OrderData> orders) {
		setOrders(orders);
	}

	public void saveReport(final String reportName) {
		Document pdfDocument = createBlankDocument(reportName);
		pdfDocument.close();
		System.out.println("Done !!!");
	}

	@Override
	public void createPageTableContent(Document document) throws DocumentException {
		PdfPTable dataTable = new PdfPTable(9);
		dataTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		dataTable.getDefaultCell().setBorder(2);

		dataTable.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
		dataTable.setWidthPercentage(100F);
		ReportTableHeaderCell header_OrderNo = new ReportTableHeaderCell("#");
		ReportTableHeaderCell header_OrderDate = new ReportTableHeaderCell("Date");
		ReportTableHeaderCell header_OrderBy = new ReportTableHeaderCell("Unit Price");
		ReportTableHeaderCell orderQuantity = new ReportTableHeaderCell("Quantity");
		ReportTableHeaderCell orderDelivery = new ReportTableHeaderCell("Transport");
		ReportTableHeaderCell orderDiscount = new ReportTableHeaderCell("Discount");
		ReportTableHeaderCell amount = new ReportTableHeaderCell("Order Total");
		ReportTableHeaderCell paymentStatus = new ReportTableHeaderCell("Payment");
		ReportTableHeaderCell deliveryStatus = new ReportTableHeaderCell("Delivery");

		dataTable.addCell(header_OrderNo);
		dataTable.addCell(header_OrderDate);
		dataTable.addCell(header_OrderBy);
		dataTable.addCell(orderQuantity);
		dataTable.addCell(orderDelivery);
		dataTable.addCell(orderDiscount);
		dataTable.addCell(amount);
		dataTable.addCell(paymentStatus);
		dataTable.addCell(deliveryStatus);

		for (OrderData data : getOrders()) {
			ReportTableDataCell data_OrderNo = new ReportTableDataCell(String.valueOf(data.getCode()),
					CellValueType.TEXT);
			ReportTableDataCell data_OrderDate = new ReportTableDataCell(data.getCreatedDate(), CellValueType.DATE);
			ReportTableDataCell data_OrderedBy = new ReportTableDataCell(data.getOrderEntries().get(0).getPrice(),
					CellValueType.AMOUNT);
			ReportTableDataCell data_quantity = new ReportTableDataCell(
					Double.valueOf(data.getOrderEntries().get(0).getOrderedQuantity()), CellValueType.QUANTITY);
			ReportTableDataCell data_delivery = new ReportTableDataCell(
					Double.valueOf(data.getOrderEntries().get(0).getTransportationCost()), CellValueType.AMOUNT);
			ReportTableDataCell data_discount = new ReportTableDataCell(
					Double.valueOf(data.getOrderEntries().get(0).getDiscount()), CellValueType.AMOUNT);
			ReportTableDataCell data_amount = new ReportTableDataCell(data.getTotalPrice(), CellValueType.AMOUNT);
			ReportTableDataCell data_paymentStatus = new ReportTableDataCell(data.getPaymentStatus().toString(),
					CellValueType.TEXT);
			String delivery = data.getDeliveryStatus().toString();
			ReportTableDataCell data_deliveryStatus = new ReportTableDataCell(delivery, CellValueType.TEXT);
			data_deliveryStatus.setColspan(2);

			dataTable.addCell(data_OrderNo);
			dataTable.addCell(data_OrderDate);
			dataTable.addCell(data_OrderedBy);
			dataTable.addCell(data_quantity);
			dataTable.addCell(data_delivery);
			dataTable.addCell(data_discount);
			dataTable.addCell(data_amount);
			dataTable.addCell(data_paymentStatus);
			dataTable.addCell(data_deliveryStatus);
		}

		PdfPTable consolidationTable = new PdfPTable(5);
		ReportTableHeaderCell totalOrder = new ReportTableHeaderCell("Total Order Amount");
		ReportTableHeaderCell receivedPayment = new ReportTableHeaderCell("Amount Paid");
		ReportTableHeaderCell pendingPayment = new ReportTableHeaderCell("Amount Pending");
		ReportTableHeaderCell customerContact = new ReportTableHeaderCell("Contact Number");

		ReportTableDataCell totalOrderValue = new ReportTableDataCell(getOrderValues().get("TOTAL_ORDER_VALUE"),
				CellValueType.AMOUNT, true);
		ReportTableDataCell paidValue = new ReportTableDataCell(getOrderValues().get("TOTAL_ORDER_PAID"),
				CellValueType.AMOUNT, true);
		ReportTableDataCell pendingValue = new ReportTableDataCell(getOrderValues().get("TOTAL_ORDER_PENDING"),
				CellValueType.AMOUNT, true);
		ReportTableDataCell contactValue = new ReportTableDataCell(getOrders().get(0).getCustomer().getPrimaryContact(),
				CellValueType.TEXT, true);

		consolidationTable.addCell(totalOrder);
		totalOrderValue.setColspan(5);
		consolidationTable.addCell(totalOrderValue);

		consolidationTable.addCell(receivedPayment);
		paidValue.setColspan(5);
		consolidationTable.addCell(paidValue);

		consolidationTable.addCell(pendingPayment);
		pendingValue.setColspan(2);
		consolidationTable.addCell(pendingValue);

		consolidationTable.addCell(customerContact);
		pendingValue.setColspan(2);
		consolidationTable.addCell(contactValue);

		PdfPTable outerTable = new PdfPTable(1);
		outerTable.setWidthPercentage(100F);
		outerTable.addCell(consolidationTable);
		outerTable.addCell(dataTable);
		document.add(outerTable);
	}

	private Map<String, Double> getOrderTotalMap() {
		double totalValue = 0;
		double totalPaid = 0;
		for (OrderData data : getOrders()) {
			totalValue = totalValue + data.getTotalPrice();
			if (data.getPaymentStatus() == PaymentStatus.PAID) {
				totalPaid = totalPaid + data.getTotalPrice();
			}
			if (data.getPaymentStatus() == PaymentStatus.PARTIAL) {
				List<PaymentEntryData> paymentEntries = data.getOrderEntries().get(0).getPaymentEntries();
				for (PaymentEntryData paymentEntry : paymentEntries) {
					if (paymentEntry.getPaymentStatus() == PaymentStatus.PARTIAL) {
						System.out.println("PaymentStatus.PARTIAL :: " + data.getCode());
						System.out.println("Payable Amount : " + paymentEntry.getPayableAmount());
						System.out.println("Paid Amount : " + paymentEntry.getAmount());
					}
				}
			}

		}
		HashMap<String, Double> map = new HashMap<String, Double>();
		map.put("TOTAL_ORDER_VALUE", totalValue);
		map.put("TOTAL_ORDER_PAID", totalPaid);
		map.put("TOTAL_ORDER_PENDING", totalValue - totalPaid);
		return map;
	}

	private Map<String, Double> getOrderValues() {
		Double total = 0.0;
		Double paid = 0.0;
		Double orderCount = 0.0;
		Double pending = 0.0;

		for (OrderData orderData : getOrders()) {
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
		document.addTitle("Consolidated Order Report By Customer");

	}

	@Override
	public String getPageTitle() {
		return "Order Report By Customer - " + getOrders().get(0).getCustomer().getName();
	}

	public List<OrderData> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderData> orders) {
		this.orders = orders;
	}
}
