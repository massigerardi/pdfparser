/**
 * 
 */
package com.dart.archive.pdfparser;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.dart.archive.pdfparser.model.PdfDocument;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

/**
 * @author massi
 *
 */
public class ITextPdfDocumentReader implements PdfDocumentReader {

	String outputDir;
	
	
    public ITextPdfDocumentReader() {
    	this(null);
	}

    public ITextPdfDocumentReader(String outputDir) {
		this.outputDir = outputDir;
	}

    /* (non-Javadoc)
	 * @see com.dart.archive.pdfparser.PdfImageReader#extractImages(java.lang.String, java.lang.String)
	 */
	public PdfDocument getPages(String filename) throws DocumentException {

    	File file = new File(filename);
        
    	if (outputDir!=null) {
        	File output = new File(outputDir);
        	if (!output.exists()) {
        		output.mkdir();
        	}
        	outputDir = output.getAbsolutePath();
    	}
    	
    	PdfDocument document;
		try {
			PdfReader reader = new PdfReader(filename);

			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			
			document = new PdfDocument(file.getName(), file);
			
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PageRenderer  pageRenderer = new PageRenderer(i, outputDir);
			    parser.processContent(i, pageRenderer);
			    document.addPage(pageRenderer.getPage());
			}
			return document;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

}
