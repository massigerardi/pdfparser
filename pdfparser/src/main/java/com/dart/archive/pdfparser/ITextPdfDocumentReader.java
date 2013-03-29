/**
 * 
 */
package com.dart.archive.pdfparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
				System.out.println("files: "+files);
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

			}
		}
		return images;
	}

	private List<File> extractImages(File file, String outputDir) {
		String name = FilenameUtils.getBaseName(file.getName());
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		try {
			extracyImages(file, outputDir, name);
			convertImages(outputDir);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		List<File> files = new ArrayList<File>(FileUtils.listFiles(new File(outputDir), new String[] {"jpg", "JPG", "JPEG", "jpeg"}, true));
		System.out.println("files: "+files);
		return files;
	}

	private void convertImages(String outputDir) throws InterruptedException, IOException {
		List<File> files = new ArrayList<File>(FileUtils.listFiles(new File(outputDir), new String[] {"ppm", "PPM"}, true));
		for (File file : files) {
			File dest = new File(file.getParent(), FilenameUtils.getBaseName(file.getName()) + ".jpg");
			System.out.print("running "+pnmtojpeg+" "+file.getAbsolutePath() + " > " + dest.getAbsolutePath());
			int result = new ProcessBuilder(pnmtojpeg, file.getAbsolutePath()+ " > " + dest.getAbsolutePath()).start().waitFor();
			System.out.println(" --> "+result+ " " + dest.exists());
		}
	}

	private void extracyImages(File file, String outputDir, String name) throws IOException, InterruptedException {
		File dest = new File(outputDir, name);
		System.out.println("running "+pdfimages+" -p "+file.getAbsolutePath() +" "+ dest.getAbsolutePath());
		int result = new ProcessBuilder(pdfimages, "-p", file.getAbsolutePath(), dest.getAbsolutePath()).start().waitFor();
		System.out.println(result);
	}

}
