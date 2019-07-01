package com.saikrupa.app.util;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public DateUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static String convertToString(String pattern, Date date) {
		if(date == null) {
			return "N/A";
		}
		
		try {
			SimpleDateFormat f = new SimpleDateFormat(pattern);
			return f.format(date);
		} catch(Exception e) {
			System.out.println("Exception while formatting Date : "+e.getMessage());
		}
		return date.toString();
	}
	
	public static String convertToStandard(Date date) {
		if(date == null) {
			return "N/A";
		}		
		return convertToString("dd-MMM-yyyy HH:mm:ss", date);
	}

	public static String simpleDateText(Date date) {
		if(date == null) {
			return "N/A";
		}		
		return convertToString("dd MMM yyyy", date);
	}
	
	public static java.util.Date convertDate(java.sql.Date sqlDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(sqlDate.getTime());
		return cal.getTime();
	}
	
	public static java.util.Date convertDate(java.sql.Timestamp sqlDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(sqlDate.getTime());
		return cal.getTime();
	}
	
	public static Timestamp createCurrentTimeStamp() { 
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
	

}
