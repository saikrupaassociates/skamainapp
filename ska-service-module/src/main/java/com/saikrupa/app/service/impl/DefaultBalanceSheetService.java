package com.saikrupa.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.service.BalanceSheetService;

public class DefaultBalanceSheetService implements BalanceSheetService {

	public DefaultBalanceSheetService() {
		// TODO Auto-generated constructor stub
	}

	public Map<String, Double> getOrderSummary(String fromDate, String toDate) {
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		PreparedStatement ps = null;

		final String ORDER_COUNT = "SELECT COUNT(A.CODE) FROM COM_ORDER A, COM_ORDER_ENTRY B"
				+ " WHERE A.CODE = B.ORDER_CODE" + " AND A.ORDER_STATUS > 0 AND A.CREATED_DATE BETWEEN ? AND ?";

		final String ORDER_SUM = "SELECT SUM(B.QUANTITY) FROM COM_ORDER A, COM_ORDER_ENTRY B"
				+ " WHERE A.CODE = B.ORDER_CODE" + " AND A.ORDER_STATUS > 0 AND A.CREATED_DATE BETWEEN ? AND ?";

		final String ORDER_COUNT_TYPE = "SELECT COUNT(A.CODE) FROM COM_ORDER A, COM_ORDER_ENTRY B"
				+ " WHERE A.CODE = B.ORDER_CODE"
				+ " AND A.ORDER_STATUS > 0 AND A.CREATED_DATE BETWEEN ? AND ? AND B.PRODUCT_CODE = ?";

		final String ORDER_SUM_TYPE = "SELECT SUM(B.QUANTITY) FROM COM_ORDER A, COM_ORDER_ENTRY B"
				+ " WHERE A.CODE = B.ORDER_CODE"
				+ " AND A.ORDER_STATUS > 0 AND A.CREATED_DATE BETWEEN ? AND ? AND B.PRODUCT_CODE = ?";

		Map<String, Double> map = new HashMap<String, Double>();
		try {
			ps = connection.prepareStatement(ORDER_COUNT);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				map.put("OS_ORDER_COUNT", rs.getDouble(1));
			}
			ps.close();
			ps = connection.prepareStatement(ORDER_SUM);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put("OS_ORDER_SUM", rs.getDouble(1));
			}

			ps.close();
			ps = connection.prepareStatement(ORDER_COUNT_TYPE);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ps.setInt(3, 1000);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put("OS_ORDER_COUNT_9", rs.getDouble(1));
			}

			ps.close();
			ps = connection.prepareStatement(ORDER_SUM_TYPE);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ps.setInt(3, 1000);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put("OS_ORDER_SUM_9", rs.getDouble(1));
			}

			ps.close();
			ps = connection.prepareStatement(ORDER_COUNT_TYPE);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ps.setInt(3, 1001);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put("OS_ORDER_COUNT_10", rs.getDouble(1));
			}

			ps.close();
			ps = connection.prepareStatement(ORDER_SUM_TYPE);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ps.setInt(3, 1001);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put("OS_ORDER_SUM_10", rs.getDouble(1));
			}

			ps.close();
			ps = connection.prepareStatement(ORDER_COUNT_TYPE);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ps.setInt(3, 1002);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put("OS_ORDER_COUNT_BLOCK", rs.getDouble(1));
			}

			ps.close();
			ps = connection.prepareStatement(ORDER_SUM_TYPE);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ps.setInt(3, 1002);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put("OS_ORDER_SUM_BLOCK", rs.getDouble(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public Map<String, Double> getExpenseSummary(String fromDate, String toDate) {
		final String EXP_QUERY = "SELECT B.EXP_CAT_NAME, SUM(A.AMOUNT) FROM EXPENSE A, EXPENSE_CATEGORY B WHERE A.CATEGORY_CODE=B.EXP_CAT_CODE"
				+ " AND A.EXPENSE_DATE BETWEEN ? AND ? GROUP BY A.CATEGORY_CODE";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		PreparedStatement ps = null;
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			ps = connection.prepareStatement(EXP_QUERY);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString(1), rs.getDouble(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public Map<String, Double> getCollectionSummary(String fromDate, String toDate) {

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		PreparedStatement ps = null;

		final String query = "SELECT A.NAME, SUM(D.PAID_AMOUNT) FROM CUSTOMER A, COM_ORDER B, COM_ORDER_ENTRY C, COM_ORDER_PAYMENT_ENTRY D WHERE D.ORDER_ENTRY_CODE = C.CODE"
				+ " AND C.ORDER_CODE = B.CODE AND B.CUSTOMER_CODE = A.CODE AND B.ORDER_STATUS > 0 AND D.PAYMENT_DATE BETWEEN ? AND ? GROUP BY A.CODE";
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString(1), rs.getDouble(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public Map<String, Double> getDeliveryVehicleSummary(String fromDate, String toDate) {

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		PreparedStatement ps = null;

		final String query = "SELECT B.VEHICLE_NO, COUNT(A.ORDER_CODE) FROM COM_ORDER_DELIVERY A, DELIVERY_VEHICLE B WHERE A.VEHICLE_CODE=B.CODE"
				+ " AND A.DELIVERY_DATE BETWEEN ? AND ? GROUP BY A.VEHICLE_CODE";
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString(1), rs.getDouble(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public Map<String, Double> getProfitSummary(String fromDate, String toDate) {
		final String TOTAL_ORDER_VALUE = "SELECT SUM((B.QUANTITY * B.PRICE) + B.DELIVERY_COST - B.DISCOUNT) FROM COM_ORDER A, COM_ORDER_ENTRY B"
				+ " WHERE A.CODE = B.ORDER_CODE AND A.ORDER_STATUS > 0 AND A.CREATED_DATE BETWEEN ? AND ? ";

		final String TOTAL_EXPENSE = "SELECT SUM(AMOUNT) FROM EXPENSE WHERE EXPENSE_DATE BETWEEN ? AND ?";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		PreparedStatement ps = null;
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			ps = connection.prepareStatement(TOTAL_ORDER_VALUE);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				map.put("TOTAL_SALES_PRICE", rs.getDouble(1));
			}

			ps.close();
			ps = connection.prepareStatement(TOTAL_EXPENSE);
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put("TOTAL_EXPENSE", rs.getDouble(1));
			}
			Double totalSalesPrice = map.get("TOTAL_SALES_PRICE");
			final Double totalExpense = map.get("TOTAL_EXPENSE");
			final Double margin = totalSalesPrice - totalExpense;
			map.put("PROFIT_MARGIN", margin);
			if (margin < 0) {
				totalSalesPrice = Double.valueOf(1);
				map.put("PROFIT_PERCENT", Double.valueOf(0));
			} else {
				final Double percent = (margin / totalSalesPrice) * 100;
				map.put("PROFIT_PERCENT", percent);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

}
