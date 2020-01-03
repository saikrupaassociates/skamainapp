package com.saikrupa.app.service.report.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.saikrupa.app.service.report.ReportService;
import com.saikrupa.app.service.report.common.ReportHeaderFooterPageEvent;
import com.saikrupa.app.service.report.common.ReportPageTitleEvent;

public abstract class AbstractReportService implements ReportService {

	private void addTitleHeader(PdfWriter writer) {

		ReportPageTitleEvent titleEvent = new ReportPageTitleEvent();
		writer.setPageEvent(titleEvent);

		ReportHeaderFooterPageEvent headerFooter = new ReportHeaderFooterPageEvent();
		writer.setPageEvent(headerFooter);
	}

	public Document createBlankDocument(final String reportFilePathName) {
		Document document = new Document(PageSize.A4);
		document.setMarginMirroringTopBottom(false);
		document.setMargins(20, 20, 40, 40);

		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(reportFilePathName));
			addTitleHeader(writer);
			document.open();
			addEmptyLine(new Paragraph(" "), 1);
			addMetaData(document);
			addTitlePage(document);
			createPageTableContent(document);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public abstract void createPageTableContent(Document document) throws DocumentException;

	public abstract void addMetaData(Document document);

	public void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(""));
		}
	}

	private void addTitlePage(Document document) throws DocumentException {
		Phrase reportTypeName = new Phrase(getPageTitle(),
				FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK));

		Paragraph preface = new Paragraph();
		preface.setSpacingAfter(10);
		preface.add(reportTypeName);
//		addEmptyLine(preface, 1);
//		Font catFont = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);
//		preface.add(new Paragraph(getPageTitle(), catFont));
//		addEmptyLine(preface, 1);
		document.add(preface);
	}

	public abstract String getPageTitle();

}
