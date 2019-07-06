package com.saikrupa.app.ui.order;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.SwingConstants;

import com.alee.extended.date.WebDateField;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import com.alee.utils.CollectionUtils;
import com.saikrupa.app.dao.OrderDAO;
import com.saikrupa.app.dao.impl.DefaultOrderDAO;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.dto.PaymentStatus;
import com.saikrupa.app.ui.BaseAppDialog;
import com.saikrupa.app.ui.SKAMainApp;
import com.saikrupa.app.ui.component.AppWebLabel;
import com.saikrupa.app.ui.models.PaymentEntryTableModel;

public class UpdateOrderPaymentDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UpdateOrderDialog parentDialog;
	private OrderEntryData currentOrderEntry;
	private WebTable paymentEntryTable;
	private WebButton addPaymentButton;
	private WebDateField paymentEntryDateText;

	private WebTextField actualPaidAmountText;

	public UpdateOrderPaymentDialog(SKAMainApp owner) {
		super(owner);
	}

	public UpdateOrderPaymentDialog(UpdateOrderDialog dialog, OrderEntryData data) {
		super(dialog);
		setParentDialog(dialog);
		setCurrentOrderEntry(data);
		setTitle("Update Payment");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI(dialog, data);
		setLocationRelativeTo(dialog);
		setResizable(false);
	}

	private void buildGUI(UpdateOrderDialog dialog, final OrderEntryData data) {
		WebPanel formPanel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Order Number : ", SwingConstants.RIGHT);
		WebLabel t1 = new WebLabel(data.getOrder().getCode());
		t1.setFont(applyLabelFont());

		WebLabel l2 = new WebLabel("Product : ", SwingConstants.RIGHT);
		WebLabel t2 = new WebLabel(data.getProduct().getName());
		t2.setFont(applyLabelFont());

		WebLabel l3 = new WebLabel("Total Payable Amount : ", SwingConstants.RIGHT);
		WebLabel t3 = new WebLabel(String.valueOf(data.getOrder().getTotalPrice()));
		t3.setFont(applyLabelFont());

		WebLabel l31 = new WebLabel("Paid Amount : ", SwingConstants.RIGHT);
		actualPaidAmountText = new WebTextField(20);

		WebLabel l4 = new WebLabel("Payment Date : ", SwingConstants.RIGHT);
		paymentEntryDateText = new WebDateField(new Date());

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 15, 5, 0);
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 15, 5, 0);
		layout.setConstraints(t1, c);
		formPanel.add(t1);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(15, 0, 5, 0); // top padding
		layout.setConstraints(l2, c);
		formPanel.add(l2);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(15, 15, 5, 0);
		layout.setConstraints(t2, c);
		formPanel.add(t2);

		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(15, 0, 5, 0); // top padding
		layout.setConstraints(l3, c);
		formPanel.add(l3);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(15, 15, 5, 0);
		layout.setConstraints(t3, c);
		formPanel.add(t3);

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(15, 0, 0, 0); // top padding
		layout.setConstraints(l31, c);
		formPanel.add(l31);

		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(15, 15, 0, 0);
		layout.setConstraints(actualPaidAmountText, c);
		formPanel.add(actualPaidAmountText);

		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(15, 0, 15, 0); // top padding
		layout.setConstraints(l4, c);
		formPanel.add(l4);

		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(15, 15, 15, 0);
		layout.setConstraints(paymentEntryDateText, c);
		formPanel.add(paymentEntryDateText);
		
		OrderDAO orderDAO = new DefaultOrderDAO();

		paymentEntryTable = new WebTable(new PaymentEntryTableModel(orderDAO.getPaymentEntries(data)));
		paymentEntryTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 12));
		paymentEntryTable.setRowHeight(30);
		paymentEntryTable.setFont(new Font("verdana", Font.PLAIN, 12));

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("ADD_PAYMENT")) {
					if(actualPaidAmountText.getText().isEmpty() || !isNumeric(actualPaidAmountText.getText())) {
						final WebPopOver popOver = new WebPopOver(UpdateOrderPaymentDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Paid Amount is not filled or invalid"));
						popOver.show((WebTextField) actualPaidAmountText);						
					} else {
						if (data.getOrder().getTotalPrice() < Double.valueOf(actualPaidAmountText.getText())) {
							WebOptionPane.showMessageDialog(UpdateOrderPaymentDialog.this, "Can not over pay Order");
						} else {
							evaluatePayments(data);
						}						
					}
				} else if (e.getActionCommand().equalsIgnoreCase("CANCEL")) {
					dispose();
				} else if (e.getActionCommand().equalsIgnoreCase("UPDATE_PAYMENT")) {
					updatePaymentData(getCurrentOrderEntry());
				}
			}
		};

		addPaymentButton = new WebButton("Add Payment");
		addPaymentButton.setEnabled(false);
		c.gridx = 2;
		c.gridy = 4;
		c.insets = new Insets(15, 15, 15, 0);
		layout.setConstraints(addPaymentButton, c);
		formPanel.add(addPaymentButton);

		if (getTotalPaymentEntries(data) == data.getOrder().getTotalPrice()) {
			addPaymentButton.setEnabled(false);
		}

		WebButton updatePaymentButton = new WebButton(" Ok ");
		updatePaymentButton.setEnabled(false);
		WebButton cancelButton = new WebButton("Cancel");
		WebPanel buttPanel = new WebPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		buttPanel.add(updatePaymentButton);
		buttPanel.add(cancelButton);

		addPaymentButton.setActionCommand("ADD_PAYMENT");
		cancelButton.setActionCommand("CANCEL");
		updatePaymentButton.setActionCommand("UPDATE_PAYMENT");

		addPaymentButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
		updatePaymentButton.addActionListener(listener);

		WebPanel p1 = new WebPanel();
		p1.setLayout(new BorderLayout());
		p1.add(formPanel, BorderLayout.WEST);

		getContentPane().add(p1, BorderLayout.NORTH);
		getContentPane().add(new WebScrollPane(paymentEntryTable), BorderLayout.CENTER);
		//getContentPane().add(buttPanel, BorderLayout.SOUTH);
		setSize(900, 500);
	}

	private Double getTotalPaymentEntries(OrderEntryData orderEntry) {
		Double value = new Double(0);
		if (CollectionUtils.isEmpty(orderEntry.getPaymentEntries())) {
			return value;
		}	

		for (PaymentEntryData paymentEntry : orderEntry.getPaymentEntries()) {
			value = value + paymentEntry.getAmount();
		}
		return value;
	}

	private void evaluatePayments(OrderEntryData data) {
		PaymentEntryTableModel model = (PaymentEntryTableModel) paymentEntryTable.getModel();
		PaymentEntryData d = new PaymentEntryData();
		d.setOrderEntryData(data);
		d.setAmount(Double.valueOf(actualPaidAmountText.getText()));
		d.setEntryNumber(model.getPaymentEntryList().size() + 1);

		Double alreadyPaidAmount = getTotalPaymentEntries(data);
		Double totalPayableAmount = data.getOrder().getTotalPrice();

		if (totalPayableAmount > alreadyPaidAmount) {
			if ((totalPayableAmount - alreadyPaidAmount) > d.getAmount()) {				
				d.setPaymentStatus(PaymentStatus.PARTIAL);
			} else if ((totalPayableAmount - alreadyPaidAmount) == d.getAmount()) {
				d.setPaymentStatus(PaymentStatus.PAID);
				data.getOrder().setPaymentStatus(PaymentStatus.PAID);
				addPaymentButton.setEnabled(false);
			} else if ((totalPayableAmount - alreadyPaidAmount) < d.getAmount()) {
				String msg = "paid amount (" + d.getAmount() + ") can not exceed pending payable amount ("
						+ (totalPayableAmount - alreadyPaidAmount) + ")";
				WebOptionPane.showMessageDialog(this, msg);
				return;
			}			
		}
		d.setPayableAmount(totalPayableAmount - alreadyPaidAmount);
		d.setPaymentDate(paymentEntryDateText.getDate());
		model.getPaymentEntryList().add(d);
		model.fireTableDataChanged();
		revalidate();
		data.setPaymentEntries(model.getPaymentEntryList());
		setCurrentOrderEntry(data);

	}

	protected void updatePaymentData(OrderEntryData data) {
		OrderData orderData = getParentDialog().getCurrentOrder();
		ArrayList<OrderEntryData> entries = new ArrayList<OrderEntryData>();
		entries.add(data);
		orderData.setOrderEntries(entries);
		int lastEntryNo = -1;
		for(PaymentEntryData paymentEntry : data.getPaymentEntries()) {			
			if(paymentEntry.getPaymentStatus() == PaymentStatus.PAID && paymentEntry.getEntryNumber() > lastEntryNo) {
				orderData.setPaymentStatus(PaymentStatus.PAID);
			} else {
				lastEntryNo = paymentEntry.getEntryNumber();
				orderData.setPaymentStatus(PaymentStatus.PARTIAL);
			}
		}
		getParentDialog().setCurrentOrder(orderData);
		dispose();
	}

	public UpdateOrderDialog getParentDialog() {
		return parentDialog;
	}

	public void setParentDialog(UpdateOrderDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	public OrderEntryData getCurrentOrderEntry() {
		return currentOrderEntry;
	}

	public void setCurrentOrderEntry(OrderEntryData currentOrderEntry) {
		this.currentOrderEntry = currentOrderEntry;
	}

	public WebTable getPaymentEntryTable() {
		return paymentEntryTable;
	}

	public void setPaymentEntryTable(WebTable paymentEntryTable) {
		this.paymentEntryTable = paymentEntryTable;
	}
	
	private boolean isNumeric(String text) {
		boolean numeric = true;
		try {
			Double.valueOf(text);
		} catch (NumberFormatException ne) {
			numeric = false;
		}
		return numeric;
	}
}
