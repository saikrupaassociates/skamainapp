package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingConstants;

import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.saikrupa.app.dao.ExpenseDAO;
import com.saikrupa.app.dao.impl.DefaultExpenseDAO;
import com.saikrupa.app.dto.EmployeeData;
import com.saikrupa.app.dto.ExpenseData;
import com.saikrupa.app.dto.ExpenseTypeData;
import com.saikrupa.app.dto.VendorData;
import com.saikrupa.app.ui.component.AppTextField;
import com.saikrupa.app.ui.models.ExpenseTypeListCellRenderer;
import com.saikrupa.app.ui.models.ExpenseTypeModel;

public class SearchExpenseDialog extends BaseAppDialog {

	private AppTextField paidToVendorText;
	private WebComboBox expCategoryCombo;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<ExpenseData> expenseResult;

	public SearchExpenseDialog(SKAMainApp owner) {
		super(owner, true);
		setTitle("Search Expense");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI();
		setLocationRelativeTo(owner);
		expenseResult = new ArrayList<ExpenseData>();
	}

	private void displayVendorList() {
		DisplayVendorListDialog dialog = new DisplayVendorListDialog(this);
		dialog.setVisible(true);
	}

	private void displayEmployeeList() {
		DisplayEmployeeListDialog d = new DisplayEmployeeListDialog(this);
		d.setVisible(true);
	}

	private void buildGUI() {
		final WebPanel formPanel = new WebPanel(true);
		final GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Filters");
		l1.setFont(applyTableFont());
		l1.setForeground(Color.BLUE);

		final WebLabel l11 = new WebLabel("By Expense Category", SwingConstants.RIGHT);
		l11.setFont(applyLabelFont());

		final WebLabel l12 = new WebLabel("By Paid To", SwingConstants.RIGHT);
		l12.setFont(applyLabelFont());

		WebButton searchButton = new WebButton("Search");
		searchButton.setFont(applyLabelFont());
		searchButton.setActionCommand("SEARCH");

		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setFont(applyLabelFont());
		cancelButton.setActionCommand("CANCEL");

		WebButton resetButton = new WebButton("Reset");
		resetButton.setFont(applyLabelFont());
		resetButton.setActionCommand("RESET");

		paidToVendorText = new AppTextField(15);
		paidToVendorText = new AppTextField(15);
		paidToVendorText.setEditable(false);

		final WebButton lookupButton = new WebButton();
		lookupButton.setActionCommand("LOOKUP_VENDOR_LIST");
		lookupButton.setBackground(Color.white);
		lookupButton.setIcon(createImageIcon("search.png"));
		//paidToVendorText.setModel(new VendorData());
		paidToVendorText.setTrailingComponent(lookupButton);

		expCategoryCombo = new WebComboBox(new ExpenseTypeModel());
		expCategoryCombo.setRenderer(new ExpenseTypeListCellRenderer());
		expCategoryCombo.setActionCommand("COMBO_SELECTION");

		ActionListener l = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if ("SEARCH".equalsIgnoreCase(e.getActionCommand())) {					
					ExpenseTypeModel model = (ExpenseTypeModel) expCategoryCombo.getModel();
					ExpenseTypeData expenseType = model.getSelectedItem();					
					Object paidToModel = paidToVendorText.getModel();
					if (paidToModel == null) {
						expenseResult = search(expenseType);
					} else {
						if (paidToModel instanceof VendorData) {
							VendorData data = (VendorData) paidToModel;
							expenseResult = search(expenseType, data);
						} else if (paidToModel instanceof EmployeeData) {
							EmployeeData data = (EmployeeData) paidToModel;
							expenseResult = search(expenseType, data);
						}
					}
					setExpenseResult(expenseResult);
					paidToVendorText.setModel(null);
					dispose();
				} else if ("CANCEL".equalsIgnoreCase(e.getActionCommand())) {
					dispose();
				} else if ("LOOKUP_VENDOR_LIST".equalsIgnoreCase(e.getActionCommand())) {
					displayVendorList();
				} else if ("LOOKUP_EMPLOYEE_LIST".equalsIgnoreCase(e.getActionCommand())) {
					displayEmployeeList();
				} else if ("RESET".equalsIgnoreCase(e.getActionCommand())) {
					paidToVendorText.setModel(new VendorData());
					paidToVendorText.setText("");
				} else if ("COMBO_SELECTION".equalsIgnoreCase(e.getActionCommand())) {
					ExpenseTypeModel model = (ExpenseTypeModel) expCategoryCombo.getModel();
					ExpenseTypeData data = model.getSelectedItem();
					if ("Salary Payment".equalsIgnoreCase(data.getName())) {
						lookupButton.setActionCommand("LOOKUP_EMPLOYEE_LIST");
					} else {
						lookupButton.setActionCommand("LOOKUP_VENDOR_LIST");
					}
				}
			}
		};

		searchButton.addActionListener(l);
		cancelButton.addActionListener(l);
		lookupButton.addActionListener(l);
		resetButton.addActionListener(l);
		expCategoryCombo.addActionListener(l);

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 20, 20);
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 20, 20, 20);
		layout.setConstraints(l11, c);
		formPanel.add(l11);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 20, 20);
		layout.setConstraints(expCategoryCombo, c);
		formPanel.add(expCategoryCombo);

		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 20, 20, 20);
		layout.setConstraints(l12, c);
		formPanel.add(l12);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 0, 20, 20);
		layout.setConstraints(paidToVendorText, c);
		formPanel.add(paidToVendorText);

		final WebPanel buttonPanel = new WebPanel(true);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(searchButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(resetButton);

		getContentPane().add(formPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		pack();
		setResizable(false);

	}
	
	private List<ExpenseData> search(ExpenseTypeData expenseTypeData) {
		ExpenseDAO dao = new DefaultExpenseDAO();
		Map<String, String> params = new HashMap<String, String>();
		params.put("EXPENSE_TYPE", expenseTypeData.getCode());
		return dao.searchExpenseWithFilter(params);
	}

	private List<ExpenseData> search(ExpenseTypeData expenseTypeData, VendorData data) {
		ExpenseDAO dao = new DefaultExpenseDAO();
		Map<String, String> params = new HashMap<String, String>();
		params.put("EXPENSE_TYPE", expenseTypeData.getCode());
		params.put("VENDOR", data.getCode());
		return dao.searchExpenseWithFilter(params);
	}

	private List<ExpenseData> search(ExpenseTypeData expenseTypeData, EmployeeData data) {
		ExpenseDAO dao = new DefaultExpenseDAO();
		Map<String, String> params = new HashMap<String, String>();
		params.put("EXPENSE_TYPE", expenseTypeData.getCode());
		params.put("EMPLOYEE", data.getCode());
		return dao.searchExpenseWithFilter(params);
	}

	public AppTextField getPaidToVendorText() {
		return paidToVendorText;
	}

	public void setPaidToVendorText(AppTextField paidToVendorText) {
		this.paidToVendorText = paidToVendorText;
	}

	public WebComboBox getExpCategoryCombo() {
		return expCategoryCombo;
	}

	public void setExpCategoryCombo(WebComboBox expCategoryCombo) {
		this.expCategoryCombo = expCategoryCombo;
	}

	public List<ExpenseData> getExpenseResult() {
		return expenseResult;
	}

	public void setExpenseResult(List<ExpenseData> expenseResult) {
		this.expenseResult = expenseResult;
	}

}
