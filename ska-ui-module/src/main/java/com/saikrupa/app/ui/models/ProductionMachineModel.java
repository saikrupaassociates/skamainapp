package com.saikrupa.app.ui.models;

import javax.swing.DefaultComboBoxModel;

import com.saikrupa.app.dto.MachineData;
import com.saikrupa.app.service.MachineService;
import com.saikrupa.app.service.impl.DefaultMachineService;

public class ProductionMachineModel extends DefaultComboBoxModel<MachineData> {

	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductionMachineModel() {
		super();
		loadMachineData();
	}
	
	private void loadMachineData() {
		MachineService service = new DefaultMachineService();
		for(MachineData data : service.getMachines()) {
			addElement(data);
		}
		
	}
	
	@Override
    public Object getSelectedItem() {		
		return super.getSelectedItem();
	}
	
	@Override
	public void setSelectedItem(Object anObject) {
		super.setSelectedItem(anObject);	
			
	}
}
