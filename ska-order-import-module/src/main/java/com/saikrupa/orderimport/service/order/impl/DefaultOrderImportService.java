package com.saikrupa.orderimport.service.order.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.saikrupa.app.dao.ApplicationUserDAO;
import com.saikrupa.app.dao.ProductDAO;
import com.saikrupa.app.dao.impl.DefaultApplicationUserDAO;
import com.saikrupa.app.dao.impl.DefaultProductDAO;
import com.saikrupa.app.dto.ApplicationUserData;
import com.saikrupa.app.dto.DeliveryStatus;
import com.saikrupa.app.dto.InventoryData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.OrderStatus;
import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.dto.PaymentStatus;
import com.saikrupa.app.dto.ProductData;
import com.saikrupa.app.service.OrderService;
import com.saikrupa.app.service.impl.DefaultOrderService;
import com.saikrupa.app.session.ApplicationSession;
import com.saikrupa.orderimport.dto.CustomerData;
import com.saikrupa.orderimport.dto.DeliveryData;
import com.saikrupa.orderimport.dto.OrderData;
import com.saikrupa.orderimport.service.order.OrderImportService;

public class DefaultOrderImportService implements OrderImportService {

	public DefaultOrderImportService() {
	}

	public List<OrderData> getOrderDataFromFile(String filePath) {
		List<OrderData> orders = new ArrayList<OrderData>();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File(filePath));
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				if (row.getRowNum() == 0) {
					continue;
				}

