package com.dart.archive.pdfparser;

import java.io.File;

import com.dart.archive.pdfparser.model.PdfDocument;
import com.itextpdf.text.DocumentException;

public interface PdfDocumentReader {

	/**
	
	 * Parses a PDF and extracts all the images.
	
	 * @param src the source PDF
	
	 * @param outputDir the output folder
	
	 */

	public PdfDocument getPages(File src, File dest, boolean writeText, boolean writeImage) throws DocumentException;

}