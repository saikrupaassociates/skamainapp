package com.saikrupa.app.ui.models;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.util.DateUtil;

public class OrderDeliveryTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = { "Order #", "Customer", "Location", "Quantity", "Vehicle", "Challan No", "Delivery Date"};
	private List<OrderData> orderDataList;
	
	private ReportSelectionData selectionData;

	public OrderDeliveryTableModel(List<OrderData> orderDataList) {
		setOrderDataList(orderDataList);
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return orderDataList.size();
	}

	public Object getValueAt(int row, int col) {
		OrderData data = orderDataList.get(row);
		if(col == 0) {
			return data.getCode();
		} else if(col == 1) {
			return data.getCustomer().getName();
		} else if(col == 2) {
			return data.getDeliveryAddress().getLine1();
		} else if(col == 3) {
			return data.getOrderEntries().get(0).getDeliveryData().getActualDeliveryQuantity();
		} else if(col == 4) {
			return data.getOrderEntries().get(0).getDeliveryData().getDeliveryVehicle().getNumber();
		} else if(col == 5) {
			return data.getOrderEntries().get(0).getDeliveryData().getDeliveryReceiptNo();
		} else if(col == 6) {
			return DateUtil.convertToString("dd-MMM-yyyy", data.getOrderEntries().get(0).getDeliveryData().getDeliveryDate());
					
		} 
		return "---";
	}
	
	public void setValueAt(Object value, int row, int col) {
		
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	

	public ReportSelectionData getSelectionData() {
		return selectionData;
	}

	public void setSelectionData(ReportSelectionData selectionData) {
		this.selectionData = selectionData;
	}

	public List<OrderData> getOrderDataList() {
		return orderDataList;
	}

	public void setOrderDataList(List<OrderData> orderDataList) {
		this.orderDataList = orderDataList;
	}

}
