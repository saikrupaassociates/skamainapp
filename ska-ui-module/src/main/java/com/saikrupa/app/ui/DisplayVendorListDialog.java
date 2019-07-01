package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.saikrupa.app.dto.VendorData;
import com.saikrupa.app.service.VendorService;
import com.saikrupa.app.service.impl.DefaultVendorService;
import com.saikrupa.app.ui.models.VendorTableModel;

public class DisplayVendorListDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DisplayVendorListDialog(BaseAppDialog owner) {
		super(owner, true);
		setTitle("Select Vendor..."); 
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI(owner);
		setLocationRelativeTo(owner);
		setModal(true);
	}
	


	private void buildGUI(final BaseAppDialog owner) {
		final WebTable vendorContentTable = new WebTable();
		loadVendorData(vendorContentTable);
		vendorContentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(new WebScrollPane(vendorContentTable));

		vendorContentTable.getTableHeader().setFont(applyTableFont());
		vendorContentTable.setRowHeight(35);
		vendorContentTable.setFont(applyTableFont());

		getContentPane().add(new WebScrollPane(vendorContentTable), BorderLayout.CENTER);

		WebPanel buttonPanel = new WebPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		final WebButton useVendorButton = new WebButton("Use this Vendor");
		useVendorButton.setFont(applyLabelFont());
		useVendorButton.setEnabled(false);
		useVendorButton.setActionCommand("USE_VENDOR");

		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setFont(applyLabelFont());
		cancelButton.setActionCommand("CANCEL");
		
		vendorContentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				if(vendorContentTable.getSelectedRow() != -1) {
					useVendorButton.setEnabled(true);
				} 				
			}
		});
		
		vendorContentTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					performSelectionOperation(vendorContentTable, owner);
				}
			}
		});

		ActionListener listener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("USE_VENDOR")) {
					performSelectionOperation(vendorContentTable, owner);
				} 
				dispose();
			}
		};
		useVendorButton.addActionListener(listener);
		cancelButton.addActionListener(listener);

		buttonPanel.add(useVendorButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);		
		setSize(new Dimension(1000, 400));
	}

	private void performSelectionOperation(WebTable vendorContentTable, BaseAppDialog owner) {
		int selection = vendorContentTable.getSelectedRow();
		VendorTableModel model = (VendorTableModel) vendorContentTable.getModel();
		VendorData data = model.getVendorDataList().get(selection);	
		if(owner instanceof ManageExpenseDialog) {
			ManageExpenseDialog d = (ManageExpenseDialog) owner;
			d.getPaidToVendorText().setModel(data);
			d.getPaidToVendorText().setText(data.getName());
		} else if(owner instanceof SearchExpenseDialog) {
			SearchExpenseDialog d = (SearchExpenseDialog) owner;
			d.getPaidToVendorText().setModel(data);
			d.getPaidToVendorText().setText(data.getName());
		} else if(owner instanceof DisplayExpenseReportSelectionDialog) {
			DisplayExpenseReportSelectionDialog d = (DisplayExpenseReportSelectionDialog) owner;
			d.getPaidToText().setModel(data);
			d.getPaidToText().setText(data.getName());
		}

	}
	
	private void loadVendorData(WebTable vendorContentTable) {
		VendorService service = new DefaultVendorService();
		List<VendorData> vendors = service.getAllVendors();		
		try {
			vendorContentTable.setModel(new VendorTableModel(vendors));
		} catch(Exception w) {
			w.printStackTrace();
			
		}
		
	}

}
