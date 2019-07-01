package com.saikrupa.app.dto;

import java.util.Date;

public class InventoryEntryData {
	
	private int code;
	
	private InventoryData inventory;
	private Date createdDate;
	
	private Double addedQuantity;
	private Double reservedQuantity;
	private Double damagedQuantity;
	private Integer labourPaymentStatus;
	
	private Double openingBalance;
	private Double closingBalance;
	private Double reducedQuantity;
	
	private OrderData referencedOrder;
	
	private MachineData machine;
	

	public InventoryEntryData() {
		// TODO Auto-generated constructor stub
	}

	public InventoryData getInventory() {
		return inventory;
	}

	public void setInventory(InventoryData inventory) {
		this.inventory = inventory;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Double getAddedQuantity() {
		return addedQuantity;
	}

	public void setAddedQuantity(Double addedQuantity) {
		this.addedQuantity = addedQuantity;
	}

	public Double getReservedQuantity() {
		return reservedQuantity;
	}

	public void setReservedQuantity(Double reservedQuantity) {
		this.reservedQuantity = reservedQuantity;
	}

	public Double getDamagedQuantity() {
		return damagedQuantity;
	}

	public void setDamagedQuantity(Double damagedQuantity) {
		this.damagedQuantity = damagedQuantity;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Integer getLabourPaymentStatus() {
		return labourPaymentStatus;
	}

	public void setLabourPaymentStatus(Integer labourPaymentStatus) {
		this.labourPaymentStatus = labourPaymentStatus;
	}

	public Double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public Double getReducedQuantity() {
		return reducedQuantity;
	}

	public void setReducedQuantity(Double reducedQuantity) {
		this.reducedQuantity = reducedQuantity;
	}

	public OrderData getReferencedOrder() {
		return referencedOrder;
	}

	public void setReferencedOrder(OrderData referencedOrder) {
		this.referencedOrder = referencedOrder;
	}

	public MachineData getMachine() {
		return machine;
	}

	public void setMachine(MachineData machine) {
		this.machine = machine;
	}

}
