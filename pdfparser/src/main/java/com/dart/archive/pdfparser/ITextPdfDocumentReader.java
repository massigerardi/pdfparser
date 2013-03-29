/**
 * 
 */
package com.dart.archive.pdfparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.dart.archive.pdfparser.model.Image;
import com.dart.archive.pdfparser.model.Page;
import com.dart.archive.pdfparser.model.PdfDocument;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

/**
 * @author massi
 *
 */
public class ITextPdfDocumentReader implements PdfDocumentReader {

	private ImageHelper helper = new ImageHelper();
    
	/* (non-Javadoc)
	 * @see com.dart.archive.pdfparser.PdfImageReader#extractImages(java.lang.String, java.lang.String)
	 */
	public PdfDocument getPages(File pdfFile, File dest, boolean writeText, boolean writeImage) throws DocumentException {

		String pdfName = FilenameUtils.getBaseName(pdfFile.getName());
    	
    	try {
			PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			PdfDocument document = new PdfDocument(pdfName, pdfFile);
			
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PageRenderer  pageRenderer = new PageRenderer(i, dest.getAbsolutePath(), pdfName, writeText, writeImage);
				parser.processContent(i, pageRenderer);
			    Page page =  pageRenderer.getPage();
			    if (writeImage) {
				    page.setImages(helper.filterPageImages(pdfFile, dest, page.getPageNumber()));
			    }
				document.addPage(page);
			    
			}
			return document;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

	/**
	 * for testing purposes
	 * @param helper
	 */
	public void setHelper(ImageHelper helper) {
		this.helper = helper;
	}

}
