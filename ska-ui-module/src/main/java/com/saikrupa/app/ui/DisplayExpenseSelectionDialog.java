package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alee.extended.date.WebDateField;
import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.saikrupa.app.dto.FilterParameter;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.dto.VendorData;
import com.saikrupa.app.ui.component.AppTextField;
import com.saikrupa.app.ui.models.ExpenseTypeListCellRenderer;
import com.saikrupa.app.ui.models.ExpenseTypeModel;

public class DisplayExpenseSelectionDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean actionCancelled;
	private ReportSelectionData selectionData;

	private AppTextField paidToText;
	private WebComboBox expCategoryCombo;

	public DisplayExpenseSelectionDialog(SKAMainApp owner) {
		super(owner, true);
		setTitle("Search Expense");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);

		buildNewUI();
		setLocationRelativeTo(owner);
		setModal(true);
	}

	private void buildNewUI() {
		WebPanel formPanel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

//		final WebRadioButton consolidatedRadio = new WebRadioButton("Consolidated", true);
//		consolidatedRadio.setActionCommand("CONSOLIDATED");
//		consolidatedRadio.setFont(applyLabelFont());
//
//		WebRadioButton filterRadio = new WebRadioButton("Filtered", true);
//		filterRadio.setActionCommand("FILTERED");
//		filterRadio.setFont(applyLabelFont());

//		WebButtonGroup bg = new WebButtonGroup(true);
//		bg.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
//		bg.add(consolidatedRadio);
//		bg.add(filterRadio);
//		bg.setButtonsSelectedForeground(Color.GREEN);
//		consolidatedRadio.setSelected(false);
//		filterRadio.setSelected(false);

//		c.gridx = 0;
//		c.gridy = 0;
//		c.insets = new Insets(0, 0, 20, 20);
//		layout.setConstraints(bg, c);
//		formPanel.add(bg);

		final WebPanel filteredPanel = new WebPanel();
		GridBagLayout filteredLayout = new GridBagLayout();
		filteredPanel.setLayout(filteredLayout);
		GridBagConstraints filteredConstraints = new GridBagConstraints();
		filteredConstraints.fill = GridBagConstraints.HORIZONTAL;

		final WebCheckBox dateCB = new WebCheckBox("Expense Date");
		dateCB.setActionCommand("BY_DATE");
		dateCB.setFont(applyLabelFont());

		final WebCheckBox categoryCB = new WebCheckBox("Category");
		categoryCB.setActionCommand("BY_CAT");
		categoryCB.setFont(applyLabelFont());

		expCategoryCombo = new WebComboBox(new ExpenseTypeModel());
		expCategoryCombo.setRenderer(new ExpenseTypeListCellRenderer());
		
		

		final WebCheckBox payeeCB = new WebCheckBox("Payee");
		payeeCB.setActionCommand("BY_PAYEE");
		payeeCB.setFont(applyLabelFont());
		
		
		paidToText = new AppTextField(15);		
		final WebButton searchButton = new WebButton();
		searchButton.setActionCommand("LOOKUP");
		searchButton.setBackground(Color.white);
		searchButton.setIcon(createImageIcon("search.png"));
		paidToText.setModel(new VendorData());
		paidToText.setTrailingComponent(searchButton);
		paidToText.setEditable(false);		

		
		Calendar fromCal = Calendar.getInstance();
		LocalDate lastMonth = LocalDate.now().minusMonths(1);
		fromCal.set(Calendar.YEAR, lastMonth.getYear());
		fromCal.set(Calendar.MONTH, lastMonth.getMonthValue());
		fromCal.set(Calendar.DAY_OF_MONTH, lastMonth.getDayOfMonth());		
				
		

		final WebLabel tl1 = new WebLabel("From :");
		tl1.setFont(applyLabelFont());
		final WebDateField fromDateField = new WebDateField(fromCal.getTime());

		final WebLabel tl2 = new WebLabel("To :");
		tl2.setFont(applyLabelFont());
		final WebDateField toDateField = new WebDateField(new Date());
		
		
//		expCategoryCombo.setEnabled(false);
//		paidToText.setEnabled(false);
//		searchButton.setEnabled(false);
//		tl1.setEnabled(false);
//		tl2.setEnabled(false);
//		fromDateField.setEnabled(false);
//		toDateField.setEnabled(false);
		
		filteredConstraints.gridx = 0;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 20, 10, 20);
		filteredLayout.setConstraints(dateCB, filteredConstraints);
		filteredPanel.add(dateCB);

		filteredConstraints.gridx = 1;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 0, 10, 0);
		filteredLayout.setConstraints(tl1, filteredConstraints);
		filteredPanel.add(tl1);

		filteredConstraints.gridx = 2;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 0, 10, 10);
		filteredLayout.setConstraints(fromDateField, filteredConstraints);
		filteredPanel.add(fromDateField);

		filteredConstraints.gridx = 3;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 0, 10, 0);
		filteredLayout.setConstraints(tl2, filteredConstraints);
		filteredPanel.add(tl2);

		filteredConstraints.gridx = 4;
		filteredConstraints.gridy = 0;
		filteredConstraints.insets = new Insets(0, 0, 10, 10);
		filteredLayout.setConstraints(toDateField, filteredConstraints);
		filteredPanel.add(toDateField);

		filteredConstraints.gridx = 0;
		filteredConstraints.gridy = 1;
		filteredConstraints.insets = new Insets(0, 20, 10, 20);
		filteredLayout.setConstraints(categoryCB, filteredConstraints);
		filteredPanel.add(categoryCB);

		filteredConstraints.gridx = 1;
		filteredConstraints.gridy = 1;
		filteredConstraints.insets = new Insets(0, 0, 10, 10);
		filteredConstraints.gridwidth = 5;
		filteredLayout.setConstraints(expCategoryCombo, filteredConstraints);
		filteredPanel.add(expCategoryCombo);

		filteredConstraints.gridx = 0;
		filteredConstraints.gridy = 2;
		filteredConstraints.gridwidth = 1;
		filteredConstraints.insets = new Insets(0, 20, 10, 20);
		filteredLayout.setConstraints(payeeCB, filteredConstraints);
		filteredPanel.add(payeeCB);		
		

		filteredConstraints.gridx = 1;
		filteredConstraints.gridy = 2;
		filteredConstraints.insets = new Insets(0, 0, 10, 10);
		filteredConstraints.gridwidth = 5;
		filteredLayout.setConstraints(paidToText, filteredConstraints);
		filteredPanel.add(paidToText);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 20, 20);
		filteredConstraints.gridwidth = 1;
		layout.setConstraints(filteredPanel, c);
		formPanel.add(filteredPanel);

		WebPanel buttonPanel = new WebPanel();
		WebButton okButton = new WebButton("OK");
		okButton.setActionCommand("OK_ACTION");
		okButton.setFont(applyLabelFont());

		WebButton cancelButton = new WebButton("Cancel");
		cancelButton.setActionCommand("CANCEL_ACTION");
		cancelButton.setFont(applyLabelFont());

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 0));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		searchButton.addMouseListener(new MouseListener() {			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent e) {
				if(searchButton.isEnabled()) {
					setCursor(Cursor.getDefaultCursor());
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				if(searchButton.isEnabled()) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				if ("CONSOLIDATED".equalsIgnoreCase(e.getActionCommand())) {
//					dateCB.setEnabled(false);
//					categoryCB.setEnabled(false);
//					payeeCB.setEnabled(false);
//
//					tl1.setEnabled(false);
//					tl2.setEnabled(false);
//
//					fromDateField.setEnabled(false);
//					toDateField.setEnabled(false);
//
//					expCategoryCombo.setEnabled(false);
//					paidToText.setEnabled(false);
//					searchButton.setEnabled(false);
//				} else if ("FILTERED".equalsIgnoreCase(e.getActionCommand())) {
//					dateCB.setEnabled(true);
//					categoryCB.setEnabled(true);
//					payeeCB.setEnabled(true);
//					
//				} else 
					
					if (e.getActionCommand().equalsIgnoreCase("OK_ACTION")) {
					setActionCancelled(false);
					ReportSelectionData selectionData = new ReportSelectionData();
					
					List<FilterParameter> params = new ArrayList<FilterParameter>();
					
					if(dateCB.isSelected()) {
						FilterParameter param = new FilterParameter();
						param.setFilterName("DATE");
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("FROM_DATE", new Timestamp(fromDateField.getDate().getTime()));
						parameters.put("TO_DATE", new Timestamp(toDateField.getDate().getTime()));
						param.setParameters(parameters);
						params.add(param);
					}
					if(categoryCB.isSelected()) {
						FilterParameter param = new FilterParameter();
						param.setFilterName("CATEGORY");
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("CATEGORY", expCategoryCombo.getSelectedItem());
						param.setParameters(parameters);
						params.add(param);
						
					}
					if(payeeCB.isSelected()) {
						FilterParameter param = new FilterParameter();
						param.setFilterName("PAYEE");
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("PAYEE", paidToText.getModel());
						param.setParameters(parameters);
						params.add(param);
					}
					if(!params.isEmpty()) {
						selectionData.setFilters(params);
					}	
					setSelectionData(selectionData);
					dispose();
				} else if (e.getActionCommand().equalsIgnoreCase("CANCEL_ACTION")) {
					setActionCancelled(true);
					setSelectionData(null);
					dispose();
				} else if (e.getActionCommand().equalsIgnoreCase("LOOKUP")) {
					DisplayVendorListDialog d = new DisplayVendorListDialog(DisplayExpenseSelectionDialog.this);
					d.setVisible(true);
				} else if (e.getActionCommand().equalsIgnoreCase("BY_DATE")) {
					tl1.setEnabled(true);
					tl2.setEnabled(true);
					fromDateField.setEnabled(true);
					toDateField.setEnabled(true);					
				} else if (e.getActionCommand().equalsIgnoreCase("BY_CAT")) {
					expCategoryCombo.setEnabled(true);
				} else if (e.getActionCommand().equalsIgnoreCase("BY_PAYEE")) { 
					paidToText.setEnabled(true);
					searchButton.setEnabled(true);
				}
			}
		};

//		consolidatedRadio.addActionListener(l);
//		filterRadio.addActionListener(l);
		dateCB.addActionListener(l);
		categoryCB.addActionListener(l);
		payeeCB.addActionListener(l);
		okButton.addActionListener(l);
		cancelButton.addActionListener(l);
//		filterRadio.setSelected(true);
		searchButton.addActionListener(l);

		getContentPane().add(formPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
	}	

	public boolean isActionCancelled() {
		return actionCancelled;
	}

	public void setActionCancelled(boolean actionCancelled) {
		this.actionCancelled = actionCancelled;
	}

	public ReportSelectionData getSelectionData() {
		return selectionData;
	}

	public void setSelectionData(ReportSelectionData selectionData) {
		this.selectionData = selectionData;
	}

	public AppTextField getPaidToText() {
		return paidToText;
	}

	public void setPaidToText(AppTextField paidToText) {
		this.paidToText = paidToText;
	}

	public WebComboBox getExpCategoryCombo() {
		return expCategoryCombo;
	}

	public void setExpCategoryCombo(WebComboBox expCategoryCombo) {
		this.expCategoryCombo = expCategoryCombo;
	}

}
