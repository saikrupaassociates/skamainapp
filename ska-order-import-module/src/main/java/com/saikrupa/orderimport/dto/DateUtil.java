package com.saikrupa.orderimport.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public DateUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getFormattedDate(Date date) {
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		return f.format(date);
	}

}
