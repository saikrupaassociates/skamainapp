package com.saikrupa.orderimport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.saikrupa.app.dao.DefaultPaymentModeDAO;
import com.saikrupa.app.dao.ExpenseTypeDAO;
import com.saikrupa.app.dao.InvestorDAO;
import com.saikrupa.app.dao.PaymentModeDAO;
import com.saikrupa.app.dao.impl.CustomerDAO;
import com.saikrupa.app.dao.impl.DefaultCustomerDAO;
import com.saikrupa.app.dao.impl.DefaultExpenseTypeDAO;
import com.saikrupa.app.dao.impl.DefaultInvestorDAO;
import com.saikrupa.app.dto.CustomerData;
import com.saikrupa.app.dto.ExpenseData;
import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.dto.PaymentTypeData;
import com.saikrupa.app.dto.VendorData;
import com.saikrupa.app.service.ExpenseService;
import com.saikrupa.app.service.VendorService;
import com.saikrupa.app.service.impl.DefaultExpenseService;
import com.saikrupa.app.service.impl.DefaultVendorService;
import com.saikrupa.importservice.expense.ExpenseImportService;
import com.saikrupa.importservice.expense.impl.DefaultExpenseImportService;
import com.saikrupa.importservice.product.ProductImportService;
import com.saikrupa.importservice.product.impl.DefaultProductImportService;
import com.saikrupa.orderimport.dto.ExpenseImportData;
import com.saikrupa.orderimport.dto.OrderData;
import com.saikrupa.orderimport.dto.ProductImportData;
import com.saikrupa.orderimport.service.order.OrderImportService;
import com.saikrupa.orderimport.service.order.impl.DefaultOrderImportService;

public class ImportService {
	
	private static final String TYPE_INVENTORY = "INVENTORY";
	private static final String TYPE_ORDER = "ORDER";
	private static final String TYPE_EXPENSE = "EXPENSE";
	
	private String type;
	private String filePath;
	
	private static final Logger LOG = Logger.getLogger(ImportService.class);
	
	

	public ImportService(final String type, final String filePath) {
		if(type == null) {
			LOG.warn("Invalid operation type specified. \nUsage : java -jar ska-order-import-module-0.0.1-R1.jar <INVENTORY or ORDER>");
			return;
		}
		if(type.toLowerCase().equals(TYPE_INVENTORY.toLowerCase())) {
			setType(TYPE_INVENTORY);
		} else if(type.toLowerCase().equals(TYPE_ORDER.toLowerCase())) {
			setType(TYPE_ORDER);					
		} else if(type.toLowerCase().equals(TYPE_EXPENSE.toLowerCase())) {
			setType(TYPE_EXPENSE);
		} else {
			LOG.warn("Invalid operation type specified. \nUsage : java -jar ska-order-import-module-0.0.1-R1.jar <PRODUCT or ORDER>");
			return;
		}
		setFilePath(filePath);
		
	}

	public List<InventoryEntryData> processInventoryImport() {
		LOG.info("INFO - Processing INVENTORY import...");
		ProductImportService service = new DefaultProductImportService();
		List<ProductImportData> products = service.getProductDataFromFile(getFilePath());
		if(products.isEmpty()) {
			LOG.info("No Inventory entries available to import -- File ["+getFilePath()+"]");
			return Collections.emptyList();
		}
		return executeProductImport(products);
	}

	private List<InventoryEntryData> executeProductImport(List<ProductImportData> products) {
		ProductImportService service = new DefaultProductImportService();
		return service.importProductInventory(products);		
	}

	public List<com.saikrupa.app.dto.OrderData> processOrderImport() {
		LOG.info("INFO - Processing ORDER import...");
		OrderImportService importService = new DefaultOrderImportService();
		List<OrderData> orders = importService.getOrderDataFromFile(getFilePath());
		if(orders == null || orders.isEmpty()) {
			LOG.info("No Order entries available to import -- File ["+getFilePath()+"]");
			return Collections.emptyList();
		}
		return executeOrderImport(orders);		
	}
	
