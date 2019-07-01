package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebTextField;
import com.saikrupa.app.dto.CustomerData;
import com.saikrupa.app.dto.EmployeeData;
import com.saikrupa.app.service.EmployeeService;
import com.saikrupa.app.service.impl.DefaultEmployeeService;
import com.saikrupa.app.ui.order.CustomerDetailPanel;

public class CreateCustomerDialog extends BaseAppDialog {

	private static final long serialVersionUID = 1L;
	
	private boolean cancelledOperation;

	public CreateCustomerDialog(CustomerDetailPanel owner) {
		super(owner.getOwner().getMainApp(), true);
		setTitle("Create New Customer...");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI(owner);
		setLocationRelativeTo(owner);
		setResizable(false);
	}

	private void buildGUI(final CustomerDetailPanel owner) {
		WebPanel formPanel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Customer Name : ", SwingConstants.RIGHT);
		l1.setFont(applyLabelFont());
		final WebTextField customerNameText = new WebTextField(20);

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(customerNameText, c);
		formPanel.add(customerNameText);

		WebLabel l2 = new WebLabel("Primary Contact Number : ", SwingConstants.RIGHT);
		l2.setFont(applyLabelFont());
		final WebTextField contactNumberText = new WebTextField(12);

		c.gridx = 0;
		c.gridy = 1;

		layout.setConstraints(l2, c);
		formPanel.add(l2);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(contactNumberText, c);
		formPanel.add(contactNumberText);

		WebButton createButton = new WebButton("Create Customer");
		createButton.setActionCommand("CREATE_CUSTOMER");
		createButton.setFont(applyLabelFont());

		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.setFont(applyLabelFont());

		WebPanel buttonPanel = new WebPanel(true);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(createButton);
		buttonPanel.add(cancelButton);

		getContentPane().add(formPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("CREATE_CUSTOMER")) {
					CustomerData data = new CustomerData();
					data.setActive(true);
					data.setName(customerNameText.getText());
					data.setPrimaryContact(contactNumberText.getText());					
					dispose();					
					owner.updateNewCustomerData(data);
				} else if (e.getActionCommand().equalsIgnoreCase("CANCEL")) {
					setCancelledOperation(true);
					dispose();
				}
			}
		};
		createButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
	}

	private boolean processCreateEmployeeEvent(final EmployeeData employee) {
		EmployeeService service = new DefaultEmployeeService();
		try {
			service.createEmployee(employee);
		} catch (Exception w) {
			w.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isCancelledOperation() {
		return cancelledOperation;
	}

	public void setCancelledOperation(boolean cancelledOperation) {
		this.cancelledOperation = cancelledOperation;
	}
}
