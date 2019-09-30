package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.alee.extended.date.WebDateField;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import com.saikrupa.app.dao.OrderDAO;
import com.saikrupa.app.dao.impl.CustomerDAO;
import com.saikrupa.app.dao.impl.DefaultCustomerDAO;
import com.saikrupa.app.dao.impl.DefaultOrderDAO;
import com.saikrupa.app.dto.CustomerData;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.dto.PaymentStatus;
import com.saikrupa.app.dto.PaymentTypeData;
import com.saikrupa.app.service.PaymentService;
import com.saikrupa.app.service.impl.DefaultPaymentService;
import com.saikrupa.app.session.ApplicationSession;
import com.saikrupa.app.ui.component.AppWebLabel;
import com.saikrupa.app.ui.models.AdhocPaymentEntryTableModel;
import com.saikrupa.app.ui.models.OrderTableModel;
import com.saikrupa.app.ui.models.PaymentTypeModel;
import com.saikrupa.app.ui.models.paymentTypeListCellRenderer;

public class CreateAdhocOrderPaymentDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OrderEntryData currentOrderEntry;
	private WebTable paymentEntryTable;
	private WebButton addPaymentButton;
	private WebDateField paymentEntryDateText;

	private WebTextField actualPaidAmountText;

	private PaymentEntryData currentPaymentEntry;	

	private AppWebLabel balanceAmountPayableLabel;
	
	private AppWebLabel chequeNoLabel;
	
	private WebComboBox paymentTypeCombo;

	private CustomerData currentCustomer;
	
	private static Logger LOG = Logger.getLogger(CreateAdhocOrderPaymentDialog.class);

	public CreateAdhocOrderPaymentDialog(SKAMainApp owner) {
		super(owner);
	}

	private List<OrderData> ordersWithPendingPayment;

	public CreateAdhocOrderPaymentDialog(SKAMainApp dialog, OrderTableModel orderModel) {
		super(dialog);
		setTitle("Update Payment");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI(dialog, orderModel);
		setLocationRelativeTo(dialog);
		setResizable(false);
	}

	private void buildGUI(SKAMainApp dialog, final OrderTableModel data) {
		setCurrentCustomer(data.getOrderDataList().get(0).getCustomer());
		final Double[] values = dialog.getValueNew(data);

		final WebPanel formPanel = new WebPanel();
		final GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Customer Name : ", SwingConstants.RIGHT);
		WebLabel t1 = new WebLabel(data.getOrderDataList().get(0).getCustomer().getName());
		t1.setFont(applyLabelFont());

		WebLabel l2 = new WebLabel("Total Order Amount : ", SwingConstants.RIGHT);
		WebLabel t2 = new WebLabel(String.format("%,.2f", values[0]));
		t2.setFont(applyLabelFont());

		WebLabel l21 = new WebLabel("Balance Payable : ", SwingConstants.RIGHT);
		balanceAmountPayableLabel = new AppWebLabel(String.format("%,.2f", values[2]));
		balanceAmountPayableLabel.setFont(applyLabelFont());
		balanceAmountPayableLabel.setActualValue(values[2]);
		LOG.info("balanceAmountPayableLabel value : " + balanceAmountPayableLabel.getText()+ " Actual Value ["+balanceAmountPayableLabel.getActualValue()+"]");

		WebLabel l3 = new WebLabel("Paid Amount : ", SwingConstants.RIGHT);
		actualPaidAmountText = new WebTextField(20);

		paymentTypeCombo = new WebComboBox(new PaymentTypeModel());
		paymentTypeCombo.setRenderer(new paymentTypeListCellRenderer());

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

		/**
		 * 		
		 */
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(15, 0, 5, 0); // top padding
		layout.setConstraints(l21, c);
		formPanel.add(l21);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(15, 15, 5, 0);
		layout.setConstraints(balanceAmountPayableLabel, c);
		formPanel.add(balanceAmountPayableLabel);

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(15, 0, 5, 0); // top padding
		layout.setConstraints(l3, c);
		formPanel.add(l3);

		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(15, 15, 5, 0);
		layout.setConstraints(actualPaidAmountText, c);
		formPanel.add(actualPaidAmountText);

		c.gridx = 2;
		c.gridy = 3;
		c.insets = new Insets(15, 15, 5, 0);
		layout.setConstraints(paymentTypeCombo, c);
		formPanel.add(paymentTypeCombo);		

		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				if(state == ItemEvent.SELECTED) {
					PaymentTypeData paymentMode = (PaymentTypeData) itemEvent.getItem();
					if(paymentMode.getCode().equals("1002")) {
						String value = WebOptionPane.showInputDialog(CreateAdhocOrderPaymentDialog.this, "Enter DD / Cheque No");
						chequeNoLabel = new AppWebLabel();
						chequeNoLabel.setText("("+value+")");
						chequeNoLabel.setActualValue(value);
						chequeNoLabel.setFont(applyLabelFont());
						
						c.gridx = 3;
						c.gridy = 3;
						c.insets = new Insets(15, 15, 5, 0);
						layout.setConstraints(chequeNoLabel, c);
						formPanel.add(chequeNoLabel);
						formPanel.revalidate();
					} else {
						if(chequeNoLabel != null) {			
							chequeNoLabel.setText("");
							chequeNoLabel.setActualValue(null);
							chequeNoLabel.setVisible(false);
							formPanel.revalidate();
						}
					}
				}
			}
		};
		paymentTypeCombo.addItemListener(itemListener);
		

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

		addPaymentButton = new WebButton("Add Payment");
		Double value = Double.valueOf((Double) balanceAmountPayableLabel.getActualValue());
		if((value == 0)) {
			addPaymentButton.setEnabled(false);
			paymentTypeCombo.setEnabled(false);
			actualPaidAmountText.setEnabled(false);
			paymentEntryDateText.setEnabled(false);
		}
		final WebButton updatePaymentButton = new WebButton("Confirm");
		updatePaymentButton.setEnabled(false);
		WebButton cancelButton = new WebButton("Cancel");

		addPaymentButton.setFont(applyLabelFont());
		updatePaymentButton.setFont(applyLabelFont());
		cancelButton.setFont(applyLabelFont());

		addPaymentButton.setActionCommand("ADD_PAYMENT");
		updatePaymentButton.setActionCommand("CONFIRM");
		cancelButton.setActionCommand("CANCEL");

		CustomerDAO customerDAO = new DefaultCustomerDAO();

		paymentEntryTable = new WebTable(new AdhocPaymentEntryTableModel(
				customerDAO.findAdhocPaymentByCustomer(data.getOrderDataList().get(0).getCustomer().getCode())));
		paymentEntryTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 12));
		paymentEntryTable.setRowHeight(30);
		paymentEntryTable.setFont(new Font("verdana", Font.PLAIN, 12));

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("ADD_PAYMENT")) {
					if (actualPaidAmountText.getText().isEmpty() || !isNumeric(actualPaidAmountText.getText())) {
						final WebPopOver popOver = new WebPopOver(CreateAdhocOrderPaymentDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Paid Amount is not filled or invalid"));
						popOver.show((WebTextField) actualPaidAmountText);
					} else {
						addPaymentToTableView((Double) balanceAmountPayableLabel.getActualValue());
						addPaymentButton.setEnabled(false);
						updatePaymentButton.setEnabled(true);
					}

				} else if (e.getActionCommand().equalsIgnoreCase("CONFIRM")) {
					addPaymentEntry();
					dispose();
				} else if (e.getActionCommand().equalsIgnoreCase("CANCEL")) {
					dispose();
				}
			}
		};

		addPaymentButton.addActionListener(listener);
		updatePaymentButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
		paymentTypeCombo.addActionListener(listener);

		c.gridx = 2;
		c.gridy = 4;
		c.insets = new Insets(15, 15, 15, 0);
		layout.setConstraints(addPaymentButton, c);
		formPanel.add(addPaymentButton);

		WebPanel buttPanel = new WebPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		buttPanel.add(updatePaymentButton);
		buttPanel.add(cancelButton);

		WebPanel p1 = new WebPanel();
		p1.setLayout(new BorderLayout());
		p1.add(formPanel, BorderLayout.WEST);

		getContentPane().add(p1, BorderLayout.NORTH);
		getContentPane().add(new WebScrollPane(paymentEntryTable), BorderLayout.CENTER);
		getContentPane().add(buttPanel, BorderLayout.SOUTH);
		setSize(1200, 500);
	}

	private void addPaymentEntry() {
		ordersWithPendingPayment = new ArrayList<OrderData>();
		LOG.info("-------------------- Now addPaymentEntry started.....---------------------------");
		OrderDAO orderDAO = new DefaultOrderDAO();
		List<OrderData> currentCustomerOrders = orderDAO
				.findOrdersByCustomer(Integer.valueOf(getCurrentCustomer().getCode()));
		List<PaymentEntryData> partialPaymentsToConsider = null;
		Collections.reverse(currentCustomerOrders);
		
		for (OrderData order : currentCustomerOrders) {
			if (order.getPaymentStatus() == PaymentStatus.PARTIAL) {
				partialPaymentsToConsider = evaluatePaymentForPartialEntries(order);
			} else if (order.getPaymentStatus() == PaymentStatus.PENDING) {
				for (OrderEntryData orderEntry : order.getOrderEntries()) {
					if (orderEntry.getPaymentEntries().isEmpty()) {
						ordersWithPendingPayment.add(order);
					}
				}
				LOG.info("ordersWithPendingPayment :: Size : "+ordersWithPendingPayment.size());
			}
		}
		boolean mergeRequired = false;
		boolean processPartialPaymentsOnly = false;
		boolean processPendingPaymentsOnly = false;
		List<PaymentEntryData> pendingPaymentEntries = evaluateForPendingOrders(ordersWithPendingPayment);
		if (partialPaymentsToConsider != null && !partialPaymentsToConsider.isEmpty()) {
			if (pendingPaymentEntries != null && !pendingPaymentEntries.isEmpty()) {
				mergeRequired = true;
			} else {
				processPartialPaymentsOnly = true;
			}
		} else {
			if (pendingPaymentEntries != null && !pendingPaymentEntries.isEmpty()) {
				processPendingPaymentsOnly = true;
			}
		}

		LOG.info("mergeRequired : " + mergeRequired);
		LOG.info("processPartialPaymentsOnly : " + processPartialPaymentsOnly);
		LOG.info("processPendingPaymentsOnly : " + processPendingPaymentsOnly);

		if (mergeRequired) {			
			List<PaymentEntryData> mergedEntries = merge(partialPaymentsToConsider, pendingPaymentEntries);			
			processPaymentEntries(mergedEntries);
		} else {
			if (processPartialPaymentsOnly) {
				printPaymentEntries(partialPaymentsToConsider);
				processPaymentEntries(partialPaymentsToConsider);
			}
			if (processPendingPaymentsOnly) {
				printPaymentEntries(pendingPaymentEntries);
				processPaymentEntries(pendingPaymentEntries);
			}
		}
		LOG.info("-------------------- Now addPaymentEntry Completed.....---------------------------");
	}

	private void processPaymentEntries(List<PaymentEntryData> paymentEntries) {
		LOG.info("-------------------- Now performing persisting.....---------------------------");
		PaymentService paymentService = new DefaultPaymentService();
		PaymentEntryData customerPaymentEntry = new PaymentEntryData();
		customerPaymentEntry.setAdjusted(Boolean.FALSE);
		LOG.info("balanceAmountPayableLabel getActualValue : "+balanceAmountPayableLabel.getActualValue());
		customerPaymentEntry.setPayableAmount((Double) balanceAmountPayableLabel.getActualValue());
		customerPaymentEntry.setAmount(Double.valueOf(actualPaidAmountText.getText()));
		customerPaymentEntry.setPaymentDate(getCurrentPaymentEntry().getPaymentDate());
		customerPaymentEntry.setPaymentMode(getCurrentPaymentEntry().getPaymentMode());
		customerPaymentEntry.setChequeNumber(getCurrentPaymentEntry().getChequeNumber());
		paymentService.addAdhocPayment(getCurrentCustomer().getCode(), paymentEntries, customerPaymentEntry);
		processPaymentProcessedEvent((SKAMainApp) getOwner());
		LOG.info("-------------------- Now performing persisting...Completed ---------------------------");
	}

	private List<PaymentEntryData> merge(List<PaymentEntryData> partials, List<PaymentEntryData> pending) {
		if (pending == null || pending.isEmpty()) {
			return partials;
		}
		if (partials == null || partials.isEmpty()) {
			return pending;
		}
		for (PaymentEntryData entry : pending) {
			partials.add(entry);

		}
		printPaymentEntries(partials);
		return partials;
	}

	private void printPaymentEntries(List<PaymentEntryData> payments) {
		for (PaymentEntryData data : payments) {
			LOG.info("############################################################");
			LOG.info("Payment Entry No : " + data.getEntryNumber());
			LOG.info("Payment Status : " + data.getPaymentStatus());
			LOG.info("Amount : " + data.getAmount());
			LOG.info("Payable Amount : " + data.getPayableAmount());
			LOG.info("Order No : " + data.getOrderEntryData().getOrder().getCode());
			LOG.info("Order Status : " + data.getOrderEntryData().getOrder().getPaymentStatus());
			LOG.info("############################################################");
		}
	}

	private List<PaymentEntryData> evaluatePaymentForPartialEntries(OrderData order) {
		ArrayList<PaymentEntryData> paymentEntriesToConsider = new ArrayList<PaymentEntryData>();
		if (order.getPaymentStatus() == PaymentStatus.PARTIAL) {
			for (OrderEntryData orderEntry : order.getOrderEntries()) {
				if (!orderEntry.getPaymentEntries().isEmpty() && getCurrentPaymentEntry().getAmount() > 0) {
					// get the most recent entry with Partial first
					PaymentEntryData mostRecentEntry = getLatestPaymentEntryForOrderEntry(orderEntry);
					Double nowPableAmount = mostRecentEntry.getPayableAmount() - mostRecentEntry.getAmount();
					/**
					 * Paying less than or same as this Order balance amount
					 */
					if (nowPableAmount > getCurrentPaymentEntry().getAmount()) {
						PaymentEntryData newPartialPaymentEntry = new PaymentEntryData();
						newPartialPaymentEntry.setPayableAmount(nowPableAmount);
						newPartialPaymentEntry.setAmount(getCurrentPaymentEntry().getAmount());
						newPartialPaymentEntry.setPaymentDate(getCurrentPaymentEntry().getPaymentDate());
						newPartialPaymentEntry.setPaymentStatus(PaymentStatus.PARTIAL);
						newPartialPaymentEntry.setEntryNumber(mostRecentEntry.getEntryNumber() + 1);
						newPartialPaymentEntry.setOrderEntryData(orderEntry);
						orderEntry.getPaymentEntries().add(newPartialPaymentEntry);
						paymentEntriesToConsider.add(newPartialPaymentEntry);
						getCurrentPaymentEntry().setAmount(Double.valueOf(0));
						/**
						 * Paying more than this Order balance amount
						 */
					} else if (nowPableAmount <= getCurrentPaymentEntry().getAmount()) {
						PaymentEntryData newPartialPaymentEntry = new PaymentEntryData();
						newPartialPaymentEntry.setPayableAmount(nowPableAmount);
						newPartialPaymentEntry.setAmount(nowPableAmount);
						newPartialPaymentEntry.setPaymentDate(getCurrentPaymentEntry().getPaymentDate());
						newPartialPaymentEntry.setPaymentStatus(PaymentStatus.PAID);
						newPartialPaymentEntry.setEntryNumber(mostRecentEntry.getEntryNumber() + 1);
						newPartialPaymentEntry.setOrderEntryData(orderEntry);
						orderEntry.getPaymentEntries().add(newPartialPaymentEntry);
						// No more Orders to be processed
						paymentEntriesToConsider.add(newPartialPaymentEntry);
						getCurrentPaymentEntry().setAmount(getCurrentPaymentEntry().getAmount() - nowPableAmount);
						/**
						 * Paying exact Order balance amount
						 */
					}
				} else {
					LOG.info("Skipping Split as current entry has been exhausted...");
				}
			}
		}
		return paymentEntriesToConsider;
	}

	private List<PaymentEntryData> evaluateForPendingOrders(List<OrderData> ordersWithPendingPayments) {
		LOG.info(
				"****************************************** evaluateForPendingOrders ******************************************");
		ArrayList<PaymentEntryData> pendingPaymentEntries = new ArrayList<PaymentEntryData>();
		for (OrderData order : ordersWithPendingPayments) {
			LOG.info("Order No : " + order.getCode());
			LOG.info("Order Amount : " + order.getTotalPrice());
			LOG.info("Customer Entry Amount  : " + getCurrentPaymentEntry().getAmount());	
			
			for (OrderEntryData oe : order.getOrderEntries()) {
				if (getCurrentPaymentEntry().getAmount() > 0) {
					PaymentEntryData ped = new PaymentEntryData();
					if (order.getTotalPrice() > getCurrentPaymentEntry().getAmount()) {
						ped.setPaymentStatus(PaymentStatus.PARTIAL);
						ped.setAmount(getCurrentPaymentEntry().getAmount());
						ped.setPayableAmount(order.getTotalPrice());
						getCurrentPaymentEntry().setAmount(getCurrentPaymentEntry().getAmount() - ped.getAmount());
						LOG.info("New Entry Amount: " + ped.getAmount());
						LOG.info("New Entry Payable Amount: " + ped.getPayableAmount());
						LOG.info("Balance Amount Available : " + getCurrentPaymentEntry().getAmount());
					} else if (order.getTotalPrice() - getCurrentPaymentEntry().getAmount() == 0) {
						ped.setPaymentStatus(PaymentStatus.PAID);
						ped.setAmount(getCurrentPaymentEntry().getAmount());
						ped.setPayableAmount(order.getTotalPrice());
						getCurrentPaymentEntry().setAmount(getCurrentPaymentEntry().getAmount() - ped.getAmount());
						LOG.info("New Entry Amount: " + ped.getAmount());
						LOG.info("New Entry Payable Amount: " + ped.getPayableAmount());
						LOG.info("Balance Amount Available : " + getCurrentPaymentEntry().getAmount());
					} else if (order.getTotalPrice() < getCurrentPaymentEntry().getAmount()) {
						ped.setPaymentStatus(PaymentStatus.PAID);
						ped.setAmount(order.getTotalPrice());
						ped.setPayableAmount(order.getTotalPrice());
						getCurrentPaymentEntry().setAmount(getCurrentPaymentEntry().getAmount() - ped.getAmount());
						LOG.info("New Entry Amount: " + ped.getAmount());
						LOG.info("New Entry Payable Amount: " + ped.getPayableAmount());
						LOG.info("Balance Amount Available : " + getCurrentPaymentEntry().getAmount());
					}

					ped.setEntryNumber(1);
					ped.setPaymentDate(getCurrentPaymentEntry().getPaymentDate());
					ped.setOrderEntryData(oe);
					oe.getPaymentEntries().add(ped);
					pendingPaymentEntries.add(ped);
				}
			}
			LOG.info(
					"****************************************** evaluateForPendingOrders ******************************************");
		}
		return pendingPaymentEntries;
	}

	private PaymentEntryData getLatestPaymentEntryForOrderEntry(OrderEntryData oe) {
		PaymentEntryData lastPaymentEntry = null;
		for (PaymentEntryData pe : oe.getPaymentEntries()) {
			if (lastPaymentEntry == null) {
				lastPaymentEntry = pe;
			} else {
				if (pe.getEntryNumber() > lastPaymentEntry.getEntryNumber()) {
					lastPaymentEntry = pe;
				}
			}
		}
		LOG.info("lastPaymentEntry balance Amount : "
				+ (lastPaymentEntry.getPayableAmount() - lastPaymentEntry.getAmount()));
		return lastPaymentEntry;
	}

	private void addPaymentToTableView(Double payableAmount) {
		AdhocPaymentEntryTableModel model = (AdhocPaymentEntryTableModel) paymentEntryTable.getModel();
		PaymentEntryData data = new PaymentEntryData();
		data.setAmount(Double.valueOf(actualPaidAmountText.getText()));
		data.setPaymentDate(paymentEntryDateText.getDate());
		data.setEntryNumber(model.getPaymentEntryList().size() + 1);
		data.setPayableAmount(Double.valueOf(payableAmount));
		data.setAdjusted(Boolean.FALSE);
		data.setPaymentMode((PaymentTypeData)paymentTypeCombo.getSelectedItem());
		if(chequeNoLabel != null) {			
			data.setChequeNumber((String) chequeNoLabel.getActualValue());
		}
		setCurrentPaymentEntry(data);
		model.getPaymentEntryList().add(data);
		model.fireTableDataChanged();
		validate();		

	}

	protected void processPaymentProcessedEvent(SKAMainApp owner) {
		try {
			dispose();
			showSuccessNotification();
			OrderDAO orderDAO = new DefaultOrderDAO();
			CustomerData lastCustomer = (CustomerData) ApplicationSession.getSession()
					.getProperty("CUSTOMER_SEARCH_KEY");
			OrderTableModel orderTableModel = new OrderTableModel(
					orderDAO.findOrdersByCustomer(Integer.valueOf(lastCustomer.getCode())));
			owner.getOrderContentTable().setModel(orderTableModel);
			owner.updateOrderSummary();
			owner.revalidate();

		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public PaymentEntryData getCurrentPaymentEntry() {
		return currentPaymentEntry;
	}

	public void setCurrentPaymentEntry(PaymentEntryData currentPaymentEntry) {
		this.currentPaymentEntry = currentPaymentEntry;
	}

	public CustomerData getCurrentCustomer() {
		return currentCustomer;
	}

	public void setCurrentCustomer(CustomerData currentCustomer) {
		this.currentCustomer = currentCustomer;
	}
}