	public List<com.saikrupa.app.dto.ExpenseData> processExpenseImport() {
		LOG.info("INFO - Processing Expense import...");
		ExpenseImportService service = new DefaultExpenseImportService();
		List<ExpenseImportData> expenses = service.getExpenseDataFromFile(getFilePath());
		
		if(expenses == null || expenses.isEmpty()) {
			LOG.info("No Order entries available to import -- File ["+getFilePath()+"]");
			return Collections.emptyList();
		}
		return executeExpenseImport(expenses);		

	}

	private List<ExpenseData> executeExpenseImport(List<ExpenseImportData> expenses) {
		ExpenseService service = new DefaultExpenseService();
		VendorService vendorService = new DefaultVendorService();
		PaymentModeDAO paymentModeDao = new DefaultPaymentModeDAO();
		ExpenseTypeDAO expenseTypeDAO = new DefaultExpenseTypeDAO();
		InvestorDAO investorDAO = new DefaultInvestorDAO();
		List<ExpenseData> importedExpenseList = new ArrayList<ExpenseData>();
		for(ExpenseImportData expenseImportData : expenses) {
			VendorData vendorData = vendorService.findVendorByCode(String.valueOf(expenseImportData.getVendorCode()));
			PaymentTypeData paymentTypeData = paymentModeDao.getPaymentModeByCode(String.valueOf(expenseImportData.getPaymentMode()));
			
			ExpenseData newExpense = new ExpenseData();			
			newExpense.setCreatedDate(expenseImportData.getExpenseDate());
			newExpense.setExpenseType(expenseTypeDAO.findExpenseTypeByCode(String.valueOf(expenseImportData.getExpenseCategoryCode())));			
			newExpense.setVendor(vendorData);
			newExpense.setAmount(expenseImportData.getAmount());
			newExpense.setExpenseDate(expenseImportData.getPaymentDate());
			newExpense.setPaymentData(paymentTypeData);
			newExpense.setComments(expenseImportData.getRemarks());
			
			newExpense.setPaidBy(investorDAO.findInvestorByCode("1000"));
			
			try {
				ExpenseData createdExpenseData = service.createExpense(newExpense);
				importedExpenseList.add(createdExpenseData);
			} catch(Exception e) {
				e.printStackTrace();
			}			
		}
		LOG.info("Total Expense Imported : "+importedExpenseList.size());
		return importedExpenseList;
	}

	private List<com.saikrupa.app.dto.OrderData> executeOrderImport(List<OrderData> orders) {
		CustomerDAO customerDao = new DefaultCustomerDAO();
		List<com.saikrupa.app.dto.OrderData> newOrderList = new ArrayList<com.saikrupa.app.dto.OrderData>();
		for(OrderData order : orders) {
			final String customerName = order.getCustomer().getName();
			CustomerData existingCustomerData = customerDao.lookupCustomerByName(customerName);
			if(existingCustomerData == null) {
				LOG.info("No Customer available with Name : "+customerName+". Creating one...");
				order.getCustomer().setExistingCustomer(Boolean.FALSE);
			} else {
				order.getCustomer().setExistingCustomer(Boolean.TRUE);
				order.getCustomer().setCode(Integer.valueOf(existingCustomerData.getCode()));
			}		
			com.saikrupa.app.dto.OrderData newOrder = createOrder(order);
			if(newOrder != null) {
				newOrderList.add(newOrder);
			}
		}
		return newOrderList;
	}

	private com.saikrupa.app.dto.OrderData createOrder(OrderData order) {
		OrderImportService importService = new DefaultOrderImportService();
		com.saikrupa.app.dto.OrderData commerceOrder = importService.createOrder(order);
		if(commerceOrder == null) {
			LOG.warn("Failed to Import Order");
		} else {
			LOG.info("Order ["+commerceOrder.getCode()+"] created successfully");
		}
		return commerceOrder;
	}	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
