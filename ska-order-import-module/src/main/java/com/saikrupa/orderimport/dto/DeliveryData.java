package com.saikrupa.orderimport.dto;

import java.util.Date;

public class DeliveryData {
	
	private Date deliveryDate;
	private String vehicleNumber;
	private String challanNumber;
	private String location;
	

	public DeliveryData() {
		// TODO Auto-generated constructor stub
	}


	public Date getDeliveryDate() {
		return deliveryDate;
	}


	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}


	public String getVehicleNumber() {
		return vehicleNumber;
	}


	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}


	public String getChallanNumber() {
		return challanNumber;
	}


	public void setChallanNumber(String challanNumber) {
		this.challanNumber = challanNumber;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}
	
	public String toString() {
		return "Delivery ["+DateUtil.getFormattedDate(deliveryDate)+", "+location+", "+vehicleNumber+", "+challanNumber+"]";
	}

}
