package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.saikrupa.app.dto.VendorData;
import com.saikrupa.app.service.VendorService;
import com.saikrupa.app.service.impl.DefaultVendorService;
import com.saikrupa.app.ui.component.AppWebLabel;
import com.saikrupa.app.ui.models.ContactPersonListCellRenderer;
import com.saikrupa.app.ui.models.ContactPersonListModel;
import com.saikrupa.app.ui.models.VendorTableModel;

public class UpdateVendorDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WebList contactPersonsList;

	public UpdateVendorDialog(SKAMainApp owner, VendorData vendorData) {
		super(owner, true);
		setTitle("Update Vendor - "+vendorData.getName());
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI(owner, vendorData);
		setLocationRelativeTo(owner);
		setResizable(false);
	}

	@SuppressWarnings("unchecked")
	private void buildGUI(final SKAMainApp owner, final VendorData dataToUpdate) {
		WebPanel formPanel = new WebPanel();
		formPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Vendor Name : ", SwingConstants.RIGHT);
		l1.setFont(applyLabelFont());
		final WebTextField vendorNameText = new WebTextField(30);
		vendorNameText.setText(dataToUpdate.getName());

		WebLabel l2 = new WebLabel("Primary Contact No : ", SwingConstants.RIGHT);
		l2.setFont(applyLabelFont());
		final WebTextField primaryContact = new WebTextField(15);
		primaryContact.setText(dataToUpdate.getPrimaryContactNo());

		WebLabel l3 = new WebLabel("Contact Person : ", SwingConstants.RIGHT);
		l3.setFont(applyLabelFont());
		
		ContactPersonListModel model = new ContactPersonListModel();
		model.setContactList(dataToUpdate.getContactPersons());
		contactPersonsList = new WebList(model);		
		contactPersonsList.setCellRenderer(new ContactPersonListCellRenderer());
		

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 10, 0); // Left padding
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 0); // Left padding

		layout.setConstraints(vendorNameText, c);
		formPanel.add(vendorNameText);

		c.gridx = 0;
		c.gridy = 1;

		layout.setConstraints(l2, c);
		formPanel.add(l2);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 10, 0); // Left padding

		layout.setConstraints(primaryContact, c);
		formPanel.add(primaryContact);

		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.NORTH;
		layout.setConstraints(l3, c);
		formPanel.add(l3);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 10, 0, 0); // Left padding
		c.gridwidth = 3;
		WebScrollPane areaScroll = new WebScrollPane(contactPersonsList);
		areaScroll.setBorder(primaryContact.getBorder());
		layout.setConstraints(areaScroll, c);
		formPanel.add(areaScroll);

		WebButton addContactPersonButton = new WebButton();
		addContactPersonButton.setBackground(Color.white);
		addContactPersonButton.setIcon(createImageIcon("plus.png"));
		addContactPersonButton.setActionCommand("CREATE_CONTACT");

//		c.gridx = 4;
//		c.gridy = 2;
//		c.insets = new Insets(0, 1, 0, 0); // Left padding
//
//		layout.setConstraints(addContactPersonButton, c);
//		formPanel.add(addContactPersonButton);
		
		WebButton removeContactPersonButton = new WebButton();
		removeContactPersonButton.setBackground(Color.white);
		removeContactPersonButton.setIcon(createImageIcon("minus_n1.jpeg"));
		removeContactPersonButton.setActionCommand("REMOVE_CONTACT");

