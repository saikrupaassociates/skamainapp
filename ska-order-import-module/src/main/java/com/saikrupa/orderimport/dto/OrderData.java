package com.saikrupa.orderimport.dto;

import java.util.Date;

public class OrderData {
	
	private CustomerData customer;
	private DeliveryData delivery;
	private Date orderedDate;
	private String productCode;
	private double quantity;
	private double unitPrice;
	private double transportationCost;
	private double discount;
	private int rowIndex;
	
	public String toString() {
		return "Order["+rowIndex+"] - ["+customer+"\n, "+delivery+"\n, "+DateUtil.getFormattedDate(orderedDate)+", "+productCode+", "+quantity+", "+unitPrice+", "+transportationCost+", "+discount+"]";
	}
	

	public OrderData() {
		// TODO Auto-generated constructor stub
	}


	public CustomerData getCustomer() {
		return customer;
	}


	public void setCustomer(CustomerData customer) {
		this.customer = customer;
	}


	public DeliveryData getDelivery() {
		return delivery;
	}


	public void setDelivery(DeliveryData delivery) {
		this.delivery = delivery;
	}


	public Date getOrderedDate() {
		return orderedDate;
	}


	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}


	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public double getQuantity() {
		return quantity;
	}


	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}


	public double getUnitPrice() {
		return unitPrice;
	}


	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}


	public double getTransportationCost() {
		return transportationCost;
	}


	public void setTransportationCost(double transportationCost) {
		this.transportationCost = transportationCost;
	}


	public double getDiscount() {
		return discount;
	}


	public void setDiscount(double discount) {
		this.discount = discount;
	}


	public int getRowIndex() {
		return rowIndex;
	}


	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

}
