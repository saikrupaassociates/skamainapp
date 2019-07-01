package com.saikrupa.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.saikrupa.app.dao.impl.CustomerDAO;
import com.saikrupa.app.dao.impl.DefaultCustomerDAO;
import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.ApplicationUserData;
import com.saikrupa.app.dto.DeliveryStatus;
import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.OrderStatus;
import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.dto.PaymentStatus;
import com.saikrupa.app.service.PaymentService;
import com.saikrupa.app.session.ApplicationSession;
import com.saikrupa.app.util.OrderUtil;

public class DefaultPaymentService implements PaymentService {

	public DefaultPaymentService() {
	}

	public void addPaymentEntryForOrderEntry(OrderEntryData orderEntryData, Connection connection) throws Exception {
		final String sql = "INSERT INTO COM_ORDER_PAYMENT_ENTRY (PAYMENT_ENTRY_NO, PAYABLE_AMOUNT, PAID_AMOUNT, PAYMENT_DATE, ORDER_ENTRY_CODE, PAYMENT_ENTRY_PAYMENT_STATUS) VALUES(?, ?, ?, ?, ?, ?)";

		OrderData order = orderEntryData.getOrder();
		try {
			PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			for (PaymentEntryData paymentEntryData : orderEntryData.getPaymentEntries()) {
				if (paymentEntryData.getCode() > 0) {
					continue;
				}
				statement.setInt(1, paymentEntryData.getEntryNumber());
				statement.setDouble(2, paymentEntryData.getPayableAmount());
				statement.setDouble(3, paymentEntryData.getAmount());
				statement.setTimestamp(4, new java.sql.Timestamp(paymentEntryData.getPaymentDate().getTime()));
				statement.setInt(5, paymentEntryData.getOrderEntryData().getCode());
				statement.setInt(6, OrderUtil.getCodeByPaymentStatus(paymentEntryData.getPaymentStatus()));
				order.setPaymentStatus(paymentEntryData.getPaymentStatus());
				int count = statement.executeUpdate();
				if (count > 0) {
					ResultSet keys = statement.getGeneratedKeys();
					keys.next();
					String code = keys.getString(1);
					paymentEntryData.setCode(Integer.valueOf(code).intValue());
				}
			}
			if (order.getPaymentStatus() == PaymentStatus.PAID) {
				if (order.getDeliveryStatus() == DeliveryStatus.SHIPPED) {
					order.setOrderStatus(OrderStatus.COMPLETED);
				} else if (order.getDeliveryStatus() == DeliveryStatus.SHIPPING) {
					order.setOrderStatus(OrderStatus.CONFIRMED);
				}
			} else if (order.getPaymentStatus() == PaymentStatus.PARTIAL) {
				if (order.getDeliveryStatus() == DeliveryStatus.SHIPPED) {
					order.setOrderStatus(OrderStatus.DELIVERED);
				} else if (order.getDeliveryStatus() == DeliveryStatus.SHIPPING) {
					order.setOrderStatus(OrderStatus.CREATED);
				}
			}

			updateOrder(order, connection);
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	private void updateOrder(OrderData order, Connection connection) throws Exception {
		System.out.println("updateOrder : Order Created Date : "+order.getCreatedDate());
		final String sql = "UPDATE COM_ORDER SET ORDER_STATUS = ?, PAYMENT_STATUS = ? , CREATED_DATE = ?, MODIFIED_DATE = ? , LAST_MODIFIED_BY = ? WHERE CODE=?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);			
			statement.setInt(1, OrderUtil.getOrderStatusCode(order));
			statement.setInt(2, OrderUtil.getPaymentStatusCode(order));
			statement.setTimestamp(3, new java.sql.Timestamp(order.getCreatedDate().getTime()));
			statement.setTimestamp(4, new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
			ApplicationUserData currentUser = (ApplicationUserData) ApplicationSession.getSession().getCurrentUser();
			statement.setString(5, currentUser.getUserId());
			statement.setString(6, order.getCode());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public void updateOrderPaymentsForOrder(OrderData orderData, List<PaymentEntryData> newEntries,
			List<PaymentEntryData> existingPayments) {
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		boolean hasError = false;
		try {
			OrderEntryData orderEntry = orderData.getOrderEntries().get(0);
			orderEntry.setOrder(orderData);
			connection.setAutoCommit(false);
			for (PaymentEntryData unadjustedCustomerEntry : existingPayments) {
				unadjustedCustomerEntry.setOrderEntryData(orderEntry);
				updateCustomerPaymentEntry(unadjustedCustomerEntry, connection);
			}
			for (PaymentEntryData newEntry : newEntries) {
				newEntry.setOrderEntryData(orderEntry);
				if(orderEntry.getPaymentEntries() == null || orderEntry.getPaymentEntries().isEmpty()) {
					ArrayList<PaymentEntryData> newPaymentEntries = new ArrayList<PaymentEntryData>();
					newPaymentEntries.add(newEntry);
					orderEntry.setPaymentEntries(newPaymentEntries);
				} else {
					orderEntry.getPaymentEntries().add(newEntry);
				}				
				addPaymentEntryForOrderEntry(orderEntry, connection);
				if(newEntry.getPaymentStatus() == PaymentStatus.PARTIAL) {
					orderData.setOrderStatus(OrderStatus.CREATED);
				} else if(newEntry.getPaymentStatus() == PaymentStatus.PAID) {
					orderData.setOrderStatus(OrderStatus.CONFIRMED);
				}				
				updateOrder(orderData, connection);
				
			}
			connection.commit();
			System.out.println("***************************     updateOrderPaymentsForCustomer : COMITTED ..");
		} catch (Exception e) {
			e.printStackTrace();
			hasError = true;
		} finally {
			if (hasError) {
				try {
					connection.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void updateCustomerPaymentEntry(PaymentEntryData existingPaymentEntry, Connection connection)
			throws Exception {
		final String sql = "UPDATE ADHOC_PAYMENT_ENTRY SET AD_PAYABLE_AMOUNT =? WHERE CODE = ?";
		try {
			System.out.println("Entry Code : "+existingPaymentEntry.getCode());
			System.out.println("Updarted PayableAmount : "+existingPaymentEntry.getPayableAmount());
			PreparedStatement statement = connection.prepareStatement(sql);			
			statement.setDouble(1, existingPaymentEntry.getPayableAmount());
			statement.setInt(2, existingPaymentEntry.getCode());
			System.out.println("Statement : "+statement.toString());
			int rows = statement.executeUpdate();
			System.out.println("updateCustomerPaymentEntry :: Rows updated :"+rows);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void addAdhocPayment(String customerCode, List<PaymentEntryData> paymentEntries,
			PaymentEntryData customerPayment) {
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		final String sql = "INSERT INTO ADHOC_PAYMENT_ENTRY(AD_PAYMENT_ENTRY_NO, AD_PAYABLE_AMOUNT, PAID_AMOUNT, PAYMENT_DATE, AD_ADJUSTED, CUSTOMER_CODE, PAYMENT_MODE, CHEQUE_NO) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

		boolean hasError = false;
		try {
			System.out.println(paymentEntries.size());
			connection.setAutoCommit(false);
			for (PaymentEntryData entry : paymentEntries) {
				System.out.println("Amount : "+entry.getAmount());
				System.out.println("payableAmount : "+entry.getPayableAmount());
				System.out.println("Payment Status : "+entry.getPaymentStatus());
				addPaymentEntryForOrderEntry(entry.getOrderEntryData(), connection);
			}
			CustomerDAO customerDAO = new DefaultCustomerDAO();
			List<PaymentEntryData> existingPaymentEntries = customerDAO.findAdhocPaymentByCustomer(customerCode);
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, existingPaymentEntries.size() + 1);
			statement.setDouble(2, customerPayment.getPayableAmount());
			statement.setDouble(3, customerPayment.getAmount());
			statement.setTimestamp(4, new java.sql.Timestamp(customerPayment.getPaymentDate().getTime()));
			statement.setInt(5, (paymentEntries.isEmpty() ? 0 : 1)); // Adjusted
																		// =1 ,
																		// Not
																		// Adjusted
																		// = 0
			statement.setInt(6, Integer.valueOf(customerCode));
			statement.setInt(7, Integer.valueOf(customerPayment.getPaymentMode().getCode()));
			statement.setString(8, customerPayment.getChequeNumber());
			statement.executeUpdate();

		} catch (SQLException e) {
			hasError = true;
			e.printStackTrace();
		} catch (Exception e) {
			hasError = true;
			e.printStackTrace();
		} finally {
			if (hasError) {
				try {
					System.out.println("Roll back...");
					connection.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {
					connection.commit();
					System.out.println("Comitted");
				} catch (SQLException e) {
					System.out.println("Exception while commiting Order ");
					e.printStackTrace();
				}
			}
		}
	}

	public void updateInventoryEntryForPayment(List<InventoryEntryData> entries) {
		final String sql = "UPDATE INVENTORY_ENTRY SET LABOUR_PAYMENT_STATUS = 1 where CODE= ?";
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		try {
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement(sql);
			for(InventoryEntryData data : entries) {
				statement.setInt(1, data.getCode());
				statement.addBatch();
			}
			int[] rows = statement.executeBatch();
			System.out.println("updateInventoryEntryForPayment :: Rows updated :"+rows.length);
			connection.commit();
			System.out.println("Committed");
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}		
	}
}
