package com.saikrupa.app.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.MachineData;
import com.saikrupa.app.service.MachineService;

public class DefaultMachineService implements MachineService {

	public List<MachineData> getMachines() {
		String query = "SELECT CODE, NAME from PRODUCTION_MACHINE";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		List<MachineData> machines = new ArrayList<MachineData>();
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				MachineData data = new MachineData();
				data.setCode(rs.getInt(1));
				data.setName(rs.getString(2));				
				machines.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return machines;
	}

	public MachineData getMachineByCode(int machineCode) {
		String query = "SELECT CODE, NAME from PRODUCTION_MACHINE WHERE CODE=?";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		MachineData data = new MachineData();
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, machineCode);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {				
				data.setCode(rs.getInt(1));
				data.setName(rs.getString(2));			
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

}
