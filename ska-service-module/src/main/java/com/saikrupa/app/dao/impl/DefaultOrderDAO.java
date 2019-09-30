package com.saikrupa.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.saikrupa.app.dao.OrderDAO;
import com.saikrupa.app.dao.ProductDAO;
import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.AddressData;
import com.saikrupa.app.dto.AddressType;
import com.saikrupa.app.dto.DeliveryData;
import com.saikrupa.app.dto.DeliveryStatus;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.dto.PaymentStatus;
import com.saikrupa.app.dto.VehicleData;
import com.saikrupa.app.service.VehicleService;
import com.saikrupa.app.service.impl.DefaultVehicleService;
import com.saikrupa.app.util.OrderUtil;

public class DefaultOrderDAO implements OrderDAO {

	public List<OrderData> findAllOrders() {
		List<OrderData> orderList = new ArrayList<OrderData>();
		final String sql = "SELECT CODE, ORDER_STATUS, PAYMENT_STATUS, DELIVERY_STATUS, CUSTOMER_CODE, CREATED_DATE FROM COM_ORDER ORDER BY CODE DESC";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		CustomerDAO customerDAO = new DefaultCustomerDAO();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OrderData data = new OrderData();
				data.setCode(rs.getString(1));
				data.setOrderStatus(OrderUtil.getOrderStatusByCode(rs.getInt(2)));
				data.setPaymentStatus(OrderUtil.getPaymentStatusByCode(rs.getInt(3)));
				data.setDeliveryStatus(OrderUtil.getDeliveryStatusByCode(rs.getInt(4)));
				data.setCustomer(customerDAO.findCustomerByCode(rs.getString(5)));
				data.setCreatedDate((java.util.Date) rs.getDate(6));
				data.setOrderEntries(getOrderEntries(data));
				updateOrderTotalPrice(data);				
				orderList.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderList;
	}
	
	public OrderData findOrderByCode(final int orderCode) {
		final String sql = "SELECT CODE, ORDER_STATUS, PAYMENT_STATUS, DELIVERY_STATUS, CUSTOMER_CODE, CREATED_DATE FROM COM_ORDER WHERE CODE=?";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		CustomerDAO customerDAO = new DefaultCustomerDAO();
		OrderData data = null;
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderCode);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {		
				data = new OrderData();
				data.setCode(rs.getString(1));
				data.setOrderStatus(OrderUtil.getOrderStatusByCode(rs.getInt(2)));
				data.setPaymentStatus(OrderUtil.getPaymentStatusByCode(rs.getInt(3)));
				data.setDeliveryStatus(OrderUtil.getDeliveryStatusByCode(rs.getInt(4)));
				data.setCustomer(customerDAO.findCustomerByCode(rs.getString(5)));
				data.setCreatedDate((java.util.Date) rs.getDate(6));
				data.setOrderEntries(getOrderEntries(data));
				updateOrderTotalPrice(data);			
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public List<OrderData> findOrdersByCustomer(Integer customerCode) {
		List<OrderData> orderList = new ArrayList<OrderData>();
		final String sql = "SELECT CODE, ORDER_STATUS, PAYMENT_STATUS, DELIVERY_STATUS, CUSTOMER_CODE, CREATED_DATE FROM COM_ORDER WHERE CUSTOMER_CODE = ? ORDER BY CREATED_DATE ASC";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		CustomerDAO customerDAO = new DefaultCustomerDAO();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, customerCode.intValue());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OrderData data = new OrderData();
				data.setCode(rs.getString(1));
				data.setOrderStatus(OrderUtil.getOrderStatusByCode(rs.getInt(2)));
				data.setPaymentStatus(OrderUtil.getPaymentStatusByCode(rs.getInt(3)));
				data.setDeliveryStatus(OrderUtil.getDeliveryStatusByCode(rs.getInt(4)));
				data.setCustomer(customerDAO.findCustomerByCode(rs.getString(5)));
				data.setCreatedDate((java.util.Date) rs.getDate(6));
				data.setOrderEntries(getOrderEntries(data));
				updateOrderTotalPrice(data);				
				orderList.add(data);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}
	
