package com.saikrupa.app.ui.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.utils.CollectionUtils;
import com.saikrupa.app.dto.AddressData;
import com.saikrupa.app.dto.CustomerData;
import com.saikrupa.app.service.CustomerService;
import com.saikrupa.app.service.impl.DefaultCustomerService;
import com.saikrupa.app.ui.CreateCustomerDialog;
import com.saikrupa.app.ui.DisplayCustomerListDialog;
import com.saikrupa.app.ui.component.AppTextField;
import com.saikrupa.app.ui.component.AppWebLabel;
import com.saikrupa.app.ui.component.AppWebPanel;

public class CustomerDetailPanel extends AppWebPanel {

	private static final long serialVersionUID = 1L;

	private ManageOrderDialog owner;

	private AppTextField customerNameText;
	private WebTextField contactNoText;

	private WebTextField addressLine1;
	private WebTextField addressLine2;
	private WebTextField landMark;
	private WebTextField pinCodeText;

	private WebPanel customerDetailPanel;
	private WebButton continueButton;
	private boolean deliveryAddressModified;
	private WebComboBox villageListCombo;
	private WebButton areaLookupButton;
	private WebButton findCustomerButton;

	public CustomerDetailPanel(ManageOrderDialog owner) {
		this.owner = owner;
		buildUI();
	}

	private void buildUI() {
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
		setLayout(new BorderLayout());
		buildDetailPanel();
		resetCustomerData();
	}

	private void buildDetailPanel() {
		customerDetailPanel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		customerDetailPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Customer Name : ", SwingConstants.RIGHT);
		l1.setFont(applyLabelFont());

		customerNameText = new AppTextField(20);
		findCustomerButton = new WebButton();
		findCustomerButton.setBackground(Color.white);
		findCustomerButton.setIcon(createImageIcon("search.png"));
		findCustomerButton.setActionCommand("SEARCH_CUSTOMER");
		customerNameText.setTrailingComponent(findCustomerButton);
		findCustomerButton.setToolTipText("Find Customer");
		customerNameText.setEditable(false);

		findCustomerButton.addMouseListener(new MouseListener() {			
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {				
				if (findCustomerButton.isEnabled()) {
					setCursor(Cursor.getDefaultCursor());
				}
			}
			public void mouseEntered(MouseEvent e) {				
				if (findCustomerButton.isEnabled()) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
			}
			public void mouseClicked(MouseEvent e) {
			}
		});

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l1, c);
		customerDetailPanel.add(l1);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(customerNameText, c);
		customerDetailPanel.add(customerNameText);

		WebButton newCustomerButton = new WebButton();
		newCustomerButton.setText("Add New Customer");
		newCustomerButton.setFont(applyLabelFont());
		newCustomerButton.setActionCommand("CREATE_NEW_CUSTOMER");

		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(newCustomerButton, c);
		customerDetailPanel.add(newCustomerButton);

