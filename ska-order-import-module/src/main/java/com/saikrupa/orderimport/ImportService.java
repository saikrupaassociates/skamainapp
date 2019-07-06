package com.saikrupa.orderimport;

import java.util.List;

import com.saikrupa.app.dao.impl.CustomerDAO;
import com.saikrupa.app.dao.impl.DefaultCustomerDAO;
import com.saikrupa.app.dto.CustomerData;
import com.saikrupa.importservice.product.ProductImportService;
import com.saikrupa.importservice.product.impl.DefaultProductImportService;
import com.saikrupa.orderimport.dto.OrderData;
import com.saikrupa.orderimport.dto.ProductImportData;
import com.saikrupa.orderimport.service.order.OrderImportService;
import com.saikrupa.orderimport.service.order.impl.DefaultOrderImportService;

public class ImportService {
	
	private static final String TYPE_INVENTORY = "INVENTORY";
	private static final String TYPE_ORDER = "ORDER";

	public ImportService(final String type, final String filePath) {
		if(type == null) {
			System.out.println("ERROR - Invalid operation type specified. \nUsage : java -jar ska-order-import-module-0.0.1-R1.jar <INVENTORY or ORDER>");
			return;
		}
		if(type.toLowerCase().equals(TYPE_INVENTORY.toLowerCase())) {
			System.out.println("INFO - Processing INVENTORY import...");
			processInventoryImport(filePath);
		} else if(type.toLowerCase().equals(TYPE_ORDER.toLowerCase())) {
			System.out.println("INFO - Processing ORDER import...");
			processOrderImport(filePath);
		} else {
			System.out.println("ERROR - Invalid operation type specified. \nUsage : java -jar ska-order-import-module-0.0.1-R1.jar <PRODUCT or ORDER>");
			return;
		}
		
		
	}

	private void processInventoryImport(String filePath) {
		ProductImportService service = new DefaultProductImportService();
		List<ProductImportData> products = service.getProductDataFromFile(filePath);
		if(products.isEmpty()) {
			System.out.println("INFO - No Products available to Process.");
			return;
		}
		executeProductImport(products);
	}

	private void executeProductImport(List<ProductImportData> products) {
		ProductImportService service = new DefaultProductImportService();
		service.importProductInventory(products);		
	}

	private void processOrderImport(String filePath) {
		OrderImportService importService = new DefaultOrderImportService();
		List<OrderData> orders = importService.getOrderDataFromFile(filePath);
		if(orders == null || orders.isEmpty()) {
			System.out.println("No Order entries available to import -- File ["+filePath+"]");
			return;
		}
		executeOrderImport(orders);		
	}

	private void executeOrderImport(List<OrderData> orders) {
		CustomerDAO customerDao = new DefaultCustomerDAO();
		for(OrderData order : orders) {
			final String customerName = order.getCustomer().getName();
			CustomerData existingCustomerData = customerDao.lookupCustomerByName(customerName);
			if(existingCustomerData == null) {
				System.out.println("No Customer available with Name : "+customerName+". Creating...");
				order.getCustomer().setExistingCustomer(Boolean.FALSE);
			} else {
				order.getCustomer().setExistingCustomer(Boolean.TRUE);
				order.getCustomer().setCode(Integer.valueOf(existingCustomerData.getCode()));
			}		
			createOrder(order);
		}
	}

	private void createOrder(OrderData order) {
		OrderImportService importService = new DefaultOrderImportService();
		com.saikrupa.app.dto.OrderData commerceOrder = importService.createOrder(order);
		if(commerceOrder == null) {
			System.out.println("Failed to Import Order");
		} else {
			System.out.println("Order ["+commerceOrder.getCode()+"] created successfully");
		}
	}

	public static void main(String[] args) {
		ImportService oi = new ImportService(args[0], "F:/prasun/OrderImport.xlsx");
	}

}
