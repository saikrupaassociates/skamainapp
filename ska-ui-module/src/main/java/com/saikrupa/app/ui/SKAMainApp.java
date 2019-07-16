package com.saikrupa.app.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.time.ClockType;
import com.alee.extended.time.WebClock;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.utils.CollectionUtils;
import com.saikrupa.app.dao.ApplicationUserDAO;
import com.saikrupa.app.dao.EmployeeDAO;
import com.saikrupa.app.dao.ExpenseDAO;
import com.saikrupa.app.dao.InvestorDAO;
import com.saikrupa.app.dao.OrderDAO;
import com.saikrupa.app.dao.OrderDeliveryDAO;
import com.saikrupa.app.dao.ProductDAO;
import com.saikrupa.app.dao.impl.CustomerDAO;
import com.saikrupa.app.dao.impl.DefaultApplicationUserDAO;
import com.saikrupa.app.dao.impl.DefaultCustomerDAO;
import com.saikrupa.app.dao.impl.DefaultEmployeeDAO;
import com.saikrupa.app.dao.impl.DefaultExpenseDAO;
import com.saikrupa.app.dao.impl.DefaultInvestorDAO;
import com.saikrupa.app.dao.impl.DefaultOrderDAO;
import com.saikrupa.app.dao.impl.DefaultOrderDeliveryDAO;
import com.saikrupa.app.dao.impl.DefaultProductDAO;
import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.ApplicationUserData;
import com.saikrupa.app.dto.CustomerData;
import com.saikrupa.app.dto.DeliveryStatus;
import com.saikrupa.app.dto.EmployeeData;
import com.saikrupa.app.dto.ExpenseData;
import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.dto.InvestorData;
import com.saikrupa.app.dto.OrderData;
import com.saikrupa.app.dto.OrderEntryData;
import com.saikrupa.app.dto.PaymentEntryData;
import com.saikrupa.app.dto.PaymentStatus;
import com.saikrupa.app.dto.ProductData;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.dto.VendorData;
import com.saikrupa.app.service.VendorService;
import com.saikrupa.app.service.impl.DefaultVendorService;
import com.saikrupa.app.service.report.ReportService;
import com.saikrupa.app.service.report.impl.ConsolidatedExpenseReportService;
import com.saikrupa.app.service.report.impl.ConsolidatedOrderReportService;
import com.saikrupa.app.service.report.impl.CustomerOrderReportService;
import com.saikrupa.app.service.report.impl.DeliveredPendingPaymentOrderReportService;
import com.saikrupa.app.service.report.impl.FilteredExpenseReportService;
import com.saikrupa.app.service.report.impl.FilteredInventoryReportService;
import com.saikrupa.app.service.report.impl.OrderByCustomerGroupReportService;
import com.saikrupa.app.service.report.impl.PendingDeliveryOrderReportService;
import com.saikrupa.app.service.report.impl.PendingPaymentOrderReportService;
import com.saikrupa.app.session.ApplicationSession;
import com.saikrupa.app.ui.component.AppWebLabel;
import com.saikrupa.app.ui.models.EmployeeTableModel;
import com.saikrupa.app.ui.models.ExpenseTableModel;
import com.saikrupa.app.ui.models.InventoryHistoryModel;
import com.saikrupa.app.ui.models.InvestorTableModel;
import com.saikrupa.app.ui.models.OrderDeliveryTableModel;
import com.saikrupa.app.ui.models.OrderTableModel;
import com.saikrupa.app.ui.models.ProductTableModel;
import com.saikrupa.app.ui.models.VendorTableModel;
import com.saikrupa.app.ui.order.ManageOrderDialog;
import com.saikrupa.app.ui.order.UpdateOrderDeliveryDetailDialog;
import com.saikrupa.app.ui.order.UpdateOrderDialog;
import com.saikrupa.app.util.ApplicationResourceBundle;
import com.saikrupa.orderimport.ImportService;

public class SKAMainApp extends WebFrame {

	private static final long serialVersionUID = 1L;

	private WebTable expenseContentTable;
	private WebTable vendorContentTable;
	private WebTable investorContentTable;
	private WebTable orderContentTable;
	private WebTable productContentTable;
	private WebTable employeeContentTable;
	private WebTable orderDeliveryContentTable;
	private WebTable productInventoryHistoryTable;
	private WebButton exportReportButton;

	private WebLabel totalOrderPendingValueLabel;
	private WebLabel totalOrderPaidValueLabel;
	private WebLabel totalOrderValueLabel;
	private WebLabel totalOrderCountLabel;

	private WebMenu profileMenu;
	
	private static Logger LOG = Logger.getLogger(SKAMainApp.class);

	public SKAMainApp() {
		super();
		LOG.info("SKAMainApp - Initializing application...");
		init();
		setIconImage(createImageIcon("appLOGO.jpg").getImage());
	}

