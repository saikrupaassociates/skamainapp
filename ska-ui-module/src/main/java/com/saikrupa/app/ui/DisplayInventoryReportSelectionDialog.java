package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alee.extended.date.WebDateField;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.rootpane.WebDialog;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.dto.FilterParameter;
import com.saikrupa.app.ui.models.ProductListCellRenderer;
import com.saikrupa.app.ui.models.ProductSelectionModel;
import com.saikrupa.app.ui.models.ProductionMachineListCellRenderer;
import com.saikrupa.app.ui.models.ProductionMachineModel;

public class DisplayInventoryReportSelectionDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean actionCancelled;
	private ReportSelectionData selectionData;


	private WebComboBox machineSelectionCombo;
	private WebComboBox productComboBox;

	public DisplayInventoryReportSelectionDialog(SKAMainApp owner) {
		super(owner, true);
		setTitle("Select Report Type");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);

		buildNewUI();
		setLocationRelativeTo(owner);
		setModal(true);
	}

	private void buildNewUI() {
		WebPanel formPanel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		final WebRadioButton consolidatedRadio = new WebRadioButton("Consolidated", true);
		consolidatedRadio.setActionCommand("CONSOLIDATED");
		consolidatedRadio.setFont(applyLabelFont());

		WebRadioButton filterRadio = new WebRadioButton("Filtered", true);
		filterRadio.setActionCommand("FILTERED");
		filterRadio.setFont(applyLabelFont());

		WebButtonGroup bg = new WebButtonGroup(true);
		bg.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		bg.add(consolidatedRadio);
		bg.add(filterRadio);
		bg.setButtonsSelectedForeground(Color.GREEN);
		consolidatedRadio.setSelected(false);
		filterRadio.setSelected(false);

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 20, 20);
		layout.setConstraints(bg, c);
		formPanel.add(bg);

		final WebPanel filteredPanel = new WebPanel();
		GridBagLayout filteredLayout = new GridBagLayout();
		filteredPanel.setLayout(filteredLayout);
		GridBagConstraints filteredConstraints = new GridBagConstraints();
		filteredConstraints.fill = GridBagConstraints.HORIZONTAL;

		final WebCheckBox dateCB = new WebCheckBox("Date Range");
		dateCB.setActionCommand("BY_DATE");
		dateCB.setFont(applyLabelFont());

		final WebCheckBox machineCB = new WebCheckBox("Machine");
		machineCB.setActionCommand("BY_MACHINE");
		machineCB.setFont(applyLabelFont());

		machineSelectionCombo = new WebComboBox(new ProductionMachineModel());
		machineSelectionCombo.setRenderer(new ProductionMachineListCellRenderer());
		
		final WebCheckBox productCB = new WebCheckBox("Product");
		productCB.setActionCommand("BY_PRODUCT");
		productCB.setFont(applyLabelFont());
		
		productComboBox = new WebComboBox(new ProductSelectionModel());
		productComboBox.setRenderer(new ProductListCellRenderer());

		final WebLabel tl1 = new WebLabel("From :");
		tl1.setFont(applyLabelFont());
		final WebDateField fromDateField = new WebDateField(new Date());

		final WebLabel tl2 = new WebLabel("To :");
		tl2.setFont(applyLabelFont());
		final WebDateField toDateField = new WebDateField(new Date());

		filteredConstraints.gridx = 0;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 20, 10, 20);
		filteredLayout.setConstraints(dateCB, filteredConstraints);
		filteredPanel.add(dateCB);

		filteredConstraints.gridx = 1;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 0, 10, 0);
		filteredLayout.setConstraints(tl1, filteredConstraints);
		filteredPanel.add(tl1);

		filteredConstraints.gridx = 2;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 0, 10, 10);
		filteredLayout.setConstraints(fromDateField, filteredConstraints);
		filteredPanel.add(fromDateField);

		filteredConstraints.gridx = 3;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 0, 10, 0);
		filteredLayout.setConstraints(tl2, filteredConstraints);
		filteredPanel.add(tl2);

		filteredConstraints.gridx = 4;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 0, 10, 10);
		filteredLayout.setConstraints(toDateField, filteredConstraints);
		filteredPanel.add(toDateField);

		filteredConstraints.gridx = 0;
		filteredConstraints.gridy = 1;
		filteredConstraints.insets = new Insets(0, 20, 10, 20);
		filteredLayout.setConstraints(productCB, filteredConstraints);
		filteredPanel.add(productCB);

		filteredConstraints.gridx = 1;
		filteredConstraints.gridy = 1;
		filteredConstraints.insets = new Insets(0, 0, 10, 10);
		filteredConstraints.gridwidth = 5;
		filteredLayout.setConstraints(productComboBox, filteredConstraints);
		filteredPanel.add(productComboBox);

		filteredConstraints.gridx = 0;
		filteredConstraints.gridy = 2;
		filteredConstraints.gridwidth = 1;
		filteredConstraints.insets = new Insets(0, 20, 10, 20);
		filteredLayout.setConstraints(machineCB, filteredConstraints);
		filteredPanel.add(machineCB);		
		

		filteredConstraints.gridx = 1;
		filteredConstraints.gridy = 2;
		filteredConstraints.insets = new Insets(0, 0, 10, 10);
		filteredConstraints.gridwidth = 5;
		filteredLayout.setConstraints(machineSelectionCombo, filteredConstraints);
		filteredPanel.add(machineSelectionCombo);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 20, 20);
		filteredConstraints.gridwidth = 1;
		layout.setConstraints(filteredPanel, c);
		formPanel.add(filteredPanel);

		WebPanel buttonPanel = new WebPanel();
		WebButton okButton = new WebButton("OK");
		okButton.setActionCommand("OK_ACTION");
		okButton.setFont(applyLabelFont());

		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setActionCommand("CANCEL_ACTION");
		cancelButton.setFont(applyLabelFont());

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 0));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);		
		

		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("CONSOLIDATED".equalsIgnoreCase(e.getActionCommand())) {
					dateCB.setEnabled(false);
					machineCB.setEnabled(false);
					machineSelectionCombo.setEnabled(false);
					productCB.setEnabled(false);
					productComboBox.setEnabled(false);
					tl1.setEnabled(false);
					tl2.setEnabled(false);

					fromDateField.setEnabled(false);
					toDateField.setEnabled(false);

				} else if ("FILTERED".equalsIgnoreCase(e.getActionCommand())) {
					dateCB.setEnabled(true);
					machineCB.setEnabled(true);
					productCB.setEnabled(true);					
				} else if (e.getActionCommand().equalsIgnoreCase("OK_ACTION")) {
					setActionCancelled(false);
					ReportSelectionData selectionData = new ReportSelectionData();
					selectionData.setSelectionType(consolidatedRadio.isSelected() ? 0 : 1);
					
					List<FilterParameter> params = new ArrayList<FilterParameter>();
					
					if(dateCB.isSelected()) {
						FilterParameter param = new FilterParameter();
						param.setFilterName("DATE");
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("FROM_DATE", new Timestamp(fromDateField.getDate().getTime()));
						parameters.put("TO_DATE", new Timestamp(toDateField.getDate().getTime()));
						param.setParameters(parameters);
						params.add(param);
					}
					if(machineCB.isSelected()) {
						FilterParameter param = new FilterParameter();
						param.setFilterName("MACHINE");
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("MACHINE", machineSelectionCombo.getSelectedItem());
						param.setParameters(parameters);
						params.add(param);
						
					}
					if(productCB.isSelected()) {
						FilterParameter param = new FilterParameter();
						param.setFilterName("PRODUCT");
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("PRODUCT", productComboBox.getSelectedItem());
						param.setParameters(parameters);
						params.add(param);
						
					}		
					if(!params.isEmpty()) {
						selectionData.setFilters(params);
					}	
					setSelectionData(selectionData);
					dispose();
				} else if (e.getActionCommand().equalsIgnoreCase("CANCEL_ACTION")) {
					setActionCancelled(true);
					setSelectionData(null);
					dispose();				
				} else if (e.getActionCommand().equalsIgnoreCase("BY_DATE")) {
					if(dateCB.isSelected()) {
						tl1.setEnabled(true);
						tl2.setEnabled(true);
						fromDateField.setEnabled(true);
						toDateField.setEnabled(true);
					} else {
						tl1.setEnabled(false);
						tl2.setEnabled(false);
						fromDateField.setEnabled(false);
						toDateField.setEnabled(false);
					}
					
				} else if (e.getActionCommand().equalsIgnoreCase("BY_MACHINE")) {
					if(machineCB.isSelected()) {
						machineSelectionCombo.setEnabled(true);
					} else {
						machineSelectionCombo.setEnabled(false);
					}
				} else if (e.getActionCommand().equalsIgnoreCase("BY_PRODUCT")) {
					if(productCB.isSelected()) {
						productComboBox.setEnabled(true);
					} else {
						productComboBox.setEnabled(false);
					}					
				}
			}
		};

		consolidatedRadio.addActionListener(l);
		filterRadio.addActionListener(l);
		
		dateCB.addActionListener(l);
		machineCB.addActionListener(l);
		productCB.addActionListener(l);
		
		okButton.addActionListener(l);
		cancelButton.addActionListener(l);
		

		getContentPane().add(formPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
	}	

	public boolean isActionCancelled() {
		return actionCancelled;
	}

	public void setActionCancelled(boolean actionCancelled) {
		this.actionCancelled = actionCancelled;
	}

	public ReportSelectionData getSelectionData() {
		return selectionData;
	}

	public void setSelectionData(ReportSelectionData selectionData) {
		this.selectionData = selectionData;
	}

	public WebComboBox getMachineSelectionCombo() {
		return machineSelectionCombo;
	}

	public void setMachineSelectionCombo(WebComboBox machineSelectionCombo) {
		this.machineSelectionCombo = machineSelectionCombo;
	}
}
