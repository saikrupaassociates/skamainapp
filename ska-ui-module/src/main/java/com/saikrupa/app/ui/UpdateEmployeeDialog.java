package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import com.alee.extended.date.WebDateField;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebTextField;
import com.saikrupa.app.dao.impl.DefaultEmployeeDAO;
import com.saikrupa.app.dto.ApplicationRole;
import com.saikrupa.app.dto.EmployeeData;
import com.saikrupa.app.service.EmployeeService;
import com.saikrupa.app.service.impl.DefaultEmployeeService;
import com.saikrupa.app.ui.component.AppWebLabel;
import com.saikrupa.app.ui.models.EmployeeTableModel;

public class UpdateEmployeeDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WebList contactPersonsList;
	private EmployeeData currentEmployee;

	public UpdateEmployeeDialog(SKAMainApp owner, EmployeeData employee) {
		super(owner, true);
		setTitle("Update Employee...");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI(owner, employee);
		setLocationRelativeTo(owner);
		setResizable(false);
	}

	@SuppressWarnings("unchecked")
	private void buildGUI(final SKAMainApp owner, EmployeeData employee) {
		setCurrentEmployee(employee);
		WebPanel formPanel = new WebPanel();
		formPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		WebLabel l0 = new WebLabel("Employee Code : ", SwingConstants.RIGHT);
		final WebLabel codeText = new WebLabel(employee.getCode());
		l0.setFont(applyLabelFont());
		
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l0, c);
		formPanel.add(l0);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(codeText, c);
		formPanel.add(codeText);

		WebLabel l1 = new WebLabel("Employee Name : ", SwingConstants.RIGHT);
		final WebTextField nameText = new WebTextField(30);
		nameText.setText(employee.getName());
		l1.setFont(applyLabelFont());

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(nameText, c);
		formPanel.add(nameText);

		WebLabel l2 = new WebLabel("Primary Contact No : ", SwingConstants.RIGHT);
		final WebTextField contactText = new WebTextField(15);
		contactText.setText(employee.getPrimaryContactNo());
		l2.setFont(applyLabelFont());

		c.gridx = 0;
		c.gridy = 2;

		layout.setConstraints(l2, c);
		formPanel.add(l2);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(contactText, c);
		formPanel.add(contactText);

		WebLabel l3 = new WebLabel("Date of Joining : ", SwingConstants.RIGHT);
		final WebDateField dojField = new WebDateField(new Date());
		dojField.setDate(employee.getJoiningDate());
		l3.setFont(applyLabelFont());

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l3, c);
		formPanel.add(l3);

		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(dojField, c);
		formPanel.add(dojField);

		WebLabel l4 = new WebLabel("ID Proof Document : ", SwingConstants.RIGHT);
		final WebTextField idProofDocumentField = new WebTextField(15);
		l4.setFont(applyLabelFont());

		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l4, c);
		formPanel.add(l4);
		
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(idProofDocumentField, c);
		formPanel.add(idProofDocumentField);
		
		WebButton uploadIDButton = new WebButton("Upload");
		uploadIDButton.setActionCommand("UPLOAD_ID_PROOF");
		c.gridx = 2;
		c.gridy = 4;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(uploadIDButton, c);
		formPanel.add(uploadIDButton);

		WebLabel l5 = new WebLabel("Address Proof Document : ", SwingConstants.RIGHT);
		final WebTextField addressProofField = new WebTextField(15);
		l5.setFont(applyLabelFont());

		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(l5, c);
		formPanel.add(l5);

		c.gridx = 1;
		c.gridy = 5;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(addressProofField, c);
		formPanel.add(addressProofField);
		
		WebButton uploadProofButton = new WebButton("Upload");
		uploadProofButton.setActionCommand("UPLOAD_ADDRESS_PROOF");
		c.gridx = 2;
		c.gridy = 5;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(uploadProofButton, c);
		formPanel.add(uploadProofButton);
		
		WebPanel buttonPanel = new WebPanel(new FlowLayout(FlowLayout.RIGHT));
		
		WebButton createEmployeeButton = new WebButton("Update Employee");		
		createEmployeeButton.setFont(applyLabelFont());
		createEmployeeButton.setActionCommand("UPDATE_EMPLOYEE");
		
		WebButton addRoleButton = new WebButton("Set Role");		
		addRoleButton.setFont(applyLabelFont());
		addRoleButton.setActionCommand("SET_ROLE");
		
		WebButton addRevisionButton = new WebButton("Salary Revisions");
		addRevisionButton.setFont(applyLabelFont());
		addRevisionButton.setActionCommand("REVISIONS");
		
		buttonPanel.add(addRoleButton);
		buttonPanel.add(createEmployeeButton);
		buttonPanel.add(addRevisionButton);

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("UPDATE_EMPLOYEE")) {
					if (nameText.getText().isEmpty()) {
						final WebPopOver popOver = new WebPopOver(UpdateEmployeeDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Missing Employee Name"));
						popOver.show((WebTextField) nameText);
					} else if (contactText.getText().isEmpty()) {
						final WebPopOver popOver = new WebPopOver(UpdateEmployeeDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Missing Employee Primary Contact Number"));
						popOver.show((WebTextField) contactText);
					} else {
						EmployeeData employee = getCurrentEmployee();
						employee.setName(nameText.getText());
						employee.setPrimaryContactNo(contactText.getText());
						employee.setJoiningDate(dojField.getDate());
						employee.setActive(true);
						
						if (processUpdateEmployeeEvent(employee)) {
							dispose();
							EmployeeTableModel tableModel = (EmployeeTableModel) owner.getEmployeeContentTable().getModel();
							tableModel.setEmployeeDataList(new DefaultEmployeeDAO().getAllEmployees());
							tableModel.fireTableDataChanged();
							showSuccessNotification();
						}
					}
				} else if (e.getActionCommand().equalsIgnoreCase("UPLOAD_ID_PROOF")) {
					WebFileChooser chooser = new WebFileChooser("/");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf", "PDF");
					chooser.setFileFilter(filter);
					chooser.setDialogTitle("Open...");
					int selection = chooser.showOpenDialog(UpdateEmployeeDialog.this);
					if(selection == WebFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();
						idProofDocumentField.setText(file.getName());
						final String fileName = getCurrentEmployee().getCode()+"_ID_PROOF.pdf";
						File destFile = new File(fileName);
						try {
							FileUtils.copyFile(file, destFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}						
					}
				} else if (e.getActionCommand().equalsIgnoreCase("UPLOAD_ADDRESS_PROOF")) {
					WebFileChooser chooser = new WebFileChooser("/");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf", "PDF");
					chooser.setFileFilter(filter);
					chooser.setDialogTitle("Open...");
					int selection = chooser.showOpenDialog(UpdateEmployeeDialog.this);
					if(selection == WebFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();
						addressProofField.setText(file.getName());
						final String fileName = getCurrentEmployee().getCode()+"_ADDRESS_PROOF.pdf";
						File destFile = new File(fileName);
						try {
							FileUtils.copyFile(file, destFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}						
					}
				} else if(e.getActionCommand().equalsIgnoreCase("REVISIONS")) {					
					showRevisionDialog();
				} else if(e.getActionCommand().equalsIgnoreCase("SET_ROLE")) {					
					ApplicationRole[] roles = { ApplicationRole.ADMIN, ApplicationRole.EMPLOYEE };
					ApplicationRole selected = (ApplicationRole) WebOptionPane.showInputDialog(UpdateEmployeeDialog.this,
							"Role", "Select Role", WebOptionPane.QUESTION_MESSAGE, null,
							roles, null);

					if (selected != null) {
						getCurrentEmployee().setRole(selected);
						
					}

				}
			}
		};
		createEmployeeButton.addActionListener(listener);
		uploadIDButton.addActionListener(listener);
		uploadProofButton.addActionListener(listener);
		addRevisionButton.addActionListener(listener);
		addRoleButton.addActionListener(listener);
		
		getContentPane().add(new GroupPanel(formPanel), BorderLayout.CENTER);
		getContentPane().add(buttonPanel , BorderLayout.SOUTH);
		pack();
	}
	
	private void showRevisionDialog() {
		ManageRevisionDialog dialog = new ManageRevisionDialog(this, getCurrentEmployee());
		dialog.setVisible(true);
		setCurrentEmployee(dialog.getCurrentEmployee());
	}

	private boolean processUpdateEmployeeEvent(final EmployeeData employee) {
		EmployeeService service = new DefaultEmployeeService();
		try {
			service.updateEmployee(employee);	
		} catch (Exception w) {
			w.printStackTrace();
			return false;
		}
		return true;
	}

	public WebList getContactPersonsList() {
		return contactPersonsList;
	}

	public EmployeeData getCurrentEmployee() {
		return currentEmployee;
	}

	public void setCurrentEmployee(EmployeeData currentEmployee) {
		this.currentEmployee = currentEmployee;
	}
}
