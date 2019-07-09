package com.saikrupa.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.saikrupa.app.dao.EmployeeDAO;
import com.saikrupa.app.dao.OrderDAO;
import com.saikrupa.app.dao.ProductDAO;
import com.saikrupa.app.db.PersistentManager;
import com.saikrupa.app.dto.FilterParameter;
import com.saikrupa.app.dto.InventoryData;
import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.dto.MachineData;
import com.saikrupa.app.dto.PriceRowData;
import com.saikrupa.app.dto.ProductData;
import com.saikrupa.app.dto.ReportSelectionData;
import com.saikrupa.app.service.MachineService;
import com.saikrupa.app.service.ProductPriceService;
import com.saikrupa.app.service.impl.DefaultMachineService;
import com.saikrupa.app.service.impl.DefaultProductPriceService;
import com.saikrupa.app.util.DateUtil;

public class DefaultProductDAO implements ProductDAO {

	public DefaultProductDAO() {
		// TODO Auto-generated constructor stub
	}

	public List<ProductData> findAllProducts() {
		List<ProductData> productList = new ArrayList<ProductData>();
		final String sql = "SELECT CODE, NAME, DESCRIPTION, SALEABLE FROM PRODUCT";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		ProductPriceService priceService = new DefaultProductPriceService();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ProductData data = new ProductData();
				data.setCode(rs.getString(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				data.setSaleable(rs.getInt(4) == 0 ? true : false);

				InventoryData inventory = findInventoryLevelByProduct(data);

				if (inventory == null) {
					inventory = new InventoryData();
					inventory.setTotalAvailableQuantity(Double.valueOf(0));
					inventory.setTotalReservedQuantity(Double.valueOf(0));
					inventory.setTotalDamagedQuantity(Double.valueOf(0));
					Calendar cal = Calendar.getInstance();
					inventory.setLastUpdatedDate(cal.getTime());
				}
				data.setInventory(inventory);
				PriceRowData priceRow = priceService.getBestMatchingUnitPrice(data.getCode());
				if (priceRow != null) {
					data.setPriceRow(priceRow);
				}
				productList.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productList;
	}

	public ProductData findProductByCode(String productCode) {
		final String sql = "SELECT CODE, NAME, DESCRIPTION, SALEABLE FROM PRODUCT WHERE CODE=?";

		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		ProductPriceService priceService = new DefaultProductPriceService();
		ProductData data = null;
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, productCode);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				data = new ProductData();
				data.setCode(rs.getString(1));
				data.setName(rs.getString(2));
				data.setDescription(rs.getString(3));
				data.setSaleable(rs.getInt(4) == 0 ? true : false);

				InventoryData inventory = findInventoryLevelByProduct(data);
				if (inventory == null) {
					inventory = new InventoryData();
					inventory.setTotalAvailableQuantity(Double.valueOf(0));
					inventory.setTotalReservedQuantity(Double.valueOf(0));
					inventory.setTotalDamagedQuantity(Double.valueOf(0));
					inventory.setLastUpdatedDate(new java.util.Date());
				}
				data.setInventory(inventory);

				PriceRowData priceRow = priceService.getBestMatchingUnitPrice(data.getCode());
				if (priceRow != null) {
					data.setPriceRow(priceRow);
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public InventoryData findInventoryLevelByProduct(ProductData productData) {
		final String sql = "SELECT I.CODE, I.TOTAL_AVAILABLE_QUANTITY, I.TOTAL_RESERVED_QUANTITY, I.TOTAL_DAMAGED_QUANTITY, I.LAST_UPDATED_DATE, I.LAST_MODIFIED_BY "
				+ "FROM INVENTORY I, PRODUCT P " + "WHERE P.CODE=I.PRODUCT_CODE AND P.CODE=? "
				+ "ORDER BY I.LAST_UPDATED_DATE DESC";
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		List<InventoryData> invList = new ArrayList<InventoryData>();
		EmployeeDAO empDAO = new DefaultEmployeeDAO();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, productData.getCode());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				InventoryData data = new InventoryData();
				data.setCode(rs.getInt(1));
				data.setTotalAvailableQuantity(rs.getDouble(2));
				data.setTotalReservedQuantity(rs.getDouble(3));
				data.setTotalDamagedQuantity(rs.getDouble(4));
				data.setLastUpdatedDate(DateUtil.convertDate(rs.getTimestamp(5)));
				data.setLastUpdatedBy(empDAO.findEmployeeByCode(rs.getString(6)));
				data.setProduct(productData);
				invList.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (invList.isEmpty() ? null : invList.get(0));
	}

	public List<InventoryEntryData> findInventoryHistoryForAllProduct() {
		final String sql = "SELECT CODE, CREATED_DATE, OPENING_BALANCE, QUANTITY_ADDED, QUANTITY_REDUCED, QUANTITY_DAMAGED, LABOUR_PAYMENT_STATUS,CLOSING_BALANCE, ORDER_REF, MACHINE_CODE  FROM INVENTORY_ENTRY WHERE INVENTORY_CODE=? ORDER BY CODE DESC";
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		List<InventoryEntryData> invList = new ArrayList<InventoryEntryData>();
		OrderDAO orderDao = new DefaultOrderDAO();
		for (ProductData productData : findAllProducts()) {
			try {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, productData.getCode());
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					InventoryEntryData data = new InventoryEntryData();
					data.setCode(rs.getInt(1));
					data.setCreatedDate(DateUtil.convertDate(rs.getTimestamp(2)));
					data.setOpeningBalance(rs.getDouble(3));
					data.setAddedQuantity(rs.getDouble(4));
					data.setReducedQuantity(rs.getDouble(5));
					data.setDamagedQuantity(rs.getDouble(6));
					data.setInventory(findInventoryLevelByProduct(productData));
					data.setLabourPaymentStatus(rs.getInt(7));
					data.setClosingBalance(rs.getDouble(8));
					data.setReferencedOrder(orderDao.findOrderByCode(rs.getInt(9)));
					MachineService machineService = new DefaultMachineService();
					data.setMachine(machineService.getMachineByCode(rs.getInt(10)));
					invList.add(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return invList;
	}

	public List<InventoryEntryData> searchInventoryWithFilter(ReportSelectionData selection) {		
		StringBuilder b = new StringBuilder();
		b.append("SELECT A.CODE, A.CREATED_DATE, A.OPENING_BALANCE,");
		b.append(" A.QUANTITY_ADDED, A.QUANTITY_REDUCED, A.QUANTITY_DAMAGED,");
		b.append(" A.LABOUR_PAYMENT_STATUS,A.CLOSING_BALANCE,");
		b.append(" A.ORDER_REF, A.MACHINE_CODE, C.CODE");
		b.append(" FROM INVENTORY_ENTRY A");
		b.append(" JOIN INVENTORY AS B ON A.INVENTORY_CODE=B.CODE");
		b.append(" JOIN PRODUCT AS C ON B.PRODUCT_CODE=C.CODE WHERE A.ORDER_REF IS NULL ");
		
		
		int selectionType = selection.getSelectionType();
		if (selectionType == 0) {
			System.out.println("Report Type : - CONSOLIDATED");
			b.append(" ORDER BY A.CODE DESC");
			return buildInventoryResult(b.toString());
		}
		

		List<FilterParameter> filters = selection.getFilters();
		if (filters != null && !filters.isEmpty()) {			
			for (FilterParameter param : filters) {
				String filterName = param.getFilterName();
				System.out.println("\nFilter Name : " + filterName);
				Map<String, Object> entries = param.getParameters();
				if (filterName.equalsIgnoreCase("DATE")) {
					final Timestamp fromDate = (Timestamp) entries.get("FROM_DATE");
					final Timestamp toDate = (Timestamp) entries.get("TO_DATE");
					b.append(" AND A.CREATED_DATE BETWEEN '" + fromDate + "' AND '" + toDate + "' ");
				} else if (filterName.equalsIgnoreCase("MACHINE")) {
					final MachineData type = (MachineData) entries.get("MACHINE");
					b.append(" AND A.MACHINE_CODE = '" + type.getCode() + "' ");
				} else if (filterName.equalsIgnoreCase("PRODUCT")) {
					final ProductData type = (ProductData) entries.get("PRODUCT");
					b.append(" AND C.CODE = '" + type.getCode() + "' ");
				}
			}
		}
		b.append(" ORDER BY A.CODE DESC");
		return buildInventoryResult(b.toString());
	}

	private List<InventoryEntryData> buildInventoryResult(String sql) {		
		PersistentManager manager = PersistentManager.getPersistentManager();
		Connection connection = manager.getConnection();
		List<InventoryEntryData> invList = new ArrayList<InventoryEntryData>();
		OrderDAO orderDao = new DefaultOrderDAO();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				InventoryEntryData data = new InventoryEntryData();
				data.setCode(rs.getInt(1));
				data.setCreatedDate(DateUtil.convertDate(rs.getTimestamp(2)));
				data.setOpeningBalance(rs.getDouble(3));
				data.setAddedQuantity(rs.getDouble(4));
				data.setReducedQuantity(rs.getDouble(5));
				data.setDamagedQuantity(rs.getDouble(6));				
				data.setLabourPaymentStatus(rs.getInt(7));
				data.setClosingBalance(rs.getDouble(8));
				data.setReferencedOrder(orderDao.findOrderByCode(rs.getInt(9)));
				MachineService machineService = new DefaultMachineService();
				data.setMachine(machineService.getMachineByCode(rs.getInt(10)));				
				data.setInventory(findInventoryLevelByProduct(findProductByCode(rs.getString(11))));
				invList.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return invList;
	}
}
