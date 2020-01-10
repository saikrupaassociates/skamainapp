package com.saikrupa.app.util;

import java.text.DecimalFormat;

public class CurrencyUtil {

	public CurrencyUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static String format(Double value) {
		DecimalFormat IndianCurrencyFormat = new DecimalFormat("##,##,###.00");
		return IndianCurrencyFormat.format(value);
	}

}
