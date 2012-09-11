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
	public PdfDocument getPages(String filepath) throws DocumentException {

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
			
			String name = StringUtils.replace(file.getName(), ".pdf", "");
			
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PageRenderer  pageRenderer = new PageRenderer(i, outputDir, name);
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
