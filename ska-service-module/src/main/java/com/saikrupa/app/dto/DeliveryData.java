package com.saikrupa.app.dto;

import java.util.Date;

public class DeliveryData {
	
	private int code;
	private OrderEntryData orderEntryData;
	private String deliveryReceiptNo;
	private Date deliveryDate;
	private VehicleData deliveryVehicle;
	
	private double actualDeliveryQuantity;

	public DeliveryData() {
		
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public OrderEntryData getOrderEntryData() {
		return orderEntryData;
	}

	public void setOrderEntryData(OrderEntryData orderEntryData) {
		this.orderEntryData = orderEntryData;
	}

	public String getDeliveryReceiptNo() {
		return deliveryReceiptNo;
	}

	public void setDeliveryReceiptNo(String deliveryReceiptNo) {
		this.deliveryReceiptNo = deliveryReceiptNo;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public double getActualDeliveryQuantity() {
		return actualDeliveryQuantity;
	}

	public void setActualDeliveryQuantity(double actualDeliveryQuantity) {
		this.actualDeliveryQuantity = actualDeliveryQuantity;
	}

	public VehicleData getDeliveryVehicle() {
		return deliveryVehicle;
	}

	public void setDeliveryVehicle(VehicleData deliveryVehicle) {
		this.deliveryVehicle = deliveryVehicle;
	}

	

}
