package com.saikrupa.importservice.product;

import java.util.List;

import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.orderimport.dto.ProductImportData;

public interface ProductImportService {
	public List<ProductImportData> getProductDataFromFile(final String filePath);
	public List<InventoryEntryData> importProductInventory(List<ProductImportData> products);
}
