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

	int imageCounter = 0;
	private String pdfimages = "pdfimages";
	
	private String pnmtojpeg = "pnmtojpeg";
	
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
			
			List<File> files = new ArrayList<File>();
			if (writeImage) {
				files = extractImages(file, outputDir);
			}
			
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				PageRenderer  pageRenderer = new PageRenderer(i, outputDir, name, writeText, writeImage);
				parser.processContent(i, pageRenderer);
			    Page page =  pageRenderer.getPage();
			    if (writeImage) {
			    	List<Image> images = getImages(files, page.getPageNumber(), name);
				    page.setImages(images);
			    }
				document.addPage(page);
			    
			}
			return document;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
	
	private List<Image> getImages(List<File> files, int pageNumber, String name) {
		List<Image> images = new ArrayList<Image>();
		for (File file : files) {
			if (file.getName().startsWith(name+"-"+StringUtils.leftPad(String.valueOf(pageNumber), 3, '0'))) {
				images.add(new Image(FilenameUtils.getBaseName(file.getName()), file.getAbsolutePath()));
			}
		}
		return images;
	}

	private List<File> extractImages(File file, String outputDir) {
		String name = FilenameUtils.getBaseName(file.getName());
		try {
			extracyImages(file, outputDir, name);
			convertImages(outputDir);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new ArrayList<File>(FileUtils.listFiles(new File(outputDir), new String[] {"jpg", "JPG", "JPEG", "jpeg"}, true));
	}

	private void convertImages(String outputDir) {
		List<File> files = new ArrayList<File>(FileUtils.listFiles(new File(outputDir), new String[] {"ppm", "PPM"}, true));
		for (File file : files) {
			File dest = new File(file.getParent(), FilenameUtils.getBaseName(file.getName()) + ".jpg");
			StringBuffer args = new StringBuffer();
			args.append(file.getAbsolutePath()).append(" > ").append(dest.getAbsolutePath());
			Process process;
			try {
				process = new ProcessBuilder(pnmtojpeg , args.toString()).start();
				int result = process.waitFor();
				if (result!=0) {
					System.out.println("failed converting "+file.getAbsolutePath());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void extracyImages(File file, String outputDir, String name) throws IOException, InterruptedException {
		File dest = new File(outputDir, name);
		StringBuffer args = new StringBuffer();
		args.append(" -p ").append(file.getAbsolutePath()).append(" ").append(dest.getAbsolutePath());
		System.out.println("running "+pdfimages+" "+args);
		Process process = new ProcessBuilder(pdfimages , args.toString()).start();
		int result = process.waitFor();
		if (result!=0) {
			throw new IOException("error while extracting images");
		}
		
	}

}
