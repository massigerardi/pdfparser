/**
 * 
 */
package com.dart.archive.pdfparser;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import com.dart.archive.pdfparser.model.PdfDocument;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

/**
 * @author massi
 *
 */
public class ITextPdfDocumentReader implements PdfDocumentReader {

	int imageCounter = 0;
	
    /* (non-Javadoc)
	 * @see com.dart.archive.pdfparser.PdfImageReader#extractImages(java.lang.String, java.lang.String)
	 */
	public PdfDocument getPages(String filepath, String outputDir, boolean writeText, boolean writeImage) throws DocumentException {

    	File file = new File(filepath);
        
    	if (outputDir!=null) {
        	File output = new File(outputDir);
        	if (!output.exists()) {
        		output.mkdirs();
        	}
        	outputDir = output.getAbsolutePath();
    	}
    	
    	PdfDocument document;
		try {
			PdfReader reader = new PdfReader(filepath);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			document = new PdfDocument(file.getName(), file);
			String name = FilenameUtils.getBaseName(file.getName());
			
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PageRenderer  pageRenderer = new PageRenderer(i, outputDir, name, this, writeText, writeImage);
				parser.processContent(i, pageRenderer);
			    document.addPage(pageRenderer.getPage());
			}
			extractImages(file);
			return document;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

	public String getNumber() {
		
		StringBuilder builder = new StringBuilder();
		if (imageCounter<100)
			builder.append(0);
		if (imageCounter<10)
			builder.append(0);
		builder.append(imageCounter);
		imageCounter++;
		return builder.toString();
	}
	
	private void extractImages(File file) {
		String name = FilenameUtils.getBaseName(file.getName());
		
	}

}
