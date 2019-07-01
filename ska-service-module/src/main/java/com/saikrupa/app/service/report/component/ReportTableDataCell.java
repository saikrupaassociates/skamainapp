package com.saikrupa.app.service.report.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.saikrupa.app.service.report.common.CellValueType;

public class ReportTableDataCell extends PdfPCell {

	public ReportTableDataCell(Object cellValue, CellValueType type) {
		Paragraph p = null;
		if (type == CellValueType.AMOUNT) {
			p = new Paragraph(String.format("%,.2f", cellValue),
					FontFactory.getFont(FontFactory.TIMES, 9, BaseColor.BLACK));

		} else if (type == CellValueType.DATE) {
			p = new Paragraph(getFormattedDate((Date) cellValue),
					FontFactory.getFont(FontFactory.TIMES, 9, BaseColor.BLACK));
		} else if (type == CellValueType.TEXT) {
			p = new Paragraph((String) cellValue, FontFactory.getFont(FontFactory.TIMES, 9, BaseColor.BLACK));
		} else if (type == CellValueType.QUANTITY) {
			p = new Paragraph(String.format("%,.0f", cellValue),
					FontFactory.getFont(FontFactory.TIMES, 9, BaseColor.BLACK));

		}
		p.setAlignment(Element.ALIGN_JUSTIFIED);
		setPhrase(p);
		setBackgroundColor(BaseColor.WHITE);
		setHorizontalAlignment(Element.ALIGN_LEFT);
	}
	
	public ReportTableDataCell(Object cellValue, CellValueType type, boolean boldFont) {
		this(cellValue, type);
		getPhrase().setFont(FontFactory.getFont(FontFactory.TIMES_BOLD, 9, BaseColor.BLACK));
	}

	public ReportTableDataCell() {
	}

	private String getFormattedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		return sdf.format(date);
	}

}
