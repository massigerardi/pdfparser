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
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import com.dart.archive.pdfparser.model.Image;
import com.dart.archive.pdfparser.model.Page;
import com.dart.archive.pdfparser.model.PdfDocument;

/**
 * @author massi
 *
 */
public class ITextPdfDocumentReaderTest {
	
	ImageHelper imageHelper;
	
	String pdfPath = "src/test/resources/learmidellarte.pdf";
	String destinationDir = "target/learmidellarte";
	ITextPdfDocumentReader reader;

	@Before
	public void setUp() {
		reader = new ITextPdfDocumentReader();
		imageHelper = Mockito.mock(ImageHelper.class);
		reader.setHelper(imageHelper);
	}
	
	@After
	public void clean() throws IOException {
		reader = null;
	}
	
	/**
	 * Test method for {@link com.gerardi.pdfparser.PdfImageReader#extractImages(java.lang.String)}.
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	public void testExtractImagesNoWrite() throws Exception {
		File file = new File(pdfPath);
		PdfDocument document = reader.getPages(file, file, false, false);
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
		assertEquals("number of images", 0, imagesCount);
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
		List<Image> images = new ArrayList<Image>();
		for (int i = 0; i < 10; i++) {
			images.add(new Image("image_"+i, "/targer/images/image_"+i));
		}
		when(imageHelper.filterPageImages(any(File.class), any(File.class), any(Integer.class))).thenReturn(images);
		PdfDocument document = reader.getPages(file, dir, false, true);
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
		}
		assertEquals("number of images", 640, imagesCount);
	}

}