//		c.gridx = 4;
//		c.gridy = 3;
//		c.insets = new Insets(0, 1, 0, 0); // Left padding
//
//		layout.setConstraints(removeContactPersonButton, c);
//		formPanel.add(removeContactPersonButton);
		
		WebPanel addRemovePanel = new WebPanel();		
		GridBagLayout layout1 = new GridBagLayout();
		addRemovePanel.setLayout(layout1);
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		
		c1.gridx = 0;
		c1.gridy = 0;
		c1.insets = new Insets(0, 0, 10, 0); // Left padding
		layout1.setConstraints(addContactPersonButton, c1);
		addRemovePanel.add(addContactPersonButton);
		
		c1.gridx = 0;
		c1.gridy = 1;
		c1.insets = new Insets(30, 0, 0, 0); // Left padding
		layout1.setConstraints(removeContactPersonButton, c1);
		addRemovePanel.add(removeContactPersonButton);
		
		
		
		
		c.gridx = 4;
		c.gridy = 2;
		c.insets = new Insets(0, 1, 0, 0); // Left padding

		layout.setConstraints(addRemovePanel, c);
		formPanel.add(addRemovePanel);

		

		WebButton updateVendorButton = new WebButton("Update Vendor");
		updateVendorButton.setFont(applyLabelFont());
		updateVendorButton.setActionCommand("UPDATE_VENDOR");

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("CREATE_CONTACT")) {
					displayAddContactPersonDialog(UpdateVendorDialog.this);
				} else if (e.getActionCommand().equalsIgnoreCase("REMOVE_CONTACT")) {
					if(contactPersonsList.getSelectedIndex() == -1) {
						final WebPopOver popOver = new WebPopOver(UpdateVendorDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Select the Contact Person to remove"));
						popOver.show(contactPersonsList);
					} else {
						ContactPersonListModel model = (ContactPersonListModel) contactPersonsList.getModel();
						model.removeElementAt(contactPersonsList.getSelectedIndex());						
					}
					
				} else if (e.getActionCommand().equalsIgnoreCase("UPDATE_VENDOR")) {
					if (vendorNameText.getText().isEmpty()) {
						final WebPopOver popOver = new WebPopOver(UpdateVendorDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Missing Vendor Name"));
						popOver.show((WebTextField) vendorNameText);
					} else if (primaryContact.getText().isEmpty()) {
						final WebPopOver popOver = new WebPopOver(UpdateVendorDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Missing Vendor Primary Contact Number"));
						popOver.show((WebTextField) primaryContact);
					}
					ContactPersonListModel listModel = (ContactPersonListModel) contactPersonsList.getModel();
					if (listModel.getContactList().isEmpty()) {
						final WebPopOver popOver = new WebPopOver(UpdateVendorDialog.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Missing Vendor Contact Person"));
						popOver.show((WebList) contactPersonsList);
					} else {						
						dataToUpdate.setName(vendorNameText.getText());
						dataToUpdate.setPrimaryContactNo(primaryContact.getText());
						ContactPersonListModel model = (ContactPersonListModel) contactPersonsList.getModel();
						System.out.println("Now Updating Contact persons  ["+model.getContactList().size()+"] contacts");
						dataToUpdate.setContactPersons(listModel.getContactList());
						if (processUpdateVendorEvent(dataToUpdate)) {
							dispose();							
							VendorTableModel tableModel = (VendorTableModel) owner.getVendorContentTable().getModel();
							VendorService service = new DefaultVendorService();	
							tableModel.setVendorDataList(service.getAllVendors());
							tableModel.fireTableDataChanged();
							showSuccessNotification();
						}
					}
				}
			}
		};
		updateVendorButton.addActionListener(listener);
		addContactPersonButton.addActionListener(listener);
		removeContactPersonButton.addActionListener(listener);		

		
		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setFont(applyLabelFont());
		cancelButton.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		WebPanel bottomPanel = new WebPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(updateVendorButton);
		bottomPanel.add(cancelButton);


		getContentPane().add(new GroupPanel(formPanel), BorderLayout.CENTER);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		pack();
	}

	private void displayAddContactPersonDialog(UpdateVendorDialog createVendorDialog) {
		CreateContactPersonDialog contactPersonDialog = new CreateContactPersonDialog(this);
		contactPersonDialog.setVisible(true);
	}

	private boolean processUpdateVendorEvent(final VendorData vendor) {
		VendorService service = new DefaultVendorService();
		try {
			service.updateVendor(vendor);
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
