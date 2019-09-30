package com.saikrupa.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saikrupa.app.dao.OrderDAO;
import com.saikrupa.app.dao.OrderDeliveryDAO;
import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.FilterParameter;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.dto.VehicleData;

public class DefaultOrderDeliveryDAO implements OrderDeliveryDAO {

	public List<OrderData> findOrderDeliveriesByVehicleCode(int vehicleCode) {
		String sql = "SELECT A.CODE, C.VEHICLE_NO FROM COM_ORDER A, COM_ORDER_DELIVERY B, DELIVERY_VEHICLE C"
				+ " WHERE A.CODE=B.ORDER_CODE" + " AND B.VEHICLE_CODE = C.CODE AND C.CODE=?";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		OrderDAO orderDao = new DefaultOrderDAO();
		List<OrderData> orders = new ArrayList<OrderData>();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, vehicleCode);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OrderData order = orderDao.findOrderByCode(rs.getInt(1));
				orders.add(order);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	public List<OrderData> findOrdersByAllVehicles() {
		String sql = "SELECT A.CODE, C.VEHICLE_NO FROM COM_ORDER A, COM_ORDER_DELIVERY B, DELIVERY_VEHICLE C"
				+ " WHERE A.CODE=B.ORDER_CODE" + " AND B.VEHICLE_CODE = C.CODE";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		OrderDAO orderDao = new DefaultOrderDAO();
		List<OrderData> orders = new ArrayList<OrderData>();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OrderData order = orderDao.findOrderByCode(rs.getInt(1));
				orders.add(order);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	public List<OrderData> findOrdersByVehicleCode(int vehicleCode) {
		String sql = "SELECT A.CODE, C.VEHICLE_NO FROM COM_ORDER A, COM_ORDER_DELIVERY B, DELIVERY_VEHICLE C"
				+ " WHERE A.CODE = B.ORDER_CODE" + " AND B.VEHICLE_CODE = C.CODE AND C.CODE = ?";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		OrderDAO orderDao = new DefaultOrderDAO();
		List<OrderData> orders = new ArrayList<OrderData>();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, vehicleCode);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OrderData order = orderDao.findOrderByCode(rs.getInt(1));
				orders.add(order);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	public List<OrderData> findOrderDeliveriesByFilters(ReportSelectionData filters) {

		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT A.CODE, C.VEHICLE_NO FROM COM_ORDER A, COM_ORDER_DELIVERY B, DELIVERY_VEHICLE C WHERE A.CODE=B.ORDER_CODE AND B.VEHICLE_CODE=C.CODE ");
		if (filters.getSelectionType() == 1) {
			sql.append("AND C.CODE=?");
		}

		if (filters.getSelectionType() == 2) {
			sql.append("AND B.DELIVERY_DATE BETWEEN ? AND ?");
		}

		if (filters.getSelectionType() == 3) {
			sql.append("AND C.CODE=? AND B.DELIVERY_DATE BETWEEN ? AND ?");
		}

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		OrderDAO orderDao = new DefaultOrderDAO();
		List<OrderData> orders = new ArrayList<OrderData>();
		try {
			PreparedStatement ps = connection.prepareStatement(sql.toString());
			List<FilterParameter> parameters = filters.getFilters();
			if (filters.getSelectionType() == 1) {
				for (FilterParameter param : parameters) {
					if (param.getFilterName().equalsIgnoreCase("BY_VEH")) {
						VehicleData vehicle = (VehicleData) param.getParameters().get("VEHICLE_DATA");
						ps.setInt(1, vehicle.getCode());
						break;
					}
				}
			}

			if (filters.getSelectionType() == 2) {
				for (FilterParameter param : parameters) {
					if (param.getFilterName().equalsIgnoreCase("BY_DATE")) {
						Date fromDate = (Date) param.getParameters().get("DATE_FROM");
						Date toDate = (Date) param.getParameters().get("DATE_TO");
						ps.setDate(1, new java.sql.Date(fromDate.getTime()));
						ps.setDate(2, new java.sql.Date(toDate.getTime()));
						break;
					}
				}
			}

			if (filters.getSelectionType() == 3) {
				for (FilterParameter param : parameters) {
					if (param.getFilterName().equalsIgnoreCase("BY_VEH")) {
						VehicleData vehicle = (VehicleData) param.getParameters().get("VEHICLE_DATA");
						ps.setInt(1, vehicle.getCode());
						break;
					}
				}

				for (FilterParameter param : parameters) {
					if (param.getFilterName().equalsIgnoreCase("BY_DATE")) {
						Date fromDate = (Date) param.getParameters().get("DATE_FROM");
						Date toDate = (Date) param.getParameters().get("DATE_TO");
						ps.setDate(2, new java.sql.Date(fromDate.getTime()));
						ps.setDate(3, new java.sql.Date(toDate.getTime()));
						break;
					}
				}
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OrderData order = orderDao.findOrderByCode(rs.getInt(1));
				orders.add(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

}
