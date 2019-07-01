package com.saikrupa.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.saikrupa.app.dao.DefaultPaymentModeDAO;
import com.saikrupa.app.dao.EmployeeDAO;
import com.saikrupa.app.dao.ExpenseDAO;
import com.saikrupa.app.dao.ExpenseTypeDAO;
import com.saikrupa.app.dao.InvestorDAO;
import com.saikrupa.app.dao.PaymentModeDAO;
import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.ExpenseData;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.dto.ExpenseTypeData;
import com.saikrupa.app.dto.FilterParameter;
import com.saikrupa.app.dto.InvestorData;
import com.saikrupa.app.dto.PaymentTypeData;
import com.saikrupa.app.dto.VendorData;
import com.saikrupa.app.service.impl.DefaultVendorService;
import com.saikrupa.app.util.DateUtil;

public class DefaultExpenseDAO implements ExpenseDAO {

	public DefaultExpenseDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public List<ExpenseData> findAllExpenses() {
		List<ExpenseData> expList = new ArrayList<ExpenseData>();
		final String sql = "SELECT CODE, EXPENSE_DATE, AMOUNT, VENDOR_CODE, INVESTOR_CODE, CATEGORY_CODE, PAYMEMT_CODE, NOTES, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE FROM EXPENSE ORDER BY EXPENSE_DATE DESC";
		
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		EmployeeDAO employeeDAO = new DefaultEmployeeDAO();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ExpenseData data = new ExpenseData();
				data.setCode(rs.getString(1));
				data.setExpenseDate((java.util.Date)rs.getDate(2));
				data.setAmount(rs.getDouble(3));
				
				String vendorCode = rs.getString(4);
				DefaultVendorService vendorService = new DefaultVendorService();
				VendorData vendor = vendorService.findVendorByCode(vendorCode);
				data.setVendor(vendor);
				
				String paidByCode = rs.getString(5);
				InvestorDAO investorDAO = new DefaultInvestorDAO();
				InvestorData investor = investorDAO.findInvestorByCode(paidByCode);
				data.setPaidBy(investor);
				
				String categoryCode = rs.getString(6);
				ExpenseTypeDAO expTypeDAO = new DefaultExpenseTypeDAO();
				ExpenseTypeData expType = expTypeDAO.findExpenseTypeByCode(categoryCode);
				data.setExpenseType(expType);
				
				
				String paymentMode = rs.getString(7);
				PaymentModeDAO paymentModeDAO = new DefaultPaymentModeDAO();
				PaymentTypeData paymentData = paymentModeDAO.getPaymentModeByCode(paymentMode);
				data.setPaymentData(paymentData);
				
				data.setComments(rs.getString(8));
				//CREATED_BY,CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE
				data.setCreatedBy(employeeDAO.findEmployeeByCode(rs.getString(9)));
				data.setCreatedDate(DateUtil.convertDate(rs.getTimestamp(10)));
				
				data.setUpdatedBy(employeeDAO.findEmployeeByCode(rs.getString(11)));
				data.setLastModifedDate(DateUtil.convertDate(rs.getTimestamp(12)));
				expList.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return expList;
	}

