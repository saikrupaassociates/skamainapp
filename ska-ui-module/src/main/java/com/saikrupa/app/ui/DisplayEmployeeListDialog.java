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
import com.saikrupa.app.dao.EmployeeDAO;
import com.saikrupa.app.dao.impl.DefaultEmployeeDAO;
import com.saikrupa.app.dto.EmployeeData;
import com.saikrupa.app.dto.VendorData;
import com.saikrupa.app.ui.models.EmployeeTableModel;

public class DisplayEmployeeListDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DisplayEmployeeListDialog(BaseAppDialog owner) {
		super(owner, true);
		setTitle("Select Employee..."); 
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		buildGUI(owner);
		setLocationRelativeTo(owner);
		setModal(true);
	}
	


	private void buildGUI(final BaseAppDialog owner) {
		final WebTable employeeContentTable = new WebTable();
		loadEmployeeData(employeeContentTable);
		employeeContentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(new WebScrollPane(employeeContentTable));

		employeeContentTable.getTableHeader().setFont(applyTableFont());
		employeeContentTable.setRowHeight(35);
		employeeContentTable.setFont(applyTableFont());

		getContentPane().add(new WebScrollPane(employeeContentTable), BorderLayout.CENTER);

		WebPanel buttonPanel = new WebPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		final WebButton useVendorButton = new WebButton("Use this Employee");
		useVendorButton.setFont(applyLabelFont());
		useVendorButton.setEnabled(false);
		useVendorButton.setActionCommand("USE_EMPLOYEE");

		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setFont(applyLabelFont());
		cancelButton.setActionCommand("CANCEL");
		
		employeeContentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				if(employeeContentTable.getSelectedRow() != -1) {
					useVendorButton.setEnabled(true);
				} 				
			}
		});
		
		employeeContentTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					performSelectionOperation(employeeContentTable, owner);
				}
			}
		});

		ActionListener listener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equalsIgnoreCase("USE_EMPLOYEE")) {
					performSelectionOperation(employeeContentTable, owner);
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

	private void performSelectionOperation(WebTable employeeContentTable, BaseAppDialog owner) {
		int selection = employeeContentTable.getSelectedRow();
		EmployeeTableModel model = (EmployeeTableModel) employeeContentTable.getModel();
		VendorData data = model.getEmployeeDataList().get(selection);	
		if(owner instanceof ManageExpenseDialog) {
			ManageExpenseDialog d = (ManageExpenseDialog) owner;
			d.getPaidToVendorText().setModel(data);
			d.getPaidToVendorText().setText(data.getName());
		} else if(owner instanceof SearchExpenseDialog) {
			SearchExpenseDialog d = (SearchExpenseDialog) owner;
			d.getPaidToVendorText().setModel(data);
			d.getPaidToVendorText().setText(data.getName());
		}

	}
	
	private void loadEmployeeData(WebTable employeeContentTable) {
		EmployeeDAO dao = new DefaultEmployeeDAO();
		List<EmployeeData> employees = dao.getAllEmployees();		
		try {
			employeeContentTable.setModel(new EmployeeTableModel(employees));
		} catch(Exception w) {
			w.printStackTrace();
			
		}
		
	}

}
