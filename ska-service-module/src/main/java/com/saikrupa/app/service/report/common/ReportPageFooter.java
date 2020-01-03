package com.saikrupa.app.service.report.common;

import java.util.Locale;
import java.util.ResourceBundle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportPageFooter extends PdfPageEventHelper {

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		final ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources", Locale.getDefault());
		PdfContentByte cb = writer.getDirectContent();
		
		
	}

	public ReportPageFooter() {
		
	}
	

}