				OrderData order = new OrderData();
				order.setRowIndex(row.getRowNum());
				CustomerData customer = new CustomerData();
				DeliveryData delivery = new DeliveryData();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getColumnIndex()) {
					case 0:
						customer.setName(cell.getStringCellValue());
						break;
					case 1:
						long contactNo = (long) cell.getNumericCellValue();
						customer.setContactNumber(contactNo);
						break;
					case 2:
						delivery.setLocation(cell.getStringCellValue());
						break;
					case 3:
						order.setProductCode(String.valueOf(cell.getNumericCellValue()));
						break;
					case 4:
						order.setQuantity(cell.getNumericCellValue());
						break;
					case 5:
						order.setUnitPrice(cell.getNumericCellValue());
						break;
					case 6:
						order.setTransportationCost(cell.getNumericCellValue());
						break;
					case 7:
						order.setDiscount(cell.getNumericCellValue());
						break;
					case 8:
						order.setOrderedDate(cell.getDateCellValue());
						break;
					case 9:
						delivery.setDeliveryDate(cell.getDateCellValue());
						break;
					case 10:						
						delivery.setVehicleNumber(cell.getStringCellValue());
						break;
					case 11:
						delivery.setChallanNumber(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					}
				}
				order.setCustomer(customer);
				order.setDelivery(delivery);
				orders.add(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Order Collected : " + orders.size());
		return orders;
	}

	public com.saikrupa.app.dto.OrderData createOrder(OrderData importOrderData) {
		com.saikrupa.app.dto.OrderData commerceOrder = new com.saikrupa.app.dto.OrderData();
		OrderEntryData entry = createOrderEntry(importOrderData);
		if (entry == null) {
			System.out.println("Order Entry could not be created.");
			return null;
		}
		double orderTotalPrice = (entry.getPrice() * entry.getOrderedQuantity()) + entry.getTransportationCost()
				- entry.getDiscount();
		commerceOrder.setTotalPrice(orderTotalPrice);

		com.saikrupa.app.dto.CustomerData commerceCustomer = new com.saikrupa.app.dto.CustomerData();
		commerceCustomer.setActive(true);
		commerceCustomer.setName(importOrderData.getCustomer().getName());
		commerceCustomer.setPrimaryContact(String.valueOf(importOrderData.getCustomer().getContactNumber()));
		if (importOrderData.getCustomer().isExistingCustomer()) {
			commerceCustomer.setCode(String.valueOf(importOrderData.getCustomer().getCode()));
		} else {
			commerceCustomer.setCode(null);
		}
		com.saikrupa.app.dto.AddressData deliveryAddress = new com.saikrupa.app.dto.AddressData();
		deliveryAddress.setLine1(importOrderData.getDelivery().getLocation());

		entry.setDeliveryAddress(deliveryAddress);
		commerceCustomer.setAddress(deliveryAddress);
		commerceCustomer.setBillingAddress(deliveryAddress);
		commerceOrder.setCustomer(commerceCustomer);

		commerceOrder.setPaymentStatus(PaymentStatus.PENDING);
		if (deliveryEntryAvailable(importOrderData.getDelivery())) {
			commerceOrder.setDeliveryStatus(DeliveryStatus.SHIPPED);
		} else {
			commerceOrder.setDeliveryStatus(DeliveryStatus.SHIPPING);
		}
		commerceOrder.setOrderStatus(OrderStatus.CREATED);
		OrderService orderService = new DefaultOrderService();
		entry.setOrder(commerceOrder);
		List<OrderEntryData> orderEntries = new ArrayList<OrderEntryData>();
		orderEntries.add(entry);
		commerceOrder.setOrderEntries(orderEntries);
		commerceOrder.setCreatedDate(importOrderData.getOrderedDate());

		ApplicationUserDAO userDao = new DefaultApplicationUserDAO();
		ApplicationUserData userData = userDao.findUserByCode("admin");
		ApplicationSession session = ApplicationSession.getSession();
		session.setCurrentUser(userData);
		com.saikrupa.app.dto.OrderData commerceCreatedOrder = orderService.createOrder(commerceOrder);

		if (commerceCreatedOrder.getPaymentStatus() == PaymentStatus.PAID) {
			commerceCreatedOrder.setOrderStatus(OrderStatus.CONFIRMED);
		}

		if (commerceCreatedOrder.getPaymentStatus() == PaymentStatus.PENDING
				|| commerceCreatedOrder.getPaymentStatus() == PaymentStatus.PARTIAL) {
			commerceCreatedOrder.setOrderStatus(OrderStatus.CREATED);
		}
		if (commerceCreatedOrder.getDeliveryStatus() == DeliveryStatus.SHIPPED) {
			commerceCreatedOrder.setOrderStatus(OrderStatus.DELIVERED);
		}

		if (commerceCreatedOrder.getDeliveryStatus() == DeliveryStatus.SHIPPED
				&& commerceCreatedOrder.getPaymentStatus() == PaymentStatus.PAID) {
			commerceCreatedOrder.setOrderStatus(OrderStatus.COMPLETED);
		}
		
		if(deliveryEntryAvailable(importOrderData.getDelivery())) {
			System.out.println("Updating Delivery Entry for Order : " + entry.getOrder().getCode());
			orderService.updateOrderStatus(commerceCreatedOrder);
		} else {
			System.out.println("Delivery Entry not Available as expected... Marking Order as CREATED.");
		}		
		return commerceCreatedOrder;
	}

	private OrderEntryData createOrderEntry(OrderData importOrderData) {
		ProductDAO pdao = new DefaultProductDAO();
		ProductData product = pdao.findProductByCode(importOrderData.getProductCode());
		InventoryData inventoryLevel = pdao.findInventoryLevelByProduct(product);
		if (inventoryLevel.getTotalAvailableQuantity() < importOrderData.getQuantity()) {
			System.out.println("Ordered Quantity [" + importOrderData.getQuantity() + "] exceeds Available Quantity ["
					+ inventoryLevel.getTotalAvailableQuantity() + "]");
			return null;
		}

		OrderEntryData entry = new OrderEntryData();
		entry.setEntryNumber(1);
		entry.setProduct(product);
		entry.setDiscount(importOrderData.getDiscount());
		entry.setEntryNote("Imported from File - Order entry : " + importOrderData.getRowIndex());
		entry.setOrderedQuantity(Double.valueOf(importOrderData.getQuantity()).intValue());
		entry.setPrice(importOrderData.getUnitPrice());
		entry.setTransportationCost(importOrderData.getTransportationCost());

		List<PaymentEntryData> paymentEntries = new ArrayList<PaymentEntryData>();
		entry.setPaymentEntries(paymentEntries);

		com.saikrupa.app.dto.DeliveryData deliveryEntry = new com.saikrupa.app.dto.DeliveryData();
		deliveryEntry.setActualDeliveryQuantity(entry.getOrderedQuantity());
		deliveryEntry.setDeliveryDate(importOrderData.getDelivery().getDeliveryDate());
		deliveryEntry.setDeliveryReceiptNo(importOrderData.getDelivery().getChallanNumber());
		deliveryEntry.setDeliveryVehicleNo(importOrderData.getDelivery().getVehicleNumber());
		deliveryEntry.setOrderEntryData(entry);
		entry.setDeliveryData(deliveryEntry);
		return entry;
	}

	private boolean deliveryEntryAvailable(DeliveryData importedDeliveryData) {		
		if (importedDeliveryData == null) {
			return false;
		}
		System.out.println("****************************** DELIVERY ENTRY DATA *************************");
		System.out.println("File Delivery Date : " + importedDeliveryData.getDeliveryDate());
		System.out.println("File Challan Number : " + importedDeliveryData.getChallanNumber());
		System.out.println("File Vehicle Number : " + importedDeliveryData.getVehicleNumber());
		System.out.println("****************************************************************************");

		if (importedDeliveryData.getDeliveryDate() == null || importedDeliveryData.getChallanNumber() == null
				|| importedDeliveryData.getVehicleNumber() == null) {
			return false;
		}
		return true;

	}

}
