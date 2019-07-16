package com.saikrupa.app.dao;

import java.util.Date;
import java.util.List;

import com.saikrupa.app.dto.DeliveryData;
import com.saikrupa.app.dto.OrderData;

public interface OrderDeliveryDAO {
	
	public List<DeliveryData> findOrderDeliveriesByVehicleCode(final int vehicleCode);
	public List<DeliveryData> findOrderDeliveriesByVehicleByDate(final int vehicleCode, final Date fromDate, final Date toDate);
	public List<OrderData> findOrdersByVehicleCode(final int vehicleCode);
	public List<OrderData> findOrdersByAllVehicles();
}
