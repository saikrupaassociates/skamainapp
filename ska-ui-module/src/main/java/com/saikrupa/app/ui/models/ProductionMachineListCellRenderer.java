package com.saikrupa.app.ui.models;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.saikrupa.app.dto.MachineData;

public class ProductionMachineListCellRenderer implements ListCellRenderer<MachineData> {
	
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	public Component getListCellRendererComponent(JList<? extends MachineData> paramJList, MachineData data,
			int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
		
		JLabel label = (JLabel)defaultRenderer.getListCellRendererComponent(paramJList, data, paramInt, paramBoolean1, paramBoolean2);
		label.setText(data.getName());
		label.setForeground(Color.BLUE);
		return label;
	}

}
