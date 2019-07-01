package com.saikrupa.app.ui.models;

import javax.swing.DefaultComboBoxModel;

import com.saikrupa.app.dto.VehicleData;
import com.saikrupa.app.service.VehicleService;
import com.saikrupa.app.service.impl.DefaultVehicleService;

public class DeliveryVehicleModel extends DefaultComboBoxModel<VehicleData> {

	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeliveryVehicleModel() {
		super();
		loadVehicleData();
	}
	
	private void loadVehicleData() {
		VehicleService service = new DefaultVehicleService();
		for(VehicleData data : service.getVehicles()) {
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
