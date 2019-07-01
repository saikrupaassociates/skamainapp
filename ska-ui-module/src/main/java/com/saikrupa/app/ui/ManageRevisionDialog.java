package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import com.alee.extended.date.WebDateField;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import com.saikrupa.app.dao.EmployeeDAO;
import com.saikrupa.app.dao.impl.DefaultEmployeeDAO;
import com.saikrupa.app.dto.EmployeeData;
import com.saikrupa.app.dto.EmployeeSalaryData;
import com.saikrupa.app.ui.models.EmployeeRevisionTableModel;
import com.saikrupa.app.util.DateUtil;

public class ManageRevisionDialog extends BaseAppDialog {

	private static final long serialVersionUID = 1L;
	private WebTable revisionContentTable;
	private EmployeeData currentEmployee;

	public ManageRevisionDialog(SKAMainApp owner) {
		super(owner);
		setSize(new Dimension(600, 800));
	}

	public ManageRevisionDialog(UpdateEmployeeDialog updateEmployeeDialog, EmployeeData employee) {
		super(updateEmployeeDialog, true);
		setCurrentEmployee(employee);
		setTitle("Manage Salary Revisions...");
		setSize(new Dimension(600, 800));
		setLocationRelativeTo(updateEmployeeDialog);
		buildGUI(updateEmployeeDialog, employee);
	}

	private void buildGUI(UpdateEmployeeDialog owner, EmployeeData employee) {

		WebPanel formPanel = new WebPanel();
		formPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Employee Code : ", SwingConstants.RIGHT);
		final WebLabel codeLabel = new WebLabel(employee.getCode());
		l1.setFont(applyLabelFont());

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(codeLabel, c);
		formPanel.add(codeLabel);

		WebLabel l2 = new WebLabel("Employee Name : ", SwingConstants.RIGHT);
		final WebLabel contactLabel = new WebLabel(employee.getName());
		l2.setFont(applyLabelFont());

		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(0, 80, 10, 0);
		layout.setConstraints(l2, c);
		formPanel.add(l2);

		c.gridx = 3;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(contactLabel, c);
		formPanel.add(contactLabel);

		/**
		 * 
		 */
		WebLabel l3 = new WebLabel("Date of Joining : ", SwingConstants.RIGHT);
		final WebLabel dojLabel = new WebLabel(DateUtil.convertToString("dd-MMM-yyyy", employee.getJoiningDate()));
		l3.setFont(applyLabelFont());

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l3, c);
		formPanel.add(l3);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(dojLabel, c);
		formPanel.add(dojLabel);

		WebLabel l4 = new WebLabel("Revised Salary : ", SwingConstants.RIGHT);
		final WebTextField revisedSalaryText = new WebTextField(15);
		l4.setFont(applyLabelFont());
		revisedSalaryText.setText("0.0");

		c.gridx = 2;
		c.gridy = 1;
		c.insets = new Insets(0, 80, 10, 0);
		layout.setConstraints(l4, c);
		formPanel.add(l4);

		c.gridx = 3;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(revisedSalaryText, c);
		formPanel.add(revisedSalaryText);

		/**
		 * Row 3
		 */

		WebLabel l5 = new WebLabel("Effective From : ", SwingConstants.RIGHT);
		final WebDateField effectiveFromText = new WebDateField(new Date());
		if (employee.getRevisions().isEmpty()) {
			effectiveFromText.setDate(employee.getJoiningDate());
		}
		
		l5.setFont(applyLabelFont());

		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l5, c);
		formPanel.add(l5);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(effectiveFromText, c);
		formPanel.add(effectiveFromText);

		WebLabel l6 = new WebLabel("Effective Till : ", SwingConstants.RIGHT);
		final WebDateField effectiveTillText = new WebDateField(new Date());
		l6.setFont(applyLabelFont());

		c.gridx = 2;
		c.gridy = 2;
		c.insets = new Insets(0, 80, 10, 0);
		layout.setConstraints(l6, c);
		formPanel.add(l6);

		c.gridx = 3;
		c.gridy = 2;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(effectiveTillText, c);
		formPanel.add(effectiveTillText);

		final WebButton addRevisionButton = new WebButton("Add Revision");
		addRevisionButton.setActionCommand("ADD_REVISION");
		addRevisionButton.setFont(applyLabelFont());
		c.gridx = 4;
		c.gridy = 3;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(addRevisionButton, c);
		formPanel.add(addRevisionButton);
		EmployeeDAO dao = new DefaultEmployeeDAO();
		List<EmployeeSalaryData> revisions = dao.findRevisionsByEmployee(getCurrentEmployee());
		revisionContentTable = new WebTable(new EmployeeRevisionTableModel(revisions));

		revisionContentTable.getTableHeader().setFont(applyLabelFont());
		revisionContentTable.setRowHeight(25);
		revisionContentTable.setFont(applyTableFont());

		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.setFont(applyLabelFont());

		final WebButton saveButton = new WebButton("Save");
		saveButton.setEnabled(false);
		saveButton.setActionCommand("SAVE");
		saveButton.setFont(applyLabelFont());

		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("CANCEL")) {
					dispose();
				} else if (e.getActionCommand().equalsIgnoreCase("ADD_REVISION")) {
					EmployeeSalaryData data = new EmployeeSalaryData();
					data.setEmployee(getCurrentEmployee());
					data.setEffectiveFrom(effectiveFromText.getDate());
					data.setEffectiveTill(effectiveTillText.getDate());
					data.setSalary(Double.valueOf(revisedSalaryText.getText()).doubleValue());
					updateRevisionTableModel(data);
					revalidate();
					addRevisionButton.setEnabled(false);
					saveButton.setEnabled(true);
				} else if (e.getActionCommand().equalsIgnoreCase("SAVE")) {
					EmployeeRevisionTableModel model = (EmployeeRevisionTableModel) revisionContentTable.getModel();
					currentEmployee.setRevisions(model.getRevisionDataList());
					dispose();
				}
			}
		};
		addRevisionButton.addActionListener(l);
		saveButton.addActionListener(l);
		cancelButton.addActionListener(l);

		add(new GroupPanel(formPanel), BorderLayout.NORTH);
		WebPanel mainPanel = new WebPanel(new BorderLayout());
		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new WebScrollPane(revisionContentTable), BorderLayout.CENTER);

		WebPanel southPanel = new WebPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(saveButton);
		southPanel.add(cancelButton);
		add(mainPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		setSize(800, 400);
		setLocationRelativeTo(owner);
	}

	private void updateRevisionTableModel(EmployeeSalaryData data) {
		data.setCurrentRevision(true);
		EmployeeRevisionTableModel model = (EmployeeRevisionTableModel) revisionContentTable.getModel();
		model.getRevisionDataList().add(0, data);
		model.fireTableDataChanged();
		revalidate();

	}

	public WebTable getRevisionContentTable() {
		return revisionContentTable;
	}

	public void setRevisionContentTable(WebTable revisionContentTable) {
		this.revisionContentTable = revisionContentTable;
	}

	public EmployeeData getCurrentEmployee() {
		return currentEmployee;
	}

	public void setCurrentEmployee(EmployeeData currentEmployee) {
		this.currentEmployee = currentEmployee;
	}

}