	public List<OrderData> findOrdersByCustomerByDateRange(Integer customerCode, Timestamp startDate, Timestamp endDate) {
		List<OrderData> orderList = new ArrayList<OrderData>();
		final String sql = "SELECT CODE, ORDER_STATUS, PAYMENT_STATUS, DELIVERY_STATUS, CUSTOMER_CODE, CREATED_DATE FROM COM_ORDER WHERE CUSTOMER_CODE = ? AND CREATED_DATE between ? and ? ORDER BY CREATED_DATE DESC";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		CustomerDAO customerDAO = new DefaultCustomerDAO();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, customerCode.intValue());
			ps.setTimestamp(2, startDate);
			ps.setTimestamp(3, endDate);
			System.out.println(startDate);
			System.out.println(endDate);
			System.out.println(customerCode);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OrderData data = new OrderData();
				data.setCode(rs.getString(1));
				data.setOrderStatus(OrderUtil.getOrderStatusByCode(rs.getInt(2)));
				data.setPaymentStatus(OrderUtil.getPaymentStatusByCode(rs.getInt(3)));
				data.setDeliveryStatus(OrderUtil.getDeliveryStatusByCode(rs.getInt(4)));
				data.setCustomer(customerDAO.findCustomerByCode(rs.getString(5)));
				data.setCreatedDate((java.util.Date) rs.getDate(6));
				data.setOrderEntries(getOrderEntries(data));
				updateOrderTotalPrice(data);				
				orderList.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}
	
	

	private void updateOrderTotalPrice(OrderData data) {
		double totalPrice = 0.0;
		for (OrderEntryData entry : data.getOrderEntries()) {
			totalPrice = totalPrice + (entry.getPrice() * entry.getOrderedQuantity()) + entry.getTransportationCost()
					- entry.getDiscount();
		}
		data.setTotalPrice(totalPrice);
	}

	private List<OrderEntryData> getOrderEntries(OrderData orderData) {
		List<OrderEntryData> orderEntries = new ArrayList<OrderEntryData>();
		final String sql = "SELECT CODE, ENTRY_NO, QUANTITY, PRICE, DELIVERY_COST, DISCOUNT,PRODUCT_CODE FROM COM_ORDER_ENTRY WHERE ORDER_CODE = ? ORDER BY ENTRY_NO ASC";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		ProductDAO productDAO = new DefaultProductDAO();

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, orderData.getCode());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OrderEntryData data = new OrderEntryData();
				data.setCode(rs.getInt(1));
				data.setEntryNumber(rs.getInt(2));
				data.setOrderedQuantity(rs.getInt(3));
				data.setPrice(rs.getDouble(4));
				data.setTransportationCost(rs.getDouble(5));
				data.setDiscount(rs.getDouble(6));
				data.setProduct(productDAO.findProductByCode(rs.getString(7)));
				data.setOrder(orderData);
				DeliveryData deliveryData = findDeliveryDetailForEntry(data);
				data.setDeliveryData(deliveryData);
				AddressData deliveryAddressData = findDeliveryAddressForEntry(data);
				data.setDeliveryAddress(deliveryAddressData);
				data.getOrder().setDeliveryAddress(deliveryAddressData);
				data.setPaymentEntries(getPaymentEntries(data));
				orderEntries.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderEntries;
	}
	
	private AddressData findDeliveryAddressForEntry(OrderEntryData data) {
		final String SQL = "SELECT ADDRESS_LINE1, ADDRESS_LINE2, ADDRESS_LANDMARK, ADDRESS_ZIP FROM COM_ORDER_DELIVERY_ADDRESS WHERE ORDER_ENTRY_CODE = ?"; 
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		AddressData address = new AddressData();
		try {
			PreparedStatement ps = connection.prepareStatement(SQL);
			ps.setInt(1, data.getCode());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				address.setLine1(rs.getString(1));
				address.setLine2(rs.getString(2));
				address.setLandmark(rs.getString(3));
				address.setZipCode(rs.getString(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return address;
	}

	public DeliveryData findDeliveryDetailForEntry(OrderEntryData entry) {
		final String sql = "SELECT CODE,DELIVERED_QUANTITY, DELIVERY_RECEIPT_NO, VEHICLE_CODE, DELIVERY_DATE "
				+ "FROM COM_ORDER_DELIVERY "
				+ "WHERE ORDER_CODE = ? AND ENTRY_NO=?";
		
		DeliveryData deliveryData = null;
		VehicleService vehicleService = new DefaultVehicleService();
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, entry.getOrder().getCode());
			ps.setInt(2, entry.getEntryNumber());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				deliveryData = new DeliveryData();
				deliveryData.setCode(rs.getInt(1));
				deliveryData.setActualDeliveryQuantity(rs.getDouble(2));
				deliveryData.setDeliveryReceiptNo(rs.getString(3));
				
				
				VehicleData vehicleData = vehicleService.getVehicleByCode(rs.getInt(4));
				deliveryData.setDeliveryVehicle(vehicleData);
				deliveryData.setDeliveryDate(new java.sql.Date(rs.getDate(5).getTime()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(deliveryData != null) {
			deliveryData.setOrderEntryData(entry);
		}
		return deliveryData;
	}

	

	public List<OrderData> searchOrderWithFilter(final String condition, Object[] params) {
		List<OrderData> orderList = new ArrayList<OrderData>();
		String searchQuery = "";
		String whereCluse = "";
		if (condition.equals("PENDING") && PaymentStatus.valueOf(condition) == PaymentStatus.PENDING) {
			whereCluse = " PAYMENT_STATUS IN (1,2)";  //PENDING & PARTIAL
		} else if(condition.equals("SHIPPING") && DeliveryStatus.valueOf(condition) == DeliveryStatus.SHIPPING) {
			whereCluse = " DELIVERY_STATUS = 1";					
		} else if(condition.equals("DELIVERED_PENDING_PAYMENT")) {
			whereCluse = " PAYMENT_STATUS = 1 AND DELIVERY_STATUS = 0";
		} else if(condition.equals("DELIVERY_QUANTITY_MISMATCH")) {
			searchQuery = "SELECT O.CODE, O.ORDER_STATUS, O.PAYMENT_STATUS, O.DELIVERY_STATUS, O.CUSTOMER_CODE, O.CREATED_DATE"
					+ " FROM COM_ORDER O, COM_ORDER_ENTRY E, COM_ORDER_DELIVERY D"
					+ " WHERE O.CODE = E.ORDER_CODE"
					+ " AND E.ENTRY_NO = D.ENTRY_NO"
					+ " AND D.ORDER_CODE = O.CODE"
					+ " AND E.QUANTITY <> D.DELIVERED_QUANTITY";			
		} else if(condition.equals("REPORT_ORDER_BY_CUSTOMER") || condition.equalsIgnoreCase("MANAGE_ORDER_CUSTOMER")) {
			searchQuery = "SELECT O.CODE, O.ORDER_STATUS, O.PAYMENT_STATUS, O.DELIVERY_STATUS, O.CUSTOMER_CODE, O.CREATED_DATE, O.CREATED_BY"
					+ " FROM COM_ORDER O "
					+ " WHERE O.CUSTOMER_CODE = ?";

		} else if(condition.equals("REPORT_ORDER_CONSOLIDATED")) {
			searchQuery = "SELECT O.CODE, O.ORDER_STATUS, O.PAYMENT_STATUS, O.DELIVERY_STATUS, O.CUSTOMER_CODE, O.CREATED_DATE, O.CREATED_BY"
					+ " FROM COM_ORDER O ";		
		} else if(condition.equals("REPORT_ORDER_BY_CUSTOMER_GROUP")) {
			searchQuery = "SELECT O.CODE, O.ORDER_STATUS, O.PAYMENT_STATUS, O.DELIVERY_STATUS, O.CUSTOMER_CODE, O.CREATED_DATE, O.CREATED_BY"
					+ " FROM COM_ORDER O ";
		}
		
		if(searchQuery == null || searchQuery.trim().length() < 1) {
			searchQuery = "SELECT CODE, ORDER_STATUS, PAYMENT_STATUS, DELIVERY_STATUS, CUSTOMER_CODE, CREATED_DATE "
			 		+ "FROM COM_ORDER "
			 		+ "WHERE "+whereCluse
			 		+ " ORDER BY CODE DESC";
		}

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		CustomerDAO customerDAO = new DefaultCustomerDAO();
		try {
			PreparedStatement ps = connection.prepareStatement(searchQuery);
			if(condition.equals("REPORT_ORDER_BY_CUSTOMER") || condition.equals("MANAGE_ORDER_CUSTOMER")) {
				if(params != null) {
					Integer param = (Integer) params[0];
					ps.setInt(1, param);
				}
			}
			if(condition.equals("REPORT_ORDER_BY_CUSTOMER_GROUP")) {
				
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OrderData data = new OrderData();
				data.setCode(rs.getString(1));
				data.setOrderStatus(OrderUtil.getOrderStatusByCode(rs.getInt(2)));
				data.setPaymentStatus(OrderUtil.getPaymentStatusByCode(rs.getInt(3)));
				data.setDeliveryStatus(OrderUtil.getDeliveryStatusByCode(rs.getInt(4)));
				data.setCustomer(customerDAO.findCustomerByCode(rs.getString(5)));
				data.setCreatedDate((java.util.Date) rs.getDate(6));
				data.setOrderEntries(getOrderEntries(data));
				data.setDeliveryAddress(findDeliveryAddressForOrderEntry(data.getOrderEntries().get(0).getCode()));
				updateOrderTotalPrice(data);
				orderList.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderList;
	}
	
	public AddressData findDeliveryAddressForOrderEntry(Integer entryCode) {
		final String sql = "SELECT ADDRESS_CODE,ADDRESS_LINE1,ADDRESS_LINE2,ADDRESS_LINE3,ADDRESS_LANDMARK,ADDRESS_ZIP,ORDER_ENTRY_CODE,ADDRESS_TYPE "
				+ "FROM COM_ORDER_DELIVERY_ADDRESS WHERE ORDER_ENTRY_CODE = ?";
		
		AddressData data = null;
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, entryCode);			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				data = new AddressData();
				data.setCode(rs.getString(1));
				data.setLine1(rs.getString(2));
				data.setLine2(rs.getString(3));
				data.setLandmark(rs.getString(4));
				data.setZipCode(rs.getString(5));
				data.setAddressType(AddressType.DELIVERY);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	

	public List<PaymentEntryData> getPaymentEntries(OrderEntryData entry) {
		final String sql = "SELECT CODE, PAYMENT_ENTRY_NO, PAYABLE_AMOUNT, PAID_AMOUNT, PAYMENT_DATE, "
				+ "PAYMENT_ENTRY_PAYMENT_STATUS "
				+ "FROM COM_ORDER_PAYMENT_ENTRY WHERE ORDER_ENTRY_CODE = ?";
		
		List<PaymentEntryData> list = new ArrayList<PaymentEntryData>();
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, entry.getCode());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PaymentEntryData paymentEntryData = new PaymentEntryData();
				paymentEntryData.setCode(rs.getInt(1));
				paymentEntryData.setEntryNumber(rs.getInt(2));
				paymentEntryData.setPayableAmount(rs.getDouble(3));
				paymentEntryData.setAmount(rs.getDouble(4));				
				paymentEntryData.setPaymentDate(new java.sql.Date(rs.getDate(5).getTime()));
				paymentEntryData.setPaymentStatus(OrderUtil.getPaymentStatusByCode(rs.getInt(6)));
				paymentEntryData.setOrderEntryData(entry);
				list.add(paymentEntryData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}

}
