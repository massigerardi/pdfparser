package com.dart.archive.pdfparser;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.dart.archive.pdfparser.model.Image;

@RunWith(MockitoJUnitRunner.class)
public class ImageHelperTest {
	
	String pdfPath = "src/test/resources/learmidellarte.pdf";
	String destinationDir = "target/learmidellarte";

	@Mock
	private CommandHelper commandHelper;
	@InjectMocks
	private ImageHelper imageHelper;

	@Before
	public void setUp() throws Exception {
	
	}

	@Test
	public void testFilterPageImages() {
		File file = new File(pdfPath);
		File dir = new File(destinationDir);
		List<File> files = new ArrayList<File>();
		for (int p = 1; p < 10; p++) {
			for (int i = 0; i < (p%3); i++) {
				files.add(new File("target/images/learmidellarte-"+StringUtils.leftPad(String.valueOf(p), 3, '0')+"-"+(i+1)) );
			}
		}
		when(commandHelper.extractImages(any(File.class), any(File.class))).thenReturn(10);
		when(commandHelper.convertPpmToJpg(any(File.class))).thenReturn(10);
		when(commandHelper.listJpegImages(any(File.class), anyBoolean())).thenReturn(files);
		for (int p = 1; p < 10; p++) {
			assertImages(file, dir, p);
		}
	}

	private void assertImages(File file, File dir, int pageNumber) {
		List<Image> images = imageHelper.filterPageImages(file, dir, pageNumber);
		int imagesNumber = (pageNumber%3);
		assertNotNull(images);
		System.out.println("page "+pageNumber+" "+images);
		assertEquals("page "+pageNumber, imagesNumber, images.size());		
	}
}