	public ExpenseData findExpenseByCode(String expenseCode) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<ExpenseData> searchExpenseWithFilter(Map<String, String> params){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT CODE, EXPENSE_DATE, AMOUNT, VENDOR_CODE, INVESTOR_CODE, CATEGORY_CODE, PAYMEMT_CODE, NOTES, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE FROM EXPENSE WHERE CATEGORY_CODE=?");		
		for(Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();			
			if("VENDOR".equalsIgnoreCase(key)) {
				builder.append(" AND VENDOR_CODE=?");
			}			
		}
		builder.append(" ORDER BY EXPENSE_DATE DESC");
		
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		EmployeeDAO employeeDAO = new DefaultEmployeeDAO();
		List<ExpenseData> expList = new ArrayList<ExpenseData>();
		try {
			PreparedStatement ps = connection.prepareStatement(builder.toString());
			if(params.containsKey("EXPENSE_TYPE")) {
				ps.setString(1, params.get("EXPENSE_TYPE"));
			}
			if(params.containsKey("VENDOR")) {
				ps.setString(2, params.get("VENDOR"));
			}
			if(params.containsKey("EMPLOYEE")) {
				ps.setString(2, params.get("EMPLOYEE"));
			}
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ExpenseData data = new ExpenseData();
				data.setCode(rs.getString(1));
				data.setExpenseDate((java.util.Date)rs.getDate(2));
				data.setAmount(rs.getDouble(3));
				
				String vendorCode = rs.getString(4);
				DefaultVendorService vendorService = new DefaultVendorService();
				VendorData vendor = vendorService.findVendorByCode(vendorCode);
				data.setVendor(vendor);
				
				String paidByCode = rs.getString(5);
				InvestorDAO investorDAO = new DefaultInvestorDAO();
				InvestorData investor = investorDAO.findInvestorByCode(paidByCode);
				data.setPaidBy(investor);
				
				String categoryCode = rs.getString(6);
				ExpenseTypeDAO expTypeDAO = new DefaultExpenseTypeDAO();
				ExpenseTypeData expType = expTypeDAO.findExpenseTypeByCode(categoryCode);
				data.setExpenseType(expType);
				
				
				String paymentMode = rs.getString(7);
				PaymentModeDAO paymentModeDAO = new DefaultPaymentModeDAO();
				PaymentTypeData paymentData = paymentModeDAO.getPaymentModeByCode(paymentMode);
				data.setPaymentData(paymentData);
				
				data.setComments(rs.getString(8));
				//CREATED_BY,CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE
				data.setCreatedBy(employeeDAO.findEmployeeByCode(rs.getString(9)));
				data.setCreatedDate(DateUtil.convertDate(rs.getTimestamp(10)));
				
				data.setUpdatedBy(employeeDAO.findEmployeeByCode(rs.getString(11)));
				data.setLastModifedDate(DateUtil.convertDate(rs.getTimestamp(12)));
				expList.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return expList;
	}

	public List<ExpenseData> searchExpenseWithFilter(ReportSelectionData selection) {		
		int selectionType = selection.getSelectionType();
		if(selectionType == 0) {
			System.out.println("Report Type : - CONSOLIDATED");
			return findAllExpenses();
		}
		StringBuilder b = new StringBuilder();
		b.append("SELECT CODE, EXPENSE_DATE, AMOUNT, VENDOR_CODE, INVESTOR_CODE, CATEGORY_CODE, "
				+ "PAYMEMT_CODE, NOTES, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE FROM EXPENSE");
		
		List<FilterParameter> filters = selection.getFilters();
		if(filters != null && !filters.isEmpty()) {
			int counter = 0;
			for(FilterParameter param : filters) {
				String filterName = param.getFilterName();
				System.out.println("\nFilter Name : "+filterName);
				Map<String, Object> entries = param.getParameters();
				if(filterName.equalsIgnoreCase("DATE")) {
					final Timestamp fromDate = (Timestamp) entries.get("FROM_DATE");
					final Timestamp toDate = (Timestamp) entries.get("TO_DATE");
					if(counter == 0) {
						b.append(" WHERE EXPENSE_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' ");
						counter++;
					} else {
						b.append(" AND EXPENSE_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' ");
					}
				} else if(filterName.equalsIgnoreCase("CATEGORY")) {
					final ExpenseTypeData type = (ExpenseTypeData) entries.get("CATEGORY");
					if(counter == 0) {
						b.append(" WHERE CATEGORY_CODE = '"+type.getCode()+"' ");
						counter++;
					} else {
						b.append(" AND CATEGORY_CODE = '"+type.getCode()+"' ");
					}
				} else if(filterName.equalsIgnoreCase("PAYEE")) {
					final VendorData type = (VendorData) entries.get("PAYEE");
					if(counter == 0) {
						b.append(" WHERE VENDOR_CODE = '"+type.getCode()+"' ");
						counter++;
					} else {
						b.append(" AND VENDOR_CODE = '"+type.getCode()+"' ");
					}
				}				
			}
		}
		b.append(" ORDER BY EXPENSE_DATE DESC");	
		return buildExpenseResult(b.toString());
	}
	
	private List<ExpenseData> buildExpenseResult(final String sql) {
		List<ExpenseData> expList = new ArrayList<ExpenseData>();
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		EmployeeDAO employeeDAO = new DefaultEmployeeDAO();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ExpenseData data = new ExpenseData();
				data.setCode(rs.getString(1));
				data.setExpenseDate((java.util.Date)rs.getDate(2));
				data.setAmount(rs.getDouble(3));
				
				String vendorCode = rs.getString(4);
				DefaultVendorService vendorService = new DefaultVendorService();
				VendorData vendor = vendorService.findVendorByCode(vendorCode);
				data.setVendor(vendor);
				
				String paidByCode = rs.getString(5);
				InvestorDAO investorDAO = new DefaultInvestorDAO();
				InvestorData investor = investorDAO.findInvestorByCode(paidByCode);
				data.setPaidBy(investor);
				
				String categoryCode = rs.getString(6);
				ExpenseTypeDAO expTypeDAO = new DefaultExpenseTypeDAO();
				ExpenseTypeData expType = expTypeDAO.findExpenseTypeByCode(categoryCode);
				data.setExpenseType(expType);
				
				
				String paymentMode = rs.getString(7);
				PaymentModeDAO paymentModeDAO = new DefaultPaymentModeDAO();
				PaymentTypeData paymentData = paymentModeDAO.getPaymentModeByCode(paymentMode);
				data.setPaymentData(paymentData);
				
				data.setComments(rs.getString(8));
				//CREATED_BY,CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE
				data.setCreatedBy(employeeDAO.findEmployeeByCode(rs.getString(9)));
				data.setCreatedDate(DateUtil.convertDate(rs.getTimestamp(10)));
				
				data.setUpdatedBy(employeeDAO.findEmployeeByCode(rs.getString(11)));
				data.setLastModifedDate(DateUtil.convertDate(rs.getTimestamp(12)));
				expList.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return expList;
	}
}
