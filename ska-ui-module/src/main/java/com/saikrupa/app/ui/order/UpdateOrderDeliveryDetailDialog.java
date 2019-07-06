package com.saikrupa.app.ui.order;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import com.alee.extended.date.WebDateField;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebTextField;
import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.DeliveryData;
import com.saikrupa.app.dto.DeliveryStatus;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.VehicleData;
import com.saikrupa.app.service.VehicleService;
import com.saikrupa.app.service.impl.DefaultVehicleService;
import com.saikrupa.app.ui.BaseAppDialog;
import com.saikrupa.app.ui.SKAMainApp;
import com.saikrupa.app.ui.component.AppWebLabel;
import com.saikrupa.app.ui.models.DeliveryVehicleModel;
import com.saikrupa.app.ui.models.VehicleListCellRenderer;

public class UpdateOrderDeliveryDetailDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WebTextField receiptNoText;
	private WebDateField deliveryDateText;
	private WebComboBox deliveryVehicleCombo;
	private WebTextField actualQuantityText;

	private UpdateOrderDialog parentDialog;

	private OrderEntryData currentOrderEntry;

	private boolean cancelledOperation;

	public UpdateOrderDeliveryDetailDialog(SKAMainApp owner) {
		super(owner);
	}

	public UpdateOrderDeliveryDetailDialog(UpdateOrderDialog dialog, OrderEntryData data) {
		super(dialog);
		setParentDialog(dialog);
		setCurrentOrderEntry(data);
		setTitle("Update Order Delivery");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI_Update(dialog, data);
		setLocationRelativeTo(dialog);
		setResizable(false);
	}

	private void buildGUI_Update(UpdateOrderDialog dialog, final OrderEntryData data) {
		WebPanel formPanel = new WebPanel();
		formPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Order Number : ", SwingConstants.RIGHT);
		l1.setFont(applyLabelFont());
		WebLabel t1 = new WebLabel(data.getOrder().getCode());

		WebLabel l2 = new WebLabel("Product : ", SwingConstants.RIGHT);
		l2.setFont(applyLabelFont());
		WebLabel t2 = new WebLabel(data.getProduct().getName());

		WebLabel l3 = new WebLabel("Ordered Quantity : ", SwingConstants.RIGHT);
		l3.setFont(applyLabelFont());
		WebLabel t3 = new WebLabel(String.valueOf(data.getOrderedQuantity()));

		WebLabel l31 = new WebLabel("Delivered Quantity : ", SwingConstants.RIGHT);
		l31.setFont(applyLabelFont());
		actualQuantityText = new WebTextField(20);
		actualQuantityText.setText(String.valueOf(data.getOrderedQuantity()));

		WebLabel l4 = new WebLabel("Delivery Date : ", SwingConstants.RIGHT);
		l4.setFont(applyLabelFont());
		deliveryDateText = new WebDateField(data.getOrder().getCreatedDate());
		if (data.getDeliveryData() != null) {
			deliveryDateText.setDate(data.getDeliveryData().getDeliveryDate());
		}

		WebLabel l41 = new WebLabel("Delivery Challan No : ", SwingConstants.RIGHT);
		l41.setFont(applyLabelFont());
		receiptNoText = new WebTextField(20);
		receiptNoText.setText(data.getDeliveryData() != null ? data.getDeliveryData().getDeliveryReceiptNo() : "");

		WebLabel l5 = new WebLabel("Delivery Vehicle No : ", SwingConstants.RIGHT);
		l5.setFont(applyLabelFont());
		deliveryVehicleCombo = new WebComboBox(new DeliveryVehicleModel());
		deliveryVehicleCombo.setRenderer(new VehicleListCellRenderer());

		if (data.getDeliveryData() != null) {
			DeliveryVehicleModel model = (DeliveryVehicleModel) deliveryVehicleCombo.getModel();
			for (int i = 0; i < model.getSize(); i++) {
				VehicleData vehicleData = model.getElementAt(i);
				if (vehicleData.getNumber().equalsIgnoreCase(data.getDeliveryData().getDeliveryVehicleNo())) {
					deliveryVehicleCombo.setSelectedIndex(i);
					break;
				}
			}
		}

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
		layout.setConstraints(actualQuantityText, c);
		formPanel.add(actualQuantityText);

		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(15, 0, 0, 0); // top padding
		layout.setConstraints(l4, c);
		formPanel.add(l4);

		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(15, 15, 0, 0);
		layout.setConstraints(deliveryDateText, c);
		formPanel.add(deliveryDateText);

		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(15, 0, 0, 0); // top padding
		layout.setConstraints(l41, c);
		formPanel.add(l41);

		c.gridx = 1;
		c.gridy = 5;
		c.insets = new Insets(15, 15, 0, 0);
		layout.setConstraints(receiptNoText, c);
		formPanel.add(receiptNoText);

		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(15, 0, 0, 0); // top padding
		layout.setConstraints(l5, c);
		formPanel.add(l5);

		c.gridx = 1;
		c.gridy = 6;
		c.insets = new Insets(15, 15, 0, 0);
		layout.setConstraints(deliveryVehicleCombo, c);
		formPanel.add(deliveryVehicleCombo);

		WebButton updateDeliveryDataButton = new WebButton("Update");
		updateDeliveryDataButton.setFont(applyLabelFont());
		updateDeliveryDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (receiptNoText.getText().trim().isEmpty()) {
					final WebPopOver popOver = new WebPopOver(UpdateOrderDeliveryDetailDialog.this);
					popOver.setCloseOnFocusLoss(true);
					popOver.setMargin(10);
					popOver.setLayout(new VerticalFlowLayout());
					popOver.add(new AppWebLabel("Delivery Challan Number is missing"));
					popOver.show((WebTextField) receiptNoText);
				} else {
					VehicleData vehicle = (VehicleData) deliveryVehicleCombo.getSelectedItem();
					if (data.getTransportationCost() == 0) {
						if (!vehicle.getNumber().equalsIgnoreCase("EXTERNAL")) {
							int confirmed = WebOptionPane.showConfirmDialog(UpdateOrderDeliveryDetailDialog.this,
									"Customer has not been charged for Order Delivery.\nHowever, Factory owned Vehicle '"+vehicle.getNumber()+"' has been selected.\n\nDo you wish to continue?",
									"Confirm", WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE);
							if (confirmed == WebOptionPane.YES_OPTION) {
								updateDeliveryData(data);
							}
						} else {
							updateDeliveryData(data);
						}
					} else {
						updateDeliveryData(data);
					}
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				getParentDialog().getCurrentOrder().setDeliveryStatus(DeliveryStatus.SHIPPING);
				getParentDialog().resetDeliveryStatus();
			}
		});
		updateDeliveryDataButton.setFont(applyLabelFont());

		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCancelledOperation(true);
				dispose();
			}
		});

		cancelButton.setFont(applyLabelFont());
		WebPanel buttPanel = new WebPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		buttPanel.add(updateDeliveryDataButton);
		buttPanel.add(cancelButton);

		c.gridx = 1;
		c.gridy = 7;
		c.insets = new Insets(15, 15, 0, 0);
		c.gridwidth = 1;
		layout.setConstraints(buttPanel, c);
		formPanel.add(buttPanel);

		getContentPane().add(new GroupPanel(formPanel), BorderLayout.CENTER);
		getContentPane().add(new GroupPanel(new WebPanel()), BorderLayout.EAST);
		getContentPane().add(new GroupPanel(new WebPanel()), BorderLayout.WEST);
		pack();
	}

	protected void updateDeliveryData(OrderEntryData data) {
		DeliveryData delivery = new DeliveryData();
		delivery.setOrderEntryData(data);
		delivery.setDeliveryReceiptNo(receiptNoText.getText());
		VehicleData vehicle = (VehicleData) deliveryVehicleCombo.getSelectedItem();
		delivery.setDeliveryVehicleNo(vehicle.getNumber());
		delivery.setDeliveryDate(deliveryDateText.getDate());
		if (actualQuantityText.getText() == null || actualQuantityText.getText().trim().length() < 1) {
			delivery.setActualDeliveryQuantity(getCurrentOrderEntry().getOrderedQuantity());
		} else {
			delivery.setActualDeliveryQuantity(Double.valueOf(actualQuantityText.getText()));
		}

		data.setDeliveryData(delivery);
		getParentDialog().setCurrentOrder(data.getOrder());
		getParentDialog().notifyParent();
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

	public boolean isCancelledOperation() {
		return cancelledOperation;
	}

	public void setCancelledOperation(boolean cancelledOperation) {
		this.cancelledOperation = cancelledOperation;
	}
}
