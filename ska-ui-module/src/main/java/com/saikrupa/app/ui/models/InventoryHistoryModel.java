package com.saikrupa.app.ui.models;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.util.DateUtil;

public class InventoryHistoryModel extends AbstractTableModel {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] columnNames = {"Product", "Opening Balance", "Added", "Damaged", "Reduced","Ref. Customer", "Closing Balance", "As On", "Machine"};
	
	private List<InventoryEntryData> inventoryDataList;
	
	private ReportSelectionData reportSelectionData;
 
	public InventoryHistoryModel(List<InventoryEntryData> inventoryDataList) {
		this.inventoryDataList = inventoryDataList;
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	
	public int getRowCount() {		
		return inventoryDataList.size();
	}

	public Object getValueAt(int row, int col) {
		InventoryEntryData data = inventoryDataList.get(row);
		if(col == 0) {
			return data.getInventory().getProduct().getName();
		} else if(col == 1) {
			return data.getOpeningBalance();
		} else if(col == 2) {
			return data.getAddedQuantity();
		} else if(col == 3) {
			return data.getDamagedQuantity();
		} else if(col == 4) {
			if(data.getReferencedOrder() != null) {
				return data.getReducedQuantity();
			} else {
				return "";
			}
		} else if(col == 5) {
			if(data.getReferencedOrder() != null) {
				return data.getReferencedOrder().getCustomer().getName();
			} else {
				return "";
			}
		} else if(col == 6) {
			return data.getClosingBalance();
		} else if(col == 7) {				
			return DateUtil.convertToStandard(data.getCreatedDate());
		} else if(col == 8) {				
			return data.getMachine().getName();
		}
//		return (data.getLabourPaymentStatus() == 0 ? "Pending" : "Paid");
		return "---";
	}
	
 
	
	
	public void setValueAt(Object value, int row, int col) {
		
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public List<InventoryEntryData> getInventoryDataList() {
		return inventoryDataList;
	}

	public void setInventoryDataList(List<InventoryEntryData> inventoryDataList) {
		this.inventoryDataList = inventoryDataList;
	}

	public ReportSelectionData getReportSelectionData() {
		return reportSelectionData;
	}

	public void setReportSelectionData(ReportSelectionData reportSelectionData) {
		this.reportSelectionData = reportSelectionData;
	}

	
}