		WebLabel l2 = new WebLabel("Contact No : ", SwingConstants.RIGHT);
		l2.setFont(applyLabelFont());
		contactNoText = new WebTextField(20);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l2, c);
		customerDetailPanel.add(l2);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 10, 0);

		layout.setConstraints(contactNoText, c);
		customerDetailPanel.add(contactNoText);

		WebLabel addressHeader = new WebLabel("Delivery Location ", SwingConstants.RIGHT);
		addressHeader.setFont(applyLabelFont());
		addressHeader.setForeground(Color.BLUE);

		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(20, 10, 0, 0);
		layout.setConstraints(addressHeader, c);
		customerDetailPanel.add(addressHeader);

		WebLabel line1 = new WebLabel("Village / Area Name :", SwingConstants.RIGHT);
		line1.setFont(applyLabelFont());

		l1.setFont(applyLabelFont());
		addressLine1 = new WebTextField(15);

		areaLookupButton = new WebButton();
		areaLookupButton.setBackground(Color.white);
		areaLookupButton.setIcon(createImageIcon("search.png"));
		areaLookupButton.setActionCommand("SEARCH_VILLAGE");
		addressLine1.setTrailingComponent(areaLookupButton);
		addressLine1.setEnabled(false);
		
		areaLookupButton.setToolTipText("Lookup Delivery Location");		
		areaLookupButton.setEnabled(false);
		

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(10, 10, 0, 0);
		layout.setConstraints(line1, c);
		customerDetailPanel.add(line1);

		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(10, 10, 0, 0);
		layout.setConstraints(addressLine1, c);
		customerDetailPanel.add(addressLine1);

		villageListCombo = new WebComboBox();
		villageListCombo.setActionCommand("LOCATION_RESULT");

		c.gridx = 2;
		c.gridy = 3;
		c.insets = new Insets(10, 10, 0, 0);
		layout.setConstraints(villageListCombo, c);
		customerDetailPanel.add(villageListCombo);
		villageListCombo.setVisible(false);

		continueButton = new WebButton("Save and Continue...");
		continueButton.setFont(applyLabelFont());
		continueButton.setActionCommand("SAVE_CONTINUE");

		c.gridx = 2;
		c.gridy = 6;
		c.insets = new Insets(10, 10, 0, 0);
		layout.setConstraints(continueButton, c);
		customerDetailPanel.add(continueButton);

		final WebButton resetButton = new WebButton("Reset Customer");
		c.gridx = 3;
		c.gridy = 6;
		c.insets = new Insets(10, 20, 0, 0);
		layout.setConstraints(resetButton, c);
		customerDetailPanel.add(resetButton);
		resetButton.setActionCommand("RESET_CUSTOMER");
		resetButton.setFont(applyLabelFont());
		resetButton.setEnabled(false);

		add(customerDetailPanel, BorderLayout.WEST);

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("SAVE_CONTINUE")) {
					if (validated()) {
						saveAndContinue();
					}
				} else if (event.getActionCommand().equals("SEARCH_CUSTOMER")) {
					DisplayCustomerListDialog d = new DisplayCustomerListDialog(CustomerDetailPanel.this);
					d.setVisible(true);
					if (!d.isDisposed()) {
						resetButton.setEnabled(true);
						addressLine1.setEnabled(true);
						areaLookupButton.setEnabled(true);
						updateCustomerData(d.performSelectionOperation());
					}
				} else if (event.getActionCommand().equals("RESET_CUSTOMER")) {
					resetCustomerData();
				} else if (event.getActionCommand().equals("CHANGE_ADDRESS")) {
					resetCustomerAddressData(true);
				} else if (event.getActionCommand().equals("CREATE_NEW_CUSTOMER")) {
					CreateCustomerDialog d = new CreateCustomerDialog(CustomerDetailPanel.this);
					d.setVisible(true);
					if (d.isCancelledOperation()) {
						resetCustomerData();
					} else {
						resetButton.setEnabled(true);
						addressLine1.setEnabled(true);
						areaLookupButton.setEnabled(false);
					}
				} else if (event.getActionCommand().equals("SEARCH_VILLAGE")) {
					CustomerData customer = (CustomerData) customerNameText.getModel();
					CustomerService customerService = new DefaultCustomerService();
					List<String> locations = customerService.getDeliveryLocationByCustomer(customer.getCode());
					if (!CollectionUtils.isEmpty(locations)) {
						DefaultComboBoxModel<String> locationModel = new DefaultComboBoxModel<String>();
						for (String loc : locations) {
							locationModel.addElement(loc);
						}
						villageListCombo.setModel(locationModel);
						if (!villageListCombo.isVisible()) {
							villageListCombo.setVisible(true);
						}
					}
				} else if (event.getActionCommand().equals("LOCATION_RESULT")) {
					if (villageListCombo.getSelectedIndex() != -1) {
						addressLine1.setText(villageListCombo.getSelectedItem().toString());
					}
				}
			}
		};

		continueButton.addActionListener(listener);
		findCustomerButton.addActionListener(listener);
		areaLookupButton.addActionListener(listener);
		resetButton.addActionListener(listener);
		newCustomerButton.addActionListener(listener);
		villageListCombo.addActionListener(listener);
		
		areaLookupButton.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
				if (areaLookupButton.isEnabled()) {
					setCursor(Cursor.getDefaultCursor());
				}
			}
			public void mouseEntered(MouseEvent e) {
				if (areaLookupButton.isEnabled()) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
			}
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	private boolean validated() {
		if (customerNameText.getText().isEmpty()) {
			showMessage("Customer Name missing", customerNameText);
		} else if (contactNoText.getText().isEmpty() || contactNoText.getText().trim().length() > 10) {
			showMessage("Customer Contact is missing or Invalid", contactNoText);
		} else if (addressLine1.getText().isEmpty()) {
			showMessage("Delivery Location is missing", addressLine1);
		} else {
			return true;
		}
		return false;
	}

	private void showMessage(String message, Component invoker) {
		final WebPopOver popOver = new WebPopOver(CustomerDetailPanel.this);
		popOver.setCloseOnFocusLoss(true);
		popOver.setMargin(10);
		popOver.setLayout(new VerticalFlowLayout());
		popOver.add(new AppWebLabel(message));
		popOver.show(invoker);
	}

	private void saveAndContinue() {
		CustomerData data = null;
		if (customerNameText.getModel() == null) {
			data = new CustomerData();
			data.setName(customerNameText.getText());
			data.setPrimaryContact(contactNoText.getText());
			AddressData deliveryTo = new AddressData();
			deliveryTo.setLine1(addressLine1.getText());
			data.setAddress(deliveryTo);
			data.setBillingAddress(deliveryTo);
			customerNameText.setModel(data);
		} else {
			data = (CustomerData) customerNameText.getModel();
			AddressData deliveryTo = new AddressData();
			deliveryTo.setLine1(addressLine1.getText());
			data.setAddress(deliveryTo);
			data.setBillingAddress(deliveryTo);
			customerNameText.setModel(data);
		}

		owner.getOrderData().setDeliveryAddress(data.getAddress());
		owner.getOrderData().getOrderEntries().get(0).setDeliveryAddress(data.getAddress());
		owner.getOrderData().setCustomer(data);

		owner.getOrderTabbedPane().setEnabledAt(2, true);
		owner.getOrderTabbedPane().setSelectedIndex(2);
	}

	private Icon createImageIcon(String path) {
		this.getClass().getClassLoader();
		java.net.URL imgURL = ClassLoader.getSystemResource("icons/" + path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file:  --> " + path);
			return null;
		}
	}

	public ManageOrderDialog getOwner() {
		return owner;
	}

	public void setOwner(ManageOrderDialog owner) {
		this.owner = owner;
	}

	public AppTextField getCustomerNameText() {
		return customerNameText;
	}

	public void setCustomerNameText(AppTextField customerNameText) {
		this.customerNameText = customerNameText;
	}

	public void updateCustomerData(CustomerData data) {
		customerNameText.setModel(data);
		customerNameText.setText(data.getName());
		contactNoText.setText(data.getPrimaryContact());

		customerNameText.setEnabled(false);
		contactNoText.setEnabled(false);
		addressLine1.setEnabled(true);
	}

	public void updateNewCustomerData(CustomerData data) {
		customerNameText.setModel(data);
		customerNameText.setText(data.getName());
		contactNoText.setText(data.getPrimaryContact());

		customerNameText.setEnabled(false);
		contactNoText.setEnabled(false);

	}

	private void resetCustomerAddressData(boolean requestFocus) {
		addressLine1.setEnabled(true);
		addressLine2.setEnabled(true);
		landMark.setEnabled(true);
		pinCodeText.setEnabled(true);
		if (requestFocus) {
			addressLine1.requestFocusInWindow();
		}
	}

	private void resetCustomerData() {
		customerNameText.setModel(null);
		customerNameText.setText("");
		contactNoText.setText("");
		addressLine1.setText("");
		customerNameText.setEnabled(true);
		customerNameText.setEditable(false);
		contactNoText.setEnabled(true);
		addressLine1.setEnabled(false);
		areaLookupButton.setEnabled(false);
		villageListCombo.setVisible(false);
	}

	public boolean isDeliveryAddressModified() {
		return deliveryAddressModified;
	}

	public void setDeliveryAddressModified(boolean deliveryAddressModified) {
		this.deliveryAddressModified = deliveryAddressModified;
	}
}
