package com.saikrupa.app.dao;

import java.util.List;

import com.saikrupa.app.dto.InventoryData;
import com.saikrupa.app.dto.InventoryEntryData;
import com.saikrupa.app.dto.ProductData;
import com.saikrupa.app.dto.ReportSelectionData;

public interface ProductDAO {
	public List<ProductData> findAllProducts();
	public ProductData findProductByCode(String productCode);
	public InventoryData findInventoryLevelByProduct(ProductData productData);
	
	public List<InventoryEntryData> findInventoryHistoryForAllProduct();	
	public List<InventoryEntryData> searchInventoryWithFilter(ReportSelectionData selection);


}
