/**
 * 
 */
package com.dart.archive.pdfparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dart.archive.pdfparser.model.Image;
import com.dart.archive.pdfparser.model.Page;
import com.dart.archive.pdfparser.model.PdfDocument;

/**
 * @author massi
 *
 */
public class ITextPdfDocumentReaderTest {

	String pdfPath = "src/test/resources/learmidellarte.pdf";
	String destinationDir = "target/learmidellarte";
	PdfDocumentReader reader;

	@Before
	public void setUp() {
		reader = new ITextPdfDocumentReader();
	}
	
	@After
	public void clean() throws IOException {
		reader = null;
//		FileUtils.deleteDirectory(new File(destinationDir));
	}
	
	/**
	 * Test method for {@link com.gerardi.pdfparser.PdfImageReader#extractImages(java.lang.String)}.
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	public void testExtractImagesNoWrite() throws Exception {
		File file = new File(pdfPath);
		PdfDocument document = reader.getPages(file.getAbsolutePath(), null, false, false);
		assertNotNull(document);
		assertFalse(document.getPages().isEmpty());
		assertEquals(64, document.getPages().size());
		int imagesCount = 0;
		for (Page page : document.getPages()) {
			if (page.isEmpty()) {
				assertTrue(page.getImages().isEmpty());
				assertNull(page.getText());
				continue;
			}
			imagesCount += page.getImagesNumber();
		}
		assertEquals("number of images", 58, imagesCount);
	}

	/**
	 * Test method for {@link com.gerardi.pdfparser.PdfImageReader#extractImages(java.lang.String)}.
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	public void testExtractImagesWrite() throws Exception {
		File file = new File(pdfPath);
		File dir = new File(destinationDir);
		PdfDocument document = reader.getPages(file.getAbsolutePath(),dir.getAbsolutePath(), true, true);
		assertNotNull(document);
		assertFalse(document.getPages().isEmpty());
		assertEquals(64, document.getPages().size());
		assertTrue(dir.exists());
		assertTrue(dir.isDirectory());
		int imagesCount = 0;
		for (Page page : document.getPages()) {
			if (page.isEmpty()) {
				assertTrue(page.getImages().isEmpty());
				assertNull(page.getText());
				continue;
			}
			imagesCount += page.getImagesNumber();
			if (page.hasText()) {
				String pageTextFile = String.format(PageRenderer.TEXT_NAME, page.getPageNumber());
				File textFile = new File(dir, pageTextFile);
				assertTrue("text file "+textFile.getAbsolutePath()+" for "+page.toString(), textFile.exists());
			}
			for (Image image : page.getImages()) {
				String path = image.getPath();
				System.out.println(page.getPageNumber() +" --> "+ path);
				assertNotNull(path);
				assertTrue("image file "+path+" for "+page.toString(), new File(path).exists());
			}
		}
		assertEquals("number of images", 58, imagesCount);
	}

}
