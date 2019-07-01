package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import com.alee.extended.date.WebDateField;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebTextField;
import com.saikrupa.app.dao.impl.DefaultEmployeeDAO;
import com.saikrupa.app.dto.EmployeeData;
import com.saikrupa.app.service.EmployeeService;
import com.saikrupa.app.service.impl.DefaultEmployeeService;
import com.saikrupa.app.ui.component.AppWebLabel;
import com.saikrupa.app.ui.models.EmployeeTableModel;

public class CreateEmployeeDialog extends BaseAppDialog {

	/**
	 * Admin user has the access to perform Employee Specific operation 
	 */
	private static final long serialVersionUID = 1L;

	private WebList contactPersonsList;

	public CreateEmployeeDialog(SKAMainApp owner) {
		super(owner, true);
		setTitle("Create New Employee...");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI(owner);
		setLocationRelativeTo(owner);
		setResizable(false);
	}

	private void buildGUI(final SKAMainApp owner) {
		WebPanel formPanel = new WebPanel();
		formPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Employee Name : ", SwingConstants.RIGHT);
		final WebTextField nameText = new WebTextField(30);
		l1.setFont(applyLabelFont());

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(nameText, c);
		formPanel.add(nameText);

		WebLabel l2 = new WebLabel("Primary Contact No : ", SwingConstants.RIGHT);
		final WebTextField contactText = new WebTextField(15);
		l2.setFont(applyLabelFont());
		
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l2, c);
		formPanel.add(l2);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(contactText, c);
		formPanel.add(contactText);

		WebLabel l3 = new WebLabel("Date of Joining : ", SwingConstants.RIGHT);
		final WebDateField dojField = new WebDateField(new Date());
		l3.setFont(applyLabelFont());
		
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l3, c);
		formPanel.add(l3);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(dojField, c);
		formPanel.add(dojField);

		WebLabel l4 = new WebLabel("ID Proof Document : ", SwingConstants.RIGHT);
		//final WebTextField idProofDocumentField = new WebTextField(15);
		l4.setFont(applyLabelFont());

//		c.gridx = 0;
//		c.gridy = 3;
//		c.anchor = GridBagConstraints.NORTH;
//		layout.setConstraints(l4, c);
//		formPanel.add(l4);

//		c.gridx = 1;
//		c.gridy = 3;
//		c.insets = new Insets(0, 10, 10, 0);
//		layout.setConstraints(idProofDocumentField, c);
//		formPanel.add(idProofDocumentField);

//		WebLabel l5 = new WebLabel("Address Proof Document : ", SwingConstants.RIGHT);
		//final WebTextField addressProofField = new WebTextField(15);

//		c.gridx = 0;
//		c.gridy = 4;
//		c.anchor = GridBagConstraints.NORTH;
//		layout.setConstraints(l5, c);
//		formPanel.add(l5);

//		c.gridx = 1;
//		c.gridy = 4;
//		c.insets = new Insets(0, 10, 10, 0);
//		layout.setConstraints(addressProofField, c);
//		formPanel.add(addressProofField);
		
		WebButton createEmployeeButton = new WebButton("Create Employee");
		createEmployeeButton.setFont(applyLabelFont());
		createEmployeeButton.setActionCommand("CREATE_EMPLOYEE");

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("CREATE_EMPLOYEE")) {
					if (nameText.getText().isEmpty()) {
						final WebPopOver popOver = new WebPopOver(CreateEmployeeDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Missing Employee Name"));
						popOver.show((WebTextField) nameText);
					} else if (contactText.getText().isEmpty()) {
						final WebPopOver popOver = new WebPopOver(CreateEmployeeDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Missing Employee Primary Contact Number"));
						popOver.show((WebTextField) contactText);
					} else {
						EmployeeData employee = new EmployeeData();
						employee.setName(nameText.getText());
						employee.setPrimaryContactNo(contactText.getText());
						employee.setJoiningDate(dojField.getDate());
						employee.setActive(false);
						
						if (processCreateEmployeeEvent(employee)) {
							dispose();
							EmployeeTableModel tableModel = (EmployeeTableModel) owner.getEmployeeContentTable().getModel();
							tableModel.setEmployeeDataList(new DefaultEmployeeDAO().getAllEmployees());
							tableModel.fireTableDataChanged();
							showSuccessNotification();
						}
					}
				} else if(e.getActionCommand().equalsIgnoreCase("CANCEL")) {
					dispose();
				}
			}
		};
		createEmployeeButton.addActionListener(listener);
		
		WebPanel buttonPanel = new WebPanel(new FlowLayout(FlowLayout.RIGHT));
		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(listener);
		
		buttonPanel.add(createEmployeeButton);
		buttonPanel.add(cancelButton);
		
		getContentPane().add(new GroupPanel(formPanel), BorderLayout.CENTER);
		getContentPane().add(new WebPanel(), BorderLayout.EAST);
		getContentPane().add(new WebPanel(), BorderLayout.WEST);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
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

	public WebList getContactPersonsList() {
		return contactPersonsList;
	}
}
