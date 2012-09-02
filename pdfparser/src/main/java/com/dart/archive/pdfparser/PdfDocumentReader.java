package com.dart.archive.pdfparser;

import com.dart.archive.pdfparser.model.PdfDocument;
import com.itextpdf.text.DocumentException;

public interface PdfDocumentReader {

	/**
	
	 * Parses a PDF and extracts all the images.
	
	 * @param src the source PDF
	
	 * @param outputDir the output folder
	
	 */

	public PdfDocument getPages(String src) throws DocumentException;

}