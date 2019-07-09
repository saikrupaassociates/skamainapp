package com.saikrupa.app.service;

import java.util.List;

import com.saikrupa.app.dto.VehicleData;

public interface VehicleService {
	public List<VehicleData> getVehicles();
	public VehicleData getExternalVehicle();
	public VehicleData getVehicleByNumber(final String vehicleNumber);
}
