package com.saikrupa.app.dto;

import java.util.Date;

public class PaymentEntryData implements Cloneable{
	
	private OrderEntryData orderEntryData;
	private int entryNumber;
	private Double amount;
	private Double payableAmount;
	private Date paymentDate;	
	private PaymentStatus paymentStatus;
	private int code;	
	
	private PaymentTypeData paymentMode;
	
	private String chequeNumber;
	 
	
	private boolean adjusted = Boolean.FALSE;
	

	public PaymentEntryData() {
		// TODO Auto-generated constructor stub
	}

	public OrderEntryData getOrderEntryData() {
		return orderEntryData;
	}

	public void setOrderEntryData(OrderEntryData orderEntryData) {
		this.orderEntryData = orderEntryData;
	}

	public int getEntryNumber() {
		return entryNumber;
	}

	public void setEntryNumber(int entryNumber) {
		this.entryNumber = entryNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isAdjusted() {
		return adjusted;
	}

	public void setAdjusted(boolean adjusted) {
		this.adjusted = adjusted;
	}

	public PaymentTypeData getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentTypeData paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

}
