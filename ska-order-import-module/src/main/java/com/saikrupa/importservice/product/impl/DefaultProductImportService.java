package com.saikrupa.importservice.product.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.saikrupa.app.dao.ApplicationUserDAO;
import com.saikrupa.app.dao.ProductDAO;
import com.saikrupa.app.dao.impl.DefaultApplicationUserDAO;
import com.saikrupa.app.dao.impl.DefaultProductDAO;
import com.saikrupa.app.dto.ApplicationUserData;
import com.saikrupa.app.dto.InventoryData;
import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.dto.MachineData;
import com.saikrupa.app.dto.ProductData;
import com.saikrupa.app.service.MachineService;
import com.saikrupa.app.service.ProductService;
import com.saikrupa.app.service.impl.DefaultMachineService;
import com.saikrupa.app.service.impl.DefaultProductService;
import com.saikrupa.app.session.ApplicationSession;
import com.saikrupa.importservice.product.ProductImportService;
import com.saikrupa.orderimport.dto.ProductImportData;

public class DefaultProductImportService implements ProductImportService {

	public List<ProductImportData> getProductDataFromFile(String filePath) {
		List<ProductImportData> products = new ArrayList<ProductImportData>();
		FileInputStream fileInputStream = null;
		
		try {
			fileInputStream = new FileInputStream(new File(filePath));
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(1);
			Iterator<Row> rowIterator = sheet.iterator();	
			
			while (rowIterator.hasNext()) {				
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();				
				if(row.getRowNum() == 0) {
					continue;
				}
				
				ProductImportData product = new ProductImportData();
				product.setRowIndex(row.getRowNum());
				
				
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();										
					switch(cell.getColumnIndex()) {
					case 0:
						product.setCode(String.valueOf(cell.getNumericCellValue()));
						break;
					case 1:		
						Date entryDate = cell.getDateCellValue();
						Calendar cal = Calendar.getInstance();
						cal.setTime(entryDate);
						cal.add(Calendar.HOUR_OF_DAY, 23);
						cal.add(Calendar.MINUTE, 59);
						cal.add(Calendar.SECOND, 59);
						System.out.println(cal.getTime());
						product.setEntryDate(cal.getTime());
						break;
					case 2:
						product.setQuantityAdded(cell.getNumericCellValue());
						break;
					case 3:
						product.setQuantityRejected(cell.getNumericCellValue());
						break;
					case 4:
						product.setMachineCode(Double.valueOf(cell.getNumericCellValue()).intValue());
						break;
					
					}					
				}				
				products.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Products Collected : "+products.size());
		return products;
	}

	public List<InventoryEntryData> importProductInventory(List<ProductImportData> products) {
		ProductDAO productDao = new DefaultProductDAO();
		MachineService machineService = new DefaultMachineService();
		ProductService productService = new DefaultProductService();
		
		List<InventoryEntryData> inventoryEntryList = new ArrayList<InventoryEntryData>();
		for(ProductImportData importProduct : products) {
			ProductData product = productDao.findProductByCode(importProduct.getCode());
			if(product == null) {
				System.out.println("WARNING - No Product Available maching with code ["+importProduct.getCode()+"]");
				continue;
			}
			MachineData machine = machineService.getMachineByCode(importProduct.getMachineCode());
			if(machine == null) {
				System.out.println("WARNING - No machine Available maching with code ["+importProduct.getMachineCode()+"]");
				continue;
			}	
			
			InventoryData inventory = productDao.findInventoryLevelByProduct(product);
			
			InventoryEntryData entryData = new InventoryEntryData();			
			entryData.setAddedQuantity(importProduct.getQuantityAdded());
			entryData.setReservedQuantity(Double.valueOf("0"));
			entryData.setDamagedQuantity(importProduct.getQuantityRejected());
			entryData.setCreatedDate(importProduct.getEntryDate());			
			entryData.setMachine(machine);
			entryData.setInventory(inventory);			
			
			
			ApplicationUserDAO userDao = new DefaultApplicationUserDAO();
			ApplicationUserData userData = userDao.findUserByCode("admin");
			ApplicationSession session = ApplicationSession.getSession();
			session.setCurrentUser(userData);			
			productService.updateInventory(entryData);
			System.out.println("INFO - Inventory Added ["+importProduct.getQuantityAdded()+"] for Product ["+product.getCode()+"], ["+product.getName()+"]");
			inventoryEntryList.add(entryData);			
		}
		return inventoryEntryList;
		
	}

}
