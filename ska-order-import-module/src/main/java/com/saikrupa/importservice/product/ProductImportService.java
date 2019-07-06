package com.saikrupa.importservice.product;

import java.util.List;

import com.saikrupa.orderimport.dto.ProductImportData;

public interface ProductImportService {
	public List<ProductImportData> getProductDataFromFile(final String filePath);
	public void importProductInventory(List<ProductImportData> products);
}
