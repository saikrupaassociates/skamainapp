package com.saikrupa.app.service;

import java.util.Map;

public interface BalanceSheetService {
	public Map<String, Double> getOrderSummary(final String fromDate, final String toDate);
	public Map<String, Double> getExpenseSummary(final String fromDate, final String toDate);
	public Map<String, Double> getCollectionSummary(final String fromDate, final String toDate);
	public Map<String, Double> getProfitSummary(final String fromDate, final String toDate);
	public Map<String, Double> getDeliveryVehicleSummary(String fromDate, String toDate);
}