	private void init() {
		if (!isDBConnectionOK()) {
			WebOptionPane.showMessageDialog(this, "DB Connection not available");
			System.exit(0);
		}
		final ApplicationResourceBundle bundle = ApplicationResourceBundle.getApplicationResourceBundle();
		setTitle(bundle.getResourceValue("app.main.title"));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitApplication(bundle);
			}
		});

		loadDefaultPage();
	}

	private boolean isDBConnectionOK() {
		boolean connected = true;
		try {
			ExpenseDAO dao = new DefaultExpenseDAO();
			dao.findAllExpenses();
		} catch (Exception e) {
			e.printStackTrace();
			connected = false;
		}
		return connected;
	}

	private void loadDefaultPage() {
		showLogin();
	}

	private void showLogin() {
		final WebPanel loginPanel = new WebPanel(new FlowLayout(FlowLayout.RIGHT));

		WebPanel formPanel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("User ID : ", SwingConstants.RIGHT);
		l1.setFont(getLabelFont());
		final WebTextField userNameText = new WebTextField(15);
		userNameText.setInputPrompt("Employee ID");
		userNameText.setInputPromptFont(userNameText.getFont().deriveFont(Font.ITALIC));

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 10, 0); // Left padding
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 10, 0); // Left padding

		layout.setConstraints(userNameText, c);
		formPanel.add(userNameText);

		WebLabel l2 = new WebLabel("Password : ", SwingConstants.RIGHT);
		l2.setFont(getLabelFont());
		final WebPasswordField passwordText = new WebPasswordField(15);
		passwordText.setInputPrompt("Enter password");
		passwordText.setInputPromptFont(passwordText.getFont().deriveFont(Font.ITALIC));

		// userNameText.setText("1000");
		// passwordText.setText("123");

		final WebButton loginButton = new WebButton();
		loginButton.setIcon(createImageIcon("login-button.png"));
		loginButton.setToolTipText("Login");

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (userNameText.getText().isEmpty()) {
					final WebPopOver popOver = new WebPopOver(SKAMainApp.this);
					popOver.setCloseOnFocusLoss(true);
					popOver.setMargin(10);
					popOver.setLayout(new VerticalFlowLayout());
					popOver.add(new AppWebLabel("User Name is Missing"));
					popOver.show((WebTextField) userNameText);
				} else if (passwordText.getPassword().toString().length() < 1) {
					final WebPopOver popOver = new WebPopOver(SKAMainApp.this);
					popOver.setCloseOnFocusLoss(true);
					popOver.setMargin(10);
					popOver.setLayout(new VerticalFlowLayout());
					popOver.add(new AppWebLabel("Password is Missing"));
					popOver.show((WebPasswordField) passwordText);
				} else if (!userExists(userNameText.getText())) {
					final WebPopOver popOver = new WebPopOver(SKAMainApp.this);
					popOver.setCloseOnFocusLoss(true);
					popOver.setMargin(10);
					popOver.setLayout(new VerticalFlowLayout());
					popOver.add(new AppWebLabel("User Name does not exists"));
					popOver.show((WebTextField) userNameText);
				} else {
					ApplicationUserDAO userDao = new DefaultApplicationUserDAO();
					ApplicationUserData userData = userDao.findUserByCode(userNameText.getText());
					if (!loginValid(String.valueOf(passwordText.getPassword()),
							String.valueOf(userData.getPassword()))) {
						final WebPopOver popOver = new WebPopOver(SKAMainApp.this);
						popOver.setCloseOnFocusLoss(true);
						popOver.setMargin(10);
						popOver.setLayout(new VerticalFlowLayout());
						popOver.add(new AppWebLabel("Invalid Login Credentials."));
						popOver.show((WebButton) loginButton);
					} else {
						ApplicationSession session = ApplicationSession.getSession();
						session.setCurrentUser(userData);
						getContentPane().remove(loginPanel);
						setupMenus();
						decorateSouthPanel();
						revalidate();
					}
				}
			}
		};

		passwordText.addActionListener(listener);
		loginButton.addActionListener(listener);

		c.gridx = 0;
		c.gridy = 1;

		layout.setConstraints(l2, c);
		formPanel.add(l2);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 10, 0); // Left padding

		layout.setConstraints(passwordText, c);
		formPanel.add(passwordText);

		loginButton.setFont(getLabelFont());
		c.gridx = 2;
		c.gridy = 1;
		c.insets = new Insets(0, 10, 10, 0);
		layout.setConstraints(loginButton, c);
		formPanel.add(loginButton);

		ImageIcon icon = createImageIcon("appLOGO.jpg");
		WebDecoratedImage img3 = new WebDecoratedImage(icon);
		img3.setGrayscale(false, false);
		img3.setRound(2);

		loginPanel.add(formPanel);
		getContentPane().add(img3, BorderLayout.CENTER);
		getContentPane().add(loginPanel, BorderLayout.NORTH);
	}

	private boolean userExists(String userName) {
		ApplicationUserDAO userDao = new DefaultApplicationUserDAO();
		ApplicationUserData userData = userDao.findUserByCode(userName);
		return userData != null;
	}

	private boolean loginValid(String value1, String value2) {
		return value1.equals(value2);
	}

	public ImageIcon createImageIcon(String path) {
		this.getClass().getClassLoader();
		java.net.URL imgURL = ClassLoader.getSystemResource("icons/" + path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file:  --> " + path);
			return null;
		}
	}

	public Font getMenuFont() {
		return new Font("Verdana", Font.BOLD, 13);
	}

	public Font getTableFont() {
		return new Font("Verdana", Font.BOLD, 13);
	}

	public Font getLabelFont() {
		return new Font("Verdana", Font.BOLD, 12);
	}

	private void exitApplication(ApplicationResourceBundle bundle) {
		int confirmed = WebOptionPane.showConfirmDialog(SKAMainApp.this, bundle.getResourceValue("app.main.exit.title"),
				"Confirm", WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE);
		if (confirmed == WebOptionPane.YES_OPTION) {
			PersistentManager manager = PersistentManager.getPersistentManager();
			manager.closeConnection();
			System.exit(0);
		} else {
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		}

	}

	private SDMenuBar menubar;

	private void setupMenus() {
		ApplicationUserData loggedOnUser = ApplicationSession.getSession().getCurrentUser();

		menubar = new SDMenuBar();
		WebMenu optionMenu = new WebMenu("Operations");
		optionMenu.setFont(getMenuFont());
		optionMenu.setToolTipText("Available Operations");

		WebMenu manageMenu = new WebMenu("Manage...");
		manageMenu.setFont(getMenuFont());
		manageMenu.setToolTipText("Manage Operations");

		WebMenu importMenu = new WebMenu("Import...");
		importMenu.setFont(getMenuFont());
		importMenu.setToolTipText("Import Orders / Inventory");

		WebLabel tip = new WebLabel("Expense");
		TooltipManager.setTooltip(tip, "Expense", TooltipWay.trailing);
		tip.setMargin(4);

		final WebMenuItem expenseMenuItem = new WebMenuItem("Expense");
		expenseMenuItem.setFont(getMenuFont());
		expenseMenuItem.setToolTipText("manage Expense");

		final WebMenuItem vendorMenuItem = new WebMenuItem("Vendor / Supplier");
		vendorMenuItem.setFont(getMenuFont());
		vendorMenuItem.setToolTipText("manage Vendor or Supplier");

		final WebMenuItem employeeMenuItem = new WebMenuItem("Employee");
		employeeMenuItem.setFont(getMenuFont());
		employeeMenuItem.setToolTipText("manage Employee");

		final WebMenuItem investMenuItem = new WebMenuItem("Investment");
		investMenuItem.setFont(getMenuFont());
		investMenuItem.setToolTipText("manage Investment");

		final WebMenu orderMenuItem = new WebMenu("Order");
		orderMenuItem.setFont(getMenuFont());
		orderMenuItem.setToolTipText("manage Order");
		
		final WebMenuItem orderDeliveryMenuItem = new WebMenuItem("Order Delivery");
		orderDeliveryMenuItem.setFont(getMenuFont());
		orderDeliveryMenuItem.setToolTipText("manage Order Delivery");		
		

		final WebMenuItem productMenuItem = new WebMenuItem("Product");
		productMenuItem.setFont(getMenuFont());
		productMenuItem.setToolTipText("manage Product");

		final WebMenuItem exitMenuItem = new WebMenuItem("Exit...");
		exitMenuItem.setFont(getMenuFont());
		exitMenuItem.setToolTipText("Exit from App...");

		profileMenu = new WebMenu("User Settings");
		profileMenu.setFont(getMenuFont());

		final WebMenuItem changePasswordMenuItem = new WebMenuItem("Change Password...");
		changePasswordMenuItem.setFont(getMenuFont());
		changePasswordMenuItem.setToolTipText("Change Password");
		changePasswordMenuItem.setActionCommand("CHANGE_PASSWORD");
		profileMenu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		profileMenu.add(changePasswordMenuItem);

		final WebMenuItem logOffMenuItem = new WebMenuItem("Log off");
		logOffMenuItem.setFont(getMenuFont());
		logOffMenuItem.setToolTipText("Log Off");
		logOffMenuItem.setActionCommand("LOG_OFF");
		logOffMenuItem.addActionListener(actionListener());
		profileMenu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		profileMenu.add(logOffMenuItem);

		manageMenu.add(vendorMenuItem);
		manageMenu.addSeparator();

		if (loggedOnUser.isAdmin()) {
			manageMenu.add(employeeMenuItem);
			manageMenu.addSeparator();
		}

		if (loggedOnUser.isAdmin()) {
			manageMenu.add(investMenuItem);
			manageMenu.addSeparator();
		}

		manageMenu.add(productMenuItem);
		manageMenu.addSeparator();

		manageMenu.add(expenseMenuItem);
		manageMenu.addSeparator();

		manageMenu.add(orderMenuItem);
		manageMenu.addSeparator();
		manageMenu.add(orderDeliveryMenuItem);
		

		final WebMenuItem importOrderMenuItem = new WebMenuItem("Orders from Excel");
		importOrderMenuItem.setFont(getMenuFont());
		importOrderMenuItem.setToolTipText("Import Orders from Excel file");
		importOrderMenuItem.setActionCommand("IMPORT_ORDERS");

		final WebMenuItem importInventoryMenuItem = new WebMenuItem("Inventories from Excel");
		importInventoryMenuItem.setFont(getMenuFont());
		importInventoryMenuItem.setToolTipText("Import Inventories from Excel file");
		importInventoryMenuItem.setActionCommand("IMPORT_INVENTORIES");

		if (loggedOnUser.isAdmin()) {
			importMenu.add(importOrderMenuItem);
			importMenu.add(importInventoryMenuItem);
		}		

		optionMenu.add(manageMenu);
		optionMenu.addSeparator();
		optionMenu.add(importMenu);
		optionMenu.addSeparator();
		optionMenu.add(exitMenuItem);

		WebMenu reportMenu = new WebMenu("Reports");
		reportMenu.setFont(getMenuFont());
		reportMenu.setToolTipText("Execute Reports");

		final WebMenu expenseReportItem = new WebMenu("Expenses");
		expenseReportItem.setFont(getMenuFont());
		expenseReportItem.setToolTipText("Expense Reports");
		reportMenu.add(expenseReportItem);

		final WebMenuItem expenseConsolidatedItem = new WebMenuItem("Filtered Expense");
		expenseConsolidatedItem.setFont(getMenuFont());
		expenseConsolidatedItem.setToolTipText("Consolidated and Filtered Expense Report");
		expenseReportItem.add(expenseConsolidatedItem);
		expenseConsolidatedItem.setActionCommand("EXPENSE_CONSOLIDATED_FILTERED");
		expenseConsolidatedItem.addActionListener(actionListener());

		final WebMenuItem allOrderMenuItem = new WebMenuItem("All Orders");
		allOrderMenuItem.setFont(getMenuFont());
		allOrderMenuItem.setToolTipText("Display All Orders");

		final WebMenuItem allOrdersByCustomerMenuItem = new WebMenuItem("Consolidated - Orders By Customer");
		allOrdersByCustomerMenuItem.setFont(getMenuFont());
		allOrdersByCustomerMenuItem.setToolTipText("Display All Orders Group by Customer");

		final WebMenuItem customerOrderMenuItem = new WebMenuItem("Orders By Customer");
		customerOrderMenuItem.setFont(getMenuFont());
		customerOrderMenuItem.setToolTipText("Display Orders placed by a specific Customer");

		orderMenuItem.add(allOrderMenuItem);
		orderMenuItem.add(allOrdersByCustomerMenuItem);
		orderMenuItem.add(customerOrderMenuItem);

		final WebMenu orderReportItem = new WebMenu("Orders");
		orderReportItem.setFont(getMenuFont());
		orderReportItem.setToolTipText("Find Orders with specific search criteria");

		final WebMenuItem orderPendingPaymentItem = new WebMenuItem("Pending Payment");
		orderPendingPaymentItem.setFont(getMenuFont());
		orderPendingPaymentItem.setToolTipText("Find Orders with Pending Payment");

		final WebMenuItem orderPendingDeliveryItem = new WebMenuItem("Pending Delivery");
		orderPendingDeliveryItem.setFont(getMenuFont());
		orderPendingDeliveryItem.setToolTipText("Find Orders with Pending Delivery");

		final WebMenuItem orderByCustomerItem = new WebMenuItem("Order By Customer");
		orderByCustomerItem.setFont(getMenuFont());
		orderByCustomerItem.setToolTipText("Find Orders by Customer");

		final WebMenuItem consolidatedOrderItem = new WebMenuItem("Consolidated - All Orders");
		consolidatedOrderItem.setFont(getMenuFont());
		consolidatedOrderItem.setToolTipText("All Orders placed");

		final WebMenuItem deliveredPendingPaymentItem = new WebMenuItem("Delivered - Pending Payment");
		deliveredPendingPaymentItem.setFont(getMenuFont());
		deliveredPendingPaymentItem.setToolTipText("Delivery Completed but payment is pending");

		final WebMenuItem orderDeliveryConflictItem = new WebMenuItem("Delivery Quantity Discrepency");
		orderDeliveryConflictItem.setFont(getMenuFont());
		orderDeliveryConflictItem.setToolTipText("Find Orders with Delivery Quantity Discrepency");

		orderReportItem.add(orderPendingPaymentItem);
		orderReportItem.add(orderPendingDeliveryItem);
		orderReportItem.add(orderByCustomerItem);
		orderReportItem.add(allOrdersByCustomerMenuItem);
		orderReportItem.add(deliveredPendingPaymentItem);

		orderReportItem.add(consolidatedOrderItem);
		orderReportItem.add(orderDeliveryConflictItem);
		reportMenu.add(orderReportItem);

		final WebMenu inventoryReportItem = new WebMenu("Inventory");
		inventoryReportItem.setFont(getMenuFont());
		inventoryReportItem.setToolTipText("Find Inventory with specific search criteria");

		final WebMenuItem inventoryCustomMenuItem = new WebMenuItem("Advanced Search");
		inventoryCustomMenuItem.setFont(getMenuFont());
		inventoryCustomMenuItem.setToolTipText("Search based on Selection");
		inventoryReportItem.add(inventoryCustomMenuItem);
		inventoryCustomMenuItem.setActionCommand("REPORT_INVENTORY");

		reportMenu.add(inventoryReportItem);

		menubar.add(optionMenu);
		if (loggedOnUser.isAdmin()) {
			menubar.add(reportMenu);
		}

		menubar.add(Box.createHorizontalGlue());
		menubar.add(profileMenu);
		setJMenuBar(menubar);

		// Standard Menu Items
		expenseMenuItem.setActionCommand("MANAGE_EXPENSE");
		vendorMenuItem.setActionCommand("MANAGE_VENDOR");
		employeeMenuItem.setActionCommand("MANAGE_EMPLOYEE");
		exitMenuItem.setActionCommand("EXIT_APP");
		investMenuItem.setActionCommand("MANAGE_INVESTMENT");
		allOrderMenuItem.setActionCommand("MANAGE_ORDER_ALL");
		customerOrderMenuItem.setActionCommand("MANAGE_ORDER_CUSTOMER");
		productMenuItem.setActionCommand("MANAGE_PRODUCT");
		orderDeliveryMenuItem.setActionCommand("MANAGE_ORDER_DELIVERY");

		// Order report Menu Items
		orderPendingDeliveryItem.setActionCommand("REPORT_ORDER_PENDING_DELIVERY");
		orderPendingPaymentItem.setActionCommand("REPORT_ORDER_PENDING_PAYMENT");
		orderDeliveryConflictItem.setActionCommand("REPORT_DELIVERY_QUANTITY_MISMATCH");
		orderByCustomerItem.setActionCommand("REPORT_ORDER_BY_CUSTOMER");
		allOrdersByCustomerMenuItem.setActionCommand("REPORT_ORDER_BY_CUSTOMER_GROUP");
		consolidatedOrderItem.setActionCommand("REPORT_ORDER_CONSOLIDATED");
		deliveredPendingPaymentItem.setActionCommand("DELIVERED_PENDING_PAYMENT");

		expenseMenuItem.addActionListener(actionListener());
		exitMenuItem.addActionListener(actionListener());
		vendorMenuItem.addActionListener(actionListener());
		employeeMenuItem.addActionListener(actionListener());
		investMenuItem.addActionListener(actionListener());
		allOrderMenuItem.addActionListener(actionListener());
		orderDeliveryMenuItem.addActionListener(actionListener());
		customerOrderMenuItem.addActionListener(actionListener());
		productMenuItem.addActionListener(actionListener());
		orderReportItem.addActionListener(actionListener());

		orderPendingDeliveryItem.addActionListener(actionListener());
		orderPendingPaymentItem.addActionListener(actionListener());
		orderDeliveryConflictItem.addActionListener(actionListener());
		orderByCustomerItem.addActionListener(actionListener());
		allOrdersByCustomerMenuItem.addActionListener(actionListener());
		consolidatedOrderItem.addActionListener(actionListener());
		deliveredPendingPaymentItem.addActionListener(actionListener());
		changePasswordMenuItem.addActionListener(actionListener());

		inventoryCustomMenuItem.addActionListener(actionListener());
		importInventoryMenuItem.addActionListener(actionListener());
		importOrderMenuItem.addActionListener(actionListener());
	}

	private ActionListener actionListener() {
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final ApplicationResourceBundle bundle = ApplicationResourceBundle.getApplicationResourceBundle();
				if (e.getActionCommand().equalsIgnoreCase("MANAGE_EXPENSE")) {
					displayExpenseScreen(bundle);
				} else if (e.getActionCommand().equalsIgnoreCase("EXIT_APP")) {
					exitApplication(bundle);
				} else if (e.getActionCommand().equalsIgnoreCase("MANAGE_VENDOR")) {
					displayVendorScreen(bundle);
				} else if (e.getActionCommand().equalsIgnoreCase("MANAGE_EMPLOYEE")) {
					displayEmployeeScreen(bundle);
				} else if (e.getActionCommand().equalsIgnoreCase("MANAGE_INVESTMENT")) {
					displayInvestmentScreen(bundle);
				} else if (e.getActionCommand().equalsIgnoreCase("MANAGE_ORDER_ALL")) {
					ApplicationSession.getSession().setProperty("CUSTOMER_SEARCH_KEY", null);
					displayOrderScreen(bundle, null, null);
				} else if (e.getActionCommand().equalsIgnoreCase("MANAGE_ORDER_CUSTOMER")) {
					DisplayCustomerListDialog dialog = new DisplayCustomerListDialog(SKAMainApp.this);
					dialog.setVisible(true);
					CustomerData customer = dialog.performSelectionOperation();
					if (customer != null) {
						ApplicationSession.getSession().setProperty("CUSTOMER_SEARCH_KEY", customer);
						Integer[] params = new Integer[1];
						params[0] = Integer.valueOf(customer.getCode());
						displayOrderScreen(bundle, "MANAGE_ORDER_CUSTOMER", params);
					} 
				} else if (e.getActionCommand().equalsIgnoreCase("MANAGE_ORDER_DELIVERY")) {
					displayOrderDeliveryScreen(bundle);
				} else if (e.getActionCommand().equalsIgnoreCase("MANAGE_PRODUCT")) {
					displayProductScreen(bundle);
				} else if (e.getActionCommand().equalsIgnoreCase("REPORT_ORDER_PENDING_DELIVERY")) {
					buildOrderReport(bundle, DeliveryStatus.SHIPPING.toString(), null, e);
				} else if (e.getActionCommand().equalsIgnoreCase("REPORT_ORDER_PENDING_PAYMENT")) {
					buildOrderReport(bundle, PaymentStatus.PENDING.toString(), null, e);
				} else if (e.getActionCommand().equalsIgnoreCase("DELIVERED_PENDING_PAYMENT")) {
					buildOrderReport(bundle, "DELIVERED_PENDING_PAYMENT", null, e);
				} else if (e.getActionCommand().equalsIgnoreCase("REPORT_ORDER_CONSOLIDATED")) {
					buildOrderReport(bundle, e.getActionCommand(), null, e);
				} else if (e.getActionCommand().equalsIgnoreCase("REPORT_ORDER_BY_CUSTOMER_GROUP")) {
					buildOrderReport(bundle, e.getActionCommand(), null, e);
				} else if (e.getActionCommand().equalsIgnoreCase("REPORT_ORDER_BY_CUSTOMER")) {
					DisplayCustomerListDialog dialog = new DisplayCustomerListDialog(SKAMainApp.this);
					dialog.setVisible(true);
					CustomerData customer = dialog.performSelectionOperation();
					if (customer != null) {
						Integer[] params = new Integer[1];
						params[0] = Integer.valueOf(customer.getCode());
						buildOrderReport(bundle, "REPORT_ORDER_BY_CUSTOMER", params, e);
					}

				} else if (e.getActionCommand().equalsIgnoreCase("REPORT_DELIVERY_QUANTITY_MISMATCH")) {
					buildOrderReport(bundle, "DELIVERY_QUANTITY_MISMATCH", null, e);
				} else if (e.getActionCommand().equals("LOG_OFF")) {
					processLogoff();
				} else if (e.getActionCommand().equals("CHANGE_PASSWORD")) {
					processChangePassword();
				} else if (e.getActionCommand().equalsIgnoreCase("EXPENSE_CONSOLIDATED_FILTERED")) {
					DisplayExpenseReportSelectionDialog d = new DisplayExpenseReportSelectionDialog(SKAMainApp.this);
					d.setVisible(true);
					if (d.isActionCancelled()) {
						return;
					}
					ReportSelectionData selection = d.getSelectionData();
					Object[] params = new Object[1];
					params[0] = selection;
					buildExpenseReport(bundle, e.getActionCommand(), params, e);
				} else if (e.getActionCommand().equalsIgnoreCase("REPORT_INVENTORY")) {
					DisplayInventoryReportSelectionDialog d = new DisplayInventoryReportSelectionDialog(
							SKAMainApp.this);
					d.setVisible(true);
					if (d.isActionCancelled()) {
						return;
					}
					ReportSelectionData selection = d.getSelectionData();
					Object[] params = new Object[1];
					params[0] = selection;
					buildInventoryReport(bundle, e.getActionCommand(), params, e);
				} else if (e.getActionCommand().equalsIgnoreCase("IMPORT_INVENTORIES")) {
					WebFileChooser chooser = new WebFileChooser();
					chooser.setMultiSelectionEnabled(false);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL Files", "xlsx");
					chooser.setFileFilter(filter);
					chooser.setDialogTitle("Select Inventory Import File");
					chooser.setFileSelectionMode(WebFileChooser.FILES_ONLY);
					chooser.setMultiSelectionEnabled(false);

					int selection = chooser.showOpenDialog(SKAMainApp.this);
					if (selection == WebFileChooser.APPROVE_OPTION) {
						String filepath = chooser.getSelectedFile().getAbsolutePath();
						LOG.info("SKAMainApp - Inventory Import - File Selected : "+filepath);
						ImportService service = new ImportService("INVENTORY", filepath);
						List<InventoryEntryData> newEntries = service.processInventoryImport();
						if(newEntries.isEmpty()) {
							WebOptionPane.showMessageDialog(SKAMainApp.this, "Inventories could not be imported. Please check Log.");
						} else {
							WebOptionPane.showMessageDialog(SKAMainApp.this, "["+newEntries.size()+"] Inventory entries got imported. ");
						}
					}
				} else if (e.getActionCommand().equalsIgnoreCase("IMPORT_ORDERS")) {
					WebFileChooser chooser = new WebFileChooser();
					chooser.setMultiSelectionEnabled(false);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL Files", "xlsx");
					chooser.setFileFilter(filter);
					chooser.setDialogTitle("Select Order Import File");
					chooser.setFileSelectionMode(WebFileChooser.FILES_ONLY);
					chooser.setMultiSelectionEnabled(false);

					int selection = chooser.showOpenDialog(SKAMainApp.this);
					if (selection == WebFileChooser.APPROVE_OPTION) {
						String filepath = chooser.getSelectedFile().getAbsolutePath();
						LOG.info("SKAMainApp - File Selected : "+filepath);
						ImportService service = new ImportService("ORDER", filepath);
						List<com.saikrupa.app.dto.OrderData> newEntries = service.processOrderImport();
						if(newEntries.isEmpty()) {
							WebOptionPane.showMessageDialog(SKAMainApp.this, "Orders could not be imported. Please check Log.");
						} else {
							WebOptionPane.showMessageDialog(SKAMainApp.this, "["+newEntries.size()+"] Orders imported. ");
						}
					}
				}
			}
		};
		return listener;
	}
	
	private void showOrderDelivery(OrderData data) {
		UpdateOrderDeliveryDetailDialog d = new UpdateOrderDeliveryDetailDialog(this, data.getOrderEntries().get(0));
		d.setVisible(true);
		
	}
	
	private void displayOrderDeliveryScreen(ApplicationResourceBundle bundle) {
		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());
		
		OrderDeliveryDAO orderDeliveryDao = new DefaultOrderDeliveryDAO();
		orderDeliveryContentTable = new WebTable(new OrderDeliveryTableModel(orderDeliveryDao.findOrdersByAllVehicles()));		
		contentPanel.add(new WebScrollPane(orderDeliveryContentTable));

		orderDeliveryContentTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));

		orderDeliveryContentTable.setRowHeight(35);
		orderDeliveryContentTable.setFont(new Font("verdana", Font.PLAIN, 14));

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == orderDeliveryContentTable) {
					if (e.getClickCount() > 1) {
						int currentRow = orderDeliveryContentTable.getSelectedRow();
						OrderDeliveryTableModel model = (OrderDeliveryTableModel) orderDeliveryContentTable.getModel();
						OrderData data = model.getOrderDataList().get(currentRow);
						showOrderDelivery(data);
					}
				}
			}			
		};
		orderDeliveryContentTable.addMouseListener(mouseListener);		

		WebPanel bottomPanel = new WebPanel();
		bottomPanel.setLayout(new BorderLayout());
		
		WebButton button = new WebButton("Test this");
		bottomPanel.add(button, BorderLayout.CENTER);
		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				bottomPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(200);
		splitPane.setContinuousLayout(false);

		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();	
	}

	private void processChangePassword() {
		ChangePasswordDialog d = new ChangePasswordDialog(this);
		d.setVisible(true);
	}

	private void processLogoff() {
		getContentPane().removeAll();
		menubar.removeAll();
		showLogin();
		revalidate();
	}

	private void exportReport(String actionCommand) throws IOException {
		WebFileChooser chooser = new WebFileChooser();
		chooser.setMultiSelectionEnabled(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf", "PDF");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Save Report As...");
		chooser.setFileSelectionMode(WebFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		int selection = chooser.showSaveDialog(this);
		if (selection == WebFileChooser.APPROVE_OPTION) {
			ReportService reportService = null;
			if ("REPORT_ORDER_PENDING_PAYMENT".equalsIgnoreCase(actionCommand)) {
				OrderTableModel model = (OrderTableModel) orderContentTable.getModel();
				reportService = new PendingPaymentOrderReportService(model.getOrderDataList());
			} else if ("REPORT_ORDER_PENDING_DELIVERY".equalsIgnoreCase(actionCommand)) {
				OrderTableModel model = (OrderTableModel) orderContentTable.getModel();
				reportService = new PendingDeliveryOrderReportService(model.getOrderDataList());
			} else if ("REPORT_ORDER_BY_CUSTOMER".equalsIgnoreCase(actionCommand)) {
				OrderTableModel model = (OrderTableModel) orderContentTable.getModel();
				reportService = new CustomerOrderReportService(model.getOrderDataList());
			} else if ("REPORT_ORDER_CONSOLIDATED".equalsIgnoreCase(actionCommand)) {
				OrderTableModel model = (OrderTableModel) orderContentTable.getModel();
				reportService = new ConsolidatedOrderReportService(model.getOrderDataList());
			} else if ("REPORT_ORDER_BY_CUSTOMER_GROUP".equalsIgnoreCase(actionCommand)) {
				OrderTableModel model = (OrderTableModel) orderContentTable.getModel();
				reportService = new OrderByCustomerGroupReportService(model.getOrderDataList());
			} else if ("DELIVERED_PENDING_PAYMENT".equalsIgnoreCase(actionCommand)) {
				OrderTableModel model = (OrderTableModel) orderContentTable.getModel();
				reportService = new DeliveredPendingPaymentOrderReportService(model.getOrderDataList());
			} else if ("EXPENSE_CONSOLIDATED".equalsIgnoreCase(actionCommand)) {
				ExpenseTableModel expenseModel = (ExpenseTableModel) expenseContentTable.getModel();
				reportService = new ConsolidatedExpenseReportService(expenseModel.getExpenseDataList());
			} else if ("EXPENSE_FILTERED".equalsIgnoreCase(actionCommand)) {
				ExpenseTableModel expenseModel = (ExpenseTableModel) expenseContentTable.getModel();
				reportService = new FilteredExpenseReportService(expenseModel.getExpenseDataList(),
						expenseModel.getSelectionData());
			} else if ("REPORT_INVENTORY".equalsIgnoreCase(actionCommand)) {
				InventoryHistoryModel invModel = (InventoryHistoryModel) productInventoryHistoryTable.getModel();
				reportService = new FilteredInventoryReportService(invModel.getInventoryDataList(),
						invModel.getReportSelectionData());

			} else {
				WebOptionPane.showMessageDialog(this, "Not Implemented");
				return;
			}
			if (chooser.getSelectedFile() != null) {
				LOG.info("SKAMainApp - Selected File : " + chooser.getSelectedFile().getCanonicalPath());
				reportService.saveReport(chooser.getSelectedFile().getAbsolutePath());
				showSuccessNotification();
			}
		}
	}

	private void buildInventoryReport(ApplicationResourceBundle bundle, final String status, Object[] params,
			ActionEvent e) {
		getContentPane().removeAll();
		WebPanel basePanel = new WebPanel(true);
		exportReportButton = new WebButton("Export Inventory Report");
		exportReportButton.setFont(getLabelFont());
		exportReportButton.setActionCommand(e.getActionCommand());
		basePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());

		exportReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					exportReport(exportReportButton.getActionCommand());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		ReportSelectionData selectionData = (ReportSelectionData) params[0];
		ProductDAO productDAO = new DefaultProductDAO();
		InventoryHistoryModel invHistoryModel = new InventoryHistoryModel(
				productDAO.searchInventoryWithFilter(selectionData));

		invHistoryModel.setReportSelectionData(selectionData);
		productInventoryHistoryTable = new WebTable(invHistoryModel);
		productInventoryHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		contentPanel.add(new WebScrollPane(productInventoryHistoryTable));
		if (invHistoryModel != null && !invHistoryModel.getInventoryDataList().isEmpty()) {
			basePanel.add(exportReportButton);
		}

		productInventoryHistoryTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));
		productInventoryHistoryTable.setRowHeight(35);
		productInventoryHistoryTable.setFont(new Font("verdana", Font.PLAIN, 14));

		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				basePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(400);
		splitPane.setContinuousLayout(false);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();
	}

	private void buildExpenseReport(ApplicationResourceBundle bundle, final String status, Object[] params,
			ActionEvent e) {
		getContentPane().removeAll();
		WebPanel basePanel = new WebPanel(true);
		exportReportButton = new WebButton("Export Expense Report");
		exportReportButton.setFont(getLabelFont());
		exportReportButton.setActionCommand(e.getActionCommand());
		basePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());

		exportReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					exportReport(exportReportButton.getActionCommand());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		ExpenseDAO expenseDAO = new DefaultExpenseDAO();
		ReportSelectionData selectionData = (ReportSelectionData) params[0];
		if (selectionData.getSelectionType() == 0) {
			exportReportButton.setActionCommand("EXPENSE_CONSOLIDATED");
		} else {
			exportReportButton.setActionCommand("EXPENSE_FILTERED");
		}
		ExpenseTableModel expenseTableModel = new ExpenseTableModel(expenseDAO.searchExpenseWithFilter(selectionData));
		expenseTableModel.setSelectionData(selectionData);
		expenseContentTable = new WebTable(expenseTableModel);
		expenseContentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		contentPanel.add(new WebScrollPane(expenseContentTable));
		if (expenseTableModel != null && !expenseTableModel.getExpenseDataList().isEmpty()) {
			basePanel.add(exportReportButton);
		}

		expenseContentTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));
		expenseContentTable.setRowHeight(35);
		expenseContentTable.setFont(new Font("verdana", Font.PLAIN, 14));

		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				basePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(400);
		splitPane.setContinuousLayout(false);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();
	}

	private void buildOrderReport(ApplicationResourceBundle bundle, final String status, Object[] params,
			ActionEvent e) {
		getContentPane().removeAll();
		WebPanel basePanel = new WebPanel(true);
		exportReportButton = new WebButton("Export Report");
		exportReportButton.setFont(getLabelFont());
		exportReportButton.setActionCommand(e.getActionCommand());
		basePanel.setLayout(new BorderLayout(20, 20));
		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());

		WebPanel baseReportLabelPanel = new WebPanel();
		baseReportLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		WebPanel baseReporButtontPanel = new WebPanel();
		baseReporButtontPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		exportReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					exportReport(exportReportButton.getActionCommand());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		OrderDAO orderDAO = new DefaultOrderDAO();
		OrderTableModel orderTableModel = new OrderTableModel(orderDAO.searchOrderWithFilter(status, params));
		orderContentTable = new WebTable(orderTableModel);
		orderContentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		contentPanel.add(new WebScrollPane(orderContentTable));
		if (orderTableModel != null && !orderTableModel.getOrderDataList().isEmpty()) {
			WebLabel reportNameLabel = new WebLabel();
			reportNameLabel.setFont(getLabelFont());
			reportNameLabel.setText(status);
			baseReportLabelPanel.add(reportNameLabel);
			baseReporButtontPanel.add(exportReportButton);

			basePanel.add(baseReportLabelPanel, BorderLayout.NORTH);
			basePanel.add(baseReporButtontPanel, BorderLayout.SOUTH);
		}

		orderContentTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));
		orderContentTable.setRowHeight(35);
		orderContentTable.setFont(new Font("verdana", Font.PLAIN, 14));

		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				basePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(400);
		splitPane.setContinuousLayout(false);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();

	}

	public void displayOrderScreen(ApplicationResourceBundle bundle, String operation, Integer[] param) {
		getContentPane().removeAll();
		final WebButton createOrderButton = new WebButton("Create Order");
		createOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displayCreateOrderDialog();
			}
		});

		final WebButton bulkPaymentButton = new WebButton("Add Payment");
		bulkPaymentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displayCreateAdhocPaymentDialog();
			}
		});
		WebPanel bottomPanel = new WebPanel();
		bottomPanel.setLayout(new BorderLayout());

		WebPanel buttonPanel = new WebPanel(true);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		buttonPanel.add(createOrderButton);
		buttonPanel.add(bulkPaymentButton);

		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

		createOrderButton.setFont(getLabelFont());
		bulkPaymentButton.setFont(getLabelFont());

		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());

		OrderDAO orderDAO = new DefaultOrderDAO();
		CustomerData lastCustomer = (CustomerData) ApplicationSession.getSession().getProperty("CUSTOMER_SEARCH_KEY");
		if (operation == null && lastCustomer == null) {
			OrderTableModel orderTableModel = new OrderTableModel(orderDAO.findAllOrders());
			orderContentTable = new WebTable(orderTableModel);
			bulkPaymentButton.setEnabled(false);
		} else if (operation != null && operation.equalsIgnoreCase("MANAGE_ORDER_CUSTOMER")) {
			OrderTableModel orderTableModel = new OrderTableModel(
					orderDAO.findOrdersByCustomer(Integer.valueOf(lastCustomer.getCode())));
			orderContentTable = new WebTable(orderTableModel);
			Double[] values = getValueNew(orderTableModel);
			// if(values[2] == 0) {
			// bulkPaymentButton.setEnabled(false);
			// }
		}
		contentPanel.add(new WebScrollPane(orderContentTable));

		orderContentTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));
		orderContentTable.setRowHeight(35);
		orderContentTable.setFont(new Font("verdana", Font.PLAIN, 14));

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					if (e.getSource() == orderContentTable) {
						int currentRow = orderContentTable.getSelectedRow();
						OrderTableModel model = (OrderTableModel) orderContentTable.getModel();
						OrderData data = model.getOrderDataList().get(currentRow);
						displayOrderUpdateDialog(data);
					}
				}
			}
		};
		orderContentTable.addMouseListener(mouseListener);

		WebPanel formPanel = new WebPanel();
		GridBagLayout layout = new GridBagLayout();
		formPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		WebLabel l1 = new WebLabel("Total Order Value : ", SwingConstants.RIGHT);
		l1.setFont(new Font("verdana", Font.BOLD, 14));
		Double[] dataValues = getValueNew(orderContentTable.getModel());
		totalOrderValueLabel = new WebLabel(String.format("%,.2f", dataValues[0]));
		totalOrderValueLabel.setFont(new Font("verdana", Font.BOLD, 14));

		WebLabel l2 = new WebLabel("Paid Amount : ", SwingConstants.RIGHT);
		totalOrderPaidValueLabel = new WebLabel(String.format("%,.2f", dataValues[1]));
		l2.setFont(new Font("verdana", Font.BOLD, 14));
		totalOrderPaidValueLabel.setFont(new Font("verdana", Font.BOLD, 14));

		WebLabel l3 = new WebLabel("Pending Payment : ", SwingConstants.RIGHT);
		totalOrderPendingValueLabel = new WebLabel(String.format("%,.2f", dataValues[2]));

		l3.setFont(new Font("verdana", Font.BOLD, 14));
		totalOrderPendingValueLabel.setFont(new Font("verdana", Font.BOLD, 14));

		WebLabel l4 = new WebLabel("Total Order Count : ", SwingConstants.RIGHT);
		OrderTableModel model = (OrderTableModel) orderContentTable.getModel();
		totalOrderCountLabel = new WebLabel();
		totalOrderCountLabel.setText(String.valueOf(model.getRowCount()));

		l4.setFont(new Font("verdana", Font.BOLD, 14));
		totalOrderCountLabel.setFont(new Font("verdana", Font.BOLD, 14));

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 15, 5, 0);
		layout.setConstraints(l1, c);
		formPanel.add(l1);

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 15, 5, 0);
		layout.setConstraints(totalOrderValueLabel, c);
		formPanel.add(totalOrderValueLabel);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(15, 0, 5, 0); // top padding
		layout.setConstraints(l2, c);
		formPanel.add(l2);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(15, 15, 5, 0);
		layout.setConstraints(totalOrderPaidValueLabel, c);
		formPanel.add(totalOrderPaidValueLabel);

		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(15, 0, 5, 0); // top padding
		layout.setConstraints(l3, c);
		formPanel.add(l3);

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(15, 15, 5, 0);
		layout.setConstraints(totalOrderPendingValueLabel, c);
		formPanel.add(totalOrderPendingValueLabel);

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(15, 0, 5, 0); // top padding
		layout.setConstraints(l4, c);
		formPanel.add(l4);

		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(15, 15, 5, 0);
		layout.setConstraints(totalOrderCountLabel, c);
		formPanel.add(totalOrderCountLabel);

		WebPanel summaryPanel = new WebPanel();
		summaryPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		summaryPanel.add(formPanel, BorderLayout.WEST);
		bottomPanel.add(summaryPanel, BorderLayout.WEST);

		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				bottomPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(400);
		splitPane.setContinuousLayout(false);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();
	}

	private void displayCreateAdhocPaymentDialog() {
		OrderTableModel model = (OrderTableModel) orderContentTable.getModel();
		if (CollectionUtils.isEmpty(model.getOrderDataList())) {
			WebOptionPane.showMessageDialog(this, "No Order List Available for Payment");
		} else {
			CreateAdhocOrderPaymentDialog dialog = new CreateAdhocOrderPaymentDialog(this, model);
			dialog.setVisible(true);
		}

	}

	public void updateOrderSummary() {
		Double[] dataValues = getValueNew(orderContentTable.getModel());
		totalOrderValueLabel.setText(String.format("%,.2f", dataValues[0]));
		totalOrderPaidValueLabel.setText(String.format("%,.2f", dataValues[1]));
		totalOrderPendingValueLabel.setText(String.format("%,.2f", dataValues[2]));
		totalOrderCountLabel.setText(String.valueOf(dataValues[3].intValue()));
		revalidate();
	}

	public Double[] getValue(TableModel tableModel) {
		OrderTableModel model = (OrderTableModel) tableModel;
		Double total = 0.0;
		Double paid = 0.0;
		Double partialPaidAmount = 0D;
		for (OrderData orderData : model.getOrderDataList()) {
			total = total + orderData.getTotalPrice();
			if (orderData.getPaymentStatus() == PaymentStatus.PAID) {
				paid = paid + orderData.getTotalPrice();
			} else if (orderData.getPaymentStatus() == PaymentStatus.PARTIAL) {
				OrderEntryData entry = orderData.getOrderEntries().get(0);

				for (PaymentEntryData paymentEntry : entry.getPaymentEntries()) {
					if (paymentEntry.getPaymentStatus() == PaymentStatus.PARTIAL) {
						partialPaidAmount = partialPaidAmount + paymentEntry.getAmount();
					}
				}
			}
		}
		Double[] values = new Double[3];
		values[0] = total;
		values[1] = paid + partialPaidAmount;
		values[2] = values[0] - values[1];

		return values;
	}

	public Double[] getValueNew(TableModel tableModel) {
		OrderTableModel model = (OrderTableModel) tableModel;
		Double total = 0.0;
		Double paid = 0.0;
		Double orderCount = 0.0;

		for (OrderData orderData : model.getOrderDataList()) {
			orderCount = orderCount + 1;
			total = total + orderData.getTotalPrice();
			OrderEntryData entry = orderData.getOrderEntries().get(0);
			for (PaymentEntryData paymentEntry : entry.getPaymentEntries()) {
				if (paymentEntry.getPaymentStatus() == PaymentStatus.PAID
						|| paymentEntry.getPaymentStatus() == PaymentStatus.PARTIAL) {
					paid = paid + paymentEntry.getAmount();
				}
			}

		}
		Double[] values = new Double[4];
		values[0] = total;
		values[1] = paid;
		values[2] = total - paid;
		values[3] = orderCount;

		CustomerData lastCustomer = (CustomerData) ApplicationSession.getSession().getProperty("CUSTOMER_SEARCH_KEY");
		if (lastCustomer != null) {
			CustomerDAO customerDAO = new DefaultCustomerDAO();
			List<PaymentEntryData> customerPayments = customerDAO.findAdhocPaymentByCustomer(lastCustomer.getCode());
			for (PaymentEntryData entry : customerPayments) {
				if (entry.getPayableAmount() - entry.getAmount() < 0) {
					values[2] = values[2] + (entry.getPayableAmount() - entry.getAmount());
				}
			}
			double paidAmount = 0.0;
			for (PaymentEntryData entry : customerPayments) {
				paidAmount = paidAmount + entry.getAmount();
			}
			values[1] = paidAmount;

		}

		LOG.info("SKAMainApp - *********************** getValueNew ************************");
		LOG.info("SKAMainApp - Total Order Amount : " + values[0]);
		LOG.info("SKAMainApp - Paid Amount : " + values[1]);
		LOG.info("SKAMainApp - Balance Amount : " + values[2]);
		LOG.info("SKAMainApp - Order Count : " + values[3]);
		LOG.info("SKAMainApp - ***********************************************");
		return values;
	}

	public WebTable getProductInventoryHistoryTable() {
		return productInventoryHistoryTable;
	}

	public void setProductInventoryHistoryTable(WebTable productInventoryHistoryTable) {
		this.productInventoryHistoryTable = productInventoryHistoryTable;
	}

	private void displayOrderUpdateDialog(OrderData data) {
		UpdateOrderDialog dialog = new UpdateOrderDialog(this, data);
		dialog.setVisible(true);

	}

	private void displayVendorUpdateDialog(VendorData data) {
		UpdateVendorDialog dialog = new UpdateVendorDialog(this, data);
		dialog.setVisible(true);
	}

	private void displayEmployeeUpdateDialog(EmployeeData data) {
		UpdateEmployeeDialog dialog = new UpdateEmployeeDialog(this, data);
		dialog.setVisible(true);
	}

	private boolean hasInventorySameProductType(List<InventoryEntryData> entries) {
		String productCode = null;
		for (InventoryEntryData entry : entries) {
			String nextProductCode = entry.getInventory().getProduct().getCode();
			if (productCode == null) {
				productCode = nextProductCode;
			} else {
				if (!nextProductCode.equals(productCode)) {
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}

	private void displayProductScreen(ApplicationResourceBundle bundle) {
		getContentPane().removeAll();
		

		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());

		ProductDAO productDAO = new DefaultProductDAO();

		productContentTable = new WebTable(new ProductTableModel(productDAO.findAllProducts()));
		contentPanel.add(new WebScrollPane(productContentTable));

		productContentTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));

		productContentTable.getColumnModel().getColumn(1).setMinWidth(230);
		productContentTable.getColumnModel().getColumn(1).setMaxWidth(230);

		productContentTable.getColumnModel().getColumn(5).setMinWidth(220);
		productContentTable.getColumnModel().getColumn(5).setMaxWidth(220);

		productContentTable.getColumnModel().getColumn(6).setMinWidth(220);
		productContentTable.getColumnModel().getColumn(6).setMaxWidth(220);

		productContentTable.setRowHeight(35);
		productContentTable.setFont(new Font("verdana", Font.PLAIN, 14));

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == productContentTable) {
					if (e.getClickCount() > 1) {
						int currentRow = productContentTable.getSelectedRow();
						ProductTableModel model = (ProductTableModel) productContentTable.getModel();
						ProductData data = model.getProductDataList().get(currentRow);
						showProduct(data);
					}
				}
			}
		};
		productContentTable.addMouseListener(mouseListener);

		productInventoryHistoryTable = new WebTable(
				new InventoryHistoryModel(productDAO.findInventoryHistoryForAllProduct()));
		productInventoryHistoryTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));

		productInventoryHistoryTable.getColumnModel().getColumn(1).setMinWidth(230);
		productInventoryHistoryTable.getColumnModel().getColumn(1).setMaxWidth(230);

		productInventoryHistoryTable.getColumnModel().getColumn(5).setMinWidth(220);
		productInventoryHistoryTable.getColumnModel().getColumn(5).setMaxWidth(220);

		productInventoryHistoryTable.getColumnModel().getColumn(6).setMinWidth(220);
		productInventoryHistoryTable.getColumnModel().getColumn(6).setMaxWidth(220);

		productInventoryHistoryTable.setRowHeight(35);
		productInventoryHistoryTable.setFont(new Font("verdana", Font.PLAIN, 14));

		// productInventoryHistoryTable.getSelectionModel().addListSelectionListener(new
		// ListSelectionListener() {
		//
		// public void valueChanged(ListSelectionEvent e) {
		// InventoryHistoryModel model = (InventoryHistoryModel)
		// productInventoryHistoryTable.getModel();
		// if (productInventoryHistoryTable.getSelectedRowCount() > 0
		// &&
		// model.getInventoryDataList().get(e.getFirstIndex()).getLabourPaymentStatus()
		// != 1) {
		// markAsPaidButton.setEnabled(true);
		// markAsPaidButton.setBottomBgColor(Color.GREEN);
		// } else {
		// markAsPaidButton.setEnabled(false);
		// markAsPaidButton.setBottomBgColor(Color.LIGHT_GRAY);
		// }
		// }
		// });

		WebPanel bottomPanel = new WebPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(new WebScrollPane(productInventoryHistoryTable), BorderLayout.CENTER);
		// bottomPanel.add(basePanel, BorderLayout.NORTH);

		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				bottomPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(200);
		splitPane.setContinuousLayout(false);

		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();
	}

	private void displayInvestmentScreen(ApplicationResourceBundle bundle) {
		getContentPane().removeAll();
		final WebButton createInvestmentButton = new WebButton("Create Investment");
		createInvestmentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displayCreateInvestmentDialog(SKAMainApp.this);
			}
		});

		WebPanel basePanel = new WebPanel(true);
		basePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		basePanel.add(createInvestmentButton);

		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());

		InvestorDAO dao = new DefaultInvestorDAO();
		investorContentTable = new WebTable(new InvestorTableModel(dao.findInvestors()));
		contentPanel.add(new WebScrollPane(investorContentTable));

		investorContentTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));
		investorContentTable.setRowHeight(35);
		investorContentTable.setFont(new Font("verdana", Font.PLAIN, 14));
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == investorContentTable) {
					if (e.getClickCount() > 1) {
						int currentRow = investorContentTable.getSelectedRow();
						InvestorTableModel model = (InvestorTableModel) investorContentTable.getModel();
						InvestorData data = model.getInvestorDataList().get(currentRow);
						showInvestments(data);
					}
				}
			}
		};
		investorContentTable.addMouseListener(mouseListener);

		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				basePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(400);
		splitPane.setContinuousLayout(false);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();
	}

	private void decorateSouthPanel() {
		WebPanel mainSouthPanel = new WebPanel(true);
		mainSouthPanel.setLayout(new BorderLayout());

		WebPanel dbPanel = new WebPanel(true);
		dbPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		WebLabel dbLabel = new WebLabel();
		dbLabel.setFont(new Font("verdana", Font.BOLD, 12));
		PersistentManager manager = PersistentManager.getPersistentManager();
		if (manager.getDatabaseName() != null) {
			dbLabel.setText(" " + manager.getDatabaseName());
			dbPanel.add(dbLabel);
			mainSouthPanel.add(dbLabel, BorderLayout.WEST);
		}

		WebPanel southPanel = new WebPanel(true);
		southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 4));
		ApplicationSession session = ApplicationSession.getSession();
		ApplicationUserData userData = (ApplicationUserData) session.getProperty("CURRENT_USER");
		if (userData != null) {
			WebLabel user = new WebLabel(userData.getName());
			user.setFont(getLabelFont());
			southPanel.add(new WebLabel("Logged On : "));
			southPanel.add(user);

			WebButton logoffButton = new WebButton();
			logoffButton.setIcon(createImageIcon("button_logoff.png"));
			southPanel.add(logoffButton);
			logoffButton.setActionCommand("LOG_OFF");
			logoffButton.setToolTipText("Log off");
			logoffButton.addActionListener(actionListener());
		}
		mainSouthPanel.add(southPanel, BorderLayout.EAST);
		getContentPane().add(mainSouthPanel, BorderLayout.SOUTH);
	}

	private void displayCreateOrderDialog() {
		ManageOrderDialog orderDialog = new ManageOrderDialog(this);
		orderDialog.setVisible(true);

	}

	private void displayCreateVendorDialog(SKAMainApp owner) {
		CreateVendorDialog vendorDialog = new CreateVendorDialog(owner);
		vendorDialog.setVisible(true);
	}

	private void displayCreateEmployeeDialog(SKAMainApp owner) {
		CreateEmployeeDialog dialog = new CreateEmployeeDialog(owner);
		dialog.setVisible(true);
	}

	private void displayCreateInvestmentDialog(SKAMainApp owner) {
		ManageInvestmentDialog dialog = new ManageInvestmentDialog(owner);
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	private void displayCreateExpenseDialog(SKAMainApp owner) {
		ManageExpenseDialog expenseDialog = new ManageExpenseDialog(owner);
		expenseDialog.setVisible(true);
	}

	private void displayEmployeeScreen(ApplicationResourceBundle bundle) {
		getContentPane().removeAll();
		final WebButton createEmployeeButton = new WebButton("Create Employee");
		createEmployeeButton.setFont(getLabelFont());
		createEmployeeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displayCreateEmployeeDialog(SKAMainApp.this);
			}
		});

		WebPanel basePanel = new WebPanel(true);
		basePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		basePanel.add(createEmployeeButton);

		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());

		EmployeeDAO dao = new DefaultEmployeeDAO();
		employeeContentTable = new WebTable(new EmployeeTableModel(dao.getAllEmployees()));
		contentPanel.add(new WebScrollPane(employeeContentTable));

		employeeContentTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));
		employeeContentTable.setRowHeight(35);
		employeeContentTable.setFont(new Font("verdana", Font.PLAIN, 14));

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == employeeContentTable) {
					if (e.getClickCount() > 1) {
						int currentRow = employeeContentTable.getSelectedRow();
						EmployeeTableModel model = (EmployeeTableModel) employeeContentTable.getModel();
						EmployeeData data = model.getEmployeeDataList().get(currentRow);
						displayEmployeeUpdateDialog(data);
					}
				}
			}
		};
		employeeContentTable.addMouseListener(mouseListener);

		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				basePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(400);
		splitPane.setContinuousLayout(false);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();
	}

	private void displayVendorScreen(ApplicationResourceBundle bundle) {
		getContentPane().removeAll();
		final WebButton createVendorButton = new WebButton("Create Vendor");
		createVendorButton.setFont(getLabelFont());
		createVendorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displayCreateVendorDialog(SKAMainApp.this);
			}
		});

		WebPanel basePanel = new WebPanel(true);
		basePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		basePanel.add(createVendorButton);

		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());

		VendorService vendorService = new DefaultVendorService();
		vendorContentTable = new WebTable(new VendorTableModel(vendorService.getAllVendors()));
		vendorContentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPanel.add(new WebScrollPane(vendorContentTable));

		vendorContentTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));
		vendorContentTable.setRowHeight(35);
		vendorContentTable.setFont(new Font("verdana", Font.PLAIN, 14));

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					if (e.getSource() == vendorContentTable) {
						int currentRow = vendorContentTable.getSelectedRow();
						VendorTableModel model = (VendorTableModel) vendorContentTable.getModel();
						VendorData data = model.getVendorDataList().get(currentRow);
						displayVendorUpdateDialog(data);
					}
				}
			}
		};
		vendorContentTable.addMouseListener(mouseListener);

		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				basePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(400);
		splitPane.setContinuousLayout(false);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();
	}

	private void displaySearchExpenseDialog() {
		SearchExpenseDialog d = new SearchExpenseDialog(this);
		d.setVisible(true);
		List<ExpenseData> results = d.getExpenseResult();
		ExpenseTableModel expenseTableModel = (ExpenseTableModel) expenseContentTable.getModel();
		expenseTableModel.setExpenseDataList(results);
		expenseTableModel.fireTableDataChanged();
	}

	private void displayExpenseScreen(ApplicationResourceBundle bundle) {
		getContentPane().removeAll();
		final WebButton createExpenseButton = new WebButton("Create Expense");
		createExpenseButton.setFont(getLabelFont());
		createExpenseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displayCreateExpenseDialog(SKAMainApp.this);
			}

		});

		final WebButton searchExpenseButton = new WebButton("Search Expense");
		searchExpenseButton.setFont(getLabelFont());
		searchExpenseButton.setIcon(createImageIcon("search.png"));

		searchExpenseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displaySearchExpenseDialog();
			}
		});

		WebPanel basePanel = new WebPanel(true);
		basePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		basePanel.add(createExpenseButton);
		basePanel.add(searchExpenseButton);

		WebPanel contentPanel = new WebPanel(true);
		contentPanel.setLayout(new BorderLayout());

		ExpenseDAO expenseDAO = new DefaultExpenseDAO();

		expenseContentTable = new WebTable(new ExpenseTableModel(expenseDAO.findAllExpenses()));
		contentPanel.add(new WebScrollPane(expenseContentTable));
		expenseContentTable.getTableHeader().setFont(new Font("verdana", Font.BOLD, 14));
		expenseContentTable.setRowHeight(35);
		expenseContentTable.setFont(new Font("verdana", Font.PLAIN, 14));

		expenseContentTable.getColumnModel().getColumn(0).setMaxWidth(110);
		expenseContentTable.getColumnModel().getColumn(0).setMinWidth(110);

		expenseContentTable.getColumnModel().getColumn(1).setMaxWidth(370);
		expenseContentTable.getColumnModel().getColumn(1).setMinWidth(370);

		expenseContentTable.getColumnModel().getColumn(2).setMaxWidth(90);
		expenseContentTable.getColumnModel().getColumn(2).setMinWidth(90);

		expenseContentTable.getColumnModel().getColumn(3).setMaxWidth(140);
		expenseContentTable.getColumnModel().getColumn(3).setMinWidth(140);

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == expenseContentTable) {
					if (e.getClickCount() > 1) {
						int currentRow = expenseContentTable.getSelectedRow();
						ExpenseTableModel model = (ExpenseTableModel) expenseContentTable.getModel();
						ExpenseData data = model.getExpenseDataList().get(currentRow);
						showExpense(data);
					}

				} else if (e.getSource() == investorContentTable) {
					if (e.getClickCount() > 1) {
						int currentRow = investorContentTable.getSelectedRow();
						InvestorTableModel model = (InvestorTableModel) investorContentTable.getModel();
						InvestorData data = model.getInvestorDataList().get(currentRow);
						showInvestments(data);
					}
				}
			}
		};
		expenseContentTable.addMouseListener(mouseListener);

		WebSplitPane splitPane = new WebSplitPane(com.alee.laf.splitpane.WebSplitPane.VERTICAL_SPLIT, contentPanel,
				basePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(400);
		splitPane.setContinuousLayout(false);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		decorateSouthPanel();
		revalidate();
	}

	private void showInvestments(InvestorData data) {
		UpdateInvestmentDialog d = new UpdateInvestmentDialog(this, data);
		d.setVisible(true);

	}

	private void showProduct(ProductData data) {
		ManageProductDialog dialog = new ManageProductDialog(this, data);
		dialog.setVisible(true);
	}

	private void showExpense(ExpenseData data) {
		ManageExpenseDialog d = new ManageExpenseDialog(this, data);
		d.setVisible(true);

	}

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					WebLookAndFeel.install(true);
					WebLookAndFeel.setDecorateFrames(true);
					WebLookAndFeel.setDecorateDialogs(true);

					// Opening SDMainApp
					SKAMainApp app = new SKAMainApp();					
					app.setSize(new Dimension(800, 700));
					app.setResizable(true);
					app.setVisible(true);
				}
			});
		} catch (Exception e) {
			LOG.info("SKAMainApp - Unable to load WebLookAndFeel...");
		}
	}

	public WebTable getExpenseContentTable() {
		return expenseContentTable;
	}

	public WebTable getVendorContentTable() {
		return vendorContentTable;
	}

	public WebTable getInvestorContentTable() {
		return investorContentTable;
	}

	public void setInvestorContentTable(WebTable investorContentTable) {
		this.investorContentTable = investorContentTable;
	}

	public WebTable getOrderContentTable() {
		return orderContentTable;
	}

	public void setOrderContentTable(WebTable orderContentTable) {
		this.orderContentTable = orderContentTable;
	}

	public WebTable getProductContentTable() {
		return productContentTable;
	}

	public void setProductContentTable(WebTable productContentTable) {
		this.productContentTable = productContentTable;
	}

	public WebTable getEmployeeContentTable() {
		return employeeContentTable;
	}

	public void setEmployeeContentTable(WebTable employeeContentTable) {
		this.employeeContentTable = employeeContentTable;
	}

	public void showSuccessNotification() {
		final WebNotification notificationPopup = new WebNotification();
		notificationPopup.setIcon(NotificationIcon.plus);
		notificationPopup.setDisplayTime(3000);

		final WebClock clock = new WebClock();
		clock.setClockType(ClockType.timer);
		clock.setTimeLeft(3000);
		clock.setTimePattern("'Operation Successfully executed !!!' ");
		clock.setFont(new Font("Verdana", Font.BOLD, 14));
		clock.setForeground(Color.BLACK);
		notificationPopup.setContent(new GroupPanel(clock));
		NotificationManager.showNotification(notificationPopup);
		clock.start();
	}

	public WebTable getOrderDeliveryContentTable() {
		return orderDeliveryContentTable;
	}

	public void setOrderDeliveryContentTable(WebTable orderDeliveryContentTable) {
		this.orderDeliveryContentTable = orderDeliveryContentTable;
	}
}
