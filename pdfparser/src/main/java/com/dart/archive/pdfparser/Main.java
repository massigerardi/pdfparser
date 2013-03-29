/**
 * 
 */
package com.dart.archive.pdfparser;

import java.io.File;
import java.util.Collection;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
		File dest = null;
		String destName = null;
		if (args.length>1) {
			destName = args[1];
			dest = new File(destName);
			if (dest.exists() && !dest.isDirectory()) {
				throw new RuntimeException(dest.getAbsolutePath()+" must be a directory");
			}
		}
		
		File src = new File(srcName);
		if (src.exists() && src.isDirectory()) {
			if (dest==null) {
				dest = new File(srcName);
			}
			readFolder(src,dest);
		} else if (src.exists()) {
			if (dest==null) {
				dest = src.getParentFile();
			}
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
		File folder = new File(dest, FilenameUtils.getBaseName(src.getName()));
		System.out.println("parsing "+src.getAbsolutePath()+" in "+folder.getAbsolutePath());
		reader = new ITextPdfDocumentReader();
		try {
			PdfDocument document = reader.getPages(src, folder, true, true);
			System.out.println("parsed "+document.getPages().size()+" pages for document "+src.getName()+" in "+folder.getAbsolutePath());
		} catch (DocumentException e) {
			System.err.println("error while parsing "+src.getName());
			e.printStackTrace();
		}
	}

}
