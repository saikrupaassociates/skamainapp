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
import com.saikrupa.app.dto.DeliveryData;
import com.saikrupa.app.dto.OrderData;

public class DefaultOrderDeliveryDAO implements OrderDeliveryDAO {

	public List<DeliveryData> findOrderDeliveriesByVehicleCode(int vehicleCode) {
		// TODO Auto-generated method stub
		return null;
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

	public List<DeliveryData> findOrderDeliveriesByVehicleByDate(int vehicleCode, Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
