package com.saikrupa.app.ui.models;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.saikrupa.app.dto.VehicleData;

public class VehicleListCellRenderer implements ListCellRenderer<VehicleData> {

	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	public VehicleListCellRenderer() {
		// TODO Auto-generated constructor stub
	}

	public Component getListCellRendererComponent(JList<? extends VehicleData> paramJList, VehicleData data,
			int paramInt, boolean paramBoolean1, boolean paramBoolean2) {

		JLabel label = (JLabel) defaultRenderer.getListCellRendererComponent(paramJList, data, paramInt, paramBoolean1,
				paramBoolean2);
		if (label == null || data == null) {
			label.setText("-- Not Available --");
		} else {
			label.setText(data.getNumber());			
		}
		return label;
	}

}
