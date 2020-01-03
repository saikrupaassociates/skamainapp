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

public class ReportPageTitleEvent extends PdfPageEventHelper {

	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		final ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources", Locale.getDefault());
		PdfContentByte cb = writer.getDirectContent();

		Phrase titleHeader = new Phrase(bundle.getString("report.header.title"),
				FontFactory.getFont(FontFactory.COURIER_BOLD, 16, BaseColor.BLACK));
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, titleHeader,
				(document.right() - document.left()) / 2 + document.leftMargin(), document.top() + 10, 0);
		Phrase subHeader1 = new Phrase(bundle.getString("report.header.subtitle"),
				FontFactory.getFont(FontFactory.COURIER_BOLD, 8, BaseColor.BLUE));
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, subHeader1,
				(document.right() - document.left()) / 2 + document.leftMargin(), document.top(), 0);

//		Phrase footer = new Phrase("Registration No : " + bundle.getString("report.footer.registration"),
//				FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK));
//
//		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
//				(document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);
	}

	public ReportPageTitleEvent() {
		// TODO Auto-generated constructor stub
	}

}
