package com.saikrupa.app.dao;

import java.util.Date;
import java.util.List;

import com.saikrupa.app.dto.DeliveryData;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.ReportSelectionData;

public interface OrderDeliveryDAO {
	
	public List<OrderData> findOrderDeliveriesByVehicleCode(final int vehicleCode);	
	public List<OrderData> findOrdersByVehicleCode(final int vehicleCode);
	public List<OrderData> findOrdersByAllVehicles();
	public List<OrderData> findOrderDeliveriesByFilters(final ReportSelectionData filters);
}
