/**
 * 
 */
package com.dart.archive.pdfparser;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.dart.archive.pdfparser.model.PdfDocument;
import com.itextpdf.text.DocumentException;

/**
 * @author massi
 *
 */
public class Main {

	private static ITextPdfDocumentReader reader;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length<1) {
			return;
		}
		String srcName = args[0];
		String destName = srcName;
		if (args.length>1) {
			destName = args[1];
		}
		
		File dest = new File(destName);
		if (dest.exists() && !dest.isDirectory()) {
			throw new RuntimeException(dest.getAbsolutePath()+" must be a directory");
		}
		File src = new File(srcName);
		if (src.exists() && src.isDirectory()) {
			readFolder(src,dest);
		} else if (src.exists()) {
			readFile(src, dest);
		} else {
			throw new RuntimeException(src.getAbsolutePath()+" does not exist");
		}
	}

	private static void readFolder(File src, File dest) {
		Collection<File> files = FileUtils.listFiles(src, new String[] {"pdf"}, true);
		for (File file : files) {
			readFile(file, dest);
		}
	}

	private static void readFile(File src, File dest) {
		File folder = new File(dest, StringUtils.remove(src.getName(), ".pdf"));
		System.out.println("parsing "+src.getAbsolutePath()+" in "+folder.getAbsolutePath());
		reader = new ITextPdfDocumentReader(folder.getAbsolutePath());
		try {
			PdfDocument document = reader.getPages(src.getAbsolutePath());
			System.out.println("parsed "+document.getPages().size()+" pages for document "+src.getName()+" in "+folder.getAbsolutePath());
		} catch (DocumentException e) {
			System.err.println("error while parsing "+src.getName());
			e.printStackTrace();
		}
	}

}
