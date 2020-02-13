package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.rootpane.WebDialog;

public class DateSelectionDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(DateSelectionDialog.class);
	
	final Integer[] years = { 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030 };
	
	private Map<String, String> selectionMap; 

	public DateSelectionDialog(SKAMainApp owner) {
		super(owner, true);
		setTitle("Select Interval");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);

		buildUI();
		setLocationRelativeTo(owner);
		setModal(true);
	}

	private void buildUI() {
		
		final WebPanel filteredPanel = new WebPanel();
		GridBagLayout filteredLayout = new GridBagLayout();
		filteredPanel.setLayout(filteredLayout);
		GridBagConstraints fc = new GridBagConstraints();
		fc.fill = GridBagConstraints.HORIZONTAL;

		final WebRadioButton annualSelectionRadio = new WebRadioButton("Annual");
		annualSelectionRadio.setActionCommand("BY_YEAR");
		annualSelectionRadio.setFont(applyTableFont());
		
		final WebRadioButton monthlySelectionRadio = new WebRadioButton("Monthly");
		monthlySelectionRadio.setFont(applyTableFont());
		monthlySelectionRadio.setActionCommand("BY_YEAR_MONTH");	
		monthlySelectionRadio.setSelected(true);
		
		
		WebButtonGroup bg = new WebButtonGroup(true);
		bg.setOrientation(SwingConstants.HORIZONTAL);
		bg.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 0));
		
		bg.add(annualSelectionRadio);
		bg.add(monthlySelectionRadio);
		
		WebPanel cbPanel = new WebPanel();
		cbPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		cbPanel.add(bg);
		

		fc.gridx = 0;
		fc.gridy = 0;
		fc.insets = new Insets(0, 20, 10, 0);
		filteredLayout.setConstraints(cbPanel, fc);
		filteredPanel.add(cbPanel);
		
		WebPanel selectionPanel = new WebPanel();
		selectionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));		
		
		final WebLabel yearLabel = new WebLabel();
		yearLabel.setText("Year : ");
		yearLabel.setFont(applyLabelFont());
		selectionPanel.add(yearLabel);
		
		final WebComboBox yearComboBox = new WebComboBox(years);
		selectionPanel.add(yearComboBox);
		
		Calendar currentDate = Calendar.getInstance();
		int year = currentDate.get(Calendar.YEAR);
		yearComboBox.setSelectedItem(Integer.valueOf(year));
		
		fc.gridx = 0;
		fc.gridy = 1;
		fc.insets = new Insets(0, 20, 10, 0);
		filteredLayout.setConstraints(selectionPanel, fc);
		filteredPanel.add(selectionPanel);


		final WebLabel monthLabel = new WebLabel("Month : ");
		monthLabel.setFont(applyLabelFont());
		final WebComboBox monthComboBox = new WebComboBox(Month.values());
		
		selectionPanel.add(monthLabel);
		selectionPanel.add(monthComboBox);
		Month currentMonth = Month.of(currentDate.get(Calendar.MONTH)+1);
		monthComboBox.setSelectedItem(currentMonth);

		final WebButton searchButton = new WebButton("Search");
		searchButton.setActionCommand("SEARCH");
		searchButton.setFont(applyLabelFont());
		fc = new GridBagConstraints();
		fc.gridx = 0;
		fc.gridy = 2;
		fc.ipadx = 4;
		fc.insets = new Insets(0, 20, 10, 0);
		filteredLayout.setConstraints(searchButton, fc);
		filteredPanel.add(searchButton);
		
		ActionListener l = new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if(annualSelectionRadio.isSelected()) {
					monthLabel.setEnabled(false);
					monthComboBox.setEnabled(false);
					
					yearLabel.setEnabled(true);
					yearComboBox.setEnabled(true);
				}
				
				if(monthlySelectionRadio.isSelected()) {
					yearLabel.setEnabled(true);
					yearComboBox.setEnabled(true);
					
					monthLabel.setEnabled(true);
					monthComboBox.setEnabled(true);
				}
				if(e.getActionCommand().equalsIgnoreCase("SEARCH")) {
					if(annualSelectionRadio.isSelected()) {
						Integer year = (Integer) yearComboBox.getSelectedItem();
						Map<String, String> map = getSelectionMap(year, null);	
						map.put("SELECTION_TYPE", "ANNUAL");
						setSelectionMap(map);						
					} else if(monthlySelectionRadio.isSelected()) {
						Integer year = (Integer) yearComboBox.getSelectedItem();
						Month month = (Month) monthComboBox.getSelectedItem();
						Map<String, String> map = getSelectionMap(year, month);						
						map.put("SELECTION_TYPE", "MONTHLY");
						setSelectionMap(map);						
					}
					dispose();
				}
			}
		};
		annualSelectionRadio.addActionListener(l);
		monthlySelectionRadio.addActionListener(l);
		searchButton.addActionListener(l);
		
		getContentPane().add(filteredPanel, BorderLayout.CENTER);
		pack();		
	}
	
	private Map<String, String> getSelectionMap(Integer year, Month month) {
		Map<String, String> map = new HashMap<String, String>();
		if(month == null) {
			String startDate = String.valueOf(year)+"-01-01 00:00:00";
			String endDate = String.valueOf(year)+"-12-31 23:59:59";
			map.put("START_DATE", startDate);
			map.put("END_DATE", endDate);
		} else {
			YearMonth ym = YearMonth.of(year, month.getValue());
			String startDate = ym.atDay(1)+" 00:00:00";
			String endDate = ym.atEndOfMonth()+" 23:59:59";
			map.put("START_DATE", startDate);
			map.put("END_DATE", endDate);
		}
		return map;
	}

	public Map<String, String> getSelectionMap() {
		return selectionMap;
	}

	public void setSelectionMap(Map<String, String> selectionMap) {
		this.selectionMap = selectionMap;
	}
	
}
