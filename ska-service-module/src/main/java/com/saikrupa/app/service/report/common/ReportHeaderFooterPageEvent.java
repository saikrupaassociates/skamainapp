package com.saikrupa.app.service.report.common;

import java.util.Locale;
import java.util.ResourceBundle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportHeaderFooterPageEvent extends PdfPageEventHelper {

	public void onStartPage(PdfWriter writer, Document document) {
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Left"), 30, 800, 0);
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Right"), 550, 800, 0);
    }

    public void onEndPage(PdfWriter writer, Document document) {
    	final ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources", Locale.getDefault());
    	
    	
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Registration No : "+bundle.getString("report.footer.registration"), FontFactory.getFont(FontFactory.COURIER_BOLD, 8, BaseColor.BLACK)), 110, 28, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("GST No : " +bundle.getString("report.footer.gstn"), FontFactory.getFont(FontFactory.COURIER_BOLD, 8, BaseColor.BLACK)), 500, 28, 0);
    }

	

}
