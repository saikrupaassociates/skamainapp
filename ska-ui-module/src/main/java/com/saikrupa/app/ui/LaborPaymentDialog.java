package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.SwingConstants;

import com.alee.extended.date.WebDateField;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import com.saikrupa.app.dao.DefaultPaymentModeDAO;
import com.saikrupa.app.dao.ExpenseTypeDAO;
import com.saikrupa.app.dao.InvestorDAO;
import com.saikrupa.app.dao.PaymentModeDAO;
import com.saikrupa.app.dao.ProductDAO;
import com.saikrupa.app.dao.impl.DefaultExpenseTypeDAO;
import com.saikrupa.app.dao.impl.DefaultInvestorDAO;
import com.saikrupa.app.dao.impl.DefaultProductDAO;
import com.saikrupa.app.dto.ExpenseData;
import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.service.PaymentService;
import com.saikrupa.app.service.impl.DefaultPaymentService;
import com.saikrupa.app.service.impl.DefaultVendorService;
import com.saikrupa.app.ui.component.AppWebLabel;
import com.saikrupa.app.ui.models.InventoryHistoryModel;

public class LaborPaymentDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WebLabel inventoryCountLabel;
	private WebTextField payablePerBrickField;
	

	public LaborPaymentDialog(SKAMainApp owner, List<InventoryEntryData> entries) {
		super(owner, true);
		setTitle("Create Labour Expense...");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI(owner, entries);
		setLocationRelativeTo(owner);
		setResizable(false);
	}
	
	private Double getSelectedInventoryCount(List<InventoryEntryData> entries) {
		Double val = Double.valueOf(0.0);
		for(InventoryEntryData entry : entries) {
			val = val + entry.getAddedQuantity();
		}
		return val;
	}

	private void buildGUI(final SKAMainApp owner, final List<InventoryEntryData> entries) {
		if(getSelectedInventoryCount(entries) == Double.NaN) {
			WebOptionPane.showMessageDialog(owner, "Please select similar type of Products");
			return;
		}
		WebPanel formPanel = new WebPanel();		
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		


		WebLabel l1 = new WebLabel("Total Quantity Selected : ", SwingConstants.RIGHT);
		inventoryCountLabel = new WebLabel(String.valueOf(getSelectedInventoryCount(entries).doubleValue()));
		l1.setFont(applyLabelFont());

		
		WebLabel l31 = new WebLabel("Payble per Brick : ", SwingConstants.RIGHT);
		l31.setFont(applyLabelFont());
		payablePerBrickField = new WebTextField(5);
		payablePerBrickField.setText("0.60");

		WebLabel l4 = new WebLabel("Payment Date : ", SwingConstants.RIGHT);
		l4.setFont(applyLabelFont());
		final WebDateField paymentDateText = new WebDateField(new Date());		
		

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 15, 5, 0);
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 15, 5, 0);
		layout.setConstraints(inventoryCountLabel, c);
		formPanel.add(inventoryCountLabel);


		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(15, 0, 0, 0); // top padding
		layout.setConstraints(l31, c);
		formPanel.add(l31);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(15, 15, 0, 0);
		layout.setConstraints(payablePerBrickField, c);
		formPanel.add(payablePerBrickField);
		
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(15, 15, 15, 5); // top padding
		layout.setConstraints(l4, c);
		formPanel.add(l4);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(15, 15, 15, 5);
		layout.setConstraints(paymentDateText, c);
		formPanel.add(paymentDateText);
		
		final AppWebLabel paybleAmountLabel = new AppWebLabel("", SwingConstants.RIGHT);
		paybleAmountLabel.setFont(applyLabelFont());
		
	
		final WebTable table = new WebTable(new InventoryHistoryModel(entries));
		table.getTableHeader().setFont(new Font("verdana", Font.BOLD, 12));
		table.setRowHeight(30);
		table.setFont(new Font("verdana", Font.PLAIN, 12));
		
		WebButton button = new WebButton("Calculate Expense");
		button.setFont(applyLabelFont());
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Double paybleAmount = Double.valueOf(payablePerBrickField.getText()) * Double.valueOf(inventoryCountLabel.getText()); 
				paybleAmountLabel.setText("INR "+String.valueOf(paybleAmount));
				paybleAmountLabel.setBackground(Color.GREEN);
				paybleAmountLabel.setActualValue(paybleAmount);
			}
		});
		
		c.gridx = 2;
		c.gridy = 2;
		c.insets = new Insets(15, 15, 15, 5);
		layout.setConstraints(button, c);
		formPanel.add(button);	
		
		c.gridx = 3;
		c.gridy = 2;
		c.insets = new Insets(15, 15, 15, 5);
		layout.setConstraints(paybleAmountLabel, c);
		formPanel.add(paybleAmountLabel);

		
		WebButton updateButton = new WebButton("Update");
		updateButton.setActionCommand("UPDATE");
		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setActionCommand("CANCEL");
		WebPanel buttPanel = new WebPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		buttPanel.add(updateButton);
		buttPanel.add(cancelButton);
		
		ActionListener listener = new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equalsIgnoreCase("CANCEL")) {
					dispose();
				} else if(e.getActionCommand().equalsIgnoreCase("UPDATE")) {
					ManageExpenseDialog dialog = new ManageExpenseDialog();
					ExpenseData expense = new ExpenseData();
					expense.setAmount((Double) paybleAmountLabel.getActualValue());
					expense.setCreatedDate(new Date());
					expense.setExpenseDate(paymentDateText.getDate());
					
					ExpenseTypeDAO expType = new DefaultExpenseTypeDAO();
					expense.setExpenseType(expType.findExpenseTypeByCode("1000"));
					
					InvestorDAO paidBy = new DefaultInvestorDAO();
					expense.setPaidBy(paidBy.findInvestorByCode("1000"));
					
					PaymentModeDAO paymentType = new DefaultPaymentModeDAO();
					expense.setPaymentData(paymentType.getPaymentModeByCode("1000"));
					
					DefaultVendorService vendorService = new DefaultVendorService();
					expense.setVendor(vendorService.findVendorByCode("1000"));
					
					expense.setComments("Auto Created expense Labour payment - ["+expense.getExpenseType().getDescription()+"] - "+inventoryCountLabel.getText()+"@"+payablePerBrickField.getText());
					dialog.processCreateExpenseEvent(expense, owner);
					PaymentService paymentService = new DefaultPaymentService();
					paymentService.updateInventoryEntryForPayment(entries);
					InventoryHistoryModel model = (InventoryHistoryModel) owner.getProductInventoryHistoryTable().getModel();
					ProductDAO productDao = new DefaultProductDAO();					
					model.setInventoryDataList(productDao.findInventoryHistoryForAllProduct());
					model.fireTableDataChanged();
					dispose();
				}
			}
		};
		
		updateButton.addActionListener(listener);
		cancelButton.addActionListener(listener);

		
		WebPanel p1 = new WebPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		p1.add(formPanel);


		getContentPane().add(p1, BorderLayout.NORTH);		
		getContentPane().add(new WebScrollPane(table), BorderLayout.CENTER);
		getContentPane().add(buttPanel, BorderLayout.SOUTH);
		setSize(900,500);		
	}

}
