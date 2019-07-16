package com.saikrupa.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.VehicleData;
import com.saikrupa.app.service.VehicleService;

public class DefaultVehicleService implements VehicleService {

	public List<VehicleData> getVehicles() {
		String query = "SELECT CODE, VEHICLE_NO from DELIVERY_VEHICLE";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		List<VehicleData> vehicles = new ArrayList<VehicleData>();
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				VehicleData data = new VehicleData();
				data.setCode(rs.getInt(1));
				data.setNumber(rs.getString(2));
				vehicles.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vehicles;
	}

	public VehicleData getExternalVehicle() {
		for (VehicleData data : getVehicles()) {
			if(data.getNumber().equalsIgnoreCase("EXTERNAL")) {
				return data;
			}
		}
		return null;
	}

	public VehicleData getVehicleByNumber(final String vehicleNumber) {
		String query = "SELECT CODE, VEHICLE_NO from DELIVERY_VEHICLE WHERE VEHICLE_NO =?";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		VehicleData vehicleData = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, vehicleNumber);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				vehicleData = new VehicleData();
				vehicleData.setCode(rs.getInt(1));
				vehicleData.setNumber(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vehicleData;
	}

	public VehicleData getVehicleByCode(int vehicleCode) {
		String query = "SELECT CODE, VEHICLE_NO from DELIVERY_VEHICLE WHERE CODE =?";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		VehicleData vehicleData = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, vehicleCode);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				vehicleData = new VehicleData();
				vehicleData.setCode(rs.getInt(1));
				vehicleData.setNumber(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vehicleData;
	}

}
