package com.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.saikrupa.app.service.report.common.CellValueType;
import com.saikrupa.app.service.report.component.ReportTableDataCell;
import com.saikrupa.app.service.report.component.ReportTableHeaderCell;

public class ITextDummy {

	private Document document;

	public ITextDummy() {
		document = new Document(PageSize.A4);

		if (openPdf()) {
			addContent();
		}
		getDocument().close();
		System.out.println("Report Generation Completed");

	}

	private void addContent() {
		PdfPTable dataTable = new PdfPTable(7);
		dataTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		dataTable.getDefaultCell().setBorder(2);

		dataTable.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
		//dataTable.setWidthPercentage(100F);
		ReportTableHeaderCell header_OrderNo = new ReportTableHeaderCell("Order Number");
		ReportTableHeaderCell header_OrderDate = new ReportTableHeaderCell("Ordered On");
		ReportTableHeaderCell header_OrderBy = new ReportTableHeaderCell("Unit Price");
		ReportTableHeaderCell orderQuantity = new ReportTableHeaderCell("Quantity");
		ReportTableHeaderCell amount = new ReportTableHeaderCell("Order Total");
		ReportTableHeaderCell paymentStatus = new ReportTableHeaderCell("Payment");
		ReportTableHeaderCell deliveryStatus = new ReportTableHeaderCell("Delivery");
		
		dataTable.addCell(header_OrderNo);
		dataTable.addCell(header_OrderDate);
		dataTable.addCell(header_OrderBy);
		dataTable.addCell(orderQuantity);
		dataTable.addCell(amount);
		dataTable.addCell(paymentStatus);
		dataTable.addCell(deliveryStatus);
		
		ReportTableDataCell data_OrderNo = new ReportTableDataCell("haha", CellValueType.TEXT);
		for(int j = 0; j < 60; j++) {
			for(int i = 0; i < dataTable.getNumberOfColumns(); i++) {
				dataTable.addCell(data_OrderNo);
			}
		}
		
		
		PdfPTable consolidationTable = new PdfPTable(5);
		ReportTableHeaderCell totalOrder = new ReportTableHeaderCell("Total Order Amount");
		ReportTableHeaderCell receivedPayment = new ReportTableHeaderCell("Payment Received");
		ReportTableHeaderCell pendingPayment = new ReportTableHeaderCell("Payment Pending");
		
		ReportTableDataCell totalOrderValue = new ReportTableDataCell(new Double("150000"), CellValueType.AMOUNT);
		ReportTableDataCell paidValue = new ReportTableDataCell(new Double("45000"), CellValueType.AMOUNT);
		ReportTableDataCell pendingValue = new ReportTableDataCell(new Double("105000"), CellValueType.AMOUNT);
		
		consolidationTable.addCell(totalOrder);
		totalOrderValue.setColspan(4);
		consolidationTable.addCell(totalOrderValue);
		
		
		
		consolidationTable.addCell(receivedPayment);
		paidValue.setColspan(4);
		consolidationTable.addCell(paidValue);
		
		consolidationTable.addCell(pendingPayment);
		pendingValue.setColspan(4);
		consolidationTable.addCell(pendingValue);
		
		PdfPTable outerTable = new PdfPTable(1);
		outerTable.addCell(consolidationTable);
		outerTable.addCell(dataTable);
		
		try {
			outerTable.setWidthPercentage(100F);	
			outerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			getDocument().add(outerTable);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean openPdf() {
		boolean status = false;
		try {
			File pdfFile = new File("D:/gaga/test.pdf");
			if (pdfFile != null) {
				PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
				document.open();
				status = true;
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (DocumentException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return status;
	}

	public static void main(String[] args) {
		new ITextDummy();
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
