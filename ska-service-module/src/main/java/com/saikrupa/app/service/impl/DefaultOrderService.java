package com.saikrupa.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.saikrupa.app.dao.ProductDAO;
import com.saikrupa.app.dao.impl.DefaultProductDAO;
import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.ApplicationUserData;
import com.saikrupa.app.dto.CustomerData;
import com.saikrupa.app.dto.DeliveryData;
import com.saikrupa.app.dto.DeliveryStatus;
import com.saikrupa.app.dto.InventoryData;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.dto.VehicleData;
import com.saikrupa.app.service.OrderService;
import com.saikrupa.app.service.PaymentService;
import com.saikrupa.app.service.VehicleService;
import com.saikrupa.app.session.ApplicationSession;
import com.saikrupa.app.util.OrderUtil;

public class DefaultOrderService implements OrderService {
	
	private static Logger LOG = Logger.getLogger(DefaultOrderService.class);

	public OrderData createOrder(OrderData order) {
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		final String sql = "INSERT INTO COM_ORDER ("
				+ "ORDER_STATUS, "
				+ "PAYMENT_STATUS, " 
				+ "DELIVERY_STATUS, "
				+ "CUSTOMER_CODE, "
				+ "CREATED_DATE, "
				+ "CREATED_BY, " 
				+ "MODIFIED_DATE, "
				+ "LAST_MODIFIED_BY) "
				+ "VALUES(?,?,?,?,?,?,?,?)";

		boolean commit = false;
		try {
			connection.setAutoCommit(false);
			CustomerData customer = createCustomer(order.getCustomer(), connection);

			PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			statement.setInt(1, OrderUtil.getOrderStatusCode(order));
			statement.setInt(2, OrderUtil.getPaymentStatusCode(order));
			statement.setInt(3, OrderUtil.getDeliveryStatusCode(order)); // Delivery Status
			statement.setInt(4, Integer.valueOf(customer.getCode()));
			statement.setTimestamp(5, new java.sql.Timestamp(order.getCreatedDate().getTime()));
			ApplicationUserData currentUser = (ApplicationUserData) ApplicationSession.getSession().getCurrentUser();
			statement.setString(6, currentUser.getUserId());
			statement.setTimestamp(7, new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
			statement.setString(8, currentUser.getUserId());

			int count = statement.executeUpdate();
			if (count > 0) {
				ResultSet keys = statement.getGeneratedKeys();
				keys.next();
				String code = keys.getString(1);
				order.setCode(code);
				order.setCustomer(customer);
				createOrderEntries(order, connection);
				commit = true;
			}
		} catch (SQLException e) {
			commit = false;
			e.printStackTrace();
		} catch (Exception e) {
			commit = false;
			e.printStackTrace();
		} finally {
			if (!commit) {
				try {
					LOG.warn("Roll back...");
					connection.rollback();
				} catch (SQLException e) {
					LOG.error("Exception while commiting Order", e);
				}
			} else {
				try {
					connection.commit();
				} catch (SQLException e) {
					LOG.error("Exception while commiting Order", e);
				}
			}
		}
		return order;
	}

	public void updateOrderStatus(final OrderData order) {
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();

		final String sql = "UPDATE COM_ORDER " + "SET ORDER_STATUS = ?, PAYMENT_STATUS = ?, " + "DELIVERY_STATUS = ? , "
				+ "MODIFIED_DATE = ? , LAST_MODIFIED_BY = ? WHERE CODE=?";
		PreparedStatement statement = null;
		try {

			connection.setAutoCommit(false);
			updateOrderEntryDetail(order, connection);
			statement = connection.prepareStatement(sql);
			statement.setInt(1, OrderUtil.getOrderStatusCode(order));
			statement.setInt(2, OrderUtil.getPaymentStatusCode(order));
			statement.setInt(3, OrderUtil.getDeliveryStatusCode(order));
			statement.setTimestamp(4, new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
			ApplicationUserData currentUser = (ApplicationUserData) ApplicationSession.getSession().getCurrentUser();
			statement.setString(5, currentUser.getUserId());
			statement.setString(6, order.getCode());
			int row = statement.executeUpdate();
			if (row < 1) {
				throw new Exception("updateOrderStatus failed to Update Order");
			}
			PaymentService paymentService = new DefaultPaymentService();
			for (OrderEntryData entry : order.getOrderEntries()) {
				paymentService.addPaymentEntryForOrderEntry(entry, connection);
				createInventoryEntryForOrder(entry, connection);
				updateProductInventory(entry, connection);
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean deliveryEntryExists(OrderEntryData entry, Connection connection) {
		final String SQL = "SELECT CODE, DELIVERY_DATE FROM COM_ORDER_DELIVERY WHERE ORDER_CODE=? AND ENTRY_NO=?";
		boolean value = false;
		try {
			PreparedStatement ps = connection.prepareStatement(SQL);
			ps.setString(1, entry.getOrder().getCode());
			ps.setInt(2, entry.getEntryNumber());
			ResultSet result = ps.executeQuery();
			if (result != null && result.next()) {
				value = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			value = false;
		}
		return value;
	}

	private void updateOrderEntryDetail(final OrderData order, Connection connection) {
		for (OrderEntryData entry : order.getOrderEntries()) {
			DeliveryData entryDeliveryData = entry.getDeliveryData();
			if (order.getDeliveryStatus() == DeliveryStatus.SHIPPED) {
				if (!deliveryEntryExists(entry, connection)) {
					createDeliveryEntry(entryDeliveryData, entry, connection);
				}
				
			}
		}
	}

	private void createDeliveryEntry(DeliveryData entryDeliveryData, OrderEntryData entry, Connection connection) {
		final String SQL_CREATE_DELIVERY_ENTRY = "INSERT INTO COM_ORDER_DELIVERY(ORDER_CODE, ENTRY_NO, DELIVERED_QUANTITY, DELIVERY_RECEIPT_NO, DELIVERY_DATE, VEHICLE_CODE, LAST_MODIFIED_BY) VALUES(?,?,?,?,?,?,?)";

		try {
			PreparedStatement ps = connection.prepareStatement(SQL_CREATE_DELIVERY_ENTRY,
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, Integer.valueOf(entry.getOrder().getCode()));
			ps.setInt(2, Integer.valueOf(entry.getEntryNumber()));
			ps.setDouble(3, entryDeliveryData.getActualDeliveryQuantity());
			ps.setInt(4, Integer.valueOf(entryDeliveryData.getDeliveryReceiptNo()));
			ps.setDate(5, new java.sql.Date(entryDeliveryData.getDeliveryDate().getTime()));
			
			VehicleService vehicleService = new DefaultVehicleService();	
			VehicleData vehicle = vehicleService.getVehicleByNumber(entryDeliveryData.getDeliveryVehicle().getNumber());
			ps.setInt(6, vehicle.getCode());
			ApplicationUserData currentUser = (ApplicationUserData) ApplicationSession.getSession().getCurrentUser();
			ps.setString(7, currentUser.getUserId());

			int row = ps.executeUpdate();
			if (row < 1) {
				throw new SQLException("createDeliveryEntry failed to Create Order Delivery Entry for Order "
						+ entry.getOrder().getCode());
			}
			ResultSet keys = ps.getGeneratedKeys();
			keys.next();
			int code = keys.getInt(1);
			entryDeliveryData.setCode(code);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void createOrderEntries(OrderData order, Connection connection) throws Exception {
		final String SQL_CREATE_ORDER_ENTRIES = "insert into COM_ORDER_ENTRY(ENTRY_NO, QUANTITY, PRICE, DELIVERY_COST, DISCOUNT, NOTES, ORDER_CODE, PRODUCT_CODE, LAST_MODIFIED_BY) values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(SQL_CREATE_ORDER_ENTRIES,
				PreparedStatement.RETURN_GENERATED_KEYS);
		for (OrderEntryData entry : order.getOrderEntries()) {
			ps.setInt(1, entry.getEntryNumber());
			ps.setDouble(2, entry.getOrderedQuantity());
			ps.setDouble(3, entry.getPrice());
			ps.setDouble(4, entry.getTransportationCost());
			ps.setDouble(5, entry.getDiscount());
			ps.setString(6, entry.getEntryNote());
			ps.setString(7, order.getCode());
			ps.setString(8, entry.getProduct().getCode());

			ApplicationUserData currentUser = (ApplicationUserData) ApplicationSession.getSession().getCurrentUser();
			ps.setString(9, currentUser.getUserId());

			int count = ps.executeUpdate();
			if (count > 0) {
				ResultSet keys = ps.getGeneratedKeys();
				keys.next();
				int code = keys.getInt(1);
				entry.setCode(code);				
				entry.setOrder(order);				
//				createInventoryEntryForOrder(entry, connection);
//				updateProductInventory(entry, connection);
				createEntryDeliveryAddress(entry, connection);
			}
		}
	}

	private void createEntryDeliveryAddress(OrderEntryData entry, Connection connection) throws SQLException {
		final String SQL_INSERT_DELIVERY_ADDRESS = "INSERT INTO COM_ORDER_DELIVERY_ADDRESS "
				+ "(ADDRESS_LINE1, ADDRESS_TYPE, ORDER_ENTRY_CODE) VALUES(?,?,?)";

		PreparedStatement statement = connection.prepareStatement(SQL_INSERT_DELIVERY_ADDRESS);
		statement.setString(1, entry.getDeliveryAddress().getLine1());
		statement.setInt(2, 0);
		statement.setInt(3, entry.getCode());
		statement.executeUpdate();
	}

	private void updateProductInventory(OrderEntryData entry, Connection connection) throws Exception {
		final String SQL_UPDATE_INVENTORY = "UPDATE INVENTORY SET TOTAL_AVAILABLE_QUANTITY = ?, LAST_MODIFIED_BY = ?, LAST_UPDATED_DATE=? WHERE PRODUCT_CODE=?";
		double newQuantity = entry.getProduct().getInventory().getTotalAvailableQuantity() - entry.getOrderedQuantity();
		PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_INVENTORY);
		statement.setDouble(1, newQuantity);
		statement.setString(2, "SYSTEM");
		statement.setTimestamp(3, new java.sql.Timestamp(entry.getOrder().getCreatedDate().getTime()));
		statement.setString(4, entry.getProduct().getCode());
		
		int resultCount = statement.executeUpdate();
		if (resultCount > 0) {
			LOG.info("Inventory reduced to " + newQuantity + " for product " + entry.getProduct().getName()+" On ["+entry.getOrder().getCreatedDate()+"]");
		}
	}

	private void createInventoryEntryForOrder(OrderEntryData orderEntry, Connection connection) {
		PreparedStatement ps = null;
		String sql = "INSERT INTO INVENTORY_ENTRY ("
				+ "INVENTORY_CODE, CREATED_DATE, "
				+ "OPENING_BALANCE, QUANTITY_ADDED, "
				+ "QUANTITY_REDUCED, QUANTITY_RESERVED, "
				+ "QUANTITY_DAMAGED, CLOSING_BALANCE, "
				+ "LAST_MODIFIED_BY, ORDER_REF, MACHINE_CODE) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			//INVENTORY_CODE
			ps.setInt(1, orderEntry.getProduct().getInventory().getCode());
			
			//CREATED_DATE
			ps.setTimestamp(2, new java.sql.Timestamp(orderEntry.getOrder().getCreatedDate().getTime()));
			
			ProductDAO productDao = new DefaultProductDAO();
			InventoryData currentInventoryData = productDao.findInventoryLevelByProduct(orderEntry.getProduct());
			//Opening Balance
			ps.setDouble(3, currentInventoryData.getTotalAvailableQuantity());
			
			//QUANTITY_ADDED
			ps.setDouble(4, Double.valueOf("0.0"));
			
			//QUANTITY_REDUCED
			ps.setDouble(5, orderEntry.getOrderedQuantity());
			
			//QUANTITY_RESERVED
			ps.setDouble(6, Double.valueOf("0.0"));
			
			//QUANTITY_DAMAGED
			ps.setDouble(7, Double.valueOf("0.0"));	
			
			//CLOSING_BALANCE
			ps.setDouble(8, currentInventoryData.getTotalAvailableQuantity() - orderEntry.getOrderedQuantity());
			
			ApplicationUserData currentUser = (ApplicationUserData) ApplicationSession.getSession().getCurrentUser();
			ps.setString(9, currentUser.getUserId());			
			ps.setString(10, orderEntry.getOrder().getCode());	
			ps.setInt(11, -1);

			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	private CustomerData createCustomer(CustomerData customer, Connection connection) throws Exception {
		if (customer.getCode() == null) {
			
			final String SQL_CREATE_CUSTOMER = "INSERT INTO CUSTOMER (NAME, CONTACT_PRIMARY, CONTACT_SECONDARY, LAST_MODIFIED_BY) VALUES(?,?,?,?)";
			try {
				PreparedStatement statement = connection.prepareStatement(SQL_CREATE_CUSTOMER,
						PreparedStatement.RETURN_GENERATED_KEYS);
				statement.setString(1, customer.getName());
				statement.setString(2, customer.getPrimaryContact());
				statement.setString(3, customer.getSecondaryContact());
				ApplicationUserData currentUser = (ApplicationUserData) ApplicationSession.getSession()
						.getCurrentUser();
				statement.setString(4, currentUser.getUserId());

				int count = statement.executeUpdate();
				if (count > 0) {
					ResultSet keys = statement.getGeneratedKeys();
					keys.next();
					String code = keys.getString(1);
					customer.setCode(code);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return customer;
	}

	public void updateOrderPayment(OrderData order, List<PaymentEntryData> newEntries,
			List<PaymentEntryData> customerExistingPayments) {
		LOG.info("***************************     updateOrderPayment ************************");
		OrderData newOrder = createOrder(order);
		PaymentService paymentService = new DefaultPaymentService();
		paymentService.updateOrderPaymentsForOrder(newOrder, newEntries, customerExistingPayments);
		LOG.info("***************************     updateOrderPayment :: DONE !!!************************");

	}

	public void updateOrderDelivery(OrderEntryData orderEntry) throws Exception {
		LOG.info("updateOrderDelivery :: ["+orderEntry.getCode()+"] : - Now Updating...");
		final String SQL_UPDATE_DELIVERY = "UPDATE COM_ORDER_DELIVERY SET DELIVERED_QUANTITY=?, DELIVERY_RECEIPT_NO=?, DELIVERY_DATE=?, VEHICLE_CODE=?, LAST_MODIFIED_BY=? WHERE CODE=?";
		
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		
		DeliveryData deliveryData = orderEntry.getDeliveryData();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_DELIVERY);
			statement.setDouble(1, deliveryData.getActualDeliveryQuantity());
			statement.setString(2, deliveryData.getDeliveryReceiptNo());
			statement.setDate(3, new java.sql.Date(deliveryData.getDeliveryDate().getTime()));
			statement.setInt(4, deliveryData.getDeliveryVehicle().getCode());
			
			ApplicationUserData currentUser = (ApplicationUserData) ApplicationSession.getSession()
					.getCurrentUser();
			statement.setString(5, currentUser.getUserId());			
			statement.setInt(6,  orderEntry.getCode());

			int count = statement.executeUpdate();
			LOG.info("updateOrderDelivery :: ["+orderEntry.getCode()+"] : Records Updated : "+count);

		} catch (SQLException e) {
			LOG.error("updateOrderDelivery :: SQL Exception", e);
			throw e;
		} catch (Exception e) {
			LOG.error("updateOrderDelivery :: Exception", e);
			throw e;
		}		
	}

	public void deleteOrder(OrderData order) {
		
		
	}
}
