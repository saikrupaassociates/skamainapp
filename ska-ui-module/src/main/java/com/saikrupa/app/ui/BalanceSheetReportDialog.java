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
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.saikrupa.app.service.BalanceSheetService;
import com.saikrupa.app.service.impl.DefaultBalanceSheetService;
import com.saikrupa.app.util.CurrencyUtil;
import com.saikrupa.app.util.DateUtil;

public class BalanceSheetReportDialog extends BaseAppDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalanceSheetReportDialog(SKAMainApp owner, Map<String, String> selectionMap) {
		super(owner, true);
		setTitle("Balance Sheet Report");
		setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
		createBalanceSheetReportDialog(selectionMap);
		setLocationRelativeTo(owner);
		setModal(true);
		
	}

		
	private WebPanel createHeaderPanel(Map<String, String> selectionMap) {
		final WebPanel headerPanel = new WebPanel();
		GridBagLayout headerLayout = new GridBagLayout();
		headerPanel.setLayout(headerLayout);
		GridBagConstraints fc = new GridBagConstraints();
		fc.fill = GridBagConstraints.HORIZONTAL;
		
		WebLabel headerLabel = new WebLabel();
		headerLabel.setText("Summary");		
		Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
		fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		Font font = new Font("verdana",Font.BOLD, 18).deriveFont(fontAttributes);
		headerLabel.setFont(font);
		headerLabel.setForeground(Color.RED);
		
		WebPanel titlePanel = new WebPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.add(headerLabel);
		
		fc.gridx = 0;
		fc.gridy = 0;
		fc.insets = new Insets(0, 20, 10, 0);
		headerLayout.setConstraints(titlePanel, fc);
		headerPanel.add(titlePanel);
		
		WebLabel dateRangeLabel = new WebLabel();
		final String startDateString = DateUtil.convertStringToStandard(selectionMap.get("START_DATE"));
		final String endDateString = DateUtil.convertStringToStandard(selectionMap.get("END_DATE"));
		dateRangeLabel.setText("(Between date "+startDateString+" and "+endDateString+")");
		dateRangeLabel.setFont(new Font("verdana",Font.BOLD|Font.ITALIC, 14));
		dateRangeLabel.setForeground(Color.BLUE);
		
		WebPanel dateRangePanel = new WebPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
		dateRangePanel.add(dateRangeLabel);
		
		fc.gridx = 0;
		fc.gridy = 1;
		fc.insets = new Insets(0, -30, 10, 0);
		headerLayout.setConstraints(dateRangePanel, fc);
		headerPanel.add(dateRangePanel);
		return headerPanel;
		
	}
	
	private WebPanel createCollectionSummaryPanel(Map<String, Double> collectionMap) {
		if(checkMap(collectionMap) != null) {
			return checkMap(collectionMap);
		}
		
		WebPanel panel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		
		int gridx = 0;
		int gridy = 0;
		
		for (Entry<String, Double> entry : collectionMap.entrySet()) {
			WebLabel l1 = new WebLabel(entry.getKey()+ " : ", SwingConstants.RIGHT);
			l1.setFont(applyLabelFont());
			
			final WebLabel orderVolume = new WebLabel(CurrencyUtil.format(entry.getValue()));
			orderVolume.setFont(applyLabelFont());
			
			gc.gridx = gridx;
			gc.gridy = gridy;
			gc.insets = new Insets(0, 0, 10, 0);
			layout.setConstraints(l1, gc);
			panel.add(l1);
			
			gc.gridx = gridx + 1;
			gc.gridy = gridy;
			gc.insets = new Insets(0, 0, 10, 0);
			layout.setConstraints(orderVolume, gc);
			panel.add(orderVolume);
			
			gridy++; 
		}		
		WebPanel basePanel = new WebPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		basePanel.add(panel);
		return basePanel;
	}
	
	private WebPanel checkMap(Map<String, Double> expenseMap) {
		if(expenseMap == null || expenseMap.isEmpty()) {
			WebPanel panel = new WebPanel(new FlowLayout(FlowLayout.LEFT, 10,10));
			
			WebLabel label = new WebLabel("No Entries available");
			label.setFont(applyLabelFont());
			panel.add(label);
			return panel;
		}
		return null;
	}
	
	private WebPanel createExpensePanel(Map<String, Double> expenseMap) {
		if(checkMap(expenseMap) != null) {
			return checkMap(expenseMap);
		}
		WebPanel panel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		
		int gridx = 0;
		int gridy = 0;
		
		for (Entry<String, Double> entry : expenseMap.entrySet()) {
			WebLabel l1 = new WebLabel(entry.getKey()+ " : ", SwingConstants.RIGHT);
			l1.setFont(applyLabelFont());
			
			final WebLabel orderVolume = new WebLabel(CurrencyUtil.format(entry.getValue()));
			orderVolume.setFont(applyLabelFont());
			
			gc.gridx = gridx;
			gc.gridy = gridy;
			gc.insets = new Insets(0, 0, 10, 0);
			layout.setConstraints(l1, gc);
			panel.add(l1);
			
			gc.gridx = gridx + 1;
			gc.gridy = gridy;
			gc.insets = new Insets(0, 0, 10, 0);
			layout.setConstraints(orderVolume, gc);
			panel.add(orderVolume);
			
			gridy++; 
		}		
		WebPanel basePanel = new WebPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		basePanel.add(panel);
		return basePanel;
	}
	
	private WebPanel createDeliveryVehiclePanel(Map<String, Double> map) {
		if(checkMap(map) != null) {
			return checkMap(map);
		}
		WebPanel panel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		
		int gridx = 0;
		int gridy = 0;
		
		for (Entry<String, Double> entry : map.entrySet()) {
			WebLabel l1 = new WebLabel(entry.getKey()+ " : ", SwingConstants.RIGHT);
			l1.setFont(applyLabelFont());
			
			final WebLabel label = new WebLabel(CurrencyUtil.format(entry.getValue()));
			label.setFont(applyLabelFont());
			
			gc.gridx = gridx;
			gc.gridy = gridy;
			gc.insets = new Insets(0, 0, 10, 0);
			layout.setConstraints(l1, gc);
			panel.add(l1);
			
			gc.gridx = gridx + 1;
			gc.gridy = gridy;
			gc.insets = new Insets(0, 0, 10, 0);
			layout.setConstraints(label, gc);
			panel.add(label);
			
			gridy++; 
		}		
		WebPanel basePanel = new WebPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		basePanel.add(panel);
		return basePanel;
	}
	
	private WebPanel createProfitSummaryPanel(Map<String, Double> summaryMap) {
		if(checkMap(summaryMap) != null) {
			return checkMap(summaryMap);
		}
		
		WebPanel panel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		
		WebLabel l11 = new WebLabel("Total Sales Order Price : ", SwingConstants.RIGHT);
		final WebLabel l12 = new WebLabel(CurrencyUtil.format(summaryMap.get("TOTAL_SALES_PRICE")));
		
		
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l11, gc);
		panel.add(l11);
		
		gc.gridx = 1;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l12, gc);
		panel.add(l12);
		
		WebLabel l21 = new WebLabel("Total Expense : ", SwingConstants.RIGHT);
		final WebLabel l22 = new WebLabel(CurrencyUtil.format(summaryMap.get("TOTAL_EXPENSE")));
		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l21, gc);
		panel.add(l21);
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l22, gc);
		panel.add(l22);
		
		WebLabel l31 = new WebLabel("Profit Margin : ", SwingConstants.RIGHT);
		final WebLabel l32 = new WebLabel(CurrencyUtil.format(summaryMap.get("PROFIT_MARGIN")));
		
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l31, gc);
		panel.add(l31);
		
		gc.gridx = 1;
		gc.gridy = 2;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l32, gc);
		panel.add(l32);
		
		WebLabel l41 = new WebLabel("Profit Percent : ", SwingConstants.RIGHT);
		final WebLabel l42 = new WebLabel(String.valueOf(new DecimalFormat("#.##").format(summaryMap.get("PROFIT_PERCENT"))));
		
		
		gc.gridx = 0;
		gc.gridy = 3;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l41, gc);
		panel.add(l41);
		
		gc.gridx = 1;
		gc.gridy = 3;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l42, gc);
		panel.add(l42);
		
		l11.setFont(applyLabelFont());
		l12.setFont(applyLabelFont());
		
		l21.setFont(applyLabelFont());
		l22.setFont(applyLabelFont());
		
		l31.setFont(applyLabelFont());
		l32.setFont(applyLabelFont());
		
		l41.setFont(applyLabelFont());
		l42.setFont(applyLabelFont());
		
		
		WebPanel basePanel = new WebPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		basePanel.add(panel);
		return basePanel;
	}
	
	private WebPanel createOrderDeliveredPanel(Map<String, Double> salesOrderMap) {
		if(checkMap(salesOrderMap) != null) {
			return checkMap(salesOrderMap);
		}
		WebPanel panel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		
		WebLabel l11 = new WebLabel("Total Order Count : ", SwingConstants.RIGHT);
		l11.setFont(applyLabelFont());
		final WebLabel orderVolume = new WebLabel(String.valueOf(salesOrderMap.get("OS_ORDER_COUNT")));
		orderVolume.setFont(applyLabelFont());
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l11, gc);
		panel.add(l11);
		
		gc.gridx = 1;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(orderVolume, gc);
		panel.add(orderVolume);
		
		WebLabel l12 = new WebLabel("Total Bricks Delivered : ", SwingConstants.RIGHT);
		final WebLabel totalSumBricks = new WebLabel(String.valueOf(salesOrderMap.get("OS_ORDER_SUM")));
		l12.setFont(applyLabelFont());
		totalSumBricks.setFont(applyLabelFont());
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l12, gc);
		panel.add(l12);
		
		gc.gridx = 1;
		gc.gridy = 2;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(totalSumBricks, gc);
		panel.add(totalSumBricks);
		
		
		
		
		WebLabel l13 = new WebLabel("Total Order (9 Inch) : ", SwingConstants.RIGHT);
		final WebLabel order9Inch = new WebLabel(String.valueOf(salesOrderMap.get("OS_ORDER_COUNT_9")));
		l13.setFont(applyLabelFont());
		order9Inch.setFont(applyLabelFont());
		
		gc.gridx = 0;
		gc.gridy = 3;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l13, gc);
		panel.add(l13);
		
		gc.gridx = 1;
		gc.gridy = 3;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(order9Inch, gc);
		panel.add(order9Inch);
		
		WebLabel l14 = new WebLabel("Total Bricks (9 Inch) : ", SwingConstants.RIGHT);
		final WebLabel sum9Inch = new WebLabel(String.valueOf(salesOrderMap.get("OS_ORDER_SUM_9")));
		l14.setFont(applyLabelFont());
		sum9Inch.setFont(applyLabelFont());
		
		gc.gridx = 0;
		gc.gridy = 4;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l14, gc);
		panel.add(l14);
		
		gc.gridx = 1;
		gc.gridy = 4;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(sum9Inch, gc);
		panel.add(sum9Inch);
		
		
		
		
		WebLabel l15 = new WebLabel("Total Order (10 Inch) : ", SwingConstants.RIGHT);
		final WebLabel order10Inch = new WebLabel(String.valueOf(salesOrderMap.get("OS_ORDER_COUNT_10")));
		l15.setFont(applyLabelFont());
		order10Inch.setFont(applyLabelFont());
		
		gc.gridx = 0;
		gc.gridy = 5;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l15, gc);
		panel.add(l15);
		
		gc.gridx = 1;
		gc.gridy = 5;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(order10Inch, gc);
		panel.add(order10Inch);
		
		WebLabel l16 = new WebLabel("Total Bricks (10 Inch) : ", SwingConstants.RIGHT);
		final WebLabel sum10Inch = new WebLabel(String.valueOf(salesOrderMap.get("OS_ORDER_SUM_10")));
		l16.setFont(applyLabelFont());
		sum10Inch.setFont(applyLabelFont());
		
		gc.gridx = 0;
		gc.gridy = 6;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l16, gc);
		panel.add(l16);
		
		gc.gridx = 1;
		gc.gridy = 6;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(sum10Inch, gc);
		panel.add(sum10Inch);
		
		
		WebLabel l17 = new WebLabel("Total Order (Block) : ", SwingConstants.RIGHT);
		final WebLabel orderBlock = new WebLabel(String.valueOf(salesOrderMap.get("OS_ORDER_COUNT_BLOCK")));
		l17.setFont(applyLabelFont());
		orderBlock.setFont(applyLabelFont());
		
		gc.gridx = 0;
		gc.gridy = 7;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l17, gc);
		panel.add(l17);
		
		gc.gridx = 1;
		gc.gridy = 7;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(orderBlock, gc);
		panel.add(orderBlock);
		
		WebLabel l18 = new WebLabel("Total Bricks (Block) : ", SwingConstants.RIGHT);
		final WebLabel sumBlock = new WebLabel(String.valueOf(salesOrderMap.get("OS_ORDER_SUM_BLOCK")));
		l18.setFont(applyLabelFont());
		sumBlock.setFont(applyLabelFont());
		
		gc.gridx = 0;
		gc.gridy = 8;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(l18, gc);
		panel.add(l18);
		
		gc.gridx = 1;
		gc.gridy = 8;
		gc.insets = new Insets(0, 0, 10, 0);
		layout.setConstraints(sumBlock, gc);
		panel.add(sumBlock);
		
		WebPanel basePanel = new WebPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		basePanel.add(panel);
		return basePanel;
	}
	
	private void createBalanceSheetReportDialog(Map<String, String> selectionMap) {
		BalanceSheetService service = new DefaultBalanceSheetService();
		final String fromDate = selectionMap.get("START_DATE");
		final String toDate = selectionMap.get("END_DATE");
		
		WebPanel salesOrderPanel = createOrderDeliveredPanel(service.getOrderSummary(fromDate, toDate));
		salesOrderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), " Order Summary "));
		
		WebPanel expensePanel = createExpensePanel(service.getExpenseSummary(fromDate, toDate));
		expensePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), " Expense Summary "));
		
		WebPanel summaryPanel = createProfitSummaryPanel(service.getProfitSummary(fromDate, toDate));
		summaryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), " Profit Margin Summary "));
		
		WebPanel vehicleSummaryPanel = createDeliveryVehiclePanel(service.getDeliveryVehicleSummary(fromDate, toDate));
		vehicleSummaryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), " Delivery Summary "));
		
		WebPanel collectionSummaryPanel = createCollectionSummaryPanel(service.getCollectionSummary(fromDate, toDate));
		collectionSummaryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), " Collection Summary "));
		
		WebPanel body1Panel = new WebPanel();
		body1Panel.setLayout(new BoxLayout(body1Panel, BoxLayout.Y_AXIS));
		body1Panel.add(salesOrderPanel);
		body1Panel.add(Box.createVerticalGlue());
		body1Panel.add(summaryPanel);
		
		WebPanel body2Panel = new WebPanel();
		body2Panel.setLayout(new BoxLayout(body2Panel, BoxLayout.Y_AXIS));
		body2Panel.add(vehicleSummaryPanel);
		body2Panel.add(expensePanel);
		body2Panel.add(Box.createVerticalGlue());
		body2Panel.add(collectionSummaryPanel);
		
		
		WebPanel bodyPanel = new WebPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		bodyPanel.add(body1Panel);
		bodyPanel.add(body2Panel);
		WebPanel headerPanel = createHeaderPanel(selectionMap);
		
		WebPanel buttonPanel = new WebPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10), true);
		WebButton printButton = new WebButton();
		printButton.setText("Print Summary");
		printButton.setFont(applyLabelFont());
		printButton.setActionCommand("PRINT");
		
		WebButton closeButton = new WebButton();
		closeButton.setText("Close");
		closeButton.setFont(applyLabelFont());
		closeButton.setActionCommand("CLOSE");
		
		ActionListener l = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equalsIgnoreCase("PRINT")) {
					printSummary();
				} else if(e.getActionCommand().equalsIgnoreCase("CLOSE")) {
					dispose();
				}
			}
		};
		printButton.addActionListener(l);
		closeButton.addActionListener(l);
		
		buttonPanel.add(printButton);
		buttonPanel.add(closeButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder());
		
		
		getContentPane().add(headerPanel, BorderLayout.NORTH);
		getContentPane().add(bodyPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
	}
	
	private void printSummary() {
		// TODO Auto-generated method stub
		
	}
}
